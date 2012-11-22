
package deeplearning;

import java.util.Random;

public class CRBM {
    int numberVisible;
    int numberHidden;
    double learning_rate_weights;
    double[][] weights;

    double theta_low;
    double theta_high;
    double sigma;
    double learning_rate_aj;

    double[][] aj_visible;
    double[][] aj_hidden;

    public CRBM(int numberVisible, int numberHidden, double learning_rate_weights, double learning_rate_aj, double theta_low, double theta_high, double sigma) {
        this.numberVisible = numberVisible;
        this.numberHidden = numberHidden;
        this.learning_rate_weights = learning_rate_weights;
        this.learning_rate_aj = learning_rate_aj;
        this.theta_low = theta_low;
        this.theta_high = theta_high;
        this.sigma = sigma;
        this.aj_visible = new double[1][numberVisible];
        this.aj_hidden = new double[1][numberHidden];

        // Randomly inicializes the weight matrix, whose size is (numberVisible x numberHidden).
        weights = randomMatrix(numberVisible, numberHidden);
    }

    private double[][] randomMatrix(int height, int width) {
        double[][] new_matrix = new double[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                new_matrix[i][j] = (2 * new Random().nextDouble()) - 1;
        return new_matrix;
    }

    private double[][] dot(double[][] matrix1, double[][] matrix2) {
        return multiplyMatrices(matrix1, matrix2);
    }

    private double[][] ones(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = 1.0;
            }
        }
        return matrix;
    }

    private double[][] calculateActivations(double[][] training_set) {
        double[][] weighted_sum = this.multiplyMatrices(training_set, weights);

        // adds each weighted sum to (sigma * N(0,1))
        for (int i = 0; i < weighted_sum.length; i++) {
            for (int j = 0; j < weighted_sum[0].length; j++) {
                weighted_sum[i][j] += sigma * Math.random();
            }
        }

        return weighted_sum;
    }

    private double logisticFunction(double x) {
        double exp = Math.exp(x);
        return (1.0 / (1.0 + (1.0 / exp)));
    }

    private double[][] calculateSigmoid(double[][] activations, double[][] aj_matrix) {
        double[][] sigmoid_matrix = new double[activations.length][activations[0].length];

        // puts each activation value as a parameter to the logistc function
        for (int i = 0; i < activations.length; i++) {
            for (int j = 0; j < activations[0].length; j++) {
                sigmoid_matrix[i][j] = theta_low + (theta_high - theta_low) * logisticFunction(aj_matrix[i][j] * activations[i][j]);
            }
        }

        return sigmoid_matrix;
    }

    private void normalizeWeights() {
        // Each column of the weight matrix ir related to a hidden neuron,
        // therefore each column must have its own weight norm.
        for (int column = 0; column < weights[0].length; column++) {
            // Calculates this column's weight norm.
            double weight_norm = 0.0;
            for (int row = 0; row < weights.length; row++) {
                weight_norm += Math.pow(weights[row][column], 2);
            }
            weight_norm = Math.sqrt(weight_norm);

            // Updates every weight in this column dividing it by the
            // norm calculated before.
            for (int row = 0; row < weights.length; row++) {
                weights[row][column] = weights[row][column] / weight_norm;
            }
        }
    }

    private double[][] hidden_layer;

    public double[][] train(double[][] training_set, int number_epochs) {
        // Returns: the reconstruction of the visible layer
        double[][] neg_visible_sj = null;

        int number_training_examples = training_set.length;

        // Local matrices of 'noise controls' (aj). Every neuron has its own noise control.
        // They all inicialize at 1.
        //   - Visible neurons' aj values:
        double[][] _aj_visible = ones(new double[number_training_examples][numberVisible]);
        //   - Hidden neurons' aj values:
        double[][] _aj_hidden = ones(new double[number_training_examples][numberHidden]);

        // Hidden layer.
        double[][] neg_hidden_sj = new double[number_training_examples][numberHidden];

        for (int epoch = 1; epoch <= number_epochs; epoch++) {
            // Clamp to the data and sample from the hidden units.
            // (This is the "positive CD phase", aka the reality phase.)
            double[][] pos_hidden_activations = calculateActivations(training_set);
            double[][] pos_hidden_sj = calculateSigmoid(pos_hidden_activations, _aj_hidden);
            // Corresponds to <si*sj>, used to update the weights.
            double[][] pos_associations = dot(getTranspose(training_set), pos_hidden_sj);

            // Reconstruct the visible units and sample again from the hidden units.
            // (This is the "negative CD phase", aka the daydreaming phase.)
            double[][] neg_visible_activations = dot(pos_hidden_sj, getTranspose(weights));
            neg_visible_sj = calculateSigmoid(neg_visible_activations, _aj_visible);
            double[][] neg_hidden_activations = dot(neg_visible_sj, weights);
            neg_hidden_sj = calculateSigmoid(neg_hidden_activations, _aj_hidden);
            // Corresponds to <s^i*s^j>, used to update the weights.
            double[][] neg_associations = dot(getTranspose(neg_visible_sj), neg_hidden_sj);

            // Updates the weights.
            for (int i = 0; i < weights.length; i++) {
                for (int j = 0; j < weights[0].length; j++) {
                    weights[i][j] += learning_rate_weights * ((pos_associations[i][j] - neg_associations[i][j]) / (double) number_training_examples);
                }
            }
            // Normalizes the updated weights.
            normalizeWeights();

            // Updates each neuron's 'noise control' (aj).
            for (int current_example = 0; current_example < number_training_examples; current_example++) {
                // For each example in the traning set,

                //   - updates each visible neuron's aj.
                for (int visible_neuron_iterator = 0; visible_neuron_iterator < _aj_visible[0].length; visible_neuron_iterator++) {
                    _aj_visible[current_example][visible_neuron_iterator] += (learning_rate_aj / Math.pow(_aj_visible[current_example][visible_neuron_iterator],
                            2)) * (((Math.pow(training_set[current_example][visible_neuron_iterator],
                            2)) - (Math.pow(neg_visible_sj[current_example][visible_neuron_iterator],
                            2))) / (double) number_training_examples);
                }
                //   - updates each hidden neuron's aj.
                for (int hidden_neuron_iterator = 0; hidden_neuron_iterator < _aj_hidden[0].length; hidden_neuron_iterator++) {
                    _aj_hidden[current_example][hidden_neuron_iterator] += (learning_rate_aj / Math.pow(_aj_hidden[current_example][hidden_neuron_iterator],
                            2)) * (((Math.pow(pos_hidden_sj[current_example][hidden_neuron_iterator],
                            2)) - (Math.pow(neg_hidden_sj[current_example][hidden_neuron_iterator],
                            2))) / (double) number_training_examples);
                }
            }

            // Calculates the mean square error
            double error = CRBM.meanSquareError(training_set, neg_visible_sj);
            System.out.println("\t\tEpoch: " + epoch + "\n\t\t\tMSE = " + error);

            // Updates the global variables of the matrices of 'noise controls'.
            this.aj_visible = _aj_visible;
            this.aj_hidden = _aj_hidden;
        }

        // Global hidden layer is now the hidden layer states after the negative phase.
        this.hidden_layer = neg_hidden_sj;

        return neg_visible_sj;
    }

    public double[][] reconstruct(double[][] data) {
        double[][] hidden_states = run_visible(data);
        return run_hidden(hidden_states);
    }

    private double[][] run_visible(double[][] data) {
        double[][] ativacoes = calculateActivations(data);
        return this.calculateSigmoid(ativacoes, this.aj_visible);
    }

    private double[][] run_hidden(double[][] data) {
        double[][] activations = calculateActivations(data);
        return this.calculateSigmoid(activations, this.aj_hidden);
    }

    public static double meanSquareError(double[][] training_set, double[][] reconstruction) {
        double error = 0;

        for (int i = 0; i < training_set.length; i++) {
            for (int j = 0; j < reconstruction[0].length; j++) {
                error += Math.pow((training_set[i][j] - reconstruction[i][j]), 2);
            }
        }

        // Calculates the mean value.
        double denominator = training_set.length;
        denominator *= training_set[0].length;
        error /= denominator;

        return error;
    }

    public double[][] getWeights() {
        return this.weights;
    }

    private double[][] multiplyMatrices(double a[][], double b[][]) {
        int aRows = a.length, aColumns = a[0].length, bRows = b.length, bColumns = b[0].length;

        if (aColumns != bRows) {
//            //TESTE
//        System.out.println("a[" + a.length + "][" + a[0].length + "]");
//        System.out.println("b[" + b.length + "][" + b[0].length + "]");

            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
        }

        double[][] resultant = new double[aRows][bColumns];

//        //TESTE
//        System.out.println("a[" + a.length + "][" + a[0].length + "]");
//        System.out.println("b[" + b.length + "][" + b[0].length + "]");
//        System.out.println("resultant[" + resultant.length + "][" + resultant[0].length + "]");

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    try {
                        resultant[i][j] += a[i][k] * b[k][j];
                    }
                    catch(Exception e) {
//                        //TESTE
//                        System.out.println("\ti=" + i);
//                        System.out.println("\t\tj=" + j);
//                        System.out.println("\t\t\tk=" + k);
                    }
                }
            }
        }

        return resultant;
    }

    public static double[][] getTranspose(double[][] someMatrix) {
        double[][] resultMatrix = new double[someMatrix[0].length][];
        for (int i = 0; i < someMatrix[0].length; i++) {
            resultMatrix[i] = getCol(someMatrix, i);
        }
        return resultMatrix;
    }

    public static double[] getCol(double[][] someMatrix, int i) {
        double[] col_i = new double[someMatrix.length];
        for (int j = 0; j < someMatrix.length; j++) {
            col_i[j] = someMatrix[j][i];
        }
        return col_i;
    }

    public double[][] getHiddenLayer() {
        return this.hidden_layer;
    }

    public static void main(String[] args) {
        int numberVisible = 28*28;

        // Parameters with the same values used in "Continuous restricted Boltzmann
        // machine with an implementable training algorithm", by H. Chen e A.F. Murray.
        CRBM crbm = new CRBM(numberVisible, 4, 1.5, 1.0, -1.0, 1.0, 0.2);

        double[][] training_set = crbm.randomMatrix(30000, numberVisible);
        double[][] recons = crbm.train(training_set, 4000);

        double error = meanSquareError(training_set, recons);
        System.out.println("Mean Square Error: " + error);
    }
}
