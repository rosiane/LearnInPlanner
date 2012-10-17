package feature.selector.ga.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import feature.selector.ga.Chromosome;

public class RandomUtils {
	private static Random random = new Random();

	public static int nextInt(int max) {
		return (int) (nextDouble() * max);
	}

	public static double nextDouble() {
		return random.nextDouble();
	}

	public static List<Chromosome> initializePopulation(int numberIndividuals,
			int numberGenes) {
		List<Chromosome> population = new ArrayList<>();
		Chromosome individual = null;
		int[] gene = null;
		double sorted = 0;
		for (int indexIndividual = 0; indexIndividual < numberIndividuals; indexIndividual++) {
			individual = new Chromosome();
			gene = new int[numberGenes];
			for (int indexGene = 0; indexGene < numberGenes; indexGene++) {
				sorted = nextDouble();
				if (sorted < 0.5) {
					gene[indexGene] = 0;
				} else {
					gene[indexGene] = 1;
				}
			}
			individual.setGene(gene);
			population.add(individual);
		}
		return population;
	}

}
