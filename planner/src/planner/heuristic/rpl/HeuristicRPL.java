package planner.heuristic.rpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import planner.javaff.data.Action;
import planner.javaff.data.TotalOrderPlan;
import planner.javaff.data.strips.Proposition;
import planner.javaff.planning.MetricState;


import common.feature.PrefixEnum;

public class HeuristicRPL {
	public static Solution calculate(final MetricState initialState) {
		final Solution solution = new Solution();
		solution.setValue(initialState.getHValue());
		final TotalOrderPlan top = (TotalOrderPlan) initialState.getRPG()
				.getPlan(initialState);
		solution.setRelaxedPlan(top);
		return solution;
	}

	public static LinkedList<String> generateDatabase(
			final MetricState currentState) {
		final LinkedList<String> database = new LinkedList<>();
		final Set<Proposition> facts = currentState.facts;
		final Iterator<Proposition> factsIterator = facts.iterator();
		while (factsIterator.hasNext()) {
			final Proposition proposition = factsIterator.next();
			database.addLast(PrefixEnum.FACT.prefix() + proposition.toString());
		}
		final Solution solution = calculate(currentState);
		Action action = null;
		Proposition proposition = null;

		Iterator<Action> actionsIterator = null;
		Iterator<Proposition> propostionsIterator = null;

		@SuppressWarnings("unchecked")
		final Set<Action> actions = (Set<Action>) solution.getRelaxedPlan()
				.getActions();
		Set<Proposition> propositions = null;

		if (actions != null) {
			actionsIterator = actions.iterator();
			while (actionsIterator.hasNext()) {
				action = actionsIterator.next();
				propositions = action.getAddPropositions();
				if (propositions != null) {
					propostionsIterator = propositions.iterator();
					while (propostionsIterator.hasNext()) {
						proposition = propostionsIterator.next();
						database.addLast(PrefixEnum.ADD.prefix()
								+ proposition.toString());
					}
				}
				propositions = action.getDeletePropositions();
				if (propositions != null) {
					propostionsIterator = propositions.iterator();
					while (propostionsIterator.hasNext()) {
						proposition = propostionsIterator.next();
						database.addLast(PrefixEnum.DEL.prefix()
								+ proposition.toString());
					}
				}

			}
		}

		if ((currentState.goal != null)
				&& (currentState.goal.getConditionalPropositions() != null)) {
			final Iterator<Proposition> iterator = currentState.goal
					.getConditionalPropositions().iterator();
			while (iterator.hasNext()) {
				proposition = iterator.next();
				database.addLast(PrefixEnum.GOAL.prefix()
						+ proposition.toString());
			}

			final Iterator<Proposition> factsAtual = currentState.facts
					.iterator();
			Iterator<Proposition> factsGoal = null;
			final List<Proposition> factsEquals = new ArrayList<>();
			Proposition factAtual = null;
			Proposition factGoal = null;
			while (factsAtual.hasNext()) {
				factAtual = factsAtual.next();
				factsGoal = currentState.goal.getConditionalPropositions()
						.iterator();
				while (factsGoal.hasNext()) {
					factGoal = factsGoal.next();
					if (factAtual.toString().equalsIgnoreCase(
							factGoal.toString())) {
						factsEquals.add(factGoal);
						break;
					}
				}
			}
			final Iterator<Proposition> factsEqualsIterator = factsEquals
					.iterator();
			while (factsEqualsIterator.hasNext()) {
				database.addLast(PrefixEnum.CURRENT.prefix()
						+ factsEqualsIterator.next().toString());
			}
		}
		return database;
	}

}
