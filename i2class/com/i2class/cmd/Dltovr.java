/*
 * Created on Nov 15, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * OVRDBF (Override database file) processing.
 * @author ANDREWC
 */
public class Dltovr extends AbstractCommand {

	private String m_file;
	
	public Dltovr() {}
	public Dltovr(I2Connection rconn) {
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
	
	/** Delete the specified override. */
	public void exec(String file) throws Exception {
		setFile(file);
		exec();
	}
	public void exec(fixed file) throws Exception {
		exec(file.toString());
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		
		String toFile=null;
		// *ALL means to remove all overrides
		boolean removeAll = (m_file.compareTo("*ALL")==0);
		// Check for overrides anywhere on call stack
		Application oapp=getApp();
		do
		{
			if (oapp.ovrFiles != null)
			{
				// Remove all overrides
				if (removeAll)
				{
					oapp.ovrFiles.clear();
					toFile=m_file;
				}
				else
				{
					toFile = (String)oapp.ovrFiles.remove(m_file);
					break;
				}
			}
			oapp = oapp.prvApp();
		} while(oapp!=null);
		
		// If the override was not found, then throw exception
		if (toFile==null)
		{ 
			fixed msgdta = new fixed(52);
			msgdta.setFixedAt(2, new fixed(10, m_file));
			// Override not found at specified level
			throw new Pgmmsg("CPF9841", "QCPFMSG", msgdta);
		}
	}
}
