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

package javaff.data;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javaff.data.metric.FunctionSymbol;
import javaff.data.strips.Operator;
import javaff.data.strips.PDDLObject;
import javaff.data.strips.PredicateSymbol;
import javaff.data.strips.Proposition;
import javaff.data.strips.SimpleType;
import neural.network.interfaces.NeuralNetworkIF;
import neural.network.util.Weight;

import com.syvys.jaRBM.Layers.Layer;
import common.ClassExpression;

public class UngroundProblem {
	public String DomainName; // Name of Domain
	public String ProblemName; // Name of Problem
	public String ProblemDomainName; // Name of Domain as specified by the
										// Problem

	public Set requirements = new HashSet(); // Requirements of the domain
												// (String)

	public Set types = new HashSet(); // For simple object types in this domain
										// (SimpleTypes)
	public Map typeMap = new Hashtable(); // Set for mapping String -> types
											// (String => Type)
	public Map typeSets = new Hashtable(); // Maps a type on to a set of
											// PDDLObjects (Type => Set
											// (PDDLObjects))

	public Set predSymbols = new HashSet(); // Set of all (ungrounded) predicate
											// (PredicateSymbol)
	public Map predSymbolMap = new Hashtable(); // Maps Strings of the symbol to
												// the Symbols (String =>
												// PredicateSymbol)

	public Set constants = new HashSet(); // Set of all constant (PDDLObjects)
	public Map constantMap = new Hashtable(); // Maps Strings of the constant to
												// the PDDLObject

	public Set funcSymbols = new HashSet(); // Set of all function symbols
											// (FunctionSymbol)
	public Map funcSymbolMap = new Hashtable(); // Maps Strings onto the Symbols
												// (String => FunctionSymbol)

	public Set actions = new HashSet(); // List of all (ungrounded) actions
										// (Operators)

	public Set objects = new HashSet(); // Objects in the problem (PDDLObject)
	public Map objectMap = new Hashtable(); // Maps Strings onto PDDLObjects
											// (String => PDDLObject)

	public Set initial = new HashSet(); // Set of initial facts (Proposition)
	public Map funcValues = new Hashtable(); // Maps functions onto numbers
												// (NamedFunction => BigDecimal)
	public GroundCondition goal;

	public Metric metric;

	public Map staticPropositionMap = new Hashtable(); // (PredicateName => Set
														// (Proposition))

	public UngroundProblem() {
		this.typeMap.put(SimpleType.rootType.toString(), SimpleType.rootType);
	}

	private void buildTypeSets() // builds typeSets for easy access of all the
									// objects of a particular type
	{
		final Iterator tit = this.types.iterator();
		while (tit.hasNext()) {
			final SimpleType st = (SimpleType) tit.next();
			final Set s = new HashSet();
			this.typeSets.put(st, s);

			final Iterator oit = this.objects.iterator();
			while (oit.hasNext()) {
				final PDDLObject o = (PDDLObject) oit.next();
				if (o.isOfType(st)) {
					s.add(o);
				}
			}

			final Iterator cit = this.constants.iterator();
			while (cit.hasNext()) {
				final PDDLObject c = (PDDLObject) cit.next();
				if (c.isOfType(st)) {
					s.add(c);
				}
			}
		}

		final Set s = new HashSet(this.objects);
		s.addAll(this.constants);
		this.typeSets.put(SimpleType.rootType, s);
	}

	private void calculateStatics() // Determines whether the predicateSymbols
									// and funcSymbols are static or not
	{
		final Iterator pit = this.predSymbols.iterator();
		while (pit.hasNext()) {
			boolean isStatic = true;
			final PredicateSymbol ps = (PredicateSymbol) pit.next();
			final Iterator oit = this.actions.iterator();
			while (oit.hasNext() && isStatic) {
				final Operator o = (Operator) oit.next();
				isStatic = !o.effects(ps);
			}
			ps.setStatic(isStatic);
		}

		final Iterator fit = this.funcSymbols.iterator();
		while (fit.hasNext()) {
			boolean isStatic = true;
			final FunctionSymbol fs = (FunctionSymbol) fit.next();
			final Iterator oit = this.actions.iterator();
			while (oit.hasNext() && isStatic) {
				final Operator o = (Operator) oit.next();
				isStatic = !o.effects(fs);
			}
			fs.setStatic(isStatic);
		}
	}

	public GroundProblem ground(final LinkedList<ClassExpression> features,
			final NeuralNetworkIF neuralNetwork, final Layer[] net,
			final Weight[] weights) {
		this.calculateStatics();
		this.makeStaticPropositionMap();
		this.buildTypeSets();
		final Set groundActions = new HashSet();
		final Iterator ait = this.actions.iterator();
		while (ait.hasNext()) {
			final Operator o = (Operator) ait.next();
			final Set s = o.ground(this);
			groundActions.addAll(s);
		}

		// static-ify the functions
		final Iterator gait = groundActions.iterator();
		while (gait.hasNext()) {
			final Action a = (Action) gait.next();
			a.staticify(this.funcValues);
		}

		// remove static functions from the intial state
		this.removeStaticsFromInitialState();

		// -could put in code here to
		// a) get rid of static functions in initial state - DONE
		// b) get rid of static predicates in initial state - DONE
		// c) get rid of static propositions in the actions (this may have
		// already been done)
		// d) get rid of no use actions (i.e. whose preconditions can't be
		// achieved)

		final GroundProblem rGP = new GroundProblem(groundActions,
				this.initial, this.goal, this.funcValues, this.metric,
				features, neuralNetwork, net, weights);
		return rGP;
	}

	private void makeStaticPropositionMap() {
		final Iterator pit = this.predSymbols.iterator();
		while (pit.hasNext()) {
			final PredicateSymbol ps = (PredicateSymbol) pit.next();
			if (ps.isStatic()) {
				this.staticPropositionMap.put(ps, new HashSet());
			}
		}

		final Iterator iit = this.initial.iterator();
		while (iit.hasNext()) {
			final Proposition p = (Proposition) iit.next();
			if (p.name.isStatic()) {
				final Set pset = (Set) this.staticPropositionMap.get(p.name);
				pset.add(p);
			}
		}
	}

	private void removeStaticsFromInitialState() {
		// remove static functions
		/*
		 * Iterator fit = funcValues.keySet().iterator(); Set staticFuncs = new
		 * HashSet(); while (fit.hasNext()) { NamedFunction nf = (NamedFunction)
		 * fit.next(); if (nf.isStatic()) staticFuncs.add(nf); } fit =
		 * staticFuncs.iterator(); while (fit.hasNext()) { Object o =
		 * fit.next(); funcValues.remove(o); }
		 */

		// remove static Propositions
		final Iterator init = this.initial.iterator();
		final Set staticProps = new HashSet();
		while (init.hasNext()) {
			final Proposition p = (Proposition) init.next();
			if (p.isStatic()) {
				staticProps.add(p);
			}
		}
		this.initial.removeAll(staticProps);
	}

}