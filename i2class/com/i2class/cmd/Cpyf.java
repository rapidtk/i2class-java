/*
 * Created on Nov 15, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * CPYF (copy file) processing.
 * 
 */
public class Cpyf extends AbstractCommand {

	private Statement m_stmt;
	private String m_fromfile, m_tofile;
	private String m_frommbr="*FIRST";
	private String m_tombr="*FIRST";
	private String m_mbropt="*NONE";
	private String m_crtfile="*NO";
	
	public Cpyf() {}
	public Cpyf(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"FROMFILE", "TOFILE", "FROMMBR", "TOMBR", "CRTFILE"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}

	public void setFromfile(String fromfile) {
		m_fromfile = fromfile;
	}

	public void setTofile(String tofile) {
		m_tofile = Application.trimr(tofile);
	}

	public void setFrommbr(String frommbr) {
		m_frommbr = Application.trimr(frommbr);
	}
	public void setFrommbr(FixedChar frommbr) {
		setFrommbr(frommbr);
	}

	public void setTombr(String tombr) {
		m_tombr = Application.trimr(tombr);
	}
	public void setTombr(FixedChar tombr) {
		setTombr(tombr.toString());
	}

	public void setCrtfile(String crtfile)
	{
		m_crtfile=Application.trimr(crtfile);
	}
	public void setCrtfile(FixedChar crtfile)
	{
		setCrtfile(crtfile.toString());
	}
	public void setMbropt(String mbropt)
	{
		setCrtfile(Application.trimr(mbropt));
	}
	public void setMbropt(FixedChar mbropt)
	{
		setCrtfile(mbropt.toString());
	}
	
	/** Copy data from one file to another. */
	public void exec(Object fromfile, Object tofile) throws Exception {
		setFromfile(fromfile.toString());
		setTofile(tofile.toString());
		exec();
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		// Extract schema, file name
		QfileName fromfile = getRconn().qfileName(m_fromfile);
		
		// Copy data from one member
		m_stmt = getStatement();
		String frommbr="";
		QfileName copyfile; 
		boolean allMembers = (m_frommbr.compareTo("*ALL")==0);
		if (!allMembers && m_frommbr.compareTo("*FIRST")!=0 && m_frommbr.compareTo(fromfile.fileName)!=0)
			copyfile = new QfileName(fromfile.libName, fromfile.schemaName, '(' + m_frommbr + ')');
		else
			copyfile = fromfile;
		try
		{
			copyData(copyfile);
				
			// If this a FROMMBR(*ALL), copy data from all members except *FIRST
			if (allMembers)
			{
				DatabaseMetaData dbmd = getConnection().getMetaData();
				ResultSet rsTables = dbmd.getTables(null, fromfile.schemaName, fromfile.fileName+'(', TABLE_TYPES);
				while (rsTables.next())
				{
					copyfile.fileName = rsTables.getString(3);
					copyData(copyfile);
				}
				rsTables.close();
			}
		}
		// Handle any errors issued from command
		catch (SQLException e)
		{
			I2Logger.logger.info(e);
			throw new Pgmmsg("CPF2817", "QCPFMSG");
		}
	}
	
	private void copyData(QfileName fromTable) throws Exception
	{
		// Extract schema, file name
		I2Connection rconn = getRconn();
		QfileName tofile = rconn.qfileName(m_tofile);

		// Calculate to-member name
		String tombr="";
		// Strip off member name from fromTable
		if (m_tombr.compareTo("*FROMMBR")==0)
		{
			int i = fromTable.fileName.indexOf('(');
			if (i>=0)
				tombr = fromTable.fileName.substring(i);
		}
		// Otherwise, use specific member name
		else if (m_tombr.compareTo("*FIRST")!=0 && m_tombr.compareTo(tofile.fileName)!=0)
			tombr = '(' + m_tombr + ')';
		
		
		String toTable = tofile.schemaName + '"' + tofile.fileName + tombr + '"';

		// Create table, if necessary
		String ddl=null;
		boolean created=false;
		if (m_crtfile.compareTo("*YES")==0)
		{
			try
			{
				rconn.createTableLike(fromTable, tofile); 
				created=true; 
			}
			// If an error occurs, it is probably because the to-file already exists
			catch (SQLException e) 
			{
				if (m_mbropt.compareTo("*NONE")==0)
					throw e;
			}
		}

		try
		{
			// Clear data, if necessary
			if (!created && m_mbropt.compareTo("*REPLACE")==0)
			{
				ddl = "DELETE FROM " + toTable;
				m_stmt.execute(ddl);
			}
	
			// Copy data
			ddl = "INSERT INTO " + toTable + " SELECT * FROM " + fromTable.schemaName + fromTable.fileName;
			m_stmt.execute(ddl);
		} 
		catch (SQLException e)
		{
			I2Logger.logger.info(ddl);
			throw e;
		} 
	}

}
