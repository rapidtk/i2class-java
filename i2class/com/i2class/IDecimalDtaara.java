package com.i2class;

import java.math.BigDecimal;
/**
 * A basic read/write interface to a packed decimal TYPE(*DEC) data area.
 * @see ICharacterDtaara
 * @author Andrew Clark 
 */
interface IDecimalDtaara
{
	/**
	 * Read a decimal data area's value.
	 */
	BigDecimal read() throws Exception;
	/**
	 * Set a decimal data area's value.
	 */
	void write(BigDecimal value) throws Exception;
}
