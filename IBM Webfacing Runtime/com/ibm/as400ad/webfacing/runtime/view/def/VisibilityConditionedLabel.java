// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;


// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view.def:
//            ConditionedLabel

public class VisibilityConditionedLabel extends ConditionedLabel
{

    public VisibilityConditionedLabel(String s, String s1)
    {
        super(s);
        _fieldID = s1;
    }

    public VisibilityConditionedLabel(String s, String s1, String s2)
    {
        super(s);
        _fieldID = s1;
        if(null != s2 && s2.equalsIgnoreCase("true"))
            super._isDynamic = true;
    }

    public String getFieldID()
    {
        return _fieldID;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2003");
    private String _fieldID;

}
