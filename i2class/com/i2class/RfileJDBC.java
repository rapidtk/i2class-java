package com.i2class;

import java.sql.*;

import com.ibm.as400.access.*;
import java.util.*;

/**
 * A database file class for sequential JDBC access.
 * @author Andrew Clark
 */
public class RfileJDBC
	extends RfileCycle implements RfileDisk /*extends com.ibm.Connection.access.KeyedFile*/
{
	//protected RecordJDBC dbRecord;
	/** 
	 * The current direction of the cursor:
	 *  'F'=Forward : read moves to next record, readp reads current.
	 *  'B'=Backward : read reads current record, readp moves to previous.
	 *  'C'=Chain: read moves to next record, readp moves to previous record.
	 */
	protected char direction;
	protected Connection conn;
	protected I2Connection rconn;
	ResultSet rs;
	protected int concur;
	protected boolean _updatable;
	protected boolean readOnlyWrite;
	// Are changes made through an updatable cursor visible?
	protected boolean deletesAreVisible;
	protected boolean updatesAreVisible;
	protected boolean insertsAreVisible;
	protected Statement stmt;
	protected Statement stmtWork;
	String sql;
	static protected int cursorCount = 0;
	//protected int lastRecord;
	int fldIndexMap[];
	private PreparedStringStatement preparedUpdate, preparedDelete, preparedInsert;

	protected Vector identityFields;
	protected Vector identityFunctions;
	String correlation = "";
	private QfileName m_qfilename;
	String queryExpression = "";
	/*
	 * Flag that indicates the current state of a READE statements '='=equal key, '>'=> than key.
	 * Since it is common to do a READE followed by a SETGT, save this flag so that we know when 
	 * this condition occurs since we typically don't have to reposition in this case e.g.:
	 * LIB CHAIN CUSTMAST
	 * DOU %EOF
	 * LIB:FILE READE CUSTMAST
	 * ENDDO
	 * LIB:FILE SETGT CUSTMAST
	 * LIB READE CUSTMAST
	 */
	protected char m_reade;
	protected Object[] m_readeKey;

	public RfileJDBC(
		Connection connection,
		String lfileName) 
	{
		// Add this reference here so that objects that aren't opened don't have reference to class
		rconn = (I2Connection)connection;
		try
		{
			setFileName(lfileName);
		}
		catch (Exception e)
		{
			I2Logger.logger.printStackTrace(e);
		}

		return;
	}
	
	/** Destroy the connection objects that this object references. */
	protected void finalize() throws Throwable
	{
		super.finalize();
		conn=null;
	}
	/**
	 * Use the input() and output() methods of the format objects that has already been set
	 * to build the list of fields that the file is actually going to select from.
	 * @version 2/17/2003 11:21:08 AM
	 * @return java.lang.String
	 */
	protected StringBuffer buildSelect() throws Exception
	{
		StringBuffer selectBuf = new StringBuffer("SELECT ");
		// Select * doesn't seem to work on SQL server.  Build explicit list
		RecordJDBC dbRecord = (RecordJDBC)irecord;
		if (dbRecord.fieldList == null)
		{
			// Construct a field-list object to 'intercept' the field names that need to be selected
			FieldSet fs = new FieldSet(dbRecord);
			rs = fs;
			int fldCount = dbRecord.fldNames.size();
			fldIndexMap = new int[fldCount];
			for (int i = 0; i < fldCount; i++)
				fldIndexMap[i]=i;

			// Make calls to input() and output() to build the field select list
			dbRecord.input();
			// Have to make sure that _updatable is true to ensure that field list is built correctly
			boolean supdatable = _updatable;
			_updatable=true;
			dbRecord.output();
			_updatable = supdatable;
			// Reset so that open builds new ResultSet
			rs=null;
			dbRecord.clearUpdates();
			//TODO!!! Field names can get added through the result set or through updateValue(), so merge list
			//fs.fieldNames.addAll(dbRecord.fldNames);
			// We have to add all of the fields in the ORDER BY to the SELECT clause if they aren't already there.
			// This allows keyed positioning of files (because we have to return the value of the keys
			int keyCount = dbRecord.keyNames.size();
			for (int i=0; i<keyCount; i++)
			{
				String keyName = (String)dbRecord.keyNames.elementAt(i);
				if (!fs.fieldNames.contains(keyName))
					fs.fieldNames.add(keyName);
			}
			 
			//dbRecord.fieldList="xxx.*";
			//selectBuf = new StringBuffer();
			String comma = "";
			//fldCount = dbRecord.fldNames.size();
			//for (int i = 0; i < fldCount; i++)
			Iterator it = fs.fieldNames.iterator();
			while (it.hasNext())
			{
				//String fldName = (String) fs.fieldNames.elementAt(i);
				String fldName = (String) it.next();
				selectBuf.append(comma);
				// Add delimiters around field name?
				selectBuf.append('"').append(fldName).append('"');
				comma = ",";
			}
			dbRecord.fieldList = selectBuf.substring(7); // 7=len("SELECT ")
			//selectBuf.append("xxx.*");
		}
		else
			selectBuf.append(dbRecord.fieldList);
		return selectBuf;
	}
	/** Chain to a specific record number. */
	public boolean chain(RecordJDBC r, int row) throws Exception
	{
		direction = 'C';
		found = setLowerLimit(r, row);
		if (!found)
			return true;
		return readx();
	}
	/** Chain to a specific record number. */
	public boolean chain(RecordJDBC record, FigConstNum row) throws Exception
	{
		return chain(record, row.intValue());
	}
	/** Chain to a specific record number. */
	public boolean chain(RecordJDBC record, INumeric row) throws Exception
	{
		return chain(record, row.intValue());
	}

	public void close() throws SQLException
	{
		if (opened)
		{
			if (rs != null)
			{
				rs.close();
				m_reade=' ';
				rs = null;
			}
			if (stmt != null)
			{
				stmt.close();
				stmt=null;
			}
			if (preparedUpdate != null)
			{
				preparedUpdate.m_pstmt.close();
				preparedUpdate=null;
			}
			if (preparedDelete != null)
			{
				preparedDelete.m_pstmt.close();
				preparedDelete=null;
			}
			if (preparedInsert != null)
			{
				preparedInsert.m_pstmt.close();
				preparedInsert=null;
			}
		}
		removeClosedFile();
	}
	public void commit() throws SQLException
	{
		conn.commit();
	}
	public static void commit(Connection connection) throws SQLException
	{
		connection.commit();
	}
	
	/** Check to see if file is ready to be opened */
	protected void checkOpen() throws Exception
	{
		if (irecord==null)
			throw new Exception("Record format must be set before file can be opened");
		// Reset format if this file has been closed and then reopened
		//setRecord(irecord); 
		if (rconn != null)
			app = rconn.getApp();
		checkOverrides();
	}
	
	/**
	 * Use the open type, block factor, and commit level from an open() statement to create the JDBC statement 
	 * with the correct concur level and commitment type.  
	 * @return java.sql.DatabaseMetaData the database metadata associated with the statement.
	 * @param openType int
	 * @param commitType int
	 */
	protected DatabaseMetaData concurCommit(
		int openType,
		int blockFactor,
		int commit,
		DatabaseMetaData connMetaData)
		throws Exception
	{
		
		// Make sure that the connection object exists before we try to do anything
		conn = rconn.getConn(commit);
		if (conn == null)
			throw new SQLException(
				"Connection object must be created before "
					+ actualFileName
					+ " can be opened");

		// Set cursor stability
		this.openType = openType;
		if (openType == Application.READ_ONLY)
			concur = ResultSet.CONCUR_READ_ONLY;
		else
			concur = ResultSet.CONCUR_UPDATABLE;
		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, concur);

		// If this is not a READ_ONLY open, then set appropriate INSERT/UPDATE/DELETE flags
		if (openType != Application.READ_ONLY)
		{
			// getConcurrency() doesn't seem to really return 'downgraded' value, use isDefinitelyWritable() instead
			//_updatable = (rs.getConcurrency()==ResultSet.CONCUR_UPDATABLE);
			//This doesn't work either!  Just assume: _updatable = !RindexJDBC
			//_updatable = rs.getMetaData().isDefinitelyWritable(1);
			
			// For WRITE_ONLY opens, we want to always do external writes since there is no reason to actually
			// open the result set.
			_updatable = (connMetaData==null && openType!=Application.WRITE_ONLY);
			if (connMetaData == null)
				connMetaData = conn.getMetaData();
			// The type of result set actually returned.  This may be different than what was asked for because the
			// driver may 'downgrade' it to deal with certain SQL statements (e.g. ORDER BY)
			//int rsType = rs.getType();
			// We need to change this to accomodate openResultSet() below -- the type has to be TYPE_SCROLL_SENSITIVE
			// anyway, or nothing is going to work.
			int rsType = ResultSet.TYPE_SCROLL_SENSITIVE;
			// Set appropriate flags
			if (_updatable)
			{
				deletesAreVisible = connMetaData.ownDeletesAreVisible(rsType);
				updatesAreVisible = connMetaData.ownUpdatesAreVisible(rsType);
				insertsAreVisible = connMetaData.ownInsertsAreVisible(rsType);
				// If this is READ_WRITE updatable result set, set the block size to 1
				// so that changes are always visible
					
				// The driver should really deal with this, but move this (see above) so that updates from 
				// server-side cursors are always available regardless of the JDBC driver used.  
				//if (openType==Application.READ_WRITE)
				//	rs.setFetchSize(1);
			}
			else
			{
				deletesAreVisible = connMetaData.othersDeletesAreVisible(rsType);
				updatesAreVisible = connMetaData.othersUpdatesAreVisible(rsType);
				insertsAreVisible = connMetaData.othersInsertsAreVisible(rsType);
				// DB2/400 allows writes through result sets that are not updatable
				readOnlyWrite = (connMetaData.getDatabaseProductName().indexOf("400")>=0);
				// Create work statement for external updates/etc.
				stmtWork = conn.createStatement();
			}
		}

		// Add this becuase Oracle (and others?) can't reflect changes from underlying database unless the
		// fetch size is 1
		/* Is this necessary !!!
		if (openType != Application.READ_ONLY)
		{
			if (connMetaData == null)
				connMetaData = conn.getMetaData();
			if (blockFactor == 0)
			{
				String databaseName = connMetaData.getDatabaseProductName();
				if (databaseName.indexOf("Oracle")>=0)
					blockFactor=1;
			}
		}
		*/
		
		// If this is READ_WRITE updatable result set and (any kind of) changes are not visible, set the block size to 1
		// so that changes are always visible
		if (openType==Application.READ_WRITE && !(deletesAreVisible && updatesAreVisible && insertsAreVisible))
			blockFactor = 1;
		if (blockFactor > 0)
			stmt.setFetchSize(blockFactor);
			
		// Add this file to the list of open files so that they can be closed during the RETURN step
		addToOpenFiles();

		bof=true;
		eof=false;
		direction='F';
		
		return connMetaData;	
	}
	
	/** 
	 * Actually open the result set if it has not already been opened.  We do this here instead of at
	 * open time so that we don't have to do 'SELECT *' on a file if we are later going to do 
	 * any file i/o (e.g. CHAIN, SETLL) that changes the result set.
	 */ 
	protected void openResultSet() throws Exception
	{
		if (rs==null)
		{
			// Get result set
			ResultSet rs = null;
			if (sql != null)
			{
				try
				{
					rs = stmt.executeQuery(sql);
				}
				catch (SQLException e)
				{
					I2Logger.logger.severe("Error in SQL:" + sql);
					throw e;
				}
			}	
			openNewResultSet(rs);
		
			/* Map field numbers to field names
			RecordJDBC dbRecord = (RecordJDBC)irecord;
			int fldCount = dbRecord.fldNames.size();
			fldIndexMap = new int[fldCount];
			for (int i = 0; i < fldCount; i++)
			{
				String fldName = (String) dbRecord.fldNames.elementAt(i);
				try
				{
					fldIndexMap[i] = rs.findColumn(fldName);
				}
				catch (SQLException e)
				{
				}
			}
			*/
	
		}
	}
	
	/** If this is a new result set, map field names
	 * 
	 * @throws SQLException
	 */
	protected void openNewResultSet(ResultSet rsNew) throws Exception
	{
		// If the new result set hasn't been set yet, then the file hasn't been opened -- error 
		if (rsNew == null)
		{
			//QfileName q = rconn.qfileName(fileName);
			fixed msgdta = new fixed(59);
			fixed fileName = new fixed(10, m_qfilename.fileName);
			msgdta.setFixedAt(49, fileName);
			throw new Pgmmsg("RPG1211", "QRPGMSGE", msgdta);
		}
		
		boolean firstOpen=(rs==null);
		rs = rsNew;
		// Only do the mapping the first time that the result set is opened
		if (firstOpen)
		{
			// Map field numbers to field names
			RecordJDBC dbRecord = (RecordJDBC)irecord;
			int fldCount = dbRecord.fldNames.size();
			fldIndexMap = new int[fldCount];
			for (int i = 0; i < fldCount; i++)
			{
				String fldName = (String) dbRecord.fldNames.elementAt(i);
				try
				{
					fldIndexMap[i] = rs.findColumn(fldName);
				}
				catch (SQLException e)
				{
				}
			}
		}
	}
	
	public boolean Delete() throws Exception
	{
		if (_updatable)
			rs.deleteRow();
		else
		{
			if (preparedDelete==null)
			{
				StringBuffer sqlDelete =
					new StringBuffer("DELETE FROM " + actualFileName + correlation);
				sqlDelete = getIdentityClause(sqlDelete);
				preparedDelete = new PreparedStringStatement(conn, sqlDelete.toString());
			}
			// There are no place holders up to this point in a delete statement, so use 0
			prepareIdentityClause(preparedDelete, 0);
			//stmtWork.executeUpdate(sqlUpdate.toString());
			preparedDelete.m_pstmt.executeUpdate();
		}
		// If deletes are not visible, then we have to refresh the result set
		if (!deletesAreVisible)
			refreshResultSet();
		//lastRecord--;
		return true;
	}
	public boolean Delete(RecordJDBC r) throws Exception
	{
		setRecordFormat(r);
		return Delete();
	}
	public boolean Delete(RecordJDBC r, long rrn) throws Exception
	{
		//Make sure that the result set is actually open
		openResultSet();
		rs.absolute((int) rrn);
		return Delete(r);
	}
	/**
	 * Return the WHERE clause that corresponds to the identity columns in the database
	 * @version 2/19/2003 10:54:31 AM
	 * @return java.lang.StringBuffer
	 * @param sqlUpdate java.lang.StringBuffer
	 */
	final private StringBuffer getIdentityClause(StringBuffer sqlUpdate)
		throws SQLException
	{
		//int identityCount = identityList.size();
		String and = " WHERE ";
		int identityCount = identityFunctions.size();
		for (int i = 0; i < identityCount; i++)
		{
			//String identityName = (String) identityFields.elementAt(i);
			String identityFunction = (String) identityFunctions.elementAt(i);
			//String quote = (String) identityQuote.elementAt(i);
			sqlUpdate.append(and).append(identityFunction).append("=?");
			/* Since we're using prepared statements, just use placeholders instead of values. 
					+ quote
					+ rs.getString(identityName)
					+ quote);
			*/
			and = " AND ";
		}
		return sqlUpdate;
	}

	/**
	 * Update a previously built statement to use the identity columns in the database
	 * @param prepared the prepared statement to update
	 * @param index the index of the placeholders to begin to update
	 */
	final private void prepareIdentityClause(PreparedStringStatement prepared, int index)
		throws SQLException
	{
		int identityCount = identityFields.size();
		for (int i = 0; i < identityCount; i++)
		{
			String identityName = (String) identityFields.elementAt(i);
			// Don't forget!!! JDBC indices are 1-based!!!
			index++;
			prepared.m_pstmt.setObject(index, rs.getObject(identityName));
		}
	}

	public void open(int openType) throws Exception
	{
		open(openType, 0, Application.COMMIT_LOCK_LEVEL_NONE);
	}
	public void open(int openType, int blockFactor, int commit) throws Exception
	{
		//if (rs == null)
		if (!opened)
		{
			checkOpen();
			// Oracle doesn't allow updatable SELECT *, so use xxx.* instead
			sql =
				buildSelect().append(" FROM ").append(actualFileName).append(" xxx ").append(
				 queryExpression).toString();
			concurCommit(openType, blockFactor, commit, null);
		}
	}
	protected boolean positionReadp() throws Exception
	{
		m_reade=' ';
		boolean isFound;
		if (direction != 'F')
		{
			// Open the result set if it has not already been opened.
			openResultSet();
			isFound = previous();
			bof = !isFound;
			eof = false;
		}
		else
			//found=!rs.isBeforeFirst();
			isFound = !bof;
		direction = 'B';
		setI2Eof(bof);
		return isFound;
	}
	public boolean read()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		if (!readCycle())
			return true;
		return readx();
	}
	public boolean read(RecordJDBC r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return read();
	}
	protected boolean readCycle() throws Exception
	{
		m_reade=' ';
		// Open the result set if it has not already been opened.
		openResultSet();
		boolean isFound;
		if (direction == 'F' || direction == 'C')
		{
			isFound = next();
			eof = !isFound;
			bof = false;
		}
		else
			//found=!rs.isAfterLast();
			isFound = !eof;
		direction = 'F';
		setI2Eof(eof);
		return isFound;
	}
	/**
	 * Read a record with no record lock
	 */
	public boolean readn()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		return read();
	}
	public boolean readn(RecordJDBC r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readn();
	}
	public boolean readp()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		if (!positionReadp())
			return true;
		return readx();
	}
	public boolean readp(RecordJDBC r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readp();
	}
	/**
	 * Read the previous record with no record lock
	 */
	public boolean readpn()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		return readp();
	}
	public boolean readpn(RecordJDBC r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readpn();
	}
	protected boolean readx() throws Exception
	{
		irecord.input();
		return false;
	}
	
	/** Read the next undeleted record.  In a TYPE_SCROLL_SENSITIVE result set, changes from other processes
	 * might delete the record after it has already been read.
	 */
	protected boolean next() throws Exception
	{
		/*
		boolean found;
		do 
		{
			found = rs.next();				
		} while(found && rs.rowDeleted());
		return found;
		*/
		return rs.next();
	}
	
	/** Read the previous record.  In a TYPE_SCROLL_SENSITIVE result set, changes from other processes
	 * might delete the record after it has already been read.
	 */
	protected boolean previous() throws Exception
	{
		return rs.previous();
	}


	/**
	 * Refresh a result set if an UPDATE/DELETE/INSERT was not visible
	 * @version 2/13/2003 1:34:14 PM
	 */
	private void refreshResultSet() throws Exception
	{
		//int row=rs.getRow();
		// Just pray that the server-side cursor with fetchSize=1 works ok
		/*
		if (openType==Application.READ_WRITE)
		{
			rs.close();
			rs = stmt.executeQuery(sql);
			//rs.absolute(row);
			direction = 'F';
			bof = true;
			eof = false;
		}
		*/
	}
	public void rolbk() throws SQLException
	{
		conn.rollback();
	}
	public static void rolbk(Connection connection) throws SQLException
	{
		connection.rollback();
	}
	/**
	 * Set the 'open' file name.
	 */
	public void setFileName(String lfileName) throws Exception
	{
		/*
		int i = lfileName.indexOf('/');
		if (i > 0)
			lfileName = lfileName.substring(0, i) + '.' + lfileName.substring(i + 1);
		*/
		try
		{
			m_qfilename= rconn.qfileName(lfileName);
			lfileName = m_qfilename.schemaName + m_qfilename.fileName;
		}
		finally
		{
			super.setFileName(lfileName);
		}
	}
	void setFormat(IRecord r) throws Exception
	{
		/*
		fixed savIN=null;
		if (irecord==null)
		{
			if (conn instanceof I2Connection)
			{
				savIN = new fixed(99);
				savIN.assign(((I2Connection)conn).app.IN);
			}
			
		}
		*/
		boolean firstSet=(irecord==null);
		setRecord(r);
		((RecordJDBC)r).file = this;
		// If this is the first setFormat(), then build field list now so that field values don't get reset by the
		// input method at a later time
		if (firstSet)
			buildSelect();
		/*
		// Reset IN
		if (savIN!=null)
			((I2Connection)conn).app.IN.assign(savIN);
		*/
	}
	/** Position to a specific record number. */
	public boolean setgt(RecordJDBC r, int row) throws Exception
	{
		return setLowerLimit(r, row + 1);
	}
	/** Position past a specific record number. */
	public boolean setgt(RecordJDBC record, FigConstNum row) throws Exception
	{
		int rowInt = row.intValue();
		return setgt(record, rowInt);
	}

	/** Position to a specific record number. */
	public boolean setll(RecordJDBC r, int row) throws Exception
	{
		boolean equal = setLowerLimit(r, row);
		setI2Equal(equal);
		return equal;
	}
	/** Position to a specific record number. */
	protected boolean setLowerLimit(RecordJDBC r, int row) throws Exception
	{
		setRecordFormat(r);
		/*
		if (row > lastRecord)
		{
			if (!eof)
			{
				rs.afterLast();
				eof = true;
			}
			bof = false;
			direction = 'F';
			found=false;
		}*/
		m_reade=' ';
		boolean equal=false;
		// Open the result set if it has not already been opened.
		openResultSet();
		// Position to the beginning of the file
		if (row <= 0)
		{
			if (!bof)
			{
				rs.beforeFirst();
				bof = true;
			}
			eof = false;
			direction = 'B';
		}
		// Try to position to the specified record number.  If an exception is thrown assume that we have tried to 
		// move past the end of the file.
		else
		{
			try
			{
				rs.absolute(row);
				direction = 'B';
				equal = true;
				bof=false;
				eof=false;
			}
			// An exception was thrown, so move to the end of the file
			catch (SQLException e)
			{
				if (!eof)
				{
					rs.afterLast();
					eof = true;
				}
				bof = false;
				direction = 'F';
				equal=false;
			}
		}
		setI2Found(!eof);
		return equal;
	}
	/** Position to a specific record number. */
	public boolean setll(RecordJDBC record, FigConstNum row) throws Exception
	{
		int rowInt = row.intValue();
		return setll(record, rowInt);
	}
	public void setRecordFormat(RecordJDBC r)
		throws Exception
	{
		setFormat(r);
	}
	/** Remove the record lock from the current record. */
	public void unlock() 
	{
		//TODO We can do this by just updating the record with current values???
		/*
		if (_updatable && !bof && !eof)
		{
			try
			{
				rs.cancelRowUpdates();
				rs.updateRow();
			}
			catch (Exception e) {}
		}
		*/
	}
	public void update()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		RecordJDBC dbRecord = (RecordJDBC)irecord;
		dbRecord.output();
		// If the recordset is updatable, just update directly (this will essentially unlock the record if there are no fields
		if (_updatable)
			rs.updateRow();
		// If the cursor is not updatable (no driver than I've found supports it), then update the record
		// with a separate UPDATE ... SET ... WHERE CURRENT OF ... clause
		// Don't update the record if no fields have been changed
		else if (dbRecord.updateCount>0)

		{
			{
				//int updateCount = dbRecord.updateFields.size();
				StringBuffer sqlUpdate =
					new StringBuffer("UPDATE " + actualFileName + correlation);
				String comma = " SET ";
				for (int i = 0; i < dbRecord.updateCount; i++)
				{
					// instead of using the string values to update the record, use place holders and update
					// the values using setObject() through a PreparedStatement
					//+ dbRecord.updateValues.elementAt(i));
					
					// Change to do individual appends to avoid String concatenation
					//sqlUpdate.append(comma + dbRecord.updateFields.elementAt(i) + "=?");
					sqlUpdate.append(comma);
					// Add delimiters around field names
					sqlUpdate.append('"').append(dbRecord.updateFields.elementAt(i)).append("\"=?");
					comma = ",";
				}
				// Ackk, pathetic but WHERE CURRENT OF actually tries to use the rs cursor to do the update, which is read only,
				// which is why we're here in the first place.  Can't use key/column values to do update because there is no
				// guarantee of the uniqueness of those values.  Punt and use ROWID because there's nothing better, and because
				// Oracle and DB2 (?) both seem to support it
				//sqlUpdate.append(" WHERE CURRENT OF " + rs.getCursorName());
	
				// Use identity list to determine the best column identifier to do the update			
				//sqlUpdate.append(" WHERE " + rowid + "=" + rowidQuote + rs.getString("rowid") + rowidQuote);
				sqlUpdate = getIdentityClause(sqlUpdate);
	
				/*
				if (!rsRowid.first())
					sqlUpdate.append(" WHERE ROWID='" + rs.getString("ROWID") + "'");
				// If it's supported, the getBestRowIdentifier() should return a list of field names to test.  Nothing does...
				else
				{
					rsRowid.next();
					comma = " WHERE ";
					do {
						String rowField = rsRowid.getString(2);
						short digits = rsRowid.getShort(6);
						char quote=' ';
						if (digits<0)
							quote='\'';
						sqlUpdate.append(comma + rowField + '=' + quote + rs.getString(rowField) + quote);
						comma = ",";
					} while (rsRowid.next());
				}
				*/
				// Instead of executing the SQL statement directly, loop through and update the placeholder values
				//stmtWork.executeUpdate(sqlUpdate.toString());
				// Reuse the prepared statement previously build if the SQL strings are equivalent
				// (this should always be the case for I2 update()
				String sqlUpdateString = sqlUpdate.toString();
				if (preparedUpdate!=null && preparedUpdate.m_sql.compareTo(sqlUpdateString)!=0)
				{	
					preparedUpdate.m_pstmt.close();
					preparedUpdate = null;
				}
				if (preparedUpdate==null)
					preparedUpdate = new PreparedStringStatement(conn, sqlUpdateString);
				setUpdateParameters(preparedUpdate, dbRecord.updateCount);
				prepareIdentityClause(preparedUpdate, dbRecord.updateCount);
				preparedUpdate.m_pstmt.executeUpdate();
				
			}
		}
		// If a change was made, update result set if updates are not visible
		if (dbRecord.updateCount>0)
		{
			if (!updatesAreVisible)
				refreshResultSet();
			dbRecord.clearUpdates();
		}
		//return false;
	}
	
	// Set the parameters of the prepared statement used by an update 
	private void setUpdateParameters(PreparedStringStatement prepared, int prepareCount) throws SQLException
	{
		// Don't forget!!! JDBC indices are 1-based!!! (i+1)
		RecordJDBC dbRecord = (RecordJDBC)irecord;
		for (int i=0; i < prepareCount; i++)
		{
			try
			{
				prepared.m_pstmt.setObject(i+1, dbRecord.updateValues.elementAt(i));
			}
			catch (SQLException e)
			{
				I2Logger.logger.severe("Error occurred updating " + dbRecord.updateFields.elementAt(i) +  " of " + actualFileName +
				 " with value " + dbRecord.updateValues.elementAt(i));
				 throw e;
			}
		}
	}
	
	public void update(RecordJDBC r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		update();
	}
	public boolean write()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		RecordJDBC dbRecord = (RecordJDBC)irecord;
		// If the cursor is updatable, or if this is a driver (e.g. OS/400) that allows writes through read-only
		// result sets, then do internal write.
		if (_updatable || readOnlyWrite)
		{
			// Open the result set if it has not already been opened.
			openResultSet();
			boolean sav_updatable = _updatable;
			_updatable = true;
			rs.moveToInsertRow();
			dbRecord.output();
			// If this is an empty EXCPT, then just unlock the record
			if (dbRecord.updateCount>0)
				rs.insertRow();
			else
				unlock();
			rs.moveToCurrentRow();
			_updatable = sav_updatable;
		}
		else
		{
			dbRecord.output();
			// This may be an empty EXCPT to unlock the record, so don't do if updateCount==0
			if (dbRecord.updateCount!=0)
			{
				StringBuffer sqlInsert =
					new StringBuffer("INSERT INTO " + actualFileName + " (");
				// Build field name list
				String comma = "";
				int insertCount = dbRecord.updateFields.size();
				for (int i = 0; i < insertCount; i++)
				{
					sqlInsert.append(comma).append(dbRecord.updateFields.elementAt(i));
					comma = ",";
				}
				// Build vlues list
				comma = ") VALUES(";
				for (int i = 0; i < insertCount; i++)
				{
					// instead of using the string values to update the record, use place holders and update
					// the values using setObject() through a PreparedStatement
					//sqlInsert.append(comma + dbRecord.updateValues.elementAt(i));
					sqlInsert.append(comma).append("?");
					comma = ",";
				}
				sqlInsert.append(")");
				// Instead of executing the SQL statement directly, loop through and update the placeholder values
				//stmtWork.executeUpdate(sqlInsert.toString());
				// Reuse the prepared statement previously build if the SQL strings are equivalent
				// (this should always be the case for I2 write()
				String sqlInsertString = sqlInsert.toString();
				if (preparedInsert!=null && preparedInsert.m_sql.compareTo(sqlInsertString)!=0)
				{
					preparedInsert.m_pstmt.close();
					preparedInsert=null;
				}
				if (preparedInsert==null)
					preparedInsert = new PreparedStringStatement(conn, sqlInsertString);
				setUpdateParameters(preparedInsert, insertCount);
				preparedInsert.m_pstmt.executeUpdate();
				// If inserts are not visible, then we have to refresh the result set
			}
		}
		// If a change was made, update result set if writes are not visible
		if (dbRecord.updateCount>0)
		{
			if (!insertsAreVisible)
				refreshResultSet();
			dbRecord.clearUpdates();
		}
		//lastRecord++;
		return false;
	}
	public void write(RecordJDBC r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		write();
	}
	/**
	 * Set the query selection that criteria for the file.
	 * @version 2/19/2003
	 * @param fieldList java.lang.String
	 */
	public void setQueryExpression(String qryExpression)
	{
		queryExpression = qryExpression;
	}
}
