// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.convert.external;

import com.ibm.as400ad.code400.dom.constants.IFieldType;
import com.ibm.as400ad.code400.dom.constants.IRecordType;

public interface IFieldInput
{

    public abstract String getFieldName();

    public abstract IFieldType getFieldType();

    public abstract String getRecordName();

    public abstract IRecordType getRecordType();

    public abstract String getFieldValue();

    public abstract int getStartColumn();

    public abstract int getEndColumn();

    public abstract int getStartRow();

    public abstract int getEndRow();

    public abstract String getFieldHTML();
}
