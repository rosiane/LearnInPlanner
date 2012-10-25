package search.util;

import java.util.Comparator;

import planner.util.LearnInPlannerState;

public class HeuristicValueComparator implements
		Comparator<LearnInPlannerState> {
	public int compare(LearnInPlannerState obj1, LearnInPlannerState obj2) {
		long d1 = obj1.getHValue().longValue();
		long d2 = obj2.getHValue().longValue();
		if (d1 < d2) {
			return -1;
		}
		if (d1 > d2) {
			return 1;
		}
		return 0;
	}

}
