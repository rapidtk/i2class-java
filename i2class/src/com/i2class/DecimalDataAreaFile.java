package com.i2class;

import java.math.BigDecimal;
import java.io.*;
/**
 * A packed decimal data area class that acutally stores data in a local byte-stream file.
 * 
 * @see DecimalDataArea400
 */
class DecimalDataAreaFile implements IDecimalDtaara
{
	private RandomAccessFile file;
	/**
	 * Construct a packed decimal data area 'file'
	 * @param name the name of the dataarea
	 */
	DecimalDataAreaFile(String name)
	{
		try
		{
			file = new RandomAccessFile(name + ".dtaara", "rw");
		}
		catch (Exception e)
		{
		}
	}
	/**
	 * Read data from the specified data area
	 * @return data from the data area
	 */
	public BigDecimal read() throws Exception
	{
		file.seek(0);
		String value = file.readLine();
		BigDecimal bd = new BigDecimal(value);
		return bd;
	}
	/**
	 * Write data to the specified data area.
	 * @param value The value to write
	 */
	public void write(BigDecimal value) throws Exception
	{
		file.seek(0);
		file.writeBytes(value.toString());
	}
}
