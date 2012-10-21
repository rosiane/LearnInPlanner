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
			String prefixSampleTest, int numberInput, int numberAttribute,
			int numberOutput, ParameterTraining parameterTraining, int k)
			throws IOException {
		Weight[] update = null;
		double[] errors = new double[k];
		int memberPerFold = numberInput / k;
		double[] learningRate = new double[net.length];
		for (int indexNet = 0; indexNet < net.length; indexNet++) {
			learningRate[indexNet] = net[indexNet].getLearningRate();
		}
		for (int index = 0; index < k; index++) {
			System.out.println("Running " + (index + 1));
			update = train(neuralNetwork, net, prefixSampleTraining, weights,
					(k - 1) * memberPerFold, numberAttribute, numberOutput,
					parameterTraining, index + 1);
			errors[index] = test(neuralNetwork, net, update, prefixSampleTest,
					memberPerFold, numberAttribute, numberOutput,
					parameterTraining, index + 1);
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

	private Weight[] train(NeuralNetworkIF neuralNetwork, Layer[] net,
			String prefixSampleTraining, Weight[] weights, int numberInput,
			int numberAttribute, int numberOutput,
			ParameterTraining parameterTraining, int indexK) throws IOException {
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
		 dataTraining.getLabel(), parameterTraining, null);
		return update;
	}

	private double test(NeuralNetworkIF neuralNetwork, Layer[] net,
			Weight[] weights, String prefixSampleTest, int numberInput,
			int numberAttribute, int numberOutput,
			ParameterTraining parameterTraining, int indexK) throws IOException {
		Data dataTest = new Data();
		double[][] sample = new double[numberInput][numberAttribute];
		dataTest.setSample(sample);
		double[][] label = new double[numberInput][numberOutput];
		dataTest.setLabel(label);

		dataTest = FileManager.read(prefixSampleTest + indexK + ".csv",
				dataTest);
		dataTest = MatrixHandler.randomize(dataTest.getSample(),
				dataTest.getLabel());
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
		System.out.println("Correct: " + numberCorrect + ", Error Rate: "
				+ errorRate + ", Execution: " + indexK);
		return errorRate;
	}

}
