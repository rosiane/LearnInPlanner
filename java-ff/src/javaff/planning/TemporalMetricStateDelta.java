package javaff.planning;

import heuristic.rpl.HeuristicRPL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
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
import neural.network.interfaces.NeuralNetworkIF;
import neural.network.util.Weight;

import com.syvys.jaRBM.Layers.Layer;
import common.ClassExpression;

public class TemporalMetricStateDelta extends MetricState {

	public Set openActions; // Set of (DurativeActions)
	public List invariants; // Set of Propositions (Propositions)
	public SchedulabilityChecker checker;
	private LinkedList<ClassExpression> features;
	private final NeuralNetworkIF neuralNetwork;
	private final Layer[] net;
	private final Weight[] weights;

	public TemporalMetricStateDelta(final Set a, final Set f,
			final GroundCondition g, final Map funcs, final Metric m,
			final LinkedList<ClassExpression> features,
			final NeuralNetworkIF neuralNetwork, final Layer[] net,
			final Weight[] weights) {
		super(a, f, g, funcs, m);
		this.openActions = new HashSet();
		this.invariants = new ArrayList();
		this.checker = new VelosoSchedulabilityChecker();
		this.features = features;
		this.neuralNetwork = neuralNetwork;
		this.net = net;
		this.weights = weights;
	}

	protected TemporalMetricStateDelta(final Set a, final Set f,
			final GroundCondition g, final Map funcs, final TotalOrderPlan p,
			final Metric m, final Set oAc, final List i,
			final LinkedList<ClassExpression> features,
			final NeuralNetworkIF neuralNetwork, final Layer[] net,
			final Weight[] weights) {
		super(a, f, g, funcs, p, m);
		this.openActions = oAc;
		this.invariants = i;
		this.features = features;
		this.neuralNetwork = neuralNetwork;
		this.net = net;
		this.weights = weights;
	}

	@Override
	public State apply(final Action a) // return a cloned copy
	{
		final TemporalMetricStateDelta s = (TemporalMetricStateDelta) super
				.apply(a);
		if (a instanceof SplitInstantAction) {
			final SplitInstantAction sia = (SplitInstantAction) a;
			sia.applySplit(s);
		}
		s.checker.addAction((InstantAction) a, s);
		return s;
	}

	@Override
	public boolean checkAvailability(final Action a) {
		final List rList = new ArrayList(this.invariants);
		if (a instanceof SplitInstantAction) {
			final SplitInstantAction da = (SplitInstantAction) a;
			final Iterator iit = da.parent.invariant
					.getConditionalPropositions().iterator();
			while (iit.hasNext()) {
				rList.remove(iit.next());
			}
		}
		rList.retainAll(a.getDeletePropositions());
		if (!rList.isEmpty()) {
			return false;
		}
		// This should have to be cloned here and below
		final SchedulabilityChecker c = (VelosoSchedulabilityChecker) this.checker
				.clone();
		boolean result = c.addAction((InstantAction) a, this);
		if (result && (a instanceof StartInstantAction)) {
			final TemporalMetricStateDelta dupli = (TemporalMetricStateDelta) this
					.clone();
			dupli.apply(a);
			result = c.addAction(((StartInstantAction) a).getSibling(), dupli);

		}
		return result;
	}

	@Override
	public Object clone() {
		final Set nf = (Set) ((HashSet) this.facts).clone();
		final TotalOrderPlan p = (TotalOrderPlan) this.plan.clone();
		final Map nfuncs = (Map) ((Hashtable) this.funcValues).clone();
		final Set oA = (Set) ((HashSet) this.openActions).clone();
		final List i = (List) ((ArrayList) this.invariants).clone();
		final Set na = (Set) ((HashSet) this.actions).clone();
		final TemporalMetricStateDelta ts = new TemporalMetricStateDelta(na,
				nf, this.goal, nfuncs, p, this.metric, oA, i, this.features,
				this.neuralNetwork, this.net, this.weights);
		ts.setRPG(this.RPG);
		// ts.setFilter(filter);
		ts.checker = (VelosoSchedulabilityChecker) this.checker.clone();
		return ts;
	}

	public LinkedList<ClassExpression> getFeatures() {
		return this.features;
	}

	@Override
	public BigDecimal getHValue() {
		BigDecimal hValue = super.getHValue();
		if ((this.features != null) && !this.features.isEmpty()) {
			final LinkedList<String> database = HeuristicRPL
					.generateDatabase(this);
			final double[] data = new double[this.features.size()];
			for (int indexFeatures = 0; indexFeatures < this.features.size(); indexFeatures++) {
				data[indexFeatures] = this.features.get(indexFeatures)
						.cardinality(database);
			}
			final double[] delta = this.neuralNetwork.run(this.net,
					this.weights, data);
			final double hValueNew = hValue.doubleValue() + delta[0];
			hValue = new BigDecimal(hValueNew);
		}
		return hValue;
	}

	@Override
	public boolean goalReached() {
		return (this.openActions.isEmpty() && super.goalReached());
	}

	public void setFeatures(final LinkedList<ClassExpression> features) {
		this.features = features;
	}

	@Override
	public String toString() {
		return this.facts.toString() + "@" + this.funcValues.toString();
	}
}
