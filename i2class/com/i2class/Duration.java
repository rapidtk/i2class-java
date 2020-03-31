package com.i2class;

/**
 * A duration (YEARS, MONTHS, DAYS, HOURS, MINUTES, SECONDS) that can be added to a date/time value.
 *  
 * @author ANDREWC
 * 
 * @see com.i2class.Application#YEARS
 * @see Application#MONTHS
 * @see Application#DAYS
 * @see Application#HOURS
 * @see Application#MINUTES
 * @see Application#SECONDS
 * @see Application#MICROSECONDS
 */
public class Duration {
	int duration, durationCode;
	/** Create a duration of the specified type and length. */
	public Duration(int duration, int durationCode)
	{
		this.duration = duration;
		this.durationCode = durationCode;
	}
}
