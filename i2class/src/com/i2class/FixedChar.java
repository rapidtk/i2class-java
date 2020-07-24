package com.i2class;

/**
 * A mutable fixed-length character string class.
 * The data for the class is stored in <code>overlay</code>, which is a <code>byte</code> array -- double-byte data would have to be stored across multiple elements.
 * <P>
 * RPG has the concept of subfields, where variables can overlay the memory used to store other variables.  
 * For example, the RPG code:
 * <pre>
 * I            DS
 * I                                        1   2 0 PAYMN
 * I                                        3   4 0 PAYDY
 * I                                        5   6 0 PAYYR
 * I                                        1   6 0 PAYDTE
 * </pre>
 * describes 6 bytes of memory.  Bytes 1-2 are the variable PAYMN (payment month), 
 * bytes 3-4 are the variable PAYDY (payment day), and bytes 5-6 are the variable PAYYR (payment year).  
 * Bytes 1-6 are the variable PAYDTE (payment date), so whenever PAYDTE changes, PAYMN, PAYDY, and PAYYR also change.
 * That also means that when PAYMN, PAYDY, or PAYYR change, PAYDTE changes as well (since they are all represented by the same bytes of memory).  
 * Subfields are similar to a C union or a C++ reference.
 * Wherever an overlay occurs, I2 inserts appropriate code to automatically map the subfields, creating code in 
 * the <code>readSubfields</code> and <code>updateSubfields</code> method.
 * <pre>
 * int           paymn;
 * int           paydy;
 * int           payyr;
 * class PAYDTE extends zoned {
 *    PAYDTE() { super(6,0); }
 *    protected void readSubfields() {
 *       setFixedAt(0,new zoned(2,0,paymn));
 *       setFixedAt(2,new zoned(2,0,paydy));
 *       setFixedAt(4,new zoned(2,0,payyr));
 *    } 
 *    protected void updateSubfields() {      
 *       paymn=zonedAt(0,2, 0).intValue();   
 *       paydy=zonedAt(2,2, 0).intValue();   
 *       payyr=zonedAt(4,2, 0).intValue();   
 *    }                                      
 * }                                         
 * </pre>  
 * 
 */
public class FixedChar extends FixedData implements CharSequence
{

	/** An fixed value with no allocated buffer. */
	protected FixedChar()
	{
		super();
	}

	/**
	 * Create a fixed length character string initialized to all blanks.
	 * @param sz The maximum size of the character string
	 */
	public FixedChar(int sz)
	{
		super(sz);
		pad(0);
	}
	
	/**
	 * Create a fixed length character string initialized to a certain offset of a byte string.
	 * @param sz The maximum size of the character string
	 * @param array The byte array that the initial value will be set to
	 * @param int index The 1-based index into <code>array</code> to begin copying values from
	 */
	FixedChar(int sz, byte array[], int index)
	{
		super(sz, array, index);
	}

	/**
	 * Create a fixed length character string initialized to a character value.
	 * @param sz The maximum size of the character string
	 * @param c The initial character value
	 */
	public FixedChar(int sz, char c)
	{
		super(sz);
		assign(c);
	}
	/**
	 * Create a fixed length character initialized to a figurative constant.
	 * @param sz The maximum size of the character string
	 * @param fc The initial figurative constant value
	 */
	public FixedChar(int sz, FigConst fc)
	{
		super(sz);
		assign(fc);
	}
	/**
	 * @param sz
	 * @param overlay
	 */
	public FixedChar(int sz, FixedPointer overlay)
	{
		super(sz, overlay);
	}

	/**
	 * Create a fixed length character string initialized to a String value.
	 * @param sz The maximum size of the character string
	 * @param str The initial String value
	 */
	public FixedChar(int sz, String str)
	{
		super(sz);
		assign(str);
	}
	/**
	 * Assign a boolean value to a fixed-length string - true='1', false='0'.
	 */
	public void assign(boolean value)
	{
		FigConst c;
		if (value)
			c = Application.ONES;
		else
			c = Application.ZEROS;
		assign(c);
	}

	/**
	 * Assign a character value to this fixed-length string.
	 */
	public void assign(char value)
	{
		/*
		for (int i = 1; i < overlay.length; i++)
			overlay[i] = (byte) ' ';
		*/
		pad(1);
		setCharAt(0, value);
		setVlength(1);
	}
	/** Assign a formatted date (for example YEAR) */
	public void assign(FmtDate value)
	{
		movel(value);
	}
	/**
	 * Assign a figurative constant value (for example *LOVAL, *HIVAL) to this fixed-length string.
	 */
	public void assign(FigConst fc)
	{
		//movel(fc);
		/*
		length = overlay.length;
		for (int i = 0; i < length; i++)
			overlay[i] = fc.fillChar;
		*/
		fillArray(fc.fillChar);
		updateThis();
	}
	/**
	 * Assign another fixed-string value to this fixed-length string.
	 */
	public void assign(FixedChar fStr)
	{
		int vlength = java.lang.Math.min(fStr.len(), msize());
		pad(vlength);
		movelFixedData(fStr);
		setVlength(vlength);
	}
	/**
	 * Assign a string value to this fixed-length string.
	 */
	public void assign(String value)
	{
		int vlength = java.lang.Math.min(value.length(), msize());
		pad(vlength);
		movelString(value);
		setVlength(vlength);
	}
	/**
	 * Move the right-most characters of a value to the right-most positions of a fixed-length variable.
	 */
	public void assignr(FixedChar fStr)
	{
		fStr.rSubfields();
		int msize = msize();
		int blankLen = msize - fStr.msize();
		padl(blankLen);
		move(fStr);
		setVlength(msize);
	}
	/**
	 * Move the right-most characters of a value to the right-most positions of a fixed-length variable.
	 */
	/*
	public void assignr(Object value)
	{
		assignr(value.toString());
	}
	*/
	/**
	 * Move the right-most characters of a value to the right-most positions of a fixed-length variable.
	 */
	public void assignr(String str)
	{
		int msize = msize();
		int blankLen = msize - str.length();
		padl(blankLen);
		move(str);
		setVlength(msize);
	}

	/** Return the boolean value of this variable.
	 * @return true if the value is all '1' (*ON)
	 */
	public boolean booleanValue()
	{
		return equals(true);
	}

	/** Add a character value to this fixed-length string, and return a new value. */
	public FixedChar add(char c1)
	{
		FixedChar result = new FixedChar(len() + 1);
		result.assign(toString() + c1);
		return result;
	}

	/** Add a String value to this fixed-length string, and return a new value. */
	public FixedChar add(String str1)
	{
		FixedChar result = new FixedChar(len() + str1.length());
		result.assign(toString() + str1);
		return result;
	}

	/** Add a FixedChar value to this fixed-length string, and return a new value. */
	public FixedChar add(FixedChar fStr)
	{
		FixedChar result = new FixedChar(len() + fStr.len());
		result.assign(toString() + fStr.toString());
		return result;
	}

/** Concatenate a character value to this fixed-length string. */
	public void cat(char c1)
	{
		cat(c1, 0);
	}
/** Concatenate a character value to this fixed-length string. 
 * @param c1 The character value to concatenate
 * @param blanks The number of blank spaces to leave between the last non-blank value of this string
 * and the concatenated value.
 */
	public void cat(char c1, int blanks)
	{
		byte[] c = new byte[1];
		c[0] = (byte) c1;
		catStr(c, 0, 1, blanks);
	}
/** Concatenate a character value to this fixed-length string. 
 * @see #cat(char c1, int blanks)
 */
	public void cat(char c1, INumeric blanks)
	{
		cat(c1, blanks.intValue());
	}
/** Concatenate another string to this fixed-length string. */
	public void cat(FixedChar t2)
	{
		cat(t2, 0);
	}
/** Concatenate another string to the blank-trimmed value of this fixed-length string. 
 * @param t2 The string value to concatenate
 * @param blanks The number of blank spaces to leave between the last non-blank value of this 
 * fixed-length string and the string
 */
	public void cat(FixedChar t2, int blanks)
	{
		catStr(t2.getOverlay(), t2.getOffset(), t2.checkr(' '), blanks);
	}
/** Concatenate another string to the blank-trimmed value of this fixed-length string. 
 * @see #cat(FixedChar t2, int blanks)
 */
	public void cat(FixedChar t2, INumeric blanks)
	{
		cat(t2, blanks.intValue());
	}
/** Concatenate another string to this fixed-length string. */
	public void cat(String t2)
	{
		cat(t2, 0);
	}
/** Concatenate another string to the blank-trimmed value of this fixed-length string. 
 * @see #cat(FixedChar t2, int blanks)
 */
	public void cat(String t2, int blanks)
	{
		catStr(t2.getBytes(), 0, Application.checkr(' ', t2), blanks);
	}
/** Concatenate another string to the blank-trimmed value of this fixed-length string. 
 * @see #cat(FixedChar t2, int blanks)
 */
	public void cat(String t2, INumeric blanks)
	{
		cat(t2, blanks.intValue());
	}

/** 
 * Concatenate two byte arrays together using the specified number of blanks.
 * Used by all of the cat(...) functions.
 */
	private void catStr(byte[] s2, int s2Offset, int s2Len, int blanks)
	{
		int offset = checkr(' ') + blanks;
		if (offset < msize())
			arrayCopy(offset, s2, s2Offset, s2Len);
	}
	
	//TODO: maybe optimize this a little...
	public FixedChar plus(CharSequence seq1) {
		String str1 = this.toString() + seq1.toString();
		return new FixedChar(str1.length(), str1);
	}

/** 
 * Return the left-most position of this fixed-length string that is not set to the 
 * specified character value - check(' ') is essentially triml().
 */
	public int check(char c)
	{
		return check(c, 1);
	}
/** 
 * Beginning at <code>start</code>, return the left-most position of this fixed-length string that is not set to the 
 * specified character value.
 * @param c the character value to trim
 * @param start The 1-based index to begin checking from.
 */
	public int check(char c, int start)
	{
		rSubfields();
		start--;
		int msize=msize();
		for (; start < msize; start++)
			if (sbyteAt(start) != c)
				return scanFound(start);
		return scanFound(-1);
	}
/** 
 * Beginning at <code>start</code>, return the left-most position of this fixed-length string that is not set to the 
 * specified character value.
 * @see #check(char c, int start)
 */
	public int check(char c, INumeric start)
	{
		return check(c, start.intValue());
	}
/** 
 * Return the left-most position of this fixed-length string that is not set to any of the 
 * values in the specified string.
 */
	public int check(FixedChar fStr)
	{
		return check(fStr, 0);
	}
/** 
 * Beginning at <code>start</code>, return the left-most position of this fixed-length string that is not set to any of the 
 * values in the specified string.
 * @param fStr the string of values to check against
 * @param start The 1-based index to begin checking from.
 */
	public int check(FixedChar fStr, int start)
	{
		fStr.rSubfields();
		rSubfields();
		start--;
		int msize=msize();
		for (; start < msize; start++)
			if (fStr.scan(sbyteAt(start)) == 0)
				return scanFound(start);
		return scanFound(-1);
	}
/** 
 * Beginning at <code>start</code>, return the left-most position of this fixed-length string that is not set to any of the 
 * values in the specified string.
 * @see #check(FixedChar fStr, int start)
 */
	public int check(FixedChar fStr, INumeric start)
	{
		return check(fStr, start.intValue());
	}
/** 
 * Return the left-most position of this fixed-length string that is not set to any of the 
 * values in the specified string.
 */
	public int check(String str)
	{
		return check(str, 1);
	}
/** 
 * Beginning at <code>start</code>, return the left-most position of this fixed-length string that is not set to any of the 
 * values in the specified string.
 * @see #check(FixedChar fStr, int start)
 */
	public int check(String str, int start)
	{
		rSubfields();
		start--;
		int msize=msize();
		for (; start < msize; start++)
			if (str.indexOf(sbyteAt(start)) == -1)
				return scanFound(start);
		return scanFound(-1);
	}
/** 
 * Beginning at <code>start</code>, return the left-most position of this fixed-length string that is not set to any of the 
 * values in the specified string.
 * @see #check(FixedChar fStr, int start)
 */
	public int check(String str, INumeric start)
	{
		return check(str, start.intValue());
	}

/** 
 * Return the right-most position of this fixed-length string that is not set to the 
 * specified character value - checkr(' ') is essentially trimr().
 */
	public int checkr(char c)
	{
		return checkr(c, len());
	}
/** 
 * Beginning at <code>start</code> and moving backwards, return the right-most position of this fixed-length string that is not set to the 
 * specified character value.
 * @param c the character value to trim
 * @param start The 1-based index to begin checking from.
 */
	public int checkr(char c, int start)
	{
		rSubfields();
		start--;
		for (; start >= 0; start--)
			if (sbyteAt(start) != c)
				break;
		return scanFound(start);
	}
/** 
 * Beginning at <code>start</code>, return the right-most position of this fixed-length string that is not set to the 
 * specified character value.
 * @see #checkr(char c, int start)
 */
	public int checkr(char c, INumeric start)
	{
		return checkr(c, start.intValue());
	}
/** 
 * Return the right-most position of this fixed-length string that is not set to any of the 
 * values in the specified string.
 */
	public int checkr(FixedChar fStr)
	{
		return checkr(fStr, 0);
	}
/** 
 * Beginning at <code>start</code>, return the right-most position of this fixed-length string that is not set to 
 * any of the values in the specified string.
 * @param fStr the string of values to check against
 * @param start The 1-based index to begin checking from.
 */
	public int checkr(FixedChar fStr, int start)
	{
		rSubfields();
		fStr.rSubfields();
		start--;
		for (; start >= 0; start--)
			if (fStr.scan(sbyteAt(start)) == 0)
				break;
		return scanFound(start);
	}
/** 
 * Beginning at <code>start</code>, return the right-most position of this fixed-length string that is not set to 
 * any of the values in the specified string.
 * @see #checkr(FixedChar fStr, int start)
 */
	public int checkr(FixedChar fStr, INumeric start)
	{
		return checkr(fStr, start.intValue());
	}
/** 
 * Return the right-most position of this fixed-length string that is not set to any of the 
 * values in the specified string.
 */
	public int checkr(String str)
	{
		return checkr(str, len());
	}
/** 
 * Beginning at <code>start</code>, return the right-most position of this fixed-length string that is not set to 
 * any of the values in the specified string.
 * @see #checkr(FixedChar fStr, int start)
 */
	public int checkr(String str, int start)
	{
		rSubfields();
		start--;
		for (; start >= 0; start--)
			if (str.indexOf(sbyteAt(start)) < 0)
				break;
		return scanFound(start);
	}
/** 
 * Beginning at <code>start</code>, return the right-most position of this fixed-length string that is not set to 
 * any of the values in the specified string.
 * @see #checkr(FixedChar fStr, int start)
 */
	public int checkr(String str, INumeric start)
	{
		return checkr(str, start.intValue());
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.FixedData#clear()
	 */
	public void clear() {
		assign(" ");
	}


	/**
	 * Compare the fixed value to a boolean.  *ON would compare against all '1', *OFF against all '0'
	 */
	public int compareTo(boolean value)
	{
		rSubfields();
		char c = '0';
		if (value)
			c = '1';
		int len=len();
		for (int i = 0; i < len; i++)
		{
			int dif = c - sbyteAt(i);
			if (dif != 0)
				return dif;
		}
		return 0;
	}

	public int compareTo(char c)
	{
		rSubfields();
		char ubyteAt0=ubyteAt(0);
		if (ubyteAt0 != c)
			return ubyteAt0 - c;
		int msize=msize();
		for (int i = 1; i<msize; i++)
		{
			if (sbyteAt(i) != ' ')
				return 1;
		}
		return 0;
	}
	protected int compareTo(FigConst fc)
	{
		rSubfields();
		int len=len();
		for (int i = 0; i < len; i++)
		{
			int dif = sbyteAt(i) - fc.charAt(0);
			if (dif != 0)
				return dif;
		}
		return 0;
	}
	private int compareTo(FixedChar fStr)
	{
		rSubfields();
		fStr.rSubfields();
		int i = 0;
		int len=len();
		int fStrLen = fStr.len();
		for (; i < len && i < fStrLen; i++)
		{
			int dif = sbyteAt(i) - fStr.sbyteAt(i);
			if (dif != 0)
				return dif;
		}
		for (; i < len; i++)
			if (charAt(i) != ' ')
				return 1;
		for (; i < fStrLen; i++)
			if (fStr.charAt(i) != ' ')
				return -1;
		return 0;
	}
	/**
	 * Compare to another object.
	 * @throws ClassCastException if <code>o</code> in not one of 
	 * <code>fixed</code>, <code>String</code> or
	 * <code>FigConst</code>
	 */
	public int compareTo(Object o) throws ClassCastException
	{
		if (o instanceof FixedChar)
			return compareTo((FixedChar) o);
		if (o instanceof String)
			return compareTo((String) o);
		if (o instanceof FigConst)
			return compareTo((FigConst) o);
		throw new ClassCastException("Cannot cast to fixed");
	}
	public int compareTo(String str)
	{
		rSubfields();
		/*
			int i=overlay.length-1;
			while (overlay[i]==(byte)' ')
				i--;
			String s = new String(overlay, 0, i+1);	
			i=s.compareTo(str);
			return i;
		*/
		int i = 0;
		int strLength = str.length();
		int len=len();
		for (; i < len && i < strLength; i++)
		{
			int dif = /*(byte)*/
			sbyteAt(i) - str.charAt(i);
			if (dif != 0)
				return dif;
		}
		for (; i < len; i++)
			if (charAt(i) != (byte) ' ')
				return 1;
		for (; i < strLength; i++)
			if (str.charAt(i) != ' ')
				return -1;
		return 0;
	}
	/**
	 * Compare this fixed length string with a boolean value.
	 * When comparing to boolean values, <code>true</code>='1', <code>false</code>='0'.
	 */
	public boolean equals(boolean value)
	{
		return (compareTo(value)==0);
	}
	/** Compare this fixed-length string to a character value. */
	public boolean equals(char c)
	{
		return (compareTo(c) == 0);
	}
	/** Compare this fixed-length string to a figurative constant (for example *HIVAL, *LOVAL). */
	public boolean equals(FigConst fc)
	{
		return (compareTo(fc) == 0);
	}
	/** Compare this fixed-length string to another fixed-length string. */
	public boolean equals(FixedChar str)
	{
		return (compareTo(str) == 0);
	}
	/** Compare this fixed-length string to a Java String. */
	public boolean equals(String str)
	{
		return (compareTo(str) == 0);
	}
	/** 
	 * Scan this fixed length string for the occurrence of a boolean value - true='1', false='0'. 
	 * @return the 1-based index of the occurrence of the search value, or 0 if none found. 
	 */
	public int scan(byte c)
	{
		return scan(c, 1);
	}
	/** 
	 * Scan this fixed length string for the occurrence of a boolean value - true='1', false='0'. 
	 * @param c the value to scan for 
	 * @param startIndex the 1-based index to start the search from 
	 * @return the 1-based index of the occurrence of the search value, or 0 if none found. 
	 */
	public int scan(byte c, int startIndex)
	{
		rSubfields();
		int msize=msize();
		for (int i = startIndex - 1; i < msize; i++)
			if (sbyteAt(i) == c)
				return scanFound(i);
		return scanFound(-1);
	}
	/** 
	 * Scan this fixed length string for the occurrence of a boolean value - true='1', false='0'. 
	 * @param c the value to scan for 
	 * @param startIndex the 1-based index to start the search from 
	 * @return the 1-based index of the occurrence of the search value, or 0 if none found. 
	 */
	public int scan(byte c, INumeric startIndex)
	{
		return scan(c, startIndex.intValue());
	}
	/** 
	 * Scan this fixed length string for the occurrence of a character value. 
	 * @return the 1-based index of the occurrence of the search value, or 0 if none found. 
	 */
	public int scan(char c)
	{
		return scan((byte) c);
	}
	/** 
	 * Scan this fixed length string for the occurrence of another string. 
	 * @return the 1-based index of the occurrence of the search value, or 0 if none found. 
	 */
	public int scan(FixedChar fStr)
	{
		return scan(fStr, 1);
	}
	/** 
	 * Scan this fixed length string for the occurrence of another string. 
	 * @param fStr the value to scan for 
	 * @param startIndex the 1-based index to start the search from 
	 * @return the 1-based index of the occurrence of the search value, or 0 if none found. 
	 */
	public int scan(FixedChar fStr, int startIndex)
	{
		fStr.rSubfields();
		rSubfields();
		int fStrLen=fStr.msize();
		int end=msize()-fStrLen;
		out : for (int i = startIndex - 1; i < end; i++)
		{
			int found = i;
			for (int j = 0; j < fStrLen; j++)
			{
				if (sbyteAt(i) != fStr.sbyteAt(j))
					continue out;
				i++;
			}
			return scanFound(found);
		}
		return scanFound(-1);
	}
	/** 
	 * Scan this fixed length string for the occurrence of another string. 
	 * @param fStr the value to scan for 
	 * @param startIndex the 1-based index to start the search from 
	 * @return the 1-based index of the occurrence of the search value, or 0 if none found. 
	 */
	public int scan(FixedChar fStr, INumeric startIndex)
	{
		return scan(fStr, startIndex.intValue());
	}
	/** 
	 * Scan this fixed length string for the occurrence of another string. 
	 * @see #scan(FixedChar fStr)
	 */
	public int scan(String str)
	{
		return scan(str, 1);
	}
	/** 
	 * Scan this fixed length string for the occurrence of another string. 
	 * @see #scan(FixedChar fStr, int startIndex)
	 */
	public int scan(String str, int startIndex)
	{
		rSubfields();
		int strLength = str.length();
		int len=len();
		int end=len-strLength;
		out : for (
			int i = startIndex - 1; i < end; i++)
		{
			int found = i;
			for (int j = 0; j < strLength; j++)
			{
				if (sbyteAt(i) != str.charAt(j))
					continue out;
				i++;
			}
			return scanFound(found);
		}
		return scanFound(-1);
	}
	
	//TODO This is supposed to set the %found attribute??? 
	int scanFound(int index)
	{
		// found = index+1???
		return index+1;
	}
	
	/** 
	 * Scan this fixed length string for the occurrence of another string. 
	 * @see #scan(FixedChar fStr, int startIndex)
	 */
	public int scan(String str, INumeric startIndex)
	{
		return scan(str, startIndex.intValue());
	}
	
	/** Return the substring from the 1-based <code>start</code> to the end of this fixed-length string. */
	public String subst(int start)
	{
		return subst(start, len() - start + 1);
	}
	/** Return a substring no longer than <code>length</code> from the 1-based <code>start</code>. */
	public String subst(int start, int length)
	{
		rSubfields();
		return new String(getOverlay(), getOffset()+(start - 1), length);
	}
	/** Return a substring no longer than <code>length</code> from the 1-based <code>start</code>. */
	public String subst(int start, INumeric length)
	{
		return subst(start, length.intValue());
	}
	/** Return the substring from the 1-based <code>start</code> to the end of this fixed-length string. */
	public String subst(INumeric start)
	{
		return subst(start.intValue());
	}
	/** Return a substring no longer than <code>length</code> from the 1-based <code>start</code>. */
	public String subst(INumeric start, int length)
	{
		return subst(start.intValue(), length);
	}
	/** Return a substring no longer than <code>length</code> from the 1-based <code>start</code>. */
	public String subst(INumeric start, INumeric length)
	{
		return subst(start.intValue(), length.intValue());
	}

	/** Return this string with all blanks trimmed from both sides. */
	public String trim()
	{
		return toString().trim();
	}

	/** Return this string with all blanks trimmed from the left side. */
	public String triml()
	{
		rSubfields();
		int i = 0;
		int len=len();
		while (i < len && sbyteAt(i) == ' ')
			i++;
		return subst(i);
	}
	/** Return this string with all blanks trimmed from the right side. */
	public String trimr()
	{
		rSubfields();
		int i = len() - 1;
		while (i >= 0 && sbyteAt(i) == ' ')
			i--;
		return subst(1, i+1);
	}
	/** Pad (blank out) the characters >= offset */
	protected void pad(int offset)
	{
		if (offset<msize())
			fillArray((byte)' ', offset);
	}
	/** Pad (blank out) the characters < offset */
	void padl(int offset)
	{
		if (offset > 0)
		{
			int moffset=getOffset();
			// m_size-msize() compensates for variable-length strings and 2-byte length
			java.util.Arrays.fill(getOverlay(), moffset, moffset+offset+m_size-msize(), (byte)' ');
		}
	}
	
	// Returns the item at the specified index
	
	/** Allow CL-style assignment from a numeric variable type. */
	public void chgvar(AbstractNumeric n)
	{
		assign(n.toString());
	}	
	/** Allow CL-style assignment from a numeric variable type. */
	public void chgvar(long l)
	{
		assign(Long.toString(l));
	}

	//@Override
	public int length() {
		return len();
	}

	//@Override
	public CharSequence subSequence(int start, int end) {
		return subst(start-1, end-start);
	}	
}
