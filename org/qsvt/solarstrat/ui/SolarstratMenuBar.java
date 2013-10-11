/*
 * Created on May 17, 2005
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

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.qsvt.solarstrat.solarsim.Sim;
import org.qsvt.solarstrat.ui.actions.ConfigureTelemetryAction;
import org.qsvt.solarstrat.ui.actions.ExitAction;
import org.qsvt.solarstrat.ui.actions.ExportAction;
import org.qsvt.solarstrat.ui.actions.ImportAction;
import org.qsvt.solarstrat.ui.actions.ImportTelemetryAction;
import org.qsvt.solarstrat.ui.actions.LoadAction;
import org.qsvt.solarstrat.ui.actions.MaxAnalyzeAction;
import org.qsvt.solarstrat.ui.actions.MeanAnalyzeAction;
import org.qsvt.solarstrat.ui.actions.MinAnalyzeAction;
import org.qsvt.solarstrat.ui.actions.NewSimAction;
import org.qsvt.solarstrat.ui.actions.NewSimBasedOnPresentAction;
import org.qsvt.solarstrat.ui.actions.PauseSimAction;
import org.qsvt.solarstrat.ui.actions.RMSAnalyzeAction;
import org.qsvt.solarstrat.ui.actions.SaveAction;
import org.qsvt.solarstrat.ui.actions.StartSimAction;
import org.qsvt.solarstrat.ui.actions.StopSimAction;
import org.qsvt.solarstrat.ui.actions.VarAnalyzeAction;

/**
 * @author mrsmiles
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SolarstratMenuBar extends JMenuBar {
	private SolarstratMainWindow mainWindow = null;
	
	private JMenu fileMenu = null;
	private JMenu simulationMenu = null;
	private JMenu telemetryMenu = null;
	private JMenu analyzeMenu = null;

	
	//Simulation class
	private Sim sim = new Sim();
	//private SimEventManager simStarter = null;
	
	/**
	 * 
	 */
	public SolarstratMenuBar(SolarstratMainWindow mainWindow) {
		super();
		this.setBorder(BorderFactory.createEmptyBorder());
		this.mainWindow = mainWindow;
		this.add(getFileMenu());
		this.add(getSimulationMenu());
		this.add(getTelemetryMenu());
		this.add(getAnalyzeMenu());
	}
	
	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private javax.swing.JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new javax.swing.JMenu();
			fileMenu.setText("File");
			fileMenu.add(new JMenuItem(new LoadAction(mainWindow)));
			fileMenu.add(new JMenuItem(new SaveAction(mainWindow)));
			fileMenu.add(new JMenuItem(new ImportAction(mainWindow)));
			fileMenu.add(new JMenuItem(new ExportAction(mainWindow)));
			fileMenu.add(new JMenuItem(new ExitAction(mainWindow)));
		}
		return fileMenu;
	}
	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private javax.swing.JMenu getSimulationMenu() {
		if (simulationMenu == null) {
			simulationMenu = new javax.swing.JMenu();
			simulationMenu.setText("Simulation");
			simulationMenu.add(new JMenuItem(new NewSimAction(mainWindow)));
			simulationMenu.add(new JMenuItem(new StartSimAction(mainWindow)));
			simulationMenu.add(new JMenuItem(new NewSimBasedOnPresentAction(mainWindow)));
			simulationMenu.add(new JMenuItem(new PauseSimAction(mainWindow)));
			simulationMenu.add(new JMenuItem(new StopSimAction(mainWindow)));
		}
		return simulationMenu;
	}
	
	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private javax.swing.JMenu getTelemetryMenu() {
		if (telemetryMenu == null) {
			telemetryMenu = new javax.swing.JMenu();
			telemetryMenu.setText("Telemetry");
			telemetryMenu.add(new JMenuItem(new ImportTelemetryAction(mainWindow)));
			telemetryMenu.add(new JMenuItem(new ConfigureTelemetryAction(mainWindow)));
		}
		return telemetryMenu;
	}
	
	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private JMenu getAnalyzeMenu() {
		if (analyzeMenu == null) {
			analyzeMenu = new JMenu();
			analyzeMenu.setText("Analyze");
			analyzeMenu.add(new JMenuItem(new MaxAnalyzeAction(mainWindow)));
			analyzeMenu.add(new JMenuItem(new MinAnalyzeAction(mainWindow)));
			analyzeMenu.add(new JMenuItem(new MeanAnalyzeAction(mainWindow)));
			analyzeMenu.add(new JMenuItem(new VarAnalyzeAction(mainWindow)));
			analyzeMenu.add(new JMenuItem(new RMSAnalyzeAction(mainWindow)));
		}
		return analyzeMenu;
	}
}
