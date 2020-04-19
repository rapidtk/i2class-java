package com.i2class;
import java.sql.*;
import java.util.*;
import java.util.Enumeration;

/**
 * 
 *
 * A thread used to close the connection to the database server.
 */
class RETURNThread implements Runnable /*extends Thread*/ {
	private Application m_app;
	Connection m_conn;

	RETURNThread(Application app, Connection conn)
	{
		m_app = app;
		m_conn=conn;
	}
	
	/** Completely deactivate this program.
	 * Clean up like RETURN, and then close all open connections.
	 */
	static void deactivate(Application app) throws Throwable
	{
		try
		{
			rreturn(app);
		}
		finally
		{
			app.finalize();
		}
	}

	/** Clean up after a normal RETURN.
	 *  Close all open files and reclaim the resources associated with the specified app.
	 */ 
	static void rreturn(Application app)
	{
		// Close all of the files that this application has open
		/* This locks up in a call to next(), so don't use iterator
		Iterator it = app.openFiles.iterator();
		while (it.hasNext())
		{
			try
			{
				((Rfile)it.next()).close();
			}
			catch (Exception x) {}
		}
		*/
		app.closeAllFiles();
		
		// Null out object references
		app.cycleFile=null;
		if (app.cycleFiles != null)
			app.cycleFiles.clear();
			
		// Reclaim resources associated with this program
		app.rclrsc();
		
		// Remove program from call stack
		Application prvApp = app.prvApp();
		if (prvApp!=null)
		{
			if (prvApp.calledApps!=null)
				prvApp.calledApps.remove(app);
			app.prvAppRef.clear();
		}
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		try
		{
			deactivate(m_app);
		}
		catch (Throwable e)
		{
			I2Logger.logger.printStackTrace(e);
		}
	}
	
}
