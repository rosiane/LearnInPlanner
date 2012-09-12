package preprocessor.utils;

public enum TargetDatabaseEnum {
	INIT(1, "init"),
	ADD(2, "add"),
	DELETE(3, "delete"),
	GOAL(4, "goal"),
	CURRENT(5, "current");
	

	private int code;
	private String label;

	TargetDatabaseEnum(int code, String label) {
		this.code = code;
		this.label = label;
	}

	public int getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}
}
