package com.i2class;

import javax.servlet.http.*;
/**
 * A class that encapsulates an XML/XSLT interface 
 */
public class XMLApplication extends ThreadApplication {
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
		
		HttpSession session = request.getSession();
		Application program = (Application)session.getAttribute("program");
		if (program==null || program.threadLock.terminated != null)
		{
			program = (Application)programClass.newInstance();
			program.threadLock = new ThreadLockXML();
			session.setAttribute("program", program);
			program.start();
		}
		return ((ThreadLockServlet)(program.threadLock)).processDisplay(request, response);
	}
}
