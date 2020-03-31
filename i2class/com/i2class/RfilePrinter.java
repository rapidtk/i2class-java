package com.i2class;

/**
 * A generic print output class.
 * @see RecordPrinter
 */
abstract public class RfilePrinter extends Rfile
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
	 * @see com.asc.rio.Rfile#setRecord(com.asc.rio.IRecord)
	 */
	void setRecord(IRecord rcd)
	{
		super.setRecord(rcd);
		// Update the date/time values of the record
		RecordPrinter record = (RecordPrinter)rcd;
		if (app != null)
		{
			record.TIME = app.TIME;
			record.UDATE = app.UDATE;
		}
	}
}