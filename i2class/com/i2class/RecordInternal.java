package com.i2class;
import com.ibm.as400.access.*;

/**
 * A class representation of a program-described (internally described) record.
 * 
 */
public class RecordInternal extends Record400
{
	protected byte[] buffer;
	/**
	 * RinternalRecord constructor comment.
	 * @param recordName java.lang.String
	 */
	public RecordInternal(String recordName, int rcdLen)
	{
		super(recordName);
		addFieldDescription(
			new HexFieldDescription(new AS400ByteArray(rcdLen), recordName));
	}
	protected char charAt(int offset)
	{
		AS400Text ebcdic = new AS400Text(1);
		String s = (String) ebcdic.toObject(buffer, offset);
		return s.charAt(0);
	}
	protected FixedChar fixedAt(int offset, int len)
	{
		AS400Text ebcdic = new AS400Text(len);
		String s = (String) ebcdic.toObject(buffer, offset);
		return new FixedChar(len, s);
	}
	protected int intAt(int offset)
	{
		int i = (int) FixedChar.getBinary(buffer, offset, 4);
		return i;
	}
	protected long longAt(int offset)
	{
		long l = FixedChar.getBinary(buffer, offset, 8);
		return l;
	}
	protected PackedDecimal packedAt(int offset, int len, int precision)
	{
		return new PackedDecimal(len, precision, buffer, offset);
	}
	/**
	 * Set the buffer array to the byte representation of this record
	 * Creation date: (8/2/2002 7:16:25 AM)
	 */
	void setBuffer()
	{
		// If the contents of this record have already been retrieved, then no need to do this again.
		//if (lastRecord != record)
		{
			//lastRecord = record;
			try
			{
				buffer = record.getContents();
			}
			catch (Exception e)
			{
			}
		}
	}
	protected ZonedDecimal zonedAt(int offset, int len, int precision)
	{
		AS400Text ebcdic = new AS400Text(len);
		String s = (String) ebcdic.toObject(buffer, offset);
		ZonedDecimal z = new ZonedDecimal(len, precision);
		z.assign(s.getBytes(), 0);
		return z;
	}
}
