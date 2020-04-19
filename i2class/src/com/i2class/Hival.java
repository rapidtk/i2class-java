package com.i2class;

import java.math.BigDecimal;
/**
  * A class the represents the special value <code>*HIVAL</code>.
  * 
  */
public class Hival extends FigConstNum
{
	Hival()
	{
		super(Character.MAX_VALUE);
	}
	/**
	 * Return the largest value that can be held in the specified precision/scale.
	 */
	double doubleValue(int length, int scale)
	{
		// Create the maximum value for the specified length/scale
		double bigval = Math.pow(10, length) - 1;
		bigval /= Math.pow(10, scale);
		return bigval;
	}
	BigDecimal decimalValue(int length, int scale)
	{
		return ShortDecimal.newBigDecimal(doubleValue(length, scale));
	}
	public double doubleValue()
	{
		return Double.MAX_VALUE;
	}
	public float floatValue()
	{
		return Float.MAX_VALUE;
	}
	public int intValue()
	{
		return Integer.MAX_VALUE;
	}
	public long longValue()
	{
		return Long.MAX_VALUE;
	}
	public short shortValue()
	{
		return Short.MAX_VALUE;
	}
}
