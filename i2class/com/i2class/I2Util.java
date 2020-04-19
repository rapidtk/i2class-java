/*
 * Created on Nov 16, 2004
 *
 */
package com.i2class;

/**
 * I2 utility methods.
 * 
 */
public class I2Util {

	/** Parse a string in the form library/name into fixed(20) NAME(10 bytes) and LIBRARY(10 bytes) */
	public static FixedChar stringToQual(String s)
	{
		FixedChar name = new FixedChar(10);
		FixedChar lib = new FixedChar(10);
		int i = s.indexOf('/');
		if (i>=0)
		{
			lib.assign(s.substring(0, i));
			name.assign(s.substring(i+1));
		}
		else
			name.assign(s);
		FixedChar qual = new FixedChar(20);
		qual.movel(name);
		qual.move(lib);
		return qual;
	}

}
