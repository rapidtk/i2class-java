// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import java.io.Serializable;

public interface IIndicatorUpdate
    extends Serializable
{

    public abstract void setIndicator(int i, boolean flag);

    public static final String Copyright = "(C) Copyright IBM Corp. 2000, 2002.  All Rights Reserved.";
}
