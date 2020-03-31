package com.i2class;

import com.ibm.as400.access.*;

/**
 * Write out RPG-like 'green bar' (column based) output to a OS/400 spool file.
 * @see RecordSpool
 */
public class RfileSpool extends RfilePrinter
{
	private SCS5256Writer file;
	private PrinterFile prtf;
	private PrintParameterList options;
	SpooledFileOutputStream stream;
	private RecordSpool prtRecord;
	int pageWidth;
	private int overflow = -1;
	private int cur_page_line_num = 0;
	/** 
	 * Create a report on the specified OS/400 system. 
	 * @param system The target OS/400 system.
	 * @param fileName The name of the spool file.
	 * @param len The width (in characters) of the report.
	 */
	public RfileSpool(AS400 system, String fileName, int len)
	{
		options = new PrintParameterList();
		//options.setParameter(PrintObject.ATTR_CONTROLCHAR, "*FCFC");
		//options.setParameter(PrintObject.ATTR_PAGEWIDTH, len+1);
		pageWidth = len /*+1*/;

		String path = Application.getPath(fileName);
		prtf = new PrinterFile(system, path);
	}
	private boolean checkOverflow()
	{
		if (((RecordSpool) prtRecord).splf == null)
			return false;

		// We only need to get the overflow value once.  Java doesn't allow static local variables, so it is a private data member
		if (overflow == -1)
		{
			try
			{
				overflow =
					((RecordSpool) prtRecord)
						.splf
						.getIntegerAttribute(PrintObject.ATTR_OVERFLOW)
						.intValue();
			}
			catch (Exception e)
			{
			}
		}
		cur_page_line_num = prtRecord.cur_page_line_num;
		return (cur_page_line_num > overflow);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/19/2001 11:03:32 AM)
	 * @return boolean
	 */
	@Override
	public void close() throws java.io.IOException
	{
		file.close();
		// Don't remove this because we may still have a reference to the app object
		removeClosedFile();
	}
	/**
	 * Open this printer file.
	 */
	@Override
	public void open(int openType)
	{
		// This is nuts, but you can't open() a file, but you can close() it.  So, if someone closes the file and then
		// tries to reopen it we have to recreate the objects.  Arghhhh...
		try
		{
			AS400 system = prtf.getSystem();
			stream = new SpooledFileOutputStream(system, options, prtf, null);
			file = new SCS5256Writer(stream, system.getCcsid(), system);
		}
		catch (Exception e)
		{
		}
		page = 1;
		// Add this file to the list of open files so that they can be closed during the RETURN step
		addToOpenFiles();
	}
	@Override
	public void setFormat(IRecord rcd)
	{
		setRecord(rcd);
		prtRecord = (RecordSpool) rcd;
		prtRecord.printer = file;
		prtRecord.stream = stream;
		prtRecord.pageWidth = pageWidth;
		prtRecord.cur_page_line_num = cur_page_line_num;
	}
	public void setRecordFormat(RecordSpool rcd)
	{
		setFormat(rcd);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/26/2001 10:02:49 AM)
	 * @return int
	 * @param c char
	 */

	@Override
	public boolean write() throws Exception
	{
		prtRecord.cur_page_line_num = cur_page_line_num;
		prtRecord.output();
		return checkOverflow();
	}
	public boolean write(RecordSpool rcd) throws Exception
	{
		setRecordFormat(rcd);
		return write();
	}
}
