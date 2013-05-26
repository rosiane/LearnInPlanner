/************************************************************************
 * Strathclyde Planning Group,
 * Department of Computer and Information Sciences,
 * University of Strathclyde, Glasgow, UK
 * http://planning.cis.strath.ac.uk/
 * 
 * Copyright 2007, Keith Halsey
 * Copyright 2008, Andrew Coles and Amanda Smith
 *
 * (Questions/bug reports now to be sent to Andrew Coles)
 *
 * This file is part of JavaFF.
 * 
 * JavaFF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * JavaFF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with JavaFF.  If not, see <http://www.gnu.org/licenses/>.
 * 
 ************************************************************************/

package planner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Random;

import planner.javaff.data.GroundProblem;
import planner.javaff.data.Plan;
import planner.javaff.data.TotalOrderPlan;
import planner.javaff.data.UngroundProblem;
import planner.javaff.parser.PDDL21parser;
import planner.javaff.planning.HelpfulFilter;
import planner.javaff.planning.NullFilter;
import planner.javaff.planning.State;
import planner.javaff.planning.TemporalMetricState;
import planner.javaff.planning.TemporalMetricStateDelta;
import planner.javaff.search.BestFirstSearch;
import planner.javaff.search.EnforcedHillClimbingSearch;

public class Planner {
	public static BigDecimal EPSILON = new BigDecimal(0.01);
	public static BigDecimal MAX_DURATION = new BigDecimal("100000"); // maximum
																		// duration
																		// in a
																		// duration
																		// constraint
	public static boolean VALIDATE = false;

	public static Random generator = null;

	public static PrintStream planOutput = System.out;
	public static PrintStream parsingOutput = System.out;
	public static PrintStream infoOutput = System.out;
	public static PrintStream errorOutput = System.err;

	// public static void main (String args[]) {
	// EPSILON = EPSILON.setScale(2,BigDecimal.ROUND_HALF_EVEN);
	// MAX_DURATION = MAX_DURATION.setScale(2,BigDecimal.ROUND_HALF_EVEN);
	//
	// generator = new Random();
	//
	// if (args.length < 2) {
	// System.out.println("Parameters needed: domainFile.pddl problemFile.pddl [random seed] [outputfile.sol");
	//
	// } else {
	// File domainFile = new File(args[0]);
	// File problemFile = new File(args[1]);
	// File solutionFile = null;
	// if (args.length > 2)
	// {
	// generator = new Random(Integer.parseInt(args[2]));
	// }
	//
	// if (args.length > 3)
	// {
	// solutionFile = new File(args[3]);
	// }
	//
	// Plan plan = plan(domainFile,problemFile);
	//
	// if (solutionFile != null && plan != null) writePlanToFile(plan,
	// solutionFile);
	//
	// }
	// }

	public static void main(final String args[]) {
		EPSILON = EPSILON.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		MAX_DURATION = MAX_DURATION.setScale(2, BigDecimal.ROUND_HALF_EVEN);

		generator = new Random();

		// if (args.length < 2) {
		// System.out.println("Parameters needed: domainFile.pddl problemFile.pddl [random seed] [outputfile.sol");
		//
		// } else {
		final File domainFile = new File(
				"../Examples/IPC3/Tests1/Depots/Strips/Depots.pddl");
		final File problemFile = new File(
				"../Examples/IPC3/Tests1/Depots/Strips/pfile3");

		File solutionFile = null;
		// if (args.length > 2) {
		// generator = new Random(Integer.parseInt(args[2]));
		// }

		// if (args.length > 3) {
		solutionFile = new File(
		// "../Examples/IPC3/Tests1/Depots/Strips/pfile3Solution.pddl");
				"../Examples/IPC3/Tests1/Depots/Strips/pfile3SolutionDelta.pddl");
		// }

		final Plan plan = plan(domainFile, problemFile);
		// Plan plan = planDelta(domainFile, problemFile);

		if ((solutionFile != null) && (plan != null)) {
			writePlanToFile(plan, solutionFile);
		}

		// }
	}

	public static State performFFSearch(final TemporalMetricState initialState) {

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

	public static State performFFSearchDelta(
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

	public static void performHValue(final TemporalMetricState initialState,
			final State goalState) {
		System.out.println(initialState.getHValue());

		final TotalOrderPlan top = (TotalOrderPlan) initialState.getRPG()
				.getPlan(initialState);
		top.print(planOutput);

		// Iterator<State> iterator = initialState.getNextStates(
		// HelpfulFilter.getInstance().getActions(initialState))
		// .iterator();

		// while (iterator.hasNext()) {
		// TemporalMetricState succ = (TemporalMetricState) iterator.next();
		// System.out.println(succ.getHValue());
		// top = (TotalOrderPlan) succ.getRPG().getPlan(succ);
		// top.print(planOutput);
		// }
		//
		// System.out.println("######################");
		// Iterator<Action> plan =
		// goalState.getSolution().getActions().iterator();
		// TemporalMetricState succ = initialState;
		// // while (plan.hasNext()) {
		// succ = (TemporalMetricState) succ.apply(plan.next());
		// System.out.println(succ.getHValue());
		// top = (TotalOrderPlan) succ.getRPG().getPlan(succ);
		// top.print(planOutput);
		// }
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

		final GroundProblem ground = unground.ground(null);

		final long afterGrounding = System.currentTimeMillis();

		// ********************************
		// Search for a plan
		// ********************************

		// Get the initial state
		final TemporalMetricState initialState = ground
				.getTemporalMetricInitialState();

		final State goalState = performFFSearch(initialState);
		// State goalState = null;

		final long afterPlanning = System.currentTimeMillis();

		TotalOrderPlan top = null;
		if (goalState != null) {
			top = (TotalOrderPlan) goalState.getSolution();
		}
		if (top != null) {
			top.print(planOutput);
		}

		/*
		 * javaff.planning.PlanningGraph pg = initialState.getRPG(); Plan plan =
		 * pg.getPlan(initialState); plan.print(planOutput); return null;
		 */

		// ********************************
		// Schedule a plan
		// ********************************

		// TimeStampedPlan tsp = null;

		// if (goalState != null)
		// {

		// infoOutput.println("Scheduling");

		// Scheduler scheduler = new JavaFFScheduler(ground);
		// tsp = scheduler.schedule(top);
		// }

		// long afterScheduling = System.currentTimeMillis();

		// if (tsp != null) tsp.print(planOutput);

		final double groundingTime = (afterGrounding - startTime) / 1000.00;
		final double planningTime = (afterPlanning - afterGrounding) / 1000.00;
		// double schedulingTime = (afterScheduling - afterPlanning)/1000.00;

		infoOutput.println("Instantiation Time =\t\t" + groundingTime + "sec");
		infoOutput.println("Planning Time =\t" + planningTime + "sec");
		// infoOutput.println("Scheduling Time =\t"+schedulingTime+"sec");

		// performHValue(initialState, goalState);

		return top;
	}

	public static Plan planDelta(final File dFile, final File pFile) {
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

		final GroundProblem ground = unground.ground(null);

		final long afterGrounding = System.currentTimeMillis();

		// ********************************
		// Search for a plan
		// ********************************

		// Get the initial state
		final TemporalMetricStateDelta initialState = ground
				.getTemporalMetricInitialStateDelta();

		final State goalState = performFFSearchDelta(initialState);
		// State goalState = null;

		final long afterPlanning = System.currentTimeMillis();

		TotalOrderPlan top = null;
		if (goalState != null) {
			top = (TotalOrderPlan) goalState.getSolution();
		}
		if (top != null) {
			top.print(planOutput);
		}

		/*
		 * javaff.planning.PlanningGraph pg = initialState.getRPG(); Plan plan =
		 * pg.getPlan(initialState); plan.print(planOutput); return null;
		 */

		// ********************************
		// Schedule a plan
		// ********************************

		// TimeStampedPlan tsp = null;

		// if (goalState != null)
		// {

		// infoOutput.println("Scheduling");

		// Scheduler scheduler = new JavaFFScheduler(ground);
		// tsp = scheduler.schedule(top);
		// }

		// long afterScheduling = System.currentTimeMillis();

		// if (tsp != null) tsp.print(planOutput);

		final double groundingTime = (afterGrounding - startTime) / 1000.00;
		final double planningTime = (afterPlanning - afterGrounding) / 1000.00;
		// double schedulingTime = (afterScheduling - afterPlanning)/1000.00;

		infoOutput.println("Instantiation Time =\t\t" + groundingTime + "sec");
		infoOutput.println("Planning Time =\t" + planningTime + "sec");
		// infoOutput.println("Scheduling Time =\t"+schedulingTime+"sec");

		// performHValue(initialState, goalState);

		return top;
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