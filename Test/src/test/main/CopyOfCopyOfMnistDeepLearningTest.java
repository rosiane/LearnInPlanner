package test.main;

import java.io.IOException;

import neural.network.enums.Task;
import neural.network.impl.MLP;
import neural.network.impl.ParameterTraining;
import neural.network.interfaces.NeuralNetworkIF;
import neural.network.util.NeuralNetworkUtils;
import neural.network.util.Weight;
import preprocessor.file.FileManager;

import com.syvys.jaRBM.Layers.Layer;
import com.syvys.jaRBM.Layers.LogisticLayer;
import com.syvys.jaRBM.Layers.SoftmaxLayer;
import common.Data;
import common.MatrixHandler;

import deeplearning.DeepLearning;
import deeplearning.ParameterTrainingCRBM;

public class CopyOfCopyOfMnistDeepLearningTest {

	public static void main(String[] args) throws IOException {
		// testPaper();
		testPaperParametersComDeepLearning();
	}

	private static void testPaper() throws IOException {
		int quantityTraining = 10000;
		int quantityValidation = 2000;
		int quantityTest = 50000;

		int numberAttribute = 784;
		int[] numberUnitHidden = { 200 };
		int numberHiddenLayers = 2;
		int numberOutput = 10;

		double momentum = 0;
		double learningRate = 0.1;
		int intervalEpochPercentage = 2;

		long numberEpochs = 200;
		double maxError = 0;
		double learningRateDecrease = 0.99;
		double minLearningRate = 0.000000001;
		boolean initializeRandom = false;
		Task task = Task.CLASSIFICATION;

		String pathTraining = "./data/MNIST/normal/mnist_train_paper_binary.csv";
		String pathValidation = "./data/MNIST/normal/mnist_validation_binary.csv";
		String pathTest = "./data/MNIST/normal/mnist_test_binary.csv";

		NeuralNetworkIF neuralNetwork = new MLP();

		Layer[] net = new Layer[3];
		net[0] = new LogisticLayer(numberUnitHidden[0]);
		net[0].setMomentum(momentum);
		net[0].setLearningRate(learningRate);
		net[1] = new LogisticLayer(numberUnitHidden[0]);
		net[1].setMomentum(momentum);
		net[1].setLearningRate(learningRate);
		net[2] = new LogisticLayer(numberOutput);
		net[2].setMomentum(momentum);
		net[2].setLearningRate(learningRate);
		// net[3] = new LogisticLayerMLP(numberOutput);
		// net[3].setMomentum(momentum);
		// net[3].setLearningRate(learningRate);

		// Weight[] weights = new Weight[4];
		// weights[0] = new Weight(numberAttribute, numberUnitHidden);
		// weights[1] = new Weight(numberUnitHidden, numberUnitHidden);
		// weights[2] = new Weight(numberUnitHidden, numberUnitHidden);
		// weights[3] = new Weight(numberUnitHidden, numberOutput);

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

		// CRBM training parameters.
		ParameterTrainingCRBM parameterTrainingCRBM = new ParameterTrainingCRBM();
		parameterTrainingCRBM.setLearning_rate_weights(1.0);
		parameterTrainingCRBM.setLearning_rate_aj(1.0);
		parameterTrainingCRBM.setTheta_low(-1.0);
		parameterTrainingCRBM.setTheta_high(1.0);
		parameterTrainingCRBM.setSigma(0.2);

		int numberEpochsCRBM = 100;
		DeepLearning deep_learning = new DeepLearning(numberAttribute,
				numberHiddenLayers, numberUnitHidden, numberOutput,
				numberEpochsCRBM, parameterTrainingCRBM);
		// Aplicando o deep learning
		System.out.println("CRBM parameters:");
		System.out.println("Learning rate (weights): "
				+ parameterTrainingCRBM.getLearning_rate_weights());
		System.out.println("Learning rate (noise controls; 'aj'): "
				+ parameterTrainingCRBM.getLearning_rate_aj());
		System.out
				.println("Theta low: " + parameterTrainingCRBM.getTheta_low());
		System.out.println("Theta high: "
				+ parameterTrainingCRBM.getTheta_high());
		System.out.println("Sigma: " + parameterTrainingCRBM.getSigma() + "\n");

		Weight[] weights = deep_learning.runDeepLearning(dataTraining
				.getSample());

		System.out.println("MLP parameters:");
		System.out.println("Number of attributes: " + numberAttribute);
		System.out.println("Number of hidden layers: " + numberHiddenLayers);
		System.out.println("Number of hidden units: " + numberUnitHidden);
		System.out.println("Number of examples: "
				+ (quantityTraining + quantityTest) + "\n");
		System.out.println("Learning rate: " + learningRate);
		System.out.println("Learning rate decrease: " + learningRateDecrease);
		System.out.println("Max error: " + maxError + "\n");

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

	private static void testPaperParametersComDeepLearning() throws IOException {
		// Parametros fixos
		String fileResult = "./data/MNIST/normal/resultados/Resultados_DeepLearning_Softmax_2.doc";
		int quantityTraining = 50000;
		int quantityValidation = 2000;
		int quantityTest = 10000;

		int numberAttribute = 784;
		int numberHiddenLayers = 3;
		int[] numberUnitHidden = { 500, 500, 50 };
		/* int[] numberUnitHidden = { 10, 10, 20 }; */
		int numberOutput = 10;

		double momentum = 0.05;
		int intervalEpochPercentage = 8;
		double minLearningRate = 0.000000000001;
		Task task = Task.CLASSIFICATION;
		boolean initializeRandom = false;
		// boolean initializeRandom = true;

		// Parametros para variar
		double[] learningRate = {/* 0.05 */0.1/* , 0.2, 0.5, 0.9 */};
		long[] numberEpochs = { 200/*, 10*/ };
		double[] maxError = { 0 /* , 15, 12 */};
		double[] learningRateDecrease = { /* 0.99, */1 };

		// Carregando dados
		String pathTest = "./data/MNIST/normal/mnist_train_paper_binary.csv";
		String pathValidation = "./data/MNIST/normal/mnist_validation_binary.csv";
		String pathTraining = "./data/MNIST/normal/mnist_test_binary.csv";

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

		testPaperParametersAuxComDeepLearning(quantityTraining, quantityTest,
				numberAttribute, numberHiddenLayers, numberOutput, momentum,
				intervalEpochPercentage, minLearningRate, task,
				initializeRandom, numberUnitHidden, learningRate, numberEpochs,
				maxError, learningRateDecrease, dataTraining, sample,
				dataValidation, dataTest, fileResult);

	}

	private static void testPaperParametersAuxComDeepLearning(
			int quantityTraining, int quantityTest, int numberAttribute,
			int numberHiddenLayers, int numberOutput, double momentum,
			int intervalEpochPercentage, double minLearningRate, Task task,
			boolean initializeRandom, int[] numberUnitHidden,
			double[] learningRate, long[] numberEpochs, double[] maxError,
			double[] learningRateDecrease, Data dataTraining,
			double[][] sample, Data dataValidation, Data dataTest,
			String fileResult) throws IOException {
		NeuralNetworkIF neuralNetwork = new MLP();
		Layer[] net = new Layer[4];
		Weight[] weights = null;
		ParameterTraining parameterTraining = null;
		long begin = 0;
		long duration = 0;

		// CRBM training parameters.
		ParameterTrainingCRBM parameterTrainingCRBM = new ParameterTrainingCRBM();
		parameterTrainingCRBM.setLearning_rate_weights(0.5);
		parameterTrainingCRBM.setLearning_rate_aj(0.5);
		parameterTrainingCRBM.setTheta_low(-1.0);
		parameterTrainingCRBM.setTheta_high(1.0);
		parameterTrainingCRBM.setSigma(0.2);

		int numberEpochsCRBM = 10;
		DeepLearning deep_learning = null;
		double numberCorrect = 0;
		double[] result = null;
		double errorRate = 0;
		Weight[] update = null;
		// for (int indexUnitHidden = 0; indexUnitHidden <
		// numberUnitHidden.length; indexUnitHidden++) {
		for (int indexLearningRate = 0; indexLearningRate < learningRate.length; indexLearningRate++) {
			for (int indexNumberEpochs = 0; indexNumberEpochs < numberEpochs.length; indexNumberEpochs++) {
				for (int indexMaxError = 0; indexMaxError < maxError.length; indexMaxError++) {
					for (int indexLearningRateDecrease = 0; indexLearningRateDecrease < learningRateDecrease.length; indexLearningRateDecrease++) {
						net[0] = new LogisticLayer(numberUnitHidden[0]);
						net[0].setMomentum(momentum);
						net[0].setLearningRate(learningRate[indexLearningRate]);
						net[1] = new LogisticLayer(numberUnitHidden[1]);
						net[1].setMomentum(momentum);
						net[1].setLearningRate(learningRate[indexLearningRate]);
						net[2] = new LogisticLayer(numberUnitHidden[2]);
						net[2].setMomentum(momentum);
						net[2].setLearningRate(learningRate[indexLearningRate]);
						net[3] = new SoftmaxLayer(numberOutput);
						net[3].setMomentum(momentum);
						net[3].setLearningRate(learningRate[indexLearningRate]);

						// Aplicando o deep learning
						System.out.println("CRBM parameters:");
						System.out.println("Learning rate (weights): "
								+ parameterTrainingCRBM
										.getLearning_rate_weights());
						System.out
								.println("Learning rate (noise controls; 'aj'): "
										+ parameterTrainingCRBM
												.getLearning_rate_aj());
						System.out.println("Theta low: "
								+ parameterTrainingCRBM.getTheta_low());
						System.out.println("Theta high: "
								+ parameterTrainingCRBM.getTheta_high());
						System.out.println("Sigma: "
								+ parameterTrainingCRBM.getSigma() + "\n");
						FileManager.write(fileResult, "CRBM parameters:", true);
						FileManager.write(
								fileResult,
								"Learning rate (weights): "
										+ parameterTrainingCRBM
												.getLearning_rate_weights(),
								true);
						FileManager.write(
								fileResult,
								"Learning rate (noise controls; 'aj'): "
										+ parameterTrainingCRBM
												.getLearning_rate_aj(), true);
						FileManager.write(fileResult, "Theta low: "
								+ parameterTrainingCRBM.getTheta_low(), true);
						FileManager.write(fileResult, "Theta high: "
								+ parameterTrainingCRBM.getTheta_high(), true);
						FileManager.write(fileResult, "Sigma: "
								+ parameterTrainingCRBM.getSigma(), true);
						FileManager.write(fileResult, "", true);

						begin = System.currentTimeMillis();

						deep_learning = new DeepLearning(numberAttribute,
								numberHiddenLayers, numberUnitHidden,
								numberOutput, numberEpochsCRBM,
								parameterTrainingCRBM);
						weights = deep_learning.runDeepLearning(dataTraining
								.getSample());

						System.out.println("MLP parameters:");
						System.out.println("Number of attributes: "
								+ numberAttribute);
						System.out.println("Number of hidden layers: "
								+ numberHiddenLayers);
						System.out.println("Number of hidden units: {"
								+ numberUnitHidden[0] + ", "
								+ numberUnitHidden[1] + ", "
								+ numberUnitHidden[2] + "}");

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

						FileManager.write(fileResult, "MLP parameters:", true);
						FileManager.write(fileResult, "Number of attributes: "
								+ numberAttribute, true);
						FileManager.write(fileResult,
								"Number of hidden layers: "
										+ numberHiddenLayers, true);
						FileManager.write(fileResult,
								"Number of hidden units: {"
										+ numberUnitHidden[0] + ", "
										+ numberUnitHidden[1] + ", "
										+ numberUnitHidden[2] + "}", true);
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
						parameterTraining.setMaxError(maxError[indexMaxError]);
						parameterTraining.setTask(task.getValue());
						parameterTraining
								.setLearningRateDecrease(learningRateDecrease[indexLearningRateDecrease]);
						parameterTraining.setMinLearningRate(minLearningRate);
						parameterTraining
								.setWeightsInitializationRandom(initializeRandom);
						parameterTraining
								.setIntervalEpochPercentage(intervalEpochPercentage);
						parameterTraining.setUpdateBatch(false);
						parameterTraining.setNormalizeWeights(true);
						parameterTraining.setValidation(true);

						update = weights.clone();
						update = neuralNetwork.train(net, update,
								dataTraining.getSample(),
								dataTraining.getLabel(), parameterTraining,
								dataValidation, fileResult);

						numberCorrect = 0;
						result = null;
						for (int indexData = 0; indexData < MatrixHandler
								.rows(dataTest.getSample()); indexData++) {
							result = neuralNetwork.run(net, weights,
									MatrixHandler.getRow(dataTest.getSample(),
											indexData));
							if (NeuralNetworkUtils.isCorrect(result,
									MatrixHandler.getRow(dataTest.getLabel(),
											indexData), parameterTraining
											.getTask())) {
								numberCorrect++;
							}
						}
						errorRate = 100 - ((double) numberCorrect / MatrixHandler
								.rows(sample)) * 100;

						duration = System.currentTimeMillis() - begin;

						System.out.println("Correct: " + numberCorrect
								+ ", Error Rate: " + errorRate);
						System.out.println("Time millisseconds: " + duration);

						FileManager.write(fileResult, "Correct: "
								+ numberCorrect + ", Error Rate: " + errorRate,
								true);
						FileManager.write(fileResult, "Time millisseconds: "
								+ duration, true);
						FileManager
								.write(fileResult,
										"#######################################################################",
										true);
					}
				}
			}
		}
		// }
	}
}
