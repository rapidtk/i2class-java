// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.util;

import com.ibm.as400ad.webfacing.common.BaseProperties;
import java.io.*;
import java.net.URL;
import java.util.Properties;

public class TraceProperties extends BaseProperties
{

    public TraceProperties()
    {
        initializeToDefaults();
    }

    public TraceProperties(String s)
        throws IOException, FileNotFoundException
    {
        super(s);
    }

    public TraceProperties(URL url)
        throws IOException, FileNotFoundException
    {
        super(url);
    }

    public BaseProperties createNewProperties()
        throws IOException
    {
        return new TraceProperties();
    }

    public String getDebugLevel()
    {
        String s = getProperty("JT_DBG");
        if(s == null || "" == s)
            s = "0";
        return s;
    }

    public String getErrorLevel()
    {
        String s = getProperty("JT_ERR");
        if(s == null || "" == s)
            s = "0";
        return s;
    }

    public String getEventLevel()
    {
        String s = getProperty("JT_EVT");
        if(s == null || "" == s)
            s = "0";
        return s;
    }

    public String getHost()
    {
        String s = getProperty("JT_HOST");
        if(s != null && s.length() <= 0)
            s = null;
        return s;
    }

    public static TraceProperties getInstance(String s)
    {
        TraceProperties traceproperties = null;
        try
        {
            traceproperties = new TraceProperties(s + "tracing.properties");
        }
        catch(IOException ioexception)
        {
            traceproperties = new TraceProperties();
            traceproperties.initializeToDefaults();
        }
        return traceproperties;
    }

    public String getJT_LOG()
    {
        String s = getProperty("JT_LOG");
        if(s == null || s == "")
            s = "0";
        return s;
    }

    public String getMaxLogLevel()
    {
        String s = getProperty("JT_MAXLOG");
        if(s == null || s == "")
            s = "0";
        return s;
    }

    public String getPort()
    {
        String s = getProperty("JT_PORT");
        if(s != null && "" == s)
            s = null;
        return s;
    }

    public static TraceProperties getTraceProperties()
    {
        TraceProperties traceproperties = null;
        try
        {
            traceproperties = new TraceProperties();
            traceproperties.loadFromClassPath("tracing.properties");
        }
        catch(Throwable throwable)
        {
            System.out.println(throwable.toString());
        }
        return traceproperties;
    }

    public void initializeToDefaults()
    {
        setHost(null);
        setPort("8800");
        setErrorLevel("1");
        setEventLevel("0");
        setDebugLevel("0");
        setMaxLogLevel("0");
    }

    public void setDebugLevel(String s)
    {
        if(null == s)
            s = "";
        setPropertyString("JT_DBG", s);
    }

    public void setErrorLevel(String s)
    {
        if(null == s)
            s = "";
        setPropertyString("JT_ERR", s);
    }

    public void setEventLevel(String s)
    {
        if(null == s)
            s = "";
        setPropertyString("JT_EVT", s);
    }

    public void setHost(String s)
    {
        if(null == s)
            s = "";
        setPropertyString("JT_HOST", s);
    }

    public void setJT_LOG(String s)
    {
        if(s == null)
            s = "";
        setPropertyString("JT_LOG", s);
    }

    public void setMaxLogLevel(String s)
    {
        if(s == null)
            s = "";
        setPropertyString("JT_MAXLOG", s);
    }

    public void setPort(String s)
    {
        if(null == s)
            s = "";
        setPropertyString("JT_PORT", s);
    }

    private static final String DEBUG = "JT_DBG";
    private static final String ERROR = "JT_ERR";
    private static final String EVENT = "JT_EVT";
    private static final String HOST = "JT_HOST";
    private static final String LOG = "JT_LOG";
    private static final String MAXLOG = "JT_MAXLOG";
    private static final String PORT = "JT_PORT";
    private static final String FILE_NAME = "tracing.properties";
}
