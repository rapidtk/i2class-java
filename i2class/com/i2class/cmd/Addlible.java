/*
 * Created on Nov 19, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * ADDLIBLE (Add library list) processing.
 * @author ANDREWC
 */
public class Addlible extends AbstractCommand {

	private String m_lib;
	private String m_position="*FIRST";
	
	public Addlible() {}
	public Addlible(I2Connection rconn) {
		super(rconn);
	}
	
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	static final String[] PARM_NAMES={"LIB", "POSITION"};
	protected String[] getParmNames() {
		return PARM_NAMES;
	}
	
	public void setLib(String lib) {
		m_lib = Application.trimr(lib);
	}

	public void setPosition(String position) {
		m_position = Application.trimr(position);
	}
	public void setPosition(fixed position) {
		setPosition(position.toString());
	}
	
	/** Add the specified library to the library list. */
	public void exec(String lib) throws Exception {
		setLib(lib);
		exec();
	}
	public void exec(fixed lib) throws Exception {
		exec(lib.toString());
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		// Get the usrlibl vector
		Vector usrlibl = getAppJob().getUsrlibl();
		// Parse out position parameter
		StringTokenizer st = new StringTokenizer(m_position, " ");
		String position=st.nextToken();
		// If specified, the next token is the reference library 
		String reference=null;
		if (st.hasMoreTokens())
			reference = st.nextToken();
		
		fixed msgdta = new fixed(10, m_lib);
		// See if the library to add is already in the library list.
		int usrliblSize = usrlibl.size();
		int i=0;
		for (; i<usrliblSize; i++)
		{
			if (((String)usrlibl.elementAt(i)).compareTo(m_lib)==0)
			{
				// CPF2103 -- Library &1 already exists in library list
				throw new Pgmmsg("CPF2103", "QCPFMSG", msgdta);
			}
		}
		
		// Calculate position to enter library in library list
		// Add library to end of list
		int index=-1;
		if (position.compareTo("*LAST")==0)
			index = usrliblSize;
		// For any of the other types, we have to calculate the insert index
		else
		{
			if (position.compareTo("*FIRST")==0)
				index=0;
			// For *BEFORE, *AFTER, and *REPLACE find reference library
			else	
			{
				for (i=0; i<usrliblSize; i++)
				{
					if (((String)(usrlibl.elementAt(index))).compareTo(reference)==0)
					{
						if (position.compareTo("*BEFORE")==0)
							index=i;
						else if (position.compareTo("*AFTER")==0)
							index=i+1;
						// Replace the element at the current position
						else //if (position.compareTo("*REPLACE")==0)
							usrlibl.setElementAt(m_lib, index);
					}
				}
			}
		}
		// If the library wasn't replaced, insert into correct position in list
		if (index>=0)
		{
			if (index>=usrliblSize)
				// TODO ensure that library list <=250 elements
				usrlibl.add(m_lib);
			else
				usrlibl.insertElementAt(m_lib, index);			
		}
		// CPC2196 -- Library &1 added to library list
		new Sndpgmmsg(getApp()).completion("CPC2196", "QCPFMSG", msgdta);
	}

}
