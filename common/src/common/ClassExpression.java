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

import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;

public class ClassExpression {
	private String predicate;
	private LinkedList<ClassExpression[]> intersection;
	private ClassExpression[] parameter;
	private String[] parameterType;
	private boolean not;

	public ClassExpression(String predicate, int arity, String[] parameterType) {
		this.predicate = predicate;
		this.parameter = new ClassExpression[arity];
		this.parameterType = parameterType;
		initializeIntersection(arity);
	}

	private void initializeIntersection(int arity) {
		this.intersection = new LinkedList<>();
		for (int countArity = 0; countArity < arity; countArity++) {
			intersection.addLast(null);
		}
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

	public LinkedList<ClassExpression[]> getIntersection() {
		return intersection;
	}

	public void setIntersection(LinkedList<ClassExpression[]> intersection) {
		this.intersection = intersection;
	}

	public void setIntersection(int index, ClassExpression[] intersection) {
		this.intersection.set(index, intersection);
	}

	public String[] getParameterType() {
		return parameterType;
	}

	public void setParameterType(String[] parameterType) {
		this.parameterType = parameterType;
	}

	@Override
	public String toString() {
		StringBuffer builder = new StringBuffer();
		if (not) {
			builder.append("¬ ");
		}
		builder.append(predicate + " ");
		for (int indexParameter = 0; indexParameter < parameter.length; indexParameter++) {
			if (intersection != null && intersection.get(indexParameter) != null) {
				for (int indexIntersection = 0; indexIntersection < intersection.get(indexParameter).length; indexIntersection++) {
					if (indexIntersection > 0) {
						builder.append(" && ");
					}
					builder.append(intersection.get(indexParameter).toString());
				}
			} else {
				if (parameter[indexParameter] == null) {
					builder.append(parameterType[indexParameter]);
				} else {
					builder.append(parameter[indexParameter].toString());
				}
			}
			if (indexParameter < parameter.length - 1) {
				builder.append(" ");
			}
		}
		return builder.toString();
	}

	@Override
	public ClassExpression clone() throws CloneNotSupportedException {
		ClassExpression classExpression = new ClassExpression(new String(
				predicate), parameter.length, parameterType.clone());
		LinkedList<ClassExpression[]> intersectionNew = new LinkedList<>();
		int index = 0;
		for (ClassExpression[] intersec : intersection) {
			intersectionNew.set(index, intersec.clone());
			index++;
		}
		classExpression.setIntersection(intersectionNew);
		classExpression.setNot(new Boolean(not));
		classExpression.setParameter(parameter.clone());
		return classExpression;
	}

	public double cardinality(String pathProblem) throws IOException {
		List<String[]> predicateObject = objects(pathProblem);
		if (predicate.startsWith(PrefixEnum.ACTION.prefix())) {
			return predicateObject.size();
		}
		double cardinality = 0;
		Map<Integer, List<String[]>> parameterObject = new HashMap<>();
		for (int indexIntersection = 0; indexIntersection < intersection.size(); indexIntersection++) {
			if (intersection.get(indexIntersection) != null) {
				List<String[]> objectsList = new ArrayList<>();
				List<String[]> objectsTmp;
				List<String[]> objectsTmp2 = new ArrayList<>();
				for (ClassExpression i : intersection.get(indexIntersection)) {
					objectsTmp = i.objects(pathProblem);
					if (objectsList.size() == 0) {
						objectsList.addAll(objectsTmp);
					} else {
						objectsTmp2.addAll(objectsList);
						for (String[] tmp : objectsTmp) {
							boolean contains = false;
							for (String[] tmp2 : objectsTmp2) {
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
				if (parameter[indexIntersection] != null) {
					parameterObject.put(indexIntersection,
							parameter[indexIntersection].objects(pathProblem));
				}
			}
		}

		if (parameterObject.size() == 0) {
			cardinality = predicateObject.size();
		} else {
			List<String[]> result = new ArrayList<>();
			for (int indexParameter = 0; indexParameter < parameter.length; indexParameter++) {
				if (parameterObject.get(indexParameter) != null) {
					for (String[] tmp : predicateObject) {
						boolean contains = false;
						for (String[] tmp2 : parameterObject
								.get(indexParameter)) {
							if (tmp[indexParameter]
									.equalsIgnoreCase(tmp2[indexParameter])) {
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

	public List<String[]> objects(String pathProblem)
			throws FileNotFoundException, IOException {
		List<String[]> objects = new ArrayList<>();
		DataInputStream dataInputStream = null;
		BufferedReader bufferedReader = null;
		try {
			dataInputStream = new DataInputStream(new FileInputStream(
					pathProblem));
			bufferedReader = new BufferedReader(new InputStreamReader(
					dataInputStream));
			String strLine;
			while ((strLine = bufferedReader.readLine()) != null) {
				if (strLine.startsWith(predicate)) {
					if (predicate.startsWith(PrefixEnum.ACTION.prefix())) {
						objects.add(new String[] { PrefixEnum.ACTION.prefix() });
					} else {
						objects.add(strLine.replace(predicate + " ", "").split(
								" "));
					}
				}
			}
		} catch (FileNotFoundException e) {
			throw e;
		} finally {
			if (dataInputStream != null) {
				dataInputStream.close();
			}
		}
		return objects;
	}
}
