// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.util;

import com.ibm.etools.iseries.webfacing.tags.def.ILogger;
import java.io.Serializable;

public interface ITraceLogger
    extends Serializable, ILogger
{

    public abstract void dbg(int i, String s);

    public abstract void err(int i, String s);

    public abstract void err(int i, Throwable throwable);

    public abstract void err(int i, Throwable throwable, String s);

    public abstract void evt(int i, String s);

    public abstract void setTraceDocRoot(String s);

    public static final String Copyright = "(C) Copyright IBM Corp. 2002.  All Rights Reserved.";
}
