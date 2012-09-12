package preprocessor.utils;

import heuristic.rpl.HeuristicRPL;
import heuristic.rpl.Solution;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javaff.data.Action;
import javaff.data.strips.Proposition;
import javaff.planning.TemporalMetricState;
import preprocessor.file.FileManager;

public class GenerateExampleUtils {
	public static void printValueToEstimate(Solution solution,
			String fileSolution, int distanceToGoal) throws IOException {
		int delta = distanceToGoal - solution.getValue().intValue();
		FileManager.write(fileSolution, "" + delta + "", true);
	}

	public static void printCurrentEqualsGoal(TemporalMetricState currentState,
			String fileSolution) throws IOException {
		if (currentState.goal != null
				&& currentState.goal.getConditionalPropositions() != null) {
			Iterator<Proposition> factsAtual = currentState.facts.iterator();
			Iterator<Proposition> factsGoal = null;
			List<Proposition> factsEquals = new ArrayList<>();
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
			PrintWriter printWriter = null;
			try {
				Iterator<Proposition> factsEqualsIterator = factsEquals
						.iterator();
				printWriter = new PrintWriter(
						new FileWriter(fileSolution, true));
				printWriter.println("(:"
						+ TargetDatabaseEnum.CURRENT.getLabel());
				while (factsEqualsIterator.hasNext()) {
					printWriter.println("\t("
							+ factsEqualsIterator.next().toString() + ")");
				}
				printWriter.println(")");
			} catch (IOException e) {
				throw e;
			} finally {
				if (printWriter != null) {
					printWriter.close();
				}
			}
		}
	}

	public static void printGoal(TemporalMetricState currentState,
			String fileSolution) throws IOException {
		PrintWriter printWriter = null;
		try {
			if (currentState.goal != null
					&& currentState.goal.getConditionalPropositions() != null) {
				Iterator<Proposition> iterator = currentState.goal
						.getConditionalPropositions().iterator();
				Proposition proposition = null;
				printWriter = new PrintWriter(
						new FileWriter(fileSolution, true));
				printWriter.println("(:" + TargetDatabaseEnum.GOAL.getLabel());
				while (iterator.hasNext()) {
					proposition = iterator.next();
					printWriter.println("\t(" + proposition.toString() + ")");
				}
				printWriter.println(")");
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}
	}

	public static void printAddList(Solution solution, String fileSolution)
			throws IOException {
		PrintWriter printWriter = null;

		Action action = null;
		Proposition proposition = null;

		Iterator<Action> actionsIterator = null;
		Iterator<Proposition> propostionsIterator = null;

		@SuppressWarnings("unchecked")
		Set<Action> actions = (Set<Action>) solution.getRelaxedPlan()
				.getActions();
		Set<Proposition> propositions = null;

		try {
			printWriter = new PrintWriter(new FileWriter(fileSolution, true));
			if (actions != null) {
				printWriter.println("(:" + TargetDatabaseEnum.ADD.getLabel());
				actionsIterator = actions.iterator();
				while (actionsIterator.hasNext()) {
					action = actionsIterator.next();
					propositions = action.getAddPropositions();
					if (propositions != null) {
						propostionsIterator = propositions.iterator();
						while (propostionsIterator.hasNext()) {
							proposition = propostionsIterator.next();
							printWriter.println("\t(" + proposition.toString()
									+ ")");
						}
					}
				}
				printWriter.println(")");
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}
	}

	public static void printDeleteList(Solution solution, String fileSolution)
			throws IOException {
		PrintWriter printWriter = null;

		Action action = null;
		Proposition proposition = null;

		Iterator<Action> actionsIterator = null;
		Iterator<Proposition> propostionsIterator = null;

		@SuppressWarnings("unchecked")
		Set<Action> actions = (Set<Action>) solution.getRelaxedPlan()
				.getActions();
		Set<Proposition> propositions = null;

		try {
			printWriter = new PrintWriter(new FileWriter(fileSolution, true));
			if (actions != null) {
				printWriter
						.println("(:" + TargetDatabaseEnum.DELETE.getLabel());
				actionsIterator = actions.iterator();
				while (actionsIterator.hasNext()) {
					action = actionsIterator.next();
					propositions = action.getDeletePropositions();
					if (propositions != null) {
						propostionsIterator = propositions.iterator();
						while (propostionsIterator.hasNext()) {
							proposition = propostionsIterator.next();
							printWriter.println("\t(" + proposition.toString()
									+ ")");
						}
					}
				}
				printWriter.println(")");
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}
	}

	public static void printStatesFactsInS(TemporalMetricState currentState,
			String fileSolution) throws IOException {
		PrintWriter printWriter = null;
		try {
			Set<Proposition> facts = currentState.facts;

			Iterator<Proposition> factsIterator = facts.iterator();
			printWriter = new PrintWriter(new FileWriter(fileSolution, true));
			printWriter.println("(:" + TargetDatabaseEnum.INIT.getLabel());
			while (factsIterator.hasNext()) {
				Proposition proposition = factsIterator.next();
				printWriter.println("\t(" + proposition.toString() + ")");
			}
			printWriter.println(")");
		} catch (IOException e) {
			throw e;
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}
	}

	public static Solution calculateRPL(Solution solution,
			TemporalMetricState currentState, String fileSolution)
			throws IOException {
		PrintWriter printWriter = null;
		try {
			solution = HeuristicRPL.calculate(currentState);
			FileManager.write(fileSolution, solution.getValue().toString(),
					true);
			printWriter = new PrintWriter(new FileWriter(fileSolution, true));
			solution.getRelaxedPlan().print(printWriter);
		} catch (IOException e) {
			throw e;
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}
		return solution;
	}

	public static boolean existSolutionFF(String path) {
		return new File(path).exists();
	}

}
