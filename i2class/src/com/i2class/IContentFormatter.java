package com.i2class;

public interface IContentFormatter {
	public void addField(StringBuffer buf, String fldName, String value);
	public void addFkey(StringBuffer buf, String name);

}
