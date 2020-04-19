/*
 * Created on Nov 18, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * DLCOBJ (Deallocate object) processing.
 * 
 */
public class Dlcobj extends AbstractCommand {

	private String m_obj;
	
	public Dlcobj() {}
	public Dlcobj(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"OBJ"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}
	
	public void setObj(String obj) {
		m_obj = Application.trimr(obj);
	}

	/** Deallocate the specified object. */
	public void exec(String obj) throws Exception {
		setObj(obj);
		exec();
	}
	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		// TODO find statement associated with m_obj and close it
	}
}
