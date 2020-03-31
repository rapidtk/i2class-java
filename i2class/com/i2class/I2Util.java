/*
 * Created on Nov 16, 2004
 *
 */
package com.i2class;

/**
 * I2 utility methods.
 * @author ANDREWC
 */
public class I2Util {

	/** Parse a string in the form library/name into fixed(20) NAME(10 bytes) and LIBRARY(10 bytes) */
	public static fixed stringToQual(String s)
	{
		fixed name = new fixed(10);
		fixed lib = new fixed(10);
		int i = s.indexOf('/');
		if (i>=0)
		{
			lib.assign(s.substring(0, i));
			name.assign(s.substring(i+1));
		}
		else
			name.assign(s);
		fixed qual = new fixed(20);
		qual.movel(name);
		qual.move(lib);
		return qual;
	}

}
