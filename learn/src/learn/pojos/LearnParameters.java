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
	private String resultFile;

	public String getDatabaseDir() {
		return this.databaseDir;
	}

	public String getDirPlanningProblem() {
		return this.dirPlanningProblem;
	}

	public String getDirPlanningProblemRPL() {
		return this.dirPlanningProblemRPL;
	}

	public String getDomainPath() {
		return this.domainPath;
	}

	public String getExamplePathPrefix() {
		return this.examplePathPrefix;
	}

	public String getFeaturesFile() {
		return this.featuresFile;
	}

	public int getNumberExamples() {
		return this.numberExamples;
	}

	public int getNumberExpansion() {
		return this.numberExpansion;
	}

	public int getNumberIndividualInitialGA() {
		return this.numberIndividualInitialGA;
	}

	public ParameterGA getParameterGA() {
		return this.parameterGA;
	}

	public ParameterTraining getParameterTrainingMLP() {
		return this.parameterTrainingMLP;
	}

	public int[] getProblemTest() {
		return this.problemTest;
	}

	public int[] getProblemTraining() {
		return this.problemTraining;
	}

	public int[] getProblemValidation() {
		return this.problemValidation;
	}

	public String getResultFile() {
		return this.resultFile;
	}

	public String getSolutionFFPathPrefix() {
		return this.solutionFFPathPrefix;
	}

	public String getSolutionFFPathSufix() {
		return this.solutionFFPathSufix;
	}

	public void setDatabaseDir(final String databaseDir) {
		this.databaseDir = databaseDir;
	}

	public void setDirPlanningProblem(final String dirPlanningProblem) {
		this.dirPlanningProblem = dirPlanningProblem;
	}

	public void setDirPlanningProblemRPL(final String dirPlanningProblemRPL) {
		this.dirPlanningProblemRPL = dirPlanningProblemRPL;
	}

	public void setDomainPath(final String domainPath) {
		this.domainPath = domainPath;
	}

	public void setExamplePathPrefix(final String examplePathPrefix) {
		this.examplePathPrefix = examplePathPrefix;
	}

	public void setFeaturesFile(final String featuresFile) {
		this.featuresFile = featuresFile;
	}

	public void setNumberExamples(final int numberExamples) {
		this.numberExamples = numberExamples;
	}

	public void setNumberExpansion(final int numberExpansion) {
		this.numberExpansion = numberExpansion;
	}

	public void setNumberIndividualInitialGA(final int numberIndividualInitialGA) {
		this.numberIndividualInitialGA = numberIndividualInitialGA;
	}

	public void setParameterGA(final ParameterGA parameterGA) {
		this.parameterGA = parameterGA;
	}

	public void setParameterTrainingMLP(
			final ParameterTraining parameterTrainingMLP) {
		this.parameterTrainingMLP = parameterTrainingMLP;
	}

	public void setProblemTest(final int[] problemTest) {
		this.problemTest = problemTest;
	}

	public void setProblemTraining(final int[] problemTraining) {
		this.problemTraining = problemTraining;
	}

	public void setProblemValidation(final int[] problemValidation) {
		this.problemValidation = problemValidation;
	}

	public void setResultFile(final String resultFile) {
		this.resultFile = resultFile;
	}

	public void setSolutionFFPathPrefix(final String solutionFFPathPrefix) {
		this.solutionFFPathPrefix = solutionFFPathPrefix;
	}

	public void setSolutionFFPathSufix(final String solutionFFPathSufix) {
		this.solutionFFPathSufix = solutionFFPathSufix;
	}

}
