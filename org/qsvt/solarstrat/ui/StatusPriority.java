/*
 * Created on May 29, 2005
 *
 *  qSolarSim, a solar car simulator for used by Queen's University.
 *  Copyright (C) 2005  Michael Rooke, mike.rooke@gmail.com
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  mike.rooke@gmail.com
 */
package org.qsvt.solarstrat.ui;

/**
 * @author mrsmiles
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StatusPriority implements Comparable{

	private String priority;
	private int index;
	
	private StatusPriority(String priority, int index) {
		this.priority = priority;
		this.index = index;
	}
	
	public String toString() {
		return priority;
	}
	
	public int getIndex() {
		return index;
	}
	
	// The index thing didn't seem to be working for some odd reason....
	public int compareTo(Object o){
		if(!(o instanceof StatusPriority)){
			throw new ClassCastException();
		}
		StatusPriority sp = ((StatusPriority)o);
		// if both are not default, we can nicely use thier alphabetically sorted nature
		if(this.toString().compareTo("default")!=0 && sp.toString().compareTo("default")!=0){
			return this.toString().compareTo(sp.toString());
		}
		// if they're both default...
		else if(this.toString().compareTo(sp.toString()) == 0){
			return 0;
		}
		// then 'this' is NOT default, so the other must be
		else if (this.toString().compareTo("default")!= 0){
			return -1;
		}
		// finally, if 'this' IS default, then o can't be.
		else return 1;
	}
	
	public static final StatusPriority CRITICAL = new StatusPriority("critical", 0);
	public static final StatusPriority IMPORTANT = new StatusPriority("important", 1);
	public static final StatusPriority NORMAL = new StatusPriority("normal", 2);
	public static final StatusPriority DEFAULT = new StatusPriority("default", 3);
}
