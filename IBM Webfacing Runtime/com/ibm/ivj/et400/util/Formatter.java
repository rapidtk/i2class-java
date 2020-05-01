// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Formatter.java

package com.ibm.ivj.et400.util;

import java.beans.PropertyVetoException;

// Referenced classes of package com.ibm.ivj.et400.util:
//            Attributes

public interface Formatter
{

    public abstract String formatString(String s);

    public abstract Attributes getDataAttributes();

    public abstract void setDataAttributes(Attributes attributes)
        throws PropertyVetoException;

    public static final String copyright = "(C) Copyright IBM Corporation 1997, 2002. All Rights Reserved.";
}
