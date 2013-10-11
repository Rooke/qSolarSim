/* 
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
 *
 * Created on Feb 20, 2005
 * (Actually created Aug. '04)
 * Simulates the power output of a flat, square solar panel given the time of day,
 * the date, and the location on the earth.
 * Fudging with this model is encouraged to fit real results.
 * 
 */
package org.qsvt.solarstrat.solarsim;


/**
 * 
 * <p>A class to calculate the power (in Watts) of a simulated solar array,
 * given the time of day (provided by <code>{@link org.qsvt.solarstrat.solarsim.TimeManager}</code>),
 * position on the earth (provided by <code>{@link org.qsvt.solarstrat.solarsim.GradeManager}</code>), and
 * the shape of the array (provided by <code>{@link org.qsvt.solarstrat.solarsim.SunAngle}</code>, not yet working).
 * </p>
 * 
 * <p>Without the use of <code>SunAngle</code>, the array is simulated as a
 * flat square, parallel to the road (ground);  when the car is stopped, it is
 * assumed the array is pointed at the sun. </p>
 * 
 * @author Michael Rooke
 * 		   QSVT
 * 		   Queen's University
 *
 * 
 */
public class Array {
	  private TimeManager time;
	  private double arrayPower;
	  private GradeManager grade;
	  private final int Irrad = 8000; // Solar Irradiation / square metre
	  //private SunAngle array;

	  public Array(TimeManager time, GradeManager grade) {
	    this.grade = grade;
	    this.time = time;
	    //this.array = new SunAngle("Gemini Array.raw");
	  }
	  /** 
	   * Recalculates the power given by the array using
	   * <code>TimeManager.getTimeOfDay()</code>, and position values in
	   * <code>GradeManager</code>
	   *
	   */
	  public void updateArray(){
	    /*big long calculation including..*/
	  	if(time!=null){
	  		double SSM = time.getTimeOfDay()%TimeManager.ONE_DAY;
	  		int dn = time.getDate();
	  		double fi = grade.getLat();
	  		double delta, omega, omega_s, a, b, rd, rg, rsource, rtot;
	  		
	  		
	  		delta = 23.45 * Math.sin( (dn-81)*2*Math.PI/365) * Math.PI/180;    //Calculates the angle of the sun on any given day of the year.
	  		
	  		omega = ((SSM-(43200))/86400)*2*Math.PI;  //Calculates the hour angle(units mean seconds must be used)
	  		
	  	    double temp = Math.abs(Math.tan(delta)*Math.tan(fi*Math.PI/180));
	  	    while(temp>Math.PI){
	  	    	temp-=Math.PI*2;
	  	    }
	  	    omega_s = -(1.2)*Math.acos(temp);//Calculates the angle of the sun at sunset(hemisphere specific)
	  		
	  		a=0.409-0.5016*Math.sin(omega_s + 1.1047);          //A fudge factor (I don't know what it means but it works)
	  		b=0.6609 + 0.4767*Math.sin(omega_s + 1.1047);     //A fudge factor (I don't know what it means but it works)
	  		
	  		//The next two calculate the approximate ratidation from direct and reflected light
	  		rd=(Math.PI/86400)*(Math.cos(omega)-Math.cos(omega_s))/(omega_s*Math.cos(omega_s)-Math.sin(omega_s));
	  		rg=(Math.PI/86400)*(a+b*Math.cos(omega))*(Math.cos(omega)-Math.cos(omega_s))/(omega_s*Math.cos(omega_s) - Math.sin(omega_s));
	  		
	  		rsource = rd + rg;      //Sums the two sources of radiation
	  		rtot = rsource * Irrad; //Scales the incedent radiation by a site specific index
	  		
	  		arrayPower = rtot * 2.75;    //Scales the radiation on the area covered by the array to the output of the array
	  		//int [] sun = {whatever direction the sun is in}
	  		//arrayPower = rtot * array.calcNorm();
	  		if (arrayPower <0) arrayPower = 0;  //There is no such thing as negative array power...
	  		
	  		//Additional power due to pointing the array at the sun.
	  		if(!time.isRunning()){// If the car is not driving...
	  			double alfa = Math.asin(Math.sin(delta) * Math.sin(fi) + Math.cos(delta) * Math.cos(fi) * Math.cos(omega));   // Find sun angle.
	  			System.out.println(alfa);
	  			arrayPower = arrayPower +1.7 * arrayPower * Math.pow((Math.cos(alfa)),7);   // Find equivelent power (this is an emperical fit to data a fourier analysis may be performed to produce a more accurate model in the future).
	  		}
	  	}

	  }
	  /** 
	   * 
	   * @return The power of the array last calculated by </code>updateArray</code>
	   */
	  public double getArrayPower(){
	    return arrayPower;
	  }
	}
	/*Original Toban Labview Code:

	 float delta, omega, omega_s, a, b, rd, rg, rsource, rtot;


	 delta = PI*23.45*sin((dn-81)*2*PI/365)/180;    //Calculates the angle of the sun on any given day of the year.

	 omega = ((SSM-(offset+43200))/86400)*2*PI;  //Calculates the hour angle(units mean seconds must be used)
	 omega_s = -acos(-tan(delta)*tan(fi));                 //Calculates the angle of the sun at sunset(hemisphere specific)

	 a=0.409-0.5016*sin(omega_s + 1.1047);          //A fudge factor (I don't know what it means but it works)
	 b=0.6609 + 0.4767*sin(omega_s + 1.1047);     //A fudge factor (I don't know what it means but it works)

//	The next two calculate the approximate ratidation from direct and reflected light
	 rd=(PI/86400)*(cos(omega)-cos(omega_s))/(omega_s*cos(omega_s)-sin(omega_s));
	 rg=(PI/86400)*(a+b*cos(omega))*(cos(omega)-cos(omega_s))/(omega_s*cos(omega_s) - sin(omega_s));

	 rsource = rd + rg;      //Sums the two sources of radiation
	 rtot = rsource * Irrad; //Scales the incedent radiation by a site specific index

	 arraypower = rtot * 0.75;    //Scales the radiation on the area covered by the array to the output of the array

	 if (arraypower <0) arraypower = 0;  //There is no such thing as negative array power...

//	Additional power due to pointing the array at the sun.
	alfa = asin(sin(delta) * sin(fi) + cos(delta) * cos(fi) * cos(omega));   // Find sun angle.
	arraypower = arraypower +2.7 * arraypower * (cos(alfa))**7;   // Find equivelent power (this is an emperical fit to data a fourier analysis may be performed to produce a more accurate model in the future).


	*/
