package com.i2class;

import javax.servlet.http.*;
/**
 * A class that encapsulates a JSON interface 
 */
public class JSONApplication extends ContentApplication {
	protected JSONApplication() {
		super();
	}
	public JSONApplication(Application app) {
		super(app);
	}
	public JSONApplication(Application app, String datedit) {
		super(app, datedit);
	}
	public JSONApplication(Application app, String datedit, String datfmt) {
		super(app, datedit, datfmt);
	}
	static public String performThread(HttpServletRequest request, HttpServletResponse response, Class programClass) throws Exception
	{
		return ContentApplication.performThread(request, response, programClass, ThreadLockJSON.class);
	}
}
