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

package javaff.planning;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javaff.data.Action;
import javaff.data.GroundCondition;
import javaff.data.Metric;
import javaff.data.TotalOrderPlan;
import javaff.data.metric.NamedFunction;
import javaff.data.strips.Proposition;

public class MetricState extends STRIPSState {
	public Map<NamedFunction, BigDecimal> funcValues; // maps Named Functions
														// onto BigDecimals
	public Metric metric;

	protected MetricState() {

	}

	public MetricState(Set<Action> a, Set<Proposition> f, GroundCondition g,
			Map<NamedFunction, BigDecimal> funcs, Metric m) {
		super(a, f, g);
		funcValues = funcs;
		metric = m;
	}

	protected MetricState(Set<Action> a, Set<Proposition> f, GroundCondition g,
			Map<NamedFunction, BigDecimal> funcs, TotalOrderPlan p, Metric m) {
		super(a, f, g, p);
		funcValues = funcs;
		metric = m;
	}

	public Object clone() {
		@SuppressWarnings("unchecked")
		Set<Proposition> nf = (Set<Proposition>) ((HashSet<Proposition>) facts)
				.clone();
		TotalOrderPlan p = (TotalOrderPlan) plan.clone();
		@SuppressWarnings("unchecked")
		Map<NamedFunction, BigDecimal> nfuncs = (Map<NamedFunction, BigDecimal>) ((Hashtable<NamedFunction, BigDecimal>) funcValues)
				.clone();
		MetricState ms = new MetricState(actions, nf, goal, nfuncs, p, metric);
		ms.setRPG(RPG);
		// ms.setFilter(filter);
		return ms;
	}

	public BigDecimal getValue(NamedFunction nf) {
		return funcValues.get(nf);
	}

	public void setValue(NamedFunction nf, BigDecimal d) {
		funcValues.put(nf, d);
	}

	// WARNING - not yet implemented - must be overridden and take account of
	// the metric
	public BigDecimal getHValue() {
		return super.getHValue();
	}

	public BigDecimal getGValue() {
		return super.getGValue();
	}

	public boolean equals(Object obj) {
		if (obj instanceof MetricState) {
			MetricState s = (MetricState) obj;
			return (s.facts.toString().equals(facts.toString()) && s.funcValues.toString().equals(funcValues.toString()));
		} else
			return false;
	}

	public int hashCode() {
		int hash = 8;
		hash = 31 * hash ^ facts.hashCode();
		hash = 31 * hash ^ funcValues.hashCode();
		return hash;
	}

}
