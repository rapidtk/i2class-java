/*
 * Created on Nov 18, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * CHKOBJ (check object) processing.
 * 
 */
public class Chkobj extends AbstractCommand {

	private String m_obj, m_objtype;
	private String m_mbr="*NONE";
	private String m_aut="*NONE";
	
	public Chkobj() {}
	public Chkobj(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"OBJ", "OBJTYPE", "MBR", "AUT"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}

	public void setObj(String obj) {
		m_obj = Application.trimr(obj);
	}
	
	public void setObjtype(String objtype) {
		m_objtype = Application.trimr(objtype);
	}

	public void setMbr(String mbr) {
		m_mbr = Application.trimr(mbr);
	}
	public void setMbr(FixedChar mbr) {
		setMbr(mbr.toString());
	}

	public void setAut(String aut) {
		m_aut = Application.trimr(aut);
	}
	public void setAut(FixedChar aut) {
		setAut(aut.toString());
	}
	
	/** Check for the existance of the specified object. */
	public void exec(Object obj, Object objtype) throws Exception {
		setObj(obj.toString());
		setObjtype(objtype.toString());
		exec();
	}
	
	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		// Extract schema, file name
		boolean isFile = (m_objtype.compareTo("*FILE")==0);
		String fileSuffix;
		if (isFile)
			fileSuffix="";
		else
			fileSuffix='.' + m_objtype.substring(1).toLowerCase();
		QfileName q = getRconn().qfileName(m_obj, fileSuffix);
		String objName = q.fileName;
		// CPF9801 -- Object &2 in library &3 not found
		String msgid="CPF9801";

		// A data queue/message queue is a table that contains a serialized object
		boolean isQ = (!isFile && (m_objtype.compareTo("*DTAQ")==0 || m_objtype.compareTo("*MSGQ")==0));
		// TODO Do CHKOBJ for Display files		
		// Check to see if the table exists on the database server
		if (isFile || isQ) 
		{
			// If a member is specified, add it to file name
			boolean checkMember=false;
			if (isFile)
			{
				checkMember=(m_mbr.compareTo("*NONE")!=0 && m_mbr.compareTo("*FIRST")!=0 && m_mbr.compareTo(q.fileName)!=0);
				if (checkMember)
					q.fileName = q.fileName + '(' + m_mbr + ')';
			}
			// Append '.msgq' or '.dtaq' suffix to file name
			else
				q.fileName = q.fileName + fileSuffix;
	
			// Build table list
			DatabaseMetaData dbmd = getConnection().getMetaData();
			ResultSet rsTables = dbmd.getTables(null, q.searchSchema, q.fileName, TABLE_TYPES);
			boolean found = rsTables.next();
			// See if it is the file that doesn't exist or if it is the member
			rsTables.close();
			// Object found, return
			if (found)
				return;
			if (checkMember)
			{
				rsTables = dbmd.getTables(null, q.schemaName, objName, TABLE_TYPES);
				// If the file exists but not the member...
				if (rsTables.next())
				{
					// CPF9815 -- Member &5 file &2 in library &3 not found
					msgid="CPF9815";
				}
			}
		}
		// A program (*PGM) or service program (*SRVPGM) is really a Java class
		// We can only deal with *PGM and *SRVPGM objects that are the same name as their default module object
		else if (m_objtype.compareTo("*PGM")==0 || m_objtype.compareTo("*SRVPGM")==0)
		{
			try
			{
				Call.resovlePgmClass(getApp(), q.fileName);
				// If we get here then the class was successfully found
				return;
			}
			catch (Exception e) {}
		}
		// See if the resource bundle corresponding to the message file name exists
		else if (m_objtype.compareTo("*MSGF")==0)
		{
			if (Pgmmsg.resolveResourceBundle(q.fileName)!=null)
				return;
		}
		
		// We only get here if the object (CPF9801) or member (CPF9815) was not found
		FixedChar msgdta = new FixedChar(30);
		msgdta.setFixedAt(0, new FixedChar(10, objName));
		msgdta.setFixedAt(10, new FixedChar(10, q.libName));
		if (msgid.compareTo("CPF9815")==0)
			msgdta.setFixedAt(20, new FixedChar(10, m_mbr));
		else if (msgid.compareTo("CPF9801")==0)
			msgdta.setFixedAt(20, new FixedChar(7, m_objtype.substring(1)));
		throw new Pgmmsg(msgid, "QCPFMSG", msgdta);
	}
	

}
