/*
 * Created on Feb 28, 2005
 */
package com.i2class;

/**
 * A qualified file name tuple.
 * 
 */
public class QfileName {

	public String libName;
	public String schemaName;
	public String searchSchema;
		public String fileName;
	
	public QfileName() {}
	public QfileName(String libName, String schemaName, String fileName) {
		this.libName=libName;
		this.schemaName=schemaName;
		if (schemaName.endsWith("."))
			searchSchema=schemaName.substring(0, schemaName.length());
		else if (schemaName=="")
			searchSchema=null;
		this.fileName=fileName;
	}
	
	public String toString()
	{
		return schemaName + fileName;
	}

}
