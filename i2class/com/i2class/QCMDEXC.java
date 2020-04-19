/*
 * Created on Nov 16, 2004
 *
 */
package com.i2class;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

import com.i2class.cmd.*;

/**
 * Execute command (QCMDEXC) API
 * 
 */
public class QCMDEXC extends Application {
	
	private static final Class[] APP_PARM_TYPES={Application.class};
	private static final Class[] PARM_TYPES = {String.class};
	
	public FixedChar cmdString;
	public PackedDecimal cmdLength = new PackedDecimal(15,5);

	public QCMDEXC(Application app) throws Exception {
	   super(app);
	}
	
	public void call(FixedChar cmdString, PackedDecimal cmdLength) throws Exception
	{	this.cmdString=cmdString;
		this.cmdLength=cmdLength;
		runMain();
	}
	public void run() {
	   try {
		  runMain();
	   } catch(Exception e) 
	   {
			I2Logger.logger.printStackTrace(e);
	   }
	}
	public void runMain() throws Pgmmsg
	{
		try
		{
			String s = cmdString.toString().trim()+' ';
			int i = s.indexOf(' ');
			// The first token is the command name
			String cmdName = s.substring(0, i);
			// Convert command name to first letter uppercase, everything else lowercase
			String className = "com.asc.rio.cmd." + cmdName.substring(0, 1).toUpperCase() + cmdName.substring(1).toLowerCase();
			Class cmdClass = Class.forName(className);
			AbstractCommand cmd = (AbstractCommand)cmdClass.newInstance();
			// Pull off parameter names
			int p=0;
			int parmPos=0; // The tenth parameter is the beginning of the 'non-base' methods
			int j=i;
			int sLength=s.length()-1;
			Object[] args = new Object[1];
			String[] methodNames=null;
			// Loop through entire command stirng
			endParm:
			do
			{
				// Pick off current parameter
				String parmName=null;
				// Trim whitespace
				do
				{ 
					j++;
					if (j>=sLength)
						break endParm;
				} while (s.charAt(j)==' ');
				i=j;
				do
				{
					char c = s.charAt(j);
					// If this is a matching closing bracket, then this is the end of the parameter
					if (c==')')
					{
						p--;
						if (p==0)
							break;
					}
					else if (c=='(')
					{
						// If this is the first (, then this is a parameter name
						if (p==0)
						{
							parmName=s.substring(i, j);
							i=j+1;
						}
						p++;
					}
					// Deal with quoted Strings
					else if (c=='\'')
						j = s.indexOf('\'', j)+1;
					// Process last token
					if (c==' ' && p==0)
						break;
					j++;
				} while (j<sLength);
				// Process current parameter
				Method method=null;
				// Deal with positional parameters
				if (parmName==null)
				{
					/*
					if (methods==null)
						methods=cmdClass.getMethods();
					// Find the next setXXX method
					while (parmPos<methods.length)
					{
						Method m = methods[parmPos];
						String methodName = m.getName();
						// Find the next parameter that begins with "set", is not the same as the last "set" parameter, 
						if (methodName.startsWith("set") && methodName.compareTo(lastMethod)!=0 && method.getParameterTypes().equals(PARM_TYPES))
						{
							method=m;
							lastMethod = methodName;
							break;
						}
						parmPos++;
					};
					*/
					if (methodNames==null)
						methodNames = cmd.getParmNames();
					//TODO make sure that the index isn't out of range
					parmName=methodNames[parmPos];
					parmPos++;
					
				}
				// Once a named parametr has been used, positional parameters are not allowed
				else
					parmPos=-1;
					
				// Resolve to method name
				String methodName = "set" + Character.toUpperCase(parmName.charAt(0)) + parmName.substring(1).toLowerCase();
				// Find the method with the specified name and signature(String)
				method = cmdClass.getMethod(methodName, PARM_TYPES);
	
				// Set the parameters for the call
				args[0]=s.substring(i, j);
				// Invoke the method (set the parameter)
				method.invoke(cmd, args);
			} while(true); // :endParm
			// At this point all of the parameters should be set.
			// Set app object
			cmd.setApp(this.prvApp());
			// Invoke exec() function
			cmd.exec();
		}
		catch (Exception e)
		{
			I2Logger.logger.printStackTrace(e);
			// Signal error if parameter not found, etc.
			// CPF0006 -- Errors occurred in command
			throw new Pgmmsg("CPF0006", "QCPFMSG");
		}
	}
}
