/*
 * Created on Nov 15, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * RMVM (remove member) processing.
 * 
 */
public class Rmvm extends AbstractCommand {

	private Statement m_stmt;
	private String m_file, m_mbr;
	private FixedChar msgdta;
	
	public Rmvm() {}
	public Rmvm(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"FILE", "MBR"};
	/* (non-Javadoc)
	 * @see com.i2class.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}

	public void setFile(String file) {
		m_file = Application.trimr(file);
	}

	public void setMbr(String mbr) {
		m_mbr = Application.trimr(mbr);
	}
	
	/** Remove a member from the specified file. */
	public void exec(Object file, Object mbr) throws Exception {
		setFile(file.toString());
		setMbr(mbr.toString());
		exec();
	}

	/* (non-Javadoc)
	 * @see com.i2class.ICommand#exec()
	 */
	public void exec() throws Exception {
		// Extract schema, file name
		String schemaName, fileName, libName;
		int i = m_file.indexOf('/');
		if (i>0)
		{
			libName = m_file.substring(0, i);
			schemaName = '"' + libName + "\".";
			fileName=m_file.substring(i+1);
		}
		else
		{
			schemaName="";
			libName="*LIBL";
			fileName=m_file;
		}
		String mbrPattern; 
		boolean generic = (m_mbr.indexOf('*')>=0);
		if (generic)
			mbrPattern = m_mbr.replace('*', '%');
		else
			mbrPattern = m_mbr + ')';
		String filePattern = fileName + '(' + mbrPattern;

		// Set up message data
		msgdta = new FixedChar(30, fileName);
		msgdta.setFixedAt(10, new FixedChar(10, m_mbr));
		msgdta.setFixedAt(20, new FixedChar(10, libName));
		// Build generic member list
		m_stmt = getStatement();
		if (generic)
		{
			DatabaseMetaData dbmd = getConnection().getMetaData();
			ResultSet rsTables = dbmd.getTables(null, schemaName, filePattern, TABLE_TYPES);
			while (rsTables.next())
				removeMember(schemaName, rsTables.getString(3));
			rsTables.close();
		}
		else
			removeMember(schemaName, filePattern);
		
	}
	
	private void removeMember(String qschemaName, String fileName) throws SQLException, Pgmmsg
	{
		String ddl = "DROP TABLE " + qschemaName + '"' + fileName + '"';
		try
		{
			m_stmt.execute(ddl);
		}
		// CPF7310 -- Member &1 not removed from file &2 in &3
		catch (SQLException e)
		{
			throw new Pgmmsg("CPF7310", "QCPFMSG", msgdta);
		}
		// If we get here then the member was dropped
		// CPC7309 -- Member &1 removed from file &2 in &3
		new Sndpgmmsg(getApp()).completion("CPC7305", "QCPFMSG", msgdta);
	}
	

}
