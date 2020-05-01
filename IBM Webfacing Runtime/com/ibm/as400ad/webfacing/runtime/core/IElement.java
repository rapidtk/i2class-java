// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.core;

import java.io.Serializable;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.core:
//            IKey

public interface IElement
    extends Serializable
{

    public abstract IKey getKey();

    public static final String Copyright = "(C) Copyright IBM Corp. 2000, 2002.  All Rights Reserved.";
}
