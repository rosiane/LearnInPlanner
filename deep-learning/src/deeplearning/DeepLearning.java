package deeplearning;

import neural.network.impl.MLP;
import neural.network.interfaces.NeuralNetworkIF;
import neural.network.util.Weight;

import com.syvys.jaRBM.Layers.Layer;
import com.syvys.jaRBM.Layers.LogisticLayer;
import com.syvys.jaRBM.Math.Matrix;
import common.MatrixHandler;

public class DeepLearning {

	private int numberAttribute;
	private int numberHiddenLayers;
	private int numberHidden;
	private int numberOutput;
	private int numberEpochsCRBM;

	public static void main(String[] args) {
		double[][] data = { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 } };
		DeepLearning dp = new DeepLearning(data[0].length, 3, 5, 2, 100);
		Weight[] weight_matrices = dp.runDeepLearning(data);
		dp.printMatrices(weight_matrices);
	}

	public DeepLearning(int numberAttribute, int numberHiddenLayers,
			int numberHidden, int numberOutput, int numberEpochsCRBM) {
		this.numberAttribute = numberAttribute;
		this.numberHiddenLayers = numberHiddenLayers;
		this.numberHidden = numberHidden;
		this.numberOutput = numberOutput;
		this.numberEpochsCRBM = numberEpochsCRBM;
	}

	/**
	 * 
	 * @param data
	 *            the input examples from the MLP training set.
	 * @return weight_matrices_list an array of objects of the Weight type.
	 */
	public Weight[] runDeepLearning(double[][] data) {
		Weight[] weight_matrices_list = new Weight[numberHiddenLayers + 1];
		double[][] input = data;

		// First weight matrix: weights[numberAttribute][numHidden]
		CRBM crbm = new CRBM(numberAttribute, numberHidden, 1.5, 1.0, -1.0,
				1.0, 0.2, null);
		System.out.println("\t- First weight matrix -");
		crbm.treinar(input, numberEpochsCRBM);
		double[][] weights_CRBM = crbm.getWeights();
		weights_CRBM = removeBiases(weights_CRBM);

		Weight new_weight = new Weight(numberAttribute, numberHidden);
		new_weight.setWeights(weights_CRBM);
		weight_matrices_list[0] = new_weight;

		// Input for next layer: hidden layer of the CRBM
		// double[][] nextInput = crbm.getHiddenLayer();
		// mlp.run(net, weights, data);

		// Weight matrices between hidden layers
		for (int layer_weights_count = 1; layer_weights_count < weight_matrices_list.length - 1; layer_weights_count++) {
			input = getNextInput(input, new_weight, numberHidden);
			crbm = new CRBM(numberHidden, numberHidden, 1.5, 1.0, -1.0, 1.0,
					0.2, null);
			System.out.println("\t- Hidden weight matrix "
					+ layer_weights_count + " -");
			crbm.treinar(input, numberEpochsCRBM);
			weights_CRBM = crbm.getWeights();
			weights_CRBM = removeBiases(weights_CRBM);

			new_weight = new Weight(numberHidden, numberHidden);
			new_weight.setWeights(weights_CRBM);
			weight_matrices_list[layer_weights_count] = new_weight;

			// Input for next layer: hidden layer of the CRBM
			// if (layer_weights_count < weight_matrices_list.length - 2) {
			// input = getNextInput(input, new_weight, numberHidden);
			// }
		}

		input = getNextInput(input, new_weight, numberOutput);
		// Last weight matrix: weights[numHidden][numberOutput]
		crbm = new CRBM(numberHidden, numberOutput, 1.5, 1.0, -1.0, 1.0, 0.2,
				null);
		System.out.println("\t- Last weight matrix -");
		crbm.treinar(input, numberEpochsCRBM);
		weights_CRBM = crbm.getWeights();
		weights_CRBM = removeBiases(weights_CRBM);

		new_weight = new Weight(numberHidden, numberOutput);
		new_weight.setWeights(weights_CRBM);
		weight_matrices_list[weight_matrices_list.length - 1] = new_weight;

		return weight_matrices_list;
	}

	private double[][] getNextInput(double[][] data, Weight new_weight,
			int numberOutput) {
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

	/**
	 * Removes the first row and the first column from weights (containing the
	 * biases).
	 * 
	 * @param weights
	 *            the original weight matrix
	 * @return a new matrix without the first row and column
	 */
	private double[][] removeBiases(double[][] weights) {
		double[][] new_matrix = new double[weights.length - 1][weights[0].length - 1];

		for (int i = 1; i < weights.length; i++)
			for (int j = 1; j < weights[0].length; j++)
				new_matrix[i - 1][j - 1] = weights[i][j];

		return new_matrix;
	}

	private void printMatrices(Weight[] weight_matrices) {
		for (int i = 0; i < weight_matrices.length; i++) {
			System.out.println("Weights[" + i + "]:");
			Weight weight = weight_matrices[i];
			double[][] weight_matrix = weight.getWeights();
			for (int j = 0; j < weight_matrix.length; j++) {
				System.out.print("\t{ ");
				for (int k = 0; k < weight_matrix[0].length; k++) {
					System.out.print(weight_matrix[j][k] + " ");
				}
				System.out.println("}");
			}
		}
	}
}
