package com.i2class;

import java.util.Vector;

/**
 * Thread subfile manipulations.
 * @author Andrew Clark
 */
// See getSfl() below for why ISFLCTL is implemented
public class RecordWorkstnSFL extends RecordWorkstn implements IRecordSFL//, IRecordSFLX
{
	RecordSFL sfl_ = new RecordSFL();
	public int recno;
	int pageSize;
	//RecordThreadSFLCTL sflctl;
	ThreadLock scrollLock;
	String sflctlRecordName;
	/**
	 * RRecordThreadSFL constructor comment.
	 * @param recordName java.lang.String
	 */
	public RecordWorkstnSFL(String recordName)
	{
		super(recordName);
	}

	/** Get the changed values vector. 
	public Vector getChangedValues()
	{
		return sfl_.getChangedValues();
	}
	*/ 
	
	/* (non-Javadoc)
	 * @see com.asc.rio.IRecordSFL#addChangedValue(java.lang.Integer)
	 */
	public void addChangedValue(Integer rrn) {
		sfl_.addChangedRRN(rrn);
	}

	public int getReadcRRN()
	{
		return sfl_.getReadcRRN();
	}
	
	public int getRecno()
	{
		return sfl_.getRecno();
	}


	/**
	 * Return the subfile associated with this record (this).
	 */
	/* This is necessary because, for whatever reason, WebFacing associates all of the fields in a subfile
	 * with the control format.  Since it is easier/cleaner to associate them with the SFL itself, this needs to be here 
	 */
	public IRecordSFL getSfl()
	{
		return this;
	}
	
	public int getCurrentSflSize()
	{
		return sfl_.getCurrentSflSize();
	}

	public String getString(int index)
	{
		// If we're in the middle of a scroll request, retrieve the values from the parameter Vectors...
		if (scrollLock != null)
		{
			// The 4.0 WebFacing tooling always adds "l1_" to the front of the field name
			//String parmField = name + "$" + fieldName + "$" + Integer.toString(realrrn);
			String parmField =
				"l1_" + sflctlRecordName + "$" + fldNames.elementAt(index) + "$" + Integer.toString(sfl_.recno);
			int i = scrollLock.parmNames.indexOf(parmField);
			if (i >= 0)
				return (String) (scrollLock.parmValues.elementAt(i));
		}
		return super.getString(index);
	}
	
	public final int getTopRRN()
	{
		return sfl_.getTopRRN();
	}

	/**
	 * Clear (blanks, 0) any input-capable fields.
	 */
	void clearInputBuffer()
	{
	}

	// For subfiles, return nothing here.  The SFLCTL format will return the SFL data.
	String getXML()
	{
		return "";
	}
	
	/** Build XML string for this record.
	 * Loop through all of the subfile records and output a record for each.
	 */
	String getXMLsfl()
	{
		StringBuffer buf = new StringBuffer();
		int sflCount = getCurrentSflSize();
		// Save the current record number so that it can be restored
		int lrecno = getRecno();
		int p=pageSize;
		int topRRN=getTopRRN();
		if (p==0)
			p=sflCount-topRRN+1;
		for (int i=0; i<p; i++)
		{
			setRRN(topRRN+i);
			buf.append(super.getXML());
		}
		setRRN(lrecno);
		return buf.toString();
	}

	// Set the subfile relative record number
	public void setRRN(int rrn)
	{
		fldValues=sfl_.setRRN_(rrn, fldValues, fldNames);
		recno = rrn;
	}

	public void setTopRRN(int rrn)
	{
		sfl_.setTopRRN_(rrn, (RfileWorkstn)file);
	}

	/** Clear subfile. */
	public void sflclr()
	{
		sfl_.sflclr();
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.IRecordSFL#getSFLCTL()
	public IRecordSFLCTL getSFLCTL() {
		return sflctl;
	}
	 */

	/* (non-Javadoc)
	 * @see com.asc.rio.IRecordSFL#checkSFLNXTCHG()
	 */
	public void checkSFLNXTCHG() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.IRecordSFL#getSubfileSize()
	 */
	public int getSubfileSize() {
		return sfl_.subfileSize;
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.IRecordSFL#sflinz()
	 */
	public void sflinz() {
		sfl_.sflinz();
	}


}
