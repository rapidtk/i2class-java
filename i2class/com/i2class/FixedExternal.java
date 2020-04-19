package com.i2class;

import java.util.Vector;

/**
 * A fixed-length buffer that represents an externally-described data structure (record buffer).
 * 
 */
public class FixedExternal extends FixedChar {
	/** The record format that describes this data structure. */
	IRecordFormat recordFormat;
	/** The cached data types associated with this record. */
	Vector fixedDataTypes = new Vector();
	
	public FixedExternal(IRecordFormat rcdFmt)
	{
		super(rcdFmt.size());
		recordFormat = rcdFmt;
	}
	
	protected FixedExternal(IRecordFormat rcdFmt, FixedPointer overlay)
	{
		super(rcdFmt.size(), overlay);
		recordFormat = rcdFmt;
	}
	
	/** Get the fixed data type of the field at the specified index */
	static FixedData getFixedData_(int fieldIndex, IRecordFormat recordFormat, FixedChar value, Vector fixedDataTypes)
	{
		// See if this value has already been cached
		FixedData f=null;
		int fixedDataTypesSize = fixedDataTypes.size();
		int offset = recordFormat.getFieldOffset(fieldIndex);
		if (fieldIndex < fixedDataTypesSize)
			f = (FixedData)fixedDataTypes.elementAt(fieldIndex);
		// If the subfield doesn't exist yet, then create it and add to list
		if (f==null)
		{
			f = recordFormat.getFixedDataType(fieldIndex)/*.superclone();
			f.m_ptr = new pointer(value.getOverlay(), offset)*/;
			if (fieldIndex==fixedDataTypesSize)
				fixedDataTypes.addElement(f);
			else
			{
				fixedDataTypes.setSize(fieldIndex+1);
				fixedDataTypes.setElementAt(f, fieldIndex);
			}
		}
		/* ?!? If the element does exist, then set its value to this DS's buffer
		else
			f.arrayCopy(0, value.getOverlay(), offset);
		*/
		//System.arraycopy(value.overlay, offset, f.overlay, 0, f.overlay.length);
		f.arrayCopy(0, value.getOverlay(), offset);
		return f;
	}
	/** Get the fixed data type of the field at the specified index */
	public FixedData getFixedData(int fieldIndex)
	{
		return getFixedData_(fieldIndex, recordFormat, this, fixedDataTypes);
	}
	/** Get the fixed data type of the field at the specified index */
	public FixedChar getFixed(int fieldIndex)
	{
		return (FixedChar)getFixedData(fieldIndex);
	}

	/** Return the character value of the specified field. */
	public char getChar(int fieldIndex)
	{
		int fieldOffset = recordFormat.getFieldOffset(fieldIndex);
		char c = charAt(fieldOffset);
		return c;
	}
	/** Return the short value of the specified field. */
	public short getShort(int fieldIndex)
	{
		int fieldOffset = recordFormat.getFieldOffset(fieldIndex);
		short s = shortAt(fieldOffset);
		return s;
	}
	/** Return the int value of the specified field. */
	public int getInt(int fieldIndex)
	{
		int fieldOffset = recordFormat.getFieldOffset(fieldIndex);
		int i = intAt(fieldOffset);
		return i;
	}
	/** Return the long value of the specified field. */
	public long getLong(int fieldIndex)
	{
		int fieldOffset = recordFormat.getFieldOffset(fieldIndex);
		long l = longAt(fieldOffset);
		return l;
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
		int offset = recordFormat.getFieldOffset(fieldIndex);
		setFixedAt(offset, f);
	}
	/** Set the character value of the specified field. */
	public void setValue(int fieldIndex, char value)
	{
		FixedChar f = (FixedChar)getFixed(fieldIndex);
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
	public void setValue(int fieldIndex, FixedChar value)
	{
		FixedChar f = (FixedChar)getFixed(fieldIndex);
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
