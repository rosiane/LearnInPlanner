package learn.pojos;

public class ClassExpression {
	private String predicate;
	private ClassExpression[] intersection;
	private ClassExpression[] parameter;
	private boolean not;

	public ClassExpression(String predicate, int arity) {
		this.predicate = predicate;
		this.parameter = new ClassExpression[arity];
	}

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

	public void setParameter(int index, ClassExpression parameter) {
		this.parameter[index] = parameter;
	}

	public boolean isNot() {
		return not;
	}

	public void setNot(boolean not) {
		this.not = not;
	}

	public ClassExpression[] getIntersection() {
		return intersection;
	}

	public void setIntersection(ClassExpression[] intersection) {
		this.intersection = intersection;
	}
}
