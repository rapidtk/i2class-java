package com.i2class;

/**
 * An interface for basic i/o record-level operations.
 */
interface IRecord
{
	/** 
	 * This stub is used during RPG cycle processing to see what control-level indicators should be set.
	 */
	void control() throws Exception;
	/** 
	 * This stub is used whenever data is 'read' from the file.
	 */
	void input() throws Exception;
	/** 
	 * This stub is used whenever data is 'written' (including updates) to the file.
	 */
	void output() throws Exception;
	
	/* Set the Application application object that this record is associated with 
	void setApp(Application app);
	*/
	
	/** Set the file object that this record is associated with
	 */ 
	void setFile(Rfile file);
	
	/** Get the Application application object that this record is associated with
	 */ 
	Application app();
}
