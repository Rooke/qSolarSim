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

package org.qsvt.solarstrat.core;

import info.monitorenter.gui.chart.ITrace2D;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Mike Rooke
 * 
 * This class's job is to take care of external telemetry data.
 * 
 * Information to deal with will include: the SQL url, JDBC junk,
 * 
 * Each instance will deal with a separate command in the telemetry DB. They will
 * be called from each TelemDataNode
 * 
 * 
 */
public class TelemDataCollector implements Runnable {

	// Thread & Trace stuff
	private boolean stop = false;

	private long latency = 400;
	
	private String name = null;

	private ITrace2D trace;

	private double y = 0.0;

	private long x = 0;

	private String cmd = "BV"; // used to identify the command (e.g. BV, BC,
							   // etc.)

	private DataTableColumn dataCol, timeCol;

	private long starttime = System.currentTimeMillis();

	private long lastTime = 0;  // this is MySQL's timestamp

	//SQL Stuff
	Statement stmt;

	ResultSet rs = null;

	private static Connection conn;

	
	  //REAL constructor 
	  public TelemDataCollector(String cmd, String sqlurl,
	  String dataName, ITrace2D trace, DataTableColumn data, DataTableColumn time) 
	  throws SQLException{
	  	
	  this.latency = 1000; 
	  this.cmd = cmd;
	  name = dataName;
	  dataCol = data;
	  timeCol = time;
	  this.trace = trace;

	  SQLInitCrap();
	   }
	 
	/*TEST Constructor
	public TelemDataCollector(ITrace2D trace, int latency) throws SQLException{
		this.latency = latency;
		this.trace = trace;

		SQLInitCrap();
		
		stmt = TelemDataCollector.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
					java.sql.ResultSet.CONCUR_UPDATABLE);

	}*/

	public void SQLInitCrap() throws SQLException{
		///////////////////////////
		// THIS SHOULD LOOK *SOMETHING* LIKE THE FINAL CODE
		//////////////////////////////////
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			// handle the error
		}

		// Need to make this 'inputable'
		TelemDataCollector.conn = DriverManager.getConnection(
					"jdbc:mysql://192.168.0.1/Telem", "telem_reader", "user");

		

		try {
			// Create a Statement instance that we can use for
			// 'normal' result sets assuming you have a
			// Connection 'conn' to a MySQL database already
			// available

			stmt = TelemDataCollector.conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
					java.sql.ResultSet.CONCUR_UPDATABLE);

			//stmt.executeUpdate("CREATE TABLE IF NOT EXISTS TelemTest");
			
			//	stmt.executeUpdate("SELECT * FROM TelemTest WHERE time>" +
			// lasttime);
			rs = stmt.executeQuery("SELECT * FROM telemdata WHERE command = '"
					+ cmd + "'");
			// temp Date object to deal with the SQL timestamps
			Date ts;
			
			// The third and fourth column correspond to the VALUE and TIME
			// (i.e. what we're interested in)
			while (rs.next()) {
				
				y = rs.getDouble(3);
				x = rs.getDate(4).getTime();
				this.trace.addPoint(x , y );
				dataCol.add(new Double(y));
				timeCol.add(new Double(x));
			}
			this.trace.setName(name);
			this.lastTime = x;

		} catch (SQLException ex) {
			//      handle any errors
			System.out.println("SLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());

		}

	}

	public void SQLRefresh(){
		

		try {

			//	stmt.executeQuery("SELECT * FROM TelemTest WHERE time>" +
			// lasttime);
			
			rs = stmt.executeQuery("SELECT * FROM telemdata WHERE time >"+
					lastTime+ " AND command = '" + cmd + "'");

			// The thrid and fourth column correspond to the VALUE and TIME
			while (rs.next()) {
				System.out.println(x + " " + y);
				y = rs.getDouble(3);
				x = rs.getDate(4).getTime();
				this.trace.addPoint(x , y );
				dataCol.add(new Double(y));
				timeCol.add(new Double(x));
			}

			rs = stmt.executeQuery("SELECT CURTIME() + 0");
			lastTime = Integer.valueOf(rs.toString()).intValue();
			
		} catch (SQLException ex) {
			System.out.println("QLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		long lasttime;

		while (!stop) {
			lasttime = System.currentTimeMillis();
			SQLRefresh();
			try {
				Thread.sleep(Math.max(this.latency - System.currentTimeMillis()
						+ lasttime, 0));
			} catch (InterruptedException e) {
				this.stop = true;
			}
			if (Thread.interrupted()) {
				this.stop = true;
			}
		}
		// Allow restart (by assingment to a new Thread from outside) if this
		// instance is cached!
		this.stop = false;
	}

	public void stop() {
		this.stop = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	public void finalize() throws Throwable {
		super.finalize();
		this.stop();
	}

	/**
	 * @return Returns the trace.
	 */
	public ITrace2D getTrace() {
		return trace;
	}

	/**
	 * @return Returns the latency.
	 */
	public long getLatency() {
		return latency;
	}

	/**
	 * @param latency
	 *            The latency to set.
	 */
	public void setLatency(long latency) {
		this.latency = latency;
	}
}