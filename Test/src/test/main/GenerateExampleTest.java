package test.main;

import java.io.IOException;

import preprocessor.file.GenerateExample;

public class GenerateExampleTest {
	public static void main(String[] args) {
		try {
			generateRPL();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void generateRPL() throws IOException {
		String domainPath = "../Examples/IPC3/Tests1/Depots/Strips/Depots.pddl";
		String examplePathPrefix = "../Examples/IPC3/Tests1/Depots/Strips/pfile";
		int numberExamples = 22;
		String solutionFFPathPrefix = "../Examples/IPC3/Tests1/Depots/Strips/training/pfile";
		String solutionFFPathSufix = "Solution.pddl";
		String resultDir = "../Examples/IPC3/Tests1/Depots/Strips/training/rpl";
		GenerateExample.generate(domainPath, examplePathPrefix, numberExamples,
				solutionFFPathPrefix, solutionFFPathSufix, resultDir);
	}

}
