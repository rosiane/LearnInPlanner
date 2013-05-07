package feature.selector.ga;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import common.preprocessor.file.FileManager;

import feature.selector.ga.util.RandomUtilsFeatureSelector;

public class Operators {
	public static List<Chromosome> crossing(
			final List<Chromosome> currentPopulation,
			final int numberIndividualCrossing) {
		final List<Chromosome> result = new ArrayList<>();
		Chromosome newIndividual = null;
		int[] mother = null;
		int[] father = null;
		int indexMother = 0;
		int indexFather = 0;
		int[] son1 = null;
		int[] son2 = null;
		int indexMutation = 0;
		int indexSon = 0;
		for (int index = 0; index < numberIndividualCrossing; index++) {
			indexMother = RandomUtilsFeatureSelector.nextInt(currentPopulation
					.size());
			while (true) {
				indexFather = RandomUtilsFeatureSelector
						.nextInt(currentPopulation.size());
				if (indexMother != indexFather) {
					break;
				}
			}
			mother = currentPopulation.get(indexMother).getGene();
			father = currentPopulation.get(indexFather).getGene();
			indexMutation = RandomUtilsFeatureSelector.nextInt(mother.length);
			son1 = new int[mother.length];
			son2 = new int[mother.length];
			for (indexSon = 0; indexSon < son1.length; indexSon++) {
				if (indexSon <= indexMutation) {
					son1[indexSon] = mother[indexSon];
					son2[indexSon] = father[indexSon];
				} else {
					son1[indexSon] = father[indexSon];
					son2[indexSon] = mother[indexSon];
				}
			}
			newIndividual = new Chromosome();
			newIndividual.setGene(son1);
			result.add(newIndividual);

			newIndividual = new Chromosome();
			newIndividual.setGene(son2);
			result.add(newIndividual);
		}
		return result;
	}

	public static List<Chromosome> mutation(
			final List<Chromosome> currentPopulation,
			final double mutationRate) {
		final List<Chromosome> result = currentPopulation;
		for (int indexIndividual = 0; indexIndividual < currentPopulation.size(); indexIndividual++) {
			for(int indexGene = 0; indexGene < result.get(indexIndividual).getGene().length; indexGene++){
				if(RandomUtilsFeatureSelector.nextDouble() <= mutationRate){
					result.get(indexIndividual).mutation(indexGene);
				}
			}
		}

		return result;
	}

	public static List<Chromosome> reproduction(
			final List<Chromosome> currentPopulation,
			final ParameterGA parameterGA, final String resultFile)
					throws IOException {
		if ((resultFile != null) && !resultFile.isEmpty()) {
			FileManager.write(resultFile,
					"+++++++++++++++++ Reproduction +++++++++++++++++", true);
		} else {
			System.out
			.println("+++++++++++++++++ Reproduction +++++++++++++++++");
		}
		final List<Chromosome> result = currentPopulation;
		List<Chromosome> newIndividuals = new ArrayList<>();
		newIndividuals = crossing(currentPopulation,
				parameterGA.getNumberIndividualCrossing());
		newIndividuals = mutation(newIndividuals,
				parameterGA.getMutationRate());
		result.addAll(newIndividuals);
		return result;
	}

	public static List<Chromosome> selection(final List<Chromosome> population,
			final FitnessFunction fitnessFunction, final int numberFathers,
			final String resultFile) throws IOException {
		if ((resultFile != null) && !resultFile.isEmpty()) {
			FileManager.write(resultFile,
					"+++++++++++++++++ Selection +++++++++++++++++", true);
		} else {
			System.out.println("+++++++++++++++++ Selection +++++++++++++++++");
		}
		final List<Chromosome> fathers = new ArrayList<>();
		final Iterator<Chromosome> populationIterator = population.iterator();
		Chromosome individual = null;
		double evaluation = 100;
		while (populationIterator.hasNext()) {
			individual = populationIterator.next();
			evaluation = fitnessFunction.evaluate(individual);
			individual.setEvaluation(evaluation);
		}
		Collections.sort(population, new Comparator<Chromosome>() {
			@Override
			public int compare(final Chromosome o1, final Chromosome o2) {
				if (o1.getEvaluation() < o2.getEvaluation()) {
					return -1;
				}
				if (o1.getEvaluation() > o2.getEvaluation()) {
					return 1;
				}
				return 0;
			}
		});
		for (int index = 0; index < numberFathers; index++) {
			fathers.add(population.get(index));
		}
		return fathers;
	}
}
