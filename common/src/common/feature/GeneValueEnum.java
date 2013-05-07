package common.feature;

public enum GeneValueEnum {
	TRUE(1), FALSE(0);

	private int value;

	private GeneValueEnum(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}
}
