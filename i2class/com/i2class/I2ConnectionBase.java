package com.i2class;
import java.sql.*;
import java.sql.Connection;
import java.util.Map;

import com.i2class.cmd.*;
/** An I2 Connection object that contains the instance of the Application that it was instantiated from. */
public class I2ConnectionBase implements IRHost, Cloneable
{
	// Constants indicating the type of database connected to
	static final int SERVER_SQLSERVER=1;
	static final int SERVER_DB2=2;
	int m_serverType=-1;

	Connection conn;
	// This is a shared statement handle that can be used for DDL only!  
	// Trying to use multiple result sets with the same statement causes disastrous results. 
	Statement ddlStmt;
	public Application app;
	String m_url, m_usrid, m_password;
	

	public I2ConnectionBase(Application app)
	{
		this(app, null, null, null);
	}
	public I2ConnectionBase(Application app, String url, String usrid, String password)
	{
		this.app= app;
		m_url= url;
		m_usrid= usrid;
		m_password= password;
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#createStatement()
	 */
	public Statement createStatement() throws SQLException
	{
		return conn.createStatement();
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareStatement(java.lang.String)
	 */
	public PreparedStatement prepareStatement(String arg0) throws SQLException
	{
		return getConn().prepareStatement(arg0);
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#prepareCall(java.lang.String)
	 */
	public CallableStatement prepareCall(String arg0) throws SQLException
	{
		return conn.prepareCall(arg0);
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#nativeSQL(java.lang.String)
	 */
	public String nativeSQL(String arg0) throws SQLException
	{
		return conn.nativeSQL(arg0);
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#setAutoCommit(boolean)
	 */
	public void setAutoCommit(boolean arg0) throws SQLException
	{
		conn.setAutoCommit(arg0);
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#getAutoCommit()
	 */
	public boolean getAutoCommit() throws SQLException
	{
		return conn.getAutoCommit();
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#commit()
	 */
	public void commit() throws SQLException
	{
		conn.commit();
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#rollback()
	 */
	public void rollback() throws SQLException
	{
		conn.rollback();
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#close()
	 */
	public void close() throws SQLException
	{
		if (conn != null)
			conn.close();
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#isClosed()
	 */
	public boolean isClosed() throws SQLException
	{
		return conn.isClosed();
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#getMetaData()
	 */
	public DatabaseMetaData getMetaData() throws SQLException
	{
		return conn.getMetaData();
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#setReadOnly(boolean)
	 */
	public void setReadOnly(boolean arg0) throws SQLException
	{
		conn.setReadOnly(arg0);
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#isReadOnly()
	 */
	public boolean isReadOnly() throws SQLException
	{
		return conn.isReadOnly();
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#setCatalog(java.lang.String)
	 */
	public void setCatalog(String arg0) throws SQLException
	{
		conn.setCatalog(arg0);
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#getCatalog()
	 */
	public String getCatalog() throws SQLException
	{
		return conn.getCatalog();
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#setTransactionIsolation(int)
	 */
	public void setTransactionIsolation(int arg0) throws SQLException
	{
		conn.setTransactionIsolation(arg0);
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#getTransactionIsolation()
	 */
	public int getTransactionIsolation() throws SQLException
	{
		return conn.getTransactionIsolation();
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#getWarnings()
	 */
	public SQLWarning getWarnings() throws SQLException
	{
		return conn.getWarnings();
	}
	/* (non-Javadoc)
	 * @see java.sql.Connection#clearWarnings()
	 */
	public void clearWarnings() throws SQLException
	{
		conn.clearWarnings();
	}
	
	/** Set the Java Connection object associated with this I2 connection. */
	void setConn(Connection conn) throws SQLException
	{
		if (conn != this.conn)
		{
			this.conn = conn;
			// If the server type hasn't been determined yet, then calculate it
			if (m_serverType==-1)
			{
				String productName = conn.getMetaData().getDatabaseProductName();
				if (productName.startsWith("DB2"))
					m_serverType=SERVER_DB2;
				else if (productName.compareTo("SQLSERVER")==0)
					m_serverType=SERVER_SQLSERVER;
				else
					m_serverType=0;
			}
			ddlStmt=conn.createStatement();
		}
	}
	
	
	public Connection getConn() throws SQLException
	{
		return getConn(Application.COMMIT_LOCK_LEVEL_NONE);
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.IHost#getHost()
	 */
	public Object getHost() 
	{
		return conn;
	}
	/* (non-Javadoc)
	 * @see com.asc.rio.IHost#setHost(java.lang.Object)
	public void setHost(Object o)
	{
		conn = (Connection)o;
	}
	*/
	
	
	/**
	 * Return the Java SQL connection associated with this I2connection at the specified commitment control level.
	 * @param commit One of the Application COMMIT_* commitment control levels
	 * @see Application#COMMIT_LOCK_LEVEL_ALL
	 * @see Application#COMMIT_LOCK_LEVEL_CHANGE
	 * @see Application#COMMIT_LOCK_LEVEL_CURSOR_STABILITY
	 * @see Application#COMMIT_LOCK_LEVEL_DEFAULT
	 * @see Application#COMMIT_LOCK_LEVEL_NONE
	 * @see Application#COMMIT_LOCK_LEVEL_SERIALIZABLE
	 */
	Connection getConn(int commit) throws SQLException
	{
		Connection c=null;
		if (app != null)
		{
			//Set any commitment control options
			if (commit != Application.COMMIT_LOCK_LEVEL_NONE)
			{
				c= app.appJob.connCommit;
				// If COMMIT_LOCK_LEVEL_DEFAULT is specified, then use job-level commitment defintion created by STRCMTCTL
				// TODO This is an error if the user tries to open a file under commitment control with a different isolation level then that specified by STRCMTCTL
				/*
				if (commit != Application.COMMIT_LOCK_LEVEL_DEFAULT)
				{
					int javaCommit=Connection.TRANSACTION_NONE;
					switch (commit)
					{
					// Lowest isolation level: allows commit/rollback
					case Application.COMMIT_LOCK_LEVEL_CHANGE :
						 javaCommit=Connection.TRANSACTION_READ_UNCOMMITTED;
						 break;
					 // Prevents application from reading uncommitted data (dirty reads)
					case Application.COMMIT_LOCK_LEVEL_CURSOR_STABILITY :
						 javaCommit=Connection.TRANSACTION_READ_COMMITTED;
						 break;
					 // Highest RPG isolation level:  
					case Application.COMMIT_LOCK_LEVEL_ALL :
						 javaCommit=Connection.TRANSACTION_REPEATABLE_READ;
						 break;
					 // Highest SQL isolation level:  
					case Application.COMMIT_LOCK_LEVEL_SERIALIZABLE :
						 javaCommit=Connection.TRANSACTION_SERIALIZABLE;
						 break;
					}
					if (javaCommit != cconn.getTransactionIsolation())
						throw new Pgmmsg();
				}
				*/
			}
			// Otherwise, use the QTEMP connection if it exists
			else
				c = app.appJob.conn;		
		}
		
		if (c == null)
		{
			// Use the current connection object, if it already exists
			if (conn != null)
			{
				if (I2Logger.logger.isDebuggable())
					I2Logger.logger.debug("Using current connection " + conn);
				c=conn;
			}
			// If the current connection hasn't been created, then go to connection pool
			else
			{
				if (app==null)
					throw new RuntimeException("Application object has been destroyed -- no connection can be established");
				c=(Connection)app.retrievePooledHost(m_url, m_usrid, m_password);
			}
		}
		else
		{
			if (I2Logger.logger.isDebuggable())
				I2Logger.logger.debug("Using job-level connection " + c + " commitment control level " + commit);
		}
			
		setConn(c);
		return c;
	}
	
	public QfileName qfileName(String fileName) throws Exception 
	{
		return qfileName(fileName, "");
	}
	/** Return the qualified schema name from a lib/file name.
	 * If QTEMP is specified, then create a job-level connection object to use for all future connetions
	 */
	public QfileName qfileName(String fileName, String fileSuffix) throws Exception 
	{
		QfileName q= new QfileName();
		q.schemaName= "";
		boolean isQtemp=false;
		boolean isLibl;
		boolean isLDA=false;
		int i= fileName.indexOf('/');
		if (i > 0)
		{
			q.fileName= fileName.substring(i + 1);
			q.libName= Application.trimr(fileName.substring(0, i));
			isQtemp = (q.libName.compareTo("QTEMP") == 0);
			isLibl = (!isQtemp && q.libName.compareTo("*LIBL") == 0);
		}
		else
		{
			q.fileName= fileName;
			q.libName= "*LIBL";
			isLDA = (fileSuffix.compareTo(".dtaara")==0 && fileName.compareTo("*LDA")==0);
			isQtemp = isLDA;
			isLibl=!isLDA;
		}

		// If a library name is not specified and the specified object exists in QTEMP, then see if the object 
		// exists in the default schema and then fall back to QTEMP
		getConn();
		if (isLibl)
		{
			String searchName = fileName + fileSuffix;
			if (app.appJob.inQtemp(searchName))
			{
				String defaultSchema;
				if (m_serverType==SERVER_SQLSERVER)
					defaultSchema="%";
				else
					defaultSchema=m_usrid;
				ResultSet rsTables = conn.getMetaData().getTables(null, defaultSchema, searchName, AbstractCommand.TABLE_TYPES);
				// If the file does not exist in the default schema, then return QTEMP for the library qualification
				if (!rsTables.next())
				{
					q.libName="QTEMP";
					isQtemp=true;
				}
			}
		}

		if (isQtemp)
		{
			// Create a QTEMP connection object if one doesn't already exist
			if (app.appJob.conn == null)
			{
				app.appJob.conn= DriverManager.getConnection(m_url, m_usrid, m_password);
				setConn(app.appJob.conn);
			}
			// For DB2, for whatever reason, the schema name is always 'SESSION' for QTEMP objects
			if (m_serverType==SERVER_DB2)
				q.schemaName="SESSION.";
			// For SQL server, temporary file names always begin with '#'
			else if (m_serverType==SERVER_SQLSERVER)
				q.schemaName = "#";
			// If *LDA is specified and a *LDA object hasn't been created yet, then create it
			if (isLDA && !app.appJob.LDA)
			{
				app.appJob.LDA= true;
				Crtdtaara crtdtaara= new Crtdtaara((I2Connection)this);
				crtdtaara.setLen(2000);
				crtdtaara.exec("QTEMP/*LDA", "*CHAR");
			}
		}
		else if (!isLibl)
			q.schemaName = q.libName + '.';

		return q;
	}
	
	/* Return the CREATE xxx statement (without column definitions) for the qualified file name. */ 
	public String createTableString(QfileName q) throws SQLException
	{
		String create;
		// Deal with temporary files created into QTEMP
		if (q.libName.compareTo("QTEMP")==0)
		{ 
			// ANSI says CREATE GLOBAL TEMPORARY, but DB2 uses DECLARE GLOBAL TEMPORARY
			// and SQLServer uses # in front of file name
			if (m_serverType==SERVER_DB2)
				create = "DECLARE GLOBAL TEMPORARY TABLE ";
			else if (m_serverType==SERVER_SQLSERVER)
				create = "CREATE TABLE #";
			else
				create = "CREATE GLOBAL TEMPORARY TABLE ";
		}
		else
			create = "CREATE TABLE ";
		// Create table DDL
		String ddl = create + q.schemaName + '"' + q.fileName + '"';
		return ddl;
	}

	/* Delete (drop) the specified file  */ 
	public void deleteTable(QfileName q) throws SQLException
	{
		String ddl = "DROP TABLE " + q.schemaName + q.fileName;
		ddlStmt.execute(ddl);
		
		// Remove objects from list of files in QTEMP
		if (q.libName==null || q.libName.compareTo("QTEMP")==0 || (m_serverType==SERVER_SQLSERVER && q.fileName.charAt(0)=='#')
		 || (m_serverType==SERVER_DB2 && q.schemaName.compareTo("SESSION")==0))
			app.appJob.removeFromQtemp(q.fileName);
	}

	public void createTableExec(String ddl, QfileName q) throws SQLException
	{
		ddlStmt.execute(ddl);
		// Add object to list of files in QTEMP
		app.appJob.addToQtemp(q);
	}

	/* Create the specified file name using the specified DDL */ 
	public void createTable(QfileName q, String columnDefinitions) throws SQLException
	{
		String ddl = createTableString(q) + columnDefinitions;
		createTableExec(ddl, q);
	}
	
	/* Create a file 'LIKE' (with the same attributes as) another file.  */ 
	//TODO this won't work for LFs...
	public void createTableLike(QfileName q, QfileName likeFile) throws SQLException, Exception
	{
		Connection conn = getConn(Application.COMMIT_LOCK_LEVEL_NONE);
		String createTableString = createTableString(q);
		String ddl;
		// Everything but SQL server support CREATE TABLE LIKE 
		// and SQLServer uses # in front of file name
		String xlikeFile = likeFile.schemaName + likeFile.fileName;
		if (m_serverType==SERVER_SQLSERVER)
		{
			// SQLServer uses # in front of temporary file names
			if (likeFile.libName.compareTo("QTEMP")==0)
				likeFile.fileName = '#' + likeFile.fileName; 
			ddl = "SELECT * INTO " + createTableString.substring(13) + " FROM " + xlikeFile + 
			 "WHERE 1=0"; 
		}
		else
			ddl = createTableString + " LIKE " + xlikeFile; 
		createTableExec(ddl, q);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable
	{
		this.app=null;
		this.conn=null;
		super.finalize();
	}
	/* (non-Javadoc)
	 * @see com.asc.rio.IHost#deactivate()
	 */
	public void invalidate() throws Throwable
	{
		finalize();
	}
	
	/* (non-Javadoc)
	 * @see com.asc.rio.IRHost#getApp()
	 */
	public Application getApp()
	{
		return app;
	}
	
	/** Return the user id associated with this connection */
	public String getUsrid()
	{
		return m_usrid;
	}
}
