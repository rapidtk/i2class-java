
package com.i2class;

import javax.servlet.http.*;

/**
 * An interface to a SFLCTL format.
 * @author ANDREWC
 */
interface IRecordSFLCTL extends IRecordSFLX {
	/** Get the webfaced name (SFLCTL for WebFacing), SFL for Thread) of the subfile. */
	//String getSflDisplayName();
	RecordSFL getSfl_();
	void setScrollLock(ThreadLock threadLock);
	int getPageSize();
	//int getSubfileSize();
}
