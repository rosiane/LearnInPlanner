package neural.network.util;

import com.syvys.jaRBM.Layers.Layer;
import com.syvys.jaRBM.Math.Matrix;

public class HyperbolicTangentLayer extends Layer {

	public HyperbolicTangentLayer(int numUnits) {
		super(numUnits);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public double[] getLayerDerivative(double[] layerActivities) {
		double[] derivative = new double[this.getNumUnits()];
		for (int i = 0; i < this.getNumUnits(); i++) {
			derivative[i] = (1 - Math.pow(layerActivities[i], 2));
		}
		return derivative;
	}

	@Override
	public double[][] getActivationProbabilities(double[][] sw_sum) {
		double[][] activation_probabilities = sw_sum;
		// for each data vector
		for (int ithDataVector = 0; ithDataVector < sw_sum.length; ithDataVector++) {
			for (int j = 0; j < this.getNumUnits(); j++) {
				activation_probabilities[ithDataVector][j] = Math
						.tanh(sw_sum[ithDataVector][j] - this.biases[j]);
			}
		}
		return activation_probabilities;
	}

	public double[][] generateData(double[][] activation_probabilities) {
		return Matrix.clone(activation_probabilities);
	}

	public HyperbolicTangentLayer clone() {
		return (HyperbolicTangentLayer) super.clone();
	}

}
