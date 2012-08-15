package test.main;

import heuristic.rpl.HeuristicRPL;
import heuristic.rpl.Solution;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import javaff.data.Action;
import javaff.data.GroundProblem;
import javaff.data.UngroundProblem;
import javaff.data.strips.Operator;
import javaff.data.strips.OperatorName;
import javaff.data.strips.STRIPSInstantAction;
import javaff.parser.PDDL21parser;
import javaff.planning.TemporalMetricState;

public class HeuristicRPLTest {

	public static void main(String[] args) {
		File domainFile = new File(
				"../Examples/IPC3/Tests1/Depots/Strips/Depots.pddl");
		File problemFile = new File(
				"../Examples/IPC3/Tests1/Depots/Strips/pfile1");
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
		Iterator<STRIPSInstantAction> i = initialState.getActions().iterator();
		while (i.hasNext()) {
			STRIPSInstantAction stripsInstantAction = i.next();
			if (stripsInstantAction.name.toString().equalsIgnoreCase("Lift")) {
				if (stripsInstantAction.params.get(0).toString()
						.equalsIgnoreCase("HOIST0")
						&& stripsInstantAction.params.get(1).toString()
								.equalsIgnoreCase("CRATE1")
						&& stripsInstantAction.params.get(2).toString()
								.equalsIgnoreCase("PALLET0")
						&& stripsInstantAction.params.get(3).toString()
								.equalsIgnoreCase("DEPOT0")) {
					TemporalMetricState next = (TemporalMetricState) initialState
							.apply(stripsInstantAction);
					solution = HeuristicRPL.calculate(next);
					System.out.println(solution.getValue());
					solution.getRelaxedPlan().print(System.out);
				}
			}
		}
	}
}
