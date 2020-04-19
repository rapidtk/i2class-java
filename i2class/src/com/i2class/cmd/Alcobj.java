/*
 * Created on Nov 18, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * ALCOBJ (Allocate object) processing.
 * 
 */
public class Alcobj extends AbstractCommand {

	private String m_obj;
	
	public Alcobj() {}
	public Alcobj(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"OBJ"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}
	
	public void setObj(String obj) {
		m_obj = Application.trimr(obj);
	}

	/** Allocate the specified object. */
	public void exec(String obj) throws Exception {
		setObj(obj);
		exec();
	}
	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		// Parse out object specification
		StringTokenizer st = new StringTokenizer(m_obj, " ()");
		// The first token should be the relationship part of TOPGMQ (*PRV, *EXT, *SAME)
		if (st.hasMoreTokens())
		{
			String object = st.nextToken();
			// The next token should be the object type
			if (st.hasMoreTokens())
			{
				String objtype = st.nextToken();
				// The next token is the lock type
				if (st.hasMoreTokens())
				{
					String lockState = st.nextToken();
					// SQL has only two lock states, SHARE or EXCLUSIVE
					String sqlLockState;
					if (lockState.startsWith("*SHR")) //*SHRUPD, *SHRNUP, *SHRRD
						sqlLockState="SHARE";
					else
						sqlLockState="EXCLUSIVE"; // *EXCL, *EXCLRD
					// Build file name
					String schemaName, fileName;
					int i = object.indexOf('/');
					if (i>0)
					{
						schemaName = object.substring(0, i) + '.';
						fileName=object.substring(i+1);
					}
					else
					{
						schemaName="";
						fileName=object;
					}
					String objName = fileName;
					// If the next token exists, it is the member name
					if (st.hasMoreTokens())
					{
						String memberName = st.nextToken();
						if (memberName.compareTo("*FIRST")!=0 && memberName.compareTo(fileName)!=0)
							fileName = fileName + '(' + st.nextToken() + ')';
					}
					// TODO map m_obj to a specific (new?) connection so that it can be dropped
					// Lock object
					String ddl = "LOCK TABLE " + schemaName + fileName + " IN " + sqlLockState + " MODE";
					try {
						getStatement().execute(ddl);
						//TODO save statement handle
					} catch (SQLException e) {
						FixedChar msgdta = new FixedChar(10, objName);
						// CPF1002 -- Cannot allocate object &1
						throw new Pgmmsg("CPF1002", "QCPFMSG", msgdta);
					} 
				}
			}
		}
	}
}
