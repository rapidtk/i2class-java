package com.i2class;

import java.util.Date;
import java.text.SimpleDateFormat;
/**
 * A formatted date class used by UDATE, DATE, etc.
 * 
 */
public class FmtDate extends ZonedDecimal
{
	/**
	 * This is created a single time and becomes the Java equivalent of Application.JOB_DATE
	 * 
	 * @see Application.JOB_DATE
	 */
	static final Date SEED_DATE = new Date();
	
	/**
	 * Construct an I2 date with the specified size (number of bytes) and Java date format.
	 */
	public FmtDate(int sz, String fmt)
	{
		super(sz, 0);
		String dateString = new SimpleDateFormat(fmt).format(SEED_DATE);
		movel(dateString);
	}
}
