package neural.network.impl;

public class ParameterTraining {

	private long numberEpochs;
	private int task;
	private double maxError;
	private double minLearningRate;
	private double learningRateDecrease;
	private boolean weightsInitializationRandom;
	private int intervalEpochPercentage;

	public long getNumberEpochs() {
		return numberEpochs;
	}

	public void setNumberEpochs(long number) {
		numberEpochs = number;
	}

	public int getTask() {
		return task;
	}

	public void setTask(int task) {
		this.task = task;
	}

	public double getMaxError() {
		return maxError;
	}

	public void setMaxError(double maxError) {
		this.maxError = maxError;
	}

	public double getMinLearningRate() {
		return minLearningRate;
	}

	public void setMinLearningRate(double minLearningRate) {
		this.minLearningRate = minLearningRate;
	}

	public double getLearningRateDecrease() {
		return learningRateDecrease;
	}

	public void setLearningRateDecrease(double learningRateDecrease) {
		this.learningRateDecrease = learningRateDecrease;
	}

	public boolean isWeightsInitializationRandom() {
		return weightsInitializationRandom;
	}

	public void setWeightsInitializationRandom(
			boolean weightsInitializationRandom) {
		this.weightsInitializationRandom = weightsInitializationRandom;
	}

	public int getIntervalEpochPercentage() {
		return intervalEpochPercentage;
	}

	public void setIntervalEpochPercentage(int intervalEpochPercentage) {
		this.intervalEpochPercentage = intervalEpochPercentage;
	}
}
