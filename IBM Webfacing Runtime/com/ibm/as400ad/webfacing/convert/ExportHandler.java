// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert;

import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.webfacing.convert.util.EventLog;
import com.ibm.as400ad.webfacing.util.TraceLogger;
import java.io.File;

// Referenced classes of package com.ibm.as400ad.webfacing.convert:
//            IStatusCallback, WebFaceConverter, IWebFaceConverter, IConversionFactory, 
//            DefaultProvider, Util

public class ExportHandler
{

    public ExportHandler()
    {
    }

    private static void cleanUpTrace()
    {
        if(_trace != null && TraceLogger.JT_LOG)
            _trace.closeLogFile();
        _trace = null;
    }

    public static final void dbg(int i, String s)
    {
        if(_trace != null)
            _trace.dbg(i, s);
    }

    public static final void err(int i, String s)
    {
        if(_trace != null)
            _trace.err(i, s);
    }

    public static final void err(int i, Throwable throwable)
    {
        if(_trace != null)
            _trace.err(i, throwable);
    }

    public static final void err(int i, Throwable throwable, String s)
    {
        if(_trace != null)
            _trace.err(i, throwable, s);
    }

    public static final void evt(int i, String s)
    {
        if(_trace != null)
            _trace.evt(i, s);
    }

    public static int exportInvoker(String as[])
    {
        return exportInvoker(as, (FileNode)null, (IStatusCallback)null);
    }

    public static int exportInvoker(String as[], FileNode filenode)
    {
        return exportInvoker(as, filenode, (IStatusCallback)null);
    }

    public static int exportInvoker(String as[], FileNode filenode, IStatusCallback istatuscallback)
    {
        int i = 1;
        Object obj = null;
        Object obj1 = null;
        caller = istatuscallback;
        initTrace();
        milestoneTimerElapseTime("Beginning of exportInvoker().");
        if(logger == null)
            logger = new Logger();
        logger.logMessage("INSIDE EXPORTINVOKER");
        try
        {
            i = 55;
            String s = new String(as[0]);
            String s1 = new String(as[1]);
            logger.logMessage(" factoryclass = " + s);
            logger.logMessage(" converterclass = " + s1);
            if(null == s || s.length() < 2)
                s = "com.ibm.as400ad.webfacing.convert.ConversionFactory";
            if(null == s1 || s1.length() < 2)
                s1 = "com.ibm.as400ad.webfacing.convert.WebFaceConverter";
            logger.logMessage("Instantiating factory class");
            Object obj2 = DefaultProvider.makeInstance(s, "com.ibm.as400ad.webfacing.convert.ConversionFactory");
            logger.logMessage("Instantiation done for " + (obj2 != null ? obj2.getClass().getName() : "null"));
            logger.logMessage("Instantiating converter class");
            Object obj3 = DefaultProvider.makeInstance(s1, "com.ibm.as400ad.webfacing.convert.WebFaceConverter");
            if(obj3 == null)
                obj3 = new WebFaceConverter();
            logger.logMessage("Instantiation done for " + (obj3 != null ? obj3.getClass().getName() : "null"));
            if(filenode != null)
            {
                i = 95;
                Util.showFrameMessage("Loading DDS into Java memory");
                ElapsedTime elapsedtime = new ElapsedTime();
                elapsedtime.setStartTime();
                com.ibm.as400ad.code400.dom.RecordNodeEnumeration recordnodeenumeration = filenode.getSelectedRecords();
                if(null == recordnodeenumeration)
                {
                    logger.logMessage("WEBFACING BEGIN: ALL RECORDS");
                    i = 94;
                    ((IWebFaceConverter)obj3).convert(filenode, (IConversionFactory)obj2);
                } else
                {
                    logger.logMessage("WEBFACING BEGIN: SELECTED RECORDS");
                    i = 93;
                    ((IWebFaceConverter)obj3).convert(recordnodeenumeration, (IConversionFactory)obj2);
                }
                elapsedtime.setEndTime();
                logger.logMessage("WEBFACING DONE! " + elapsedtime);
            } else
            {
                i = 101;
                logger.logMessage("File node not valid");
            }
        }
        catch(Throwable throwable)
        {
            err(1, throwable);
            i += 1000;
            Util.logThrowableMessage("Exception in exportInvoker", throwable, true);
        }
        finally
        {
            logger.close();
            logger = null;
            EventLog.closeEventLog();
            milestoneTimerElapseTime("End of exportInvoker().");
            cleanUpTrace();
        }
        return i;
    }

    public static Logger getLogger()
    {
        return logger;
    }

    public static IStatusCallback getStatusCallback()
    {
        return caller;
    }

    public static TraceLogger getTrace()
    {
        Util.showDebugMessage("getTrace =" + _trace);
        return _trace;
    }

    private static void initTrace()
    {
        try
        {
            ExportSettings exportsettings = ExportSettings.getExportSettings();
            String s = exportsettings.getTraceDirectory();
            _trace = new TraceLogger(s, "export_trace");
            Util.showDebugMessage("got the tracelogger =" + _trace);
            if(TraceLogger.JT_LOG)
            {
                File file = new File(s + System.getProperty("file.separator"));
                if(!file.isDirectory())
                    file.mkdir();
            }
        }
        catch(Throwable throwable)
        {
            String s1 = "Error in ExportHandler.initTrace()";
            Util.logThrowableMessage(s1, throwable, true);
        }
    }

    public static final void milestoneTimerElapseTime(String s)
    {
    }

    public static void setLogger(Logger logger1)
    {
        logger = logger1;
    }

    public static void setStatusCallback(IStatusCallback istatuscallback)
    {
        caller = istatuscallback;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2000, 2001");
    private static TraceLogger _trace = null;
    static IStatusCallback caller = null;
    static FileNode domFileNode = null;
    static Logger logger = null;
    public static final boolean DBG = false;
    public static final boolean ERR = true;
    public static final boolean EVT = false;

}
