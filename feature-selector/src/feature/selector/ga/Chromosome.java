package feature.selector.ga;

public class Chromosome {

	private int[] gene;
	private double evaluation;

	public int[] getGene() {
		return gene;
	}

	public void setGene(int[] gene) {
		this.gene = gene;
	}

	public double getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(double evaluation) {
		this.evaluation = evaluation;
	}

	public void mutation(int index) {
		if (gene[index] == 0) {
			gene[index] = 1;
		} else {
			gene[index] = 0;
		}
	}

	
}
