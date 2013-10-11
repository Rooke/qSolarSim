/*
 * Created on Jun 27, 2005
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

/**
 * @author mrsmiles
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Task extends Status {
	private int progress = 0;
	private boolean isIndeterminate = false;
	
	public Task(String text) {
		super(text);
	}

	public Task(String text, int progress) {
		super(text);
		
		if(progress < 0) {
			this.progress = 0;
		}
		else if(progress > 100) {
			this.progress = 100;
		}
	}
	
	public Task(String text, boolean isIndeterminate) {
		super(text);
		this.isIndeterminate = isIndeterminate;  
	}
	
	public void setProgress(int progress) {
		if(!this.isIndeterminate) {
			if(progress < 0) {
				this.progress = 0;
			}
			else if(progress > 100) {
				this.progress = 100;
			}
			else {
				this.progress = progress;
			}
			
			this.fireStatusEvent("progressUpdated");
		}
	}
	
	public int getProgress() {
		return progress;
	}
	
	public boolean isIndeterminate() {
		return this.isIndeterminate;
	}
}
