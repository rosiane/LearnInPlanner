package neural.network.util;

public class ErrorRate {
	private int countCorrect;
	private double errorRate;

	public ErrorRate(int countCorrect, double errorRate) {
		this.countCorrect = countCorrect;
		this.errorRate = errorRate;
	}

	public int getCountCorrect() {
		return countCorrect;
	}

	public double getErrorRate() {
		return errorRate;
	}

}
