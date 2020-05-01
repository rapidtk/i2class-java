package com.i2class;

import java.io.*;
import javax.servlet.http.*;
/**
 * Insert the type's description here.
 * Creation date: (7/14/2003 2:30:30 PM)
 * @author: 
 */
public class ThreadLockXML extends ThreadLockServlet
{
	/*
	ThreadLockXML(Application app) {
		super(app);
		// TODO Auto-generated constructor stub
	}
	*/

	public String processDisplay(
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		//PrintWriter out = response.getWriter();
		//out.println("<?xml version=\"1.0\"?>");
		//out.println("<DSPF-DATA>");
		boolean bsfl = readParms(request);
		String xml=getXML();
		//out.println(xml);
		//out.println("</DSPF-DATA>");
		return xml;
	}
}
