package com.i2class;

/**
 * A mutable class that represents a java float stored in fixed-length data.
 * 
 *  
 */
public class FixedFloat extends FixedBinary {

	/**
	 * Construct a value with the specified length
	 * @param sz The length of the binary value (4=float, 8=double)
	 */
	public FixedFloat(int sz) {
		super(sz);
	}
	
	/* (non-Javadoc)
	 * @see com.i2class.fixedBin#assign(double)
	 */
	public void assign(double value) {
		if (size()==4)
			setBinary(Float.floatToIntBits((float)value), 0, 4);
		else
			setBinary(Double.doubleToLongBits(value), 0, 8);
	}

	/** Assign a long value to this variable. */
	public void assign(long value)
	{
		assign((double)value);
	}


	/**
	 * Return the length of this binary number: 12=float, 15=double.
	 */
	public int len()
	{
		if (size()<=4)
			return 12;
		else
			return 15;
	}

	/* (non-Javadoc)
	 * @see com.i2class.AbstractNumeric#intValue()
	 */
	public int intValue() {
		return (int)doubleValue();
	}
	
	/* (non-Javadoc)
	 * @see com.i2class.fixedBin#longValue()
	 */
	public long longValue() {
		return (long)doubleValue();
	}


	/* (non-Javadoc)
	 * @see com.i2class.fixedBin#doubleValue()
	 */
	public double doubleValue() {
		if (size()<=4)
			return Float.intBitsToFloat((int)getBinary(0, 4));
		else
			return Double.longBitsToDouble(getBinary(0, 8));
	}
	
	/*
	public static void main(String[] args)
	{
		fixedBin b = new fixedBin(4);
		b.assign(10.2);
		fixedFloat f = new fixedFloat(4);
		f.assign(10.2);
		String s = f.toString();
		fixedFloat d = new fixedFloat(8);
		d.assign(10.2);
		s = d.toString();
		return;
	}
	*/
}
