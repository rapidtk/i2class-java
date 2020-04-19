package com.i2class;

/**
 * A basic record format interface.  A record format describes the fields that make up a SQL-like record of data.
 * 
 */
public interface IRecordFormat
{
	/**
	 * Return the field name at the specified index.
	 */
	public String getFieldName(int index);
	/**
	 * Return the offset into the record buffer of the field at the specified index.
	 */
	public int getFieldOffset(int index);
	/**
	 * Return the I2 fixed data type representation of the FieldDescription at the specified index.
	 */
	public FixedData getFixedDataType(int index);
	/**
	 * Return the number of fields in this record format.
	 */
	int getNumberOfFields();
	
	/** Return the size of this record format */
	int size();
}
