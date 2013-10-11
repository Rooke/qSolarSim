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
 */
package org.qsvt.solarstrat.core;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;


/**
 * @author Mike Rooke
 *
 *Depreciated
 */
public class TelemParamNode extends DefaultMutableTreeNode implements DataNode{

	private DataTableModel data;
	private boolean visible = false;
	private PlotNode plot;
	private TelemDataCollector tdc;
	
	
	public TelemParamNode (DataTableColumn data, DataTableColumn time)throws DataTableException{
		plot = new PlotNode(data, time);
		this.data = new DataTableModel();
		try{
		this.data.addCol(data);
		this.data.addCol(time);
		}
		catch(DataTableException e){
			throw e;
		}
		/*try {
			//Assuming here that trace(0) is the one we want...
			//tdc = new TelemDataCollector((ITrace2D)plot.getTraces().get(0), 1000);
		} */
		}
	public TelemParamNode (DataTableColumn data, DataTableColumn time, PlotNode plot)throws DataTableException{

		this.data = new DataTableModel();
		try{
		this.data.addCol(data);
		this.data.addCol(time);
		}
		catch(DataTableException e){
			throw e;
		}
		this.plot = plot;
		/*try {
			//		Assuming here that trace(0) is the one we want...
			//tdc = new TelemDataCollector((ITrace2D)plot.getTraces().get(0), 1000);
		} */
		
	}
	public DataTableModel getData(){
		return data;
	}
	public PlotNode getPlot(){
		return plot;
	}
	
	// All this is junk.  We know this will be a leaf, so all methods return predictable
	// results
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
	
	public String toString(){
		return data.getColumnName(0);
	}
}
