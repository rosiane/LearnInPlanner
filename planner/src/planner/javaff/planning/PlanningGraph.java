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

package planner.javaff.planning;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import planner.javaff.data.Action;
import planner.javaff.data.GroundCondition;
import planner.javaff.data.GroundProblem;
import planner.javaff.data.Plan;
import planner.javaff.data.TotalOrderPlan;
import planner.javaff.data.metric.BinaryComparator;
import planner.javaff.data.metric.ResourceOperator;
import planner.javaff.data.strips.Proposition;


public class PlanningGraph {
	// ******************************************************
	// Data Structures
	// ******************************************************
	Map<Proposition, PGProposition> propositionMap = new Hashtable<Proposition, PGProposition>(); // (Proposition
																									// =>
																									// PGProposition)
	Map<Action, PGAction> actionMap = new Hashtable<Action, PGAction>(); // (Action
																			// =>
																			// PGAction)

	Set<PGProposition> propositions = new HashSet<PGProposition>();
	Set<PGAction> actions = new HashSet<PGAction>();

	Set<PGProposition> initial, goal;
	Set<MutexPair> propMutexes, actionMutexes;
	List<Set<Set<PGProposition>>> memorised;

	protected Set<PGAction> readyActions = null; // PGActions that have all
													// their propositions met,
													// but not their
													// PGBinaryComparators or
													// preconditions are mutex

	boolean level_off = false;
	static int NUMERIC_LIMIT = 4;
	int numeric_level_off = 0;
	int num_layers;

	// ******************************************************
	// Main methods
	// ******************************************************
	protected PlanningGraph() {

	}

	public PlanningGraph(GroundProblem gp) {
		setActionMap(gp.actions);
		setLinks();
		createNoOps();
		setGoal(gp.goal);
	}

	public Plan getPlan(State s) {
		setInitial(s);
		resetAll(s);

		// set up the intital set of facts
		Set<PGProposition> scheduledFacts = new HashSet<PGProposition>(initial);
		List<PGAction> scheduledActs = null;

		scheduledActs = createFactLayer(scheduledFacts, 0);
		List<PGAction> plan = null;

		// create the graph==========================================
		while (true) {
			scheduledFacts = createActionLayer(scheduledActs, num_layers);
			++num_layers;
			scheduledActs = createFactLayer(scheduledFacts, num_layers);

			if (goalMet() && !goalMutex()) {
				plan = extractPlan();
			}
			if (plan != null)
				break;
			if (!level_off)
				numeric_level_off = 0;
			if (level_off || numeric_level_off >= NUMERIC_LIMIT) {
				// printGraph();
				break;
			}
		}

		if (plan != null) {
			Iterator<PGAction> pit = plan.iterator();
			TotalOrderPlan p = new TotalOrderPlan();
			PGAction a = null;
			while (pit.hasNext()) {
				a = pit.next();
				if (!(a instanceof PGNoOp))
					p.addAction(a.action);
			}
			// p.print(javaff.JavaFF.infoOutput);
			return p;
		} else {
			return null;
		}
	}

	// ******************************************************
	// Setting it all up
	// ******************************************************
	protected void setActionMap(Set<Action> gactions) {
		Iterator<Action> ait = gactions.iterator();
		Action a = null;
		PGAction pga = null;
		while (ait.hasNext()) {
			a = ait.next();
			pga = new PGAction(a);
			actionMap.put(a, pga);
			actions.add(pga);
		}
	}

	protected PGProposition getProposition(Proposition p) {
		Object o = propositionMap.get(p);
		PGProposition pgp;
		if (o == null) {
			pgp = new PGProposition(p);
			propositionMap.put(p, pgp);
			propositions.add(pgp);
		} else
			pgp = (PGProposition) o;
		return pgp;
	}

	protected void setLinks() {
		Iterator<PGAction> ait = actions.iterator();
		PGAction pga = null;
		Iterator<Proposition> csit = null;
		Proposition p = null;
		PGProposition pgp = null;
		Iterator<Proposition> alit = null;
		Iterator<Proposition> dlit = null;
		while (ait.hasNext()) {
			pga = ait.next();
			csit = pga.action.getConditionalPropositions().iterator();
			while (csit.hasNext()) {
				p = csit.next();
				pgp = getProposition(p);
				pga.conditions.add(pgp);
				pgp.achieves.add(pga);
			}

			alit = pga.action.getAddPropositions().iterator();
			while (alit.hasNext()) {
				p = alit.next();
				pgp = getProposition(p);
				pga.achieves.add(pgp);
				pgp.achievedBy.add(pga);
			}

			dlit = pga.action.getDeletePropositions().iterator();
			while (dlit.hasNext()) {
				p = dlit.next();
				pgp = getProposition(p);
				pga.deletes.add(pgp);
				pgp.deletedBy.add(pga);
			}
		}
	}

	protected void resetAll(State s) {
		propMutexes = new HashSet<MutexPair>();
		actionMutexes = new HashSet<MutexPair>();

		memorised = new ArrayList<Set<Set<PGProposition>>>();

		readyActions = new HashSet<PGAction>();

		num_layers = 0;

		Iterator<PGAction> ait = actions.iterator();
		PGAction a = null;
		while (ait.hasNext()) {
			a = ait.next();
			a.reset();
		}

		Iterator<PGProposition> pit = propositions.iterator();
		PGProposition p = null;
		while (pit.hasNext()) {
			p = pit.next();
			p.reset();
		}
	}

	protected void setGoal(GroundCondition g) {
		goal = new HashSet<PGProposition>();
		Iterator<Proposition> csit = g.getConditionalPropositions().iterator();
		Proposition p = null;
		PGProposition pgp = null;
		while (csit.hasNext()) {
			p = csit.next();
			pgp = getProposition(p);
			goal.add(pgp);
		}
	}

	protected void setInitial(State S) {
		Set<Proposition> i = ((STRIPSState) S).facts;
		initial = new HashSet<PGProposition>();
		Iterator<Proposition> csit = i.iterator();
		Proposition p = null;
		PGProposition pgp = null;
		while (csit.hasNext()) {
			p = csit.next();
			pgp = getProposition(p);
			initial.add(pgp);
		}
	}

	protected void createNoOps() {
		Iterator<PGProposition> pit = propositions.iterator();
		PGProposition p = null;
		PGNoOp n = null;
		while (pit.hasNext()) {
			p = pit.next();
			n = new PGNoOp(p);
			n.conditions.add(p);
			n.achieves.add(p);
			p.achieves.add(n);
			p.achievedBy.add(n);
			actions.add(n);
		}
	}

	// ******************************************************
	// Graph Construction
	// ******************************************************

	protected ArrayList<PGAction> createFactLayer(Set<PGProposition> pFacts,
			int pLayer) {
		memorised.add(new HashSet<Set<PGProposition>>());
		ArrayList<PGAction> scheduledActs = new ArrayList<PGAction>();
		HashSet<MutexPair> newMutexes = new HashSet<MutexPair>();
		Iterator<PGProposition> fit = pFacts.iterator();
		PGProposition f = null;
		Iterator<PGProposition> pit = null;
		PGProposition p = null;
		while (fit.hasNext()) {
			f = fit.next();
			if (f.layer < 0) {
				f.layer = pLayer;
				scheduledActs.addAll(f.achieves);
				level_off = false;

				// calculate mutexes
				if (pLayer != 0) {
					pit = propositions.iterator();
					while (pit.hasNext()) {
						p = pit.next();
						if (p.layer >= 0 && checkPropMutex(f, p, pLayer)) {
							makeMutex(f, p, pLayer, newMutexes);
						}
					}
				}

			}
		}

		// check old mutexes
		Iterator<MutexPair> pmit = propMutexes.iterator();
		MutexPair m = null;
		while (pmit.hasNext()) {
			m = pmit.next();
			if (checkPropMutex(m, pLayer)) {
				makeMutex(m.node1, m.node2, pLayer, newMutexes);
			} else {
				level_off = false;
			}
		}

		// add new mutexes to old mutexes and remove those which have
		// disappeared
		propMutexes = newMutexes;

		return scheduledActs;
	}

	protected boolean checkPropMutex(MutexPair m, int l) {
		return checkPropMutex((PGProposition) m.node1, (PGProposition) m.node2,
				l);
	}

	protected boolean checkPropMutex(PGProposition p1, PGProposition p2, int l) {
		if (p1 == p2) {
			return false;
		}

		// Componsate for statics
		if (p1.achievedBy.isEmpty() || p2.achievedBy.isEmpty()) {
			return false;
		}

		Iterator<PGAction> a1it = p1.achievedBy.iterator();
		Iterator<PGAction> a2it = null;
		PGAction a1 = null;
		PGAction a2 = null;
		while (a1it.hasNext()) {
			a1 = a1it.next();
			if (a1.layer >= 0) {
				a2it = p2.achievedBy.iterator();
				while (a2it.hasNext()) {
					a2 = a2it.next();
					if (a2.layer >= 0 && !a1.mutexWith(a2, l - 1)) {
						return false;
					}
				}
			}

		}
		return true;
	}

	protected void makeMutex(Node n1, Node n2, int l, Set<MutexPair> mutexPairs) {
		n1.setMutex(n2, l);
		n2.setMutex(n1, l);
		mutexPairs.add(new MutexPair(n1, n2));
	}

	protected HashSet<PGProposition> createActionLayer(List<PGAction> pActions,
			int pLayer) {
		level_off = true;
		HashSet<PGAction> actionSet = getAvailableActions(pActions, pLayer);
		actionSet.addAll(readyActions);
		readyActions = new HashSet<PGAction>();
		HashSet<PGAction> filteredSet = filterSet(actionSet, pLayer);
		HashSet<PGProposition> scheduledFacts = calculateActionMutexesAndProps(
				filteredSet, pLayer);
		return scheduledFacts;
	}

	protected HashSet<PGAction> getAvailableActions(List<PGAction> pActions,
			int pLayer) {
		HashSet<PGAction> actionSet = new HashSet<PGAction>();
		Iterator<PGAction> ait = pActions.iterator();
		PGAction a = null;
		while (ait.hasNext()) {
			a = ait.next();
			if (a.layer < 0) {
				a.counter++;
				a.difficulty += pLayer;
				if (a.counter >= a.conditions.size()) {
					actionSet.add(a);
					level_off = false;
				}
			}
		}
		return actionSet;
	}

	protected HashSet<PGAction> filterSet(Set<PGAction> pActions, int pLayer) {
		HashSet<PGAction> filteredSet = new HashSet<PGAction>();
		Iterator<PGAction> ait = pActions.iterator();
		PGAction a = null;
		while (ait.hasNext()) {
			a = ait.next();
			if (noMutexes(a.conditions, pLayer)) {
				filteredSet.add(a);
			} else {
				readyActions.add(a);
			}
		}
		return filteredSet;
	}

	protected HashSet<PGProposition> calculateActionMutexesAndProps(
			Set<PGAction> filteredSet, int pLayer) {
		HashSet<MutexPair> newMutexes = new HashSet<MutexPair>();

		HashSet<PGProposition> scheduledFacts = new HashSet<PGProposition>();

		Iterator<PGAction> ait = filteredSet.iterator();
		PGAction a = null;
		Iterator<PGAction> a2it = null;
		PGAction a2 = null;
		while (ait.hasNext()) {
			a = ait.next();
			scheduledFacts.addAll(a.achieves);
			a.layer = pLayer;
			level_off = false;

			// caculate new mutexes
			a2it = actions.iterator();
			while (a2it.hasNext()) {
				a2 = a2it.next();
				if (a2.layer >= 0 && checkActionMutex(a, a2, pLayer)) {
					makeMutex(a, a2, pLayer, newMutexes);
				}
			}
		}

		// check old mutexes
		Iterator<MutexPair> amit = actionMutexes.iterator();
		MutexPair m = null;
		while (amit.hasNext()) {
			m = amit.next();
			if (checkActionMutex(m, pLayer)) {
				makeMutex(m.node1, m.node2, pLayer, newMutexes);
			} else {
				level_off = false;
			}
		}

		// add new mutexes to old mutexes and remove those which have
		// disappeared
		actionMutexes = newMutexes;
		return scheduledFacts;
	}

	protected boolean checkActionMutex(MutexPair m, int l) {
		return checkActionMutex((PGAction) m.node1, (PGAction) m.node2, l);
	}

	protected boolean checkActionMutex(PGAction a1, PGAction a2, int l) {
		if (a1 == a2) {
			return false;
		}

		Iterator<PGProposition> p1it = a1.deletes.iterator();
		PGProposition p1 = null;
		while (p1it.hasNext()) {
			p1 = p1it.next();
			if (a2.achieves.contains(p1)) {
				return true;
			}
			if (a2.conditions.contains(p1)) {
				return true;
			}
		}

		Iterator<PGProposition> p2it = a2.deletes.iterator();
		PGProposition p2 = null;
		while (p2it.hasNext()) {
			p2 = p2it.next();
			if (a1.achieves.contains(p2)) {
				return true;
			}
			if (a1.conditions.contains(p2)) {
				return true;
			}
		}

		Iterator<PGProposition> pc1it = a1.conditions.iterator();
		Iterator<PGProposition> pc2it = null;
		while (pc1it.hasNext()) {
			p1 = pc1it.next();
			pc2it = a2.conditions.iterator();
			while (pc2it.hasNext()) {
				p2 = pc2it.next();
				if (p1.mutexWith(p2, l)) {
					return true;
				}
			}
		}
		return false;
	}

	protected boolean goalMet() {
		PGProposition p = null;
		Iterator<PGProposition> git = goal.iterator();
		while (git.hasNext()) {
			p = git.next();
			if (p.layer < 0) {
				return false;
			}
		}
		return true;
	}

	protected boolean goalMutex() {
		return !noMutexes(goal, num_layers);
	}

	protected boolean noMutexes(Set<PGProposition> s, int l) {
		Iterator<PGProposition> sit = s.iterator();
		if (sit.hasNext()) {
			Node n = (Node) sit.next();
			HashSet<PGProposition> s2 = new HashSet<PGProposition>(s);
			s2.remove(n);
			Iterator<PGProposition> s2it = s2.iterator();
			Node n2 = null;
			while (s2it.hasNext()) {
				n2 = s2it.next();
				if (n.mutexWith(n2, l))
					return false;
			}
			return noMutexes(s2, l);
		} else {
			return true;
		}
	}

	/**
	 * Tests to see if there is a mutex between n and all nodes in s
	 * 
	 * @param n
	 * @param s
	 * @param l
	 * @return
	 */
	protected boolean noMutexesTest(Node n, Set<PGAction> s, int l) {
		Iterator<PGAction> sit = s.iterator();
		Node n2 = null;
		while (sit.hasNext()) {
			n2 = sit.next();
			if (n.mutexWith(n2, l)) {
				return false;
			}
		}
		return true;
	}

	// ******************************************************
	// Plan Extraction
	// ******************************************************

	public List<PGAction> extractPlan() {
		return searchPlan(goal, num_layers);
	}

	public List<PGAction> searchPlan(Set<PGProposition> goalSet, int l) {

		if (l == 0) {
			if (initial.containsAll(goalSet))
				return new ArrayList<PGAction>();
			else
				return null;
		}
		// do memorisation stuff
		Set<Set<PGProposition>> badGoalSet = memorised.get(l);
		if (badGoalSet.contains(goalSet)) {
			return null;
		}

		List<Set<PGAction>> ass = searchLevel(goalSet, (l - 1)); // returns a
																	// set of
																	// sets of
		// possible action
		// combinations
		Iterator<Set<PGAction>> assit = ass.iterator();
		Set<PGAction> as = null;
		Set<PGProposition> newgoal = null;
		Iterator<PGAction> ait = null;
		PGAction a = null;
		List<PGAction> al = null;
		List<PGAction> plan = null;

		while (assit.hasNext()) {
			as = assit.next();
			newgoal = new HashSet<PGProposition>();

			ait = as.iterator();
			while (ait.hasNext()) {
				a = ait.next();
				newgoal.addAll(a.conditions);
			}

			al = searchPlan(newgoal, (l - 1));
			if (al != null) {
				plan = new ArrayList<PGAction>(al);
				plan.addAll(as);
				return plan;
			}

		}

		// do more memorisation stuff
		badGoalSet.add(goalSet);
		return null;

	}

	public List<Set<PGAction>> searchLevel(Set<PGProposition> goalSet, int layer) {
		if (goalSet.isEmpty()) {
			Set<PGAction> s = new HashSet<PGAction>();
			List<Set<PGAction>> li = new ArrayList<Set<PGAction>>();
			li.add(s);
			return li;
		}

		List<Set<PGAction>> actionSetList = new ArrayList<Set<PGAction>>();
		Set<PGProposition> newGoalSet = new HashSet<PGProposition>(goalSet);

		Iterator<PGProposition> git = goalSet.iterator();
		PGProposition g = git.next();
		newGoalSet.remove(g);

		Iterator<PGAction> ait = g.achievedBy.iterator();
		PGAction a = null;
		List<Set<PGAction>> l = null;
		Iterator<Set<PGAction>> lit = null;
		Set<PGAction> s = null;
		while (ait.hasNext()) {
			a = ait.next();
			if ((a instanceof PGNoOp) && a.layer <= layer && a.layer >= 0) {
				Set<PGProposition> newnewGoalSet = new HashSet<PGProposition>(
						newGoalSet);
				newnewGoalSet.removeAll(a.achieves);
				l = searchLevel(newnewGoalSet, layer);
				lit = l.iterator();
				while (lit.hasNext()) {
					s = lit.next();
					if (noMutexesTest(a, s, layer)) {
						s.add(a);
						actionSetList.add(s);
					}
				}
			}
		}

		ait = g.achievedBy.iterator();
		Set<PGProposition> newnewGoalSet = null;
		while (ait.hasNext()) {
			a = ait.next();
			if (!(a instanceof PGNoOp) && a.layer <= layer && a.layer >= 0) {
				newnewGoalSet = new HashSet<PGProposition>(newGoalSet);
				newnewGoalSet.removeAll(a.achieves);
				l = searchLevel(newnewGoalSet, layer);
				lit = l.iterator();
				while (lit.hasNext()) {
					s = lit.next();
					if (noMutexesTest(a, s, layer)) {
						s.add(a);
						actionSetList.add(s);
					}
				}
			}
		}

		return actionSetList;
	}

	// ******************************************************
	// Useful Methods
	// ******************************************************

	public int getLayer(Action a) {
		PGAction pg = (PGAction) actionMap.get(a);
		return pg.layer;
	}

	// ******************************************************
	// protected Classes
	// ******************************************************
	protected class Node {
		public int layer;
		public Set<MutexPair> mutexes;

		public Map<Node, Integer> mutexTable;

		public Node() {
		}

		public void reset() {
			layer = -1;
			mutexes = new HashSet<MutexPair>();
			mutexTable = new Hashtable<Node, Integer>();
		}

		public void setMutex(Node n, int l) {
			n.mutexTable.put(this, new Integer(l));
			this.mutexTable.put(n, new Integer(l));
		}

		public boolean mutexWith(Node n, int l) {
			/*
			 * if (this == n) return false; Iterator mit = mutexes.iterator();
			 * while (mit.hasNext()) { Mutex m = (Mutex) mit.next(); if
			 * (m.contains(n)) { return m.layer >= l; } } return false;
			 */
			Integer o = mutexTable.get(n);
			if (o == null) {
				return false;
			}
			return o.intValue() >= l;
		}
	}

	protected class PGAction extends Node {
		public Action action;
		public int counter, difficulty;

		public Set<PGProposition> conditions = new HashSet<PGProposition>();
		public Set<PGProposition> achieves = new HashSet<PGProposition>();
		public Set<PGProposition> deletes = new HashSet<PGProposition>();

		public PGAction() {

		}

		public PGAction(Action a) {
			action = a;
		}

		public Set<BinaryComparator> getComparators() {
			return action.getComparators();
		}

		public Set<ResourceOperator> getOperators() {
			return action.getOperators();
		}

		public void reset() {
			super.reset();
			counter = 0;
			difficulty = 0;
		}

		public String toString() {
			return action.toString();
		}
	}

	protected class PGNoOp extends PGAction {
		public PGProposition proposition;

		public PGNoOp(PGProposition p) {
			proposition = p;
		}

		public String toString() {
			return ("No-Op " + proposition);
		}

		public Set<BinaryComparator> getComparators() {
			return new HashSet<BinaryComparator>();
		}

		public Set<ResourceOperator> getOperators() {
			return new HashSet<ResourceOperator>();
		}
	}

	protected class PGProposition extends Node {
		public Proposition proposition;

		public Set<PGAction> achieves = new HashSet<PGAction>();
		public Set<PGAction> achievedBy = new HashSet<PGAction>();
		public Set<PGAction> deletedBy = new HashSet<PGAction>();

		public PGProposition(Proposition p) {
			proposition = p;
		}

		public String toString() {
			return proposition.toString();
		}
	}

	protected class MutexPair {
		public Node node1, node2;

		public MutexPair(Node n1, Node n2) {
			node1 = n1;
			node2 = n2;
		}
	}

	// ******************************************************
	// Debugging Classes
	// ******************************************************
	public void printGraph() {
		for (int i = 0; i <= num_layers; ++i) {
			System.out.println("-----Layer " + i
					+ "----------------------------------------");
			printLayer(i);
		}
		System.out
				.println("-----End -----------------------------------------------");
	}

	public void printLayer(int i) {
		System.out.println("Facts:");
		Iterator<PGProposition> pit = propositions.iterator();
		Iterator<?> mit = null;
		PGProposition p = null;
		while (pit.hasNext()) {
			p = pit.next();
			if (p.layer <= i && p.layer >= 0) {
				System.out.println("\t" + p);
				System.out.println("\t\tmutex with");
				mit = p.mutexTable.keySet().iterator();
				PGProposition pm = null;
				while (mit.hasNext()) {
					pm = (PGProposition) mit.next();
					Integer il = p.mutexTable.get(pm);
					if (il.intValue() >= i) {
						System.out.println("\t\t\t" + pm);
					}
				}
			}
		}
		if (i == num_layers)
			return;
		System.out.println("Actions:");
		Iterator<PGAction> ait = actions.iterator();
		PGAction a = null;
		PGAction am = null;
		Integer il = null;
		while (ait.hasNext()) {
			a = ait.next();
			if (a.layer <= i && a.layer >= 0) {
				System.out.println("\t" + a);
				System.out.println("\t\tmutex with");
				mit = a.mutexTable.keySet().iterator();
				while (mit.hasNext()) {
					am = (PGAction) mit.next();
					il = a.mutexTable.get(am);
					if (il.intValue() >= i) {
						System.out.println("\t\t\t" + am);
					}
				}
			}
		}
	}

}