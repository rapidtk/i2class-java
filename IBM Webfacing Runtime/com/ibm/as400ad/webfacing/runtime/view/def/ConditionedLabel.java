// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import java.io.Serializable;

public class ConditionedLabel
    implements Serializable
{

    public ConditionedLabel(String s)
    {
        _isDynamic = false;
        _fieldName = null;
        _label = s;
    }

    public String getLabel()
    {
        return _label;
    }

    public boolean isDynamic()
    {
        return _isDynamic;
    }

    public String getFieldName()
    {
        return _fieldName;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2003");
    private String _label;
    protected boolean _isDynamic;
    protected String _fieldName;

}
