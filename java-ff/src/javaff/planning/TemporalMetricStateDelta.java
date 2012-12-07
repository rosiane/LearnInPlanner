package javaff.planning;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javaff.data.Action;
import javaff.data.GroundCondition;
import javaff.data.Metric;
import javaff.data.TotalOrderPlan;
import javaff.data.strips.InstantAction;
import javaff.data.temporal.SplitInstantAction;
import javaff.data.temporal.StartInstantAction;
import javaff.scheduling.SchedulabilityChecker;
import javaff.scheduling.VelosoSchedulabilityChecker;

public class TemporalMetricStateDelta extends MetricState {

	public Set openActions; // Set of (DurativeActions)
	public List invariants; // Set of Propositions (Propositions)
	public SchedulabilityChecker checker;

	protected TemporalMetricStateDelta(Set a, Set f, GroundCondition g,
			Map funcs, TotalOrderPlan p, Metric m, Set oAc, List i) {
		super(a, f, g, funcs, p, m);
		openActions = oAc;
		invariants = i;
	}

	public TemporalMetricStateDelta(Set a, Set f, GroundCondition g, Map funcs,
			Metric m) {
		super(a, f, g, funcs, m);
		openActions = new HashSet();
		invariants = new ArrayList();
		checker = new VelosoSchedulabilityChecker();

	}

	public boolean goalReached() {
		return (openActions.isEmpty() && super.goalReached());
	}

	public boolean checkAvailability(Action a) {
		List rList = new ArrayList(invariants);
		if (a instanceof SplitInstantAction) {
			SplitInstantAction da = (SplitInstantAction) a;
			Iterator iit = da.parent.invariant.getConditionalPropositions()
					.iterator();
			while (iit.hasNext()) {
				rList.remove(iit.next());
			}
		}
		rList.retainAll(a.getDeletePropositions());
		if (!rList.isEmpty())
			return false;
		// This should have to be cloned here and below
		SchedulabilityChecker c = (VelosoSchedulabilityChecker) checker.clone();
		boolean result = c.addAction((InstantAction) a, this);
		if (result && a instanceof StartInstantAction) {
			TemporalMetricStateDelta dupli = (TemporalMetricStateDelta) this
					.clone();
			dupli.apply(a);
			result = c.addAction(((StartInstantAction) a).getSibling(), dupli);

		}
		return result;
	}

	public Object clone() {
		Set nf = (Set) ((HashSet) facts).clone();
		TotalOrderPlan p = (TotalOrderPlan) plan.clone();
		Map nfuncs = (Map) ((Hashtable) funcValues).clone();
		Set oA = (Set) ((HashSet) openActions).clone();
		List i = (List) ((ArrayList) invariants).clone();
		Set na = (Set) ((HashSet) actions).clone();
		TemporalMetricStateDelta ts = new TemporalMetricStateDelta(na, nf,
				goal, nfuncs, p, metric, oA, i);
		ts.setRPG(RPG);
		// ts.setFilter(filter);
		ts.checker = (VelosoSchedulabilityChecker) checker.clone();
		return ts;
	}

	public State apply(Action a) // return a cloned copy
	{
		TemporalMetricStateDelta s = (TemporalMetricStateDelta) super.apply(a);
		if (a instanceof SplitInstantAction) {
			SplitInstantAction sia = (SplitInstantAction) a;
			sia.applySplit(s);
		}
		s.checker.addAction((InstantAction) a, s);
		return s;
	}
	
	@Override
	public BigDecimal getHValue() {
		BigDecimal hValue = super.getHValue();
//		System.out.println("Inserir deltaaa");
		// TODO inserir heurística aqui
		return hValue;
	}

}
