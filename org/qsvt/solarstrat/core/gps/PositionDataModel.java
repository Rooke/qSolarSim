/*
 * Created on Jun 21, 2005
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
package org.qsvt.solarstrat.core.gps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import org.qsvt.solarstrat.core.DataSourceModel;

/**
 * @author Mike Rooke
 *
 * Model for the display of an on-the-fly road map.  Does the work of getting the
 * position of the user through some sort of GPS (determined externally).  Also,
 * for practical visualization, the entire race route can't be displayed, thus a
 * Field Of View (FOV) array is referenced.  This array is the chunk of the road data
 * currently displayed. Silly plot explanation:
 * 
 *           |     *<-- Plus index
 *           |      *
 *           |     *
 *           |       *
 *           |        (*)<-- current pos
 *           |          *
 *           |           *
 *           |             *
 *           |               * *
 *           |                   * <-- Minus index
 *           __________________________________
 * 
 * 
 * 
 */
public class PositionDataModel extends DataSourceModel{
	
	int FOVindexMinus = 0; // meant to keep track of the map limits
	int FOVindexPlus = 0;
	int posIndex = 0;
	
	// Borrowed from solarsim.GradeManager
	 private double[][] roadData;
	 private final short DIST_INDEX = 0;
	 private final short ALT_INDEX = 1;
	 private final short LAT_INDEX = 2;
	 private short LON_INDEX = 3;
	
	public PositionDataModel(String filename){
		super("Position");
		
		initGPS();
		
		processFile(filename);
	}
	
	// set up some sort of link for the ability to implement "getPos()"
	private void initGPS(){
		return;
	}
	
	public double[][] getFOV(int numPointsToDisplay) {
		return new double[0][0];
	}
	
	public double[] getPos() {
		return new double[0];
	}
	
//	 Borrowed from solarsim.GradeManager
	private void processFile (String filename){

		   File inFile  = new File(filename);  // read from file specified

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
		     for (i = 0; (line = bufRdr.readLine()) != null; i++){
		       StringTokenizer st = new StringTokenizer(line, "\t");
		       roadData [LAT_INDEX][i] =
		            Double.valueOf(st.nextToken()).doubleValue();//lat
		          roadData[LON_INDEX][i] =
		              Double.valueOf(st.nextToken()).doubleValue(); //lon
		          roadData[DIST_INDEX][i] =
		              Double.valueOf(st.nextToken()).doubleValue(); //dist	//Note:  multiplcation by 1000 
		          															//       here causes a bug.
		          roadData[ALT_INDEX][i] =
		              Double.valueOf(st.nextToken()).doubleValue(); // alt
		     }
		     bufRdr.close();

		   } catch (IOException e) {
		     System.err.println(e);
		   }



		   try{
		     //while (roadData[DIST_INDEX][index] - start > 0) {
		     //  Try to get the position index as close as possible to a point
		   	// in the road data.
		     }
		   
		   catch(ArrayIndexOutOfBoundsException e){
		   }
		   catch(NullPointerException e){
		     System.err.println("Road data file contains no road data!");
		   }

		 }


}