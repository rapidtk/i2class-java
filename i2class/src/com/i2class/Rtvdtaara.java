/*
 * Created on Feb 28, 2005
 *
 */
package com.i2class;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.ResultSet;
import java.util.StringTokenizer;

/**
 * RTVDTAARA (retrieve data area) processing.
 * 
 */
public class Rtvdtaara extends AbstractCommand { 

	private String m_dtaara;
	private FixedData m_rtnvar;

	public Rtvdtaara() {}
	public Rtvdtaara(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"DTAARA", "RTNVAR"};
	/* (non-Javadoc)
	 * @see com.i2class.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}

	public void setDtaara(String dtaara) {
		m_dtaara = Application.trimr(dtaara);
	}
	public void setRtnvar(FixedData rtnvar) {
		m_rtnvar = rtnvar;
	}
	
	/** Retrieve the value of the specified dataarea. */
	public void exec(String dtaara, FixedData rtnvar) throws Exception {
		setDtaara(dtaara);
		setRtnvar(rtnvar);
		exec();
	}

	/* (non-Javadoc)
	 * @see com.i2class.ICommand#exec()
	 */
	public void exec() throws Exception {
		
		// Split file name into parts
		I2Connection rconn = getRconn();
		// The data area parameter can be a data name and a substring specification
		StringTokenizer st = new StringTokenizer(m_dtaara, " ");
		QfileName q = rconn.qfileName(st.nextToken(), ".dtaara");
		try {
			DataAreaJDBC dtaara = new DataAreaJDBC(rconn, q);
			Object value = dtaara.getObject();
			// Numeric data area
			if (m_rtnvar instanceof AbstractNumeric)
				((AbstractNumeric)m_rtnvar).assign((BigDecimal)value);
			// Character data area
			else
			{
				// If a substring is specified, only return that portion of the data
				if (st.hasMoreTokens())
				{
					String start = st.nextToken("( )");
					if (start.compareTo("*ALL")!=0 && st.hasMoreTokens())
					{
						int s = Integer.parseInt(start)-1;
						int l = Integer.parseInt(st.nextToken());
						value = value.toString().substring(s, s+l);
					}
				}
				m_rtnvar.movelp((String)value);
			}
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
