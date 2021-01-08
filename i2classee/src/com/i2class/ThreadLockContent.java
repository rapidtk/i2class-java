package com.i2class;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Thread lock based upon content type
 * 
 * @author: ACL
 */
public class ThreadLockContent extends ThreadLockServlet
{
	public String getContent(HttpServletResponse response) throws Exception 
	{
		// Get content
		String content;
		String contentType = response.getContentType();
		// Assume JSON contentType, unless XML explicitly specified...
		if ("text/xml".equals(contentType))
			content =getXML();
		else
			content =getJSON();
		return content;
	}

	public String processDisplay(
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		boolean bsfl = readParms(request);
		
		return getContent(response);
		
	}
}
