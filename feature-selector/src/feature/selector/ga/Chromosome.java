package feature.selector.ga;


import neural.network.util.Weight;
import common.MatrixHandler;

public class Chromosome {
	private int[] gene;
	private double evaluation;
	private Weight[] weights;

	public double getEvaluation() {
		return evaluation;
	}

	public int[] getGene() {
		return gene;
	}

	public Weight[] getWeights() {
		return weights;
	}

	public void mutation(final int index) {
		if (gene[index] == 0) {
			gene[index] = 1;
		} else {
			gene[index] = 0;
		}
	}

	public void setEvaluation(final double evaluation) {
		this.evaluation = evaluation;
	}

	public void setGene(final int[] gene) {
		this.gene = gene;
	}

	public void setWeights(final Weight[] weights) {
		this.weights = weights;
	}

	@Override
	public String toString() {
		final String text = "gene=[" + MatrixHandler.toStringArray(gene)
				+ "], evaluation=[" + evaluation + "]";
		return text;
	}

}
