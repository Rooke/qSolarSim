/*
 * Created on Feb 17, 2005
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

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;

import org.qsvt.solarstrat.solarsim.Sim;

/**
 * @author Eyrak Paen
 *
 */
public class SolarstratMainWindow extends JFrame {

	private javax.swing.JPanel jContentPane = null;
	private SolarstratMenuBar mainMenuBar = null;
	private StatusBar statusBar = null;
	private WorkspaceView workspaceView = null;

//	private JPanel plotspacePanel = null;
	private JScrollPane dataTableContainer = null;
	private JLabel jLabel = null;
	private JTable dataTable = null;
//	private SimProgressBar statusProgress = null;

	private JPanel outputPanel = null;
	private JTextPane outputText = null;
	private JLabel outputTitle = null;
	
	// Dialogs
//	private SimConfigDialog simConfig = null;
	
	//Simulation class
	private Sim sim = new Sim();
//	private SimEventManager simStarter = null;
	
	/**
	 * This is the default constructor
	 */
	public SolarstratMainWindow() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.mainMenuBar = new SolarstratMenuBar(this);
		this.statusBar = new StatusBar("Solarstrat v.0.1");
		workspaceView = new WorkspaceView(this);
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		this.setSize(640, 480);
		this.setJMenuBar(mainMenuBar);
		this.setContentPane(getJContentPane());
		this.setTitle("SolarStrat");
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			BorderLayout borderLayout6 = new BorderLayout();
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(borderLayout6);
			borderLayout6.setVgap(3);
			jContentPane.add(workspaceView, java.awt.BorderLayout.CENTER);
			jContentPane.add(statusBar, java.awt.BorderLayout.SOUTH);
		}
		return jContentPane;
	}
	/**
	 * This method gets mainMenuBar	
	 * 	
	 * @return org.qsvt.solarstrat.ui.SolarstratMenuBar	
	 */    
	public SolarstratMenuBar getSolarstratMenuBar() {
		return mainMenuBar;
	}

	/**
	 * This method gets statusBar	
	 * 	
	 * @return org.qsvt.solarstrat.ui.StatusBar	
	 */    
	public StatusBar getStatusBar() {
		return statusBar;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getOutputPanel() {
		if (outputPanel == null) {
			outputTitle = new JLabel();
			BorderLayout borderLayout5 = new BorderLayout();
			outputPanel = new JPanel();
			outputPanel.setLayout(borderLayout5);
			borderLayout5.setVgap(6);
			outputTitle.setText("Output for: [data source/plotspace]");
			outputTitle.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
			outputPanel.add(getOutputText(), getOutputText().getName());
			outputPanel.add(outputTitle, java.awt.BorderLayout.NORTH);
		}
		return outputPanel;
	}
	/**
	 * This method initializes jTextPane	
	 * 	
	 * @return javax.swing.JTextPane	
	 */    
	private JTextPane getOutputText() {
		if (outputText == null) {
			outputText = new JTextPane();
			outputText.setEditable(false);
			outputText.setText("Note: each data source and plotspace will contain its own output text buffer.");
		}
		return outputText;
	}
	
	
	/**
	 * @return Returns the workspaceView.
	 */
	public WorkspaceView getWorkspaceView() {
		return workspaceView;
	}
	/**
	 * @param workspaceView The workspaceView to set.
	 */
	public void setWorkspaceView(WorkspaceView workspaceView) {
		this.workspaceView = workspaceView;
	}
	
	/**
	 * Launches this application
	 */
	public static void main(String[] args) {
		try {
			//PlasticLookAndFeel.setMyCurrentTheme(new ExperienceBlueDefaultFont());
			//UIManager.setLookAndFeel(new PlasticLookAndFeel());
		} catch (Exception e) {}
		
		SolarstratMainWindow application = new SolarstratMainWindow();
		application.setVisible(true);
	}
}  //  @jve:decl-index=0:visual-constraint="10,8"
