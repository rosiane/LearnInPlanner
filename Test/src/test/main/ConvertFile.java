package test.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import preprocessor.file.FileManager;

public class ConvertFile {
	public static void main(String[] args) {
		// patternize();
//		preparaCrossvalidation();
		 numberToBinary();

	}

	private static void numberToBinary() {
		Map<Integer, String> map = new HashMap<>();
		// map.put(1, "1 0 0");
		// map.put(2, "0 1 0");
		// map.put(3, "0 0 1");
		map.put(1, "1 0");
		map.put(2, "0 1");
		try {
			// for (int index = 1; index <= 10; index++) {
			for (int index = 1; index <= 1; index++) {
				// FileManager.convertLabelNumberToBinary("./data/iris_numberTest"
				// + index + ".csv", "./data/iris_binaryTest"
				// + index + ".csv", map);
//				FileManager.convertLabelNumberToBinary(
//						"./data/cancer/cancer_numberTest" + index
//								+ ".csv",
//						"./data/cancer/cancer_binaryTest" + index
//								+ ".csv", map);
				FileManager.convertLabelNumberToBinary(
						"./data/cancer/cancer_number_patternizedTest" + index
								+ ".csv",
						"./data/cancer/cancer_binary_patternizedTest" + index
								+ ".csv", map);
				// FileManager.convertLabelNumberToBinary(
				// "./data/iris_numberTraining" + index + ".csv",
				// "./data/iris_binaryTraining" + index + ".csv",
				// map);
				FileManager.convertLabelNumberToBinary(
						"./data/cancer/cancer_number_patternizedTraining"
								+ index + ".csv",
						"./data/cancer/cancer_binary_patternizedTraining"
								+ index + ".csv", map);
			}
			FileManager.convertLabelNumberToBinary(
					"./data/cancer/cancer_number_patternized_validation.csv",
					"./data/cancer/cancer_binary_patternized_validation.csv",
					map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void patternize() {
		try {
			int patterns = 569;
			int attributes = 30;
			FileManager.patternizeAttributes("./data/cancer/cancer_number.csv",
					"./data/cancer/cancer_number_patternized.csv", patterns,
					attributes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void preparaCrossvalidation() {
		// String pathInputFile = "./data/iris_number_patternized_1_2.csv";
		// String pathInputFile = "./data/iris_number.csv";
		// String prefixOutputFile = "./data/iris_number";
		// String pathInputFile = "./data/cancer/cancer_number.csv";
//		String prefixOutputFile = "./data/cancer/cancer_number";
//		String pathInputFile = "./data/cancer/cancer_numberTraining.csv";
//		 String pathInputFile = "./data/cancer/cancer_number_patternized.csv";
		 String pathInputFile =
		 "./data/cancer/cancer_number_patternizedTraining.csv";
		 String prefixOutputFile = "./data/cancer/cancer_number_patternized";
		List<Double> classes = new ArrayList<>();
		classes.add(1.0);
		classes.add(2.0);
		// classes.add(3.0);
		try {
			FileManager.prepareCrossvalidation(pathInputFile, prefixOutputFile,
					10, classes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
