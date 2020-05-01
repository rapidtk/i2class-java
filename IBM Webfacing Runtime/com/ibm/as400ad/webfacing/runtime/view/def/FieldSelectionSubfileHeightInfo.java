// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.webfacing.runtime.view.IndicatorAndRow;
import java.io.Serializable;
import java.util.*;

public class FieldSelectionSubfileHeightInfo
    implements Serializable
{

    public FieldSelectionSubfileHeightInfo()
    {
        _minimumHeight = 0;
        _indicatorAndRowVector = new Vector();
    }

    public FieldSelectionSubfileHeightInfo(int i)
    {
        _minimumHeight = 0;
        _indicatorAndRowVector = new Vector();
        _minimumHeight = i;
    }

    public void addIndicatorAndRow(String s, int i)
    {
        _indicatorAndRowVector.addElement(new IndicatorAndRow(s, i));
    }

    public Iterator getIndicatorAndRowIterator()
    {
        return getIndicatorAndRowVector().iterator();
    }

    public Vector getIndicatorAndRowVector()
    {
        return _indicatorAndRowVector;
    }

    public int getMinimumHeight()
    {
        return _minimumHeight;
    }

    public boolean isSubfileFixedHeight()
    {
        return getIndicatorAndRowVector().size() == 0;
    }

    public void setMinimumHeight(int i)
    {
        _minimumHeight = i;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    private int _minimumHeight;
    private Vector _indicatorAndRowVector;
}
