// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model.def;

import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.util.*;

public class IndicatorDataDefinition
    implements IElement
{

    public IndicatorDataDefinition()
    {
        _optionIndicators = null;
        _responseIndicators = null;
        _AIDResponseIndicators = null;
        _optionIndicators = new Vector();
        _responseIndicators = new Vector();
        _AIDResponseIndicators = new Vector();
    }

    public void addReferencedOptionIndicator(int i)
    {
        if(i < 1 || i > 99)
            WFSession.getTraceLogger().err(1, "THIS SHOULD NEVER HAPPEN: Invalid indicator " + i + " specified as referenced option indicator.");
        else
            _optionIndicators.add(new Integer(i));
    }

    public void addReferencedResponseIndicator(int i, boolean flag)
    {
        if(i < 1 || i > 99)
        {
            WFSession.getTraceLogger().err(1, "THIS SHOULD NEVER HAPPEN: Invalid indicator " + i + " specified as referenced response indicator.");
        } else
        {
            _responseIndicators.add(new Integer(i));
            if(flag)
                _AIDResponseIndicators.add(new Integer(i));
        }
    }

    public IKey getKey()
    {
        return _key;
    }

    public Iterator getReferencedAIDResponseIndicators()
    {
        return _AIDResponseIndicators.iterator();
    }

    public List getReferencedOptionIndicators()
    {
        return _optionIndicators;
    }

    public List getReferencedResponseIndicatorList()
    {
        return _responseIndicators;
    }

    public Iterator getReferencedResponseIndicators()
    {
        return _responseIndicators.iterator();
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2000, 2002.  All Rights Reserved.";
    private static final Key _key = new Key("IndicatorDataDefinition");
    private Vector _optionIndicators;
    private Vector _responseIndicators;
    private Vector _AIDResponseIndicators;

}
