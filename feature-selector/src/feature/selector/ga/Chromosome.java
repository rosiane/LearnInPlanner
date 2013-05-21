package feature.selector.ga;

import neural.network.util.Weight;

import common.MatrixHandler;

public class Chromosome {
	private int[] gene;
	private double evaluation;
	private Weight[] weights;
	private int expansionNumber;

	public double getEvaluation() {
		return this.evaluation;
	}

	public int getExpansionNumber() {
		return this.expansionNumber;
	}

	public int[] getGene() {
		return this.gene;
	}

	public Weight[] getWeights() {
		return this.weights;
	}

	public void mutation(final int index) {
		if (this.gene[index] == 0) {
			this.gene[index] = 1;
		} else {
			this.gene[index] = 0;
		}
	}

	public void setEvaluation(final double evaluation) {
		this.evaluation = evaluation;
	}

	public void setExpansionNumber(final int expansionNumber) {
		this.expansionNumber = expansionNumber;
	}

	public void setGene(final int[] gene) {
		this.gene = gene;
	}

	public void setWeights(final Weight[] weights) {
		this.weights = weights;
	}

	@Override
	public String toString() {
		final String text = "gene=[" + MatrixHandler.toStringArray(this.gene)
				+ "], evaluation=[" + this.evaluation + "], expansionNumber=["
				+ this.expansionNumber + "]";
		return text;
	}

}
