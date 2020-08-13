package com.i2class;

/**
 * A fixed length substring class used on the left side of an eval statement.
 * <p>  
 * Example: eval %subst(cname:1:10)='X' -> new subst(cname,1,10).assign("X")
 * </p>
 */
public class subst extends FixedChar
{
	/**
	 * @param fStr The referenced <code>fixed</code> type 
	 * @param start The start of the substring
	 * @param len The length of the substring
	 */
	public subst(FixedChar fStr, int start, int len)
	{
		super(len, new FixedPointer(fStr, start-1));
	}
	public subst(FixedChar fStr, INumeric start, int len)
	{
		this(fStr, start.intValue(), len);
	}
	public subst(FixedChar fStr, int start, INumeric len)
	{
		this(fStr, start, len.intValue());
	}
	public subst(FixedChar fStr, INumeric start, INumeric len)
	{
		this(fStr, start.intValue(), len.intValue());
	}
	
	public subst(FixedChar fStr, int start)
	{
		super(fStr.len()-start+1, new FixedPointer(fStr, start-1));
	}
	public subst(FixedChar fStr, INumeric start)
	{
		this(fStr, start.intValue());
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
