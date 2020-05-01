// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import java.io.Serializable;

public final class CenturyType
    implements Serializable
{

    private CenturyType(int i)
    {
        _digit = 0;
        _digit = i;
    }

    public int getDigits()
    {
        return _digit;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    public static final CenturyType TWO_DIGITS = new CenturyType(0);
    public static final CenturyType FOUR_DIGITS = new CenturyType(1);
    private int _digit;

}
