package com.i2class;

/**
 * A 'logoff' exception that can be gracefully handled.
 *  
 */
public class ELogoff extends ThreadDeath
{
	public ELogoff()
	{
		super();
	}
	/**
	 * Construct an error with descriptive text.
	 */
	public ELogoff(String s)
	{
		//super(s);
		super();
	}
}
