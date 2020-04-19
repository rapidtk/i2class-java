/*
 * Created on Nov 15, 2004
 *
 */
package com.i2class;

import java.io.File;
import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * CHGDTAARA (change data area) base class processing.
 * 
 */
public abstract class ChgdtaaraBase extends AbstractCommand { 

	private String m_dtaara;
	private Object m_value;

	public ChgdtaaraBase() {}
	public ChgdtaaraBase(I2Connection rconn) {
		super(rconn);
	}

	public void setDtaara(String dtaara) {
		m_dtaara = dtaara;
	}
	public void setValue(String value) {
		m_value = value;
	}
	public void setValue(double value) {
		m_value = Double.toString(value);
	}
	public void setValue(char value) {
		m_value = new Character(value).toString();
	}
	

	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		
		// The data area parameter can be a data name and a substring specification
		StringTokenizer st = new StringTokenizer(m_dtaara, " ");
		
		// The file name is DTAARA.dtaara and contains one field 'DATA'
		
		// If a substring is specified, only update that portion of the data
		
		/* There are no common (between SQL Server/Oracle/DB2) CONCAT function, so just get value and do string
		 * manipulation instead
		String expression="?";
		if (st.hasMoreTokens())
		{
			String start = st.nextToken("( )");
			if (start.compareTo("*ALL")!=0)
			{
				int s = Integer.parseInt(start);
				if (s>1)
					expression = "substring(DATA, 1, " + start + ") CONCAT " + expression;
				String length = st.nextToken();
				int l = Integer.parseInt(length);
				expression = expression + " CONCAT substring(DATA, " + (s+l) + ')';
			}
		}
		 */
		String data = m_value.toString();
		I2Connection rconn = getRconn();
		QfileName q = rconn.qfileName(st.nextToken(), ".dtaara");
		try {
			DataAreaJDBC dtaara = new DataAreaJDBC(rconn, q);
			if (st.hasMoreTokens())
			{
				String start = st.nextToken("( )");
				if (start.compareTo("*ALL")!=0 && st.hasMoreTokens())
				{
					StringBuffer buf = new StringBuffer(dtaara.getObject().toString());
					int s = Integer.parseInt(start);
					int l = Integer.parseInt(st.nextToken());
					s--;
					buf.replace(s, s+l, data);
					data=buf.toString();
				}
			}
			dtaara.updateObject(data);
		} catch (SQLException e) {
			// CPF1015 -- Data area &1 in &2 not found
			String sqlState = e.getSQLState();
			if (sqlState.compareTo("42704")==0) // An undefined object name was detected
			{
				FixedChar msgdta = new FixedChar(20, q.fileName);
				msgdta.setFixedAt(10, new FixedChar(10, q.libName));
				throw new Pgmmsg("CPF1015", "QCPFMSG", msgdta);
			}
			else
				throw e;
		} 
	}
}
