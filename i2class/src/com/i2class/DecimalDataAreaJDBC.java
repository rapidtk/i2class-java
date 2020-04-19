package com.i2class;

import java.math.BigDecimal;
import java.io.*;
/**
 * A packed decimal data area class that stores data in a remote database file.
 * 
 */
class DecimalDataAreaJDBC extends DataAreaJDBC implements IDecimalDtaara
{
	/**
	 * Construct a packed decimal data area 'file'
	 * @param name the name of the dataarea
	 */
	DecimalDataAreaJDBC(I2Connection rconn, String name) throws Exception
	{
		super(rconn, name);
	}
	/**
	 * Read data from the specified data area
	 * @return data from the data area
	 */
	public BigDecimal read() throws Exception
	{
		return (BigDecimal)getObject();
	}

	/**
	 * Write data to the specified data area.
	 * @param value The value to write
	 */
	public void write(BigDecimal value) throws Exception
	{
		updateObject(value);
	}
}
