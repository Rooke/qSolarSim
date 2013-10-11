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

import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mike Rooke
 *
 * We'll need 	:  A SQL url (e.g. "jdbc:mysql://130.15.86.212/test", "user", "pass")
 * 				:  A bunch of ITrace2D's, or be able to return them all
 * 
 */
public class TelemSourceModel extends DataSourceModel {
	
	DataTableModel dataTable = new DataTableModel();
	
	Set childTelemCollectors = new HashSet();
	static final int	COMMAND = 1, MEASUREMENT = 2, 
						VALUE = 3, TIME = 4; 		//SQL Table Constants
	static final String	names[] = {"Battery Voltage",
									"Battery Current",
									"Array Current",
									"Motor Current",
									"Speed"};
	static final String units [] = {"V","A","A","A","m/s"};
	static final String cmds [] = {"BV", "BC", "AC", "MC", "SP"};
	
	ITrace2D trace[];  //array of all the traces
	
	public TelemSourceModel(String name, String SQLurl) throws SQLException{
		super(name);
		
		trace = new ITrace2D [5];
		for(int i = 0; i<=4; i++){
		DataTableColumn data = new DataTableColumn(names[i], units[i]);
		DataTableColumn time = new DataTableColumn("Time", "Seconds");
		trace[i] = new Trace2DLtd(1000);
		TelemDataCollector TDCol = new TelemDataCollector(cmds[i], "TheSQLURL",
						names[i], trace[i], data, time);
			//...plus every other argument to be included in this constructor later
		try {
			dataTable.addCol(data);
			dataTable.addCol(time);
		} catch (DataTableException e) {
			System.out.println("No success in entering data");
		}
		}
	}
	
	public ITrace2D getTrace(int i){
		return trace[i];
	}
	
	public DataTableModel getTableModel(){
		return dataTable;
	}
	

}
