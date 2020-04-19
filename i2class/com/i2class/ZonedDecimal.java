package com.i2class;

import java.math.*;

/**
 * A mutable, fixed-length 
 * <a href="http://publib.boulder.ibm.com/iseries/v5r2/ic2924/books/c092508415.htm#HDRZODECFO">zoned decimal</a> data type.
 * A <code>zoned</code> type has a fixed length and scale, for example zoned(9,2) means nine total digits with
 * two digits to the right of the decimal point.  
 * Zoned numeric data is stored internally as a string of numeric digits.  If the number is negative, the last digit
 * is the equivalent of that EBCDIC character logically anded (&) with 0xD0.
 * In EBCDIC, the numeric characters 0-9 are stored as 0xF0-0xF9, so the last digit will be
 * in the range 0xD0-0xD9 (the character '{', and the characters J-R).
 * For example, the zoned 5,2 number 12.34 is internally stored as '01234'.  
 * The negative number -12.34 would be stored as '0123M'.
 * Calculations done with the <code>zoned</code> data type are exact, i.e. they do not lose digits
 * or precision like a double value can.
 *  
 */
public class ZonedDecimal extends AbstractNumeric {

	ZonedDecimal(int length) {
		super(length, length, 0);
	}
	/*
	private zoned updateThis()
	{
		updateSubfields();0
		return this;
	}
	*/
	/*
		public zoned(double val) {
			super(val);
		}
		public zoned(String val) {
			super(val);
		}
		public zoned(java.math.BigInteger val) {
			super(val);
		}
		public zoned(java.math.BigInteger unscaledVal, int scale) {
			super(unscaledVal, scale);
		}
	*/

	/** Create a zoned decimal object with the specified length, scale, initialized to 0. */
	public ZonedDecimal(int length, int scale) {
		super(length, length, scale);
		fillArray((byte)'0');
	}
	/** 
	 * Create a zoned decimal object with the specified length, scale, 
	 * initialized to the contents of the specified byte array at the 
	 * specified 0-based index.
	 */
	public ZonedDecimal(int length, int scale, byte[] array, int offset) {
		super(length, length, scale);
		arrayCopy(0, array, offset);
	}
	/** Create a zoned decimal value that overlays another value. */
	public ZonedDecimal(int length, int scale, FixedPointer overlay)
	{
		super(length, length, scale, overlay);
	}

	
	/**
	 * Create a zoned decimal value initialized to a certain value.
	 * @param length The maximum length (total number of digits) of the value
	 * @param scale The scale (number of digits to the right of the decimal point) of the value
	 * @param value The initial value
	 */
	public ZonedDecimal(int length, int scale, BigDecimal bd) {
		super(length, length, scale);
		assign(bd);
	}

	/** Assign a double value to this number. */
	public void assign(double d) {
		assign(d, Application.ROUND_DOWN);
	}
	/** 
	 * Create a zoned decimal object with the specified length, scale, 
	 * and initialized to the specified double value. 
	 * @see #zoned(int length, int scale, BigDecimal bd)
	 */
	public ZonedDecimal(int length, int scale, double value) {
		this(length, scale);
		assign(value);
	}
	
	/** 
	 * Create a zoned decimal object with the specified length, scale, 
	 * and initialized to the specified string value. 
	 * @see #zoned(int length, int scale, BigDecimal bd)
	 */
	public ZonedDecimal(int length, int scale, String value) {
		this(length, scale);
		assign(Double.parseDouble(value));
	}
	/**
	 * Create a zoned decimal value with the specified length and scale initialized
	 * to the specified figurative constant.
	 */
	public ZonedDecimal(int length, int scale, FigConstNum fc)
	{
		this(length, scale);
		assign(fc);
	}
	
	/** Assign a rounded double value to this number. */
	public void assign(double d, int roundingMode) {
		//BigDecimal bd = new BigDecimal(d);
		//assign(bd);
		tempDecimal_.assign(d, roundingMode);
		assign((INumeric)tempDecimal_);
		//return (zoned)updateThis();
		//updateThis();
	}
	/** Assign an int value to this number. */
	public void assign(int value) {
		assign((long)value);
	}
	/** Assign a long value to this number. */
	public void assign(long value) {
		tempDecimal_.assign(value);
		assign((INumeric)tempDecimal_);
	}

	/**
	 * @throws SignificantDigitsLostException if the value to assign would require decimal digits to be lost.
	 */
	protected final void assignNumericString(String str, boolean negative) throws SignificantDigitsLostException
	{
		int strLength = str.length();
		int size = size();
		int i = strLength-size-1;
		// If any of the values in the assignment string are not '0', then decimal
		// digits are lost.
		for (; i >= 0; i--)
		{
			if (str.charAt(i)!='0')
				throw new SignificantDigitsLostException(this, str);
		}
		
		move(str);
		for (i=size-strLength-1; i >= 0; i--)
			setCharAt(i, '0');
		if (negative)
			changeSign();
		//return (zoned)updateThis();
		updateThis();
	}

	/** Assign a new value to this number. */
	public void /*zoned*/ assign(BigDecimal bd) {
		assign(bd, Application.ROUND_DOWN);
	}
	/** Assign a rounded value to this number. */
	public void /*zoned*/ assign(BigDecimal bd, int roundingMode) {
		if (bd.scale() != Scale)
			bd = bd.setScale(Scale, roundingMode);
	
		boolean negative = bd.signum() < 0;
		// remove negative sign
		/* The J# compiler doesn't have any deleteXXX() functions, so do it the old way
		StringBuffer sb = new StringBuffer(bd.toString());
		if (negative)
			sb.deleteCharAt(0);
		if (scale>0)
			sb.deleteCharAt(sb.length()-scale-1);
		assignNumericString(sb.toString(), negative);
		*/
		if (negative)
			bd = bd.negate();
		// remove decimal point
		if (Scale > 0)
			// J# compiler doesn't support unscaledValue()
			//str = bd.unscaledValue().toString();
			bd = bd.movePointRight(Scale);
		assignNumericString(bd.toString(), negative);
	}

	/** Assign a numeric value to this number. */
	public void assign(INumeric value) {
		assign(value, Application.ROUND_DOWN);
	}
	/** Assign a rounded numeric value to this number. */
	public void assign(INumeric value, int roundingMode) {
		tempDecimal_.assign(value, roundingMode);
		long unscaledValue = tempDecimal_.unscaledValue();
		boolean negative = unscaledValue < 0;
		if (negative)
			unscaledValue = -unscaledValue;
		String str = Long.toString(unscaledValue);
		assignNumericString(str, negative);
	}

	/** Toggle the sign (-/+) of the value. */
	public void changeSign() {
		int offset = msize()-1;
		char sign = (char) sbyteAt(offset);
		if (Character.isDigit(sign)) {
			if (sign == '0')
				setByteAt(offset, (byte) '}');
			else
				setByteAt(offset,  (byte) ('J' + (sign - '1')));
		} else {
			if (sign == '}')
				setByteAt(offset,  (byte) '0');
			else
				setByteAt(offset,  (byte) ('1' + (sign - 'J')));
		}
	}

	/** Scan a fixed length character string for blanks. */
	private static int scanFixedBlanks(FixedData fStr)
	{
		int flength=fStr.len();
		for (int i=0; i<flength; i++)
		{
			if (fStr.sbyteAt(i)==' ')
				return i;
		}
		return -1;
	}

	/** 
	 * Move a character value to the right-most byte of this variable.  
	 * Translate blanks to '0'.
	 */
	public FixedData move(char c)
	{
		if (c==' ')
			c='0';
		return super.move(c);
	}
	/** 
	 * Move a string to the right-most bytes of this variable.
	 * Translate blanks to '0'.
	 */
	public FixedData move(String str)
	{
		return super.move(str.replace(' ', '0'));
	}
	/** 
	 * Move fixed-length data to the right-most bytes of this variable.
	 * Translate blanks to '0'.
	 */
	public FixedData move(IFixed fStr)
	{
		//return move(value.toFixedString());
		// We don't want to have to create a String object for every move, so check for blanks first
		FixedData f = fStr.toFixedChar();
		if (scanFixedBlanks(f)>=0)
			return move(f.toFixedString());
		return super.move(f);
	}
	public FixedData move(FixedDate value)
	{
		return move(moveDateString(value));
	}
	
	/** 
	 * Move a character value to the left-most byte of this variable.  
	 * Translate blanks to '0'.
	 */
	public FixedData movel(char c)
	{
		if (c==' ')
			c='0';
		return super.movel(c);
	}
	/** 
	 * Move a string to the left-most bytes of this variable.
	 * Translate blanks to '0'.
	 */
	public FixedData movel(String str)
	{
		return super.movel(str.replace(' ', '0'));
	}
	/** 
	 * Move fixed-length data to the left-most bytes of this variable.
	 * Translate blanks to '0'.
	 */
	public FixedData movel(IFixed fStr)
	{
		//return movel(value.toFixedString());
		// We don't want to have to create a String object for every move, so check for blanks first
		FixedData f = fStr.toFixedChar();
		if (scanFixedBlanks(f)>=0)
			return movel(f.toFixedString());
		return super.movel(f);
	}

	/** 
	 * Return a new zoned object whose sign (-/+) has been toggled.
	 * @see #changeSign()
	 */
	public INumeric negate() {
		ZonedDecimal z = (ZonedDecimal) super.clone();
		z.changeSign();
		return z;
	}
	/*
	public short shortValue()
	{
		return (short)intValue();
	}
	*/
	/*
	public zoned add(BigDecimal val)
	{
		BigDecimal bd = toBigDecimal();
		bd = bd.add(val);
		zoned z = new zoned(overlay.length, precision, bd);
		return z;
	}
	*/

	/** 
	 * Return the sign of the value.
	 * @see BigDecimal#signum()
	 */
	public int signum() {
		rSubfields();
		char sign = ubyteAt(msize()-1);
		if (sign == '0')
			return 0;
		else if (Character.isDigit(sign))
			return 1;
		else
			return -1;
	}

	/** Return the I2 decimal representation of this value. */
	public DecimalData toDecimal() {
		rSubfields();
		boolean negative = (signum() < 0);
		String s;
		ZonedDecimal z;
		int multiplier;
		if (negative)
		{
			z = (ZonedDecimal)this.negate();
			multiplier=-1;
		}
		else
		{
			z= this;
			multiplier=1;
		}
		s = new String(z.toFixedString());
		long l = Long.parseLong(s)*multiplier;
		tempDecimal_.setUnscaledValue(l);
		return tempDecimal_;
	}

	/** Return the numeric string representation of this value. */
	public String toNumericString() {
		rSubfields();
		boolean negative = (signum() < 0);
		ZonedDecimal z;
		if (negative)
			z = (ZonedDecimal)this.negate();
		else
			z = this;
		int zmoffset = z.getOffset();
		int zmsize=z.msize();
		String s = new String(z.getOverlay(), zmoffset, zmsize);
		if (negative || Scale>0)
		{
			StringBuffer sb = new StringBuffer(s);
			if (Scale>0)
				sb.insert(zmsize-Scale,'.');
			if (negative)
				sb.insert(0,'-');
			s = sb.toString();
		}
		return s;
	}

	/** Return the Java BigDecimal representation of this value. */
	public BigDecimal toBigDecimal() {
		/*
		readSubfields();
		boolean negative = (signum() < 0);
		String s;
		if (negative) {
			zoned z = (zoned)this.negate();
			s = new String(z.overlay);
		} else
			s = new String(overlay);
		BigDecimal bd;
		if (scale > 0) {
			BigInteger bi = new BigInteger(s);
			bd = new BigDecimal(bi, scale);
		} else
			bd = new BigDecimal(s);
		if (negative)
			bd = bd.negate();
		return bd;
		*/
		return ShortDecimal.newBigDecimal(toNumericString());
	}

	/**
	 * Return the fixed-string representation (as opposed to the human-friendly toString()) of this value.
	 * This returns the raw, unformatted values of the byte string.
	 */
	public String toFixedString() {
		rSubfields();
		int moffset = getOffset();
		return new String(getOverlay(), moffset, msize());
	}

	/** Return the zoned representation of this value. Always just return <code>this</code>.*/
	public ZonedDecimal toZoned() {
		return this;
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractNumeric#assignZoned(com.asc.rio.zoned)
	 */
	protected void assignZoned(ZonedDecimal z) {
		// Nothing gets done here because we are assigning a value to itself
		return;
	}
	
	public static void main(String[] args)
	{
		ZonedDecimal z5 = new ZonedDecimal(5,0);
		z5.assign(-2);
		/*
		fixed f = new fixed(3, "345");
		zoned z = new zoned(3,0, new pointer(f, 0));
		byte[] bytes = {'0','1','2'};
		z.assign(bytes, 0);
		String s = z.toFixedString();
	*/
	}
}
