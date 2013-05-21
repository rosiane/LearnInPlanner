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

package planner.javaff.data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import neural.network.interfaces.NeuralNetworkIF;
import neural.network.util.Weight;
import planner.javaff.data.metric.NamedFunction;
import planner.javaff.data.strips.InstantAction;
import planner.javaff.data.temporal.DurativeAction;
import planner.javaff.planning.MetricState;
import planner.javaff.planning.RelaxedMetricPlanningGraph;
import planner.javaff.planning.RelaxedPlanningGraph;
import planner.javaff.planning.RelaxedTemporalMetricPlanningGraph;
import planner.javaff.planning.STRIPSState;
import planner.javaff.planning.TemporalMetricState;
import planner.javaff.planning.TemporalMetricStateDelta;

import com.syvys.jaRBM.Layers.Layer;
import common.feature.ClassExpression;

public class GroundProblem {
	// public Set facts = new HashSet(); // (Proposition)
	public Set<Action> actions = new HashSet<Action>(); // (GroundAction)
	public Map<NamedFunction, BigDecimal> functionValues = new Hashtable<NamedFunction, BigDecimal>(); // (NamedFunction
																										// =>
																										// BigDecimal)
	public Metric metric;

	public GroundCondition goal;
	public Set initial; // (Proposition)

	public TemporalMetricState state = null;
	public TemporalMetricStateDelta stateDelta = null;
	private final LinkedList<ClassExpression> features;
	private final NeuralNetworkIF neuralNetwork;
	private final Layer[] net;
	private final Weight[] weights;

	public GroundProblem(final Set a, final Set i, final GroundCondition g,
			final Map f, final Metric m,
			final LinkedList<ClassExpression> features,
			final NeuralNetworkIF neuralNetwork, final Layer[] net,
			final Weight[] weights) {
		this.actions = a;
		this.initial = i;
		this.goal = g;
		this.functionValues = f;
		this.metric = m;
		this.features = features;
		this.neuralNetwork = neuralNetwork;
		this.net = net;
		this.weights = weights;
	}

	public MetricState getMetricInitialState() {
		final MetricState ms = new MetricState(this.actions, this.initial,
				this.goal, this.functionValues, this.metric);
		ms.setRPG(new RelaxedMetricPlanningGraph(this));
		return ms;
	}

	public STRIPSState getSTRIPSInitialState() {
		final STRIPSState s = new STRIPSState(this.actions, this.initial,
				this.goal);
		s.setRPG(new RelaxedPlanningGraph(this));
		return s;
	}

	public TemporalMetricState getTemporalMetricInitialState() {
		if (this.state == null) {
			final Set na = new HashSet();
			final Set ni = new HashSet();
			final Iterator ait = this.actions.iterator();
			while (ait.hasNext()) {
				final Action act = (Action) ait.next();
				if (act instanceof InstantAction) {
					na.add(act);
					ni.add(act);
				} else if (act instanceof DurativeAction) {
					final DurativeAction dact = (DurativeAction) act;
					na.add(dact.startAction);
					na.add(dact.endAction);
					ni.add(dact.startAction);
				}
			}
			final TemporalMetricState ts = new TemporalMetricState(ni,
					this.initial, this.goal, this.functionValues, this.metric);
			final GroundProblem gp = new GroundProblem(na, this.initial,
					this.goal, this.functionValues, this.metric, this.features,
					this.neuralNetwork, this.net, this.weights);
			ts.setRPG(new RelaxedTemporalMetricPlanningGraph(gp));
			this.state = ts;
		}
		return this.state;
	}

	public TemporalMetricStateDelta getTemporalMetricInitialStateDelta() {
		if (this.stateDelta == null) {
			final Set na = new HashSet();
			final Set ni = new HashSet();
			final Iterator ait = this.actions.iterator();
			while (ait.hasNext()) {
				final Action act = (Action) ait.next();
				if (act instanceof InstantAction) {
					na.add(act);
					ni.add(act);
				} else if (act instanceof DurativeAction) {
					final DurativeAction dact = (DurativeAction) act;
					na.add(dact.startAction);
					na.add(dact.endAction);
					ni.add(dact.startAction);
				}
			}
			final TemporalMetricStateDelta ts = new TemporalMetricStateDelta(
					ni, this.initial, this.goal, this.functionValues,
					this.metric, this.features, this.neuralNetwork, this.net,
					this.weights);
			final GroundProblem gp = new GroundProblem(na, this.initial,
					this.goal, this.functionValues, this.metric, this.features,
					this.neuralNetwork, this.net, this.weights);
			ts.setRPG(new RelaxedTemporalMetricPlanningGraph(gp));
			this.stateDelta = ts;
		}
		return this.stateDelta;
	}

}
