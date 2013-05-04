package feature.selector.ga.mlp;

import java.io.IOException;

import neural.network.impl.MLP;
import neural.network.impl.ParameterTraining;
import neural.network.interfaces.NeuralNetworkIF;
import neural.network.util.ErrorRate;
import neural.network.util.NeuralNetworkUtils;
import neural.network.util.Weight;

import com.syvys.jaRBM.Layers.Layer;
import com.syvys.jaRBM.Layers.LogisticLayer;
import common.Data;
import common.GeneValueEnum;
import common.MatrixHandler;
import common.preprocessor.file.FileManager;
import common.preprocessor.file.ReaderFeature;

import feature.selector.ga.Chromosome;
import feature.selector.ga.FitnessFunction;

public class FitnessFunctionMLP implements FitnessFunction {
	private final ReaderFeature readerFeature;
	private NeuralNetworkIF neuralNetwork;
	private Layer[] net;
	private Weight[] weights;
	private final ParameterTraining parameterTraining;
	private final String resultFile;

	public FitnessFunctionMLP(final ReaderFeature readerFeature,
			final ParameterTraining parameterTraining, final String resultFile) {
		this.readerFeature = readerFeature;
		this.parameterTraining = parameterTraining;
		this.resultFile = resultFile;
	}

	@Override
	public double evaluate(final Chromosome chromosome) throws IOException {
		double evaluation = 0;
		if (MatrixHandler.isAllValue(chromosome.getGene(),
				GeneValueEnum.FALSE.value())) {
			evaluation = 100;
		} else {
			final int numberAttribute = MatrixHandler.countValue(
					chromosome.getGene(), GeneValueEnum.TRUE.value());
			this.initializeNetwork(numberAttribute);
			final Weight[] result = this.train(chromosome.getGene());
			evaluation = this.test(result, chromosome.getGene());
		}
		return evaluation;
	}

	private void initializeNetwork(final int numberAttribute) {
		this.net = new LogisticLayer[this.parameterTraining
				.getNumberHiddenLayers() + 1];
		int numberUnitHidden = this.parameterTraining.getNumberUnitHidden();
		if (this.parameterTraining.isUseHeuristicUnitHidden()) {
			final double[] array = { numberAttribute,
					this.parameterTraining.getNumberOutput() };
			numberUnitHidden = (int) MatrixHandler.mean(array);
		}
		for (int index = 0; index < this.net.length; index++) {
			if (index == (this.net.length - 1)) {
				this.net[index] = new LogisticLayer(
						this.parameterTraining.getNumberOutput());
			} else {
				this.net[index] = new LogisticLayer(numberUnitHidden);
			}
			this.net[index].setMomentum(this.parameterTraining.getMomentum());
			this.net[index].setLearningRate(this.parameterTraining
					.getLearningRate());
		}

		this.weights = new Weight[this.parameterTraining
				.getNumberHiddenLayers() + 1];

		for (int index = 0; index < this.weights.length; index++) {
			if (index == 0) {
				this.weights[index] = new Weight(numberAttribute,
						numberUnitHidden);
			} else if (index == (this.weights.length - 1)) {
				this.weights[index] = new Weight(numberUnitHidden,
						this.parameterTraining.getNumberOutput());
			} else {
				this.weights[index] = new Weight(numberUnitHidden,
						numberUnitHidden);
			}
		}
		this.neuralNetwork = new MLP();
	}

	private double test(final Weight[] weights, final int[] indexes)
			throws IOException {
		Data dataTest = this.readerFeature.readTest(indexes);
		dataTest = MatrixHandler.randomize(dataTest.getSample(),
				dataTest.getLabel());
		final ErrorRate errorRate = NeuralNetworkUtils.calculateErrorRate(
				this.net, weights, dataTest, this.parameterTraining,
				this.neuralNetwork);
		if ((this.resultFile != null) && !this.resultFile.isEmpty()) {
			FileManager.write(this.resultFile,
					"Error Rate Test " + errorRate.getErrorRate(), true);
		} else {
			System.out.println("Error Rate Test " + errorRate.getErrorRate());
		}
		return errorRate.getErrorRate();
	}

	private Weight[] train(final int[] indexes) throws IOException {
		final Data dataTraining = this.readerFeature.readTraining(indexes);
		final Data dataValidation = this.readerFeature.readValidation(indexes);
		Weight[] update = this.weights.clone();
		update = this.neuralNetwork.train(this.net, update,
				dataTraining.getSample(), dataTraining.getLabel(),
				this.parameterTraining, dataValidation, this.resultFile);
		return update;
	}

}
