/*
 * Created on Nov 16, 2004
 *
 */
package com.i2class;

import java.sql.*;
import java.util.*;


/**
 * RMVMSG (Remove message) processing.
 * @author ANDREWC
 */
public class Rmvmsg extends AbstractCommand {

	private String m_pgmq="*SAME";
	private fixed m_msgkey;
	private String m_clear="*BYKEY";

	/**
	 * 
	 */
	public Rmvmsg(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}

	public void setPgmq(String pgmq) {
		m_pgmq = Application.trimr(pgmq);
	}
	public void setMsgkey(fixed msgkey) {
		m_msgkey = msgkey;
	}
	public void setClear(String clear) {
		m_clear = Application.trimr(clear);
	}
	

	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		//TODO actually remove message
	}
}
