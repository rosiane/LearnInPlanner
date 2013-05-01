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
import common.preprocessor.file.ReaderFeature;

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
		net = new LogisticLayer[parameterTraining.getNumberHiddenLayers() + 1];
		for (int index = 0; index < net.length; index++) {
			if (index == net.length - 1) {
				net[index] = new LogisticLayer(
						parameterTraining.getNumberOutput());
			} else {
				net[index] = new LogisticLayer(
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
				dataTraining.getLabel(), parameterTraining, dataValidation,
				null);
		return update;
	}

	private double test(Weight[] weights, int[] indexes) throws IOException {
		Data dataTest = readerFeature.readTest(indexes);
		dataTest = MatrixHandler.randomize(dataTest.getSample(),
				dataTest.getLabel());
		ErrorRate errorRate = NeuralNetworkUtils.calculateErrorRate(net,
				weights, dataTest, parameterTraining, neuralNetwork);
		System.out.println("Error Rate Test " + errorRate.getErrorRate());
		return errorRate.getErrorRate();
	}

}
