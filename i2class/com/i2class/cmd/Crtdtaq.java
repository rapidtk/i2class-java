/*
 * Created on Nov 15, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * CRTDTAQ (Create data queue) processing.
 * @author ANDREWC
 */
public class Crtdtaq extends AbstractCommand {

	private String m_dtaq, m_force, m_seq;
	private int m_maxlen;
	private Integer m_keylen;
	
	public Crtdtaq() {}
	public Crtdtaq(I2Connection rconn) {
		super(rconn);
	}
	
	static final String[] PARM_NAMES={"DTAQ", "MAXLEN", "FORCE", "SEQ", "KEYLEN"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}
	
	public void setDtaq(String dtaq) {
		m_dtaq= Application.trimr(dtaq);
	}
	
	public void setMaxlen(Object maxlen) {
		setMaxlen(Integer.parseInt(maxlen.toString()));
	}
	public void setMaxlen(int maxlen) {
		m_maxlen = maxlen;
	}

	public void setForce(String force) {
		m_force=Application.trimr(force);
	}
	public void setForce(fixed force) {
		setForce(force.toString());
	}
	
	public void setKeyLen(Object keylen) {
		m_keylen=Integer.valueOf(keylen.toString());
	}
	public void setKeylen(int keylen) {
		m_keylen = numeric.newInteger(keylen);
	}
	
	/** Create the specified data queue. */
	public void exec(String dtaq) throws Exception {
		setDtaq(dtaq);
		exec();
	}
	public void exec(fixed dtaq) throws Exception {
		exec(dtaq.toString());
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		// Extract schema, file name
		String schemaName, queueName, libName;
		int i = m_dtaq.indexOf('/');
		if (i>0)
		{
			libName = m_dtaq.substring(0, i);
			schemaName = libName + '.';
			queueName=m_dtaq.substring(i+1);
		}
		else
		{
			libName="*LIBL";
			schemaName="";
			queueName=m_dtaq;
		}
		
		fixed msgdta = new fixed(30, queueName);
		msgdta.setFixedAt(10, new fixed(10, libName));
		msgdta.setFixedAt(20, new fixed(7, "DTAQ"));
		String qFileName = schemaName + '"' + queueName + ".dtaq\"";
		String ddl = "CREATE TABLE " + qFileName + " (data BLOB, maxlen INT, force CHAR(4), seq CHAR(6), keylen INT)";
		try {
			Connection conn = getConnection();
			getStatement().execute(ddl);
			PreparedStatement ps = conn.prepareStatement("INSERT INTO " + qFileName + " (maxlen, force, seq, keylen) VALUES(?,?,?,?)");
			ps.setInt(1, m_maxlen);
			ps.setString(2, m_force);
			ps.setString(3, m_seq);
			ps.setObject(4, m_keylen);
			ps.execute(); 
		} catch (SQLException e) {
			String sqlState = e.getSQLState();
			I2Logger.logger.info(e);
			if (sqlState.compareTo("42710")==0) // "A duplicate object or constraint name was detected"
				// CPF9870 -- Object &2 type *&5 already exists in library &3
				throw new Pgmmsg("CPF9870", "QCPFMSG", msgdta);
			else
				throw e;
		} 
		// CPC9801 -- Object &2 type *&5 created in library &3
		new Sndpgmmsg(getApp()).completion("CPC9801", "QCPFMSG", msgdta);
	}
	

}
