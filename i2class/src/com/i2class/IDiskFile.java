package com.i2class;

/**
 * An interface to a database file.
 * <P>
 * Database access in RPG is different than SQL database access.
 * In RPG, there is an implied 'cursor' (a pointer to the current record in the record-set) available to all files 
 * in a RPG program.  Within the program, certain I/O operation codes (opcodes) are used to move the cursor to a 
 * new record.  Any opcode that reads from the file will set the variable name corresponding to the value of each 
 * field.  Typically the variable name is the same as the field name, and does not even have to be declared in 
 * the RPG source.  
 * <P>
 * Keyed access is also directly available to an RPG program.  
 * In RPG, a single database file can have both data (table) and a key (index) attached to it.  
 * RPG always knows about those keys, and can position the cursor anywhere within that 
 * key (very similar to locate in ADO).
 * <P>
 * In I2, before you can use a file, you have to set its 'record format' (the description of all of the fields that make up a file).  After the file is opened, you perform operations on the file using a file.opcode() syntax.  If you are doing key manipulations, you must set the key in the record format by doing format.setKey() before performing any file operation.
 * <pre>
 * custmast.setRecordFormat(cusfmt);
 * custmast.open(READ_ONLY);
 * cusfmt.setKey(cusno);
 * IN90=custmast.chain();
 * </pre>
 * Notice how the indicator IN90 is used to communicate the 'result' of the CHAIN opcode.
 * The indicator will be set on (*ON, true) when EOF (no record) is found, otherwise it will be off (*OFF, false).

 * @see Rfile
 *  
 */
public interface IDiskFile
{
	/** 
	 * Commit any changes (updates/deletes) made to a file opened with commitment control.
	 * @see #open(int, int, int)
	 */
	public void commit()
		throws Exception;
	/** Delete the current record from the file. */
	public boolean delete() throws Exception;
	/**
	 * Open the file.
	 * @param openType How the file should be opened: READ_ONLY, READ_WRITE, WRITE_ONLY.
	 * @param blockFactor The number of records to block on each read.  0 means use system default.
	 * @param commit How to commit updates.  One of the COMMIT_LOCK_LEVEL_xxx constants in Application.
	 * @see Application#COMMIT_LOCK_LEVEL_SERIALIZABLE
	 * @see Application#COMMIT_LOCK_LEVEL_ALL
	 * @see Application#COMMIT_LOCK_LEVEL_CURSOR_STABILITY
	 * @see Application#COMMIT_LOCK_LEVEL_CHANGE
	 * @see Application#COMMIT_LOCK_LEVEL_DEFAULT
	 * @see Application#COMMIT_LOCK_LEVEL_NONE
	 */
	public void open(int openType, int blockFactor, int commit)
		throws Exception;
	/** 
	 * Read the next record from the file.
	 * @return eof
	 */
	public boolean read()
		throws Exception; //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
	/**
	 * Read the next record from the file without putting a record lock on it.
	 */
	public boolean readn()
		throws Exception; //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
/** 
	 * Read the previous record.
	 * @return bof
	 */
	public boolean readp()
		throws Exception; //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
/** Rollback any changes (update/deletes) before they are committed. */
	public void rolbk()
		throws Exception;
	/**
	 * Unlock the current record.  Usually, when a file is opened for READ_WRITE, the current record is locked and cannot
	 * be updated by another process.
	 */
	public void unlock()
		throws Exception;
	/** Update the current record with the values previously set by setDouble(), etc. */
	public void update()
		throws Exception; //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
/** Write the current values out to a new record. */
	public boolean write()
		throws Exception; //AS400Exception, AS400SecurityException, ConnectionDroppedException, InterruptedException, IOException
}
