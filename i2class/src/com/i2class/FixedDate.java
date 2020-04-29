package com.i2class;

import java.util.*;
import java.util.Date;
import java.text.*;
import java.text.SimpleDateFormat;
/**
 * A mutable, fixed-length date class that represents an IBM i date data type e.g. YYYY-MM-DD.
 * 
 * <p>
 * Each date data type has a 
 * <a href="http://publib.boulder.ibm.com/iseries/v5r2/ic2924/books/x091315228.htm">date format</a> associated
 * with it that determines how the data is output.
 * </p>
 * 
 * 
 */
public class FixedDate extends FixedChar
{

	//static final public Date LOVAL_DATE=new Date(-62135683200000L); // Offset, in milliseconds, from 01/01/0001 to 01/01/1970 
	// Offset, in milliseconds, from 01/01/0001 to 01/01/1970 -- the original calculation is off by 18 hours, I'm sure something
	// to do with the Gregorian Calendar switch date (October 4, 1582) and some other rounding...
	/** 4-digit year *LOVAL 01/01/0001 */
	static final Date LOVAL_DATE = new Date( -62135748000000L);
	/** 3-digit (*CMDY) year *LOVAL 01/01/000 (1900) */
	static final Date LOVAL_DATE_3 = new Date(-2208967200000L);
	/** 2-digit year *LOVAL 01/01/40 (1940) */
	static final Date LOVAL_DATE_2 = new Date( -946749600000L);

	/** 4-digit year *HIVAL 12/31/9999 */
	static final Date HIVAL_DATE = new Date(253402322399999L);
	/** 3-digit (*CMDY) year *HIVAL 12/31/999 (2899) */
	static final Date HIVAL_DATE_3 = new Date(29348027999999L);
	/** 2-digit year *LOVAL 12/31/39 (2039) */
	static final Date HIVAL_DATE_2 = new Date( 2209010399999L);
	static final private GregorianCalendar calendar = new GregorianCalendar();

	String datfmt;
	String m_datfmt400;
	protected SimpleDateFormat simpleDateFormat;

	/**
	 * Create a data using the default (I2App._DATFMT) date format 
	 */
	public FixedDate()
	{
		this(Application.dateFormat, 0, Application._DATFMT);
	}
	/**
	 * Create a date using the specified OS/400-style date format (for example *MDY/, ISO).
	 */
	public FixedDate(String datfmt400)
	{
		this(getDateFormat(datfmt400), 0, datfmt400);
	}
	/**
	 * Create a date using *LOVAL
	 */
	public FixedDate(String datfmt400, Loval value)
	{
		this(datfmt400);
		//assign(value); *LOVAL is the default value
	}
	/**
	 * Create a date using *HIVAL
	 */
	public FixedDate(String datfmt400, Hival value)
	{
		this(datfmt400);
		assign(value);
	}
	/** Create a date with an initial date value */
	public FixedDate(String datfmt400, FixedDate value)
	{
		this(datfmt400);
		updating=true;
		try
		{
			assign(value);
		}
		catch(Exception e) {}
		updating=false;
	}
	
	protected FixedDate newDate0(String datfmt400, FixedDate value)
	{
		return new FixedDate(datfmt400, value);
	}
	
	/**
	 * Just to differentiate between the public datfmt400 constructor.
	private date(String datfmt, int length)
	{
		this(datfmt, length, null);
	}
	 */
	
	/**
	 * Create a date that overlays another value using the default (I2App._DATFMT) date format 
	 */
	public FixedDate(FixedPointer overlay)
	{
		//super(Application.dateFormat.length(), overlay);
		this(Application.dateFormat, Application._DATFMT, overlay);
	}
	
	protected FixedDate(String datfmt, String datfmt400, FixedPointer overlay)
	{
		super(datfmt.length(), overlay);
		construct(datfmt, datfmt400);
	}
	
	/**
	 * Just to differentiate between the public datfmt400 constructor.
	 */
	FixedDate(String datfmt, int length, String datfmt400)
	{
		super(length>0?length:datfmt.length());
		construct(datfmt, datfmt400);
	}
	
	private void setDateFormat(String datfmt, String datfmt400)
	{
		this.datfmt = datfmt;
		m_datfmt400 = datfmt400;
		simpleDateFormat = new SimpleDateFormat(datfmt);
		Rdouble.set2DigitYearStart(simpleDateFormat,LOVAL_DATE_2); // Limit to 1940-2039
		simpleDateFormat.setLenient(false); // Don't allow dates with month=0, etc.
	}

	private void construct(String datfmt, String datfmt400)
	{
		setDateFormat(datfmt, datfmt400);
		updating=true; // Set so that updateSubfields function doesn't get called (subfields aren't 'constructed' yet) 
		assign(Application.LOVAL);
		updating=false;
	}

	/**
	 * Create a new date with the specified format and value.  
	 * The format of the <code>inzval</code> inital value must
	 * match the <code>datfmt400</code> date format.
	 * @param datfmt400 The OS/400 date type. 
	 * @param inzval The initial data value.
	 */
	public FixedDate(String datfmt400, String inzval)
	{
		this(datfmt400);
		updating=true;
		assign(inzval);
		updating=false;
	}

	/**
	 * Add a duration to this date.
	 * @param duration int
	 * @param durationCode the duration code: (Application.)MONTHS, DAYS, YEARS
	 */
	public void adddur(int duration, int durationCode) throws Exception
	{
		String s = plusdurString(duration, durationCode);
		assign(s);
	}

	/**
	 * Add a duration to this date.
	 * @param duration int
	 * @param durationCode the duration code: (Application.)MONTHS, DAYS, YEARS.
	 */
	public void adddur(INumeric duration, int durationCode) throws Exception
	{
		adddur(duration.intValue(), durationCode);
	}
	/**
	 * Add a duration to this date and return a new FixedDate value.
	 */
	public FixedDate add(Duration duration) throws Exception
	{
		FixedDate newDate = new FixedDate(this.datfmt, this);
		newDate.adddur(duration.duration, duration.durationCode);
		return newDate;
	}

	/**
	 * Assign another date value to this date class.
	 */
	public void assign(FmtDate value)
	{
		assign(FmtDate.SEED_DATE);
	}

	/**
	 * Assign a timestamp value to this date class.
	 */
	public void assign(FmtTime value)
	{
		assign(new Date());
	}

	/**
	 * Assign another date value to this date class.
	 */
	public void assign(FixedDate value) throws ParseException
	{
		// Reformat value so that it is the same format as this date data type, then assign
		Date d = value.toDate();
		assign(d);
	}

	/**
	 * Assign a Java date value to this date class.
	 * @param value java.util.Date
	 */
	public void assign(Date value)
	{
		// Reformat value so that it is the same format as this date data type, then assign
		String s = format(value);
		assign(s);
	}

	/** Return the *LOVAL Date equivalent of this date. */
	private Date lovalDate()
	{
		Date lovalDate;
		// For *CMDY, *CDMY, *CYMD use 3-digit *LOVAL 01/01/000 (1900)
		if (m_datfmt400.charAt(1)=='C')
			lovalDate = LOVAL_DATE_3;
		else if (m_datfmt400.indexOf('Y')>=0 || m_datfmt400.compareTo("*JUL")==0)
			lovalDate = LOVAL_DATE_2;
		else
			lovalDate = LOVAL_DATE;
		return lovalDate;
	}

	/**
	 * The figurative constant *LOVAL.
	 */
	public void assign(Loval value)
	{
		// For *MDY, *DMY, *YMD, and *JUL, use 2-digit *LOVAL 01/01/40 (1940)
		Date lovalDate=lovalDate();
		String dateString = format(lovalDate);
		movel(dateString);
	}

	/** Return the *HIVAL Date equivalent of this date. */
	private Date hivalDate()
	{
		Date hivalDate;
		// For *CMDY, *CDMY, *CYMD use 3-digit *HIVAL 12/31/999 (2899)
		if (m_datfmt400.charAt(1)=='C')
			hivalDate = HIVAL_DATE_3;
		else if (m_datfmt400.indexOf('Y')>=0 || m_datfmt400.compareTo("*JUL")==0)
			hivalDate = HIVAL_DATE_2;
		else
			hivalDate = HIVAL_DATE;
		return hivalDate;
	}
	/**
	 * The figurative constant *HIVAL.
	 */
	public void assign(Hival value)
	{
		// For *MDY, *DMY, *YMD, and *JUL, use 2-digit *HIVAL 12/31/39 (2039)
		Date hivalDate=hivalDate();
		String dateString = format(hivalDate);
		movel(dateString);
	}
	
	/** Return the fiexed-length representation of this date in the specified Java-formatted (e.g. "yyyy-mm-dd") format. */
	FixedChar CharJava(String datfmt)
	{
		// Get the Date value for the current buffer
		try
		{
			Date d = toDate();
			// Create a new formatter with that format, and return the formatted results
			SimpleDateFormat sdf = new SimpleDateFormat(datfmt);
			String s = sdf.format(d);
			//return s;
			FixedChar fStr = new FixedChar(s.length(), s);
			return fStr;
		}
		catch (Exception e)
		{
			return null;
		}
	}
	/**
	 * Return a string representing the date in the specified format.
	 * @return fixed
	 * @param dateFormat400 The 400 style date string (for example "*MDY/", "*ISO")
	 */
	public FixedChar Char(String datfmt400)
	{
		// Translate the 400 date format (e.g. "*ISO" to the Java date format (e.g. "yyyy-MM-dd")
		return CharJava(getDateFormat(datfmt400));
	}

	/**
	 * Get Java date format (e.g. "yyyy-mm-dd") from a 'special' OS/400 date style (e.g. "*ISO").
	 * @return java.lang.String
	 * @param datfmt java.lang.String
	 */
	static String getDateFormat(String datfmt400)
	{
		String datsep = "";
		int dlen = datfmt400.length() - 1;
		int maxDlen;
		if (datfmt400.startsWith("*CYMD"))
			maxDlen=5;
		else
			maxDlen=4;
		if (dlen >= maxDlen)
		{
			// A '&' means no separator
			char dsep = datfmt400.charAt(dlen);
			if (dsep != '&' && dsep != '0')
				datsep = datfmt400.substring(dlen);
			datfmt400 = datfmt400.substring(0, dlen);
			if (datfmt400.compareTo("*JOBRUN") == 0)
				datfmt400 = Application._DATFMT;
		}
		else if (
			datfmt400.compareTo("*ISO") == 0 || datfmt400.compareTo("*JIS") == 0)
			datsep = "-";
		else if (datfmt400.compareTo("*EUR") == 0)
			datsep = ".";
		else
			datsep = "/";

		String yearFmt;
		if (datfmt400.compareTo("*ISO") == 0
			|| datfmt400.compareTo("*USA") == 0
			|| datfmt400.compareTo("*EUR") == 0
			|| datfmt400.compareTo("*JIS") == 0
			|| datfmt400.compareTo("*CYMD") == 0)
			yearFmt = "yyyy";
		else
			yearFmt="yy";
		

		String dateFormat;
		if (datfmt400.compareTo("*YMD") == 0
			|| datfmt400.compareTo("*ISO") == 0
			|| datfmt400.compareTo("*JIS") == 0
			|| datfmt400.compareTo("*CYMD") == 0)
			dateFormat = yearFmt + datsep + "MM" + datsep + "dd";
		else if (
			datfmt400.compareTo("*DMY") == 0 || datfmt400.compareTo("*EUR") == 0)
			dateFormat = "dd" + datsep + "MM" + datsep + yearFmt;
		else if (datfmt400.compareTo("*JUL") == 0)
			dateFormat = "yy" + datsep + "DDD";
		else
			dateFormat = "MM" + datsep + "dd" + datsep + yearFmt;
		return dateFormat;
	}
	/**
	 * Get Java date/time format (e.g. "yyyy-mm-dd") from a 'special' OS/400 date style (e.g. "*ISO").
	 */
	protected String getJavaFormat(String datfmt400)
	{
		return FixedDate.getDateFormat(datfmt400);
	}

	/** Return the date formatted as a String. */
	protected String format(Date value)
	{
		String fmtDate = simpleDateFormat.format(value);
		// If this is a *CYMD date, then we must transform the 'C' in the first position to a 2-digit year prefix 
		// i.e. '0'=19, '1'=20, etc.
		if (m_datfmt400!=null && m_datfmt400.startsWith("*CYMD"))
		{
			char c = (char)('0'+(Integer.parseInt(fmtDate.substring(0, 2))-19));
			fmtDate = c + fmtDate.substring(2);
				
		}
		return fmtDate;
	}
	/** Return the date formatted as a String with its micro value preserved. */
	protected String formatMicro(Date value)
	{
		return format(value);
	}

	/** Return the microsecond remainder (microseconds % 1000) associated with this date. */
	protected int getMicrorem()
	{
		return 0;
	}
	/** Set the microsecond remainder (microseconds % 1000) associated with this date. */
	protected void setMicrorem(int microrem) 
	{
	}
	
	/**
	 * Subtract a duration from this date and return a new I2 date object.
	 * @param int
	 * @param durationCode the duration code: (Application.)MONTHS, DAYS, YEARS
	 * @return date
	 */
	public FixedDate minusdur(int duration, int durationCode) throws Exception
	{
		return plusdur(duration * -1, durationCode);
	}

	/**
	 * Subtract a duration from this date and return a new date object.
	 * @param duration Numeric
	 * @param durationCode the duration code: (Application.)MONTHS, DAYS, YEARS
	 * @return date 
	 */
	public FixedDate minusdur(INumeric duration, int durationCode) throws Exception
	{
		return minusdur(duration.intValue(), durationCode);
	}
	/**
	 * Subtract a duration from this date and return a new date object.
	 */
	public FixedDate minus(Duration duration) throws Exception
	{
		return minusdur(duration.duration, duration.durationCode);
	}

	/**
	 * Add a duration and return a new date object.
	 * @vers 2/6/2003
	 * @param duration int
	 * @param durationCode the duration code: (Application.)MONTHS, DAYS, YEARS
	 * @return date 
	 */
	public FixedDate plusdur(int duration, int durationCode) throws Exception
	{
		String s = plusdurString(duration, durationCode);
		FixedDate d1 = (FixedDate) clone();
		d1.assign(s);
		return d1;
	}

	/**
	 * Add a duration and return a new date object.
	 * @param duration Numeric
	 * @param durationCode the duration code: (Application.)MONTHS, DAYS, YEARS
	 * @return date
	 */
	public FixedDate plusdur(INumeric duration, int durationCode) throws Exception
	{
		return plusdur(duration.intValue(), durationCode);
	}
	/**
	 * Add a duration and return a new date object.
	 */
	public FixedDate plus(Duration duration) throws Exception
	{
		return plusdur(duration.duration, duration.durationCode);
	}

	/**
	 * Return the string representation of the plusdur operation.
	 * @param duration int
	 * @param durationCode the duration code: (Application.)MONTHS, DAYS, YEARS, HOURS, MINUTES, SECONDS, MICROSECONDS
	 * @return String 
	 */
	private String plusdurString(int duration, int durationCode) throws Exception
	{
			// Parse buf[] to get a Date representation
			Date d = toDate();
			// Create a new Calendar and use the returned date to extract the specified portion
			calendar.setTime(d);
			/* A duration isn't 0-11!!!
			   For whatever reason, the MONTH is 0-11 instead of 1-12
			if (durationCode == Application.MONTHS)
				duration++;
			*/
			// Translate microseconds to milliseconds (Application.MICROSECONDS=Calendar.MILLISECOND)
			if (durationCode == Application.MSECONDS)
			{
				int microrem = duration % 1000;
				duration /= 1000;
				// Adjust microseconds
				if (microrem>0)
				{
					microrem += getMicrorem();
					if (microrem>999)
					{
						duration++;
						microrem -=1000;
					}
					setMicrorem(microrem);
				}
			}
			calendar.add(durationCode, duration);
			return formatMicro(calendar.getTime());
	}

	/**
	 * Extract a portion of the date.
	 * @param durationCode the duration code: MONTH, DAY, YEAR, HOURS, MINUTES, SECONDS, MICROSECONDS
	 * @return int 
	 */
	public int subdt(int durationCode) throws Exception
	{
		// Parse buf[] to get a Date representation
		int duration = 0;
			Date d = toDate();
			// Create a new Calendar and use the returned date to extract the specified portion
			calendar.setTime(d);
			duration = calendar.get(durationCode);
			// For whatever reason, the MONTH is 0-11 instead of 1-12
			if (durationCode == Application.MONTHS)
				duration++;
			// Translate milliseconds to microseconds (Application.MICROSECONDS=Calendar.MILLISECOND)
			else if (durationCode == Application.MSECONDS)
				duration = duration*1000 + getMicrorem();
			return duration;
	}

	/**
	 * Subtract a duration from this date.
	 * @param int
	 * @param durationCode the duration code: (Application.)MONTHS, DAYS, YEARS
	 */
	public void subdur(int duration, int durationCode) throws Exception
	{
		adddur(duration * -1, durationCode);
	}

	/**
	 * Subtract a duration from this date.
	 * @param duration Numeric
	 * @param durationCode the duration code: (Application.)MONTHS, DAYS, YEARS
	 */
	public void subdur(INumeric duration, int durationCode) throws Exception
	{
		subdur(duration.intValue(), durationCode);
	}
	/**
	 * Subtract a duration from this date.
	 */
	public void sub(Duration duration) throws Exception
	{
		subdur(duration.duration, duration.durationCode);
	}

	/**
	 * Return the Java Date representation of this 400 date data type
	 * @return java.util.Date
	 */
	public java.sql.Date toDate() throws ParseException 
	{
			String dateString = toFixedString();
			// If this is a *CYMD date, then we must transform the 'C' in the first position to a 2-digit year prefix 
			// i.e. '0'=19, '1'=20, etc.
			if (m_datfmt400!=null && m_datfmt400.startsWith("*CYMD"))
			{
				int c = (charAt(0)-'0')+19;
				dateString=Integer.toString(c)+dateString.substring(1);
				
			}
			Date d = simpleDateFormat.parse(dateString);
			return new java.sql.Date(d.getTime());
	}

	/** Create a new date with the same format as this one but no date separator characters */
	private FixedDate date0() throws ParseException
	{
		/*
		String datfmt0 = datfmt0(this);
		date d;
		if (datfmt0.compareTo(datfmt)==0)
			d=this;
		else
		{
			d = new date(datfmt0, 0);
			d.assign(this);
		}
		*/
		// If the last character of date format is '0' (e.g. *MDY0) then we can use 'this'
		int df400len1 = m_datfmt400.length()-1;
		char datSep = m_datfmt400.charAt(df400len1);
		FixedDate d;
		if (datSep=='0')
			d=this;
		// Otherwise, we need the '0' version of the date
		else
		{
			String datfmt400=m_datfmt400;
			// If a different separator is specified (e.g. *MDY/) then remove it
			if (!Character.isLetter(datSep))
				datfmt400=m_datfmt400.substring(0, df400len1);
			datfmt400=m_datfmt400+'0';
			//d = new date(datfmt400, this);
			d = newDate0(datfmt400, this);
		}
		return d;
	}
	
	/** Move another date to this one. */
	public void move(FixedDate value) throws ParseException
	{
		assign(value);
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.FixedData#moveFixedData(com.asc.rio.FixedData)
	 */
	protected FixedData moveFixedData(FixedData fStr)
	{
		// For numeric data types only!!!  Convert this date to a date with no separators (e.g. *ISO), 
		// then do MOVE of data
		if (fStr instanceof AbstractNumeric)
		{
			try
			{
				FixedDate d = date0();
				d.moveArrayData(fStr.getOverlay(), fStr.getOffset(), fStr.msize());
				if (d!= this)
					assign(d);
			}
			catch (Exception e)
			{
				I2Logger.logger.printStackTrace(e);
				throw new Error(e.getMessage());
			}
			return this;
		}
		else
			return super.moveFixedData(fStr);
	}
	
	/* (non-Javadoc)
	 * @see com.asc.rio.FixedData#movelFixedData(com.asc.rio.FixedData)
	 */
	protected FixedData movelFixedData(FixedData fStr)
	{
		// For numeric data types only!!!  Convert this date to a date with no separators (e.g. *ISO), 
		// then do MOVEL of data
		if (fStr instanceof AbstractNumeric)
		{
			try
			{
				FixedDate d = date0();
				d.movelArrayData(fStr.getOverlay(), fStr.getOffset(), fStr.msize());
				if (d!= this)
					assign(d);
			}
			catch (Exception e)
			{
				throw new Error(e.getMessage());
			}
			return this;
		}
		else
			return super.movelFixedData(fStr);
	}


	/** Return the Java date format of the specified I2 date with no separators. */
	static String datfmt0(FixedDate value)
	{
		// This seems ludicrous, but the rule is if you move a DATE to a number, just always strip format characters
		// Just build a new format string with anything that is not a letter (or the ' placeholder) removed
		StringBuffer datfmt = new StringBuffer(value.datfmt);
		int length = datfmt.length();
		for (int i=0; i<length; )
		{
			char c = datfmt.charAt(i);
			if (i=='\'' || Character.isLetter(c))
				i++;
			else
			{
				datfmt.deleteCharAt(i);
				length--;
			}
		}
		return datfmt.toString();
	}
	
	/** 
	 * Compare to another object.
	 * @throws ClassCastException if not one of date, Loval, Hival
	 */
	public int compareTo(Object o) throws ClassCastException
	{
		try
		{
			if (o instanceof FixedDate)
				return compareTo((FixedDate) o);
			if (o instanceof Hival)
				return compareTo((Hival) o);
			if (o instanceof Loval)
				return compareTo((Loval) o);
		}
		catch (Exception e) {}
		throw new ClassCastException("Cannot cast to date");
	}
	

	/** Compare this date to another date. */
	public int compareTo(FixedDate value) throws ParseException
	{
		return toDate().compareTo(value.toDate());
	}
	
	/** Compare this date to *HIVAL. */
	protected int compareTo(Hival value) throws ParseException
	{
		return toDate().compareTo(hivalDate());
	}
	
	/** Compare this date to *LOVAL. */
	protected int compareTo(Loval value) throws ParseException
	{
		return toDate().compareTo(lovalDate());
	}

	/** Move a numeric value to the right-most bytes of this date. 
	public FixedData move(IFixed value)
	{
		try
		{
			date d = date0();
			d.move(value);
			if (d != this)
				assign(d);
		}
		catch (Exception e)
		{
			throw new Error(e.getMessage());
		}
		return this;
	}

	/** Move a numeric value to the left-most number 
	public FixedData movel(IFixed value)
	{
		try
		{
			date d = date0();
			d.movel(value);
			if (d != this)
				assign(d);
		}
		catch (Exception e)
		{
			throw new Error(e.getMessage());
		}
		return this;
	}
	*/
	
	/*
	public static void main(String[] args)
	{
		date mdy = new date("*MDY");
		date iso = new date("*ISO");
		date cymd = new date("*CYMD");
		mdy.assign(Application.HIVAL);
		iso.assign(Application.HIVAL);
		cymd.assign(Application.HIVAL);
	}
	*/
}
