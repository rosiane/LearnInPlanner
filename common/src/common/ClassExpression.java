package common;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ClassExpression {
	private String predicate;
	private LinkedList<ClassExpression[]> intersection;
	private ClassExpression[] parameter;
	private String[] parameterType;
	private boolean not;

	public ClassExpression(final String predicate, final int arity,
			final String[] parameterType) {
		this.predicate = predicate;
		this.parameter = new ClassExpression[arity];
		this.parameterType = parameterType;
		this.initializeIntersection(arity);
	}

	public double cardinality(final LinkedList<String> database) {
		final List<String[]> predicateObject = this.objects(database);
		if (this.predicate.startsWith(PrefixEnum.ACTION.prefix())) {
			return predicateObject.size();
		}
		double cardinality = 0;
		final Map<Integer, List<String[]>> parameterObject = new HashMap<>();
		for (int indexIntersection = 0; indexIntersection < this.intersection
				.size(); indexIntersection++) {
			if (this.intersection.get(indexIntersection) != null) {
				List<String[]> objectsList = new ArrayList<>();
				List<String[]> objectsTmp;
				final List<String[]> objectsTmp2 = new ArrayList<>();
				for (final ClassExpression i : this.intersection
						.get(indexIntersection)) {
					objectsTmp = i.objects(database);
					if (objectsList.size() == 0) {
						objectsList.addAll(objectsTmp);
					} else {
						objectsTmp2.addAll(objectsList);
						objectsList = new ArrayList<>();
						for (final String[] tmp : objectsTmp) {
							boolean contains = false;
							for (final String[] tmp2 : objectsTmp2) {
								if (tmp[0].equalsIgnoreCase(tmp2[0])) {
									contains = true;
									break;
								}
							}
							if (contains) {
								objectsList.add(tmp);
							}
						}
					}
				}
				parameterObject.put(indexIntersection, objectsList);
			} else {
				if (this.parameter[indexIntersection] != null) {
					parameterObject
							.put(indexIntersection,
									this.parameter[indexIntersection]
											.objects(database));
				}
			}
		}

		if (parameterObject.size() == 0) {
			cardinality = predicateObject.size();
		} else {
			final List<String[]> result = new ArrayList<>();
			for (int indexParameter = 0; indexParameter < this.parameter.length; indexParameter++) {
				if (parameterObject.get(indexParameter) != null) {
					for (final String[] tmp : predicateObject) {
						boolean contains = false;
						for (final String[] tmp2 : parameterObject
								.get(indexParameter)) {
							if (tmp[indexParameter].equalsIgnoreCase(tmp2[0])) {
								contains = true;
								break;
							}
						}
						if (contains) {
							result.add(tmp);
						}
					}
				}
			}
			cardinality = result.size();
		}
		return cardinality;
	}

	public double cardinality(final String pathProblem) throws IOException {
		final List<String[]> predicateObject = this.objects(pathProblem);
		if (this.predicate.startsWith(PrefixEnum.ACTION.prefix())) {
			return predicateObject.size();
		}
		double cardinality = 0;
		final Map<Integer, List<String[]>> parameterObject = new HashMap<>();
		for (int indexIntersection = 0; indexIntersection < this.intersection
				.size(); indexIntersection++) {
			if (this.intersection.get(indexIntersection) != null) {
				List<String[]> objectsList = new ArrayList<>();
				List<String[]> objectsTmp;
				final List<String[]> objectsTmp2 = new ArrayList<>();
				for (final ClassExpression i : this.intersection
						.get(indexIntersection)) {
					objectsTmp = i.objects(pathProblem);
					if (objectsList.size() == 0) {
						objectsList.addAll(objectsTmp);
					} else {
						objectsTmp2.addAll(objectsList);
						objectsList = new ArrayList<>();
						for (final String[] tmp : objectsTmp) {
							boolean contains = false;
							for (final String[] tmp2 : objectsTmp2) {
								if (tmp[0].equalsIgnoreCase(tmp2[0])) {
									contains = true;
									break;
								}
							}
							if (contains) {
								objectsList.add(tmp);
							}
						}
					}
				}
				parameterObject.put(indexIntersection, objectsList);
			} else {
				if (this.parameter[indexIntersection] != null) {
					parameterObject.put(indexIntersection,
							this.parameter[indexIntersection]
									.objects(pathProblem));
				}
			}
		}

		if (parameterObject.size() == 0) {
			cardinality = predicateObject.size();
		} else {
			final List<String[]> result = new ArrayList<>();
			for (int indexParameter = 0; indexParameter < this.parameter.length; indexParameter++) {
				if (parameterObject.get(indexParameter) != null) {
					for (final String[] tmp : predicateObject) {
						boolean contains = false;
						for (final String[] tmp2 : parameterObject
								.get(indexParameter)) {
							if (tmp[indexParameter].equalsIgnoreCase(tmp2[0])) {
								contains = true;
								break;
							}
						}
						if (contains) {
							result.add(tmp);
						}
					}
				}
			}
			cardinality = result.size();
		}
		return cardinality;
	}

	@Override
	public ClassExpression clone() throws CloneNotSupportedException {
		final ClassExpression classExpression = new ClassExpression(new String(
				this.predicate), this.parameter.length,
				this.parameterType.clone());
		final LinkedList<ClassExpression[]> intersectionNew = new LinkedList<>();
		for (final ClassExpression[] intersec : this.intersection) {
			if (intersec == null) {
				intersectionNew.addLast(null);
			} else {
				intersectionNew.addLast(intersec.clone());
			}
		}
		classExpression.setIntersection(intersectionNew);
		classExpression.setNot(new Boolean(this.not));
		classExpression.setParameter(this.parameter.clone());
		return classExpression;
	}

	public LinkedList<ClassExpression[]> getIntersection() {
		return this.intersection;
	}

	public ClassExpression[] getParameter() {
		return this.parameter;
	}

	public ClassExpression getParameter(final int index) {
		return this.parameter[index];
	}

	public String[] getParameterType() {
		return this.parameterType;
	}

	public String getPredicate() {
		return this.predicate;
	}

	private void initializeIntersection(final int arity) {
		this.intersection = new LinkedList<>();
		for (int countArity = 0; countArity < arity; countArity++) {
			this.intersection.addLast(null);
		}
	}

	public boolean isNot() {
		return this.not;
	}

	public List<String[]> objects(final LinkedList<String> database) {
		final List<String[]> objects = new ArrayList<>();
		for (final String strLine : database) {
			if (strLine.startsWith(this.predicate)) {
				if (this.predicate.startsWith(PrefixEnum.ACTION.prefix())) {
					objects.add(new String[] { PrefixEnum.ACTION.prefix() });
				} else {
					objects.add(strLine.replace(this.predicate + " ", "")
							.split(" "));
				}
			}
		}
		return objects;
	}

	public List<String[]> objects(final String pathProblem)
			throws FileNotFoundException, IOException {
		final List<String[]> objects = new ArrayList<>();
		DataInputStream dataInputStream = null;
		BufferedReader bufferedReader = null;
		try {
			dataInputStream = new DataInputStream(new FileInputStream(
					pathProblem));
			bufferedReader = new BufferedReader(new InputStreamReader(
					dataInputStream));
			String strLine;
			while ((strLine = bufferedReader.readLine()) != null) {
				if (strLine.startsWith(this.predicate)) {
					if (this.predicate.startsWith(PrefixEnum.ACTION.prefix())) {
						objects.add(new String[] { PrefixEnum.ACTION.prefix() });
					} else {
						objects.add(strLine.replace(this.predicate + " ", "")
								.split(" "));
					}
				}
			}
		} catch (final FileNotFoundException e) {
			throw e;
		} finally {
			if (dataInputStream != null) {
				dataInputStream.close();
			}
		}
		return objects;
	}

	public void setIntersection(final int index,
			final ClassExpression[] intersection) {
		this.intersection.set(index, intersection);
	}

	public void setIntersection(final LinkedList<ClassExpression[]> intersection) {
		this.intersection = intersection;
	}

	public void setNot(final boolean not) {
		this.not = not;
	}

	public void setParameter(final ClassExpression[] parameter) {
		this.parameter = parameter;
	}

	public void setParameter(final int index, final ClassExpression parameter) {
		this.parameter[index] = parameter;
	}

	public void setParameterType(final String[] parameterType) {
		this.parameterType = parameterType;
	}

	public void setPredicate(final String predicate) {
		this.predicate = predicate;
	}

	@Override
	public String toString() {
		final StringBuffer builder = new StringBuffer();
		if (this.not) {
			builder.append("¬ ");
		}
		builder.append(this.predicate + " ");
		for (int indexParameter = 0; indexParameter < this.parameter.length; indexParameter++) {
			if ((this.intersection != null)
					&& (this.intersection.get(indexParameter) != null)) {
				for (int indexIntersection = 0; indexIntersection < this.intersection
						.get(indexParameter).length; indexIntersection++) {
					if (indexIntersection > 0) {
						builder.append(" && ");
					}
					builder.append(this.intersection.get(indexParameter)[indexIntersection]
							.toString());
				}
			} else {
				if (this.parameter[indexParameter] == null) {
					builder.append(this.parameterType[indexParameter]);
				} else {
					builder.append(this.parameter[indexParameter].toString());
				}
			}
			if (indexParameter < (this.parameter.length - 1)) {
				builder.append(" ");
			}
		}
		return builder.toString();
	}
}
