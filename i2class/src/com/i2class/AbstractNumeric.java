package com.i2class;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidParameterException;
/**
 * A mutable fixed-length and scale Numeric type.
 * 
 */
public abstract class AbstractNumeric extends FixedData implements INumeric
{
	protected int Scale;
	protected DecimalData tempDecimal_;
	protected AbstractNumeric()
	{
	}
	/**
	 * Create a Numeric value with the specified length and scale.
	 * @param size The length, in bytes
	 * @param digits The total number of digits
	 * @param scale The number of digits to the right of the decimal point
	 */
	AbstractNumeric(int size, int digits, int scale)
	{
		super(size);
		construct(digits, scale);
	}
	private void construct(int digits, int scale)
	{
		this.Scale = scale;
		// This could be different then length because of packed decimal data
		// If less than 18 digits of precision are required, then we can return the 'fast' decimal type...
		if (digits<=18)
			tempDecimal_ = new ShortDecimal(digits, scale);
		// ...otherwise we have to fall back to the 'slow' BigDecimal-backed decimalBig type.
		else
			tempDecimal_ = new LongDecimal(digits, scale);
	}
	/**
	 * Create a Numeric value with the specified length and scale that overlays another field.
	 * @param size The length, in bytes
	 * @param digits The total number of digits
	 * @param scale The number of digits to the right of the decimal point
	 */
	AbstractNumeric(int size, int digits, int scale, FixedPointer overlay)
	{
		super(size, overlay);
		construct(digits, scale);
	}
	
	/** Add to the current (mutable) value (this is different than BigDecimal add which returns a new object!). */
	public void add(double value)
	{
		//add(new BigDecimal(value));
		toDecimal();
		tempDecimal_.add(value);
		assign((INumeric)tempDecimal_);
	}
	/** Add to the current (mutable) value (this is different than BigDecimal add which returns a new object!). */
	public void add(int value)
	{
		//add(new BigDecimal(value));
		toDecimal();
		tempDecimal_.add(value);
		assign((INumeric)tempDecimal_);
	}
	/** Add to the current (mutable) value (this is different than BigDecimal add which returns a new object!) */
	public void add(INumeric value)
	{
		toDecimal();
		tempDecimal_.add(value);
		assign((INumeric)tempDecimal_);
	}

	/**
	 * Add to the current (mutable) value (this is different than BigDecimal add which returns a new object!)
	 */
	public void add(BigDecimal value)
	{
		BigDecimal bd = toBigDecimal();
		bd = bd.add(value);
		assign(bd);
	}
	/**
	 * Assign a character result to this variable.
	 */
	public abstract void assign(double value);
	// This isn't abstract because fixedInt, DS, etc. don't implement
	/**
	 * Assign a numeric figurative constant to this value
	 */
	public void assign(FigConstNum value)
	{
		assign(value.decimalValue(len(), scale()));
	}
	/**
	 * Assign a character figurative constant to this value
	 */
	public void assign(FigConst value)
	{
		ZonedDecimal z = toZoned();
		z.fillArray(value.fillChar);
		if (z != this)
			assign(z);
	}

	/** Assign a decimal value to this Numeric. */
	public abstract void assign(INumeric value);
	
	
	protected void assign(InitialValue inz)
	{
		Object value = inz.value();
		if (value instanceof FigConstNum)
			assign((FigConstNum)value);
		else if (value instanceof BigDecimal)
			assign((BigDecimal)value);
		else if (value instanceof Double)
			assign(((Long)value).longValue());
		else
			throw new InvalidParameterException(inz.toString());
	}


	/* (non-Javadoc)
	 * @see com.i2class.FixedData#clear()
	 */
	public void clear() {
		assign(0);
	}

	public Object clone()
	{
		AbstractNumeric cloned = (AbstractNumeric)super.clone();
		cloned.tempDecimal_ = (DecimalData)tempDecimal_.clone();
		return cloned;
	}

	/**
	 * Assign a Java BigDecimal value to this variable.
	 */
	public abstract void assign(BigDecimal bd);

	static int compareDouble(double value1, double value2)
	{
		double diff=value1 - value2;
		if (diff==0)
			return 0;
		if (diff>0)
			return 1;
		return -1;
	}
	/** Compare to a double value. */
	// This isn't abstract because fixedInt, DS, etc. don't implement
	public int compareTo(double value)
	{
		return compareDouble(doubleValue(), value);
	}

	/**
	 * Compare to another (numeric) object type.
	 * @param o The object to compare to.  It must be of type
	 * <code>Numeric</code>, <code>java.lang.Number</code>,
	 * <code>FigConstNum</code>, <code>BigDecimal</code>, or
	 * <code>BigInteger</code>.
	 */
	public int compareTo(Object o) throws ClassCastException
	{
		if (o instanceof INumeric)
		{
			BigDecimal bd = ((INumeric) o).toBigDecimal();
			return toBigDecimal().compareTo(bd);
		}
		if (o instanceof java.lang.Number)
		{
			BigDecimal bd = ShortDecimal.newBigDecimal(o.toString());
			return toBigDecimal().compareTo(bd);
		}
		if (o instanceof FigConstNum)
		{
			/*
			double d = ((FigConstNum) o).doubleValue();
			return compareDouble(doubleValue(), d);
			*/
			o = ((FigConstNum)o).decimalValue(len(), Scale);
			return compareTo(o);
		}
		if (o instanceof BigDecimal)
			return toBigDecimal().compareTo((BigDecimal) o);
		if (o instanceof BigInteger)
			return toBigInteger().compareTo((BigInteger) o);
		throw new ClassCastException("Cannot cast to Numeric");
	}

	/**
	 * Divide the current (mutable) value.
	 */
	public void div(double value)
	{
		//div(new BigDecimal(value));
		toDecimal();
		tempDecimal_.div(value);
		assign((INumeric)tempDecimal_);
	}
	/**
	 * Divide the current (mutable) value.
	 */
	public void div(INumeric value)
	{
		//div(new BigDecimal(value));
		toDecimal();
		tempDecimal_.div(value);
		assign((INumeric)tempDecimal_);
	}
	/**
	 * Divide the current (mutable) value.
	 */
	public void div(BigDecimal value)
	{
		BigDecimal bd = toBigDecimal();
		bd = bd.divide(value, BigDecimal.ROUND_HALF_UP);
		assign(bd);
	}

	/**
	 * Divide and return a new object.
	 */
	static LongDecimal dividedBy(BigDecimal value1, BigDecimal value2)
	{
		int scale = java.lang.Math.max(value1.scale(), value2.scale());
		BigDecimal bd = value1.divide(value2, scale, BigDecimal.ROUND_HALF_UP);
		return new LongDecimal(31, bd.scale(), bd);
	}
	/**
	 * Divide and return a new object.
	 */
	public LongDecimal dividedBy(double value)
	{
		return dividedBy(ShortDecimal.newBigDecimal(value));
	}
	/**
	 * Divide and return a new object.
	 */
	public LongDecimal dividedBy(INumeric value)
	{
		return dividedBy(value.toBigDecimal());
	}
	/**
	 * Divide and return a new object.
	 */
	public LongDecimal dividedBy(BigDecimal value)
	{
		return dividedBy(toBigDecimal(), value);
	}

	/** Return the <code>double</code> representation of this numeric value. */
	public double doubleValue()
	{
		//BigDecimal bd = toBigDecimal();
		//return bd.doubleValue();
		toDecimal();
		return tempDecimal_.doubleValue();
	}

	/** Compare this numeric value to another. */
	public boolean equals(double value)
	{
		double d = doubleValue();
		return (d == value);
	}

	/** 
	 * Return the scale (number of digits to the right of the decimal point) of
	 * this number.
	 */
	public int scale()
	{
		return Scale;
	}

	/** Return the integer representation of this object. */
	public int intValue()
	{
		/*
		try
		{
			BigInteger bi = toBigInteger();
			return bi.intValue();
		}
		catch (Exception e)
		{
			return 0;
		}
		*/
		return (int)longValue();
	}

	/** Return the <code>long</code> representation of this object. */
	public long longValue()
	{
		/*
		BigInteger bi = toBigInteger();
		return bi.longValue();
		*/
		/*
		String longString = toNumericString();
		if (scale>0)
			longString = longString.substring(0,longString.length()-scale-1);
		return Long.parseLong(longString);
		*/
		toDecimal();
		return tempDecimal_.longValue();
	}
	
	/** Move a date data type to a numeric value. */
	abstract public FixedData move(FixedDate value);
	
	/** Return the fixed-length date value with no separators appropriate for MOVE/MOVEL opcodes. */
	static protected FixedChar moveDateString(FixedDate value)
	{
		return value.CharJava(FixedDate.datfmt0(value));
	}



	/**
	 * Subtract and return a new object.
	 */
	static LongDecimal minus(BigDecimal value1, BigDecimal value2)
	{
		BigDecimal bd = value1.subtract(value2);
		return new LongDecimal(31, bd.scale(), bd);
	}
	/**
	 * Subtract and return a new object.
	 */
	public LongDecimal minus(double value)
	{
		return minus(ShortDecimal.newBigDecimal(value));
	}
	/**
	 * Subtract and return a new object.
	 */
	public LongDecimal minus(INumeric value)
	{
		return minus(value.toBigDecimal());
	}
	/**
	 * Subtract and return a new object.
	 */
	public LongDecimal minus(BigDecimal value)
	{
		return minus(toBigDecimal(), value);
	}
	
	/**
	 * Multiply the current (mutable) value.
	 */
	public void mult(double value)
	{
		//mult(new BigDecimal(value));
		toDecimal();
		tempDecimal_.mult(value);
		assign((INumeric)tempDecimal_);
	}
	/**
	 * Multiply the current (mutable) value.
	 */
	public void mult(INumeric value)
	{
		toDecimal();
		tempDecimal_.mult(value);
		assign((INumeric)tempDecimal_);
	}
	/**
	 * Multiply the current (mutable) value.
	 */
	public void mult(BigDecimal value)
	{
		BigDecimal bd = toBigDecimal();
		bd = bd.multiply(value);
		assign(bd);
	}
	
	/* (non-Javadoc)
	 * @see com.i2class.INumeric#negate()
	 */
	public INumeric negate() {
		toDecimal();
		tempDecimal_.mult(-1);
		return tempDecimal_;
	}


	/** Add two BigDecimal objects and return a longDecimal value. */
	static LongDecimal plus(BigDecimal value1, BigDecimal value2)
	{
		BigDecimal bd = value1.add(value2);
		return new LongDecimal(31, bd.scale(), bd);
	}
	/**
	 * Add and return a new object (similar to BigDecimal add).
	 */
	public LongDecimal plus(double value)
	{
		return plus(ShortDecimal.newBigDecimal(value));
	}
	/**
	 * Add and return a new object (similar to BigDecimal add).
	 */
	public LongDecimal plus(INumeric value)
	{
		return plus(value.toBigDecimal());
	}
	/**
	 * Add and return a new object (similar to BigDecimal add).
	 */
	public LongDecimal plus(BigDecimal value)
	{
		return plus(toBigDecimal(), value);
	}
	
	/** Assign a zoned value of equal scale to this number. */
	abstract protected void assignZoned(ZonedDecimal z);
	
	/**
	 * Set the numeric scale of the object.
	 */
	public void setScale(int scale)
	{
		this.Scale = scale;
	}
	public short shortValue()
	{
		return (short) intValue();
	}
	/**
	 * Subtract from the current (mutable) value.
	 */
	public void sub(double value)
	{
		//sub(new BigDecimal(value));
		toDecimal();
		tempDecimal_.sub(value);
		assign((INumeric)tempDecimal_);
	}
	/**
	 * Subtract from the current (mutable) value.
	 */
	public void sub(INumeric value)
	{
		//sub(new BigDecimal(value));
		toDecimal();
		tempDecimal_.sub(value);
		assign((INumeric)tempDecimal_);
	}
	/**
	 * Subtract from the current (mutable) value.
	 */
	public void sub(BigDecimal value)
	{
		BigDecimal bd = toBigDecimal();
		bd = bd.subtract(value);
		assign(bd);
	}

	/**
	 * Multiply and return a new object.
	 */
	static LongDecimal times(BigDecimal value1, BigDecimal value2)
	{
		BigDecimal bd = value1.multiply(value2);
		return new LongDecimal(31, bd.scale(), bd);
	}
	/**
	 * Multiply and return a new object.
	 */
	public LongDecimal times(double value)
	{
		return times(ShortDecimal.newBigDecimal(value));
	}
	/**
	 * Multiply and return a new object.
	 */
	public LongDecimal times(INumeric value)
	{
		return times(value.toBigDecimal());
	}
	/**
	 * Multiply and return a new object.
	 */
	public LongDecimal times(BigDecimal value)
	{
		return times(toBigDecimal(), value);
	}
	
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
	public abstract BigDecimal toBigDecimal();
	/** Return the <code>BigInteger</code> representation of this object. */
	public BigInteger toBigInteger()
	{
		return toBigDecimal().toBigInteger();
	}
	/**
	 * Return the fixed-string representation (as opposed to the human-friendly toString()) of this value.
	 */
	public String toFixedString()
	{
		return toZoned().toFixedString();
	}
	
	/* (non-Javadoc)
	 * @see com.i2class.INumeric#toNumericString()
	 */
	public String toNumericString() {
		return toZoned().toNumericString();
	}

	/**
	 * Return the string value of this value.
	 */
	public String toString()
	{
		return toBigDecimal().toString();
	}
	
	/** Allow CL-style assignment from a fixed variable type. */
	public void chgvar(FixedChar f)
	{
		/*
		String s =f.toString().trim();
		zoned z = new zoned(s.length(), 0);
		z.move(s);
		assign(z);
		*/
		assign(Double.parseDouble(f.toString()));
	}
	
}
