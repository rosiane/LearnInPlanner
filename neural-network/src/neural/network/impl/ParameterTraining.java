package neural.network.impl;

public class ParameterTraining {

	private long numberEpochs;
	private int task;
	private double maxError;
	private double minLearningRate;
	private double learningRateDecrease;
	private boolean weightsInitializationRandom;
	private int intervalEpochPercentage;
	private int numberHiddenLayers;
	private int numberUnitHidden;
	private double momentum;
	private double learningRate;
	private int numberOutput;
	private boolean updateBatch;

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

	public int getNumberHiddenLayers() {
		return numberHiddenLayers;
	}

	public void setNumberHiddenLayers(int numberHiddenLayers) {
		this.numberHiddenLayers = numberHiddenLayers;
	}

	public int getNumberUnitHidden() {
		return numberUnitHidden;
	}

	public void setNumberUnitHidden(int numberUnitHidden) {
		this.numberUnitHidden = numberUnitHidden;
	}

	public double getMomentum() {
		return momentum;
	}

	public void setMomentum(double momentum) {
		this.momentum = momentum;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	public int getNumberOutput() {
		return numberOutput;
	}

	public void setNumberOutput(int numberOutput) {
		this.numberOutput = numberOutput;
	}

	public boolean isUpdateBatch() {
		return updateBatch;
	}

	public void setUpdateBatch(boolean updateBatch) {
		this.updateBatch = updateBatch;
	}
}
