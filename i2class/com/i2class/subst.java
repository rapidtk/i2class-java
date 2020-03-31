package com.i2class;

/**
 * A fixed length substring class used on the left side of a eval.  
 * Example eval %subst(cname:1:10)='X' -> new subst(cname,1,10).assign("X")
 * @author Andrew Clark
 */
public class subst extends fixed
{
	private int offset;
	private int length;
	private byte overlay[];
	/**
	 * @param fStr The referenced <code>fixed</code> type 
	 * @param start The start of the substring
	 * @param len The length of the substring
	 */
	public subst(fixed fStr, int start, int len)
	{
		super(len, new pointer(fStr, start-1));
	}
	public subst(fixed fStr, INumeric start, int len)
	{
		this(fStr, start.intValue(), len);
	}
	public subst(fixed fStr, int start, INumeric len)
	{
		this(fStr, start, len.intValue());
	}
	public subst(fixed fStr, INumeric start, INumeric len)
	{
		this(fStr, start.intValue(), len.intValue());
	}
	/*
	public static void main(String[] args) 
	{
		fixed f = new fixed(6, "ABCDEFG");
		subst s = new subst(f, 1, 3);
		s = new subst(f, 4, 3);
	}
	*/
}
