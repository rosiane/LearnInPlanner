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

public class ReaderFeatureCancer implements ReaderFeature {

	private String pathTraining;
	private String pathTest;
	private Data dataTraining;
	private Data dataTest;

	public ReaderFeatureCancer(String pathTraining, String pathTest,
			Data dataTraining, Data dataTest) {
		this.pathTraining = pathTraining;
		this.pathTest = pathTest;
		this.dataTraining = dataTraining;
		this.dataTest = dataTest;
	}

	@Override
	public Data readTraining(int[] indexes) throws IOException {
		return read(pathTraining, dataTraining, indexes);
	}

	@Override
	public Data readTest(int[] indexes) throws IOException {
		return read(pathTest, dataTest, indexes);
	}

	public static Data read(String path, Data data, int[] indexes)
			throws IOException {
		Data dataReading = data;
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
