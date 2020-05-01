// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import java.io.Serializable;

public final class DateType
    implements Serializable
{

    private DateType()
    {
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    public static final DateType SYS_DATE = new DateType();
    public static final DateType JOB_DATE = new DateType();

}
