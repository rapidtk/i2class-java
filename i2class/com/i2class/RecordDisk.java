package com.i2class;

/**
 * A 
 * @author Andrew Clark
 *
 */
public class RecordDisk  {
	
	private IRHost m_host;
	private IRecord m_record;
	
	
	
	protected void addFieldDescription(I2FieldDescription field)
	{
	}


	/*
	public void addCharField(int length, String fieldName) {
	      addFieldDescription(new CharacterFieldDescription(new AS400Text(length) , fieldName));
	}
	
	public void addBinaryField(int sz, String fieldName) { 
		if (sz<=2) 
			addFieldDescription(new BinaryFieldDescription(new AS400Bin2(), fieldName));
		else if (sz<=4) 
			addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), fieldName));
		else 
			addFieldDescription(new BinaryFieldDescription(new AS400Bin8(), fieldName));
		
	}
	public void addFloatField(int sz, String fieldName) {
		if (sz<=4) 
			addFieldDescription(new FloatFieldDescription(new AS400Float4(), fieldName));
		else 
			addFieldDescription(new FloatFieldDescription(new AS400Float8(), fieldName));
	}
	
	public void addPackedField(int length, int scale, String fieldName) {
		addFieldDescription(new PackedDecimalFieldDescription(new AS400PackedDecimal(length,scale) , fieldName));
	}
	
	public void addZonedField(int length, int scale, String fieldName) {
	    addFieldDescription(new PackedDecimalFieldDescription(new AS400PackedDecimal(length,scale) , fieldName));
	}
	
	public void addDateField(int sz, String fieldName) {
		addFieldDescription(new DateFieldDescription(new AS400Text(sz) , fieldName));
	}
	
	public void addTimeField(int sz, String fieldName) {
		addFieldDescription(new TimeFieldDescription(new AS400Text(sz) , fieldName));
	}
	public void addTimeStampField(int sz, String fieldName) {
		addFieldDescription(new TimestampFieldDescription(new AS400Text(sz) , fieldName));
	}
	*/

}
