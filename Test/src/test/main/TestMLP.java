package test.main;

import java.io.IOException;

import neural.network.enums.Task;
import neural.network.impl.MLP;
import neural.network.impl.ParameterTraining;
import neural.network.interfaces.NeuralNetworkIF;
import neural.network.util.Weight;

import com.syvys.jaRBM.Layers.Layer;
import com.syvys.jaRBM.Layers.LogisticLayer;
import com.syvys.jaRBM.Layers.SoftmaxLayer;
import common.MatrixHandler;

public class TestMLP {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// test();
		testAriane();
	}

	public static void test() throws IOException {
		NeuralNetworkIF neuralNetwork = new MLP();
		Weight[] update = new Weight[4];
		double[][] weights = initializeWeights(4, 2);
		update[0] = new Weight(weights);
		weights = initializeWeights(2, 2);
		update[1] = new Weight(weights);
		weights = initializeWeights(2, 3);
		update[2] = new Weight(weights);
		weights = initializeWeights(3, 2);
		update[3] = new Weight(weights);
		Layer[] net = new Layer[4];
		net[0] = new LogisticLayer(2);
		net[0].setLearningRate(0.1);
		net[1] = new LogisticLayer(2);
		net[1].setLearningRate(0.1);
		net[2] = new LogisticLayer(3);
		net[2].setLearningRate(0.1);
		net[3] = new SoftmaxLayer(2);
		net[3].setLearningRate(0.1);

		double[][] sample = { { 4, 3, 2, 1 } };
		double[][] label = { { 1, 0 } };

		long numberEpochs = 2;
		double maxError = 0;
		double learningRateDecrease = 0.99;
		double minLearningRate = 0.000000001;
		boolean initializeRandom = false;
		Task task = Task.CLASSIFICATION;
		int intervalEpochPercentage = 2;

		ParameterTraining parameterTraining = new ParameterTraining();
		parameterTraining.setNumberEpochs(numberEpochs);
		parameterTraining.setMaxError(maxError);
		parameterTraining.setTask(task.getValue());
		parameterTraining.setLearningRateDecrease(learningRateDecrease);
		parameterTraining.setMinLearningRate(minLearningRate);
		parameterTraining.setWeightsInitializationRandom(initializeRandom);
		parameterTraining.setIntervalEpochPercentage(intervalEpochPercentage);
		parameterTraining.setUpdateBatch(false);
		parameterTraining.setNormalizeWeights(false);
		parameterTraining.setValidation(false);

		update = neuralNetwork.train(net, update, sample, label,
				parameterTraining, null, null);

	}

	private static double[][] initializeWeights(int rows, int cols) {
		double[][] weights = new double[rows][cols];
		double init = 0.9;
		for (int indexRows = 0; indexRows < MatrixHandler.rows(weights); indexRows++) {
			for (int indexCols = 0; indexCols < MatrixHandler.cols(weights); indexCols++) {
				weights[indexRows][indexCols] = init;
				init = init - 0.1;
			}
		}
		return weights;
	}

	public static void testAriane() throws IOException {
		NeuralNetworkIF neuralNetwork = new MLP();
		Weight[] update = new Weight[2];
		double[][] weights = new double[3][2];
		weights[0][0] = 0.2;
		weights[0][1] = 0.7;
		weights[1][0] = -0.1;
		weights[1][1] = -1.2;
		weights[2][0] = 0.4;
		weights[2][1] = 1.2;
		System.out.println(">>>Matrix 1<<<");
		MatrixHandler.printMatrix(weights);
		update[0] = new Weight(weights);
		weights = new double[2][2];
		weights[0][0] = 1.1;
		weights[0][1] = 3.1;
		weights[1][0] = 0.1;
		weights[1][1] = 1.17;
		System.out.println(">>>Matrix 2<<<");
		update[1] = new Weight(weights);
		Layer[] net = new Layer[2];
		net[0] = new LogisticLayer(2);
		net[0].setLearningRate(0.1);
		net[1] = new LogisticLayer(2);
		net[1].setLearningRate(0.1);

		double[][] sample = { { 10, 30, 20 } };
		double[][] label = { { 1, 0 } };

		long numberEpochs = 1;
		double maxError = 0;
		double learningRateDecrease = 0.99;
		double minLearningRate = 0.000000001;
		boolean initializeRandom = false;
		Task task = Task.CLASSIFICATION;
		int intervalEpochPercentage = 2;

		ParameterTraining parameterTraining = new ParameterTraining();
		parameterTraining.setNumberEpochs(numberEpochs);
		parameterTraining.setMaxError(maxError);
		parameterTraining.setTask(task.getValue());
		parameterTraining.setLearningRateDecrease(learningRateDecrease);
		parameterTraining.setMinLearningRate(minLearningRate);
		parameterTraining.setWeightsInitializationRandom(initializeRandom);
		parameterTraining.setIntervalEpochPercentage(intervalEpochPercentage);
		parameterTraining.setUpdateBatch(false);
		parameterTraining.setNormalizeWeights(false);
		parameterTraining.setValidation(false);

		update = neuralNetwork.train(net, update, sample, label,
				parameterTraining, null, null);

	}

}
