package preprocessor.file;

public class Sample {
	private String sample;
	private double label;

	public Sample(String sample, double label) {
		this.sample = sample;
		this.label = label;
	}

	public String getSample() {
		return sample;
	}

	public void setSample(String sample) {
		this.sample = sample;
	}

	public double getLabel() {
		return label;
	}

	public void setLabel(double label) {
		this.label = label;
	}

}
