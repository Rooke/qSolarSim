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
 * <p>Title: Fuel Cell Simulation</p>
 * <p>Description: Simulates the fuel cell based on decreasing pressure in the fuel tank
 * requires "Fuel Cell Data.txt" a tab ("\t") delimited file containg: output power, fuel flow rate
 * (mole/sec.), efficiency.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: QSVT</p>
 * @author Robert Clarke
 * @version 1.0
 * 
 * Note by M.Rooke(Feb.05):  This class is defunct.  We no longer plan to have a fuel cell in the car.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

public class FuelCell{

    private static int tankVolume = 4; //Volume of storage tank (L)
    private static double R = 1.20591; //universal gas constant (L*psi)/(mol*K) - note the conversion from standard L*atm/mol*K notation
    private static double tankTemp = 314; //temperature of fuel tank (Kelvin)
    private int dataPoints;

    private double  tankPressure; //get current tank pressure (before this iteration)

    //this file will be created initally from theoritical calculations, but needs to be hard numbers from tests
    //to be conducted with actual FC on lab bench (hopefully before christmas)
    File flowRateFile = new File("U:\\Users\\Clarke\\Fuel Cell Sim\\Fuel Cell Data.txt"); //file containing data for fuel cell


    private double[] power;
    private double[] flowRate;
    private double[] efficiency;

    private TimeManager time;


   public FuelCell(TimeManager time){
    //import fuel cell flow rate data
            if(ImportData(flowRateFile)){
                JOptionPane.showMessageDialog(null,"Fuel Cell Data Imported successfully!");
            }else{
                JOptionPane.showMessageDialog(null,"UNSUCCESSFUL Fuel Cell Data Import!");
            }
     this.time = time;
    }//end FuelCell constructor


    /************************************************************************
    * public double runIteration (double power, double pressure):           *
    *                                                                       *
    * Main method to call to simulate fuel cell for each iterition, accepts *
    * power:(the power you want the fuel cell to output, must be            *
    * between 0 - 600 W) and specified in W                                 *
    * pressure: the current tank pressure before the start of this iteration*
    * (supplied in psi)                                                     *
    *                                                                       *
    * returns: new tank pressure after this iteration operating at         *
    * specified power                                                       *
    *************************************************************************/
    public double runIteration (double power, double pressure){
        double pressureDrop = 0;
        double  molesConsumed = 0; //moles of h2 consumed this iteration

        tankPressure = pressure;
        double simInterval = time.getTimeSeg();  //get simInterval

        molesConsumed = simInterval*interpolateFlowRate(power);
        pressureDrop = (molesConsumed*R*tankTemp)/tankVolume; //decrase in tank pressure during interval
        tankPressure -= pressureDrop; //new pressure of tank

        if(tankPressure > 0){ //somthing is left in tank
            return tankPressure;
        }else{
            return 0; //tank is empty
        }
    }//end runIteration

    private double interpolateFlowRate(double targetPower){
        int check = (dataPoints-1)/2;
        int endPoint = dataPoints;
        int startPoint = 0;

        //find flow rate using log(n) algorithm
        try{
            while(power[check] != targetPower){
                if((power.length != check+1) && (power[check] < targetPower && power[check+1] > targetPower)){
                    break; //exact power value not in data table, must interpolate to find
                }
                if(targetPower > power[check]){
                    if(targetPower > power[(power.length)-1]){ //target power is more than last data point
                        JOptionPane.showMessageDialog(null,"Fuel Cell power out of bounds, assuming MAX power!");
                        return flowRate[(power.length)-1]; //so give data for last data point
                    }
                    startPoint = check;
                    check = (endPoint - startPoint)/2 + check;
                }
                if(targetPower < power[check]){
                    if(targetPower < power[0]){ //target power is less than first data point
                        JOptionPane.showMessageDialog(null,"Fuel Cell power out of bounds, assuming MIN power!");
                        return flowRate[0]; //so give data for first data point
                    }
                    endPoint = check;
                    check = (endPoint - startPoint)/2;
                }
            }
            if(targetPower == power[check]){ //exact value is located in table, so use it
                return flowRate[check];
            }else{ //value doesn't exist in table, interporalte to get closest approximation
            //how this is done is wrong ..... need proper algorithm
                return (((power[check+1]-targetPower)*flowRate[check+1])+((targetPower-power[check])*flowRate[check]))/(power[check+1]+power[check]);
            }
        }catch(ArrayIndexOutOfBoundsException e){
            JOptionPane.showMessageDialog(null,"Error in Fuel Cell Module, array out of bounds in inteperlate Flow Rate!  This data is invlid");
            return -999;
        }
    }//end inteperlateFlowRate




    private boolean ImportData(File file){
        try{  //...load the data
            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedReader counter = new BufferedReader(new FileReader(file));
            String inputLine = reader.readLine();
            String count = counter.readLine();

            dataPoints = 0;
            while(count != null){ //count the number of data points in the file
                dataPoints++;
                count = counter.readLine();
            }

            power = new double[dataPoints];
            flowRate = new double[dataPoints];
            efficiency = new double[dataPoints];

            int i = 0;
            while(inputLine != null){//at end of file
               String results[] = inputLine.split("\t");
               power[i] = Double.parseDouble(results[0].trim());
               flowRate[i] = Double.parseDouble(results[1].trim());
               efficiency[i] = Double.parseDouble(results[2].trim());
               inputLine = reader.readLine();//get the next line
               i++;
            }
        }catch (FileNotFoundException e){
            return false;
        }catch(IOException e){
            return false;
        }catch(NumberFormatException e){
            System.out.println("Number Format Exception");
            return false;
        }//end try/catch block
        return true;
    }//end private ImportData*/

    //return current tank pressure
    public double getTankPressure (){
        return tankPressure;
    }//end getTankPressure
}//end FuelCell class


