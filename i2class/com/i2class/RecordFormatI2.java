package com.i2class;

/**
 * A record class with AS400 field compatibility layer.
 * @author Andrew Clark
 */
import java.util.Vector;
class RecordFormatI2 extends RecordI2 implements IRecordFormat
{
	private int addOffset;
	Vector fldNames = new Vector();
	Vector fldDescriptions = new Vector();
	//Vector offsets = new Vector();
	
	static protected final int KEY_DESCEND=1;
	static protected final int KEY_ABSVAL=2;

	public RecordFormatI2(String recordName)
	{
		super(recordName);
	}
	/** 2-byte binary value. */
	public class AS400Bin2 extends I2DataType
	{
		public AS400Bin2()
		{
			super(5, 0);
		}
		public FixedData getFixedType()
		{
			return new int_f(2);
		}
	}
	/** 4-byte binary value. */
	public class AS400Bin4 extends I2DataType
	{
		public AS400Bin4()
		{
			super(10, 0);
		}
		public FixedData getFixedType()
		{
			return new int_f(4);
		}
	}
	/** 8-byte binary value. */
	public class AS400Bin8 extends I2DataType
	{
		public AS400Bin8()
		{
			super(19, 0);
		}
		public FixedData getFixedType()
		{
			return new int_f(8);
		}
	}
	public class AS400PackedDecimal extends I2DataType
	{
		public AS400PackedDecimal(int size, int precision)
		{
			super(size, precision);
		}
		public FixedData getFixedType()
		{
			return new packed(size, precision);
		}
	}
	public class AS400ZonedDecimal extends I2DataType
	{
		public AS400ZonedDecimal(int size, int precision)
		{
			super(size, precision);
		}
		public FixedData getFixedType()
		{
			return new zoned(size, precision);
		}
	}
	public class AS400Float4 extends I2DataType
	{
		public AS400Float4()
		{
			super(31, 15);
		}
		public FixedData getFixedType()
		{
			return new int_f(4);
		}
	}
	public class AS400Text extends I2DataType
	{
		public AS400Text(int length)
		{
			super(length, -1);
		}
		public FixedData getFixedType()
		{
			return new fixed(size);
		}
	}
	public class BinaryFieldDescription extends I2FieldDescription
	{
		public BinaryFieldDescription(AS400Bin2 field, String fieldName)
		{
			super(field, fieldName);
		}
		public BinaryFieldDescription(AS400Bin4 field, String fieldName)
		{
			super(field, fieldName);
		}
		public BinaryFieldDescription(AS400Bin8 field, String fieldName)
		{
			super(field, fieldName);
		}
	}
	public class CharacterFieldDescription extends I2FieldDescription
	{
		public CharacterFieldDescription(AS400Text field, String fieldName)
		{
			super(field, fieldName);
		}
	}
	public class PackedDecimalFieldDescription extends I2FieldDescription
	{
		public PackedDecimalFieldDescription(
			AS400PackedDecimal field,
			String fieldName)
		{
			super(field, fieldName);
		}
	}
	public class ZonedDecimalFieldDescription extends I2FieldDescription
	{
		public ZonedDecimalFieldDescription(
			AS400ZonedDecimal field,
			String fieldName)
		{
			super(field, fieldName);
		}
	}
	public class FloatFieldDescription extends I2FieldDescription
	{
		public FloatFieldDescription(AS400Float4 field, String fieldName)
		{
			super(field, fieldName);
		}
	}
	public class DateFieldDescription extends I2FieldDescription
	{
		public DateFieldDescription(AS400Text field, String fieldName)
		{
			super(field, fieldName);
		}
	}
	public class TimeFieldDescription extends I2FieldDescription
	{
		public TimeFieldDescription(AS400Text field, String fieldName)
		{
			super(field, fieldName);
		}
	}
	public class TimestampFieldDescription extends I2FieldDescription
	{
		public TimestampFieldDescription(AS400Text field, String fieldName)
		{
			super(field, fieldName);
		}
	}

	protected void addFieldDescription(I2FieldDescription field)
	{
		fldNames.addElement(field.dataType.name);
		//offsets.addElement(new Integer(addOffset));
		field.offset = addOffset;
		fldDescriptions.addElement(field);
		addOffset += field.dataType.getFixedType().size();
	}
	/**
	 * Return the I2 fixed data type representation of the FieldDescription at the specified index.
	 */
	public String getFieldName(int index)
	{
		I2FieldDescription fd = (I2FieldDescription) fldDescriptions.elementAt(index);
		return fd.toString();
	}
	/**
	 * Return the offset into this buffer of this field.
	 */
	public int getFieldOffset(int index)
	{
		//Integer i = (Integer)offsets.elementAt(index);
		//return i.intValue();
		return ((I2FieldDescription)fldDescriptions.elementAt(index)).offset;
	}
	/**
	 * Return the I2 fixed data type representation of the FieldDescription at the specified index.
	 */
	public FixedData getFixedDataType(int index)
	{
		I2FieldDescription fd = (I2FieldDescription) fldDescriptions.elementAt(index);
		return fd.dataType.getFixedType();
	}
	/**
	 * Return the number of field descriptions in this format.
	 */
	public int getNumberOfFields()
	{
		return fldDescriptions.size();
	}
	
	/** Return the size (in bytes) of this record format */
	public int size()
	{
		return addOffset+1;
	}
}
