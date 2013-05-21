package planner.heuristic.delta;

public class ParameterReader {
	private String dirResult;
	private String dirFeatures;
	private int numberHiddenLayers;
	private int numberUnitHidden;
	private boolean useHeuristicUnitHidden;
	private int numberOutput;
	private String domainFilePath;
	private String problemFilePath;

	public String getDirFeatures() {
		return this.dirFeatures;
	}

	public String getDirResult() {
		return this.dirResult;
	}

	public int getNumberHiddenLayers() {
		return this.numberHiddenLayers;
	}

	public int getNumberOutput() {
		return this.numberOutput;
	}

	public int getNumberUnitHidden() {
		return this.numberUnitHidden;
	}

	public boolean isUseHeuristicUnitHidden() {
		return this.useHeuristicUnitHidden;
	}

	public void setDirFeatures(final String dirFeatures) {
		this.dirFeatures = dirFeatures;
	}

	public void setDirResult(final String dirResult) {
		this.dirResult = dirResult;
	}

	public void setNumberHiddenLayers(final int numberHiddenLayers) {
		this.numberHiddenLayers = numberHiddenLayers;
	}

	public void setNumberOutput(final int numberOutput) {
		this.numberOutput = numberOutput;
	}

	public void setNumberUnitHidden(final int numberUnitHidden) {
		this.numberUnitHidden = numberUnitHidden;
	}

	public void setUseHeuristicUnitHidden(final boolean useHeuristicUnitHidden) {
		this.useHeuristicUnitHidden = useHeuristicUnitHidden;
	}

	public String getDomainFilePath() {
		return domainFilePath;
	}

	public void setDomainFilePath(String domainFilePath) {
		this.domainFilePath = domainFilePath;
	}

	public String getProblemFilePath() {
		return problemFilePath;
	}

	public void setProblemFilePath(String problemFilePath) {
		this.problemFilePath = problemFilePath;
	}
}
