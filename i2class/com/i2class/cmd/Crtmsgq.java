/*
 * Created on Nov 15, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * CRTMSGQ (Create message queue) processing.
 * @author ANDREWC
 */
public class Crtmsgq extends AbstractCommand {

	private String m_msgq;
	
	public Crtmsgq() {}
	public Crtmsgq(I2Connection rconn) {
		super(rconn);
	}
	
	static final String[] PARM_NAMES={"MSGQ"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}
	
	public void setMsgq(String msgq) {
		m_msgq= Application.trimr(msgq);
	}
	
	/** Create the specified data queue. */
	public void exec(String msgq) throws Exception {
		setMsgq(msgq);
		exec();
	}
	public void exec(fixed msgq) throws Exception {
		exec(msgq.toString());
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		// Extract schema, file name
		String schemaName, queueName, libName;
		int i = m_msgq.indexOf('/');
		if (i>0)
		{
			libName = m_msgq.substring(0, i);
			schemaName = libName + '.';
			queueName=m_msgq.substring(i+1);
		}
		else
		{
			libName="*LIBL";
			schemaName="";
			queueName=m_msgq;
		}
		
		fixed msgdta = new fixed(30, queueName);
		msgdta.setFixedAt(10, new fixed(10, libName));
		msgdta.setFixedAt(20, new fixed(7, "DTAQ"));
		String qFileName = schemaName + '"' + queueName + ".msgq\"";
		String ddl = "CREATE TABLE " + qFileName + " (data BLOB, maxlen INT, force CHAR(4))";
		try {
			getStatement().execute(ddl);
		} catch (SQLException e) {
			I2Logger.logger.info(e);
			// CPF2112 -- Object &1 in &2 type *&3 already exists.
			throw new Pgmmsg("CPF2112", "QCPFMSG", msgdta);
		} 
		// CPC9801 -- Object &2 type *&5 created in library &3
		new Sndpgmmsg(getApp()).completion("CPC9801", "QCPFMSG", msgdta);
	}
	

}
