// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import com.ibm.as400ad.code400.dom.constants.FieldType;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingLevelCheckException;
import java.io.Serializable;

public interface IFormattableFieldData
    extends Serializable
{

    public abstract int getFieldLength();

    public abstract String getFieldName();

    public abstract char getKeyboardShift();

    public abstract boolean isInputCapable();

    public abstract void validateDataLength(int i)
        throws WebfacingLevelCheckException;

    public abstract boolean isNumeric();

    public abstract String getCheckAttr();

    public abstract String getTimSep();

    public abstract String getTimFmt();

    public abstract String getDatSep();

    public abstract String getDatFmt();

    public abstract FieldType getDataType();

    public abstract int getDecimalPrecision();

    public static final String Copyright = "(C) Copyright IBM Corp. 2002.  All Rights Reserved.";
}
