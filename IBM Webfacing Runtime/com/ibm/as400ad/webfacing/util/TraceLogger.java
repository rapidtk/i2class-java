// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.util;

import java.awt.Frame;
import java.awt.Window;
import java.io.*;
import java.net.*;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.util:
//            TraceList, ITraceLogger, TraceProperties

public final class TraceLogger
    implements ITraceLogger
{

    public TraceLogger()
    {
        this("");
    }

    public TraceLogger(String s)
    {
        JT_ENABLE = true;
        clientName = "UNKNOWN_APP";
        traceLogName = clientName + ".txt";
        osLog = null;
        xmlLog = null;
        socket = null;
        osOut = System.out;
        osErr = System.err;
        _traceList = null;
        if(s != null && !s.equals(""))
            clientName = s;
        active = initialize();
    }

    public TraceLogger(String s, String s1)
    {
        this(s, s1, false);
    }

    public TraceLogger(String s, String s1, boolean flag)
    {
        JT_ENABLE = true;
        clientName = "UNKNOWN_APP";
        traceLogName = clientName + ".txt";
        osLog = null;
        xmlLog = null;
        socket = null;
        osOut = System.out;
        osErr = System.err;
        _traceList = null;
        _traceDocRoot = s;
        _fileName = s + System.getProperty("file.separator") + s1 + ".log";
        if(s1 != null && !s1.equals(""))
            clientName = s1;
        active = initialize();
        if(JT_LOG && flag)
        {
            _traceList = new TraceList(s);
            File file = new File(s);
            if(!file.isDirectory())
                file.mkdir();
        }
    }

    public final void closeLogFile()
    {
        if(JT_LOG && null != osLog)
        {
            osLog.flush();
            osLog.close();
            osLog = null;
        }
    }

    private final void closeXml()
    {
        xmlLog.println(" </logrecv> ");
        xmlLog.println("</logfile> ");
        xmlLog.flush();
        xmlLog.close();
    }

    public final void dbg(int i, String s)
    {
        if(i <= debugLevel && JT_ENABLE)
        {
            String s1 = formatMsg(clientName, "dbg", i, s);
            if(active)
                osOut.println(s1);
            if(JT_LOG)
                osLogPrintln(s1);
        }
    }

    private static final void displayPopup(Frame frame, String s)
    {
        frame.show();
        frame.setTitle(s);
        try
        {
            Thread.sleep(5000L);
        }
        catch(InterruptedException interruptedexception) { }
    }

    public final void err(int i, String s)
    {
        err(i, s, ((Frame) (null)));
    }

    public final void err(int i, String s, Frame frame)
    {
        String s1 = formatMsg(clientName, "ERR", i, s);
        if(i <= errorLevel)
        {
            if(active)
                osErr.println(s1);
            if(active)
                osErr.flush();
            if(frame != null)
                displayPopup(frame, s1);
        }
        if(JT_LOG)
            osLogPrintln(s1);
    }

    public final void err(int i, Throwable throwable)
    {
        err(i, throwable, throwable.toString());
    }

    public final void err(int i, Throwable throwable, String s)
    {
        String s1 = formatMsg(clientName, "ERR", i, s);
        if(i <= errorLevel && active)
        {
            osErr.println(s1);
            throwable.printStackTrace(osErr);
            osErr.flush();
        }
        if(JT_LOG)
        {
            osLogPrintln(s1);
            if(null != getosLog())
                throwable.printStackTrace(getosLog());
        }
    }

    public final void evt(int i, String s)
    {
        if(i <= eventLevel && JT_ENABLE)
        {
            String s1 = formatMsg(clientName, "evt", i, s);
            if(active)
                osOut.println(s1);
            if(JT_LOG)
                osLogPrintln(s1);
        }
    }

    protected final void finalize()
    {
        flush();
        if(socket != null)
            try
            {
                socket.close();
            }
            catch(IOException ioexception) { }
            catch(Exception exception)
            {
                handleException(exception);
            }
        if(!active);
    }

    public final void flush()
    {
        if(xmlLog != null)
            closeXml();
        if(active)
        {
            System.out.flush();
            try
            {
                Thread.sleep(1000L);
            }
            catch(InterruptedException interruptedexception) { }
        }
    }

    private final String formatMsg(String s, String s1, int i, String s2)
    {
        String s3 = s1 + "[" + i + "]: " + s;
        if(JT_THREAD)
            s3 = s3 + "[" + Thread.currentThread().getName() + "]";
        s3 = s3 + ": " + s2;
        if(xmlLog != null)
            formatXml(s, s1, i, s2);
        return s3;
    }

    private final void formatXml(String s, String s1, int i, String s2)
    {
        int j = s2.indexOf('<');
        if(j >= 0)
            if(j > 0)
                s2 = s2.substring(0, j - 1) + "&lt;";
            else
                s2 = s2.substring(1) + "&lt;";
        j = s2.indexOf('>');
        if(j >= 0)
            s2 = s2.substring(j) + "&gt;";
        xmlLog.println("    <logrec processid=\"" + s + "\" threadid=\"" + Thread.currentThread().getName() + "\" category=\"" + s1 + "\" severity=\"" + i + "\" extendedmessage=\"" + s2 + "\" >  </logrec> ");
    }

    public static final int getDebugLevel()
    {
        return debugLevel;
    }

    public static final int getErrorLevel()
    {
        return errorLevel;
    }

    public static final int getEventLevel()
    {
        return eventLevel;
    }

    public static final int getMaxLogLevel()
    {
        return maxlogLevel;
    }

    public final int getMessageLevel()
    {
        return messageLevel;
    }

    private PrintStream getosLog()
    {
        if(null == osLog)
            openLogFile();
        return osLog;
    }

    private static String getResourceString(String s)
    {
        try
        {
            return _messagesBundle.getString(s);
        }
        catch(MissingResourceException missingresourceexception)
        {
            System.out.println("Error: Resource " + s + " not found");
            return "Error: Resource " + s + " not found";
        }
        catch(NullPointerException nullpointerexception)
        {
            System.out.println("Error: Cannot get resource.  Message bundle not initialized.");
        }
        return "Error: Cannot get resource.  Message bundle not initialized.";
    }

    private final void handleException(Exception exception)
    {
        osErr.println(exception.toString());
        osErr.println(exception.getMessage());
        exception.printStackTrace(osErr);
    }

    private final void init_Debug_Event_Error_Levels()
    {
        TraceProperties traceproperties = TraceProperties.getTraceProperties();
        String s = System.getProperty("JT_HOST");
        if(s == null)
            s = traceproperties.getHost();
        if(s != null)
        {
            if(!s.equals(""))
                hostName = s;
            traceCatcher = true;
        } else
        {
            traceCatcher = false;
        }
        s = System.getProperty("JT_PORT");
        if(s == null)
            s = traceproperties.getPort();
        if(s != null && !s.equals(""))
            try
            {
                tracePort = Integer.parseInt(s);
            }
            catch(NumberFormatException numberformatexception)
            {
                showError("INVALID_JT_PORT", s);
            }
        s = System.getProperty("JT_LOG");
        if(s == null)
            s = traceproperties.getJT_LOG();
        if(s != null)
        {
            int i = 0;
            try
            {
                i = Integer.parseInt(s);
            }
            catch(NumberFormatException numberformatexception7)
            {
                showError("INVALID_MESSG_LEVEL", s);
            }
            if(i > 0)
                JT_LOG = true;
        }
        s = System.getProperty("JT_XML");
        if(s != null)
            JT_XML = true;
        s = System.getProperty("JT_THREAD");
        if(s != null)
            JT_THREAD = true;
        s = System.getProperty("JT_MSG");
        if(s != null)
            try
            {
                messageLevel = Integer.parseInt(s);
            }
            catch(NumberFormatException numberformatexception1)
            {
                showError("INVALID_MESSG_LEVEL", s);
            }
        s = System.getProperty("JT_DBG");
        if(s == null)
            s = traceproperties.getDebugLevel();
        if(s != null)
            try
            {
                debugLevel = Integer.parseInt(s);
            }
            catch(NumberFormatException numberformatexception2)
            {
                showError("INVALID_DEBUG_LEVEL", s);
            }
        s = System.getProperty("JT_EVT");
        if(s == null)
            s = traceproperties.getEventLevel();
        if(s != null)
            try
            {
                eventLevel = Integer.parseInt(s);
            }
            catch(NumberFormatException numberformatexception3)
            {
                showError("INVALID_EVENT_LEVEL", s);
            }
        s = System.getProperty("JT_ERR");
        if(s == null)
            s = traceproperties.getErrorLevel();
        if(s != null)
            try
            {
                errorLevel = Integer.parseInt(s);
            }
            catch(NumberFormatException numberformatexception4)
            {
                showError("INVALID_ERROR_LEVEL", s);
            }
        s = System.getProperty("JT_MAXLOG");
        if(s == null)
            s = traceproperties.getMaxLogLevel();
        if(s != null)
            try
            {
                maxlogLevel = Integer.parseInt(s);
            }
            catch(NumberFormatException numberformatexception5)
            {
                showError("INVALID_MAXLOG_LEVEL", s);
            }
        s = System.getProperty("JT_OUT");
        if(s != null)
            try
            {
                outLevel = Integer.parseInt(s);
            }
            catch(NumberFormatException numberformatexception6)
            {
                showError("INVALID_OUT_LEVEL", s);
            }
        s = System.getProperty("JT_TIME");
        if(s != null)
        {
            timings = true;
        } else
        {
            s = System.getProperty("JT_NOTIME");
            if(s != null)
                timings = false;
            else
                timings = debugLevel > 0;
        }
        s = System.getProperty("JT_ENABLE");
        if(s != null)
            if(s.equals("") || s.equalsIgnoreCase("all") || s.equalsIgnoreCase(clientName))
                JT_ENABLE = true;
            else
                JT_ENABLE = false;
    }

    private static final void init_Initialization(String s)
    {
        try
        {
            _messagesBundle = ResourceBundle.getBundle("com.ibm.as400ad.webfacing.util.MessageStrings");
        }
        catch(MissingResourceException missingresourceexception)
        {
            System.out.println("No Resource file for language=\"" + language + "\" country=\"" + country + "\" variant=\"" + variant + "\"");
        }
    }

    private final boolean initialize()
    {
        init_Initialization(language);
        init_Debug_Event_Error_Levels();
        if(JT_XML)
        {
            String s = clientName + ".xml";
            try
            {
                xmlLog = new PrintStream(new FileOutputStream(s));
                initXml();
            }
            catch(Exception exception)
            {
                showError("CANNOT_OPEN_XML_FILE", s);
            }
        }
        if(traceCatcher)
        {
            if(tracePort < 8000 || tracePort > 8999)
                showError("INVALID_PORT", Integer.toString(tracePort));
            try
            {
                socket = new Socket(hostName, tracePort);
            }
            catch(UnknownHostException unknownhostexception)
            {
                showError("UNKNOWN_HOSTNAME_MSG", hostName);
                return false;
            }
            catch(BindException bindexception)
            {
                showError("CONNECT_BUSY_MSG", hostName);
                return false;
            }
            catch(ConnectException connectexception)
            {
                showError("CONNECT_FAILED_MSG", null);
                return false;
            }
            catch(Exception exception1)
            {
                handleException(exception1);
                return false;
            }
            try
            {
                PrintStream printstream = new PrintStream(new BufferedOutputStream(socket.getOutputStream()), true);
                osOut = printstream;
                osErr = printstream;
                String s1 = System.getProperty("JT_SHOWOUT");
                if(s1 == null)
                    System.setOut(osOut);
                s1 = System.getProperty("JT_SHOWERR");
                if(s1 == null)
                    System.setErr(osErr);
            }
            catch(IOException ioexception)
            {
                showError("UNABLE_TO_ASSIGN_SYSTEM_OUT_TO_SOCKET", null);
                return false;
            }
        }
        return true;
    }

    private final void initXml()
    {
        String s = System.getProperty("user.name");
        String s1 = "unknownHostName";
        try
        {
            InetAddress inetaddress = InetAddress.getLocalHost();
            String s2 = inetaddress.getHostName();
        }
        catch(UnknownHostException unknownhostexception) { }
        GregorianCalendar gregoriancalendar = new GregorianCalendar();
        String s3 = gregoriancalendar.get(1) + "-" + gregoriancalendar.get(2) + "-" + gregoriancalendar.get(5);
        String s4 = gregoriancalendar.get(11) + ":" + gregoriancalendar.get(12) + ":" + gregoriancalendar.get(13) + "." + gregoriancalendar.get(14);
        xmlLog.println("<?xml version=\"1.0\"?> ");
        xmlLog.println("<!DOCTYPE logfile SYSTEM \"dbglog.dtd\"> ");
        xmlLog.println("<logfile manufacturer=\"IBM\" product=\"Distributed Debugger\" version=\"7.0\" ");
        xmlLog.println("         clientuserid=\"" + s + "\"  clienthostname=\"searle\" ");
        xmlLog.println("         date=\"" + s3 + "\" time=\"" + s4 + "\" xmlfilename=\"" + clientName + ".xml\"  > ");
        xmlLog.println("  <logrecv> ");
    }

    public final void milestoneTimerElapseTime(String s)
    {
        if(eventLevel > 0 && timings && milestoneTimer != -1L)
        {
            long l = System.currentTimeMillis();
            String s1 = (l - milestoneTimer) + " ";
            String s2 = "        ";
            int i = s1.length();
            if(i < 0)
                i = 0;
            else
            if(i > 8)
                i = 8;
            s1 = s2.substring(i) + s1;
            if(JT_THREAD)
                s1 = s1 + " *** Milestone " + clientName + "[" + Thread.currentThread().getName() + "]: " + s + " *** " + l;
            else
                s1 = s1 + " *** Milestone " + clientName + ": " + s + " *** " + l;
            if(active)
                osOut.println(s1);
            if(JT_LOG)
                osLogPrintln(s1);
        }
    }

    public final void milestoneTimerElapseTimeAndStop(String s)
    {
        milestoneTimerElapseTime(s);
        milestoneTimerStop();
    }

    public final void milestoneTimerStart()
    {
        if(timings)
            milestoneTimer = System.currentTimeMillis();
    }

    public final void milestoneTimerStart(String s)
    {
        if(timings)
        {
            milestoneTimer = System.currentTimeMillis();
            milestoneTimerElapseTime(s);
        }
    }

    public final void milestoneTimerStop()
    {
        if(timings)
            milestoneTimer = -1L;
    }

    private final void minorTimerElapseTime()
    {
        if(timings)
        {
            String s = (System.currentTimeMillis() - minorTimer) + " ";
            String s1 = "        ";
            int i = s.length();
            if(i < 0)
                i = 0;
            else
            if(i > 8)
                i = 8;
            s = s1.substring(i) + s;
            if(active && null != osOut)
                osOut.print(s);
            if(JT_LOG)
                osLogPrint(s);
        }
    }

    private final void minorTimerReset()
    {
        if(timings)
            minorTimer = System.currentTimeMillis();
    }

    public final void msg(int i, String s)
    {
        if(i <= messageLevel)
            if(active)
                osOut.println(s);
            else
                System.out.println(s);
        if(JT_LOG)
            osLogPrintln(s);
    }

    private void openLogFile()
    {
        if(JT_LOG)
            try
            {
                String s = _fileName.substring(0, _fileName.lastIndexOf(System.getProperty("file.separator")));
                File file = new File(s);
                if(!file.exists())
                    try
                    {
                        file.mkdirs();
                    }
                    catch(Exception exception1)
                    {
                        osErr.println("JT_LOGGER: Failed to open trace log directory :" + s + " due to exception " + exception1);
                        exception1.printStackTrace(osErr);
                    }
                osLog = new PrintStream(new FileOutputStream(_fileName, true));
                if(null != _traceList)
                    _traceList.appendSession(clientName);
            }
            catch(Exception exception)
            {
                osErr.println("JT_LOGGER: Failed to open trace file:" + _fileName + " due to exception " + exception);
                showError("CANNOT_OPEN_LOG_FILE", _fileName);
            }
    }

    private void osLogPrint(String s)
    {
        PrintStream printstream = getosLog();
        if(null != printstream)
            printstream.print(s);
    }

    private void osLogPrintln(String s)
    {
        PrintStream printstream = getosLog();
        if(null != printstream)
            printstream.println(s);
    }

    public final void out(int i, String s)
    {
        if(i <= outLevel)
        {
            String s1 = s;
            if(active)
                osOut.print(s1);
            else
                System.out.print(s1);
        }
        if(JT_LOG)
            osLogPrint(s);
    }

    public final void setDebugLevel(int i)
    {
        debugLevel = i;
    }

    public final void setErrorLevel(int i)
    {
        errorLevel = i;
    }

    public final void setEventLevel(int i)
    {
        eventLevel = i;
    }

    public final void setMessageLevel(int i)
    {
        messageLevel = i;
    }

    public void setModelTrace(boolean flag)
    {
        java.util.Properties properties = System.getProperties();
        String s = "JT_MODEL";
        String s1 = (String)properties.get(s);
        if(flag)
            s1 = (String)properties.put(s, "1");
        else
            properties.remove(s);
        System.setProperties(properties);
        String s2 = (String)properties.get(s);
    }

    public final void setThread(boolean flag)
    {
        JT_THREAD = flag;
    }

    public final void setTimings(boolean flag)
    {
        timings = flag;
    }

    public void setTraceDocRoot(String s)
    {
        _traceDocRoot = s;
    }

    private final void showError(String s, String s1)
    {
        String s2 = getResourceString("JT_LOGGER") + getResourceString(s);
        if(s1 != null)
            s2 = s2 + " " + s1;
        osErr.println(s2);
    }

    private final void showMessage(String s, String s1)
    {
        String s2 = getResourceString("JT_LOGGER") + getResourceString(s);
        if(s1 != null)
            s2 = s2 + " " + s1;
        osOut.println(s2);
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2000, 2002  All Rights Reserved.";
    private static ResourceBundle _messagesBundle = null;
    private static String language = "";
    private static String country = "";
    private static String variant = "";
    private static boolean errorPopup = true;
    public static boolean JT_LOG = false;
    private boolean JT_ENABLE;
    private static boolean JT_XML = false;
    private static boolean JT_THREAD = false;
    private static boolean traceCatcher = false;
    private static boolean active = true;
    private String clientName;
    private static String hostName = "localhost";
    protected String traceLogName;
    public static final int JT_PORT = 8800;
    public static int tracePort = 8800;
    private PrintStream osLog;
    private PrintStream xmlLog;
    private Socket socket;
    private PrintStream osOut;
    private PrintStream osErr;
    private final long milestoneTimerStopped = -1L;
    private static long milestoneTimer = System.currentTimeMillis();
    private static long minorTimer = System.currentTimeMillis();
    private static int messageLevel = 1;
    private static int outLevel = 1;
    private static int errorLevel = 1;
    private static int eventLevel = 0;
    private static int debugLevel = 0;
    private static boolean timings = false;
    public static final boolean MSG = true;
    public static final boolean OUT = true;
    public static final boolean ERR = true;
    public static final boolean EVT = false;
    public static final boolean DBG = false;
    private String _fileName;
    private String _traceDocRoot;
    private static int maxlogLevel = 5;
    private TraceList _traceList;

}
