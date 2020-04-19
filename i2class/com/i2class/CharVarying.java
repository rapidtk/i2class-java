package com.i2class;

/**
 * A varying-length string with a fixed-length (maximum) buffer size.
 */
public class CharVarying extends FixedChar {
	
	//private fixed voverlay;
	/** The actual length of the varying-length data. */
	//private int m_vlength;
	
	
	protected CharVarying()
	{
		super();
	}
	// Don't call super here because we want to initialize to "", not all blanks. 
	public CharVarying(int sz)
	{
		construct(sz+2);
	}
	public CharVarying(int sz, byte array[], int index)
	{
		super(sz+2, array, index);
	}
	public CharVarying(int sz, char c)
	{
		super(sz+2, c);
	}
	public CharVarying(int sz, FigConst fc)
	{
		super(sz+2, fc);
	}
	public CharVarying(int sz, String str)
	{
		super(sz+2, str);
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.FixedData#len()
	 */
	final public int len() {
		return (int)getBinary(-2, 2);
	}


	final int getOffset()
	{
		return m_ptr.m_offset+2;
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.FixedData#getBytes()
	public byte[] getBytes() {
		if (voverlay==null)
			voverlay = new fixed(m_size+2);
		voverlay.setBinary(m_vlength, 0, 2);
		voverlay.arrayCopy(2, this);
		return voverlay.getOverlay();
	}
	 */


	/* (non-Javadoc)
	 * @see com.asc.rio.FixedData#setVlength(int)
	 */
	protected void setVlength(int vlength)
	{
		//m_vlength = vlength;
		setBinary(vlength, -2, 2);
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.FixedData#msize()
	 */
	final int msize()
	{
		return m_size-2;
	}

	/*
	public static void main(String args[])
	{
		fixed fxd = new fixed(10, "ABC");
		varying v = new varying(10);
		v.assign(fxd);
		byte[] b = v.getBytes();
		v.assign("");
		b = v.getBytes();
		String s = v.toString();
		v.assign(fxd.toString()+"DEF");
		v.assign(fxd.trimr()+"DEF");
		v.assign("ABC");
		v.assign('c');
		s = v.toFixedString();
		s = v.toString();
		fxd.assign(v);
		v.move('x');
		fxd.move('x');
	}
	*/

}
