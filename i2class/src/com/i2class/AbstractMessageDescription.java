/*
 * Created on Nov 16, 2004
 *
 */
package com.i2class;

import java.sql.*;
import java.util.Vector;

/**
 * A message description.
 * 
 *
 */
public abstract class AbstractMessageDescription {

	/** Message text. */
	String m_msg;
	/** Second level text. */
	String m_seclvl;
	/** 7-character message ID */
	String m_msgid;
	/* Severity level 0-99 */
	int m_sev;
	
	/** FMT substitution variable entry */
	class FMT
	{
		int m_dataType;
		int m_length;
		int m_varbyteOrDec;
		FMT(int dataType, int length, int varbyteOrDec)
		{
			m_dataType=dataType;
			m_length=length;
			m_varbyteOrDec=varbyteOrDec;
		}
	}
	// Vector of FMT entries
	Vector fmts = new Vector();
	
	public static final int TYPES_QTDCHAR=1;
	public static final int TYPES_CHAR = 2;
	public static final int TYPES_CCHAR = 3;
	public static int TYPES_HEX = 4;
	public static final int TYPES_SPP = 5;
	public static final int TYPES_DEC = 6;
	public static final int TYPES_BIN = 7;
	public static final int TYPES_UBIN = 8;
	public static final int TYPES_DTS = 9;
	public static final int TYPES_SYP = 10;
	public static final int TYPES_ITV = 11;
	public final String[] DATA_TYPES={"*QTDCHAR", "*CHAR", "*CCHAR", "*HEX", "*SPP", "*DEC", "*BIN", "*UBIN", "*DTS", "*SYP", "*ITV"};
	
	public AbstractMessageDescription(String msgid, String msg, String seclvl, int sev)
	{
		m_msgid=msgid;
		m_msg=msg;
		m_seclvl=seclvl;
		m_sev=sev;
	}
	
	public void addFmt(String dataType, int length, int varbyteOrDec)
	{
		for (int i=0; i<DATA_TYPES.length; i++)
		{
			if (dataType.compareTo(DATA_TYPES[i])==0)
			{
				FMT fmt = new FMT(i+1, length, varbyteOrDec);
				fmts.add(fmt);
			}
		}
	}
	
}
