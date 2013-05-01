package test.main;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import neural.network.enums.Task;
import neural.network.impl.ParameterTraining;

import common.MatrixHandler;

import feature.selector.ga.Chromosome;
import feature.selector.ga.ParameterGA;

import javaff.data.UngroundProblem;
import javaff.data.strips.UngroundInstantAction;
import javaff.parser.PDDL21parser;
import learn.Learn;
import learn.pojos.LearnParameters;

public class LearnTest {

	public static void main(String args[]) {
		// initialFeatures();
		try {
			learn();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void initialFeatures() {
		String domainFilePath = "../Examples/IPC3/Tests1/Depots/Strips/Depots.pddl";
		String problemFilePath = "../Examples/IPC3/Tests1/Depots/Strips/pfile1";
		File domainFile = new File(domainFilePath);
		File problemFile = new File(problemFilePath);
		UngroundProblem unground = PDDL21parser.parseFiles(domainFile,
				problemFile);

		if (unground == null) {
			System.out.println("Parsing error - see console for details");
			return;
		}

		Iterator<UngroundInstantAction> iterator = unground.actions.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next().name);
		}
	}

	public static void learn() throws Exception {
		String domainPath = "../Examples/IPC3/Tests1/Depots/Strips/Depots.pddl";
		String examplePathPrefix = "../Examples/IPC3/Tests1/Depots/Strips/pfile";
		int numberExamples = 22;
		String solutionFFPathPrefix = "../Examples/IPC3/Tests1/Depots/Strips/training/pfile";
		String solutionFFPathSufix = "Solution.pddl";
		String databaseDir = "../Examples/IPC3/Tests1/Depots/Strips/training/rpl";
		String featuresFile = "../Examples/IPC3/Tests1/Depots/Strips/training/features/currentFeatures";
		int numberIndividualInitialGA = 3;
		String dirPlanningProblem = "../Examples/IPC3/Tests1/Depots/Strips/training";
		String dirPlanningProblemRPL = "../Examples/IPC3/Tests1/Depots/Strips/training/rpl";
		int[] problemTraining = { 1 };
		int[] problemValidation = { 2 };
		int[] problemTest = { 3 };
		int numberExpansion = 1;

		long numberEpochs = 1;
		double maxError = 0;
		Task task = Task.REGRESSION;
		double learningRateDecrease = 1;
		double minLearningRate = 0;
		boolean initializeRandom = true;
		int intervalEpochPercentage = 2;
		int numberHiddenLayers = 1;
		int numberOutput = 1;
		double learningRate = 0.3;
		int numberUnitHidden = 3;
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
		parameterTraining.setNumberHiddenLayers(numberHiddenLayers);
		parameterTraining.setNumberOutput(numberOutput);
		parameterTraining.setLearningRate(learningRate);
		parameterTraining.setNumberUnitHidden(numberUnitHidden);
		

		int numberFathers = 3;
		int numberGeneration = 2;
		int numberIndividualCrossing = 2;
		int numberIndividualMutation = 1;
		ParameterGA parameterGA = new ParameterGA();
		parameterGA.setNumberFathers(numberFathers);
		parameterGA.setNumberGeneration(numberGeneration);
		parameterGA.setNumberIndividualCrossing(numberIndividualCrossing);
		parameterGA.setNumberIndividualMutation(numberIndividualMutation);

		LearnParameters learnParameters = new LearnParameters();
		learnParameters.setDomainPath(domainPath);
		learnParameters.setExamplePathPrefix(examplePathPrefix);
		learnParameters.setNumberExamples(numberExamples);
		learnParameters.setSolutionFFPathPrefix(solutionFFPathPrefix);
		learnParameters.setSolutionFFPathSufix(solutionFFPathSufix);
		learnParameters.setDatabaseDir(databaseDir);
		learnParameters.setFeaturesFile(featuresFile);
		learnParameters.setNumberIndividualInitialGA(numberIndividualInitialGA);
		learnParameters.setDirPlanningProblem(dirPlanningProblem);
		learnParameters.setDirPlanningProblemRPL(dirPlanningProblemRPL);
		learnParameters.setProblemTraining(problemTraining);
		learnParameters.setProblemValidation(problemValidation);
		learnParameters.setProblemTest(problemTest);
		learnParameters.setNumberExpansion(numberExpansion);
		learnParameters.setParameterTrainingMLP(parameterTraining);
		learnParameters.setParameterGA(parameterGA);

		Learn learn = new Learn();
		Chromosome chromosome = learn.learn(learnParameters);
		if (chromosome != null) {
			System.out.println(chromosome.getEvaluation());
			MatrixHandler.printArray(chromosome.getGene());
		}
	}

}
