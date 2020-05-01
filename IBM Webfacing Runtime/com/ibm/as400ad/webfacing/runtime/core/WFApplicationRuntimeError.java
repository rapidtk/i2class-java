// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.core;


// Referenced classes of package com.ibm.as400ad.webfacing.runtime.core:
//            WFException

public class WFApplicationRuntimeError extends WFException
{

    public WFApplicationRuntimeError(String s, String s1)
    {
        super(s1);
        _msgid = "CPF9999";
        _msgid = s;
    }

    public String getMsgid()
    {
        return _msgid;
    }

    String _msgid;
}
