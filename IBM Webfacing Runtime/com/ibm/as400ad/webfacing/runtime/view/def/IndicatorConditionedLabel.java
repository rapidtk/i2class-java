// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;


// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view.def:
//            ConditionedLabel

public class IndicatorConditionedLabel extends ConditionedLabel
{

    public IndicatorConditionedLabel(String s, String s1, String s2, String s3)
    {
        super(s);
        _indicatorExpression = s1;
        if(null != s2 && s2.equalsIgnoreCase("true"))
        {
            super._isDynamic = true;
            super._fieldName = s3;
        }
    }

    public String getIndicatorExpression()
    {
        return _indicatorExpression;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2003");
    private String _indicatorExpression;

}
