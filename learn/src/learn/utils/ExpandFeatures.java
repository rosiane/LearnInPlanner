package learn.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import common.feature.ClassExpression;
import common.feature.PrefixEnum;

public class ExpandFeatures {
	private static LinkedList<ClassExpression> clean(
			final LinkedList<ClassExpression> list) {
		final LinkedList<ClassExpression> cleanedList = new LinkedList<>();
		for (final ClassExpression item : list) {
			if (!contains(cleanedList, item)) {
				cleanedList.add(item);
			}
		}
		return cleanedList;
	}

	private static LinkedList<ClassExpression> complement(
			final LinkedList<ClassExpression> newExpressionsCurrent,
			final LinkedList<ClassExpression> seed)
			throws CloneNotSupportedException {
		final LinkedList<ClassExpression> result = new LinkedList<>();
		final Iterator<ClassExpression> seedIterator = seed.iterator();
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
				result.add(classExpressionNew);
			}
		}
		return result;
	}

	private static boolean contains(final LinkedList<ClassExpression> list,
			final ClassExpression classExpression) {
		for (final ClassExpression item : list) {
			if (item.toString().equalsIgnoreCase(classExpression.toString())) {
				return true;
			}
		}
		return false;
	}

	public static LinkedList<ClassExpression> expand(
			final LinkedList<ClassExpression> seed, final List<String> types)
			throws CloneNotSupportedException {
		LinkedList<ClassExpression> result = new LinkedList<>();
		result.addAll(seed);
		result.addAll(relationalExtension(result, seed, types));
		result.addAll(specialize(result, seed, types));
		result.addAll(complement(result, seed));
		result = clean(result);
		return result;
	}

	private static String getType(final List<String> types,
			final String typeSpecific) {
		String type = null;
		for (final String typeTmp : types) {
			if (typeTmp.startsWith(typeSpecific)) {
				type = typeTmp;
				break;
			}
		}
		return type;
	}

	private static LinkedList<ClassExpression> relationalExtension(
			final LinkedList<ClassExpression> newExpressionsCurrent,
			final LinkedList<ClassExpression> seed, final List<String> types)
			throws CloneNotSupportedException {
		final LinkedList<ClassExpression> result = new LinkedList<>();
		final Iterator<ClassExpression> seedIterator = seed.iterator();
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
								final ClassExpression[] parameterClean = new ClassExpression[classExpressionNew
										.getParameter(indexArity)
										.getParameterType().length];
								classExpressionNew.getParameter(indexArity)
										.setParameter(parameterClean);
								result.add(classExpressionNew);
							}
						}
					}
				}
			}
		}
		return result;
	}

	private static LinkedList<ClassExpression> specialize(
			final LinkedList<ClassExpression> newExpressionsCurrent,
			final LinkedList<ClassExpression> seed, final List<String> types)
			throws CloneNotSupportedException {
		final LinkedList<ClassExpression> result = new LinkedList<>();
		final Iterator<ClassExpression> seedIterator = seed.iterator();
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
								if ((parameter != null)
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

}
