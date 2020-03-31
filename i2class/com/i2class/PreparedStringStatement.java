/* Created on Aug 25, 2004 */
package com.i2class;

import java.sql.*;

/**
 * A prepared statement that preserves the String used to initially create it.  
 * Why doesn't java.sql.PreparedStatement have this?!?
 * 
 * @author ANDREWC
 */
class PreparedStringStatement {
	PreparedStatement m_pstmt;
	String m_sql;

	/**
	 * Create a prepared statement that actually preserves the SQL string used to create it.
	 */
	public PreparedStringStatement(Connection conn, String sql) throws SQLException 
	{
		m_pstmt = conn.prepareStatement(sql);
		m_sql = sql;
	}
	
}
