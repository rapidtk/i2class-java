package com.i2class;

/**
 * An I2-translated app that can be run in a Java thread.
 * @author Andrew Clark
 */
public class ThreadApplication extends Application
{

	protected ThreadApplication()
	{
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (6/5/2002 1:58:20 PM)
	 * @param app com.asc.rio.RPGapp
	 */
	public ThreadApplication(Application app)
	{
		super(app);
	}
	/**
	 * Construct this I2 thread with the specified date edit
	 * @param datfmt java.lang.String date Date format (*MDY, *YMD, *DMY, *JUL)
	 */
	public ThreadApplication(Application app, String datedit)
	{
		super(app, datedit);
	}
	/**
	 * Construct this I2 thread with the specified date edit, date format. 
	 */
	public ThreadApplication(Application app, String datedit, String datfmt)
	{
		super(app, datedit, datfmt);
	}
}
