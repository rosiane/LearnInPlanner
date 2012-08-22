package preprocessor.file;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Data;
import common.MatrixHandler;

public class FileManager {

	public static Data read(String path, Data data) throws IOException {
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
			while ((strLine = bufferedReader.readLine()) != null) {
				if (countLine % 2 != 0) {
					double[] row = MatrixHandler
							.parseDouble(strLine.split(" "));
					MatrixHandler.setRow(sample, row, countSample);
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

	public static void convertLabelNumberToBinary(String pathInputFile,
			String pathOutputFile, Map<Integer, String> map) throws IOException {
		PrintWriter printWriter = null;
		DataInputStream dataInputStream = null;
		BufferedReader bufferedReader = null;
		try {
			printWriter = new PrintWriter(new FileWriter(pathOutputFile, true));
			dataInputStream = new DataInputStream(new FileInputStream(
					pathInputFile));
			bufferedReader = new BufferedReader(new InputStreamReader(
					dataInputStream));
			String strLine;
			String[] lineArray;
			StringBuffer line = new StringBuffer();
			while ((strLine = bufferedReader.readLine()) != null) {
				lineArray = strLine.split(" ");
				line = new StringBuffer();
				for (int index = 0; index < lineArray.length - 1; index++) {
					line.append(lineArray[index] + " ");
				}
				printWriter.println(line.toString());
				printWriter.println(map.get(Integer
						.parseInt(lineArray[lineArray.length - 1])));
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (dataInputStream != null) {
				dataInputStream.close();
			}
			if (printWriter != null) {
				printWriter.close();
			}
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
	}

	public static void patternizeAttributes(String pathInputFile,
			String pathOutputFile, int numberPatterns, int numberAttributes)
			throws IOException {
		PrintWriter printWriter = null;
		DataInputStream dataInputStream = null;
		BufferedReader bufferedReader = null;
		try {
			printWriter = new PrintWriter(new FileWriter(pathOutputFile, true));
			dataInputStream = new DataInputStream(new FileInputStream(
					pathInputFile));
			bufferedReader = new BufferedReader(new InputStreamReader(
					dataInputStream));
			String strLine;
			String[] lineArray;
			double[][] data = new double[numberPatterns][numberAttributes];
			String[] label = new String[numberPatterns];
			int countPatterns = 0;
			while ((strLine = bufferedReader.readLine()) != null) {
				lineArray = strLine.split(" ");
				int index = 0;
				for (; index < lineArray.length - 1; index++) {
					data[countPatterns][index] = Double
							.parseDouble(lineArray[index]);
				}
				label[countPatterns] = lineArray[index];
				countPatterns++;
			}
			data = MatrixHandler.patternizeCols(data);

			StringBuffer line = null;
			for (int indexRows = 0; indexRows < MatrixHandler.rows(data); indexRows++) {
				line = new StringBuffer();
				for (int indexCols = 0; indexCols < MatrixHandler.cols(data); indexCols++) {
					line.append(data[indexRows][indexCols] + " ");
				}
				line.append(label[indexRows]);
				printWriter.println(line.toString());
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (dataInputStream != null) {
				dataInputStream.close();
			}
			if (printWriter != null) {
				printWriter.close();
			}
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
	}

	/**
	 * Create sets of training and test for crossvalidation with balanced
	 * classes.
	 * 
	 * @param pathInputFile
	 * @param prefixOutputFile
	 * @param k
	 * @param classes
	 * @throws IOException
	 */
	public static void prepareCrossvalidation(String pathInputFile,
			String prefixOutputFile, int k, List<Double> classes)
			throws IOException {
		PrintWriter printWriterTraining = null;
		PrintWriter printWriterTest = null;
		DataInputStream dataInputStream = null;
		BufferedReader bufferedReader = null;
		try {
			dataInputStream = new DataInputStream(new FileInputStream(
					pathInputFile));
			bufferedReader = new BufferedReader(new InputStreamReader(
					dataInputStream));
			String strLine;
			String[] lineArray;
			Map<Double, List<Sample>> samplesClasses = new HashMap<Double, List<Sample>>();
			List<Sample> data = new ArrayList<>();
			while ((strLine = bufferedReader.readLine()) != null) {
				lineArray = strLine.split(" ");
				data.add(new Sample(strLine, Double
						.parseDouble(lineArray[lineArray.length - 1])));
			}
			for (Double clazz : classes) {
				samplesClasses.put(clazz, new ArrayList<Sample>());
			}
			for (Sample sample : data) {
				samplesClasses.get(sample.getLabel()).add(sample);
			}
			List<Sample> temp = null;
			int numberSamples = 0;
			int beginTestSample = 0;
			int endTestSample = 0;
			for (Double clazz : classes) {
				temp = samplesClasses.get(clazz);
				numberSamples = temp.size() / k;
				for (int indexK = 0; indexK < k; indexK++) {
					beginTestSample = indexK * numberSamples;
					endTestSample = indexK * numberSamples + numberSamples;
					printWriterTraining = new PrintWriter(new FileWriter(
							((prefixOutputFile.concat("Training")).concat(""
									+ (indexK + 1) + "")).concat(".csv"), true));
					printWriterTest = new PrintWriter(new FileWriter(
							((prefixOutputFile.concat("Test")).concat(""
									+ (indexK + 1) + "")).concat(".csv"), true));
					for (int indexSample = 0; indexSample < temp.size(); indexSample++) {
						if (indexSample >= beginTestSample
								&& indexSample < endTestSample) {
							printWriterTest.println(temp.get(indexSample)
									.getSample());
						} else {
							printWriterTraining.println(temp.get(indexSample)
									.getSample());
						}
					}
					printWriterTraining.close();
					printWriterTest.close();
				}
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (NumberFormatException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (dataInputStream != null) {
				dataInputStream.close();
			}
			if (printWriterTraining != null) {
				printWriterTraining.close();
			}
			if (printWriterTest != null) {
				printWriterTest.close();
			}
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
	}

	public static void write(String path, String content, boolean append)
			throws IOException {
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new FileWriter(path, append));
			printWriter.println(content);
		} catch (FileNotFoundException e) {
			throw e;
		} catch (NumberFormatException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}

	}
}
