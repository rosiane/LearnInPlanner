package common;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import com.syvys.jaRBM.Math.Matrix;

public class MatrixHandler extends Matrix {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static double[] abs(final double[] array) {
		final double[] result = new double[array.length];
		for (int index = 0; index < array.length; index++) {
			result[index] = Math.abs(array[index]);
		}
		return result;
	}

	public static int countValue(final int[] array, final int value) {
		int count = 0;
		for (int index = 0; index < array.length; index++) {
			if (array[index] == value) {
				count++;
			}
		}
		return count;
	}

	public static double[] division(final double[] vector, final double number) {
		final double[] result = new double[vector.length];
		for (int index = 0; index < vector.length; index++) {
			result[index] = vector[index] / number;
		}
		return result;
	}

	public static double[][] getSubmatrixRows(final double[][] data,
			final int firstRow, final int maxRow) {
		final double[][] result = new double[maxRow][cols(data)];
		int indexOrigin = firstRow;
		for (int indexRow = 0; indexRow < maxRow; indexRow++, indexOrigin++) {
			result[indexRow] = getRow(data, indexOrigin);
		}
		return result;
	}

	public static boolean isAllValue(final int[] array, final int value) {
		for (int index = 0; index < array.length; index++) {
			if (array[index] != value) {
				return false;
			}
		}
		return true;
	}

	public static int maxNumber(final double[] vector) {
		int indexMax = 0;
		for (int index = 0; index < vector.length; index++) {
			if (vector[index] > vector[indexMax]) {
				indexMax = index;
			}
		}
		return indexMax;
	}

	public static double mean(final double[] data) {
		return new Mean().evaluate(data);
	}

	public static double[] multiply(final double[] vector, final double number) {
		return getRow(multiply(new double[][] { vector }, number), 0);
	}

	public static double[] multiply(final double[] leftSide,
			final double[] rightSide) {
		final double[] result = new double[leftSide.length];
		for (int index = 0; index < leftSide.length; index++) {
			result[index] = leftSide[index] * rightSide[index];
		}
		return result;
	}

	public static double[] multiply(final double[] inputsLayer,
			final double[][] weights) {
		final double[] result = new double[MatrixHandler.rows(weights)];
		for (int rows = 0; rows < MatrixHandler.rows(weights); rows++) {
			for (int cols = 0; cols < MatrixHandler.cols(weights); cols++) {
				result[rows] += inputsLayer[cols] * weights[rows][cols];
			}
		}
		return result;
	}

	public static double[] multiplySum(final double[][] matrix,
			final double[] vector) {
		final double[] result = new double[MatrixHandler.rows(matrix)];
		for (int rows = 0; rows < MatrixHandler.rows(matrix); rows++) {
			for (int cols = 0; cols < MatrixHandler.cols(matrix); cols++) {
				result[rows] += matrix[rows][cols] * vector[cols];
			}
		}
		return result;
	}

	public static double normal(final double[] data) {
		double normal = 0;
		for (final double value : data) {
			normal += Math.pow(value, 2);
		}
		return Math.sqrt(normal);
	}

	public static double[][] normalizeCols(final double[][] data) {
		double normal = 0;
		final double[][] dataNormalized = new double[rows(data)][cols(data)];
		for (int indexCols = 0; indexCols < cols(data); indexCols++) {
			normal = normal(getCol(data, indexCols));
			for (int indexRows = 0; indexRows < rows(data); indexRows++) {
				dataNormalized[indexRows][indexCols] = data[indexRows][indexCols]
						/ normal;
			}
		}
		return dataNormalized;
	}

	public static double[] parseDouble(final String[] matrix) {
		final double[] result = new double[matrix.length];
		for (int index = 0; index < result.length; index++) {
			result[index] = Double.parseDouble(matrix[index]);
		}
		return result;
	}

	public static double[][] patternizeCols(final double[][] data) {
		double mean = 0;
		double standardDeviation = 0;
		final double[][] dataPatternized = new double[rows(data)][cols(data)];
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

	public static Data randomize(final double[][] data, final double[][] label) {

		for (int i = 0; i < data.length; i++) {
			final int r = i + (int) (Math.random() * (data.length - i));
			double[] t = data[r];
			data[r] = data[i];
			data[i] = t;
			t = label[r];
			label[r] = label[i];
			label[i] = t;
		}
		final Data randomResult = new Data();
		randomResult.setSample(data);
		randomResult.setLabel(label);
		return randomResult;
	}

	public static void setRow(final double[][] matrix, final double[] row,
			final int index) {
		matrix[index] = row;
	}

	public static double standardDeviation(final double[] data,
			final double mean) {
		return new StandardDeviation().evaluate(data, mean);
	}

	public static double[] subtract(final double[] leftSide,
			final double[] rightSide) {
		final double[] result = new double[leftSide.length];

		for (int index = 0; index < leftSide.length; index++) {
			result[index] = leftSide[index] - rightSide[index];
		}
		return result;
	}

	public static double[] sum(final double[] leftSide, final double[] rightSide) {
		final double[] result = new double[leftSide.length];

		for (int index = 0; index < leftSide.length; index++) {
			result[index] = leftSide[index] + rightSide[index];
		}
		return result;
	}

	public static int sumArray(final int[] someArray) {
		int sum = 0;
		for (int i = 0; i < someArray.length; i++) {
			sum += someArray[i];
		}
		return sum;
	}

	public static String toStringArray(final int[] array) {
		final StringBuffer arrayString = new StringBuffer();
		for (int j = 0; j < array.length; j++) {
			arrayString.append(array[j] + " ");
		}
		return arrayString.toString();
	}
}
