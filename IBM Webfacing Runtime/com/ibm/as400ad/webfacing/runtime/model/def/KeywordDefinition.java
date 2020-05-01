// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model.def;

import com.ibm.as400ad.webfacing.runtime.core.ElementContainer;
import java.util.*;

public class KeywordDefinition extends ElementContainer
{

    public KeywordDefinition(long l)
    {
        super((new Long(l)).toString());
        _indicatorExpression = "";
        _ddsKeyword = l;
    }

    public void addParameter(String s)
    {
        if(null == _params)
            _params = new Vector();
        _params.add(s);
    }

    public String getIndicatorExpression()
    {
        return _indicatorExpression;
    }

    public KeywordDefinition(long l, String s)
    {
        this(l);
        _indicatorExpression = s;
    }

    public Iterator getParameters()
    {
        if(null == _params)
            _params = new Vector();
        return _params.iterator();
    }

    public long getKeywordIdentifier()
    {
        return _ddsKeyword;
    }

    long _ddsKeyword;
    String _indicatorExpression;
    Vector _params;
}
