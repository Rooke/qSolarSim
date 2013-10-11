/*
 * Created on Jun 6, 2005
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
 * This implements the chart(stuff) and
 * plot(stuff) methods.  Classes should be able to exist with a TableModel (the chart) 
 * and a JPanel (the plot), and send them to be plotted using this class.
 */
package org.qsvt.solarstrat.core;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;

/**
 * @author Mike Rooke
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WorkspaceDisplayer implements TreeSelectionListener{
	
		JScrollPane chartArea = null;
		JTabbedPane drawSpace = null;
		TreeNode lastClicked;
		JTree tree;
		
		public WorkspaceDisplayer(JTabbedPane drawSpace, JScrollPane chartArea, JTree tree){
			this.chartArea = chartArea;
			this.drawSpace = drawSpace ;
			this.tree = tree;
		}
		public void plot(PlotNode plot){
			drawSpace.setComponentAt(0, plot.getPlot());
		}
		public void chart(DataNode node){
			//drawSpace.setComponentAt(0, );
			JTable dataTable = new JTable(node.getData());
			dataTable.setColumnSelectionAllowed(true);
			dataTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			dataTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
			chartArea.setViewportView(dataTable);
		}
		public void valueChanged(TreeSelectionEvent e){
			 TreeNode node = (TreeNode)
			 tree.getLastSelectedPathComponent();
			 
			 if(node.equals(lastClicked)){
			 	if(node.isLeaf()){
			 		// If it's a leaf, we know it's a data node
			 		DataNode dNode = (DataNode)
					 tree.getLastSelectedPathComponent();
			 	chart(dNode);
			 	plot(dNode.getPlot());
			 	}
			 	else{
			 		//if it isn't, we can probably chart it..
			 		//TODO: chart it if possible
			 	}
			 }
			 lastClicked = node;
		}

	
}
