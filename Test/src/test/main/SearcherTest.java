package test.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Iterator;

import common.preprocessor.file.FileManager;

import javaff.data.Action;
import javaff.data.GroundProblem;
import javaff.data.Plan;
import javaff.data.TotalOrderPlan;
import javaff.data.UngroundProblem;
import javaff.parser.PDDL21parser;
import javaff.planning.HelpfulFilter;
import javaff.planning.NullFilter;
import javaff.planning.State;
import javaff.planning.TemporalMetricStateDelta;
import javaff.search.BestFirstSearch;
import javaff.search.BreadthFirstSearch;
import javaff.search.EnforcedHillClimbingSearch;
import javaff.search.LimitedEnforcedHillClimbingSearch;

public class SearcherTest {
	public static boolean VALIDATE = false;

	public static PrintStream planOutput = System.out;
	public static PrintStream parsingOutput = System.out;
	public static PrintStream infoOutput = System.out;
	public static PrintStream errorOutput = System.err;

	public static void main(String args[]) {
		testDepots();
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

	public static void testDepots() {
		String domainFilePath = "../Examples/IPC3/Tests1/Depots/Strips/Depots.pddl";
		String problemFilePathPrefix = "../Examples/IPC3/Tests1/Depots/Strips/pfile";
		String solutionFilePathPrefix = "../Examples/IPC3/Tests1/Depots/Strips/mysearch/pfileSolution_MySearch";
		File domainFile = null;
		File problemFile = null;
		File solutionFile = null;
		for (int index = 1; index < 22; index++) {
			domainFile = new File(domainFilePath);
			problemFile = new File(problemFilePathPrefix + index);
			solutionFile = new File(solutionFilePathPrefix + index + ".pddl");
			Plan plan = plan(domainFile, problemFile);
			if (solutionFile != null && plan != null) {
				writePlanToFile(plan, solutionFile);
			}
		}
	}

	public static void testDepotsMemory() {
		String domainFilePath = "../Examples/IPC3/Tests1/Depots/Strips/Depots.pddl";
		String problemFilePath = "../Examples/IPC3/Tests1/Depots/Strips/pfile3";
		String solutionFilePath = "../Examples/IPC3/Tests1/Depots/Strips/mysearch/pfileSolution_MySearch3.pddl";
		File domainFile = new File(domainFilePath);
		File problemFile = new File(problemFilePath);
		File solutionFile = new File(solutionFilePath);
		Plan plan = plan(domainFile, problemFile);
		if (solutionFile != null && plan != null) {
			writePlanToFile(plan, solutionFile);
		}
	}

	public static Plan plan(File dFile, File pFile) {
		// ********************************
		// Parse and Ground the Problem
		// ********************************
		long startTime = System.currentTimeMillis();

		UngroundProblem unground = PDDL21parser.parseFiles(dFile, pFile);

		if (unground == null) {
			System.out.println("Parsing error - see console for details");
			return null;
		}

		String fileResult = "../Examples/IPC3/Tests1/Depots/Strips/mysearch/performance";
		try {
			FileManager.write(fileResult, "Domain " + dFile.getAbsolutePath(),
					true);

			FileManager.write(fileResult, "Problem " + pFile.getAbsolutePath(),
					true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// PDDLPrinter.printDomainFile(unground, System.out);
		// PDDLPrinter.printProblemFile(unground, System.out);

		GroundProblem ground = unground.ground();

		long afterGrounding = System.currentTimeMillis();

		// ********************************
		// Search for a plan
		// ********************************

		// Get the initial state
		TemporalMetricStateDelta initialState = ground
				.getTemporalMetricInitialStateDelta();

		// State goalState = performSearchFFModified(initialState);
		State goalState = performSearch(initialState);
		// State goalState = performSearchFFLimited(initialState);
		// State goalState = performSearchBreadthFirstSearch(initialState);

		long afterPlanning = System.currentTimeMillis();

		TotalOrderPlan top = null;
		if (goalState != null) {
			top = (TotalOrderPlan) goalState.getSolution();
			infoOutput.println("Plan Lenght " + top.getPlanLength());
			try {
				FileManager.write(fileResult,
						"Plan Lenght " + top.getPlanLength(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		double groundingTime = (afterGrounding - startTime) / 1000.00;
		double planningTime = (afterPlanning - afterGrounding) / 1000.00;

		infoOutput.println("Instantiation Time =\t\t" + groundingTime + "sec");
		infoOutput.println("Planning Time =\t" + planningTime + "sec");

		try {
			FileManager.write(fileResult, "Instantiation Time =\t\t"
					+ groundingTime + "sec", true);
			FileManager.write(fileResult, "Planning Time =\t" + planningTime
					+ "sec", true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return top;
	}

	private static void writePlanToFile(Plan plan, File fileOut) {
		try {
			FileOutputStream outputStream = new FileOutputStream(fileOut);
			PrintWriter printWriter = new PrintWriter(outputStream);
			plan.print(printWriter);
			printWriter.close();
		} catch (FileNotFoundException e) {
			errorOutput.println(e);
			e.printStackTrace();
		}
	}

	public static State performSearch(TemporalMetricStateDelta initialState) {

		infoOutput.println("Performing search");

		long timeLimit = 30 * 60 * 1000;
		String fileResult = "../Examples/IPC3/Tests1/Depots/Strips/mysearch/performance";

		// Now, initialise an BestFirst searcher
		BestFirstSearch bestFirst = new BestFirstSearch(initialState,
				timeLimit, fileResult);

		// using the 'all actions' neighbourhood (a null filter, as it removes
		// nothing)
		bestFirst.setFilter(NullFilter.getInstance());

		// Try and find a plan using BestFirst
		State goalState = bestFirst.search();

		if (goalState == null) {
			infoOutput.println("BestFirst failed");
			try {
				FileManager.write(fileResult, "BestFirst failed", true);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return goalState; // return the plan
	}

	public static State performSearchFFModified(
			TemporalMetricStateDelta initialState) {
		// Implementation of standard FF-style search

		infoOutput
				.println("Performing search as in FF - first considering EHC with only helpful actions");

		// Now, initialise an EHC searcher
		EnforcedHillClimbingSearch EHCS = new EnforcedHillClimbingSearch(
				initialState);

		EHCS.setFilter(HelpfulFilter.getInstance());

		// Try and find a plan using EHC
		State goalState = EHCS.search();

		if (goalState == null) // if we can't find one
		{
			infoOutput
					.println("EHC failed, using best-first search, with all actions");

			// create a Best-First Searcher
			BestFirstSearch BFS = new BestFirstSearch(initialState,
					Long.MAX_VALUE, null);

			// ... change to using the 'all actions' neighbourhood (a null
			// filter, as it removes nothing)

			BFS.setFilter(NullFilter.getInstance());

			// and use that
			goalState = BFS.search();
		}

		return goalState; // return the plan
	}

	public static State performSearchFFLimited(
			TemporalMetricStateDelta initialState) {
		// Implementation of standard FF-style search

		infoOutput
				.println("Performing search as in FF - first considering EHC with only helpful actions");

		// Now, initialise an EHC searcher
		LimitedEnforcedHillClimbingSearch EHCS = new LimitedEnforcedHillClimbingSearch(
				initialState, new BigDecimal(200));

		EHCS.setFilter(HelpfulFilter.getInstance()); // and use the helpful
														// actions neighbourhood

		// Try and find a plan using EHC
		State goalState = EHCS.search();

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

	public static State performSearchBreadthFirstSearch(
			TemporalMetricStateDelta initialState) {

		infoOutput.println("Performing search breadthFirstSearch");

		// Now, initialise an BestFirst searcher
		// BestFirst bestFirst = new BestFirst(new LearnInPlannerState(
		// initialState), new HeuristicValueComparator());
		BreadthFirstSearch breadthFirstSearch = new BreadthFirstSearch(
				initialState);

		breadthFirstSearch.setFilter(HelpfulFilter.getInstance()); // and use
																	// the
																	// helpful
		// actions neighbourhood

		// Try and find a plan using BestFirst
		State goalState = breadthFirstSearch.search();

		if (goalState == null) {
			infoOutput.println("BreadthFirstSearch failed");

		}
		return goalState; // return the plan
	}
}
