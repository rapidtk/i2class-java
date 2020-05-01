// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import com.ibm.as400ad.webfacing.util.TraceLogger;
import java.io.*;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            WebfaceInvoker

public class Logger
{

    public Logger()
    {
        this(getWDTInstallDir() + File.separator + "tmp", "DDSExport.log");
    }

    public Logger(String s, String s1)
    {
        lineCounter = 0;
        logDetail = true;
        logExcToSysOut = false;
        loggingEnabled = true;
        logToSysOut = false;
        openLogFileException = null;
        if(!s.endsWith(File.separator))
            s = s + File.separator;
        logFileName = s + s1;
        logFile = new File(logFileName);
        if(DBG && TraceLogger.getDebugLevel() > 0)
            System.out.println("Logger logFileName = " + logFileName);
        try
        {
            FileOutputStream fileoutputstream = new FileOutputStream(logFile);
            logWriter = new PrintWriter(fileoutputstream);
        }
        catch(Exception exception)
        {
            System.out.println("Error opening log file: " + exception.getClass().getName() + ": " + exception.getMessage());
            openLogFileException = exception;
        }
    }

    public void close()
    {
        if(logWriter != null)
        {
            logWriter.flush();
            logWriter.close();
            logWriter = null;
        }
    }

    public void enableLogDetail(boolean flag)
    {
        logDetail = flag;
    }

    public void enableLogging(boolean flag)
    {
        loggingEnabled = flag;
    }

    public File getLogFile()
    {
        return logFile;
    }

    public PrintWriter getLogFileStream()
    {
        return logWriter;
    }

    public String getMessagePrefix()
    {
        return logPrefix;
    }

    public Exception getOpenLogFileException()
    {
        return openLogFileException;
    }

    public static String getWDTInstallDir()
    {
        if(wdtDir != null)
        {
            return wdtDir;
        } else
        {
            wdtDir = WebfaceInvoker.getWDTInstallDir();
            return wdtDir;
        }
    }

    public static String getWebFacingProjectsDir()
    {
        String s = getWDTInstallDir();
        if(!s.endsWith(File.separator))
            s = s + File.separator;
        String s1 = s + "projects" + File.separator + "WebFacing Projects";
        return s1;
    }

    public void launchCODEBrowse()
    {
        Runtime runtime = Runtime.getRuntime();
        Process process;
        try
        {
            process = runtime.exec("notepad " + logFileName);
        }
        catch(Exception exception)
        {
            logException("Exception in launchCODEBrowse", exception, true);
        }
    }

    public void logDetail(String s)
    {
        if(logDetail)
            logMessage(s);
    }

    public void logErrorMessage(String s)
    {
        if(logExcToSysOut)
            System.out.println(s);
        if(logWriter != null && loggingEnabled)
        {
            if(logPrefix == null)
                logWriter.println(s);
            else
                logWriter.println(logPrefix + " " + s);
            logWriter.flush();
        }
    }

    public void logException(String s, Exception exception, boolean flag)
    {
        logErrorMessage(s + ": " + exception.getClass().getName() + ": " + exception.getMessage());
        if(flag && loggingEnabled && logWriter != null)
        {
            exception.printStackTrace(logWriter);
            logWriter.flush();
            if(logExcToSysOut)
                exception.printStackTrace();
        }
    }

    public void logMessage(String s)
    {
    }

    public void logThrowable(String s, Throwable throwable, boolean flag)
    {
        logErrorMessage(s + ": " + throwable.getClass().getName() + ": " + throwable.getMessage());
        if(flag && loggingEnabled && logWriter != null)
        {
            throwable.printStackTrace(logWriter);
            logWriter.flush();
            if(logExcToSysOut)
                throwable.printStackTrace();
        }
    }

    public void setLogExceptionsOnlyToSysOut(boolean flag)
    {
        logExcToSysOut = flag;
    }

    public void setLogToSysOut(boolean flag)
    {
        logToSysOut = flag;
        logExcToSysOut = flag;
    }

    public void setMessagePrefix(String s)
    {
        logPrefix = s;
    }

    public static boolean DBG = false;
    private static String wdtDir = null;
    private int lineCounter;
    private boolean logDetail;
    private boolean logExcToSysOut;
    private File logFile;
    private String logFileName;
    private String logPrefix;
    private boolean loggingEnabled;
    private boolean logToSysOut;
    private PrintWriter logWriter;
    private Exception openLogFileException;

}
