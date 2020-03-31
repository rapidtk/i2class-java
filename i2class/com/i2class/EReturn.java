package com.i2class;

/**
 * A 'return from subroutine' exception that can be gracefully handled.
 * @author Andrew Clark 
 */
public class EReturn extends Exception
{
	public EReturn()
	{
		super();
	}
	/**
	 * Construct an error with descriptive text.
	 */
	public EReturn(String s)
	{
		super(s);
	}
}
