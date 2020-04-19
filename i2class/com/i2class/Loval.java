package com.i2class;

import java.math.BigDecimal;
/**
 * The specific figurative constant *LOVAL.  *LOVAL is the most negative number that a number of a
 * specific type can hold.  For instance, then *LOVAL value of a 9,2 number is -9999999.99.  The
 * *LOVAL value of a fixed-length(5) character value is x'0000000000'.
 * 
 */
public class Loval extends FigConstNum
{
	public Loval()
	{
		super(Character.MIN_VALUE);
	}
	/**
	 * Return the smallest (largest negative) value that can be held in the specified precision/scale.
	 */
	double doubleValue(int length, int scale)
	{
		// Create the maximum value for the specified length/scale
		double bigval = Math.pow(10, length) - 1;
		bigval /= Math.pow(10, scale);
		return (-bigval);
	}
	public double doubleValue()
	{
		return -Double.MAX_VALUE;
	}
	public float floatValue()
	{
		return -Float.MAX_VALUE;
	}
	public int intValue()
	{
		return Integer.MIN_VALUE;
	}
	public long longValue()
	{
		return Long.MIN_VALUE;
	}
	public short shortValue()
	{
		return Short.MIN_VALUE;
	}
}
