// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import java.io.Serializable;

public class IndicatorAndRow
    implements Serializable
{

    public IndicatorAndRow(String s, int i)
    {
        _indExpr = s;
        _rowInSubfile = i;
    }

    public String getIndicatorExpression()
    {
        return _indExpr;
    }

    public int getRowInSubfile()
    {
        return _rowInSubfile;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    private String _indExpr;
    private int _rowInSubfile;
}
