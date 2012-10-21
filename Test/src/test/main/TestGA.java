package test.main;

import java.io.IOException;
import java.util.List;

import common.MatrixHandler;

import neural.network.enums.Task;
import neural.network.impl.ParameterTraining;

import preprocessor.file.ReaderFeatureBinaryFile;
import feature.selector.GeneticAlgorithm;
import feature.selector.ga.Chromosome;
import feature.selector.ga.ParameterGA;
import feature.selector.ga.mlp.FitnessFunctionMLP;
import feature.selector.ga.util.RandomUtils;

public class TestGA {

	public static void main(String[] args) {
		try {
			test();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void test() throws IOException {
		int numberInputTraining = 467;
		int numberInputTest = 51;
		int numberInputValidation = 51;
		int numberOutput = 2;

		// String pathTraining = "./data/cancer/cancer_binaryTraining1.csv";
		// String pathTest = "./data/cancer/cancer_binaryTest1.csv";
		// String pathValidation = "./data/cancer/cancer_binary_validation.csv";
		String pathTraining = "./data/cancer/cancer_binary_patternizedTraining1.csv";
		String pathTest = "./data/cancer/cancer_binary_patternizedTest1.csv";
		String pathValidation = "./data/cancer/cancer_binary_patternized_validation.csv";

		int numberIndividualInitial = 20;
		int numberGenes = 30;

		// Parameters GA
		int numberFathers = 10;
		long numberGeneration = 50;
		int numberIndividualCrossing = 4;
		int numberIndividualMutation = 2;

		// Parameters MLP
		long numberEpochs = 100;
		double maxErrorMLPTraining = 6;
		double learningRateDecrease = 0.99;
		double minLearningRate = 0.1;
		boolean initializeRandom = true;
		Task task = Task.CLASSIFICATION;
		int intervalEpochPercentage = 2;
		double momentum = 0.1;
		double learningRate = 0.4;
		int numberHiddenLayers = 1;
		int numberUnitHidden = 10;
		boolean updateBatch = false;
		boolean normalizeWeights = true;
		boolean validation = true;

		GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
		List<Chromosome> population = RandomUtils.initializePopulation(
				numberIndividualInitial, numberGenes);

		ReaderFeatureBinaryFile readerFeatureCancer = new ReaderFeatureBinaryFile(
				pathTraining, pathTest, pathValidation, numberInputTraining,
				numberInputTest, numberInputValidation, numberOutput);

		ParameterTraining parameterTraining = new ParameterTraining();
		parameterTraining.setNumberEpochs(numberEpochs);
		parameterTraining.setMaxError(maxErrorMLPTraining);
		parameterTraining.setTask(task.getValue());
		parameterTraining.setLearningRateDecrease(learningRateDecrease);
		parameterTraining.setMinLearningRate(minLearningRate);
		parameterTraining.setWeightsInitializationRandom(initializeRandom);
		parameterTraining.setIntervalEpochPercentage(intervalEpochPercentage);
		parameterTraining.setLearningRate(learningRate);
		parameterTraining.setMomentum(momentum);
		parameterTraining.setNumberHiddenLayers(numberHiddenLayers);
		parameterTraining.setNumberOutput(numberOutput);
		parameterTraining.setNumberUnitHidden(numberUnitHidden);
		parameterTraining.setUpdateBatch(updateBatch);
		parameterTraining.setNormalizeWeights(normalizeWeights);
		parameterTraining.setValidation(validation);

		FitnessFunctionMLP fitnessFunction = new FitnessFunctionMLP(
				readerFeatureCancer, parameterTraining);

		ParameterGA parameterGA = new ParameterGA();
		parameterGA.setNumberFathers(numberFathers);
		parameterGA.setNumberGeneration(numberGeneration);
		parameterGA.setNumberIndividualCrossing(numberIndividualCrossing);
		parameterGA.setNumberIndividualMutation(numberIndividualMutation);

		Chromosome chromosome = geneticAlgorithm.run(population,
				fitnessFunction, parameterGA);

		System.out.println("Genes Finais");
		MatrixHandler.printArray(chromosome.getGene());
		System.out.println("Evaluation: " + chromosome.getEvaluation());
	}

}
