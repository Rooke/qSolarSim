/*
 * Created on Feb 20, 2005
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
 * @author Michael Rooke
 * 		   Queen's University
 * 
 * Description: keeps track of the length of the current time segment/step
* as well as the time of day</p>
* Notice the GradeManager also has a isRunning boolean.  It is an OR operation
* that determines if we're running (i.e. !isRunning OR SimEnd)
 * 
 * 
 * TODO clean up this business of checkpoints and stopping.  
 */

public class TimeManager{

  public static final int ONE_DAY = 86400;// 86400 = full day of seconds

  private double timeSeg;
  private double timeOfDay; //  seconds since 00:00
  private int date;
  private boolean isRunning;

  private int simLength;
  private int raceEnd;
  private int raceStart;
  private double ctrlCount=0;
  private boolean ctrlFlag = false; // are we at a control point?

  private double secondsSinceStart;

  // Note startTimeOfDay argument must be in the seconds-since-00:00 format
  public TimeManager(int simStart, int simLength, int raceEnd, int raceStart,
                     int newDate) {
    if(newDate>0&&newDate<366){//no leap years!
      date = newDate;

      this.simLength = simLength;
      if (raceEnd > 0 && raceStart > 0
          && raceEnd<=ONE_DAY && raceStart<=ONE_DAY){
        this.raceEnd = raceEnd;
        this.raceStart = raceStart;
      }
    }

    else System.err.println("Bad Arguments in" + this);


    timeOfDay = simStart;
    secondsSinceStart = 0;

    isRunning = true;
  }

  public double getTimeSeg(){
    return timeSeg;
  }
  public double getTimeOfDay(){
    return timeOfDay;
  }
  public int getDate(){
      return date;
    }
    public boolean isRunning(){
      return isRunning;
    }

    public void ctrlPointHit(int ctrlLength){
      isRunning = false;
      ctrlFlag = true;
      ctrlCount = ctrlLength; // an int-to-double cast
    }

  public void updateTM (double distance, double speed) throws SimulationStopException{
    timeOfDay+=timeSeg;
    timeSeg = distance/speed;
    secondsSinceStart+=timeSeg;
    if(!isRunning && ctrlFlag){// if the control point time is up, start running
      ctrlCount-=timeSeg;
      if(ctrlCount<=0) {
      	System.out.println("Control Point Finish: ");
      	isRunning = true;
      	ctrlFlag = false;
      }
    }
    
    // small hack to allow for multi-day sims
    if(timeOfDay<86400 && !ctrlFlag){
    	if((timeOfDay)<(raceStart)||timeOfDay>(raceEnd) ) {
    		if(isRunning!=false){
    			isRunning = false;
    		}
    		}
    	else if (isRunning!=true){
    		isRunning = true;

    		}
    }
    else if (!ctrlFlag){
    	
    	double realTimeOfDay = timeOfDay%86400;
    	if((realTimeOfDay)<(raceStart)||realTimeOfDay>(raceEnd)) {
    		if(isRunning!=false){
    				isRunning = false;
        		}
    		}
    	else if (isRunning==false){
    		isRunning = true;
    		}
    }// end of small hack
    
    
    
    if(secondsSinceStart>simLength) throw new SimulationStopException("Simulation stopped with no errors");
    /*while(timeOfDay>86400){
      timeOfDay-=86400;
      date++;
    }*/ // if this is included, plot overlay of multi-day sims happen
  }
  
  public double getRaceEnd(){
  	return raceEnd;
  }
  public double getRaceStart(){
  	return raceStart;
  }
}


