/*
 * Created on Jun 16, 2005
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import org.qsvt.solarstrat.core.TelemSourceModel;
import org.qsvt.solarstrat.core.event.SourceEvent;
import org.qsvt.solarstrat.core.event.SourceEventListener;

/**
 * @author Mike Rooke
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TelemSourceNode extends DataSourceNode implements SourceEventListener, ActionListener {

	TelemSourceModel model = null;
	JScrollPane outputPane = new JScrollPane();
	JTable dataTable = new JTable();
	Status telemStatus = null; // ??  perhaps ??
	
	JPanel simParamPanel;
	JPanel consolePanel;
	
	JTabbedPane tabs = new JTabbedPane(javax.swing.JTabbedPane.BOTTOM);
	
	public TelemSourceNode(SolarstratMainWindow wnd, TelemSourceModel model) {
		super(wnd);
		this.model = model;
		this.model.addSourceListener(this);
		

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
		
		refreshOutputNodes();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		wnd.getStatusBar().addMessage(e.getActionCommand());
	}
	private void refreshOutputNodes() {
		this.removeAllChildren();
		
		for(int i = 0; i < 10; i+=2) {
			this.add(new TelemOutputNode(wnd,model,i,i+1));
		}
	}
	
	public Component getView() {
		return tabs;
	}
	
	/**
	 * @return Returns the model.
	 */
	public TelemSourceModel getModel() {
		return model;
	}
	
	public String toString() {
		return model.getName();
	}
	
	/* (non-Javadoc)
	 * @see org.qsvt.solarstrat.core.event.SourceEventListener#sourceEventPerformed(org.qsvt.solarstrat.core.event.SourceEvent)
	 */
	public void sourceEventPerformed(SourceEvent se) {
		
	}

}
