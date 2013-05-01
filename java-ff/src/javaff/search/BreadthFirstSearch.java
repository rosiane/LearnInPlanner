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

package javaff.search;

import java.util.Hashtable;
import java.util.LinkedList;

import javaff.planning.Filter;
import javaff.planning.State;

public class BreadthFirstSearch extends Search {

	protected LinkedList<State> open;
	protected Hashtable<Integer, String> closed;
	protected Filter filter = null;
	private Runtime runtime = Runtime.getRuntime();
	
	public BreadthFirstSearch(State s) {
		super(s);
		open = new LinkedList<State>();
		closed = new Hashtable<Integer, String>();
	}

	public void setFilter(Filter f) {
		filter = f;
	}

	public void updateOpen(State S) {
		open.addAll(S.getNextStates(filter.getActions(S)));
	}

	public State removeNext() {
		return open.removeFirst();
	}

	public boolean needToVisit(State s) {
		Integer Shash = new Integer(s.hashCode());
		String D = closed.get(Shash);
		String stateString = s.toString().replace(" ", "");

		if (closed.containsKey(Shash) && D.equals(stateString)) {
			return false;
		}

		closed.put(Shash, stateString);
		return true;
	}

	public State search() {

		open.add(start);

		State s = null;
		while (!open.isEmpty()) {
			if (nodeCount % 1000 == 0) {
				runtime.gc();
			}
			if (nodeCount % 10000 == 0) {
				System.out.println("Execution node " + nodeCount);
				System.out.println("Open size " + open.size());
			}
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
