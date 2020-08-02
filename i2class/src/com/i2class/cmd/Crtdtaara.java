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
public class Crtdtaara extends AbstractCommand {

	private String m_dtaara, m_type, m_len, m_value;

	public Crtdtaara() {}
	public Crtdtaara(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"DTAARA", "TYPE", "LEN", "VALUE"};
	/* (non-Javadoc)
	 * @see com.i2class.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}

	public void setDtaara(String dtaara) {
		m_dtaara = Application.trimr(dtaara);
	}
	
	public void setType(String type) {
		m_type = Application.trimr(type);
	}
	
	public void setLen(String len) {
		m_len = Application.trimr(len);
	}
	public void setLen(INumeric len) {
		setLen(len.toString());
	}
	public void setLen(int len) {
		m_len = Integer.toString(len);
	}
	
	public void setValue(Object value) {
		m_value = value.toString();
	}
	public void setValue(double value) {
		m_value = Double.toString(value);
	}
	public void setValue(char value) {
		m_value = new Character(value).toString();
	}
	
	/** Create the specified dataarea. */
	public void exec(Object dtaara, Object type) throws Exception {
		setDtaara(dtaara.toString());
		setType(type.toString());
		exec();
	}

	/* (non-Javadoc)
	 * @see com.i2class.ICommand#exec()
	 */
	public void exec() throws Exception {
		I2Connection rconn = getRconn();
		QfileName q = rconn.qfileName(m_dtaara, ".dtaara");
		String dataType;
		// Decimal (*DEC) data area
		if (m_type.compareTo("*DEC")==0)
		{
			// For decimal lengths, a scale and precision can be specified
			StringTokenizer st = new StringTokenizer(m_len, " ");
			String length = st.nextToken();
			String scale;
			if (st.hasMoreTokens())
				scale = st.nextToken();
			else
				scale="0";
			dataType = "NUMERIC(" + length + ',' + scale + ')';
		}
		else if (m_type.compareTo("*LGL")==0)
			dataType = "INTEGER";
		else
		{
			// For *CHAR values, the default length is the length of the initial value, if specified
			if (m_len==null && m_value!=null)
					m_len = Integer.toString(m_value.length());
			dataType = "CHAR(" + m_len + ')';
		}
					
		FixedChar msgdta = new FixedChar(20, q.fileName);
		msgdta.setFixedAt(10, new FixedChar(10, q.libName));
		Sndpgmmsg sndpgmmsg = new Sndpgmmsg(rconn);
		sndpgmmsg.setMsgdta(msgdta);
		
		String ddl;
		Statement stmt = getStatement();
		try
		{
			q.fileName = q.fileName + ".dtaara";
			String createTableString = rconn.createTableString(q);
			
			ddl = createTableString + " (DATA " + dataType + " DEFAULT NOT NULL)";
			rconn.createTableExec(ddl, q);
		}
		catch (SQLException e)
		{
			I2Logger.logger.info(e);
			String sqlState = e.getSQLState();
			if (sqlState.compareTo("42710")==0) // A duplicate object or constraint name was detected
				//  Data area &1 exists in &2
				sndpgmmsg.exec("CPF1023", "QCPFMSG");
			else
				throw e;
		}
		
		// Insert dummy row
		ddl = "INSERT INTO " + q.schemaName + '"' + q.fileName + "\" VALUES(DEFAULT)";
		stmt.execute(ddl);
		
		// Change the value of the dataarea if an initial value is specified
		if (m_value != null)
		{
			Chgdtaara chgdtaara = new Chgdtaara(getRconn());
			chgdtaara.exec(m_dtaara, m_value);
		}
		
		// If we get here then the data area was successfully created
		// Data area &1 created in library &2
		sndpgmmsg.completion("CPC0904", "QCPFMSG");
	}
}
