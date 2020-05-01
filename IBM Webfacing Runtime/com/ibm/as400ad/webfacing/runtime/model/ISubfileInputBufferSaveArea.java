// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import java.io.OutputStream;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model:
//            IInputBufferSaveArea, IIndicatorArea

public interface ISubfileInputBufferSaveArea
    extends IInputBufferSaveArea
{

    public abstract IIndicatorArea getResponseIndArea();

    public abstract int getRRN();

    public abstract void writeSubfileInputBuffer(OutputStream outputstream);
}
