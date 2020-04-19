/*
 * Created on Nov 16, 2004
 *
 */
package com.i2class.cmd;

import java.sql.*;
import java.util.*;

import com.i2class.*;

/**
 * CRTDUPOBJ (create duplicate object) processing.
 * 
 */
public class Crtdupobj extends AbstractCommand {

	private String m_obj;
	private String m_fromlib;
	private String m_objtype;
	private String m_tolib="*FROMLIB";
	private String m_newobj="*OBJ";
	private String m_data="*NO";
	
	public Crtdupobj() {}
	public Crtdupobj(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"OBJ", "FROMLIB", "OBJTYPE", "TOLIB", "NEWOBJ"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}

	public void setObj(String obj) {
		m_obj = Application.trimr(obj);
	}

	public void setFromlib(String fromlib) {
		m_fromlib = Application.trimr(fromlib);
	}

	public void setObjtype(String objtype) {
		m_objtype = Application.trimr(objtype);
	}

	public void setTolib(String tolib) {
		m_tolib = Application.trimr(tolib);
	}
	public void setTolib(FixedChar tolib) {
		setTolib(tolib.toString());
	}

	public void setNewobj(String newobj) {
		m_newobj = Application.trimr(newobj);
	}
	public void setNewobj(FixedChar newobj) {
		setNewobj(newobj.toString());
	}

	public void setData(String data)
	{
		m_data = Application.trimr(data);
	}
	public void setData(FixedChar data)
	{
		setData(data.toString());
	}
	
	/** Duplicate an object. */
	public void exec(Object obj, Object fromlib, Object objtype) throws Exception {
		setObj(obj.toString());
		setFromlib(fromlib.toString());
		setObjtype(objtype.toString());
		exec();
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		if (!m_objtype.equals("*FILE"))
			throw new Exception("Unsupported object type " + m_objtype);
		// Just do CPYF for CRTDUPOBJ
		String tolib;
		if (m_tolib.equals("*FROMLIB"))
			tolib=m_fromlib;
		else
			tolib=m_tolib;
		
		String newobj;
		if (m_newobj.equals("*OBJ"))
			newobj=m_obj;
		else
			newobj=m_newobj;
		Cpyf cpyf = new Cpyf(getRconn());
		cpyf.setCrtfile("*YES"); 
		try
		{
			cpyf.exec(m_fromlib+'/'+m_obj, tolib+'/'+newobj);
			// TODO send CPC2130 'nnn objects duplicated'? 
		}
		// If the file was not copied, signal error
		catch (Pgmmsg e)
		{
			//TODO break out file copy step from CPYF so that intervening CPF message doesn't get set, and get object count
			throw new Pgmmsg("CPF2130", "QCPFMSG");
		}
	}
}
