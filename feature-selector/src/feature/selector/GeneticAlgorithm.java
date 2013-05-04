package feature.selector;

import java.io.IOException;
import java.util.List;

import common.preprocessor.file.FileManager;

import feature.selector.ga.Chromosome;
import feature.selector.ga.FitnessFunction;
import feature.selector.ga.Operators;
import feature.selector.ga.ParameterGA;

public class GeneticAlgorithm {
	private String resultFile;

	public GeneticAlgorithm() {

	}

	public GeneticAlgorithm(final String resultFile) {
		this.resultFile = resultFile;
	}

	public Chromosome run(final List<Chromosome> population,
			final FitnessFunction fitnessFunction, final ParameterGA parameterGA)
			throws IOException {
		Chromosome result = null;
		List<Chromosome> currentPopulation = population;
		for (int index = 0; index < parameterGA.getNumberGeneration(); index++) {
			if ((this.resultFile != null) && !this.resultFile.isEmpty()) {
				FileManager.write(this.resultFile, "########## Run Generation "
						+ (index + 1) + "##########", true);
				FileManager.write(this.resultFile, "Population size="
						+ currentPopulation.size(), true);
			} else {
				System.out.println("########## Run Generation " + (index + 1)
						+ "##########");
				System.out.println("Population size="
						+ currentPopulation.size());
			}
			currentPopulation = Operators.selection(currentPopulation,
					fitnessFunction, parameterGA.getNumberFathers(),
					this.resultFile);
			if (index < (parameterGA.getNumberGeneration() - 1)) {
				currentPopulation = Operators.reproduction(currentPopulation,
						parameterGA, this.resultFile);
			}
		}
		result = currentPopulation.get(0);
		return result;
	}

}
