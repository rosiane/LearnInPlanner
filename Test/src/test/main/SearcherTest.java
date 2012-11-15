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
import javaff.planning.State;
import javaff.planning.TemporalMetricState;
import planner.util.LearnInPlannerState;
import search.BestFirst;
import search.util.HeuristicValueComparator;

public class SearcherTest {
	public static boolean VALIDATE = false;

	public static PrintStream planOutput = System.out;
	public static PrintStream parsingOutput = System.out;
	public static PrintStream infoOutput = System.out;
	public static PrintStream errorOutput = System.err;

	public static void main(String args[]) {
		File domainFile = new File(
				"../Examples/IPC3/Tests1/Depots/Strips/Depots.pddl");
		File problemFile = new File(
				"../Examples/IPC3/Tests1/Depots/Strips/pfile2");

		File solutionFile = null;
		solutionFile = new File(
				"../Examples/IPC3/Tests1/Depots/Strips/pfile2Solution_MySearch.pddl");

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

		// PDDLPrinter.printDomainFile(unground, System.out);
		// PDDLPrinter.printProblemFile(unground, System.out);

		GroundProblem ground = unground.ground();

		long afterGrounding = System.currentTimeMillis();

		// ********************************
		// Search for a plan
		// ********************************

		// Get the initial state
		TemporalMetricState initialState = ground
				.getTemporalMetricInitialState();

		State goalState = performSearch(initialState);

		long afterPlanning = System.currentTimeMillis();

		TotalOrderPlan top = null;
		if (goalState != null)
			top = (TotalOrderPlan) goalState.getSolution();
		if (top != null)
			top.print(planOutput);

		double groundingTime = (afterGrounding - startTime) / 1000.00;
		double planningTime = (afterPlanning - afterGrounding) / 1000.00;

		infoOutput.println("Instantiation Time =\t\t" + groundingTime + "sec");
		infoOutput.println("Planning Time =\t" + planningTime + "sec");

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

	public static State performSearch(TemporalMetricState initialState) {

		infoOutput.println("Performing search");

		// Now, initialise an BestFirst searcher
		BestFirst bestFirst = new BestFirst(new LearnInPlannerState(
				initialState), new HeuristicValueComparator());

		bestFirst.setFilter(HelpfulFilter.getInstance()); // and use the helpful
		// actions neighbourhood

		// Try and find a plan using BestFirst
		State goalState = bestFirst.search();

		if (goalState == null) {
			infoOutput.println("BestFirst failed");

		}
		return goalState; // return the plan
	}

}
