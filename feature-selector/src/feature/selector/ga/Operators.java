package feature.selector.ga;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import feature.selector.ga.util.RandomUtils;

public class Operators {
	public static List<Chromosome> reproduction(
			List<Chromosome> currentPopulation, ParameterGA parameterGA) {
		List<Chromosome> result = currentPopulation;
		List<Chromosome> newIndividuals = new ArrayList<>();
		newIndividuals = crossing(currentPopulation,
				parameterGA.getNumberIndividualCrossing());
		newIndividuals = mutation(newIndividuals,
				parameterGA.getNumberIndividualMutation());
		result.addAll(newIndividuals);
		return result;
	}

	public static List<Chromosome> crossing(List<Chromosome> currentPopulation,
			int numberIndividualCrossing) {
		List<Chromosome> result = new ArrayList<>();
		Chromosome newIndividual = null;
		int[] mother = null;
		int[] father = null;
		int indexMother = 0;
		int indexFather = 0;
		int[] son = null;
		int indexMutation = 0;
		int indexSon = 0;
		for (int index = 0; index < numberIndividualCrossing; index++) {
			indexMother = RandomUtils.nextInt(currentPopulation.size());
			while (true) {
				indexFather = RandomUtils.nextInt(currentPopulation.size());
				if (indexMother != indexFather) {
					break;
				}
			}
			indexMutation = RandomUtils.nextInt(mother.length);
			mother = currentPopulation.get(indexMother).getGene();
			father = currentPopulation.get(indexFather).getGene();
			son = new int[mother.length];
			for (indexSon = 0; indexSon < son.length; indexSon++) {
				if (indexSon <= indexMutation) {
					son[indexSon] = mother[indexSon];
				} else {
					son[indexSon] = father[indexSon];
				}
			}
			newIndividual = new Chromosome();
			newIndividual.setGene(son);
			result.add(newIndividual);
		}
		return result;
	}

	public static List<Chromosome> mutation(List<Chromosome> currentPopulation,
			int numberIndividualMutation) {
		Chromosome individual = null;
		List<Chromosome> result = currentPopulation;
		for (int index = 0; index < numberIndividualMutation; index++) {
			individual = result.get(RandomUtils.nextInt(result.size()));
			individual
					.mutation(RandomUtils.nextInt(individual.getGene().length));
		}

		return result;
	}

	public static List<Chromosome> selection(List<Chromosome> population,
			FitnessFunction fitnessFunction, int numberFathers)
			throws IOException {
		List<Chromosome> fathers = new ArrayList<>();
		Iterator<Chromosome> populationIterator = population.iterator();
		Chromosome individual = null;
		double evaluation = 100;
		while (populationIterator.hasNext()) {
			individual = populationIterator.next();
			evaluation = fitnessFunction.evaluate(individual);
			individual.setEvaluation(evaluation);
		}
		Collections.sort(population, new Comparator<Chromosome>() {
			@Override
			public int compare(Chromosome o1, Chromosome o2) {
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
