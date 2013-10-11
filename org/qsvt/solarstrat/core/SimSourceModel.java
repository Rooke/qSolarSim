/*
 * Created on Jun 12, 2005
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.qsvt.solarstrat.solarsim.Sim;
import org.qsvt.solarstrat.solarsim.SimOutput;

/**
 * @author Eyrak Paen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimSourceModel extends DataSourceModel {
	Sim sim = new Sim(); // Changed from initial value of null (why?)
	DataTableModel tableModel = new DataTableModel("Simulation Data");
	String simError = null;
	
	public SimSourceModel(String name) {
		super(name);
	}
	
	public Sim getSim() {
		return sim;
	}
	
	public void runSim(){
		new SimThread();
		return;
	}
	
	public void setSimArgs(String[] args){
		if(args!=null)
			sim.setArgs(args);
	}
	
	private class SimThread implements Runnable, ActionListener {
		public SimThread(){
			new Thread(this).start();
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getActionCommand().equals("UpdateSim")) {
				fireEvent("progressUpdated");
			}
		}
		
		public void run(){
			// We actually simulate here!
			sim = new Sim();
			sim.addActionListener(this);
			SimOutput so[] = null;
			fireEvent("simStart");
			
			try{
				so = sim.simulate();
				if(so == null) {
					simError = "An error occurred during simulation.";
					fireEvent("simError");
				}
				else {
					fireEvent("simSuccess");
				}
				sim = null;
			}
			catch(Exception x){
				System.err.println(x.getMessage() + "");
				simError = x.getMessage();
				fireEvent("simError");
				return;
			}
			finally {
				fireEvent("simFinish");
			}
			
			tableModel.removeAllColumns();
			for(int i = 0; i<so.length; i++){
				try {
					tableModel.addCol(new DataTableColumn(so[i].getProperty("name"),so[i].getProperty("unit"),so[i]));
					so[i] = null;
				} catch (DataTableException e) {
					e.printStackTrace();
					
				}
			}
			
			fireEvent("outputChanged");
		}
	}
	/**
	 * @return Returns the tableModel.
	 */
	public DataTableModel getTableModel() {
		return tableModel;
	}
	
	public String getSimError() {
		return simError;
	}
}
