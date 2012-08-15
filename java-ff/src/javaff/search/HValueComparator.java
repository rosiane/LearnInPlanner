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

import javaff.planning.State;
import java.util.Comparator;
import java.math.BigDecimal;

public class HValueComparator implements Comparator<State> {
	public int compare(State obj1, State obj2) {
		int r = 0;
		BigDecimal d1 = obj1.getHValue();
		BigDecimal d2 = obj2.getHValue();
		r = d1.compareTo(d2);
		if (r == 0) {
			d1 = obj1.getGValue();
			d2 = obj2.getGValue();
			r = d1.compareTo(d2);
			if (r == 0) {
				if (obj1.hashCode() > obj2.hashCode()) {
					r = 1;
				} else if (obj1.hashCode() == obj2.hashCode()
						&& obj1.equals(obj2)) {
					r = 0;
				} else {
					r = -1;
				}
			}
		}
		return r;
	}
}