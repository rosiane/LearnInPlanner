package test.main;

import java.io.IOException;

import neural.network.enums.Task;
import neural.network.impl.MLP;
import neural.network.impl.ParameterTraining;
import neural.network.interfaces.NeuralNetworkIF;
import neural.network.test.Crossvalidation;
import neural.network.util.Weight;
import preprocessor.file.FileManager;

import com.syvys.jaRBM.Layers.Layer;
import com.syvys.jaRBM.Layers.LogisticLayer;
import common.Data;
import common.MatrixHandler;

public class Main {

	public static void main(String[] args) {
		try {
			testCrossvalidation();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void testNet() {
		int numberInput = 100;
		int numberAttribute = 4;
		int numberOutput = 2;
		String pathFile = "./data/iris4crossvalidation_1_2.csv";
		double momentum = 0.2;
		double learningRate = 0.5;
		int numberUnitHidden = 5;
		long numberEpochs = 50;
		double maxError = 5;
		double learningRateDecrease = 0.45;
		double minLearningRate = 0.0002;
		boolean initializeRandom = true;
		Task task = Task.CLASSIFICATION;

		Data data = new Data();
		double[][] sample = new double[numberInput][numberAttribute];
		data.setSample(sample);
		double[][] label = new double[numberInput][numberOutput];
		data.setLabel(label);
		Data randomData = null;
		try {
			data = FileManager.read(pathFile, data);
			randomData = MatrixHandler.randomize(data.getSample(),
					data.getLabel());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ParameterTraining parameterTraining = new ParameterTraining();
		parameterTraining.setNumberEpochs(numberEpochs);
		parameterTraining.setMaxError(maxError);
		parameterTraining.setTask(task.getValue());
		parameterTraining.setLearningRateDecrease(learningRateDecrease);
		parameterTraining.setMinLearningRate(minLearningRate);
		parameterTraining.setWeightsInitializationRandom(initializeRandom);

		Layer[] net = new Layer[2];
		net[0] = new LogisticLayer(numberUnitHidden);
		net[0].setMomentum(momentum);
		net[0].setLearningRate(learningRate);
		net[1] = new LogisticLayer(numberOutput);
		net[1].setMomentum(momentum);
		net[1].setLearningRate(learningRate);

		Weight[] weights = new Weight[2];
		weights[0] = new Weight(numberAttribute, numberUnitHidden);
		weights[1] = new Weight(numberUnitHidden, numberOutput);

		NeuralNetworkIF neuralNetwork = new MLP();
		neuralNetwork.train(net, weights, randomData.getSample(),
				randomData.getLabel(), parameterTraining);
	}

	public static void testCrossvalidation() throws IOException {
		int numberInput = 100;

		int numberAttribute = 4;
		int numberUnitHidden = 8;
		int numberOutput = 2;

		double momentum = 0.1;
		double learningRate = 0.4;
		int intervalEpochPercentage = 2;

		long numberEpochs = 2000;
		double maxError = 3;
		double learningRateDecrease = 0.99;
		double minLearningRate = 0.1;
		boolean initializeRandom = true;
		Task task = Task.CLASSIFICATION;

		String prefixSampleTraining = "./data/1_2_nopattern/iris_binaryTraining";
		String prefixSampleTest = "./data/1_2_nopattern/iris_binaryTest";

		int k = 10;

		NeuralNetworkIF neuralNetwork = new MLP();

		Layer[] net = new Layer[2];
		net[0] = new LogisticLayer(numberUnitHidden);
		net[0].setMomentum(momentum);
		net[0].setLearningRate(learningRate);
		net[1] = new LogisticLayer(numberOutput);
		net[1].setMomentum(momentum);
		net[1].setLearningRate(learningRate);

		Weight[] weights = new Weight[2];
		weights[0] = new Weight(numberAttribute, numberUnitHidden);
		weights[1] = new Weight(numberUnitHidden, numberOutput);

		ParameterTraining parameterTraining = new ParameterTraining();
		parameterTraining.setNumberEpochs(numberEpochs);
		parameterTraining.setMaxError(maxError);
		parameterTraining.setTask(task.getValue());
		parameterTraining.setLearningRateDecrease(learningRateDecrease);
		parameterTraining.setMinLearningRate(minLearningRate);
		parameterTraining.setWeightsInitializationRandom(initializeRandom);
		parameterTraining.setIntervalEpochPercentage(intervalEpochPercentage);

		Crossvalidation crossvalidation = new Crossvalidation();
		crossvalidation.run(neuralNetwork, net, weights, prefixSampleTraining,
				prefixSampleTest, numberInput, numberAttribute, numberOutput,
				parameterTraining, k);
	}

}
