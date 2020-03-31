/*
 * Created on Nov 8, 2004
 *
 */
package com.i2class;

import java.sql.*;
import java.util.*;
import java.util.Hashtable;

/**
 * The Export file class allows data from one JDBC-enabled database to be exported to another.
 * @author ANDREWC
 *
 */
public class ExportFile {
	
	private Connection toConn;
	private Connection conn;
	
	private static final int SERVER_TYPES_ISERIES=0;
	private static final int SERVER_TYPES_ORACLE=1;
	private static final int SERVER_TYPES_SQLSERVER=2;
	private static final int SERVER_TYPES_DB2=3;
	private static final int SERVER_TYPES_MYSQL=4;
	private static final String[] SERVER_TYPES={"*ISERIES", "*ORACLE", "*SQLSERVER", "*DB2", "*MYSQL"};
	private int mToServerType;
	private int MAX_NUMERIC_DIGITS=31;
	//private int MAX_FLOAT_DIGITS=53;
	private int MAX_CHAR=255;
	private long MAX_CLOB=2147483647;
	/** Flag that shows whether the data connection is to the *LOCAL system. */
	private boolean isLocal;
	/** Flag if ROWID is needed for I2 export to SQLServer */
	private boolean addROWID;
	
	private Throwable m_error;
	 	
	static class LabelPair
	{
		String column;
		String label;
		LabelPair(String column, String label)
		{
			this.column=column;
			this.label=label;
		}
	}

	private static final int types[]= {
				  Types.BIGINT, Types.BLOB,     Types.CHAR,    Types.CLOB,          Types.DATE,        Types.DECIMAL,
				  Types.DOUBLE, Types.FLOAT,    Types.INTEGER, Types.LONGVARBINARY, Types.LONGVARCHAR, Types.NUMERIC, 
				  Types.REAL,   Types.SMALLINT, Types.TIME,    Types.TIMESTAMP,     Types.TINYINT,     Types.VARCHAR,
				  Types.BIT,    Types.BINARY,   Types.VARBINARY};
	private static final String typeNames[]={
				 "BIGINT",     "BLOB",          "CHAR",        "CLOB",              "DATE",            "DECIMAL",
				 "DOUBLE",     "FLOAT",         "INTEGER",     "BLOB",              "CLOB",            "NUMERIC",
				 "REAL",       "SMALLINT",      "TIME",        "TIMESTAMP",         "SMALLINT",        "VARCHAR",
				 "BIT",        "BINARY",        "VARBINARY"};
				 
	// Constructor used by RPG for I2
	public ExportFile(byte[] toUsrid, byte[] toPassword, byte[] toDriver, byte[] toConnectionString, byte[] toServerType) throws Exception
	{
		this(new String(toUsrid).trim(), new String(toPassword).trim(), new String(toDriver).trim(), 
		 new String(toConnectionString).trim(), new String(toServerType).trim());
	}
	public ExportFile(String toUsrid, String toPassword, String toDriver, String toConnectionString, String toServerType) throws Exception
	{
		//String dconnectStr = "jdbc:as400://asc406";
		//String dconnectStr = "jdbc:as400://*LOCAL";
		//String dconnectStr = "jdbc:as400://ASC404";
		String dconnectStr = "jdbc:db2:localhost";
		
		// Translate binary data so that CCSID 65535 comes across as character
		//??? always if (serverType.compareTo("*DB2")!=0 && serverType.compareTo("*ISERIES")!=0)
			dconnectStr = dconnectStr + ";translate binary=true;";
		
		//dconnectStr = dconnectStr + ";trace=true;server trace=4;";
		//construct(usrid, password, driver, connectionString, serverType, "*CURRENT", "*CURRENT", "com.ibm.as400.access.AS400JDBCDriver", dconnectStr);
		
		// Flag SQLServer to add ROWID field
		boolean addROWID=(toServerType.compareTo("*SQLSERVER")==0);
		construct("*CURRENT", "*CURRENT", "com.ibm.db2.jdbc.app.DB2Driver", dconnectStr, toServerType, toUsrid, toPassword, toDriver, toConnectionString, addROWID);
	}

	/** Construct used by RPG for EXPORTFSQL */
	public ExportFile(byte[]  usrid, byte[]  password, byte[]  driver, byte[]  connectionString, byte[] toServerType,
							byte[] toUsrid, byte[] toPassword, byte[] toDriver, byte[] toConnectionString) throws Exception
	{
		this(new String(usrid).trim(), new String(password).trim(), new String(driver).trim(), 
		 new String(connectionString).trim(), new String(toServerType).trim(),
		 new String(toUsrid).trim(), new String(toPassword).trim(), new String(toDriver).trim(), 
		 new String(toConnectionString).trim());
	}
	public ExportFile(String usrid, String password, String driver, String connectionString, String toServerType,
	                  String toUsrid, String toPassword, String toDriver, String toConnectionString) throws Exception
	{
		construct(usrid, password, driver, connectionString, toServerType, toUsrid, toPassword, toDriver, toConnectionString, false);
	}
	
	// Construct this object using the specified parameters
	private void construct(String usrid,  String password,  String driver,  String connectionString, String toServerType, 
							     String toUsrid, String toPassword, String toDriver, String toConnectionString, boolean addROWID) throws Exception
	{
		try
		{
			//TOSERVER
			Class.forName(toDriver);
			isLocal = toUsrid.compareTo("*CURRENT")==0;
			toConn=DriverManager.getConnection(toConnectionString, toUsrid, toPassword);

			//SERVER
			Class.forName(driver);
			conn =DriverManager.getConnection(connectionString, usrid, password);
			
			// Save server type
			for (mToServerType=0; mToServerType<SERVER_TYPES.length; mToServerType++)
				if (SERVER_TYPES[mToServerType].compareTo(toServerType)==0)
				{
					switch (mToServerType)
					{
					// DB2 and iSeries (<=V5R2) only allow 31 for precision
					case SERVER_TYPES_DB2:
					case SERVER_TYPES_ISERIES:
						MAX_CHAR=32700;
						MAX_NUMERIC_DIGITS=31;
						break;
					case SERVER_TYPES_ORACLE:
						MAX_CHAR=2000;
						MAX_CLOB=4294967295L;
						break;
					case SERVER_TYPES_SQLSERVER:
						MAX_CHAR=8000;
						break;
					case SERVER_TYPES_MYSQL:
						MAX_CLOB=65535;
						break;
					}
					
					break;
				}
				
				this.addROWID = addROWID;
		}

		catch (Exception e) 
		{
			//I2logger.logger.severe("Error occurred creating connection to host");
			System.err.println("Error occurred creating connection to host");
			
			// TOSERVER
			//I2logger.logger.severe("TOSERVER Usrid: " + toUsrid + " Connection String: " + toConnectionString + " Driver: " + toDriver);
			System.err.println("TOSERVER Usrid: " + toUsrid + " Connection String: " + toConnectionString + " Driver: " + toDriver);
			
			// SERVER
			//I2logger.logger.severe("SERVER Usrid: " + usrid + " Connection String: " + connectionString + " Driver: " + driver);
			System.err.println("SERVER Usrid: " + usrid + " Connection String: " + connectionString + " Driver: " + driver);
			
			if (toConn!=null)
				toConn.close();
			throwException(e);
		}
			
	}
	
	/** Throw SQLException message. */
	private void throwSQLException(SQLException e) throws SQLException
	{
		printException(e, true);
		throw e;
	}
	private void throwException(Exception e) throws Exception
	{
		printException(e, false);
		throw e;
	}

	/** Print error message. */
	private void printException(Throwable e, boolean isSQLException)
	{
		printThrowable(e, isSQLException);
		m_error = e;
	}

	/** Send generic SQLException error message. */
	public static void printThrowable(Throwable e, boolean isSQLException)
	{
		if (isSQLException || e instanceof SQLException)
		{
			SQLException se = (SQLException)e;
			//I2logger.logger.severe("SQL state: " + se.getSQLState());
			///I2logger.logger.severe("Error code: " + se.getErrorCode());
			System.err.println("SQL state: " + se.getSQLState());
			System.err.println("Error code: " + se.getErrorCode());
		}
		// If the error is longer than what will fit on the spool file, make sure that the whole thing can be seen
		String msg=e.getMessage();
		if (msg.length()>110)
			printLongString(msg);
		//I2logger.logger.printStackTrace(e);
		e.printStackTrace();
	}
	
	/** Return the error string to the calling program. */
	public byte[] errorBytes()
	{
		byte[] errBytes=null;
		if (m_error!=null)
		{
			errBytes=errorString(m_error).getBytes();
			m_error=null;
		}
		return errBytes;			
	}

	// Return the error message associated with the last error that occurred
	static public String errorString(Throwable error)
	{
		String errorMessage=null;
		// If this is an SQLException, then try to 'clean up' error message for specific vendor types:
		if (error instanceof SQLException)
		{
			errorMessage = error.getMessage();
			if (errorMessage!=null)
			{
				int errLength = errorMessage.length();
				// SQL Server: [Microsoft][SQLServer 2000 Driver for JDBC][SQLServer]Actual error
				if (errorMessage.charAt(0)=='[')
				{
					/*
					int index = errorMessage.indexOf("[SQLServer]");
					if (index>=0)
					{
						index += 11; // 11 = sizeof("[SQLServer]")
						if (index < errLength)
							errorMessage = errorMessage.substring(index);
					}
					*/
					int index = errorMessage.lastIndexOf(']');
					if (index > 0)
					{
						index++;
						if (index < errLength)
							errorMessage = errorMessage.substring(index);
					}
				}
				// Oracle: ORA-nnnnn: Actual error
				else if (errorMessage.startsWith("ORA-"))
				{
					if (errLength>11)
						errorMessage = errorMessage.substring(11); // 11 = sizeof("ORA-nnnnn: ")
				}
				// MySQL: xxx error, message from server: "Actual error" 
				else
				{
					int index = errorMessage.indexOf("message from server: \"");
					if (index>=0)
					{
						index += 22; // 22 = sizeof("message from server: \"")
						if (index < errLength)
							errorMessage = errorMessage.substring(index, errLength-1); // Strip trailing quote 
					}
				}
			}
		}
		// If there is no error description then at least send error type
		if (errorMessage==null)
			errorMessage = error.toString();
		return errorMessage;
	}
	
	/** Break long string into 132-byte chunks and print it. */
	private static void printLongString(String str)
	{
		int slen = str.length();
		for (int i=0; i<slen; i+=130)
		{
			int j = i+130;
			if (j>slen)
				j = slen;
			//I2logger.logger.severe(str.substring(i, j));
			System.err.println(str.substring(i, j));
		}
	}

	/**
	 * Export file(s) from the local file system to a JDBC-connected database 
	 */
	private void exportFiles(String schemaName, String tablePattern) throws Exception
	{
		ResultSet rsFiles = null;
		try
		{
			DatabaseMetaData md = conn.getMetaData();
			String[] fileTypes={"TABLE"};
			rsFiles = md.getTables(null, schemaName, tablePattern, fileTypes);
			// Loop through list of tables
			while (rsFiles.next())
			{
				String tableSchem = rsFiles.getString(2);
				String tableName = rsFiles.getString(3);
				// Export a single file
				String newName = tableSchem + "." + tableName;
				exportFile(tableSchem, tableName, newName, md);
				
			} // while (rsFiles.next())
		}
		catch (Throwable e)
		{
			//I2logger.logger.printStackTrace(e);
			e.printStackTrace();
		}
		finally
		{
			if (rsFiles!=null)
				rsFiles.close();
		}
	}

	/**
	 * Export one file from the local file system to a JDBC-connected database 
	 */
	private void exportFile(byte[] tableSchem, byte[] tableName, byte[] newName) throws SQLException
	{
		exportFile(new String(tableSchem).trim(), new String(tableName).trim(), new String(newName).trim(), conn.getMetaData());
	}
	/**
	 * Export one file from the local file system to a JDBC-connected database 
	 */
	private void exportFile(String tableSchem, String tableName, String newName, DatabaseMetaData md) throws SQLException
	{
		// Open up current table
		ResultSet rsColumns = md.getColumns(null, tableSchem, tableName, "%");
		Statement stmt = conn.createStatement();
		ResultSet rsData = stmt.executeQuery("SELECT * FROM " + tableSchem+"."+tableName);
		ResultSetMetaData rsmd = rsData.getMetaData();
		StringBuffer crtTbl = new StringBuffer("CREATE TABLE " + newName + " (");
		StringBuffer insert = new StringBuffer("INSERT INTO " + newName + " VALUES(");
		String comma="";
		String quoteString = toConn.getMetaData().getIdentifierQuoteString();
		int fldCount=rsmd.getColumnCount();
		Vector labels = new Vector(fldCount);
		Vector comments = new Vector(fldCount);
		int columnIndex=0;
		while (rsColumns.next())
		{
			columnIndex++;
			// Add to INSERT statement
			insert.append(comma + "?");
			crtTbl.append(comma);

			String defaultValue = rsColumns.getString(13); // COLUMN_DEF
			//String columnName = rsColumns.getString(4);
			//int ordinal = rsColumns.getInt(17);
			//System.out.println("Column: " + columnName + " Ordinal: " + ordinal + " Default Value: " + defaultValue);

			addColumnInfo(crtTbl, insert, rsmd, columnIndex, labels, defaultValue, quoteString);
			
			
	
			comma=",";
		} // for (int i=1; i<=fldCount
		rsColumns.close();
		// Add ROWID field for I2 export of SQL Server, if necessary
		if (addROWID)
			crtTbl.append(", ROWID BIGINT IDENTITY");

		// Get primary key information for table:
		// For whatever reason, the keys aren't returned in key order (they're in alpha order), 
		// so we have to sort them
		ResultSet rsKeys = md.getPrimaryKeys(null, tableSchem, tableName);
		if (rsKeys.next())
		{
			int keySize = 0;
			Vector pkeys = new Vector();
			String pkName = rsKeys.getString(6); //Constraint name PK_NAME 
			do
			{
				int keySeq = rsKeys.getInt(5); // KEY_SEQ
				if (keySeq > keySize)
				{
					keySize = keySeq;
					pkeys.setSize(keySize);
				}
				pkeys.setElementAt(rsKeys.getString(4), keySeq); //COLUMN_NAME
			} while(rsKeys.next());
			rsKeys.close();
					
			// Add constraint name, if it exists
			if (pkName != null)
			{
				crtTbl.append(" CONSTRAINT ");
				crtTbl.append(pkName);
			}
			crtTbl.append(" PRIMARY KEY(");
			comma="";
			for (int i=0; i<keySize; i++)
			{
				crtTbl.append(comma);
				crtTbl.append((String)pkeys.elementAt(i));
			} while(rsKeys.next());
			crtTbl.append(")");
		}

		// Create table on target system
		try
		{
			Statement toStmt= createToTable(crtTbl, newName, labels);
					
			// Add comment information to columns
			int commentCount = comments.size();
			for (int i=0; i<commentCount; i++)
			{
				LabelPair lp = (LabelPair)comments.elementAt(i);
				String commentString = "COMMENT ON COLUMN " + newName + '.' + lp.column + " IS '" + lp.label + '\''; 
				toStmt.execute(commentString); 
			}
			toStmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
				
		// Now loop through data and insert into target table
		copyData(rsData, rsmd, insert, fldCount);

		stmt.close();
	}
	
	/**
	 * Run an SQL statement and then export the result set to a JDBC-connected database 
	 */
	public long exportSQL(byte[] sql, byte[] newName, long nbrRcd) throws SQLException
	{
		return exportSQL(new String(sql), new String(newName).trim(), nbrRcd);
	}
	/**
	 * Run an SQL statement and then export the result set to a JDBC-connected database
	 * @return the number of rows successfully copied 
	 */
	private long exportSQL(String sql, String newName) throws SQLException
	{
		return exportSQL(sql, newName, Long.MAX_VALUE);
	}
	/**
	 * Run an SQL statement and then export the result set to a JDBC-connected database
	 * @return the number of rows successfully copied 
	 */
	private long exportSQL(String sql, String newName, long nbrRcd) throws SQLException
	{
		long rcdCount=-1;
		try
		{
			//rcdCount=exportQuery(toConn.createStatement().executeQuery(sql), newName, toConn.getMetaData(), nbrRcd);
			rcdCount=exportQuery(conn.createStatement().executeQuery(sql), newName, conn.getMetaData(), nbrRcd);
		}
		catch (SQLException e)
		{
			//I2logger.logger.severe("Error occurred exporting query to " + newName);
			System.err.println("Error occurred exporting query to " + newName);
			printLongString(sql);
			throwSQLException(e);
		}
		// Always close the local connection
		finally
		{
			//if (isLocal)
				toConn.close();
				conn.close();
		}
		return rcdCount;
	}

	/**
	 * Export a query (result set) from the local file system to a JDBC-connected database 
	 */
	private long exportQuery(ResultSet rsData, String newName, DatabaseMetaData md, long nbrRcd) throws SQLException
	{
		ResultSetMetaData rsmd = rsData.getMetaData();
		StringBuffer crtTbl = new StringBuffer("CREATE TABLE " + newName + " (");
		StringBuffer insert = new StringBuffer("INSERT INTO " + newName + " VALUES(");
		String comma="";
		int fldCount=rsmd.getColumnCount();
		Vector labels = new Vector(fldCount);
		String quoteString = toConn.getMetaData().getIdentifierQuoteString();
		for (int columnIndex=1; columnIndex<=fldCount; columnIndex++)
		{
			// Add to INSERT statement
			insert.append(comma + "?");
			crtTbl.append(comma);
			addColumnInfo(crtTbl, insert, rsmd, columnIndex, labels, null, quoteString);
			comma=",";
		} // for (int i=1; i<=fldCount

		// Create table on target system
		try
		{
			createToTable(crtTbl, newName, labels);
		}
		catch (SQLException e)
		{
			// Ignore SQLSTATE 42710 'Object already exists'
			//System.out.println("SQL State: " + e.getSQLState());
			//System.out.println(e.getLocalizedMessage());
			/*
			if (e.getSQLState().compareTo("42710")!=0)
				throw e;
			*/
		}
				
		// Now loop through data and insert into remote table
		return copyData(rsData, rsmd, insert, fldCount, nbrRcd);
	}
	 
	public Statement createToTable(StringBuffer crtTbl, String newName, Vector labels) throws SQLException
	{
		// Create table on remote system
		String crtTblString = crtTbl.append(")").toString();
		Statement toStmt = toConn.createStatement();
		try
		{
			toStmt.execute(crtTblString);
		}
		// If an error occurrs, write out error message
		catch (SQLException e)
		{
			//I2logger.logger.severe("Error occurred when creating table using statement:");
			System.err.println("Error occurred when creating table using statement:");
			printLongString(crtTblString.toString());
			throwSQLException(e);
		}
					
		// Add label information to columns
		int labelCount = labels.size();
		for (int i=0; i<labelCount; i++)
		{
			LabelPair lp = (LabelPair)labels.elementAt(i);
			String labelString = "LABEL ON COLUMN " + newName + ".\"" + lp.column + "\" IS '" + lp.label + '\''; 
			toStmt.execute(labelString); 
		}
		return toStmt;
	}
	
	/**
	 * Add column information from a result set to a create/insert string buffer. 
	 */
	private String addColumnInfo(StringBuffer crtTbl, StringBuffer insert, ResultSetMetaData rsmd, int columnIndex, Vector labels, 
	 String defaultValue, String quoteString) throws SQLException
	{
		String colName = rsmd.getColumnName(columnIndex);
		// Column names with @, $, #, etc. are not allowed by certain DBs, so always quote
		crtTbl.append(quoteString).append(colName).append(quoteString).append(' ');

		// getColumn() returns the wrong type for CHAR CCSID 65535 (CHAR instead of BIN), so use ResultSetMetaData instead
		//int type = rsColumns.getInt(5); // DATA_TYPE
		long size = rsmd.getColumnDisplaySize(columnIndex);
		int scale = rsmd.getScale(columnIndex);
		int type = rsmd.getColumnType(columnIndex);

		// The LONG version of CHAR and BINARY seem irrelevant/unsupported
		// Adjust CHAR/BINARY data to CLOB/BLOB if too big
		if (type==Types.LONGVARCHAR || type==Types.CLOB)
		{
			if (size<=MAX_CHAR)
				type = Types.VARCHAR;
			else
				type = Types.CLOB;
		}
		else if (type==Types.LONGVARBINARY || type==Types.BLOB)
		{
			if (size<=MAX_CHAR)
				type = Types.VARBINARY;
			else
				type = Types.BLOB;
		}
		// Limit maximum size of CLOB/BLOB
		if ((type==Types.CLOB || type==Types.BLOB) & size>MAX_CLOB)
			size=MAX_CLOB;

		String columnTypeName=null;
		if (type==Types.BINARY || type==Types.BIT || type==Types.VARBINARY)
		{
			/* On OS/400 releases before V5R3, bit data is created with the (weird)
			/* CHAR(n) FOR BIT DATA clause -- DB2 still doesn't seem to support it? */
			/* Oracle only allows plain old CHAR */ 
			if (mToServerType==SERVER_TYPES_ISERIES  || mToServerType==SERVER_TYPES_DB2)
			{
				if (type==Types.VARBINARY)
					columnTypeName = "VARCHAR";
				else
					columnTypeName = "CHAR";
				columnTypeName = columnTypeName + "(" + size + ") FOR BIT DATA";
				type = Types.OTHER; 
			}
			// Oracle calls it 'RAW'
			else if (mToServerType==SERVER_TYPES_ORACLE)
				columnTypeName="RAW";
			else
				columnTypeName="BINARY";
		}
		// Oracle and SQL Server don't support double...
		else if (type==Types.DOUBLE && (mToServerType==SERVER_TYPES_SQLSERVER || mToServerType==SERVER_TYPES_ORACLE)
		// Oracle returns float data types as number with -127 scale
					 || scale==-127)
			type = Types.FLOAT;
		// SQLserver and MySQL call CLOB "TEXT"
		else if (type==Types.CLOB && (mToServerType==SERVER_TYPES_SQLSERVER || mToServerType==SERVER_TYPES_MYSQL))
			columnTypeName="TEXT";
		// SQL Server is stupid and doesn't have support for...
		else if (mToServerType==SERVER_TYPES_SQLSERVER)
		{
			// ...DATE or TIME data types, so change to datetime
			if (type==Types.DATE || type==Types.TIME || type==Types.TIMESTAMP)
				columnTypeName="DATETIME";
			// ...and maps BLOB to IMAGE
			else if (type==Types.BLOB)
				columnTypeName="IMAGE";
		}
		// Oracle is stupid and doesn't have support for...
		else if (mToServerType==SERVER_TYPES_ORACLE)
		{
			// ...TIME data types, so change to TIMESTAMP
			if (type==Types.TIME)
				type = Types.TIMESTAMP;
			// ...and no BIGINT?!? datatype
			else if (type==Types.BIGINT)
				type = Types.NUMERIC;
		}
			
		if (columnTypeName==null)
		{
			for (int l=0; l<types.length; l++)
			{
				if (types[l]==type)
				{
					columnTypeName = typeNames[l];
					break;
				}
			}
			if (columnTypeName==null)
			{
				columnTypeName = rsmd.getColumnTypeName(columnIndex);
				int j=columnTypeName.indexOf("()");
				if (j>0)
				{
					columnTypeName = columnTypeName.substring(0,j);
					type = Types.CHAR;
				}
			}
		}
		
		crtTbl.append(columnTypeName);
		// Add length for DECIMAL, NUMERIC, VAR/CHAR, VAR/BINARY and FLOAT (not REAL (single) or DOUBLE)
		if (type==Types.DECIMAL || type==Types.NUMERIC || type==Types.CHAR || type==Types.VARCHAR || 
			 type==Types.BINARY || type==Types.VARBINARY   
			 /* || type==Types.FLOAT */) // Float is non-standard, so don't use length
		{
			boolean isNumber = type==Types.DECIMAL || type==Types.NUMERIC || type==Types.FLOAT;
			// Oracle returns float numbers as scale=-127
			if (isNumber)
			{
				size = rsmd.getPrecision(columnIndex);
				// 31,31 largest size possible for <=V5R2
				// Oracle allows 0 for length?!? -- assume largest length
				if (size<=0 || size>MAX_NUMERIC_DIGITS)
					size=MAX_NUMERIC_DIGITS;
				if (scale>size)
					scale=(int)size;
			}
			if (size>0)
			{
				crtTbl.append("(" + size);
				// If this is a numeric/decimal column, include scale
				if (isNumber && type!=Types.FLOAT)
					crtTbl.append("," + scale);
				crtTbl.append(')');
			}
		}

		// Oracle requires the deault value come before nullability...
		// Append default value
		// Always append DEFAULT?
		if (defaultValue != null)
			crtTbl.append(" DEFAULT ").append(defaultValue);
		
		// Append nullability
		// ???only if not null (DB2/Oracle don't allow it!!!) 
		int nullable = rsmd.isNullable(columnIndex);
		//if (nullable != ResultSetMetaData.columnNullableUnknown)
		if (nullable == ResultSetMetaData.columnNoNulls)
		{
			if (nullable==ResultSetMetaData.columnNoNulls)
				crtTbl.append(" NOT");
			crtTbl.append(" NULL");
		}
			
		// Save label information
		String label = rsmd.getColumnLabel(columnIndex);
		if (label != null && label.compareTo(colName)!=0) // Don't add label if it is just column name
			labels.add(new LabelPair(colName, label));
			
		return colName;
	}

	/**
	 * Export file data from the local file system to a JDBC-connected database 
	 */
	public void exportFileData(byte[] tableSchem, byte[] tableName, byte[] memberName, byte[] toSchemaq) throws Exception
	{
		exportFileData(new String(tableSchem).trim(), new String(tableName).trim(), new String(memberName).trim(), new String(toSchemaq).trim());
	}
	public void exportFileData(String tableSchem, String tableName) throws Exception
	{
		exportFileData(tableSchem, tableName, null, "");
	}
	/**
	 * @param tableSchem
	 * @param tableName
	 * @param memberName
	 * @param toSchemaq the qualified schema name (for example "OOTEST." or "" if no schema is used)
	 * @throws Exception
	 */
	public void exportFileData(String tableSchem, String tableName, String memberName, String toSchemaq) throws SQLException
	{
		String newName=toSchemaq + tableName;
		String oldName = tableSchem + "." + tableName;
		Statement stmt = conn.createStatement();
		if (memberName!=null && !memberName.equals(""))
		{
			// Override to specified member
			String ovrcmd = "OVRDBF " + tableName + " MBR(" + memberName + ") OVRSCOPE(*JOB)";
			// Pad to exactly 50 characters (the goofy command call length will fail without it)
			ovrcmd = (ovrcmd+"                  ").substring(0, 50); 
			String cmd = "CALL QSYS.QCMDEXC('" + ovrcmd + "',0000000050.00000)";
			stmt.execute(cmd);
			// Generate new file name
			newName = '"' + newName + '(' + memberName + ")\"";
		}
		// The from-member must be overridden to (i.e. OVRDBF) at this point
		try
		{
			ResultSet rsData =stmt.executeQuery("SELECT * FROM " + oldName);
			ResultSetMetaData rsmd = rsData.getMetaData();
			StringBuffer insert = new StringBuffer("INSERT INTO " + newName + " VALUES(");
			String comma="";
			int fldCount = rsmd.getColumnCount();
			for (int i=1; i<=fldCount; i++)
			{
				// Add to INSERT statement
				insert.append(comma + "?");
				comma=",";
			} // for (int i=1; i<=fldCount
			
			// Now loop through data and insert into remote table
			copyData(rsData, rsmd, insert, fldCount);
		}
		catch (SQLException e)
		{
			//I2logger.logger.severe("Error occurred exporting data from " + oldName + " to " + newName);
			System.err.println("Error occurred exporting data from " + oldName + " to " + newName);
			//I2logger.logger.printStackTrace(e);
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Copy data from the local file system to a JDBC-connected database 
	 */
	private long copyData(ResultSet rsData, ResultSetMetaData rsmd, StringBuffer insert, int fldCount) throws SQLException
	{
		return copyData(rsData, rsmd, insert, fldCount, Long.MAX_VALUE);
	}
	/**
	 * Copy data from the local file system to a JDBC-connected database 
	 */
	private long copyData(ResultSet rsData, ResultSetMetaData rsmd, StringBuffer insert, int fldCount, long nbrRcd) throws SQLException
	{
		String insertString = insert.append(")").toString();
		PreparedStatement ps = toConn.prepareStatement(insertString);
		long copyCount=0;
		while (rsData.next() && copyCount<nbrRcd)
		{
			// Build values array
			for (int i=1; i<=fldCount; i++)
			{
				Object o = rsData.getObject(i);
				try
				{
					if (o != null)
					{
						try
						{
							ps.setObject(i, o);
						}
						catch (SQLException e)
						{
							// This is even more horrible and unbelievable but SQL server can't deal with Date data types, so 
							// translate them to timestamp
							if (mToServerType==SERVER_TYPES_SQLSERVER && o instanceof java.util.Date)
								o = new java.sql.Timestamp(((java.util.Date)o).getTime());
							// Map byte[] to String if the driver can't handle the conversion
							else if (o instanceof byte[])
								o = new String((byte[])o);
							// Pathetic and unaccepatable!!! Oracle return oracle.sql.TIMESTAMP?!?
							//else if (o instanceof oracle.sql.TIMESTAMP) // don't do this because it creates dependency
							else if (o.getClass().getName().compareTo("oracle.sql.TIMESTAMP")==0)
								o = rsData.getTimestamp(i);
							else
								throw e;
							ps.setObject(i, o);
						}
					}
					else
						ps.setNull(i, Types.CHAR);
				}
				catch (SQLException e)
				{
					//I2logger.logger.severe("Error occurred setting value for field " + rsmd.getColumnName(i));
					//I2logger.logger.severe("Object value: " + o);
					System.err.println("Error occurred setting value for field " + rsmd.getColumnName(i));
					System.err.println("Object value: " + o);
					throwSQLException(e);
				}
			}
			ps.executeUpdate();
			copyCount++;
			//ps.addBatch();
		}
		//ps.executeBatch();
		ps.close();
		rsData.close();
		return copyCount;
	}

	/**
	 * Execute a DDL statement 
	 */
	public void execDDL(byte[] ddl) throws Exception
	{
		execDDL(new String(ddl));
	}
	/**
	 * Execute a DDL statement and ignore the specified SQL state
	 */
	public void execDDL(byte[] ddl, byte[] monmsg) throws Exception
	{
		execDDL(new String(ddl), new String(monmsg));
	}
	
	/**
	 * Execute a DDL statement  
	 */
	public void execDDL(String ddl) throws SQLException
	{
		execDDL(ddl, null);
	}

	private static int ERR_WIDTH=130;
	/**
	 * Execute a DDL statement 
	 * @param ddl The SQL statement to execute
	 * @param monmsg The SQL state to ignore if thrown, null if no code should be ignored 
	 */
	public void execDDL(String ddl, String monmsg) throws SQLException
	{
		Statement stmt=null;
		try
		{
			if (I2Logger.logger.isDetailable())
				I2Logger.logger.detail("DDL:" + ddl);
			stmt = toConn.createStatement();
			stmt.execute(ddl);
		}
		catch (SQLException e)
		{
			/* If the SQL State returned is expected, don't signal error message */
			if (monmsg==null || e.getSQLState().compareTo(monmsg)!=0)
			{
				//I2logger.logger.severe("Error occurred processing statement:");
				System.err.println("Error occurred processing statement:");
				// Print out ddl statement in 130 (the width of DSPSPLF) byte chunks
				while (ddl.length()>ERR_WIDTH)
				{
					//I2logger.logger.severe(ddl.substring(0, ERR_WIDTH));
					System.err.println(ddl.substring(0, ERR_WIDTH));
					ddl = ddl.substring(ERR_WIDTH);
				}
				//I2logger.logger.severe(ddl);
				System.err.println(ddl);
				throwSQLException(e);
			}
		}
		finally
		{
			if (stmt!=null)
				stmt.close();
		}
	}
	
	public static void main(String[] args)
	{ 
		try {
			//ExportFile xf = new ExportFile("ANDREWC", "SP8DS", "com.ibm.as400.access.AS400JDBCDriver", "jdbc:as400://asc404", "*ISERIES");
			//ExportFile xf = new ExportFile("CDBELMS", "CDBELMS", "com.microsoft.jdbc.sqlserver.SQLServerDriver", "jdbc:microsoft:sqlserver://192.168.1.162:1433;DatabaseName=CDBELMS");
			//ExportFile xf = new ExportFile("DBUSER", "dbuserpwd", "com.ibm.db2.jcc.DB2Driver", "jdbc:db2://ASCSERVER2:50000/SAMPLE", "*DB2");
			//xf.exportFileData("APLSAMPLE","ORDLINE",null,"APLSAMPLE.");
			
			// iSeries->SQLserver
			ExportFile xf = new ExportFile("ANDREWC", "SP8DS", "com.ibm.as400.access.AS400JDBCDriver", 
			 "jdbc:as400://asc404", "*SQLSERVER", "ABLE", "ablepwd", "com.microsoft.jdbc.sqlserver.SQLServerDriver", 
			 "jdbc:microsoft:sqlserver://ASCServer2:1433;SelectMethod=cursor" 
			 
			);
			/*
			// iSeries->DB2
			ExportFile xf = new ExportFile("DBUSER", "dbuserpwd", "com.ibm.db2.jcc.DB2Driver", 
			 "jdbc:db2://ASCSERVER2:50000/APLSAMPL:retrieveMessagesFromServerOnGetMessage=true;", "*DB2",
			 "ANDREWC", "SP8DS", "com.ibm.as400.access.AS400JDBCDriver", "jdbc:as400://asc404"
			// iSeries->Oracle
			ExportFile xf = new ExportFile("DBUSER", "dbuserpwd", "oracle.jdbc.OracleDriver", 
			 "jdbc:oracle:thin:@ASCSERVER2:1521:ORCL", "*ORACLE",
			 "ANDREWC", "SP8DS", "com.ibm.as400.access.AS400JDBCDriver", "jdbc:as400://asc404"
			};
			// iSeries->MySQL
			ExportFile xf = new ExportFile("ascas400", "aJ7xtu92", "com.mysql.jdbc.Driver", 
			 "jdbc:mysql://www.asc-iseries.com:3306/ascas400", "*MYSQL",
			 "ANDREWC", "SP8DS", "com.ibm.as400.access.AS400JDBCDriver", "jdbc:as400://asc404"
			};

			// Oracle->iSeries
			ExportFile xf = new ExportFile("ANDREWC", "SP8DS", "com.ibm.as400.access.AS400JDBCDriver", 
          	"jdbc:as400://asc404", "*ISERIES", 
				"DBUSER", "dbuserpwd", "oracle.jdbc.OracleDriver", 
			 	"jdbc:oracle:thin:@ASCSERVER2:1521:ORCL"
			// SQLserver->iSeries
			ExportFile xf = new ExportFile("ANDREWC", "SP8DS", "com.ibm.as400.access.AS400JDBCDriver", 
				"jdbc:as400://asc404", "*ISERIES", 
				"APLSAMPLE", "aplsamplepwd", "com.microsoft.jdbc.sqlserver.SQLServerDriver", 
				"jdbc:microsoft:sqlserver://ASCServer2:1433;SelectMethod=cursor"
			// DB2->iSeries 
			ExportFile xf = new ExportFile("ANDREWC", "SP8DS", "com.ibm.as400.access.AS400JDBCDriver", 
				"jdbc:as400://asc404", "*ISERIES", 
				"DBUSER", "dbuserpwd", "com.ibm.db2.jcc.DB2Driver", 
			 	"jdbc:db2://ASCSERVER2:50000/APLSAMPL:retrieveMessagesFromServerOnGetMessage=true;" 
			// MySQL->iSeries
			ExportFile xf = new ExportFile("ANDREWC", "SP8DS", "com.ibm.as400.access.AS400JDBCDriver", 
			   "jdbc:as400://asc404", "*ISERIES",
			   "ascas400", "aJ7xtu92", "com.mysql.jdbc.Driver", "jdbc:mysql://www.asc-iseries.com:3306/ascas400"

			// Oracle->SQLServer
			ExportFile xf = new ExportFile("APLSAMPLE", "aplsamplepwd", "com.microsoft.jdbc.sqlserver.SQLServerDriver", 
			 "jdbc:microsoft:sqlserver://ASCServer2:1433;SelectMethod=cursor", "*SQLSERVER",
			 "DBUSER", "dbuserpwd", "oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@ASCSERVER2:1521:ORCL"
			// Oracle->DB2
			ExportFile xf = new ExportFile("DBUSER", "dbuserpwd", "com.ibm.db2.jcc.DB2Driver", 
			 "jdbc:db2://ASCSERVER2:50000/APLSAMPL:retrieveMessagesFromServerOnGetMessage=true;", "*DB2",
			 "DBUSER", "dbuserpwd", "oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@ASCSERVER2:1521:ORCL"

			// SQLserver->Oracle
			ExportFile xf = new ExportFile("DBUSER", "dbuserpwd", "oracle.jdbc.OracleDriver", 
			 "jdbc:oracle:thin:@ASCSERVER2:1521:ORCL", "*ORACLE", 
				"APLSAMPLE", "aplsamplepwd", "com.microsoft.jdbc.sqlserver.SQLServerDriver", 
				"jdbc:microsoft:sqlserver://ASCServer2:1433;SelectMethod=cursor"
			// SQLserver->DB2
			ExportFile xf = new ExportFile("DBUSER", "dbuserpwd", "com.ibm.db2.jcc.DB2Driver", 
			 "jdbc:db2://ASCSERVER2:50000/APLSAMPL:retrieveMessagesFromServerOnGetMessage=true;", "*DB2", 
				"APLSAMPLE", "aplsamplepwd", "com.microsoft.jdbc.sqlserver.SQLServerDriver", 
				"jdbc:microsoft:sqlserver://ASCServer2:1433;SelectMethod=cursor"

			// DB2->SQLserver
			ExportFile xf = new ExportFile("APLSAMPLE", "aplsamplepwd", "com.microsoft.jdbc.sqlserver.SQLServerDriver", 
			 "jdbc:microsoft:sqlserver://ASCServer2:1433;SelectMethod=cursor", "*SQLSERVER", 
				"DBUSER", "dbuserpwd", "com.ibm.db2.jcc.DB2Driver", 
				"jdbc:db2://ASCSERVER2:50000/APLSAMPL:retrieveMessagesFromServerOnGetMessage=true;" 
			// DB2->Oracle
			ExportFile xf = new ExportFile("DBUSER", "dbuserpwd", "oracle.jdbc.OracleDriver", 
			 "jdbc:oracle:thin:@ASCSERVER2:1521:ORCL", "*ORACLE", 
				"DBUSER", "dbuserpwd", "com.ibm.db2.jcc.DB2Driver", 
				"jdbc:db2://ASCSERVER2:50000/APLSAMPL:retrieveMessagesFromServerOnGetMessage=true;" 
			*/

	
	// iSeries->Oracle
	/*
	//ExportFile xf = new ExportFile("ANDREWC", "SP8DS", "com.ibm.as400.access.AS400JDBCDriver", "jdbc:as400://asc406",
		"*ORACLE","DBUSER", "dbuserpwd", "oracle.jdbc.OracleDriver","jdbc:oracle:thin:@ASCSERVER2:1521:ORCL"
	 
	
	// iSeries->iSeries
	/*ExportFile xf = new ExportFile("ANDREWC", "SP8DS", "com.ibm.as400.access.AS400JDBCDriver", "jdbc:as400://asc406",
		"*ISERIES",
				                      "ANDREWC", "SP8DS", "com.ibm.as400.access.AS400JDBCDriver", "jdbc:as400://asc404;translate binary=true;"
	*/			                       
	 
			try
			{
			//xf.execDDL("DROP TABLE orcl_1");
			//xf.execDDL("DROP TABLE orcl_dtype");
			//xf.execDDL("DROP TABLE PIRUSFJF..ffcodpfa");
			xf.execDDL("DROP TABLE ABLE.CUSTMAST");
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			//xf.exportSQL("SELECT empno, ename, job, hiredate, mgr, sal, comm, deptno FROM scott.emp", "orcl_1");
			/*
			xf.exportSQL("SELECT chardata, nchardata, varchardata, nvarchardata, numberdata, " + 
			             "integerdata, floatdata, numdata15_0, intdata2_2, fltdata31_31," +
			             "datedata, timestampdata " +
			             "FROM     dbuser.datatypes", "orcl_dtype");
			xf.exportSQL("SELECT   char10, hex2, znd50, pkd50, znd155, pkd155, bin2, bin2_43, bin4," +
			             "bin4_98, bin8, sfp98, dfp178, datefld, timefld, tstamp, dbcsg, vchar " + 
			             "FROM     sequelu#.datatypes", "LOCL_DTYPE");
							 //"FROM LOCL_DTYPE", "LOCL_DTYPE");
			*/
			//xf.exportFile("TEST", "ONEFLD", "COLLSAVE.RCD132", xf.conn.getMetaData());
			/*
			xf.exportFileData("ANDREWC", "RCD132", "", "ANDREWC.");
							 //"FROM LOCL_DTYPE", "LOCL_DTYPE");
			*/
			xf.exportSQL("SELECT * FROM rmt01.custmast01","ABLE.CUSTMAST");
		} catch (Exception e) {
			//I2logger.logger.printStackTrace(e);
			e.printStackTrace();
		}
	}
}
