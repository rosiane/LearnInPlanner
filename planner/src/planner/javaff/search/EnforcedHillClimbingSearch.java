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

package planner.javaff.search;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import planner.javaff.planning.Filter;
import planner.javaff.planning.State;


public class EnforcedHillClimbingSearch extends Search {
	protected BigDecimal bestHValue;

	protected LinkedList<String> closed;
	protected LinkedList<State> open;
	protected Filter filter = null;
	private Runtime runtime = Runtime.getRuntime();

	public EnforcedHillClimbingSearch(State s) {
		this(s, new HValueComparator());
	}

	public EnforcedHillClimbingSearch(State s, Comparator<State> c) {
		super(s);
		setComparator(c);

		closed = new LinkedList<String>();
		open = new LinkedList<State>();
	}

	public void setFilter(Filter f) {
		filter = f;
	}

	public State removeNext() {

		return open.removeFirst();
	}

	public boolean needToVisit(State s) {
		String stateString = s.toString().replace(" ", "");
		if (closed.contains(stateString)) {
			return false;
		}
		closed.add(stateString);
		return true;
	}

	public State search() {

		if (start.goalReached()) { // wishful thinking
			return start;
		}

		needToVisit(start); // dummy call (adds start to the list of 'closed'
							// states so we don't visit it again

		open.add(start); // add it to the open list
		bestHValue = start.getHValue(); // and take its heuristic value as the
										// best so far

		planner.Planner.infoOutput.println(bestHValue);

		State s = null;
		Set<State> successors = null;
		Iterator<State> succItr = null;
		State succ = null;
		int countVisited = 0;
		while (!open.isEmpty()) // whilst still states to consider
		{
			s = removeNext(); // get the next one

			successors = s.getNextStates(filter.getActions(s)); // and find
																// its
																// neighbourhood

			succItr = successors.iterator();

			while (succItr.hasNext()) {
				if (countVisited % 1000 == 0) {
					runtime.gc();
					System.out.println("Open size " + open.size());
				}
				if (countVisited % 10000 == 0) {
					System.out.println("Visited real " + countVisited);
				}
				succ = succItr.next(); // next successor

				if (needToVisit(succ)) {
					if (succ.goalReached()) { // if we've found a goal state -
												// return it as the solution
						return succ;
					} else if (succ.getHValue().compareTo(bestHValue) < 0) {
						// if we've found a state with a better heuristic value
						// than the best seen so far

						bestHValue = succ.getHValue(); // note the new best
														// avlue
						planner.Planner.infoOutput.println(bestHValue);
						open = new LinkedList<State>(); // clear the open list
						open.add(succ); // put this on it
						break; // and skip looking at the other successors
					} else {
						open.add(succ); // otherwise, add to the open list
					}
				}
				countVisited++;
			}

		}
		return null;
	}
}
