package feature.selector.ga;

public class ParameterGA {

	private long numberGeneration;
	private int numberFathers;
	private double maxError;
	private int numberIndividualMutation;
	private int numberIndividualCrossing;

	public long getNumberGeneration() {
		return numberGeneration;
	}

	public void setNumberGeneration(long numberGeneration) {
		this.numberGeneration = numberGeneration;
	}

	public int getNumberFathers() {
		return numberFathers;
	}

	public void setNumberFathers(int numberFathers) {
		this.numberFathers = numberFathers;
	}

	public double getMaxError() {
		return maxError;
	}

	public void setMaxError(double maxError) {
		this.maxError = maxError;
	}

	public int getNumberIndividualMutation() {
		return numberIndividualMutation;
	}

	public void setNumberIndividualMutation(int numberIndividualMutation) {
		this.numberIndividualMutation = numberIndividualMutation;
	}

	public int getNumberIndividualCrossing() {
		return numberIndividualCrossing;
	}

	public void setNumberIndividualCrossing(int numberIndividualCrossing) {
		this.numberIndividualCrossing = numberIndividualCrossing;
	}
}
