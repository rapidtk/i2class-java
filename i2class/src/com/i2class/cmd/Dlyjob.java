/*
 * Created on Oct 15, 2004
 *
 */
package com.i2class.cmd;

import java.sql.Connection;

import com.i2class.*;

/**
 * A Delay Job class.
 * 
 *
 */
public class Dlyjob extends AbstractCommand {

	private int dly;
	
	public Dlyjob() {}
	public Dlyjob(I2Connection rconn) 
	{
		super(rconn);
	}

	static final String[] PARM_NAMES={"DLY"};
	/* (non-Javadoc)
	 * @see com.i2class.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}
	
	/** Set the number of seconds to delay the job (thread). */
	public void setDly(int seconds)
	{
		dly = seconds;
	}
	public void setDly(Object seconds)
	{
		setDly(Integer.parseInt(seconds.toString()));
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
	public void exec()
	{
		delay(dly);
	}
	/** Delay the execution of the current job (thread) by the specified number of seconds */
	public void exec(int dly)
	{
		setDly(dly);
		exec();
	}
	/** Delay the execution of the current job (thread) by the specified number of seconds */
	public void exec(Object dly)
	{
		setDly(dly);
		exec();
	}

}
