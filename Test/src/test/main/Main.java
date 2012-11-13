package test.main;

import java.io.IOException;

import neural.network.enums.Task;
import neural.network.impl.MLP;
import neural.network.impl.ParameterTraining;
import neural.network.interfaces.NeuralNetworkIF;
import neural.network.test.Crossvalidation;
import neural.network.util.LogisticLayerMLP;
import neural.network.util.Weight;
import preprocessor.file.FileManager;

import com.syvys.jaRBM.Layers.Layer;
import com.syvys.jaRBM.Layers.LogisticLayer;
import common.Data;
import common.MatrixHandler;

import deeplearning.DeepLearning;

public class Main {

	public static void main(String[] args) {
		try {
			// testCrossvalidation();
			// testMNIST();
			testDeepLearning();
//			testNet();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// debugMLP();
	}

	public static void debugMLP() {
		int numberInput = 1;
		int numberAttribute = 4;
		int numberOutput = 3;
		// String pathFile = "./data/iris4crossvalidation_1_2.csv";
		double momentum = 0;
		double learningRate = 0.2;
		int numberUnitHidden = 9;
		long numberEpochs = 1;
		double maxError = 2;
		double learningRateDecrease = 0.45;
		double minLearningRate = 0.0002;
		boolean initializeRandom = false;
		Task task = Task.CLASSIFICATION;

		Data data = new Data();
		double[][] sample = new double[numberInput][numberAttribute];
		sample[0][0] = 5.4;
		sample[0][1] = 3.4;
		sample[0][2] = 1.7;
		sample[0][3] = 0.2;
		data.setSample(sample);
		double[][] label = new double[numberInput][numberOutput];
		label[0][0] = 1;
		label[0][1] = 0;
		label[0][2] = 0;
		data.setLabel(label);
		// Data randomData = null;
		// try {
		// data = FileManager.read(pathFile, data);
		// randomData = MatrixHandler.randomize(data.getSample(),
		// data.getLabel());
		// } catch (IOException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		ParameterTraining parameterTraining = new ParameterTraining();
		parameterTraining.setNumberEpochs(numberEpochs);
		parameterTraining.setMaxError(maxError);
		parameterTraining.setTask(task.getValue());
		parameterTraining.setLearningRateDecrease(learningRateDecrease);
		parameterTraining.setMinLearningRate(minLearningRate);
		parameterTraining.setWeightsInitializationRandom(initializeRandom);

		Layer[] net = new Layer[2];
		net[0] = new LogisticLayerMLP(numberUnitHidden);
		net[0].setMomentum(momentum);
		net[0].setLearningRate(learningRate);
		net[1] = new LogisticLayerMLP(numberOutput);
		net[1].setMomentum(momentum);
		net[1].setLearningRate(learningRate);

		Weight[] weights = new Weight[2];
		weights[0] = new Weight(numberAttribute, numberUnitHidden);
		double[][] weightsMatrix1 = new double[4][9];
		weightsMatrix1[0][0] = -0.3591481678994284;
		weightsMatrix1[0][1] = -0.24163894383084394;
		weightsMatrix1[0][2] = 0.5816945090062686;
		weightsMatrix1[0][3] = -0.4746693518741911;
		weightsMatrix1[0][4] = -0.26869842521972753;
		weightsMatrix1[0][5] = 0.03168821769994645;
		weightsMatrix1[0][6] = 0.32278534933025416;
		weightsMatrix1[0][7] = -0.2663891877636717;
		weightsMatrix1[0][8] = 0.12752819022930129;

		weightsMatrix1[1][0] = 0.4043563718718255;
		weightsMatrix1[1][1] = -0.7598607289554964;
		weightsMatrix1[1][2] = -0.6464171188955321;
		weightsMatrix1[1][3] = -0.03388048997043058;
		weightsMatrix1[1][4] = -0.7002517636012813;
		weightsMatrix1[1][5] = -0.3925064046331088;
		weightsMatrix1[1][6] = 0.2633646080856915;
		weightsMatrix1[1][7] = -0.7371252941851032;
		weightsMatrix1[1][8] = -0.13942354128864465;

		weightsMatrix1[2][0] = 0.9278986996258596;
		weightsMatrix1[2][1] = -0.9812790563755334;
		weightsMatrix1[2][2] = 0.6157183287906027;
		weightsMatrix1[2][3] = -0.910148532149281;
		weightsMatrix1[2][4] = 0.48755062690085094;
		weightsMatrix1[2][5] = -0.30744665015113504;
		weightsMatrix1[2][6] = 0.31863463079494947;
		weightsMatrix1[2][7] = -0.6391077555794784;
		weightsMatrix1[2][8] = -0.5898474596530936;

		weightsMatrix1[3][0] = -0.37759661185601656;
		weightsMatrix1[3][1] = -0.666295090782856;
		weightsMatrix1[3][2] = -0.22870443129829998;
		weightsMatrix1[3][3] = 0.8590817556023764;
		weightsMatrix1[3][4] = -0.9386071319705738;
		weightsMatrix1[3][5] = 0.9218451930495024;
		weightsMatrix1[3][6] = 0.934917637049383;
		weightsMatrix1[3][7] = 0.5444069681650237;
		weightsMatrix1[3][8] = -0.7465862720269416;
		weights[0].setWeights(weightsMatrix1);

		weights[1] = new Weight(numberUnitHidden, numberOutput);
		double[][] weightsMatrix2 = new double[9][3];
		weightsMatrix2[0][0] = 0.8988594191493309;
		weightsMatrix2[0][1] = 0.23756525953055285;
		weightsMatrix2[0][2] = -0.9859363305617821;

		weightsMatrix2[1][0] = -0.7087286556163417;
		weightsMatrix2[1][1] = 0.4619361407804028;
		weightsMatrix2[1][2] = -0.6737681739871553;

		weightsMatrix2[2][0] = 0.34326337717816924;
		weightsMatrix2[2][1] = 0.2668750445320045;
		weightsMatrix2[2][2] = 0.32273453985637723;

		weightsMatrix2[3][0] = -0.3416440732334205;
		weightsMatrix2[3][1] = 0.7834764149091;
		weightsMatrix2[3][2] = -0.31453886009804566;

		weightsMatrix2[4][0] = 0.48672036121759454;
		weightsMatrix2[4][1] = -0.6932800797315308;
		weightsMatrix2[4][2] = -0.30790550882193735;

		weightsMatrix2[5][0] = -0.1087761817620192;
		weightsMatrix2[5][1] = 0.4344275703892255;
		weightsMatrix2[5][2] = 0.8972468024819937;

		weightsMatrix2[6][0] = -0.36385559120481314;
		weightsMatrix2[6][1] = -0.6603799996900772;
		weightsMatrix2[6][2] = -0.3111617523143564;

		weightsMatrix2[7][0] = 0.551094243064369;
		weightsMatrix2[7][1] = 0.948190439448205;
		weightsMatrix2[7][2] = 0.9520048274850526;

		weightsMatrix2[8][0] = 0.7940878055574592;
		weightsMatrix2[8][1] = -0.9909141931938015;
		weightsMatrix2[8][2] = 0.20982793551334766;
		weights[1].setWeights(weightsMatrix2);

		NeuralNetworkIF neuralNetwork = new MLP();
		neuralNetwork.train(net, weights, data.getSample(), data.getLabel(),
				parameterTraining, null);
	}

	public static void testNet() {
		int numberInput = 100;
		int numberAttribute = 4;
		int numberOutput = 2;
		String pathFile = "./data/1_2_nopattern/iris_binaryTraining1.csv";
		double momentum = 0;
		double learningRate = 0.5;
		int numberUnitHidden = 5;
		long numberEpochs = 50;
		double maxError = 5;
		double learningRateDecrease = 0.99;
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
		parameterTraining.setNormalizeWeights(true);
		parameterTraining.setValidation(false);
		parameterTraining.setUpdateBatch(false);
		parameterTraining.setIntervalEpochPercentage(1);
		
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
				randomData.getLabel(), parameterTraining, null);
	}

	public static void testCrossvalidation() throws IOException {
		int numberInput = 100;

		int numberAttribute = 4;
		int numberUnitHidden = 9;
		int numberOutput = 2;

		double momentum = 0;
		double learningRate = 0.2;
		int intervalEpochPercentage = 2;

		long numberEpochs = 1000;
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
		net[0] = new LogisticLayerMLP(numberUnitHidden);
		net[0].setMomentum(momentum);
		net[0].setLearningRate(learningRate);
		net[1] = new LogisticLayerMLP(numberOutput);
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
		parameterTraining.setUpdateBatch(false);
		parameterTraining.setNormalizeWeights(false);
		parameterTraining.setValidation(false);

		Crossvalidation crossvalidation = new Crossvalidation();
		crossvalidation.run(neuralNetwork, net, weights, prefixSampleTraining,
				prefixSampleTest, numberInput, numberAttribute, numberOutput,
				parameterTraining, k);
	}

	public static void testMNIST() throws IOException {
		int numberInput = 6000;
		// int numberInput = 6500;

		int numberAttribute = 28 * 28;
		int numberUnitHidden = 10;
		int numberOutput = 2;

		double momentum = 0.1;
		double learningRate = 0.4;
		int intervalEpochPercentage = 2;

		long numberEpochs = 100;
		double maxError = 5;
		double learningRateDecrease = 0.99;
		double minLearningRate = 0.1;
		boolean initializeRandom = true;
		Task task = Task.CLASSIFICATION;

		// String prefixSampleTraining =
		// "./data/1_2_nopattern/iris_binaryTraining";
		// String prefixSampleTest = "./data/1_2_nopattern/iris_binaryTest";
		String prefixSampleTraining = "./data/MNIST/MNIST_MLP_2_exemplos_TRAIN";
		String prefixSampleTest = "./data/MNIST/MNIST_MLP_2_exemplos_TEST";

		// String prefixSampleTraining =
		// "./data/MNIST/MNIST_MLP_10_digitos_train_";
		// String prefixSampleTest = "./data/MNIST/MNIST_MLP_10_digitos_test_";

		int k = 10;

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

		Crossvalidation crossvalidation = new Crossvalidation();
		crossvalidation.run(neuralNetwork, net, weights, prefixSampleTraining,
				prefixSampleTest, numberInput, numberAttribute, numberOutput,
				parameterTraining, k);
	}

	public static void testDeepLearning() throws IOException {
//		int numberInput = 6500;
//		 int numberInput = 100;
		 int numberInput = 569;

		// int numberInput = 30000;

		 int numberAttribute = 30;
//		int numberAttribute = 28 * 28;
//		 int numberAttribute = 4;
		int numberUnitHidden = 9;
		 int numberHiddenLayers = 3;
//		int numberHiddenLayers = 1;
		int numberOutput = 2;

		double momentum = 0;
		double learningRate = 0.4;
		int intervalEpochPercentage = 2;

		long numberEpochs = 100;
		double maxError = 5;
		double learningRateDecrease = 0.99;
		double minLearningRate = 0.01;
		boolean initializeRandom = true;
		Task task = Task.CLASSIFICATION;

//		 String prefixSampleTraining =
//		 "./data/1_2_nopattern/iris_binaryTraining";
//		 String prefixSampleTest = "./data/1_2_nopattern/iris_binaryTest";
//		String prefixSampleTraining = "./data/MNIST/MNIST_MLP_2_exemplos_1_e_0_TRAIN";
//		String prefixSampleTest = "./data/MNIST/MNIST_MLP_2_exemplos_1_e_0_TEST";

		 String prefixSampleTraining =
		 "./data/cancer/cancer_binary_patternizedTraining";
		 String prefixSampleTest =
		 "./data/cancer/cancer_binary_patternizedTest";

		// String prefixSampleTraining =
		// "./data/MNIST/MNIST_MLP_10_digitos_30000_exemplos_train_";
		// String prefixSampleTest =
		// "./data/MNIST/MNIST_MLP_10_digitos_30000_exemplos_test_";

		int k = 10;

		NeuralNetworkIF neuralNetwork = new MLP();

//		Layer[] net = new Layer[2];
		// Layer[] net = new Layer[4];
//		net[0] = new LogisticLayerMLP(numberUnitHidden);
//		net[0].setMomentum(momentum);
//		net[0].setLearningRate(learningRate);
//		net[1] = new LogisticLayerMLP(numberOutput);
//		net[1].setMomentum(momentum);
//		net[1].setLearningRate(learningRate);

		 Layer[] net = new Layer[4];
		 net[0] = new LogisticLayerMLP(numberUnitHidden);
		 net[0].setMomentum(momentum);
		 net[0].setLearningRate(learningRate);
		 net[1] = new LogisticLayerMLP(numberUnitHidden);
		 net[1].setMomentum(momentum);
		 net[1].setLearningRate(learningRate);
		 net[2] = new LogisticLayerMLP(numberUnitHidden);
		 net[2].setMomentum(momentum);
		 net[2].setLearningRate(learningRate);
		 net[3] = new LogisticLayerMLP(numberOutput);
		 net[3].setMomentum(momentum);
		 net[3].setLearningRate(learningRate);
//		 net[4] = new LogisticLayerMLP(numberOutput);
//		 net[4].setMomentum(momentum);
//		 net[4].setLearningRate(learningRate);

//		 Weight[] weightsTest = new Weight[2];
//		 weightsTest[0] = new Weight(numberAttribute, numberUnitHidden);
//		 weightsTest[1] = new Weight(numberUnitHidden, numberOutput);

//		 Weight[] weights = new Weight[5];
//		 weights[0] = new Weight(numberAttribute, numberUnitHidden);
//		 weights[1] = new Weight(numberUnitHidden, numberUnitHidden);
//		 weights[2] = new Weight(numberUnitHidden, numberUnitHidden);
//		 weights[3] = new Weight(numberUnitHidden, numberUnitHidden);
//		 weights[4] = new Weight(numberUnitHidden, numberOutput);

		// -- DEEP LEARNING
		Data dataTraining = new Data();
		double[][] sample = new double[ 513 /* 90 *//*5850*/][numberAttribute];
		dataTraining.setSample(sample);
		double[][] label = new double[ 513  /*90*/  /*5850*/][numberAttribute];
		dataTraining.setLabel(label);

		int numberEpochsCRBM = 10;

		// A list of 'k' lists of weight matrices
		Weight[][] weight_folds = new Weight[k][numberHiddenLayers + 1];

		for (int fold = 0; fold < k; fold++) {
			System.out.println("CRBM pre-training: fold " + (fold + 1) + ":");

			dataTraining = FileManager.read(prefixSampleTraining + (fold + 1)
					+ ".csv", dataTraining);
			dataTraining = MatrixHandler.randomize(dataTraining.getSample(),
					dataTraining.getLabel());
			DeepLearning deep_learning = new DeepLearning(numberAttribute,
					numberHiddenLayers, numberUnitHidden, numberOutput,
					numberEpochsCRBM);
			Weight[] weights = deep_learning.runDeepLearning(dataTraining
					.getSample());
			weight_folds[fold] = weights;
		}

		System.out.println("- MLP -");
		// -- DEEP LEARNING

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

		Crossvalidation crossvalidation = new Crossvalidation();
		crossvalidation.run(neuralNetwork, net, weight_folds,
				prefixSampleTraining, prefixSampleTest, numberInput,
				numberAttribute, numberOutput, parameterTraining, k);
	}

}
