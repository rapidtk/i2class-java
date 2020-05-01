// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.core;


// Referenced classes of package com.ibm.as400ad.webfacing.runtime.core:
//            IAction

public interface ITraverser
{

    public abstract void forAllDo(IAction iaction)
        throws Exception;

    public abstract boolean hasNext();

    public abstract Object next();

    public abstract void reset();
}
