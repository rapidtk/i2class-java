package com.i2class;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A mutable fixed-length/Scale decimal data type for exact arithmetic.
 * 
 * 
 *
 */
abstract class DecimalData implements INumeric, IFixed, Cloneable, Serializable {
	protected int length, Scale;
	
	/** Construct a decimal object with no length or scale. */
	DecimalData()
	{
	}
	/** Construct a decimal object with the specified length and scale. */
	DecimalData(int length, int scale)
	{
		this.length = length;
		this.Scale = scale;
	}
	
	/** Add another decimal value to this one. */
	public void add(BigDecimal value) {
		BigDecimal bd = toBigDecimal().add(value);
		assign(bd);
	}
	/** Add a double value to this decimal value. */
	public abstract void add(double value);
	
	/** Add a integer value to this decimal value. */
	public abstract void add(int value);
	
	/** Add another decimal value to this one. */
	public abstract void add(INumeric value);

	/** Assign a BigDecimal value to this decimal. */
	public abstract void assign(BigDecimal bd);
	
	/** Assign a double value to this decimal. */
	public abstract void assign(double value);
	
	/** Assign a numeric figurative constant value to this decimal. */
	public void assign(FigConstNum value) {
		assign(value.decimalValue(length, Scale));
	}

	/** Assign a double value to this decimal. */
	public abstract void assign(int value);

	/** Assign a IDecimal value to this decimal. */
	public abstract void assign(INumeric value);
	
	/** Clone this decimal object. */ 
	public abstract Object clone();
	/*
	{
		try
		{
			DecimalData cloned = (DecimalData)super.clone();
			cloned.assign(this);
			return cloned;
		}
		catch (Exception e)
		{
			return null;
		}
	}
	*/
	
	/** Compare to a double value. */
	public int compareTo(double value) {
		//return toBigDecimal().compareTo(new BigDecimal(Double.toString(value)));
		return AbstractNumeric.compareDouble(doubleValue(), value);
	}

	/** Compare the value of this decimal to another object. */
	public abstract int compareTo(Object o) throws ClassCastException;
	

	/** Return the correct decimal/longDecimal type for the specified length, scale */
	static DecimalData decimal(int length, int scale)
	{
		if (length<=18)
			return new ShortDecimal(length, scale);
		return new LongDecimal(length, scale);
	}
	
	/** Divide this decimal value by another one. */
	public void div(BigDecimal value) {
		BigDecimal bd = toBigDecimal().divide(value, BigDecimal.ROUND_HALF_UP);
		assign(bd);
	}
	/** Divide this decimal value by a double value. */
	public abstract void div(double value) ;
	
	/** Divide this decimal value by a integer value. */
	public abstract void div(int value);
	
	/** Divide this decimal value by another one. */
	public abstract void div(INumeric value);

	/** Divide this decimal value by another one and return a new object. */
	public LongDecimal dividedBy(BigDecimal value)
	{
		return AbstractNumeric.dividedBy(toBigDecimal(), value);
	}
	/** Divide this decimal value by another one and return a new object. */
	public LongDecimal dividedBy(double value)
	{
		return dividedBy(ShortDecimal.newBigDecimal(value));
	}
	/** Divide this decimal value by another one and return a new object. */
	public LongDecimal dividedBy(INumeric value)
	{
		return dividedBy(value.toBigDecimal());
	}
	
	/** Return the double equivalent of this value. */
	public abstract double doubleValue();

	/** Compare to a double value. */
	public boolean equals(double value) {
		return (doubleValue() == value);
	}
	/** Compare this decimal data to a figurative constant (for example *HIVAL, *LOVAL). */
	public boolean equals(FigConst fc)
	{
		return (compareTo(fc) == 0);
	}
	
	/** Return the integer equivalent of this value. */
	public int intValue() {
		return (int) doubleValue();
	}

	/** Return the length (number of digits) of this value. */
	public final int len() {
		return this.length;
	}

	/** Return the long equivalent of this value. */
	public long longValue() {
		return (long) doubleValue();
	}

	/** Move a boolean ('1', '0') value to the right-most digit of this value. */	
	public DecimalData move(boolean value) {
		ZonedDecimal z = toZoned();
		z.move(value);
		assign(z);
		return this;
	}
	
	/** Move a character value to the right-most digit of this value. */	
	public DecimalData move(char value) {
		ZonedDecimal z = toZoned();
		z.move(value);
		assign(z);
		return this;
	}

	/** Move a character array to the right-most digits of this value. */	
	public DecimalData move(char[] value) {
		ZonedDecimal z = toZoned();
		z.move(value);
		assign(z);
		return this;
	}

	/** Move fixed-length data to the right-most digits of this value. */	
	public DecimalData move(IFixed value) {
		ZonedDecimal z = toZoned();
		z.move(value);
		assign(z);
		return this;
	}

	/** Move a String to the right-most digits of this value. */	
	public DecimalData move(String value) {
		ZonedDecimal z = toZoned();
		z.move(value);
		assign(z);
		return this;
	}
	/** Move all - fill the byte array associated with this fixed-length string with a repeating occurence of a character. */
	public void moveall(char c)
	{
		moveall(c, 1);
	}

	/** 
	 * Move all - fill the byte array associated with this fixed-length string with a repeating occurence of a character. 
	 * @param c The character to move
	 * @param index The 1-based index into the fixed-length string to change
	 */
	public void moveall(char c, int index)
	{
		ZonedDecimal z = toZoned();
		z.moveall(c, index);
		assign(z);
	}

	/** 
	 * Move all - fill the byte array associated with this fixed-length string with a repeating occurence of a character. 
	 * @see #moveall(char c, int index)
	 */
	public void moveall(char c, INumeric index)
	{
		moveall(c, index.intValue());
	}

	/** 
	 * Move all - fill the byte array associated with this fixed-length string with a repeating occurence of a string. 
	 * For example, with a <code>fixed(6)</code> with the value "ABCDEF", <code>moveall("GH", 2)</code> would 
	 * change the contents to "AGHGHG".
	 * @param c The string to move
	 * @param index The 1-based index into the fixed-length string to change
	 */
	public void moveall(String str, int index)
	{
		ZonedDecimal z = toZoned();
		z.moveall(str, index);
		assign(z);
	}

	/** 
	 * Move all - fill the byte array associated with this fixed-length string with a repeating occurence of a string. 
	 * @see #moveall(String str, int index)
	 */
	public void moveall(String str, INumeric index)
	{
		moveall(str, index.intValue());
	}
	

	/** Move a boolean ('1', '0') value to the right-most digit of this value. */	
	public DecimalData movel(boolean value) {
		ZonedDecimal z = toZoned();
		z.movel(value);
		assign(z);
		return this;
	}
	
	/** Move a character value to the right-most digit of this value. */	
	public DecimalData movel(char value) {
		ZonedDecimal z = toZoned();
		z.movel(value);
		assign(z);
		return this;
	}

	/** Move a character array to the right-most digits of this value. */	
	public DecimalData movel(char[] value) {
		ZonedDecimal z = toZoned();
		z.movel(value);
		assign(z);
		return this;
	}

	/** Move fixed-length data to the right-most digits of this value. */	
	public DecimalData movel(IFixed value) {
		ZonedDecimal z = toZoned();
		z.movel(value);
		assign(z);
		return this;
	}

	/** Move a String to the right-most digits of this value. */	
	public DecimalData movel(String value) {
		ZonedDecimal z = toZoned();
		z.movel(value);
		assign(z);
		return this;
	}

	/** Subtract and return a new object. */
	public LongDecimal minus(BigDecimal value)
	{
		return AbstractNumeric.minus(toBigDecimal(), value);
	}
	/** Subtract and return a new object. */
	public LongDecimal minus(double value)
	{
		return minus(ShortDecimal.newBigDecimal(value));
	}
	/** Subtract and return a new object. */
	public LongDecimal minus(INumeric value)
	{
		return minus(value.toBigDecimal());
	}


	/** Multiply this decimal value by another one. */
	public void mult(BigDecimal value) {
		BigDecimal bd = toBigDecimal().multiply(value);
		assign(bd);
	}
	/** Multiply this decimal value by a double value. */
	public abstract void mult(double value);
	
	/** Multiply this decimal value by a integer one. */
	public abstract void mult(int value);
	
	/** Multiply this decimal value by another one. */
	public abstract void mult(INumeric value);

	/* (non-Javadoc)
	 * @see com.i2class.INumeric#negate()
	 */
	public INumeric negate() {
		DecimalData dec = (DecimalData)clone();
		dec.mult(-1);
		return dec;
	}


	/**
	 * Add and return a new object (similar to BigDecimal add).
	 */
	public LongDecimal plus(BigDecimal value)
	{
		return AbstractNumeric.plus(toBigDecimal(), value);
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

	/** Return the scale of this decimal value. */
	public int scale() {
		return Scale;
	}

	public void setScale(int scale) {
		this.Scale = scale;
	}
	/** Set the unscaled value of this decimal. */
	public abstract void setUnscaledValue(long unscaledValue);
	
	/** Return the short equivalent of this value. */
	public short shortValue() {
		return (short) doubleValue();
	}

	/** Subtract another decimal value from this one. */
	public void sub(BigDecimal value) {
		BigDecimal bd = toBigDecimal().subtract(value);
		assign(bd);
	}
	/** Subtract a double value from this decimal one. */
	public abstract void sub(double value) ;
	
	/** Subtract a integer value from this decimal one. */
	public abstract void sub(int value) ;
	
	/** Subtract another decimal value from this one. */
	public abstract void sub(INumeric value) ;

	/** Multiply this decimal value by another one and return a new object. */
	public LongDecimal times(BigDecimal value)
	{
		return AbstractNumeric.times(toBigDecimal(), value);
	}
	/** Multiply this decimal value by another one and return a new object. */
	public LongDecimal times(double value)
	{
		return times(ShortDecimal.newBigDecimal(value));
	}
	/** Multiply this decimal value by another one and return a new object. */
	public LongDecimal times(INumeric value)
	{
		return times(value.toBigDecimal());
	}

	/** Generate the BigDecimal equivalent of this number */
	public abstract BigDecimal toBigDecimal();
	
	public BigInteger toBigInteger() {
		return toBigDecimal().toBigInteger();
	}

	/** Return the decimal value of this object. */
	final public DecimalData toDecimal() {
		return this;
	}

	/* Create a zoned decimal equivalent of this object. */
	public FixedData toFixedChar() {
		return toZoned();
	}

	public String toString() {
		return toBigDecimal().toString();
	}
	



	/* Create a zoned decimal equivalent of this object. */
	public ZonedDecimal toZoned() {
		ZonedDecimal zonedValue = new ZonedDecimal(length, Scale);
		zonedValue.assign(this);
		return zonedValue;
	}
	/** Return the unscaled value of this decimal. */
	public abstract long unscaledValue() ;
	
	/*
	public static void main (String args[])
	{
		decimal value = new decimal(15,2);
		value.assign(131550.72d);
		double d = value.doubleValue();
		value.assign(131550.73d);
		d = value.doubleValue();
		value.add(.01);
	}
	*/
}
