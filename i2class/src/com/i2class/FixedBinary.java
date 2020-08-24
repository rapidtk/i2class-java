package com.i2class;

import java.math.*;
import java.security.InvalidParameterException;
/**
 * A mutable class that represents a java binary (int/float) value stored in fixed-length data.
 *  
 */
public class FixedBinary extends AbstractNumericCoded
{
	/**
	 * Construct a value with the specified length
	 * @param sz The length of the integer value (2=short, 4=int, 8=long)
	 */
	public FixedBinary(int sz)
	{
		super(sz, len(sz), 0);
	}
	/**
	 * Construct a value with the specified length that overlays another value.
	 * @param sz The length of the integer value (2=short, 4=int, 8=long)
	 */
	public FixedBinary(int sz, FixedPointer overlay)
	{
		super(sz, len(sz), 0, overlay);
	}
	/**
	 * Construct a value with the specified length, and set its initial value.
	 * @param sz The length of the integer value (2=short, 4=int, 8=long)
	 * @param value Initial value
	 */
	public FixedBinary(int sz, long value)
	{
		this(sz);
		assign(value);
	}
	/**
	 * Create a fixed-length integer value with the specified length initialized
	 * to the specified figurative constant.
	 * @param sz The length of the integer value (2=short, 4=int, 8=long)
	 */
	public FixedBinary(int sz, FigConstNum fc)
	{
		this(sz);
		assign(fc);
	}
	public FixedBinary(int sz, InitialValue inz)
	{
		this(sz);
		Object value = inz.value();
		if (value instanceof FigConstNum)
			assign((FigConstNum)value);
		else if (value instanceof Long)
			assign((Long)value);
		else
			throw new InvalidParameterException(inz.toString());
	}

	/** Assign a long value to this variable. */
	public void assign(long value)
	{
		setBinary(value, 0, msize());
	}
	/** Assign a double value to this variable. */
	public void assign(double value)
	{
		assign((long) value);
	}
	/** Assign a double value to this variable. */
	public void assign(double value, int roundingMode)
	{
		if (roundingMode==Application.ROUND_DOWN)
			assign(value);
		else
			assign(ShortDecimal.newBigDecimal(value).setScale(Scale, roundingMode).doubleValue());
	}
	/** Assign a int value to this variable. */
	public void assign(int value)
	{
		assign((long)value);
	}
	/** Assign a BigDecimal value to this variable. */
	public void assign(BigDecimal bd)
	{
		assign(bd.doubleValue());
	}
	/** Assign a BigDecimal value to this variable. */
	public void assign(BigDecimal bd, int roundingMode)
	{
		assign(bd.setScale(Scale, roundingMode));
	}
	/** Assign a decimal value to this variable. */
	public void assign(INumeric value)
	{
		assign(value.doubleValue());
	}
	/** Assign a rounded decimal value to this variable. */
	public void assign(INumeric value, int roundingMode)
	{
		assign(value.toBigDecimal(), roundingMode);
	}
	/**
	 * Return the <code>double</code> representation of this value.
	 */
	public double doubleValue()
	{
		double d = longValue();
		return d;
	}
	
	/** Return the length in digits of a particular size in bytes. */
	static protected int len(int size)
	{
		if (size <= 2)
			return 5;
		if (size <= 4)
			return 10;
		return 19;
	}
	
	/**
	 * Return the length of this integer number: 5=short, 10=int, 19=long.
	 */
	public int len()
	{
		return len(size());
	}
	/**
	 * Return the <code>long</code> representation of this value.
	 */
	public long longValue()
	{
		long l = getBinary(0, msize());
		return l;
	}
	
	/**
	 * Move a integer value to the right-most bytes of this variable.
	 */
	public FixedData move(FixedBinary value)
	{
		//return super.move((FixedData) value);
		// If the value that is being moved is >= the length of this value and has the same scale 
		// then we can just copy bytes.
		if (value.Scale == Scale && value.msize()>=msize())
			return moveFixedData(value);
		return move(value.toZoned());
	}
	/** Move a date data type to a numeric value. */
	public FixedData move(FixedDate value)
	{
		return move(moveDateString(value));
	}

	/**
	 * Move a integer value to the left-most bytes of this variable.
	 */
	public FixedData movel(FixedBinary value)
	{
		//return super.move((FixedData) value);
		// If the value that is being moved is >= the length of this value and has the same scale 
		// then we can just copy bytes.
		if (value.Scale == Scale && value.msize()==msize())
			return movelFixedData(value);
		return movel(value.toZoned());
	}
	
	/**
	 * Return the <code>BigDecimal</code> representation of this value.
	 */
	public BigDecimal toBigDecimal()
	{
		BigDecimal bd = ShortDecimal.newBigDecimal(doubleValue());
		return bd;
	}
	/**
	 * Return the <code>zoned</code> representation of this value.
	 */
	public ZonedDecimal toZoned()
	{
		ZonedDecimal z = new ZonedDecimal(len(), Scale, doubleValue());
		return z;
	}
	
	/** Set the temporary decimal object. */
	public DecimalData toDecimal()
	{
		long l = longValue();
		tempDecimal_.setUnscaledValue(l);
		return tempDecimal_;
	}
	
	/* (non-Javadoc)
	 * @see com.i2class.AbstractNumeric#assignZoned(com.i2class.zoned)
	 */
	protected void assignZoned(ZonedDecimal z) {
		assign(z.doubleValue());
	}
}
