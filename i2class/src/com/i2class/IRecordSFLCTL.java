
package com.i2class;

/**
 * An interface to a SFLCTL format.
 * 
 */
interface IRecordSFLCTL extends IRecordSFLX {
	/** Get the webfaced name (SFLCTL for WebFacing), SFL for Thread) of the subfile. */
	//String getSflDisplayName();
	RecordSFL getSfl_();
	void setScrollLock(ThreadLock threadLock);
	int getPageSize();
	//int getSubfileSize();
}
