package learn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javaff.data.UngroundProblem;
import javaff.data.strips.PredicateSymbol;
import javaff.data.strips.SimpleType;
import javaff.data.strips.UngroundInstantAction;
import javaff.data.strips.Variable;
import javaff.parser.PDDL21parser;
import learn.pojos.LearnParameters;
import learn.utils.ExpandFeatures;

import org.apache.log4j.Logger;

import common.ClassExpression;
import common.PrefixEnum;
import common.preprocessor.file.FileManager;
import common.preprocessor.file.ReaderFeaturePlanning;

import feature.selector.GeneticAlgorithm;
import feature.selector.ga.Chromosome;
import feature.selector.ga.mlp.FitnessFunctionMLP;
import feature.selector.ga.util.RandomUtilsFeatureSelector;

public class Learn {
	private static final Logger LOGGER = Logger.getLogger(Learn.class);
	private List<String> types;

	public Chromosome learn(LearnParameters learnParameters) throws Exception {
		LinkedList<ClassExpression> features = initialFeatures(learnParameters);
		writeFeatures(learnParameters.getFeaturesFile(), features);
		List<Chromosome> population = RandomUtilsFeatureSelector
				.initializePopulation(
						learnParameters.getNumberIndividualInitialGA(),
						features.size());
		GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
		ReaderFeaturePlanning readerFeaturePlanning = new ReaderFeaturePlanning(
				features, learnParameters.getDirPlanningProblem(),
				learnParameters.getDirPlanningProblemRPL(),
				learnParameters.getProblemTraining(),
				learnParameters.getProblemValidation(),
				learnParameters.getProblemTest());
		FitnessFunctionMLP fitnessFunction = new FitnessFunctionMLP(
				readerFeaturePlanning,
				learnParameters.getParameterTrainingMLP());
		Chromosome chromosome = null;
		Chromosome best = null;
		LinkedList<ClassExpression> featuresNew;
		for (int indexExpansion = 0; indexExpansion < learnParameters
				.getNumberExpansion(); indexExpansion++) {
			if (indexExpansion > 0) {
				featuresNew = ExpandFeatures.expand(features, types);
				readerFeaturePlanning = new ReaderFeaturePlanning(featuresNew,
						learnParameters.getDirPlanningProblem(),
						learnParameters.getDirPlanningProblemRPL(),
						learnParameters.getProblemTraining(),
						learnParameters.getProblemValidation(),
						learnParameters.getProblemTest());
			}
			chromosome = geneticAlgorithm.run(population, fitnessFunction,
					learnParameters.getParameterGA());
			if (best == null) {
				best = chromosome;
			} else {
				if (chromosome.getEvaluation() > best.getEvaluation()) {
					best = chromosome;
				}
			}
		}
		return best;
	}

	private void writeFeatures(String featuresFile,
			LinkedList<ClassExpression> features) throws IOException {
		boolean append = false;
		for (int indexFeatures = 0; indexFeatures < features.size(); indexFeatures++) {
			if (indexFeatures > 0) {
				append = true;
			}
			FileManager.write(featuresFile, features.get(indexFeatures)
					.toString(), append);
		}
	}

	private LinkedList<ClassExpression> initialFeatures(
			LearnParameters learnParameters) throws Exception {
		LinkedList<ClassExpression> features = new LinkedList<>();
		File domainFile = new File(learnParameters.getDomainPath());
		File problemFile = new File(learnParameters.getExamplePathPrefix() + 1);
		UngroundProblem unground = PDDL21parser.parseFiles(domainFile,
				problemFile);

		if (unground == null) {
			LOGGER.error("Parsing error - see console for details");
			throw new Exception("Parsing error - see console for detail");
		}

		@SuppressWarnings("unchecked")
		Iterator<UngroundInstantAction> iteratorActions = unground.actions
				.iterator();

		ClassExpression classExpression;
		UngroundInstantAction ungroundInstantAction;
		while (iteratorActions.hasNext()) {
			ungroundInstantAction = iteratorActions.next();
			String[] parameterType = new String[ungroundInstantAction.params
					.size()];
			@SuppressWarnings("unchecked")
			Iterator<Variable> parameters = ungroundInstantAction.params
					.iterator();
			for (int indexParameter = 0; indexParameter < parameterType.length; indexParameter++) {
				parameterType[indexParameter] = parameters.next().getType()
						.toString();
			}
			classExpression = new ClassExpression(PrefixEnum.ACTION.prefix()
					+ ungroundInstantAction.name.toString(),
					ungroundInstantAction.params.size(), parameterType);
			features.add(classExpression);
		}
		@SuppressWarnings("unchecked")
		Iterator<PredicateSymbol> iteratorFacts = unground.predSymbols
				.iterator();
		PredicateSymbol predicateSymbol;
		while (iteratorFacts.hasNext()) {
			predicateSymbol = iteratorFacts.next();
			String[] parameterType = new String[predicateSymbol.getParams()
					.size()];
			@SuppressWarnings("unchecked")
			Iterator<Variable> parameters = predicateSymbol.getParams()
					.iterator();
			for (int indexParameter = 0; indexParameter < parameterType.length; indexParameter++) {
				parameterType[indexParameter] = parameters.next().getType()
						.toString();
			}
			classExpression = new ClassExpression(predicateSymbol.getName(),
					predicateSymbol.getParams().size(), parameterType);
			features.add(classExpression);
			classExpression = new ClassExpression(PrefixEnum.ADD.prefix()
					+ predicateSymbol.getName(), predicateSymbol.getParams()
					.size(), parameterType);
			features.add(classExpression);
			classExpression = new ClassExpression(PrefixEnum.DEL.prefix()
					+ predicateSymbol.getName(), predicateSymbol.getParams()
					.size(), parameterType);
			features.add(classExpression);
			classExpression = new ClassExpression(PrefixEnum.GOAL.prefix()
					+ predicateSymbol.getName(), predicateSymbol.getParams()
					.size(), parameterType);
			features.add(classExpression);
			classExpression = new ClassExpression(PrefixEnum.CURRENT.prefix()
					+ predicateSymbol.getName(), predicateSymbol.getParams()
					.size(), parameterType);
			features.add(classExpression);
		}
		Iterator<SimpleType> iteratorT = unground.types.iterator();
		types = new ArrayList<>();
		while (iteratorT.hasNext()) {
			types.add(iteratorT.next().toString());
		}
		return features;
	}

}
