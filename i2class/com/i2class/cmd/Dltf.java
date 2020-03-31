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
public class Dltf extends AbstractCommand {

	private String m_file;

	public Dltf() {}
	public Dltf(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"FILE"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}

	/**
	 * @param string
	 */
	public void setFile(String file) {
		m_file = Application.trimr(file);
	}
	
	/** Delete the specified file. */
	public void exec(String file) throws Exception {
		setFile(file);
		exec();
	}
	public void exec(fixed file) throws Exception {
		exec(file.toString());
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		Dltobj dltobj = new Dltobj(getRconn());
		dltobj.exec(m_file, "*FILE");
	}
	

}
