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
 */

/*
* <p>Title: GradeManager class</p>
* <p>Description: Does the dirty work of figuring out the current road grade.
*    Simulation resolution is determined by the file data's resolution. To
*    decrease the simulation's resolution, just periodically skip a data
*    point.
*     One other thing I thought of - we need the position on the earth, hence
*     we need to incldue lat, lon data</p>
* Use: depending on which constructor is called, this class can serve two
* purposes.  One is in simulation: it just reads in a file, after which
* the updateGrade method steps through the file.  The other use is theoretically
* during the race:  a type 'GPS' identifier is sent to the constructor, and updateGrade
*  spits out a calculation based on two really quick readings of the GPS
* position.  This helps to eleminate the really bad altitude accuracy in the
*  GPS (or does it?).  This is not very important during the race, and if this
* way of doing things doesn't work, one can simply compare one's current
* position to the roadData, and get the grade from there.
*
* The roadData array should be as follows:
* distance FROM START!! in m
* Altitude FROM START in m
* latitude in deg (not the x100 form the GPS gives - this is important)
* longitude in deg
*
* NB: I suppose index+segsLeft should (always) equal the array length
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;


public class GradeManager {
/*
 * Distance / Grade stuff
 */
 private double grade; // the main reason for this class

 private String filename;
 private int index;
 private double[][] roadData;
 private final short distIndex = 0;
 private final short altIndex = 1;
 private final short latIndex = 2;
 private short lonIndex = 3;
 private int segsLeft;
 private double carAngle;


/*
 * Time stuff
 */
 //private GPS gps;
 private TimeManager time;
 private boolean realtime;
 private int ctrlIndex;
 private int ctrlTimeLength;
 private int [] ctrlPts;
 private short ctrlSize;

//Constructor
 public GradeManager (String filename, double startPos, int [] ctrlPts,
                     short ctrlSize, int ctrlTimeLength, TimeManager time)
throws SimulationStopException{
   this.filename = filename;
   this.ctrlTimeLength = ctrlTimeLength;
   this.ctrlPts = ctrlPts;
   this.ctrlSize = ctrlSize;
   this.time = time;
   ctrlIndex = 0;
   realtime = false;
   segsLeft = 0;

   try{processFile(startPos);}
   catch(SimulationStopException e){throw e;}

 }// end constructor

 public GradeManager(/*GPS gps*/){
   //this.gps = gps;
   realtime = true;
 }

 //
 private void processFile (double start)throws SimulationStopException {

   File inFile  = new File("US05Route.txt");  // read from file specified
   index = 0;

   double product = (6378100)*Math.PI/18000; // a constant for finding distance
                                             // between two points

   try {
     BufferedReader bufRdr  = new BufferedReader(new FileReader(inFile));
     String line = null;
     int i = 0;
     while ((line = bufRdr.readLine())!= null) i++;
     roadData = new double [4][i];
     bufRdr.close();
     line = null;

     bufRdr  = new BufferedReader(new FileReader(inFile));
     while ((line = bufRdr.readLine()) != null){
       StringTokenizer st = new StringTokenizer(line, "\t");
       roadData [latIndex][segsLeft] =
            Double.valueOf(st.nextToken()).doubleValue();//lat
          roadData[lonIndex][segsLeft] =
              Double.valueOf(st.nextToken()).doubleValue(); //lon
          roadData[distIndex][segsLeft] =
              Double.valueOf(st.nextToken()).doubleValue(); //dist	//Note:  multiplcation by 1000 
          															//       here causes a bug.
          roadData[altIndex][segsLeft] =
              Double.valueOf(st.nextToken()).doubleValue(); // alt
          segsLeft++;
     }





 /* Next block is for raw GPS data
  *
  *
  *
  *
  *
     // the first while loop will catch the first instance of the POS
     // line.  This only defines the first (zeroith) element of the roadData.
     bufRdr  = new BufferedReader(new FileReader(inFile));
     while ((line = bufRdr.readLine()) != null && segsLeft==0){
       StringTokenizer st = new StringTokenizer(line, ",");
       System.out.println(line + st.nextToken());
       if(st.nextToken().equals("POS")){
         System.out.print(st.nextToken());
         st.nextToken();
         st.nextToken();
         try{
           roadData [latIndex][segsLeft] =
             Double.valueOf(st.nextToken()).doubleValue();//lat
           st.nextToken();
           roadData[lonIndex][segsLeft] =
               Double.valueOf(st.nextToken()).doubleValue(); //lon
           st.nextToken();
           roadData[altIndex][segsLeft] =
               Double.valueOf(st.nextToken()).doubleValue(); //alt
           roadData[distIndex][segsLeft] = 0;
           segsLeft++;
         }
         catch(NumberFormatException e){segsLeft = 0;}
       }
     }
     while ((line = bufRdr.readLine()) != null){
       //parse the line, store the data
       //not done yet
       //$PASHR,POS,0,04,222408.00,4413.584042,N,07629.674166,W,00086.776,,257.2,000.2,000.0,06.1,03.6,05.0,04.6,UE00*37
       StringTokenizer st = new StringTokenizer(line, ",");
       System.out.println(line + st.nextToken());
       if(st.nextToken().equals("POS")){
         System.out.print(st.nextToken());
         System.out.print(st.nextToken());
         System.out.print(st.nextToken());
         roadData [latIndex][segsLeft] =
             Double.valueOf(st.nextToken()).doubleValue();//lat
         st.nextToken();
         roadData [lonIndex][segsLeft] =
             Double.valueOf(st.nextToken()).doubleValue();//lon
         st.nextToken();
         roadData [altIndex][segsLeft] =
             Double.valueOf(st.nextToken()).doubleValue();//alt
         roadData[distIndex][segsLeft] =
             Math.sqrt(
     Math.pow((roadData[latIndex][segsLeft]-roadData[latIndex][segsLeft-1])*product,2) +
     Math.pow((roadData[lonIndex][segsLeft]-roadData[lonIndex][segsLeft-1])*product,2)
                      ) + roadData[distIndex][segsLeft-1];
         segsLeft++;
   }

 }// end while
}*/

     bufRdr.close();

   } catch (IOException e) {
     System.err.println(e);
   }



   try{
     while (roadData[distIndex][index] - start > 0) {
       try {updateGrade();}
       catch (SimulationStopException e){
         throw new SimulationStopException("invalid race start argument");
       }
     }
   }
   catch(ArrayIndexOutOfBoundsException e){
     throw new SimulationStopException("invalid race start argument");
   }
   catch(NullPointerException e){
     System.err.println("Road data file contains no road data!");
     throw new SimulationStopException("Road data file contains no road data!");
   }

 }

 public double getGrade(){
 return grade/10;
 }

//Note this is the number of segments left in the road data list!
 // the simulation may end before this reaches 0.
 public int getSegsLeft(){
   return segsLeft;
 }

 public double getLat(){
   return roadData[latIndex][index];
 }

 public double getLon(){
     return roadData[lonIndex][index];
   }


 public double getAlt(){
   return roadData[altIndex][index];
 }

 public double getDistSeg(){
   if(index!=0){
     return roadData[distIndex][index]-roadData[distIndex][index-1];
   }
   else return 0;
 }
 // method throws SimStopExcpn if grade cannot be updated (list has ended,
 // or in realtime, perhaps we get a crap reading from the GPS... not
 // implemented)
 public void updateGrade() throws SimulationStopException{
   /*if(realtime){
     double[] pos1 = gps.getPos();
    
     double []pos2 = gps.getPos();
     double product = (6378100)*Math.PI/18000;
     grade = (pos1[2]-pos2[2])/
         Math.sqrt(Math.pow((pos1[1]-pos2[1])*product,2) +
                   Math.pow((pos1[0]-pos2[0])*product,2));
   }*/
   if(time.isRunning()){

       try {
       	//calculate grade
       	if(index!=0){
         grade = ((roadData[altIndex][index] - roadData[altIndex][index-1])/
             (roadData[distIndex][index] - roadData[distIndex][index-1]));
       	}
         
         // calculate orientation (used for wind and turns)
         /* Understandable version...
         * product = 60/0.5399555*1000
         *
         * distLat = (tempLat - lat)*product;
         * distLon = (tempLon - lon)*product*Math.cos((tempLat + lat)/2*Math.PI/180);
         * carAngle = atan(distLon/distLat) + PI/2
         * */
   
         carAngle = Math.atan((roadData[lonIndex][index]-roadData[lonIndex][index+1])
         		*Math.cos((roadData[latIndex][index]+roadData[latIndex][index+1])/2*Math.PI/180)/
                (roadData[latIndex][index]-roadData[latIndex][index+1])) + Math.PI/2;
         while(carAngle>Math.PI*2){
         	carAngle-=Math.PI*2;
         }

         index++;
       segsLeft--;
       // if we hit a control point, stop for ctrlTimeLength seconds of time
       if(roadData[distIndex][index]>ctrlPts[ctrlIndex]&&ctrlIndex<=ctrlPts.length){
         time.ctrlPointHit(ctrlTimeLength);
         System.out.println("Control Point: " + roadData[distIndex][index]);
         ctrlIndex++;
       }
     }
     catch (IndexOutOfBoundsException e) {
         throw new SimulationStopException("Simulation stopped at control point "+ctrlIndex);
         // Is this message right!?
     }
        }
 }
 
 public double getCarAngle(){
 	return carAngle;
 }
 
 public boolean isRunning(){
	 return time.isRunning();
 }
}


