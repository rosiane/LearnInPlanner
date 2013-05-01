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

public class MnistMLPTest {

	public static void main(String[] args) throws IOException {
		testSeis();
		testSete();
		testOito();
		testAll();
	}

	private static void testSeis() throws IOException {
		// Fixos
		int quantityTraining = 34943;
		int quantityValidation = 1393;
		int quantityTest = 7008;

		int numberAttribute = 784;

		int numberHiddenLayers = 1;
		int numberOutput = 7;
		double maxError = 0;
		double learningRateDecrease = 1;
		double minLearningRate = 0.000000001;
		boolean initializeRandom = true;
		double momentum = 0;
		Task task = Task.CLASSIFICATION;

		// Variável
		int[][] numberUnitHidden = { { 50 }, { 100 }, { 150 }, { 200 } };
		double[] learningRate = { 0.1, 0.05, 0.005, 0.00005 };
		int[] intervalEpochPercentage = { 2, 10 };
		long[] numberEpochs = { 100, 200, 500 };

		// Arquivos
		String pathTraining = "./data/MNIST/normal/mnist_train_0_1_2_3_4_5_6_binary.csv";
		String pathValidation = "./data/MNIST/normal/mnist_validation_0_1_2_3_4_5_6_binary.csv";
		String pathTest = "./data/MNIST/normal/mnist_test_0_1_2_3_4_5_6_binary.csv";
		String fileResult = "./data/MNIST/normal/resultados/Resultados_MNIST_WithoutDeepLearning_ZeroUmDoisTresQuatroCincoSeis.doc";

		test(quantityTraining, quantityValidation, quantityTest,
				numberAttribute, numberHiddenLayers, numberOutput, maxError,
				learningRateDecrease, minLearningRate, initializeRandom,
				momentum, task, numberUnitHidden, learningRate,
				intervalEpochPercentage, numberEpochs, pathTraining,
				pathValidation, pathTest, fileResult);
	}

	private static void testSete() throws IOException {
		// Fixos
		int quantityTraining = 40134;
		int quantityValidation = 1602;
		int quantityTest = 8078;

		int numberAttribute = 784;

		int numberHiddenLayers = 1;
		int numberOutput = 8;
		double maxError = 0;
		double learningRateDecrease = 1;
		double minLearningRate = 0.000000001;
		boolean initializeRandom = true;
		double momentum = 0;
		Task task = Task.CLASSIFICATION;

		// Variável
		int[][] numberUnitHidden = { { 50 }, { 100 }, { 150 }, { 200 } };
		double[] learningRate = { 0.1, 0.05, 0.005, 0.00005 };
		int[] intervalEpochPercentage = { 2, 10 };
		long[] numberEpochs = { 100, 200, 500 };

		// Arquivos
		String pathTraining = "./data/MNIST/normal/mnist_train_0_1_2_3_4_5_6_7_binary.csv";
		String pathValidation = "./data/MNIST/normal/mnist_validation_0_1_2_3_4_5_6_7_binary.csv";
		String pathTest = "./data/MNIST/normal/mnist_test_0_1_2_3_4_5_6_7_binary.csv";
		String fileResult = "./data/MNIST/normal/resultados/Resultados_MNIST_WithoutDeepLearning_ZeroUmDoisTresQuatroCincoSeisSete.doc";

		test(quantityTraining, quantityValidation, quantityTest,
				numberAttribute, numberHiddenLayers, numberOutput, maxError,
				learningRateDecrease, minLearningRate, initializeRandom,
				momentum, task, numberUnitHidden, learningRate,
				intervalEpochPercentage, numberEpochs, pathTraining,
				pathValidation, pathTest, fileResult);
	}

	private static void testOito() throws IOException {
		// Fixos
		int quantityTraining = 45050;
		int quantityValidation = 1785;
		int quantityTest = 9022;

		int numberAttribute = 784;

		int numberHiddenLayers = 1;
		int numberOutput = 9;
		double maxError = 0;
		double learningRateDecrease = 1;
		double minLearningRate = 0.000000001;
		boolean initializeRandom = true;
		double momentum = 0;
		Task task = Task.CLASSIFICATION;

		// Variável
		int[][] numberUnitHidden = { { 50 }, { 100 }, { 150 }, { 200 } };
		double[] learningRate = { 0.1, 0.05, 0.005, 0.00005 };
		int[] intervalEpochPercentage = { 2, 10 };
		long[] numberEpochs = { 100, 200, 500 };

		// Arquivos
		String pathTraining = "./data/MNIST/normal/mnist_train_0_1_2_3_4_5_6_7_8_binary.csv";
		String pathValidation = "./data/MNIST/normal/mnist_validation_0_1_2_3_4_5_6_7_8_binary.csv";
		String pathTest = "./data/MNIST/normal/mnist_test_0_1_2_3_4_5_6_7_8_binary.csv";
		String fileResult = "./data/MNIST/normal/resultados/Resultados_MNIST_WithoutDeepLearning_ZeroUmDoisTresQuatroCincoSeisSeteOito.doc";

		test(quantityTraining, quantityValidation, quantityTest,
				numberAttribute, numberHiddenLayers, numberOutput, maxError,
				learningRateDecrease, minLearningRate, initializeRandom,
				momentum, task, numberUnitHidden, learningRate,
				intervalEpochPercentage, numberEpochs, pathTraining,
				pathValidation, pathTest, fileResult);
	}

	private static void testAll() throws IOException {
		// Fixos
		int quantityTraining = 50000;
		int quantityValidation = 2000;
		int quantityTest = 10000;

		int numberAttribute = 784;

		int numberHiddenLayers = 1;
		int numberOutput = 10;
		double maxError = 0;
		double learningRateDecrease = 1;
		double minLearningRate = 0.000000001;
		boolean initializeRandom = true;
		double momentum = 0;
		Task task = Task.CLASSIFICATION;

		// Variável
		int[][] numberUnitHidden = { { 50 }, { 100 }, { 150 }, { 200 } };
		double[] learningRate = { 0.1, 0.05, 0.005, 0.00005 };
		int[] intervalEpochPercentage = { 2, 10 };
		long[] numberEpochs = { 100, 200, 500 };

		// Arquivos
		String pathTraining = "./data/MNIST/normal/mnist_train_paper_binary.csv";
		String pathValidation = "./data/MNIST/normal/mnist_validation_binary.csv";
		String pathTest = "./data/MNIST/normal/mnist_test_binary.csv";
		String fileResult = "./data/MNIST/normal/resultados/Resultados_MNIST_WithoutDeepLearning_All.doc";

		test(quantityTraining, quantityValidation, quantityTest,
				numberAttribute, numberHiddenLayers, numberOutput, maxError,
				learningRateDecrease, minLearningRate, initializeRandom,
				momentum, task, numberUnitHidden, learningRate,
				intervalEpochPercentage, numberEpochs, pathTraining,
				pathValidation, pathTest, fileResult);
	}

	private static void test(int quantityTraining, int quantityValidation,
			int quantityTest, int numberAttribute, int numberHiddenLayers,
			int numberOutput, double maxError, double learningRateDecrease,
			double minLearningRate, boolean initializeRandom, double momentum,
			Task task, int[][] numberUnitHidden, double[] learningRate,
			int[] intervalEpochPercentage, long[] numberEpochs,
			String pathTraining, String pathValidation, String pathTest,
			String fileResult) throws IOException {
		// Dados
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

		NeuralNetworkIF neuralNetwork = new MLP();
		Layer[] net = new Layer[2];
		Weight[] weights = new Weight[2];
		ParameterTraining parameterTraining;
		long begin;
		Weight[] update;
		double numberCorrect;
		double[] result;
		double errorRate;
		long end;

		for (int indexNumberUnitHidden = 0; indexNumberUnitHidden < numberUnitHidden.length; indexNumberUnitHidden++) {
			for (int indexLearningRate = 0; indexLearningRate < learningRate.length; indexLearningRate++) {
				for (int indexIntervalEpochPercentage = 0; indexIntervalEpochPercentage < intervalEpochPercentage.length; indexIntervalEpochPercentage++) {
					for (int indexNumberEpochs = 0; indexNumberEpochs < numberEpochs.length; indexNumberEpochs++) {
						net[0] = new HyperbolicTangentLayer(
								numberUnitHidden[indexNumberUnitHidden][0]);
						net[0].setMomentum(momentum);
						net[0].setLearningRate(learningRate[indexLearningRate]);
						net[1] = new SoftmaxLayer(numberOutput);
						net[1].setMomentum(momentum);
						net[1].setLearningRate(learningRate[indexLearningRate]);

						weights[0] = new Weight(numberAttribute,
								numberUnitHidden[indexNumberUnitHidden][0]);
						weights[1] = new Weight(
								numberUnitHidden[indexNumberUnitHidden][0],
								numberOutput);

						parameterTraining = new ParameterTraining();
						parameterTraining
								.setNumberEpochs(numberEpochs[indexNumberEpochs]);
						parameterTraining.setMaxError(maxError);
						parameterTraining.setTask(task.getValue());
						parameterTraining
								.setLearningRateDecrease(learningRateDecrease);
						parameterTraining.setMinLearningRate(minLearningRate);
						parameterTraining
								.setWeightsInitializationRandom(initializeRandom);
						parameterTraining
								.setIntervalEpochPercentage(intervalEpochPercentage[indexIntervalEpochPercentage]);
						parameterTraining.setUpdateBatch(false);
						parameterTraining.setNormalizeWeights(true);
						parameterTraining.setValidation(true);

						begin = System.currentTimeMillis();
						System.out.println("MLP parameters:");
						System.out.println("Number of attributes: "
								+ numberAttribute);
						System.out.println("Number of hidden layers: "
								+ numberHiddenLayers);
						System.out.println("Number of hidden units: "
								+ numberUnitHidden[indexNumberUnitHidden][0]);
						System.out.println("Number of examples: "
								+ (quantityTraining + quantityTest) + "\n");
						System.out.println("Learning rate: "
								+ learningRate[indexLearningRate]);
						System.out.println("Learning rate decrease: "
								+ learningRateDecrease);
						System.out.println("Number Epochs: "
								+ numberEpochs[indexNumberEpochs]);
						System.out.println("Max error: " + maxError);
						System.out.println("Momentum: " + momentum);
						System.out
								.println("IntervalEpochPercentage: "
										+ intervalEpochPercentage[indexIntervalEpochPercentage]
										+ "\n");

						FileManager.write(fileResult, "MLP parameters:", true);
						FileManager.write(fileResult, "Number of attributes: "
								+ numberAttribute, true);
						FileManager.write(fileResult,
								"Number of hidden layers: "
										+ numberHiddenLayers, true);
						FileManager
								.write(fileResult,
										"Number of hidden units: "
												+ numberUnitHidden[indexNumberUnitHidden][0],
										true);
						FileManager.write(fileResult, "Number of examples: "
								+ (quantityTraining + quantityTest) + "\n",
								true);
						FileManager.write(fileResult, "Learning rate: "
								+ learningRate[indexLearningRate], true);
						FileManager.write(fileResult,
								"Learning rate decrease: "
										+ learningRateDecrease, true);
						FileManager.write(fileResult, "Number Epochs: "
								+ numberEpochs[indexNumberEpochs], true);
						FileManager.write(fileResult, "Max error: " + maxError,
								true);
						FileManager.write(fileResult, "Momentum: " + momentum,
								true);
						FileManager
								.write(fileResult,
										"IntervalEpochPercentage: "
												+ intervalEpochPercentage[indexIntervalEpochPercentage]
												+ "\n", true);

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
						end = System.currentTimeMillis() - begin;
						System.out.println("Correct: " + numberCorrect
								+ ", Error Rate: " + errorRate);
						FileManager.write(fileResult, "Correct: "
								+ numberCorrect + ", Error Rate: " + errorRate,
								true);
						System.out.println("Time millisseconds " + end);
						FileManager.write(fileResult, "Time millisseconds "
								+ end, true);
					}
				}
			}
		}
	}

}
