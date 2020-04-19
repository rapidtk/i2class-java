/*
 * Created on Nov 15, 2004
 *
 */
package com.i2class.cmd;

import java.io.File;
import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * CRTDTAARA (create data area) processing.
 * 
 */
public class Chgdtaara extends com.i2class.ChgdtaaraBase { 

	public Chgdtaara() {}
	public Chgdtaara(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"DTAARA", "VALUE"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}

	/** Change the value of the specified dataarea. */
	public void exec(Object dtaara, double value) throws Exception {
		exec(dtaara, Double.toString(value));
	}
	/** Change the value of the specified dataarea. */
	public void exec(Object dtaara, char value) throws Exception {
		exec(dtaara, new Character(value).toString());
	}

	/** Change the value of the specified dataarea. */
	public void exec(Object dtaara, Object value) throws Exception {
		setDtaara(dtaara.toString());
		setValue(value.toString());
		exec();
	}

}
