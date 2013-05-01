package common;

public enum PrefixEnum {
	ACTION("action_"), ADD("a_"), DEL("d_"), GOAL("g_"), CURRENT("c_"), FACT("f_");

	private String prefix;

	PrefixEnum(String prefix) {
		this.prefix = prefix;
	}

	public String prefix() {
		return prefix;
	}

}
