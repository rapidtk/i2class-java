/*
 * Created on Sep 13, 2004
 *
 */
package com.i2class;

/**
 * An abstract class for numeric data that is 'coded' i.e. not zoned.
 * @author ANDREWC
 *
 */
abstract public class AbstractNumericCoded extends AbstractNumeric {

	/**
	 * @param size
	 * @param digits
	 * @param scale
	 * @param overlay
	 */
	public AbstractNumericCoded(
		int size,
		int digits,
		int scale,
		pointer overlay)
	{
		super(size, digits, scale, overlay);
	}

	/**
	 * @param len
	 * @param scale
	 * @param digits
	 */
	public AbstractNumericCoded(int len, int digits, int scale) {
		super(len, digits, scale);
	}
	
	/** 
	 * Move a character value to the right-most byte of this variable.  
	 * Translate blanks to '0'.
	 */
	public FixedData move(char c)
	{
		zoned z = toZoned();
		z.move(c);
		assignZoned(z);
		return this;
	}


	/** 
	 * Move a fixed-length value to the right-most bytes of this variable.
	 * Translate blanks to '0'.
	 */
	public FixedData move(IFixed fStr)
	{
		zoned z = toZoned();
		z.move(fStr);
		assignZoned(z);
		return this;
	}


	/** 
	 * Move a string to the right-most bytes of this variable.
	 * Translate blanks to '0'.
	 */
	public FixedData move(String str)
	{
		zoned z = toZoned();
		z.move(str);
		assignZoned(z);
		return this;
	}


	/** 
	 * Move a character value to the left-most byte of this variable.  
	 * Translate blanks to '0'.
	 */
	public FixedData movel(char c)
	{
		zoned z = toZoned();
		z.movel(c);
		assignZoned(z);
		return this;
	}


	/** 
	 * Move a fixed-length value to the left-most bytes of this variable.
	 * Translate blanks to '0'.
	 */
	public FixedData movel(IFixed fStr)
	{
		zoned z = toZoned();
		z.movel(fStr);
		assignZoned(z);
		return this;
	}



	/** 
	 * Move a string to the left-most bytes of this variable.
	 * Translate blanks to '0'.
	 */
	public FixedData movel(String str)
	{
		zoned z = toZoned();
		z.movel(str);
		assignZoned(z);
		return this;
	}

}
