package deeplearning;

import neural.network.impl.MLP;
import neural.network.interfaces.NeuralNetworkIF;
import neural.network.util.Weight;

import com.syvys.jaRBM.Layers.Layer;
import com.syvys.jaRBM.Layers.LogisticLayer;
import common.MatrixHandler;

public class DeepLearning {

    private int numberAttribute;
    private int numberHiddenLayers;
    private int numberHidden;
    private int numberOutput;
    private int numberEpochsCRBM;

    private double CRBM_learning_rate_weights;
    private double CRBM_theta_low;
    private double CRBM_theta_high;
    private double CRBM_sigma;
    private double CRBM_learning_rate_aj;

    public DeepLearning(int numberAttribute, int numberHiddenLayers, int numberHidden, int numberOutput, int numberEpochsCRBM, ParameterTrainingCRBM parameterTrainingCRBM) {
        this.numberAttribute = numberAttribute;
        this.numberHiddenLayers = numberHiddenLayers;
        this.numberHidden = numberHidden;
        this.numberOutput = numberOutput;
        this.numberEpochsCRBM = numberEpochsCRBM;

        this.CRBM_learning_rate_weights = parameterTrainingCRBM.getLearning_rate_weights();
        this.CRBM_learning_rate_aj = parameterTrainingCRBM.getLearning_rate_aj();
        this.CRBM_theta_low = parameterTrainingCRBM.getTheta_low();
        this.CRBM_theta_high = parameterTrainingCRBM.getTheta_high();
        this.CRBM_sigma = parameterTrainingCRBM.getSigma();
    }

    /**
     *
     * @param data the input examples from the MLP training set.
     * @return weight_matrices_list an array of objects of the Weight type.
     */
    public Weight[] runDeepLearning(double[][] data) {
        Weight[] weight_matrices_list = new Weight[numberHiddenLayers + 1];
        double[][] input = data;

        // First weight matrix: weights[numberAttribute][numHidden]
        CRBM crbm = new CRBM(numberAttribute, numberHidden, CRBM_learning_rate_weights, CRBM_learning_rate_aj, CRBM_theta_low, CRBM_theta_high, CRBM_sigma);
        System.out.println("\t- First weight matrix -");
        crbm.train(input, numberEpochsCRBM);
        double[][] weightsCRBM = crbm.getWeights();

        Weight new_weight = new Weight(numberAttribute, numberHidden);
        new_weight.setWeights(weightsCRBM);
        weight_matrices_list[0] = new_weight;

        // Weight matrices between hidden layers
        for (int layer_weights_count = 1; layer_weights_count < weight_matrices_list.length - 1; layer_weights_count++) {
            input = getNextInput(input, new_weight, numberHidden);
            crbm = new CRBM(numberHidden, numberHidden, CRBM_learning_rate_weights, CRBM_learning_rate_aj, CRBM_theta_low, CRBM_theta_high, CRBM_sigma);
            System.out.println("\t- Hidden weight matrix " + layer_weights_count + " -");
            crbm.train(input, numberEpochsCRBM);
            weightsCRBM = crbm.getWeights();

            new_weight = new Weight(numberHidden, numberHidden);
            new_weight.setWeights(weightsCRBM);
            weight_matrices_list[layer_weights_count] = new_weight;
        }

        input = getNextInput(input, new_weight, numberOutput);
        // Last weight matrix: weights[numHidden][numberOutput]
        crbm = new CRBM(numberHidden, numberOutput, CRBM_learning_rate_weights, CRBM_learning_rate_aj, CRBM_theta_low, CRBM_theta_high, CRBM_sigma);
        System.out.println("\t- Last weight matrix -");
        crbm.train(input, numberEpochsCRBM);
        weightsCRBM = crbm.getWeights();

        new_weight = new Weight(numberHidden, numberOutput);
        new_weight.setWeights(weightsCRBM);
        weight_matrices_list[weight_matrices_list.length - 1] = new_weight;

        return weight_matrices_list;
    }

    private double[][] getNextInput(double[][] data, Weight new_weight, int numberOutput) {
        double[][] input;
        NeuralNetworkIF mlp = new MLP();
        Layer[] net = new Layer[1];
        net[0] = new LogisticLayer(numberOutput);
        input = new double[MatrixHandler.rows(data)][numberOutput];

        Weight[] weigthsArray = new Weight[1];
        weigthsArray[0] = new_weight;
        for (int index = 0; index < MatrixHandler.rows(input); index++) {
            MatrixHandler.setRow(
                    input,
                    mlp.run(net, weigthsArray,
                    MatrixHandler.getRow(data, index)), index);
        }
        return input;
    }
}
