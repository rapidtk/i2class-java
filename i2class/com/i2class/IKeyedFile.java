package com.i2class;

/**
 * An interface to a keyed database file.
 * @author Andrew Clark 
 */
public interface IKeyedFile extends IDiskFile
{
	/**
	 * CHAIN (position and read) to a specific record.  Use setKey() to set the key value.  Each record format will implement its own setKey method.
	 * @return eof: true if no record found, false if exact key match found.
	 */
	public boolean chain()
		throws Exception; //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
/**
	 * Chain to a record without putting a record lock on it.
	 * @return eof: true if no record found, false if exact key match found.
	 */
	public boolean chainn()
		throws Exception; //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	/** Delete the current record from the file. */
	public boolean Delete() throws Exception;
/* Everything from this point on is considered 'keyed' access */
	/** 
	 * Read the next record if its key matches the values previously applied with setKey(). 
	 * @return eof true=no more records match, false=record returned successfully.
	 */
	public boolean reade()
		throws Exception; //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
/**
	 * Read the next equal record from the file without putting a record lock on it.
	 * @see #reade()
	 */
	public boolean readen()
		throws Exception; //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
/** 
	 * Read the next record if its key matches the value of the current record. 
	 * @return eof true=no more records match, false=record returned successfully.
	 */
	public boolean readEqual()
		throws Exception; //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
/**
	 * Read the next equal record from the file without putting a record lock on it.
	 * @see #readEqual()
	 */
	public boolean readEqualn()
		throws Exception; //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
/** 
	 * Read the previous record.
	 * @return bof
	 */
	public boolean readp()
		throws Exception; //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
/** 
	 * Read Previous Equal - read the previous record if its key matches the values previously applied with setKey(). 
	 * @return bof true=no more records match, false=record returned successfully.
	 */
	public boolean readpe()
		throws Exception; //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
/**
	 * Read the previous equal record from the file without putting a record lock on it.
	 * @see #readpe()
	 */
	public boolean readpen()
		throws Exception; //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
/** 
	 * Read Previous Equal - read the previous record if its key matches the value of the current record. 
	 * @return bof true=no more records match, false=record returned successfully.
	 */
	public boolean readpEqual()
		throws Exception; //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
/**
	 * Read the previous equal record from the file without putting a record lock on it.
	 * @see #readpEqual()
	 */
	public boolean readpEqualn()
		throws Exception; //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
/**
	 * Read the previous record from the file without putting a record lock on it.
	 */
	public boolean readpn()
		throws Exception; //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	/**
	 * Set Greater Than - position past the record with the key value applied in setKey().
	 * @return no record (eof): false=cursor positioned correctly, true=no matching record found
	 */
	public boolean setgt() throws Exception;
	/**
	 * Set Lower Limit - position to the record with the key value >= to the key applied in setKey().
	 * @return record found: true=exact match found, false=cursor positioned after the specified key
	 */
	public boolean setll() throws Exception;
}
