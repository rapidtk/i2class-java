package com.i2class;

import java.math.*;

/**
 * Implement <code>*ZERO</code> functionallity.
 * For numeric values, *ZEROS is the same as 0.
 * For fixed-length strings, *ZEROS is a string of character '0' that is the same length as the fixed string.
 * @author Andrew Clark
 */
public class Zeros extends FigConstNum
{
	Zeros()
	{
		super('0');
	}
	double doubleValue(int length, int scale)
	{
		return 0;
	}

	public double doubleValue()
	{
		return 0;
	}
	public float floatValue()
	{
		return 0;
	}
	public int intValue()
	{
		return 0;
	}
	public long longValue()
	{
		return 0;
	}
	public short shortValue()
	{
		return 0;
	}
}
