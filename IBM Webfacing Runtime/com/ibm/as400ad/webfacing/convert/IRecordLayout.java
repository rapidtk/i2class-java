// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert;

import com.ibm.as400ad.code400.dom.RecordNode;
import com.ibm.as400ad.webfacing.convert.model.FieldOutputEnumeration;

public interface IRecordLayout
{

    public abstract FieldOutputEnumeration getAllFields();

    public abstract FieldOutputEnumeration getDisplayableFields();

    public abstract int getDisplaySizeIndex();

    public abstract int getFirstRow();

    public abstract int getHeight();

    public abstract int getLastRow();

    public abstract RecordNode getRecordNode();

    public abstract String getViewInterface();
}
