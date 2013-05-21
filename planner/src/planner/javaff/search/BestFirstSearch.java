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

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.TreeSet;

import planner.javaff.planning.Filter;
import planner.javaff.planning.State;


import common.preprocessor.file.FileManager;

public class BestFirstSearch extends Search {

	protected LinkedList<String> closed;
	protected TreeSet<State> open;
	protected Filter filter = null;
	private Runtime runtime = Runtime.getRuntime();
	private long timeLimit;
	private String fileResult;

	public BestFirstSearch(State s, long timeLimit, String fileResult) {
		this(s, new HValueComparator(), timeLimit, fileResult);
	}

	public BestFirstSearch(State s, Comparator<State> c, long timeLimit, String fileResult) {
		super(s);
		setComparator(c);

		closed = new LinkedList<>();
		open = new TreeSet<State>(comp);
		this.timeLimit = timeLimit;
		this.fileResult = fileResult;
	}

	public void setFilter(Filter f) {
		filter = f;
	}

	public void updateOpen(State S) {
		open.addAll(S.getNextStates(filter.getActions(S)));
	}

	public State removeNext() {
		return open.pollFirst();
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
		open.add(start);
		State s = null;
		int countVisited = 0;
		long initialTime = System.currentTimeMillis();
		while (!open.isEmpty()) {
			if (countVisited % 1000 == 0) {
				runtime.gc();
				System.out.println("Open size " + open.size());
				if(fileResult != null){
					try {
						FileManager.write(fileResult, "Open size " + open.size(), true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			if (countVisited % 10000 == 0) {
				System.out.println("Execution node " + nodeCount);
				System.out.println("Visited real " + countVisited);
				if(fileResult != null){
					try {
						FileManager.write(fileResult, "Execution node " + nodeCount, true);
						FileManager.write(fileResult, "Visited real " + countVisited, true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			s = removeNext();
			if (needToVisit(s)) {
				++nodeCount;
				if (s.goalReached()) {
					System.out.println("Execution node " + nodeCount);
					System.out.println("Visited " + countVisited);
					if(fileResult != null){
						try {
							FileManager.write(fileResult, "Found goal", true);
							FileManager.write(fileResult, "Execution node " + nodeCount, true);
							FileManager.write(fileResult, "Visited " + countVisited, true);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					return s;
				} else {
					updateOpen(s);
				}
			}
			countVisited++;
			if(System.currentTimeMillis() - initialTime > timeLimit){
				System.out.println("Execution node " + nodeCount);
				System.out.println("Visited real " + countVisited);
				if(fileResult != null){
					try {
						FileManager.write(fileResult, "Time reached", true);
						FileManager.write(fileResult, "Execution node " + nodeCount, true);
						FileManager.write(fileResult, "Visited real " + countVisited, true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				break;
			}
		}
		System.out.println("Visited " + countVisited);
		if(fileResult != null){
			try {
				FileManager.write(fileResult, "Visited " + countVisited, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
