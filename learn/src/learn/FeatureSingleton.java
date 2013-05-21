package learn;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import learn.pojos.LearnParameters;

import org.apache.log4j.Logger;

import planner.javaff.data.UngroundProblem;
import planner.javaff.data.strips.PredicateSymbol;
import planner.javaff.data.strips.SimpleType;
import planner.javaff.data.strips.UngroundInstantAction;
import planner.javaff.data.strips.Variable;
import planner.javaff.parser.PDDL21parser;

import common.feature.ClassExpression;
import common.feature.PrefixEnum;

public final class FeatureSingleton {
	private static Map<String, LinkedList<ClassExpression>> features = new HashMap<>();
	private static final Logger LOGGER = Logger.getLogger(Learn.class);
	private static Map<String, List<String>> types = new HashMap<>();

	public static List<String> getTypes(final String problem) {
		return types.get(problem);
	}

	public static synchronized LinkedList<ClassExpression> initialFeatures(
			final String problem, final LearnParameters learnParameters)
			throws Exception {
		if (features.get(problem) == null) {
			final LinkedList<ClassExpression> featuresList = new LinkedList<>();
			final File domainFile = new File(learnParameters.getDomainPath());
			final File problemFile = new File(
					learnParameters.getExamplePathPrefix() + 1);
			final UngroundProblem unground = PDDL21parser.parseFiles(
					domainFile, problemFile);

			if (unground == null) {
				LOGGER.error("Parsing error - see console for details");
				throw new Exception("Parsing error - see console for detail");
			}

			@SuppressWarnings("unchecked")
			final Iterator<UngroundInstantAction> iteratorActions = unground.actions
					.iterator();

			ClassExpression classExpression;
			UngroundInstantAction ungroundInstantAction;
			while (iteratorActions.hasNext()) {
				ungroundInstantAction = iteratorActions.next();
				final String[] parameterType = new String[ungroundInstantAction.params
						.size()];
				@SuppressWarnings("unchecked")
				final Iterator<Variable> parameters = ungroundInstantAction.params
						.iterator();
				for (int indexParameter = 0; indexParameter < parameterType.length; indexParameter++) {
					parameterType[indexParameter] = parameters.next().getType()
							.toString();
				}
				classExpression = new ClassExpression(
						PrefixEnum.ACTION.prefix()
								+ ungroundInstantAction.name.toString(),
						ungroundInstantAction.params.size(), parameterType);
				featuresList.add(classExpression);
			}
			@SuppressWarnings("unchecked")
			final Iterator<PredicateSymbol> iteratorFacts = unground.predSymbols
					.iterator();
			PredicateSymbol predicateSymbol;
			while (iteratorFacts.hasNext()) {
				predicateSymbol = iteratorFacts.next();
				final String[] parameterType = new String[predicateSymbol
						.getParams().size()];
				@SuppressWarnings("unchecked")
				final Iterator<Variable> parameters = predicateSymbol
						.getParams().iterator();
				for (int indexParameter = 0; indexParameter < parameterType.length; indexParameter++) {
					parameterType[indexParameter] = parameters.next().getType()
							.toString();
				}
				classExpression = new ClassExpression(PrefixEnum.FACT.prefix()
						+ predicateSymbol.getName(), predicateSymbol
						.getParams().size(), parameterType);
				featuresList.add(classExpression);
				classExpression = new ClassExpression(PrefixEnum.ADD.prefix()
						+ predicateSymbol.getName(), predicateSymbol
						.getParams().size(), parameterType);
				featuresList.add(classExpression);
				classExpression = new ClassExpression(PrefixEnum.DEL.prefix()
						+ predicateSymbol.getName(), predicateSymbol
						.getParams().size(), parameterType);
				featuresList.add(classExpression);
				classExpression = new ClassExpression(PrefixEnum.GOAL.prefix()
						+ predicateSymbol.getName(), predicateSymbol
						.getParams().size(), parameterType);
				featuresList.add(classExpression);
				classExpression = new ClassExpression(
						PrefixEnum.CURRENT.prefix() + predicateSymbol.getName(),
						predicateSymbol.getParams().size(), parameterType);
				featuresList.add(classExpression);
			}
			final Iterator<SimpleType> iteratorT = unground.types.iterator();
			final List<String> typesProblem = new ArrayList<>();
			while (iteratorT.hasNext()) {
				typesProblem.add(iteratorT.next().toString());
			}
			features.put(problem, featuresList);
			types.put(problem, typesProblem);
		}
		return features.get(problem);
	}
}
