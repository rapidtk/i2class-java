package com.i2class;

import java.util.*;
import java.math.BigDecimal;
import java.sql.*;

import com.ibm.as400.access.AS400JDBCDriver;

/**
 * A database file class for keyed JDBC access that uses a binary search to find a certain key.
 * @author Andrew Clark
 */
public class RfileSearch extends RfileKeyedJDBC
{
	private int _seekDiff;
	
	private static final int CACHED_KEY_COUNT=64;
	private static final int CACHED_BLOCK_SIZE=128;
	private int maxCachedKeys;
	private TreeMap cachedKeys = new TreeMap();
	// This is used to communicate the first key back from checkKey
	private Comparable _checkKey[];
	private ComparableArray _cacheckKey;
	private ComparableArray _seekKey; // = new ComparableArray(true);
	private int _absoluteRow;
	private int _start, _end;
	private Comparable _firstKey;
	private int lastRecord;
	private PreparedStatement preparedCount;
	/**
	 * Construct a keyed file where the index name (LF) is different than the table (PF) that data is 
	 * actually selected from.  This is important for SQL INDEX files since JDBC does not allow data to be
	 * selected from them.
	 * @param system The system that the specified files reside on.
	 * @param indexName The name of the index that contains the key to use for this file. 
	 * @param tableName The name of the table that contains the actual data.
	 */
	public RfileSearch(java.sql.Connection system, String indexName, String tableName)
	{
		super(system, tableName);
	}
	public RfileSearch(java.sql.Connection system, String lfileName)
	{
		super(system, lfileName);
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
		_checkKey[0]=null;
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
			_checkKey[i] = checkKeyi;
			// If we are saving the entire key, then diff may already be set
			if (i<keyLength && diff==0)
				diff = checkKeyi.compareTo(key[i]);
		}
		return diff;
	}

	/**
	 * Get Evaluate the record count for this file.
	 */
	private void refreshRecordCount() throws SQLException 
	{
		ResultSet rsCount = preparedCount.executeQuery();
		/*
		String sqlOpen = "SELECT COUNT(*) FROM " + fileName;
		rs = conn.createStatement().executeQuery(sqlOpen);
		*/
		rsCount.next();
		lastRecord = rsCount.getInt(1);
		rsCount.close();
	}
	

	
	/**Return the cached key object for this index.*/
	public AbstractMap getKeyCache()
	{
		return cachedKeys;
	}
	
	/** 
	 * Preload the key cache with the specified number of values.  
	 * @param count the number of keys to preload.  
	 * Specify 0 to preload no records.
	 * Specify a negative value to load a percentage of the records (for example -50=50%).  
	 */
	public void loadKeyCache(int count) throws SQLException
	{
		if (count != 0)
		{
			// Read from the file and cache the record key value.  Space the reads so that the cache
			// values are equadistant.
			int blockSize=0;
			// If the count is less than 0, then this is a percentage of records to read.
			if (count<0)
			{
				// Anything < -100 is all records.
				if (count>=-100)
					blockSize = (int)(((double)-count/100)*lastRecord);
			}
			else //if (count>0)
				blockSize = lastRecord/count;
			if (blockSize <= 0)
				blockSize = 1;
			bof=false;
			eof=false;
			try
			{
				int rrn=1;
				while (rrn<=lastRecord)
				{
					rs.absolute(rrn);
					// 'Trick' cache into being build by checking current record (we don't care about the return value)
					checkKey(((RecordJDBC)irecord).key, 0, true);
					cachedKeys.put(new ComparableArray(_checkKey), numeric.newInteger(rrn));
					rrn += blockSize;
				} 
			}
			catch (Exception e) {}
			finally
			{
				rs.beforeFirst();
				bof=true;
			}
		}
	}
	
	public void open(int openType, int blockFactor, int commit) throws Exception
	{
		// Only open the result set once
		if (rs == null)
		{
			super.open(openType, blockFactor, commit);
			// Create key array
			_checkKey = new Comparable[maxKeyCount];
			// If this is a keyed open, then we need to get the last record number in the result set
			preparedCount = conn.prepareStatement("SELECT COUNT(*) FROM " + actualFileName);
			refreshRecordCount();
			// Calculate the maximum size of the key cache
			// It seems reasonable to always cache a certain number of values (CACHED_KEY_COUNT=32), 
			// but for larger files that limit should increase so that the maximum block of records
			// actually searched is no larger than CACHED_BLOCK_SIZE=1024
			if (maxCachedKeys==0)
				maxCachedKeys = CACHED_KEY_COUNT + lastRecord/CACHED_BLOCK_SIZE;
			// Preload key cache if Application.cachedKeySize is set
			//loadKeyCache(Application.getKeyCacheSize());
		}
		bof=true;
		eof=false;
	}
	/* (non-Javadoc)
	 * @see com.asc.rio.RkeyedJDBC#set_checkKey(int, java.lang.Comparable)
	 */
	protected void set_checkKey(int i, Comparable checkKeyi) {
		_checkKey[i]=checkKeyi;
	}

	// Position to the seek record and cache key value
	private int seekPosition(Object key[], int start, int end) throws SQLException
	{
		int rowDiff = end-start;
		_absoluteRow = start + rowDiff / 2;
		rs.absolute(_absoluteRow);
		//_checkKey = new Comparable[keyCount];
		boolean allKeys = (rowDiff<CACHED_BLOCK_SIZE || cachedKeys.size() < maxCachedKeys);
		int diff = checkKey(key, key.length, allKeys);
		// Insert the key value into the key cache so that it can be used later
		if (allKeys && _checkKey[0] != null)
		{
			/*
			for (int i=key.length; i<keyCount; i++)
			{
				String keyName = dbRecord.getKeyName(i);
				_checkKey[i] = (Comparable)rs.getObject(keyName);
			}
			*/
			_cacheckKey = new ComparableArray(_checkKey);
			if (!cachedKeys.containsKey(_cacheckKey))
				cachedKeys.put(_cacheckKey, numeric.newInteger(_absoluteRow));
		}
		return diff;
	}

	// Do binary search to find key in result set
	// seekAfter() is used by SETGT.
	private int seekAfter(Object key[], int start, int end)
		throws Exception
	{
		// No exact match has been found, but we are 'greater' than the key that we are looking for
		int diff;
		if (start>end)
		{
			// If we are not eof, then we are the correct record - return
			if (start <= lastRecord)
				return 0;
			// If we are at end of file, then fall through here so that the cursor gets positioned after the 
			// end of the file
			diff=0;
		}
		else
			diff = seekPosition(key, start, end);
		// A match has been found.  But, for SETGT we need to find the record after the LAST unique key value.
		// Check by going forward one record to see if it is not a match
		if (diff == 0)
		{
			boolean isFound = next();
			if (isFound)
				diff = checkKey(key, key.length, false);
			eof = !isFound;
			if (eof || diff != 0)
				return 0;
		}
		if (diff > 0)
			return seekAfter(key, start, _absoluteRow - 1);
		return seekAfter(key, _absoluteRow + 1, end);
	}

	// Do binary search to find key in result set.
	// seekBefore() is used by SETLL and CHAIN.
	private int seekBefore(Object key[], int start, int end) throws Exception
	{
		// No exact match has been found, but we are 'greater' than the key that we are looking for
		if (start>end)
		{
			eof = start > lastRecord;
			// Position past the end of the file
			if (eof)
			// At this point eof should always be true.  But, if another thread has updated the data in this file then
			// there may be more records 
				eof = !next();
			return _seekDiff;
		}
		_seekDiff = seekPosition(key, start, end);
		// A match has been found.  But, for SETLL and CHAIN we need to find the FIRST unique key value.
		// Check by going back one record to see if the previous record is not a match.
		if (_seekDiff == 0)
		{
			boolean isFound = rs.previous();
			if (isFound)
				_seekDiff = checkKey(key, key.length, false);
			// This is an exact match.  Replace the key with a negative position value to flag this
			if (!isFound || _seekDiff != 0)
			{
				cachedKeys.put(_cacheckKey, new Integer(-_absoluteRow)); // Always minus, so numeric.newInteger() won't help
				next();
				eof = false;
				return 0;
			}
		}
		if (_seekDiff < 0)
			return seekBefore(key, _absoluteRow + 1, end);
		return seekBefore(key, start, _absoluteRow - 1);
	}

	// Do binary search to find key in result set
	private int seekKey(Object[] key, boolean seekBefore)
		throws Exception
	{
		int offset;
		while (true)
		{
			_seekDiff = 1;
			bof = false;
			eof = false;
			// If this key is the same as the last key, then we already know what record to position to
			// But, don't use it if the last seek returned bof (_absoluteRow==0) or eof (_absoluteRow>lastRecord)
			if (_seekKey != null && _absoluteRow>0 && _absoluteRow<=lastRecord && 
			 _seekKey.compareTo(key, key.length)==0)
			{
				_start=_absoluteRow;
				_end = _start;
				if (_end<lastRecord)
					_end++;
			}
			else
			{
				_start=1;
				_end = lastRecord;
				//_seekKey.array = key;
				_seekKey= new ComparableArray(key);
				// Try to find a match in the key cache for this value
				if (!cachedKeys.isEmpty())
				{
					Integer position=null;
					// First check to see if an exact match exists.
					//ComparableArray cakey = new ComparableArray(key);
					//_seekKey.length = key.length; // Flag so that partial match is ok
					// More than one key can exist for a specified key, so we can't assume this is an exact match 
					// unless the file is unique
					position = (Integer)cachedKeys.get(_seekKey);
					if (position != null)
					{
						int iposition = position.intValue();
						// This is an exact match.  If we are doing a seekBefore, then we can return
						if (iposition < 0 && seekBefore)
						{
							_absoluteRow = -iposition;
							_start = _absoluteRow;
							_end = _start;
							rs.absolute(_absoluteRow);
							return 0;
						}
					}
					// If there is no exact match, check for the closest match that is 'less than' the key
					SortedMap headMap, tailMap;
					headMap = cachedKeys.headMap(_seekKey);
					// We can make cakey a successor by setting the length element larger than the acutal array
					// This allows us to get the > elements instead of >=
					if (position != null)
						_seekKey.length++;
					tailMap = cachedKeys.tailMap(_seekKey);
					// If the head map is empty, then no keys exist in the map that are 'less than' the current key
					// Find the next largest key value and search from the start to that value.
					// The record that we are looking for is somewhere between this record and the next (higher) cached value
					if (!headMap.isEmpty())
						_start = Math.abs(((Integer)headMap.get(headMap.lastKey())).intValue());
					if (!tailMap.isEmpty())
						_end = Math.abs(((Integer)tailMap.get(tailMap.firstKey())).intValue());
				}
			}
			// Set the fetch size to 1 before we do this so that the smallest possible block is returned...
			//rs.setFetchSize(1);
			offset=0;
			_absoluteRow = 0;
			if (seekBefore)
			{
				offset = seekBefore(key, _start, _end);
				//found = (offset == 0);
				//setI2Found(found);
			}
			else
				seekAfter(key, _start, _end);
			// If this value was cached and the offset is !=0, then changes (delete/insert/updates) from other
			// processes have invalidated this key cached.  Flush it and try again 
			if (offset==0 || (offset>0 && (_absoluteRow>_start || _absoluteRow<=1)) 
			              || (offset<0 && (_absoluteRow<_end || eof)))
				break;
			_seekKey=null;
			cachedKeys.clear();
			refreshRecordCount();
		} // while true
		// Set back fetch size
		//rs.setFetchSize(0);
		// For whatever reason %EOF with no file name is not changed
		//Application.eof = (bof | eof);
		return offset;
	}
	
	public boolean setgt() throws Exception
	{
		// If we find the exact record, do READEs until we position past it
		seekKey(((RecordJDBC)irecord).getKey(), false);
		direction = 'B';
		return eof;
	}
	protected boolean setLowerLimit() throws Exception
	{
		direction = 'B';
		int i = seekKey(((RecordJDBC)irecord).getKey(), true);
		// Since this is a SETLL, we need to position >= record, so go to next if the result is <0
		if (i < 0)
			next();
		// If i==0, then an exact match was found
		return i == 0;
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
