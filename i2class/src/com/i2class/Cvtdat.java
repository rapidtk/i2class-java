/*
 * Created on Nov 15, 2004
 *
 */
package com.i2class;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

import com.i2class.cmd.*;

/**
 * CVTDAT (Convert date) processing.
 * 
 */
public class Cvtdat extends AbstractCommand {

	private String m_date;
	private String tovarString;
	private String m_fromfmt="*JOB";
	private String m_tofmt="*JOB";
	private String m_tosep="*JOB";
	private Rtvjoba m_rtvjoba;
	
	
	/**
	 * 
	 */
	public Cvtdat(I2Connection rconn) {
		super(rconn);
	}
	
	private Rtvjoba rtvjoba()
	{
		if (m_rtvjoba==null)
			m_rtvjoba=new Rtvjoba(getRconn());
		return m_rtvjoba;
	}

	
	/** Convert the format of one date to another. */
	public void exec(Object fromvar, FixedChar tovar) throws Exception {
		setDate(fromvar);
		exec();
		tovar.assign(tovarString);
	}
	/** Convert the format of one date to another. */
	public void exec(Object fromvar, AbstractNumeric tovar) throws Exception {
		setDate(fromvar);
		exec();
		tovar.assign(new BigDecimal(tovarString));
	}
	
	/* (non-Javadoc)
	 * @see com.i2class.ICommand#exec()
	 */
	public void exec() throws Exception {
		// From format
		String fromfmt;
		if (m_fromfmt.compareTo("*JOB")==0)
			fromfmt=rtvjoba().datfmt();
		else if (m_fromfmt.compareTo("*SYSVAL")==0)
			fromfmt=Rtvsysval.datfmt();
		else
			fromfmt=m_fromfmt;

		// To format
		String tofmt;
		if (m_tofmt.compareTo("*JOB")==0)
			tofmt=rtvjoba().datfmt();
		else if (m_tofmt.compareTo("*SYSVAL")==0)
			tofmt=Rtvsysval.datfmt();
		else
			tofmt=m_tofmt;

		// To separator
		String tosep;
		if (m_tosep.compareTo("*JOB")==0)
			tosep=new Character(rtvjoba().datsep()).toString();
		else if (m_tosep.compareTo("*SYSVAL")==0)
			tosep=new Character(Rtvsysval.datsep()).toString();
		else if (m_tosep.compareTo("*NONE")==0)
			tosep="";
		else if (m_tosep.compareTo("*BLANK")==0)
			tosep=" ";
		else
			tosep=m_tosep;

		// Strip separators from from-date
		int dateLength = m_date.length();
		StringBuffer buf = new StringBuffer(m_date);
		for (int i=0; i<dateLength; )
		{
			char c = buf.charAt(i);
			if (c=='/' || c=='-' || c=='.' || c==',')
			{
				buf.deleteCharAt(i);
				dateLength--;
			}
			else
				i++;
		}
		m_date = buf.toString();
		// Convert date format to 'no separators' (e.g. *YMD0)
		fromfmt = fromfmt + '0';
		FixedDate fromdate = new FixedDate(fromfmt, m_date);
		FixedDate todate = new FixedDate(tofmt + tosep);
		todate.assign(fromdate);
		tovarString = todate.toString();		
	}

	static final String[] PARM_NAMES={"DATE", "TOVAR", "FROMFMT", "TOFMT", "TOSEP"};
	/* (non-Javadoc)
	 * @see com.i2class.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}
	
	public void setDate(Object date)
	{
		m_date = date.toString();
	}
	public void setFromfmt(String fromfmt) {
		m_fromfmt = fromfmt;
	}
	public void setTofmt(String tofmt) {
		m_tofmt = tofmt;
	}
	public void setTosep(String tosep) {
		m_tosep = tosep;
	}
}
