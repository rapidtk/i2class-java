// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import com.ibm.as400ad.webfacing.runtime.fldformat.FieldDataFormatter;
import java.io.DataOutputStream;
import java.io.Serializable;

public interface IFieldData
    extends Serializable
{

    public abstract void toInputBuffer(DataOutputStream dataoutputstream);

    public abstract String toString();

    public abstract void formatAndSetValue(String s);

    public abstract void transformAndSetValue(String s);

    public abstract FieldDataFormatter getFieldDataFormatter();

    public static final String Copyright = "(C) Copyright IBM Corp. 2002.  All Rights Reserved.";
}
