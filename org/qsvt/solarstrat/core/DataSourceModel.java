/*
 * Created on Jun 11, 2005
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
package org.qsvt.solarstrat.core;

import java.util.HashSet;
import java.util.Set;

import org.qsvt.solarstrat.core.event.SourceEvent;
import org.qsvt.solarstrat.core.event.SourceEventListener;

/**
 * @author Eyrak Paen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class DataSourceModel {
	protected String name = null;
	protected Set listeners = new HashSet();
	
	public DataSourceModel(String name) {
		this.name = name;
	}
	
	protected void fireNameChangeEvent() {
		SourceEvent se = new SourceEvent(this, "nameChange");
		java.util.Iterator i = listeners.iterator();
		while(i.hasNext()) {
			((SourceEventListener)i.next()).sourceEventPerformed(se);
		}
	}
	
	protected void fireEvent(String eventName) {
		SourceEvent se = new SourceEvent(this, eventName);
		java.util.Iterator i = listeners.iterator();
		while(i.hasNext()) {
			((SourceEventListener)i.next()).sourceEventPerformed(se);
		}
	}
	
	public boolean isListener(SourceEventListener sl) {
		return listeners.contains(sl);
	}
	  
	// if some ActionListener wants to listen in, it may add itself manually
	public void addSourceListener(SourceEventListener sl) {
		listeners.add(sl);
	}
	
	public void removeSourceListener(SourceEventListener sl) {
		listeners.remove(sl);
	}

	public String toString(){
		return name;
	}
	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
		this.fireNameChangeEvent();
	}
}
