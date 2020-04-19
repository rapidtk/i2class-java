package com.i2class;

import java.io.*;
import java.net.URL;
/**
 * A character data area class that acutally stores data in a local byte-stream file.
 * 
 * @see CharacterDataArea400
 */
class CharacterDataAreaFile implements ICharacterDtaara
{
	protected RandomAccessFile file;
	/**
	 * An empty constructor used by LocalDataAreaFile
	 * @version 10/9/2002 9:46:34 AM
	 */
	protected CharacterDataAreaFile()
	{
	}
	/**
	 * Construct an character data area 'file'
	 * @param name the name of the dataarea
	 */
	CharacterDataAreaFile(String name)
	{
		String fileName = name + ".dtaara";
		File f = new File(fileName);
		boolean exists = f.exists();
		if (exists)
		{
			try
			{
				file = new RandomAccessFile(f, "rw");
			}
			catch (Exception e) 
			{
				I2Logger.logger.severe(e);
				exists=false;
			}
		}
		if (!exists)
		I2Logger.logger.severe("Unable to open data area file " + fileName);
	}
	/**
	 * Read data from the specified data area
	 * @version 10/9/2002 8:09:46 AM
	 * @return data from the data area
	 */
	public String read() throws Exception
	{
		String value = read(0, 2000);
		return value;
	}
	/**
	 * Read data from the specified data area at the specified offset
	 * @param offset The offset (0-based, in bytes) of the data to read
	 * @version 10/9/2002
	 * @return data from the data area
	 */
	public String read(int offset, int length) throws Exception
	{
		file.seek(offset);
		String value = file.readLine();
		if (value==null)
			value="";
		if (value.length() <= length)
			return value;
		return value.substring(0, length);
	}
	/**
	 * Write data to the specified data area
	 * @param value The value to write
	 * @version 10/9/2002
	 */
	public void write(String value) throws Exception
	{
		write(value, 0);
	}
	/**
	 * Write data to the specified data area
	 * @version 10/9/2002
	 * @param value The value to write
	 * @param start The offset (1-based) to write to
	 */
	public void write(String value, int offset) throws Exception
	{
		file.seek(offset);
		file.writeBytes(value);
	}
}
