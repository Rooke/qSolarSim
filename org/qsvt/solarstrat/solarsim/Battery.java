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
 * 		   QSVT
 * 		   Queen's University
 * 
 */
/**
 * Description:  Given an initial state of this simulated battery, 
 * passed through the constructor, this class does
 * some simple calculations, based on an electrical current. 
 * (the 'Simulated Battery' in this case is one with a
 * max State Of Charge (SOC) of 80Ah and with a cellular 
 * (meaning one cell or indivisible battery unit) internal 
 * ressitance of 0.19 Ohms)
 * 
 * TODO:  implement a LUT, or simulated model.  At least one is required
 */

public class Battery {

  private double SOC;
  private final double MAX_SOC = 80;//? in Ah
  private final double MIN_SOC = 0;
  private double batteryCurrent;
  private double totalBatVoltage;
  private TimeManager time;
  private double floatVoltage;
  private final double INTERNAL_RESISTANCE = 0.19;



  public Battery(double SOCIC, TimeManager time) {
    this.time = time;
    this.SOC = SOCIC;
    batteryCurrent = 0;
    updateFloatVoltage(SOC);
    totalBatVoltage = floatVoltage;
  }

  public void updateSOC(double powerToMotor, double arrayPower){
    // Calculate everything
  	
  	// total voltage can be calculated two ways:
  	// a look-up table or a battery model
    updateFloatVoltage(SOC);
    batteryCurrent = (floatVoltage-Math.sqrt(
    		Math.pow(floatVoltage, 2) - 4*INTERNAL_RESISTANCE*(arrayPower-powerToMotor)))
               /(2*INTERNAL_RESISTANCE);
    SOC+=batteryCurrent*time.getTimeSeg()/3600;//"integrate" current over time
    totalBatVoltage = floatVoltage + INTERNAL_RESISTANCE*batteryCurrent
        + polarizationVoltage();
    if(SOC>MAX_SOC) SOC = MAX_SOC;
    if(SOC<MIN_SOC) SOC = MIN_SOC;
  }

  public double getCurSOC(){
    return SOC;
  }

  public double getCurrent(){
    return batteryCurrent;
  }

  public double getVoltage(){
    return totalBatVoltage;
  }

  // floatVoltage method
  // This finds the float voltage for an entire string of cells.
  private void updateFloatVoltage(double SoC){
   floatVoltage=98.489+1.9148*SoC-0.2413*(Math.pow(SoC, 2))+
        0.0147*(Math.pow(SoC, 3))-0.0004*(Math.pow(SoC, 4))+
        7e-6*(Math.pow(SoC, 5))-(4e-8)*(Math.pow(SoC, 6));
  }

  private double polarizationVoltage(){
    return 0;// incomplete
  }
}


