package com.i2class;

import java.util.*;
import java.util.Hashtable;
import java.util.Vector;
/** 
 * A class used to implement display file READ/EXFMT functionality.
 * This class is the base class used to represent a thread that will stall
 * at a READ/EXFMT statement of a display file until some sort of user
 * interaction occurs (e.g. function key pressed, ENTER).
 * @author Andrew Clark
 * @see ThreadLock
 */
public class RfileWorkstn extends Rfile /*extends com.ibm.Connection.access.KeyedFile*/
{

	//protected RecordThread record;
	//String fileName;
	private boolean writing = true;
	//public ThreadLock fileLock;
	fixed indds; // INDDS structure
	fixed infds; // INFDS structure
	Hashtable specialValues = new Hashtable();

	/* Contstruct the specified object. 
	 * @param m The thread locking object used to communicate between the RPG thread and the application thread.
	 * @param lfileName The name of this thread.
	public RdspfThread(
		ThreadLock m,
		String lfileName) //throws java.beans.PropertyVetoException
	{
		fileName = lfileName;
		fileLock = m;
		app = m.app;
		//mainThread = thread;
	}
	 */
	 
	/** Contstruct the specified object. 
	 * @param m The thread locking object used to communicate between the RPG thread and the application thread.
	 * @param lfileName The name of this thread.
	 */
	public RfileWorkstn(
		Application app,
		String lfileName) //throws java.beans.PropertyVetoException
	{
		actualFileName = lfileName;
		//fileLock = app.threadLock;
		this.app = app;
		// Add this file to the list of open files here instead of in open because this object always has a 
		// reference to an app object
		addToOpenFiles();
	}
	/*
	public RdspfThread(
		ThreadLock m,
		String lfileName,
		RecordThread rcd) //throws java.beans.PropertyVetoException
	{
		this(m, lfileName);
		try
		{
			setRecordFormat(rcd);
		}
		catch (Exception e)
		{
		}
	}
	*/
	/** Read a record from the specified relative record number of the subfile.  A subfile is a 'combo box' that 
	 * can contain multiple columns of data.
	 */
	public boolean chain(/*RecordThreadSFL*/
	IRecordSFL r, int rrn)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		r.setRRN(rrn);
		// If we chain to a record before the screen has been read, don't wait for input
		boolean w = writing;
		writing = false;
		boolean c=read(r);
		writing = w;
		return c;
	}
	public void close()
	{
		//fileLock.releasing();
		if (app==null || app.INLR)
		{
			//fileLock = null;
			infds=null;
		}
		removeClosedFile();
	}
	/** Execute format - display the record formats (screens) written to this file and wait for user input. */
	public void exfmt()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		write();
		read();
	}
	public void exfmt(RecordWorkstn r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		exfmt();
	}
	/**
	 * Return the XML data associated with all of the records written to this file. 
	 */
	public String getXML()
	{
		return ((RecordWorkstn)irecord).getXML();
	}
	public void open(int openType)
	{
		//if (fileLock==null)
		//	fileLock = app.threadLock;
		// Add this file to the list of open files
		// Don't do this anymore but do it in constructor because of the reference to the app object
		//addToOpenFiles();
	}

	/** 
	 * Read values from the display 
	 */
	private void readValues() throws Exception
	{
		app.threadLock.lastRcd = (RecordWorkstn)irecord;
		I2Logger.logger.debug("read releasing...");
		app.threadLock.releasing();
		app.threadLock.released();
		I2Logger.logger.debug("read acquiring...");
		app.threadLock.acquiringParms();

		//Hashtable specialValues = new Hashtable();
		specialValues.clear();
		// Loop through each format and set their values now instead of on a subsequent read so that
		// subsequent EXFMTs don't blast the values
		int writtenCount = app.threadLock.writtenFormats.size();
		for (int w=0; w<writtenCount; w++)
		{
			RecordWorkstn writeRecord = (RecordWorkstn)app.threadLock.writtenFormats.elementAt(w);
			// This record may have been written by an application that is no longer on the stack, if so then  
			// don't process it
			if (writeRecord.file.app != null)
			{
				// Clear any feedback indicator values
				writeRecord.clearFeedbackIndicators();
				int parmCount = app.threadLock.parmNames.size();
				for (int i = 0; i < parmCount; /*i++*/)
				{
					boolean remove=false;
					String parm = (String) (app.threadLock.parmNames.elementAt(i));
					String value = (String) app.threadLock.parmValues.elementAt(i);
					int j = parm.indexOf("$");
					if (j > 0)
					{
						// In the 4.0 WebFacing run-time, the record name will always be prefixed with "l1_"
						if (parm.substring(3, j).compareTo(writeRecord.recordName) == 0)
						{
							String fieldName = parm.substring(j + 1);
							// If this field is part of a subfile, then everything after the '$' is the record number
							RecordWorkstn changeRecord=null;
							int k = fieldName.indexOf("$");
							if (k > 0)
							{
								Integer rrn = Integer.valueOf(fieldName.substring(k + 1));
								fieldName = fieldName.substring(0, k);
								// For whatever reason, Webfacing defines subfile fields as part of the control format, so
								// we have to indirectly reference them here.
								//IRecordSFLX sflctl = (IRecordSFLX)record;
								IRecordSFLX sflctl = (IRecordSFLX)writeRecord;
								IRecordSFL isfl = sflctl.getSfl();
								//RecordSFL sfl_ = sflctl.getSfl_();
								// We changed this so that the parameters get inserted with the correct RRN
								//sflctlrcd.sfl.setRRN(Integer.parseInt(rrn)+sflctlrcd.sfl.topRRN-1);
								isfl.setRRN(rrn.intValue());
								// Keep track of what records were changed
								isfl.addChangedValue(rrn);
								changeRecord=(RecordWorkstn)isfl;
							}
							else
								//changeRecord=record;
								changeRecord=writeRecord;
							changeRecord.setTextChange(fieldName, value);
							// Remove parameter so that it doesn't get processed again
							// THIS WAS COMMENTED OUT -- WHY!!!???
							remove=true;
						}
					}
					// Save special parameter values: AID, W_WIDTH, CURSOR, BLANKFIELDS, etc.
					else
					{
						specialValues.put(parm, value);
						remove=true;
					}
					if (remove)
					{
						app.threadLock.parmNames.removeElementAt(i);
						app.threadLock.parmValues.removeElementAt(i);
						parmCount--;
					}
					else
						i++;
				}
				writeRecord.processSpecialValues(specialValues);
			}
		}
		app.threadLock.writtenFormats.removeAllElements();
		//formatRRN.clear();
		I2Logger.logger.debug("released2...");
		//fileLock.parmAcquire();
	}



	/** 
	 * Read data from any screens written to. 
	 * @see #exfmt
	 */
	public boolean read()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		// Wait for parameter values
		if (writing)
			readValues();

		//TODO This should have been done in readValues() above
		//record.processSpecialValues(specialValues);

		// Read data from current record into global (I2App) variables
		boolean b = readx();

		// Signal that we're done with the parameter values
		if (writing)
		{
			// If a separate indicator structure is specfied, then write the indicator values
			if (indds!=null)
			{
				RecordWorkstn record = (RecordWorkstn)irecord;
				for (int i=0; i<99; i++)
				{
					byte bt = (byte)(record.getIndicator(i+1)?'1':'0');
					indds.setByteAt(i, bt);
				}
				indds.updateThis();
			}
			//fileLock.parmAcquire2();
			app.threadLock.acquiredParms();
			I2Logger.logger.debug("read acquired...");
			writing = false;
		}
		return b;
	}
	public boolean read(IRecord r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		// Didn't know that you could do this, but evidenly READ FILE for a work station means to 
		// read from all formats that have been written to.  
		// Do the same thing with FILE.read(null);
		if (r==null)
		{
			// We can't iterate through this list because the first read() clears the elements
			Object w[] = app.threadLock.writtenFormats.toArray();
			for (int i=0; i<w.length; i++)
			{
				RecordWorkstn writeRecord = (RecordWorkstn)w[i];
				read(writeRecord);
			}
			return false;
		}
		else
		{
			setRecordFormat(r);
			return read();
		}
	}
	/** Read changed records - read records whose input fields have changed. */
	public boolean readc(IRecordSFL r) throws Exception
	{
		setRecordFormat((RecordWorkstn)r);
		/*
		Integer rrnObject=(Integer)formatRRN.get(r);
		if (rrnObject == null)
		{
			formatRRN.put(r, new Integer(0));
			r.readcRRN=1;
		}
		else
			r.readcRRN = rrnObject.intValue();
		if (r.readcRRN > r.fldValuesRRN.size())
			return true;
		return read(r);
		*/
		// Get the next changed record rrn
		int readcRRN = r.getReadcRRN();
		// If the data for this record hasn't been read yet, then get values
		//if (readcRRN==0)
		//	read();
		
		
		/*		
		Vector changedValues = r.getChangedValues();
		if (readcRRN >= changedValues.size())
			return true;
		*/
		// -1 means end of subfile -- set eof
		if (readcRRN<=0)
			eof=true;
		else
		{
			//String rrn = (String) changedValues.elementAt(readcRRN);
			//r.setRRN(Integer.parseInt(rrn));
			r.setRRN(readcRRN);
			//r.setReadcRRN(readcRRN++); automatically incremented by getReadcRRN(), above
			eof = read((RecordWorkstn)r);
		}
		setI2Eof(eof);
		return eof;
	}

	protected boolean readx() throws Exception
	{
		irecord.input();
		return false;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3/20/2002 2:01:27 PM)
	 */
	private void release()
	{
		//fileLock.release();
	}
	/**
	 * Set the file INDARA (separate indicator) DS.
	 * The only fields updated in the INFDS are the cursor position and AID (function key) hex code.
	 */
	public void setINDDS(fixed fStr)
	{
		indds = fStr;
	}
	/**
	 * Set the file INFormation DS.
	 * The only fields updated in the INFDS are the cursor position and AID (function key) hex code.
	 */
	public void setINFDS(fixed fStr)
	{
		infds = fStr;
	}
	public void setFormat(IRecord r) throws Exception
	{
		setRecord(r);
		((RecordWorkstn)irecord).setFile(this);
	}
	public void setRecordFormat(IRecord r) throws Exception
	{
		setFormat(r);
	}
	public boolean update()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		irecord.output();
		// If we are updating a subfile, process any SFLNXTCHG keywrods
		if (irecord instanceof IRecordSFL)
			((IRecordSFL)irecord).checkSFLNXTCHG();
		return false;
	}
	public boolean update(RecordWorkstn r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return update();
	}
	/** 
	 * Write a record format to the display.  A record format describes the literals and input and output 
	 * fields that make up a screen of data.
	 */
	public boolean write()
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException
	{
		//if (!writing)
		//	fileLock.acquire();
		irecord.output();
		// If this is a SFLCTL format, only add if SFLDSPCTL is specified
		RecordWorkstn record = (RecordWorkstn)irecord;
		// If a separate indicator structure is specfied, then write the indicator values
		record.writeINDDS(indds);
		boolean isDSP = record.processIndicators();
		if (!app.threadLock.writtenFormats.contains(record) && isDSP)
			app.threadLock.writtenFormats.addElement(record);
		app.threadLock.lastRcd = record;
		record.isChanged = false;
		writing = true;
		return false;
	}
	public boolean write(IRecord r)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		setRecordFormat(r);
		return write();
	}
	/** 
	 * Write output to the specified relative record number of a subfile.
	 */
	public boolean write(IRecordSFL r, int rrn)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		r.setRRN(rrn);
		write((IRecord)r);
		r.checkSFLNXTCHG();
		// Return true if the subfile is full
		//return rrn>=r.getSFLCTL().getSubfileSize();
		return rrn>=r.getSubfileSize();
	}
	public boolean write(IRecordSFL r, INumeric rrn)
		throws Exception //ConnectionException, ConnectionSecurityException, ConnectionDroppedException, InterruptedException, IOException, java.beans.PropertyVetoException
	{
		return write(r, rrn.intValue());
	}
}
