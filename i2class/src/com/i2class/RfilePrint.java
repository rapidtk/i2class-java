package com.i2class;

/**
 * A generic print output class.
 * @see RrecordPrint
 */
abstract public class RfilePrint extends Rfile
{
	String overflowIndicator;
	int page; // This should be set to 1 on open()

	/**
	 * Set the overflow indicator for a print file associated with the RPG cycle.
	 */
	void setOverflowIndicator(String ind)
	{
		overflowIndicator = ind;
	}
	
	/* (non-Javadoc)
	 * @see com.i2class.Rfile#setRecord(com.i2class.IRecord)
	 */
	void setRecord(IRecord rcd)
	{
		super.setRecord(rcd);
		// Update the UDATE value of the record (the format is Application-specific)
		RrecordPrint record = (RrecordPrint)rcd;
		if (app != null)
			record.UDATE = app.UDATE;
		// Use default values if not already set
		else
			record.UDATE = Application.JOB_DATE;
	}
}
