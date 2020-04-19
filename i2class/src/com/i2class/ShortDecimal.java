package com.i2class;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A mutable fixed-length, scale (<=18 digits) decimal data type for exact arithmetic.
 * 
 * 
 *
 */
public /*final*/ class ShortDecimal extends DecimalData implements IFixed {
	static final long pow10[] =
		{
			1,
			10,
			100,
			1000,
			10000,
			100000,
			1000000,
			10000000,
			100000000,
			1000000000,
			10000000000L,
			100000000000L,
			1000000000000L,
			10000000000000L,
			100000000000000L,
			1000000000000000L,
			10000000000000000L,
			100000000000000000L,
			1000000000000000000L };
	private long adjustedValue, adjustedUnscaledValue;
	private ShortDecimal decimalValue_;
	private int scaleDif;
	protected long unscaledValue;

	static public Integer MINUS_ONE = new Integer(-1);
	static public Integer ZERO = new Integer(0);
	static public Integer ONE = new Integer(1);

	/** Construct a decimal object with no length, scale.. */
	ShortDecimal() {
	}
	/** Construct a decimal object with the specified length and scale. */
	public ShortDecimal(int length, int scale)
	{
		super(length, scale);
		//if (length>18)
		//	throw new EInvalidLength("No more than 18 digits can be specified for numeric length.",length,scale);
		decimalValue_ = new ShortDecimal();
		decimalValue_.length = length;
		decimalValue_.Scale = scale;
	}
	/** Construct a decimal object with the specified length and scale and initial value. */
	public ShortDecimal(int length, int scale, FigConstNum value) {
		this(length, scale);
		assign(value);
	}
	/** Construct a decimal object with the specified length and scale and initial value. */
	public ShortDecimal(int length, int scale, double value) throws Exception {
		this(length, scale);
		assign(value);
	}

	/** Add a double value to this decimal value. */
	public void add(double value) {
		decimalValue_.assign(value);
		add(decimalValue_);
	}
	/** Add a integer value to this decimal value. */
	public void add(int value) {
		decimalValue_.assign(value);
		add(decimalValue_);
	}
	/** Add another decimal value to this one. */
	public void add(INumeric value) {
		DecimalData id = value.toDecimal();
		addUnscaledValue(id.unscaledValue(), id.scale());
	}

	// Add the unscaled value with the specified scale to this decimal
	private void addUnscaledValue(long value, int valueScale) {
		// Adjust the decimal positions so that the long values are right-adjusted
		scaleDif = Scale - valueScale;
		if (scaleDif >= 0) {
			adjustedUnscaledValue = unscaledValue;
			adjustedValue = value * pow10[scaleDif];
		} else {
			adjustedUnscaledValue = unscaledValue * pow10[-scaleDif];
			adjustedValue = value;
		}
		// Perform addition
		adjustedUnscaledValue += adjustedValue;
		// Put back adjustment
		if (scaleDif < 0)
			unscaledValue = adjustedUnscaledValue / pow10[-scaleDif];
		else
			unscaledValue = adjustedUnscaledValue;
	}

	/** Assign a BigDecimal value to this decimal. */
	public void assign(BigDecimal bd) {
		assign(bd, Application.ROUND_DOWN);
	}
	/** Assign a rounded BigDecimal value to this decimal. */
	public void assign(BigDecimal bd, int roundingMode) {
		bd = bd.setScale(Scale, roundingMode);
		// The J# compiler doesn't support the unscaledValue() call
		//unscaledValue = bd.unscaledValue().longValue();
		unscaledValue = bd.movePointRight(Scale).longValue();
	}
	/** Assign a double value to this decimal. */
	public void assign(double value) {
		//double d = value * pow10[scale] + .00000000000001;
		//unscaledValue = (long)d;
		assign(value, Application.ROUND_DOWN);
	}
	/** Assign a rounded double value to this decimal. */
	public void assign(double value, int roundingMode) {
		/*
		if (roundingMode==Application.ROUND_DOWN)
		{
			assign(value);
		}
		else
			assign(new BigDecimal(Double.toString(value)).setScale(0, roundingMode).longValue());
		*/
		//assign(new BigDecimal(Double.toString(value)), roundingMode);
		//assign(new BigDecimal(new FloatingDecimal(value).toJavaFormatString()), roundingMode);
		//assign(new BigDecimal(Rdouble.toString(value)), roundingMode);
		unscaledValue=Rdouble.unscaledValue(value, Scale, roundingMode);
	}
	/** Assign a int value to this decimal.*/
 
	public void assign(int value) {
		assign((long)value);
	}
	/** Assign a long value to this decimal. */
	public void assign(long value) {
		unscaledValue = value * pow10[Scale];
	}

	/** Assign a IDecimal value to this decimal. */
	public void assign(INumeric value) {
		assign(value, Application.ROUND_DOWN);
	}
	/** Assign a rounded IDecimal value to this decimal. */
	public void assign(INumeric value, int roundingMode) {
		DecimalData id = value.toDecimal();
		int scaleDif = Scale - id.scale();
		long ul = id.unscaledValue();
		if (scaleDif >= 0)
			ul *= pow10[scaleDif];
		else
		{
			if (roundingMode==Application.ROUND_DOWN)
				ul = ul / pow10[-scaleDif];
			else
				ul = Math.round((double)ul / pow10[-scaleDif]);
		}
		unscaledValue = ul;
	}
	/** Compare the value of this decimal to another object. */
	public int compareTo(Object o) throws ClassCastException {
		// If the other decimal object has the same scale then we can just compare the unscaled values
		if (o instanceof ShortDecimal)
		{
			ShortDecimal value = (ShortDecimal)o;
			if (value.Scale == Scale)
			{
				long diff=unscaledValue - value.unscaledValue;
				if (diff==0)
					return 0;
				if (diff>0)
					return 1;
				return -1;
			}
		}
		if (o instanceof INumeric)
			return toBigDecimal().compareTo(((INumeric) o).toBigDecimal());
		if (o instanceof BigDecimal)
			return toBigDecimal().compareTo((BigDecimal) o);
		if (o instanceof java.lang.Number) {
			BigDecimal bd = ShortDecimal.newBigDecimal(o.toString());
			return toBigDecimal().compareTo(bd);
		}
		if (o instanceof FigConstNum)
			return compareTo(((FigConstNum) o).decimalValue(this.length, this.Scale));
		throw new ClassCastException("Cannot cast to numeric type");
	}

	/** Clone this decimal object. */  
	public Object clone() 
	{
		ShortDecimal	cloned = new ShortDecimal(length, Scale);
		cloned.setUnscaledValue(unscaledValue);
		return cloned;
	}
	
	/** Divide this decimal value by a double value. */
	public void div(double value) {
		decimalValue_.assign(value);
		div(decimalValue_);
	}
	/** Divide this decimal value by a integer value. */
	public void div(int value) {
		decimalValue_.assign(value);
		div(decimalValue_);
	}
	/** Divide this decimal value by another one. */
	public void div(INumeric value) {
		// Since we are doing integer division, we need to make sure that the result is going to be big
		// enough so that no decimal positions are lost
		DecimalData id = value.toDecimal();
		unscaledValue =
			(unscaledValue * pow10[id.scale()]) / id.unscaledValue();
	}

	/** Return the double equivalent of this value. */
	public double doubleValue() {
		double d = (double) unscaledValue / pow10[Scale];
		return d;
	}

	/** Multiply this decimal value by a double value. */
	public void mult(double value) {
		decimalValue_.assign(value);
		mult(decimalValue_);
	}
	/** Multiply this decimal value by a integer one. */
	public void mult(int value) {
		decimalValue_.assign(value);
		mult(decimalValue_);
	}
	/** Multiply this decimal value by another one. */
	public void mult(INumeric value) {
		DecimalData id = value.toDecimal();
		unscaledValue =
			(unscaledValue * id.unscaledValue()) / pow10[id.scale()];
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.INumeric#negate()
	public decimal negate() {
		decimalValue_.unscaledValue = -unscaledValue;
		return decimalValue_;
	}
	 */




	/** Set the unscaled value of this decimal. */
	public void setUnscaledValue(long unscaledValue) {
		this.unscaledValue = unscaledValue;
	}
	/** Subtract a double value from this decimal one. */
	public void sub(double value) {
		decimalValue_.assign(value);
		sub(decimalValue_);
	}
	/** Subtract a integer value from this decimal one. */
	public void sub(int value) {
		decimalValue_.assign(value);
		sub(decimalValue_);
	}
	// Subtraction is just add(-value)
	/** Subtract another decimal value from this one. */
	public void sub(INumeric value) {
		DecimalData id = value.toDecimal();
		addUnscaledValue(-id.unscaledValue(), id.scale());
	}

	/** Generate the BigDecimal equivalent of this number */
	public BigDecimal toBigDecimal() {
		BigDecimal bd = BigDecimal.valueOf(unscaledValue, Scale);
		return bd;
	}
	/* Create a zoned decimal equivalent of this object. */
	public FixedData toFixedChar() {
		return toZoned();
	}

	/** Return the unscaled value of this decimal. */
	public long unscaledValue() {
		return unscaledValue;
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.INumeric#toNumericString()
	 */
	public String toNumericString() {
		String s = Long.toString(unscaledValue);
		if (unscaledValue!=0 && Scale > 0)
		{
			StringBuffer sb = new StringBuffer(s);
			sb.insert(s.length()-Scale, '.');
			s = sb.toString();
		}
		return s;
	}

	// A factory method to return commonly used integer values
	static public Integer newInteger(int value)
	{
		switch(value)
		{
		case 0:
			return ZERO;
		case 1:
			return ONE;
		case -1:
			return MINUS_ONE;
		}
		return new Integer(value);
	}

	// A factory method to return commonly used double values
	static public BigDecimal newBigDecimal(double value)
	{
		if (value >= Long.MIN_VALUE && value <= Long.MAX_VALUE)
		{
			long longValue = (long)value;
			if (longValue==value)
				return BigDecimal.valueOf(longValue);
		}
		return new BigDecimal(Double.toString(value));
	}
	// A factory method to return commonly used String values
	static public BigDecimal newBigDecimal(String value)
	{
		if (value.compareTo("0")==0)
			return BigDecimal.valueOf(0);
		if (value.compareTo("1")==0)
			return BigDecimal.valueOf(1);
		return new BigDecimal(value);
	}

	/*
	public static void main (String args[])
	{
		zoned z1 = new zoned(2,0);
		if (z1.equals(Application.ZEROS))
		{
			int x = 0;
			x++;
		}
		packed p1 = new packed(2,0);
		if (p1.equals(Application.ZEROS))
		{
			int x = 0;
			x++;
		}
		decimal value1 = new decimal(15,2);
		decimal value2 = new decimal(6,0);
		if (value1.equals(Application.ZEROS))
		{
			if (value2.equals(Application.ZEROS))
			{
				int x=0;
				x++;
			}
		}
	 
		/*
		value.movel("000000000012345");
		value.assign(131550.72d);
		double d = value.doubleValue();
		value.assign(131550.73d);
		d = value.doubleValue();
		value.add(.01);
		BigDecimal in = value.plus(.02);
		in = value.times(1.3);
		in = value.minus(3.94);
		in = value.dividedBy(3.2);
		* /
	}
	*/
}
