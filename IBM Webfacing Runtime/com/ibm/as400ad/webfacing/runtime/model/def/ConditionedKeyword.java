// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model.def;

import java.io.Serializable;

public abstract class ConditionedKeyword
    implements Serializable
{

    public ConditionedKeyword()
    {
        _indicatorExpression = null;
    }

    public String getIndicatorExpression()
    {
        return _indicatorExpression == null ? "" : _indicatorExpression;
    }

    public boolean hasIndicatorExpression()
    {
        return _indicatorExpression != null && !"".equals(_indicatorExpression);
    }

    public void setIndicatorExpression(String s)
    {
        _indicatorExpression = s;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    protected String _indicatorExpression;
}
