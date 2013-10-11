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
import java.util.Vector;

/**
 * @author Eyrak Paen
 *
 * TODO Documentation!
 */
public class DataTableColumn extends Vector {
	private DataTableModel tableModel = null;
	private String name = "";
	private String units = "";
	
	public DataTableColumn() {
		name = units = "";
	}
	
	public DataTableColumn(String name, String units) {
		this.name = new String(name);
		this.units = new String(units);
	}
	
	public DataTableColumn(String name, String units, Collection col) {
		super(col);
		this.name = new String(name);
		this.units = new String(units);
	}

	/**
	 * @return Returns the tableModel.
	 */
	public DataTableModel getTableModel() {
		return tableModel;
	}
	/**
	 * @param tableModel The tableModel to set.
	 */
	public void setTableModel(DataTableModel tableModel) {
		this.tableModel = tableModel;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
		if(tableModel != null) tableModel.fireTableStructureChanged();
	}
	/**
	 * @return Returns the units.
	 */
	public String getUnits() {
		return units;
	}
	/**
	 * @param units The units to set.
	 */
	public void setUnits(String units) {
		this.units = units;
		if(tableModel != null) tableModel.fireTableStructureChanged();
	}
	
	public Object clone() {
		DataTableColumn col = (DataTableColumn)super.clone();
		col.setName(name);
		col.setUnits(units);
		col.setTableModel(tableModel);
		return col;
	}
	public String toString(){
		return name;
	}
	/* (non-Javadoc)
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void add(int arg0, Object arg1) {
		super.add(arg0, arg1);
		if(tableModel != null) tableModel.fireTableDataChanged();
	}
	/* (non-Javadoc)
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	public synchronized boolean add(Object arg0) {
		boolean result = super.add(arg0);
		if(tableModel != null) tableModel.fireTableDataChanged();
		return result;
	}
	/* (non-Javadoc)
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	public synchronized boolean addAll(Collection arg0) {
		boolean result = super.addAll(arg0);
		if(tableModel != null) tableModel.fireTableDataChanged();
		return result;
	}
	/* (non-Javadoc)
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	public synchronized boolean addAll(int arg0, Collection arg1) {
		boolean result = super.addAll(arg0, arg1);
		if(tableModel != null) tableModel.fireTableDataChanged();
		return result;
	}
	/* (non-Javadoc)
	 * @see java.util.Vector#addElement(java.lang.Object)
	 */
	public synchronized void addElement(Object arg0) {
		super.addElement(arg0);
		if(tableModel != null) tableModel.fireTableDataChanged();
	}
	/* (non-Javadoc)
	 * @see java.util.Collection#clear()
	 */
	public void clear() {
		super.clear();
		if(tableModel != null) tableModel.fireTableDataChanged();
	}
	/* (non-Javadoc)
	 * @see java.util.Vector#insertElementAt(java.lang.Object, int)
	 */
	public synchronized void insertElementAt(Object arg0, int arg1) {
		super.insertElementAt(arg0, arg1);
		if(tableModel != null) tableModel.fireTableDataChanged();
	}
	/* (non-Javadoc)
	 * @see java.util.List#remove(int)
	 */
	public synchronized Object remove(int arg0) {
		Object result = super.remove(arg0);
		if(tableModel != null) tableModel.fireTableDataChanged();
		return result;
	}
	/* (non-Javadoc)
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	public boolean remove(Object arg0) {
		boolean result = super.remove(arg0);
		if(tableModel != null) tableModel.fireTableDataChanged();
		return result;
	}
	/* (non-Javadoc)
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	public synchronized boolean removeAll(Collection arg0) {
		boolean result = super.removeAll(arg0);
		if(tableModel != null) tableModel.fireTableDataChanged();
		return result;
	}
	/* (non-Javadoc)
	 * @see java.util.Vector#removeAllElements()
	 */
	public synchronized void removeAllElements() {
		super.removeAllElements();
		if(tableModel != null) tableModel.fireTableDataChanged();
	}
	/* (non-Javadoc)
	 * @see java.util.Vector#removeElement(java.lang.Object)
	 */
	public synchronized boolean removeElement(Object arg0) {
		boolean result = super.removeElement(arg0);
		if(tableModel != null) tableModel.fireTableDataChanged();
		return result;
	}
	/* (non-Javadoc)
	 * @see java.util.Vector#removeElementAt(int)
	 */
	public synchronized void removeElementAt(int arg0) {
		super.removeElementAt(arg0);
		if(tableModel != null) tableModel.fireTableDataChanged();
	}
	/* (non-Javadoc)
	 * @see java.util.AbstractList#removeRange(int, int)
	 */
	protected void removeRange(int arg0, int arg1) {
		super.removeRange(arg0, arg1);
		if(tableModel != null) tableModel.fireTableDataChanged();
	}
	/* (non-Javadoc)
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	public synchronized Object set(int arg0, Object arg1) {
		Object result = super.set(arg0, arg1);
		if(tableModel != null) tableModel.fireTableDataChanged();
		return result;
	}
	/* (non-Javadoc)
	 * @see java.util.Vector#setElementAt(java.lang.Object, int)
	 */
	public synchronized void setElementAt(Object arg0, int arg1) {
		super.setElementAt(arg0, arg1);
		if(tableModel != null) tableModel.fireTableDataChanged();
	}
}
