package com.i2class;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A mutable fixed-length, scale decimal data type for exact arithmetic.
 * 
 * 
 *
 */
public /*final*/ class LongDecimal extends DecimalData {
	private BigDecimal decimalValue;

	/** Construct a decimal object with the specified length and scale. */
	public LongDecimal(int length, int scale) {
		this.length = length;
		this.Scale = scale;
		decimalValue = BigDecimal.valueOf(0);
	}
	/** Construct a decimal object with the specified length and scale and initial value. */
	public LongDecimal(int length, int scale, BigDecimal inz) {
		this.length = length;
		this.Scale = scale;
		decimalValue = inz;
	}
	/** Construct a decimal object with the specified length and scale and initial value. */
	public LongDecimal(int length, int scale, FigConstNum value) {
		this(length, scale);
		assign(value);
	}
	/** Construct a decimal object with the specified length and scale and initial value. */
	public LongDecimal(int length, int scale, double value) {
		this(length, scale);
		assign(value);
	}

	/** Assign a double value to this decimal. */
	public void assign(double value) {
		BigDecimal bd = ShortDecimal.newBigDecimal(value);
		assign(bd);
	}
	/** Assign a rounded double value to this decimal. */
	public void assign(double value, int roundingMode) {
		BigDecimal bd = ShortDecimal.newBigDecimal(value);
		assign(bd, roundingMode);
	}
	/** Assign an integer value to this decimal. */
	public void assign(int value) {
		assign((long)value);
	}
	/** Assign a long value to this decimal. */
	public void assign(long value) {
		BigDecimal bd = BigDecimal.valueOf(value);
		assign(bd);
	}

	/** Assign a BigDecimal value to this decimal. */
	public void assign(BigDecimal bd) {
		assign(bd, Application.ROUND_DOWN);
	}
	/** Assign a rounded BigDecimal value to this decimal. */
	public void assign(BigDecimal bd, int roundingMode) {
		decimalValue = bd.setScale(Scale, roundingMode);
	}

	/** Assign a IDecimal value to this decimal. */
	public void assign(INumeric value) {
		assign(value, Application.ROUND_DOWN);
	}
	/** Assign a rounded IDecimal value to this decimal. */
	public void assign(INumeric value, int roundingMode) {
		assign(value.toBigDecimal(), roundingMode);
	}

	/** Generate the BigDecimal equivalent of this number */
	public BigDecimal toBigDecimal() {
		return decimalValue;
	}
	/* Create a zoned decimal equivalent of this object. */
	public FixedData toFixedChar() {
		return toZoned();
	}

	/** Clone this decimal object. */  
	public Object clone()
	{
		LongDecimal cloned = new LongDecimal(length, Scale);
		cloned.assign(this);
		return cloned;
	}

	/** Compare the value of this decimal to another object. */
	public int compareTo(Object o) throws ClassCastException {
		if (o instanceof FigConstNum)
			return compareTo(((FigConstNum) o).decimalValue(this.length, this.Scale));
		try
		{
			BigDecimal bd = ShortDecimal.newBigDecimal(o.toString());
			return compareTo(bd);
		}
		catch (Exception e)
		{
			throw new ClassCastException("Cannot cast to longDecimal type");
		}
	}
	
	/** Compare to a BigDecimal value. */
	public int compareTo(BigDecimal value)
	{
		return decimalValue.compareTo(value);
	}
	/** Compare to a double value. */
	public int compareTo(double value)
	{
		return compareTo(ShortDecimal.newBigDecimal(value));
	}
	/** Compare to a long value. */
	public int compareTo(long value)
	{
		return compareTo(BigDecimal.valueOf(value));
	}

	/** Add a double value to this decimal one. */
	public void add(double value) {
		BigDecimal bd = ShortDecimal.newBigDecimal(value);
		add(bd);
	}
	/** Add an integer value to this decimal one. */
	public void add(int value) {
		add((long)value);
	}
	/** Add a long value to this decimal one. */
	public void add(long value) {
		BigDecimal bd = BigDecimal.valueOf(value);
		add(bd);
	}
	/** Add another decimal value to this one. */
	public void add(INumeric value) {
		add(value.toBigDecimal());
	}
	
	/** Subtract another decimal value from this one. */
	public void sub(INumeric value) {
		sub(value.toBigDecimal());
	}
	/** Subtract a double value from this decimal one. */
	public void sub(double value) {
		BigDecimal bd = ShortDecimal.newBigDecimal(value);
		sub(bd);
	}
	/** Subtract an integer value from this decimal one. */
	public void sub(int value) {
		sub((long)value);
	}
	/** Subtract a long value from this decimal one. */
	public void sub(long value) {
		BigDecimal bd = BigDecimal.valueOf(value);
		sub(bd);
	}

	/** Multiply this decimal value by a double. */
	public void mult(double value) {
		BigDecimal bd = ShortDecimal.newBigDecimal(value);
		mult(bd);
	}
	/** Multiply this decimal value by an integer. */
	public void mult(int value) {
		mult((long)value);
	}
	/** Multiply this decimal value by a long. */
	public void mult(long value) {
		BigDecimal bd = BigDecimal.valueOf(value);
		mult(bd);
	}
	/** Multiply this decimal value by another one. */
	public void mult(INumeric value) {
		mult(value.toBigDecimal());
	}

	/** Divide this decimal value by a double. */
	public void div(double value) {
		BigDecimal bd = ShortDecimal.newBigDecimal(value);
		div(bd);
	}
	/** Divide this decimal value by an integer. */
	public void div(int value) {
		BigDecimal bd = BigDecimal.valueOf(value);
		div(bd);
	}
	/** Divide this decimal value by a long. */
	public void div(long value) {
		BigDecimal bd = BigDecimal.valueOf(value);
		div(bd);
	}
	/** Divide this decimal value by another one. */
	public void div(INumeric value) {
		div(value.toBigDecimal());
	}

	/** Return the double equivalent of this value. */
	public double doubleValue() {
		double d = decimalValue.doubleValue();
		return d;
	}
	/** Return the int equivalent of this value. */
	public int intValue()
	{
		return decimalValue.intValue();
	}
	
	/* (non-Javadoc)
	 * @see com.asc.rio.DecimalData#negate()
	public longDecimal negate() {
		longDecimal dec = (longDecimal)super.clone();
		dec.decimalValue = decimalValue.negate();
		return dec;
	}
	 */

	/** Set the unscaled value of this decimal. */
	public void setUnscaledValue(long unscaledValue)
	{
		BigInteger bi = BigInteger.valueOf(unscaledValue);
		BigDecimal bd = new BigDecimal(bi, Scale);
		assign(bd);
	}
	/** Return the unscaled value of this decimal. */
	public long unscaledValue() {
		// The J# compiler doesn't support the unscaledValue() call
		//long l = decimalValue.unscaledValue.longValue();
		long l = decimalValue.movePointRight(decimalValue.scale()).longValue();
		return l;
	}
	
	/* (non-Javadoc)
	 * @see com.asc.rio.INumeric#toNumericString()
	 */
	public String toNumericString() {
		return toZoned().toNumericString();
	}
}
