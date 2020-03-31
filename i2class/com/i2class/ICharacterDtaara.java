package com.i2class;

/**
 * A basic read/write interface to a character TYPE(*CHAR) data area.
 */
interface ICharacterDtaara
{
	/**
	 * Read all of the data from a data area.
	 */
	String read() throws Exception;
	/**
	 * Read a substring of data from a data area.
	 * @parm offset The 1-based index to read from
	 * @parm length The length of data to read
	 */
	String read(int offset, int length) throws Exception;
	/**
	 * Rewrite the entire data area.
	 */
	void write(String value) throws Exception;
	/**
	 * Rewrite a specified substring of data in a data area.
	 * @parm offset The 1-based index to write to
	 * @parm length The length of data to write
	 */
	void write(String value, int offset) throws Exception;
}
