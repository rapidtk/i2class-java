package com.i2class;

/**
 * A 'return from application' exception that can be gracefully handled.
 *  
 */
public class ApplicationReturn extends Exception
{
	public ApplicationReturn()
	{
		super();
	}
	/**
	 * Construct an error with descriptive text.
	 */
	public ApplicationReturn(String s)
	{
		super(s);
	}
}
