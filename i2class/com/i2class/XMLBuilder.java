package com.i2class;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.ibm.as400.access.*;
import com.ibm.as400ad.webfacing.common.WFAppProperties;
import com.ibm.as400ad.webfacing.runtime.host.*;
import com.ibm.as400ad.webfacing.runtime.host.WFConnection;
import com.ibm.as400ad.webfacing.runtime.model.def.*;
import com.ibm.as400ad.webfacing.runtime.view.*;

/**
 * Build an XML string from a WebFacing request.
 * @author ANDREWC
 */
public class XMLBuilder {
	// The parameters (fields) to get passed back to the WebFacing host
	private HashMap parms = new HashMap();
	
	// The available function keys
	static private int PAGEDOWN=25;
	static private int PAGEUP=26;
	static private int HELP=27;
	private char keyTypes[] = new char[HELP]; // 25=PagDown, 26=PageUp, 'A'="CA", 'F'="CF", ' '=unavailable

	// All the parameters that need to get passed back to the WebFacing host 
	private String key, command, cursor="1:1";
	private Integer pageIdObj;
	//private boolean retrievedXML;
	
	static ResourceBundle bravoProperties;

	
	/** 
	 * Return the XML representation of the data from a WebFacing request.
	 * The format of the returned data is:
	 * <xmp>
	 * &lt;page>
	 *  &lt;formatName1>
	 *   &lt;fieldName1>
	 *   &lt;/fieldName1>
	 *   &lt;fieldName2>
	 *   &lt;/fieldName2>
	 *   ...
	 *  &lt;/formatName1>
	 *  &lt;formatName2>
	 *   ...
	 *  &lt;/formatName2>
	 *  ...
	 *  &lt;fkey Name="F1"/>&lt;fkey name="F2"/>...
	 * &lt;/page>
	 * <xmp>
	 */
	public String getXML(HttpServletRequest request)
	{
		return getXMLBuffer(request).toString();
	}
	
	// Return the XMLBuffer full of data
	private StringBuffer getXMLBuffer(HttpServletRequest request)
	{
		//StringBuffer xmlBuf = new StringBuffer("<dspf>\n");
		StringBuffer xmlBuf = new StringBuffer("");
		// Get the screen builder object that WebFacing has inserted into the request stream
		HttpSession session = request.getSession();
		IScreenBuilder screenBuilder = (IScreenBuilder)session.getAttribute("screenbuilder");
		// Loop through all of the layers on the screen
		//Iterator screenLayers = (Iterator) screenbuilder.getRecordLayersOnDevice();
		Iterator screenLayers = screenBuilder.getRecordLayersOnDevice().iterator();
		while(screenLayers.hasNext()) 
		{
			// Get each defined rectangle on the display.  These are typically record formats
			IDeviceLayer layer = (IDeviceLayer)screenLayers.next();
			Iterator rectangles = layer.getRectanglesIterator();
			IVisibleRectangle rect = null;
			while(rectangles.hasNext()) 
			{
				rect = (IVisibleRectangle)rectangles.next();
				// If this is a record, then...
				if(rect instanceof RecordViewBean)
				{
					// Start building the XML string in the form <RECORD_NAME><FIELD1>...</FIELD1>...
					RecordViewBean rvb = (RecordViewBean)rect;
					RecordFeedbackBean rfb = rvb.getFeedbackBean();
					IDisplayRecord displayRcd = rvb.getDisplayRecord();
					appendRecordXML(rvb, rfb, displayRcd, -1, xmlBuf);
					// Build output record for subfile
					if (rect instanceof SubfileControlRecordViewBean)
					{
						SubfileControlRecordViewBean cvb = (SubfileControlRecordViewBean)rect;
						SubfileControlRecordFeedbackBean cfb = (SubfileControlRecordFeedbackBean)rfb;
						IDisplaySFLCTLRecord displayRcdSFLCTL = cvb.getDisplaySFLCTLRecord();
						// Somewhat surprisingly, you loop beginning at 1 always? regardless of actual rrn
						//int firstrrn = cvb.getRRN();
						int firstrrn = 1;
						int lastrrn = firstrrn + cvb.getPageSizeConsiderFold() - 1;
						for (int row=firstrrn; row<=lastrrn; row++)
						{
							// If the record is not active then don't evaluate
							if (cvb.isActiveRecord(row))
							{
								SubfileRecordFeedbackBean sfb = cfb.getSubfileRecordFeedbackBean(row);
								appendRecordXML(cvb, sfb, displayRcdSFLCTL, row, xmlBuf);
							}
						}
					}
				}
			}
			
		}
		// Get any function keys for the display
		/*
		ICmdKeysInterface wfCmdKeys = (ICmdKeysInterface)request.getAttribute("WFCmdKeys");
		Iterator it = wfCmdKeys.getApplicationKeyList();
		buildKeys(request, it, xmlBuf);
		it = wfCmdKeys.getPageOrFieldKeyList();
		buildKeys(request, it, xmlBuf);
		*/
		Iterator keys = screenBuilder.getActiveKeysWithoutEnter();
		buildKeys(keys, xmlBuf);
		//TODO Append error messages to XML
		//Iterator[] msgs = screenBuilder.getMessagesAndIDs(); // msgs[0]=message text, msgs[1]=tag (l1_recordname$fieldname)
		xmlBuf.append("<PAGEID value='" + pageIdObj + "'/>");
		//retrievedXML=true;
		
		// Close the XML root node
		//xmlBuf.append("</dsp>");
		return xmlBuf;
	}
	
	/**
	 * Return the XML string associated with this request pre-formatted so that it can be used in an XSL request.
	 * @param request
	 * @param XSLStyleSheet
	 * @return
	 */
	public String getStylesheetXML(HttpServletRequest request, String XSLStyleSheet)
	{
		StringBuffer xmlBuf = getXMLBuffer(request);
		xmlBuf.insert(0, "<?xml version=\"1.0\" ?>\n<?xml:stylesheet type=\"text/xsl\" href=\"" + 
		 XSLStyleSheet + "\" ?>\n<page>");
		xmlBuf.append("</page>");
		return xmlBuf.toString();
	}
	
	static private void appendRecordXML(IBuildRecordViewBean rvb, RecordFeedbackBean rfb, IDisplayRecord displayRcd, int rrn, StringBuffer xmlBuf)
	{
		String recordName = rfb.getRecordDataDefinition().getName();
		xmlBuf.append("\n<" + recordName + ">\n");
		// Get the field definitions for this record
		//RecordFeedbackBean rfb = rvb.getFeedbackBean();
		IRecordDataDefinition rdd = rfb.getRecordDataDefinition();
		Collection fldCol = rdd.getFieldDefinitions();
	
		// Loop through each field and add a node for each field's worth of data 
		Iterator it = fldCol.iterator();
		while (it.hasNext())
		{
			FieldDataDefinition fieldDef = (FieldDataDefinition) it.next();
			String fieldName = fieldDef.getFieldName();
			String fieldValue;
			if (displayRcd instanceof IDisplaySFLCTLRecord)
				fieldValue = ((IDisplaySFLCTLRecord)displayRcd).getFieldValue(fieldName, rrn);
			else
				fieldValue = displayRcd.getFieldValue(fieldName);
			xmlBuf.append("<" + fieldName + ">" + fieldValue + "</" + fieldName + ">\n");
		}

		// Loop through any response indicators and add them to output
		it = rdd.getIndicatorDefinition().getReferencedResponseIndicators();
		while (it.hasNext())
		{
			Integer ind = (Integer) it.next();
			String indString = ind.toString();
			if (ind.intValue()<10)
				indString = "0" + indString;
			boolean indBoolean;
			if (displayRcd instanceof IDisplaySFLCTLRecord)
				indBoolean = ((IDisplaySFLCTLRecord)displayRcd).evaluateIndicatorExpression(rrn, indString);
			else
				indBoolean = displayRcd.evaluateIndicatorExpression(indString);
			char indValue;
			if (indBoolean)
				indValue='1';
			else
				indValue='0';
			xmlBuf.append("<IN" + indString + ">" + indValue + "</IN" + indString + ">\n");
		}
		// Close out record node
		xmlBuf.append("</" + recordName + ">\n");
	}
	
	// Build any function keys associated with this request
	private void buildKeys(Iterator it, StringBuffer xmlBuf)
	{
		java.util.Arrays.fill(keyTypes, ' '); //Blank out CA/CF flags for keys
		// Always add ENTER?
		xmlBuf.append("<fkey name='ENTER' value='Enter'/>");
		// Loop through fkey iterator, append <fkey name='FKEY_NAME' value='Descriptive text'/>
		while (it.hasNext()) { 
			IClientAIDKey fkey = (com.ibm.as400ad.webfacing.runtime.view.IClientAIDKey) it.next();
			if (fkey.isKeyShownOnClient())
			{	
				String keyName = fkey.getKeyName();
				// Flag available keys
				if (keyName.charAt(0)=='C')
				{
					int keyNum = Integer.parseInt(keyName.substring(2));
					keyTypes[keyNum-1]=keyName.charAt(1); // Either 'A'="CA" or 'F'="CF"
				}
				else if (keyName.compareTo("PAGEDOWN")==0)
					keyTypes[PAGEDOWN-1]='F';
				else if (keyName.compareTo("PAGEUP")==0)
					keyTypes[PAGEUP-1]='F';
				else if (keyName.compareTo("HELP")==0)
					keyTypes[HELP-1]='A';
				String label = fkey.getKeyLabel();
				xmlBuf.append("<fkey name='" + keyName + "' value='" + label + "'/>\n");
			}
		}
		
	}
	
	/** Set a field value that will be returned to the WebFacing host. */
	public void setFieldValue(String formatName, String fieldName, String value)
	{
		setParameter(formatName + "$" + fieldName, value);
	}
	/** 
	 * Set a field value in the subfile of a subfile control format that will be returned to the WebFacing host.
	 * @param formatNameSFLCTL the name of the subfile control format that contains the subfile field, NOT! the 
	 * subfile format name itself. 
	 * @param fieldName the name of the field to set.
	 * @param the row on the current page of the subfile, NOT! the relative record number.
	 * @param value the value to set the field to
	 */
	public void setSubfileFieldValue(String formatNameSFLCTL, String fieldName, int row, String value)
	{
		fieldName = fieldName + "$" + row;
		setFieldValue(formatNameSFLCTL, fieldName, value);
	}
	
	/** Set a parameter value that will be returned to the WebFacing host. */
	public void setParameter(String parmName, String parmValue)
	{
		parms.put(parmName, parmValue);
	}
	
	
	/** 
	 * Press a key.
	 * @return false if the key is not available. 
	 */
	private boolean pressKey(String command, int fkey)
	{
		this.command = command;
		key = "AID";
		return (keyTypes[fkey-1]!=' ');
	}
	/** Press a CA/CF key. */
	public boolean pressFKey(int fkey)
	{
		char keyType = keyTypes[fkey-1];
		if (keyType==' ')
			keyType='F';
		String command = "C" + keyType;
		// Pad so that it's CA03, not CA3
		if (fkey<10)
			command += "0";
		command += fkey;
		return pressKey(command, fkey);
	}
	
	/** 
	 * Page down to the next available batch of records.
	 * @return false if the page down was not successful (end of list), true otherwise. 
	 */
	public boolean pageDown()
	{
		return pressKey("PAGEDOWN", PAGEDOWN);
	}
	/** 
	 * Page up to the previous batch of records.
	 * @return false if the page up was not successful (beginning of list), true otherwise. 
	 */
	public boolean pageUp()
	{
		return pressKey("PAGEUP", PAGEUP);
	}
	
	/** Set the cursor position to a specific row/column pair. */
	public void setCursor(int row, int column)
	{
		cursor = row + ":" + column;
	}
	/** Set the cursor position to a specific format/field name pair. */
	public void setCursor(String formatName, String fieldName)
	{
		cursor = formatName + ":" + fieldName;
	}
	
	/** 
	 * Reposition the current subfile
	 * @return false if the positioning request failed 
	 */
	public boolean positionSubfile(String formatName, int rrn)
	{
		setParameter("SFLRRN", formatName + ":" + rrn);
		return true;
	}
	
	/** 
	 * Build the HTML that will send the parameter values back to the WebFacing host.
	 * @see #setParameter 
	 */
	public void submit(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException
	{
		// For whatever reason, if we don't retrieve the XML data from the request the WebFacing server won't return
		// the correct results, so make sure that it is read even if the user doesn't want it.
		//if (!retrievedXML)
		//	getXML(request);
		// If the key is null, then assume that this is 'ENTER'
		if (key==null)
		{
			key="AID";
			command="ENTER";
		}
		/*
		resp.setContentType("text/html; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		
		out.println("<html>");
		//out.println("<head>");
		//out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>");
		//out.println("</head>");
		out.println("<body>");
		out.println("<form name='APPDATA' target='app' method='post' action='WebFacing'>");
		out.println("<input name='" + key + "' size='30' value='" + command + "'/> ");
		out.println("<input name='CURSOR' size='30' value='" + cursor + "'/> ");
		out.println("<input name='PAGEID' size='30' value='" + pageIdObj + "'/> ");
		// Loop through each parameter and build an <INPUT> token for it 
		Iterator parmIterator = parms.entrySet().iterator();
		while (parmIterator.hasNext())
		{
			Map.Entry entry = (Map.Entry)parmIterator.next();
			String parmName = (String)entry.getKey();
			String parmValue = (String)entry.getValue();
			out.println("<input name='" + parmName + "' value='" + parmValue + "'/> ");		
		}
		out.println("</form>");
		// Write out the JavaScript to actually submit the form
		out.println("<script language='JavaScript'>");
		out.println("document.APPDATA.submit();");
		out.println("</script");
		
		out.println("</body>");
		out.println("</html>");
		*/
		// Build forward request string
		// Always pass PAGEID...
		StringBuffer forward = new StringBuffer("WebFacing?PAGEID=");
		forward.append(pageIdObj);
		//...CURSOR
		forward.append("&CURSOR=");
		forward.append(cursor);
		//...and pressed key
		forward.append('&');
		forward.append(key);
		forward.append('=');
		forward.append(command);
		Iterator parmIterator = parms.entrySet().iterator();
		while (parmIterator.hasNext())
		{
			Map.Entry entry = (Map.Entry)parmIterator.next();
			String parmName = (String)entry.getKey();
			String parmValue = (String)entry.getValue();
			forward.append('&');
			forward.append(parmName);
			forward.append('=');
			forward.append(parmValue);
		}
		// Clear requests for next time
		parms.clear();
		key = null;
		// Forward request
		;
		//request.getRequestDispatcher(forward.toString()).forward(request, resp);
		resp.sendRedirect(forward.toString());
	}
	
	static public XMLBuilder instance(HttpServletRequest request)throws ServletException
	{
		HttpSession session = request.getSession();
		XMLBuilder xmlb = (XMLBuilder)session.getAttribute("XMLBuilder");
		if (xmlb == null)
		{
			/*
			String s=null;
			String[] values = null;
			Enumeration enum = request.getParameterNames();
			while (enum.hasMoreElements())
			{
				s = (String)enum.nextElement();
				values = request.getParameterValues(s);
			}
			
			enum = request.getAttributeNames();
			while (enum.hasMoreElements())
				s = (String)enum.nextElement();
			enum = session.getAttributeNames();
			while (enum.hasMoreElements())
				s = (String)enum.nextElement();
			enum=session.getServletContext().getAttributeNames();
			while (enum.hasMoreElements())
				s = (String)enum.nextElement();
			s = s + "ABC";
			String[] names = session.getValueNames();
			*/
			
			// Create connection to AS400 host so that we can authorize
			//WFAppProperties wfprop = (WFAppProperties) session.getServletContext().getAttribute("WFAppProperties");
			WFAppProperties wfprop = WFAppProperties.getWFAppProperties(session.getServletContext());
			String hostName = wfprop.getHostName();
			//String usrid = wfprop.getUserID();
			//String password = wfprop.getPassword();
			//AS400 as400 = new AS400(hostName, usrid, password);
			//AS400 as400 = new AS400(hostName, "RIOBRAVO", "RIOBRAVO");
			String usrid="RIOBRAVO";
			String password="ASCBRAVO1";
			try
			{
				getResourceBundle();
				usrid = bravoProperties.getString("BravoHostUID");
				password = bravoProperties.getString("BravoHostPWD");
			}
			catch (Exception e) {}
			
			AS400 as400 = new AS400(hostName, usrid, password);
			try
			{
				as400.setGuiAvailable(false);
				CommandCall cc = new CommandCall(as400, "QUSRSYS/RIOBRAVO");
				if (!cc.run())
					throw new ServletException("Authorization error");
			}
			catch (Exception e)
			{
				throw new ServletException(e);
			}
			
			/*
			
			WFConnection wfconn = (WFConnection) session.getAttribute("WFConnection");
			try
			{
				wfconn.run("QUSRSYS/RIOBRAVO");
			}
			catch (Exception e)
			{
				throw new ServletException(e);
			}
			*/
			// Call RIOBRAVO command to get authorization
			// This will throw an exception if there is an error
			xmlb = new XMLBuilder();
			session.setAttribute("XMLBuilder", xmlb);
		}
		// Set flag so that we know to set the pageId object
		//xmlb.retrievedXML=false;
		// The PAGEID attribute has to be set or bad things happen when control returns to the WebFacing server
		Integer pageIdObj = (Integer)session.getAttribute("pageId");
		if(pageIdObj == null)
			pageIdObj = numeric.ONE; //new Integer(1);
		else
			pageIdObj = new Integer(pageIdObj.intValue() + 1); // This will always be >1 so no need to do numeric.newInteger()
		xmlb.pageIdObj = pageIdObj;
		session.setAttribute("pageId", pageIdObj);
		return xmlb;
	}
	
	/** Return the page id (number of pages that have displayed) associated with this XMLBuilder object. 
	 */
	public int pageId()
	{
		return pageIdObj.intValue();
	}
	
	/**
	 * Get the I2Bravo.properties file.
	 * @return java.util.ResourceBundle
	 */
	static ResourceBundle getResourceBundle()
	{
		if (bravoProperties == null)
			bravoProperties = ResourceBundle.getBundle("I2Bravo");
		return bravoProperties;
	}

}
