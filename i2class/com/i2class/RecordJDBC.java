package com.i2class;

import java.sql.*;
import java.math.BigDecimal;
import java.util.Vector;

/**
 * A JDBC record class.
 * @author Andrew Clark
 * @see RfileJDBC
 */
public class RecordJDBC extends RecordFormatI2
{
	String fieldList;
	//RfileJDBC file;
	//Vector fldDescriptions = new Vector();
	protected Object[] key;
	protected int keyCount;
	Vector keyNames;
	Vector keyTypes;
	int updateCount;
	Vector updateFields = new Vector();
	Vector updateValues = new Vector();
	
	public RecordJDBC(String recordName)
	{
		super(recordName);
		keyNames = new Vector();
		keyTypes = new Vector();
	}
	/*
	protected void addFieldDescription(FieldDescription field)
	{
		super.addFieldDescription(field);
		fldDescriptions.add(field);
	}
	*/
	/** Add and retrieve an ascending key field. */
	protected void addKeyFieldDescription(String fieldName)
	{
		addKeyFieldDescription(fieldName, "");
	}
	/** Add and retrieve key field names with Descending/Absolute value specified. */
	protected void addKeyFieldDescription(String fieldName, int keyType)
	{
		StringBuffer s = new StringBuffer();
		if ((keyType & KEY_DESCEND) != 0)
			s.append("DESC");
		/* ORDER BY ABS not supported
		if ((keyType & KEY_ABSVAL) != 0)
			s.append(" ABS");
		*/
		addKeyFieldDescription(fieldName, s.toString());
	}
	// Add and retrieve key field names
	private void addKeyFieldDescription(String fieldName, String keyType)
	{
		keyNames.add(fieldName);
		keyTypes.add(keyType);
	}
	
	public void copyString(fixed fStr, int fieldIndex) throws SQLException
	{
		String s = getString(fieldIndex);
		fStr.assign(s);
	}
	final private int getActualIndex(int fieldIndex)
	{
		int actualIndex = ((RfileJDBC)file).fldIndexMap[fieldIndex];
		return actualIndex;
	}
	public BigDecimal getBigDecimal(int fieldIndex) throws SQLException
	{
		int actualIndex = getActualIndex(fieldIndex);
		BigDecimal bd = ((RfileJDBC)file).rs.getBigDecimal(actualIndex);
		return bd;
	}
	public char getChar(int fieldIndex) throws SQLException
	{
		int actualIndex = getActualIndex(fieldIndex);
		String s = ((RfileJDBC)file).rs.getString(actualIndex);
		return s.charAt(0);
	}
	/**
	 * Return the Date representation of an object.
	 * Creation date: (2/18/2003 10:50:20 AM)
	 * @return java.sql.Date
	 * @param fldIndex int
	 */
	public java.sql.Date getDate(int fieldIndex) throws SQLException
	{
		int actualIndex = getActualIndex(fieldIndex);
		java.sql.Date d = ((RfileJDBC)file).rs.getDate(actualIndex);
		return d;
	}
	public BigDecimal getDecimal(int fieldIndex) throws SQLException
	{
		return getBigDecimal(fieldIndex);
	}
	public BigDecimal getNumeric(int fieldIndex) throws SQLException
	{
		return getBigDecimal(fieldIndex);
	}
	public double getDouble(int fieldIndex) throws SQLException
	{
		//double d=((RfileJDBC)file).rs.getDouble(fieldIndex+1);
		int actualIndex = getActualIndex(fieldIndex);
		double d = ((RfileJDBC)file).rs.getDouble(actualIndex);
		return d;
	}
	public int getInt(int fieldIndex) throws SQLException
	{
		//int i = ((RfileJDBC)file).rs.getInt(fieldIndex+1);
		int actualIndex = getActualIndex(fieldIndex);
		int i = ((RfileJDBC)file).rs.getInt(actualIndex);
		return i;
	}
	public short getShort(int fieldIndex) throws SQLException
	{
		//int i = ((RfileJDBC)file).rs.getInt(fieldIndex+1);
		int actualIndex = getActualIndex(fieldIndex);
		short s = ((RfileJDBC)file).rs.getShort(actualIndex);
		return s;
	}
	Object[] getKey()
	{
		if (key.length == keyCount || keyCount <= 0)
			return key;
		else
		{
			Object newkey[] = new Object[keyCount];
			//for (int i = 0; i < keyCount; i++)
			//	newkey[i] = key[i];
			System.arraycopy(key,0,newkey,0,keyCount);
			return newkey;
		}
	}
	String getKeyName(int keyIndex)
	{
		String s = keyNames.get(keyIndex).toString();
		return s;
	}
	String getKeyType(int keyIndex)
	{
		String s = keyTypes.get(keyIndex).toString();
		return s;
	}
	public long getLong(int fieldIndex) throws SQLException
	{
		//long l = ((RfileJDBC)file).rs.getLong(fieldIndex+1);
		RfileJDBC file = (RfileJDBC)this.file;
		long l = file.rs.getLong(file.fldIndexMap[fieldIndex]);
		return l;
	}
	public String getString(int fieldIndex) throws SQLException
	{
		//String s=((RfileJDBC)file).rs.getString(fieldIndex+1);
		int actualIndex = getActualIndex(fieldIndex);
		String s = ((RfileJDBC)file).rs.getString(actualIndex);
		return s;
	}

	/**
	 * Return the time representation of an object.
	 * @return java.sql.Time
	 * @param fldIndex int
	 */
	public java.sql.Time getTime(int fieldIndex) throws SQLException
	{
		int actualIndex = getActualIndex(fieldIndex);
		java.sql.Time t = ((RfileJDBC)file).rs.getTime(actualIndex);
		return t;
	}

	/**
	 * Return the timestamp representation of an object.
	 * @return java.sql.Timestamp
	 * @param fldIndex int
	 */
	public java.sql.Timestamp getTimestamp(int fieldIndex) throws SQLException
	{
		int actualIndex = getActualIndex(fieldIndex);
		java.sql.Timestamp t = ((RfileJDBC)file).rs.getTimestamp(actualIndex);
		return t;
	}

	/** Return true for indicator values. */
	public boolean On() 
	{
		if (((RfileJDBC)file).rs instanceof FieldSet)
			return false;
		return true;
	}

	/**
	 * Set the list of fields that the file is going to select from.
	 * @version 2/18/2003 3:01:10 PM
	 * @param fieldList java.lang.String
	 */
	public void setFieldList(String fldList)
	{
		fieldList = fldList;
	}
	/**
	 * Set the key to a figurative constant value like *LOVAL or *BLANKS
	 */
	public void setKey(FigConst value) throws ClassCastException
	{
		keyCount = key.length;
		for (int i = 0; i < keyCount; i++)
		{
			String keyName = (String) (keyNames.elementAt(i));
			I2FieldDescription fd =
				(I2FieldDescription) fldDescriptions.elementAt(
					fldNames.indexOf(keyName));
			if (fd instanceof FloatFieldDescription)
				key[i] = numeric.newBigDecimal(((FigConstNum) value).floatValue());
			else
			{
				int scale = fd.dataType.precision;
				int length = fd.dataType.size;
				// If this is a character string, then create a new key with the same length
				if (scale == -1)
				{
					fixed fStr = new fixed(length);
					fStr.assign(value);
					key[i] = fStr.toFixedString();
				}
				// If this is a number, then create a value with the appropriate length and scale
				else
					key[i] = ((FigConstNum) value).decimalValue(length, scale);
			}
		}
	}
	// Update decimal value
	public void setValue(int fieldIndex, BigDecimal value) throws SQLException
	{
		RfileJDBC file = (RfileJDBC)this.file;
		if (file._updatable)
		{
			//((RfileJDBC)file).rs.updateDouble(fieldIndex+1, value);
			int actualIndex = getActualIndex(fieldIndex);
			file.rs.updateBigDecimal(actualIndex, value);
		}
		else
			// BigDecimal(double) does freaky things to the number when toString() is called, adding
			// all sorts of digits after the decimal point.  Use Double instead.
			//updateValue(fieldIndex, new BigDecimal(value));
			updateValue(fieldIndex, value);
		updateCount++;
	}
	// Update character value
	public void setValue(int fieldIndex, char value) throws SQLException
	{
		Character c = new Character(value);
		setValue(fieldIndex, c.toString());
	}

	/** set I2 date/timestamp value */
	public void setValue(int fieldIndex, date value) throws Exception
	{
		/* If the result set is updatable, then we want to use the raw date form
		if (file._updatable)
		{
			java.util.Date d = value.toDate();
			setValue(fieldIndex, d);
		}
		// ...but if it's not, we need to build a character string that the database is going to understand
		else
			updateValue(fieldIndex, value);
		*/
		
		// Just always use the java Date form since the driver has to handle the interpretation of the object value
		// (we don't have to worry about the format e.g. mdy, ymd, etc.))
		java.util.Date d = value.toDate();
		setValue(fieldIndex, d);
	}
	// Update double value
	public void setValue(int fieldIndex, double value) throws SQLException
	{
		RfileJDBC file = (RfileJDBC)this.file;
		if (file._updatable)
		{
			//((RfileJDBC)file).rs.updateDouble(fieldIndex+1, value);
			int actualIndex = getActualIndex(fieldIndex);
			file.rs.updateDouble(actualIndex, value);
		}
		else
			// BigDecimal(double) does freaky things to the number when toString() is called, adding
			// all sorts of digits after the decimal point.  Use Double instead.
			//updateValue(fieldIndex, new BigDecimal(value));
			updateValue(fieldIndex, new Double(value));
		updateCount++;
	}
	// Update Fixed value
	public void setValue(int fieldIndex, fixed value) throws SQLException
	{
		String s = value.toString();
		setValue(fieldIndex, s);
	}
	// Update decimal value
	public void setValue(int fieldIndex, INumeric value) throws SQLException
	{
		setValue(fieldIndex, value.toBigDecimal());
	}

	/** set Date/Time/Timestamp value */
	public void setValue(int fieldIndex, java.util.Date value) throws SQLException
	{
		RfileJDBC file = (RfileJDBC)this.file;
		if (file._updatable)
		{
			java.sql.Date sqlDate = new java.sql.Date(value.getTime());
			//((RfileJDBC)file).rs.updateDouble(fieldIndex+1, value);
			int actualIndex=getActualIndex(fieldIndex);
			file.rs.updateDate(actualIndex, sqlDate);
		}
		else
			updateValue(fieldIndex, value);
		updateCount++;
	}
	// Update string value
	public void setValue(int fieldIndex, String value) throws SQLException
	{
		/*
		String fldName = fieldNames.elementAt(fieldIndex).toString();
		if (!keyNames.contains(fldName))
			((RfileJDBC)file).rs.updateString(fieldIndex+1, value);
		*/
		RfileJDBC file = (RfileJDBC)this.file;
		if (file._updatable)
		{
			//((RfileJDBC)file).rs.updateString(fieldIndex+1, value);
			int actualIndex=getActualIndex(fieldIndex);
			file.rs.updateString(actualIndex, value);
		}
		else
			//updateValue(fieldIndex, "'" + value + "'");
			updateValue(fieldIndex, value);
		updateCount++;
	}
	/** set I2 time value */
	public void setValue(int fieldIndex, time value) throws Exception
	{
		java.sql.Time sqlTime = new java.sql.Time(value.toDate().getTime());
		//((RfileJDBC)file).rs.updateDouble(fieldIndex+1, value);
		RfileJDBC file = (RfileJDBC)this.file;
		if (file._updatable)
		{
			int actualIndex=getActualIndex(fieldIndex);
			file.rs.updateTime(actualIndex, sqlTime);
		}
		else
			updateValue(fieldIndex, sqlTime);
		updateCount++;
	}
	/** set I2 timestamp value */
	public void setValue(int fieldIndex, timestamp value) throws Exception
	{
		java.sql.Timestamp sqlTimestamp = value.toTimestamp();
		//((RfileJDBC)file).rs.updateDouble(fieldIndex+1, value);
		RfileJDBC file = (RfileJDBC)this.file;
		if (file._updatable)
		{
			int actualIndex=getActualIndex(fieldIndex);
			file.rs.updateTimestamp(actualIndex, sqlTimestamp);
		}
		else
			updateValue(fieldIndex, sqlTimestamp);
		updateCount++;
	}
	
	/**
	 * Update a field with an object value.
	 */
	protected void updateValue(int fieldIndex, Object value)
	{
		String fldName = fldNames.elementAt(fieldIndex).toString();
		//updateFields.add(fldName + '=' + value);
		updateFields.add(fldName);
		//updateValues.add(value.toString());
		// Just add the raw value here instead of casting to a String since we are using prepared statements
		// and setObject() calls.
		updateValues.add(value);
	}
	
	void clearUpdates()
	{
		updateFields.clear();
		updateValues.clear();
		updateCount=0;
	}
	
}
