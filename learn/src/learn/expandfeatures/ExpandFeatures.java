package learn.expandfeatures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import learn.pojos.ClassExpression;
import learn.pojos.Predicate;

public class ExpandFeatures {
	public static List<ClassExpression> relationalExtension(
			List<ClassExpression> seed, List<Predicate> predicates)
			throws CloneNotSupportedException {
		List<ClassExpression> result = new ArrayList<>();
		Iterator<ClassExpression> seedIterator = seed.iterator();
		Iterator<Predicate> predicateIterator = predicates.iterator();
		ClassExpression classExpression = null;
		ClassExpression classExpressionNew = null;
		Predicate predicate = null;
		int indexArity = 0;
		while (seedIterator.hasNext()) {
			classExpression = seedIterator.next();
			while (predicateIterator.hasNext()) {
				predicate = predicateIterator.next();
				for (indexArity = 0; indexArity < predicate.getArity(); indexArity++) {
					classExpressionNew = new ClassExpression(new String(
							predicate.getPredicate()), predicate.getArity());
					classExpressionNew.setParameter(
							indexArity,
							new ClassExpression(new String(classExpression
									.getPredicate()), classExpression
									.getParameter().length));
					result.add(classExpressionNew);
				}
			}
		}
		return result;
	}

	public static List<ClassExpression> specialize(List<ClassExpression> seed,
			List<ClassExpression> intersections) {
		List<ClassExpression> result = new ArrayList<>();
		Iterator<ClassExpression> seedIterator = seed.iterator();
		Iterator<ClassExpression> intersectionIterator = intersections
				.iterator();
		ClassExpression classExpressionNew = null;
		ClassExpression classExpression = null;
		ClassExpression intersection = null;
		ClassExpression parameters[] = null;
		ClassExpression parameter = null;
		ClassExpression intersectionNew[] = null;
		int indexParameter = 0;
		while (seedIterator.hasNext()) {
			classExpression = seedIterator.next();
			while (intersectionIterator.hasNext()) {
				intersection = intersectionIterator.next();
				parameters = classExpression.getParameter();
				for (indexParameter = 0; indexParameter < parameters.length; indexParameter++) {
					classExpressionNew = new ClassExpression(new String(
							classExpression.getPredicate()),
							classExpression.getParameter().length);
					parameter = parameters[indexParameter];
					if (parameter == null) {
						parameter = new ClassExpression(new String(
								intersection.getPredicate()),
								intersection.getParameter().length);
					} else {
						intersectionNew = new ClassExpression[2];
						intersectionNew[0] = new ClassExpression(new String(
								parameter.getPredicate()),
								parameter.getParameter().length);
						intersectionNew[1] = new ClassExpression(new String(
								intersection.getPredicate()),
								intersection.getParameter().length);
						parameter = new ClassExpression(null, 0);
						parameter.setIntersection(intersectionNew);
					}
					classExpressionNew.setParameter(indexParameter, parameter);
					result.add(classExpressionNew);
				}
			}
		}
		return result;
	}

	public static List<ClassExpression> complement(List<ClassExpression> seed)
			throws CloneNotSupportedException {
		List<ClassExpression> result = new ArrayList<>();
		Iterator<ClassExpression> seedIterator = seed.iterator();
		ClassExpression classExpressionNew = null;
		ClassExpression classExpression = null;
		while (seedIterator.hasNext()) {
			classExpression = seedIterator.next();
			classExpressionNew = new ClassExpression(new String(
					classExpression.getPredicate()),
					classExpression.getParameter().length);
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
