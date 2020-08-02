/*
 * Created on Nov 19, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * RMVLIBLE (Remove library list entry) processing.
 * 
 */
public class Rmvlible extends AbstractCommand {

	private String m_lib;
	
	public Rmvlible() {}
	public Rmvlible(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"LIB"};
	/* (non-Javadoc)
	 * @see com.i2class.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}
	
	public void setLib(String lib) {
		m_lib = Application.trimr(lib);
	}
	
	/** Remove the specified library from the library list. */
	public void exec(String lib) throws Exception {
		setLib(lib);
		exec();
	}
	public void exec(FixedChar lib) throws Exception {
		exec(lib.toString());
	}

	/* (non-Javadoc)
	 * @see com.i2class.ICommand#exec()
	 */
	public void exec() throws Exception {
		// Get the usrlibl vector
		Vector usrlibl = getAppJob().getUsrlibl();
		// See if the library to add is already in the library list.
		int usrliblSize = usrlibl.size();
		int index=-1;
		for (int i=0; i<usrliblSize; i++)
		{
			if (((String)usrlibl.elementAt(i)).compareTo(m_lib)==0)
			{
				index=i;
				break;
			}
		}
		FixedChar msgdta = new FixedChar(10, m_lib);
		if (index>=0)
		{
			usrlibl.remove(index);
			// CPC2197 -- Library &1 removed from library list
			new Sndpgmmsg(getApp()).completion("CPC2197", "QCPFMSG", msgdta);
		}
		// CPF2104 -- Library &1 not removed from the library list
		else
			throw new Pgmmsg("CPF2104", "QCPFMSG", msgdta);
	}
}
