/*
 * Created on Jun 10, 2005
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
package org.qsvt.solarstrat.ui;



import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.qsvt.solarstrat.core.SimSourceModel;
import org.qsvt.solarstrat.core.WorkspaceModel;

/**
 * @author Michael Rooke
 *
 * Responsible for input of simulation parameters.
 * 
 * E.g. file:
 * 
 * 21600 //Sim Start
 * 100000 //Sim Length (time you want to sim for)
 * 28800 // Race Start
 * 64800 // RaceEnd
 * 200  // Date (in # days since Jan 1)
 * 20 // Speed you want to go at
 * 40 // State Of Charge (in Ah)
 * 3000  // Position from start of race
 * 1,2 // where the control points are, in relation to the start of the race
 * 1000 // the length of a control point (in seconds)
 * 
 * 
 */
public class SimInputDialog extends JDialog {
	
	private String args[] = {"11600","100000","28800","64800", "100",
            "20", "40", "1000", "833000,1253000,1500000,1839000," +
    		"2283000,2931000,3333000","1800"}; // set some numbers up for testing
	private WorkspaceModel ws = null;
	private String roadData = null;
	private JTextField name = null;

	
	public SimInputDialog(WorkspaceModel ws){
		super();
		this.ws = ws;
		args = null;
		// All we need is a Workspace...
		 setTitle("Sim Input Dialog");
		 setSize(350, 200);
		 setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		 Container c = getContentPane();
		 c.setLayout(new FlowLayout());
		 
		 
		 name = new JTextField("Sim Name");
		 JButton openArgsButton = new JButton("OpenArgs");
		 JButton noArgs = new JButton("Default Test");
		 JButton runButton = new JButton("Create");
		 final JLabel statusbar = 
		              new JLabel("Select a file to load");

		 // Create a file chooser that opens up as an Open dialog
		 openArgsButton.addActionListener(new ActionListener() {
		   public void actionPerformed(ActionEvent ae) {
		   	JFileChooser chooser = new JFileChooser();
		     chooser.setMultiSelectionEnabled(true);
		     int option = chooser.showOpenDialog(SimInputDialog.this);
		     if (option == JFileChooser.APPROVE_OPTION) {
		       File[] sf = chooser.getSelectedFiles();
		       String filename = "nothing";
		       if (sf.length > 0&& (sf[0].getName().toLowerCase().endsWith(".txt"))) {
		       	filename = sf[0].getName();
		       	load(sf[0]);
		       	statusbar.setText("Loaded " + filename);
		      } 
		       
		     }
		     else {
		       statusbar.setText("You canceled.");
		     }
		   }
		 });
		 
//		 Listen for the command to run
		 runButton.addActionListener(new ActionListener() {
		   public void actionPerformed(ActionEvent ae) {
		     if(args!=null&&roadData!=null){
		     	makeSim();
		     }
		   }
		 });
		 
		// Listen for the command to cancel
		 runButton.addActionListener(new ActionListener() {
		   public void actionPerformed(ActionEvent ae) {
		     //FIXME
		   	 //************
		   	 makeSim();
		   	 //************
		   }
		 });
		 
		 noArgs.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent ae) {
				  makeSim();
			   }
			 });

		 c.add(openArgsButton);
		 c.add(runButton);
		 c.add(statusbar);
		 c.add(name);
		 c.add(noArgs);
		 if(!this.isVisible()){
		 	this.setVisible(true);
		 }
	}
	
	private void load(File f){
		BufferedReader bufRdr;
		try {
			bufRdr = new BufferedReader(new FileReader(f));

			String line = null;
			int i = 0;
	   
			while ((line = bufRdr.readLine())!= null) i++;
			bufRdr.close();
			i = 0;
			line = null;
			if(i<=12){
				bufRdr = new BufferedReader(new FileReader(f));
				args = new String[13];// will add the road data string later - hence 13
				while ((line = bufRdr.readLine())!= null){
					args[i]=line.substring(0, line.indexOf(" "));
					System.out.println(args[i]);
					i++;
				}
			}
			else{
				System.out.println("Bad Input File - must have 10 line-delimited args");
			}
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	}
	
	private void makeSim(){
		SimSourceModel ssm = new SimSourceModel(name.getText());
		ssm.setSimArgs(args);
		ws.add(ssm);
		
		this.setVisible(false);
	}


}
