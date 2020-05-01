// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.util;


// Referenced classes of package com.ibm.as400ad.webfacing.util:
//            TraceLogger, ITraceLogger

public class TraceLoggerProxy
    implements ITraceLogger
{

    public TraceLoggerProxy(String s)
    {
        _sessionId = s;
    }

    public void dbg(int i, String s)
    {
        try
        {
            getInstance().dbg(i, s);
        }
        catch(Throwable throwable) { }
    }

    public void err(int i, String s)
    {
        try
        {
            getInstance().err(i, s);
        }
        catch(Throwable throwable) { }
    }

    public void err(int i, Throwable throwable)
    {
        try
        {
            getInstance().err(i, throwable);
        }
        catch(Throwable throwable1) { }
    }

    public void err(int i, Throwable throwable, String s)
    {
        try
        {
            getInstance().err(i, throwable, s);
        }
        catch(Throwable throwable1) { }
    }

    public void evt(int i, String s)
    {
        try
        {
            getInstance().evt(i, s);
        }
        catch(Throwable throwable) { }
    }

    private TraceLogger getInstance()
    {
        if(_impl == null)
            _impl = new TraceLogger(_traceDocRoot, _sessionId, true);
        return _impl;
    }

    public void setTraceDocRoot(String s)
    {
        _traceDocRoot = s;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2002.  All Rights Reserved.";
    private transient TraceLogger _impl;
    private String _sessionId;
    private String _traceDocRoot;
}
