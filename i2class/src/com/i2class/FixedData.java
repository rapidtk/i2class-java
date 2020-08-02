package com.i2class;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Vector;

/**
 * The base class for all fixed-length, mutable I2 data.
 * 
 * 
 */
public abstract class FixedData implements Comparable, Cloneable, IFixed, Serializable
{
	/** The pointer to the actual buffer of data. */
	FixedPointer m_ptr;
	/** The actual length of the data */
	int m_size;
	
	/*private*/protected boolean updating = false;
	private Vector overlayFields;
	
	
	/* *
	 * Create a fixed-length string whose buffer is based upon another fixed.
	 * @version 11/4/2002 10:05:10 AM
	 * @param fStr the 'other' fixed length string
	*/
	/*
	private fixed(fixed fStr) 
	{
		setOverlay(fStr);
	}
	*/

	protected FixedData()
	{
	}

	protected void construct(int sz)
	{
		m_ptr = new FixedPointer(new byte[sz], 0);
		m_size = sz;
	}
	/**
	 * Create a fixed length type. 
	 * @param sz The maximum size of the data (in bytes)
	 */
	protected FixedData(int sz)
	{
		construct(sz);
	}
	/**
	 * Create a fixed length type that overlays another field. 
	 * @param sz The maximum size of the data (in bytes)
	 */
	protected FixedData(int sz, FixedPointer overlay)
	{
		m_ptr = overlay;
		m_size = sz;
	}
	/**
	 * Create fixed length data initialized to a certain offset of a byte string.
	 * @param sz The maximum size of the fixed-length data
	 * @param array The byte array that the initial value will be set to
	 * @param int index The 0-based index into <code>array</code> to begin copying values from
	 */
	protected FixedData(int sz, byte array[], int index)
	{
		construct(sz);
		//for (int i=0; i<overlay.length && i<array.length; i++)
		//	overlay[i]=array[i];
		////int min = java.lang.Math.min(sz, array.length - index + 1);
		//System.arraycopy(array, index - 1, overlay, 0, min);
		////System.arraycopy(array, index, getOverlay(), 0, min);
		assign(array, index);
	}
	/**
	 * Assign the value of a <code>byte</code> array to this fixed-length data.
	 * @param array The byte array to copy values from
	 */
	public void assign(byte[] array)
	{
		assign(array, 0);
	}
	/**
	 * Assign the value of a <code>byte</code> array to this fixed-length data.
	 * @param array The byte array to copy values from
	 * @param int index The 0-based index into <code>array</code> to begin copying values from
	 */
	public void assign(byte[] array, int offset)
	{
		arrayCopy(0, array, offset);
		updateThis();
	}
	
	/*
	void assign(long value)
	{
		movel(value);
	}
	*/
	
	/**
	 * Return the boolean value at the specified 0-based index.
	 * @return true if '1', false otherwise.
	 */
	public boolean booleanAt(int index)
	{
		return charAt(index)=='1';
	}
	
	/**
	 * Return the character value at the specified 0-based index.
	 */
	public char charAt(int index)
	{
		rSubfields();
		/// Arggggh (once again)!  Can't just cast to char here, because the sign (left-most) bit gets extended.
		//return (char)overlay[index];
		return ubyteAt(index);
	}

	/**
	 * Return the character value at the specified 0-based index.
	 */
	public char charAt(INumeric index)
	{
		return charAt(index.intValue());
	}

	/** Clear this data (0 for numeric, blanks for character, etc.) to it's default value. */
	public void clear() {
		// Loop through and clear any overlay fields
		if (overlayFields!= null) {
			for (int i=0; i<overlayFields.size(); i++) {
				FixedData overlayField = (FixedData)overlayFields.get(i);
				overlayField.clear();
			}
				
		}
	}
	
	void addOverlayField(FixedData overlayField) {
		if (overlayFields==null)
			overlayFields = new Vector();
		overlayFields.add(overlayField);
	}
	
	public Object clone()
	{
		rSubfields();
		try
		{
			FixedData fStr = (FixedData) (super.clone());
			fStr.m_ptr = new FixedPointer(new byte[msize()], 0);
			fStr.arrayCopy(0, this);
			return fStr;
		}
		catch (CloneNotSupportedException e)
		{
			return null;
		}
	}
	
	FixedData superclone()
	{
		FixedData f = null;
		try
		{
			f = (FixedData)super.clone();
		}
		catch (CloneNotSupportedException e){}
		return f;
	}

	/**
	 * Compare to another object.
	 */
	public abstract int compareTo(Object o) throws ClassCastException;

	/**
	 * Return the I2 date at the specified 0-based offset
	 */
	public FixedChar dateAt(int offset)
	{
		return fixedAt(offset, 10);
	}
	/**
	 * Return the I2 time at the specified 0-based offset
	 */
	public FixedChar timeAt(int offset)
	{
		return fixedAt(offset, 8);
	}
	/**
	 * Return the I2 timestamp at the specified 0-based offset
	 */
	public FixedChar timestampAt(int offset)
	{
		return fixedAt(offset, 26);
	}

	/** Compare this fixed-length data to a figurative constant (for example *HIVAL, *LOVAL). */
	public boolean equals(FigConst fc)
	{
		return (compareTo(fc) == 0);
	}
	/** Compare this fixed-length data to another. */
	public boolean equals(FixedData str)
	{
		return (compareTo(str) == 0);
	}
	/** Return the I2 fixed data type at the specified 0-based index. */
	protected FixedChar fixedAt(int offset, int len)
	{
		FixedChar f = new FixedChar(len, getBytes(), offset);
		return f;
	}
	/** Return a binary value at the specified 0-based index. */
	static long getBinary(byte[] array, int offset, int bytes)
	{
		long l = 0;
		int shift = bytes;
		for (int j = 0; j < bytes; j++)
		{
			// We have to be careful here because any byte value>128 is considered negative
			////l=(l*256)+array[offset+j];
			//l |= array[offset+j] << (bytes-1)*8;
			long a = array[offset + j] & 0xFF;
			a = a << (shift - 1) * 8;
			l = l | a;
			shift--;
		}
		return l;
	}
	/** Decode a binary integer value from the fixed buffer. */
	long getBinary(int offset, int bytes)
	{
		return FixedData.getBinary(getOverlay(), getOffset()+offset, bytes);
	}
	
	/** Return the byte array associated with the fixed-length string. */
	public byte[] getBytes()
	{
		rSubfields();
		byte[] bytes;
		// If this is a stand-alone field or if it has the same length as the field it is defined over, we 
		// can just return the byte overlay...
		if (m_ptr.m_offset == 0 && m_ptr.m_overlay.length==m_size)
			bytes = getOverlay();
		// Otherwise we have to copy the value to a new array
		else
		{
			bytes = new byte[m_size];
			System.arraycopy(m_ptr.m_overlay, m_ptr.m_offset, bytes, 0, m_size);
		}
		return bytes;
	}
	/** Return the integer 'subfield' at the specified 0-based index. */
	protected int intAt(int index)
	{
		return (int) getBinary(index, 4);
	}

	/** Return the length (number of characters) of this fixed-length string. */
	public int len()
	{
		return size();
	}
	/** Return the long subfield at the specified 0-based index. */
	protected long longAt(int index)
	{
		return getBinary(index, 8);
	}
	/** Move the right-most characters of a character array to the right-most positions of this fixed-length string. */
	public FixedData move(char[] str)
	{
		return move(new String(str));
	}
	/** Move a character to the right-most positions of this fixed-length string. */
	public FixedData move(char c)
	{
		rSubfields();
		setCharAt(msize()-1, c);
		return updateThis();
	}
	
	/** Clear and then move a character to the right-most positions of this fixed-length string. */
	public FixedData movep(char c)
	{
		clear();
		return move(c);
	}
	/* * Move a figurative constant to this fixed-length string. 
	public FixedData move(FigConst fc)
	{
		return movel(fc);
	}
	*/
	
	/** Move the right-most characters of another fixed-length string to the right-most positions of this fixed-length string. */
	protected FixedData moveFixedData(FixedData fStr)
	{
		//fStr.readSubfields();
		fStr.rSubfields();
		return moveArrayData(fStr.getOverlay(), fStr.getOffset(), fStr.msize());
	}
	/** Move the right-most characters of another fixed-length string to the right-most positions of this fixed-length string. */
	protected FixedData moveArrayData(byte[] arrayData, int arrayOffset, int strLength)
	{
		int min;
		int length = size();
		if (strLength < length)
		{
			rSubfields();
			min = strLength;
		}
		else
			min = length;
		int begin=length-min;
		arrayCopyNocheck(begin, arrayData, arrayOffset+strLength-min, min);
		return updateThis();
	}
	

	/** Move the right-most characters of another fixed-length string to the right-most positions of this fixed-length string. */
	public FixedData move(IFixed value)
	{
		return moveFixedData(value.toFixedChar());
	}
	/** Clear and then move the right-most characters of another fixed-length string to the right-most positions of this fixed-length string. */
	public FixedData movep(IFixed value) throws Exception
	{
		clear();
		return move(value);
	}
	/** Move the right-most characters of a String to the right-most positions of this fixed-length string. */
	public FixedData move(String str)
	{
		int strLength = str.length();
		int length = size();
		int min = java.lang.Math.min(strLength, length);
		arrayCopy(length-min, str);
		return updateThis();
	}
	/** Clear and then move the right-most characters of a String to the right-most positions of this fixed-length string. */
	public FixedData movep(String str)
	{
		clear();
		return move(str);
	}
	/** Move a long value to the right-most characters of a String. */
	public FixedData move(long value)
	{
		return move(Long.toString(value));
	}
	/** Clear and then ove a long value to the right-most characters of a String. */
	public FixedData movep(long value)
	{
		clear();
		return move(value);
	}

	/**
	 * Move a boolean (indicator) value to the right-most position of a fixed-length field - <code>true</code>='1',
	 * <code>false</code>='0'.
	 */
	public FixedData move(boolean value)
	{
		char c = '0';
		if (value)
			c = '1';
		return move(c);
	}
	/** Clear and then move a boolean (indicator) value to the right-most position of a fixed-length field. */	
	public FixedData movep(boolean value)
	{
		clear();
		return move(value);
	}
	/**
	 * Move Array - fill the byte array associated with this fixed-length string with a certain value.
	 * For example, with a <code>fixed(3)</code> with the value "ABC", <code>movea(*LOVAL, 2)</code> would 
	 * change the contents to "A\0\0".
	 * @param fc The figurative constant 
	 * @param index The 1-based index into the fixed-length string to change
	 */
	public void movea(FigConst fc, int index)
	{
		if (index > 1)
			rSubfields();
		index--;
		int msize=msize();
		for (; index < msize; index++)
			setCharAt(index, (char)fc.fillChar);
		updateThis();
	}
	/**
	 * Move Array - set the byte array associated with this fixed-length string to a certain value.
	 * @see #movea(FigConst fc, int index)
	 */
	public void movea(FigConst fc, INumeric index)
	{
		movea(fc, index.intValue());
	}
	/**
	 * Move Array - move a string to the the byte array associated with this fixed-length string.
	 * For example, with a <code>fixed(3)</code> with the value "ABC", <code>movea("EFG", 2)</code> would 
	 * change the contents to "AEF".
	 * @param fStr The string to move
	 * @param index The 1-based index into the fixed-length string to change
	 */
	public void movea(FixedData fStr, int index)
	{
		arrayCopy(index-1, fStr);
		updateThis();
	}
	/**
	 * Move Array - move a string to the the byte array associated with this fixed-length string.
	 * @see #movea(FixedChar fStr, int index)
	 */
	public void movea(FixedData fStr, INumeric index)
	{
		movea(fStr, index.intValue());
	}
	/**
	 * Move Array - move a string to the the byte array associated with this fixed-length string.
	 * @see #movea(FixedChar fStr, int index)
	 */
	public void movea(String str, int index)
	{
		arrayCopy(index-1, str);
		updateThis();
	}
	/**
	 * Move Array - move a string to the the byte array associated with this fixed-length string.
	 * @see #movea(FixedChar fStr, int index)
	 */
	public void movea(String str, INumeric index)
	{
		movea(str, index.intValue());
	}
	/**
	 * Move Array - fill the byte array associated with this fixed-length string with a certain value.
	 * For example, with a <code>fixed(3)</code> with the value "ABC", <code>movea(*ON, 2)</code> would 
	 * change the contents to "A11".
	 * @param value The bolean value (true=ON='1', false=OFF='0'
	 * @param index The 1-based index into the fixed-length string to change
	 */
	public void movea(boolean value, int index)
	{
		if (index > 1)
			rSubfields();
		char c = '0';
		if (value)
			c = '1';
		index--;
		int msize=msize();
		for (; index < msize; index++)
			setCharAt(index, c);
		updateThis();
	}
	/**
	 * Move Array - fill the byte array associated with this fixed-length string with a certain value.
	 * @see #movea(boolean value, int index)
	 */
	public void movea(boolean value, INumeric index)
	{
		movea(value, index.intValue());
	}
	/**
	 * Move Array - move a character array to the the byte array associated with this fixed-length string.
	 * @see #movea(String fStr, int index)
	 */
	public void movea(char str[], int index)
	{
		movea(new String(str), index);
	}
	/** Move all - fill the byte array associated with this fixed-length string with a repeating occurence of a character. */
	public void moveall(char c)
	{
		moveall(c, 1);
	}
	/** 
	 * Move all - fill the byte array associated with this fixed-length string with a repeating occurence of a character. 
	 * @param c The character to move
	 * @param index The 1-based index into the fixed-length string to change
	 */
	public void moveall(char c, int index)
	{
		if (index > 1)
			rSubfields();
		index--;
		int msize=msize();
		for (; index < msize; index++)
			setCharAt(index,  c);
		updateThis();
	}
	/** 
	 * Move all - fill the byte array associated with this fixed-length string with a repeating occurence of a character. 
	 * @see #moveall(char c, int index)
	 */
	public void moveall(char c, INumeric index)
	{
		moveall(c, index.intValue());
	}
	/** 
	 * Move all - fill the byte array associated with this fixed-length string with a repeating occurence of a string. 
	 * For example, with a <code>fixed(6)</code> with the value "ABCDEF", <code>moveall("GH", 2)</code> would 
	 * change the contents to "AGHGHG".
	 * @param c The string to move
	 * @param index The 1-based index into the fixed-length string to change
	 */
	public void moveall(String str, int index)
	{
		if (index > 1)
			rSubfields();
		int strLength = str.length();
		//str.getBytes(0, str.length(), overlay, index);
		byte[] b = str.getBytes();
		index--;
		int msize=msize();
		while (index < msize)
		{
			arrayCopy(index, b, 0);
			index += strLength;
		};
		updateThis();
	}
	/** 
	 * Move all - fill the byte array associated with this fixed-length string with a repeating occurence of a string. 
	 * @see #moveall(String str, int index)
	 */
	public void moveall(String str, INumeric index)
	{
		moveall(str, index.intValue());
	}
	

	/** Move the left-most characters of a character array to the left-most positions of this fixed-length string. */
	public FixedData movel(char[] str)
	{
		return movel(new String(str));
	}
	/** Move a character to the left-most positions of this fixed-length string. */
	public FixedData movel(char c)
	{
		if (size() > 1)
			rSubfields();
		setCharAt(0, c);
		return updateThis();
	}
	/** Clear and then move a character to the left-most positions of this fixed-length string. */
	public FixedData movelp(char c)
	{
		clear();
		return movel(c);
	}
	/*
	public FixedData movel(FigConst fc)
	{
		for (int i = 0; i < overlay.length; i++)
			overlay[i] = fc.fillChar;
		return updateThis();
	}
	*/
	
	/* The actual movel implementation */
	protected FixedData movelFixedData(FixedData fStr)
	{
		//fStr.readSubfields();
		fStr.rSubfields();
		return movelArrayData(fStr.getOverlay(), fStr.getOffset(), fStr.msize());
	}
	
	/* The actual movel implementation */
	protected final FixedData movelArrayData(byte[] arrayData, int arrayOffset, int arrayLength)
	{
		arrayCopy(0, arrayData, arrayOffset, arrayLength);
		//return this;
		return updateThis();
	}

	/** Move the left-most characters of another fixed-length string to the left-most positions of this fixed-length string. */
	public FixedData movel(IFixed value)
	{
		FixedData fStr = value.toFixedChar();
		return movelFixedData(fStr);
		//return this;
	}
	/** Clear and then move the left-most characters of another fixed-length string to the left-most positions of this fixed-length string. */
	public FixedData movelp(IFixed value) throws Exception
	{
		clear();
		return movel(value);
	}

	/** Move the left-most characters of a String to the left-most positions of this fixed-length string. */
	protected FixedData movelString(String str)
	{
		return movelArrayData(str.getBytes(), 0, str.length());
	}

	/** Move the left-most characters of a String to the left-most positions of this fixed-length string. */
	public FixedData movel(String str)
	{
		return movelString(str);
	}
	
	/** Clear and then move the left-most characters of a String to the left-most positions of this fixed-length string. */
	public FixedData movelp(String str)
	{
		clear();
		return movel(str);
	}

	/** Move a long value to the left-most characters of a String. */
	public FixedData movel(long value)
	{
		return movel(Long.toString(value));
	}
	/** Clear and then move a long value to the left-most characters of a String. */
	public FixedData movelp(long value)
	{
		clear();
		return movel(value);
	}


	/**
	 * Move a boolean (indicator) value to the left-most position of a fixed-length field - <code>true</code>='1',
	 * <code>false</code>='0'.
	 */
	public FixedData movel(boolean value)
	{
		char c = '0';
		if (value)
			c = '1';
		return movel(c);
	}
	/** Clear and then move a boolean (indicator) value to the left-most position of a fixed-length field */
	public FixedData movelp(boolean value)
	{
		clear();
		return movel(value);
	}

	/**
	 * Return a packed decimal value from a fixed-length 'character' string.
	 * @param index index into fixed byte array
	 * @param sz the size of the packed decimal value to return
	 * @param scale the scale of the packed decimal value to return
	 */
	protected PackedDecimal packedAt(int index, int sz, int precision)
	{
		return new PackedDecimal(sz, precision, getBytes(), index);
	}
	/** A 'stub' implemented to read the contents of any subfields that 'overlay' this fixed-length string. */
	protected void readSubfields()
	{
	}
	/** Used internally to make sure that readSubfields() does not get called recursively. */
	final void rSubfields()
	{
		if (!updating)
		{
			boolean supdating=updating;
			updating=true;
			readSubfields();
			updating=supdating;
		}
	}

	/** 
	 * Encode a binary integer value into the fixed buffer 
	 * @throws SignificantDigitsLostException if the value to assign would require decimal digits to be lost.
	 */
	//* @throws IllegalArgumentException if the value for bytes is not one of 1(byte), 2(short), 4(int), or 8(long).
	protected void setBinary(long l, int offset, int bytes) throws SignificantDigitsLostException//, IllegalArgumentException
	{
		//if (bytes!=1 && bytes!=2 && bytes!=4 && bytes!=8)
		//	throw new IllegalArgumentException("Only binary values with 1,2,4 or 8 bytes are allowed");
		long l1=l;
		for (int j = bytes - 1; j >= 0; j--)
		{
			byte b = (byte)(l & 0xFF);
			setByteAt(offset + j, b);
			l = l >>> 8;
		}
		// If less than 8 bytes are set and everything that is left is 0xFF then this is ok
		if (l>0 && (l1>0 || l!=(java.lang.Math.pow(2, (8-bytes)*8))-1))
			throw new SignificantDigitsLostException(Long.toString((long)java.lang.Math.pow(2, bytes*8)).length(),0, Long.toString(l1));
	}
	
	/** Set the byte value at the specified 0-based index. */
	void setByteAt(int index, byte b)
	{
		getOverlay()[getOffset()+index] = b;
	}

	/** Set the character value at the specified 0-based index. */
	public void setCharAt(int index, char c)
	{
		setByteAt(index, (byte) c);
		updateThis();
	}
	/** Set the character value at the specified 0-based index to the left-most value of a fixed-length string. */
	public void setCharAt(int index, FixedChar value)
	{
		setCharAt(index, value.charAt(0));
	}
	/** Set the character value at the specified 0-based index to the left-most value of a fixed-length string. */
	public void setCharAt(INumeric index, FixedChar value)
	{
		setCharAt(index.intValue(), value);
	}
	/** Set the character value at the specified 0-based index to a boolean value - true='1', false='0'. */
	public void setBooleanAt(int index, boolean value)
	{
		byte b = '0';
		if (value)
			b = '1';
		setByteAt(index, b);
	}
	/** Set the character value at the specified 0-based index to a boolean value - true='1', false='0'. */
	public void setCharAt(int index, boolean value)
	{
		setBooleanAt(index, value);
	}

	/** Set the fixed-length 'subfield' at the specified 0-based index to another string. */
	public void setFixedAt(int offset, FixedData fStr)
	{
		//fStr.readSubfields();
		fStr.rSubfields();
		arrayCopy(offset, fStr);
	}

	/** Set the integer 'subfield' at the specified 0-based index. */
	protected void setIntAt(int offset, int i)
	{
		setBinary(i, offset, 4);
	}

	/** Set the <code>long</code> 'subfield' at the specified 0-based index. */
	protected void setLongAt(int offset, long l)
	{
		setBinary(l, offset, 8);
	}

	public void setOverlay(byte[] overlay)
	{
		m_ptr.m_overlay = overlay;
	}

	/**
	 * Sets the overlay of this object to another fixed string.
	 * This is necessary since a <code> fixed </code> is a mutable type. 
	 * This is equivalent to setting the reference (e.g. String x=y) of an 'unmutable' type.
	 * @param fStr com.i2class.fixed the <code> fixed </code> object to set a reference to
	 */
	public void setOverlay(FixedData fStr)
	{
		setOverlay(fStr.getBytes());
		updateThis();
	}

	/** Set the <code>short</code> 'subfield' at the specified 0-based index. */
	protected void setShortAt(int offset, int s)
	{
		setBinary(s, offset, 2);
	}
	
	/** Return the <code>short</code> 'subfield' at the specified 0-based index. */
	protected short shortAt(int offset)
	{
		return (short) getBinary(offset, 2);
	}
	
	/** Return the size (in bytes) of this fixed-length string. */
	final public int size()
	{
		return m_size;
	}

	/** Return the character array representation of this fixed-length string. */
	public char[] toCharArray()
	{
		rSubfields();
		//return toString().toCharArray();
		return toFixedString().toCharArray();
	}
	
	/** Returns the fixed-length character value (any decimal data is translated) of a fixed object. */
	public FixedData toFixedChar()
	{
		return this;
	}
	
	/**
	 * Return the fixed-string representation (as opposed to the human-friendly toString()) of this value.
	 * This returns the raw, unformatted values of the byte string.
	 */
	public String toFixedString()
	{
		rSubfields();
		// This is insane, but we need to check to make sure that all of the values in the byte[] array are consistent,
		// that means that they must fall in the range of 0x20-0x7e.  If not, we return a String representation of the
		// byte-extended bytes themselves (e.g. 0xFF->0xFFFF)
		/*
		int length = len();
		for (int i=0; i<length; i++)
		{
			if (overlay[i]<0x20) // >0xF0 is considered negative
			{
				char[] charOverlay = new char[length];
				for (int j=0; j<length; j++)
					charOverlay[j] = (char)overlay[j];
				return new String(charOverlay);
			}
		}
		*/
		return new String(getOverlay(), getOffset(), len());
	}

	/**
	 * Return the string representation of this value.
	 */
	public String toString()
	{
		return toFixedString();
	}

	/** Return the byte at the specified 0-based index. */
	final public byte byteAt(int index)
	{
		//radSubfields();
		rSubfields();
		return sbyteAt(index);
	}

	/** Return the signed byte at the specified 0-based index. */
	final byte sbyteAt(int index)
	{
		return getOverlay()[getOffset()+index];
	}

	/** Return the unsigned byte at the specified 0-based index. */
	final char ubyteAt(int index)
	{
		/// Arggggh (once again)!  Can't just cast to char here, because the sign (left-most) bit gets extended.
		//return (char)overlay[index];
		char c = (char) sbyteAt(index);
		c = (char) (c & 0x00FF);
		return c;
	}

	/** A 'stub' implemented to update the contents of any subfields that 'overlay' this fixed-length string. */
	protected void updateSubfields()
	{
	}
	
	final FixedData updateThis()
	{
		boolean b = updating;
		if (!b)
		{
			updating = true;
			updateSubfields();
			updating = false;
		}
		return this;
	}
	
	/** Return the zoned decimal 'subfield' at the specified 0-based index. */
	protected ZonedDecimal zonedAt(int index, int sz, int precision)
	{
		return new ZonedDecimal(sz, precision, getBytes(), index);
	}

	// This is a stub (currently) implemented only by varying
	protected void setVlength(int vlength)
	{
	}
	
	/** Copy the specified fixed-length data to this array (overlay) at the specified offset. */
	final int arrayCopy(int offset, FixedData fStr)
	{
		return arrayCopy(offset, fStr.getOverlay(), fStr.getOffset(), fStr.msize());
	}
	/** Copy the specified String to this array (overlay) at the specified offset. */
	final int arrayCopy(int offset, String str)
	{
		return arrayCopy(offset, str.getBytes(), 0, str.length());
	}
	/** Copy the specified array to this array (overlay) at the specified offset. */
	final int arrayCopy(int offset, byte[] array, int arrayOffset)
	{
		return arrayCopy(offset, array, arrayOffset, array.length);
	}
	/** 
	 * Copy the specified array to this array (overlay) at the specified offset.
	 * @return the number of bytes actually copied 
	 */
	final int arrayCopy(int offset, byte[] array, int arrayOffset, int arrayLength)
	{
		return arrayCopyNocheck(offset, array, arrayOffset, java.lang.Math.min(arrayLength, msize()-offset));
	}
	/** 
	 * Copy the specified array to this array (overlay) at the specified offset.
	 * @return the number of bytes actually copied 
	 */
	final int arrayCopyNocheck(int offset, byte[] array, int arrayOffset, int length)
	{
		int begin = getOffset()+offset;
		//System.arraycopy(array, arrayOffset, getOverlay(), begin, length);
		System.arraycopy(array, arrayOffset, m_ptr.m_overlay, begin, length);
		setVlength(length);
		return length;
	}
	
	/** Fill this array (overlay) with the specified fill byte. */
	final void fillArray(byte fillValue)
	{
		fillArray(fillValue, 0);
	}
	/** Fill this array (overlay) beginning at the specified value, with the specified fill byte. */
	final void fillArray(byte fillValue, int offset)
	{
		int begin = getOffset();
		java.util.Arrays.fill(getOverlay(), begin+offset, begin+msize(), fillValue);
	}

	final public byte[] getOverlay()
	{
		return m_ptr.m_overlay;
	}


	/** The offset into the buffer that this field actually begins at. */
	int getOffset()
	{
		return m_ptr.m_offset;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	final public boolean equals(Object o)
	{
		return (compareTo(o)==0);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	final public int hashCode()
	{
		return toString().hashCode();
	}

	/** The actual byte length of the data as opposed to the byte length (size()) of this field. */
	int msize()
	{
		return m_size;
	}
	
}
