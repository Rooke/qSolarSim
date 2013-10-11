/*
 * Created on May 13, 2005
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
 *
 * This will represent the root node of a TereModel in which all the plottable Simulation and
 * Telmetry data sets will be organized.  See 'CheesyDiag.bmp'
 */
package org.qsvt.solarstrat.core;

import java.util.HashSet;
import java.util.Set;

import org.qsvt.solarstrat.core.event.WorkspaceEvent;
import org.qsvt.solarstrat.core.event.WorkspaceEventListener;
import org.qsvt.solarstrat.ui.SolarstratMainWindow;

/**
 * @author Mike Rooke
 *
 * 
 */
public class WorkspaceModel {

	private String name = null;
	//private WorkspaceDisplayer wsDisplayer;
	private SolarstratMainWindow parent;
	
	private Set sources = new HashSet(), listeners = new HashSet();
	
	// child 0: Simulations
	// child 1: Telemetry Nodes
	// child 2: Plot Nodes
	public WorkspaceModel(String name){
		this.name = name;
		//wsDisplayer = new WorkspaceDisplayer(drawSpace, chartArea, tree);
		
		//this.add(new DefaultMutableTreeNode("Simulations"));
		//this.add(new DefaultMutableTreeNode("Telemetry"));
		//this.add(new DefaultMutableTreeNode("Plots"));
	}
	
	public boolean add(DataSourceModel source) {
		if(sources.contains(source)) {
			return false;
		}
		else {
			sources.add(source);
			fireSourceAddEvent(source);
			return true;
		}
	}
	
	public void remove(DataSourceModel source) {
		if(sources.contains(source)) {
			sources.remove(source);
			fireSourceRemoveEvent(source);
		}
	}
	
	private void fireSourceAddEvent(DataSourceModel source) {
		java.util.Iterator i = listeners.iterator();
		while(i.hasNext()) {
			((WorkspaceEventListener)i.next()).workspaceEventPerformed(new WorkspaceEvent(this,"add",source));
		}
	}
	
	private void fireSourceRemoveEvent(DataSourceModel source) {
		java.util.Iterator i = listeners.iterator();
		while(i.hasNext()) {
			((WorkspaceEventListener)i.next()).workspaceEventPerformed(new WorkspaceEvent(this,"remove",source));
		}		
	}
	
	private void fireNameChangeEvent() {
		java.util.Iterator i = listeners.iterator();
		while(i.hasNext()) {
			((WorkspaceEventListener)i.next()).workspaceEventPerformed(new WorkspaceEvent(this,"nameChange",null));
		}		
	}
	
	public boolean isListener(WorkspaceEventListener sl) {
		return listeners.contains(sl);
	}
	  
	// if some ActionListener wants to listen in, it may add itself manually
	public void addWorkspaceListener(WorkspaceEventListener sl) {
		listeners.add(sl);
	}
	
	public void removeWorkspaceListener(WorkspaceEventListener sl) {
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
		
	/**
	 * @return Returns the sources.
	 */
	public Set getSources() {
		return sources;
	}
}
