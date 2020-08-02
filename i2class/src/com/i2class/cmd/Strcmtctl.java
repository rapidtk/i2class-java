/*
 * Created on Nov 18, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * STRCMTCTL (Start commitment control) processing.
 * 
 */
public class Strcmtctl extends StrcmtctlBase {

	public Strcmtctl() {}
	public Strcmtctl(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"LCKLVL"};
	/* (non-Javadoc)
	 * @see com.i2class.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}

	
	/** Start commitment control with the specified lock level. */
	public void exec(String lcklvl) throws Exception
	{
		setLcklvl(lcklvl);
		exec();
	}
	public void exec(FixedChar lcklvl) throws Exception
	{
		setLcklvl(lcklvl.toString());
	}

}
