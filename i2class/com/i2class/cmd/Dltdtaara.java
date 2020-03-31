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
 * CRTLIB (create library) processing.
 * @author ANDREWC
 */
public class Dltdtaara extends AbstractCommand {

	private String m_dtaara;

	public Dltdtaara() {}
	public Dltdtaara(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"DTAARA"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}

	public void setDtaara(String dtaara) {
		m_dtaara = Application.trimr(dtaara);
	}
	
	/** Delete the specified dataarea. */
	public void exec(String dtaara) throws Exception {
		setDtaara(dtaara);
		exec();
	}
	public void exec(fixed dtaara) throws Exception {
		exec(dtaara.toString());
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		I2Connection rconn = getRconn();
		QfileName q = rconn.qfileName(m_dtaara, ".dtaara");
		//String ddl = "DROP TABLE " + q.schemaName + '"' + q.fileName + ".dtaara\"";
		try
		{
			rconn.deleteTable(q);
		}
		catch (SQLException e)
		{
			// CPF2105 -- Object &1 in &2 type *&3 not found
			String sqlState = e.getSQLState();
			if (sqlState.compareTo("42704")==0) // An undefined object name was detected
			{
				fixed msgdta = new fixed(27, q.fileName);
				msgdta.setFixedAt(10, new fixed(10, q.libName));
				msgdta.setFixedAt(20, new fixed(7, "*DTAARA"));
				throw new Pgmmsg("CPF2105", "QCPFMSG", msgdta);
			}
			else
				throw e;
		}
	}
}
