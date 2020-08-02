package com.i2class;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

/**
 * A record class used to pass values from a stalled (e.g. READ/EXFMT) workstation thread.
 * 
 * @see RfileWorkstn
 */
public class RecordWorkstn extends RrecordX2
{
	//RdspfThread file;
	Vector fldValues = new Vector();
	boolean isChanged;
	// Use character here instead of boolean so that we can differentiate between true ('1'), false ('0'), and not set ('\0')
	char indicators[] = new char[99];
	int firstFieldLine;
	
	// AID constants.
	public static final int AID_CA01=1;
	public static final int AID_CA02=2;
	public static final int AID_CA03=3;
	public static final int AID_CA04=4;
	public static final int AID_CA05=5;
	public static final int AID_CA06=6;
	public static final int AID_CA07=7;
	public static final int AID_CA08=8;
	public static final int AID_CA09=9;
	public static final int AID_CA10=10;
	public static final int AID_CA11=11;
	public static final int AID_CA12=12;
	public static final int AID_CA13=13;
	public static final int AID_CA14=14;
	public static final int AID_CA15=15;
	public static final int AID_CA16=16;
	public static final int AID_CA17=17;
	public static final int AID_CA18=18;
	public static final int AID_CA19=19;
	public static final int AID_CA20=20;
	public static final int AID_CA21=21;
	public static final int AID_CA22=22;
	public static final int AID_CA23=23;
	public static final int AID_CA24=24;
	public static final int AID_PAGEDOWN=25;
	public static final int AID_PAGEUP=26;
	public static final int AID_HELP=27;
	public static final int AID_VLDCMDKEY=28;
	public static final int AID_CF01= 0xF00 | AID_CA01;
	public static final int AID_CF02= 0xF00 | AID_CA02;
	public static final int AID_CF03= 0xF00 | AID_CA03;
	public static final int AID_CF04= 0xF00 | AID_CA04;
	public static final int AID_CF05= 0xF00 | AID_CA05;
	public static final int AID_CF06= 0xF00 | AID_CA06;
	public static final int AID_CF07= 0xF00 | AID_CA07;
	public static final int AID_CF08= 0xF00 | AID_CA08;
	public static final int AID_CF09= 0xF00 | AID_CA09;
	public static final int AID_CF10= 0xF00 | AID_CA10;
	public static final int AID_CF11= 0xF00 | AID_CA11;
	public static final int AID_CF12= 0xF00 | AID_CA12;
	public static final int AID_CF13= 0xF00 | AID_CA13;
	public static final int AID_CF14= 0xF00 | AID_CA14;
	public static final int AID_CF15= 0xF00 | AID_CA15;
	public static final int AID_CF16= 0xF00 | AID_CA16;
	public static final int AID_CF17= 0xF00 | AID_CA17;
	public static final int AID_CF18= 0xF00 | AID_CA18;
	public static final int AID_CF19= 0xF00 | AID_CA19;
	public static final int AID_CF20= 0xF00 | AID_CA20;
	public static final int AID_CF21= 0xF00 | AID_CA21;
	public static final int AID_CF22= 0xF00 | AID_CA22;
	public static final int AID_CF23= 0xF00 | AID_CA23;
	public static final int AID_CF24= 0xF00 | AID_CA24;
	
	public static final int KWD_SFLCLR = 29;
	public static final int KWD_SFLDLT = 30;
	public static final int KWD_SFLINZ = 31;
	public static final int KWD_SFLEND = 32;
	public static final int KWD_SFLDSP = 33;
	public static final int KWD_SFLDSPCTL = 34;
	DefinitionKeyword definitions[] = new DefinitionKeyword[KWD_SFLDSPCTL];
	
	private static StringBuffer indBuf = new StringBuffer("IN00"); 

	public RecordWorkstn(String recordName)
	{
		super(recordName);
	}
	protected void addFieldDescription(I2FieldDescription field)
	{
		super.addFieldDescription(field);
		fldValues.addElement(null);
	}
	public void copyString(FixedChar fStr, int fieldIndex) throws SQLException
	{
		String s = getString(fieldIndex);
		fStr.assign(s);
	}
	/**
	 * Evaluate an indicator expression (for example "N90"). 
	 */
	protected boolean evaluateIndicatorExpression(String indicatorExpression)
	{
		if (indicatorExpression==null)
			return true;
		String exp=indicatorExpression;
		int length=exp.length();
		int i=0;
		while (i<length && exp.charAt(i)==' ')
			i++;
		boolean not=(exp.charAt(i)=='N');
		if (not)
			i++;
		int index = Integer.parseInt(exp.substring(i, i+2));
		boolean b=indicators[index-1]=='1';
		if (not)
			b=!b;
		return b;
	}
	public char getChar(int fieldIndex)
	{
		String s = getString(fieldIndex);
		if (s.length() > 0)
			return s.charAt(0);
		else
			return ' ';
	}
	public double getDouble(int fieldIndex)
	{
		String s = getNumericString(fieldIndex);
		try
		{
			Double d = new Double(s);
			return d.doubleValue();
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	public BigDecimal getDecimal(int fieldIndex)
	{
		return getBigDecimal(fieldIndex);
	}
	public BigDecimal getBigDecimal(int fieldIndex)
	{
		String s = getNumericString(fieldIndex);
		BigDecimal bd = new BigDecimal(s);
		return bd;
	}

	public String getFileName()
	{
		return file.actualFileName;
	}

	/** Get the first line on the display that this format has fields on. */
	int getFirstFieldLine()
	{
		return firstFieldLine;
	}
	

	public int getInt(int fieldIndex)
	{
		return (int)getLong(fieldIndex);
	}
	public long getLong(int fieldIndex)
	{
		String s = getNumericString(fieldIndex);
		// This is silly, but Long can't handle .0 at the end of the number, so we need to use a Double instead
		//Long l=new Long(s);
		try
		{
			Double i = new Double(s);
			return i.longValue();
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	// Return a numeric string from a webface screen
	public String getNumericString(int fieldIndex)
	{
		// Additional handling is needed to strip off any '+' or '-' at the end of the field, which WebFacing
		// allows, but Java chokes on.
		String s = getString(fieldIndex);
		// Put '+' or '-' at beginning of string
		int l=s.length()-1;
		if (l>=0)
		{
			char c=s.charAt(l);
			if (c=='+' || c=='-')
				s = c + s.substring(0, l);
		}
		return s;
	}

	public String getString(int fieldIndex)
	{
		Object value = null;
		if (fieldIndex >= 0)
			value = fldValues.elementAt(fieldIndex);
		if (value == null)
			return "";
		return value.toString();
	}
	public String getString(String fieldName)
	{
		int fieldIndex = fldNames.indexOf(fieldName);
		return getString(fieldIndex);
	}


	private static void getXMLaddField(StringBuffer buf, String fldName, String value)
	{
		buf.append("  <");
		fldName = uname(fldName);
		buf.append(fldName);
		buf.append(">");
		buf.append(value);
		buf.append("</");
		buf.append(fldName);
		buf.append(">\n");
	}

	// Build XML string for this record
	String getXML()
	{
		return getXMLclose(getXMLBuffer());
	}
	// Build XML string for this record
	protected StringBuffer getXMLBuffer()
	{
		// The first entity is the record name
		StringBuffer buf = new StringBuffer(" <");
		buf.append(uname(recordName));
		buf.append(">\n");
		// Loop through all the fields in the record.  Add entity/value relationship for each one
		int fldCount = fldValues.size();
		for (int i = 0; i < fldCount; i++)
		{
			String value = getString(i);
			if (value != null)
			{
				String fldName = (String) fldNames.elementAt(i);
				getXMLaddField(buf, fldName, value);
			}
		}
		// Add inidicators
		for (int i=0; i<99; i++)
		{
			if (indicators[i] != '\0')
			{
				/* This builds a lot of temporary string objects, so use indBuf instead
				String fldName = null;
				if (i<10)
					fldName = "IN0";
				else
					fldName = "IN";
				fldName = fldName + Integer.toString(i+1);
				*/
				indBuf.setCharAt(2, (char)('0'+(i+1)/10));
				indBuf.setCharAt(3, (char)('0'+(i+1)%10));
				String value = new String(indicators, i, 1);
				getXMLaddField(buf, indBuf.toString(), value);
			}
		}
		return buf;
	}
	protected String getXMLclose(StringBuffer buf)
	{
		// The first entity is the record name
		buf.append(" </");
		buf.append(recordName);
		buf.append(">\n");
	
		return buf.toString();
	}

	// Build XML string for this record's function keys
	String getXMLfkey()
	{
		StringBuffer buf = new StringBuffer();
		// Loop through all of the function keys for this format and see if they are available
		for (int i=AID_CA01; i<=AID_PAGEUP; i++)
		{
			//DefinitionAID def = (DefinitionAID)definitions.elementAt(i);
			DefinitionAID def = (DefinitionAID)definitions[i-1];
			if (def != null)
			{
				if (evaluateIndicatorExpression(def.indicatorExpression))
				{
					String key = ("0" + Integer.toString(i));
					int keyLength = key.length();
					key = key.substring(keyLength-2);
					String name;
					if (def.keyword == AID_PAGEDOWN)
						name = "PAGEDOWN";
					else if (def.keyword == AID_PAGEUP)
						name = "PAGEUP";
					else if (def.keyword == AID_HELP)
						name = "HELP";
					else if (def.keyword <= AID_CA24)
						name="CA" + key;
					else
						name="CF" + key;
					buf.append("<fkey name='" + name + "' value='" + name + "'/>");
				}
			}
		}
	
		return buf.toString();
	}

	public int getZOrder()
	{
		return 0;
	}

	public boolean processIndicators()
	{
		return true;
	}

	public void processSpecialValues(Hashtable specialValues) throws Exception
	{
		// Process AID keys
		int index=-1;
		String cmdKey = (String) specialValues.remove("AID");
		if (cmdKey != null)
		{
			if (cmdKey.length()>0)
			{
				// Deal with CA/CF keys
				if (cmdKey.charAt(0)=='C')
				{
					try
					{
						index = Integer.parseInt(cmdKey.substring(2));
					}
					catch (Exception e) {}
				}
				else if (cmdKey.compareTo("PAGEDOWN")==0)
					index = AID_PAGEDOWN;
				else if (cmdKey.compareTo("PAGEUP")==0)
					index = AID_PAGEUP;
				else if (cmdKey.compareTo("HELP")==0)
					index = AID_HELP;
			}
			DefinitionAID def;
			// Loop through all of the function key indicators and turn 'off' the ones that are not pressed.
			// Turn 'on' the key that is pressed
			for (int i=AID_CA01; i<=AID_PAGEUP; i++)
			{
				def = (DefinitionAID)definitions[i-1];
				if (def != null)
				{
					// Turn on the responseIndicator if this is the correct function key
					boolean isPressed = (i==index); 
					setIndicator(def.responseIndicator, isPressed);
				}
			}
			// Set the VLDCMDKEY indicator, if specified
			def = (DefinitionAID)definitions[AID_VLDCMDKEY-1];
			if (def != null)
				setIndicator(def.responseIndicator-1, (index>=AID_CA01 && index<=AID_PAGEUP));
			// Process CHANGE keyword
		}
	}

	/**
	 * Set the file object for a specific record format.
	 * @version 11/20/2002 8:52:31 AM
	 * @param file com.i2class.RdspfThread
	 */
	void setFile(RfileWorkstn file) throws Exception
	{
		this.file = file;
	}

	// Update character value
	public void setText(int fieldIndex, char value) throws SQLException
	{
		Character c = new Character(value);
		fldValues.setElementAt(c, fieldIndex);
	}

	public void setText(int fieldIndex, double value)
	{
		fldValues.setElementAt(new Double(value), fieldIndex);
	}

	public void setText(int fieldIndex, long value)
	{
		fldValues.setElementAt(new Long(value), fieldIndex);
	}
	// Update string/decimal value
	public void setText(int fieldIndex, Object value) throws SQLException
	{
		fldValues.setElementAt(value.toString(), fieldIndex);
	}
	/** Set the text of a field and change the format-level 'isChanged' value. */
	public void setText(String fieldName, Object value) throws Exception
	{
		setTextNoChange(fieldName, value);
		isChanged=true;
	}
	
	/** Set the text of a field and change the CHANGE indicator if the value has changed. */
	void setTextChange(String fieldName, Object value) throws Exception
	{
		String oldValue = getString(fieldName);
		setText(fieldName, value);
		// This is annoying, but a value can 'CHANGE' if it is just typed into on a WebFaced display
		// regardless if the value has changed or not.
		setChangeIndicator(fieldName, oldValue, value);
	}
	/** Set the text of a field without changing the boolean 'isChanged' value. */
	void setTextNoChange(String fieldName, Object value)
		throws Exception
	{
		int fieldIndex = fldNames.indexOf(fieldName);
		/*
		if (fieldIndex <0)
			throw new Exception("Unable to set " + fieldName + "in record " + recordName);
		*/
		setText(fieldIndex, value);
	}
	
	/** Set on the CHANGE indicator for a field. */
	protected void setChangeIndicator(String fieldName, String oldValue, Object value)
	{
		if (oldValue.compareTo(value.toString())!=0)
		//TODO add XML support for CHANGE at the field level
			;
	}
	

	public String toString()
	{
		return recordName;
	}
	
	/** Return the boolean value at the specified index. */
	public boolean getIndicator(int index)
	{
		return indicators[index-1]=='1';
	}
	/** Set the boolean value at the specified index. */
	public void setIndicator(int index, boolean value)
	{
		char set='0';
		if (value)
			set='1';
		indicators[index-1]=set;
	}
	/**
	 * Add an AID key (for example CF03, PAGEDOWN) definition. 
	 * @param AID one of the AID_ predefined constants.
	 * @param response the indicator value returned to the program when the key is 'pressed'
	 * @param conditioning an indicator conditioning string that controls whether the AID key is valid
	 * (for example "N32"). 
	 */
	protected void addAIDDefinition(int AID, int response, String conditioning)
	{
		//definitions.add(AID & 0xFF, new DefinitionAID(AID, response, conditioning));
		int index=AID & 0xFF;
		definitions[index-1]=new DefinitionAID(AID, response, conditioning);
	}
	protected void addAIDDefinition(int AID, int response)
	{
		addAIDDefinition(AID, response, null);
	}
	/**
	 * Add a keyword (for example SFLCLR) definition. 
	 * @param keyword one of the KWD_ predefined constants.
	 * @param conditioning an indicator conditioning string that controls whether the keyword is valid
	 * (for example "N32"). 
	 */
	protected void addKeywordDefinition(int keyword, String conditioning)
	{
		//definitions.add(keyword, new DefinitionKeyword(keyword, conditioning));
		definitions[keyword-1]=new DefinitionKeyword(keyword, conditioning);
	}
	/**
	 * Add a unconditioned keyword (for example SFLDSP) definition. 
	 * @param keyword one of the KWD_ predefined constants.
	 */
	protected void addKeywordDefinition(int keyword)
	{
		addKeywordDefinition(keyword, null);
	}
	protected void addKeywordDefinition(int keyword, int response)
	{
		addKeywordDefinition(keyword, null);
	}

	/**
	 * Set the first line (row) that this format occupies.  
	 */	
	protected void setFirstFieldLine(int line)
	{
		firstFieldLine = line;
	}
	
	/** Return the upper-case (#@$ translated to 'nad') equivalent of a name. */
	static protected String uname(String name)
	{
		/*
		name = name.replace('#', 'n');
		name = name.replace('@', 'a');
		name = name.replace('$', 'd');
		*/
		StringBuffer sb=null;
		int nameLength = name.length();
		for (int i=0; i<nameLength; i++)
		{
			char c = name.charAt(i);
			int j = "#@$".indexOf(c);
			if (j>=0)
			{
				if (sb==null)
					sb = new StringBuffer(name);
				sb.setCharAt(i, "nad".charAt(j));
			}
		}
		if (sb!=null)
			name = sb.toString();
		return name;
	}
	
	/** Clear any feedback indicators for this record. */
	void clearFeedbackIndicators()
	{
		// TODO actually clear feedback indicators???
	}
	
	/** If a separate indicator structure (INDDS) is specfied, then write the indicator values */
	void writeINDDS(FixedChar indds)
	{
		if (indds!=null)
		{
			byte[] bytes = indds.getBytes();
			for (int i=0; i<99 && i<bytes.length; )
			{
				byte b = bytes[i];
				i++;
				setIndicator(i, b=='1');
			}
		}
	}
	
}
