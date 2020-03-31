package com.i2class;

import com.ibm.as400.access.*;
import java.io.*;

/**
 * A record class to write spool file output.
 * @author Andrew Clark
 * @see com.i2class.RfileSpool
 */
public class RecordSpool extends RecordPrinter implements IRecord
{
	SCS5256Writer printer;
	SpooledFile splf;
	OutputStream stream;
	public RecordSpool()
	{
		super("");
	}
	public void flush() throws /*java.io.IO*/Exception
	{
		if (maxColumn > 0)
		{
			//outputBuffer[0]=(byte)'+'; // Print without spacing
			//file.write(outputBuffer,0,maxColumn);
			//file.write('\n');
			printer.absoluteHorizontalPosition(1);
			printer.write(outputBuffer, 1, maxColumn);
			//file.flush();
			clearBuffer(true);
			//updatePage();
		}
	}
	public void skip(int rows) throws /*java.io.IO*/
	Exception
	{
		flush();
		// updatePage() doesn't seem to work, so just track page manually
		if (cur_page_line_num > rows)
		{
			page++;
		}
		cur_page_line_num = rows;
		printer.absoluteVerticalPosition(rows);
		updatePage();
	}
	public void space(int rows) throws /*java.io.IO*/
	Exception
	{
		flush();
		cur_page_line_num += rows;
		/*
		while (rows>0) {
			switch (java.lang.Math.min(rows, 3)) {
			case 2:
				outputBuffer[0]=(byte)'0'; // double spacing
				break;
			case 3:
				outputBuffer[0]=(byte)'-'; // triple spacing
				break;
			default:
				outputBuffer[0]=(byte)' ';
			}
			//file.write(outputBuffer,0,1);
			//file.write('\n');
			file.write(outputBuffer,0,pageWidth);
			file.flush();
			rows=rows-3;
		};
		*/
		printer.relativeVerticalPosition(rows);
		updatePage();
	}
	protected void updatePage() throws java.io.IOException
	{
		/*
		*/
		// updatePage() gets called after space/skip, so write carriage return to start printing at column 0
		printer.carriageReturn();

		// We can use the spoolfile to get the current page number.  For whatever reason, we need to make sure that some
		// data has been written (flush) or else a 'Object must be opened' exception gets thrown.  
		if (splf == null)
		{
			printer.flush();
			splf = ((SpooledFileOutputStream) stream).getSpooledFile();
		}
		try
		{
			//The current page is not being returned correctly, so just track it manually in skip
			//page = splf.getIntegerAttribute(PrintObject.ATTR_CURPAGE).intValue();
			//			int printLength = splf.getIntegerAttribute(PrintObject.ATTR_PAGELEN).intValue();
			//int printLength = splf.getIntegerAttribute(PrintObject.ATTR_OVERFLOW).intValue();
			//cur_page_line_num %= printLength;
			int i = page;

		}
		catch (Exception e)
		{
		}

	}
}
