package com.i2class;

import java.io.PrintWriter;
import java.net.URL;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.*;

import com.ibm.as400ad.webfacing.runtime.httpcontroller.IHttpSessionVariable;
import com.ibm.as400ad.webfacing.runtime.model.def.*;
import com.ibm.as400ad.webfacing.runtime.view.*;
import com.ibm.as400ad.webfacing.runtime.view.def.*;
/**
 * Lock a thread until some action is taken by a user on a WebFaced display.
 * @author Andrew Clark 
 * @version 4/22/2002 10:45:43 AM
 */
public class ThreadLockWebface
	extends ThreadLockServlet
	implements IScreenBuilder, IDeviceLayer
{

	private String styleName, rioWebDirectory;
	String ctxPath, urlPath, urlDDS;
	ArrayList layerList;
	private static final String[] pageUPDNarray = {"", ""}; 
	private static final RecordViewBean[] rollEnabledArray = {null, null};
	Vector messages = new Vector();
	Vector ids = new Vector();
	private static CursorPosition DEFAULT_CURSOR_POSITION = new CursorPosition(-1, -1);
	/* These were all used in a failed attempt to use the WebFacing Servlet to build the JSPs
	private static I2HostRequest hostRequest = new I2HostRequest(); 
	private static Integer FIELD_EXIT_KEY_CODE = new Integer(17);
	private static Boolean INSERT_MODE = new Boolean(false);
	*/
	Iterator[] messagesAndIDs = new Iterator[2];

	/*public ThreadLockWebface(Application app)*/
	public ThreadLockWebface()
	{
		//super();
		// Get style name
		ResourceBundle prop = Application.getResourceBundle();
		try
		{
			//WFAppProperties wf = WFAppProperties.getWFAppProperties();
			//styleName = wf.getStyleName();
			// For whatever reason, we can't seem to get the styleName from some WAR files -- 
			// IBM has agreed that this is a limitation and is probably related to getRealPath().
			// If it can't be retrieved, then fall back to the I2.properties file.
			if (styleName == null)
				styleName = prop.getString("I2StyleName");
		}
		// If that fails as well, then just default to "avenue" (the first, default choice)
		catch (Exception e)
		{
			styleName = "avenue";
		}
		
		// Get the default I2web directory (this may be moved for secure applications
		try
		{
				rioWebDirectory = prop.getString("I2WebDirectory");
		}
		catch (Exception e)
		{
			rioWebDirectory = "/I2web";
		}

		// Create an array list that gets used by getRecordLayersOnDevice()
		layerList = new ArrayList(1);
		layerList.add(this);
		// Set host request object
		//WFSession.setJobInfoRequestor(new HostJobInfo(new I2HostRequest())); 
	}

	/** Check for a positioning request. */  
	protected int checkPositioning(IRecordSFLCTL sflctl, javax.servlet.http.HttpServletRequest request)
	{
		int sflpos=super.checkPositioning(sflctl, request);
		if (sflpos<0)
			sflpos = ((RecordWebfaceSFLCTL)sflctl).checkPositioning(request);
		return sflpos;	
	}

	/** Return the list of active command keys on this display (a union of all of the record formats on the display. */
	private java.util.Vector getActiveKeysVector(Vector keylist)
	{
		//try
		//{
		//acquiring();
		// Get the function keys from the last format written
		//int fmtCount = writtenFormats.size();
		//RecordWebface lastRcd = (RecordWebface)writtenFormats.elementAt(fmtCount-1);

		// Loop through all of the available function keys.  Return only the ones that aren't conditioned or whose
		// condition expression is true.
		if (keylist==null)
			keylist = new Vector();
		// Always add ENTER to the list of available function keys
		//keylist.add(new AIDKey("ENTER", "Enter"));

		//boolean conditioned=false;
		//boolean labeled=false;
		// Get command key labels
		CommandKeyLabelList cklist = new CommandKeyLabelList();
		int writeCount = writtenFormats.size();
		for (int i = 0; i < writeCount; i++)
		{
			RecordWebface lrcd = (RecordWebface) writtenFormats.elementAt(i);
			if (/*rcd != lrcd &&*/
				lrcd.viewDef != null)
				//cklist.mergeList(lrcd.viewDef.getPotentialKeyLabels());
				cklist.mergeList(lrcd.viewDef.getCommandKeyLabels());
		}

		if (lastRcd != null)
		{
			RecordWebface rcd = (RecordWebface) lastRcd;
			getActiveKeysIterator(rcd.viewDef.getCommandKeys(), keylist, cklist);
			getActiveKeysIterator(rcd.viewDef.getFunctionKeys(), keylist, cklist);
			/*
			Iterator iterator = ;
			while (iterator.hasNext())
			{
				AIDKey aid = (AIDKey)iterator.next();
				String expression = aid.getIndicatorExpression();
				if (expression == null || rcd.evaluateIndicatorExpression(expression))
				{
					String label = aid.getKeyLabel();
					String keyName = aid.getKeyName();
					// If this function key is not labeled, then 
					// loop through all of the other written formats to see if a command-key label is available
					//if (label.compareTo(keyName)==0)
					{
						/*
						if (!labeled)
						{
							labeled = true;
								int writeCount=writtenFormats.size();
							for (int i=0; i<writeCount; i++)
							{
								RecordWebface lrcd = (RecordWebface)writtenFormats.elementAt(i);
								if (/*rcd != lrcd &&* / lrcd.viewDef!=null)
									cklist.mergeList(lrcd.viewDef.getPotentialKeyLabels());
							}
						}
						CommandKeyLabel ckl = cklist.getLabel(keyName.substring(2));
						// If the command key label is still blank, then loop through the AIDKey definitions for each written format
						if (ckl == null)
						{
							for (int i=0; i<writeCount; i++)
							{
								RecordWebface lrcd = (RecordWebface)writtenFormats.elementAt(i);
								if (rcd != lrcd && lrcd.viewDef!=null)
								{
									Iterator lIterator = lrcd.viewDef.getCommandKeys();
									while (lIterator.hasNext())
									{
										AIDKey lAID = (AIDKey)lIterator.next();
										// Make sure that the key that we're comparing to is the same key and is actually labeled
										if (keyName.compareTo(lAID.getKeyName())==0 && keyName.compareTo(lAID.getKeyLabel())!=0)
										{
											// Make sure that it is conditioned...
											expression = lAID.getIndicatorExpression();
											if (expression == null || lrcd.evaluateIndicatorExpression(expression))
											{
												ckl = lAID.getLabel();
												break;
											}
										}
									}
								}
							}
						}
						if (ckl != null)
							aid.setLabel(ckl);
					}
					keylist.add(aid);
				}
				//else
				//	conditioned = true;
			}
			// If none of the function keys are conditioned, then just return the viewdef iterator
			//if (!conditioned)
			//	return iterator;
						*/
		}

		return keylist;
		//}
		//catch (Exception e) {return null;}
	}
	/** Return the list of active command keys on this display (a union of all of the record formats on the display. */
	public java.util.Iterator getActiveKeys()
	{
		Vector keylist = new Vector();
		// Always add ENTER to the list of available function keys
		keylist.add(new AIDKey("ENTER", "Enter"));
		keylist = getActiveKeysVector(keylist);
		return keylist.iterator();
	}

	private void getActiveKeysIterator(
		Iterator iterator,
		Vector keylist,
		CommandKeyLabelList cklist)
	{
		int writeCount = writtenFormats.size();
		RecordWebface rcd = (RecordWebface) lastRcd;
		while (iterator.hasNext())
		{
			AIDKey aid = (AIDKey) iterator.next();
			String expression = aid.getIndicatorExpression();
			if (expression == null || rcd.evaluateIndicatorExpression(expression))
			{
				String label = aid.getKeyLabel();
				String keyName = aid.getKeyName();
				// If this function key is not labeled, then 
				// loop through all of the other written formats to see if a command-key label is available
				// Only check command key labels if this is a command key (i.e. CFxx or CAxx)
				CommandKeyLabel ckl = null;
				if (keyName.charAt(0) == 'C')
					ckl = cklist.getLabel(keyName.substring(2));
				// If the command key label is still blank, then loop through the AIDKey definitions for each written format
				if (ckl == null)
				{
					for (int i = 0; i < writeCount; i++)
					{
						RecordWebface lrcd =
							(RecordWebface) writtenFormats.elementAt(i);
						if (rcd != lrcd && lrcd.viewDef != null)
						{
							Iterator lIterator = lrcd.viewDef.getCommandKeys();
							while (lIterator.hasNext())
							{
								AIDKey lAID = (AIDKey) lIterator.next();
								// Make sure that the key that we're comparing to is the same key and is actually labeled
								if (keyName.compareTo(lAID.getKeyName()) == 0
									&& keyName.compareTo(lAID.getKeyLabel()) != 0)
								{
									// Make sure that it is conditioned...
									expression = lAID.getIndicatorExpression();
									if (expression == null
										|| lrcd.evaluateIndicatorExpression(expression))
									{
										ckl = lAID.getLabel();
										break;
									}
								}
							}
						}
					}
				}
				if (ckl != null)
					aid.setLabel(ckl);
				keylist.add(aid);
			}
		}
	}

	public com.ibm.as400ad.webfacing.runtime.view.CursorPosition getCursor()
	{
		return null;
	}

	public String getDspatrPCId()
	{
		return calculateLocationForCursor().getTagID();
	}
	
	/**
	 * Calculate the field that has focus.
	 */
	private LocationOnDevice calculateLocationForCursorFocus(RecordWebface rcd, RecordWebface dataRcd, Iterator it, LocationOnDevice idOutput)
	{
		RecordDataDefinition dataDef = dataRcd.dataDef;
		LocationOnDevice lod = null;
		CursorPosition cp = null;
		while (it.hasNext())
		{
			FieldViewDefinition fvd = (FieldViewDefinition) it.next();
			String fieldName = fvd.getFieldName();
			// ...but the dataDef contains its I/O status, so we have to get both.
			FieldDataDefinition fdd = 	dataDef.getFieldDefinition(fieldName);
				
			// Save the cursor position of the topmost visible input-capable field
			boolean topmostInputCapable = false;
			CursorPosition fvdcp=null;
			if (fdd!= null && fdd.isInputCapable() && rcd.isFieldVisible(fieldName))
			{
				// Make sure that DSPATR(PR) is not specified
				KeywordDefinition kd = fvd.getKeywordDefinition(RecordViewDefinition.PAR_DSPATR_PR);
				if (kd==null || !rcd.evaluateIndicatorExpression(kd.getIndicatorExpression()))
				{
					fvdcp = fvd.getPosition();
					topmostInputCapable = (cp==null || fvdcp.isBefore(cp));
				}
			}
			if (topmostInputCapable || idOutput == null)
			{
				//String s = "l1_" + rcd.recordName + "$" + fieldName;
				//if (dataRcd instanceof RecordWebfaceSFL)
					//fieldName = fieldName + "$1";
				fieldName = dataRcd.javascriptName(fieldName);
				LocationOnDevice fvdl = new LocationOnDevice(DEFAULT_CURSOR_POSITION, 1, rcd.getRecordName(), fieldName);
				// If the field is input capable, then we have a match
				if (topmostInputCapable)
				{
					//return lod;
					cp = fvdcp;
					lod = fvdl;
				}
				// Otherwise, store away output fields for possible use later
				else //if (idOutput == null)
					idOutput = fvdl;
			}
		}
		return lod;
	}
	
	// Return the location id associated with the specified record and iterator
	private static LocationOnDevice getDspatrPCid(RecordWebface rcd, RecordWebface dataRcd, Iterator it)
	{
		LocationOnDevice id=null;
		while (it.hasNext())
		{
			DSPATR_PCFieldInfo fi = (DSPATR_PCFieldInfo) it.next();
			if (rcd.evaluateIndicatorExpression(fi.getIndExpr()) && rcd.isFieldVisible(fi.getFieldName()))
			{
				id = new LocationOnDevice(DEFAULT_CURSOR_POSITION, 1, rcd.getRecordName(), dataRcd.javascriptName(fi.getName()));
				//id = "l1_" + rcd.recordName + "$" + fi.getName();
				// A DSPATR match was found.  Always use it.
				break;
			}
		}
		return id;
	}
	
	/**
	 * Get the SetCursor/FocusTagID of the field where the cursor should be positioned.
	 */
	public LocationOnDevice calculateLocationForCursor()
	{
		LocationOnDevice id = null;
		LocationOnDevice idOutput = null;
		// Loop through all of the fields on the display to find a suitable target for SetCursor/FocusTagID
		// The rule is: position to first error field: If none, use the first DSPATR(PC) if any.  If none, use the first input-capable field.  If none, use any field.
		Iterator formats = getWrittenFormats();
		nextFormat : while (formats.hasNext())
		{
			RecordWebface rcd = (RecordWebface) formats.next();
			// Don't consider subfile records (the control record will deal with it)
			if (!(rcd instanceof RecordWebfaceSFL))
			{
				// If any errors are specified on a field then position cursor to first error
				int errcount = rcd.errmsgs.size();
				for (int i=0; i<errcount; i++)
				{
					ERRMSGMessageDefinition md = (ERRMSGMessageDefinition)rcd.errmsgs.elementAt(i);
					// We built this ERRMSGMessageDefinition, so the LocationOnDevice isn't necessarily set correctly
					//id = md.getLocationOnDevice();
					id = new LocationOnDevice(DEFAULT_CURSOR_POSITION, 1, rcd.getRecordName(), rcd.javascriptName(md.getFieldName()));
					break nextFormat;
				}
				RecordWebfaceSFLCTL sflctl = null;
				SubfileControlRecordViewDefinition sflctldef = null;
				if (rcd instanceof RecordWebfaceSFLCTL)
				{
					sflctl = (RecordWebfaceSFLCTL)rcd;
					sflctldef = (SubfileControlRecordViewDefinition)rcd.viewDef;
				}
				// See if DSPATR(PC) was specified on this format, and use it if it is available
				id = getDspatrPCid(rcd, rcd, rcd.viewDef.getDspatrPCFieldInfos());
				if (id!=null)
					break nextFormat;
				// If this is a SFLCTL formtat, then see if DSPATR(PC) was specified for any of the subfile fields
				if (sflctldef != null)
				{
					id = getDspatrPCid(rcd, sflctl.sfl, sflctldef.getSubfileDspatrPCFieldInfos());
					if (id!=null)
						break nextFormat;
				}
								
				// If DSPATR(PC) wasn't specified for this record, then use first field in definition that is input-capable
				Iterator it = rcd.viewDef.getFieldViewDefinitions();
				id = calculateLocationForCursorFocus(rcd, rcd, it, idOutput);
				if (id != null)
					break nextFormat;
				/*
				while (it.hasNext())
				{
					FieldViewDefinition fvd = (FieldViewDefinition) it.next();
					String fieldName = fvd.getFieldName();
					// ...but the dataDef contains its I/O status, so we have to get both.
					FieldDataDefinition fdd =
						rcd.dataDef.getFieldDefinition(fieldName);
					//String s = "l1_" + rcd.recordName + "$" + fieldName;
					LocationOnDevice l = new LocationOnDevice(DEFAULT_CURSOR_POSITION, 1, rcd.getRecordName(), fieldName);
					// If the field is input capable, then we have a match
					if (fdd != null && fdd.isInputCapable())
					{
						id = l;
						break nextFormat;
					}
					// Otherwise, store away output fields for possible use later
					else if (idOutput == null)
						idOutput = l;
				}
				*/
				// If this is a SFLCTL formtat, then get the subfile fields if no control fields exist
				if (sflctldef != null)
				{
					it = sflctldef.getSubfileFieldViewDefinitions();
					id = calculateLocationForCursorFocus(rcd, sflctl.sfl, it, idOutput);
					if (id != null)
						break nextFormat;
				}
			}
		}
		// No DSPATR or input-capable field was found.  Just use the first output-capable field
		if (id == null)
		{
			if (idOutput != null)
				id = idOutput;
			else
				id = new LocationOnDevice();
		}
		return id;
	}

	public IFieldMessageDefinition getERRMSGForFocus()
	{
		return null;
	}

	public String getJobCCSID()
	//TODO calculate actual CCSID
	{
		return "37";
	}

	public IBuildRecordViewBean getLastWrittenRecord()
	{
		return null;
	}

	public LocationOnDevice getLocationOnDeviceAt(CursorPosition arg1)
	{
		return null;
	}

	public int getMaxColumn()
	{
		return 132;
	}

	public int getMaxRow()
	{
		return 27;
	}

	public java.util.Iterator getMessages()
	{
		return messages.iterator();
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IScreenBuilder#getMessagesAndIDs()
	 */
	public Iterator[] getMessagesAndIDs()
	{
		messagesAndIDs[0]=messages.iterator();
		messagesAndIDs[1]=ids.iterator();
		return messagesAndIDs;
	}

	public String getRecordAtLocation(int arg1, int arg2)
	{
		return null;
	}

	public ArrayList getRecordLayersOnDevice()
	{
		return layerList;
	}

	/**
	 * Get the style name associated with this application.
	 */
	public String getStyleName()
	{
		return styleName;
	}

	public IBuildRecordViewBean getSubfileRecordForPageOp(CursorPosition param1)
	{
		return null;
	}

	public int getTopPositioningLayerIndex(int arg1)
	{
		return 0;
	}

	/**
	 * Return an iterator to all of the record formats that have been written to.
	 */
	public Iterator getWrittenFormats()
	{
		return writtenFormats.iterator();
	}

	public boolean hasHelpSpecifications()
	{
		return false;
	}

	public boolean isDeviceReinvited()
	{
		return false;
	}

	public boolean isHelpEnabled()
	{
		return false;
	}

	public boolean isHLPRTNActive()
	{
		return false;
	}

	public boolean isHtmlHelp()
	{
		return false;
	}

	public boolean isInBiDiMode()
	{
		return false;
	}

	public boolean isRecordActive(
		com.ibm.as400ad.webfacing.runtime.view.IBuildRecordViewBean arg1)
	{
		return false;
	}

	public boolean isSecondReadFromRecord()
	{
		return false;
	}

	public boolean isWide()
	{
		return false;
	}

	/** 'Read' the values from the display and process function keys, scroll requests, etc. */
	public String processDisplay(
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		processDisplay(request, response, styleName);
		return null;
	}
	public void processDisplay(
		HttpServletRequest request,
		HttpServletResponse response,
		String StyleName)
		throws Exception
	{
		processDisplay(request, response, styleName, applicationTitle);
	}
	public void processDisplay(
		HttpServletRequest request,
		HttpServletResponse response,
		String styleName,
		String applicationTitle)
		throws Exception
	{
		PrintWriter out = response.getWriter();
		if (ctxPath==null)
		{
			ctxPath = request.getContextPath();
			// Set host request object
			//FSession.setJobInfoRequestor(new HostJobInfo(new I2HostRequest()));
		} 
		boolean ereturn=false;
		try
		{
			//String forward = ctxPath + "/styles/chrome/PageBuilder.jsp";
			HttpSession session = request.getSession();
			session.setAttribute("screenbuilder", this);
			//request.setAttribute("screenbuilder", this);
			boolean bsfl = readParms(request);
			/* These are used by the WebFacing servlets that we tried unsuccessfully to use.
			session.setAttribute("hostrequest", hostRequest);
			session.setAttribute("FIELDEXITKEYCODE", FIELD_EXIT_KEY_CODE);
			session.setAttribute("INSERTMODE", INSERT_MODE);
			*/
			out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\">");
			out.println("<html>");
			
			if (terminated!=null && terminated instanceof Exception)
				throw (Exception)terminated;
				
				writeFormats(request, styleName, applicationTitle, bsfl);
			
				//response.setContentType("text/html; charset=UTF-8");
					
				out.println("<head>");
				out.println("<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
				out.println("<title>" + applicationTitle + "</title>");
				out.println("<script language='JavaScript'> window.status = 'Opening page...'; </script> ");
				out.println("</head>");
				out.println("<script language='JavaScript'> ");
				out.println(" logoff = function(obj) { ");
				out.println(" try{window.opener.closeWinListner();}catch(anyexp){}");
				out.println(" if (window.event.clientX < 0){");
				out.println("     obj.sender.document.open();");
				out.println("     obj.sender.document.writeln(\"<html>\");");
				out.println("     obj.sender.document.writeln(\"<head>\");");
				out.println("     obj.sender.document.writeln(\"<META http-equiv=Content-Type content='text/html; charset=UTF-8'>\");");
				out.println("     obj.sender.document.writeln(\"</head>\");");
				out.println("     obj.sender.document.writeln(\"<BODY>\");");
				//printwriter.println("     obj.sender.document.writeln(\"<FORM NAME=WINDOWCLOSE METHOD=POST ACTION='WebFacing'>\");");
				out.println("     obj.sender.document.writeln(\"<FORM NAME=WINDOWCLOSE METHOD=POST>\");");
				out.println("     obj.sender.document.writeln(\"<INPUT NAME='LOGOFF'  SIZE=30 VALUE='LOGOFF'> \");");
				out.println("     obj.sender.document.writeln(\"</FORM>\");");
				out.println("     obj.sender.document.writeln(\"</BODY>\");");
				out.println("     obj.sender.document.writeln(\"</html>\");");
				out.println("     obj.sender.document.close();");
				out.println("     obj.sender.document.WINDOWCLOSE.submit();");
				out.println("   alert('You have exited the browser session. The application is ending.'); ");
				out.println(" } ");
				out.println("}</script>");
				out.println("<frameset name=\"main\" rows=\"*,100%\" border=\"0\" onunload=\"logoff(this);\">");
				//printwriter.println("<frame name=\"sender\" src=\"styles/m-t.html\">");
				//printwriter.println("<frame name='sender' src='" + ctxPath + "styles/m-t.html'>");
				out.println("<frame name='sender'>");
				//String src = ctxPath + "/I2web/I2Host.jsp";
				// String src = ctxPath + "/I2Host.jsp";
				// Open I2PageBuilder jsp (e.g. I2web/PageBuilderavenue.jsp) 
				
				// Allow I2web directory to be configured by user
				//String src = "/I2web/PageBuilder" + styleName + ".jsp";
				String src = rioWebDirectory + "/PageBuilder" + styleName + ".jsp";
				                                                                                                                                                                                                                                              
				/* This always seems to return an object???
				RequestDispatcher rd = request.getRequestDispatcher(src);
				if (rd==null)
					throw new Exception("Cannot find " + src + "-- you may have forgotten to copy the I2Web directory into your project");
				*/
				
				// TODO If one doesn't exist, then create it by substituting 
				//<jsp:include page="/WFScreenBuilder" with I2setup.jsp, I2ScreenBuilder.jsp
				/*
				URL url = session.getServletContext().getResource(src);
				if (url==null)
					;
				*/
				out.println("<frame name='app' src='" + ctxPath + src + "'>");
				out.println("</frameset>");
				//printwriter.println("</html>");
				//session.putValue("FirstScreen", new Boolean(true));
			/*
			} 
			else
			{
					request.getRequestDispatcher("/styles/chrome/PageBuilder.jsp").forward(request, response);
			}
			*/
			/*
			String forward = "/styles/chrome/PageBuilder.jsp";
			request.getRequestDispatcher(forward).forward(request, response);
			*/
		/*
		out.println("<html>");

			/*
			out.println("<head>");
			out.println("<title>Customer amount due</title>");
			out.println("</head>");
			*/
			/*
			out.println("<frameset rows='*,100%' border = 0>");
			if (ctxPath==null)
				ctxPath = request.getContextPath();
			out.println(
				"<frame name='sender' src='" + ctxPath + "/styles/m-t.html'>");
			//String s = request.getContextPath() + "I2/I2pageBuilder.jsp?turnCacheOff=1019136650671'>");
			//String s = request.getContextPath() + "/I2/I2pageBuilder.jsp";
			//out.println("<frame name='app' src='" + s + "'>");
			//out.println("<frame name='app' src='" + request.getContextPath() + "/I2web/I2pageBuilder.jsp'>");
			/*
			out.println(
				"<frame name='app' src='"
					+ ctxPath
					+ "/I2web/PageBuilder"
					+ styleName
					+ ".jsp'>");
			*/
			//out.println("<frame name='app' src='" + ctxPath	+ "/styles/chrome/PageBuilder.jsp");
			/*
			out.println("<frame name='app' src='/styles/chrome/PageBuilder.jsp");
			out.println("</frameset>");
			*/

		}
		catch (Exception e)
		{
			/*
			if (!(e instanceof Exception))
				e = new java.lang.reflect.InvocationTargetException(e);
			*/
			//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			//out.println("<body><H3>An error has occured </H3><xmp>");
			out.println("<HEAD><TITLE>Error Report</TITLE>");
			out.println("<SCRIPT language='JavaScript'> ");
			out.println("var terminated=true;");
			out.println("</SCRIPT>");
			out.println("</HEAD>");
			out.println("<BODY>");
			if (!(e instanceof EReturn))
			{
				out.println("<H1>Error 500</H1>");
				out.println(
					"<H3>An error has occured while processing request: "
						+ request.getServletPath()
						+ "</H3>");
			}
			out.println("<H3><B>Message:</B> " + e.toString() + "</H3><BR>");
			//out.println("<B>Target Servlet: </B>com.asc.custsfl.ServletSFL<BR>
			out.println("<BR><B>StackTrace: </B>");
			out.println("<HR width='100%'>");
			//out.println("<B>Root Error-1</B>:");		
			out.println("<xmp>");
			e.printStackTrace(out);
			out.println("</xmp><br></body>");
			throw (Exception)e;
		}
		catch (Throwable e)
		{
			//e.printStackTrace();
		}
		finally
		{
			out.println("</html>");
		}
	}

	public void setCursor(
		com.ibm.as400ad.webfacing.runtime.view.CursorPosition arg1)
	{
	}

	/**
	 * 'Write' all of the record formats to the JSP object. */
	public void writeFormats(
		HttpServletRequest request,
		String styleName,
		String applicationTitle,
		boolean bsfl)
		throws Throwable
	{
		HttpSession session = request.getSession();
		// The 'style' object is no longer used in the 4.0 WebFacing model
		//StyleProperties	style = new StyleProperties("/", "spen");
		//style.setStyleName("spen");
		// WebfaceStyle style = new WebfaceStyle(request, styleName);

		//InvocationProperties style = new InvocationProperties();
		//String s = style.getStyleName();
		//session.setAttribute("style", style);

		// Width of one character ???
		//session.putValue(com.ibm.as400ad.webfacing.runtime.httpcontroller.IHttpSessionVariable.W_WIDTH, "13");
		session.setAttribute(IHttpSessionVariable.W_WIDTH,	"13");
		String contextPath = request.getContextPath();
		//session.putValue("ServletContextPath", contextPath);
		session.setAttribute("ServletContextPath", contextPath);
		//session.putValue("ApplicationTitle", applicationTitle);
		session.setAttribute("ApplicationTitle", applicationTitle);
		String servletPath = request.getServletPath();
		session.setAttribute("ServletPath", servletPath);
		// If this is a subfile position request, then don't wait for the application.  Just reprocess record.
		I2Logger.logger.debug("Parms acquiring...");
		if (terminated != null)
			throw (Throwable)terminated;
		if (!bsfl)
			acquiring();
		if (terminated != null)
			throw (Throwable)terminated;
		//session.setAttribute("screenbuilder", this);
		//request.setAttribute("screenbuilder", this);
		/* This has changed so that in WDSc 5.0 everything is scoped to 'request' instead of 'session', so we have to do this in the 
		 * jsp itself (I2setup.jsp)
		int fmtCount = writtenFormats.size();
		for (int i=0; i<fmtCount; i++)
		{
			RecordWebface rcd = (RecordWebface)writtenFormats.elementAt(i);
			String rcdName = rcd.name;
			session.setAttribute(rcdName, rcd);
			//request.setAttribute(rcdName, rcd);
			// Add scroll-bar object if this is a SFLCTL format
			if (rcd instanceof RecordWebfaceSFLCTL)
			{
				String scrollid = rcd.name + "$Scrollbar";
				session.setAttribute(scrollid, rcd);
				//request.setAttribute(scrollid, rcd);
				rcd.contextPath = contextPath;
			}
		}
		*/
		String sflctlName="";
		int fmtCount = writtenFormats.size();
		for (int i=0; i<fmtCount; i++)
		{
			RecordWebface rcd = (RecordWebface)writtenFormats.elementAt(i);
			if (rcd instanceof RecordWebfaceSFLCTL)
			{
				rcd.contextPath = contextPath;
				if (((RecordWebfaceSFLCTL)rcd).isScrollbarShown())
					sflctlName=rcd.toString();
			}
		}
		session.setAttribute("sflctlName", sflctlName);

		if (!bsfl)
			acquired();
		I2Logger.logger.debug("Parms acquired...");
	}

	public String getActiveKeyName(String parm1)
	{
		return null;
	}

	/**
	 * @see com.ibm.as400ad.webfacing.runtime.view.IScreenBuilder#getActiveKeysWithoutEnter()
	 */
	public Iterator getActiveKeysWithoutEnter()
	{
		return getActiveKeysVector(null).iterator();
	}

	/**
	 * @see com.ibm.as400ad.webfacing.runtime.view.IScreenBuilder#getConceptualLayerZOrder(String)
	 */
	public int getConceptualLayerZOrder(String arg0)
	{
		return 0;
	}

	/**
	 * @see com.ibm.as400ad.webfacing.runtime.view.IScreenBuilder#getRecordsWithPageUPDNKey()
	 */
	public String[] getRecordsWithPageUPDNKey()
	{
		pageUPDNarray[0] = ""; //page up record name
		pageUPDNarray[1] = ""; //page up record name
		int fmtCount = writtenFormats.size();
		for (int i=0; i<fmtCount; i++)
		{
			RecordWebface rcd = (RecordWebface)writtenFormats.elementAt(i);
			if (rcd instanceof RecordWebfaceSFLCTL)
			{
				pageUPDNarray[0]='"' + rcd.getRecordName() + '"';
				pageUPDNarray[1]=pageUPDNarray[0];
			}
		}
		return pageUPDNarray;
	}

	/**
	 * @see com.ibm.as400ad.webfacing.runtime.view.IScreenBuilder#isRecordActive(String)
	 */
	public boolean isRecordActive(String arg0)
	{
		return false;
	}

	/**
	 * @see com.ibm.as400ad.webfacing.runtime.view.IScreenBuilder#isRecordOnTopLayer(RecordViewBean)
	 */
	public boolean isRecordOnTopLayer(RecordViewBean arg0)
	{
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IScreenBuilder#getRecordAt(com.ibm.as400ad.webfacing.runtime.view.CursorPosition)
	 */
	public IBuildRecordViewBean getRecordAt(CursorPosition arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IScreenBuilder#getFirstRollEnabledRecords()
	 */
	public RecordViewBean[] getFirstRollEnabledRecords() {
		rollEnabledArray[0] = null; //page up record name
		rollEnabledArray[1] = null; //page up record name
		//TODO this doesn't work because we can't create a RecordViewBean
		/*
		int fmtCount = writtenFormats.size();
		try
		{
			for (int i=0; i<fmtCount; i++)
			{
				RecordWebface rcd = (RecordWebface)writtenFormats.elementAt(i);
				if (rcd instanceof RecordWebfaceSFLCTL)
				{
					rollEnabledArray[0]=rcd.viewBean;
					rollEnabledArray[1]=rollEnabledArray[0];
				}
			}
		}
		catch (Exception e) {}
		*/
		return rollEnabledArray;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IDeviceLayer#isWindowed()
	 */
	public boolean isWindowed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IDeviceLayer#name()
	 */
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IDeviceLayer#getFirst()
	 */
	public VisibleRectangle getFirst() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IDeviceLayer#getFirstColumn()
	 */
	public int getFirstColumn() {
		// TODO Auto-generated method stub
		return 1;
	}
	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IDeviceLayer#getFirstRow()
	 */
	public int getFirstRow() {
		// TODO Auto-generated method stub
		return 1;
	}
	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IDeviceLayer#getLastColumn()
	 */
	public int getLastColumn() {
		// TODO Auto-generated method stub
		return 132;
	}
	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IDeviceLayer#getRecords()
	 */
	public Iterator getRecords() {
		return writtenFormats.iterator();
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IDeviceLayer#getRectanglesIterator()
	 */
	public Iterator getRectanglesIterator() {
		return getRecords();
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IDeviceLayer#isFocusCapable()
	 */
	public boolean isFocusCapable() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IDeviceLayer#isVerticallyPositioned()
	 */
	public boolean isVerticallyPositioned() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IDeviceLayer#isCLRLWindow()
	 */
	public boolean isCLRLWindow() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IDeviceLayer#getWindowTitle()
	 */
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IDeviceLayer#getWindowTitleAlignment()
	 */
	public String getWindowTitleAlignment() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ibm.as400ad.webfacing.runtime.view.IScreenBuilder#isRecordOnTopPresentationLayer(com.ibm.as400ad.webfacing.runtime.view.RecordViewBean)
	 */
	public boolean isRecordOnTopPresentationLayer(RecordViewBean arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
