package test.main;

import java.io.IOException;
import java.util.List;

import neural.network.enums.Task;
import neural.network.impl.ParameterTraining;

import common.MatrixHandler;
import common.preprocessor.file.ReaderFeatureBinaryFile;

import feature.selector.GeneticAlgorithm;
import feature.selector.ga.Chromosome;
import feature.selector.ga.ParameterGA;
import feature.selector.ga.mlp.FitnessFunctionMLP;
import feature.selector.ga.util.RandomUtilsFeatureSelector;

public class TestGA {

	public static void main(final String[] args) {
		try {
			test();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private static void test() throws IOException {
		final int numberInputTraining = 467;
		final int numberInputTest = 51;
		final int numberInputValidation = 51;
		final int numberOutput = 2;

		// String pathTraining = "./data/cancer/cancer_binaryTraining1.csv";
		// String pathTest = "./data/cancer/cancer_binaryTest1.csv";
		// String pathValidation = "./data/cancer/cancer_binary_validation.csv";
		final String pathTraining = "./data/backup/cancer/cancer_binary_patternizedTraining1.csv";
		final String pathTest = "./data/backup/cancer/cancer_binary_patternizedTest1.csv";
		final String pathValidation = "./data/backup/cancer/cancer_binary_patternized_validation.csv";

		final int numberIndividualInitial = 20;
		final int numberGenes = 30;

		// Parameters GA
		final int numberFathers = 10;
		final long numberGeneration = 10;
		final int numberIndividualCrossing = 4;
		final int numberIndividualMutation = 2;

		// Parameters MLP
		final long numberEpochs = 100;
		final double maxErrorMLPTraining = 6;
		final double learningRateDecrease = 0.99;
		final double minLearningRate = 0.1;
		final boolean initializeRandom = true;
		final Task task = Task.CLASSIFICATION;
		final int intervalEpochPercentage = 2;
		final double momentum = 0.1;
		final double learningRate = 0.4;
		final int numberHiddenLayers = 1;
		final int numberUnitHidden = 10;
		final boolean updateBatch = false;
		final boolean normalizeWeights = true;
		final boolean validation = true;

		final GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
		final List<Chromosome> population = RandomUtilsFeatureSelector
				.initializePopulation(numberIndividualInitial, numberGenes);

		final ReaderFeatureBinaryFile readerFeatureCancer = new ReaderFeatureBinaryFile(
				pathTraining, pathTest, pathValidation, numberInputTraining,
				numberInputTest, numberInputValidation, numberOutput);

		final ParameterTraining parameterTraining = new ParameterTraining();
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

		final FitnessFunctionMLP fitnessFunction = new FitnessFunctionMLP(
				readerFeatureCancer, parameterTraining, null);

		final ParameterGA parameterGA = new ParameterGA();
		parameterGA.setNumberFathers(numberFathers);
		parameterGA.setNumberGeneration(numberGeneration);
		parameterGA.setNumberIndividualCrossing(numberIndividualCrossing);
		parameterGA.setNumberIndividualMutation(numberIndividualMutation);

		final Chromosome chromosome = geneticAlgorithm.run(population,
				fitnessFunction, parameterGA);

		System.out.println("Genes Finais");
		MatrixHandler.printArray(chromosome.getGene());
		System.out.println("Evaluation: " + chromosome.getEvaluation());
	}

}
