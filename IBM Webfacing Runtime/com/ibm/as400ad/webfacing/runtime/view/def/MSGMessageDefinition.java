// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.webfacing.runtime.host.HostJobInfo;
import com.ibm.as400ad.webfacing.runtime.model.IQueryFieldData;
import com.ibm.as400ad.webfacing.runtime.view.IMessageDefinition;
import java.io.Serializable;

public abstract class MSGMessageDefinition
    implements IMessageDefinition, Serializable
{

    public MSGMessageDefinition(String s)
    {
        _responseIndicator = 0;
        _recordName = null;
        _recordLayer = 0;
        _messageText = s;
    }

    public String getIndicatorExpression()
    {
        return _indicatorExpression;
    }

    public String getMessageText()
    {
        return _messageText;
    }

    public String getRecordName()
    {
        return _recordName;
    }

    public int getResponseIndicator()
    {
        return _responseIndicator;
    }

    public boolean hasResponseIndicator()
    {
        return 0 == _responseIndicator;
    }

    public void resolveMessageText(HostJobInfo hostjobinfo, IQueryFieldData iqueryfielddata)
    {
    }

    public void setIndicatorExpression(String s)
    {
        _indicatorExpression = s;
    }

    public void setMessageText(String s)
    {
        _messageText = s;
    }

    public void setRecordName(String s)
    {
        _recordName = s;
    }

    public void setResponseIndicator(int i)
    {
        _responseIndicator = i;
    }

    public int getRecordLayer()
    {
        return _recordLayer;
    }

    public void setRecordLayer(int i)
    {
        _recordLayer = i;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    private String _messageText;
    private int _responseIndicator;
    private String _indicatorExpression;
    private String _recordName;
    private int _recordLayer;
}
