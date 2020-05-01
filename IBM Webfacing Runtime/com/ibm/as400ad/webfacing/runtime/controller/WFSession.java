// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.controller;

import com.ibm.as400ad.webfacing.common.MappingProperties;
import com.ibm.as400ad.webfacing.runtime.host.HostJobInfo;
import com.ibm.as400ad.webfacing.runtime.view.IScreenBuilder;
import com.ibm.as400ad.webfacing.util.ITraceLogger;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.controller:
//            WFSessionData

public class WFSession
{

    private WFSession()
    {
    }

    public static void clearSessionData()
    {
        _threadLocal.set(null);
    }

    public static HostJobInfo getJobInfoRequestor()
    {
        return getSessionData().getJobInfoRequestor();
    }

    private static WFSessionData getSessionData()
    {
        WFSessionData wfsessiondata = (WFSessionData)_threadLocal.get();
        if(null == wfsessiondata)
        {
            wfsessiondata = new WFSessionData();
            _threadLocal.set(wfsessiondata);
        }
        return wfsessiondata;
    }

    public static ITraceLogger getTraceLogger()
    {
        return getSessionData().getTraceLogger();
    }

    public static void setJobInfoRequestor(HostJobInfo hostjobinfo)
    {
        getSessionData().setJobInfoRequestor(hostjobinfo);
    }

    public static void setTraceLogger(ITraceLogger itracelogger, String s)
    {
        getSessionData().setTraceLogger(itracelogger, s);
    }

    public static void setScreenBuilderModel(IScreenBuilder iscreenbuilder)
    {
        getSessionData().setScreenBuilderModel(iscreenbuilder);
    }

    public static IScreenBuilder getScreenBuilderModel()
    {
        return getSessionData().getScreenBuilderModel();
    }

    public static void setInputCharMappingProperties(MappingProperties mappingproperties)
    {
        getSessionData().setInputCharMappingProperties(mappingproperties);
    }

    public static void setOutputCharMappingProperties(MappingProperties mappingproperties)
    {
        getSessionData().setOutputCharMappingProperties(mappingproperties);
    }

    public static MappingProperties getInputCharMappingProperties()
    {
        return getSessionData().getInputCharMappingProperties();
    }

    public static MappingProperties getOutputCharMappingProperties()
    {
        return getSessionData().getOutputCharMappingProperties();
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    private static ThreadLocal _threadLocal = new ThreadLocal();

}
