/*
 * Created on Apr 15, 2005
 */
package com.i2class;

import java.sql.Connection;
import java.util.Vector;

/**
 * Limited support for multi-format logical files in JDBC.
 * 
 *
 */
public class RindexMulti extends RindexJDBC
{

	private Vector m_formats = new Vector(2);
	
	/**
	 * @param system
	 * @param lfileName
	 */
	public RindexMulti(Connection system, String lfileName)
	{
		super(system, lfileName);
	}
	
	/** Add a record format to the list of formats available to be returned by the file. */
	public void addFormat(RecordJDBC format) throws Exception
	{
		m_formats.add(format);
		if (irecord==null)
			setRecordFormat(format);
	}

	/* (non-Javadoc)
	 * @see com.i2class.RindexJDBC#next()
	 */
	protected boolean next() throws Exception
	{
		int fmtCount = m_formats.size();
		boolean found = super.next();
		// Skip any records that don't match one of the records to read from
		found_loop: 
		while (found)
		{
			// Get the format name from the result set
			String formatName = rs.getString("FORMAT_NAME");
			for (int i=0; i<fmtCount; i++)
			{
				RecordJDBC record = (RecordJDBC)m_formats.elementAt(i);
				if (record.recordName.compareTo(formatName)==0)
				{
					setRecordFormat(record);
					break found_loop;
				}
			}
		}
		//throw new Exception("Undefined format name: " + formatName);
		return found;
	}
	
	/* (non-Javadoc)
	 * @see com.i2class.RfileJDBC#buildSelect()
	 */
	protected StringBuffer buildSelect() throws Exception
	{
		return super.buildSelect().append(", FORMAT_NAME");
	}

}
