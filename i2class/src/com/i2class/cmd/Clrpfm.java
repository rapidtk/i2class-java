/*
 * Created on Nov 15, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * CLRPFM (clear physical file member) processing.
 * 
 */
public class Clrpfm extends AbstractCommand {

	private String m_file, m_mbr;
	
	public Clrpfm() {}
	public Clrpfm(I2Connection rconn) {
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
	
	/** Clear the first member of the specified file. */
	public void exec(FixedChar file) throws Exception {
		exec(file.trim());
	}
	public void exec(String file) throws Exception {
		exec(file, "*FIRST");
	}
	
	/** Clear the specified member. */
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
			schemaName = '"' + m_file.substring(0, i) + "\".";
			libName=schemaName;
			fileName=m_file.substring(i+1);
		}
		else
		{
			schemaName="";
			libName="*LIBL";
			fileName=m_file;
		}
		
		String mbrName;
		FixedChar msgdta = new FixedChar(30, fileName);
		msgdta.setFixedAt(10, new FixedChar(10, m_mbr));
		msgdta.setFixedAt(20, new FixedChar(10, libName));
		// If this is the *LAST member, then get last member in list
		if (m_mbr.equals("*LAST"))
		{
			DatabaseMetaData dbmd = getConnection().getMetaData();
			String filePattern = fileName + "(%";
			ResultSet rsTables = dbmd.getTables(null, schemaName, filePattern, TABLE_TYPES);
			rsTables.last();
			mbrName=rsTables.getString(3);
			rsTables.close();
		}
		else 
			mbrName = m_mbr;
		String ddl = "DELETE FROM " + schemaName + '"' + fileName;
		// If they are deleting from a member other than *FIRST, add to DELETE
		if (m_mbr.compareTo("*FIRST")!=0 && m_mbr.compareTo(fileName)!=0)
			ddl = ddl + '(' + mbrName + ')';
		ddl = ddl + '"';
		try {
			getStatement().execute(ddl);
		} catch (SQLException e) {
			I2Logger.logger.printStackTrace(e);
			//TODO figure out what the SQLException actually was -- CPF3142 (file not found) or CPF3141 (member not found)
			throw new Pgmmsg("CPF3141", "QCPFMSG", msgdta);
		}
		// If we get here then the member was cleared 
	}
	

}
