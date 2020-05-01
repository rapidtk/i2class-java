// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.webfacing.runtime.view.def.CommandKeyLabel;
import java.io.Serializable;

public interface ICommandKeyLabel
    extends Serializable
{

    public abstract CommandKeyLabel getLabel();

    public abstract String getKeyLabel();

    public abstract int getPriority();

    public abstract Byte getKeyCode();

    public abstract String getRecordName();

    public abstract String getFieldName();

    public abstract void setRecordName(String s);

    public abstract void setKeyLabel(String s);

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
}
