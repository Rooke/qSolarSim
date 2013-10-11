/*
 * Created on Mar 26, 2005
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

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * @author Eyrak Paen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CSVImporter extends DataTableImporter {
	public CSVImporter() {
		this.setImportParam("hasHeaderNames", "true");
		this.setImportParam("hasHeaderUnits", "true");
		this.setImportParam("defaultValue", "0");
	}
	
	/* (non-Javadoc)
	 * @see org.qsvt.solarstrat.core.DataTableImporter#importData(java.lang.String, org.qsvt.solarstrat.core.DataTableModel)
	 */
	public void importData(String location, DataTableModel data) throws DataTableException {
		BufferedReader in = null;
		int i = 0, numCols = 0;
		String names[] = null, units[] = null, stringValues[] = null, line = null;
		double doubleValues[] = null;
		double defaultValue = Double.parseDouble(this.getImportParam("defaultValue"));
		
		// Open new file
		try {
			in = new BufferedReader(new FileReader(location));
		}
		catch (Throwable t) {
			throw new DataTableException("Unable to open file for CSV import (" + location + ").");
		}
		
		try {
			if(this.getImportParam("hasHeaderNames").compareTo("true") == 0) {
				names = in.readLine().split(",");
				numCols = names.length;
				for(i = 0; i < numCols; i++) {
					data.addCol(names[i],"");
				}
				if(this.getImportParam("hasHeaderUnits").compareTo("true") == 0) {
					units = in.readLine().split(",");
					for(i = 0; i < units.length; i++) {
							data.getCol(i).setUnits(units[i]);
					}
				}
			}
			else if(this.getImportParam("hasHeaderUnits").compareTo("true") == 0) {
				units = in.readLine().split(",");
				numCols = units.length;
				for(i = 0; i < numCols; i++) {
					data.addCol(Integer.toString(i),units[i]);
				}
			}
			else {
				// Init columns using the first row of data
				stringValues = in.readLine().split(",");
				doubleValues = new double[stringValues.length];
				numCols = stringValues.length;
				for(i = 0; i < numCols; i++) {
					data.addCol(Integer.toString(i), "");
					doubleValues[i] = Double.parseDouble(stringValues[i]);
				}
				
				data.addRow(doubleValues);
			}

			// Read data rows
			line = in.readLine();
			doubleValues = new double[numCols];
			
			while(line != null && line.compareTo("") != 0) {
				stringValues = line.split(",");
				i = 0;
				while(i < stringValues.length) {
					doubleValues[i] = Double.parseDouble(stringValues[i]);
					i++;
				}
				
				// Fill in remaining columns with default value
				while(i < numCols) {
					doubleValues[i] = defaultValue;
					i++;
				}
				
				data.addRow(doubleValues);
			}
		}
		catch(Throwable t) {
			throw new DataTableException("Error reading data at " + location);
		}
		finally {
			try {
				in.close();
			}
			catch(Throwable t) {
				throw new DataTableException("Unable to close file during CSV import (" + location + ").");
			}
		}

		return;
	}

}
