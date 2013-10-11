/*
 * Created on May 14, 2005
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

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import java.util.Enumeration;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * @author Mike Rooke
 *
 * Given some data to plot (in the form of a DataTableColumn), this creates a jchart2D
 * static plot (possibly dynamic for telemetry?)
 */
public class PlotNode extends DefaultMutableTreeNode{
	
	DataTableColumn data;
	DataTableColumn timeSeg;
	JPanel plot;
	

	public PlotNode(DataTableColumn data, DataTableColumn time){
		this.data = data;
		timeSeg=time;
	}
	
	// In case we want some sort of custom plot
	public PlotNode(JPanel plot){
		this.plot = plot;
	}
	
	public JPanel getPlot(){
		if (plot == null){
			createPlot();
		}
		return plot;
	}
	public Set getTraces(){
		if(plot instanceof Chart2D){
			Chart2D p = (Chart2D)plot;
			return p.getTraces();
		}
		return null;
	}
	
	// Actually makes the jChart2D to be displayed.
	private void createPlot(){
		Trace2DSimple trace = new Trace2DSimple();
		for(int i = 0; i<data.size(); i++){
			// Add each point at a time
			trace.addPoint(((Double)data.get(i)).doubleValue(), 
					((Double)timeSeg.get(i)).doubleValue()); 
		}
		trace.setName(data.getUnits() + " vs. time");
	    trace.setPhysicalUnits(data.getUnits(),"seconds");
		Chart2D chart = new Chart2D();
		chart.addTrace(trace);
		plot = chart; //  terrible naming here..  
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getChildCount()
	 */
	public int getChildCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getAllowsChildren()
	 */
	public boolean getAllowsChildren() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#isLeaf()
	 */
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return true;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#children()
	 */
	public Enumeration children() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getParent()
	 */
	public TreeNode getParent() {
		// TODO Auto-generated method stub
		return parent;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getChildAt(int)
	 */
	public TreeNode getChildAt(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeNode#getIndex(javax.swing.tree.TreeNode)
	 */
	public int getIndex(TreeNode arg0) {
		// TODO Auto-generated method stub
		return -1; // There are no children
	}

}
