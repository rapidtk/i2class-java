package com.i2class;

import java.sql.*;
import java.util.StringTokenizer;
/**
 * A data area class that stores data in a remote (JDBC) database file.
 * @author Andrew Clark
 */
class DataAreaJDBC implements IClosable
{
	private I2Connection m_rconn;
	private ResultSet rs;
	private String m_qfileName;
	//private PreparedStatement m_pstmt;
	
	/**
	 * Construct a data area object that represents a value in a file on a remote (JDBC) database.
	 * @param name the name of the dataarea
	 */
	DataAreaJDBC(I2Connection rconn, String name) throws Exception
	{
		this(rconn, rconn.qfileName(name, ".dtaara"));
	}
	/**
	 * Construct a data area object that represents a value in a file on a remote (JDBC) database.
	 * @param name the name of the dataarea
	 */
	DataAreaJDBC(I2Connection rconn, QfileName q) throws Exception
	{
		m_rconn = rconn;
		m_qfileName = q.schemaName + '"' + q.fileName + ".dtaara\"";
		//rs = rconn.stmt.executeQuery("SELECT * FROM " + m_qfileName);
		// Oracle doesn't allow updatable SELECT * so add XXX
		rs = rconn.createStatement(ResultSet.TYPE_FORWARD_ONLY, 
		 ResultSet.CONCUR_UPDATABLE).executeQuery("SELECT \"" + q.fileName + "\" FROM " + m_qfileName + " xxx ");
		rs.next();
		
		// Add this dataarea to list of open 'files'
		Application app = m_rconn.getApp();
		if (app!=null)
			app.addOpenFile(this);
	}
	
	/** Return the object value of the data area. */
	public Object getObject() throws SQLException
	{
		return rs.getObject(1);
	}
	
	/** Set the object value of the data area. */
	public void updateObject(Object value) throws Exception
	{
		/* The whole updatable result set thing seems to be a problem... */
		rs.updateObject(1, value);
		rs.updateRow();
		/*
		if (m_pstmt ==null)
			m_pstmt = m_rconn.getConn().prepareStatement("UPDATE " + m_qfileName + " SET data=?");
		m_pstmt.setObject(1, value);
		m_pstmt.execute();
		*/
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.IClosable#close()
	 */
	public void close() throws Exception
	{
		rs.close();
	}
}
