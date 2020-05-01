// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.core;


// Referenced classes of package com.ibm.as400ad.webfacing.runtime.core:
//            IKey

public class Key
    implements IKey
{

    public Key(String s)
    {
        id = s;
    }

    public String getId()
    {
        return id;
    }

    private String id;
}
