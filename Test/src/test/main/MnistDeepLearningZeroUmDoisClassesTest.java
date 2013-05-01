package test.main;

import java.io.IOException;

import neural.network.enums.Task;
import neural.network.impl.MLP;
import neural.network.impl.ParameterTraining;
import neural.network.interfaces.NeuralNetworkIF;
import neural.network.util.HyperbolicTangentLayer;
import neural.network.util.NeuralNetworkUtils;
import neural.network.util.Weight;

import com.syvys.jaRBM.Layers.Layer;
import com.syvys.jaRBM.Layers.LogisticLayer;
import com.syvys.jaRBM.Layers.SoftmaxLayer;
import common.Data;
import common.MatrixHandler;
import common.preprocessor.file.FileManager;

import deeplearning.DeepLearning;
import deeplearning.ParameterTrainingCRBM;

public class MnistDeepLearningZeroUmDoisClassesTest {

	public static void main(String[] args) throws IOException {
		 testWithoutDeepLearning();
//		testPaperParametersComDeepLearning();
	}

	private static void testWithoutDeepLearning() throws IOException {
		int quantityTraining = 15518;
		int quantityValidation = 614;
		int quantityTest = 3119;

		int numberAttribute = 784;
		int[] numberUnitHidden = { 30 };
		int numberHiddenLayers = 1;
		int numberOutput = 3;

		double momentum = 0.05;
		double learningRate = 0.2;
		int intervalEpochPercentage = 10;

		long numberEpochs = 100;
		double maxError = 0.2;
		double learningRateDecrease = 0.9;
		double minLearningRate = 0.000000001;
		boolean initializeRandom = true;
		Task task = Task.CLASSIFICATION;

		String pathTraining = "./data/MNIST/normal/mnist_train_0_1_2_binary.csv";
		String pathValidation = "./data/MNIST/normal/mnist_validation_0_1_2_binary.csv";
		String pathTest = "./data/MNIST/normal/mnist_test_0_1_2_binary.csv";
		String fileResult = "./data/MNIST/normal/resultados/Resultados_MNIST_WithoutDeepLearning_ZeroUmDois.doc";

		NeuralNetworkIF neuralNetwork = new MLP();

		Layer[] net = new Layer[2];
		net[0] = new HyperbolicTangentLayer(numberUnitHidden[0]);
		net[0].setMomentum(momentum);
		net[0].setLearningRate(learningRate);
		net[1] = new SoftmaxLayer(numberOutput);
		net[1].setMomentum(momentum);
		net[1].setLearningRate(learningRate);

		Weight[] weights = new Weight[2];
		weights[0] = new Weight(numberAttribute, numberUnitHidden[0]);
		weights[1] = new Weight(numberUnitHidden[0], numberOutput);

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

		System.out.println("MLP parameters:");
		System.out.println("Number of attributes: " + numberAttribute);
		System.out.println("Number of hidden layers: " + numberHiddenLayers);
		System.out.println("Number of hidden units: " + numberUnitHidden[0]);
		System.out.println("Number of examples: "
				+ (quantityTraining + quantityTest) + "\n");
		System.out.println("Learning rate: " + learningRate);
		System.out.println("Learning rate decrease: " + learningRateDecrease);
		System.out.println("Max error: " + maxError + "\n");
		System.out.println("Momentum: " + momentum + "\n");

		FileManager.write(fileResult, "MLP parameters:", true);
		FileManager.write(fileResult, "Number of attributes: "
				+ numberAttribute, true);
		FileManager.write(fileResult, "Number of hidden layers: "
				+ numberHiddenLayers, true);
		FileManager.write(fileResult, "Number of hidden units: "
				+ numberUnitHidden[0], true);
		FileManager.write(fileResult, "Number of examples: "
				+ (quantityTraining + quantityTest) + "\n", true);
		FileManager.write(fileResult, "Learning rate: " + learningRate, true);
		FileManager.write(fileResult, "Learning rate decrease: "
				+ learningRateDecrease, true);
		FileManager.write(fileResult, "Max error: " + maxError + "\n", true);
		FileManager.write(fileResult, "Momentum: " + momentum + "\n", true);

		Weight[] update = weights.clone();
		update = neuralNetwork.train(net, update, dataTraining.getSample(),
				dataTraining.getLabel(), parameterTraining, dataValidation,
				fileResult);

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
		long end = System.currentTimeMillis() - begin;
		System.out.println("Correct: " + numberCorrect + ", Error Rate: "
				+ errorRate);
		FileManager.write(fileResult, "Correct: " + numberCorrect
				+ ", Error Rate: " + errorRate, true);
		System.out.println("Time millisseconds " + end);
		FileManager.write(fileResult, "Time millisseconds " + end, true);

	}

	private static void testPaperParametersComDeepLearning() throws IOException {
		// Parametros fixos
		int quantityTraining = 10517;
		int quantityValidation = 429;
		int quantityTest = 2128;

		int numberAttribute = 784;
		int numberHiddenLayers = 3;
		int[] numberUnitHidden = { 10, 10, 2 };
		int numberOutput = 2;

		double momentum = 0;
		int intervalEpochPercentage = 2;
		double minLearningRate = 0.000000000001;
		Task task = Task.CLASSIFICATION;
		boolean initializeRandom = false;

		// Parametros para variar
		double[] learningRate = { 0.1 /* 0.2 *//* , 0.5, 0.9 */};
		long[] numberEpochs = { 100 /* , 10 */};
		double[] maxError = { 0 /* , 15, 12 */};
		double[] learningRateDecrease = { 0.99 /* , 1 */};

		// Carregando dados
		String pathTraining = "./data/MNIST/normal/mnist_train_0_1_binary.csv";
		String pathValidation = "./data/MNIST/normal/mnist_validation_0_1_binary.csv";
		String pathTest = "./data/MNIST/normal/mnist_test_0_1_binary.csv";
		String fileResult = "./data/MNIST/normal/resultados/Resultados_MNIST_DeepLearning_ZeroUm.doc";

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
						// net[0] = new LogisticLayer(numberUnitHidden[0]);
						net[0] = new HyperbolicTangentLayer(numberUnitHidden[0]);
						net[0].setMomentum(momentum);
						net[0].setLearningRate(learningRate[indexLearningRate]);
						// net[1] = new LogisticLayer(numberUnitHidden[1]);
						net[1] = new HyperbolicTangentLayer(numberUnitHidden[1]);
						net[1].setMomentum(momentum);
						net[1].setLearningRate(learningRate[indexLearningRate]);
						// net[2] = new LogisticLayer(numberUnitHidden[2]);
						net[2] = new HyperbolicTangentLayer(numberUnitHidden[2]);
						net[2].setMomentum(momentum);
						net[2].setLearningRate(learningRate[indexLearningRate]);
						net[3] = new SoftmaxLayer(numberOutput);
						// net[3] = new LogisticLayerMLP(numberOutput);
						net[3].setMomentum(momentum);
						net[3].setLearningRate(learningRate[indexLearningRate]);

						// weights = new Weight[4];
						// weights[0] = new Weight(numberAttribute,
						// numberUnitHidden[0]);
						// weights[1] = new Weight(numberUnitHidden[0],
						// numberUnitHidden[1]);
						// weights[2] = new Weight(numberUnitHidden[1],
						// numberUnitHidden[2]);
						// weights[3] = new Weight(numberUnitHidden[2],
						// numberOutput);

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
