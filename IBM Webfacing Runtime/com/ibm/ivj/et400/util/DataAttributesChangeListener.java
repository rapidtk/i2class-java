// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DataAttributesChangeListener.java

package com.ibm.ivj.et400.util;

import java.util.EventListener;

// Referenced classes of package com.ibm.ivj.et400.util:
//            DataAttributesChangeEvent

public interface DataAttributesChangeListener
    extends EventListener
{

    public abstract void dataAttributesChanged(DataAttributesChangeEvent dataattributeschangeevent);

    public static final String copyright = "(C) Copyright IBM Corporation 1997, 2002. All Rights Reserved.";
}
