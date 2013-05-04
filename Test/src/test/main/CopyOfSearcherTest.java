package test.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import javaff.data.GroundProblem;
import javaff.data.Plan;
import javaff.data.TotalOrderPlan;
import javaff.data.UngroundProblem;
import javaff.parser.PDDL21parser;
import javaff.planning.HelpfulFilter;
import javaff.planning.NullFilter;
import javaff.planning.State;
import javaff.planning.TemporalMetricState;
import javaff.planning.TemporalMetricStateDelta;
import javaff.search.BestFirstSearch;
import javaff.search.EnforcedHillClimbingSearch;

public class CopyOfSearcherTest {
	public static boolean VALIDATE = false;

	public static PrintStream planOutput = System.out;
	public static PrintStream parsingOutput = System.out;
	public static PrintStream infoOutput = System.out;
	public static PrintStream errorOutput = System.err;

	public static void main(final String args[]) {
		testDepots();
		// File domainFile = new File(
		// "../Examples/IPC3/Tests1/Depots/Strips/Depots.pddl");
		// File problemFile = new File(
		// "../Examples/IPC3/Tests1/Depots/Strips/pfile2");
		//
		// File solutionFile = null;
		// solutionFile = new File(
		// "../Examples/IPC3/Tests1/Depots/Strips/pfile2Solution_MySearch.pddl");
		//
		// Plan plan = plan(domainFile, problemFile);
		//
		// if (solutionFile != null && plan != null) {
		// writePlanToFile(plan, solutionFile);
		// }
	}

	public static State performSearch(
			final TemporalMetricStateDelta initialState) {

		infoOutput.println("Performing search");

		// Now, initialise an BestFirst searcher
		// BestFirst bestFirst = new BestFirst(new LearnInPlannerState(
		// initialState), new HeuristicValueComparator());
		final BestFirstSearch bestFirst = new BestFirstSearch(initialState,
				Long.MAX_VALUE, null);

		bestFirst.setFilter(HelpfulFilter.getInstance()); // and use the helpful
		// actions neighbourhood

		// Try and find a plan using BestFirst
		final State goalState = bestFirst.search();

		if (goalState == null) {
			infoOutput.println("BestFirst failed");

		}
		return goalState; // return the plan
	}

	public static State performSearchBestFirstSearch(
			final TemporalMetricState initialState) {
		// Implementation of standard FF-style search

		infoOutput.println("Using best-first search, with all actions");

		// create a Best-First Searcher
		final BestFirstSearch BFS = new BestFirstSearch(initialState,
				Long.MAX_VALUE, null);

		BFS.setFilter(NullFilter.getInstance());

		final State goalState = BFS.search();

		return goalState; // return the plan
	}

	public static State performSearchFF(final TemporalMetricState initialState) {
		// Implementation of standard FF-style search

		infoOutput
				.println("Performing search as in FF - first considering EHC with only helpful actions");

		// Now, initialise an EHC searcher
		final EnforcedHillClimbingSearch EHCS = new EnforcedHillClimbingSearch(
				initialState);

		EHCS.setFilter(HelpfulFilter.getInstance()); // and use the helpful
														// actions neighbourhood

		// Try and find a plan using EHC
		State goalState = EHCS.search();

		if (goalState == null) // if we can't find one
		{
			infoOutput
					.println("EHC failed, using best-first search, with all actions");

			// create a Best-First Searcher
			final BestFirstSearch BFS = new BestFirstSearch(initialState,
					Long.MAX_VALUE, null);

			// ... change to using the 'all actions' neighbourhood (a null
			// filter, as it removes nothing)

			BFS.setFilter(NullFilter.getInstance());

			// and use that
			goalState = BFS.search();
		}

		return goalState; // return the plan
	}

	public static State performSearchFFModified(
			final TemporalMetricStateDelta initialState) {
		// Implementation of standard FF-style search

		infoOutput
				.println("Performing search as in FF - first considering EHC with only helpful actions");

		// Now, initialise an EHC searcher
		final EnforcedHillClimbingSearch EHCS = new EnforcedHillClimbingSearch(
				initialState);

		EHCS.setFilter(HelpfulFilter.getInstance()); // and use the helpful
														// actions neighbourhood

		// Try and find a plan using EHC
		State goalState = EHCS.search();

		if (goalState == null) // if we can't find one
		{
			infoOutput
					.println("EHC failed, using best-first search, with all actions");

			// create a Best-First Searcher
			final BestFirstSearch BFS = new BestFirstSearch(initialState,
					Long.MAX_VALUE, null);

			// ... change to using the 'all actions' neighbourhood (a null
			// filter, as it removes nothing)

			BFS.setFilter(NullFilter.getInstance());

			// and use that
			goalState = BFS.search();
		}

		return goalState; // return the plan
	}

	public static Plan plan(final File dFile, final File pFile) {
		// ********************************
		// Parse and Ground the Problem
		// ********************************
		final long startTime = System.currentTimeMillis();

		final UngroundProblem unground = PDDL21parser.parseFiles(dFile, pFile);

		if (unground == null) {
			System.out.println("Parsing error - see console for details");
			return null;
		}

		// PDDLPrinter.printDomainFile(unground, System.out);
		// PDDLPrinter.printProblemFile(unground, System.out);

		final GroundProblem ground = unground.ground(null, null, null, null);

		final long afterGrounding = System.currentTimeMillis();

		// ********************************
		// Search for a plan
		// ********************************

		// Get the initial state
		final TemporalMetricState initialState = ground
				.getTemporalMetricInitialState();

		final State goalState = performSearchFF(initialState);
		// State goalState = performSearchBestFirstSearch(initialState);

		final long afterPlanning = System.currentTimeMillis();

		TotalOrderPlan top = null;
		if (goalState != null) {
			top = (TotalOrderPlan) goalState.getSolution();
		}
		// if (top != null)
		// top.print(planOutput);

		final double groundingTime = (afterGrounding - startTime) / 1000.00;
		final double planningTime = (afterPlanning - afterGrounding) / 1000.00;

		infoOutput.println("Instantiation Time =\t\t" + groundingTime + "sec");
		infoOutput.println("Planning Time =\t" + planningTime + "sec");

		return top;
	}

	public static void testDepots() {
		final String domainFilePath = "../Examples/IPC3/Tests1/Depots/Strips/Depots.pddl";
		final String problemFilePathPrefix = "../Examples/IPC3/Tests1/Depots/Strips/pfile";
		final String solutionFilePathPrefix = "../Examples/IPC3/Tests1/Depots/Strips/mysearch/pfileSolution_FFSearch";
		File domainFile = null;
		File problemFile = null;
		File solutionFile = null;
		for (int index = 1; index < 22; index++) {
			domainFile = new File(domainFilePath);
			problemFile = new File(problemFilePathPrefix + index);
			solutionFile = new File(solutionFilePathPrefix + index + ".pddl");
			final Plan plan = plan(domainFile, problemFile);
			if ((solutionFile != null) && (plan != null)) {
				writePlanToFile(plan, solutionFile);
			}
		}
	}

	private static void writePlanToFile(final Plan plan, final File fileOut) {
		try {
			final FileOutputStream outputStream = new FileOutputStream(fileOut);
			final PrintWriter printWriter = new PrintWriter(outputStream);
			plan.print(printWriter);
			printWriter.close();
		} catch (final FileNotFoundException e) {
			errorOutput.println(e);
			e.printStackTrace();
		}
	}

}
