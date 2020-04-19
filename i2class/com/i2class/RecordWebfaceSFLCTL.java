package com.i2class;

import java.io.IOException;
import java.util.*;
import java.util.List;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.runtime.controller.IReadOutputBuffer;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingLevelCheckException;
import com.ibm.as400ad.webfacing.runtime.dhtmlview.*;
import com.ibm.as400ad.webfacing.runtime.dhtmlview.IScrollbarBean;
import com.ibm.as400ad.webfacing.runtime.model.*;
import com.ibm.as400ad.webfacing.runtime.model.ISFLCTLRecordData;
import com.ibm.as400ad.webfacing.runtime.model.def.*;
import com.ibm.as400ad.webfacing.runtime.view.IDisplaySFLCTLRecord;
import com.ibm.as400ad.webfacing.runtime.view.def.*;

/**
 * A WebFacing subfile control format class.
 * 
 * @version 5.0
 */
public class RecordWebfaceSFLCTL
	extends RecordWebface
	implements IDisplaySFLCTLRecord, IScrollbarBean, IRecordSFLCTL
{

	RecordWebfaceSFL sfl;
	private String scrollbarName;
	private int pageSize;
	// Moved to RecordWebfaceSFL?
	//private int subfileSize;
	ThreadLockWebface scrollLock;

	/**
	 * Create the class with the specified format name and subfile class.
	 */
	public RecordWebfaceSFLCTL(String fmtName, RecordWebfaceSFL lsfl)
	{
		super(fmtName);
		sfl = lsfl;
		//sfl.sflctl = this;
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


	public boolean enablePageDown()
	{
		return false;
	}

	public boolean enablePageUp()
	{
		return false;
	}

	public boolean evaluateIndicatorExpression(int rrn, String expression)
	{
		int lastRRN = sfl.getRecno();
		sfl.setRRN(rrn + sfl.getTopRRN() - 1);
		boolean value = sfl.evaluateIndicatorExpression(expression);
		sfl.setRRN(lastRRN);
		return value;
	}

	public String evaluateStyleClass(
		int rrn,
		com.ibm.as400ad.webfacing.runtime.view.DisplayAttributeBean dab)
	{
		int lastRRN = sfl.getRecno();
		sfl.setRRN(rrn + sfl.getTopRRN() - 1);
		String value = sfl.evaluateStyleClass(dab);
		sfl.setRRN(lastRRN);
		return value;
	}

	public String getFieldValue(String arg1, int arg2)
	{
		return null;
	}

	public String getFieldValueWithTransform(
		String fieldName,
		int rrn,
		int transform)
	{
		int realrrn = rrn + sfl.getTopRRN() - 1;
		// If we're in the middle of a scroll request, retrieve the values from the parameter Vectors...
		if (scrollLock != null)
		{
			// The 4.0 WebFacing tooling always adds "l1_" to the front of the field name
			//String parmField = name + "$" + fieldName + "$" + Integer.toString(realrrn);
			String parmField =
				"l1_" + recordName + "$" + fieldName + "$" + Integer.toString(realrrn);
			int i = scrollLock.parmNames.indexOf(parmField);
			if (i >= 0)
				return (String) (scrollLock.parmValues.elementAt(i));
		}

		int lastRRN = sfl.getRecno();
		sfl.setRRN(realrrn);
		// For whatever reason, SFL fields are not defined in the SFL record but in the SFLCTL format?
		FieldViewDefinition fvd =
			(
				(
					SubfileControlRecordViewDefinition) viewDef)
						.getSubfileFieldViewDefinition(
				fieldName);
		String value = sfl.getEditedFieldValue(fieldName, fvd);
		/*
		FieldViewDefinition fvd = ((SubfileControlRecordViewDefinition)viewDef).getSubfileFieldViewDefinition(fieldName);
		FieldDataDefinition fdd = sfl.dataDef.getFieldDefinition(fieldName);
		fvd.setFieldDataDefinition(fdd);
		// For numeric fields, return decimal formatted string
		if (fvd.isNumeric())
		{
			int valuelen = value.length();
			if (valuelen > 2 && value.substring(valuelen-2).compareTo(".0")==0)
				value = value.substring(0, valuelen-3);
			fvd.setEditFormatter(new EditcodeEditwordFormatter());
			value = fvd.getDecimalFormattedString(new StringBuffer(value));
			EditcodeEditwordFormatter e = fvd.getEditFormatter();
			if (e != null)
				value = e.formatString(value);
		}
		*/
		/* Double any quotes in value */
		if (transform == IHTMLStringTransforms.TRIMMED_JAVA_STRING_TRANSFORM)
		{
		   int q2 = value.indexOf('"');
		   if (q2>=0)
		   {
				int q1 = 0;
				StringBuffer qvalue = new StringBuffer();
			   do
			   {
				   qvalue.append(value.substring(q1, q2));
				   qvalue.append('\\');
				   q1=q2;
				   q2 = value.indexOf('"', q2+1);
			   } while(q2>0);
				qvalue.append(value.substring(q1));
				value = qvalue.toString();
		   }
		}

		sfl.setRRN(lastRRN);
		return value;
	}

	public String getFieldValueWithTransform(
		String fieldName,
		int rrn,
		int transform,
		int arg4,
		int arg5)
	{
		return getFieldValueWithTransform(fieldName, rrn, transform);
	}

	/**
	 * Return HTML source for scrollbar.
	 */
	public String getHTMLSource()
	{
		// The scroll-bar code has changed in WebFacing 4.0
		StringBuffer html = new StringBuffer();
		int lastRecordNumber = getLastRecordNumber();

		// Determine if PAGEUP...
		int sflTopRRN = sfl.getTopRRN();
		boolean scrollUp = (sflTopRRN > 1);
		boolean pageUp = false;
		if (!scrollUp)
			pageUp = isPageUp();

		// ...or PAGEDOWN is required
		int downRRN = sflTopRRN + pageSize;
		boolean scrollDown = (downRRN <= lastRecordNumber);
		boolean pageDown = false;
		if (!scrollDown)
			pageDown = isPageDown();
		if (lastRecordNumber > pageSize || pageUp || pageDown)
		{
			// The scroll bar is actually a table with an up arrow, segments for each page of the subfile, and a down arrow
			html.append(
				"<table id='"
					+ scrollbarName
					+ "' style='position:relative; border:0; visibility:hidden; z-index:0;' border='0' cellspacing='0' cellpadding='0'>\n");

			// Build up-arrow source
			html.append("<tr ");
			//if (scrollUp || pageUp)
			//	html.append(" title='Page Up'");
			if (scrollUp)
				html.append(" title='Page Up'");
			else if (pageUp)
				html.append(" title='Previous...'");
			html.append(
				"><td id='" + scrollbarName + "UpArrow' valign='top' onClick='");
			// If we're already at the top, then the up arrow doesn't actually do anything... */
			if (scrollUp)
			{
				int upRRN = sflTopRRN - pageSize;
				if (upRRN < 1)
					upRRN = 1;
				html.append(
					"changePage(\"" + recordName + "\"," + Integer.toString(upRRN) + ")");
			}
			// ...unless a PAGEUP is allowed, which actually sends a function key to the host
			else if (pageUp)
				// Webfacing 5.1.2 adds ,false to submit request
				//html.append("validateAndSubmit(\"PAGEUP\");");
				html.append("validateAndSubmit(\"PAGEUP\",false);");
			//html.append("' class='scrollbarUpArrow' ><IMG src='" + contextPath + "/I2web/PageUp.gif'></td></tr>\n");
			//html.append("' class='scrollbarUpArrow'><IMG src='" + contextPath + "/styles/transparent.gif' width='16' height='16'></td></tr>\n");
			html.append(
				"' class='scrollbarUpArrow'><IMG src='"
					+ contextPath
					+ "/I2web/PageUp.gif' width='16' height='16'></td></tr>\n");

			// Build segments
			html.append("<tr height='100%' ><td>");
			html.append(
				"<table class='scrollbarBackground' border='0' cellspacing='0' cellpadding='0' height='100%' width='100%'>\n");
			//String pagePct = Integer.toString(pageSize*100 / (lastRecordNumber + pageSize));
			// If a page down is allowed, then allow a small slice (5%) at the end of the scroll-bar to show a 'More...' relationship
			int m100 = 100;
			if (pageDown)
				m100 = 95;
			if (pageUp)
				m100 -= 5;
			String pagePct =
				Integer.toString(
					pageSize * m100 / java.lang.Math.max(lastRecordNumber, 1));
			int n = 0;
			// If a page up is allowed, then allow a small slice (5%) at the beginning of the scroll-bar to show a 'Previous...' relationship
			if (pageUp)
			{
				html.append(
					"<tr  height='5%' title = 'Previous...'><td id='"
						+ scrollbarName
						// Webfacing 5.1.2 adds ,false to submit request
						//+ "Segment0' onClick = 'validateAndSubmit(\"PAGEUP\");'></td></tr>\n");
						+ "Segment0' onClick = 'validateAndSubmit(\"PAGEUP\",false);'></td></tr>\n");
				n = 1;
			}
			// If there are more than 50 increments, only show 50 of them
			int pageIncrement=pageSize;
			int bigIncrement=pageSize;
			if (lastRecordNumber/pageSize > 50)
				bigIncrement = lastRecordNumber/50/pageSize*pageSize;
			for (int i = 1; i <= lastRecordNumber; i += pageIncrement)
			{
				// Make sure that the first and last increment is always shown even if there are 50+ slices
				if (i==1 || lastRecordNumber-i<bigIncrement)
					pageIncrement=pageSize;
				else
					pageIncrement = bigIncrement;
				int maxrcd = i + pageIncrement - 1;
				
				if (maxrcd > lastRecordNumber)
				{
					maxrcd = lastRecordNumber;
					pagePct =
						Integer.toString(
							(lastRecordNumber - i + 1)
								* 100
								/ java.lang.Math.max(lastRecordNumber, 1));
				}
				String segBegin = Integer.toString(i);
				html.append("<tr ");
				boolean currentSegment = (i <= sflTopRRN && maxrcd >= sflTopRRN);
				if (currentSegment)
					html.append("class='scrollbarSlider' ");
				html.append(
					"height='"
						+ pagePct
						+ "%' title = '"
						+ segBegin
						+ " - "
						+ Integer.toString(maxrcd)
						+ "'><td id='"
						+ scrollbarName
						+ "Segment"
						+ Integer.toString(n)
						+ "' onClick = '");
				if (!currentSegment)
					html.append("changePage(\"" + recordName + "\", " + segBegin + ")");
				html.append("'></td></tr>\n");
				n++;
			}
			// If a page down is allowed, then allow a small slice (5%) at the end of the scroll-bar to show a 'More...' relationship
			if (pageDown)
			{
				html.append(
					"<tr  height='5%' title = 'More...'><td id='"
						+ scrollbarName
						+ "Segment"
						+ Integer.toString(n)
						// Webfacing 5.1.2 adds ,false to submit request
						//+ "' onClick = 'validateAndSubmit(\"PAGEDOWN\");'></td></tr>\n");
						+ "' onClick = 'validateAndSubmit(\"PAGEDOWN\",false);'></td></tr>\n");
			}
			html.append("</table>\n</td></tr>\n");

			// Build down-arrow source
			html.append("<tr ");
			if (scrollDown)
				html.append(" title='Page Down'");
			else if (pageDown)
				html.append(" title='More...'");
			html.append(
				"><td id='"
					+ scrollbarName
					+ "DownArrow' valign = 'bottom' onClick = '");
			// If we're already at the bottom, then the down arrow doesn't actually do anything */
			if (scrollDown)
			{
				int upRRN = sflTopRRN - pageSize;
				if (upRRN < 1)
					upRRN = 1;
				html.append(
					"changePage(\""
						+ recordName
						+ "\","
						+ Integer.toString(downRRN)
						+ ")");
			}
			// ...unless a PAGEUP is allowed, which actually sends a function key to the host
			else if (pageDown)
				// Webfacing 5.1.2 adds ,false to submit request
				//html.append("validateAndSubmit(\"PAGEDOWN\");");
				html.append("validateAndSubmit(\"PAGEDOWN\",false);");
			//html.append("' class='scrollbarDownArrow'><IMG src='" + contextPath + "/I2web/PageDown.gif'></td></tr>\n</table>");
			//html.append("' class='scrollbarDownArrow' ><IMG src='" + contextPath + "/styles/transparent.gif' width='16' height='16'></td></tr>\n</table>");
			html.append(
				"' class='scrollbarDownArrow' ><IMG src='"
					+ contextPath
					+ "/I2web/PageDown.gif' width='16' height='16'></td></tr>\n</table>");
		}
		return html.toString();

	}

	public int getLastRecordNumber()
	{
		int count = 0;
		sfl.getCurrentSflSize();
		int sflRecno = sfl.getRecno();
		if (count < sflRecno)
			return sflRecno;
		return count;
	}

	public int getNumberOfRecords()
	{
		return 0;
	}

	public String getPositioningHTMLSource()
	{
		return "<img src='"
			+ contextPath
			+ "/styles/transparent.gif' width='693' height='0'>";
	}

	public String getPositioningHTMLSource(int arg1)
	{
		return getPositioningHTMLSource();
	}

	public int getRRN()
	{
		//return sfl.previousRRN;
		return 1;
	}

	public String getSflDisplayName()
	{
		return recordName;
	}

	public int getSubfileAreaFirstRow()
	{
		SubfileControlRecordViewDefinition rvd =
			(SubfileControlRecordViewDefinition) viewDef;
		int i = rvd.getSubfileAreaFirstRow();
		return i;
	}

	public int getSubfileAreaHeight()
	{
		SubfileControlRecordViewDefinition rvd =
			(SubfileControlRecordViewDefinition) viewDef;
		int i = rvd.getSubfileAreaHeight();
		return i;
	}

	
	// Return the number of subfile records currently visible, taking into account folded records
	public int getVisibleRecordSize()
	{
		// TODO actually consider whether we are beginning/end of subfile, etc.
		return getPageSizeConsiderFold();
	}

	public boolean isActiveRecord(int rrn)
	{
		int truerrn = rrn + sfl.getTopRRN() - 1;
		// If we are testing the current record, then this will always be true
		if (truerrn == sfl.getRecno())
			return true;
		// If it is past the end of the SFLPAG, then it is always false
		if (truerrn > sfl.getCurrentSflSize() /*|| truerrn<=0*/
			)
			return false;
		// Otherwise, see if a value has been written.
		Object fieldValues = sfl.sfl_.fldValuesRRN.elementAt(truerrn - 1);
		return (fieldValues != null);
	}

	public boolean isFieldVisible(int arg1, String arg2)
	{
		// TODO Is it really visible?
		return false;
	}

	public boolean isMDTOn(int arg1, String arg2)
	{
		// TODO Is MDT really on?
		return false;
	}

	private boolean isPageDown()
	{
		boolean pageDown = false;
		KeywordDefinition kd = viewDef.getKeywordDefinition(ENUM_KeywordIdentifiers.KWD_PAGEDOWN);
		if (kd != null)
		{
			String expression = kd.getIndicatorExpression();
			pageDown = evaluateIndicatorExpression(expression);
		}
		return pageDown;
	}

	private boolean isPageUp()
	{
		boolean pageUp = false;
		KeywordDefinition kd =
			viewDef.getKeywordDefinition(
				com
					.ibm
					.as400ad
					.code400
					.dom
					.constants
					.ENUM_KeywordIdentifiers
					.KWD_PAGEUP);
		if (kd != null)
		{
			String expression = kd.getIndicatorExpression();
			pageUp = evaluateIndicatorExpression(expression);
		}
		return pageUp;
	}

	public boolean isRecordPastEndOfSubfile(int rrn)
	{
		if (rrn == sfl.getRecno())
			return false;
		int lastRRN = sfl.getCurrentSflSize();
		return ((rrn + sfl.getTopRRN() - 1) > lastRRN);
	}

	public boolean isRowOfSubfileRecordDisplayed(int arg1, int arg2)
	{
		return false;
	}

	public boolean isScrollbarShown()
	{
		int lastRecordNumber = getLastRecordNumber();
		return (lastRecordNumber > pageSize || isPageDown() || isPageUp());
	}

	public boolean isSubfileControlVisible()
	{
		//TODO Is it really visible?
		return true;
	}

	public boolean isSubfileFolded()
	{
		//TODO Is it really folded?
		return true;
	}

	public boolean isSubfileVisible()
	{
		//TODO is it really visible?
		return true;
	}

	public void setContext(String arg1)
	{
	}

	public void setContext(javax.servlet.http.HttpSession arg1)
	{
	}

	public void setControlRecordViewBean(
		com.ibm.as400ad.webfacing.runtime.view.IBuildSFLCTLViewBean arg1)
	{
	}
	
	/** Set the subfile scroll object for pageup-pagedown requests. */
	public void setScrollLock(ThreadLock threadLock)
	{
		scrollLock=(ThreadLockWebface)threadLock;
	}

	public void setScrollbarJavascriptID(int zindex, String scrollbar)
	{
		scrollbarName = "l" + Integer.toString(zindex) + "_" + scrollbar;
	}

	public void setScrollbarJavascriptID(String scrollbar)
	{
		scrollbarName = scrollbar;
	}

	public String getSubfileValuesAfterEditing(String param1)
	{
		return null;
	}

	public boolean processIndicators()
	{
		super.processIndicators();
		boolean isDSP = false;
		// Process SFLCLR
		boolean sflclr=false;
		KeywordDefinition kd = viewDef.getKeywordDefinition(ENUM_KeywordIdentifiers.KWD_SFLCLR);
		if (kd != null)
		{
			String expression = kd.getIndicatorExpression();
			sflclr = evaluateIndicatorExpression(expression);
		}
		if (sflclr)
			sfl.sflclr();
		else
		{
			// Process SFLINZ
			boolean sflinz=false;
			kd = viewDef.getKeywordDefinition(ENUM_KeywordIdentifiers.KWD_SFLINZ);
			if (kd != null)
			{
				String expression = kd.getIndicatorExpression();
				sflinz = evaluateIndicatorExpression(expression);
			}
			//TODO ??? If the record is a subfile program message queue, always initialize if not specified???
			if (sflinz || kd==null && sfl instanceof RecordWebfaceSFLMSGRCD)
				sfl.sflinz();
			// The record could be displayed on SFLINZ...
			//else
			{
				// See if SFLRCDNBR keyword is specified
				//String sflrcdnbr = ((SubfileControlRecordDataDefinition)dataDef).getSubfileRecordNumberFieldName();
				String sflrcdnbr =
					((SubfileControlRecordFeedbackDefinition) feedbackDef)
						.getSubfileRecordNumberFieldName();
				if (sflrcdnbr != null)
				{
					String value = getFieldValue(sflrcdnbr);
					if (value != "")
					{
						int top = new Double(value).intValue();
						// Set top value to the page containing the record number
						int pageSize =
							((SubfileControlRecordDataDefinition) dataDef).getPageSize();
						top = ((top - 1) / pageSize) * pageSize + 1;
						sfl.setTopRRN(top);
					}
					else
						sflrcdnbr = null;
				}
				// If no SFLRCDNBR was specified, then always go to RRN 1
				if (sflrcdnbr==null)
					sfl.setTopRRN(1);
					
	
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
		}
		// If SFLDSP is specified, then we have to clear the subfile input buffer
		//kd = viewDef.getKeywordDefinition(com.ibm.as400ad.code400.dom.ENUM_KeywordIdentifiers.KWD_SFLDSP);
		//expression = kd.getIndicatorExpression();
		//if (evaluateIndicatorExpression(expression))
		{
			int changeCount = sfl.sfl_.changedRRN_.size();
			if (changeCount > 0)
			{
				int lastRRN = sfl.getRecno();
				Iterator it = sfl.sfl_.changedRRN_.iterator();
				for (int i = 0; i < changeCount; i++)
				{
					//String rrn = (String) sfl.sfl_.changedValues.elementAt(i);
					int rrn = ((Integer)it.next()).intValue();
					sfl.setRRN(rrn);
					sfl.clearInputBuffer();
				}
				sfl.sfl_.changedRRN_.clear();
				sfl.setRRN(lastRRN);
			}
			// Re-add any values set by SFLNXTCHG
			sfl.sfl_.changedRRN_.addAll(sfl.sfl_.changedSFLNXTCHG);
			sfl.sfl_.changedSFLNXTCHG.clear();
		}


		return isDSP;
	}

	/**
	 * @see RecordWebface#setFile
	 */
	void setFile(RfileWorkstn file) throws Exception
	{
		super.setFile(file);
		sfl.setFile(file);
		// This used to be in the constructor, but change it to be delay loaded
		if (pageSize == 0)
		{
			SubfileControlRecordDataDefinition sflctlDataDef = (SubfileControlRecordDataDefinition)dataDef;
			pageSize = sflctlDataDef.getPageSize();
			//subfileSize = sflctlDataDef.getSubfileSize();
			sfl.sfl_.subfileSize = sflctlDataDef.getSubfileSize();
		}
	}
	/*
	public boolean readParmsOld(javax.servlet.http.HttpServletRequest request)
		throws Exception
	{
		clearParms();
		return readParms2(request);
	}
	*/
	
	/** Check for a positioning request. */
	public int checkPositioning(javax.servlet.http.HttpServletRequest request)
	{
		int sflpos=-1;
		String sflrrn = request.getParameter("SFLRRN");
		if (sflrrn != null)
		{
			int i = sflrrn.indexOf(":");
			sflpos = Integer.parseInt(sflrrn.substring(i + 1));
		}
		return sflpos;
	}

	public boolean disableHyperlink(String parm1, String parm2)
	{
		return false;
	}
	/* (non-Javadoc)
	 * @see com.asc.rio.IRecordSFLCTL#getPageSize()
	 */
	public int getPageSize() {
		return pageSize;
	}
	private int getPageSizeConsiderFold() {
		// TODO actually consider fold
		return pageSize;
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.IRecordSFLCTL#getSflSize()
	public int getSubfileSize() {
		return subfileSize;
	}
	 */
}
