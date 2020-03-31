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
public class Ovrdbf extends AbstractCommand {

	private String m_file;
	private String m_tofile="*FILE";
	private String m_mbr;
	
	public Ovrdbf() {}
	public Ovrdbf(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"FILE", "TOFILE", "MBR"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}
	
	public void setFile(String file) {
		m_file = qual(file);
	}

	public void setTofile(String tofile) {
		m_tofile = qual(tofile);
	}
	public void setTofile(fixed tofile) {
		setTofile(tofile.toString());
	}
	
	public void setMbr(String mbr) {
		m_mbr = Application.trimr(mbr);
	}
	public void setMbr(fixed mbr) {
		setMbr(mbr.toString());
	}
	
	/** Override one file to another. */
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
		Application app = getApp();
		if (app.ovrFiles==null)
			app.ovrFiles=new Hashtable();
		String toFile;
		if (m_tofile.compareTo("*FILE")==0)
			toFile=m_file;
		else
			toFile=m_tofile;
		// Add member name e.g. FILE(MEMBER)
		if (m_mbr!=null && m_mbr.compareTo("*FIRST")!=0 && m_mbr.compareTo(m_file)!=0)
			toFile = toFile + '(' + m_mbr + ')';
		// Map FILE name to fully qualified name
		app.ovrFiles.put(m_file, toFile);
	}
}
