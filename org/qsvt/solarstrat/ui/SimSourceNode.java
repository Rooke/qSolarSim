/*
 * Created on Jun 12, 2005
 *
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

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import org.qsvt.solarstrat.core.SimSourceModel;
import org.qsvt.solarstrat.core.event.SourceEvent;
import org.qsvt.solarstrat.core.event.SourceEventListener;

/**
 * @author Eyrak Paen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimSourceNode extends DataSourceNode implements SourceEventListener {
	private static final int TIME_COL_INDEX = 8;
	
	SimSourceModel model = null;
	JScrollPane outputPane = new JScrollPane();
	JTable dataTable = new JTable();
	Task simStatus = null;
	
	JPanel simParamPanel;
	JPanel consolePanel;
	
	JTabbedPane tabs = new JTabbedPane(javax.swing.JTabbedPane.BOTTOM);
	
	public SimSourceNode(SolarstratMainWindow wnd, SimSourceModel model) {
		super(wnd);
		this.model = model;
		this.model.addSourceListener(this);
		this.simStatus = new Task(model.getName() + " simulation");

		// Init status object
		wnd.getStatusBar().addMessage(model.getName() + " created.");
		
		dataTable.setModel(model.getTableModel());
		dataTable.setColumnSelectionAllowed(true);
		dataTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		dataTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		
		outputPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		outputPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		outputPane.setViewportView(dataTable);

		tabs.addTab("Output", null, outputPane, null);
		//tabs.addTab("Simulation Parameters", null, simParamPanel, null);
		//tabs.addTab("Console", null, consolePanel, null);
		//this.add(new SimSourceNode());
	}
	
	/* (non-Javadoc)
	 * @see org.qsvt.solarstrat.core.event.SourceListener#sourceEventPerformed(org.qsvt.solarstrat.core.event.SourceEvent)
	 */
	public void sourceEventPerformed(SourceEvent se) {
		if(se.getEventName().equals("progressUpdated")) {
			this.simStatus.setProgress(model.getSim().getPercentDone());
		}
		else if(se.getEventName().equals("simStart")) {
			wnd.getStatusBar().addMessage("Running " + model.getName() + " simulation...");
			wnd.getStatusBar().addTask(simStatus);
		}
		else if(se.getEventName().equals("simFinish")) {
			wnd.getStatusBar().removeTask(simStatus);
		}
		else if(se.getEventName().equals("simSuccess")) {
			wnd.getStatusBar().addMessage(model.getName() + " simulation finished successfully.");
		}
		else if(se.getEventName().equals("simError")) {
			wnd.getStatusBar().addMessage("Error (" + model.getName() + "): " + model.getSimError());
		}
		else if(se.getEventName().equals("outputChanged")) {
			this.refreshOutputNodes();
		}
	}
	
	private void refreshOutputNodes() {
		new SimOutputRenderThread();
	}
	
	public Component getView() {
		return tabs;
	}
	
	/**
	 * @return Returns the model.
	 */
	public SimSourceModel getModel() {
		return model;
	}
	
	public String toString() {
		return model.getName();
	}
	
	private class SimOutputRenderThread implements Runnable {
		public SimOutputRenderThread() {
			new Thread(this).start();
		}
		
		public void run() {
			DefaultTreeModel tm = wnd.getWorkspaceView().getTreeModel();
			
			for(int i = 0; i < getChildCount(); i++) {
				tm.removeNodeFromParent((MutableTreeNode)getChildAt(i));
			}
			
			for(int i = 0; i < 8; i++) {
				tm.insertNodeInto(new SimOutputNode(wnd,model,i,8), SimSourceNode.this, i);
			}
		}
	}
}
