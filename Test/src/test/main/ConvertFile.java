package test.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import preprocessor.file.FileManager;

public class ConvertFile {
	public static void main(String[] args) {
		 separeClasses();
		// patternize();
		// preparaCrossvalidation();
		// preparaCrossvalidationBinary();
		numberToBinary();

	}

	private static void numberToBinary() {
		Map<Integer, String> map = new HashMap<>();
		map.put(0, "1 0 0 0 0 0 0 0 0");
		map.put(1, "0 1 0 0 0 0 0 0 0");
		map.put(2, "0 0 1 0 0 0 0 0 0");
		map.put(3, "0 0 0 1 0 0 0 0 0");
		map.put(4, "0 0 0 0 1 0 0 0 0");
		map.put(5, "0 0 0 0 0 1 0 0 0");
		map.put(6, "0 0 0 0 0 0 1 0 0");
		map.put(7, "0 0 0 0 0 0 0 1 0");
		map.put(8, "0 0 0 0 0 0 0 0 1");
		try {
			
			String pathTraining = "./data/MNIST/normal/mnist_train_0_1_2_3_4_5_6_7_8.csv";
			String pathValidation = "./data/MNIST/normal/mnist_validation_0_1_2_3_4_5_6_7_8.csv";
			String pathTest = "./data/MNIST/normal/mnist_test_0_1_2_3_4_5_6_7_8.csv";
			String pathTrainingBinary = "./data/MNIST/normal/mnist_train_0_1_2_3_4_5_6_7_8_binary.csv";
			String pathValidationBinary = "./data/MNIST/normal/mnist_validation_0_1_2_3_4_5_6_7_8_binary.csv";
			String pathTestBinary = "./data/MNIST/normal/mnist_test_0_1_2_3_4_5_6_7_8_binary.csv";
//			String pathTraining = "./data/MNIST/normal/mnist_train_6_8.csv";
//			String pathValidation = "./data/MNIST/normal/mnist_validation_6_8.csv";
//			String pathTest = "./data/MNIST/normal/mnist_test_6_8.csv";
//			String pathTrainingBinary = "./data/MNIST/normal/mnist_train_6_8_binary.csv";
//			String pathValidationBinary = "./data/MNIST/normal/mnist_validation_6_8_binary.csv";
//			String pathTestBinary = "./data/MNIST/normal/mnist_test_6_8_binary.csv";
			FileManager.convertLabelNumberToBinary(pathTraining,
					pathTrainingBinary, map, " ");
			FileManager.convertLabelNumberToBinary(pathValidation,
					pathValidationBinary, map, " ");
			FileManager.convertLabelNumberToBinary(pathTest,
					pathTestBinary, map, " ");
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
					attributes, " ");
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
		// String prefixOutputFile = "./data/cancer/cancer_number";
		// String pathInputFile = "./data/cancer/cancer_numberTraining.csv";
		String pathInputFile = "./data/cancer/cancer_number_patternized.csv";
		// String pathInputFile =
		// "./data/cancer/cancer_number_patternizedTraining.csv";
		String prefixOutputFile = "./data/cancer/cancer_number_patternized";
		List<Double> classes = new ArrayList<>();
		classes.add(1.0);
		classes.add(2.0);
		// classes.add(3.0);
		try {
			FileManager.prepareCrossvalidation(pathInputFile, prefixOutputFile,
					10, classes, " ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void preparaCrossvalidationBinary() {
		// String pathInputFile = "./data/iris_number_patternized_1_2.csv";
		// String pathInputFile = "./data/iris_number.csv";
		// String prefixOutputFile = "./data/iris_number";
		// String pathInputFile = "./data/cancer/cancer_number.csv";
		// String prefixOutputFile = "./data/cancer/cancer_number";
		// String pathInputFile = "./data/cancer/cancer_numberTraining.csv";
		String pathInputFile = "./data/MNIST/MNIST_MLP_2_exemplos_1_e_0_6500_exemplos.csv";
		// String pathInputFile =
		// "./data/cancer/cancer_number_patternizedTraining.csv";
		String prefixOutputFile = "./data/MNIST/MNIST_MLP_2_exemplos_1_e_0_6500_exemplos";
		List<Double> classes = new ArrayList<>();
		classes.add(1.0);
		classes.add(2.0);
		// classes.add(3.0);
		try {
			FileManager.prepareCrossvalidationBinary(pathInputFile,
					prefixOutputFile, 10, classes, " ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void separeClasses() {
		String pathTrainingInput = "./data/MNIST/normal/mnist_test.csv";
		String pathValidationInput = "./data/MNIST/normal/mnist_validation.csv";
		String pathTestInput = "./data/MNIST/normal/mnist_train_paper.csv";
		String pathTrainingOutput = "./data/MNIST/normal/mnist_train_0_1_2_3_4_5_6_7_8.csv";
		String pathValidationOutput = "./data/MNIST/normal/mnist_validation_0_1_2_3_4_5_6_7_8.csv";
		String pathTestOutput = "./data/MNIST/normal/mnist_test_0_1_2_3_4_5_6_7_8.csv";
//		String pathTrainingOutput = "./data/MNIST/normal/mnist_train_6_8.csv";
//		String pathValidationOutput = "./data/MNIST/normal/mnist_validation_6_8.csv";
//		String pathTestOutput = "./data/MNIST/normal/mnist_test_6_8.csv";
		List<Double> classes = new ArrayList<>();
		classes.add(0.0);
		classes.add(1.0);
		classes.add(2.0);
		classes.add(3.0);
		classes.add(4.0);
		classes.add(5.0);
		classes.add(6.0);
		classes.add(7.0);
		classes.add(8.0);
		
		try {
			FileManager.separeClasses(pathTrainingInput, pathTrainingOutput,
					classes, " ");
			FileManager.separeClasses(pathValidationInput,
					pathValidationOutput, classes, " ");
			FileManager.separeClasses(pathTestInput, pathTestOutput, classes,
					" ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
