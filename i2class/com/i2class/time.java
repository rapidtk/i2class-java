package com.i2class;

/**
 * An OS/400-like 6 character (e.g. HH.MM.SS) time.
 * 
 * @author Andrewc
 *
 */
public class time extends date {
	public time()
	{
		this("*ISO");
	}
	
	public time(String timfmt400)
	{
		super(getTimeFormat(timfmt400), 0, timfmt400);
	}
	
	public time(String timfmt400, Hival value)
	{
		this(timfmt400);
		assign(value);
	}
	
	public time(String timfmt400, Loval value)
	{
		this(timfmt400);
		assign(value);
	}

	/**
	 * Construct a time with the specified Java date format (not OS/400 e.g. hms not *HMS)
	 */
	time(String datfmt, int length, String datfmt400) {
		super(datfmt, length, datfmt400);
	}


	/** Create a time with an initial time value */
	public time(String timfmt400, time value)
	{
		this(timfmt400);
		updating=true;
		try
		{
			assign(value);
		}
		catch(Exception e) {}
		updating=false;
	}

	/**
	 * Create a time value that overlays another value using the default (I2App._DATFMT) date format 
	 */
	public time(pointer overlay)
	{
		super(Application.timeFormat, Application._TIMFMT, overlay);
	}

	/**
	 * Create a new time with the specified format and value.  
	 * The format of the <code>inzval</code> inital value must
	 * match the <code>datfmt400</code> date format.
	 * @param datfmt400 The OS/400 date type. 
	 * @param inzval The initial data value.
	 */
	public time(String datfmt400, String inzval)
	{
		this(datfmt400);
		updating=true;
		assign(inzval);
		updating=false;
	}
	
	/* (non-Javadoc)
	 * @see com.asc.rio.date#newDate0(java.lang.String, com.asc.rio.date)
	 */
	protected date newDate0(String datfmt400, date value)
	{
		return new time(datfmt400, (time)value);
	}


	/**
	 * Get Java date format (e.g. "HH.mm.ss") from a 'special' OS/400 date style (e.g. "*ISO").
	 * @return java.lang.String
	 * @param datfmt java.lang.String
	 */
	static String getTimeFormat(String datfmt400)
	{
		String hoursep = "";
		int dlen = datfmt400.length() - 1;
		if (dlen >= 4)
		{
			// A '&' means no separator
			char dsep = datfmt400.charAt(dlen);
			if (dsep != '&' && dsep != '0')
				hoursep = datfmt400.substring(dlen);
			datfmt400 = datfmt400.substring(0, dlen);
			if (datfmt400.compareTo("*JOBRUN") == 0)
				//datfmt400 = Application._TIMFMT;
				datfmt400 = "*ISO";
		}
		else if (
			datfmt400.compareTo("*ISO") == 0 || datfmt400.compareTo("*EUR") == 0)
			hoursep = ".";
		else /*if (datfmt400.compareTo("*EUR") == 0 || datfmt400.compareTo("*HMS")==0 || datfmt400.compareTo("*USA")==0) */
			hoursep = ":";
			
		// Add AM/PM marker for *USA
		String HH, am;
		if (datfmt400.compareTo("*USA")==0)
		{
			HH="hh";
			am=" aa";
		}
		else
		{
			HH="HH";
			am="";
		}
		String dateFormat = HH + hoursep + "mm" + hoursep + "ss" + am;
		return dateFormat;
	}
	
/*
	public static void main(String []args)
	{
		time t1 = new time();
		time t2 = new time("*HMS");
		time t3 = new time("*USA");
		time t4 = new time("*ISO");
		try
		{
		t1.adddur(1, Application.HOURS);
		t1.adddur(2, Application.MINUTES);
		t1.adddur(3, Application.SECONDS);
		t2.assign(Application.HIVAL);
		t3.assign(Application.HIVAL);
		t4.assign(Application.HIVAL);
		}
		catch (Exception e) {}
	}
	*/
}
