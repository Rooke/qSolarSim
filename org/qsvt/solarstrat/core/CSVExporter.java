/*
 * Created on Mar 18, 2005
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

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * @author Eyrak Paen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CSVExporter extends DataTableExporter {
	public void exportData(String location, DataTableModel data) throws DataTableException {
		BufferedWriter out = null;
		int i = 0;
		int j = 0;
		
		// Open new file
		try {
			out = new BufferedWriter(new FileWriter(location));
		}
		catch (Throwable t) {
			throw new DataTableException("Unable to open file for CSV export (" + location + ").");
		}
		
		try {
			String names[] = data.getDataTableNames();
			String units[] = data.getDataTableUnits();
			
			// Write headers
			for(i = 0; i < names.length; i++) {
				if(i == names.length - 1) out.write(names[i] + "\n");
				else out.write(names[i] + ",");
			}
			
			for(i = 0; i < names.length; i++) {
				if(i == names.length - 1) out.write(units[i] + "\n");
				else out.write(units[i] + ",");
			}
			
			// Write data rows
			for(i = 0; i < data.getRowCount(); i++) {
				double row[] = data.getRow(i);
				for(j = 0; j < row.length; i++) {
					if(j == row.length - 1) out.write(row[j] + "\n");
					else out.write(row[j] + ",");
				}
			}
		}
		catch(Throwable t) {
			throw new DataTableException("Error writing data at " + location);
		}
		finally {
			try {
				out.close();
			}
			catch(Throwable t) {
				throw new DataTableException("Unable to close file during CSV export (" + location + ").");
			}
		}

		return;
	}
}
