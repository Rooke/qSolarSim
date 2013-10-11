/*
 * Created on Feb 21, 2005
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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/**
 * @author Eyrak Paen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DataTableModel extends AbstractTableModel {
	private String tableName = "Untitled Table";
	private Vector columns = new Vector();
	private int numRows = 0; // redundant? or worth the convenience?
	private double defaultVal = 0.0F;
	
	public DataTableModel() {
		return;
	}
	
	public DataTableModel(String tableName) {
		this.tableName = tableName;
	}
	
	public DataTableModel(String tableName, String names[], String units[]) throws DataTableException {
		this.tableName = tableName;
		if(names.length != units.length) {
			throw new DataTableException("Name list and units list differ in length.");
		}
		
		for(int i = 0; i < names.length; i++) {
			columns.add(new DataTableColumn(names[i], units[i]));
			((DataTableColumn)columns.get(i)).setTableModel(this);
		}
	}
	
	public void addRow(double values[]) throws DataTableException {
		if(values.length != columns.size()) {
			throw new DataTableException("Row size does not match number of columns.");
		}
		
		for(int i = 0; i < values.length; i++) {
			((List)columns.get(i)).add(new Double(values[i]));
		}
		
		this.numRows++;
		this.fireTableDataChanged();
	}
	
	public double[] getRow(int i) {
		double[] row = new double[columns.size()];
		
		for(int j = 0; j < columns.size(); j++) {
			row[j] = ((Double)((List)columns.get(j)).get(i)).doubleValue();
		}
		return row;
	}
	
	public void removeRow(int i) {
		if(i >= 0 && i < numRows) {
			Iterator iter = columns.iterator();
			while(iter.hasNext()) {
				((DataTableColumn)iter.next()).remove(i);
			}
			numRows--;
			
			fireTableDataChanged();
		}
	}
	
	public void addCol(String name, String units) throws DataTableException {
		if(numRows != 0) throw new DataTableException("Cannot add an empty column to a non-empty table.");
		columns.add(new DataTableColumn(name, units));
		((DataTableColumn)columns.lastElement()).setTableModel(this);
		fireTableStructureChanged();
	}
	
	public void addCol(DataTableColumn col) throws DataTableException {
		//if a column is inserted into a table and it is shorter than the number of rows, it will be padded.
		//if the column is longer, first iterate through the existing columns and pad them.
		//if it is the first column to be added, then no padding needed.
		if(columns.size() != 0 && col.size() != numRows) {
			int diff = java.lang.Math.abs(col.size()-numRows);
			Vector padding = new Vector();
			for(int i = 0; i < diff; i++) {
				padding.add(new Double(defaultVal));
			}
			
			if(col.size() > numRows) {
				Iterator it = columns.iterator();
				while(it.hasNext()) {
					DataTableColumn tempCol = (DataTableColumn)it.next();
					tempCol.add(padding.clone());
				}
			}
			else if(col.size() < numRows){
				col.add(padding.clone());
			}
		}
		
		columns.add(col);
		numRows = col.size();
		((DataTableColumn)columns.lastElement()).setTableModel(this);
		fireTableStructureChanged();
	}
	
	public void addCol(String name, String units, Collection values) throws Exception {
		this.addCol(new DataTableColumn(name, units, values));
	}
	
	public String toString() {
		return tableName;
	}
	
	public DataTableColumn getCol(int i) {
		return (DataTableColumn)columns.get(i);
	}
	
	public void removeCol(int i) {
		if(i >= 0 && i < columns.size()) {
			columns.remove(i);
			fireTableStructureChanged();
		}
	}
	
	public void removeAllColumns() {
		if(columns.size() > 0) {
			columns.removeAllElements();
			fireTableStructureChanged();
		}
	}
	
	public Iterator getColIter() {
		return columns.iterator();
	}
	
	public String[] getDataTableNames() {
		String[] names = new String[columns.size()];
		int i = 0;
		for(i = 0; i < columns.size(); i++) {
			names[i] = ((DataTableColumn)columns.get(i)).getName(); 
		}
		
		return names;
	}
	
	public String[] getDataTableUnits() {
		String[] units = new String[columns.size()];
		int i = 0;
		for(i = 0; i < columns.size(); i++) {
			units[i] = ((DataTableColumn)columns.get(i)).getUnits(); 
		}
		
		return units;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return numRows;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return columns.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int arg0, int arg1) {
		try {
			return ((List)columns.get(arg1)).get(arg0);
		}
		catch (Throwable t){
			return null;
		}
	}

	public String getColumnName(int columnIndex) {
		if(columnIndex < columns.size()) {
			DataTableColumn col = (DataTableColumn)columns.get(columnIndex);
			return col.getName() + " [" + col.getUnits() + "]";
		}
		else {
			return "";
		}
	}
	
	public Class getColumnClass(int columnIndex) {
		if(columnIndex < columns.size()) {
			// Note: is hardcoding this really such a good idea!?
			return Double.class;
		}
		else {
			return null;
		}
	}
	
	public double getDefaultVal() {
		return defaultVal;
	}
	
	public void setDefaultVal(double defaultVal) {
		this.defaultVal = defaultVal;
	}
}
