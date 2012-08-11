package neural.network.enums;

public enum Task {

	CLASSIFICATION(1), REGRESSION(2);

	private int value;

	Task(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
