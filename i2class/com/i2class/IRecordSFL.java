package com.i2class;

/**
 * A subfile record interface used by WebFaced applications.
 * @author Andrew Clark
 */
interface IRecordSFL extends IRecord
{
	/** Get the rrn of the topmost (current page) record. */
	int getTopRRN();
	/** Set the relative record number of this subfile. */
	void setRRN(int rrn);
	/** Get the next changed relative record number of this subfile. */
	int getReadcRRN();
	/** Add a relative record number to the changed values. */
	void addChangedValue(Integer rrn);
	/** Set the rrn of the topmost (current page) record. */
	void setTopRRN(int rrn);
	/** Get the current subfile size. */
	int getCurrentSflSize();
	/** Return the subfile control format associated with this subfile. */
	//IRecordSFLCTL getSFLCTL();
	/** If SFLNXTCHG specified on record, add to list of changed values. */
	void checkSFLNXTCHG();
	int getSubfileSize();
	// Clear subfile
	void sflclr();
	// Initialize subfile
	void sflinz();
}
