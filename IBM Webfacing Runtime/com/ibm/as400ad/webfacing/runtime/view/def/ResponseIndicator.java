// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.webfacing.runtime.core.Element;
import com.ibm.as400ad.webfacing.runtime.view.IResponseIndicator;

public class ResponseIndicator extends Element
    implements IResponseIndicator
{

    public ResponseIndicator(int i)
    {
        _responseIndicator = i;
    }

    public int getIndex()
    {
        return _responseIndicator;
    }

    int _responseIndicator;
}
