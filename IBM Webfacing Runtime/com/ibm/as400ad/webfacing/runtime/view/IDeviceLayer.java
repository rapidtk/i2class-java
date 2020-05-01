// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import java.io.Serializable;
import java.util.Iterator;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            VisibleRectangle

public interface IDeviceLayer
    extends Serializable
{

    public abstract boolean isWindowed();

    public abstract String name();

    public abstract VisibleRectangle getFirst();

    public abstract int getFirstColumn();

    public abstract int getFirstRow();

    public abstract int getLastColumn();

    public abstract Iterator getRecords();

    public abstract Iterator getRectanglesIterator();

    public abstract boolean isFocusCapable();

    public abstract boolean isVerticallyPositioned();

    public abstract boolean isCLRLWindow();

    public abstract String getWindowTitle();

    public abstract String getWindowTitleAlignment();

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
}
