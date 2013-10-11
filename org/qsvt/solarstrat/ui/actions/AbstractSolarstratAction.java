/*
 * Created on May 20, 2005
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
package org.qsvt.solarstrat.ui.actions;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.qsvt.solarstrat.ui.SolarstratMainWindow;

/**
 * @author Eyrak Paen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractSolarstratAction extends AbstractAction {
	private SolarstratMainWindow wnd;
	
	/**
	 * 
	 */
	public AbstractSolarstratAction(SolarstratMainWindow wnd) {
		super();
		this.wnd = wnd;
	}

	/**
	 * @param arg0
	 */
	public AbstractSolarstratAction(String arg0, SolarstratMainWindow wnd) {
		super(arg0);
		this.wnd = wnd;
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public AbstractSolarstratAction(String arg0, Icon arg1, SolarstratMainWindow wnd) {
		super(arg0, arg1);
		this.wnd = wnd;
	}

	/**
	 * @return Returns the wnd.
	 */
	public SolarstratMainWindow getWnd() {
		return wnd;
	}
	/**
	 * @param wnd The wnd to set.
	 */
	public void setWnd(SolarstratMainWindow wnd) {
		this.wnd = wnd;
	}
}
