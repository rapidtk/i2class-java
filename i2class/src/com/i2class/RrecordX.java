package com.i2class;

import java.io.Serializable;

/**
 * A base record class for I2 (not AS400) records.
 * 
 */
class RrecordX implements IRecord, Serializable
{
	String recordName;
	Rfile file;
	
	static protected final int KEY_DESCEND=1;
	static protected final int KEY_ABSVAL=2;
	
	RrecordX(String recordName)
	{
		this.recordName=recordName;
	}
	//protected Application app;
	public void control() throws Exception
	{
	}
	public void input() throws Exception
	{
	}
	public void output() throws Exception
	{
	}
	
	public void setFile(Rfile file)
	{
		this.file=file;
	}
	
	public Application app()
	{
		return file.app;
	}
	/*
	public void setApp(Application app)
	{
		this.app=app;
	}
	*/
}
