/*
 * Created on Nov 15, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * DLTF (delete file) processing.
 * @author ANDREWC
 */
public class Clrlib extends AbstractCommand {

	private String m_lib;

	public Clrlib() {}
	public Clrlib(I2Connection rconn) {
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
		Dltobj dltobj = new Dltobj(getRconn());
		dltobj.exec(m_lib+"/*ALL", "*ALL");
	}
	

}
