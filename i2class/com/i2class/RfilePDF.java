package com.i2class;

import java.io.*;
import java.sql.Connection;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
/**
 * Write out RPG-like 'green bar' output to a PDF file.
 * @author: Andrew Clark
 */
public class RfilePDF extends RfilePrinter
{
	private Document document;
	private PdfWriter writer;
	private PdfContentByte contentByte;
	private BaseFont baseFont;
	//private RecordPDF record;
	private float height;
	private double charSize;
	private int row = 1;
	private boolean overflow;
	static final int MARGIN_SIZE = 36; // 36 points = 1/2"
	private Rectangle r;
	//private int fontSize=RecordPDF.DEFAULT_FONT_SIZE;
	int_f fontSize = new int_f(2, RecordPDF.DEFAULT_FONT_SIZE);

	/**
	 * Write RPG-like output to a PDF file
	 * @parm fileName the name of the file to create (without a .pdf extension)
	 * @parm rcdLen the record length of the file
	 */
	public RfilePDF(String fileName, int rcdLen)
	{
		this.actualFileName = fileName;
		// Use A4 instead of legal since it is slightly smaller (A4 always fits on legal, not vice versa)		
		r = PageSize.A4;
		// Use landscape if we are printing more than 80 columns
		if (rcdLen > 80)
		{
			r = r.rotate();
			// Found this: http://www.math.utah.edu/~beebe/software/lptops/lptops-04.html
			// For Courier, 120.45/PointSize=CPI, so fontSize = (120.45*11)/rcdLen = 1324.95/rcdLen (11=pageWidth)
			if (rcdLen>132)
				//fontSize = RecordPDF.DEFAULT_FONT_SIZE-(rcdLen/199)-1; // <=198=9, otherwise 8 (378 max width)
				fontSize.assign(1324.95/rcdLen);
		}
	}
	/**
	 * Write RPG-like output to a PDF file
	 * @parm rhost the host name to connect to
	 * @parm fileName the name of the file to create (without a .pdf extension)
	 * @parm rcdLen the record length of the file
	 */
	public RfilePDF(Application app, String fileName, int rcdLen)
	{
		this(fileName, rcdLen);
		this.app = app;
	}
	
	public void close() throws Exception
	{
		RecordPDF record = (RecordPDF)irecord;
		if (record != null)
		{
			record.flush();
			record.page = 1;
			record.row = 1;
			record.overflow=false;
		}
		document.close();
		removeClosedFile();
	}
	public void open(int openType) throws Exception
	{
		document =
			new Document(r, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE);
		// 36 points = 1/2" margins
		//try
		//{
			//r = document.getPageSize();
			height = r.height();
			// Try to create the 'normal' file name first (file.pdf)
			File pdfFile = new File(actualFileName + ".pdf");
			// If the file exists (createNewFile returns false), then create unique file name.
			// This allows multiple files (just like spool files) to exist in the same directory with 
			// similar names.
			if (!pdfFile.createNewFile())
			{
				String tempFile = pdfFile.getName();
				// Remove pdf extension
				tempFile = tempFile.substring(0, tempFile.length()-4);
				File curDirectory = pdfFile.getParentFile();
				pdfFile = File.createTempFile(tempFile, ".pdf", curDirectory);
			}
				
			writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

			// The content byte is used to write directly to an x/y position on the document
			contentByte = writer.getDirectContent();

		/*
		}
		catch (Exception e)
		{
		}
		*/
		page = 1;
		document.open();
		// Add this file to the list of open files so that they can be closed during the RETURN step
		addToOpenFiles();
	}
	public void setFormat(IRecord format)
	{
		RecordPDF record = (RecordPDF) irecord;
		if (record != null)
		{
			page = record.page;
			row = record.row;
			overflow = record.overflow;
		}
		setRecord(format);
		record = (RecordPDF) format;
		record.document = document;
		record.contentByte = contentByte; // Content byte
		record.fontSize = fontSize.intValue();
		record.height = height;
		if (opened)
		{
			record.row = row;
			record.page = page;
			record.overflow = overflow;
		}
	}
	public void setRecordFormat(RecordPDF format)
	{
		setFormat(format);
	}
	public boolean write() throws Exception
	{
		RecordPDF record = (RecordPDF)irecord;
		record.output();
		record.printOutput();
		row = record.row;
		overflow = record.overflow;
		return overflow;
	}
	public boolean write(RecordPDF format) throws Exception
	{
		setRecordFormat(format);
		return write();
	}
	
	/** Handle to PDF internal structures. */
	public class Handle
	{
		public Rectangle 	rectangle;
		public int_f	marginSize;
		public int_f	fontSize; 

		public Document 	document;
		public PdfWriter 	writer;
	}
	/** Return handle to PDF file. */
	public Handle getHandle()
	{
		Handle h = new Handle();
		
		h.rectangle=r;
		h.fontSize=fontSize;

		h.document=document;
		h.writer=writer;
		
		return h;
	}
}
