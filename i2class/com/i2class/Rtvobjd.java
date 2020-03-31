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
 * @author ANDREWC
 */
public class Rtvobjd extends AbstractCommand {

	private String m_obj, m_objtype;

	private String m_rtnlib, m_objatr, m_text;
	
	private fixed m_objatrVar, m_rtnlibVar, m_textVar;	
	
	/**
	 * 
	 */
	public Rtvobjd(I2Connection rconn) {
		super(rconn);
	}

	static final String[] PARM_NAMES={"OBJ", "OBJTYPE"};
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
	
	// These are actually return values
	public void setObjatr(fixed objatrVar)
	{
		m_objatrVar=objatrVar;
	}
	public void setRtnlib(fixed rtnlib)
	{
		m_rtnlibVar=rtnlib;
	}
	public void setText(fixed textVar)
	{
		m_textVar=textVar;
	}

	
	/** Retrieve an object description. */
	public void exec(Object obj, Object objtype) throws Exception
	{
		setObj(obj.toString());
		setObjtype(objtype.toString());
		exec();
	}
	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		// Extract schema, file name
		String schemaName, fileName;
		int i = m_obj.indexOf('/');
		if (i>0)
		{
			schemaName = m_obj.substring(0, i) + '.';
			fileName=m_obj.substring(i+1);
		}
		else
		{
			schemaName="";
			fileName=m_obj;
		}
		// Build table list
		DatabaseMetaData dbmd = getConnection().getMetaData();
		ResultSet rsTables = dbmd.getTables(null, schemaName, fileName, TABLE_TYPES);
		
		// Object library (schema)
		m_rtnlib = rsTables.getString(2);
		if (m_rtnlibVar!=null)
			m_rtnlibVar.assign(m_rtnlib);
			
		// Object attribute - Table=PF, View=LF
		if (rsTables.getString(4).equals("VIEW"))
			m_objatr="LF";
		else
			m_objatr="PF";
		if (m_objatrVar != null)
			m_objatrVar.assign(m_objatr);
			
		// Object text (comment)
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
	public String objatr()
	{
		return m_objatr;
	}
}
