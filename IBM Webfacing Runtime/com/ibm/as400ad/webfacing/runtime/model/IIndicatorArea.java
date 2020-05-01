// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model:
//            IIndicatorUpdate, IIndicatorData

public interface IIndicatorArea
    extends IIndicatorUpdate
{

    public abstract void clearResponseIndicators(Iterator iterator);

    public abstract void mergeReferencedRIs(IIndicatorData iindicatordata);

    public abstract void writeIndicatorArea(DataOutputStream dataoutputstream)
        throws IOException;
}
