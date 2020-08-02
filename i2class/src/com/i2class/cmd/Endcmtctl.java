/*
 * Created on Nov 18, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * ENDCMTCTL (End commitment control) processing.
 * 
 */
public class Endcmtctl extends EndcmtctlBase {

	public Endcmtctl() {}
	public Endcmtctl(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"LCKLVL"};
	/* (non-Javadoc)
	 * @see com.i2class.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}

	
}
