package com.i2class;
import java.math.*;
import java.text.SimpleDateFormat;

/**
 * A wrapper around the Double.toString method call for C# compatibility.
 * @author ANDREWC
 */
public class Rdouble {
	
	static long unscaledValue(double value, int scale, int roundingMode)
	{
		return numeric.newBigDecimal(value).setScale(scale, roundingMode).unscaledValue().longValue();
	}
	static void set2DigitYearStart(SimpleDateFormat simpleDateFormat, java.util.Date date)
	{
		simpleDateFormat.set2DigitYearStart(com.i2class.date.LOVAL_DATE_2); // Limit to 1940-2039
	}
}
