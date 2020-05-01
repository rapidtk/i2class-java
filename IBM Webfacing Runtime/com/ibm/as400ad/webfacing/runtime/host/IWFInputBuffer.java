// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import java.io.IOException;
import java.io.OutputStream;

public interface IWFInputBuffer
{

    public abstract int length();

    public abstract void toStream(OutputStream outputstream)
        throws IOException;
}
