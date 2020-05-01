// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import com.ibm.as400ad.webfacing.runtime.controller.ILibraryFile;
import com.ibm.as400ad.webfacing.runtime.fldformat.FieldDataFormatter;
import com.ibm.as400ad.webfacing.runtime.model.def.FieldDataDefinition;
import com.ibm.as400ad.webfacing.runtime.model.def.IRecordDataDefinition;
import java.io.Serializable;
import java.util.Map;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model:
//            IInputBufferSaveArea, IIndicatorArea, IIndicatorEvaluation, IIndicatorData, 
//            IIndicatorValue

public interface IRecordData
    extends Serializable
{

    public abstract Object clone();

    public abstract ILibraryFile getDSPFObject();

    public abstract FieldDataDefinition getFieldDataDefinition(String s);

    public abstract Map getFieldValues();

    public abstract IInputBufferSaveArea getInputBufferSaveArea();

    public abstract IRecordDataDefinition getRecordDataDefinition();

    public abstract int getSLNO();

    public abstract String getSourceQualifiedRecordName();

    public abstract void setFieldValue(String s, String s1);

    public abstract void setHasBeenMapped(boolean flag);

    public abstract void prepareForRead();

    public abstract IIndicatorArea createSeparateIndicatorArea();

    public abstract IIndicatorEvaluation getOptionIndEval();

    public abstract IIndicatorData getResponseIndData();

    public abstract void initOptionIndsFrom(IIndicatorValue iindicatorvalue);

    public abstract boolean isDspfDbcsCapable();

    public abstract int getJobCCSID();

    public abstract String getFieldValue(String s);

    public abstract void setDefaultFieldValue(String s, String s1);

    public abstract void updateFieldValue(String s, String s1);

    public abstract long getVersionDigits();

    public abstract FieldDataFormatter getFieldDataFormatter(String s);

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
}
