package com.i2class;

/**
 * An OS/400-specific representation of the *LDA.
 * @author Andrew Clark 
 */
class LocalDataArea400
	extends com.ibm.as400.access.LocalDataArea
	implements ICharacterDtaara
{
	/**
	 * Construct an OS/400 local data area object
	 * @param host the host where the dataarea resides
	 */
	LocalDataArea400(com.ibm.as400.access.AS400 host)
	{
		super(host);
	}
}
