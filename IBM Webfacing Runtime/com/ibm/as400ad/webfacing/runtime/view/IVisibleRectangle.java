// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;


// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            VisibleRectangle, RecordViewBean

public interface IVisibleRectangle
{

    public abstract Object clone();

    public abstract int getFirstFieldLine();

    public abstract int getLastFieldLine();

    public abstract int getStartingLineNumber();

    public abstract boolean isLocatedBefore(VisibleRectangle visiblerectangle);

    public abstract boolean isOverlappedBy(VisibleRectangle visiblerectangle);

    public abstract void removeFromCoveredRecords(RecordViewBean recordviewbean);
}
