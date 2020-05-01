package com.i2class;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.ibm.as400ad.webfacing.runtime.controller.*;
import com.ibm.as400ad.webfacing.runtime.controller.XMLDefinitionLoader;
import com.ibm.as400ad.webfacing.runtime.fldformat.FieldDataFormatter;
import com.ibm.as400ad.webfacing.runtime.host.*;
import com.ibm.as400ad.webfacing.runtime.model.*;
import com.ibm.as400ad.webfacing.runtime.model.def.*;
import com.ibm.as400ad.webfacing.runtime.view.*;
import com.ibm.as400ad.webfacing.runtime.view.def.*;
import com.ibm.etools.iseries.webfacing.xml.XDocument;

/**
 * A WebFacing display file record format class.
 * 
 * @version 5.0
 * @see com.i2class.RfileWorkstn
 */
public class RecordWebface
	extends RecordWorkstn
	implements IDisplayRecord, IIndicatorValue, IBuildRecordViewBean, IVisibleRectangle {

	protected I2OptionIndicators IN;
	RecordViewDefinition viewDef;
	//RecordViewBean viewBean;
	RecordFeedbackDefinition feedbackDef;
	RecordDataDefinition dataDef;
	Vector inputOnlyFields = new Vector();
	Vector errmsgs = new Vector();
	String contextPath;
	static private SimpleDateFormat timeFormatter;
	static private ZonedDecimal timeZoned;
	// This can be different than the recordName because of webFacing name rules

	/**
	 * Construct using the specified format name.
	 */
	public RecordWebface(String fmtName) {
		//super(getWebfaceName(fmtName));
		super(fmtName);
		//recordName = fmtName;
	}

	/**
	 * A 'delayed' constructor that gets invoked when setRecordFormat() is done.  For files with large numbers of formats, this can 
	 * drastically improve the start-up time of the project.
	 * @see com.i2class.ThreadLockWebface#writeFormats(HttpServletRequest, String, String, boolean)
	 * @see com.i2class.RecordWorkstn#setFile(RfileWorkstn)
	 * @version 5.0
	 */
	void setFile(RfileWorkstn file) throws Exception {
		super.setFile(file);
		// If the data definition hasn't been created yet, then do so now
		if (dataDef == null) {
			ThreadLockWebface webLock = (ThreadLockWebface) file.app.threadLock;
			// For whatever reason, the data/view/feedback definition are all stored in an external jar file called "DDSGeneratedData.jar"
			// We have to manually extract the entry and build an 'XDocument' and then we can use some WF routines to get what we need.
			// Ugghhh!!  Spent about a week trying to get this to work, don't forget Class.getResource()!!!  You can't use URLs to access anything under WEB-INF!!!
			// getClass().getResourceAsStream("/SRCLIB.SRCFIL.SRCMBR/FORMAT.xml") will search for the xml file
			// in all of the jars in the classpath, so we don't need to hardcode any jar file names.
			String xmlFileName =
				"/" + file.actualFileName + '/' + recordName + ".xml";
			/*
			URL u = programClass.getResourceAsStream(xmlFileName);
				URL u = servlet.getServletContext().getResource("/WEB-INF/lib/DDSGeneratedData.jar");
				webLock.urlDDS= "jar:" + u + "!/";
				System.out.println("debug:" + webLock.urlDDS);
			}
			catch (Exception e) 
			{
				System.out.println("debug:" + e);
			}
			String url = webLock.urlDDS + file.fileName+ '/' + recordName + ".xml";
			URL u = new URL(url);
			*/
			InputStream i = getClass().getResourceAsStream(xmlFileName);
			XDocument x = new XDocument();
			x.load(i, false);
			try {
				dataDef =
					(
						RecordDataDefinition) XMLDefinitionLoader
							.loadDataDefinition(
						x);
			} catch (Exception e) {
			}
			if (i == null || dataDef == null)
				throw new Exception(
					"Unable to load xml definition for " + xmlFileName);
			if (!(this instanceof RecordWebfaceSFL)) {
				/*
				viewDef = defFetcher.requestViewDefinition(fmtName);
				feedbackDef = defFetcher.requestFeedbackDefinition(fmtName);
				*/
				viewDef =
					(
						RecordViewDefinition) XMLDefinitionLoader
							.loadViewDefinition(
						x);
				feedbackDef = XMLDefinitionLoader.loadFeedbackDefinition(x);
				//viewBean = viewDef.createViewBean(feedbackDef.createFeedbackBean(this));
			} else {
				viewDef = new RecordViewDefinition(recordName);
				feedbackDef = new RecordFeedbackDefinition(recordName);
			}

			IN = new I2OptionIndicators(dataDef.getIndicatorDefinition());

			// Save all of the input-only fields so that clarInputBuffer() knows what to change
			Iterator fieldDefs = dataDef.getFieldDefinitions().iterator();
			while (fieldDefs.hasNext()) {
				FieldDataDefinition fdd =
					(FieldDataDefinition) fieldDefs.next();
				if (fdd.isInputOnly()) {
					String fieldName = fdd.getFieldName();
					inputOnlyFields.add(fieldName);
				}
			}
		}
	}

	/**
	 * Clear (blanks, 0) any input-capable fields.
	 */
	void clearInputBuffer() {
		int inputOnlyCount = inputOnlyFields.size();
		for (int i = 0; i < inputOnlyCount; i++) {
			String fieldName = (String) inputOnlyFields.elementAt(i);
			try {
				//setText(fieldName, "");

				// Not sure what the 1388 is, but probably UTF-8?
				//String defaultValue = fdd.getDefaultValue(1388);

				// Just always use blank if not default value is specified to speed things up
				String defaultValue;
				FieldDataDefinition fdd = dataDef.getFieldDefinition(fieldName);
				if (fdd!=null && fdd.hasDefaultValue())
					defaultValue = fdd.getDFTVAL();
				else
					defaultValue="";
				setText(fieldName, defaultValue);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Test a 400-style indicator expression (e.g. "N50 60").
	 */
	public boolean evaluateIndicatorExpression(String expression) {
		if (expression == null)
			return false;
		//IndicatorDataDefinition id=dataDef.getIndicatorDefinition();
		//I2OptionIndicators oi = new I2OptionIndicators(id);
		//TODO deal with program-system fields
		if (expression.startsWith("PF"))
			return false;
		boolean value = IN.evaluateIndicatorExpression(expression);
		return value;
	}

	/**
	 * Return a style name for an os-400 style display attribute (e.g. BLUE+HI).
	 * The returned names changed in 4.0
	 * @version 4.0
	 */
	public String evaluateStyleClass(
		com.ibm.as400ad.webfacing.runtime.view.DisplayAttributeBean ab) {
		// The 4.0 WebFacing styles are different, prepending 'wf_' to the style names used in previous versions
		StringBuffer styleClass = new StringBuffer();
		// Highlight indicator expression
		String expression = ab.getHighlightIndExpr();
		if (evaluateIndicatorExpression(expression))
			styleClass.append("wf_hi ");

		// Underline indicator expression
		expression = ab.getUnderlineIndExpr();
		if (evaluateIndicatorExpression(expression))
			styleClass.append("wf_ul ");
		// 5.0 adds support for CHGINPDFT
		else if (expression != null || ab.getChginpdftNoUL())
			styleClass.append("wf_borderOff ");

		// ColSeparator indicator expression
		expression = ab.getColSeparatorsIndExpr();
		if (evaluateIndicatorExpression(expression))
			styleClass.append("wf_cs ");
		// Protect indicator expression
		expression = ab.getProtectIndExpr();
		if (evaluateIndicatorExpression(expression))
			styleClass.append("wf_pr ");
		// Blink indicator expression
		expression = ab.getBlinkIndExpr();
		if (evaluateIndicatorExpression(expression))
			styleClass.append("wf_bl ");

		// Append color information to StringBuffer to avoid temporary objects
		//String styleName = styleClass.toString() + "wf_" + reverse + color + " wf_field";
		styleClass.append("wf_");
		
		// Reverse image is a special case that must be prefixed to the color name instead of separate like
		// all of the other classes.
		
		// If this field is in error (ERRMSGxxx indicator on), then make it reverse image
		boolean ri = false;
		int errSize = errmsgs.size();
		if (errSize > 0) {
			String fieldName = ab.getFieldName();
			if (fieldName != null) {
				for (int i = 0; i < errSize; i++) {
					String errName =
						((ERRMSGMessageDefinition) errmsgs.elementAt(i))
							.getFieldName();
					if (errName != null && errName.compareTo(fieldName) == 0) {
						ri = true;
						break;
					}
				}
			}
		}
		// Reverse indicator expression
		expression = ab.getReverseIndExpr();
		if (ri || evaluateIndicatorExpression(expression))
			styleClass.append("ri_");
						

		// Get colo(u)r name
		String color = "default";
		Vector v = ab.getColourIndExprVector();
		if (v != null) {
			int vcount = v.size();
			for (int i = 0; i < vcount; i++) {
				ColourIndExprPair cep = (ColourIndExprPair) v.elementAt(i);
				expression = cep.getIndExpr();
				if (evaluateIndicatorExpression(expression)) {
					color = cep.getColour();
					break;
				}
			}
		}
		styleClass.append(color);

		styleClass.append(" wf_field");
		//String styleName = styleClass.toString() + color + "Field";
		return styleClass.toString();
	}

	public String getCHKMSG(String arg1) {
		return null;
	}
	
	public String getDate(
		com.ibm.as400ad.webfacing.runtime.host.DateType arg1) {
		return null;
	}
	public String getDate(
		com.ibm.as400ad.webfacing.runtime.host.DateType arg1,
		com.ibm.as400ad.webfacing.runtime.host.CenturyType arg2,
		char arg3,
		char arg4) {
		return null;
	}

	/**
	 * Return a date value with the specified type, number of digits, and separator.
	 */
	public String getDate(
		DateType dateType,
		CenturyType centuryType,
		SeparatorType separatorType) {
		ZonedDecimal znd;
		// Use DATE for 4-digit dates, otherwise use UDATE
		if (centuryType == CenturyType.FOUR_DIGITS)
			znd = /*I2*/file.app.DATE;
		else
			znd = /*I2*/file.app.UDATE;
		// If a separator is specified, then edit... 
		if (separatorType == SeparatorType.HAS_SEPARATOR) {
			String edtwrd = RrecordPrint.getEdtWrd(znd, 'Y', ' ');
			String edtZnd = RrecordPrint.editNumeric(znd, edtwrd);
			return edtZnd;
		}
		// ...otherwise, just return toString()
		else
			return znd.toString();
	}

	/**
	 * Return a date value with the specified edit word.
	 */
	public String getDate(
		com.ibm.as400ad.webfacing.runtime.host.DateType arg1,
		com.ibm.as400ad.webfacing.runtime.host.CenturyType centuryType,
		String edtwrd) {
		ZonedDecimal znd;
		// Use DATE for 4-digit dates, otherwise use UDATE
		if (centuryType == CenturyType.FOUR_DIGITS)
			znd = /*I2*/file.app.DATE;
		else
			znd = /*I2*/file.app.UDATE;
		// Edit value and return  
		String edtZnd = RrecordPrint.editNumeric(znd, edtwrd);
		return edtZnd;
	}
		
	// Return a date value at the specified index
	// This is complicated, because we have to figure out the date format before we can do anything else
	public java.sql.Date getDate(int index) throws Exception
	{
		String dateString = getString(index);
		String datfmt400 = datfmt400(index);
		FixedDate d = new FixedDate(datfmt400, dateString);
		return new java.sql.Date(d.toDate().getTime());
	}
			
	// Return a time value at the specified index
	// This is complicated, because we have to figure out the date format before we can do anything else
	public java.sql.Time getTime(int index) throws Exception
	{
		String dateString = getString(index);
		String datfmt400 = datfmt400(index);
		FixedTime t = new FixedTime(datfmt400, dateString);
		return new java.sql.Time(t.toDate().getTime());
	}

	public String getDateWithTransform(
		com.ibm.as400ad.webfacing.runtime.host.DateType arg1,
		com.ibm.as400ad.webfacing.runtime.host.CenturyType centuryType,
		String edtwrd,
		int transform) {
		return getDate(arg1, centuryType, edtwrd);
	}
	
	public String getDateWithTransform(
		com.ibm.as400ad.webfacing.runtime.host.DateType arg1,
		com.ibm.as400ad.webfacing.runtime.host.CenturyType arg2,
		String arg3,
		int arg4,
		int arg5,
		int arg6) {
		return null;
	}


	public int getDisplayZIndex() {
		return 1;
	}

	/**
	 * Return a string representation of a numeric field value with edit code/word transformations applied.
	 * @return java.lang.String
	 * @param fvd com.ibm.as400ad.webfacing.runtime.view.FieldViewDefinition
	 */
	protected String getEditedFieldValue(String fieldName,FieldViewDefinition fvd) {
		FieldDataDefinition fdd;
		try
		{
			fdd = getFieldDefinition(fieldName);
			// If the field is input only, then always return blanks
			//if (fdd.isInputOnly())
			//	return "";
			String value = getFieldValue(fieldName);
			// Change this so that we leave leading zeros if no editing specified
			//   !!!for 'Y' data types only, not 'S'!!!
			//if (fvd.hasEditCodeEditWord() && value != "")
			int valueLength = value.length();
			if (valueLength > 0) {
				if (fdd.isNumeric()) {
					// If the field length is the same length as the value, then the value is the same and no 
					// need to create a zoned value.
					int fieldLength = fdd.getFieldLength();
					boolean hasEdit = fvd.hasEditCodeEditWord();
					// If the keyboard shift is 'S' and the field is input-capable then the value is always the numeric value...
					if (fdd.getKeyboardShift()=='S' && fdd.isInputCapable())
					{
						//...unless it is 0, then it is blank
						if (value.compareTo("0")==0)
							value="";
					}
					else if (fieldLength != valueLength
						|| hasEdit
						|| Application.check("0123456789", value) != 0) {
						ZonedDecimal znd =
							new ZonedDecimal(
								fieldLength,
								fdd.getDecimalPrecision(),
								new BigDecimal(value.trim())); // Since the check, above, failed, this isn't a 'simple' number and numeric.newBigDecimal() won't help 
						// If an edit code/word is specified, then perform edit...
						if (hasEdit) {
							String edtwrd = fvd.getEditWord();
							if (edtwrd == "") {
								char edtcde = fvd.getEditCode();
								edtwrd = RrecordPrint.getEdtWrd(znd, edtcde, ' ');
							}
							value = RrecordPrint.editNumeric(znd, edtwrd);
						}
						// ...otherwise, return number with no 0-trim
						else
							value = znd.toFixedString();
					}
				} else
				{
					// Shouldn't trim leading blanks!!!
					//value = value.trim();
					value = Application.trimr(value);
					// Replace embedded quotes '"' with HTML &quot;
					int j = value.indexOf('"');
					if (j>=0)
					{
						int i=0;
						StringBuffer valBuf = new StringBuffer();
						do
						{
							valBuf.append(value.substring(i, j));
							// Insert 
							valBuf.append("&quot;");
							i=j+1;
							j = value.indexOf('"', i);
						} while(j>0);
						valBuf.append(value.substring(i));
						value = valBuf.toString();
					}
				}
			}
			return value;
		}
		catch (Throwable e)
		{
			I2Logger.logger.severe("Unable to get field value for" + file.actualFileName + ' ' +  recordName + ' ' + fieldName);
			I2Logger.logger.severe(e.getMessage());
			if (e instanceof RuntimeException)
				throw (RuntimeException)e;
			return "";
		}
	}

	public Collection getErrorCollection() {
		return errmsgs;
	}

	public Iterator getErrors() {
		return errmsgs.iterator();
	}

	public String getFieldValue(String fieldName) {
		String nameI2 = getI2Name(fieldName);
		String value = getString(nameI2);
		return value;
	}

	public String getFieldValueWithTransform(String fieldName, int transform) {
		FieldViewDefinition fvd = viewDef.getFieldViewDefinition(fieldName);
		String value = getEditedFieldValue(fieldName, fvd);
		return value;
	}

	public String getFieldValueWithTransform(
		String fieldName,
		int transform,
		int arg3,
		int arg4) {
		return getFieldValueWithTransform(fieldName, transform);
	}

	/** Get the first line on the display that this format has fields on. */
	public int getFirstFieldLine() {
		if (firstFieldLine == 0)
			firstFieldLine = viewDef.getFirstFieldLine();
		return super.getFirstFieldLine();
	}

	/**
	 * Return the HTML to generate the correct setFocusXXX statements
	 */
	public String getFocusHTML() {
		Iterator it = viewDef.getDspatrPCFieldInfos();
		while (it.hasNext()) {
			DSPATR_PCFieldInfo fi = (DSPATR_PCFieldInfo) it.next();
			if (evaluateIndicatorExpression(fi.getIndExpr())) {
				String set = "setFocusForTagID(\"" + fi.getName() + "\");";
				return set;
			}
		}
		String set =
			"var topLayerRecords = new Array(); topLayerRecords[0] = \""
				+ recordName
				+ "\"; setFocus(topLayerRecords,\""
				+ recordName
				+ "\");";
		return set;
	}

	public boolean getIndicator(int index) {
		//return IN[index];
		boolean value = IN.getIndicator(index);
		return value;
	}

	/**
	 * Return the I2 name from a WebFacing name.
	 * Webfacing uses the special characters _x=@, _y=#, _z=$, __=_.  I2 uses a=@, n=#, $=$, _=_
	 */
	protected static String getI2Name(String wfName) {
		final String wfChar[] = { "__", "_x", "_y", "_z" };
		//final String rioChar[] = { "_", "a", "n", "$" };
		final String rioChar[] = { "_", "@", "#", "$" };
		String name = wfName;
		StringBuffer newName = new StringBuffer(name);
		for (int i = 0; i < rioChar.length; i++) {
			int j = name.indexOf(wfChar[i]);
			if (j >= 0) {
				int offset = 0;
				do {
					newName.replace(j - offset, j + 2 - offset, rioChar[i]);
					offset++;
					j += 2;
					j = name.indexOf(wfChar[i], j);
				} while (j >= 0);
				name = newName.toString();
			}
		}
		return name;
	}

	public int getSLNOVAROffset() {
		return 0;
	}

	public String getSystemName() {
		return null;
	}

	public String getSystemName(int arg1, int arg2) {
		return null;
	}

	/**
	 * Retrieve the current (formatted) time.
	 */
	public String getSystemTime() {
		if (timeFormatter == null) {
			timeFormatter = new SimpleDateFormat("HHmmss");
			timeZoned = new ZonedDecimal(6, 0);
		}
		String currentTime = timeFormatter.format(new Date());
		timeZoned.move(currentTime);
		String editedTime = RrecordPrint.editNumeric(timeZoned, "  :  :  ");
		return editedTime;
	}

	public String getSystemTime(char arg1, char arg2) {
		return null;
	}

	public String getSystemTime(String arg1) {
		return null;
	}

	public String getSystemTimeWithTransform(String arg1, int arg2) {
		return null;
	}

	public String getSystemTimeWithTransform(
		String arg1,
		int arg2,
		int arg3,
		int arg4) {
		return null;
	}

	public String getUserID() {
		return null;
	}

	public String getUserID(int arg1, int arg2) {
		return null;
	}

	public String getValuesAfterEditing(String param1) {
		return null;
	}

	/**
	 * Return the webface name from a I2 name
	 * Webfacing uses the special characters _x=@, _y=#, _z=$, __=_.  I2 uses a=@, n=#, $=$, _=_
	 * Creation date: (6/4/2002 2:07:37 PM)
	 */
	protected static String getWebfaceName(String rioName) {
		final String wfChar[] = { "__", "_x", "_y", "_z" };
		final String rioChar[] = { "_", "@", "#", "$" };
		String name = rioName;
		StringBuffer newName = new StringBuffer(name);
		for (int i = 0; i < rioChar.length; i++) {
			int j = name.indexOf(rioChar[i]);
			if (j >= 0) {
				int offset = 0;
				do {
					newName.replace(j + offset, j + 1 + offset, wfChar[i]);
					offset++;
					j++;
					j = name.indexOf(rioChar[i], j);
				} while (j >= 0);
				name = newName.toString();
			}
		}
		return name;
	}
	
	/** Return the JavascriptName of the associated fieldName.  This will be the field name || "$1" in subfile records. */
	protected String javascriptName(String fieldName)
	{
		return fieldName;
	}

	/**
	 * Return the 'depth' of the current window.  I2 only supports 0-order formats (for now).
	 * deprecated in 5.0
	public int getZOrder()
	{
		return 0;
	}
	 */

	public boolean isDSPFActive() {
		return true;
	}

	public boolean isDspfDbcsCapable() {
		return false;
	}

	public boolean isFieldConditionedOn(String arg1) {
		return false;
	}

	/** See if a conditioned label is visible. */
	public boolean isFieldVisible(String label) {
		// An array of values in the form "Label:indicator[:O]" where "O" indicates overlay(?)
		String[] visdefs = viewDef.getFieldVisDef();
		if (visdefs != null) {
			int visdefLength = visdefs.length;
			for (int i = 0; i < visdefLength; i++) {
				// If the first token is the field name, then check indicator string
				StringTokenizer st = new StringTokenizer(visdefs[i], ":\n");
				if (st.nextToken().compareTo(label) == 0) {
					String exp = st.nextToken();
					// Bizarre, but if a field is never visible (overlapped with no conditioning) this is "NEVER"
					if (exp.compareTo("NEVER") == 0)
						return false;
					return evaluateIndicatorExpression(exp);
				}
			}
		}
		// If the label name doesn't match one of the visdefs, then the field is always visible (it is unconditioned) 
		return true;
	}

	public boolean isMDTOn(String arg1) {
		return false;
	}

	public boolean isProtected() {
		return false;
	}

	/**
	 * Set hex values of INFDS, then set 'global' *INxx keys
	 * @version 11/4/2002
	 * @param cmdKey the string representation of the command key to set (e.g. 'CF03', 'PAGEDOWN')
	 */
	private void processCmdKey(String cmdKey) {
		// Process any special values need by INFDS (command key function values (369), lowest sfl RRN (370-371))
		RfileWorkstn file = (RfileWorkstn)this.file;
		if (file.infds != null && file.infds.len() >= 368) {
			// Add code to set the status value for PAGEUP/PAGEDOWN/and VLDCMDKEY
			// These aren't really true because it would require checking to make sure that an indicator hasn't been
			// turned on, but there seems to be no other reason why it would be checked.
			int hex, status=0;
			if (cmdKey.compareTo("ENTER") == 0)
				hex = 0xF1;
			else if (cmdKey.compareTo("HELP") == 0)
				hex = 0xF3;
			else if (cmdKey.compareTo("PAGEDOWN") == 0)
			{
				hex = 0xF5;
				status=1122;
			}
			else if (cmdKey.compareTo("PAGEUP") == 0)
			{
				hex = 0xF4;
				status=1123;
			}
			else {
				int i = 0;
				try {
					i = (Integer.parseInt(cmdKey.substring(2, 4)));
				} catch (Exception e) {
				}
				// F1-F12 are 0x31-0x3C
				if (i <= 12)
					hex = 0x30 + i;
				// F13-F24 are 0xB1-0xBC
				else
					hex = 0xB0 + i - 12;
				status=2;
			}
			file.infds.getOverlay()[368] = (byte) hex;
			file.infds.setFixedAt(10, new ZonedDecimal(5,0,status));
			file.infds.updateThis();
		}

		/*I2*/file.app.processCmdKey(cmdKey);
	}

	/**
	 * Set the specified key (e.g. "CF03", "PAGEDOWN") indicator to 'true'
	 * Creation date: (4/25/2002 11:10:16 AM)
	 */
	private void processCmdKeyIterator(String cmdKey, Iterator responses) {
		while (responses.hasNext()) {
			ResponseIndicator ri = (ResponseIndicator) responses.next();
			String aid = ri.getKey().getId();
			int i = ri.getIndex();
			// Also turn off function key if it is not pressed
			setIndicator(i, (cmdKey!=null && cmdKey.compareTo(aid) == 0));
		}
	}

	/** 
	 * Evaluate any indicators in the current format and process any 'special' actions required by them.
	 */
	public boolean processIndicators() {
		super.processIndicators();
		Iterator it = viewDef.getERRMSGs();
		// Process any error messages
		errmsgs.clear();
		while (it.hasNext()) {
			ERRMSGMessageDefinition md = (ERRMSGMessageDefinition) it.next();
			// Make sure that the error indicator is on and the field is visible
			if (evaluateIndicatorExpression(md.getIndicatorExpression())
				&& isFieldVisible(md.getFieldName())) {
				// We need the tag as well as the text for WebFacing 5.1.2
				/*
				String errmsg = md.getMessageText();
				errmsgs.add(errmsg);
				*/
				// Message ids aren't translated, so set appropriate text here
				if (md instanceof IXXXMSGIDMessageDefinition) {
					IXXXMSGIDMessageDefinition midef =
						(IXXXMSGIDMessageDefinition) md;
					XXXMSGIDDefinition mid = midef.getMSGID();
					midef.setMessageText(
						"Message not found "
							+ mid.getMsgFile()
							+ '.'
							+ mid.getMsgId());
				}
				errmsgs.add(md);
				// Turn off any response indicator
				int response = md.getResponseIndicator();
				if (response > 0)
					IN.setIndicator(response, false);
			}

		}

		clearInputBuffer();
		return true;
	}

	/**
	 * Process the special values AID, CURSOR, etc.
	 * Creation date: (6/5/2002 9:18:23 AM)
	 */
	public void processSpecialValues(Hashtable specialValues)
		throws Exception {
		// If this is a logoff request, then we are done
		String logoff = (String) specialValues.get("LOGOFF");
		if (logoff != null) {
			ELogoff e = new ELogoff("Application logged off");
			file.app.endjob();
			file.app.threadLock.terminate(e);
			//return;
			throw e;
		}
		Iterator it;
		// Process AID keys
		String cmdKey = (String) specialValues.get("AID");
		// Treat null (which seems to come back in certain circumstances, like mouse scroll) like ENTER
		if (cmdKey!=null)
		{
			// I don't know why they do this, but we have to get the function keys...
			processCmdKeyIterator(cmdKey, feedbackDef.getCommandKeyRespInds());
			// ...and then other things (like PAGEUP/PAGEDOWN) on separate calls
			processCmdKeyIterator(cmdKey, feedbackDef.getNonCommandAIDKeyRespInds());
			// Set on INKx indicators
			processCmdKey(cmdKey);
			// Process VLDCMDKEY keyword
			it = feedbackDef.iterator(AnyAIDKeyResponseIndicator.class);
			if (it.hasNext()) {
				ResponseIndicator ri = (ResponseIndicator) it.next();
				int i = ri.getIndex();
				// Also turn off function key if it is not pressed
				setIndicator(i, (cmdKey != null && cmdKey.compareTo("ENTER") != 0));
			}
		}

		// Process CURSOR keyword
		String cursor = (String) specialValues.get("CURSOR");
		if (cursor != null) {
			// Set the INFDS values for the cursor position
			RfileWorkstn file = (RfileWorkstn)this.file;
			if (file.infds != null && file.infds.len() >= 371) {
				int i = cursor.indexOf(':');
				if (i > 0) {
					int j = cursor.indexOf(':', i + 1);
					// The first value is the row
					int row;
					try {
						row = Integer.parseInt(cursor.substring(0, i));
						int column;
						if (j > 0)
							column =
								Integer.parseInt(cursor.substring(i + 1, j));
						else
							column = Integer.parseInt(cursor.substring(i + 1));
						file.infds.getOverlay()[369] = (byte) row;
						file.infds.getOverlay()[370] = (byte) column;
						file.infds.updateThis();
					} catch (Exception e) {
					}
				}
			}
			// Set any fields that depend upon cursor location
			if (feedbackDef.isRTNCSRLOCSpecified()) {
				CursorPosition cp = new CursorPosition(cursor);
				it = feedbackDef.getRTNCSRLOCDefinitions();
				while (it.hasNext()) {
					RTNCSRLOCDefinition rc = (RTNCSRLOCDefinition) it.next();
					// If this is a *RECNAME RTNCSRLOC, then we have to transform the row:column:offset into format:field:offset 
					// Hah!  It seems to work on WebFacing 5.1.2
					/*
					if (rc instanceof RTNCSRLOCDefinition_RECNAME)
					{
						// Loop through all of the fields in this format and try to find a cursor position match
						// Darn, this doesn't work (sigh)
						CursorPosition cp = new CursorPosition(cursor);
						// Parse out indivdual elements of string and insert into cursor
						StringTokenizer st = new StringTokenizer(cursor, ":");
						if (st.hasMoreTokens())
						{
							cp.setRow(Integer.parseInt(st.nextToken()));
							if (st.hasMoreTokens())
							{
								cp.setColumn(Integer.parseInt(st.nextToken()));
								if (st.hasMoreTokens())
								{
									String columnOffset = st.nextToken();
									//cp.setColumnOffset(Integer.parseInt(columnOffset)); -- we want a match based upon no column offset (the field definition has none)
					
									Iterator itv = viewDef.getFieldViewDefinitions();
									while (itv.hasNext())
									{
										FieldViewDefinition fvd =
											(FieldViewDefinition) itv.next();
										// We've found it.  Recreate cursor string and exit
										if (fvd.getPosition().isEqual(cp))
										{
											cursor =
												recordName
													+ ':'
													+ fvd.getFieldName()
													+ ':'
													+ columnOffset;
											break;
										}
									}
								}
							}
						}
					}
					//int i=cursor.indexOf(':')+1;
						*/
					int i = 0;
					loop : for (int k = 1; k < 4; k++) {
						/*
						int j = cursor.indexOf(':', i);
						String token = null;
						if (j > i)
							token = cursor.substring(i, j);
						else
							token = cursor.substring(i);
						*/
						String csrfield = null;
						String token = null;
						// Figure out what kind of RTNCSRLOC this is, RTNCSRLOC(*WINDOW|*MOUSE)...
						if (rc instanceof RTNCSRLOCDefinition_WINDOW) {
							RTNCSRLOCDefinition_WINDOW rcw =
								(RTNCSRLOCDefinition_WINDOW) rc;
							// The row and column values come in like " :row:column[:relativeRow:relativeColumn]"
							// Looks like this has changed in the 4.0 release -- no more leading ':' " row:column[:relativeRow:relativeColumn]"
							// !!! THIS IS BACKWARDS, NOTICE ROW(Y) COMES FIRST THEN COLUMN(X) !!!
							// Changed again!!! 5.1.2 tag:tagId:columnOffset or num:row:column:scopeQualifier
							// Note: "tag" and "num" (above) are literals
							switch (k) {
								// Set absolute row field
								case 1 :
									csrfield = rcw.getAbsoluteRowField();
									token = Integer.toString(cp.getRow());
									break;
									// Set absolute column field
								case 2 :
									csrfield = rcw.getAbsoluteColumnField();
									token = Integer.toString(cp.getColumn());
									break;
									// Set relative row field
								case 3 :
									csrfield = rcw.getWindowRowField();
									token = Integer.toString(cp.getRow());
									break;
									// Set relative column field
								case 4 :
									csrfield = rcw.getWindowColumnField();
									token = Integer.toString(cp.getColumn());
							}
						}
						// ... or RTNCSRLOC(*RECNAME)
						else if (rc instanceof RTNCSRLOCDefinition_RECNAME) {
							RTNCSRLOCDefinition_RECNAME rcn =
								(RTNCSRLOCDefinition_RECNAME) rc;
							// The row and column values come in like ":recordName:fieldName[:cursorPosition]"
							switch (k) {
								// Set record name field
								case 1 :
									csrfield = rcn.getRecordNameField();
									token = cp.getTagId();
									if (token == null)
										break loop;
									i = token.indexOf('$');
									token = token.substring(3, i);
									break;
									// Set field name field
								case 2 :
									csrfield = rcn.getFieldNameField();
									token = cp.getTagId();
									token = token.substring(i + 1);
									break;
									// Set cursor position field
								case 3 :
									csrfield = rcn.getCursorPosField();
									token =
										Integer.toString(cp.getColumnOffset());
									k = 4;
							}
						}
						if (csrfield != null)
							setTextNoChange(csrfield, token);
						/*
						if (j < 0)
							break;
						i = j + 1;
						*/
					}
				}
			}
		}

		// Process format-level CHANGE keyword
		it = feedbackDef.iterator(AnyFieldResponseIndicator.class);
		if (it.hasNext()) {
			ResponseIndicator ri = (ResponseIndicator) it.next();
			int i = ri.getIndex();
			setIndicator(i, isChanged);
		}

	}

	public void setIndicator(int index, boolean value) {
		//IN[index] = value;
		// If the WebFacing indicator object hasn't been created yet, then save temporary values
		IN.setIndicator(index, value);
	}


	// This doesn't really matter for WebFaced applicatios, but it's here for XSL/XML compatibility
	public void setPageSize(int size) {
	}
	/**
	 * Set the subfile size (the total number of records that the subfile can show).  
	 */
	public void setSflSize(int sflSize) {
	}

	/** Set the name of the field that controls the topmost record dislayed in a subfile. */
	// This really isn't used by WebFacing, but is here for compatibility with XML/XSL	
	protected void setSubfileRecordNumberFieldName(String fieldName) {
	}
	protected void setSubfileRecordNumberFieldName(String fieldName, String indicatorString) {
	}

	/** Return the date value in the type required by webfacing */ 
	private String datfmt400(int fieldIndex)
	{
		String fieldName = getWebfaceName((String)fldNames.elementAt(fieldIndex));
		FieldDataDefinition fdd = dataDef.getFieldDefinition(fieldName);
		String datfmt = fdd.getDatFmt();
		if (datfmt.compareTo("JOB")==0)
			datfmt=file.app.appJob.getJobDatfmt();
		else
			datfmt="*" + datfmt;
		String datSepString = fdd.getDatSep();
		char datSep;
		if (datSepString.compareTo("JOB")==0)
			datSep=file.app.appJob.getJobDatsep();
		else
			datSep = datSepString.charAt(0);
		return datfmt + datSep;
	}

	// For date/time data types, we have to set the text string based upon the date/time format of the field
	public void setText(int fieldIndex, FixedDate value) throws SQLException
	{
		String datfmt400 = datfmt400(fieldIndex);
		if (datfmt400.compareTo(value.m_datfmt400)!=0)
		{
			FixedDate d = new FixedDate(datfmt400);
			try {
				d.assign(value);
				value = d;
			} catch (Exception e) {}
		}
		fldValues.setElementAt(value.toString(), fieldIndex);
	}

	public void setText(String fieldName, Object value) throws Exception {
		String fn = getI2Name(fieldName);
		super.setText(fn, value);
	}

	
	protected FieldDataDefinition getFieldDefinition(String fieldName) throws Exception
	{
		FieldDataDefinition fdd = dataDef.getFieldDefinition(fieldName);
		if (fdd==null)
			throw new Exception("Unable to retrieve field definition " + fieldName + " from file " + this.file.actualFileName + " format " + this.recordName);
		return fdd;
	}

	void setTextChange(String fieldName, Object value) throws Exception {
		String fn = getI2Name(fieldName);
		// This is horribly annoying, but WebFacing allows lower-case input from its displays, so it is up to 
		// us to convert it to upper-case, if necessary
		String valueString = value.toString();
		String valueStringUpper = valueString.toUpperCase();
		if (valueString.compareTo(valueStringUpper) != 0) {
			FieldDataDefinition fdd = getFieldDefinition(fn);
			String checkAttributes = fdd.getCheckAttr();
			// If CHECK(LC) was not specified, then force to upper case
			if (checkAttributes.indexOf("LC;") < 0)
				value = valueStringUpper;
		}
		super.setTextChange(fn, value);
	}

	void setTextNoChange(String fieldName, Object value) throws Exception {
		String fn = getI2Name(fieldName);
		super.setTextNoChange(fn, value);
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.RecordThread#setChangeIndicator(java.lang.String, java.lang.String, java.lang.Object)
	 */
	protected void setChangeIndicator(
		String fieldName,
		String oldValue,
		Object value) {
		// We don't care what the old and new values are since WebFacing only sends back changed values
		//if (oldValue.compareTo(value.toString())!=0)
		ResponseIndicator ri =
			(ResponseIndicator) feedbackDef.get(
				fieldName,
				FieldResponseIndicator.class);
		if (ri != null) {
			int i = ri.getIndex();
			setIndicator(i, isChanged);
		}
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.RecordThread#clearFeedbackIndicators()
	 */
	void clearFeedbackIndicators() {
		//Iterator it = feedbackDef.iterator(ResponseIndicator.class);
		Iterator it = feedbackDef.iterator(FieldResponseIndicator.class);
		while (it.hasNext()) {
			ResponseIndicator ri = (ResponseIndicator) it.next();
			int i = ri.getIndex();
			setIndicator(i, false);
		}
	}
	public boolean isRecordOnTopLayer() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IBuildRecordViewBean#getDisplayRecord()
	 */
	public IDisplayRecord getDisplayRecord() {
		return this;
	}

	/** Get path information up to (but not including) ".jsp" */
	private String getJspPath() {
		return "RecordJSPs" + "/" + getFileName() + "/" + getRecordName();
	}

	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IBuildRecordViewBean#getJspName()
	 */
	public String getJspName() {
		return getJspPath() + ".jsp";
	}

	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IBuildRecordViewBean#getRecordName()
	 */
	public String getRecordName() {
		return getWebfaceName(recordName);
	}

	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IBuildRecordViewBean#getClientScriptJSPName()
	 */
	public String getClientScriptJSPName() {
		return getJspPath() + "JavaScript.jsp";
	}

	public boolean disableHyperlink(String arg1, String arg2) {
		return false;
	}

	public String getActiveKeyName(String arg1) {
		return null;
	}

	public int getZOrderPrefix(String parm1) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IBuildRecordViewBean#getLastFieldLine()
	 */
	public int getLastFieldLine() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IBuildRecordViewBean#getMaxColumn()
	 */
	public int getMaxColumn() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IBuildRecordViewBean#getMaxRow()
	 */
	public int getMaxRow() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IBuildRecordViewBean#getWdwHeight()
	 */
	public int getWdwHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IBuildRecordViewBean#getWdwWidth()
	 */
	public int getWdwWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IBuildRecordViewBean#isWindowed()
	 */
	public boolean isWindowed() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IBuildRecordViewBean#setDisplayZIndex(int)
	 */
	public void setDisplayZIndex(int arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IBuildRecordViewBean#getVersionDigits()
	 */
	public long getVersionDigits() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IVisibleRectangle#clone()
	 */
	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IVisibleRectangle#getStartingLineNumber()
	 */
	public int getStartingLineNumber() {
		// TODO Auto-generated method stub
		return -1;
	}

	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IVisibleRectangle#isLocatedBefore(com.ibm.as400ad.webfacing.runtime.view.VisibleRectangle)
	 */
	public boolean isLocatedBefore(VisibleRectangle visiblerectangle) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IVisibleRectangle#isOverlappedBy(com.ibm.as400ad.webfacing.runtime.view.VisibleRectangle)
	 */
	public boolean isOverlappedBy(VisibleRectangle visiblerectangle) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IVisibleRectangle#removeFromCoveredRecords(com.ibm.as400ad.webfacing.runtime.view.RecordViewBean)
	 */
	public void removeFromCoveredRecords(RecordViewBean recordviewbean) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.asc.rio.RecordThread#writeINDDS(com.asc.rio.fixed)
	 */
	void writeINDDS(FixedChar indds)
	{
		if (indds!=null)
		{
			// Bizarre but apparently true -- if you try to set an indicator that isn't referenced you get a null
			// pointer error when evaluateIndicatorExpression() is run, so we can only set referenced values.
			List l = IN.getReferencedOptionIndicators();
			if (l!=null)
			{
				Iterator it = l.iterator();
				while (it.hasNext())
				{
					int i = ((Integer)it.next()).intValue();
					setIndicator(i, indds.booleanAt(i-1));
				}
			}
		}
	}

}
