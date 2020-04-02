package com.i2class;

/**
 * A generic text print record.
 * <P>
 * Edit codes and edit words 
 * <a href="http://publib.boulder.ibm.com/iseries/v5r2/ic2924/books/c092508416.htm#HDREDITNUM">format</a>
 * the output of numeric values, 
 * much like a format string (e.g. '###,000,000.00") would.
 * Edit codes are pre-built format strings that automatically adjust themselves to the length of the value 
 * that they are applied to.  
 * For example, the edit code '3' applied to the value 1000 output as the value '1,000'.  
 * The edit code '3' applied to 9321.32 would output '9,321.32'.
 * @author Andrew Clark
 */
abstract public class RrecordPrint extends RrecordX2
{
	
	/** 
	 * Natural logarithm of 10. 
	 * Approximately equal to 2.302585. 
	 */ 
	 public static final double LN10=2.3025850929940456840179914546844;
	 
	 // Date/time constants for print records
	public FmtTime TIME;
	public FmtDate UDATE;
	 
	 /** 
	 * Edit the specified value using the specified edit word and place the results into a outputBuffer 
	 * at the specified index. */
	static protected void editBuffer(
		int bufOffset,
		INumeric n,
		String edtWrd,
		char[] outputBuffer)
	{
		// Make a pass forwards through the edit word to accumulate information...
		int wrdI = edtWrd.length();
		char fillChar = ' ';
		int currency = 0, decimal = 0, precision = 0, lastBlank = 0;
		int zeroSuppress = wrdI;
		for (int i = 0; i < wrdI; i++)
		{
			switch (edtWrd.charAt(i))
			{
				case '0' :
				case '*' :
					if (i > zeroSuppress)
						break;
					zeroSuppress = i;
					if (edtWrd.charAt(i) == '*')
						fillChar = '*';
					// intentionally fall through here and count the zero suppress character
				case ' ' :
					lastBlank = i;
					if (decimal > 0)
						precision++;
					break;
				case '$' :
					currency = i;
					break;
				case '.' :
					decimal = i;
			}
		}
		int nlen = n.len();
		if (precision == 0)
			precision = nlen;

		// Start from the back of the converted string and start copying in digits
		/*
		char *str;
		str=n.overlay;
		*/
		zoned strBuf = n.toZoned();
		int str = 0;
		boolean negative = (strBuf.signum() < 0);
		if (negative)
		{
			//str++;
			strBuf = (zoned)strBuf.negate();
		}
		// Loop past any leading zeros
		int j = 0;
		//for ( ; j<nlen-n.PrecisionOf(); j++)
		for (; j < nlen; j++)
		{
			if (strBuf.charAt(str) == '0')
				str++;
			else
				break;
		}
		int strI = nlen - j;
		while (wrdI > 0)
		{
			boolean dft;
			dft = false;
			wrdI--;
			switch (edtWrd.charAt(wrdI))
			{
				case '&' :
					outputBuffer[bufOffset + wrdI] = (byte) ' ';
					break;
					// Handle CR (credit)
				case 'R' :
					if (wrdI > 0
						&& edtWrd.charAt(wrdI - 1) == 'C'
						&& (wrdI - lastBlank == 2 || wrdI == 1))
					{
						wrdI--;
						if (negative)
							"CR".getChars(0, 2, outputBuffer, bufOffset + wrdI);
					}
					else
						dft = true;
					break;
				case '-' :
					if (wrdI - lastBlank == 1 || wrdI == 0)
					{
						if (negative)
							outputBuffer[bufOffset + wrdI] = (byte) '-';
					}
					break;
				case '$' :
					if (wrdI != currency)
					{
						dft = true;
						break;
					}
				case '0' :
				case '*' :
					if (wrdI > zeroSuppress)
					{
						dft = true;
						break;
					}
					// fall through here intentionally
				case ' ' :
					strI--;
					if (strI >= 0
						&& (strBuf.charAt(str + strI) != '0' || wrdI <= zeroSuppress))
						outputBuffer[bufOffset + wrdI] = strBuf.charAt(str + strI);
					else if (wrdI > zeroSuppress)
						outputBuffer[bufOffset + wrdI] = '0';
					// Insert floating currency symbol
					else if (
						currency > 0
							&& (strI == -1 || (strI < 0 && wrdI == currency)))
						outputBuffer[bufOffset + wrdI] = '$';
					else
						outputBuffer[bufOffset + wrdI] = fillChar;
					break;
					//case ',':
					// Treat commas like any other character -- suppress them if no numbers exist 'before' them
				default :
					if (strI <= 0 && wrdI < zeroSuppress)
					{
						outputBuffer[bufOffset + wrdI] = fillChar;
						break;
					} // Fall through here intentionally
					//default:
					dft = true;
			}
			if (dft)
				outputBuffer[bufOffset + wrdI] = edtWrd.charAt(wrdI);
		}
	}
	
	/** Return the edited value of a number and an edit word. */
	public static String editNumeric(INumeric n, String edtwrd)
	{
		char buffer[] = new char[edtwrd.length()];
		RrecordPrint.editBuffer(0, n, edtwrd, buffer);
		return new String(buffer);
	}
	
	/** Return a edit word corresponding to the specified edit code/fill character applied to the numeric value. */
	static String getEdtWrd(INumeric n, char edtCde, char fillChar)
	
	{
		// If the value of n is 0, then skip this entirely for edit codes that
		// don't print a zero balance.
		//int nlen=n.len();
		int nlen = n.len();
		int precision = n.scale();
		if (n.equals(0))
		{
			if ("2BKO4DMQZ".indexOf(edtCde) >= 0)
				return "";
			if (fillChar==' ')
				fillChar='0';
		}
		// Generate appropriate edit word from edit code
		String edtWrd;
		// Generate 'W'/'Y' date edit code
		if (edtCde == 'Y')
		{
			if (nlen == 7)
				edtWrd = " 0 /  /  ";
			else
			{
				if (nlen >= 8)
					edtWrd = "0 /  /    ";
				else
					edtWrd = "0 /  /    ".substring(0, nlen + (nlen - 1) / 2);
			}
		}
		else if (edtCde == 'W')
		{
			if (nlen == 5)
				edtWrd = "0 /   ";
			else if (nlen == 7)
				edtWrd = "  0 /  ";
			else
			{
				if (nlen >= 8)
					edtWrd = "  0 /  /  ";
				else
				{
					int i = nlen;
					if (i > 2)
						i = i - 2;
					edtWrd = "  0 /  /  ".substring(0, nlen + (i - 1) / 2);
				}
			}
		}
		else
		{
			char wrdBuf[] = new char[45];
			int e = 0;
			// Add leading minus
			if (edtCde >= 'N' && edtCde <= 'Q')
			{
				wrdBuf[0] = '-';
				e++;
			}
			// Figure out if commas are needed
			boolean comma = ("12ABJKNO".indexOf(edtCde) >= 0);
			int scale = nlen - precision;
			int scale3 = scale % 3;
			if (scale3 > 0)
			{
				"   ".getChars(0, scale3, wrdBuf, e);
				e += scale3;
				scale -= scale3;
			}
			// If the length is >3, print out comma-separated 3 letter chunks
			while (scale > 0)
			{
				if (comma)
				{
					wrdBuf[e] = ',';
					e++;
				}
				"   ".getChars(0, 3, wrdBuf, e);
				e += 3;
				scale -= 3;
			}
			// Add asterisk or floating currency symbol
			if (fillChar != ' ')
			{
				if (e > 0)
					e--;
				wrdBuf[e] = fillChar;
				e++;
				if (fillChar != '*' && fillChar != '0') 
				{
					wrdBuf[e] = '0';
					e++;
				}
			}
			// Add decimal point if needed
			if (precision > 0)
			{
				wrdBuf[e] = '.';
				e++;
				"                               ".getChars(0, precision, wrdBuf, e);
				e += precision;
			}
			// Add trailing CR
			if (edtCde >= 'A' && edtCde <= 'D')
				"CR".getChars(0, 2, wrdBuf, e);
			else
			{
				// Add trailing minus
				if (edtCde >= 'J' && edtCde <= 'M')
				{
					wrdBuf[e] = '-';
					e++;
				}
				//*e='\0';
			}
			edtWrd = new String(wrdBuf, 0, e);
		}
		return edtWrd;
	}
	protected int column, row;
	int cur_page_line_num;
	protected int maxColumn;
	//char	*outputBuffer;
	char outputBuffer[] = new char[379];
	// Record buffer with room for first character form control character
	protected int outputSize;
	//int printLength;
	public int page = 1;

	int pageWidth;
	public RrecordPrint(String recordName)
	{
		super(recordName);
		maxColumn = outputBuffer.length - 1;
		clearBuffer(true);
	}
	/**
	 * Clear out the output buffer and reset column values
	 * @param reset Whether to reset column values or not
	 */
	protected void clearBuffer(boolean reset)
	{
		for (int i = 1; i <= maxColumn; i++)
			outputBuffer[i] = ' ';
		if (reset)
		{
			maxColumn = 0;
			column = 1;
		}
	}
	
	/** 
	 * Edit the specified value using the specified edit word and place the results into this record buffer
	 * at the specified index. */
	private void edit(int bufOffset, INumeric n, String edtWrd)
	{
		RrecordPrint.editBuffer(bufOffset, n, edtWrd, outputBuffer);
	}
	
	/** Flush the contents of the current output buffer to the page. */
	abstract public void flush() throws /*java.io.IO*/ Exception;
	
	/** Convert a long value to a zoned with an appropriate scale. */
	private zoned longToZoned(long value)
	{
		// Calculate the scale of the number, and use that precision to print out the value
		int scale = 1;
		if (value < 0)
			value = Math.abs(value);
		if (value >= 10)
			//scale = (int)(Math.log(value) / Math.log(10))+1;
			scale = (int)Application.log10(value)+1;
		zoned z = new zoned(scale, 0, value);
		return z;
	}
	
	/** Print a value at the current column value.*/
	public void print(char c)
	{
		print(c, column);
	}
	/** Print a value at the specified column.*/
	public void print(char c, int col)
	{
		setMaxColumn(col);
		outputBuffer[column] = c;
	}
	/** Print out a (right-adjusted) value at the current column. */
	public void print(IFixed value)
	{
		FixedData fStr = value.toFixedChar();
		print(fStr, column);
	}
	/** Print out a (right-adjusted) value at the specified column. */
	public void print(IFixed value, int col)
	{
		FixedData fStr = value.toFixedChar();
		printChar(fStr.toCharArray(), col);
	}
	/**
	 * Print out a (right-adjusted) numeric value at the specified column
	 */
	public void print(int value, int col)
	{
		/* Change so that print(long) always calculates the scale
		zoned z = new zoned(9,0,value);
		int scale = (int)(Math.log(value)/Math.log(10));
		print(z, col);
		*/
		print((long) value, col);
	}
	/** Print out a (right-adjusted) numeric value at the specified column after applying the specified edit character. */
	public void print(INumeric n, int col, char edtCde)
	{
		print(n, col, edtCde, ' ');
	}
	/** 
	 * Print out a (right-adjusted) numeric value at the specified column after applying the specified edit character
	 * and fill character. 
	 */
	public void print(INumeric n, int col, char edtCde, char fillChar)
	{
		// Edtcde(X) means no editing
		if (edtCde == 'X')
			print((IFixed) n, col);
		else
			print(n, col, getEdtWrd(n, edtCde, fillChar));
	}
	/** Print out a (right-adjusted) numeric value at the specified column after applying the specified edit word. */
	public void print(INumeric n, int col, String edtWrd)
	{
		setMaxColumn(col);

		int edtLen = edtWrd.length();
		if (edtLen > col)
			edtLen = col;
		// See if the positions in the record alrady have characters.  If they do,
		// then write out record.
		int bufNum = col - edtLen + 1;
		edit(bufNum, n, edtWrd);
		column++;
	}
	/** Print out a (right-adjusted) numeric value at the specified column. */
	public void print(long value, int col)
	{
		print(longToZoned(value), col);
	}
	/** Print out a (right-adjusted) numeric value at the specified column after applying the specified edit character. */
	public void print(long n, int col, char edtCde)
	{
		print(n, col, edtCde, ' ');
	}
	/** 
	 * Print out a (right-adjusted) numeric value at the specified column after applying the specified edit character
	 * and fill character. 
	 */
	public void print(long n, int col, char edtCde, char fillChar)
	{

		zoned z = longToZoned(n);
		print(z, col, edtCde, fillChar);
	}
	/** Print out a (right-adjusted) numeric value at the specified column after applying the specified edit word. */
	public void print(long n, int col, String edtWrd)
	{
		// Calculate a zoned with a value appropriate to the length of the edit word
		//zoned z=longToZoned(n);
		int length = edtWrd.length();
		int scale = 0;
		for (int i = 0; i < length; i++)
		{
			char edtWrdi = edtWrd.charAt(i);
			if (edtWrdi == ' ' || edtWrdi == '0')
				scale++;
		}
		if (scale == 0)
			scale = 1;
		zoned z = new zoned(scale, 0, n);
		print(z, col, edtWrd);
	}
	/** Print a (right-adjusted) value at the current column value.*/
	public void print(String str)
	{
		print(str, column);
	}
	/** Print a (right-adjusted) value at the specified column value.*/
	public void print(String str, int col)
	{
		printChar(str.toCharArray(), col);
	}
	/**
	 * Print out an array of character data
	 *
	 * @param str 		The array to print
	 * @param edtLen 	The length of data to print
	 * @param col 		The column to print to
	 */
	protected void printChar(char str[], int col)
	{
		int edtLen = str.length;
		setMaxColumn(col);

		if (edtLen > col)
			edtLen = col;
		// See if the positions in the record alrady have characters.  If they do,
		// then write out record.
		int bufOffset = col - edtLen + 1;
		for (int i = 0; i < edtLen; i++)
		{
			outputBuffer[bufOffset] = str[i];
			bufOffset++;
		}

		column++;
	}
	/** Print a left-adjusted value at the current column. */
	public void printl(char value)
	{
		printl(value, column);
	}
	/** Print a left-adjusted value at the specified column. */
	public void printl(char value, int col)
	{
		printl(new Character(value).toString(), col);
	}
	/** Print a left-adjusted value at the current column. */
	public void printl(IFixed value)
	{
		FixedData fStr = value.toFixedChar();
		printl(fStr, column);
	}
	/** Print a left-adjusted value at the specified column. */
	public void printl(IFixed value, int col)
	{
		FixedData fStr = value.toFixedChar();
		if (col <= 0)
			col = column;
		print(fStr, col + fStr.size() - 1);
	}
	/** Print out a left-adjusted numeric value at the specified column after applying the specified edit character. */
	public void printl(INumeric n, int col, char edtCde)
	{
		printl(n, col, edtCde, ' ');
	}
	/** 
	 * Print out a left-adjusted numeric value at the specified column after applying the specified edit character
	 * and fill character. 
	 */
	public void printl(INumeric n, int col, char edtCde, char fillChar)
	{
		// Edtcde(X) means no editing
		if (edtCde == 'X')
			printl((fixed) n, col);
		else
			printl(n, col, getEdtWrd(n, edtCde, fillChar));
	}
	/** Print out a left-adjusted numeric value at the specified column after applying the specified edit word. */
	public void printl(INumeric n, int col, String edtWrd)
	{
		print(n, col + edtWrd.length() - 1, edtWrd);
	}
	/** Print a left-adjusted value at the current column. */
	public void printl(String str)
	{
		printl(str, column);
	}
	/** Print a left-adjusted value at the specified column. */
	public void printl(String str, int col)
	{
		fixed f = new fixed(str.length(), str);
		printl(f, col);
	}

	/** Set the maximum (right-most) column printed to in this record format. */
	protected void setMaxColumn(int col)
	{
		if (col <= 0)
			col = column;
		else
		{
			column = col;
			if (column > maxColumn)
				maxColumn = column;
		}
	}
	
	/** Skip to the specified row of the output page.  If the current row number is greater than
	 * <code>rows</code>, then skip to the specified line on the next page.
	 *  @param rows the row number to skip to 
	*/
	abstract public void skip(int rows) throws /*java.io.IO*/ Exception;
	
	/** Space the specified number of lines -- effectively, a carriage return for each <code>rows</code>.
	 * Until a <code>space</code> or <code>skip</code> command
	 * is executed, output continues to be written to the current line (overprint). 
	 *  @param rows the number of rows to space down.
	*/
	abstract public void space(int rows) throws /*java.io.IO*/ Exception;
	
	/** Update the page number. */
	protected void updatePage() throws java.io.IOException
	{
	}
}
