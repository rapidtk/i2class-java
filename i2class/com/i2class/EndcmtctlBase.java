/*
 * Created on Nov 18, 2004
 *
 */
package com.i2class;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * A base class for ENDCMTCTL (End commitment control) processing.
 * @author ANDREWC
 */
public abstract class EndcmtctlBase extends AbstractCommand {

	public EndcmtctlBase() {}
	public EndcmtctlBase(I2Connection rconn) {
		super(rconn);
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		
		Application app = getApp();
		// If no commitment definition has been specified, then this is an error
		if (app.appJob.connCommit == null)
			throw new Pgmmsg("CPF8350", "QCPFMSG");
			
		// Rollback any uncommitted changes
		app.appJob.connCommit.rollback();
		// End transaction isolation
		app.appJob.connCommit.close();
		app.appJob.connCommit=null;
	}
}
