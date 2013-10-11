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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Michael Rooke
 * 		   Queen's University
 * 
 */
public class TestSim {
	
	  //Main method
	  public static void main(String[] args) {
	   /* try {
	      String[] str = {"21600","54000","28800","64800", "200",
	                                    "20", "40", "200", "3000", "1000", "500,1000",
	                                    "3600", "US(map).txt" };
	      Sim simObj = new Sim();
	       simObj.simulate(str);
	    }
	    catch(Exception e) {
	      e.printStackTrace();
	    }
	   
	    /*
	     * 
	     */
	  	File outputFile = new File("test.txt");
  	    FileWriter out = null;
  	    try{
  	      out = new FileWriter(outputFile);
  	    }
  	    catch (IOException e){
  	      System.err.println("problem creating file");
  	    }
  	    try{
	  	for(double i = 0; i<=Math.PI*2; i+=0.01){
	  		SunAngle sa = new SunAngle("C:\\Documents and Settings\\2mhr\\Desktop\\eclipse\\workspace\\solarsim\\org\\qsvt\\solarstrat\\solarsim\\Gemini Array.raw");
	  		double []  temp = {0,Math.cos(i)*30, Math.sin(i)*30}; 
	  		
	  		out.write(Double.toString(sa.PanelData[0][0])+"\n");
	  		out.write(Double.toString(sa.calcNorm(temp))+"\n");
	  		out.write(Double.toString(sa.calcNorm(temp))+"\n");
	  		out.write(Double.toString(sa.calcNorm(temp))+"\n");
	  		out.write(Double.toString(sa.calcNorm(temp))+"\n");
	  	  out.write(Double.toString(sa.calcNorm(temp))+"\n");
	  	    
	  	}
  	    }
  	    catch(IOException e){
  	    	
  	    }
	  }
	  	
	  	
}
