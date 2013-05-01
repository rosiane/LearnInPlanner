package test.main;

import java.io.IOException;

import neural.network.enums.Task;
import neural.network.impl.MLP;
import neural.network.impl.ParameterTraining;
import neural.network.interfaces.NeuralNetworkIF;
import neural.network.test.Crossvalidation;
import neural.network.util.Weight;

import com.syvys.jaRBM.Layers.Layer;
import com.syvys.jaRBM.Layers.LogisticLayer;
import common.Data;
import common.preprocessor.file.FileManager;

import deeplearning.DeepLearning;
import deeplearning.ParameterTrainingCRBM;

public class DeepLearningTest {

	public static void main(String[] args) {
		try {
			testCrossvalidation();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void testCrossvalidation() throws IOException {
		// int numberInput = 100;
		// int numberInput = 6500;
		// int numberInput = 30000;
		// int numberInput = 2470;
		// int numberInput = 2530;
		// int numberInput = 2560;

		// Troquei os parâmetros para resolver o problema do meu conjunto que
		// não está funcionando o crossvalidation. Separei o numberInput em
		// input de treinamento e de teste.
		int quantityTraining = 5851;
		int quantityTest = 649;

		int numberAttribute = 28 * 28;
		// int numberAttribute = 30;
		// int numberAttribute = 4;
		int[] numberUnitHidden = {10};
		int numberHiddenLayers = 3;
		int numberOutput = 2;
		// int quantityTraining = 513;
		// int quantityTest = 56;

		double momentum = 0.1;
		double learningRate = 0.2;
		int intervalEpochPercentage = 2;

		long numberEpochs = 50;
		double maxError = 3;
		double learningRateDecrease = 0.99;
		double minLearningRate = 0.01;
		boolean initializeRandom = false;
		Task task = Task.CLASSIFICATION;

		// String prefixSampleTraining =
		
		// "./data/1_2_nopattern/iris_binaryTraining";
		// String prefixSampleTest = "./data/1_2_nopattern/iris_binaryTest";
		// String prefixSampleTraining =
		// "./data/MNIST/MNIST_MLP_Rotation_12000_exemplos_10_digitos_train";
		// String prefixSampleTest =
		// "./data/MNIST/MNIST_MLP_Rotation_12000_exemplos_10_digitos_test";
		// String prefixSampleTraining =
		// "./data/MNIST/MNIST_MLP_Rotation_2560_exemplos_2_digitos_1_0_TRAIN";
		// String prefixSampleTest =
		// "./data/MNIST/MNIST_MLP_Rotation_2560_exemplos_2_digitos_1_0_TEST";
		// String prefixSampleTraining =
		// "./data/MNIST/MNIST_MLP_Background-Random_2470_imagens_2_digitos_1_0_TRAIN";
		// String prefixSampleTest =
		// "./data/MNIST/MNIST_MLP_Background-Random_2470_imagens_2_digitos_1_0_TEST";
		// String prefixSampleTraining =
		// "./data/MNIST/MNIST_MLP_Background-Images_2530_imagens_2_digitos_1_0_TRAIN";
		// String prefixSampleTest =
		// "./data/MNIST/MNIST_MLP_Background-Images_2530_imagens_2_digitos_1_0_TEST";
		// String prefixSampleTraining =
		// "./data/cancer/cancer_binary_patternizedTraining";
		// String prefixSampleTest =
		// "./data/cancer/cancer_binary_patternizedTest";
		String prefixSampleTraining = "./data/MNIST/MNIST_binary_MLP_2_exemplos_1_e_0_6500_exemplosTraining";
		String prefixSampleTest = "./data/MNIST/MNIST_binary_MLP_2_exemplos_1_e_0_6500_exemplosTest";

		// String prefixSampleTraining =
		// "./data/MNIST/MNIST_MLP_10_digitos_30000_exemplos_train_";
		// String prefixSampleTest =
		// "./data/MNIST/MNIST_MLP_10_digitos_30000_exemplos_test_";
		// String prefixSampleTraining =
		// "./data/MNIST/MNIST_MLP_2_exemplos_1_e_0_TRAIN";
		// String prefixSampleTest =
		// "./data/MNIST/MNIST_MLP_2_exemplos_1_e_0_TEST";

		int k = 10;

		NeuralNetworkIF neuralNetwork = new MLP();

		Layer[] net = new Layer[4];
		net[0] = new LogisticLayer(numberUnitHidden[0]);
		net[0].setMomentum(momentum);
		net[0].setLearningRate(learningRate);
		net[1] = new LogisticLayer(numberUnitHidden[0]);
		net[1].setMomentum(momentum);
		net[1].setLearningRate(learningRate);
		net[2] = new LogisticLayer(numberUnitHidden[0]);
		net[2].setMomentum(momentum);
		net[2].setLearningRate(learningRate);
		net[3] = new LogisticLayer(numberOutput);
		net[3].setMomentum(momentum);
		net[3].setLearningRate(learningRate);

		// Layer[] net = new Layer[2];
		// net[0] = new LogisticLayerMLP(numberUnitHidden);
		// net[0].setMomentum(momentum);
		// net[0].setLearningRate(learningRate);
		// net[1] = new LogisticLayerMLP(numberOutput);
		// net[1].setMomentum(momentum);
		// net[1].setLearningRate(learningRate);

		// Weight[] weights = new Weight[2];
		// weights[0] = new Weight(numberAttribute, numberUnitHidden);
		// weights[1] = new Weight(numberUnitHidden, numberOutput);

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
		Data dataTraining = new Data();
		double[][] sample = new double[quantityTraining][numberAttribute];
		dataTraining.setSample(sample);
		double[][] label = new double[quantityTraining][numberAttribute];
		dataTraining.setLabel(label);

		// CRBM training parameters.
		ParameterTrainingCRBM parameterTrainingCRBM = new ParameterTrainingCRBM();
		parameterTrainingCRBM.setLearning_rate_weights(1.0);
		parameterTrainingCRBM.setLearning_rate_aj(1.0);
		parameterTrainingCRBM.setTheta_low(-1.0);
		parameterTrainingCRBM.setTheta_high(1.0);
		parameterTrainingCRBM.setSigma(0.2);

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

		int numberEpochsCRBM = 10;

		// A list of 'k' lists of weight matrices.
		Weight[][] weight_folds = new Weight[k][numberUnitHidden[0] + 1];

		for (int fold = 0; fold < k; fold++) {
			System.out.println("CRBM pre-training: fold " + (fold + 1) + ":");

			dataTraining = FileManager.read(prefixSampleTraining + (fold + 1)
					+ ".csv", dataTraining);
			DeepLearning deep_learning = new DeepLearning(numberAttribute,
					numberHiddenLayers, numberUnitHidden, numberOutput,
					numberEpochsCRBM, parameterTrainingCRBM);
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

		// Troquei os parâmetros para resolver o problema do meu conjunto que
		// não está funcionando o crossvalidation. Separei o numberInput em
		// input de treinamento e de teste.
		Crossvalidation crossvalidation = new Crossvalidation();
		crossvalidation.run(neuralNetwork, net, weight_folds,
				prefixSampleTraining, prefixSampleTest, numberAttribute,
				numberOutput, parameterTraining, k, quantityTraining,
				quantityTest, null);
	}

}
