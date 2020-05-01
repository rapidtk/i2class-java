// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.webfacing.runtime.view.def.XXXMSGIDDefinition;
import java.io.Serializable;

public interface IXXXMSGIDMessageDefinition
    extends Serializable
{

    public abstract XXXMSGIDDefinition getMSGID();

    public abstract void setMessageText(String s);

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
}
