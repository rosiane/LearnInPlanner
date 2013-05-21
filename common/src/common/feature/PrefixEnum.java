package common.feature;

public enum PrefixEnum {
	ACTION("action_"), ADD("a_"), DEL("d_"), GOAL("g_"), CURRENT("c_"), FACT(
			"f_"), NOT("¬");

	private String prefix;

	PrefixEnum(final String prefix) {
		this.prefix = prefix;
	}

	public String prefix() {
		return this.prefix;
	}

}
