package test.main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javaff.data.UngroundProblem;
import javaff.data.strips.UngroundInstantAction;
import javaff.parser.PDDL21parser;
import learn.Learn;
import learn.pojos.LearnParameters;
import neural.network.enums.Task;
import neural.network.impl.ParameterTraining;

import common.MatrixHandler;
import common.preprocessor.file.FileManager;

import feature.selector.ga.Chromosome;
import feature.selector.ga.ParameterGA;

public class LearnTest {

	public static void initialFeatures() {
		final String domainFilePath = "../Examples/IPC3/Tests1/Depots/Strips/Depots.pddl";
		final String problemFilePath = "../Examples/IPC3/Tests1/Depots/Strips/pfile1";
		final File domainFile = new File(domainFilePath);
		final File problemFile = new File(problemFilePath);
		final UngroundProblem unground = PDDL21parser.parseFiles(domainFile,
				problemFile);

		if (unground == null) {
			System.out.println("Parsing error - see console for details");
			return;
		}

		final Iterator<UngroundInstantAction> iterator = unground.actions
				.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next().name);
		}
	}

	public static void learn() throws Exception {
		final SimpleDateFormat formato = new SimpleDateFormat(
				"dd_MM_yyyy_HH_mm_ss");
		final String date = formato.format(new Date());
		final String domainPath = "../Examples/IPC3/Tests1/Depots/Strips/Depots.pddl";
		final String examplePathPrefix = "../Examples/IPC3/Tests1/Depots/Strips/pfile";
		final int numberExamples = 22;
		final String solutionFFPathPrefix = "../Examples/IPC3/Tests1/Depots/Strips/training/pfile";
		final String solutionFFPathSufix = "Solution.pddl";
		final String databaseDir = "../Examples/IPC3/Tests1/Depots/Strips/training/rpl";
		final String featuresFile = "../Test/result/features/currentFeatures"
				+ date + "-";
		final String resultFile = "../Test/result/DepotsTraining.doc";
		final int numberIndividualInitialGA = 3;
		final String dirPlanningProblem = "../Examples/IPC3/Tests1/Depots/Strips/training";
		final String dirPlanningProblemRPL = "../Examples/IPC3/Tests1/Depots/Strips/training/rpl";
		final int[] problemTraining = { 1 };
		final int[] problemValidation = { 2 };
		final int[] problemTest = { 3 };
		final int numberExpansion = 2;

		final long numberEpochs = 1;
		final double maxError = 0;
		final Task task = Task.REGRESSION;
		final double learningRateDecrease = 1;
		final double minLearningRate = 0;
		final boolean initializeRandom = true;
		final int intervalEpochPercentage = 2;
		final int numberHiddenLayers = 1;
		final int numberOutput = 1;
		final double learningRate = 0.3;
		final int numberUnitHidden = 3;
		final boolean useHeuristicUnitHidden = true;

		FileManager.write(resultFile,
				"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@", true);
		FileManager.write(resultFile, "Execution time: " + date, true);
		FileManager.write(resultFile, "Problem Informations", true);
		FileManager.write(resultFile, "Domain: " + domainPath, true);
		FileManager.write(
				resultFile,
				"Problems training: "
						+ MatrixHandler.toStringArray(problemTraining), true);
		FileManager.write(
				resultFile,
				"Problems validation: "
						+ MatrixHandler.toStringArray(problemValidation), true);
		FileManager.write(resultFile,
				"Problems test: " + MatrixHandler.toStringArray(problemTest),
				true);
		FileManager.write(resultFile, "Parameter MLP", true);
		FileManager.write(resultFile, "Number Epochs: " + numberEpochs, true);
		FileManager.write(resultFile, "Max Error: " + maxError, true);
		FileManager.write(resultFile, "Learning Rate Decrease: "
				+ learningRateDecrease, true);
		FileManager.write(resultFile, "Min Learning Rate: " + minLearningRate,
				true);
		FileManager.write(resultFile, "Initialize Random: " + initializeRandom,
				true);
		FileManager.write(resultFile, "Interval Epoch Percentage: "
				+ intervalEpochPercentage, true);
		FileManager.write(resultFile, "Number Hidden Layers: "
				+ numberHiddenLayers, true);
		FileManager.write(resultFile, "Number Output: " + numberOutput, true);
		FileManager.write(resultFile, "Learning Rate: " + learningRate, true);
		FileManager.write(resultFile,
				"Number Unit Hidden: " + numberUnitHidden, true);
		FileManager.write(resultFile, "Use Heuristic Unit Hidden: "
				+ useHeuristicUnitHidden, true);
		FileManager.write(resultFile, "Initialize Random: " + initializeRandom,
				true);

		final ParameterTraining parameterTraining = new ParameterTraining();
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
		parameterTraining.setUseHeuristicUnitHidden(useHeuristicUnitHidden);

		final int numberFathers = 3;
		final int numberGeneration = 2;
		final int numberIndividualCrossing = 2;
		final int numberIndividualMutation = 1;
		final ParameterGA parameterGA = new ParameterGA();
		parameterGA.setNumberFathers(numberFathers);
		parameterGA.setNumberGeneration(numberGeneration);
		parameterGA.setNumberIndividualCrossing(numberIndividualCrossing);
		parameterGA.setNumberIndividualMutation(numberIndividualMutation);

		FileManager.write(resultFile, "Parameter GA", true);
		FileManager.write(resultFile, "Number Expansion: " + numberExpansion,
				true);
		FileManager.write(resultFile, "Number Fathers: " + numberFathers, true);
		FileManager.write(resultFile, "Number Generation: " + numberGeneration,
				true);
		FileManager.write(resultFile, "Number Individual Crossing: "
				+ numberIndividualCrossing, true);
		FileManager.write(resultFile, "Number Individual Mutation: "
				+ numberIndividualMutation, true);

		final LearnParameters learnParameters = new LearnParameters();
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
		learnParameters.setResultFile(resultFile);

		final Learn learn = new Learn();
		final Chromosome chromosome = learn.learn(learnParameters);
		if (chromosome != null) {
			FileManager.write(resultFile, "Chosen: " + chromosome.toString(),
					true);
		}
		FileManager.write(resultFile,
				"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@", true);
		System.out.println("End");
	}

	public static void main(final String args[]) {
		// initialFeatures();
		try {
			learn();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
