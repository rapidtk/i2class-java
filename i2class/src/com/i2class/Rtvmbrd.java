/*
 * Created on Nov 18, 2004
 *
 */
package com.i2class;

import java.sql.*;
import java.util.*;

import com.i2class.cmd.*;

/**
 * RTVOBJD (retrieve object description) processing.
 * 
 */
public class Rtvmbrd extends AbstractCommand {

	private String m_file, m_mbr="*FIRST";

	private FixedChar m_rtnlibVar, m_rtnmbrVar, m_fileatrVar, m_textVar;	
	private String m_rtnlib, m_rtnmbr, m_fileatr, m_text;	
	
	/**
	 * 
	 */
	public Rtvmbrd(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"FILE"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
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
	
	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		// Extract schema, file name
		String schemaName, fileName;
		int i = m_file.indexOf('/');
		if (i>0)
		{
			schemaName = m_file.substring(0, i) + '.';
			fileName=m_file.substring(i+1);
		}
		else
		{
			schemaName="";
			fileName=m_file;
		}
		// Add member name to selection criteria
		StringTokenizer st = new StringTokenizer(m_mbr, " ()");
		// The first token is the member name
		String mbr = st.nextToken();
		// The next token is the relationship.  If something other than *SAME is specified, then we have to build a list
		// of all members and loop through them
		String relationship="*SAME";
		if (st.hasMoreTokens())
			relationship = st.nextToken();
		String filePattern;
		boolean generic=!relationship.equals("*SAME");
		if (generic)
			filePattern = fileName + '%';
		else
		{
			// If a member other than *FIRST is specified, add to file name
			if (!mbr.equals("*FIRST") && !mbr.equals(fileName))
				fileName=fileName + '(' + mbr + ')';
			filePattern=fileName;
		}
		
		// Build table list
		DatabaseMetaData dbmd = getConnection().getMetaData();
		ResultSet rsTables = dbmd.getTables(null, schemaName, filePattern, TABLE_TYPES);
		// If *NEXT or *PRV is specified, find the specified member, and then reposition in the list
		if (generic)
		{
			while (rsTables.next())
			{
				if (fileName.equals(rsTables.getString(3)))
				{
					if (relationship.equals("*NEXT"))
						rsTables.next();
					else // *PRV
						rsTables.previous();
				}
			}
		}
		
		// Save results so that they can be used later
		m_rtnlib = rsTables.getString(2);
		if (m_rtnlibVar != null)
			m_rtnlibVar.assign(m_rtnlib);

		m_rtnmbr = rsTables.getString(3);
		if (m_rtnmbrVar != null)
			m_rtnmbrVar.assign(m_rtnmbr);
			
		if (rsTables.getString(4).equals("VIEW"))
			m_fileatr="*LF";
		else
			m_fileatr="*PF";
		if (m_fileatrVar != null)
			m_fileatrVar.assign(m_fileatr);
			
		m_text = rsTables.getString(5);
		if (m_textVar != null)
			m_textVar.assign(m_text);
		rsTables.close();
	}
	
	/** Return the library (schema) associated with the object */
	public String rtnlib()
	{
		return m_rtnlib;
	}
	
	/** Return the text (remark) associated with the object */
	public String text()
	{
		return m_text;
	}
	
	/** Return the object attribute associated with the object */
	public String fileatr()
	{
		return m_fileatr;
	}

}
