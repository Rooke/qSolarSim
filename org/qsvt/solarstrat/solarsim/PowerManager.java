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
 * 
 * Description: Given a speed and a grade (given by the GradeManager object)
*  the power is calculated based on the Kyle-Chester equation
* 
* TODO  Major:  put in new particular car data
* 		Minor:  recode interpolate method - it's of poor quality
 * 
 */
public class PowerManager {


	
// PARTICULAR CAR DATA //////////////////////////////////////////////////////////
	  private final double rho = 1.20;
	  private final double g = 9.80;
	  private final double mass = 400; // in kg
	  private final double cr1 = 0.0048;
	  private final double cdA = 0.1700;
	  private final double tireRadius = 0.257; //in m
	  private final double tireDiam = 0.514; 
	  private final double regenEff = 0.50;  
	  private GradeManager grade;
	  private double power;
	  private double force;
	  private final double [][] effLUT ={{0.500, 0.600, 0.600, 0.500, 0.450, 0.400},
	                                 {0.670, 0.792, 0.772, 0.710, 0.650, 0.600},
	                                 {0.720, 0.855, 0.851, 0.820, 0.770, 0.700},
	                                 {0.740, 0.885, 0.888, 0.855, 0.805, 0.740},
	                                 {0.770, 0.905, 0.897, 0.880, 0.850, 0.780},
	                                 {0.800, 0.923, 0.917, 0.900, 0.860, 0.750},
	                                 {0.810, 0.906, 0.920, 0.904, 0.850, 0.730}};
///////////////////////////////////////////////////////////////////////////////////
	  
	  
	  // Constructor
	  public PowerManager(GradeManager grade) {
	    this.grade = grade;
	  }

	  	/**
		 * @param v - velocity of car, in m/s (really it's a scalar)
		 * @param wv - wind velocity: [0] direction in radians from north (0=2pi=due north)
		 *								[1] magnitude in m/s
		 **/								 
	  public void updatePower(double v, double [] wv){
	  	if(wv[1]!=0){//NOT the zero vector
	  		double windCpnt = wv[1]*Math.cos(grade.getCarAngle()-wv[0]);
	  		v += windCpnt;
	  	}
	    //A form of the Chester Kyle Equation.  Slopes and drag calculated
	    force = 0.5*cdA*rho*Math.pow(v ,2) +
	        mass*g*(Math.atan(grade.getGrade())) +
	        cr1*Math.cos(Math.atan(grade.getGrade()));
	    if(force>0) power = force*v/motorEff(v, force*tireRadius);
	    else power = force*v*regenEff;
	  }

	  public double getPowerToMotor() {
		  if(!grade.isRunning()){
			  return 0;
		  }
	    return power;
	  }

	  public double getForce() {
	    return force;
	  }

	  private double motorEff(double v, double torque){
	    //See http://www.math.ucla.edu/~baker/java/hoefer/Lagrangesrc.htm ?
	    // Calculates the row that the RPM is called from.
	    // The 0.3 comes from (60s / Table Spacing)
	    double row = v*0.3/(Math.PI*tireDiam);
	    // I'm guessing this 10 also has something to do with table spacing
	    double column = torque / 10;
	    return interpolate(row, column);
	  }

	  
	  // interpolate method
	  // Given type double indicies, linarly interpolate along out Efficiency LUT.
	  private double interpolate(double row, double column){
	    int intRow = (int)row;
	    int intColumn = (int)column;
	    if(column>5)intColumn=5;
	    if(column<0)intColumn=0;
	    if(row>6)intRow=6;
	    if(row<0)intRow=0;

	    double product1, product2;

	    try{
	      product1 = (row-intRow)*(effLUT[intRow+1][intColumn]-effLUT[intRow][intColumn])
	          + effLUT[intRow][intColumn];
	    }
	    catch(ArrayIndexOutOfBoundsException i){// at max row
	      product1 = effLUT[intRow][intColumn];
	    }

	    try{
	      product2 = (column-intColumn)*(effLUT[intRow][intColumn+1]-effLUT[intRow][intColumn])
	          +effLUT[intRow][intColumn];
	    }
	    catch(ArrayIndexOutOfBoundsException i){// at max column
	      product2 = effLUT[intRow][intColumn];
	    }

	    return (product1 + product2)/2;
	  }


	}

