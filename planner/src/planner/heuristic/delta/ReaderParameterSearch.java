package planner.heuristic.delta;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import planner.javaff.data.UngroundProblem;
import planner.javaff.data.strips.PredicateSymbol;
import planner.javaff.data.strips.UngroundInstantAction;
import planner.javaff.data.strips.Variable;
import planner.javaff.parser.PDDL21parser;

import neural.network.impl.MLP;
import neural.network.util.Weight;

import com.syvys.jaRBM.Layers.Layer;
import com.syvys.jaRBM.Layers.LogisticLayer;
import common.MatrixHandler;
import common.feature.ClassExpression;
import common.feature.GeneValueEnum;

public class ReaderParameterSearch {
	private static HashMap<String, String[]> typeParameterPredicate;

	public static ParameterSearch read(final ParameterReader parameterReader)
			throws IOException {
		initializeQuantityParameterPredicate(
				parameterReader.getDomainFilePath(),
				parameterReader.getProblemFilePath());
		final ParameterSearch parameterSearch = new ParameterSearch();
		// Setting features selected
		final LinkedList<ClassExpression> features = readFeatureSelected(
				parameterReader.getDirResult() + File.pathSeparator
						+ "featureSelected", parameterReader.getDirFeatures());
		parameterSearch.setFeatures(features);

		// Setting NeuralNetworkIF implementation
		parameterSearch.setNeuralNetwork(new MLP());

		// Initializing network
		int numberUnitHidden = parameterReader.getNumberUnitHidden();
		if (parameterReader.isUseHeuristicUnitHidden()) {
			final double[] array = { features.size(),
					parameterReader.getNumberOutput() };
			numberUnitHidden = (int) MatrixHandler.mean(array);
		}
		final Layer[] net = new LogisticLayer[parameterReader
				.getNumberHiddenLayers() + 1];
		for (int index = 0; index < net.length; index++) {
			if (index == (net.length - 1)) {
				net[index] = new LogisticLayer(
						parameterReader.getNumberOutput());
			} else {
				net[index] = new LogisticLayer(numberUnitHidden);
			}
		}
		parameterSearch.setNet(net);

		// Setting weights
		Weight[] weights = new Weight[parameterReader.getNumberHiddenLayers() + 1];
		for (int index = 0; index < weights.length; index++) {
			if (index == 0) {
				weights[index] = new Weight(features.size(), numberUnitHidden);
			} else if (index == (weights.length - 1)) {
				weights[index] = new Weight(numberUnitHidden,
						parameterReader.getNumberOutput());
			} else {
				weights[index] = new Weight(numberUnitHidden, numberUnitHidden);
			}
		}
		weights = readWeights(parameterReader.getDirResult() + "weight",
				weights);
		parameterSearch.setWeights(weights);
		return parameterSearch;
	}

	private static void initializeQuantityParameterPredicate(
			String domainFilePath, String problemFilePath) {
		typeParameterPredicate = new HashMap<>();
		final File domainFile = new File(domainFilePath);
		final File problemFile = new File(problemFilePath);
		final UngroundProblem unground = PDDL21parser.parseFiles(domainFile,
				problemFile);

		if (unground == null) {
			System.out.println("Parsing error - see console for details");
			return;
		}

		@SuppressWarnings("unchecked")
		final Iterator<UngroundInstantAction> iterator = unground.actions
				.iterator();
		UngroundInstantAction ungroundInstantAction;
		String[] parameterType;
		while (iterator.hasNext()) {
			ungroundInstantAction = iterator.next();

			parameterType = new String[ungroundInstantAction.params.size()];
			@SuppressWarnings("unchecked")
			Iterator<Variable> parameters = ungroundInstantAction.params
					.iterator();
			for (int indexParameter = 0; indexParameter < parameterType.length; indexParameter++) {
				parameterType[indexParameter] = parameters.next().getType()
						.toString();
			}
			typeParameterPredicate.put(ungroundInstantAction.name.toString(),
					parameterType);
		}
		@SuppressWarnings("unchecked")
		final Iterator<PredicateSymbol> iteratorFacts = unground.predSymbols
				.iterator();
		PredicateSymbol predicateSymbol;
		while (iteratorFacts.hasNext()) {
			predicateSymbol = iteratorFacts.next();
			parameterType = new String[predicateSymbol.getParams().size()];
			@SuppressWarnings("unchecked")
			final Iterator<Variable> parameters = predicateSymbol.getParams()
					.iterator();
			for (int indexParameter = 0; indexParameter < parameterType.length; indexParameter++) {
				parameterType[indexParameter] = parameters.next().getType()
						.toString();
			}
			typeParameterPredicate
					.put(predicateSymbol.getName(), parameterType);
		}

	}

	private static LinkedList<ClassExpression> readFeatureSelected(
			final String pathFeatureSelected, final String dirFeatures)
			throws IOException {
		DataInputStream dataInputStream = null;
		BufferedReader bufferedReader = null;
		LinkedList<ClassExpression> features = null;
		try {
			dataInputStream = new DataInputStream(new FileInputStream(
					pathFeatureSelected));
			bufferedReader = new BufferedReader(new InputStreamReader(
					dataInputStream));
			String strLine;
			String[] lineArray = null;
			int index = 0;
			String suffixFileFeatures = null;
			while ((strLine = bufferedReader.readLine()) != null) {
				if (index == 0) {
					suffixFileFeatures = strLine;
				} else {
					lineArray = strLine.split(" ");
				}
				index++;
			}
			features = readFeatureSelected(dirFeatures + File.pathSeparator
					+ "currentFeatures-" + suffixFileFeatures, lineArray);
		} catch (final FileNotFoundException e) {
			throw e;
		} catch (final IOException e) {
			throw e;
		} finally {
			if (dataInputStream != null) {
				dataInputStream.close();
			}
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
		return features;
	}

	private static LinkedList<ClassExpression> readFeatureSelected(
			final String pathFeatureSelected, final String[] indexFeatures)
			throws IOException {
		DataInputStream dataInputStream = null;
		BufferedReader bufferedReader = null;
		LinkedList<ClassExpression> features = new LinkedList<>();
		try {
			dataInputStream = new DataInputStream(new FileInputStream(
					pathFeatureSelected));
			bufferedReader = new BufferedReader(new InputStreamReader(
					dataInputStream));
			String strLine;
			for (int index = 0; index < indexFeatures.length; index++) {
				strLine = bufferedReader.readLine();
				if (indexFeatures[index].equalsIgnoreCase(GeneValueEnum.TRUE
						.toString())) {
					features.addLast(ClassExpression.getInstance(strLine,
							typeParameterPredicate));
				}
				index++;
			}
		} catch (final FileNotFoundException e) {
			throw e;
		} catch (final IOException e) {
			throw e;
		} finally {
			if (dataInputStream != null) {
				dataInputStream.close();
			}
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
		return features;
	}

	private static Weight[] readWeights(final String prefixPathFileWeights,
			final Weight[] weights) throws IOException {
		double[][] weightsMatrix = null;
		DataInputStream dataInputStream = null;
		BufferedReader bufferedReader = null;
		String[] strArray;
		try {
			for (int indexWeights = 0; indexWeights < weights.length; indexWeights++) {
				weightsMatrix = weights[indexWeights].getWeights();
				dataInputStream = new DataInputStream(new FileInputStream(
						prefixPathFileWeights + indexWeights));
				bufferedReader = new BufferedReader(new InputStreamReader(
						dataInputStream));
				for (int indexRow = 0; indexRow < MatrixHandler
						.rows(weightsMatrix); indexRow++) {
					strArray = bufferedReader.readLine().split(" ");
					for (int indexCol = 0; indexCol < MatrixHandler
							.cols(weightsMatrix); indexCol++) {
						weightsMatrix[indexRow][indexCol] = Double
								.parseDouble(strArray[indexCol]);
					}
				}
				if (dataInputStream != null) {
					dataInputStream.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			}
		} catch (final FileNotFoundException e) {
			throw e;
		} catch (final IOException e) {
			throw e;
		} finally {
			if (dataInputStream != null) {
				dataInputStream.close();
			}
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
		return weights;
	}

}
