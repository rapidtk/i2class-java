// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;


// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            IDisplayRecord, DisplayAttributeBean

public interface IDisplaySFLCTLRecord
    extends IDisplayRecord
{

    public abstract boolean enablePageDown();

    public abstract boolean enablePageUp();

    public abstract boolean evaluateIndicatorExpression(int i, String s);

    public abstract String evaluateStyleClass(int i, DisplayAttributeBean displayattributebean);

    public abstract String getFieldValue(String s, int i);

    public abstract String getFieldValueWithTransform(String s, int i, int j);

    public abstract int getLastRecordNumber();

    public abstract int getNumberOfRecords();

    public abstract int getRRN();

    public abstract int getVisibleRecordSize();

    public abstract boolean isActiveRecord(int i);

    public abstract boolean isRecordPastEndOfSubfile(int i);

    public abstract boolean isRowOfSubfileRecordDisplayed(int i, int j);

    public abstract boolean isSubfileControlVisible();

    public abstract boolean isSubfileFolded();

    public abstract boolean isSubfileVisible();

    public abstract int getSubfileAreaFirstRow();

    public abstract int getSubfileAreaHeight();

    public abstract boolean isScrollbarShown();

    public abstract boolean isFieldVisible(int i, String s);

    public abstract boolean isMDTOn(int i, String s);

    public abstract String getFieldValueWithTransform(String s, int i, int j, int k, int l);

    public abstract String getSubfileValuesAfterEditing(String s);
}
