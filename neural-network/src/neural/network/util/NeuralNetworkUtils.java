package neural.network.util;

import neural.network.enums.Task;

import common.MatrixHandler;

public class NeuralNetworkUtils {

	public static boolean isCorrect(double[] result, double[] sampleLabel,
			int task) {
		if (task == Task.CLASSIFICATION.getValue()) {
			int indexMaxResult = MatrixHandler.maxNumber(result);
			int indexMaxLabel = MatrixHandler.maxNumber(sampleLabel);
			if (indexMaxResult == indexMaxLabel) {
				return true;
			}
		}
		return false;
	}

	public static double[] parseBinary(double[] data) {
		int indexMax = MatrixHandler.maxNumber(data);
		double[] result = new double[data.length];
		result[indexMax] = 1;
		return result;
	}
}
