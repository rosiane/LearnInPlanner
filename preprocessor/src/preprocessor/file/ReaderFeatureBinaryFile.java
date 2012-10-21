package preprocessor.file;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import common.Data;
import common.MatrixHandler;

public class ReaderFeatureBinaryFile implements ReaderFeature {

	private String pathTraining;
	private String pathTest;
	private String pathValidation;
	private int numberInputTraining;
	private int numberInputTest;
	private int numberInputValidation;
	private int numberOutput;

	public ReaderFeatureBinaryFile(String pathTraining, String pathTest,
			String pathValidation, int numberInputTraining,
			int numberInputTest, int numberInputValidation, int numberOutput) {
		this.pathTraining = pathTraining;
		this.pathTest = pathTest;
		this.pathValidation = pathValidation;
		this.numberInputTraining = numberInputTraining;
		this.numberInputTest = numberInputTest;
		this.numberInputValidation = numberInputValidation;
		this.numberOutput = numberOutput;

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

	@Override
	public Data readTraining(int[] indexes) throws IOException {
		return read(pathTraining, numberInputTraining, indexes);
	}

	@Override
	public Data readTest(int[] indexes) throws IOException {
		return read(pathTest, numberInputTest, indexes);
	}

	@Override
	public Data readValidation(int[] indexes) throws IOException {
		return read(pathValidation, numberInputValidation, indexes);
	}

	public Data read(String path, int numberInput, int[] indexes)
			throws IOException {
		Data dataReading = initialize(numberInput,
				MatrixHandler.countValue(indexes, 1), numberOutput);
		double[][] sample = dataReading.getSample();
		double[][] sampleLabel = dataReading.getLabel();
		FileInputStream fileInputStream;
		DataInputStream dataInputStream = null;
		try {
			fileInputStream = new FileInputStream(path);
			dataInputStream = new DataInputStream(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(dataInputStream));
			String strLine;
			int countLine = 1;
			int countSample = 0;
			List<Double> colSelect = new ArrayList<>();
			double[] result;
			while ((strLine = bufferedReader.readLine()) != null) {
				if (countLine % 2 != 0) {
					double[] row = MatrixHandler
							.parseDouble(strLine.split(" "));
					colSelect = new ArrayList<>();
					for (int index = 0; index < row.length; index++) {
						if (indexes[index] == 1) {
							colSelect.add(row[index]);
						}
					}
					result = new double[colSelect.size()];
					for (int index = 0; index < colSelect.size(); index++) {
						result[index] = colSelect.get(index);
					}
					MatrixHandler.setRow(sample, result, countSample);
				} else {
					double[] row = MatrixHandler
							.parseDouble(strLine.split(" "));
					MatrixHandler.setRow(sampleLabel, row, countSample);
					countSample++;
				}
				countLine++;
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (dataInputStream != null) {
				dataInputStream.close();
			}
		}
		return dataReading;
	}

}
