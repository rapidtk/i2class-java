package com.i2class;

import com.ibm.as400.access.*;
import java.io.IOException;

// Java doc inhertis from IDBFile

/**
 * An OS/400 based SQL table-like file class.
 * @see Rfile 
 * @author Andrew Clark 
 */
public class RfileDB400
	extends RfileCycle implements RfileKeyed /*extends com.ibm.as400.access.KeyedFile*/
{
	private Record400 dbRecord;
	private char direction = 'F';
	KeyedFile file;
	private com.ibm.as400.access.Record record;
	private I2AS400 rsystem;
	/** 
	 * Create a new file object that references a file on a remote OS/400 system.
	 * @param system The OS/400 system that the file resides on
	 * @param fileName The (optionally qualified) location (library/file) of the file.
	 */
	public RfileDB400(
		AS400 system,
		String fileName) //throws java.beans.PropertyVetoException
	{
		String path = Application.getPath(fileName);
		if (system instanceof I2AS400)
		{
			rsystem = (I2AS400)system;
			system = ((I2AS400)system).as400;
		}
		file = new KeyedFile(system, path);
	}
	
	/** Destroy the connection objects that this object references. */
	@Override
	protected void finalize() throws Throwable
	{
		super.finalize();
		rsystem=null;
	}


	/**
	 * CHAIN (position and read) to a specific record.  Use setKey() to set the key value.  Each record format will implement its own setKey method.
	 * @return eof: true if no record found, false if exact key match found.
	 */
	public boolean chain()
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		direction = 'C';
		record = file.read(dbRecord.getKey());
		return readx();
	}
	/**
	 * CHAIN (position and read) to a specific record of the specified format.
	 * @return eof: true if no record found, false if exact key match found.
	 */
	public boolean chain(Record400 r)
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		setRecordFormat(r);
		return chain();
	}
	/**
	 * Chain to a record without putting a record lock on it.
	 * @return eof: true if no record found, false if exact key match found.
	 */
	public boolean chainn()
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		file.setReadNoUpdate(true);
		boolean notFound = chain();
		file.setReadNoUpdate(false);
		return notFound;
	}
	/**
	 * Chain to a record of a specific format without putting a record lock on it.
	 * @return eof: true if no record found, false if exact key match found.
	 */
	public boolean chainn(Record400 r)
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		setRecordFormat(r);
		return chainn();
	}
	/**
	 * See if the thrown exception can be handled
	 * Creation date: (11/22/2002 4:16:31 PM)
	 * @exception com.ibm.as400.access.AS400Exception The exception description.
	 */
	void checkException(AS400Exception e) throws AS400Exception
	{
		if (e.toString().substring(0, 7).compareTo("CPF5025") != 0)
			throw e;
		record = null;
	}
	/**
	 * Close the file.
	 */
	@Override
	public void close()
		throws
			java.lang.InterruptedException,
			java.io.IOException,
			AS400SecurityException,
			AS400Exception
	{
		file.close();
		removeClosedFile();
	}
	/** 
	 * Commit any changes (updates/deletes) made to a file opened with commitment control.
	 * @see #open(int, int, int)
	 */
	public void commit()
		throws
			InterruptedException,
			AS400SecurityException,
			java.io.IOException,
			AS400Exception
	{
		AS400File.commit(file.getSystem());
	}
	/** 
	 * Commit any changes (updates/deletes) made to a file opened with commitment control.
	 * @see #open(int, int, int)
	 */
	public static void commit(AS400 as400)
		throws
			InterruptedException,
			AS400SecurityException,
			java.io.IOException,
			AS400Exception
	{
		AS400File.commit(as400);
	}
	/** Delete the current record from the file. */
	public boolean Delete() throws Exception
	{
		file.deleteCurrentRecord();
		return true;
	}
	/** Delete the current record of the specified format from the file. */
	public boolean Delete(Record400 r) throws Exception
	{
		setRecordFormat(r);
		return Delete();
	}
	/** Delete the record at the specified relative record number. */
	public boolean Delete(Record400 r, long rrn) throws Exception
	{
		setRecordFormat(r);
		//file.deleteRecord(rrn);
		return Delete();
	}
	/** Delete the record at the specified relative record number. */
	public boolean Delete(Record400 r, INumeric rrn) throws Exception
	{
		return Delete(r, rrn.longValue());
	}
	/**
	 * Open the file.
	 * @param openType How the file should be opened: READ_ONLY, READ_WRITE, WRITE_ONLY.
	 */
	@Override
	public void open(int openType)
		throws
			AS400Exception,
			AS400SecurityException,
			ConnectionDroppedException,
			InterruptedException,
			IOException,
			ServerStartupException
	{
		open(openType, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
	}
	public void open(int openType, int blockFactor, int commit)
		throws
			AS400Exception,
			AS400SecurityException,
			ConnectionDroppedException,
			InterruptedException,
			IOException,
			ServerStartupException
	{
		file.open(openType, blockFactor, commit);
		if (rsystem != null)
			app = rsystem.app;
		// Add this file to the list of open files so that they can be closed during the RETURN step
		addToOpenFiles();
		bof=true;
		eof=false;
	}
	/** 
	 * Read the next record from the file.
	 * @return eof
	 */
	public boolean read()
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		/*
		try
		{
		*/
		if (!eof)
		{
			readCycle();
			eof = readx();
		}
		/*
		}
		// Handle CPF5025 'Input operation past start/end of file
		catch (AS400Exception e)
		{
			checkException(e);
		}
		*/
		direction = 'F';
		return eof;
	}
	/** 
	 * Read the next record from the specified format of the file.
	 * @return eof
	 */
	public boolean read(Record400 r)
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return read();
	}
	/**
	 * Read from file but don't update input fields
	 */
	@Override
	boolean readCycle() throws Exception
	{
		boolean isFound = false;
		if (!eof)
		{
			if (direction == 'F' || direction == 'C')
				record = file.readNext();
			else
				record = file.read();
			dbRecord.record = record;
			isFound = (record != null);
		}
		setI2Eof(eof);
		return isFound;
	}
	/* Everything from this point on is considered 'keyed' access */
	/** 
	 * Read the next record if its key matches the values previously applied with setKey(). 
	 * @return eof true=no more records match, false=record returned successfully.
	 */
	public boolean reade()
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		if (direction == ' ')
		{
			eof = false;
			try
			{
				file.positionCursorToPrevious();
			}
			catch (Exception e)
			{
				file.positionCursorBeforeFirst();
			}
		}
		if (!eof)
		{
			record = file.readNextEqual(dbRecord.getKey());
			eof = readx();
		}
		direction = 'F';
		return eof;
	}
	public boolean reade(Record400 r)
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return reade();
	}
	/**
	 * Read the next equal record from the file without putting a record lock on it.
	 * @see #reade()
	 */
	public boolean readen()
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		file.setReadNoUpdate(true);
		boolean isEof = reade();
		file.setReadNoUpdate(false);
		return isEof;
	}
	public boolean readen(Record400 r)
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readen();
	}
	/** 
	 * Read the next record if its key matches the value of the current record. 
	 * @return eof true=no more records match, false=record returned successfully.
	 */
	public boolean readEqual()
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		if (!eof)
		{
			record = file.readNextEqual();
			eof = readx();
		}
		direction = 'F';
		return eof;
	}
	public boolean readEqual(Record400 r)
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readEqual();
	}
	/**
	 * Read the next equal record from the file without putting a record lock on it.
	 * @see #readEqual()
	 */
	public boolean readEqualn()
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		file.setReadNoUpdate(true);
		boolean isEof = readEqual();
		file.setReadNoUpdate(false);
		return isEof;
	}
	public boolean readEqualn(Record400 r)
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readEqualn();
	}
	/**
	 * Read the next record from the file without putting a record lock on it.
	 */
	public boolean readn()
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		file.setReadNoUpdate(true);
		boolean isEof = read();
		file.setReadNoUpdate(false);
		return isEof;
	}
	public boolean readn(Record400 r)
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readn();
	}
	/** 
	 * Read the previous record.
	 * @return bof
	 */
	public boolean readp()
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		if (!bof)
		{
			if (direction != 'F')
				record = file.readPrevious();
			else
				record = file.read();
			bof = readx();
		}
		direction = 'B';
		return bof;
	}
	public boolean readp(Record400 r)
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readp();
	}
	/** 
	 * Read Previous Equal - read the previous record if its key matches the values previously applied with setKey(). 
	 * @return bof true=no more records match, false=record returned successfully.
	 */
	public boolean readpe()
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		if (direction == 'F')
		{
			bof = false;
			try
			{
				file.positionCursorToNext();
			}
			catch (Exception e)
			{
				file.positionCursorAfterLast();
			}
		}
		if (!bof)
		{
			record = file.readPreviousEqual(dbRecord.getKey());
			bof = readx();
		}
		direction = 'B';
		return bof;
	}
	public boolean readpe(Record400 r)
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readpe();
	}
	/**
	 * Read the previous equal record from the file without putting a record lock on it.
	 * @see #readpe()
	 */
	public boolean readpen()
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		file.setReadNoUpdate(true);
		boolean isEof = readpe();
		file.setReadNoUpdate(false);
		return isEof;
	}
	public boolean readpen(Record400 r)
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readpen();
	}
	/** 
	 * Read Previous Equal - read the previous record if its key matches the value of the current record. 
	 * @return bof true=no more records match, false=record returned successfully.
	 */
	public boolean readpEqual()
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		if (!bof)
		{
			record = file.readPreviousEqual();
			bof = readx();
		}
		direction = 'B';
		return bof;
	}
	public boolean readpEqual(Record400 r)
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readpEqual();
	}
	/**
	 * Read the previous equal record from the file without putting a record lock on it.
	 * @see #readpEqual()
	 */
	public boolean readpEqualn()
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		file.setReadNoUpdate(true);
		boolean isEof = readpEqual();
		file.setReadNoUpdate(false);
		return isEof;
	}
	public boolean readpEqualn(Record400 r)
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readpEqualn();
	}
	/**
	 * Read the previous record from the file without putting a record lock on it.
	 */
	public boolean readpn()
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		file.setReadNoUpdate(true);
		boolean isEof = readp();
		file.setReadNoUpdate(false);
		return isEof;
	}
	public boolean readpn(Record400 r)
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return readpn();
	}
	@Override
	boolean readx() throws Exception
	{
		boolean isFound = (record != null);
		dbRecord.record = record;
		if (!isFound)
			return true;
		dbRecord.setBuffer();
		dbRecord.input();
		return false;
	}
	/** Rollback any changes (update/deletes) before they are committed. */
	public void rolbk()
		throws
			InterruptedException,
			AS400SecurityException,
			java.io.IOException,
			AS400Exception
	{
		AS400File.rollback(file.getSystem());
	}
	/** Rollback all changes to the specified connection before they are committed. */
	public static void rolbk(AS400 as400)
		throws
			InterruptedException,
			AS400SecurityException,
			java.io.IOException,
			AS400Exception
	{
		AS400File.rollback(as400);
	}
	@Override
	void setFormat(IRecord r) throws Exception
	{
		irecord = r;
		dbRecord = (Record400) r;
		dbRecord.record = record;
		file.setRecordFormat(dbRecord);
	}
	/**
	 * Set Greater Than - position past the record with the key value applied in setKey().
	 * @return eof: false=cursor positioned correctly, true=no matching record found
	 */
	public boolean setgt() throws Exception
	{
		try
		{
			file.positionCursor(dbRecord.getKey(), KeyedFile.KEY_GT);
			direction = ' ';
			eof = false;
			return true;
		}
		catch (Exception e)
		{
			//file.positionCursorAfterLast();
			direction = 'F';
			eof = true;
			return false;
		}
	}
	public boolean setgt(Record400 r) throws Exception
	{
		setRecordFormat(r);
		return setgt();
	}
	/**
	 * Set Lower Limit - position to the record with the key value >= to the key applied in setKey().
	 * @return record found: true=exact match found, false=cursor positioned after the specified key
	 */
	public boolean setll() throws Exception
	{
		direction = 'B';
		eof = false;
		boolean equal = false;
		try
		{
			// See if the exact key exists
			if (file.read(dbRecord.getKey()) != null)
				equal = true;
			else
				file.positionCursor(dbRecord.getKey(), KeyedFile.KEY_GE);
		}
		catch (Exception e)
		{
			//file.positionCursorAfterLast();
			direction = 'F';
			eof = true;
		}
		// For whatever reason %EOF without a file name is not set
		setI2Found(!eof);
		setI2Equal(equal);
		return equal;
	}
	public boolean setll(Record400 r) throws Exception
	{
		setRecordFormat(r);
		return setll();
	}
	/**
	 * Set the record format object to read from/into.  None of the cursor opcodes (readx, write, update, Delete) can be 
	 * used until the record format is set.
	 */
	public void setRecordFormat(Record400 r) throws Exception
	{
		setFormat(r);
	}
	
	/**
	 * Unlock the current record.  Usually, when a file is opened for READ_WRITE, the current record is locked and cannot
	 * be updated by another process.
	 */
	public void unlock()
		throws InterruptedException, IOException, AS400Exception, AS400SecurityException
	{
		// Unlock current record (if there is one) by setting ReadNoUpdate flag, regetting current record, then setting flag back
		if (record != null)
		{
			file.setReadNoUpdate(true);
			dbRecord.record = file.read();
			file.setReadNoUpdate(false);
		}
	}
	public void update()
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		if (record == null)
			throw new Exception("Unable to update record without prior read");
		dbRecord.output();
		file.update(dbRecord.record);
		//return false;
	}
	/** Update the current record with the values previously set by setDouble(), etc. */
	public void update(Record400 r)
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		update();
	}
	/** Write the current values out to a new record. */
	@Override
	public boolean write()
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		// If a record hasn't been created for this record yet, then make one
		// This is a fairly unusual case where a write is done without a prior read, so we'll do the test here every time
		// (which is fast) instead of always creating a record object in the RdbRecord constructor (which is very slow)
		if (dbRecord.record == null)
			dbRecord.record = new com.ibm.as400.access.Record(dbRecord);
		dbRecord.output();
		file.write(dbRecord.record);
		direction = ' ';
		return false;
	}
	public void write(Record400 r)
		throws Exception //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		write();
	}
}
