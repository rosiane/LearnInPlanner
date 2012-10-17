package common;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import com.syvys.jaRBM.Math.Matrix;

public class MatrixHandler extends Matrix {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static double[] subtract(double[] leftSide, double[] rightSide) {
		double[] result = new double[leftSide.length];

		for (int index = 0; index < leftSide.length; index++) {
			result[index] = leftSide[index] - rightSide[index];
		}
		return result;
	}

	public static double[] sum(double[] leftSide, double[] rightSide) {
		double[] result = new double[leftSide.length];

		for (int index = 0; index < leftSide.length; index++) {
			result[index] = leftSide[index] + rightSide[index];
		}
		return result;
	}

	public static double[] multiply(double[] leftSide, double[] rightSide) {
		double[] result = new double[leftSide.length];
		for (int index = 0; index < leftSide.length; index++) {
			result[index] = leftSide[index] * rightSide[index];
		}
		return result;
	}

	public static double[] multiply(double[] vector, double number) {
		return getRow(multiply(new double[][] { vector }, number), 0);
	}
	
	public static double[] division(double[] vector, double number) {
		double[] result = new double[vector.length];
		for (int index = 0; index < vector.length; index++) {
			result[index] = vector[index] / number;
		}
		return result;
	}

	public static double[] parseDouble(String[] matrix) {
		double[] result = new double[matrix.length];
		for (int index = 0; index < result.length; index++) {
			result[index] = Double.parseDouble(matrix[index]);
		}
		return result;
	}

	public static void setRow(double[][] matrix, double[] row, int index) {
		matrix[index] = row;
	}

	public static int maxNumber(double[] vector) {
		int indexMax = 0;
		for (int index = 0; index < vector.length; index++) {
			if (vector[index] > vector[indexMax]) {
				indexMax = index;
			}
		}
		return indexMax;
	}

	public static Data randomize(double[][] data, double[][] label) {

		for (int i = 0; i < data.length; i++) {
			int r = i + (int) (Math.random() * (data.length - i));
			double[] t = data[r];
			data[r] = data[i];
			data[i] = t;
			t = label[r];
			label[r] = label[i];
			label[i] = t;
		}
		Data randomResult = new Data();
		randomResult.setSample(data);
		randomResult.setLabel(label);
		return randomResult;
	}

	public static double[][] normalizeCols(double[][] data) {
		double normal = 0;
		double[][] dataNormalized = new double[rows(data)][cols(data)];
		for (int indexCols = 0; indexCols < cols(data); indexCols++) {
			normal = normal(getCol(data, indexCols));
			for (int indexRows = 0; indexRows < rows(data); indexRows++) {
				dataNormalized[indexRows][indexCols] = data[indexRows][indexCols]
						/ normal;
			}
		}
		return dataNormalized;
	}

	public static double normal(double[] data) {
		double normal = 0;
		for (double value : data) {
			normal += Math.pow(value, 2);
		}
		return Math.sqrt(normal);
	}

	public static double[][] patternizeCols(double[][] data) {
		double mean = 0;
		double standardDeviation = 0;
		double[][] dataPatternized = new double[rows(data)][cols(data)];
		for (int indexCols = 0; indexCols < cols(data); indexCols++) {
			mean = mean(getCol(data, indexCols));
			standardDeviation = standardDeviation(getCol(data, indexCols), mean);
			for (int indexRows = 0; indexRows < rows(data); indexRows++) {
				dataPatternized[indexRows][indexCols] = (data[indexRows][indexCols] - mean)
						/ standardDeviation;
			}
		}
		return dataPatternized;
	}

	public static double mean(double[] data) {
		return new Mean().evaluate(data);
	}

	public static double standardDeviation(double[] data, double mean) {
		return new StandardDeviation().evaluate(data, mean);
	}

	public static double[][] getSubmatrixRows(double[][] data, int firstRow,
			int maxRow) {
		double[][] result = new double[maxRow][cols(data)];
		int indexOrigin = firstRow;
		for (int indexRow = 0; indexRow < maxRow; indexRow++, indexOrigin++) {
			result[indexRow] = getRow(data, indexOrigin);
		}
		return result;
	}

	public static int countValue(int[] array, int value) {
		int count = 0;
		for (int index = 0; index < array.length; index++) {
			if (array[index] == value) {
				count++;
			}
		}
		return count;
	}

	public static boolean isAllValue(int[] array, int value) {
		for (int index = 0; index < array.length; index++) {
			if (array[index] != value) {
				return false;
			}
		}
		return true;
	}
}
