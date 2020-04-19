/*
 * Created on Nov 15, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * DLTDTAQ (delete data queue) processing.
 * 
 */
public class Dltdtaq extends AbstractCommand {

	private String m_dtaq;

	public Dltdtaq() {}
	public Dltdtaq(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"DTAQ"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}

	/**
	 * @param string
	 */
	public void setDtaq(String dtaq) {
		m_dtaq = Application.trimr(dtaq);
	}
	
	/** Delete the specified data queue. */
	public void exec(String dtaq) throws Exception {
		setDtaq(dtaq);
		exec();
	}
	public void exec(FixedChar dtaq) throws Exception {
		exec(dtaq.toString());
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		Dltobj dltobj = new Dltobj(getRconn());
		dltobj.exec(m_dtaq, "*DTAQ");
	}
	

}
