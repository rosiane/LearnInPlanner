package feature.selector;

import java.io.IOException;
import java.util.List;

import feature.selector.ga.Chromosome;
import feature.selector.ga.FitnessFunction;
import feature.selector.ga.Operators;
import feature.selector.ga.ParameterGA;

public class GeneticAlgorithm {

	public Chromosome run(List<Chromosome> population,
			FitnessFunction fitnessFunction, ParameterGA parameterGA)
			throws IOException {
		Chromosome result = null;
		List<Chromosome> currentPopulation = population;
		for (int index = 0; index < parameterGA.getNumberGeneration(); index++) {
			System.out.println("########## Run Generation " + (index + 1)
					+ "##########");
			System.out.println("Population size=" + currentPopulation.size());
			currentPopulation = Operators.selection(currentPopulation,
					fitnessFunction, parameterGA.getNumberFathers());
//			if (currentPopulation.get(0).getEvaluation() <= parameterGA
//					.getMaxError()) {
//				break;
//			}
			currentPopulation = Operators.reproduction(currentPopulation,
					parameterGA);
		}
		result = currentPopulation.get(0);
		return result;
	}

}
