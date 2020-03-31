package com.i2class;

/**
 * A mutable class that represents binary data stored in a fixed character string.
 * @author Andrew Clark 
 */
public class binary extends fixedInt {
	
	private static final long serialVersionUID = 6140337025199662671L;
	
	int m_scale;

	/**
	 * Construct a value with the specified length
	 * @param sz The length of the binary value (4=float, 8=double)
	 */
	public binary(int length, int scale) {
		super(length <= 4 ? 2 : 4);
		m_scale = scale;
	}
	
	/** Return the length in digits of a particular size in bytes. */
	static protected int len(int size)
	{
		if (size <= 2)
			return 4;
		return 9;
	}
	
	@Override
	public void assign(long value)
	{
		if (m_scale >0)
			value = (long) (value * Math.pow(10, m_scale));
		super.assign(value);
	}
	@Override
	public void assign(double value) {
		if (m_scale >0)
			value = value * Math.pow(10, m_scale);
		super.assign((long)value);
	}

	
	@Override
	public long longValue()
	{
		long l = super.longValue();
		if (m_scale >0)
			l = (long) (l / Math.pow(10, m_scale));
		return l;
	}

	@Override
	public double doubleValue()
	{
		double d = super.longValue();
		if (m_scale >0)
			d = d / Math.pow(10, m_scale);
		return d;
	}

	/*
	public static void main(String[] args)
	{
		binary b = new binary(9,0);
		b.assign(10.234);
		double d = b.doubleValue();
		b = new binary(9,2);
		b.assign(10.234);
		d = b.doubleValue();
		return;
	}
	*/
}
