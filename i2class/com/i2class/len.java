package com.i2class;

/**
 * A length class used on the left side of an eval.  
 * Example eval %len(cname)=10 -> new len(cname).assign(10)
 * 
 */
public class len
{
	private CharVarying vString;

	public len(CharVarying vStr)
	{
		vString = vStr;
	}
	
	/** Set the length of this string. */
	public void assign(int length)
	{
		vString.setVlength(length);
	}
	/** Set the length of this string. */
	public void assign(INumeric length)
	{
		assign(length.intValue());
	}
}
