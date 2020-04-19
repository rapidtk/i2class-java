package com.i2class;

import java.util.Vector;

import javax.servlet.http.*;

/**
 * A class to retrieve attributes from a OS/400-style job.
 * 
 */
public class Rtvjoba extends AbstractCommand
{
	public Rtvjoba(I2Connection rconn)
	{
		super(rconn);
	}

	/**
	 * Return the short (e.g. *Mdy) date format
	 * @return java.lang.String
	 */
	public String date()
	{
		/*
		String date;
		//String udate = Application.UDATE.toString();
		String udate = new FmtDate(6, "MMddyyyy").toFixedString();
		// If this is not a julian date, then the format is xx/xx/xx
		if (udate.length() > 5)
		{
			date =
				udate.substring(0, 2)
					+ "/"
					+ udate.substring(2, 4)
					+ udate.substring(4);
		}
		else
			date = udate.substring(0, 2) + "/" + udate.substring(2);
		return date;
		*/
		return getApp().appJob.getJobDate();
	}

	/**
	 * Retrieve the date format of the application (*MDY, *YMD, *DMY, *JUL)
	 * @return java.lang.String The date format
	 */
	public String datfmt()
	{
		//return Application._DATFMT;
		return getApp().appJob.getJobDatfmt();
	}

	/** Retrieve the job date separator */
	public char datsep()
	{
		return getApp().appJob.getJobDatsep();
	}

	/** Retrieve the job decimal format */
	public char decfmt()
	{
		return getApp().appJob.getJobDecfmt();
	}

	/** Retrieve the job time separator */
	public char timsep()
	{
		return getApp().appJob.getJobTimsep();
	}

	/** Retrieve the job switches */
	public String sws()
	{
		return getApp().appJob.jobSws;
	}
	
	private final static char[] BLANK_CHARS = "           ".toCharArray();
	/** Return the job library list */
	public String usrlibl()
	{
		Vector usrlibl = getApp().appJob.getUsrlibl();
		int usrliblSize = usrlibl.size();
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<usrliblSize; i++)
		{
			String lib = (String)usrlibl.elementAt(i);
			// Pad so that each entry is exactly 11 characters long
			sb.append(BLANK_CHARS, 0, 11-lib.length());
		}
		return sb.toString();
	}
	
	/**
	 * Retrieve the user name associated with the job.
	 * For a web application, this is the user that started the application server, 
	 * not necessarily the user associated with the current session. 
	 * @return java.lang.String The returned user
	 */
	public static String user() throws Pgmmsg
	{
		String user = System.getProperty("user.name");
		return user.toUpperCase();
	}
	
	/** Retrieve the current user.  This is the actual profile name used to sign on, not necessarily the user
	 * profile that the server job is running over.
	 */
	public String curuser() throws Pgmmsg
	{
		String user=null;
		// If this request is associated with a Http request, then use that user if available
		HttpServletRequest request = getRequest(getApp());
		if (request != null)
			user = request.getRemoteUser();
		/* If the user associated with the Http request is not available, then get the user used to 
		// log onto the database server.
		if (user==null)
			user = getRconn().m_usrid;
		*/
		
		// If no network information can be retrieved, just use process sign on information
		if (user==null)
			user = user();
		return user;
	}
	
	/*
	 * @return the request associated with this thread.
	 */
	static private HttpServletRequest getRequest(Application app) {
		if (app.threadLock instanceof ThreadLockServlet)
			return ((ThreadLockServlet)app.threadLock).getRequest();
		return null;
	}
	
	/** Return the subtype associated with this job (always '*') */
	static public char subtype()
	{
		return '*';
	}
	
	/** Return the type associated with this job (always '0') */
	static public char type()
	{
		return '0';
	}
	
	/** Return the job name. */
	public String job()
	{
		// Always returns "I2"
		return "I2       ";
	}
	
	/** Return the job number. */
	public int nbr()
	{
		return getApp().appJob.jobNumber;
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#exec()
	 */
	public void exec() throws Exception
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}
