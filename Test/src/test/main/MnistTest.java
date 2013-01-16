package test.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import neural.network.enums.Task;
import neural.network.impl.MLP;
import neural.network.impl.ParameterTraining;
import neural.network.interfaces.NeuralNetworkIF;
import neural.network.test.Crossvalidation;
import neural.network.util.LogisticLayerMLP;
import neural.network.util.NeuralNetworkUtils;
import neural.network.util.Weight;

import com.syvys.jaRBM.Layers.Layer;
import common.Data;
import common.MatrixHandler;
import deeplearning.DeepLearning;
import deeplearning.ParameterTrainingCRBM;

import preprocessor.file.FileManager;
import sun.security.util.Length;

public class MnistTest {

	public static void main(String[] args) throws IOException {
		// convertMnist();
		// prepareTest();
		// prepareCrossvalidation();
		// convertToBinary();
		// testCrossvalidation();
		// testPaper();
		testPaperParametersSemDeepLearning();
	}

	private static void testPaper() throws IOException {
		int quantityTraining = 10000;
		int quantityValidation = 2000;
		int quantityTest = 50000;

		int numberAttribute = 784;
		int numberUnitHidden = 200;
		int numberHiddenLayers = 1;
		int numberOutput = 10;

		double momentum = 0;
		double learningRate = 0.2;
		int intervalEpochPercentage = 2;

		long numberEpochs = 200;
		double maxError = 10;
		double learningRateDecrease = 0.9;
		double minLearningRate = 0.000000001;
		boolean initializeRandom = true;
		Task task = Task.CLASSIFICATION;

		String pathTraining = "./data/MNIST/normal/mnist_train_paper_binary.csv";
		String pathValidation = "./data/MNIST/normal/mnist_validation_binary.csv";
		String pathTest = "./data/MNIST/normal/mnist_test_binary.csv";

		NeuralNetworkIF neuralNetwork = new MLP();

		Layer[] net = new Layer[2];
		net[0] = new LogisticLayerMLP(numberUnitHidden);
		net[0].setMomentum(momentum);
		net[0].setLearningRate(learningRate);
		net[1] = new LogisticLayerMLP(numberOutput);
		net[1].setMomentum(momentum);
		net[1].setLearningRate(learningRate);

		Weight[] weights = new Weight[2];
		weights[0] = new Weight(numberAttribute, numberUnitHidden);
		weights[1] = new Weight(numberUnitHidden, numberOutput);

		System.out.println("MLP parameters:");
		System.out.println("Number of attributes: " + numberAttribute);
		System.out.println("Number of hidden layers: " + numberHiddenLayers);
		System.out.println("Number of hidden units: " + numberUnitHidden);
		System.out.println("Number of examples: "
				+ (quantityTraining + quantityTest) + "\n");
		System.out.println("Learning rate: " + learningRate);
		System.out.println("Learning rate decrease: " + learningRateDecrease);
		System.out.println("Max error: " + maxError + "\n");

		ParameterTraining parameterTraining = new ParameterTraining();
		parameterTraining.setNumberEpochs(numberEpochs);
		parameterTraining.setMaxError(maxError);
		parameterTraining.setTask(task.getValue());
		parameterTraining.setLearningRateDecrease(learningRateDecrease);
		parameterTraining.setMinLearningRate(minLearningRate);
		parameterTraining.setWeightsInitializationRandom(initializeRandom);
		parameterTraining.setIntervalEpochPercentage(intervalEpochPercentage);
		parameterTraining.setUpdateBatch(false);
		parameterTraining.setNormalizeWeights(true);
		parameterTraining.setValidation(true);

		long begin = System.currentTimeMillis();

		Data dataTraining = new Data();
		double[][] sample = new double[quantityTraining][numberAttribute];
		dataTraining.setSample(sample);
		double[][] label = new double[quantityTraining][numberOutput];
		dataTraining.setLabel(label);

		dataTraining = FileManager.read(pathTraining, dataTraining);
		dataTraining = MatrixHandler.randomize(dataTraining.getSample(),
				dataTraining.getLabel());

		Data dataValidation = new Data();
		sample = new double[quantityValidation][numberAttribute];
		dataValidation.setSample(sample);
		label = new double[quantityValidation][numberOutput];
		dataValidation.setLabel(label);

		dataTraining = FileManager.read(pathValidation, dataValidation);

		Weight[] update = weights.clone();
		update = neuralNetwork.train(net, update, dataTraining.getSample(),
				dataTraining.getLabel(), parameterTraining, dataValidation,
				null);

		Data dataTest = new Data();
		sample = new double[quantityTest][numberAttribute];
		dataTest.setSample(sample);
		label = new double[quantityTest][numberOutput];
		dataTest.setLabel(label);

		dataTest = FileManager.read(pathTest, dataTest);
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
				+ errorRate);

		System.out.println("Time millisseconds "
				+ (System.currentTimeMillis() - begin));

	}

	public static void prepareTest() throws IOException {
		String pathInputFile = "./data/MNIST/normal/mnist_train.csv";
		String pathTrain = "./data/MNIST/normal/mnist_train_paper.csv";
		String pathValidation = "./data/MNIST/normal/mnist_validation.csv";
		String pathTrainBinary = "./data/MNIST/normal/mnist_train_paper_binary.csv";
		String pathValidationBinary = "./data/MNIST/normal/mnist_validation_binary.csv";
		String separator = " ";
		long quantityTrain = 10000;
		FileManager.separeValidationMnist(pathInputFile, pathTrain,
				pathValidation, quantityTrain);
		Map<Integer, String> map = new HashMap<>();
		map.put(0, "1 0 0 0 0 0 0 0 0 0");
		map.put(1, "0 1 0 0 0 0 0 0 0 0");
		map.put(2, "0 0 1 0 0 0 0 0 0 0");
		map.put(3, "0 0 0 1 0 0 0 0 0 0");
		map.put(4, "0 0 0 0 1 0 0 0 0 0");
		map.put(5, "0 0 0 0 0 1 0 0 0 0");
		map.put(6, "0 0 0 0 0 0 1 0 0 0");
		map.put(7, "0 0 0 0 0 0 0 1 0 0");
		map.put(8, "0 0 0 0 0 0 0 0 1 0");
		map.put(9, "0 0 0 0 0 0 0 0 0 1");
		FileManager.convertLabelNumberToBinary(pathTrain, pathTrainBinary, map,
				separator);
		FileManager.convertLabelNumberToBinary(pathValidation,
				pathValidationBinary, map, separator);
		String pathInputTest = "./data/MNIST/normal/mnist_test.csv";
		String pathTestBinary = "./data/MNIST/normal/mnist_test_binary.csv";
		FileManager.convertLabelNumberToBinary(pathInputTest, pathTestBinary,
				map, separator);
	}

	public static void convertMnist() throws IOException {
		String pathInputFile = "./data/MNIST/normal/mnist_train.amat";
		String pathOutputFile = "./data/MNIST/normal/mnist_train.csv";
		FileManager.convertMnist(pathInputFile, pathOutputFile);
		pathInputFile = "./data/MNIST/normal/mnist_test.amat";
		pathOutputFile = "./data/MNIST/normal/mnist_test.csv";
		FileManager.convertMnist(pathInputFile, pathOutputFile);
	}

	public static void prepareCrossvalidation() {
		String pathInputFile = "./data/MNIST/normal/mnist_train.csv";
		String prefixOutputFile = "./data/MNIST/normal/mnist_normal_";
		String separator = " ";
		List<Double> classes = new ArrayList<>();
		classes.add(0.0);
		classes.add(1.0);
		classes.add(2.0);
		classes.add(3.0);
		classes.add(4.0);
		classes.add(5.0);
		classes.add(6.0);
		classes.add(7.0);
		classes.add(8.0);
		classes.add(9.0);
		try {
			FileManager.prepareCrossvalidation(pathInputFile, prefixOutputFile,
					10, classes, separator);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void convertToBinary() {
		Map<Integer, String> map = new HashMap<>();
		map.put(0, "1 0 0 0 0 0 0 0 0 0");
		map.put(1, "0 1 0 0 0 0 0 0 0 0");
		map.put(2, "0 0 1 0 0 0 0 0 0 0");
		map.put(3, "0 0 0 1 0 0 0 0 0 0");
		map.put(4, "0 0 0 0 1 0 0 0 0 0");
		map.put(5, "0 0 0 0 0 1 0 0 0 0");
		map.put(6, "0 0 0 0 0 0 1 0 0 0");
		map.put(7, "0 0 0 0 0 0 0 1 0 0");
		map.put(8, "0 0 0 0 0 0 0 0 1 0");
		map.put(9, "0 0 0 0 0 0 0 0 0 1");
		String prefix = "./data/MNIST/normal/mnist_normal_";
		try {
			for (int index = 1; index <= 10; index++) {
				FileManager.convertLabelNumberToBinary(prefix + "Test" + index
						+ ".csv", prefix + "binary_Test" + index + ".csv", map,
						" ");
				FileManager.convertLabelNumberToBinary(prefix + "Training"
						+ index + ".csv", prefix + "binary_Training" + index
						+ ".csv", map, " ");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void testCrossvalidation() throws IOException {
		// Troquei os parâmetros para resolver o problema do meu conjunto que
		// não está funcionando o crossvalidation. Separei o numberInput em
		// input de treinamento e de teste.
		int quantityTraining = 10806;
		int quantityTest = 1194;

		int numberAttribute = 784;
		int numberUnitHidden = 100;
		int numberHiddenLayers = 1;
		int numberOutput = 10;
		// int quantityTraining = 513;
		// int quantityTest = 56;

		double momentum = 0;
		double learningRate = 0.07;
		int intervalEpochPercentage = 10;

		long numberEpochs = 100;
		double maxError = 0;
		double learningRateDecrease = 1;
		double minLearningRate = 0.000000001;
		boolean initializeRandom = true;
		Task task = Task.CLASSIFICATION;

		String prefixSampleTraining = "./data/MNIST/normal/mnist_normal_binary_Training";
		String prefixSampleTest = "./data/MNIST/normal/mnist_normal_binary_Test";

		int k = 10;

		NeuralNetworkIF neuralNetwork = new MLP();

		// Layer[] net = new Layer[4];
		// net[0] = new LogisticLayerMLP(numberUnitHidden);
		// net[0].setMomentum(momentum);
		// net[0].setLearningRate(learningRate);
		// net[1] = new LogisticLayerMLP(numberUnitHidden);
		// net[1].setMomentum(momentum);
		// net[1].setLearningRate(learningRate);
		// net[2] = new LogisticLayerMLP(numberUnitHidden);
		// net[2].setMomentum(momentum);
		// net[2].setLearningRate(learningRate);
		// net[3] = new LogisticLayerMLP(numberOutput);
		// net[3].setMomentum(momentum);
		// net[3].setLearningRate(learningRate);

		Layer[] net = new Layer[2];
		net[0] = new LogisticLayerMLP(numberUnitHidden);
		net[0].setMomentum(momentum);
		net[0].setLearningRate(learningRate);
		net[1] = new LogisticLayerMLP(numberOutput);
		net[1].setMomentum(momentum);
		net[1].setLearningRate(learningRate);

		// Layer[] net = new Layer[3];
		// net[0] = new LogisticLayerMLP(numberUnitHidden);
		// net[0].setMomentum(momentum);
		// net[0].setLearningRate(learningRate);
		// net[1] = new LogisticLayerMLP(numberUnitHidden);
		// net[1].setMomentum(momentum);
		// net[1].setLearningRate(learningRate);
		// net[2] = new LogisticLayerMLP(numberOutput);
		// net[2].setMomentum(momentum);
		// net[2].setLearningRate(learningRate);

		Weight[] weights = new Weight[2];
		weights[0] = new Weight(numberAttribute, numberUnitHidden);
		weights[1] = new Weight(numberUnitHidden, numberOutput);

		// Weight[] weights = new Weight[4];
		// weights[0] = new Weight(numberAttribute, numberUnitHidden);
		// weights[1] = new Weight(numberUnitHidden, numberUnitHidden);
		// weights[2] = new Weight(numberUnitHidden, numberUnitHidden);
		// weights[3] = new Weight(numberUnitHidden, numberOutput);

		System.out.println("MLP parameters:");
		System.out.println("Number of attributes: " + numberAttribute);
		System.out.println("Number of hidden layers: " + numberHiddenLayers);
		System.out.println("Number of hidden units: " + numberUnitHidden);
		System.out.println("Number of examples: "
				+ (quantityTraining + quantityTest) + "\n");
		System.out.println("Learning rate: " + learningRate);
		System.out.println("Learning rate decrease: " + learningRateDecrease);
		System.out.println("Max error: " + maxError + "\n");

		// -- DEEP LEARNING
		// Data dataTraining = new Data();
		// double[][] sample = new double[quantityTraining][numberAttribute];
		// dataTraining.setSample(sample);
		// double[][] label = new double[quantityTraining][numberAttribute];
		// dataTraining.setLabel(label);
		//
		// // CRBM training parameters.
		// ParameterTrainingCRBM parameterTrainingCRBM = new
		// ParameterTrainingCRBM();
		// parameterTrainingCRBM.setLearning_rate_weights(1.0);
		// parameterTrainingCRBM.setLearning_rate_aj(1.0);
		// parameterTrainingCRBM.setTheta_low(-1.0);
		// parameterTrainingCRBM.setTheta_high(1.0);
		// parameterTrainingCRBM.setSigma(0.2);
		//
		// System.out.println("CRBM parameters:");
		// System.out.println("Learning rate (weights): "
		// + parameterTrainingCRBM.getLearning_rate_weights());
		// System.out.println("Learning rate (noise controls; 'aj'): "
		// + parameterTrainingCRBM.getLearning_rate_aj());
		// System.out
		// .println("Theta low: " + parameterTrainingCRBM.getTheta_low());
		// System.out.println("Theta high: "
		// + parameterTrainingCRBM.getTheta_high());
		// System.out.println("Sigma: " + parameterTrainingCRBM.getSigma() +
		// "\n");
		//
		// int numberEpochsCRBM = 100;
		//
		// // A list of 'k' lists of weight matrices.
		// Weight[][] weight_folds = new Weight[k][numberUnitHidden + 1];
		//
		// for (int fold = 0; fold < k; fold++) {
		// System.out.println("CRBM pre-training: fold " + (fold + 1) + ":");
		//
		// dataTraining = FileManager.read(prefixSampleTraining + (fold + 1)
		// + ".csv", dataTraining);
		// DeepLearning deep_learning = new DeepLearning(numberAttribute,
		// numberHiddenLayers, numberUnitHidden, numberOutput,
		// numberEpochsCRBM, parameterTrainingCRBM);
		// Weight[] weights = deep_learning.runDeepLearning(dataTraining
		// .getSample());
		// weight_folds[fold] = weights;
		// }

		System.out.println("- MLP -");

		ParameterTraining parameterTraining = new ParameterTraining();
		parameterTraining.setNumberEpochs(numberEpochs);
		parameterTraining.setMaxError(maxError);
		parameterTraining.setTask(task.getValue());
		parameterTraining.setLearningRateDecrease(learningRateDecrease);
		parameterTraining.setMinLearningRate(minLearningRate);
		parameterTraining.setWeightsInitializationRandom(initializeRandom);
		parameterTraining.setIntervalEpochPercentage(intervalEpochPercentage);
		parameterTraining.setUpdateBatch(false);
		parameterTraining.setNormalizeWeights(true);
		parameterTraining.setValidation(false);

		long begin = System.currentTimeMillis();
		// Troquei os parâmetros para resolver o problema do meu conjunto que
		// não está funcionando o crossvalidation. Separei o numberInput em
		// input de treinamento e de teste.
		Crossvalidation crossvalidation = new Crossvalidation();
		crossvalidation.run(neuralNetwork, net, weights, prefixSampleTraining,
				prefixSampleTest, numberAttribute, numberOutput,
				parameterTraining, k, quantityTraining, quantityTest, null);
		System.out.println("Time millisseconds "
				+ (System.currentTimeMillis() - begin));
	}

	private static void testPaperParametersSemDeepLearning() throws IOException {
		// Parametros fixos
		String fileResult = "./data/MNIST/normal/resultados/Resultados.doc";
		int quantityTraining = 10000;
		int quantityValidation = 2000;
		int quantityTest = 50000;

		int numberAttribute = 784;
		int numberHiddenLayers = 1;
		int numberOutput = 10;

		double momentum = 0;
		int intervalEpochPercentage = 2;
		double minLearningRate = 0.000000000001;
		Task task = Task.CLASSIFICATION;
		boolean initializeRandom = true;

		// Parametros para variar
		int[] numberUnitHidden = { 200, 300 };
		double[] learningRate = { 0.1, 0.2 };
		long[] numberEpochs = { 200, 500, 1000 };
		double[] maxError = { 0, 5 };
		double[] learningRateDecrease = { 0.99, 0.9 };

		// Carregando dados
		String pathTraining = "./data/MNIST/normal/mnist_train_paper_binary.csv";
		String pathValidation = "./data/MNIST/normal/mnist_validation_binary.csv";
		String pathTest = "./data/MNIST/normal/mnist_test_binary.csv";

		Data dataTraining = new Data();
		double[][] sample = new double[quantityTraining][numberAttribute];
		dataTraining.setSample(sample);
		double[][] label = new double[quantityTraining][numberOutput];
		dataTraining.setLabel(label);
		dataTraining = FileManager.read(pathTraining, dataTraining);
		dataTraining = MatrixHandler.randomize(dataTraining.getSample(),
				dataTraining.getLabel());

		Data dataValidation = new Data();
		sample = new double[quantityValidation][numberAttribute];
		dataValidation.setSample(sample);
		label = new double[quantityValidation][numberOutput];
		dataValidation.setLabel(label);
		dataTraining = FileManager.read(pathValidation, dataValidation);

		Data dataTest = new Data();
		sample = new double[quantityTest][numberAttribute];
		dataTest.setSample(sample);
		label = new double[quantityTest][numberOutput];
		dataTest.setLabel(label);
		dataTest = FileManager.read(pathTest, dataTest);

		testPaperParametersAux(quantityTraining, quantityTest, numberAttribute,
				numberHiddenLayers, numberOutput, momentum,
				intervalEpochPercentage, minLearningRate, task,
				initializeRandom, numberUnitHidden, learningRate, numberEpochs,
				maxError, learningRateDecrease, dataTraining, sample,
				dataValidation, dataTest, fileResult);

	}

	private static void testPaperParametersAux(int quantityTraining,
			int quantityTest, int numberAttribute, int numberHiddenLayers,
			int numberOutput, double momentum, int intervalEpochPercentage,
			double minLearningRate, Task task, boolean initializeRandom,
			int[] numberUnitHidden, double[] learningRate, long[] numberEpochs,
			double[] maxError, double[] learningRateDecrease,
			Data dataTraining, double[][] sample, Data dataValidation,
			Data dataTest, String fileResult) throws IOException {
		NeuralNetworkIF neuralNetwork = new MLP();
		Layer[] net = new Layer[2];
		Weight[] weights = new Weight[2];
		ParameterTraining parameterTraining = null;
		long begin = 0;
		long duration = 0;
		double numberCorrect = 0;
		double[] result = null;
		Weight[] update = null;
		double errorRate = 0;
		for (int indexUnitHidden = 0; indexUnitHidden < numberUnitHidden.length; indexUnitHidden++) {
			for (int indexLearningRate = 0; indexLearningRate < learningRate.length; indexLearningRate++) {
				for (int indexNumberEpochs = 0; indexNumberEpochs < numberEpochs.length; indexNumberEpochs++) {
					for (int indexMaxError = 0; indexMaxError < maxError.length; indexMaxError++) {
						for (int indexLearningRateDecrease = 0; indexLearningRateDecrease < learningRateDecrease.length; indexLearningRateDecrease++) {
							net[0] = new LogisticLayerMLP(
									numberUnitHidden[indexUnitHidden]);
							net[0].setMomentum(momentum);
							net[0].setLearningRate(learningRate[indexLearningRate]);
							net[1] = new LogisticLayerMLP(numberOutput);
							net[1].setMomentum(momentum);
							net[1].setLearningRate(learningRate[indexLearningRate]);

							weights[0] = new Weight(numberAttribute,
									numberUnitHidden[indexUnitHidden]);
							weights[1] = new Weight(
									numberUnitHidden[indexUnitHidden],
									numberOutput);

							System.out.println("MLP parameters:");
							System.out.println("Number of attributes: "
									+ numberAttribute);
							System.out.println("Number of hidden layers: "
									+ numberHiddenLayers);
							System.out.println("Number of hidden units: "
									+ numberUnitHidden[indexUnitHidden]);
							System.out.println("Number of examples: "
									+ (quantityTraining + quantityTest) + "\n");
							System.out.println("Learning rate: "
									+ learningRate[indexLearningRate]);
							System.out
									.println("Learning rate decrease: "
											+ learningRateDecrease[indexLearningRateDecrease]);
							System.out.println("Number of Epochs: "
									+ numberEpochs[indexNumberEpochs]);
							System.out.println("Max error: "
									+ maxError[indexMaxError] + "\n");

							FileManager.write(fileResult, "MLP parameters:",
									true);
							FileManager.write(fileResult,
									"Number of attributes: " + numberAttribute,
									true);
							FileManager.write(fileResult,
									"Number of hidden layers: "
											+ numberHiddenLayers, true);
							FileManager
									.write(fileResult,
											"Number of hidden units: "
													+ numberUnitHidden[indexUnitHidden],
											true);
							FileManager.write(fileResult, "Learning rate: "
									+ learningRate[indexLearningRate], true);
							FileManager
									.write(fileResult,
											"Learning rate decrease: "
													+ learningRateDecrease[indexLearningRateDecrease],
											true);
							FileManager.write(fileResult, "Number of Epochs: "
									+ numberEpochs[indexNumberEpochs], true);
							FileManager.write(fileResult, "Max error: "
									+ maxError[indexMaxError], true);

							parameterTraining = new ParameterTraining();
							parameterTraining
									.setNumberEpochs(numberEpochs[indexNumberEpochs]);
							parameterTraining
									.setMaxError(maxError[indexMaxError]);
							parameterTraining.setTask(task.getValue());
							parameterTraining
									.setLearningRateDecrease(learningRateDecrease[indexLearningRateDecrease]);
							parameterTraining
									.setMinLearningRate(minLearningRate);
							parameterTraining
									.setWeightsInitializationRandom(initializeRandom);
							parameterTraining
									.setIntervalEpochPercentage(intervalEpochPercentage);
							parameterTraining.setUpdateBatch(false);
							parameterTraining.setNormalizeWeights(true);
							parameterTraining.setValidation(true);

							begin = System.currentTimeMillis();

							update = weights.clone();
							update = neuralNetwork.train(net, update,
									dataTraining.getSample(),
									dataTraining.getLabel(), parameterTraining,
									dataValidation, fileResult);

							numberCorrect = 0;
							result = null;
							for (int indexData = 0; indexData < MatrixHandler
									.rows(dataTest.getSample()); indexData++) {
								result = neuralNetwork
										.run(net, weights, MatrixHandler
												.getRow(dataTest.getSample(),
														indexData));
								if (NeuralNetworkUtils
										.isCorrect(result, MatrixHandler
												.getRow(dataTest.getLabel(),
														indexData),
												parameterTraining.getTask())) {
									numberCorrect++;
								}
							}
							errorRate = 100 - ((double) numberCorrect / MatrixHandler
									.rows(sample)) * 100;

							duration = System.currentTimeMillis() - begin;

							System.out.println("Correct: " + numberCorrect
									+ ", Error Rate: " + errorRate);
							System.out.println("Time millisseconds: "
									+ duration);

							FileManager.write(fileResult, "Correct: "
									+ numberCorrect + ", Error Rate: "
									+ errorRate, true);
							FileManager.write(fileResult,
									"Time millisseconds: " + duration, true);
							FileManager
									.write(fileResult,
											"#######################################################################",
											true);
						}
					}
				}
			}
		}
	}
}
