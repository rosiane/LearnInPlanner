package learn;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import learn.pojos.LearnParameters;
import learn.utils.ExpandFeatures;

import common.feature.ClassExpression;
import common.preprocessor.file.FileManager;
import common.preprocessor.file.ReaderFeaturePlanning;

import feature.selector.GeneticAlgorithm;
import feature.selector.ga.Chromosome;
import feature.selector.ga.mlp.FitnessFunctionMLP;
import feature.selector.ga.util.RandomUtilsFeatureSelector;

public class Learn {

	public Chromosome learn(final String problem,
			final LearnParameters learnParameters) throws Exception {
		LinkedList<ClassExpression> features = FeatureSingleton
				.initialFeatures(problem, learnParameters);
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
				features = ExpandFeatures.expand(features,
						FeatureSingleton.getTypes(problem));
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
			chromosome.setExpansionNumber(indexExpansion);
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
