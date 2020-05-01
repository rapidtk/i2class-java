// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import java.io.Serializable;

public final class SeparatorType
    implements Serializable
{

    private SeparatorType(int i)
    {
        _separator = 0;
        _separator = i;
    }

    public int getSeparator()
    {
        return _separator;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    public static final SeparatorType NO_SEPARATOR = new SeparatorType(0);
    public static final SeparatorType HAS_SEPARATOR = new SeparatorType(1);
    private int _separator;

}
