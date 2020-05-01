package com.i2class;

import java.lang.reflect.Constructor;
import java.util.*;
import javax.servlet.http.*;
/**
 * A thread-enabled I2 app built specifically to interact with WebFaced display files.
 * 
 */
public class WebfaceApplication extends ThreadApplication
{
	static HttpServletRequest m_request;
	static HttpServletResponse m_response;
	static final Class[] CTOR_PARMS = {Application.class}; 
	/**
	 * Insert the method's description here.
	 * Creation date: (6/5/2002 1:59:22 PM)
	 * @param app com.asc.rio.RPGapp
	 */
	public WebfaceApplication(Application app)
	{
		super(app);
	}
	/**
	 * Construct this I2 WebFaced application with the specified date edit
	 * @param datfmt java.lang.String date Date format (*MDY, *YMD, *DMY, *JUL)
	 */
	public WebfaceApplication(Application app, String datedit)
	{
		super(app, datedit);
	}
	/**
	 * Construct this I2 WebFaced application with the specified date edit, date format. 
	 */
	public WebfaceApplication(Application app, String datedit, String datfmt)
	{
		super(app, datedit, datfmt);
	}
	
	/*
	synchronized static public Object newInstance(Class programClass, HttpServletRequest request) throws InstantiationException, IllegalAccessException
	{
		// Information from the request may be used during the initialization (constructor) of an object, so 
		// we create a static handle here that can be accessed.  Later accesses will use the request object
		// in the ThreadLock object, itself
		m_request = request;
		Object o =  programClass.newInstance();
		m_request = null;
		return o;
	}*/
	
	static public void performThread(
		HttpServletRequest request,
		HttpServletResponse response,
		String className)
		throws Exception
	{
		performThread(request, response, Class.forName(className));
	}
	static public void performThread(
		HttpServletRequest request,
		HttpServletResponse response,
		Class programClass)
		throws Exception
	{

		performThread(request, response, programClass, null);
	}
	static public void performThread(
		HttpServletRequest request,
		HttpServletResponse response,
		Class programClass,
		String applicationTitle)
		throws Exception
	{

		HttpSession session = request.getSession();
		Application program = (Application) session.getAttribute("program");
		//		program.threadLock.terminated should never be true if the "program" attribute gets cleaned up correctly		
		if (program == null || program.threadLock.terminated != null)  
		{
			//program = (I2webface)newInstance(programClass, request);
			
			// If this is a logoff request after the program has already ended, just return without doing anything
			if (request.getParameter("LOGOFF")!=null)
				return;
				
			program = (Application) programClass.newInstance();
			program.threadLock = new ThreadLockWebface();
			
			/*
			if (nullApp == null)
				nullApp = new Application(null, new ThreadLockWebface());
			Constructor ApplicationCtor = Application.class.getConstructor(CTOR_PARMS);
			Object[] ctorParms = {nullApp};
			program = (Application)ApplicationCtor.newInstance(ctorParms);
			*/
			
			if (applicationTitle == null)
			{
				applicationTitle = "";
				// This doesn't seem to work anymore...
				/*
				String invokeCode = request.getParameter("inv");
				if (invokeCode != null)
				try
				{
					InvocationProperties inv = InvocationProperties.getInvocationProperties(invokeCode);
					applicationTitle = inv.getTitle();
				}
				catch (Exception e) {}
				*/
				try
				{
					ResourceBundle prop = getResourceBundle();
					applicationTitle = prop.getString("I2ApplicationTitle");
				}
				catch (Exception e)
				{
					applicationTitle = "";
				}
			}
			program.threadLock.applicationTitle = applicationTitle;
			session.setAttribute("program", program);
			program.start();
		}
		
		// Save the request object so that it can be used by other processes (e.g. Rtvjoba)
		ThreadLockWebface lock = (ThreadLockWebface)program.threadLock;
		lock.m_request = request;
		lock.m_response = response;
		
		// Process the current display.  
		try
		{
			lock.processDisplay(request, response);
		}
		// If the application has ended (either abnormally or normally through an EReturn exception)
		// then remove it from the session object.
		catch (Exception e)
		{
			session.removeAttribute("program");
			//session.removeAttribute("screenbuilder");
			//program.threadLock = null;
			throw e;
		}
	}
}
