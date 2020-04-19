package com.i2class;

/**
 * An OS/400 packed decimal data area TYPE(*DEC) class.
 *  
 */
class DecimalDataArea400
	extends com.ibm.as400.access.DecimalDataArea
	implements IDecimalDtaara
{
	/**
	 * Construct an OS/400 packed decimal data area
	 * @param host the host where the dataarea resides
	 * @param name the name of the dataarea
	 */
	DecimalDataArea400(com.ibm.as400.access.AS400 host, String name)
	{
		super(host, name);
	}
}
