package com.i2class;

import java.util.Date;
import java.text.SimpleDateFormat;
/**
 * A formatted date class used by UDATE, DATE, etc.
 */
public class FmtDate extends ZonedDecimal
{
	static /*final*/ Date date /*= new Date()*/;
	private String dateString;
	/**
	 * Construct a I2 date with the specified size (number of bytes) and Java date format.
	 */
	public FmtDate(int sz, String fmt)
	{
		super(sz, 0);
		if (date==null)
			date = new Date();
		dateString = new SimpleDateFormat(fmt).format(date);
		movel(dateString);
	}
}
