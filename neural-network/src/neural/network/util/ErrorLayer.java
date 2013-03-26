package neural.network.util;

/**
 * Unit erros for each layer
 * 
 * @author rosy
 * 
 */
public class ErrorLayer {
	private double[] errors;

	public ErrorLayer(double[] errors) {
		this.errors = errors;
	}

	public double[] getErrors() {
		return errors;
	}

	public void setErrors(double[] errors) {
		this.errors = errors;
	}

}
