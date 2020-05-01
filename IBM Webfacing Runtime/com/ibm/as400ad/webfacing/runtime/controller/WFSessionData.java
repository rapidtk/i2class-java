// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.controller;

import com.ibm.as400ad.webfacing.common.MappingProperties;
import com.ibm.as400ad.webfacing.runtime.host.HostJobInfo;
import com.ibm.as400ad.webfacing.runtime.view.IScreenBuilder;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.PrintStream;

class WFSessionData
{

    WFSessionData()
    {
        _trace = null;
        _jobInfo = null;
        _screenModel = null;
        _inputCharMappingProperties = null;
        _outputCharMappingProperties = null;
    }

    void setTraceLogger(ITraceLogger itracelogger, String s)
    {
        _trace = itracelogger;
        if(null != _trace)
            _trace.setTraceDocRoot(s);
        else
            System.out.println("WFSessionData.setTraceLogger - logger in session is null");
    }

    void setJobInfoRequestor(HostJobInfo hostjobinfo)
    {
        _jobInfo = hostjobinfo;
    }

    void setScreenBuilderModel(IScreenBuilder iscreenbuilder)
    {
        _screenModel = iscreenbuilder;
    }

    void setInputCharMappingProperties(MappingProperties mappingproperties)
    {
        _inputCharMappingProperties = mappingproperties;
    }

    void setOutputCharMappingProperties(MappingProperties mappingproperties)
    {
        _outputCharMappingProperties = mappingproperties;
    }

    ITraceLogger getTraceLogger()
    {
        return _trace;
    }

    HostJobInfo getJobInfoRequestor()
    {
        return _jobInfo;
    }

    IScreenBuilder getScreenBuilderModel()
    {
        return _screenModel;
    }

    MappingProperties getInputCharMappingProperties()
    {
        return _inputCharMappingProperties;
    }

    MappingProperties getOutputCharMappingProperties()
    {
        return _outputCharMappingProperties;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    private ITraceLogger _trace;
    private HostJobInfo _jobInfo;
    private IScreenBuilder _screenModel;
    private MappingProperties _inputCharMappingProperties;
    private MappingProperties _outputCharMappingProperties;
}
