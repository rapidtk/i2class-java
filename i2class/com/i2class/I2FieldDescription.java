package com.i2class;

/**
 * A generic field description class used in place of the IBM record-level access class.
 * 
 * @see com.ibm.as400.access.FieldDescription
 */
class I2FieldDescription
{
	I2DataType dataType;
	int offset;
	public I2FieldDescription()
	{
		super();
	}
	I2FieldDescription(I2DataType type, String fieldName)
	{
		dataType = type;
		dataType.name = fieldName;
	}
	public String toString()
	{
		return dataType.name;
	}
}
