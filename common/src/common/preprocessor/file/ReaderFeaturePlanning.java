package common.preprocessor.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import common.Data;
import common.MatrixHandler;
import common.feature.ClassExpression;
import common.feature.GeneValueEnum;

public class ReaderFeaturePlanning implements ReaderFeature {
	private static Data initialize(final int numberInput,
			final int numberAttribute, final int numberOutput) {
		final Data data = new Data();
		final double[][] sample = new double[numberInput][numberAttribute];
		data.setSample(sample);
		final double[][] label = new double[numberInput][numberOutput];
		data.setLabel(label);
		return data;
	}

	private final LinkedList<ClassExpression> features;
	private final String dirPlanningProblem;
	private final String dirPlanningProblemRPL;
	private final int[] problemTraining;
	private final int[] problemValidation;

	private final int[] problemTest;

	public ReaderFeaturePlanning(final LinkedList<ClassExpression> features,
			final String dirPlanningProblem,
			final String dirPlanningProblemRPL, final int[] problemTraining,
			final int[] problemValidation, final int[] problemTest) {
		this.features = features;
		this.dirPlanningProblem = dirPlanningProblem;
		this.dirPlanningProblemRPL = dirPlanningProblemRPL;
		this.problemTraining = problemTraining;
		this.problemValidation = problemValidation;
		this.problemTest = problemTest;
	}

	private Data read(final int[] indexes, final int[] problems)
			throws IOException {
		final int[] quantityPerProblem = new int[problems.length];
		for (int indexProblem = 0; indexProblem < problems.length; indexProblem++) {
			quantityPerProblem[indexProblem] = FileManager
					.readLength(this.dirPlanningProblem + File.separator
							+ "pfile" + problems[indexProblem]
							+ "Solution.pddl");
		}
		final int numberInput = MatrixHandler.sumArray(quantityPerProblem);
		final Data dataReading = initialize(numberInput,
				MatrixHandler.countValue(indexes, GeneValueEnum.TRUE.value()),
				1);
		final double[][] sample = dataReading.getSample();
		final double[][] sampleLabel = dataReading.getLabel();
		List<Double> colSelect = new ArrayList<>();
		double[] result = null;
		int countSample = 0;
		String problemFile;
		for (int indexProblem = 0; indexProblem < problems.length; indexProblem++) {
			for (int indexRPL = 1; indexRPL <= quantityPerProblem[indexProblem]; indexRPL++) {
				colSelect = new ArrayList<>();
				problemFile = this.dirPlanningProblemRPL + File.separator
						+ "pfile" + problems[indexProblem] + "SolutionRPL"
						+ indexRPL + ".pddl";
				for (int indexFeatures = 0; indexFeatures < this.features
						.size(); indexFeatures++) {
					if (indexes[indexFeatures] == 1) {
						colSelect.add(this.features.get(indexFeatures)
								.cardinality(problemFile));
					}
				}
				result = new double[colSelect.size()];
				for (int index = 0; index < colSelect.size(); index++) {
					result[index] = colSelect.get(index);
				}
				MatrixHandler.setRow(sample, result, countSample);
				final double[] label = { FileManager.readDelta(problemFile) };
				MatrixHandler.setRow(sampleLabel, label, countSample);
				countSample++;
			}
		}
		MatrixHandler.normalizeRows(sample);
		return dataReading;
	}

	@Override
	public Data readTest(final int[] indexes) throws IOException {
		return this.read(indexes, this.problemTest);
	}

	@Override
	public Data readTraining(final int[] indexes) throws IOException {
		return this.read(indexes, this.problemTraining);
	}

	@Override
	public Data readValidation(final int[] indexes) throws IOException {
		return this.read(indexes, this.problemValidation);
	}

}
