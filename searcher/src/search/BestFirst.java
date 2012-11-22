package search;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javaff.planning.Filter;
import javaff.planning.State;
import planner.util.LearnInPlannerState;

public class BestFirst/* extends BestFirstSearch */{
	private Hashtable<Integer, LearnInPlannerState> closed;
	private TreeSet<LearnInPlannerState> open;
	private Filter filter = null;

	private LearnInPlannerState start;
	protected int nodeCount = 0;
	private Comparator<LearnInPlannerState> comp;

	public Comparator<LearnInPlannerState> getComparator() {
		return comp;
	}

	public void setComparator(Comparator<LearnInPlannerState> comp) {
		this.comp = comp;
	}

	public BestFirst(LearnInPlannerState start,
			Comparator<LearnInPlannerState> comp) {
		this.start = start;
		setComparator(comp);
		closed = new Hashtable<Integer, LearnInPlannerState>();
		open = new TreeSet<LearnInPlannerState>(comp);
	}

	public void setFilter(Filter f) {
		filter = f;
	}

	public void updateOpen(State S) {
		Set<State> states = S.getNextStates(filter.getActions(S));
		Iterator<State> iterator = states.iterator();
		while (iterator.hasNext()) {
			open.add(new LearnInPlannerState(iterator.next()));
		}
	}

	public State removeNext() {
		LearnInPlannerState S = open.first();
		open.remove(S);
		/*
		 * System.out.println("================================");
		 * S.getSolution().print(System.out);
		 * System.out.println("----Helpful Actions-------------");
		 * javaff.planning.TemporalMetricState ms =
		 * (javaff.planning.TemporalMetricState) S;
		 * System.out.println(ms.helpfulActions);
		 * System.out.println("----Relaxed Plan----------------");
		 * ms.RelaxedPlan.print(System.out);
		 */
		return S.getState();
	}

	public boolean needToVisit(State s) {
		Integer Shash = new Integer(s.hashCode());
		LearnInPlannerState D = closed.get(Shash);

		if (closed.containsKey(Shash) && D.equals(s)) {
			return false;
		}

		closed.put(Shash, new LearnInPlannerState(s));
		return true;
	}

	public State search() {

		open.add(start);
		State s = null;
		while (!open.isEmpty()) {
			s = removeNext();
			if (needToVisit(s)) {
				++nodeCount;
				if (s.goalReached()) {
					return s;
				} else {
					updateOpen(s);
				}
			}

		}
		return null;
	}

}
