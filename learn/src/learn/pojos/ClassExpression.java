package learn.pojos;

public class ClassExpression {
	private String predicate;
	private ClassExpression[] parameter;
	private boolean not;

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public ClassExpression[] getParameter() {
		return parameter;
	}

	public void setParameter(ClassExpression[] parameter) {
		this.parameter = parameter;
	}

	public boolean isNot() {
		return not;
	}

	public void setNot(boolean not) {
		this.not = not;
	}
}
