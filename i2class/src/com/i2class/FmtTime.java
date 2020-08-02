package com.i2class;

import java.util.Date;
import java.text.SimpleDateFormat;
/**
 * A formatted date class used by TIME, TIMESTAMP, and UTIMESTAMP.
 * FmtTime is different than FmtDate because it provides a current value instead of a static snapshot.
 * This class is used by the RPG opcode TIME and %BIF %TIME
 * 
 * @see FmtDate
 */
public class FmtTime extends ZonedDecimal
{
	private SimpleDateFormat m_simpleDateFormat;
	/**
	 * Construct a I2 date with the specified size (number of bytes) and Java date format.
	 */
	public FmtTime(int sz, String fmt)
	{
		super(sz, 0);
		m_simpleDateFormat = new SimpleDateFormat(fmt);
	}
	
	/* (non-Javadoc)
	 * @see com.i2class.FixedData#toFixedChar()
	 */
	//public FixedData toFixedChar() {
	/* (non-Javadoc)
	 * @see com.i2class.FixedData#readSubfields()
	 */
	protected void readSubfields() {
		movel(m_simpleDateFormat.format(new Date()));
		//return this;
	}

}
