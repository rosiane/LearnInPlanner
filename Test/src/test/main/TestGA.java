package test.main;

import java.io.IOException;
import java.util.List;

import common.MatrixHandler;

import neural.network.enums.Task;
import neural.network.impl.ParameterTraining;

import preprocessor.file.ReaderFeatureCancer;
import feature.selector.GeneticAlgorithm;
import feature.selector.ga.Chromosome;
import feature.selector.ga.ParameterGA;
import feature.selector.ga.mlp.FitnessFunctionMLP;
import feature.selector.ga.util.RandomUtils;

public class TestGA {

	public static void main(String[] args) {
//		System.out.println(RandomUtils.nextInt(10));
//		System.out.println(RandomUtils.nextDouble());
		try {
			test();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void test() throws IOException {
		int numberInputTraining = 513;
		int numberInputTest = 56;
		int numberOutput = 2;

		String pathTraining = "./data/cancer/cancer_binaryTraining1.csv";
		String pathTest = "./data/cancer/cancer_binaryTest1.csv";

		int numberIndividualInitial = 20;
		int numberGenes = 30;

		// Parameters GA
		double maxErrorGA = 5;
		int numberFathers = 10;
		long numberGeneration = 100;
//		long numberGeneration = 1;
		int numberIndividualCrossing = 4;
		int numberIndividualMutation = 1;

		// Parameters MLP
		long numberEpochs = 2000;
//		long numberEpochs = 1;
		double maxErrorMLPTraining = 10;
		double learningRateDecrease = 0.95;
		double minLearningRate = 0.1;
		boolean initializeRandom = true;
		Task task = Task.CLASSIFICATION;
		int intervalEpochPercentage = 5;
		double momentum = 0.2;
		double learningRate = 0.3;
		int numberHiddenLayers = 1;
		int numberUnitHidden = 10;
		boolean updateBatch = true;

		GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
		List<Chromosome> population = RandomUtils.initializePopulation(
				numberIndividualInitial, numberGenes);

		ReaderFeatureCancer readerFeatureCancer = new ReaderFeatureCancer(
				pathTraining, pathTest, numberInputTraining, numberInputTest,
				numberOutput);

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

		FitnessFunctionMLP fitnessFunction = new FitnessFunctionMLP(
				readerFeatureCancer, parameterTraining);
		
		ParameterGA parameterGA = new ParameterGA();
		parameterGA.setMaxError(maxErrorGA);
		parameterGA.setNumberFathers(numberFathers);
		parameterGA.setNumberGeneration(numberGeneration);
		parameterGA.setNumberIndividualCrossing(numberIndividualCrossing);
		parameterGA.setNumberIndividualMutation(numberIndividualMutation);
		
		Chromosome chromosome = geneticAlgorithm.run(population, fitnessFunction, parameterGA);
		
		System.out.println("Genes Finais");
		MatrixHandler.printArray(chromosome.getGene());
		System.out.println("Evaluation: " + chromosome.getEvaluation());
	}

}
