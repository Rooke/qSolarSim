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

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import java.awt.Component;
import java.util.Iterator;

import org.qsvt.solarstrat.core.DataTableColumn;
import org.qsvt.solarstrat.core.SimSourceModel;

/**
 * @author Eyrak Paen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimOutputNode extends DataSourceNode {
	SimSourceModel model;
	int dataIndex, timeIndex;
	Chart2D chart = new Chart2D();
	
	public SimOutputNode(SolarstratMainWindow wnd, SimSourceModel model, int dataIndex, int timeIndex) {
		super(wnd);
		this.model = model;
		this.dataIndex = dataIndex;
		this.timeIndex = timeIndex;
		refresh();
	}
	
	private void refresh() {
		DataTableColumn data, time;
		ITrace2D trace = new Trace2DSimple(); 
		data = model.getTableModel().getCol(dataIndex);
		time = model.getTableModel().getCol(timeIndex);
		
		if(data.size()>10000){
		for(int i = 0; i < data.size(); i++) {
			if(i%10 == 0)
			trace.addPoint(((Double)time.get(i)).doubleValue(), ((Double)data.get(i)).doubleValue());
		}
		}
		else{
			for(int i = 0; i < data.size(); i++) {
				
				trace.addPoint(((Double)time.get(i)).doubleValue(), ((Double)data.get(i)).doubleValue());
			}
		}
		
		
		
		Iterator i = chart.getTraces().iterator();
		while(i.hasNext()) {
			chart.removeTrace((ITrace2D)i.next());
		}

		chart.addTrace(trace);
		//chart.setScaleX(true);
		//chart.setScaleY(true);
		//chart.setGridX(true);
		//chart.setGridY(true);
	}
	
	/* (non-Javadoc)
	 * @see org.qsvt.solarstrat.ui.DataSourceNode#getView()
	 */
	public Component getView() {
		return chart;
	}

	public String toString() {
		return model.getTableModel().getColumnName(dataIndex);
	}
}
