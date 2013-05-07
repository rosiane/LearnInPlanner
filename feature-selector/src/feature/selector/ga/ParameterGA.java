package feature.selector.ga;

public class ParameterGA {

	private long numberGeneration;
	private int numberFathers;
	private double mutationRate;
	private int numberIndividualCrossing;
	private double maxEvaluation;

	public double getMutationRate() {
		return mutationRate;
	}

	public int getNumberFathers() {
		return numberFathers;
	}

	public long getNumberGeneration() {
		return numberGeneration;
	}

	public int getNumberIndividualCrossing() {
		return numberIndividualCrossing;
	}

	public void setMutationRate(final double mutationRate) {
		this.mutationRate = mutationRate;
	}

	public void setNumberFathers(final int numberFathers) {
		this.numberFathers = numberFathers;
	}

	public void setNumberGeneration(final long numberGeneration) {
		this.numberGeneration = numberGeneration;
	}

	public void setNumberIndividualCrossing(final int numberIndividualCrossing) {
		this.numberIndividualCrossing = numberIndividualCrossing;
	}

	public double getMaxEvaluation() {
		return maxEvaluation;
	}

	public void setMaxEvaluation(double maxEvaluation) {
		this.maxEvaluation = maxEvaluation;
	}
}
