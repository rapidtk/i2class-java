package com.i2class;

import java.io.Serializable;

/**
 * A base class for all record-based file interaction.
 * 
 * Remember that %FOUND is set by CHAIN, DELETE, SETGT, SETLL - 
 *               %EOF is set by READ, READC, READE, READP, READPE
 *               %EOF(filename) is set off by CHAIN, OPEN, SETGT, SETLL 
 * 
 */
abstract class Rfile implements Serializable, IClosable
{
	protected IRecord irecord;
	/** end-of-file - set to true when the record pointer is positioned after the last record in the file. */
	public boolean eof;
	/** beginning-of-file - set to true when the record pointer is positioned before the first record in the file */
	public boolean bof = true;
	boolean found;
	boolean opened;
	protected String fileName,actualFileName;
	// The Application associated with this file
	public Application app;
	protected int openType;
	
	
	
	/** Close this file. */
	public abstract void close() throws Exception;
	
	
	/** Destroy the references that this object has. */
	protected void finalize() throws Throwable
	{
		if (I2Logger.logger.isDebuggable())
			I2Logger.logger.debug("Finalizing file " + this);
		close();
	}
	
	/** Check for file overrides and set actual name if it exists. */
	protected void checkOverrides()
	{
		if (app!=null)
		{
			// Strip library out of name
			String key;
			int i = fileName.indexOf('.');
			if (i>=0)
				key = fileName.substring(i+1);
			else
				key = fileName;
			// Check for overrides anywhere on call stack
			String s = null;
			Application oapp=app;
			do
			{
				if (oapp.ovrFiles != null)
				{
					s = (String)oapp.ovrFiles.get(key);
					// Override is found
					if (s!=null)
					{
						setActualFileName(s);
						break;
					}
				}
				oapp = oapp.prvApp();
			} while(oapp!=null);
		}
	}
	
	/**
	 * Return the 'native' file name of the object.
	 */
	public String getActualFileName()
	{
		return actualFileName;
	}
	
	/** 
	 * Open a file.  No other operations (read, write, etc.) can be done until the file is opened. 
	 * @param openType How the file should be opened: READ_ONLY, READ_WRITE, WRITE_ONLY.
	 */
	abstract public void open(int openType) throws Exception;

	/** Add to the list of open files for this app */
	protected void addToOpenFiles()
	{
		if (app != null)
			app.addOpenFile(this);
		opened=true;
	}
	
	/** Remove a closed file from the list of open files. */
	protected void removeClosedFile()
	{
		if (app != null && opened)
			app.removeClosedFile(this);
		/* If the app has been deactivated or has never been set, then remove the resources associated with this file */
		if (app==null || app.INLR)
		{
			irecord=null;
			app=null;
		}
		opened=false;
	}

	/**
	 * Set the open file name.
	 */
	public void setFileName(String fileName) throws Exception
	
	{
		this.fileName = fileName;
		setActualFileName(fileName);
	}
	/**
	 * Set the 'native' file name (do not set any extension).
	 */
	public void setActualFileName(String actualFileName)
	
	{
		this.actualFileName = actualFileName;
	}
	
	abstract void setFormat(IRecord r) throws Exception;
	
	void setRecord(IRecord rcd)
	{
		irecord = rcd;
		rcd.setFile(this);
	}
	
	/** Write a new record to the specified file. */
	abstract public boolean write() throws Exception;
	
	// Set global eof indicator
	protected void setI2Eof(boolean eof)
	{
		if (app != null)
			app.eof = eof;
	}

	/**
	 * Set the file INFormation DS.
	 * This is only supported in display files, but is here as a placeholder for translated code.
	 * @deprecated
	 */
	public void setINFDS(FixedChar fStr)
	{
	}
}
