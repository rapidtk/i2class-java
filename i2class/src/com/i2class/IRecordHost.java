package com.i2class;

/**
 * A 
 * 
 *
 */
public interface IRecordHost {
	
	public void addCharField(int length, String fieldName);
	
	public void addBinaryField(int sz, String fieldName);
	public void addFloatField(int sz, String fieldName); 
	public void addPackedField(int length, int scale, String fieldName);
	public void addZonedField( int length, int scale, String fieldName);
	
	public void addDateField(int sz, String fieldName);
	public void addTimeField(int sz, String fieldName);
	public void addTimeStampField(int sz, String fieldName);
}
