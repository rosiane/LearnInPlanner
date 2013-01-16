package feature.selector.ga.mlp;

import java.io.IOException;

import neural.network.impl.MLP;
import neural.network.impl.ParameterTraining;
import neural.network.interfaces.NeuralNetworkIF;
import neural.network.util.LogisticLayerMLP;
import neural.network.util.NeuralNetworkUtils;
import neural.network.util.Weight;
import preprocessor.file.ReaderFeature;

import com.syvys.jaRBM.Layers.Layer;
import com.syvys.jaRBM.Layers.LogisticLayer;
import common.Data;
import common.MatrixHandler;

import feature.selector.ga.Chromosome;
import feature.selector.ga.FitnessFunction;

public class FitnessFunctionMLP implements FitnessFunction {
	private ReaderFeature readerFeature;
	private NeuralNetworkIF neuralNetwork;
	private Layer[] net;
	private Weight[] weights;
	private ParameterTraining parameterTraining;

	/**
	 * Avoiding create this object without mandatory attributes
	 */
	private FitnessFunctionMLP() {

	}

	public FitnessFunctionMLP(ReaderFeature readerFeature,
			ParameterTraining parameterTraining) {
		this.readerFeature = readerFeature;
		this.parameterTraining = parameterTraining;
	}

	@Override
	public double evaluate(Chromosome chromosome) throws IOException {
		double evaluation = 0;
		if (MatrixHandler.isAllValue(chromosome.getGene(), 0)) {
			evaluation = 100;
		} else {
			int numberAttribute = MatrixHandler.countValue(
					chromosome.getGene(), 1);
			initializeNetwork(numberAttribute);
			Weight[] result = train(chromosome.getGene());
			evaluation = test(result, chromosome.getGene());
		}
		return evaluation;
	}

	private void initializeNetwork(int numberAttribute) {
		net = new LogisticLayerMLP[parameterTraining.getNumberHiddenLayers() + 1];
		for (int index = 0; index < net.length; index++) {
			if (index == net.length - 1) {
				net[index] = new LogisticLayerMLP(
						parameterTraining.getNumberOutput());
			} else {
				net[index] = new LogisticLayerMLP(
						parameterTraining.getNumberUnitHidden());
			}
			net[index].setMomentum(parameterTraining.getMomentum());
			net[index].setLearningRate(parameterTraining.getLearningRate());
		}

		weights = new Weight[parameterTraining.getNumberHiddenLayers() + 1];

		for (int index = 0; index < weights.length; index++) {
			if (index == 0) {
				weights[index] = new Weight(numberAttribute,
						parameterTraining.getNumberUnitHidden());
			} else if (index == weights.length - 1) {
				weights[index] = new Weight(
						parameterTraining.getNumberUnitHidden(),
						parameterTraining.getNumberOutput());
			} else {
				weights[index] = new Weight(
						parameterTraining.getNumberUnitHidden(),
						parameterTraining.getNumberUnitHidden());
			}
		}
		neuralNetwork = new MLP();
	}

	private Weight[] train(int[] indexes) throws IOException {
		Data dataTraining = readerFeature.readTraining(indexes);
		Data dataValidation = readerFeature.readValidation(indexes);
		Weight[] update = weights.clone();
		// TODO alterar para passar o nome do arquivo de resultado
		update = neuralNetwork.train(net, update, dataTraining.getSample(),
				dataTraining.getLabel(), parameterTraining, dataValidation, null);
		return update;
	}

	private double test(Weight[] weights, int[] indexes) throws IOException {
		Data dataTest = readerFeature.readTest(indexes);
		dataTest = MatrixHandler.randomize(dataTest.getSample(),
				dataTest.getLabel());
		double numberCorrect = 0;
		double[] result = null;
		for (int indexData = 0; indexData < MatrixHandler.rows(dataTest
				.getSample()); indexData++) {
			result = neuralNetwork.run(net, weights,
					MatrixHandler.getRow(dataTest.getSample(), indexData));
			if (NeuralNetworkUtils.isCorrect(result,
					MatrixHandler.getRow(dataTest.getLabel(), indexData),
					parameterTraining.getTask())) {
				numberCorrect++;
			}
		}
		double errorRate = 100 - ((double) numberCorrect / MatrixHandler
				.rows(dataTest.getSample())) * 100;
		System.out.println("Error Rate Test " + errorRate);
		return errorRate;
	}

}
