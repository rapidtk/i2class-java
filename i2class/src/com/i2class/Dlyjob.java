/*
 * Created on Oct 15, 2004
 *
 */
package com.i2class;

import java.sql.Connection;

/**
 * A Delay Job class.
 * 
 *
 */
public class Dlyjob {

	private int dly;
	
	public Dlyjob(Connection host) {}
	
	/** Set the number of seconds to delay the job (thead). */
	public void setDly(int seconds)
	{
		dly = seconds;
	}
	
	/** Delay the execution of the current job (thread) by the specified number of seconds */
	public synchronized void delay(int seconds)
	{
		try {
			wait(seconds*1000);
		} catch (InterruptedException e) {}
	}
	
	/** 
	 * Delay the execution of the current job (thread).
	 * @see setDly(int) 
	 * */
	public void execute()
	{
		delay(dly);
	}
	
	/** Delay the execution of the current job (thread) by the specified number of seconds */
	public void execute(int dly)
	{
		delay(dly);
	}

}
