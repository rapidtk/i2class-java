package com.i2class;

import com.ibm.as400.access.*;

import java.io.UnsupportedEncodingException;
import java.math.*;
import java.util.Vector;

/**
 * A record of data read/written from an externally-described OS/400 file.
 * @see RfileDB400
 */
public class Record400
	extends com.ibm.as400.access.RecordFormat
	implements IRecord, IRecordFormat
{
	protected Object[] key;
	protected int keyCount;
	Record record;
	private int addOffset;
	private Vector offsets = new Vector();
	//private Application app;
	Rfile file;
	
	static protected final int KEY_DESCEND=1;
	static protected final int KEY_ABSVAL=2;

	/** Create a record format object with the specified name. */
	public Record400(String recordName)
	{
		super(recordName);
	}
	public void control() throws Exception
	{
	}
	
	/**
	 * @see com.ibm.as400.access.RecordFormat#addFieldDescription(com.ibm.as400.access.FieldDescription)
	 */
	public void addFieldDescription(com.ibm.as400.access.FieldDescription fldDesc) {
		super.addFieldDescription(fldDesc);
		offsets.add(ShortDecimal.newInteger(addOffset));
		addOffset += fldDesc.getDataType().getByteLength();
	}
	

	/** 
	 * Copy a string value from the specified field.
	 * @param fStr The fixed string variable to copy the value to.
	 * @param fieldIndex The field index of the field to copy the value from.
	 */
	public void copyString(FixedChar fStr, int fieldIndex)
		throws UnsupportedEncodingException
	{
		String s = record.getField(fieldIndex).toString();
		fStr.assign(s);
	}
	
	/** Get an external value from the field at the specified index. */
	public char getChar(int fieldIndex) throws UnsupportedEncodingException
	{
		String s = (String) record.getField(fieldIndex);
		return s.charAt(0);
	}
	
	public BigDecimal getBigDecimal(int fieldIndex) throws UnsupportedEncodingException
	{
		BigDecimal bd = (BigDecimal) record.getField(fieldIndex);
		return bd;
	}
	public BigDecimal getDecimal(int fieldIndex) throws UnsupportedEncodingException
	{
		return getBigDecimal(fieldIndex);
	}
	public BigDecimal getNumeric(int fieldIndex) throws UnsupportedEncodingException
	{
		return getBigDecimal(fieldIndex);
	}
	
	public double getDouble(int fieldIndex) throws UnsupportedEncodingException
	{
		BigDecimal bd = getBigDecimal(fieldIndex);
		return bd.doubleValue();
	}
	
	/**
	 * Return the field name at the specified index.
	 */
	public String getFieldName(int index)
	{
		com.ibm.as400.access.FieldDescription fd = getFieldDescription(index);
		String fldName = fd.getDDSName();
		return fldName;
	}
	/**
	 * Return the offset into this buffer of this field.
	 */
	public int getFieldOffset(int index)
	{
		Integer i = (Integer)offsets.get(index);
		return i.intValue();
	}

	/**
	 * Return the I2 fixed data type representation of the FieldDescription at the specified index.
	 */
	public FixedData getFixedDataType(int index)
	{
		com.ibm.as400.access.FieldDescription fd = getFieldDescription(index);
		// Packed
		if (fd instanceof PackedDecimalFieldDescription)
			return new PackedDecimal(
				fd.getLength(),
				((PackedDecimalFieldDescription) fd).getDecimalPositions());
		// Zoned
		else if (fd instanceof ZonedDecimalFieldDescription)
			return new ZonedDecimal(
				fd.getLength(),
				((ZonedDecimalFieldDescription) fd).getDecimalPositions());
		// Binary 
		else if (fd instanceof BinaryFieldDescription)
			return new FixedBinary(fd.getDataType().getByteLength());
		// Everything else (treat as character)
		return new FixedChar(fd.getLength());
	}
	public int getInt(int fieldIndex) throws UnsupportedEncodingException
	{
		BigDecimal bd = getBigDecimal(fieldIndex);;
		return bd.intValue();
	}

	/** Get the key value last set by setKey(). */
	Object[] getKey()
	{
		Object newkey[] = null;
		if (key.length == keyCount || keyCount <= 0)
			newkey = key;
		else
		{
			newkey = new Object[keyCount];
			for (int i = 0; i < keyCount; i++)
				newkey[i] = key[i];
			return newkey;
		}
		// If any String keys contain *HIVAL (ALL(X'ff')), then translate it here (to '9', at least for now)
		nextKey : for (int i = 0; i < keyCount; i++)
		{
			if (newkey[i] instanceof String)
			{
				String str = (String) newkey[i];
				int length = str.length();
				for (int j = 0; j < length; j++)
				{
					if (str.charAt(j) != 0x00FF)
						continue nextKey;
				}
				// Create newkey object if not already done
				if (newkey == key)
				{
					newkey = new Object[keyCount];
					for (int l = 0; l < keyCount; l++)
					{
						if (l != i)
							newkey[l] = key[l];
					}
				}
				newkey[i] = str.replace((char) 0x00FF, '9');
			}
		}
		return newkey;
	}
	public long getLong(int fieldIndex) throws UnsupportedEncodingException
	{
		BigDecimal bd = getBigDecimal(fieldIndex);;
		return bd.longValue();
	}
	public String getString(int fieldIndex) throws UnsupportedEncodingException
	{
		String s = record.getField(fieldIndex).toString();
		return s;
	}
	/** Return true for indicator values. */
	public boolean On() 
	{
		return true;
	}

	/** 
	 * An instance of RdbRecord400 implements input() to programmatically set all of the fields in a program to those 
	 * fields read from the file. 
	 */
	public void input() throws Exception
	{
	}
	/** 
	 * An instance of RdbRecord400 implements output() to programmatically set all of the fields in a file to their  
	 * corresponding values in the program. 
	 */
	public void output() throws Exception
	{
	}
	/**
	 * Set the buffer array to the byte representation of this record
	 * Creation date: (8/2/2002 7:16:25 AM)
	 */
	void setBuffer()
	{
	}
	/**
	 * Set the list of fields that the file is going to select from.
	 * @version 2/18/2003 
	 * @param fieldList java.lang.String
	 */
	public void setFieldList(String fldList)
	{

	}
	/**
	 * Set the key to a figurative constant value like *LOVAL or *BLANKS
	 */
	public void setKey(FigConst value) throws ClassCastException
	{
		int keys = key.length;
		for (int i = 0; i < keys; i++)
		{
			com.ibm.as400.access.FieldDescription fd = getKeyFieldDescription(i);
			if (fd instanceof FloatFieldDescription)
				key[i] = ShortDecimal.newBigDecimal(((FigConstNum) value).floatValue());
			else
			{
				int scale = -1;
				int length = fd.getLength();
				// Determine the scale for numbers
				if (fd instanceof PackedDecimalFieldDescription)
					scale =
						((PackedDecimalFieldDescription) fd).getDecimalPositions();
				else if (fd instanceof ZonedDecimalFieldDescription)
					scale =
						((ZonedDecimalFieldDescription) fd).getDecimalPositions();
				else if (fd instanceof BinaryFieldDescription)
				{
					BinaryFieldDescription bd = (BinaryFieldDescription) fd;
					length = bd.getLength();
					if (length <= 5)
						length = 5;
					else if (length <= 10)
						length = 10;
					else
						length = 18;
					scale = 0;
				}
				// If this is a character string, then create a new key with the same length
				if (scale == -1)
				{
					FixedChar fStr = new FixedChar(length);
					fStr.assign(value);
					key[i] = fStr.toFixedString();
				}
				// If this is a number, then create a value with the appropriate length and scale
				else
					key[i] = ((FigConstNum) value).decimalValue(length, scale);
				// If no key has been set yet, then get attribute from RecordFormat
				/*
				if (o==null)
				{
					o = getKeyFieldDescription(i);
					if (o instanceof FloatFieldDescription || o instanceof PackedDecimalFieldDescription
						|| o instanceof ZonedDecimalFieldDescription)
						o = new BigDecimal("0");
					else
						o = new String();
				}
				if (o instanceof String)
				{
					fixed fStr = new fixed(((String)o).length());
					fStr.assign(value);
					key[i] = fStr.toString();
				}
				else if (o instanceof BigDecimal)
					key[i] = new BigDecimal(((FigConstNum)value).floatValue());
				else 
					throw new ClassCastException("Cannot cast key" + i + " to " + value.getClass().getName());
				Object o = key[i];
				// If no key has been set yet, then get attribute from RecordFormat
				if (o==null)
				{
					o = getKeyFieldDescription(i);
					if (o instanceof FloatFieldDescription || o instanceof PackedDecimalFieldDescription
						|| o instanceof ZonedDecimalFieldDescription)
						o = new BigDecimal("0");
					else
						o = new String();
				}
				*/
				/*
				if (o instanceof String)
				{
					fixed fStr = new fixed(((String)o).length());
					fStr.assign(value);
					key[i] = fStr.toString();
				}
				else if (o instanceof BigDecimal)
					key[i] = new BigDecimal(((FigConstNum)value).floatValue());
				else 
					throw new ClassCastException("Cannot cast key" + i + " to " + value.getClass().getName());
				*/
			}
		}
	}
	/** Set the value in the field at the specified index. */
	public void setValue(int fieldIndex, char c)
	{
		Character chr = new Character(c);
		String s = chr.toString();
		setValue(fieldIndex, s);
	}
	public void setValue(int fieldIndex, double value)
	{
		/*
		// This is really stupid, but BigDecimal(double) gives goofy results because of the imprecise nature of double value
		// e.g. BigDecimal(.1) = .1000000000000000055511151231257827021181583404541015625.  Use String constructor instead
		//BigDecimal bd = new BigDecimal(value);
		
		// Adjust for leading '-' for negative numbers
		int negShift=0;
		if (value<0)
			negShift=1;
		
		// Arrrghh!!  Even more stupidity.  The BigDecimal(String) constructor doesn't allow 'E' in the string, so we have to do it
		// ourselves.
		BigDecimal bd;
		String s = Double.toString(value);
		// Adjust for decimal point
		int decShift=0;
		int j=s.indexOf('.');
		if (j>=0)
			decShift=1;
		int i = s.indexOf('E');
		if (i>=0)
		{
			StringBuffer buf = new StringBuffer(s.substring(0, i));
			int shift = Integer.parseInt(s.substring(i+1));
			// Strip the decimal point out of the value
			if (j>=0)
			{
				buf.deleteCharAt(j);
				i--;
			}
			else
				j = i;
			// Calculate position of decimal point insertion
			j += shift;
			// Shift the decimal point by appending or prefixing 0's
			// If the decimal point is beyond the end of the string, add 0's
			if (i <= j)
			{
				do
				{
					buf.append('0');
					i++;
				} while(i<=j);
			}
			// If the decimal point is before the beginning of the string, add 0's
			else
			{
				// Insert 0's after negative sign, if present
				while (j-negShift < 0)
				{
					buf.insert(negShift,'0');
					j++;
				}
			}
			// Reinsert decimal point (if necessary)
			if (j <buf.length())
				buf.insert(j, '.');
			s = buf.toString();
		}
		// Double values are only valid to 15 digits worth of precision
		/*if (s.length()-negShift-decShift > 15)
			s=s.substring(0, 15+negShift+decShift);
		// I guess that somehow trailing 0's are invalid for decimal values, so trim them
		if (decShift>0)
		{
			int sLength=s.length()-1;
			while (sLength>=0 && s.charAt(sLength)=='0')
				sLength--;
			s=s.substring(0, sLength+1);
		}
		bd = new BigDecimal(s);
		*/
		BigDecimal bd = ShortDecimal.newBigDecimal(value);

		setValue(fieldIndex, bd);
	}
	public void setValue(int fieldIndex, int value)
	{
		BigDecimal bd = BigDecimal.valueOf(value);
		setValue(fieldIndex, bd);
	}
	public void setValue(int fieldIndex, long value)
	{
		BigDecimal bd = BigDecimal.valueOf(value);
		setValue(fieldIndex, bd);
	}
	public void setValue(int fieldIndex, FixedChar fStr)
	{
		String s = fStr.toString();
		setValue(fieldIndex, s);
	}
	public void setValue(int fieldIndex, INumeric n)
	{
		BigDecimal bd = n.toBigDecimal();
		record.setField(fieldIndex, bd);
	}
	public void setValue(int fieldIndex, AbstractNumeric n)
	{
		setValue(fieldIndex, (INumeric)n);
	}
	// Set any string values
	public void setValue(int fieldIndex, String s)
	{
		record.setField(fieldIndex, s);
	}
	// Set any numeric values
	public void setValue(int fieldIndex, BigDecimal bd)
	{
		try
		{
			record.setField(fieldIndex, bd);
		}
		// Deal with rounding errors, illegal class types (e.g. BigDecimal instead of Integer), etc.
		catch (ExtendedIllegalArgumentException e)
		{
			int scale = 0;
			com.ibm.as400.access.FieldDescription fd =
				getFieldDescription(fieldIndex);
			if (fd instanceof com.ibm.as400.access.PackedDecimalFieldDescription)
				scale =
					((com.ibm.as400.access.PackedDecimalFieldDescription) fd)
						.getDecimalPositions();
			else if (
				fd instanceof com.ibm.as400.access.ZonedDecimalFieldDescription)
				scale =
					((com.ibm.as400.access.ZonedDecimalFieldDescription) fd)
						.getDecimalPositions();
			if (bd.scale() != scale)
				bd = bd.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
			record.setField(fieldIndex, bd);
		}
	}
	
	/** Return the size (in bytes) of this record format */
	public int size()
	{
		return addOffset+1;
	}

	/*	
	public void setApp(Application app)
	{
		this.app=app;
	}
	*/
	
	/* (non-Javadoc)
	 * @see com.asc.rio.IRecord#setFile(com.asc.rio.Rfile)
	 */
	public void setFile(Rfile file)
	{
		this.file=file;
	}
	
	/* (non-Javadoc)
	 * @see com.asc.rio.IRecord#app()
	 */
	public Application app()
	{
		return file.app;
	}
	
	/** Add and retrieve key field names with Descending/Absolute value specified. */
	protected void addKeyFieldDescription(String fieldName, int keyType)
	{
		// Descending doesn't seem to matter for KeyedFile
		addKeyFieldDescription(fieldName);
	}
}
