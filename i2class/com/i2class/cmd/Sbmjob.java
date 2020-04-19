/*
 * Created on Nov 18, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * SBMJOB (Submit job) processing.
 * 
 */
public class Sbmjob extends AbstractCommand {

	private String m_cmd;
	
	public Sbmjob() {}
	public Sbmjob(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"CMD"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}

	public void setCmd(String cmd) {
		m_cmd = cmd;
	}
	
	/** 'Submit' the specified command. */
	public void exec(String cmd) throws Exception
	{
		setCmd(cmd);
		exec();
	}
	public void exec(FixedChar cmd) throws Exception
	{
		exec(cmd.toString());
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		// Set parameters for call
		QCMDEXC qcmdexc = new QCMDEXC(getApp());
		int cmdLength = m_cmd.length();
		qcmdexc.cmdString = new FixedChar(cmdLength, m_cmd);
		qcmdexc.cmdLength.assign(cmdLength);
		// Start thread
		qcmdexc.start();
	}
}
