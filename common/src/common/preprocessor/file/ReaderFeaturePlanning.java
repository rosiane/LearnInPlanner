package common.preprocessor.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import common.ClassExpression;
import common.Data;
import common.MatrixHandler;

public class ReaderFeaturePlanning implements ReaderFeature {
	private LinkedList<ClassExpression> features;
	private String dirPlanningProblem;
	private String dirPlanningProblemRPL;
	private int[] problemTraining;
	private int[] problemValidation;
	private int[] problemTest;

	public ReaderFeaturePlanning(LinkedList<ClassExpression> features,
			String dirPlanningProblem, String dirPlanningProblemRPL,
			int[] problemTraining, int[] problemValidation, int[] problemTest) {
		this.features = features;
		this.dirPlanningProblem = dirPlanningProblem;
		this.dirPlanningProblemRPL = dirPlanningProblemRPL;
		this.problemTraining = problemTraining;
		this.problemValidation = problemValidation;
		this.problemTest = problemTest;
	}

	@Override
	public Data readTraining(int[] indexes) throws IOException {
		return read(indexes, problemTraining);
	}

	@Override
	public Data readTest(int[] indexes) throws IOException {
		return read(indexes, problemTest);
	}

	@Override
	public Data readValidation(int[] indexes) throws IOException {
		return read(indexes, problemValidation);
	}
	
	private static Data initialize(int numberInput, int numberAttribute,
			int numberOutput) {
		Data data = new Data();
		double[][] sample = new double[numberInput][numberAttribute];
		data.setSample(sample);
		double[][] label = new double[numberInput][numberOutput];
		data.setLabel(label);
		return data;
	}
	
	private Data read(int[] indexes, int[] problems) throws IOException {
		int[] quantityPerProblem = new int[problems.length];
		for (int indexProblem = 0; indexProblem < problems.length; indexProblem++) {
			quantityPerProblem[indexProblem] = FileManager
					.readLength(dirPlanningProblem + File.separator + "pfile"
							+ problems[indexProblem] + "Solution.pddl");
		}
		int numberInput = MatrixHandler.sumArray(quantityPerProblem);
		Data dataReading = initialize(numberInput,
				MatrixHandler.countValue(indexes, 1), 1);
		double[][] sample = dataReading.getSample();
		double[][] sampleLabel = dataReading.getLabel();
		List<Double> colSelect = new ArrayList<>();
		double[] result = null;
		int countSample = 0;
		String problemFile;
		for (int indexProblem = 0; indexProblem < problems.length; indexProblem++) {
			for (int indexRPL = 1; indexRPL <= quantityPerProblem[indexProblem]; indexRPL++) {
				colSelect = new ArrayList<>();
				problemFile = dirPlanningProblemRPL + File.separator + "pfile"
						+ problems[indexProblem] + "SolutionRPL" + indexRPL
						+ ".pddl";
				for (int indexFeatures = 0; indexFeatures < features.size(); indexFeatures++) {
					if (indexes[indexFeatures] == 1) {
						colSelect.add(features.get(indexFeatures).cardinality(
								problemFile));
					}
				}
				result = new double[colSelect.size()];
				for (int index = 0; index < colSelect.size(); index++) {
					result[index] = colSelect.get(index);
				}
				MatrixHandler.setRow(sample, result, countSample);
				double[] label = { FileManager.readDelta(problemFile) };
				MatrixHandler.setRow(sampleLabel, label, countSample);
				countSample++;
			}
		}
		return dataReading;
	}

}
