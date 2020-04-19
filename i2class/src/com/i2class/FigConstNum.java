package com.i2class;

import java.math.BigDecimal;
/**
 * A RPG numeric figurative constant class.  Classes like Zeros (*ZEROS) 
 * derive from this class.
 * 
 * @version 1.0
 */
abstract class FigConstNum extends FigConst
{
	/**
	 * Create a numeric constant initialized to the specified character value. 
	 */
	FigConstNum(char c)
	{
		super(c);
	}

	/** Compare to another Numeric object. **/
	public int compareTo(Object o) throws ClassCastException
	{
		if (o instanceof INumeric)
		{
			int i = ((INumeric) o).compareTo(this);
			return -i;
		}
		throw new ClassCastException("Cannot cast to FigConstNum");
	}

	/**
	 * Return the numeric value of this class with the specified
	 * length and scale as a Java BigDecimal object.
	 */
	BigDecimal decimalValue(int length, int scale)
	{
		return ShortDecimal.newBigDecimal(doubleValue(length, scale));
	}
	abstract double doubleValue(int length, int scale);

	abstract double doubleValue();
	abstract float floatValue();
	abstract int intValue();
	abstract long longValue();
	abstract short shortValue();
}
