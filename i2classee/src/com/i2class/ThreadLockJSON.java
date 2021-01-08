package com.i2class;

import javax.servlet.http.HttpServletResponse;

public class ThreadLockJSON extends ThreadLockContent {
	
	public String getContent(HttpServletResponse response) throws Exception {
		return getJSON();
	}

}
