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

import org.qsvt.solarstrat.solarsim.Sim;
import org.qsvt.solarstrat.solarsim.SimOutput;
import org.qsvt.solarstrat.solarsim.Units;

/**
 * @author Mike Rooke
 *
 * Sim Model is the class for handling a particular simulation's output.  All plottable
 * data items are displayed as children under this SimModel's tree node.
 */



public class SimModel extends DataSourceModel implements Runnable{
	
	Sim sim;
	WorkspaceModel ws;
	String arg[];
	DataTableModel dtm;
	
	public SimModel(WorkspaceModel ws, String arg[]){
		super("Untitled Simulation");
		this.sim = new Sim();
		this.ws = ws;
		this.arg = arg;
		new Thread(this).start();
	}
	
	public void run(){
		// We actually simulate here!
		sim.setArgs(arg);
		SimOutput so[] = null;
		try{
		so = sim.simulate();
		}
		catch(Exception x){
			System.err.println(x.getMessage() + "in class SimModel line 45");
			// perhaps stop this thread.
		}
		try {
			dtm = new DataTableModel("Simulation Data",Units.getNames(), Units.getUnits());
			add(so, dtm);
			////////////////Unfinished.............
			for(int i = 0; i<dtm.getColumnCount();i++){
				//add(new SimParamNode(dtm.getCol(i), dtm.getCol(8)));
				// Could also use the sum of the timeSeg column, column index 9, I believe
			}
		} catch (DataTableException e) {
			e.printStackTrace();
		}
		// Everything should now be in the tree and plottable! 
	}
	
	private void add(SimOutput so[], DataTableModel dtm){
		for(int i = 0; i<so.length; i++){
		try {
			dtm.addCol(new DataTableColumn(so[i].getProperty("name"),so[i].getProperty("unit"),so[i])); 
		} catch (DataTableException e) {
			e.printStackTrace();
		}
		}
	}
	
	
	public class SimParamNode extends DefaultMutableTreeNode implements DataNode{

	private DataTableModel data, time;
	private boolean visible = false;
	private PlotNode plot;
	

	
	
	public SimParamNode (DataTableColumn values, DataTableColumn time)throws DataTableException{
		plot = null;
		this.data = new DataTableModel();
		try{
		this.data.addCol(values);
		this.data.addCol(time);
		}
		catch(DataTableException e){
			throw e;
		}
		
		}
	public SimParamNode (DataTableColumn values, DataTableColumn time, PlotNode plot)throws DataTableException{

		this.data = new DataTableModel();
		try{
		this.data.addCol(values);
		this.data.addCol(time);
		}
		catch(DataTableException e){
			throw e;
		}
		this.plot = plot;
		
	}
	
	public PlotNode getPlot(){
		return plot;
	}
	
	public DataTableModel getData(){
		return data;
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

}
