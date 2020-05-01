// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import java.util.ArrayList;

public class FieldVisibility
{

    public FieldVisibility(String s, String s1)
    {
        _fieldVisDefIndex = NOT_INITIALIZED;
        _overlap = new ArrayList();
        _fieldName = s;
        if(s1 == null)
            _indicatorExpression = " ";
        else
            _indicatorExpression = s1.trim();
    }

    private int getFieldVisDefIndex()
    {
        return _fieldVisDefIndex;
    }

    public void buildVisDef(ArrayList arraylist, boolean flag)
    {
        String s = _indicatorExpression;
        ArrayList arraylist1 = new ArrayList();
        for(int i = 0; i < _overlap.size(); i++)
        {
            FieldVisibility fieldvisibility = (FieldVisibility)_overlap.get(i);
            if(fieldvisibility.isAlwaysVisible())
            {
                setNeverVisible();
                if(flag)
                {
                    s = "NEVER";
                    arraylist1.clear();
                } else
                {
                    return;
                }
                break;
            }
            if(fieldvisibility.isConditionallyVisible())
                arraylist1.add(new Integer(fieldvisibility.getFieldVisDefIndex()));
        }

        if(s.equals(" ") && arraylist1.size() == 0)
        {
            setAlwaysVisible();
            return;
        }
        if(arraylist1.size() > 0)
            s = s + ":" + WebfacingConstants.orderedIntListToCondensedStr(arraylist1);
        int j = arraylist.size();
        if(isVisibilityNotInitialized())
            setFieldVisDefIndex(j);
        s = "/* (" + j + ") */ \"" + _fieldName + ":" + s + "\"";
        arraylist.add(s);
    }

    public void addOverlap(FieldVisibility fieldvisibility)
    {
        _overlap.add(fieldvisibility);
    }

    public boolean isAlwaysVisible()
    {
        return _fieldVisDefIndex == ALWAYS_VISIBLE;
    }

    private ArrayList getOverlap()
    {
        return _overlap;
    }

    public boolean isConditionallyVisible()
    {
        return _fieldVisDefIndex >= 0;
    }

    private void setFieldVisDefIndex(int i)
    {
        _fieldVisDefIndex = i;
    }

    private void setNeverVisible()
    {
        _fieldVisDefIndex = NEVER_VISIBLE;
    }

    private boolean isVisibilityNotInitialized()
    {
        return _fieldVisDefIndex == NOT_INITIALIZED;
    }

    private void setAlwaysVisible()
    {
        _fieldVisDefIndex = ALWAYS_VISIBLE;
    }

    public boolean isNeverVisible()
    {
        return _fieldVisDefIndex == NEVER_VISIBLE;
    }

    private static int ALWAYS_VISIBLE = -1;
    private static int NEVER_VISIBLE = -2;
    private static int NOT_INITIALIZED = -3;
    private int _fieldVisDefIndex;
    private ArrayList _overlap;
    private String _fieldName;
    private String _indicatorExpression;

}
