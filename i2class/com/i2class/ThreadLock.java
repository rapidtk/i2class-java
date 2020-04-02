package com.i2class;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Stalled thread class to implement READ/EXFMT functionallity.
 * @author Andrew Clark
 */
public class ThreadLock implements Serializable
{

	private boolean available;
	private boolean acquired;
	private boolean availableParms;
	private boolean acquiredParms;
	//HashMap parmMap = new HashMap();
	public Vector parmNames = new Vector();
	public Vector parmValues = new Vector();
	public Vector writtenFormats = new Vector();
	public boolean firstRead = true;
	protected RecordWorkstn lastRcd;
	//protected Exception terminated;
	protected Object terminated;
	String applicationTitle;
	protected IRecordSFLCTL scrollRcd;
	
	//Job-level variables 
	
	/* User portion of library list
	Vector usrlibl;
	// Job date
	Date jobDate=new Date();
	public String jobDateString;
	// Job-level date format
	public String jobDatfmt;
	// Job-level date separator
	public char jobDatsep;
	// Job-level time separator
	public char jobTimsep;
	// Job-level switches
	public String jobSws="00000000";
	// Job-level decimal format
	public char jobDecfmt;
	*/
	
	/*
	Application app;
	ThreadLock(Application app)
	{
		this.app=app;
	}
	*/

	/** Read the parameters (changed values) passed in from the display. */
	void addFieldValue(String parm, String value)
	{
		String fieldName = sflFieldName(parm);
		int i = parmNames.indexOf(fieldName);
		// If the field isn't already set, then add it...
		if (i < 0)
		{
			parmNames.addElement(fieldName);
			parmValues.addElement(value);
			//parmMap.put(parm, value);
		}
		// ...otherwise, just overwrite what's already there
		else
			parmValues.setElementAt(value, i);
	}


	/* Thread synchronization code */
	synchronized public void acquired()
	{
		acquired = true;
		available = false;
		notifyAll();
	}
	synchronized public void acquiredParms()
	{
		acquiredParms = true;
		availableParms = false;
		notifyAll();
	}

	synchronized public void acquiring() throws Exception
	{
		if (scrollRcd==null)
		{
			while (!available && terminated == null)
				wait();
		}
	}

	synchronized public void acquiringParms() throws Exception
	{
		while (!availableParms && terminated == null)
			wait();
	}

	/* Check for a positioning request. 
	int checkPositioning(javax.servlet.http.HttpServletRequest request)
	{
		int sflpos=-1;
		String sflrrn = request.getParameter("SFLRRN");
		if (sflrrn != null)
		{
			int i = sflrrn.indexOf(":");
			sflpos = Integer.parseInt(sflrrn.substring(i + 1));
		}
		return sflpos;
	}
	*/

	void clearFormats()
	{
		writtenFormats.removeAllElements();
	}

	protected void clearParms()
	{
		parmNames.removeAllElements();
		parmValues.removeAllElements();
	}

	public String getParmValue(String parmName)
	{
		/*
		Object po = parmMap.get(parmName);
		if (po==null)
			return "";
		return (String)po;
		*/
		return null;
	}

	/**
	 * Sort the written records by their position on the screen (top to bottom)
	 * @return java.util.Iterator
	 */
	public Iterator getWrittenFormatsOrdered()
	{
		int arycnt = writtenFormats.size();
		for (int gap = arycnt / 2; 0 < gap; gap /= 2)
			for (int i = gap; i < arycnt; i++)
				for (int j = i - gap; 0 <= j; j -= gap)
				{
					//if (ary[j+gap]<ary[j])
					RecordWorkstn rcdjgap = (RecordWorkstn) writtenFormats.elementAt(j + gap);
					/*int linejgap ;
									if (rcdjgap instanceof RecordWebfaceSFL)
										linejgap=0;
									else
										linejgap
					= rcdjgap.getFirstFieldLine();
					*/
					int linejgap = rcdjgap.getFirstFieldLine();
					RecordWorkstn rcdj = (RecordWorkstn) writtenFormats.elementAt(j);
					/*int linej ;
									if (rcdj instanceof RecordWebfaceSFL)
										linej=0;
									else
										linej
					= rcdj.getFirstFieldLine();
					*/
					int linej = rcdj.getFirstFieldLine();
					if (linejgap < linej)
					{
						//T temp = ary[j];
						//RecordWebface temp = rcdj;
						//ary[j]=ary[j+gap];
						writtenFormats.setElementAt(rcdjgap, j);
						//ary[j+gap]=temp;
						writtenFormats.setElementAt(rcdj, j + gap);
					}
				}
		//return writtenFormats.iterator();
		// No J# iterator, so...
		return new VectorIterator(writtenFormats);
	}

	/**
	 * Return the XML representation of the values of all formats on the current display. */
	public String getXML() throws Exception
	{
		StringBuffer xml = new StringBuffer("<page>");
		StringBuffer fkey = new StringBuffer();
		acquiring();
		try
		{
			Iterator it = getWrittenFormatsOrdered();
			while (it.hasNext())
			{
				RecordWorkstn rcd = (RecordWorkstn)it.next();
				xml.append(rcd.getXML());
				fkey.append(rcd.getXMLfkey());
			}
			xml.append(fkey);
			xml.append("</page>");
		}
		finally
		{
			acquired();
		}
		return xml.toString();
	}

	synchronized public void released() throws Exception
	{
		while (!acquired && terminated==null)
			wait();
		acquired = false;
	}

	synchronized public void releasedParms() throws Exception
	{
		while (!acquiredParms && terminated==null)
			wait();
		acquiredParms = false;
	}

	synchronized public void releasing()
	{
		available = true;
		notifyAll();
	}

	synchronized public void releasingParms()
	{
		availableParms = true;
		notifyAll();
	}

	/**
	 * Return the subfile field name from a WebFaced name.
	 */
	protected String sflFieldName(String parm)
	{
		int j = parm.indexOf("$");
		if (j > 0)
		{
			// If this field is part of a subfile, then everything after the '$' is the record number
			String fieldName = parm.substring(j + 1);
			int k = fieldName.indexOf("$");
			if (k > 0)
			{
				// In the 4.0 WebFacing tooling, the first three characters are always "l1_"
				//String sflnam = parm.substring(0, j);
				String sflnam = parm.substring(3, j);
				int fmtCount = writtenFormats.size();
				for (int m = 0; m < fmtCount; m++)
				{
					RecordWorkstn rcd =
						(RecordWorkstn) writtenFormats.elementAt(m);
					if (sflnam.compareTo(rcd.recordName) == 0)
					{
						String rrn = fieldName.substring(k + 1);
						// Regenerate the field name so that the rrn value reflects the 'real' rrn and not the
						// rrn relative to the current page (i.e. if the top of the current subfile page is rrn 10, then
						// the first field from the first record on the page would be returned as RCD$FIELD$1 -- translate
						// to RCD$FIELD$10
						IRecordSFLX sflctlrcd = (IRecordSFLX) rcd;
						int realrrn =
							Integer.parseInt(rrn) + sflctlrcd.getSfl().getTopRRN() - 1;
						fieldName =
							parm.substring(0, j + k + 2) + Integer.toString(realrrn);
						return fieldName;
					}
				}
			}
		}
		return parm;
	}

	/**
	 * Immediately terminate the current thread.
	 */
	public synchronized void terminate(Object e) //throws Exception
	{
		if (terminated==null)
		{
			terminated = e;
			PrintStream s;
			if (e instanceof ApplicationReturn)
				s=I2Logger.logger.psLog;
			else
				s=I2Logger.logger.psError;
			if (e instanceof Throwable)
				I2Logger.logger.printStackTrace((Throwable)e);
			else
				s.println(e);
			lastRcd=null;
			scrollRcd=null;
		}
		notifyAll();
	}

	/** Check for a positioning request. */
	static protected int checkPositioning(IRecordSFLCTL sflctl, String cmdKey)
	{
		// Process AID keys
		int sflpos=-1;
		//String cmdKey = (String) request.getParameter("AID");
		if (cmdKey != null)
		{
			IRecordSFL sfl = sflctl.getSfl();
			// Deal with CA/CF keys
			int pos=-1;
			if (cmdKey.compareTo("PAGEDOWN")==0)
			{
				pos = sfl.getTopRRN() + sflctl.getPageSize();
				if (pos < sfl.getCurrentSflSize())
					sflpos = pos;
			}
			else if (cmdKey.compareTo("PAGEUP")==0)
			{
				pos = sfl.getTopRRN() - sflctl.getPageSize();
				if (pos >= 1)
					sflpos = pos;
			}
		}
		return sflpos;
	}

	public void checkTerminated() throws Exception
	{
		if (terminated != null)
			throw (Exception)terminated;
	}

	/**
	 * Save any values on the current page of the subfile.
	 */
	protected void saveSubfileValue(IRecordSFLCTL sflctlrcd, String parm, String value)
	{
		String sflDisplayName = ((RecordWorkstn)sflctlrcd).recordName;
		int j = parm.indexOf("$");
		if (j > 0)
		{
			// The 4.0 WebFacing tooling always prefixed "l1_" to the field name
			//if (parm.substring(0,j).compareTo(sflctlrcd.name) == 0)
			if (parm.substring(3, j).compareTo(sflDisplayName) == 0)
			{
				String fieldName = parm.substring(j + 1);
				// If this field is part of a subfile, then everything after the '$' is the record number
				int k = fieldName.indexOf("$");
				if (k > 0)
				{
					String rrn = fieldName.substring(k + 1);
					// Regenerate the field name so that the rrn value reflects the 'real' rrn and not the
					// rrn relative to the current page (i.e. if the top of the current subfile page is rrn 10, then
					// then first field from the first record on the page would be returned as RCD$FIELD$1 -- translate
					// to RCD$FIELD$10
					int realrrn =
						Integer.parseInt(rrn) + sflctlrcd.getSfl().getTopRRN() - 1;
					fieldName =
						parm.substring(0, j + k + 2) + Integer.toString(realrrn);
					// Insert the scroll value into the parmValues/Names Vector
					int m = parmNames.indexOf(fieldName);
					// If the field isn't already set, then add it...
					if (m < 0)
					{
						parmNames.addElement(fieldName);
						parmValues.addElement(value);
						//parmMap.put(parm, value);
					}
					// ...otherwise, just overwrite what's already there
					else
						parmValues.setElementAt(value, m);
				}
			}
		}
	}
	
}
