/*
 * Created on Jun 8, 2005
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

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.qsvt.solarstrat.core.DataSourceModel;
import org.qsvt.solarstrat.core.SimSourceModel;
import org.qsvt.solarstrat.core.TelemSourceModel;
import org.qsvt.solarstrat.core.WorkspaceModel;
import org.qsvt.solarstrat.core.event.WorkspaceEvent;
import org.qsvt.solarstrat.core.event.WorkspaceEventListener;

/**
 * @author Eyrak Paen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WorkspaceView extends JSplitPane implements WorkspaceEventListener, TreeSelectionListener {
	private WorkspaceModel model = null;
	private SolarstratMainWindow wnd = null;
	private DataSourceNode selectedNode = null;
	
	private JTree sourceTree = null;
	private DefaultTreeModel sourceTreeModel = null;
	private JScrollPane sourceTreeContainer = new JScrollPane();
	private JPanel emptySourceView = new JPanel();
	
	WorkspaceView(SolarstratMainWindow wnd) {
		super();
		this.wnd = wnd;
		this.setBorder(BorderFactory.createEmptyBorder());
		sourceTreeContainer.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		this.setLeftComponent(sourceTreeContainer);
		this.setRightComponent(emptySourceView);
		emptySourceView.add(new JLabel("No data source has been selected", javax.swing.SwingConstants.CENTER));
		emptySourceView.setBackground(Color.WHITE);
		emptySourceView.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		initializeTree();
	}
	
	WorkspaceView(SolarstratMainWindow wnd, WorkspaceModel model) {
		this(wnd);
		this.model = model;
		this.model.addWorkspaceListener(this);
		initializeTree();
	}
	
	private void initializeTree() {
		if(model == null) {
			model = new WorkspaceModel("New Workspace");
			model.addWorkspaceListener(this);
		}

		sourceTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode(model.getName()));
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)sourceTreeModel.getRoot();
		root.add(new DefaultMutableTreeNode("Simulations"));
		root.add(new DefaultMutableTreeNode("Telemetry Sessions"));
		root.add(new DefaultMutableTreeNode("Plot Spaces"));
				
		java.util.Iterator i = model.getSources().iterator();
		DataSourceModel data = null;
		
		while(i.hasNext()) {
			data = (DataSourceModel)i.next(); 
			if(data instanceof SimSourceModel) {
				((DefaultMutableTreeNode)root.getChildAt(0)).add(new SimSourceNode(wnd, (SimSourceModel)data));
			}
			/*if(data instanceof TelemSourceModel) {
				((DefaultMutableTreeNode)root.getChildAt(1)).add(new TelemSourceView(data));
			}
			if(data instanceof PlotSourceModel) {
				((DefaultMutableTreeNode)root.getChildAt(2)).add(new PlotSourceView(data));
			}*/
		}
		
		sourceTree = new JTree(sourceTreeModel);
		sourceTree.setBorder(BorderFactory.createEmptyBorder());
		sourceTree.addTreeSelectionListener(this);
		sourceTreeContainer.setViewportView(sourceTree);
	}

	/**
	 * @return Returns the workspace model.
	 */
	public WorkspaceModel getModel() {
		return model;
	}
	
	/**
	 * @return Returns the source tree model.
	 */
	public DefaultTreeModel getTreeModel() {
		return sourceTreeModel;
	}
	
	public void setModel(WorkspaceModel model) {
		this.model.removeWorkspaceListener(this);
		this.model = model;
		this.model.addWorkspaceListener(this);
		
		this.initializeTree();
	}
	public DataSourceNode getSelectedNode(){
		return selectedNode;
	}
	
	/* (non-Javadoc)
	 * @see org.qsvt.solarstrat.core.event.WorkspaceEventListener#workspaceEventPerformed(org.qsvt.solarstrat.core.event.WorkspaceEvent)
	 */
	public void workspaceEventPerformed(WorkspaceEvent we) {
		if(we.getEventName().equals("nameChange")) {
			DefaultMutableTreeNode tn = (DefaultMutableTreeNode)sourceTree.getModel().getRoot();
			tn.setUserObject(we.getSource().toString());
			sourceTreeModel.nodeChanged(tn);
		}
		else if(we.getEventName().equals("add")) {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)sourceTree.getModel().getRoot();
			
			if(we.getModel() instanceof SimSourceModel) {
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode)root.getChildAt(0);
				sourceTreeModel.insertNodeInto(new SimSourceNode(wnd, (SimSourceModel)we.getModel()), parent, parent.getChildCount());
			}
			if(we.getModel() instanceof TelemSourceModel) {
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode)root.getChildAt(1);
				sourceTreeModel.insertNodeInto(new TelemSourceNode(wnd, (TelemSourceModel)we.getModel()), parent, parent.getChildCount());
			}
		}
		else if(we.getEventName().equals("remove")) {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)sourceTree.getModel().getRoot();
			
			if(we.getModel() instanceof SimSourceModel) {
				// TODO: speed this operation up
				DefaultMutableTreeNode node = null, parent = (DefaultMutableTreeNode)root.getChildAt(0);
				SimSourceNode source = null;
				
				for(int i = 0; i < parent.getChildCount(); i++) {
					node = (DefaultMutableTreeNode)parent.getChildAt(i);
					source = (SimSourceNode)node.getUserObject();
					if(source.getModel() == we.getModel()) {
						sourceTreeModel.removeNodeFromParent(node);
						break;
					}
				}
			}
		}
	}
	/* (non-Javadoc)
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e) {
		//TreeNode node = (TreeNode)
		Object selected = sourceTree.getLastSelectedPathComponent();
		
		if(selected instanceof DataSourceNode) {
			setRightComponent(((DataSourceNode)selected).getView());
			selectedNode = (DataSourceNode)selected;
		}
		
		/*if(node.equals(lastClicked)){
			if(node.isLeaf()){
				// If it's a leaf, we know it's a data node
				DataNode dNode = (DataNode)
				tree.getLastSelectedPathComponent();
				chart(dNode);
				plot(dNode.getPlot());
			}
			else{
				//if it isn't, we can probably chart it..
				//TODO: chart it if possible
			}
		}
		lastClicked = node;*/
	}
}
