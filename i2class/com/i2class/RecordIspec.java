package com.i2class;
import com.ibm.as400.access.*;

/**
 * A class representation of an I-spec (Input specification).
 * An input-specification programmatically describes field layouts in a record. 
 * @author Andrew Clark
 */
public class RecordIspec extends Record400
{
	protected byte[] buffer;
	/**
	 * RinternalRecord constructor comment.
	 * @param recordName java.lang.String
	 */
	public RecordIspec(String recordName, int rcdLen)
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
	protected fixed fixedAt(int offset, int len)
	{
		AS400Text ebcdic = new AS400Text(len);
		String s = (String) ebcdic.toObject(buffer, offset);
		return new fixed(len, s);
	}
	protected int intAt(int offset)
	{
		int i = (int) fixed.getBinary(buffer, offset, 4);
		return i;
	}
	protected long longAt(int offset)
	{
		long l = fixed.getBinary(buffer, offset, 8);
		return l;
	}
	protected packed packedAt(int offset, int len, int precision)
	{
		return new packed(len, precision, buffer, offset);
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
	protected zoned zonedAt(int offset, int len, int precision)
	{
		AS400Text ebcdic = new AS400Text(len);
		String s = (String) ebcdic.toObject(buffer, offset);
		zoned z = new zoned(len, precision);
		z.assign(s.getBytes(), 0);
		return z;
	}
}
