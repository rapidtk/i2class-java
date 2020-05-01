package com.i2class;

import javax.servlet.http.HttpServletRequest;

public class ServletApplication extends Application {
	
	/** Return the request object (HttpServletRequest, if any) associated with this application. */
	public Object getRequest()
	{
		HttpServletRequest request = null;
		if (threadLock instanceof ThreadLockServlet)
			request = ((ThreadLockServlet)threadLock).getRequest();
		return request;
	}

}
