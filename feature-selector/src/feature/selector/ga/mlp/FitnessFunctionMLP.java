package feature.selector.ga.mlp;

import java.io.IOException;

import neural.network.impl.ParameterTraining;
import neural.network.interfaces.NeuralNetworkIF;
import neural.network.util.NeuralNetworkUtils;
import neural.network.util.Weight;
import preprocessor.file.ReaderFeature;

import com.syvys.jaRBM.Layers.Layer;
import common.Data;
import common.MatrixHandler;

import feature.selector.ga.Chromosome;
import feature.selector.ga.FitnessFunction;

public class FitnessFunctionMLP implements FitnessFunction {
	private ReaderFeature readerFeature;
	private NeuralNetworkIF neuralNetwork;
	private Layer[] net;
	private Weight[] weights;
	private int numberInput;
	private int numberOutput;
	private ParameterTraining parameterTraining;

	/**
	 * Avoiding create this object without mandatory attributes
	 */
	private FitnessFunctionMLP() {

	}

	public FitnessFunctionMLP(ReaderFeature readerFeature) {
		this.readerFeature = readerFeature;
	}

	@Override
	public double evaluate(Chromosome chromosome) throws IOException {
		int numberAttribute = chromosome.countGene(1);
		Weight[] result = train(neuralNetwork, net, weights, numberInput,
				numberAttribute, numberOutput, parameterTraining,
				chromosome.getGene());
		double evaluation = test(neuralNetwork, net, result, numberAttribute,
				numberAttribute, numberAttribute, parameterTraining,
				chromosome.getGene());
		return evaluation;
	}

	private Weight[] train(NeuralNetworkIF neuralNetwork, Layer[] net,
			Weight[] weights, int numberInput, int numberAttribute,
			int numberOutput, ParameterTraining parameterTraining, int[] indexes)
			throws IOException {
		Data dataTraining = new Data();
		double[][] sample = new double[numberInput][numberAttribute];
		dataTraining.setSample(sample);
		double[][] label = new double[numberInput][numberOutput];
		dataTraining.setLabel(label);

		dataTraining = readerFeature.readTraining(indexes);
		dataTraining = MatrixHandler.randomize(dataTraining.getSample(),
				dataTraining.getLabel());
		Weight[] update = weights.clone();
		update = neuralNetwork.train(net, update, dataTraining.getSample(),
				dataTraining.getLabel(), parameterTraining);
		return update;
	}

	private double test(NeuralNetworkIF neuralNetwork, Layer[] net,
			Weight[] weights, int numberInput, int numberAttribute,
			int numberOutput, ParameterTraining parameterTraining, int[] indexes)
			throws IOException {
		Data dataTest = new Data();
		double[][] sample = new double[numberInput][numberAttribute];
		dataTest.setSample(sample);
		double[][] label = new double[numberInput][numberOutput];
		dataTest.setLabel(label);

		dataTest = readerFeature.readTest(indexes);
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
				.rows(sample)) * 100;
		return errorRate;
	}

}
