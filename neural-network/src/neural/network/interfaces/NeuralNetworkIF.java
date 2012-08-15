package neural.network.interfaces;

import neural.network.impl.ParameterTraining;
import neural.network.util.Weight;

import com.syvys.jaRBM.Layers.Layer;

/**
 * 
 * @author rosy
 * 
 */
public interface NeuralNetworkIF {
	public Weight[] train(Layer[] net, Weight[] weights, double[][] sample,
			double[][] sampleLabel, ParameterTraining parameterTraining);

	public double[] run(Layer[] net, Weight[] weights, double[] data);

}
