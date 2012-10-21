package neural.network.util;

import java.util.Random;

import common.MatrixHandler;

/**
 * Weights between two layers
 * 
 * @author Rosiane
 * 
 */
public class Weight {
	private double[][] weights;
	private double[][] lastUpdates;

	/**
	 * Avoiding create instance without initialize weights
	 */
	private Weight() {

	}

	/**
	 * 
	 * @param numRows
	 * @param numCols
	 */
	public Weight(int numRows, int numCols) {
		this.weights = new double[numRows][numCols];
		this.lastUpdates = new double[numRows][numCols];
	}

	/**
	 * 
	 * @param weights
	 * @author Rosiane
	 */
	public Weight(double[][] weights) {
		this.weights = weights;
	}

	/**
	 * 
	 * @return
	 */
	public double[][] getWeights() {
		return weights;
	}

	/**
	 * 
	 * @param weights
	 */
	public void setWeights(double[][] weights) {
		this.weights = weights;
	}

	/**
	 * 
	 * @return
	 */
	public double[][] getLastUpdates() {
		return lastUpdates;
	}

	/**
	 * 
	 * @param lastUpdates
	 */
	public void setLastUpdates(double[][] lastUpdates) {
		this.lastUpdates = lastUpdates;
	}

	public void normalizeCols() {
		this.weights = MatrixHandler.normalizeCols(weights);
	}

	public void initializeRandom() {
		Random random = new Random();
		for (int row = 0; row < MatrixHandler.rows(weights); row++) {
			for (int col = 0; col < MatrixHandler.cols(weights); col++) {
				weights[row][col] = (2 * random.nextDouble()) - 1;
			}
		}
	}

}
