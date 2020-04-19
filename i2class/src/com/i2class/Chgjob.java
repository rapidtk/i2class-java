package com.i2class;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.i2class.*;

/**
 * A class to change attributes of an OS/400-style job.
 * 
 */
public class Chgjob
{
	Application m_app;
	
	public Chgjob() {}
	public Chgjob(I2Connection rconn)
	{
		m_app=rconn.app;
	}
	
	/**
	 * Set the short (e.g. *MDY) date format
	 */
	public void setDate(String date)
	{
		m_app.appJob.jobDateString = date.toString();
	}
	public void setDate(FixedChar date)
	{
		setDate(date.toString());
	}

	/**
	 * Set the date format of the application (*MDY, *YMD, *DMY, *JUL)
	 */
	public void setDatfmt(String datfmt)
	{
		m_app.appJob.jobDatfmt=datfmt;
	}
	public void setDatfmt(FixedChar datfmt)
	{
		setDatfmt(datfmt.toString());
	}

	/** Set the job date separator */
	public void setDatsep(char datsep)
	{
		m_app.appJob.jobDatsep=datsep;
	}
	public void setDatsep(String datsep)
	{
		setDatsep(datsep.charAt(0));
	}
	public void setDatsep(FixedChar datsep)
	{
		setDatsep(datsep.charAt(0));
	}

	/** Set the job decimal format */
	public void setDecfmt(char decfmt)
	{
		m_app.appJob.jobDecfmt=decfmt;
	}
	public void setDecfmt(String decfmt)
	{
		setDecfmt(decfmt.charAt(0));
	}
	public void setDecfmt(FixedChar decfmt)
	{
		setDecfmt(decfmt.charAt(0));
	}

	/** Set the job switches */
	public void setSws(String sws)
	{
		m_app.appJob.jobSws=sws;
	}
	public void setSws(FixedChar sws)
	{
		setSws(sws.toString());
	}


	/** Set the job time separator */
	public void setTimsep(char timsep)
	{
		m_app.appJob.jobTimsep=timsep;
	}
	public void setTimsep(String timsep)
	{
		setTimsep(timsep.charAt(0));
	}
	public void setTimsep(FixedChar timsep)
	{
		setTimsep(timsep.charAt(0));
	}

	/** Change the job's attributes. */
	public void exec()
	{
	}
	
}
