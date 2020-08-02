package com.i2class;

import java.util.Iterator;

/**
 * Thread subfile control format manipulations.
 * 
 */
public class RecordWorkstnSFLCTL extends RecordWorkstn implements IRecordSFLCTL
{
	RecordWorkstnSFL sfl;
	private String subfileRecordNumberFieldName;
	int pageSize;
	// Moved to RecordThreadSFL
	//private int subfileSize;
	//ThreadLock scrollLock;

	/**
	 * RecordWebfaceSFL constructor comment.
	 * @param fmtName java.lang.String
	 * @param rvd com.ibm.as400ad.webfacing.runtime.view.RecordViewDefinition
	 * @param rfd com.ibm.as400ad.webfacing.runtime.view.RecordFeedbackDefinition
	 */
	public RecordWorkstnSFLCTL(String fmtName, RecordWorkstnSFL lsfl)
	{
		super(fmtName);
		sfl = lsfl;
		sfl.sflctlRecordName = fmtName;
		//sfl.sflctl = this;
	}

	/** Build XML string for this record.
	 * Add subfile data to control record
	 */
	String getXML()
	{
		//return super.getXML() + sfl.getXML_();
		return getXMLclose(getXMLBuffer().append(sfl.getXMLsfl()));
	}

	/** Build XML string for this record's function keys */
	String getXMLfkey()
	{
		// Allow "positioning" PAGEUP/PAGEDOWN for subfile if they don't already exist.
		StringBuffer buf = new StringBuffer();

		// Add PAGEDOWN, if appropriate
		int topRRN = sfl.getTopRRN();
		DefinitionAID def = (DefinitionAID)definitions[AID_PAGEDOWN-1];
		if ((def == null || !evaluateIndicatorExpression(def.indicatorExpression)) && topRRN+pageSize<=sfl.getCurrentSflSize())
			buf.append("<fkey name='PAGEDOWN' value='PAGEDOWN'/>");

		// Add PAGEUP, if appropriate
		def = (DefinitionAID)definitions[AID_PAGEUP-1];
		if ((def == null || !evaluateIndicatorExpression(def.indicatorExpression)) && topRRN>1)
			buf.append("<fkey name='PAGEUP' value='PAGEUP'/>");
		return super.getXMLfkey() + buf.toString();
	}

	/**
	 * Return the subfile associated with this subfile control format
	 */
	public IRecordSFL getSfl()
	{
		return sfl;
	}
	public RecordSFL getSfl_()
	{
		return sfl.sfl_;
	}
	/*
	public void processSpecialValues(Hashtable specialValues) throws Exception
	{
		// Process PAGEUP/PAGEDOWN keys.  We need to decide if this is a simple scroll request or
		// an actual 'return control request'
		String cmdKey = (String) specialValues.get("AID");
		if (cmdKey != null)
		{
			// Deal with CA/CF keys
			int index=-1;
			if (cmdKey.compareTo("PAGEDOWN")==0)
			{
				if (sfl.topRRN + pageSize > sfl.getSflSize())
					index = AID_PAGEDOWN;
			}
			else if (cmdKey.compareTo("PAGEUP")==0)
			{
				if (sfl.topRRN - pageSize <1)
					index = AID_PAGEUP;
			}
		}
		// Process VLDCMDKEY keyword
		// Process CHANGE keyword
	}
	*/

	public String getSflDisplayName()
	{
		return sfl.recordName;
	}

	public boolean processIndicators()
	{
		boolean isDSP = false;
		//DefinitionKeyword kd = (DefinitionKeyword) definitions.elementAt(KWD_SFLCLR);
		String expression=null;
		DefinitionKeyword kd = definitions[KWD_SFLCLR-1];
		if (kd != null)
			expression = kd.indicatorExpression;
		if (kd!=null && (expression==null || evaluateIndicatorExpression(expression)))
			sfl.sflclr();
		else
		{
			// See if SFLRCDNBR keyword is specified
			//String sflrcdnbr = ((SubfileControlRecordDataDefinition)dataDef).getSubfileRecordNumberFieldName();
			String sflrcdnbr = subfileRecordNumberFieldName;
			if (sflrcdnbr != null)
			{
				String value = getString(sflrcdnbr);
				if (value != "")
				{
					int top = new Double(value).intValue();
					top = ((top - 1) / pageSize) * pageSize + 1;
					sfl.setTopRRN(top);
				}
			}

			// Process SFLDSPCTL
			/* SFLDSPCTL never seems to get returned!?!
			kd = viewDef.getKeywordDefinition(com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers.KWD_SFLDSPCTL);
			if (kd != null)
			{
				expression = kd.getIndicatorExpression();
				isSFLDSPCTL = evaluateIndicatorExpression(expression);
			}
			*/
			isDSP = true;
		}
		// If SFLDSP is specified, then we have to clear the subfile input buffer
		//kd = viewDef.getKeywordDefinition(com.ibm.as400ad.code400.dom.ENUM_KeywordIdentifiers.KWD_SFLDSP);
		//expression = kd.getIndicatorExpression();
		//if (evaluateIndicatorExpression(expression))
		{
			//int changeCount = sfl.sfl_.changedValues.size();
			int changeCount = sfl.sfl_.changedRRN_.size();
			if (changeCount > 0)
			{
				int lastRRN = sfl.getRecno();
				Iterator it = sfl.sfl_.changedRRN_.iterator();
				for (int i = 0; i < changeCount; i++)
				{
					//String rrn = (String) sfl.sfl_.changedValues.elementAt(i);
					Integer rrn = (Integer) it.next();
					sfl.setRRN(rrn.intValue());
					sfl.clearInputBuffer();
				}
				sfl.sfl_.changedRRN_.clear();
				sfl.setRRN(lastRRN);
			}
		}

		return isDSP;
	}
	
	/**
	 * Set the subfile page size (the number of records that can be shown on a page at once).  
	 */	
	protected void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
		sfl.pageSize = pageSize;
	}

	/**
	 * Set the subfile size (the total number of records that the subfile can show).  
	 */	
	protected void setSubfileSize(int subfileSize)
	{
		//this.subfileSize = subfileSize;
		sfl.sfl_.subfileSize = subfileSize;
	}
	
	/** Set the subfile scroll object for pageup-pagedown requests. */
	public void setScrollLock(ThreadLock threadLock)
	{
		sfl.scrollLock=threadLock;
	}

	/**
	 * Set the name of the field that controls the topmost record dislayed in a subfile.
	 */
	protected void setSubfileRecordNumberFieldName(String fieldName)
	{
		subfileRecordNumberFieldName = fieldName;
	}

	/* (non-Javadoc)
	 * @see com.i2class.IRecordSFLCTL#getPageSize()
	 */
	public int getPageSize() {
		return pageSize;
	}

	/* (non-Javadoc)
	 * @see com.i2class.IRecordSFLCTL#getSflSize()
	public int getSubfileSize() {
		return subfileSize;
		// TODO Auto-generated method stub
	}
	 */



}
