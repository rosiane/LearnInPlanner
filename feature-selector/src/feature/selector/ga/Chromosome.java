package feature.selector.ga;

import common.MatrixHandler;

public class Chromosome {

	private int[] gene;
	private double evaluation;

	public double getEvaluation() {
		return this.evaluation;
	}

	public int[] getGene() {
		return this.gene;
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

	public void setGene(final int[] gene) {
		this.gene = gene;
	}

	@Override
	public String toString() {
		final String text = "gene=[" + MatrixHandler.toStringArray(this.gene)
				+ "], evaluation=[" + this.evaluation + "]";
		return text;
	}

}
