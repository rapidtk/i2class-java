// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.dev;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;

public class FilterCode
{

    public FilterCode()
    {
    }

    public String filterLine(String s)
    {
        return s;
    }

    public static FilterCode getFilterCodeObj()
    {
        return new FilterCode();
    }

    public String replaceSubString(String s, String s1, String s2)
    {
        return WebfacingConstants.replaceSubstring(s, s1, s2);
    }
}
