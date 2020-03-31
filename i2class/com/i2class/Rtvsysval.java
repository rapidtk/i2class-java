/* Created 11/18/2004 */

package com.i2class;

import java.text.*;
import java.util.Date;

import javax.servlet.http.*;

/**
 * RTVSYSVAL (Retrieve system value) processing.
 * @author Andrew Clark
 */
public class Rtvsysval
{
	private static char m_cursym;
	private static String m_datfmt;
	private static char m_datsep;
	private static char m_decfmt;
	private static char m_timsep;
	
	/** Retrieve the currency symbol */
	public static char cursym()
	{
		// The currency symbol is the first character of the returned string ($1.23)
		if (m_cursym=='\0')
			m_cursym=NumberFormat.getCurrencyInstance().format(1.23).charAt(0);
		return m_cursym;
	}

	/** Retrieve the date format */
	public static String datfmt()
	{
		if (m_datfmt==null)
		{
			// Get date 1 day past 'epoch' (Jan 2, 1970)
			Date d = new Date(24*60*60*1000);
			String fdate = DateFormat.getDateInstance(DateFormat.SHORT).format(d);
			// Calculate date format based upon return value
			if (fdate.charAt(0)=='2') // 2/1/70
				m_datfmt="*DMY";
			else if (fdate.startsWith("70")) // 70/1/2
				m_datfmt="*YMD";
			else
				m_datfmt="*MDY"; // 1/2/70
			//TODO *JUL???
		}
		return m_datfmt;
	}

	/** Return date separator */
	public static char datsep() {
		// Get date separator (3rd character) from 12/31/69 24:00:00
		if (m_datsep=='\0')
		{ 
			String s = DateFormat.getDateInstance(DateFormat.SHORT).format(new Date(-1));
			m_datsep=s.charAt(2);
		}
		return m_datsep;
	}

	/** Return time separator */
	public static char timsep()
	{
		// Get time separator (3rd character) from 12/31/69 24:00:00 
		if (m_timsep=='\0')
		{ 
			String s=DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date(-1));
			m_timsep=s.charAt(2);
		}
		return m_timsep;
	}

	/** Return decimal format */
	public static char decfmt()
	{
		// Decimal format I means comma (',') for decimal symbol
		// Decimal format J same as I with leading 0
		if (m_decfmt=='\0')
		{
			String decValue = NumberFormat.getNumberInstance().format(0.12);
			char dv0 = decValue.charAt(0);
			if (dv0=='0' && decValue.charAt(1)==',')
				m_decfmt='J';
			else if (dv0==',')
				m_decfmt='I';
			// Blank means use '.'
			else
				m_decfmt=' ';
		}
		return m_decfmt;
	}

	/* REMEMBER -- all of the date/time functions (could) change so we can't use static values for them!!! */
	
	/** Return century (0=1900, 1=2000, 2=2100, etc. */
	public static char century()
	{
		String d = new SimpleDateFormat("yyyy").format(new Date());
		return Integer.toString(Integer.parseInt(d.substring(0,1))-19).charAt(0);
	}

	/** Return CYYMMDD date */
	public static String date()
	{
		String d = new SimpleDateFormat("yyyyMMdd").format(new Date());
		return Integer.toString(Integer.parseInt(d.substring(0,1))-19)+d.substring(2);
	}
	
	/** Return year portion of date */
	public static String year()
	{
		return new SimpleDateFormat("yy").format(new Date());
	}

	/** Return month portion of date */
	public static String month()
	{
		// Don't forget, month is +1!!!
		String d = new SimpleDateFormat("MM").format(new Date());
		return Integer.toString(Integer.parseInt(d)+1);
	}

	/** Return day portion of date */
	public static String day()
	{
		return new SimpleDateFormat("dd").format(new Date());
	}

	/** Return dayofweek portion of date */
	public static String dayofweek()
	{
		return new SimpleDateFormat("FF").format(new Date());
	}

	/** Return hour portion of current time */
	public static String hour()
	{
		return new SimpleDateFormat("HH").format(new Date());
	}

	/** Return minute portion of current time */
	public static String minute()
	{
		return new SimpleDateFormat("mm").format(new Date());
	}

	/** Return second portion of current time */
	public static String second()
	{
		return new SimpleDateFormat("ss").format(new Date());
	}
}
