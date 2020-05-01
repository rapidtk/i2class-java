// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.dds.dom;


// Referenced classes of package com.ibm.etools.dds.dom:
//            IDdsElement, IParmContainer

public interface IDdsKeyword
    extends IDdsElement, IParmContainer
{

    public abstract String getName();

    public abstract int getKeywordId();
}
