/*
 * Created on Nov 18, 2004
 *
 */
package com.i2class;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * A base class for STRCMTCTL (Start commitment control) processing.
 * 
 */
public abstract class StrcmtctlBase extends AbstractCommand {

	private String m_lcklvl;
	
	public StrcmtctlBase() {}
	public StrcmtctlBase(I2Connection rconn) {
		super(rconn);
	}

	public void setLcklvl(String lcklvl) {
		m_lcklvl = Application.trimr(lcklvl);
	}
	
	/* (non-Javadoc)
	 * @see com.i2class.ICommand#exec()
	 */
	public void exec() throws Exception {
		
		
		// If a commitment control definition already exists, then signal error
		Application app = getApp();
		if (app.appJob.connCommit != null)
			throw new Pgmmsg("CPF8351", "QCPFMSG");
			
		// The commitment control definition doesn't exist yet, so create it
		I2Connection rconn = getRconn();
		app.appJob.connCommit = DriverManager.getConnection(rconn.m_url, rconn.m_usrid, rconn.m_password);

		// Map OS/400 lock level to Java isolation level 
		int javaCommit=Connection.TRANSACTION_NONE;
		// Lowest isolation level: allows commit/rollback
		if (m_lcklvl.compareTo("*CHG")==0)
			javaCommit=Connection.TRANSACTION_READ_UNCOMMITTED;
		// Prevents application from reading uncommitted data (dirty reads)
		else if (m_lcklvl.compareTo("*CS")==0)
			javaCommit=Connection.TRANSACTION_READ_COMMITTED;
		// Highest RPG isolation level:  
		else /*if (m_lcklvl.compareTo("*ALL")==0)*/
			javaCommit=Connection.TRANSACTION_REPEATABLE_READ;
		app.appJob.connCommit.setTransactionIsolation(javaCommit);
		app.appJob.connCommit.setAutoCommit(false);
	}
}
