package test.main;

import heuristic.rpl.HeuristicRPL;
import heuristic.rpl.Solution;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javaff.data.GroundProblem;
import javaff.data.UngroundProblem;
import javaff.data.strips.STRIPSInstantAction;
import javaff.parser.PDDL21parser;
import javaff.planning.TemporalMetricState;
import preprocessor.file.PlanExample;

public class HeuristicRPLTest {

	public static void main(String[] args) throws IOException {
		// readSolutionPlanTest();
		heuristicTest();
	}

	public static void readSolutionPlanTest() throws IOException {
		for (String action : PlanExample
				.getPlan("../Examples/IPC3/Tests1/Depots/Strips/training/pfile1Solution.pddl")) {
			System.out.println(action);
		}
	}

	private static void heuristicTest() throws IOException {
		File domainFile = new File(
				"../Examples/IPC3/Tests1/Depots/Strips/Depots.pddl");
		File problemFile = new File(
				"../Examples/IPC3/Tests1/Depots/Strips/pfile22");
		UngroundProblem unground = PDDL21parser.parseFiles(domainFile,
				problemFile);
		if (unground == null) {
			System.out.println("Parsing error - see console for details");
			return;
		}
		GroundProblem ground = unground.ground();
		TemporalMetricState initialState = ground
				.getTemporalMetricInitialState();
		Solution solution = HeuristicRPL.calculate(initialState);
		System.out.println(solution.getValue());
		solution.getRelaxedPlan().print(System.out);
		List<String> plan = PlanExample
				.getPlan("../Examples/IPC3/Tests1/Depots/Strips/training/pfile22Solution.pddl");
		TemporalMetricState atualState = initialState;
		Iterator<STRIPSInstantAction> iterator = null;
		STRIPSInstantAction stripsInstantAction = null;
		String[] action = null;
		boolean found = false;
		for (int index = 0; index < plan.size(); index++) {
			iterator = atualState.getActions().iterator();
			System.out.println("Action: " + plan.get(index));
			action = plan.get(index).split(" ");
			found = false;
			while (iterator.hasNext()) {
				stripsInstantAction = iterator.next();
				if (stripsInstantAction.name.toString().equalsIgnoreCase(
						action[0])) {
					found = true;
					for (int indexActionParam = 1; indexActionParam < action.length; indexActionParam++) {
						if (!stripsInstantAction.params
								.get(indexActionParam - 1).toString()
								.equalsIgnoreCase(action[indexActionParam])) {
							found = false;
							break;
						}
					}
					if (found) {
						atualState = (TemporalMetricState) atualState
								.apply(stripsInstantAction);
						solution = HeuristicRPL.calculate(atualState);
						System.out.println(solution.getValue());
						solution.getRelaxedPlan().print(System.out);
						break;
					}
				}
			}
		}
	}
}
