/*
 * Created on Mar 3, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.i2class;

import java.sql.Connection;
import java.util.Vector;

import com.ibm.as400.access.AS400;

/**
 * An externally-described data area.
 * @author ANDREWC
 *
 */
public class fixedDtaaraExternal extends fixedDtaara {

	/** The record format that describes this data structure. */
	IRecordFormat recordFormat;
	/** The cached data types associated with this record. */
	Vector fixedDataTypes = new Vector();
	
	/** Create a remote data area object. */
	public fixedDtaaraExternal(IRecordFormat rcdFmt, AS400 system, String dtaaraName)
	{
		super(rcdFmt.size(), system, dtaaraName);
		recordFormat = rcdFmt;
	}
	/** Create a local data area object. */
	public fixedDtaaraExternal(IRecordFormat rcdFmt, Connection host, String dtaaraName)
	{
		super(rcdFmt.size(), host, dtaaraName);
		recordFormat = rcdFmt;
	}
	/** Create a remote (JDBC) data area object. */
	public fixedDtaaraExternal(IRecordFormat rcdFmt, I2Connection rconn, String dtaaraName)
	{
		super(rcdFmt.size(), rconn, dtaaraName);
		recordFormat = rcdFmt;
	}

	/** Return the character value of the specified field. */
	public char getChar(int fieldIndex)
	{
		int fieldOffset = recordFormat.getFieldOffset(fieldIndex);
		char c = charAt(fieldOffset);
		return c;
	}

	/** Get the fixed data type of the field at the specified index */
	public fixed getFixed(int fieldIndex)
	{
		return (fixed)getFixedData(fieldIndex);
	}


	/** Get the fixed data type of the field at the specified index */
	public FixedData getFixedData(int fieldIndex)
	{
		return fixedExternal.getFixedData_(fieldIndex, recordFormat, this, fixedDataTypes);
	}

	/** Return the numeric value of the specified field. */
	public AbstractNumeric getNumeric(int fieldIndex)
	{
		AbstractNumeric n = (AbstractNumeric)getFixedData(fieldIndex);
		return n;
	}


	/* Actually assign the value to the buffer here. */
	private void setFixedValue_(int fieldIndex, FixedData f)
	{
		/*
		int offset = recordFormat.getFieldOffset(fieldIndex);
		setFixedAt(offset, f);
		*/
	}

	/** Set the character value of the specified field. */
	public void setValue(int fieldIndex, char value)
	{
		fixed f = (fixed)getFixed(fieldIndex);
		f.assign(value);
		setFixedValue_(fieldIndex, f);
	}

	/** Set the double value of the specified field. */
	public void setValue(int fieldIndex, double value)
	{
		// If this is a numeric assignment, then this must be a Numeric fixed data type
		AbstractNumeric n = (AbstractNumeric)getFixedData(fieldIndex);
		n.assign(value);
		setFixedValue_(fieldIndex, n);
	}

	/** Set the fixed value of the specified field. */
	public void setValue(int fieldIndex, fixed value)
	{
		fixed f = (fixed)getFixed(fieldIndex);
		f.assign(value);
		setFixedValue_(fieldIndex, f);
	}

	/** Set the fixed value of the specified field. */
	public void setValue(int fieldIndex, INumeric value)
	{
		AbstractNumeric f = (AbstractNumeric)getFixedData(fieldIndex);
		f.assign(value);
		setFixedValue_(fieldIndex, f);
	}

}
