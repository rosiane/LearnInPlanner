package test.main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import planner.javaff.data.UngroundProblem;
import planner.javaff.data.strips.PredicateSymbol;
import planner.javaff.data.strips.SimpleType;
import planner.javaff.data.strips.UngroundInstantAction;
import planner.javaff.data.strips.Variable;
import planner.javaff.parser.PDDL21parser;

import learn.Learn;
import learn.pojos.LearnParameters;
import neural.network.enums.Task;
import neural.network.impl.ParameterTraining;

import common.MatrixHandler;
import common.feature.ClassExpression;
import common.feature.PrefixEnum;
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
			UngroundInstantAction ungroundInstantAction =  iterator.next();
			System.out.println(ungroundInstantAction.toString());
			System.out.println(ungroundInstantAction.name + " " + ungroundInstantAction.params.size());
		}
		final Iterator<PredicateSymbol> iteratorFacts = unground.predSymbols
				.iterator();
		PredicateSymbol predicateSymbol;
		while (iteratorFacts.hasNext()) {
			predicateSymbol = iteratorFacts.next();
			System.out.println(predicateSymbol.toStringTyped());
			System.out.println(predicateSymbol.getName() + " " + predicateSymbol.getParams().size());
		}
//		final Iterator<SimpleType> iteratorT = unground.types.iterator();
//		while (iteratorT.hasNext()) {
//			System.out.println(iteratorT.next().toString());
//		}
	}

	public static void learn() throws Exception {
		File dir;

		String dirResult;
		for (int numberGeneration = 10;; numberGeneration += 10) {
			dir = new File("../Test/result/ " + numberGeneration + "/features");
			dir.mkdirs();
			dirResult = "../Test/result/ " + numberGeneration;
			learn(dirResult, numberGeneration);
		}
	}

	public static void learn(final String dirResult, final int numberGeneration)
			throws Exception {
		final SimpleDateFormat formato = new SimpleDateFormat(
				"dd_MM_yyyy_HH_mm_ss");
		final String date = formato.format(new Date());
		final String domainPath = "../Examples/IPC3/Tests1/Depots/Strips/Depots.pddl";
		final String examplePathPrefix = "../Examples/IPC3/Tests1/Depots/Strips/pfile";
		final String solutionFFPathPrefix = "../Examples/IPC3/Tests1/Depots/Strips/training/pfile";
		final String solutionFFPathSufix = "Solution.pddl";
		final String databaseDir = "../Examples/IPC3/Tests1/Depots/Strips/training/rpl";
		final String featuresFile = dirResult + "/features/currentFeatures-";
		final String resultFile = dirResult + "/DepotsTraining.doc";
		final int numberIndividualInitialGA = 8;
		final String dirPlanningProblem = "../Examples/IPC3/Tests1/Depots/Strips/training";
		final String dirPlanningProblemRPL = "../Examples/IPC3/Tests1/Depots/Strips/training/rpl";
		final int[] problemTraining = { 6, 7 };
		final int[] problemValidation = { 5 };
		final int[] problemTest = { 2, 3 };
		final int numberExpansion = 2;

		final long numberEpochs = 2000;
		final double maxError = 0;
		final Task task = Task.REGRESSION;
		final double learningRateDecrease = 1;
		final double minLearningRate = 0;
		final boolean initializeRandom = true;
		final int intervalEpochPercentage = 2;
		final int numberHiddenLayers = 1;
		final int numberOutput = 1;
		final double learningRate = 0.05;
		final int numberUnitHidden = 3;
		final boolean useHeuristicUnitHidden = true;
		final boolean normalizeLabel = true;

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
		FileManager.write(resultFile, "Normalize label: " + normalizeLabel,
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
		parameterTraining.setNormalizeLabel(normalizeLabel);

		final int numberFathers = 6;
		final int numberIndividualCrossing = 1;
		final double mutationRate = 0.1;
		final double maxEvaluation = 0.001;
		final ParameterGA parameterGA = new ParameterGA();
		parameterGA.setNumberFathers(numberFathers);
		parameterGA.setNumberGeneration(numberGeneration);
		parameterGA.setNumberIndividualCrossing(numberIndividualCrossing);
		parameterGA.setMutationRate(mutationRate);
		parameterGA.setMaxEvaluation(maxEvaluation);

		FileManager.write(resultFile, "Parameter GA", true);
		FileManager.write(resultFile, "Number Expansion: " + numberExpansion,
				true);
		FileManager.write(resultFile, "Number Fathers: " + numberFathers, true);
		FileManager.write(resultFile, "Number Generation: " + numberGeneration,
				true);
		FileManager.write(resultFile, "Number Individual Crossing: "
				+ numberIndividualCrossing, true);
		FileManager.write(resultFile, "Mutation Rate: " + mutationRate, true);
		FileManager.write(resultFile, "Max evaluation: " + maxEvaluation, true);

		final LearnParameters learnParameters = new LearnParameters();
		learnParameters.setDomainPath(domainPath);
		learnParameters.setExamplePathPrefix(examplePathPrefix);
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
		final String problem = "Depots";
		final Chromosome chromosome = learn.learn(problem, learnParameters);
		if (chromosome != null) {
			FileManager.write(resultFile, "Chosen: " + chromosome.toString(),
					true);
			final String featureSelectedFile = dirResult + "/featureSelected";
			FileManager.write(featureSelectedFile,
					Integer.toString(chromosome.getExpansionNumber()), true);
			FileManager.write(featureSelectedFile,
					MatrixHandler.toStringArray(chromosome.getGene()), true);
			final String weightFile = dirResult + "/weight";
			for (int indexWeight = 0; indexWeight < chromosome.getWeights().length; indexWeight++) {
				MatrixHandler
						.write(weightFile + indexWeight, chromosome
								.getWeights()[indexWeight].getWeights(), true);
			}
		}
		FileManager.write(resultFile,
				"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@", true);
		System.out.println("End");
	}

	public static void main(final String args[]) {
//		 initialFeatures();
		// final File f = new File("../Test/result/features/10");
		// System.out.println(f.mkdir());
		// System.out.println(f.mkdirs());
		try {
			learn();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
