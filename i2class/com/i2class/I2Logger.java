/*
 * Created on Feb 22, 2006
 *
 */
package com.i2class;

import java.io.*;
import java.util.*;
import java.text.*;

/**
 * A utility class to log I2 messages.
 * @author ANDREWC
 *
 */
public class I2Logger
{
	public final static String[] LEVELS={"All","Debug", "Trace", "Detail", "Config", "Info", "Warning", "Severe","Off"};
	
	static private I2Logger namedLogger;
	public static I2Logger logger = new I2Logger("I2");
	static private SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss,SSS");

	PrintStream psLog;
	PrintStream psError;
	private boolean trace;
	private boolean traceToFile;
	private String m_loggerName;
	private int m_logLevel=Level.INFO;
	
	
	public I2Logger(String loggerName) 
	{
		m_loggerName = loggerName;
		// See if tracing is enabled
		try
		{
			ResourceBundle bundle = Application.getResourceBundle();
			// Calculate logging level
			String logLevel = bundle.getString("I2LogLevel");
			for (int i=0; i<LEVELS.length; i++)
			{
				if (logLevel.compareToIgnoreCase(LEVELS[i])==0)
				{
					m_logLevel = i;
					break;
				}
			}
			
			if (m_logLevel<Level.OFF)
			{
				// See if tracing to a file is enabled 
				String logFile = bundle.getString("I2LogFile");
				if (logFile!=null)
				{
					FileOutputStream fos;
					try
					{
						fos= new FileOutputStream(logFile, true);
						psLog = new PrintStream(fos);
						psError = psLog;
					}
					catch (FileNotFoundException e)
					{
						I2Logger.logger.printStackTrace(e);
					}
				}
			}
		}
		catch (Exception e) {}
		if (psError==null)
		{
			psLog = System.out;
			psError = System.err;
		}
	}
	
	/** Send an informational message. */
	public void info(Object msg)
	{
		log(Level.INFO, msg);
	}

	/** Send a warning message. */
	public void warning(Object msg)
	{
		log(Level.WARNING, msg);
	}

	/** Send a severe message. */
	public void severe(Object msg)
	{
		log(Level.SEVERE, msg);
	}
	
	/** Send a detail message. This is more specific than info, but generally useful. */
	public void detail(Object msg)
	{
		log(Level.FINE, msg);
	}
	
	/** Send a trace message.  These messages trace application flow.  */
	public void trace(Object msg)
	{
		log(Level.FINER, msg);
	}
	
	/** Send a debug message. These are messages only useful to the developer. */
	public void debug(Object msg)
	{
		log(Level.FINEST, msg);
	}
	
	/** Print a stack trace for the specified exception. */
	public void printStackTrace(Throwable e)
	{
		e.printStackTrace(psError);
	}

	/** See if a message of the specified level will be logged. */
	public boolean isLoggable(int level)
	{
		return level>=m_logLevel;
	}
	/** See if a debuggable message will be logged. */
	public boolean isDebuggable()
	{
		return Level.FINEST>=m_logLevel;
	}
	/** See if a traceable message will be logged. */
	public boolean isTraceable()
	{
		return Level.FINER>=m_logLevel;
	}
	/** See if a traceable message will be logged. */
	public boolean isDetailable()
	{
		return Level.FINE>=m_logLevel;
	}
	
	/** Log a message with the specified logging level without checking logging level. */
	void print(int level, Object message)
	{
		String line = sdf.format(new java.util.Date()) + ' ' + m_loggerName + ' ' + LEVELS[level] + ' ' + message;
		PrintStream ps;
		if (level==Level.SEVERE)
			ps = psError;
		else
			ps = psLog;
		ps.println(line);
	}
	
	/** Log a message with the specified logging level. */
	public void log(int level, Object message)
	{
		if (isLoggable(level))
			print(level, message);
	}
		
	/** Get a named logger. */
	public static I2Logger getLogger(String name)
	{
		if (namedLogger==null)
			namedLogger = new I2Logger(name);
		return namedLogger;
	}

}
