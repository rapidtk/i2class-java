package com.i2class;

import com.ibm.as400.access.AS400;

/**
 * An IBM i keyed database file class.
 */
public class Rkeyed400 extends RfileDB400 {

	public Rkeyed400(AS400 system, String fileName) {
		super(system, fileName);
	}
	public Rkeyed400(I2AS400 system, String fileName) {
		super(system, fileName);
	}

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

}
