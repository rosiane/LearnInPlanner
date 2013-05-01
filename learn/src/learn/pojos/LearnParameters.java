package learn.pojos;

import neural.network.impl.ParameterTraining;
import feature.selector.ga.ParameterGA;

public class LearnParameters {
	private String domainPath;
	private String examplePathPrefix;
	private int numberExamples;
	private String solutionFFPathPrefix;
	private String solutionFFPathSufix;
	private String databaseDir;
	private String featuresFile;
	private int numberIndividualInitialGA;
	private ParameterGA parameterGA;
	private String dirPlanningProblem;
	private String dirPlanningProblemRPL;
	private int[] problemTraining;
	private int[] problemValidation;
	private int[] problemTest;
	private ParameterTraining parameterTrainingMLP;
	private int numberExpansion;

	public String getDomainPath() {
		return domainPath;
	}

	public void setDomainPath(String domainPath) {
		this.domainPath = domainPath;
	}

	public String getExamplePathPrefix() {
		return examplePathPrefix;
	}

	public void setExamplePathPrefix(String examplePathPrefix) {
		this.examplePathPrefix = examplePathPrefix;
	}

	public int getNumberExamples() {
		return numberExamples;
	}

	public void setNumberExamples(int numberExamples) {
		this.numberExamples = numberExamples;
	}

	public String getSolutionFFPathPrefix() {
		return solutionFFPathPrefix;
	}

	public void setSolutionFFPathPrefix(String solutionFFPathPrefix) {
		this.solutionFFPathPrefix = solutionFFPathPrefix;
	}

	public String getSolutionFFPathSufix() {
		return solutionFFPathSufix;
	}

	public void setSolutionFFPathSufix(String solutionFFPathSufix) {
		this.solutionFFPathSufix = solutionFFPathSufix;
	}

	public String getDatabaseDir() {
		return databaseDir;
	}

	public void setDatabaseDir(String databaseDir) {
		this.databaseDir = databaseDir;
	}

	public String getFeaturesFile() {
		return featuresFile;
	}

	public void setFeaturesFile(String featuresFile) {
		this.featuresFile = featuresFile;
	}

	public int getNumberIndividualInitialGA() {
		return numberIndividualInitialGA;
	}

	public void setNumberIndividualInitialGA(int numberIndividualInitialGA) {
		this.numberIndividualInitialGA = numberIndividualInitialGA;
	}

	public ParameterGA getParameterGA() {
		return parameterGA;
	}

	public void setParameterGA(ParameterGA parameterGA) {
		this.parameterGA = parameterGA;
	}

	public String getDirPlanningProblem() {
		return dirPlanningProblem;
	}

	public void setDirPlanningProblem(String dirPlanningProblem) {
		this.dirPlanningProblem = dirPlanningProblem;
	}

	public String getDirPlanningProblemRPL() {
		return dirPlanningProblemRPL;
	}

	public void setDirPlanningProblemRPL(String dirPlanningProblemRPL) {
		this.dirPlanningProblemRPL = dirPlanningProblemRPL;
	}

	public int[] getProblemTraining() {
		return problemTraining;
	}

	public void setProblemTraining(int[] problemTraining) {
		this.problemTraining = problemTraining;
	}

	public int[] getProblemValidation() {
		return problemValidation;
	}

	public void setProblemValidation(int[] problemValidation) {
		this.problemValidation = problemValidation;
	}

	public int[] getProblemTest() {
		return problemTest;
	}

	public void setProblemTest(int[] problemTest) {
		this.problemTest = problemTest;
	}

	public ParameterTraining getParameterTrainingMLP() {
		return parameterTrainingMLP;
	}

	public void setParameterTrainingMLP(ParameterTraining parameterTrainingMLP) {
		this.parameterTrainingMLP = parameterTrainingMLP;
	}

	public int getNumberExpansion() {
		return numberExpansion;
	}

	public void setNumberExpansion(int numberExpansion) {
		this.numberExpansion = numberExpansion;
	}
}
