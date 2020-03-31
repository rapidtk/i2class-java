package com.i2class;

import java.math.BigDecimal;
import java.io.*;
/**
 * A character data area class that stores data in a remote database file.
 * @author Andrew Clark
 */
class CharacterDataAreaJDBC extends DataAreaJDBC implements ICharacterDtaara
{
	/**
	 * Construct a character data area 'file'
	 * @param name the name of the dataarea
	 */
	CharacterDataAreaJDBC(I2Connection rconn, String name) throws Exception 
	{
		super(rconn, name);
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICharacterDtaara#read()
	 */
	public String read() throws Exception
	{
		return (String)getObject();
	}
	/* (non-Javadoc)
	 * @see com.asc.rio.ICharacterDtaara#read(int, int)
	 */
	public String read(int offset, int length) throws Exception
	{
		String s = read();
		offset--;
		return s.substring(offset, offset+length);
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICharacterDtaara#write(java.lang.String)
	 */
	public void write(String value) throws Exception
	{
		updateObject(value);
		
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICharacterDtaara#write(java.lang.String, int)
	 */
	public void write(String value, int offset) throws Exception
	{
		StringBuffer buf = new StringBuffer((String)getObject());
		offset--;
		buf.replace(offset, offset+value.length(), value);
		write(buf.toString());
	}
}
