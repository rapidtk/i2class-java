// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;


// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view.def:
//            ResponseIndicator

public class HLPRTNResponseIndicator extends ResponseIndicator
{

    public HLPRTNResponseIndicator(int i)
    {
        super(i);
        _indExpr = null;
    }

    public HLPRTNResponseIndicator(String s, int i)
    {
        this(i);
        _indExpr = s;
    }

    public String getIndExpr()
    {
        return _indExpr;
    }

    private String _indExpr;
}
