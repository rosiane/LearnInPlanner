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
	private boolean normalizeWeights;
	private boolean validation;
	private boolean useHeuristicUnitHidden;
	private boolean normalizeLabel;

	public int getIntervalEpochPercentage() {
		return intervalEpochPercentage;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public double getLearningRateDecrease() {
		return learningRateDecrease;
	}

	public double getMaxError() {
		return maxError;
	}

	public double getMinLearningRate() {
		return minLearningRate;
	}

	public double getMomentum() {
		return momentum;
	}

	public long getNumberEpochs() {
		return numberEpochs;
	}

	public int getNumberHiddenLayers() {
		return numberHiddenLayers;
	}

	public int getNumberOutput() {
		return numberOutput;
	}

	public int getNumberUnitHidden() {
		return numberUnitHidden;
	}

	public int getTask() {
		return task;
	}

	public boolean isNormalizeLabel() {
		return normalizeLabel;
	}

	public boolean isNormalizeWeights() {
		return normalizeWeights;
	}

	public boolean isUpdateBatch() {
		return updateBatch;
	}

	public boolean isUseHeuristicUnitHidden() {
		return useHeuristicUnitHidden;
	}

	public boolean isValidation() {
		return validation;
	}

	public boolean isWeightsInitializationRandom() {
		return weightsInitializationRandom;
	}

	public void setIntervalEpochPercentage(final int intervalEpochPercentage) {
		this.intervalEpochPercentage = intervalEpochPercentage;
	}

	public void setLearningRate(final double learningRate) {
		this.learningRate = learningRate;
	}

	public void setLearningRateDecrease(final double learningRateDecrease) {
		this.learningRateDecrease = learningRateDecrease;
	}

	public void setMaxError(final double maxError) {
		this.maxError = maxError;
	}

	public void setMinLearningRate(final double minLearningRate) {
		this.minLearningRate = minLearningRate;
	}

	public void setMomentum(final double momentum) {
		this.momentum = momentum;
	}

	public void setNormalizeLabel(final boolean normalizeLabel) {
		this.normalizeLabel = normalizeLabel;
	}

	public void setNormalizeWeights(final boolean normalizeWeights) {
		this.normalizeWeights = normalizeWeights;
	}

	public void setNumberEpochs(final long number) {
		numberEpochs = number;
	}

	public void setNumberHiddenLayers(final int numberHiddenLayers) {
		this.numberHiddenLayers = numberHiddenLayers;
	}

	public void setNumberOutput(final int numberOutput) {
		this.numberOutput = numberOutput;
	}

	public void setNumberUnitHidden(final int numberUnitHidden) {
		this.numberUnitHidden = numberUnitHidden;
	}

	public void setTask(final int task) {
		this.task = task;
	}

	public void setUpdateBatch(final boolean updateBatch) {
		this.updateBatch = updateBatch;
	}

	public void setUseHeuristicUnitHidden(final boolean useHeuristicUnitHidden) {
		this.useHeuristicUnitHidden = useHeuristicUnitHidden;
	}

	public void setValidation(final boolean validation) {
		this.validation = validation;
	}

	public void setWeightsInitializationRandom(
			final boolean weightsInitializationRandom) {
		this.weightsInitializationRandom = weightsInitializationRandom;
	}
}
