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
 * 
 */
public class Chglibl extends AbstractCommand {

	private String m_libl="*SAME";
	private String m_curlib="*SAME";
	
	public Chglibl() {}
	public Chglibl(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"LIBL"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}
	
	public void setLibl(String libl) {
		m_libl = Application.trimr(libl);
	}
	
	/** Change the library list to the specified value. */
	public void exec(String libl) throws Exception {
		setLibl(libl);
		exec();
	}
	public void exec(FixedChar libl) throws Exception {
		exec(libl.toString());
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		// Get the usrlibl vector
		if (!m_libl.equals("*SAME"))
		{
			Vector usrlibl = getAppJob().getUsrlibl();
			// Clear library list...
			usrlibl.clear();
			// ...and add new libraries
			if (!m_libl.equals("*NONE"))
			{
				StringTokenizer st = new StringTokenizer(m_libl, " ");
				while (st.hasMoreTokens())
					usrlibl.add(st.nextToken());
			}
		}
		// TODO change current library?
		// CPC2101 -- Library list changed
		new Sndpgmmsg(getApp()).completion("CPC2101", "QCPFMSG");
	}
	
}
