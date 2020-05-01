// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.util;

import com.ibm.as400ad.code400.dom.ExportSettings;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.util:
//            TranslatableStrings

public class EventLog
{

    EventLog(String s)
    {
        lineCounter = 0;
        opened = false;
        _eventFileDir = null;
        _eventFileName = null;
        _loadMode = false;
        _initialSourceFile = s;
    }

    EventLog(String s, String s1)
    {
        lineCounter = 0;
        opened = false;
        _eventFileDir = null;
        _eventFileName = null;
        _loadMode = false;
        _initialSourceFile = s;
        _eventFileDir = s1;
        _loadMode = true;
    }

    public static void closeEventLog()
    {
        try
        {
            if(singleton != null)
            {
                if(singleton.eventWriter != null)
                {
                    singleton.eventWriter.flush();
                    singleton.eventWriter.close();
                    singleton.eventWriter = null;
                    singleton.eventFile = null;
                    singleton.opened = false;
                }
                singleton = null;
            }
        }
        catch(Exception exception) { }
    }

    protected static int copyFile(BufferedReader bufferedreader, PrintWriter printwriter)
    {
        int i = 0;
        try
        {
            for(String s = bufferedreader.readLine(); s != null;)
            {
                printwriter.println(s);
                s = bufferedreader.readLine();
                i++;
            }

            printwriter.flush();
        }
        catch(Exception exception) { }
        return i;
    }

    protected static int countFileLines(BufferedReader bufferedreader, PrintWriter printwriter)
    {
        int i = 0;
        try
        {
            for(String s = bufferedreader.readLine(); s != null;)
            {
                printwriter.println(s);
                s = bufferedreader.readLine();
                i++;
            }

            printwriter.flush();
        }
        catch(Exception exception) { }
        return i;
    }

    protected static int countFileLines(File file)
    {
        int i = 0;
        try
        {
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            for(String s = bufferedreader.readLine(); s != null;)
            {
                s = bufferedreader.readLine();
                i++;
            }

            bufferedreader.close();
        }
        catch(Exception exception) { }
        return i;
    }

    public File getEventFile()
    {
        return eventFile;
    }

    public String getEventFileName()
    {
        if(_eventFileName == null)
        {
            if(_eventFileDir == null)
                _eventFileDir = ExportSettings.getExportSettings().getRootExportDirectory();
            if(!_eventFileDir.endsWith(File.separator))
                _eventFileName = _eventFileDir + File.separator + "codedsu.evt";
            else
                _eventFileName = _eventFileDir + "codedsu.evt";
        }
        return _eventFileName;
    }

    public static EventLog getEventLog(String s)
    {
        if(singleton == null)
            singleton = new EventLog(s);
        return singleton;
    }

    public static EventLog getEventLog(String s, String s1)
    {
        if(singleton == null)
            singleton = new EventLog(s, s1);
        return singleton;
    }

    protected boolean openEventLog()
        throws IOException
    {
        boolean flag = false;
        if(!opened)
        {
            String s = getEventFileName();
            if(s.length() > 3)
            {
                eventFile = new File(s);
                File file = new File(eventFile.getParent());
                if(!file.exists())
                    file.mkdirs();
                FileOutputStream fileoutputstream = null;
                if(!eventFile.exists())
                {
                    fileoutputstream = new FileOutputStream(eventFile);
                    flag = true;
                } else
                {
                    fileoutputstream = new FileOutputStream(eventFile.getAbsolutePath(), true);
                    flag = false;
                }
                eventWriter = new PrintWriter(fileoutputstream);
                opened = true;
                if(flag)
                    try
                    {
                        writeHeaderLines(_initialSourceFile);
                    }
                    catch(Exception exception) { }
            }
        }
        return flag;
    }

    public void writeHeaderLines(String s)
        throws IOException
    {
        if(!openEventLog())
        {
            Date date = new Date();
            SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat simpledateformat1 = new SimpleDateFormat("HHmmss");
            String s1 = "TIMESTAMP 0 " + simpledateformat.format(date) + simpledateformat1.format(date);
            String s2 = "PROCESSOR 1 0 1";
            String s3 = "FILEID 1 1 0 " + s.length() + " " + s + " " + simpledateformat.format(date) + simpledateformat1.format(date) + " 0";
            writeEventToLog(s1);
            writeEventToLog(s2);
            writeEventToLog(s3);
        }
    }

    protected static boolean searchForFileStatement(File file, String s)
    {
        int i = 0;
        boolean flag = false;
        try
        {
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            for(String s1 = bufferedreader.readLine(); !flag && s1 != null;)
            {
                if(s1.startsWith("FILEID "))
                    flag = s1.indexOf(" " + s + " ") > 0;
                s1 = bufferedreader.readLine();
                i++;
            }

            bufferedreader.close();
        }
        catch(Exception exception) { }
        return flag;
    }

    public void writeEventToLog(String s)
        throws IOException
    {
        openEventLog();
        eventWriter.println(s);
        eventWriter.flush();
    }

    private String loadMessage(String s)
        throws IOException
    {
        String s1;
        try
        {
            s1 = TranslatableStrings.getString(s);
        }
        catch(Throwable throwable)
        {
            throw new IOException("E >>> error obtaining the message string for " + s + " : " + throwable.getMessage());
        }
        return s1;
    }

    private String eventIdToMessageId(int i)
    {
        String s = null;
        String s1 = Integer.toString(i);
        if(s1.length() < 4)
        {
            int j = 4 - s1.length();
            StringBuffer stringbuffer = new StringBuffer();
            for(int k = 0; k < j; k++)
                stringbuffer.append('0');

            stringbuffer.append(s1);
            s = stringbuffer.toString();
        } else
        {
            s = s1;
        }
        String s2 = _loadMode ? "DDS" : "DDX";
        return s2 + s;
    }

    public void logEvent(int i, String as[], int j, String s)
        throws IOException
    {
        IOException ioexception = null;
        String s1 = "ERROR 1 1 1 ";
        String s2 = Integer.toString(j).trim();
        String s3 = eventIdToMessageId(i);
        s1 = s1 + s2 + " " + s2 + " 1 " + s2 + " 80 " + s3;
        String s4;
        try
        {
            s4 = loadMessage(s3);
        }
        catch(IOException ioexception1)
        {
            s4 = ioexception1.getMessage();
            ioexception = ioexception1;
        }
        s1 = s1 + " " + s4.charAt(0) + " 0 0 " + s4.substring(2);
        s1 = replace(s1, "%N", s);
        if(as != null)
        {
            for(int k = 0; k < as.length; k++)
                s1 = replace(s1, "%" + (k + 1), as[k]);

        }
        try
        {
            writeEventToLog(s1);
        }
        catch(IOException ioexception2)
        {
            ioexception = new IOException("Error opening event file :" + (_eventFileName == null ? "" : _eventFileName) + ":");
        }
        if(null != ioexception)
            throw ioexception;
        else
            return;
    }

    public static String replace(String s, String s1, String s2)
    {
        StringBuffer stringbuffer = new StringBuffer();
        int i = 0;
        boolean flag = false;
        for(int j = s.indexOf(s1, i); j != -1; j = s.indexOf(s1, i))
        {
            if(j >= 0)
                stringbuffer.append(s.substring(i, j));
            stringbuffer.append(s2);
            j += s1.length();
            i = j;
        }

        if(i >= 0)
            stringbuffer.append(s.substring(i));
        return stringbuffer.toString();
    }

    public static void replaceEndOfLine(String s)
    {
        s = replace(s, END_OF_LINE, END_OF_LINE_DELIM);
    }

    public static void logException(Throwable throwable, String s)
        throws IOException
    {
        if(null == s)
            s = "";
        StringWriter stringwriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringwriter));
        StringBuffer stringbuffer = stringwriter.getBuffer();
        String as[] = {
            "[" + stringbuffer.toString() + "]"
        };
        replaceEndOfLine(as[0]);
        getEventLog(s).logEvent(DDX0099, as, -1, s);
    }

    private static EventLog singleton = null;
    private File eventFile;
    private PrintWriter eventWriter;
    private int lineCounter;
    private boolean opened;
    private String _eventFileDir;
    private String _eventFileName;
    private String _initialSourceFile;
    private boolean _loadMode;
    public static String END_OF_LINE = "\r\n";
    public static String END_OF_LINE_DELIM = "!!!EOL!!!";
    public static int DDX0099 = 99;

}
