package com.i2class;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import com.i2class.cmd.*;
/** An I2 Connection object that contains the instance of the Application that it was instantiated from. */
public class I2Connection extends I2ConnectionBase implements Connection
{
	

	public I2Connection(Application app)
	{
		super(app);
	}
	public I2Connection(Application app, String url, String usrid, String password)
	{
		super(app, url, usrid, password);
	}
	
	/* (non-Javadoc)
	 * @see java.sql.Connection#createStatement(int, int)
	 */
	public Statement createStatement(int arg0, int arg1) throws SQLException
	{
		return conn.createStatement(arg0, arg1);
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
	 */
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2)
		throws SQLException
	{
		return conn.prepareStatement(arg0, arg1, arg2);
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
	 */
	public CallableStatement prepareCall(String arg0, int arg1, int arg2)
		throws SQLException
	{
		return conn.prepareCall(arg0, arg1, arg2);
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#getTypeMap()
	 */
	public Map getTypeMap() throws SQLException
	{
		return conn.getTypeMap();
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#setTypeMap(java.util.Map)
	 */
	public void setTypeMap(Map arg0) throws SQLException
	{
		conn.setTypeMap(arg0);
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#getHoldability()
	 */
	public int getHoldability() throws SQLException
	{
		// TODO Auto-generated method stub
		return 0;
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#setHoldability(int)
	 */
	public void setHoldability(int arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#setSavepoint()
	public Savepoint setSavepoint() throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}
	*/
	/* (non-Javadoc)
	 * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
	public void releaseSavepoint(Savepoint arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		
	}
	 */
	/* (non-Javadoc)
	 * @see java.sql.Connection#rollback(java.sql.Savepoint)
	public void rollback(Savepoint arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		
	}
	 */
	/* (non-Javadoc)
	 * @see java.sql.Connection#createStatement(int, int, int)
	 */
	public Statement createStatement(int arg0, int arg1, int arg2) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
	 */
	public CallableStatement prepareCall(String arg0, int arg1, int arg2, int arg3) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int)
	 */
	public PreparedStatement prepareStatement(String arg0, int arg1) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
	 */
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2, int arg3) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
	 */
	public PreparedStatement prepareStatement(String arg0, int[] arg1) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#setSavepoint(java.lang.String)
	public Savepoint setSavepoint(String arg0) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}
	 */
	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
	 */
	public PreparedStatement prepareStatement(String arg0, String[] arg1) throws SQLException
	{
		// TODO Auto-generated method stub
		return null;
	}
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	public Savepoint setSavepoint() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public Savepoint setSavepoint(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public void rollback(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public void setSchema(String schema) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	public String getSchema() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public void abort(Executor executor) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	public int getNetworkTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* Java 1.6 Interface */
	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}
}
