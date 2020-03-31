/*
 * Created on Nov 15, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * CRTLIB (create library) processing.
 * @author ANDREWC
 */
public class Crtlib extends AbstractCommand {

	private String m_lib;

	public Crtlib() {}
	public Crtlib(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"LIB"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}

	public void setLib(String lib) {
		m_lib = Application.trimr(lib);
	}
	
	/** Clear the specified library. */
	public void exec(String lib) throws Exception {
		setLib(lib);
		exec();
	}
	public void exec(fixed lib) throws Exception {
		exec(lib.toString());
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		String ddl = "CREATE SCHEMA " + m_lib;
		try
		{
			getStatement().execute(ddl);
		}
		catch (SQLException e)
		{
			// CPF0001 -- Error found on &1 command
			fixed msgdta = new fixed(10, "CRTLIB");
			throw new Pgmmsg("CPF0001", "QCPFMSG", msgdta);
		}
	}
	

}
