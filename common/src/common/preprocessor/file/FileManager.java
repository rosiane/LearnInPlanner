package common.preprocessor.file;

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
import common.feature.PrefixEnum;

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
			String pathOutputFile, Map<Integer, String> map, String separator)
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
			StringBuffer line = new StringBuffer();
			int label = 0;
			while ((strLine = bufferedReader.readLine()) != null) {
				lineArray = strLine.split(separator);
				line = new StringBuffer();
				for (int index = 0; index < lineArray.length - 1; index++) {
					line.append(lineArray[index] + separator);
				}
				printWriter.println(line.toString());
				label = (int) Double
						.parseDouble(lineArray[lineArray.length - 1]);
				printWriter.println(map.get(label));
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
			String pathOutputFile, int numberPatterns, int numberAttributes,
			String separator) throws IOException {
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
				lineArray = strLine.split(separator);
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
			String prefixOutputFile, int k, List<Double> classes,
			String separator) throws IOException {
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
				lineArray = strLine.split(separator);
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
	public static void prepareCrossvalidationBinary(String pathInputFile,
			String prefixOutputFile, int k, List<Double> classes,
			String separator) throws IOException {
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
			int count = 0;
			String sampleString = null;
			int labelNumber = 0;
			double[] label = null;
			while ((strLine = bufferedReader.readLine()) != null) {
				if (count % 2 == 0) {
					sampleString = strLine;
				} else {
					lineArray = strLine.split(separator);
					label = new double[lineArray.length];
					for (int index = 0; index < label.length; index++) {
						label[index] = Double.parseDouble(lineArray[index]);
					}
					labelNumber = MatrixHandler.maxNumber(label) + 1;
					data.add(new Sample(sampleString + " " + labelNumber,
							labelNumber));
				}
				count++;
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
	public static void prepareLeaveOneOut(String pathInputFile,
			String prefixOutputFile, int k, List<Double> classes,
			String separator) throws IOException {
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
			List<Sample> data = new ArrayList<>();
			while ((strLine = bufferedReader.readLine()) != null) {
				lineArray = strLine.split(separator);
				data.add(new Sample(strLine, Double
						.parseDouble(lineArray[lineArray.length - 1])));
			}
			for (int indexK = 0; indexK < k; indexK++) {
				printWriterTraining = new PrintWriter(new FileWriter(
						((prefixOutputFile.concat("Training")).concat(""
								+ (indexK + 1) + "")).concat(".csv"), true));
				printWriterTest = new PrintWriter(new FileWriter(
						((prefixOutputFile.concat("Test")).concat(""
								+ (indexK + 1) + "")).concat(".csv"), true));
				for (int indexSample = 0; indexSample < data.size(); indexSample++) {
					if (indexSample == indexK) {
						printWriterTest.println(data.get(indexSample)
								.getSample());
					} else {
						printWriterTraining.println(data.get(indexSample)
								.getSample());
					}
				}
				printWriterTraining.close();
				printWriterTest.close();
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

	public static void convertMnist(String pathInputFile, String pathOutputFile)
			throws IOException {
		PrintWriter printWriter = null;
		DataInputStream dataInputStream = null;
		BufferedReader bufferedReader = null;
		try {
			dataInputStream = new DataInputStream(new FileInputStream(
					pathInputFile));
			bufferedReader = new BufferedReader(new InputStreamReader(
					dataInputStream));
			String strLine;
			String newLine;
			printWriter = new PrintWriter(new FileWriter(pathOutputFile, true));
			while ((strLine = bufferedReader.readLine()) != null) {
				newLine = strLine.replace("   ", " ").substring(1);
				printWriter.println(newLine);
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
			if (printWriter != null) {
				printWriter.close();
			}
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
	}

	public static void separeValidationMnist(String pathInputFile,
			String pathTrain, String pathValidation, long quantityTrain)
			throws IOException {
		PrintWriter printWriterTrain = null;
		PrintWriter printWriterValidation = null;
		DataInputStream dataInputStream = null;
		BufferedReader bufferedReader = null;
		try {
			dataInputStream = new DataInputStream(new FileInputStream(
					pathInputFile));
			bufferedReader = new BufferedReader(new InputStreamReader(
					dataInputStream));
			String strLine;
			printWriterTrain = new PrintWriter(new FileWriter(pathTrain, true));
			printWriterValidation = new PrintWriter(new FileWriter(
					pathValidation, true));
			long countTrain = 1;
			while ((strLine = bufferedReader.readLine()) != null) {
				if (countTrain <= quantityTrain) {
					printWriterTrain.println(strLine);
					countTrain++;
				} else {
					printWriterValidation.println(strLine);
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
			if (printWriterTrain != null) {
				printWriterTrain.close();
			}
			if (printWriterValidation != null) {
				printWriterValidation.close();
			}
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		}
	}

	public static void separeClasses(String pathInputFile,
			String pathOutputFile, List<Double> classes, String separator)
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
			while ((strLine = bufferedReader.readLine()) != null) {
				lineArray = strLine.split(separator);
				if (classes.contains(Double
						.parseDouble(lineArray[lineArray.length - 1]))) {
					printWriter.println(strLine);
				}

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

	public static int readLength(String pathProblemSolution) throws IOException {
		DataInputStream dataInputStream = null;
		BufferedReader bufferedReader = null;
		int length = 0;
		try {
			dataInputStream = new DataInputStream(new FileInputStream(
					pathProblemSolution));
			bufferedReader = new BufferedReader(new InputStreamReader(
					dataInputStream));
			length = Integer.parseInt(bufferedReader.readLine());
		} catch (FileNotFoundException e) {
			throw e;
		} finally {
			if (dataInputStream != null) {
				dataInputStream.close();
			}
		}
		return length;
	}

	public static double readDelta(String problemFile) throws IOException {
		DataInputStream dataInputStream = null;
		BufferedReader bufferedReader = null;
		double delta = 0;
		try {
			dataInputStream = new DataInputStream(new FileInputStream(
					problemFile));
			bufferedReader = new BufferedReader(new InputStreamReader(
					dataInputStream));
			String strLine;
			bufferedReader.readLine();
			while ((strLine = bufferedReader.readLine()) != null && strLine.startsWith(PrefixEnum.ACTION.prefix()));
			delta = Double.parseDouble(strLine);
		} catch (FileNotFoundException e) {
			throw e;
		} finally {
			if (dataInputStream != null) {
				dataInputStream.close();
			}
		}
		return delta;
	}
}
