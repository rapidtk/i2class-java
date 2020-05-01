// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import com.ibm.as400ad.webfacing.runtime.fldformat.FieldDataFormatter;
import java.io.Serializable;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model:
//            IQueryFieldData

public interface IAccessFieldData
    extends IQueryFieldData, Serializable
{

    public abstract void setFieldValue(String s, String s1);

    public abstract FieldDataFormatter getFieldDataFormatter(String s);

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
}
