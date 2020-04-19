package com.i2class;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

/**
 * An OS/400-like 26 character YYYY-MM-DD-HH:MM:SS:mmmmmm (accurate to the microsecond) timestamp.
 * 
 * 
 */
public class FixedTimestamp extends FixedDate {
	public FixedTimestamp()
	{
		// SimpleDateFormat can't handle microseconds, so we have to format based upon milliseconds and kludge
		// everything else.
		this("yyyy-MM-dd-HH.mm.ss.SSS", "*ISO");
	}
	
	private FixedTimestamp(String datfmt, String datfmt400)
	{
		super(datfmt, datfmt.length()+3, datfmt400); // Have to add 3 for micro-second length
	}

	/**
	 * Create a timestamp that overlays another value using the default (I2App._DATFMT) date format 
	 */
	public FixedTimestamp(FixedPointer overlay)
	{
		super("yyyy-MM-dd-HH.mm.ss.SSS", "*ISO", overlay);
	}

	/**
	 * Create a timestamp using *LOVAL
	 */
	public FixedTimestamp(Loval value)
	{
		this();
		//assign(value); *LOVAL is the default value
	}
	/**
	 * Create a timestamp using *HIVAL
	 */
	public FixedTimestamp(Hival value)
	{
		this();
		assign(value);
	}

	/**
	 * Create a timestamp initialized to another timestamp
	 */
	public FixedTimestamp(FixedTimestamp value)
	{
		this();
		assign(value);
	}

	/**
	 * Construct a time with the specified Java date format (not OS/400 e.g. Hms not *HMS)
	 */
	FixedTimestamp(String datfmt, int length, String datfmt400) {
		super(datfmt, length, datfmt400);
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.date#newDate0(java.lang.String, com.asc.rio.date)
	 */
	protected FixedDate newDate0(String datfmt400, FixedDate value)
	{
		FixedTimestamp ts = new FixedTimestamp("yyyyMMddHHmmssSSS", datfmt400);
		try
		{
			ts.assign(value);
		}
		catch (ParseException e) {}
		return ts;
	}

	/**
	 * Assign another timestamp value to this value.
	 */
	public void assign(FixedTimestamp value)
	{
		// All timestamps are the same format, so just do simple byte copy
		movel(value);
	}
	/**
	 * Assign a Java Timestamp value to this date class.
	 * @param value java.sql.Timestamp
	 */
	public void assign(Timestamp value)
	{
		// Reformat value so that it is the same format as this date data type, then assign
		String s = format(value);
		// Append microsecond value, if specified
		int microseconds = value.getNanos()/1000;
		if (microseconds>0)
		{
			String ms = Integer.toString(microseconds);
			s = s.substring(0, 26-ms.length()) + ms;
		}
		assign(s);
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.date#getMicroseconds()
	 */
	protected int getMicrorem() {
		return Integer.parseInt(toFixedString().substring(23));
	}
	/* (non-Javadoc)
	 * @see com.asc.rio.date#setMicroseconds(int)
	 */
	protected void setMicrorem(int microrem) {
		String ms = "00" + Integer.toString(microrem);
		move(ms.substring(ms.length()-3));
	}

	
	/* (non-Javadoc)
	 * @see com.asc.rio.date#toDate()
	 */
	public java.sql.Date toDate() throws ParseException {
		// The java Date value is everything but the last 3 positions of the timestamp (strip off the microsecond part)
		String dateString = toFixedString().substring(0,size()-3);
		java.util.Date d = simpleDateFormat.parse(dateString);
		//return d;
		return new java.sql.Date(d.getTime()); 
	}
	
	/* (non-Javadoc)
	 * @see com.asc.rio.date#format(Date value)
	 */
	protected String format(Date value)
	{
		return super.format(value) + "000";
	}
	/* (non-Javadoc)
	 * @see com.asc.rio.date#formatMicro(java.util.Date)
	 */
	protected String formatMicro(Date value) {
		return super.format(value) + toFixedString().substring(23);
	}

	
	/**
	 * Return the Java Timestamp representation of this 400 date data type
	 * @return java.sql.Timestamp
	 */
	public Timestamp toTimestamp() throws Exception
	{
			String tstring = toFixedString();
			// For whatever reason, Timestamps store the nano portion separately and never set the millisecond 
			// portion of the date, so strip off that portion (the first 20 positions).
			String dateString = tstring.substring(0,20)+"000";
			java.util.Date d = simpleDateFormat.parse(dateString); 
			Timestamp ts = new Timestamp(d.getTime());
			// The last six digits of the timestamp are the micro-second value (*1000=nanoseconds) 
			int microseconds = Integer.parseInt(tstring.substring(20)); // 20=size(timestamp)=26-len("000000")
			ts.setNanos(microseconds*1000);
			return ts;
	}
	
	public static void main(String[] args) throws Exception
	{
		FixedTimestamp ts = new FixedTimestamp();
		ts.adddur(10, Application.MONTHS);
		FixedTimestamp ts2 = new FixedTimestamp();
		ts2.assign(ts);
		FixedDate d = new FixedDate("*MDY", "03/19/70");
		ts2.assign(d);
		FixedTime t = new FixedTime("*HMS", "05:06:03");
		ts2.assign(t);
		ts2.adddur(5, Application.DAYS);
		d.assign(ts2);
		ts2.adddur(8, Application.HOURS);
		t.assign(ts2);
		PackedDecimal p = new PackedDecimal(7, 0 , 1234567);
		ts2.move(p);
		ZonedDecimal z = new ZonedDecimal(5,0,19990);
		ts2.movel(z);
		
	}
	
}
