package neural.network.util;

public class TrainingProperties {

	private double[] gradientRightLayer;
	private double[] inputsLayer;
	private double[] inputsLayerLeft;
	private double[] upwardSumLayer;

	private double[] outputsLayer;
	
	/**
	 * Avoiding create instance without initialize properties
	 */
	private TrainingProperties() {

	}

	public TrainingProperties(int lenghtLayer, int lenghtRighLayer) {
		this.inputsLayer = new double[lenghtLayer];
		this.gradientRightLayer = new double[lenghtRighLayer];
		this.upwardSumLayer = new double[lenghtRighLayer];
	}

	public double[] getGradientRightLayer() {
		return gradientRightLayer;
	}

	public void setGradientRightLayer(double[] gradientRightLayer) {
		this.gradientRightLayer = gradientRightLayer;
	}

	public double[] getInputsLayer() {
		return inputsLayer;
	}

	public void setInputsLayer(double[] inputsLayer) {
		this.inputsLayer = inputsLayer;
	}

	public double[] getUpwardSumLayer() {
		return upwardSumLayer;
	}

	public void setUpwardSumLayer(double[] upwardSumLayer) {
		this.upwardSumLayer = upwardSumLayer;
	}

	public double [] getInputsLayerLeft(){
		return this.inputsLayerLeft;
	}
	
	public void setInputsLayerLeft(double[] inputs) {
		this.inputsLayerLeft = inputs;
		
	}

	public double[] getOutputsLayer() {
		return outputsLayer;
	}

	public void setOutputsLayer(double[] outputsLayer) {
		this.outputsLayer = outputsLayer;
	}

}
