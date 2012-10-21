package neural.network.util;

import com.syvys.jaRBM.Layers.LogisticLayer;

public class LogisticLayerMLP extends LogisticLayer {

	public LogisticLayerMLP(int numUnits) {
		super(numUnits);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public double[] getLayerDerivative(double[] value) {
		double[] derivative = new double[this.getNumUnits()];
		for (int index = 0; index < this.getNumUnits(); index++) {
			derivative[index] = derivative(value[index]);
		}
		return derivative;
	}

	private double functionActivity(double value) {
		return 1 / (1 + Math.exp(-value));
	}

	private double derivative(double value) {
		return functionActivity(value) * (1 - functionActivity(value));
	}

}
