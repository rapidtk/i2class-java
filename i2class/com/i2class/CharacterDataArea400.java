package com.i2class;

/**
 * An OS/400 character data area TYPE(*CHAR) class.
 * @author Andrew Clark 
 */
class CharacterDataArea400 extends com.ibm.as400.access.CharacterDataArea implements ICharacterDtaara {
/**
 * Construct an OS/400 character data area
 * @param host the host where the dataarea resides
 * @param name the name of the dataarea
 */
CharacterDataArea400(com.ibm.as400.access.AS400 host, String name) {
	super(host, name);
}
}
