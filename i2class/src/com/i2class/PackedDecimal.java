package com.i2class;

import java.math.*;

/**
 * A mutable, fixed-length 
 * <a href="http://publib.boulder.ibm.com/iseries/v5r2/ic2924/books/c092508415.htm#HDRPADECFO">packed decimal</a> data type.
 * A <code>packed</code> type has a fixed length and scale, for example packed(9,2) means nine total digits with
 * two digits to the right of the decimal point.  
 * The size of the data (in bytes) is always (int)((length-1)/2)+1.
 * Packed numeric data is represented by a high and low nibble (4 bits) of data.  The last nibble of data
 * contains the sign of the number: 0x0D is negative, 0x0F is positive.
 * For example, the packed 5,2 number 12.34 is internally stored as 0x01234F.
 * Calculations done with the <code>packed</code> data type are exact, i.e. they do not lose digits
 * or precision like a double value can.
 *  
 */

/* For now, packed assumes that no fields overlay it.  Have to add readSubfield()/updateThis() if not true. */
public class PackedDecimal extends AbstractNumericCoded
{

	/**
	 * Insert the method's description here.
	 * Creation date: (11/20/2001 10:21:26 AM)
	 * @return com.i2class.zoned
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
	private int digits;
	private byte[] zoverlay_;

	/**
	 * Create a packed decimal value with the specified length and scale.
	 */
	public PackedDecimal(int length, int scale)
	{
		super(length / 2 + 1, length, scale);
		digits = length;
		// Instead of assigning 0 which goes through all sorts of convulsions, 
		// just set the raw byte value instead.
		//assign(0);
		//java.util.Arrays.fill(overlay, 0, overlay.length, (byte)0x0F);
		java.util.Arrays.fill(getOverlay(), 0, getOverlay().length-1, (byte)0x00);
		getOverlay()[getOverlay().length-1]=(byte)0x0F;
		//precision=scale;
	}
	/** Create a packed decimal value that overlays another value. */
	public PackedDecimal(int length, int scale, FixedPointer overlay)
	{
		super(length / 2 + 1, length, scale, overlay);
		digits = length;
	}
	/**
	 * Create a packed decimal value initialized to a certain offset of a byte string.
	 * @param length The maximum length (total number of digits) of the value
	 * @param scale The scale (number of digits to the right of the decimal point) of the value
	 * @param array The byte array that the initial value will be set to
	 * @param int index The 1-based index into <code>array</code> to begin copying values from
	 */
	public PackedDecimal(int length, int scale, byte[] array, int offset)
	{
		this(length, scale);
		/*
		for (int i=0; i<length && offset<array.length; i++)
		{
			overlay[i]=array[offset];
			offset++;
		}
		*/
		System.arraycopy(array, offset, getOverlay(), 0, getOverlay().length);
	}
	/**
	 * Create a packed decimal value with the specified length and scale initialized
	 * to the specified value.
	 */
	public PackedDecimal(int length, int scale, double value)
	{
		this(length, scale);
		assign(value);
	}
	/**
	 * Create a packed decimal value with the specified length and scale initialized
	 * to the specified string value.
	 */
	public PackedDecimal(int length, int scale, String value)
	{
		this(length, scale);
		assign(ShortDecimal.newBigDecimal(value));
	}
	/**
	 * Create a packed decimal value with the specified length and scale initialized
	 * to the specified BigDecimal value.
	 */
	public PackedDecimal(int length, int scale, BigDecimal bd)
	{
		this(length, scale);
		assign(bd);
	}
	/**
	 * Create a packed decimal value with the specified length and scale initialized
	 * to the specified figurative constant.
	 */
	public PackedDecimal(int length, int scale, FigConstNum fc)
	{
		this(length, scale);
		assign(fc);
	}
	
	/** Assign a double value to this variable. */
	public void assign(double d)
	{
		assign(d, Application.ROUND_DOWN);
	}
	/** Assign a rounded double value to this variable. */
	public void assign(double d, int roundingMode)
	{
		//BigDecimal bd = new BigDecimal(d);
		//assign(bd);
		tempDecimal_.assign(d, roundingMode);
		assign((INumeric)tempDecimal_);
	}
	/**
	 * Assign a numeric figurative constant to this packed number.
	public void assign(FigConstNum value)
	{
		assign(value.doubleValue());
	}
	 */
	
	/** Assign a zoned value of equal scale to this number. */
	protected void assignZoned(ZonedDecimal z)
	{
		boolean negative = (z.signum() < 0);
		if (negative)
			z.changeSign();
		// pack bytes
		int i = 0;
		// j will be -1 for even# digits
		int size1=size()-1; 
		int j = len() - ((size1)*2+1);
		for (; i < size1; i++)
		{
			int nib1 = 0;
			if (j>=0)
				nib1 = z.sbyteAt(j) << 4;
			setByteAt(i, (byte) (nib1 | (z.sbyteAt(j + 1) & 0x0F)));
			j = j + 2;
		}
		// pack final byte
		byte signNibble;
		if (negative)
			signNibble = 0xD;
		else
			signNibble = 0xF;
		//if (j < len())
		if (j < z.msize())
			setByteAt(i, (byte) (z.sbyteAt(j) << 4 | signNibble));
		else
			setByteAt(i, (byte) (sbyteAt(i) & 0xFF | signNibble));
		//updateThis();
		//return this;
	}

	/** Assign a BigDecimal value to this variable. */
	public void assign(BigDecimal bd)
	{
		assign(bd, Application.ROUND_DOWN);
	}
	/** Assign a rounded BigDecimal value to this variable. */
	public void assign(BigDecimal bd, int roundingMode)
	{
		ZonedDecimal z = toZoned();
		z.assign(bd, roundingMode);
		assignZoned(z);
	}
	/** Assign an int value to this number. */
	public void assign(int value) {
		assign((long)value);
	}
	/** Assign a long value to this number. */
	public void assign(long value) {
		ZonedDecimal z = toZoned();
		z.assign(value);
		assignZoned(z);
	}
	/** Assign a Decimal value to this variable. */
	public void assign(INumeric value)
	{
		assign(value, Application.ROUND_DOWN);
	}
	/** Assign a rounded Decimal value to this variable. */
	public void assign(INumeric value, int roundingMode)
	{
		ZonedDecimal z = toZoned();
		z.assign(value, roundingMode);
		assignZoned(z);
	}

	/** Toggle the sign (-/+) of the value. */
	// For packed decimal numbers, the sign is either 0x0F (positive) or 0x0D (negative)
	public void changeSign() {
		// The bit manipulation functions always cast to int, so extract just what we need.
		int offset = msize()-1;
		byte sign = (byte)((sbyteAt(offset) ^ 0x02) & 0x000000FF);
		setByteAt(offset, sign);
	}


	final public int len()
	{
		//return DigitsOf()*2-1;
		return digits;
	}

	/**
	 * Move a packed value to the right-most bytes of this variable.
	 */
	public FixedData move(PackedDecimal value)
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
	 * Move a packed value to the left-most bytes of this variable.
	 */
	public FixedData movel(PackedDecimal value)
	{
		//return super.move((FixedData) value);
		// If the value that is being moved is >= the length of this value and has the same scale 
		// then we can just copy bytes.
		if (value.Scale == Scale && value.msize()==msize())
			return movelFixedData(value);
		return movel(value.toZoned());
	}

	/** 
	 * Return a new packed object whose sign (-/+) has been toggled.
	 * @see #changeSign()
	 */
	public INumeric negate() {
		PackedDecimal p = (PackedDecimal) this.clone();
		p.changeSign();
		return p;
	}


	/** Return the BigDecimal representation of this number. */
	public BigDecimal toBigDecimal()
	{
		return toZoned().toBigDecimal();
	}
	/** Return a character array that represents the digits of this number. */
	public char[] toCharArray()
	{
		return toZoned().toCharArray();
	}

	/** 
	 * Set the temporary zoverlay byte array.
	 * @return -1 if the value is negative, 1 otherwise. 
	 */ 
	private int zoverlay() {
		//int zLength = len();
		int msize=msize();
		int zLength = (msize-1)*2+1; // This could be different than len() because of even# of digits
		if (zoverlay_ == null)
			zoverlay_ = new byte[zLength];
		// unpack bytes
		int i = 0, j = 0;
		for (; i < msize - 1; i++)
		{
			/// Arggggh!  Those suckin' java shift (>>/>) operators cast everything to 32-bits before doing the shift, so 'negative'
			// numbers (8 and 9) in the left-most bits don't shift correctly.  Use 0x0F mask to get what we really want.
			zoverlay_[j] = (byte) ('0' | (sbyteAt(i) >>> 4 & 0x0F));
			j++;
			zoverlay_[j] = (byte) ('0' | sbyteAt(i) & 0x0F);
			j++;
		}
		// For evenly packed (e.g. 2,0 6,0) numbers, this byte doesn't need to be set
		if (j < zLength)
			zoverlay_[j] = (byte) ('0' | (sbyteAt(i) >>> 4 & 0x0F));
		if ((sbyteAt(i) & 0x0F) == 0X0D)
			return -1;
		return 1;
	}
	/** Set the temporary decimal object. */ 
	public DecimalData toDecimal() {
		int multiplier = zoverlay(); // Create overlay string
		long l = Long.parseLong(new String(zoverlay_)) * multiplier;
		tempDecimal_.setUnscaledValue(l);
		return tempDecimal_;
	}
	
	/* (non-Javadoc)
	 * @see com.i2class.AbstractNumeric#toNumericString()
	 */
	public String toNumericString() {
		return toZoned().toNumericString();
	}

	/** Return the zoned decimal equivalent of this number. */
	public ZonedDecimal toZoned()
	{
		int zLength = len();
		ZonedDecimal z = new ZonedDecimal(zLength, Scale);
		int multiplier = zoverlay();
		// zLength and z.overlay.length may be different if this is a packed number with an even# of digits
		//System.arraycopy(zoverlay_, 0, z.overlay, 0, zLength);
		System.arraycopy(zoverlay_, zoverlay_.length-zLength, z.getOverlay(), 0, zLength);
		if (multiplier<0)
			z.changeSign();
		return z;
	}
	
	/** Return the zoned decimal equivalent of this number. */
	public FixedData toFixedChar()
	{
		return toZoned();
	}
	
	/** Return the fixed-string representation (as opposed to the human-friendly toString()) of this value. */
	public String toFixedString() {
		return toZoned().toFixedString();
	}

	public static void main(String args[])
	{
		PackedDecimal p5 = new PackedDecimal(5,0);
		p5.assign(-2);
/*		
		p8.assign(20030102);
		packed p4 = new packed(4,0);
		p4.movel(p8);
		p8.movel(p4);
		p4.move(p8); // 0605
		p8.move(p4);
		zoned z8 = new zoned(8,0);
		z8.move(p4);
		z8.movel(p4);
		zoned z4 = new zoned(4,0);
		z4.move(p8);
		z4.movel(p8);
		z8.movel(z4);
		z8.move(z4);
		fixed f8 = new fixed(8);
		f8.move(z4);
		f8.move(p4);
		f8.movel(z4);
		f8.movel(p4);
		f8.move(z8);
		f8.movel(z8);
		f8.move(p4);
		f8.move(p8);
		p4.move(p8);

		packed p2 = new packed(2,0);
		p2.assign(1);
		String s = p2.toString();
		packed p3 = new packed(3,0);
		p3.assign(1);
		s = p3.toString();
		p3.movel("12345");
		s = p3.toFixedString();
		s = p3.toString();
		p3.move("12345");
*/
	}
	
}
