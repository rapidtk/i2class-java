package com.i2class;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

/**
 * The record format class for PDF output
 * @author Andrew Clark
 * @see RfilePDF
 */
public class RecordPDF extends RecordPrinter
{
	Document document;
	PdfContentByte contentByte;
	static private BaseFont baseFont, boldFont;
	static private double charSize;
	private BaseFont currentFont;
	float height;
	boolean overflow;
	public static final int HIGHLIGHT = Font.BOLD;
	public static final int UNDERLINE = Font.UNDERLINE;
	private int _style, lastStyle = -1;
	static final int DEFAULT_FONT_SIZE = 10;
	int fontSize;
	protected boolean[] IN = new boolean[99];
	/**
	 * Create a PDF record format object
	 * @version 10/1/2002 1:57:32 PM
	 */
	public RecordPDF()
	{
		super("");
		if (baseFont == null)
		{
			try
			{
				baseFont =
					BaseFont.createFont(
						BaseFont.COURIER,
						BaseFont.CP1252,
						BaseFont.NOT_EMBEDDED);
				boldFont =
					BaseFont.createFont(
						BaseFont.COURIER_BOLD,
						BaseFont.CP1252,
						BaseFont.NOT_EMBEDDED);
			}
			catch (Exception e)
			{
			}
			// All COURIER characters are the same width
			charSize = baseFont.getWidthPoint('W', DEFAULT_FONT_SIZE);
		}
	}
	/**
	 * Check to see if the print style has changed.
	 * If it has, then change the style attribute of the PDF output.
	 * @version 10/1/2002 11:11:45 AM
	 * @return true if the style has changed
	 */
	private boolean checkStyle()
	{
		boolean styleChanged = (_style != lastStyle);
		if (styleChanged)
		{
			lastStyle = _style;
			try
			{
				flush(false);
			}
			catch (Exception e)
			{
			}
			// Hah!  You'd think that this would work, but it doesn't...
			// baseFont = new Font(baseFont, RfilePDF.DEFAULT_FONT_SIZE, lastStyle).getBaseFont();
			// ...so, UNDERLINE will have to work like BOLD for now
			if ((lastStyle & Font.BOLD) != 0)
				currentFont = boldFont;
			else
				currentFont = baseFont;
		}
		return styleChanged;
	}
	/**
	 * Underline the current string if it needs to be
	 * @version 10/2/2002 8:54:14 AM
	 * @param left The left-most column of data to underline
	 */
	private void checkUnderline(int left)
	{
		if ((lastStyle & Font.UNDERLINE) != 0)
		{
			/* Find the first non-blank character
			int left=1;
			while (left<=maxColumn && outputBuffer[left]==' ')
				left++;
			*/
			try
			{
				flush(false);
				// Add underline character and re-write
				while (left <= maxColumn)
				{
					outputBuffer[left] = '_';
					left++;
				}
				flush(false);
			}
			catch (Exception e)
			{
			}
		}
	}
	/** Flush the current buffer **/
	public void flush() throws /*java.io.IO*/
	Exception
	{
		flush(true);
	}
	/** Flush the current buffer **/
	public void flush(boolean reset) throws /*java.io.IO*/
	Exception
	{
		if (maxColumn > 0)
		{
			// Calculate the Y position of the text from row.
			// For whatever reason, the x,y position seem to be from the BOTTOM left???
			float y =
				(float) (height
					- RfilePDF.MARGIN_SIZE
					- ((row - 1) * charSize * 1.65));
			//1.65 approx 8 lpi
			// Allow MARGIN_SIZE for overflow
			overflow = (y <= RfilePDF.MARGIN_SIZE * 2);
			contentByte.beginText();
			//contentByte.setFontAndSize(currentFont, DEFAULT_FONT_SIZE);
			contentByte.setFontAndSize(currentFont, fontSize);
			String text = new String(outputBuffer, 1, maxColumn);
			// Print the text at the calculated y position MARGIN_SIZE points from the left
			contentByte.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				text,
				RfilePDF.MARGIN_SIZE,
				y,
				0);
			contentByte.endText();
			clearBuffer(reset);
			_style = Font.NORMAL;
		}
	}
	public void print(char c, int col)
	{
		int left = col;
		checkStyle();
		super.print(c, col);
		checkUnderline(left);
	}
	public void print(INumeric n, int col, String edtWrd)
	{
		int left = col - edtWrd.length() + 1;
		checkStyle();
		super.print(n, col, edtWrd);
		checkUnderline(left);
	}
	protected void printChar(char str[], int col)
	{
		int edtLen = str.length;
		int left = col - edtLen + 1;
		checkStyle();
		super.printChar(str, col);
		checkUnderline(left);
	}
	/**
	 * The abstract print method where text is actually written.  
	 * The <code>output</code> method sets the values before this method is called.
	 * @version 10/1/2002 8:39:32 AM
	 */
	protected void printOutput() throws Exception
	{
	}
	/**
	 * Move the cursor to the specified row, but don't move to a new page if row 
	 * is less than the current row, unless this is the first setRow.
	 * @version 9/27/2002 2:31:51 PM
	 * @param rows int The row to move to
	 * @see #skip
	 */
	public void setRow(int rows) throws Exception
	{
		skip(rows);
	}
	/**
	 * Set the next printx() to be the specified style
	 * @param lstyle the specified style
	 */
	public void setStyle(int style)
	{
		_style = style;
	}
	/** Set the specified indicator. */
	public void setIndicator(int ind, boolean value)
	{
		IN[ind-1]=value;
	}
	/**
	 * Set the specified field of this format to a value
	 * @version 10/1/2002 9:52:05 AM
	 * @param fStr The <code>fixed</code> value to change
	 * @param value The value to set to
	 */
	public void setText(fixed fStr, char value)
	{
		fStr.assign(value);
	}
	/**
	 * Set the specified field of this format to a value
	 * @version 10/1/2002 9:52:05 AM
	 * @param fStr The <code>fixed</code> value to change
	 * @param value The value to set to
	 */
	public void setText(fixed fStr, fixed value)
	{
		fStr.assign(value);
	}
	/**
	 * Set the specified field of this format to a value
	 * @version 10/1/2002
	 * @param num The <code>zoned</code>value to change
	 * @param value The value to set to
	 */
	public void setText(zoned num, double value)
	{
		num.assign(value);
	}
	/**
	 * Set the specified field of this format to a value
	 * @version 10/1/2002
	 * @param num The <code>zoned</code>value to change
	 * @param value The value to set to
	 */
	public void setText(zoned num, INumeric value)
	{
		num.assign(value.doubleValue());
	}
	/** Skip to the specified row of the output page.  If the current row number is greater than
	 * <code>rows</code>, then skip to the specified line on the next page.
	 *  @param rows the row number to skip to 
	*/
	public void skip(int rows) throws /*java.io.IO*/
	Exception
	{
		flush();
		// If we are skipping to a line < the current row, then skip to a new page
		if (rows < row)
		{
			document.newPage();
			page++;
		}
		row = rows;
	}
	/** Space the specified number of lines -- effectively, a carriage return for each <code>rows</code>.
	 * Until a <code>space</code> or <code>skip</code> command
	 * is executed, output continues to be written to the current line (overprint). 
	 *  @param rows the number of rows to space down.
	*/
	public void space(int rows) throws /*java.io.IO*/
	Exception
	{
		flush();
		row += rows;
	}
}
