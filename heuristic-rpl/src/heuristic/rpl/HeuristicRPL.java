package heuristic.rpl;

import javaff.data.TotalOrderPlan;
import javaff.planning.TemporalMetricState;

public class HeuristicRPL {
	public static Solution calculate(TemporalMetricState initialState) {
		Solution solution = new Solution();
		solution.setValue(initialState.getHValue());
		TotalOrderPlan top = (TotalOrderPlan) initialState.getRPG().getPlan(
				initialState);
		solution.setRelaxedPlan(top);
		return solution;
	}

}
