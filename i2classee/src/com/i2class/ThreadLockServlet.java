package com.i2class;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Stalled thread class to implement READ/EXFMT functionallity.
 * 
 */
public abstract class ThreadLockServlet extends ThreadLock
{
	HttpServletRequest m_request;
	HttpServletResponse m_response;


	/*
	public void includeFormats(
		HttpServlet servlet,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		ServletContext sc = servlet.getServletContext();
		acquiring();
		int fmtCount = writtenFormats.size();
		try
		{
			for (int i = 0; i < fmtCount; i++)
			{
				RecordThread rcd = (RecordThread) writtenFormats.elementAt(i);
				String fmtName = rcd.recordName;
				request.setAttribute(fmtName, rcd);
				String fmtJSP = "/" + rcd.file.fileName + "/" + fmtName + ".jsp";
				RequestDispatcher rd = sc.getRequestDispatcher(fmtJSP);
				rd.include(request, response);
			}
		}
		finally
		{
			acquired();
		}
	}
	*/

	/*
	ThreadLockServlet(Application app) {
		super(app);
	}
	*/
	
	public abstract String processDisplay(
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception;
	/*
	{
		return null;
	}
	*/

	/** Check for a positioning request. */  
	protected int checkPositioning(IRecordSFLCTL sflctl, javax.servlet.http.HttpServletRequest request)
	{
			return checkPositioning(sflctl, request.getParameter("AID"));	
	}

	/** Read the parameters (changed values) passed in from the display. */
	public boolean readParms(javax.servlet.http.HttpServletRequest request)
		throws Exception
	{
		// If this is a subfile positioning request, then don't return control to RPG program, just simply
		// reposition and go on.
		boolean bsfl = false;
		int sflpos = -1;
		//IRecordSFLCTL sflctlrcd=null;
		IRecordSFLCTL sflctlrcd=null;
		if (lastRcd instanceof IRecordSFLCTL)
		{
			sflctlrcd = (IRecordSFLCTL) lastRcd;
			//sflpos = sflctlrcd.checkPositioning(request.getParameter("AID"));
			sflpos = checkPositioning(sflctlrcd, request);
		}
		if (sflpos>0)
		{
			// Save any values from the current subfile before scrolling down
			saveSubfileValues(request, sflctlrcd);
			sflctlrcd.getSfl().setTopRRN(sflpos);
			// Mark the SFLCTL record so that it knows how to get back to the saved values
			sflctlrcd.setScrollLock(this);
			scrollRcd = sflctlrcd;
			bsfl = true;
			//break;
		}
		else
		{
			// Clear any previous scroll up/down 
			if (scrollRcd != null)
			{
				scrollRcd.setScrollLock(null);
				scrollRcd = null;
			}
			else
				clearParms();
			readParms2(request);
		}
		return bsfl;
	}



	protected boolean readParms2(HttpServletRequest request)
		throws java.lang.Exception
	{
		Enumeration e = request.getParameterNames();
		while (e.hasMoreElements())
		{
			String parm = (String)(e.nextElement());
			// Move the call to sflFieldName() to addFieldValue
			//String fieldName = sflFieldName(parm);
			String value = request.getParameter(parm);
			addFieldValue(parm, value);
		}

		// Release parameterS and allow to go on
		if (!firstRead)
		{
			//custamt.fileLock.parmRelease();
			//custamt.fileLock.parmRelease2();
			I2Logger.logger.debug("Parms releasing");
			releasingParms();
			releasedParms();
		}
		firstRead = false;
		return false;
	}

	/**
	 * Save any values on the current page of the subfile.
	 */
	protected void saveSubfileValues(
		HttpServletRequest request,
		IRecordSFLCTL sflctlrcd)
	{
		//String sflDisplayName = sflctlrcd.getSflDisplayName();
		String sflDisplayName = ((RecordWorkstn)sflctlrcd).recordName;
		// Save any subfile values from the current page
		Enumeration e = request.getParameterNames();
		while (e.hasMoreElements())
		{
			String parm = (String) (e.nextElement());
			String value = request.getParameter(parm);
			saveSubfileValue(sflctlrcd, parm, value);
		}
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (4/16/2002 7:33:17 AM)
	public void writeJSP(
		HttpServlet servlet,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		readParms(request);

		//custamt.fileLock.request = request;
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println(
			"<link rel='stylesheet' type='text/css' href='styles/spen/spen.css'>");

		//out.println("<script language='JavaScript' src='ClientScript/rioValidate.js'></script>");
		out.println(
			"<script language='JavaScript'> function submitForImmediateWrite() {");
		out.println(" document.form1.submit(); ");
		out.println("} </script>");

		out.println("<script language='JavaScript'>");
		out.println(" function setAttr() {");
		out.println(" webAppName='/Cservlet'; ");
		out.println(" webStyleName='spen'; ");
		out.println(" hostJobCCSID='37'; ");
		//out.println(" setUpMT(); ");

		//RequestDispatcher rd;
		//rd = getServletContext().getRequestDispatcher("/DSPFMT1JavaScript.jsp");
		//rd.include(request, response);

		out.println(" javascriptLoaded=true; ");
		out.println("}");
		out.println("</script>");
		out.println("</head>");

		//custamt.fileLock.acquire();
		//out.println(custamt.cname);
		//custamt.fileLock.acquire2();

		out.println("<body onLoad='setAttr()'>");
		out.println("<form name='form1'>");

		// Function keys
		out.println(
			"<table border='0' cellspacing='0' cellpadding='0' frame='box'>");
		out.println("<tr>");
		//out.println("<td class='thebutton' width='110' height='27' title='ENTER' onClick='validateAndSubmit(\"CF03\")' >");
		out.println(
			"<td class='thebutton' width='110' height='27' title='ENTER' onClick='submitForImmediateWrite()' >");
		out.println("Enter");
		out.println("</td>");
		out.println("</table>");

		out.println("<table><tbody>");

		//rd = getServletContext().getRequestDispatcher("/DSPFMT1.jsp");
		//rd = request.getRequestDispatcher("/DSPFMT1.jsp");
		//rd.include(request, response);
		includeFormats(servlet, request, response);

		out.println("</form>");
		out.println("</body>");
		out.println("</html>");
	}
	 */
	
	/*
	 * @return the request associated with this thread
	 */
	HttpServletRequest getRequest() {
		// m_request can be null if this thread is running in the constructor of a I2webface class
		if (m_request==null)
			return WebfaceApplication.m_request;
		return m_request;
	}
	/*
	 * @return the response associated with this thread
	 */
	HttpServletResponse getResponse() {
		// m_request can be null if this thread is running in the constructor of a I2webface class
		if (m_response==null)
			return WebfaceApplication.m_response;
		return m_response;
	}

}
