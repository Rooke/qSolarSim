/*
 * Created on May 17, 2005
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
package org.qsvt.solarstrat.solarsim;

/**
 * @author Mike Rooke
 *
 * really cheesy encapsulation of units and names.
 */
public final class Units {
	
	private static String[] units ={"m","m/s","Ah","m/m","m","A","W","kW","s","s"};
	private static String[] names ={"distSeg", "speed", "BattSOC", "Grade", "Altitude", 
									"Current", "PowerOutput", "ArrayPower", "TimeOfDay", "TimeSeg"};
	
	public static void setUnits(String[] s){
		units = s;
	}
	
	public static String[] getUnits(){
		return units;
	}
	public static String[] getNames(){
		return names;
	}
	public static String getUnit(int i){
		if(i<units.length){
			return units[i];
		}
		else return "";
	}
	public static String getName(int i){
		if(i<units.length){
			return names[i];
		}
		else return "";
	}
	

}
