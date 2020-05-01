// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.webfacing.runtime.model.IFormattableFieldData;
import com.ibm.ivj.et400.util.EditcodeEditwordFormatter;

public interface IFormattableFieldView
{

    public abstract int getFieldLength();

    public abstract boolean isNumeric();

    public abstract int getDecimalPrecision();

    public abstract IFormattableFieldData getFormattableDataDefinition();

    public abstract char getEditCode();

    public abstract char getEditCodeParm();

    public abstract EditcodeEditwordFormatter getEditFormatter();

    public abstract String getEditWord();

    public abstract boolean hasEditCodeEditWord();

    public abstract boolean isOutputOnly();

    public abstract boolean isInputOnly();

    public abstract boolean hasDefaultValue();

    public abstract void setEditFormatter(EditcodeEditwordFormatter editcodeeditwordformatter);

    public abstract String getDecimalFormattedString(StringBuffer stringbuffer);

    public static final String Copyright = "(C) Copyright IBM Corp. 2002.  All Rights Reserved.";
}
