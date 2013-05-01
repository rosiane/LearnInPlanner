package learn.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import common.ClassExpression;
import common.PrefixEnum;

public class ExpandFeatures {
	private static String getType(List<String> types, String typeSpecific) {
		String type = null;
		for (String typeTmp : types) {
			if (typeTmp.startsWith(typeSpecific)) {
				type = typeTmp;
				break;
			}
		}
		return type;
	}

	private static boolean contains(LinkedList<ClassExpression> list,
			ClassExpression classExpression) {
		for (ClassExpression item : list) {
			if (item.toString().equalsIgnoreCase(classExpression.toString())) {
				return true;
			}
		}
		return false;
	}

	public static LinkedList<ClassExpression> expand(
			LinkedList<ClassExpression> seed, List<String> types)
			throws CloneNotSupportedException {
		LinkedList<ClassExpression> result = new LinkedList<>();
		result.addAll(seed);
		result.addAll(relationalExtension(result, seed, types));
		result.addAll(specialize(result, seed, types));
		result.addAll(complement(result, seed));
		return result;
	}

	private static LinkedList<ClassExpression> relationalExtension(
			LinkedList<ClassExpression> newExpressionsCurrent,
			LinkedList<ClassExpression> seed, List<String> types)
			throws CloneNotSupportedException {
		LinkedList<ClassExpression> result = new LinkedList<>();
		Iterator<ClassExpression> seedIterator = seed.iterator();
		ClassExpression classExpression = null;
		ClassExpression classExpressionCurrent = null;
		String typeCurrent;
		int indexArity = 0;
		ClassExpression classExpressionNew = null;
		while (seedIterator.hasNext()) {
			classExpression = seedIterator.next();
			if (!classExpression.getPredicate().startsWith(
					PrefixEnum.ACTION.prefix())) {
				for (int indexSeed = 0; indexSeed < seed.size(); indexSeed++) {
					classExpressionCurrent = seed.get(indexSeed);
					if (!classExpressionCurrent.getPredicate().startsWith(
							PrefixEnum.ACTION.prefix())
							&& !classExpression.getPredicate()
									.equalsIgnoreCase(
											classExpressionCurrent
													.getPredicate())) {
						typeCurrent = classExpressionCurrent.getParameterType()[0];
						for (indexArity = 0; indexArity < classExpression
								.getParameter().length; indexArity++) {
							if (classExpression.getParameterType()[indexArity]
									.contains(getType(types, typeCurrent))) {
								classExpressionNew = new ClassExpression(
										classExpression.getPredicate(),
										classExpression.getParameter().length,
										classExpression.getParameterType());
								classExpressionNew.setParameter(indexArity,
										classExpressionCurrent.clone());
								ClassExpression[] parameterClean = new ClassExpression[classExpressionNew
										.getParameter(indexArity)
										.getParameterType().length];
								classExpressionNew.getParameter(indexArity)
										.setParameter(parameterClean);
								if (!contains(newExpressionsCurrent,
										classExpressionNew)) {
									result.add(classExpressionNew);
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	private static LinkedList<ClassExpression> specialize(
			LinkedList<ClassExpression> newExpressionsCurrent,
			LinkedList<ClassExpression> seed, List<String> types)
			throws CloneNotSupportedException {
		LinkedList<ClassExpression> result = new LinkedList<>();
		Iterator<ClassExpression> seedIterator = seed.iterator();
		ClassExpression classExpressionNew = null;
		ClassExpression classExpression = null;
		ClassExpression intersection = null;
		ClassExpression parameters[] = null;
		ClassExpression parameter = null;
		ClassExpression intersectionNew[] = null;
		int indexParameter = 0;
		String typeCurrent;
		while (seedIterator.hasNext()) {
			classExpression = seedIterator.next();
			if (!classExpression.getPredicate().startsWith(
					PrefixEnum.ACTION.prefix())) {
				for (int indexSeed = 0; indexSeed < seed.size(); indexSeed++) {
					intersection = seed.get(indexSeed);
					if (!intersection.getPredicate().startsWith(
							PrefixEnum.ACTION.prefix())
							&& !classExpression.getPredicate()
									.equalsIgnoreCase(
											intersection.getPredicate())) {
						parameters = classExpression.getParameter();
						typeCurrent = intersection.getParameterType()[0];
						for (indexParameter = 0; indexParameter < parameters.length; indexParameter++) {
							if (classExpression.getParameterType()[indexParameter]
									.contains(getType(types, typeCurrent))) {
								classExpressionNew = new ClassExpression(
										new String(classExpression
												.getPredicate()),
										classExpression.getParameter().length,
										classExpression.getParameterType());
								parameter = parameters[indexParameter];
								if (parameter != null
										&& !parameter
												.getPredicate()
												.equalsIgnoreCase(
														intersection
																.getPredicate())) {
									intersectionNew = new ClassExpression[2];
									intersectionNew[0] = parameter.clone();
									intersectionNew[1] = intersection.clone();
									classExpressionNew.setIntersection(
											indexParameter, intersectionNew);
									if (!contains(newExpressionsCurrent,
											classExpressionNew)) {
										result.add(classExpressionNew);
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	private static LinkedList<ClassExpression> complement(
			LinkedList<ClassExpression> newExpressionsCurrent,
			LinkedList<ClassExpression> seed) throws CloneNotSupportedException {
		LinkedList<ClassExpression> result = new LinkedList<>();
		Iterator<ClassExpression> seedIterator = seed.iterator();
		ClassExpression classExpressionNew = null;
		ClassExpression classExpression = null;
		while (seedIterator.hasNext()) {
			classExpression = seedIterator.next();
			if (!classExpression.getPredicate().startsWith(
					PrefixEnum.ACTION.prefix())) {
				classExpressionNew = classExpression.clone();
				if (classExpression.isNot()) {
					classExpressionNew.setNot(false);
				} else {
					classExpressionNew.setNot(true);
				}
				if (!contains(newExpressionsCurrent, classExpressionNew)) {
					result.add(classExpressionNew);
				}
			}
		}
		return result;
	}

}
