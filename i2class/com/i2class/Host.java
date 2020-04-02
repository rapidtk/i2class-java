package com.i2class;

import java.sql.SQLException;

/**
 * An interface to a I2 host (either I2AS400 or I2Connection) object
 * @author ANDREWC
 *
 */
public interface IRHost {
	// Close the connection to the host
	public void close() throws SQLException;
	// The 'base' host object (e.g. java.sql.Connection or com.ibm.as400.AS400)
	public Object getHost();
	// Set the 'base' host object (e.g. java.sql.Connection or com.ibm.as400.AS400)
	public void setHost(Object o);
	// Invalidate the connection
	public void invalidate() throws Throwable;
	// Return the Application associated with this object
	public Application getApp();
	
}
