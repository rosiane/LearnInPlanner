package neural.network.impl;

import java.io.IOException;

import neural.network.enums.Task;
import neural.network.interfaces.NeuralNetworkIF;
import neural.network.util.ErrorLayer;
import neural.network.util.ErrorRate;
import neural.network.util.NeuralNetworkUtils;
import neural.network.util.TrainingProperties;
import neural.network.util.Weight;

import com.syvys.jaRBM.Layers.Layer;
import common.Data;
import common.MatrixHandler;
import common.preprocessor.file.FileManager;

/**
 * 
 * @author rosiane
 * 
 */
public class MLP implements NeuralNetworkIF {

	private TrainingProperties[] trainingProperties;

	private Weight[] backpropagation(Layer[] net, Weight[] weights,
			double label[], double result[]) {
		ErrorLayer[] errorsLayer = new ErrorLayer[weights.length];

		double[] errors = null;
		for (int indexLayer = net.length - 1; indexLayer >= 0; indexLayer--) {
			if (indexLayer == (net.length - 1)) {
				 errors = MatrixHandler.multiply(
				 net[indexLayer].getLayerDerivative(result),
				 MatrixHandler.subtract(label, result));
			} else {
				errors = MatrixHandler.multiply(net[indexLayer]
						.getLayerDerivative(trainingProperties[indexLayer]
								.getOutputsLayer()), MatrixHandler.multiplySum(
						weights[indexLayer + 1].getWeights(),
						errorsLayer[indexLayer + 1].getErrors()));

			}
			errorsLayer[indexLayer] = new ErrorLayer(errors);
		}
		Weight[] weightsUpdated = weights;
		double[][] newWeight;
		double[][] update;
		for (int indexWeights = 0; indexWeights < weightsUpdated.length; indexWeights++) {
			newWeight = weightsUpdated[indexWeights].getWeights();
			update = new double[MatrixHandler.rows(newWeight)][MatrixHandler
					.cols(newWeight)];
			for (int indexRows = 0; indexRows < MatrixHandler.rows(newWeight); indexRows++) {
				for (int indexCols = 0; indexCols < MatrixHandler
						.cols(newWeight); indexCols++) {
					update[indexRows][indexCols] = net[indexWeights]
							.getLearningRate()
							* trainingProperties[indexWeights]
									.getInputsLayerLeft()[indexRows]
							* errorsLayer[indexWeights].getErrors()[indexCols];
					if (weightsUpdated[indexWeights].getLastUpdates() != null) {
						update[indexRows][indexCols] += net[indexWeights]
								.getMomentum()
								* weightsUpdated[indexWeights].getLastUpdates()[indexRows][indexCols];
					}
					newWeight[indexRows][indexCols] = newWeight[indexRows][indexCols]
							+ update[indexRows][indexCols];
				}
			}
			weightsUpdated[indexWeights].setLastUpdates(update);
		}
		return weightsUpdated;
	}

	private double[] getActivities(Layer layer, double[][] weights,
			double[] data, int indexLayer) {
		double[] upwardSum = MatrixHandler.getRow(
				getUpwardSWSum(new double[][] { data }, weights), 0);
		if (trainingProperties != null) {
			setUpwardSumLayer(upwardSum, indexLayer);
		}
		double[][] activationProbabilities = layer
				.getActivationProbabilities(new double[][] { upwardSum });
		return MatrixHandler.getRow(activationProbabilities, 0);
	}

	private double[][] getUpwardSWSum(double[][] batchData, double[][] weights) {
		return MatrixHandler.multiply(batchData, weights);
	}

	@Override
	public double[] run(Layer[] net, Weight[] weights, double[] data) {
		double[] activities = data;

		for (int index = 0; index < net.length; index++) {
			activities = getActivities(net[index], weights[index].getWeights(),
					activities, index);
			if (trainingProperties != null) {
				setOutputsLayer(activities, index);
				if (index == 0) {
					setInputsLayerLeft(data, index);
				}
				if (index < net.length - 1) {
					setInputsLayerLeft(activities, index + 1);
				}
			}
		}
		return activities;
	}

	private void setOutputsLayer(double[] outputs, int indexLayer) {
		trainingProperties[indexLayer].setOutputsLayer(outputs);
	}

	private void setInputsLayerLeft(double[] inputs, int indexLayer) {
		trainingProperties[indexLayer].setInputsLayerLeft(inputs);
	}

	private void setUpwardSumLayer(double[] upwardSum, int indexLayer) {
		trainingProperties[indexLayer].setUpwardSumLayer(upwardSum);
	}

	@Override
	public Weight[] train(Layer[] net, Weight[] weights, double[][] sample,
			double[][] sampleLabel, ParameterTraining parameterTraining,
			Data dataValidation, String fileResult) throws IOException {
		double result[];
		double errors[] = null;
		Weight[] weightsUpdated = weights;
		if (parameterTraining.isWeightsInitializationRandom()) {
			weightsUpdated = initializeRandom(weightsUpdated);
		}

		trainingProperties = new TrainingProperties[net.length];
		for (int indexProperties = 0; indexProperties < trainingProperties.length; indexProperties++) {
			if (indexProperties == 0) {
				trainingProperties[indexProperties] = new TrainingProperties(
						MatrixHandler.cols(sample),
						net[indexProperties].getNumUnits());
			} else {
				trainingProperties[indexProperties] = new TrainingProperties(
						net[indexProperties - 1].getNumUnits(),
						net[indexProperties].getNumUnits());
			}
		}
		double[] initialLearningRates = new double[net.length];
		for (int index = 0; index < initialLearningRates.length; index++) {
			initialLearningRates[index] = net[index].getLearningRate();
		}

		ErrorRate errorRate = new ErrorRate(0, 100);
		long epoch = 0;
		Data data = new Data();
		data.setSample(sample);
		data.setLabel(sampleLabel);
		if (parameterTraining.isNormalizeWeights()) {
			weightsUpdated = normalizeWeightsCols(weightsUpdated);
		}

		if (fileResult == null || fileResult.isEmpty()) {
			if (parameterTraining.isValidation()) {
				errorRate = NeuralNetworkUtils.calculateErrorRate(net, weightsUpdated,
						dataValidation, parameterTraining, this);
			} else {
				errorRate = NeuralNetworkUtils.calculateErrorRate(net, weightsUpdated, data,
						parameterTraining, this);
			}

			System.out.println("Before training");
			if (parameterTraining.getTask() == Task.CLASSIFICATION.getValue()) {
				System.out
						.println("\t Correct: " + errorRate.getCountCorrect());
			}
			System.out.println("\t Error Rate: " + errorRate.getErrorRate());
			System.out.println("\t Learning Rate: " + net[0].getLearningRate());
		}
		errorRate = new ErrorRate(0, 100);

		for (epoch = 0; epoch < parameterTraining.getNumberEpochs()
				&& errorRate.getErrorRate() > parameterTraining.getMaxError(); epoch++) {
			data = MatrixHandler.randomize(sample, sampleLabel);
			for (int indexSample = 0; indexSample < MatrixHandler.rows(data
					.getSample()); indexSample++) {
				result = run(net, weightsUpdated,
						MatrixHandler.getRow(data.getSample(), indexSample));
				if (!parameterTraining.isUpdateBatch()) {
					weightsUpdated = backpropagation(net, weightsUpdated,
							MatrixHandler.getRow(data.getLabel(), indexSample),
							result);
					if (parameterTraining.isNormalizeWeights()) {
						weightsUpdated = normalizeWeightsCols(weightsUpdated);
					}
				} else {
					if (indexSample == 0) {
						errors = MatrixHandler.subtract(MatrixHandler.getRow(
								data.getLabel(), indexSample), result);
					} else {
						errors = MatrixHandler.sum(errors, MatrixHandler
								.subtract(MatrixHandler.getRow(data.getLabel(),
										indexSample), result));
					}
				}
			}
			if (parameterTraining.isUpdateBatch()) {
				errors = MatrixHandler.division(errors,
						MatrixHandler.rows(data.getSample()));
				weightsUpdated = backpropagation(net, weightsUpdated, errors,
						null);
				if (parameterTraining.isNormalizeWeights()) {
					weightsUpdated = normalizeWeightsCols(weightsUpdated);
				}
			}

			if (parameterTraining.isValidation()) {
				errorRate = NeuralNetworkUtils.calculateErrorRate(net, weightsUpdated,
						dataValidation, parameterTraining, this);
			} else {
				errorRate = NeuralNetworkUtils.calculateErrorRate(net, weightsUpdated, data,
						parameterTraining, this);
			}

			if (epoch > 0
					&& epoch
							% (((double) (parameterTraining.getNumberEpochs() * parameterTraining
									.getIntervalEpochPercentage())) / 100) == 0) {
				if (net[0].getLearningRate() > parameterTraining
						.getMinLearningRate()) {
					net = decreaseLearningRate(net,
							parameterTraining.getLearningRateDecrease(),
							parameterTraining.getMinLearningRate());
				}
			}
			if (fileResult == null || fileResult.isEmpty()) {
				if (epoch % 100 == 0) {
					System.out.println("Running Epoch: " + epoch);
					if (parameterTraining.getTask() == Task.CLASSIFICATION
							.getValue()) {
						System.out.println("\t Correct: "
								+ errorRate.getCountCorrect());
					}
					System.out.println("\t Error Rate: "
							+ errorRate.getErrorRate());
					System.out.println("\t Learning Rate: "
							+ net[0].getLearningRate());
				}
			} else {
				if (epoch % 10 == 0) {
					FileManager.write(fileResult, "Running Epoch: " + epoch,
							true);
					if (parameterTraining.getTask() == Task.CLASSIFICATION
							.getValue()) {
						FileManager.write(fileResult, "\t Correct: "
								+ errorRate.getCountCorrect(), true);
					}
					FileManager.write(fileResult,
							"\t Error Rate: " + errorRate.getErrorRate(), true);
					FileManager.write(fileResult,
							"\t Learning Rate: " + net[0].getLearningRate(),
							true);
				}
			}
		}
		if (fileResult == null || fileResult.isEmpty()) {
			System.out.println("After training");
			if (parameterTraining.getTask() == Task.CLASSIFICATION.getValue()) {
				System.out
						.println("\t Correct: " + errorRate.getCountCorrect());
			}
			System.out.println("\t Error Rate: " + errorRate.getErrorRate());
			System.out.println("\t Learning Rate: " + net[0].getLearningRate());
			System.out.println("Number Epoches: " + epoch);
		} else {
			FileManager.write(fileResult, "Number Epoches Run: " + epoch, true);
		}
		return weightsUpdated;
	}

	private Weight[] normalizeWeightsCols(Weight[] weights) {
		Weight[] normalized = weights;
		for (Weight weight : normalized) {
			weight.normalizeCols();
		}
		return normalized;
	}

	private Weight[] initializeRandom(Weight[] weights) {
		Weight[] initialized = weights;
		for (Weight weight : initialized) {
			weight.initializeRandom();
		}
		return initialized;
	}

	private Layer[] decreaseLearningRate(Layer[] net, double decrease,
			double minimumRate) {
		Layer[] netUpdated = net;
		double newLearningRate = 0;
		for (Layer netTmp : netUpdated) {
			newLearningRate = netTmp.getLearningRate() * decrease;
			if (newLearningRate < minimumRate) {
				netTmp.setLearningRate(minimumRate);
			} else {
				netTmp.setLearningRate(newLearningRate);
			}
		}
		return netUpdated;
	}
}
