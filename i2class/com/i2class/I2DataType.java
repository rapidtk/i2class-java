package com.i2class;

import java.io.Serializable;

/**
 * A base data type for AS400xxx field definitions.
 * 
 */
public abstract class I2DataType implements Serializable 
{
	int size, precision;
	String name;
	/**
	 * Construct a I2DataType with the specified length and scale.
	 */
	I2DataType(int length, int scale)
	{
		this.size = length;
		this.precision = scale;
	}
	/**
	 * Return the I2 fixed data type that represents this field description.
	 */
	public abstract FixedData getFixedType();
	
	/** Return field name. */
	public String getName()
	{
		return name;
	}
}
