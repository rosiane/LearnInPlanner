package test.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigDecimal;

import planner.heuristic.delta.ParameterReader;
import planner.heuristic.delta.ParameterSearch;
import planner.heuristic.delta.ReaderParameterSearch;
import planner.javaff.data.GroundProblem;
import planner.javaff.data.Plan;
import planner.javaff.data.TotalOrderPlan;
import planner.javaff.data.UngroundProblem;
import planner.javaff.parser.PDDL21parser;
import planner.javaff.planning.HelpfulFilter;
import planner.javaff.planning.NullFilter;
import planner.javaff.planning.State;
import planner.javaff.planning.TemporalMetricStateDelta;
import planner.javaff.search.BestFirstSearch;
import planner.javaff.search.BreadthFirstSearch;
import planner.javaff.search.EnforcedHillClimbingSearch;
import planner.javaff.search.LimitedEnforcedHillClimbingSearch;

import common.preprocessor.file.FileManager;

public class SearcherTest {
	public static boolean VALIDATE = false;

	public static PrintStream planOutput = System.out;
	public static PrintStream parsingOutput = System.out;
	public static PrintStream infoOutput = System.out;
	public static PrintStream errorOutput = System.err;

	public static void main(final String args[]) {
//		testDepots();
		try {
			testDepotsNewHeuristic();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// testDepotsMemory();
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

		final long timeLimit = 30 * 60 * 1000;
		final String fileResult = "../Examples/IPC3/Tests1/Depots/Strips/mysearch/performance";

		// Now, initialise an BestFirst searcher
		final BestFirstSearch bestFirst = new BestFirstSearch(initialState,
				timeLimit, fileResult);

		// using the 'all actions' neighbourhood (a null filter, as it removes
		// nothing)
		bestFirst.setFilter(NullFilter.getInstance());

		// Try and find a plan using BestFirst
		final State goalState = bestFirst.search();

		if (goalState == null) {
			infoOutput.println("BestFirst failed");
			try {
				FileManager.write(fileResult, "BestFirst failed", true);
			} catch (final IOException e) {
				e.printStackTrace();
			}

		}
		return goalState; // return the plan
	}

	public static State performSearchBreadthFirstSearch(
			final TemporalMetricStateDelta initialState) {

		infoOutput.println("Performing search breadthFirstSearch");

		// Now, initialise an BestFirst searcher
		// BestFirst bestFirst = new BestFirst(new LearnInPlannerState(
		// initialState), new HeuristicValueComparator());
		final BreadthFirstSearch breadthFirstSearch = new BreadthFirstSearch(
				initialState);

		breadthFirstSearch.setFilter(HelpfulFilter.getInstance()); // and use
																	// the
																	// helpful
		// actions neighbourhood

		// Try and find a plan using BestFirst
		final State goalState = breadthFirstSearch.search();

		if (goalState == null) {
			infoOutput.println("BreadthFirstSearch failed");

		}
		return goalState; // return the plan
	}

	public static State performSearchFFLimited(
			final TemporalMetricStateDelta initialState) {
		// Implementation of standard FF-style search

		infoOutput
				.println("Performing search as in FF - first considering EHC with only helpful actions");

		// Now, initialise an EHC searcher
		final LimitedEnforcedHillClimbingSearch EHCS = new LimitedEnforcedHillClimbingSearch(
				initialState, new BigDecimal(200));

		EHCS.setFilter(HelpfulFilter.getInstance()); // and use the helpful
														// actions neighbourhood

		// Try and find a plan using EHC
		final State goalState = EHCS.search();

		// if (goalState == null) // if we can't find one
		// {
		// infoOutput
		// .println("EHC failed, using best-first search, with all actions");
		//
		// // create a Best-First Searcher
		// BestFirstSearch BFS = new BestFirstSearch(initialState);
		//
		// // ... change to using the 'all actions' neighbourhood (a null
		// // filter, as it removes nothing)
		//
		// BFS.setFilter(NullFilter.getInstance());
		//
		// // and use that
		// goalState = BFS.search();
		// }

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

		EHCS.setFilter(HelpfulFilter.getInstance());

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

		final String fileResult = "../Examples/IPC3/Tests1/Depots/Strips/mysearch/performance";
		try {
			FileManager.write(fileResult, "Domain " + dFile.getAbsolutePath(),
					true);

			FileManager.write(fileResult, "Problem " + pFile.getAbsolutePath(),
					true);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		// PDDLPrinter.printDomainFile(unground, System.out);
		// PDDLPrinter.printProblemFile(unground, System.out);

		final GroundProblem ground = unground.ground(null);

		final long afterGrounding = System.currentTimeMillis();

		// ********************************
		// Search for a plan
		// ********************************

		// Get the initial state
		final TemporalMetricStateDelta initialState = ground
				.getTemporalMetricInitialStateDelta();

		// State goalState = performSearchFFModified(initialState);
		final State goalState = performSearch(initialState);
		// State goalState = performSearchFFLimited(initialState);
		// State goalState = performSearchBreadthFirstSearch(initialState);

		final long afterPlanning = System.currentTimeMillis();

		TotalOrderPlan top = null;
		if (goalState != null) {
			top = (TotalOrderPlan) goalState.getSolution();
			infoOutput.println("Plan Lenght " + top.getPlanLength());
			try {
				FileManager.write(fileResult,
						"Plan Lenght " + top.getPlanLength(), true);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		final double groundingTime = (afterGrounding - startTime) / 1000.00;
		final double planningTime = (afterPlanning - afterGrounding) / 1000.00;

		infoOutput.println("Instantiation Time =\t\t" + groundingTime + "sec");
		infoOutput.println("Planning Time =\t" + planningTime + "sec");

		try {
			FileManager.write(fileResult, "Instantiation Time =\t\t"
					+ groundingTime + "sec", true);
			FileManager.write(fileResult, "Planning Time =\t" + planningTime
					+ "sec", true);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return top;
	}

	public static Plan plan(final File dFile, final File pFile,
			final ParameterSearch parameterSearch) {
		// ********************************
		// Parse and Ground the Problem
		// ********************************
		final long startTime = System.currentTimeMillis();

		final UngroundProblem unground = PDDL21parser.parseFiles(dFile, pFile);

		if (unground == null) {
			System.out.println("Parsing error - see console for details");
			return null;
		}

		final String fileResult = "../Examples/IPC3/Tests1/Depots/Strips/mysearch/performance";
		try {
			FileManager.write(fileResult, "Domain " + dFile.getAbsolutePath(),
					true);

			FileManager.write(fileResult, "Problem " + pFile.getAbsolutePath(),
					true);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		// PDDLPrinter.printDomainFile(unground, System.out);
		// PDDLPrinter.printProblemFile(unground, System.out);

		final GroundProblem ground = unground.ground(parameterSearch);

		final long afterGrounding = System.currentTimeMillis();

		// ********************************
		// Search for a plan
		// ********************************

		// Get the initial state
		final TemporalMetricStateDelta initialState = ground
				.getTemporalMetricInitialStateDelta();

		// State goalState = performSearchFFModified(initialState);
		final State goalState = performSearch(initialState);
		// State goalState = performSearchFFLimited(initialState);
		// State goalState = performSearchBreadthFirstSearch(initialState);

		final long afterPlanning = System.currentTimeMillis();

		TotalOrderPlan top = null;
		if (goalState != null) {
			top = (TotalOrderPlan) goalState.getSolution();
			infoOutput.println("Plan Lenght " + top.getPlanLength());
			try {
				FileManager.write(fileResult,
						"Plan Lenght " + top.getPlanLength(), true);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		final double groundingTime = (afterGrounding - startTime) / 1000.00;
		final double planningTime = (afterPlanning - afterGrounding) / 1000.00;

		infoOutput.println("Instantiation Time =\t\t" + groundingTime + "sec");
		infoOutput.println("Planning Time =\t" + planningTime + "sec");

		try {
			FileManager.write(fileResult, "Instantiation Time =\t\t"
					+ groundingTime + "sec", true);
			FileManager.write(fileResult, "Planning Time =\t" + planningTime
					+ "sec", true);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return top;
	}

	public static void testDepots() {
		final String domainFilePath = "../Examples/IPC3/Tests1/Depots/Strips/Depots.pddl";
		final String problemFilePathPrefix = "../Examples/IPC3/Tests1/Depots/Strips/pfile";
		final String solutionFilePathPrefix = "../Examples/IPC3/Tests1/Depots/Strips/mysearch/pfileSolution_MySearch";
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

	public static void testDepotsMemory() {
		final String domainFilePath = "../Examples/IPC3/Tests1/Depots/Strips/Depots.pddl";
		final String problemFilePath = "../Examples/IPC3/Tests1/Depots/Strips/pfile3";
		final String solutionFilePath = "../Examples/IPC3/Tests1/Depots/Strips/mysearch/pfileSolution_MySearch3.pddl";
		final File domainFile = new File(domainFilePath);
		final File problemFile = new File(problemFilePath);
		final File solutionFile = new File(solutionFilePath);
		final Plan plan = plan(domainFile, problemFile);
		if ((solutionFile != null) && (plan != null)) {
			writePlanToFile(plan, solutionFile);
		}
	}

	public static void testDepotsNewHeuristic() throws IOException {
		final String domainFilePath = "../Examples/IPC3/Tests1/Depots/Strips/Depots.pddl";
		final String problemFilePathPrefix = "../Examples/IPC3/Tests1/Depots/Strips/pfile";
		final String solutionFilePathPrefix = "../Examples/IPC3/Tests1/Depots/Strips/mysearch/pfileSolution_MySearch";
		final String dirResult = "../Test/result/10";
		final String dirFeatures = "../Test/result/10/features";
		File domainFile = null;
		File problemFile = null;
		File solutionFile = null;
		final ParameterReader parameterReader = new ParameterReader();
		parameterReader.setDirFeatures(dirFeatures);
		parameterReader.setDirResult(dirResult);
		parameterReader.setNumberHiddenLayers(1);
		parameterReader.setNumberOutput(1);
		parameterReader.setUseHeuristicUnitHidden(true);
		parameterReader.setDomainFilePath(domainFilePath);
		parameterReader.setProblemFilePath(problemFilePathPrefix + "1");
		final ParameterSearch parameterSearch = ReaderParameterSearch
				.read(parameterReader);
		for (int index = 1; index < 22; index++) {
			domainFile = new File(domainFilePath);
			problemFile = new File(problemFilePathPrefix + index);
			solutionFile = new File(solutionFilePathPrefix + index + ".pddl");
			final Plan plan = plan(domainFile, problemFile, parameterSearch);
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
