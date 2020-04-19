package com.i2class;

/**
 * A record class with AS400 field compatibility layer.
 * 
 */
import java.util.Vector;

class RrecordX2 extends RrecordX implements IRecordFormat
{
	private int addOffset;
	Vector fldNames = new Vector();
	Vector fldDescriptions = new Vector();
	//Vector offsets = new Vector();

	public RrecordX2(String recordName)
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
		@Override
		public FixedData getFixedType()
		{
			return new FixedBinary(2);
		}
	}
	/** 4-byte binary value. */
	public class AS400Bin4 extends I2DataType
	{
		public AS400Bin4()
		{
			super(10, 0);
		}
		@Override
		public FixedData getFixedType()
		{
			return new FixedBinary(4);
		}
	}
	/** 8-byte binary value. */
	public class AS400Bin8 extends I2DataType
	{
		public AS400Bin8()
		{
			super(19, 0);
		}
		@Override
		public FixedData getFixedType()
		{
			return new FixedBinary(8);
		}
	}
	public class AS400PackedDecimal extends I2DataType
	{
		public AS400PackedDecimal(int size, int precision)
		{
			super(size, precision);
		}
		@Override
		public FixedData getFixedType()
		{
			return new PackedDecimal(size, precision);
		}
	}
	public class AS400ZonedDecimal extends I2DataType
	{
		public AS400ZonedDecimal(int size, int precision)
		{
			super(size, precision);
		}
		@Override
		public FixedData getFixedType()
		{
			return new ZonedDecimal(size, precision);
		}
	}
	public class AS400Float4 extends I2DataType
	{
		public AS400Float4()
		{
			super(30, 9);
		}
		@Override
		public FixedData getFixedType()
		{
			return new FixedFloat(4);
		}
	}
	public class AS400Float8 extends I2DataType
	{
		public AS400Float8()
		{
			super(63, 17);
		}
		@Override
		public FixedData getFixedType()
		{
			return new FixedFloat(8);
		}
	}
	public class AS400Text extends I2DataType
	{
		public AS400Text(int length)
		{
			super(length, -1);
		}
		@Override
		public FixedData getFixedType()
		{
			return new FixedChar(size);
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
		public FloatFieldDescription(AS400Float8 field, String fieldName)
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
