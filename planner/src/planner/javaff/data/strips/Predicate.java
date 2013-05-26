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

package planner.javaff.data.strips;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import planner.javaff.data.GroundCondition;
import planner.javaff.data.GroundEffect;
import planner.javaff.data.Literal;
import planner.javaff.data.UngroundCondition;
import planner.javaff.data.UngroundEffect;


public class Predicate extends Literal implements UngroundCondition, UngroundEffect
{
	public Predicate(PredicateSymbol p)
    {
		name = p;
	}

	public boolean effects(PredicateSymbol ps)
    {
		return name.equals(ps);
	}

	public UngroundCondition minus(UngroundEffect effect)
    {
		return effect.effectsAdd(this);
    }

	public UngroundCondition effectsAdd(UngroundCondition cond)
    {
		if (this.equals(cond)) return TrueCondition.getInstance();
		else return cond;
    }

	public Set getStaticPredicates()
    {
		Set rSet = new HashSet();
		if (name.isStatic()) rSet.add(this);
		return rSet;
    }

	public Proposition ground(Map varMap)
    {
		Proposition p = new Proposition(name);
		Iterator pit = parameters.iterator();
		while (pit.hasNext())
		{
                        Object o = pit.next();
                        PDDLObject po;
                        if (o instanceof PDDLObject) po = (PDDLObject) o;
                        else 
                        {
                           Variable v = (Variable) o;                          
                           po = (PDDLObject) varMap.get(v);
                        }
                        
			p.addParameter(po);
		}
		return p;
    }

	public GroundCondition groundCondition(Map varMap)
    {
		return ground(varMap);
    }

	public GroundEffect groundEffect(Map varMap)
    {
		return ground(varMap);
	}

	public int hashCode()
    {
		int hash = 5;
		hash = 31 * hash ^ name.hashCode();
		hash = 31 * hash ^ parameters.hashCode();
		return hash;
	}

}