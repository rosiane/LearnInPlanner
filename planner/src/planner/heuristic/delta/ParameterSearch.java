package planner.heuristic.delta;

import java.util.LinkedList;

import neural.network.interfaces.NeuralNetworkIF;
import neural.network.util.Weight;

import com.syvys.jaRBM.Layers.Layer;
import common.feature.ClassExpression;

public class ParameterSearch {

	private LinkedList<ClassExpression> features;
	private NeuralNetworkIF neuralNetwork;
	private Layer[] net;
	private Weight[] weights;

	public LinkedList<ClassExpression> getFeatures() {
		return this.features;
	}

	public Layer[] getNet() {
		return this.net;
	}

	public NeuralNetworkIF getNeuralNetwork() {
		return this.neuralNetwork;
	}

	public Weight[] getWeights() {
		return this.weights;
	}

	public void setFeatures(final LinkedList<ClassExpression> features) {
		this.features = features;
	}

	public void setNet(final Layer[] net) {
		this.net = net;
	}

	public void setNeuralNetwork(final NeuralNetworkIF neuralNetwork) {
		this.neuralNetwork = neuralNetwork;
	}

	public void setWeights(final Weight[] weights) {
		this.weights = weights;
	}

}
