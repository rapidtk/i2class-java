// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import java.io.Serializable;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            IDisplayRecord

public interface IBuildRecordViewBean
    extends Serializable
{

    public abstract IDisplayRecord getDisplayRecord();

    public abstract int getFirstFieldLine();

    public abstract String getJspName();

    public abstract int getLastFieldLine();

    public abstract int getMaxColumn();

    public abstract int getMaxRow();

    public abstract String getRecordName();

    public abstract int getWdwHeight();

    public abstract int getWdwWidth();

    public abstract boolean isWindowed();

    public abstract String getClientScriptJSPName();

    public abstract void setDisplayZIndex(int i);

    public abstract int getDisplayZIndex();

    public abstract long getVersionDigits();

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
}
