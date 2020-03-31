package com.i2class;

import java.io.Serializable;
import java.util.Arrays;

/**
 * A RPG figurative constant class.  Classes like Blanks (*BLANKS)
 * derive from this class.
 * @author Andrew Clark
 */

public class FigConst implements Comparable, Serializable
{
	public byte fillChar;
	/**
	 * A figurative constant initialized to the specified character value.
	 */
	public FigConst(char c)
	{
		fillChar = (byte) c;
	}

	/**
	 * Retrieve the character representation of this figurative constant.
	 * @param index Any number returns the same value
	 * @return char
	 */
	public char charAt(int index)
	{
		/// Arggggh (once again)!  Can't just cast to char here, because the sign (left-most) bit gets extended.
		//return (char)fillChar;
		char c = (char) fillChar;
		c = (char) (c & 0x00FF);
		return c;
	}

	/** 
	 * Compare to another object type.  Can only compare to <code>fixed</code> data types.
	 * @see fixed
	 */
	public int compareTo(Object o) throws ClassCastException
	{
		if (o instanceof fixed)
		{
			int i = ((fixed) o).compareTo(this);
			return -i;
		}
		throw new ClassCastException("Cannot cast to FigConst");
	}
	
	/** Return a substring no longer than <code>length</code> from the 1-based <code>start</code>. */
	public String subst(int start, int length)
	{
		// This is silly, but it's here to support %subst(*BLANKS, 1, 2) or %subst(*ZEROS, 1, 10), etc.
		//TODO is there a better way to do this???
		char[] ca = new char[length];
		java.util.Arrays.fill(ca, charAt(0));
		return new String(ca);
	}
}
