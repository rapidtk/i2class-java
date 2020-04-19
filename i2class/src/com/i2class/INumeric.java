package com.i2class;

import java.math.BigDecimal;
import java.math.BigInteger;
/**
 * A mutable fixed-length and scale Numeric type.
 * 
 */
public interface INumeric
{
	/** Add to the current (mutable) value (this is different than BigDecimal add which returns a new object!). */
	public void add(double value);
	/** Add to the current (mutable) value (this is different than BigDecimal add which returns a new object!). */
	public void add(int value);
	/** Add to the current (mutable) value (this is different than BigDecimal add which returns a new object!) */
	public void add(INumeric value);
	public void add(BigDecimal value);

	/** Assign a double result to this variable. */
	public abstract void assign(double value);
	/** Assign a rounded double result to this variable. */
	public abstract void assign(double value, int roundingMode);
	
	// This isn't abstract because fixedInt, DS, etc. don't implement
	/**
	 * Assign a numeric figurative constant to this value
	 */
	public void assign(FigConstNum value);
	/** Assign a Numeric value to this variable. */
	public void assign(INumeric value);
	/** Assign a rounded Numeric value to this variable. */
	public void assign(INumeric value, int roundingMode);
	
	
	/** Assign a Java BigDecimal value to this variable. */
	public abstract void assign(BigDecimal bd);
	/** Assign and round a Java BigDecimal value to this variable. */
	public abstract void assign(BigDecimal bd, int roundingMode);

	/** Compare to a double value. */
	// This isn't abstract because fixedInt, DS, etc. don't implement
	public int compareTo(double value);
	/** Compare to another object. */
	public int compareTo(Object value);


	/**
	 * Divide the current (mutable) value.
	 */
	public void div(double value);
	/**
	 * Divide the current (mutable) value.
	 */
	public void div(INumeric value);
	/**
	 * Divide the current (mutable) value.
	 */
	public void div(BigDecimal value);

	/** Return the <code>double</code> representation of this numeric value. */
	public double doubleValue();

	/** Compare this numeric value to another. */
	public boolean equals(double value);

	/* * Return the same value with the opposite sign. */
	//public INumeric negate();
	
	/** 
	 * Return the scale (number of digits to the right of the decimal point) of
	 * this number.
	 */
	public int scale();

	/** Return the integer representation of this object. */
	public int intValue();

	/** Return the length (number of digits) of this value. */
	public int len();

	/** Return the <code>long</code> representation of this object. */
	public long longValue();

	/**
	 * Multiply the current (mutable) value.
	 */
	public void mult(double value);
	/**
	 * Multiply the current (mutable) value.
	 */
	public void mult(INumeric value);
	/**
	 * Multiply the current (mutable) value.
	 */
	public void mult(BigDecimal value);

	/**
	 * Set the numeric scale of the object.
	 */
	public void setScale(int scale);
	public short shortValue();

	/**
	 * Subtract from the current (mutable) value.
	 */
	public void sub(double value);
	/**
	 * Subtract from the current (mutable) value.
	 */
	public void sub(INumeric value);
	/**
	 * Subtract from the current (mutable) value.
	 */
	public void sub(BigDecimal value);

	
	/* 
	private void setDecimal()
	{
		int len=len();
		if (decimalValue==null)
		{
			// If less than 15 digits are used, then we can use the 'fast' decimal class
			if (len<=15)
				decimalValue = new decimal(len,scale);
			// ...otherwise, we have to use the 'slow' decimalLong class
			else
				decimalValue = new decimal(len,scale);
		}
		decimalValue.assign(toBigDecimal());
		//return decimalValue;
	}
	*/


	/** Return the <code>BigDecimal</code> representation of this object. */
	public BigDecimal toBigDecimal();
	/** Return the <code>BigInteger</code> representation of this object. */
	public BigInteger toBigInteger();

	/*
	 * Return the fixed-string representation (as opposed to the human-friendly toString()) of this value.
	public String toFixedString();
	 */
	 
	/** Return the numeric string ("0001" instead of "1") representation of a value. */
	public String toNumericString();

	/**
	 * Return the zoned decimal equivalent of this value.
	 */
	public abstract ZonedDecimal toZoned();
	
	/**
	 * Return the decimal value of this object. 
	 */
	public DecimalData toDecimal();
	/** Divide this decimal value by another one and return a new object. */
	public abstract LongDecimal dividedBy(BigDecimal value);
	/** Divide this decimal value by another one and return a new object. */
	public abstract LongDecimal dividedBy(double value);
	/** Divide this decimal value by another one and return a new object. */
	public abstract LongDecimal dividedBy(INumeric value);
	/** Subtract and return a new object. */
	public abstract LongDecimal minus(BigDecimal value);
	/** Subtract and return a new object. */
	public abstract LongDecimal minus(double value);
	/** Subtract and return a new object. */
	public abstract LongDecimal minus(INumeric value);
	/**
	 * Add and return a new object (similar to BigDecimal add).
	 */
	public abstract LongDecimal plus(BigDecimal value);
	/**
	 * Add and return a new object (similar to BigDecimal add).
	 */
	public abstract LongDecimal plus(double value);
	/**
	 * Add and return a new object (similar to BigDecimal add).
	 */
	public abstract LongDecimal plus(INumeric value);
	/** Multiply this decimal value by another one and return a new object. */
	public abstract LongDecimal times(BigDecimal value);
	/** Multiply this decimal value by another one and return a new object. */
	public abstract LongDecimal times(double value);
	/** Multiply this decimal value by another one and return a new object. */
	public abstract LongDecimal times(INumeric value);

}
