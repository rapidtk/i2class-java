// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;


// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            IBuildRecordViewBean, IDisplaySFLCTLRecord

public interface IBuildSFLCTLViewBean
    extends IBuildRecordViewBean
{

    public abstract boolean enablePageDown();

    public abstract boolean enablePageUp();

    public abstract int getLastRecordNumber();

    public abstract int getNumberOfRecords();

    public abstract int getRRN();

    public abstract int getVisibleRecordSize();

    public abstract IDisplaySFLCTLRecord getDisplaySFLCTLRecord();

    public abstract int getSubfileSize();

    public abstract boolean isSFLENDActive();

    public abstract int getPageSize();

    public abstract boolean isPageEqualToSize();

    public abstract boolean isSFLENDScrollBar();

    public abstract boolean isSFLENDSpecified();

    public abstract boolean isScrollbarShown();

    public abstract int getRecordsInFirstPage();

    public abstract int pageNumberFor(int i);

    public abstract int getPageSizeConsiderFold();
}
