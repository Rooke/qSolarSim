/*
 * Created on May 29, 2005
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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import org.qsvt.solarstrat.ui.event.StatusEvent;
import org.qsvt.solarstrat.ui.event.StatusEventListener;

/**
 * @author mrsmiles
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StatusBar extends JToolBar implements ActionListener {
	private String defaultMessage = "Status Bar Default Message";
	private DefaultListModel messages = new DefaultListModel();
	private TaskListModel tasks = new TaskListModel();
	
	private JButton detailsButton = new JButton("Details...");
	private JLabel message = new JLabel();
	private JProgressBar progress = new JProgressBar();
	private ExpandedView expandedView = new ExpandedView();
	private JFrame detailFrame = new JFrame("Status Details");
	
	public StatusBar() {
		this.detailFrame.getContentPane().add(expandedView, BorderLayout.CENTER);
		this.detailFrame.setSize(640,320);
		this.detailFrame.hide();
		
		detailsButton.setActionCommand("showDetails");
		detailsButton.addActionListener(this);
		
		progress.setMaximumSize(new Dimension(100,16));
		progress.setMinimumSize(new Dimension(100,16));
		
		message.setText(defaultMessage);
		message.setMaximumSize(new Dimension(32767,16));
		message.setMinimumSize(new Dimension(1,16));
		
		this.setMaximumSize(new Dimension(32767,20));
		this.setMinimumSize(new Dimension(200,20));
		this.setSize(300,20);
		this.setFloatable(false);
		this.add(detailsButton);
		this.addSeparator();
		this.add(message);
		this.add(progress);
		
		refresh();
	}
	
	public StatusBar(String defaultMessage) {
		this();
		this.defaultMessage = defaultMessage;
		refresh();
	}
	
	public void addMessage(String message) {
		messages.addElement(new Message(message));
		refreshMessages();
	}
	
	public void addMessage(String message, StatusPriority priority) {
		messages.addElement(new Message(message, priority));
		refreshMessages();
	}
	
	public void addTask(Task task) {
		task.addStatusEventListener(tasks);		
		tasks.addElement(task);
		refreshTasks();
	}
	
	public void removeTask(Task task) {
		task.removeStatusEventListener(tasks);
		tasks.removeElement(task);
		refreshTasks();
	}
	
	private void refreshMessages() {
		if(messages.getSize() == 0) {
			this.setMessage(defaultMessage);
		}
		else {
			this.setMessage(messages.lastElement().toString());
		}
	}
	
	private void refreshTasks() {
		if(tasks.getSize() == 0) {
			this.disableProgress();
			
		}
		else if(tasks.getSize() == 1 && ((Task)tasks.get(0)).isIndeterminate()) {
			this.setIndeterminate();
		}
		else {
			int count = 0;
			boolean hasDetProgress = false;
			Task t;
			for(int i = 0; i < tasks.getSize(); i++) {
				t = (Task)tasks.get(i);
				if(!t.isIndeterminate()) {
					hasDetProgress = true;
					count += t.getProgress(); 
				}
			}
			if(!hasDetProgress) {
				this.setIndeterminate();
			}
			else {
				this.setProgress(count/tasks.getSize()); // TODO: rounding and such
			}
		}
	}
	
	public void refresh() {
		refreshMessages();
		refreshTasks();
	}
	
	public void clearMessages() {
		this.messages.removeAllElements();
		refreshMessages();
	}
	
	public void setMessage(String message) {
		this.message.setText(message);
	}
	
	public void setProgress(int progress) {
		if(!this.progress.isEnabled()) {
			this.progress.setEnabled(true);
			this.progress.setStringPainted(true);
		}
		if(this.progress.isIndeterminate()) {
			this.progress.setIndeterminate(false);
		}
		this.progress.setValue(progress);
	}
	
	public void setIndeterminate() {
		this.progress.setIndeterminate(true);
		this.progress.setStringPainted(false);
	}
	
	public void disableProgress() {
		this.progress.setValue(0);
		this.progress.setEnabled(false);
		this.progress.setStringPainted(false);
	}
	
	public boolean isProgressEnabled() {
		return this.progress.isEnabled();
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getActionCommand().equals("clearMessages")) {
			this.clearMessages();
		}
		else if(arg0.getActionCommand().equals("showDetails")) {
			this.detailFrame.show();
		}
	}
	
	// NOTE: Assert all objects added and removed are Task objects!!
	private class TaskListModel extends DefaultListModel implements StatusEventListener {
		public void addElement(Object element) {
			super.addElement(element);
			refreshTasks();
		}
		
		public void add(int index, Object obj) {
			super.add(index, obj);
			refreshTasks();
		}
		

		/* (non-Javadoc)
		 * @see org.qsvt.solarstrat.ui.event.StatusEventListener#statusEventPerformed(org.qsvt.solarstrat.ui.event.StatusEvent)
		 */
		public void statusEventPerformed(StatusEvent se) {
			if(se.getEventName().equals("progressUpdated")) {
				int index = this.indexOf(se.getSource());
				if(index != -1) {
					this.fireContentsChanged(this, index, index);
					refreshTasks();
				}
			}
			else if(se.getEventName().equals("textUpdated")) {
				int index = this.indexOf(se.getSource());
				if(index != -1) {
					this.fireContentsChanged(this, index, index);
				}
			}
		}
		/* (non-Javadoc)
		 * @see javax.swing.DefaultListModel#remove(int)
		 */
		public Object remove(int arg0) {
			Object result = super.remove(arg0);
			refreshTasks();
			return result;
		}
		/* (non-Javadoc)
		 * @see javax.swing.DefaultListModel#removeAllElements()
		 */
		public void removeAllElements() {
			super.removeAllElements();
			refreshTasks();
		}
		/* (non-Javadoc)
		 * @see javax.swing.DefaultListModel#removeElement(java.lang.Object)
		 */
		public boolean removeElement(Object arg0) {
			boolean result = super.removeElement(arg0);
			refreshTasks();
			return result;
		}
		/* (non-Javadoc)
		 * @see javax.swing.DefaultListModel#removeElementAt(int)
		 */
		public void removeElementAt(int arg0) {
			super.removeElementAt(arg0);
			refreshTasks();
		}
		/* (non-Javadoc)
		 * @see javax.swing.DefaultListModel#removeRange(int, int)
		 */
		public void removeRange(int arg0, int arg1) {
			super.removeRange(arg0, arg1);
			refreshTasks();
		}
	}
	
	private class ExpandedView extends JPanel {
		private JLabel messageTitle = new JLabel("Messages:");
		private JLabel taskTitle = new JLabel("Current Tasks:");
		private JList messageList = new JList(messages);
		private JList taskList = new JList(tasks);
		
		public ExpandedView() {
			this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
			this.setBorder(new EmptyBorder(16,16,16,16));
			
			Box mainBox = Box.createHorizontalBox();
			Box messageBox = Box.createVerticalBox();
			Box taskBox = Box.createVerticalBox();
			
			JScrollPane scrollPane = new JScrollPane(messageList);
			
			messageTitle.setMaximumSize(new Dimension(32767,20));
			messageTitle.setMinimumSize(new Dimension(1,20));
			scrollPane.setColumnHeaderView(messageTitle);
			messageBox.add(scrollPane);
			
			scrollPane = new JScrollPane(taskList);
			
			taskTitle.setMaximumSize(new Dimension(32767,20));
			taskTitle.setMinimumSize(new Dimension(1,20));
			scrollPane.setColumnHeaderView(taskTitle);
			taskBox.add(scrollPane);
			
			mainBox.add(messageBox);
			mainBox.add(Box.createHorizontalStrut(24));
			mainBox.add(taskBox);

			JToolBar buttons = new JToolBar();
			JButton clearMessagesButton = new JButton("Clear Messages");
			clearMessagesButton.addActionListener(StatusBar.this);
			clearMessagesButton.setActionCommand("clearMessages");
			buttons.add(clearMessagesButton);
			
			this.add(mainBox);
			this.add(buttons);
		}
	}
}
