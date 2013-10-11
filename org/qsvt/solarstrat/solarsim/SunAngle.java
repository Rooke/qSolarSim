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
 */
package org.qsvt.solarstrat.solarsim;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
/** @author unknown
 * 
 * Used to find angles, normals, etc. related to the sun given
 * a date and time.
 *
 */
class SunAngle {

	double PanelData[][];//(0 To 6, 0 To 50000) As Double

	double PanelVectors[][]; //(1 To 6, 0 To 50000) As Double

	int PanelCount;// As Integer

	double  maxVal, maxAngle;
	
	final int X1 = 1, Y1 = 2, Z1 = 3, X2 = 4, Y2 = 5, Z2 = 6;

	double Sun[] = new double[3];//(1 To 3) As Double
	//double pixelPlot[][];
	
	public SunAngle(String dataFile){
		openFile(dataFile);
	}
	


	public double calcNorm(double sunVector[]) {
		double TotalEffective = 0;
		double TotalArea = 0;
		
		try{
		Sun[0] = sunVector[0];
		Sun[1] = sunVector[1];
		Sun[2] = sunVector[2];
		}
		catch(NullPointerException e){
			System.err.println("Bad Sun Arguments");
		}
		//DisplayPic.Cls
		//DisplayPic.Line (-700, 0)-(700, 0), &HC0C0C0
		//DisplayPic.Line (0, 200)-(0, -200), &HC0C0C0
		double DX;
		double DY;
		double Gra;

		for (int i = 0; i < PanelData[0].length; i++) {
			calcPanel(i);
			
			DX = PanelData[5][i];
			DY = PanelData[6][i];
			
			if (Math.abs(PanelData[4][i]) < (Math.PI / 2)) {
				Gra = 255 - Math.abs(PanelData[4][i]) * (255 / (Math.PI / 2));
				//pixelPlot = RGB(Gra, Gra, Gra)
				TotalEffective = TotalEffective + PanelData[0][i]* Math.cos(PanelData[4][i]);
			} else {
				//DisplayPic.FillColor = &HFF
			}

			//DisplayPic.Line (DX, DY)-(DX + 10, DY + 6), DisplayPic.FillColor, BF

			TotalArea = TotalArea + PanelData[0][i];
			//PerpAreaOutLbl.Caption = Format(TotalEffective / 100 ^ 2, "0.000")
			//AreaOutLbl.Caption = Format(TotalArea / 100 ^ 2, "0.000")
			
		}
		return TotalEffective;// or should it return TotalArea?
	}

	private void openFile(String FileToOpen) {
		PanelCount = 0;
		File inFile = new File(FileToOpen); // read from file specified

		try {
			BufferedReader bufRdr = new BufferedReader(new FileReader(inFile));
			String line = null;

			 int i = 0;
		     while ((line = bufRdr.readLine())!= null) i++;
		     PanelData = new double [7][i];
		     PanelVectors = new double [7][i];
		     bufRdr.close();
		     line = null;
		     
			double coord[] = new double[10];//(1 To 9) As Double

			bufRdr = new BufferedReader(new FileReader(inFile));
			for (int j = 0; (line = bufRdr.readLine()) != null; j++) {
				//  Read data and reduce to two vectors to define triangle

				StringTokenizer st = new StringTokenizer(line, " ");
				for (i = 0; i < 9; i++) {
					coord[i] = Double.valueOf(st.nextToken()).doubleValue();
				}
				PanelVectors[X1][j] = coord[4] - coord[1];// 'x dist, vector 1
				PanelVectors[Y1][j] = coord[5] - coord[2];// 'y dist, vector 1
				PanelVectors[Z1][j] = coord[6] - coord[3];// 'z dist, vector 1
				PanelVectors[X2][j] = coord[7] - coord[1];// 'x dist, vector 2
				PanelVectors[Y2][j] = coord[8] - coord[2];// 'y dist, vector 2
				PanelVectors[Z2][j] = coord[9] - coord[3];// 'z dist, vector 2
				PanelData[5][j] = coord[1];
				PanelData[6][j] = coord[2];

			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public void calcPanel(int AreaCount){
	  //'Area of a triangle is base times height. In three space this is the perp
	  //'of the two vectors times the other vector.
		double XA,YA,ZA,XB,YB,ZB,bDOTa ,MAGa2,XPROJaB ,YPROJaB ,ZPROJaB ,XPERPaB ,YPERPaB,ZPERPaB, 
			MagPERP ,FArea,Xn ,Yn ,Zn ,Xs ,Ys ,Zs ,Xns ,Yns ,Zns ,MAGn ,MAGs ,MAGns2 ,ThetaRad; 
	
	 // 'Perp is vector 2 minus the projection of vector 2 onto 1.
	 // 'The projection is given by b.a/mag(a)2 times vector a
	  
	  XA = PanelVectors[X1][AreaCount];
	YA = PanelVectors[Y1][AreaCount];
	ZA = PanelVectors[Z1][AreaCount];
	 XB = PanelVectors[X2][AreaCount];
	YB =  PanelVectors[Y2][AreaCount];
	ZB = PanelVectors[Z2][AreaCount];
	  
	 bDOTa = XA * XB + YA * YB + ZA * ZB;
	  MAGa2 = XA * XA + YA * YA + ZA * ZA;
	  XPROJaB = (bDOTa / MAGa2) * XA;
	 YPROJaB = (bDOTa / MAGa2) * YA;
	   ZPROJaB = (bDOTa / MAGa2) * ZA;
	  
	   XPERPaB = XB - XPROJaB;
	   YPERPaB = YB - YPROJaB;
	  ZPERPaB = ZB - ZPROJaB;
	  
	  MagPERP = Math.sqrt(XPERPaB * XPERPaB + YPERPaB * YPERPaB + ZPERPaB * ZPERPaB);
	   FArea = MagPERP *Math.sqrt(MAGa2) / 2;
	  PanelData[0][AreaCount] = FArea;
	  
	  //'Finding the direction of the normal amounts to finding the cross product
	  // of the
	  //'two vectors (a x b).
	   
	  PanelData[0][AreaCount] = (YA * ZB - ZA * YB) ; //'XaCROSSb
	  PanelData[1][ AreaCount] = (XA * ZB - ZA * XB);  //'YaCROSSb
	  PanelData[2][AreaCount] = (XA * YB - YA * XB) ; //'ZaCROSSb
	 
	 //'Use the known vector for the normal, the known vector for the sun direction
	 // and
	 //'use the cosine law to find the angle.
	 //'Note due to not knowing what direction the normal is in, the angle is
	 // assumed to
	 //'be in the range + -90 degrees
	 //'AB2=OA2 + OB2 - 2*OA*OB*Cos(Theta)
	 
	 Xn = PanelData[1][AreaCount];
	  Yn = PanelData[2][AreaCount];
	  Zn = PanelData[3][AreaCount];
	 
	  Xs = Sun[0];
	 Ys = Sun[1];
	  Zs = Sun[2];
	  
	  Xns = Xn - Xs;
	  Yns = Yn - Ys;
	 Zns = Zn - Zs;
	   
	 MAGn = Math.sqrt(Xn * Xn + Yn * Yn + Zn * Zn);
	 MAGs = Math.sqrt(Xs * Xs + Ys * Ys + Zs * Zs);
	 MAGns2 = Xns * Xns + Yns * Yns + Zns * Zns;
	 
	 ThetaRad = Math.acos((MAGns2 - MAGn*MAGn - MAGs*MAGs) / (2 * MAGs * MAGn));
	 //ThetaRad = Arccos((MAGns2 - MAGn ^ 2 - MAGs ^ 2) / (2 * MAGs * MAGn));
	if(ThetaRad>=0){
	 PanelData[4][AreaCount] = ThetaRad - Math.PI;
	}
	else {
		PanelData[4][AreaCount] = ThetaRad + Math.PI;
	}
	}	

private void frontViewPlot(double AngleSun) {

		double TotalEffective = 0;

		Sun[0] = Math.sin((AngleSun / 180) * Math.PI) * 10;
		Sun[1] = 0;
		Sun[2] = Math.cos((AngleSun / 180) * Math.PI) * 10;

		for (int i = 0; i < PanelData[0].length; i++) {
			calcPanel(i);
			TotalEffective = TotalEffective + PanelData[0][i]
					* Math.cos(PanelData[4][i]);
		}

		if (TotalEffective > maxVal) {
			maxVal = TotalEffective;
			maxAngle = AngleSun;
			//MaxLbl.Caption = MaxVal & ", " & MaxAngle
			//MaxLbl.Refresh
		}
	}

	private void SideViewPlot(double AngleSun) {
		//LateralPlot.DrawWidth = 1
		//LateralPlot.Line (0, 0)-(0, 20), &HC0C0C0
		//LateralPlot.Line (30, 5)-(60, 5), &HC0C0C0
		//LateralPlot.DrawWidth = 2
		//LateralPlot.CurrentX = -90
		//LateralPlot.CurrentY = 0

		double TotalEffective = 0;

		Sun[0] = 0;
		Sun[1] = Math.sin((AngleSun / 180) * Math.PI) * 10;
		Sun[2] = Math.cos((AngleSun / 180) * Math.PI) * 10;

		for (int i = 0; i < PanelData[0].length; i++) {
			calcPanel(i);
			TotalEffective = TotalEffective + PanelData[0][i]
					* Math.cos(PanelData[4][i]);
		}

		//LateralPlot.Line -(AngleSun, TotalEffective / 100 ^ 2)
	}
}
