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

	private LinkedList<ClassExpression> initialFeatures(
			final LearnParameters learnParameters) throws Exception {
		final LinkedList<ClassExpression> features = new LinkedList<>();
		final File domainFile = new File(learnParameters.getDomainPath());
		final File problemFile = new File(
				learnParameters.getExamplePathPrefix() + 1);
		final UngroundProblem unground = PDDL21parser.parseFiles(domainFile,
				problemFile);

		if (unground == null) {
			LOGGER.error("Parsing error - see console for details");
			throw new Exception("Parsing error - see console for detail");
		}

		@SuppressWarnings("unchecked")
		final Iterator<UngroundInstantAction> iteratorActions = unground.actions
				.iterator();

		ClassExpression classExpression;
		UngroundInstantAction ungroundInstantAction;
		while (iteratorActions.hasNext()) {
			ungroundInstantAction = iteratorActions.next();
			final String[] parameterType = new String[ungroundInstantAction.params
					.size()];
			@SuppressWarnings("unchecked")
			final Iterator<Variable> parameters = ungroundInstantAction.params
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
		final Iterator<PredicateSymbol> iteratorFacts = unground.predSymbols
				.iterator();
		PredicateSymbol predicateSymbol;
		while (iteratorFacts.hasNext()) {
			predicateSymbol = iteratorFacts.next();
			final String[] parameterType = new String[predicateSymbol
					.getParams().size()];
			@SuppressWarnings("unchecked")
			final Iterator<Variable> parameters = predicateSymbol.getParams()
					.iterator();
			for (int indexParameter = 0; indexParameter < parameterType.length; indexParameter++) {
				parameterType[indexParameter] = parameters.next().getType()
						.toString();
			}
			classExpression = new ClassExpression(PrefixEnum.FACT.prefix()
					+ predicateSymbol.getName(), predicateSymbol.getParams()
					.size(), parameterType);
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
		final Iterator<SimpleType> iteratorT = unground.types.iterator();
		this.types = new ArrayList<>();
		while (iteratorT.hasNext()) {
			this.types.add(iteratorT.next().toString());
		}
		return features;
	}

	public Chromosome learn(final LearnParameters learnParameters)
			throws Exception {
		LinkedList<ClassExpression> features = this
				.initialFeatures(learnParameters);
		this.writeFeatures(learnParameters.getFeaturesFile() + 0, features);
		List<Chromosome> population = RandomUtilsFeatureSelector
				.initializePopulation(
						learnParameters.getNumberIndividualInitialGA(),
						features.size());
		final GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(
				learnParameters.getResultFile());
		ReaderFeaturePlanning readerFeaturePlanning = new ReaderFeaturePlanning(
				features, learnParameters.getDirPlanningProblem(),
				learnParameters.getDirPlanningProblemRPL(),
				learnParameters.getProblemTraining(),
				learnParameters.getProblemValidation(),
				learnParameters.getProblemTest());
		FitnessFunctionMLP fitnessFunction = new FitnessFunctionMLP(
				readerFeaturePlanning,
				learnParameters.getParameterTrainingMLP(),
				learnParameters.getResultFile());
		Chromosome chromosome = null;
		Chromosome best = null;
		Chromosome last = null;
		for (int indexExpansion = 0; indexExpansion <= learnParameters
				.getNumberExpansion(); indexExpansion++) {
			if ((learnParameters.getResultFile() != null)
					&& !learnParameters.getResultFile().isEmpty()) {
				FileManager.write(learnParameters.getResultFile(),
						"$$$$$$$$$$ Run Expansion " + (indexExpansion + 1)
								+ "$$$$$$$$$$", true);
			} else {
				System.out.println("$$$$$$$$$$ Run Expansion "
						+ (indexExpansion + 1) + "$$$$$$$$$$");
			}
			if (indexExpansion > 0) {
				features = this.selectedFeatures(features, last.getGene());
				features = ExpandFeatures.expand(features, this.types);
				this.writeFeatures(learnParameters.getFeaturesFile()
						+ (indexExpansion), features);
				readerFeaturePlanning = new ReaderFeaturePlanning(features,
						learnParameters.getDirPlanningProblem(),
						learnParameters.getDirPlanningProblemRPL(),
						learnParameters.getProblemTraining(),
						learnParameters.getProblemValidation(),
						learnParameters.getProblemTest());
				fitnessFunction = new FitnessFunctionMLP(readerFeaturePlanning,
						learnParameters.getParameterTrainingMLP(),
						learnParameters.getResultFile());
				population = RandomUtilsFeatureSelector.initializePopulation(
						learnParameters.getNumberIndividualInitialGA(),
						features.size());
			}
			chromosome = geneticAlgorithm.run(population, fitnessFunction,
					learnParameters.getParameterGA());
			if (best == null) {
				best = chromosome;
				last = chromosome;
			} else {
				if (chromosome.getEvaluation() < best.getEvaluation()) {
					best = chromosome;
				}
				last = chromosome;
			}
			if ((learnParameters.getResultFile() != null)
					&& !learnParameters.getResultFile().isEmpty()) {
				FileManager.write(learnParameters.getResultFile(), "Current: "
						+ last.toString(), true);
				FileManager.write(learnParameters.getResultFile(), "Best: "
						+ best.toString(), true);
			}
		}
		return best;
	}

	private LinkedList<ClassExpression> selectedFeatures(
			final LinkedList<ClassExpression> features, final int[] gene) {
		final LinkedList<ClassExpression> result = new LinkedList<>();
		for (int indexGene = 0; indexGene < gene.length; indexGene++) {
			if (gene[indexGene] == 1) {
				result.addLast(features.get(indexGene));
			}
		}
		return result;
	}

	private void writeFeatures(final String featuresFile,
			final LinkedList<ClassExpression> features) throws IOException {
		boolean append = false;
		for (int indexFeatures = 0; indexFeatures < features.size(); indexFeatures++) {
			if (indexFeatures > 0) {
				append = true;
			}
			FileManager.write(featuresFile, features.get(indexFeatures)
					.toString(), append);
		}
	}

}
