package com.i2class;

import java.util.*;
import java.math.BigDecimal;
import java.sql.*;

import com.ibm.as400.access.AS400JDBCDriver;

/**
 * A database file class for keyed JDBC access.
 * @author Andrew Clark
 */
public abstract class RkeyedJDBC extends RfileJDBC implements IKeyedFile
{
	// This is set on open from dbRecord
	protected int maxKeyCount;
	
	/**
	 * Construct a keyed file where the index name (LF) is different than the table (PF) that data is 
	 * actually selected from.  This is important for SQL INDEX files since JDBC does not allow data to be
	 * selected from them.
	 * @param system The system that the specified files reside on.
	 * @param indexName The name of the index that contains the key to use for this file. 
	 * @param tableName The name of the table that contains the actual data.
	 */
	public RkeyedJDBC(java.sql.Connection system, String indexName, String tableName) 
	{
		super(system, tableName);
	}
	public RkeyedJDBC(java.sql.Connection system, String lfileName) 
	{
		super(system, lfileName);
	}

	public boolean chain()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		found = setLowerLimit();
		direction = 'C';
		// This is weird, but %found for CHAIN means a different thing (exact match) than SETLL (!eof), so we
		// have to explicitly set it here.
		setI2Found(found);
		if (!found)
			return true;
		m_reade='=';
		return readx();
	}
	public boolean chain(RecordJDBC r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		setRecordFormat(r);
		return chain();
	}
	/**
	 * Chain to a record with no record lock.
	 */
	public boolean chainn()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		return chain();
	}
	public boolean chainn(RecordJDBC r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		setRecordFormat(r);
		return chainn();
	}
	/** 
	 * Do binary search to find key in result set.  
	 * Returns 0 if exact match found.
	 * Returns >0 if recordKey > searchKey.
	 * Returns <0 if recordKey < searchKey.
	 * @param allKeys true if all keys should be checked, false otherwise.
	 */
	private int checkKey(Object[] key, int keyLength, boolean allKeys) throws SQLException
	{
		set_checkKey(0, null);
		//_checkKey = new Comparable[keyCount];
		if (eof)
			return 1;
		if (bof)
			return -1;
		int diff=0;
		int keyLoop;
		if (allKeys)
			keyLoop=maxKeyCount;
		else
			keyLoop=keyLength;
		for (int i = 0; i < keyLoop; i++)
		{
			String keyName = ((RecordJDBC)irecord).getKeyName(i);
			/*
			if (keyi instanceof String)
			{
				String s = rs.getString(keyName);
				diff = s.compareTo(keyi.toString());
			}
			else
			{
				//double d=rs.getDouble(keyName);
				//diff = (int)(d-((BigDecimal)keyi).doubleValue());
				BigDecimal bd = rs.getBigDecimal(keyName);
				diff = bd.compareTo(keyi);
			}
			*/
			Comparable checkKeyi = (Comparable)rs.getObject(keyName);
			//_checkKey[i] = checkKeyi;
			set_checkKey(i, checkKeyi);
			// If we are saving the entire key, then diff may already be set
			if (i<keyLength && diff==0)
				diff = checkKeyi.compareTo(key[i]);
		}
		return diff;
	}
	
	/** Used by RseekJDBC to cache key values. */
	protected void set_checkKey(int i, Comparable checkKeyi) {
	}
	
	public void open(int openType, int blockFactor, int commit) throws Exception
	{
		// Only open the result set once
		//if (rs == null)
		if (!opened)
		{
			checkOpen();
			RecordJDBC dbRecord = (RecordJDBC)irecord;
			maxKeyCount = dbRecord.keyNames.size();

			// Build SQL statement to actually open file
			/*
			StringBuffer sqlBuf=new StringBuffer("SELECT ");
			String comma="";
			// Build list of all fields in SELECT clause.  Do this instead of SELECT * so that we can add the ROWID field to the end
			// of the SELECT clause
			int fieldCount = dbRecord.fldNames.size();
			for (int i=0; i<fieldCount; i++)
			{
				sqlBuf.append(comma + dbRecord.fldNames.elementAt(i));
				comma=",";
			}
			*/
			StringBuffer sqlBuf = buildSelect();
			DatabaseMetaData connMetaData = null;
			// If this is an updatable cursor, then we need to add ROWID to the list of selected fields
			String dbName = null;
			boolean nativeRowid = false;
			String rowid = null;
			String rowidQuote = "";
			if (openType == com.ibm.as400.access.AS400File.READ_WRITE)
			{
				// Try to get a rowid-like equivalent.  
				//connMetaData = conn.getMetaData();
				connMetaData = rconn.getConn(commit).getMetaData();
				dbName = connMetaData.getDatabaseProductName();
				// DB2/400 
				if (dbName.indexOf("400") >= 0)
				{
					rowid = "rrn(xxx)";
					correlation = " xxx";
				}
				else if (dbName.compareTo("PostgreSQL") == 0)
					rowid = "oid";
				// Any other database has to implement rowid
				else
				{
					rowid = "rowid";
					rowidQuote = "'";
				}
				// Oracle, Informix, and OS/390 support a rowid function, so add to select list.  
				// Everything else has to have a row named "rowid"
				nativeRowid =
					(dbName.indexOf("Oracle") >= 0
						|| dbName.indexOf("Informix") >= 0
						|| dbName.indexOf("OS/390") >= 0);
				// Always do this, since we are always building a list of fields to support SQL server
				//if (rowid != "rowid" || nativeRowid)
				sqlBuf.append(',').append(rowid);
				if (!rowid.equals("rowid"))
					sqlBuf.append(" as rowid");
			}
			sqlBuf.append(" FROM ").append(actualFileName).append(" xxx ");
			// Add query expression if the user has set one
			sqlBuf.append(queryExpression);
			// Build ORDER BY portion of selection
			String comma = " ORDER BY ";
			for (int i = 0; i < maxKeyCount; i++)
			{
				sqlBuf.append(comma).append('"').append(dbRecord.getKeyName(i)).append("\" ").append(dbRecord.getKeyType(i));
				comma = ",";
			}

			// Get statement with proper CONCUR, fetchSize and COMMIT levels
			sql = sqlBuf.toString();
			connMetaData =
				concurCommit(openType, blockFactor, commit, connMetaData);
			// Create identity clause
			if (openType == Application.READ_WRITE)
			{
				// If we are stuck with rowid, and the database does not natively support it, then build identity column list
				identityFields = new Vector();
				identityFunctions = new Vector();
				if (rowid.equals("rowid") && !nativeRowid)
				{
					String schema = "";
					String table = null;
					int i = actualFileName.indexOf('.');
					if (i >= 0)
					{
						schema = actualFileName.substring(0, i);
						table = actualFileName.substring(i + 1);
					}
					else
						table = actualFileName;
					ResultSet rsRowid =
						connMetaData.getBestRowIdentifier(
							null,
							schema,
							table,
							DatabaseMetaData.bestRowTransaction,
							true);
					while (rsRowid.next())
					{
						String identityColumn = rsRowid.getString(2);
						identityFields.add(identityColumn);
						identityFunctions.add(identityColumn);
						/*
						String quote = "";
						// If this is a character string, then quote it
						short dataType = rsRowid.getShort(3);
						if (dataType != Types.BIGINT
							&& dataType != Types.DECIMAL
							&& dataType != Types.DOUBLE
							&& dataType != Types.FLOAT
							&& dataType != Types.INTEGER
							&& dataType != Types.NUMERIC
							&& dataType != Types.REAL
							&& dataType != Types.SMALLINT
							&& dataType != Types.TINYINT)
						{
							quote = "'";
						}
						identityQuote.add(quote);
						*/
					}
					rsRowid.close();
				}
				// If no identity fields were found, then just add rowid as identity field
				if (identityFields.size() == 0)
				{
					identityFields.add("rowid");
					identityFunctions.add(rowid);
				}
			}

			// All of the getBestRowIdentifier() stuff, below, conceptually is a better solution to the problem, but
			// doesn't work with any of the drivers that I've tried.  Just use ROWID, above
			/*
			_updatable = rs.getMetaData().isDefinitelyWritable(1);
			_updatable = false;
			// If the result is not updatable, then we need to get rowIdentifier information
			if (!_updatable)
			{
				String schema="";
				String table=null;
				int i=fileName.indexOf('.');
				if (i>=0)
				{
					schema=fileName.substring(0, i);
					table = fileName.substring(i+1);
				}
				else
					table = fileName;
				rsRowid=conn.getMetaData().getBestRowIdentifier(null,schema,table,DatabaseMetaData.bestRowSession,false);
				String rowField=null;
				while (rsRowid.next())
					rowField = rsRowid.getString(2);
				rowField += " xxx";
			}
			openSQL(openType, sqlBuf.toString());
			// Append FOR UPDATE OF clause for all fields that are not part of ORDER BY
			/*
			if (concur==ResultSet.CONCUR_UPDATABLE)
			{
				String forUpdate=" FOR UPDATE OF ";
				int fieldCount = dbRecord.fieldNames.size();
				for (int i=0; i<fieldCount; i++)
				{
					String fldName = dbRecord.fieldNames.elementAt(i).toString();
					if (!dbRecord.keyNames.contains(fldName))
					{
						sqlBuf.append(forUpdate + fldName);
						forUpdate=",";
					}
				}
			}
				
			rs = stmt.executeQuery(sqlBuf.toString());
			// If the result set is not updatable, then we'll have to do individual updates
			_updatable = rs.getMetaData().isDefinitelyWritable(1);
			*/
		}
	}

	/* Everything from this point on is considered 'keyed' access */
	public boolean reade()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		char reade = m_reade;
		if (!readCycle())
			return true;
		RecordJDBC dbRecord = (RecordJDBC)irecord;
		//int diff = checkKey(dbRecord.getKey(), dbRecord.keyCount, false);
		int diff = checkKey(dbRecord.key, dbRecord.keyCount, false);
		if (diff != 0)
		{
			// If there are still records and we have just positioned 'past' the last record with the same key 
			// then save position so that a SETGT to the same key doesn't need to reposition the list
			if (diff>0 && reade=='=')
			{
				m_reade='>';
				m_readeKey = new Object[dbRecord.keyCount];
				System.arraycopy(dbRecord.key, 0, m_readeKey, 0, m_readeKey.length);
			}
			eof=true;
		}
		else
		{
			m_reade='=';
			readx();
		}
		setI2Eof(eof);
		return eof;
	}
	public boolean reade(RecordJDBC r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return reade();
	}
	public boolean readen()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		return reade();
	}
	public boolean readen(RecordJDBC r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readen();
	}
	public boolean readEqual()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		readxe();
		return reade();
	}
	public boolean readEqual(RecordJDBC r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readEqual();
	}
	public boolean readEqualn()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		return readEqual();
	}
	public boolean readEqualn(RecordJDBC r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readEqualn();
	}
	public boolean readpe()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		if (!positionReadp())
			return true;
		RecordJDBC dbRecord = (RecordJDBC)irecord;
		//int diff = checkKey(dbRecord.getKey(), dbRecord.keyCount, false);
		int diff = checkKey(dbRecord.key, dbRecord.keyCount, false);
		if (diff != 0)
			return true;
		return readx();
	}
	public boolean readpe(RecordJDBC r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readpe();
	}
	public boolean readpen()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		return readpe();
	}
	public boolean readpen(RecordJDBC r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readpen();
	}
	public boolean readpEqual()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		readxe();
		return readpe();
	}
	public boolean readpEqual(RecordJDBC r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readpEqual();
	}
	public boolean readpEqualn()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		return readpEqualn();
	}
	public boolean readpEqualn(RecordJDBC r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readpEqualn();
	}
	private void readxe() throws SQLException
	{
		// This is a READE with no factor 1.  Populate the key with the current value in the
		// record and then call the normal reade().
		RecordJDBC dbRecord = (RecordJDBC)irecord;
		int vSize = dbRecord.keyNames.size();
		for (int i = 0; i < vSize; i++)
		{
			String keyName = dbRecord.getKeyName(i);
			dbRecord.key[i] = rs.getObject(keyName);
		}
		dbRecord.keyCount = vSize;
	}
	
	public abstract boolean setgt() throws Exception;
	public boolean setgt(RecordJDBC r) throws Exception
	{
		setRecordFormat(r);
		return setgt();
	}
	
	protected abstract boolean setLowerLimit() throws Exception;
	public boolean setll() throws Exception
	{
		setI2Equal(setLowerLimit());
		return m_equal;
	}
	public boolean setll(RecordJDBC r) throws Exception
	{
		setRecordFormat(r);
		return setll();
	}
	
	/*
	public static void main(String args[])
	{
		Driver driver = Application.registerDriver(com.ibm.as400.access.AS400JDBCDriver.class);
		Connection host=Application.getI2Connection("jdbc:as400://ASC406","ANDREWC","SP8DS");
		try
		{
			Statement stmt = host.createStatement();
			//ResultSet rs = stmt.executeQuery("SELECT * FROM TEST.PACKED");
			ResultSet rs = stmt.executeQuery("SELECT glbybl,GLBBNK,GLBBRN, glbgln, glbccy FROM JAVA.GLBLNL3 WHERE GLBBRN=1110100100020000 order by glbbnk, glbbrn, glbgln, glbccy");
			while (rs.next())
			{
				BigDecimal bd = rs.getBigDecimal(1);
				double d = rs.getDouble(1);
				d += .01;
			}
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	*/
}
