/*
 * Created on Feb 9, 2005
 *
 */
package com.i2class;

import java.io.Serializable;
import java.sql.Connection;
import java.util.*;
import java.util.Date;

/**
 * A wrapper that allows each I2 stack to operator in its own OS/400-like 'job'.
 * @author ANDREWC
 *
 */
public class I2Job implements Serializable {
	// Job date
	Date jobDate=new Date();
	String jobDateString;

	// Job-level date format
	String jobDatfmt;
	// Job-level date separator
	char jobDatsep;
	// Job-level time separator
	char jobTimsep;
	// Job-level decimal format
	public char jobDecfmt;
	public String jobSws="00000000";
	
	// User portion of library list
	Vector usrlibl;
	
	/** The connection associated with this job.  This is only needed when files in QTEMP are referenced. */
	Connection conn;
	boolean LDA=false; // Has the LDA been referenced in this job?
	HashSet qtempObjects;
	/** The connection used by this job for files opened under commitment control. */
	Connection connCommit;
	
	int jobNumber;
	/** System-wide 'last' job number. */
	static int systemJobNumber; 

	/**
	 * 
	 */
	public I2Job() {
		// Generate 'unique' job number (they roll over at 999999)
		if (systemJobNumber<999999)
			systemJobNumber++;
		else
			systemJobNumber=1;
		jobNumber=systemJobNumber;
		
		// TODO: should really generate job date here?, but adds overhead to all jobs which might not use it
	}

	/** Return the job date */
	String getJobDate()
	{
		if (jobDateString==null)
		{
			date d = new date(getJobDatfmt()+'0'); // e.g. *MDY0
			jobDateString = d.format(jobDate);
		}
		return jobDateString;
	}
	

	/** Return the job date format */
	String getJobDatfmt()
	{
		if (jobDatfmt==null)
			jobDatfmt=Rtvsysval.datfmt();
		return jobDatfmt;
	}
	

	/** Return the job date separator */
	char getJobDatsep()
	{
		if (jobDatsep=='\0')
			jobDatsep=Rtvsysval.datsep();
		return jobDatsep;
	}


	/** Return the job-level decimal format */
	char getJobDecfmt()
	{
		if (jobDecfmt=='\0')
			jobDecfmt=Rtvsysval.decfmt();
		return jobDecfmt;
	}
	

	/** Return the job-level time separator */
	char getJobTimsep()
	{
		if (jobTimsep=='\0')
			jobTimsep=Rtvsysval.timsep();
		return jobTimsep;
	}
	

	/** Return the Vector that describes the job library list */
	public Vector getUsrlibl()
	{
		if (usrlibl==null)
			usrlibl=new Vector(25);
		return usrlibl;
	}
	
	// Remove the specified object from the list of objects in QTEMP
	public void removeFromQtemp(String objectName)
	{
		if (qtempObjects!=null)
			qtempObjects.remove(objectName);
	}
	
	// Add the specified object to list of objects in QTEMP if the qualified library name is QTEMP
	public void addToQtemp(QfileName q)
	{
		if (q.libName.compareTo("QTEMP")==0)
		{
			if (qtempObjects==null)
				qtempObjects = new HashSet();
			qtempObjects.add(q.fileName);
		}
	}
	
	// Returns a boolean value indicating whether the specified value exists in QTEMP
	public boolean inQtemp(String objectName)
	{
		return qtempObjects != null && qtempObjects.contains(objectName);
	}



}
