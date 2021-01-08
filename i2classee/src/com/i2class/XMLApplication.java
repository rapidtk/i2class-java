package com.i2class;

import javax.servlet.http.*;
/**
 * A class that encapsulates an XML/XSLT interface 
 */
public class XMLApplication extends ContentApplication {
	protected XMLApplication() {
		super();
	}
	public XMLApplication(Application app) {
		super(app);
	}
	public XMLApplication(Application app, String datedit) {
		super(app, datedit);
	}
	public XMLApplication(Application app, String datedit, String datfmt) {
		super(app, datedit, datfmt);
	}
	static public String performThread(HttpServletRequest request, HttpServletResponse response, Class programClass) throws Exception
	{
		return ContentApplication.performThread(request, response, programClass, ThreadLockXML.class);
	}
}
