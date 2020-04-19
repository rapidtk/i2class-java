package com.i2class;

import java.util.*;
import java.math.BigDecimal;
import java.sql.*;

import com.ibm.as400.access.AS400JDBCDriver;

/**
 * A database file class for keyed JDBC access that uses an SQL index to approximate positioning.
 * 
 */
public class RindexJDBC extends RkeyedJDBC
{
	/** Prepared statements mapped to the SQL strings from preparedStrings[][] */
	private HashMap preparedStatements = new HashMap();
	/** SQL statements used by Connection.prepareStatement() */
	private String preparedStrings[][];
	/**  The current and last index into the prepared string array. */
	private int pindex, pkeyCount;

	public RindexJDBC(java.sql.Connection system, String indexName, String tableName)
	{
		super(system, tableName);
	}
	public RindexJDBC(java.sql.Connection system, String lfileName)
	{
		super(system, lfileName);
	}

	// Create prepared strings.
	// There will always be maxKeyCount*2+1 statements.
	// The 'middle-most' request is that statement that is equivalent to a CHAIN.
	// middle+1 is the statement that is equivalent to a SETGT.
	// As you move away from the middle-most request, the selection is more restrictive
	// For example:
	// 1 key: 3 statements
	//   <key1
	//   =key1
	//   >key2
	// 2 keys: 5 statements
	//   <key1
	//   =key1, <key2
	//   =key1, =key2
	//   =key1, >key2
	//   >key1
	// 3 keys: 7 statements
	//   <key1
	//   =key1, <key2
	//   =key1, =key2, <key3
	//   =key1, =key2, =key3
	//   =key1, =key2, >key3
	//   =key1, >key2
	//   >key1
	
	// etc...
	// This is implemented as a two-dimensional array.  The first index is the number of keys, the second index is the 
	// index to the statement itself.  Notice how the 'leaf' strings are 'shared', and only the 'middle' statements
	// for each key length is unique.
	private void createPreparedStrings()
	{
		if (preparedStrings==null)
		{
			//int maxKeyCount = dbRecord.keyNames.size();
			preparedStrings = new String[maxKeyCount][];
			for (int i=0; i<maxKeyCount; i++)
				preparedStrings[i] = new String[(i+1)*2+1];
			// Build a prepared statement to return only those records that meet chain() request
			StringBuffer whereBuf = new StringBuffer();
			// Build WHERE portion of selection
			String and = "WHERE ";
			int o = sql.indexOf(" ORDER BY ");
			String sqlSelectFrom = sql.substring(0, o);
			String sqlOrderBy = sql.substring(o);
			RecordJDBC record = (RecordJDBC)irecord;
			for (int i = 0; i < maxKeyCount; i++)
			{
				// Build < condition first
				//whereBuf.append(and + ((RecordJDBC)irecord).getKeyName(i) + "<?");
				whereBuf.append(and);
				String keyName = record.getKeyName(i);
				String keyType = record.getKeyType(i);
				/* For absolute keys, wrap where condition with ABS() function
				  ...errr, ABS not supported in ORDER BY... 
				boolean absolute = keyType.endsWith("ABS"); 
				if (absolute)
					whereBuf.append("ABS(");
				*/
				// Add delimiters around field name
				whereBuf.append('"').append(keyName).append('"');
				/*
				if (absolute)
					whereBuf.append(')');
				*/

				// For descending keys, the comparison is opposite
				char gtOp, ltOp;
				if (keyType.startsWith("DESC"))
				{
					gtOp='<';
					ltOp='>';
				}
				else
				{
					gtOp='>';
					ltOp='<';
				}
				// Create <? clause (for descending keys, the comparison is opposite)
				whereBuf.append(ltOp).append('?');
				
				String sqlKey = sqlSelectFrom + whereBuf.toString() + sqlOrderBy;
				for (int j=i; j<maxKeyCount; j++)
					preparedStrings[j][i] = sqlKey; 
				// Build > condition 
				// by just replacing '<' with '>' (for descending keys, the comparison is opposite)
				int opPosition = whereBuf.length()-2;
				whereBuf.setCharAt(opPosition, gtOp);
				
				sqlKey = sqlSelectFrom + whereBuf.toString() + sqlOrderBy;
				for (int j=i; j<maxKeyCount; j++)
					preparedStrings[j][(j+1)*2-i] = sqlKey; 
				// Set up = selection for next loop 
				whereBuf.setCharAt(opPosition, '=');
				preparedStrings[i][i+1] = sqlSelectFrom + whereBuf.toString() + sqlOrderBy;
				and = " AND ";
			}
		}
	}

	// Open the ResultSet corresponding to the prepared statement at the specified index (pindex).
	// If it doesn't exist yet, then create (prepare) it.
	// pindex is usually the 'middle-most' index for CHAIN, middle+1 for SETGT 
	private void openPreparedStatement() throws Exception
	{
		// If no keys have been set for this record format than exit (more) gracefully.
		RecordJDBC dbRecord = (RecordJDBC)irecord;
		if (dbRecord.keyCount<=0)
			throw new Exception("No key value set for record " + dbRecord.recordName + " of " + actualFileName);
		ResultSet rs = null;
		// If the SQL statement hasn't been set yet, then this is an error
		if (sql!=null)
		{
			createPreparedStrings();
			//String ps = preparedStrings[dbRecord.keyCount-1][pindex];
			String ps = preparedStrings[pkeyCount-1][pindex];
			PreparedStatement p = (PreparedStatement)preparedStatements.get(ps);
			// Some containers may close the statement object while they are still in use -- check to make sure they are not
			if (p != null)
			{
				try
				{
					p.getMaxFieldSize(); // Any getXXX method should be fine here...
				}
				catch (SQLException e)
				{
					p=null;
				}
			}
			if (p==null)
			{
				p = conn.prepareStatement(ps, ResultSet.TYPE_SCROLL_SENSITIVE, concur);
				preparedStatements.put(ps, p);
			} 
			// Set the variables for the statement
			// The 'more-outer' statements may have less keys than the actual key itself 
			//int setCount = dbRecord.keyCount;
			int setCount = pkeyCount;
			int diff=java.lang.Math.abs(pindex-setCount);
			if (diff>1)
				setCount -= (diff-1);
			for (int i=0; i<setCount; i++)
				p.setObject(i+1, dbRecord.key[i]);
			// Close old result set
			if (rs!=null)
				rs.close();
			//return p;
			rs = p.executeQuery();
		}
		openNewResultSet(rs);
	}

	// SETGT can't really be simulated with an SQL statement because no WHERE statement can accurately simulate
	// the request (the key has to be treated as one contiguous value -- trust me I've tried it and it can't be done).
	// The best way to do setgt seems to be to create a series of results, each less restrictive than the last.
	// For each key that is set, create a '=' relationship for all keys except the last, for which we 
	// create a '>' relationship.  Once all records in that set are exhausted, repeat the process but use
	// one less key value.
	// For example ABSTRACT/FILFLDL4 is keyed by WHFILE, WHNAME, WHFLDE.  
	// SETGT(WHFILE, WHNAME, WHFLDE) would get turned into 3 requests:
	//    WHERE WHFILE=? AND WHNAME=? AND WHFLDE>?
	//    WHERE WHFILE=? AND WHNAME>?
	//    WHERE WHFILE>?
	// The first request likely returns the smallest number of records and the last returns the largest number of records.
	// See createPreparedStrings(), above 
	public boolean setgt() throws Exception
	{
		direction = 'B';
		// If we have just finished doing a READE with the same key as this one, then there is no need to reposition
		if (m_reade=='>')
		{
			RecordJDBC dbRecord = (RecordJDBC)irecord;
			if (dbRecord.keyCount == m_readeKey.length)
			{
				boolean matchingKey=true;
				for (int i=0; i<dbRecord.keyCount; i++)
				{
					if (((Comparable)dbRecord.key[i]).compareTo(m_readeKey[i])!=0)
					{
						matchingKey=false;
						break;
					}
				}
				if (matchingKey)
				{
					eof=false;
					return false;
				}
			}
		}
			
		// Use the middle+1 (>) index
		//pindex = ((RecordJDBC)irecord).keyCount+1;
		pindex = pkeyCount()+1;
		//rs = getPreparedStatement().executeQuery();
		openPreparedStatement();
		setI2Found(next());
		return !found;
	}
	

	protected boolean setLowerLimit() throws Exception
	{
		direction = 'B';
		m_reade=' ';
		// Use the middle-most (=) index
		//pindex = ((RecordJDBC)irecord).keyCount;
		pindex = pkeyCount();
		//rs = getPreparedStatement().executeQuery();
		openPreparedStatement();
		// true if exact record found
		boolean equal=rs.next();
		if (equal)
		{
			bof=false;
			eof=false;
		}
		// If an exact match is not found, see if any record matches request (i.e. are we at eof)
		else
			next();
		setI2Found(!eof);
		return equal;
	}

	/**
	 * Return the 'next' logical record.
	 * Since there are multiple statements involved in each request, move to the 'next' statement once all of 
	 * the results from the current set are exhausted. 
	 */
	protected boolean next() throws Exception
	{
		boolean foundNext = rs.next();
		// If no keyed access has been done, then just use 'default' result set
		if (preparedStrings!=null)
		{
			//int lastStatement = ((RecordJDBC)irecord).keyCount*2;
			//int lastStatement = ((RecordJDBC)irecord).keyCount*2;
			while (!foundNext && pindex < pkeyCount*2)
			{
				pindex++;
				openPreparedStatement();
				foundNext = rs.next();
			}
		}
		bof = false;
		eof = !foundNext;
		return foundNext;
	}
	
	/**
	 * Return the 'previous' logical record.
	 * Since there are multiple statements involved in each request, move to the 'previous' statement once all of 
	 * the results from the current set are exhausted. 
	 */
	protected boolean previous() throws Exception
	{
		boolean found = rs.previous();
		while (!found && pindex > 0)
		{
			pindex--;
			openPreparedStatement();
			rs.afterLast();
			found = rs.previous();
		}
		bof = !found;
		eof = false;
		return found;
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.RfileJDBC#close()
	 */
	public void close() throws SQLException {
		super.close();
		// Loop through and close any of the prepared statements
		Iterator it = preparedStatements.values().iterator();
		while (it.hasNext())
		{
			PreparedStatement ps = (PreparedStatement)it.next();
			ps.close();
		}
		preparedStatements.clear();
	}
	
	/** Return the current key count, and set the upper-most limit of pstatements */
	private int pkeyCount()
	{
		pkeyCount = ((RecordJDBC)irecord).keyCount;
		return pkeyCount;
	}
}

