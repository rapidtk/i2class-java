/*
 * Created on Oct 15, 2004
 */
package com.i2class;

import java.lang.ref.*;
import java.sql.*;
import java.sql.Connection;


/**
 * The interface that all command (*CMD) objects must implement
 * 
 * @author ANDREWC
 *
 */
public abstract class AbstractCommand {
	
	private WeakReference m_appRef;
	private I2Connection m_rconn;
	protected static final String[] TABLE_TYPES = {"TABLE", "VIEW"};
	protected static String[] m_parmNames;
	
	public AbstractCommand() { }
	public AbstractCommand(I2Connection rconn)
	{
		m_rconn=rconn;
		setApp(rconn.app);
	}
	/*
	AbstractCommand(Application app)
	{
		setApp(app);
	}
	*/
	
	
	/** Execute the command. */
	public abstract void exec() throws Exception;

	protected Connection getConnection() throws Exception {
		return getRconn().getConn();
	}
	
	protected I2Connection getRconn()
	{
		// If no connection has been specified, then get it (the default, first connection) from the Application object
		if (m_rconn==null)
		{
			Application app = getApp();
			m_rconn = app.defaultI2Connection();
		}
		return m_rconn;
	}
	
	protected Statement getStatement() throws Exception
	{
		getConnection();
		return getRconn().ddlStmt;
	}

	protected Application getApp() {
		return (Application)m_appRef.get();
	}
	protected void setApp(Application app)
	{
		m_appRef=new WeakReference(app);
	}
	protected I2Job getAppJob()
	{
		return getApp().appJob;
	}
	
	abstract protected String[] getParmNames();
	
	/** Return the qualified (lib.qobject) representation of this object. */
	static protected String qual(String qobject)
	{
		qobject = Application.trimr(qobject);
		int i = qobject.indexOf('/');
		if (i>0)
		{
			String library=qobject.substring(0, i);
			qobject = qobject.substring(i+1);
			if (library.compareTo("*LIBL")!=0)
				return library + '.' + qobject;
		}
		return qobject;
	}

}
