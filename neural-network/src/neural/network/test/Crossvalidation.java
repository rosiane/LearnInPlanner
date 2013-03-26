package neural.network.test;

import java.io.IOException;

import neural.network.impl.ParameterTraining;
import neural.network.interfaces.NeuralNetworkIF;
import neural.network.util.NeuralNetworkUtils;
import neural.network.util.Weight;
import preprocessor.file.FileManager;

import com.syvys.jaRBM.Layers.Layer;
import common.Data;
import common.MatrixHandler;

public class Crossvalidation {

	public void run(NeuralNetworkIF neuralNetwork, Layer[] net,
			Weight[] weights, String prefixSampleTraining,
			String prefixSampleTest, int numberAttribute, int numberOutput,
			ParameterTraining parameterTraining, int k, int quantityTraining,
			int quantityTest, String fileResult) throws IOException {
		Weight[] update = null;
		double[] errors = new double[k];
		double[] learningRate = new double[net.length];
		for (int indexNet = 0; indexNet < net.length; indexNet++) {
			learningRate[indexNet] = net[indexNet].getLearningRate();
		}
		for (int index = 0; index < k; index++) {
			if (fileResult == null || fileResult.isEmpty()) {
				System.out.println("Running " + (index + 1));
			}
			update = train(neuralNetwork, net, prefixSampleTraining, weights,
					quantityTraining, numberAttribute, numberOutput,
					parameterTraining, fileResult, index + 1);
			errors[index] = test(neuralNetwork, net, update, prefixSampleTest,
					quantityTest, numberAttribute, numberOutput,
					parameterTraining, fileResult, index + 1);
			for (int indexNet = 0; indexNet < net.length; indexNet++) {
				net[indexNet].setLearningRate(learningRate[indexNet]);
			}
		}
		System.out.println(k + "-Fold Crossvalidation");
		double mean = MatrixHandler.mean(errors);
		System.out.println("Mean: " + mean);
		double standardDeviation = MatrixHandler
				.standardDeviation(errors, mean);
		System.out.println("Standard Deviation: " + standardDeviation);
		System.out.println("\n\n");
		if (fileResult != null && !fileResult.isEmpty()) {
			FileManager.write(fileResult, "Mean: " + mean, true);
			FileManager.write(fileResult, "Standard Deviation: "
					+ standardDeviation, true);
		}
	}

	private Weight[] train(NeuralNetworkIF neuralNetwork, Layer[] net,
			String prefixSampleTraining, Weight[] weights, int numberInput,
			int numberAttribute, int numberOutput,
			ParameterTraining parameterTraining, String fileResult, int indexK)
			throws IOException {
		// System.out.println(numberInput);
		Data dataTraining = new Data();
		double[][] sample = new double[numberInput][numberAttribute];
		dataTraining.setSample(sample);
		double[][] label = new double[numberInput][numberOutput];
		dataTraining.setLabel(label);

		dataTraining = FileManager.read(prefixSampleTraining + indexK + ".csv",
				dataTraining);
		dataTraining = MatrixHandler.randomize(dataTraining.getSample(),
				dataTraining.getLabel());
		Weight[] update = weights.clone();
		// TODO Alterar para validacao
		update = neuralNetwork.train(net, update, dataTraining.getSample(),
				dataTraining.getLabel(), parameterTraining, null, fileResult);
		return update;
	}

	private double test(NeuralNetworkIF neuralNetwork, Layer[] net,
			Weight[] weights, String prefixSampleTest, int numberInput,
			int numberAttribute, int numberOutput,
			ParameterTraining parameterTraining, String fileResult, int indexK)
			throws IOException {
		Data dataTest = new Data();
		double[][] sample = new double[numberInput][numberAttribute];
		dataTest.setSample(sample);
		double[][] label = new double[numberInput][numberOutput];
		dataTest.setLabel(label);

		dataTest = FileManager.read(prefixSampleTest + indexK + ".csv",
				dataTest);
		double numberCorrect = 0;
		double[] result = null;
		for (int indexData = 0; indexData < MatrixHandler.rows(dataTest
				.getSample()); indexData++) {
			result = neuralNetwork.run(net, weights,
					MatrixHandler.getRow(dataTest.getSample(), indexData));
			if (NeuralNetworkUtils.isCorrect(result,
					MatrixHandler.getRow(dataTest.getLabel(), indexData),
					parameterTraining.getTask())) {
				numberCorrect++;
			}
		}
		double errorRate = 100 - ((double) numberCorrect / MatrixHandler
				.rows(sample)) * 100;
		if (fileResult == null || fileResult.isEmpty()) {
			System.out.println("Correct: " + numberCorrect + ", Error Rate: "
					+ errorRate + ", Execution: " + indexK);
		}
		return errorRate;
	}

	public void run(NeuralNetworkIF neuralNetwork, Layer[] net,
			Weight[][] weights_folds, String prefixSampleTraining,
			String prefixSampleTest, int numberAttribute, int numberOutput,
			ParameterTraining parameterTraining, int k, int quantityTraining,
			int quantityTest, String fileResult) throws IOException {
		Weight[] update = null;
		double[] errors = new double[k];
		double[] learningRate = new double[net.length];
		for (int indexNet = 0; indexNet < net.length; indexNet++) {
			learningRate[indexNet] = net[indexNet].getLearningRate();
		}
		for (int index = 0; index < k; index++) {
			System.out.println("Running " + (index + 1));
			// The current fold's weight matrices
			Weight[] weights = weights_folds[index];
			update = train(neuralNetwork, net, prefixSampleTraining, weights,
					quantityTraining, numberAttribute, numberOutput,
					parameterTraining, null, index + 1);
			errors[index] = test(neuralNetwork, net, update, prefixSampleTest,
					quantityTest, numberAttribute, numberOutput,
					parameterTraining, null, index + 1);
			for (int indexNet = 0; indexNet < net.length; indexNet++) {
				net[indexNet].setLearningRate(learningRate[indexNet]);
			}
		}
		System.out.println(k + "-Fold Crossvalidation");
		double mean = MatrixHandler.mean(errors);
		System.out.println("Mean: " + mean);
		double standardDeviation = MatrixHandler
				.standardDeviation(errors, mean);
		System.out.println("Standard Deviation: " + standardDeviation);
	}

}
