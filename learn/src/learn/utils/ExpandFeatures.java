package learn.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import common.ClassExpression;


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

	public static LinkedList<ClassExpression> expand(LinkedList<ClassExpression> seed,
			List<String> types) throws CloneNotSupportedException {
		LinkedList<ClassExpression> result = new LinkedList<>();
		result.addAll(seed);
		result.addAll(relationalExtension(seed, types));
		result.addAll(specialize(seed, types));
		result.addAll(complement(seed));
		return result;
	}

	private static LinkedList<ClassExpression> relationalExtension(
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
			for (int indexSeed = 0; indexSeed < seed.size(); indexSeed++) {
				classExpressionCurrent = seed.get(indexSeed);
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
						result.add(classExpressionNew);
					}
				}
			}
		}
		return result;
	}

	private static LinkedList<ClassExpression> specialize(LinkedList<ClassExpression> seed,
			List<String> types) throws CloneNotSupportedException {
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
			for (int indexSeed = 0; indexSeed < seed.size(); indexSeed++) {
				intersection = seed.get(indexSeed);
				parameters = classExpression.getParameter();
				typeCurrent = intersection.getParameterType()[0];
				for (indexParameter = 0; indexParameter < parameters.length; indexParameter++) {
					if (classExpression.getParameterType()[indexParameter]
							.contains(getType(types, typeCurrent))) {
						classExpressionNew = new ClassExpression(new String(
								classExpression.getPredicate()),
								classExpression.getParameter().length,
								classExpression.getParameterType());
						parameter = parameters[indexParameter];
						if (parameter == null) {
							parameter = intersection.clone();
						} else {
							intersectionNew = new ClassExpression[2];
							intersectionNew[0] = parameter.clone();
							intersectionNew[1] = intersection.clone();
							parameter.setIntersection(indexParameter, intersectionNew);
						}
						classExpressionNew.setParameter(indexParameter,
								parameter);
						result.add(classExpressionNew);
					}
				}
			}
		}
		return result;
	}

	private static LinkedList<ClassExpression> complement(LinkedList<ClassExpression> seed)
			throws CloneNotSupportedException {
		LinkedList<ClassExpression> result = new LinkedList<>();
		Iterator<ClassExpression> seedIterator = seed.iterator();
		ClassExpression classExpressionNew = null;
		ClassExpression classExpression = null;
		while (seedIterator.hasNext()) {
			classExpression = seedIterator.next();
			classExpressionNew = classExpression.clone();
			if (classExpression.isNot()) {
				classExpressionNew.setNot(false);
			} else {
				classExpressionNew.setNot(true);
			}
			result.add(classExpressionNew);
		}
		return result;
	}

}
