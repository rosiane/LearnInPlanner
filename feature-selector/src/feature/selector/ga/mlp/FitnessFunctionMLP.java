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
import common.MatrixHandler;
import common.feature.GeneValueEnum;
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
	private Data dataTraining;
	private Data dataValidation;
	private Data dataTest;

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
			initializeNetwork(numberAttribute);
			dataTraining = readerFeature.readTraining(chromosome.getGene());
			dataValidation = readerFeature.readValidation(chromosome.getGene());
			dataTest = readerFeature.readTest(chromosome.getGene());
			if (parameterTraining.isNormalizeLabel()) {
				normalizeLabel();
			}
			final Weight[] result = train(chromosome.getGene());
			evaluation = test(result, chromosome.getGene());
			chromosome.setWeights(weights);
		}
		return evaluation;
	}

	private void initializeNetwork(final int numberAttribute) {
		net = new LogisticLayer[parameterTraining.getNumberHiddenLayers() + 1];
		int numberUnitHidden = parameterTraining.getNumberUnitHidden();
		if (parameterTraining.isUseHeuristicUnitHidden()) {
			final double[] array = { numberAttribute,
					parameterTraining.getNumberOutput() };
			numberUnitHidden = (int) MatrixHandler.mean(array);
		}
		for (int index = 0; index < net.length; index++) {
			if (index == (net.length - 1)) {
				net[index] = new LogisticLayer(
						parameterTraining.getNumberOutput());
			} else {
				net[index] = new LogisticLayer(numberUnitHidden);
			}
			net[index].setMomentum(parameterTraining.getMomentum());
			net[index].setLearningRate(parameterTraining.getLearningRate());
		}

		weights = new Weight[parameterTraining.getNumberHiddenLayers() + 1];

		for (int index = 0; index < weights.length; index++) {
			if (index == 0) {
				weights[index] = new Weight(numberAttribute, numberUnitHidden);
			} else if (index == (weights.length - 1)) {
				weights[index] = new Weight(numberUnitHidden,
						parameterTraining.getNumberOutput());
			} else {
				weights[index] = new Weight(numberUnitHidden, numberUnitHidden);
			}
		}
		neuralNetwork = new MLP();
	}

	private void normalizeLabel() {
		final double[][] allLabel = new double[MatrixHandler.rows(dataTraining
				.getLabel())
				+ MatrixHandler.rows(dataValidation.getLabel())
				+ MatrixHandler.rows(dataTest.getLabel())][MatrixHandler
				                                           .cols(dataTraining.getLabel())];
		int indexRowNormalized = 0;
		for (int indexRow = 0; indexRow < dataTraining.getLabel().length; indexRow++) {
			MatrixHandler.setRow(allLabel,
					MatrixHandler.getRow(dataTraining.getLabel(), indexRow),
					indexRowNormalized);
			indexRowNormalized++;
		}
		for (int indexRow = 0; indexRow < dataValidation.getLabel().length; indexRow++) {
			MatrixHandler.setRow(allLabel,
					MatrixHandler.getRow(dataValidation.getLabel(), indexRow),
					indexRowNormalized);
			indexRowNormalized++;
		}
		for (int indexRow = 0; indexRow < dataTest.getLabel().length; indexRow++) {
			MatrixHandler.setRow(allLabel,
					MatrixHandler.getRow(dataTest.getLabel(), indexRow),
					indexRowNormalized);
			indexRowNormalized++;
		}
		final double[] normal = MatrixHandler.normal(allLabel);
		dataTraining.setLabel(MatrixHandler.normalizeCols(
				dataTraining.getLabel(), normal));
		dataValidation.setLabel(MatrixHandler.normalizeCols(
				dataValidation.getLabel(), normal));
		dataTest.setLabel(MatrixHandler.normalizeCols(dataTest.getLabel(),
				normal));
	}

	private double test(final Weight[] weights, final int[] indexes)
			throws IOException {
		dataTest = MatrixHandler.randomize(dataTest.getSample(),
				dataTest.getLabel());
		final ErrorRate errorRate = NeuralNetworkUtils.calculateErrorRate(net,
				weights, dataTest, parameterTraining, neuralNetwork);
		if ((resultFile != null) && !resultFile.isEmpty()) {
			FileManager.write(resultFile,
					"Error Rate Test " + errorRate.getErrorRate(), true);
		} else {
			System.out.println("Error Rate Test " + errorRate.getErrorRate());
		}
		return errorRate.getErrorRate();
	}

	private Weight[] train(final int[] indexes) throws IOException {
		Weight[] update = weights.clone();
		update = neuralNetwork.train(net, update, dataTraining.getSample(),
				dataTraining.getLabel(), parameterTraining, dataValidation,
				resultFile);
		return update;
	}

}
