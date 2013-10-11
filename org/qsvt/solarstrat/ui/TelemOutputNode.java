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

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;

import java.awt.Component;

import org.qsvt.solarstrat.core.DataTableColumn;
import org.qsvt.solarstrat.core.TelemSourceModel;

/**
 * @author Mike Rooke
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TelemOutputNode extends DataSourceNode{
	TelemSourceModel model;
	int dataIndex, timeIndex;
	Chart2D chart;
	
	public TelemOutputNode(SolarstratMainWindow wnd, TelemSourceModel model, int dataIndex, int timeIndex) {
		super(wnd);
		this.model = model;
		this.dataIndex = dataIndex;
		this.timeIndex = timeIndex;
	}
	
	private void refresh() {
		DataTableColumn data, time;
		ITrace2D trace = model.getTrace(dataIndex/2);// since our data columns are divided
													// by time columns, the trace index
													// is half of the data index
														
		data = model.getTableModel().getCol(dataIndex);
		time = model.getTableModel().getCol(timeIndex);
		
		try {
			//chart.removeTrace((ITrace2D)(chart.getTraces().get(0)));
			//chart.addTrace(trace);
			chart.paintAll(chart.getGraphics());
		} catch (NullPointerException e) {
			wnd.getStatusBar().addMessage(model.getName() + " has nothing to display!");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.qsvt.solarstrat.ui.DataSourceNode#getView()
	 */
	public Component getView() {
		refresh();
		return chart;
	}

	public String toString() {
		return model.getTableModel().getColumnName(dataIndex);
	}

}
