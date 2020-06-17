package com.i2class;

import java.lang.reflect.Field;
import java.sql.Connection;

import com.ibm.as400.access.AS400;

/**
 * Extension to Rfile to deal with RPG cycle 
 * <a href="http://publib.boulder.ibm.com/iseries/v5r2/ic2924/books/c092508407.htm#HDRRPGLOG">cycle</a> processing.
 */
abstract class RfileCycle extends Rfile
{

	// This really shouldn't be here (there should be some kind of IIndex.equal() method), but...
	boolean m_equal;
	
	abstract boolean readCycle() throws Exception;
	abstract boolean readx() throws Exception;

	// If the Connection object is a I2Connection, then set 'global' found indicator.
	protected void setI2Found(boolean found)
	{
		this.found = found;
		if (app != null)
			app.FOUND = found;
	}
	
	// Set global equal indicator
	protected void setI2Equal(boolean equal)
	{
		m_equal = equal;
		if (app != null)
			app.EQUAL = equal;
	}
}
