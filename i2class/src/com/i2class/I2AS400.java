package com.i2class;

import java.sql.SQLException;

import com.ibm.as400.access.AS400;

/** An I2 AS400 object that contains the instance of the Application that it was instantiated from. */
public class I2AS400 implements IRHost {
	AS400 as400;
	Application app;
	I2AS400(AS400 as400, Application app)
	{
		this.as400=as400;
		this.app=app;
	}
	/* (non-Javadoc)
	 * @see com.asc.rio.IHost#close()
	 */
	public void close() throws SQLException {
		as400.disconnectAllServices();
	}
	
	/* (non-Javadoc)
	 * @see com.asc.rio.IHost#getHost()
	 */
	public Object getHost() 
	{
		return as400;
	}
	/* (non-Javadoc)
	 * @see com.asc.rio.IHost#setHost(java.lang.Object)
	public void setHost(Object o)
	{
		as400 = (AS400)o;
	}
	 */
	public AS400 getAS400() 
	{
		return as400;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable
	{
		this.app=null;
		this.as400=null;
		super.finalize();
	}
	/* (non-Javadoc)
	 * @see com.asc.rio.IHost#deactivate()
	 */
	public void invalidate() throws Throwable
	{
		finalize();
	}
	
	/* (non-Javadoc)
	 * @see com.asc.rio.IRHost#getApp()
	 */
	public Application getApp()
	{
		return app;
	}
	
}
