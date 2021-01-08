package com.i2class;

import javax.servlet.http.*;
/**
 * A class that encapsulates an XML or JSON interface 
 */
public class ContentApplication extends ThreadApplication {
	protected ContentApplication() {
		super();
	}
	public ContentApplication(Application app) {
		super(app);
	}
	public ContentApplication(Application app, String datedit) {
		super(app, datedit);
	}
	public ContentApplication(Application app, String datedit, String datfmt) {
		super(app, datedit, datfmt);
	}
	static public String performThread(HttpServletRequest request, HttpServletResponse response, Class programClass, Class threadLockClass) throws Exception
	{
		
		HttpSession session = request.getSession();
		Application program = (Application)session.getAttribute("program");
		if (program==null || program.threadLock.terminated != null)
		{
			program = (Application)programClass.newInstance();
			//program.threadLock = new ThreadLockContent();
			program.threadLock = (ThreadLock)threadLockClass.newInstance();
			session.setAttribute("program", program);
			program.start();
		}
		return ((ThreadLockServlet)(program.threadLock)).processDisplay(request, response);
	}
	static public String performThread(HttpServletRequest request, HttpServletResponse response, Class programClass) throws Exception
	{
		return ContentApplication.performThread(request, response, programClass, ThreadLockContent.class);
	}
}
