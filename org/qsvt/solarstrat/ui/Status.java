/*
 * Created on May 29, 2005
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

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.qsvt.solarstrat.ui.event.StatusEvent;
import org.qsvt.solarstrat.ui.event.StatusEventListener;

/**
 * @author mrsmiles
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Status {
	protected String text;
	protected List statusListeners = new Vector();

	public Status(String text) {
		this.text = text;
	}
	
	public Status(String text, StatusPriority priority) {
		this.text = text;
	}
	
	public void addStatusEventListener(StatusEventListener sl) {
		statusListeners.add(sl);
	}
	
	public boolean removeStatusEventListener(StatusEventListener sl) {
		return statusListeners.remove(sl);
	}
	
	public void fireStatusEvent(String eventName) {
		Iterator iter = statusListeners.iterator();
		StatusEvent se = new StatusEvent(this, eventName);
		while(iter.hasNext()) {
			((StatusEventListener)iter.next()).statusEventPerformed(se);
		}
	}
	/**
	 * @return Returns the text.
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text The text to set.
	 */
	public void setText(String text) {
		this.text = text;
		this.fireStatusEvent("textChanged");
	}
	
	public String toString() {
		return text;
	}
}
