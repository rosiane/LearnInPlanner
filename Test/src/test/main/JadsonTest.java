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
import neural.network.util.Weight;
import preprocessor.file.FileManager;
import sun.nio.ch.EPollSelectorProvider;

import com.syvys.jaRBM.Layers.Layer;

public class JadsonTest {

	public static void main(String[] args) {
		// prepareLeaveOneOut();
		// numberToBinary();
		try {
			test();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void numberToBinary() {
		Map<Integer, String> map = new HashMap<>();
		map.put(0, "1 0");
		map.put(1, "0 1");
		String separator = "	";
		try {
			for (int index = 1; index <= 70; index++) {
				FileManager.convertLabelNumberToBinary(
						"./data/jadson/spca-npcs-10-gamma-0.10_Test" + index
								+ ".csv",
						"./data/jadson/spca-npcs-10-gamma-0.10_binary_Test"
								+ index + ".csv", map, separator);
				FileManager.convertLabelNumberToBinary(
						"./data/jadson/spca-npcs-10-gamma-0.10_Training"
								+ index + ".csv",
						"./data/jadson/spca-npcs-10-gamma-0.10_binary_Training"
								+ index + ".csv", map, separator);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void prepareLeaveOneOut() {
		String pathInputFile = "./data/jadson/spca-npcs-10-gamma-0.10.txt";
		String prefixOutputFile = "./data/jadson/spca-npcs-10-gamma-0.10_";
		List<Double> classes = new ArrayList<>();
		classes.add(0.0);
		classes.add(1.0);
		String separator = "	";
		try {
			FileManager.prepareLeaveOneOut(pathInputFile, prefixOutputFile, 70,
					classes, separator);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void test() throws IOException {
		// Troquei os parâmetros para resolver o problema do meu conjunto que
		// não está funcionando o crossvalidation. Separei o numberInput em
		// input de treinamento e de teste.
		int quantityTraining = 69;
		int quantityTest = 1;

		int numberAttribute = 10;
		int numberUnitHidden = 3;
		int numberHiddenLayers = 1;
		int numberOutput = 2;

		double momentum = 0;
		double learningRate = 0.09;
		int intervalEpochPercentage = 4;

		long numberEpochs = 1000;
		double maxError = 0;
		double learningRateDecrease = 1;
		double minLearningRate = 0.01;
		boolean initializeRandom = true;
		Task task = Task.CLASSIFICATION;

		String prefixSampleTraining = "./data/jadson/spca-npcs-10-gamma-0.10_binary_Training";
		String prefixSampleTest = "./data/jadson/spca-npcs-10-gamma-0.10_binary_Test";

		int k = 70;

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
		System.out.println("Number of epochs: " + numberEpochs + "\n");
		System.out.println("Learning rate: " + learningRate);
		System.out.println("Learning rate decrease: " + learningRateDecrease);
		System.out.println("Max error: " + maxError + "\n");

		// -- DEEP LEARNING
		// Data dataTraining = new Data();
		// double[][] sample = new double[quantityTraining][numberAttribute];
		// dataTraining.setSample(sample);
		// double[][] label = new double[quantityTraining][numberAttribute];
		// dataTraining.setLabel(label);

		// CRBM training parameters.
		// ParameterTrainingCRBM parameterTrainingCRBM = new
		// ParameterTrainingCRBM();
		// parameterTrainingCRBM.setLearning_rate_weights(1.0);
		// parameterTrainingCRBM.setLearning_rate_aj(1.0);
		// parameterTrainingCRBM.setTheta_low(-1.0);
		// parameterTrainingCRBM.setTheta_high(1.0);
		// parameterTrainingCRBM.setSigma(0.2);

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

		// int numberEpochsCRBM = 10;

		// A list of 'k' lists of weight matrices.
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
		crossvalidation.run(neuralNetwork, net, weights, prefixSampleTraining,
				prefixSampleTest, numberAttribute, numberOutput,
				parameterTraining, k, quantityTraining, quantityTest);
	}
}
