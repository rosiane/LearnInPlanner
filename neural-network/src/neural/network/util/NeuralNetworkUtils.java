package neural.network.util;

import com.syvys.jaRBM.Layers.Layer;

import neural.network.enums.Task;
import neural.network.impl.ParameterTraining;
import neural.network.interfaces.NeuralNetworkIF;

import common.Data;
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
	
	public static ErrorRate calculateErrorRate(Layer[] net, Weight[] weights,
			Data data, ParameterTraining parameterTraining, NeuralNetworkIF network) {
		int countCorrect = 0;
		double errorSumRegression = 0;
		double errorRate = 100;
		double result[];
		for (int indexSample = 0; indexSample < MatrixHandler.rows(data
				.getSample()); indexSample++) {
			result = network.run(net, weights,
					MatrixHandler.getRow(data.getSample(), indexSample));
			if (parameterTraining.getTask() == Task.CLASSIFICATION.getValue()) {
				if (isCorrect(result,
						MatrixHandler.getRow(data.getLabel(), indexSample),
						parameterTraining.getTask())) {
					countCorrect++;
				}
			} else {
				errorSumRegression += MatrixHandler.getSquaredError(
						MatrixHandler.getRow(data.getLabel(), indexSample),
						result);
			}
		}

		if (parameterTraining.getTask() == Task.CLASSIFICATION.getValue()) {
			errorRate = 100 - ((double) countCorrect / MatrixHandler.rows(data
					.getSample())) * 100;
		} else {
			errorRate = errorSumRegression
					/ MatrixHandler.rows(data.getSample());
		}
		return new ErrorRate(countCorrect, errorRate);
	}
}
