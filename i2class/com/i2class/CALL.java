/*
 * Created on Nov 18, 2004
 *
 */
package com.i2class;

import java.lang.reflect.*;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;


/**
 * Variable CALL processing.
 * @author ANDREWC
 */
public class CALL extends AbstractCommand {

	public CALL() {}
	public CALL(I2Connection rconn) {
		super(rconn);
		// TODO Auto-generated constructor stub
	}

	private static final Class[] PARM_TYPES = {String[].class};

	private String m_pgm, m_parm;
	private Vector vargs = new Vector();

	static final String[] PARM_NAMES={"PGM", "PARM"};
	/* (non-Javadoc)
	 * @see com.asc.rio.AbstractCommand#getParmNames()
	 */
	protected String[] getParmNames() {
		return PARM_NAMES;
	}

	public void setPgm(fixed pgm) {
		setPgm(pgm.toString());
	}
	public void setPgm(String pgm) {
		m_pgm = qual(pgm);
	}

	public void setParm(String parm) {
		m_parm = parm.toString();
	}
	public void setParm(fixed parm) {
		setParm(parm.toString());
	}
	
	// Add a parameter to the list of available parameters
	public void addParm(FixedData parm)
	{
		vargs.add(parm);
	}
	
	
	/** CALL the specified program. */
	public void exec(String pgm) throws Exception
	{
		setPgm(pgm);
		exec();
	}
	public void exec(fixed pgm) throws Exception
	{
		exec(pgm.toString());
	}

	/** Resolve the Java class that corresponds to a *PGM object name. */
	public static Class resovlePgmClass(Application app, String pgmName) throws ClassNotFoundException
	{
		Package pkg = app.getClass().getPackage();
		String qname;
		if (pkg==null)
			qname=pgmName;
		else
			qname=pkg.getName() + '.' + pgmName;
		return Class.forName(qname);
	}

	/* (non-Javadoc)
	 * @see com.asc.rio.ICommand#exec()
	 */
	public void exec() throws Exception {
		try
		{
			Class pgmClass = resovlePgmClass(getApp(), m_pgm);
			// Pull off parameters
			if (m_parm != null)
			{
				int p=0;
				int j=-1;
				String s = m_parm;
				int sLength=s.length();
				// Loop through entire command string
				endParm:
				while (j<sLength)
				{
					// Remove white space
					do
					{
						j++;
						if (j>=sLength)
							break endParm;
					} while(s.charAt(j)==' ');
					int i=j;
					// Pick off current parameter
					while (j<sLength)
					{
						char c = s.charAt(j);
						// Deal with quoted Strings
						if (c=='\'')
							j = s.indexOf('\'', j+1);
						// Process last token
						else if (c==' ')
							break;
						j++;
					}
					String arg=s.substring(i, j);
					// Strip single (OS/400) quotes from around string
					if (arg.charAt(0)=='\'')
						arg = arg.substring(1, arg.length()-1);
					vargs.add(arg);
				}
			}
			// At this point all of the parameters should be set.
			Method method;
			Object[] args;
			int vsize = vargs.size();
			Application pgmApp;
			// If this is a just a CALL through QCMDEXC (the parameter is one lone string), then use main() method 
			if (m_parm != null)
			{
				pgmApp=null;
				method = pgmClass.getMethod("main", PARM_TYPES);
				args = new Object[1];
				String[] strings = new String[vsize];
				vargs.toArray(strings);
				args[0]=strings;
			}
			// If this is a variable call, then build call() with the specified FixedData types
			else
			{
				// Get instance and set app object
				pgmApp = (Application)pgmClass.newInstance();
				pgmApp.addToCallStack(getApp()); 
				Class[] parmTypes = new Class[vsize];
				for (int i=0; i<vsize; i++)
					parmTypes[i] = vargs.elementAt(i).getClass();
				method = pgmClass.getMethod("call", parmTypes);
				args = new Object[vsize];
				vargs.toArray(args);
			}
			try
			{
				method.invoke(pgmApp, args);
			}
			// Rethrow the target exception
			catch (InvocationTargetException e)
			{
				Throwable e1=e.getTargetException();
				if (e1 instanceof Error)
					throw (Error)e1;
				throw (Exception)e1;
			}
		}
		catch (Exception e)
		{
			I2Logger.logger.severe(e);
			// CPF0001 -- Error found on &1 command
			fixed msgdta = new fixed(10, "CALL");
			throw new Pgmmsg("CPF0001", "QCPFMSG", msgdta);
		}
	}
}
