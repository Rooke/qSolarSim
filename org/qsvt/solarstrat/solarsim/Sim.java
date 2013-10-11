/*
 * Created on Feb 20, 2005
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
 * @author Michael Rooke
 * 		   Queen's University
 * to use, call setArgs(args) and perform a "Start" action.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Sim {
	// State and threading 
	private volatile SimState state = SimState.STOPPED;
	private Object simLock = new Object(); // Only one simulation may be performed per sim object at any given time
	private Object writeLock = new Object(); // For synchronization of sim output read/write
	
	// Progress junk
	private int progress = 0; // to indicate simulation progress
	private int taskMax = 0; // maximum (total) sim iterrations to be done
	private boolean isDone;
	private int percentDone;
	// linked list of listeners that want to know about this simulation
	private LinkedList listeners = new LinkedList();
	
	// Simulation arguments
	private String classArgs[] = {"11600","100000","28800","64800", "100",
            "20", "40", "1000", "833000,1253000,1500000,1839000," +
            		"2283000,2931000,3333000",
			"1800", "US05Route.txt" };
	
	// checkpoints (by distance along race)
	// Broken Arrow:   833 000
	// Topeka:       1 253 000
	// Omaha:       ~1 500 000
	// Sioux Falls:  1 839 000
	// Fargo:		 2 283 000
	// Brandon:		 2 931 000
	// Regina:	     3 333 000
	// default fake arguments, for testing
    int simStart ;
    int simLength;
    int raceStart;
    int raceEnd;
    int date;
    double speed;
    double SOC;
    double hPower = 0;
    double position;
    int [] ctrlPts;
    int ctrlLength;
    short numOfCtrlPts;
    String filename;
    double hPressure = 0;
  
	
  public Sim() {
  	this.setArgs(classArgs);
  }
  
 
/*
 *  This method really needs to be cleaned up!!
 */
  // Let's see here, args should be as follows:
  // simulation start time (in seconds since 00:00/12:00AM)
  // seconds to simulate
  // race start (again, in seconds since 00:00)
  // race end
  // date (1-365)
  // initial speed
  // initial battery SOC
  // the power level the fuel cell is to operate at
  // starting pressure of hydrogen tank
  // initial position (m from beginning of race)
  // control points (in m) (comma delimited)
  // wait time for control points (in seconds)
  // filename of dist/alt file
  /**
 * @param args
 * @throws Exception
 */
public SimOutput[] simulate()throws Exception{
	
	
	synchronized(simLock) {
		
		state = SimState.RUNNING;
	  GradeManager grade = null;
	
	
	    TimeManager timeObj = new TimeManager(simStart, simLength, raceEnd,
	                                          raceStart, date);
	    try{
	      grade = new GradeManager(filename, position, ctrlPts,
	                                            numOfCtrlPts,
	                                            ctrlLength, timeObj);
	    }
	    catch(SimulationStopException e){
	    	// FIXME
	    	state = SimState.STOPPED;
	    	return new SimOutput[0];
	    }
	    Battery battery = new Battery(SOC, timeObj);
	    Array arrayObj = new Array(timeObj, grade);
	    PowerManager power = new PowerManager(grade);
	    //FuelCell fc = new FuelCell(timeObj);
	
	    //simArray = new double[grade.getSegsLeft()][10];//huge!
	
	    // at 1000 simulation segments, we're already taking up 80K (consec.)
	    // in memory.  Something should be done...?
	
	    // Steps through each simulation step
	    // (step size determined by our road data)
	    // This loop can be ended by a thrown SimulationStopException
	
	    // Try to open file to output to
	    System.out.println("Starting Simulation...");
	    File outputFile = new File("outagain.csv");
	    FileWriter out = null;
	    try{
	      out = new FileWriter(outputFile);
	    }
	    catch (IOException e){
	      System.err.println("problem creating file");
	      state = SimState.STOPPED;
	      return new SimOutput[0];
	    }
	    
	    taskMax = grade.getSegsLeft();
	    progress = 0;
	    percentDone = 0;
	    
	    // initialize SimOutput objects
	    SimOutput[] so = new SimOutput[Units.getNames().length];
	    for(int i = 0; i < so.length; i++) {
	    	so[i] = new SimOutput();
	    	so[i].setProperty("name", Units.getName(i));
	    	so[i].setProperty("unit", Units.getUnit(i));
	    }
	    
	    //MAIN LOOP
	    for(int i = 1;grade.getSegsLeft()>1;i++){
	    	//check progress, send action if we're one more 1/100th the way there
	    	if(percentDone<((int)(i*100/taskMax))){
	    		percentDone = ((int)(i*100/taskMax));
	    		fireProgressEvent();
	    	}
	    	
	      try{
	        grade.updateGrade();
	        timeObj.updateTM(grade.getDistSeg(), speed);
	      }
	      catch(SimulationStopException e){
	      	System.out.println(e.getMessage());
	      	state = SimState.STOPPED;
	        break;
	      }
	      if(timeObj.isRunning()){
	      	double windVector[] = {0,0};
	      	//////////////////////////////
	      	// TODO: get a windspeed profile, and use it here
	        power.updatePower(speed, windVector);
	        arrayObj.updateArray();
	        //hPressure = fc.runIteration(hPower, hPressure);
	        battery.updateSOC(power.getPowerToMotor(),arrayObj.getArrayPower());
	      }
	      else{
	        arrayObj.updateArray();
	        //hPressure = fc.runIteration(hPower, hPressure);
	        battery.updateSOC(0,arrayObj.getArrayPower());
	      }
	
	      /*try{// proceed normally if not at beginning of list*/
	      // Output values
	      synchronized(writeLock) {
	      	so[0].add(new Double(grade.getDistSeg()));
	      	so[1].add(new Double(speed));
	      	so[2].add(new Double(battery.getCurSOC()));
	      	so[3].add(new Double(grade.getGrade()));
	      	so[4].add(new Double(grade.getAlt()));
	      	so[5].add(new Double(battery.getCurrent()));
	      	so[6].add(new Double(power.getPowerToMotor()));
	      	so[7].add(new Double(arrayObj.getArrayPower()*1000));
	      	so[8].add(new Double(timeObj.getTimeOfDay()));
	      	so[9].add(new Double(timeObj.getTimeSeg()));
	      }
	      /*simArray[i][0] = grade.getDistSeg();
	
	      simArray[i][1] = speed;
	      out.write(Double.toString(simArray[i][2] = battery.getCurSOC())+",");
	      out.write(Double.toString(simArray[i][3] = grade.getGrade())+",");
	      out.write(Double.toString(simArray[i][4] = grade.getAlt())+",");
	      simArray[i][5] = battery.getCurrent();
	      out.write(Double.toString(simArray[i][6] = power.getPowerToMotor())+",");
	      out.write(Double.toString(simArray[i][7] = arrayObj.getArrayPower())+",");
	      out.write(Double.toString(simArray[i][7] = timeObj.getTimeOfDay())+"\n");
	      simArray[i][8] = battery.getVoltage();
	      simArray[i][9] = timeObj.getTimeSeg();
	      try{
	      dtm.addRow(simArray[i]);
	      }
	      catch(DataTableException e){
	      	System.out.println(e.getMessage());
	      }*/
	    /*}
	    catch(ArrayIndexOutOfBoundsException e){//beginning/end list case
	      if(grade.getSegsLeft()<=5) break;
	    }*/
	    progress  = i;
	    
	    // Check desired simulation state
	    try {
	    	synchronized(this) {
	        	if(state == SimState.PAUSED) {
	        		wait();
	        	}
	    	}
	    }
	    catch(InterruptedException e) {
	    	System.out.println("Simulation Wait Interrupted!");
	    }
	    
	    // NOTE: by seperating the logic for paused and stopped states, we can handle the case
	    // where an already paused simulation is stopped. 
	    synchronized(this) {
	    	if(state == SimState.STOPPED) {
	    		System.out.println("Simulation Stopped By User");
	    		out.close();
	    		this.fireDoneEvent();
	    		return so;
	    	}
    	}
	  }// END MAIN LOOP
	    
	    System.out.println("Simulation Finished");
	    out.close();
	    //results = dtm;
	    this.fireDoneEvent();
	    state = SimState.STOPPED;
	    
	    return so;
	}
  }// end Simulation method

	public synchronized void stop() {
		state = SimState.STOPPED;
		
		if(state == SimState.PAUSED) {
			notify();
		}
	}
	
	public synchronized void suspend() {
		if(state == SimState.RUNNING) {
			state = SimState.PAUSED;
		}
	}
	
	public synchronized void resume() {
		if(state == SimState.PAUSED) {
			state = SimState.RUNNING;
			notify();
		}
	}
	
  // parse a string (of integers) s using the delimeter c
  // returns in integer array
  // Warning: this was a pointless challenge.
  private static int [] parse(String s, String c){

    StringTokenizer st = new StringTokenizer(s, c);
    int [] rtnArr = new int [st.countTokens()];
     for(int i = 0; st.hasMoreTokens(); i++) {
         rtnArr[i] = Integer.valueOf(st.nextToken()).intValue();
     }
    return rtnArr;
  }

  private static short length(String s, String c){
    StringTokenizer st = new StringTokenizer(s, c);
    return (short)st.countTokens();
  }

  public int getProgress(){
  	return progress;
  }
  public synchronized SimState getState() {
  	return state;
  }
  public int getTaskMax(){
  	return taskMax;
  }
  public int getPercentDone(){
  	return percentDone;
  }
  
  private void fireProgressEvent(){
  	ActionEvent ae = new ActionEvent(this, getPercentDone(), "UpdateSim");
	for(int j = 0; j<listeners.size(); j++){
		((ActionListener)listeners.get(j)).actionPerformed(ae);
	}
  }
  
  private void fireDoneEvent(){
  	ActionEvent ae = new ActionEvent(this, getPercentDone(), "SimDone");
	for(int j = 0; j<listeners.size(); j++){
		((ActionListener)listeners.get(j)).actionPerformed(ae);
	}
  }
  private void fireErrorEvent(String s){
  	ActionEvent ae = new ActionEvent(this, getPercentDone(), s);
	for(int j = 0; j<listeners.size(); j++){
		((ActionListener)listeners.get(j)).actionPerformed(ae);
	}
  }
  
  public boolean isListener(ActionListener al){
  	for(int j = 0; j<listeners.size(); j++){
		if( ((ActionListener)listeners.get(j)).equals(al)){
			return true;
		}
	}
  	return false;
  }
  
  // if some ActionListener wants to listen in, it may add itself manually
  public void addActionListener(ActionListener al){
  	listeners.add(al);
  }
  
  // takes a 2D array, and makes an array of ArrayLists out of it (an object that 
  // implements the AbstractCollection interface)
  private ArrayList[] createAL(double[][] arr){
  	ArrayList al []= new ArrayList[arr.length];
  	for(int i= 0; i<arr.length; i++){
  		System.out.print(i+ ": ");
  		al[i] = new ArrayList(arr[0].length);
  		for(int j= 0; j<arr[0].length; j++){
  			al[i].add(j, new Double(arr[i][j]));
  		}
  	}
  	return al;
  }
  
	// Simulation parameter getters
    public int getParamSimStart(){
    	return simStart;
    }
    public int getParamSimLength(){
    	return simLength;
    } 
    public int getParamRaceStart(){
    	return raceStart;
    } 
    public int getParamRaceEnd(){
    	return raceEnd;
    } 
    public int getParamDate(){
    	return date;
    } 
    public double getParamSpeed(){
    	return speed;
    }
    public double getParamSOC(){
    	return SOC;
    } 
    public double getParamHPower(){
    	return hPower;
    }
    public double getParamPosition(){
    	return position;
    } 
    public int [] getParamCtrlPts(){
    	return ctrlPts;
    } 
    public int getParamCtrlLength(){
    	return ctrlLength;
    } 
    public String getParamFilename(){
    	return filename;
    } 
    public double getParamHPressure(){
    	return hPressure;
    } 
    public String [] getClassArgs(){
    	return classArgs;
    }
    // Simulation parameter setters
    public void setParamSimStart(int arg0){
    	simStart = arg0;
    }
    public void setParamSimLength(int arg0){
    	simLength = arg0;
    } 
    public void setParamRaceStart(int arg0){
    	raceStart = arg0;
    } 
    public void setParamRaceEnd(int arg0){
    	raceEnd = arg0;
    } 
    public void setParamDate(int arg0){
    	date = arg0;
    } 
    public void setParamSpeed(double arg0){
    	speed = arg0;
    }
    public void setParamSOC(double arg0){
    	SOC = arg0;
    } 
    public void setParamHPower(double arg0){
    	hPower = arg0;
    }
    public void setParamPosition(double arg0){
    	position = arg0;
    } 
    public void setParamCtrlPts(int [] arg0){
    	ctrlPts = arg0;
    } 
    public void setParamCtrlLength(int arg0){
    	ctrlLength = arg0;
    } 
    public void setParamFilename(String arg0){
    	filename = arg0;
    } 
    public void setParamHPressure(double arg0){
    	hPressure = arg0;
    }

	  public void setArgs (String args[]){
	  	try{
    	classArgs = args;
        simStart = Integer.valueOf(args[0]).intValue();
        simLength = Integer.valueOf(args[1]).intValue();
        raceStart = Integer.valueOf(args[2]).intValue();
        raceEnd = Integer.valueOf(args[3]).intValue();
        date = Integer.valueOf(args[4]).intValue();
        speed = Double.valueOf(args[5]).doubleValue();
        SOC = Double.valueOf(args[6]).doubleValue();
 
        position = Double.valueOf(args[7]).doubleValue();
        ctrlPts = parse(args[8], ",");
        numOfCtrlPts = length(args[8], ",");
        ctrlLength = Integer.valueOf(args[9]).intValue();
        filename = args[10];
        
	  	}
	  	catch(ArrayIndexOutOfBoundsException e){
	  		this.fireErrorEvent("Warning: not all possible input args updated");
	  	}
      
      }
	  
	  //e.g...
	  /*
	   * 
	   * sim.setParamSimStart(arg[0]);
		sim.setParamSimLength(arg[1]);
		sim.setParamRaceStart(arg[2]);
		sim.setParamRaceEnd(arg[3]);
		sim.setParamDate(arg[4]);
		sim.setParamSpeed(arg[5]);
		sim.setParamSOC(arg[6]);

		sim.setParamPosition(arg[8]);
		sim.setParamCtrlPts(arg[9]);
		sim.setParamCtrlLength(arg[10]);
	
	   */

}
