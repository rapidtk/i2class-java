// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.dhtmlview;

import com.ibm.as400ad.webfacing.runtime.view.*;
import com.ibm.as400ad.webfacing.runtime.view.def.FieldViewDefinition;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.dhtmlview:
//            DhtmlViewBean

public class DhtmlSubfileControlViewBean extends DhtmlViewBean
    implements IDisplaySFLCTLRecord
{

    public DhtmlSubfileControlViewBean(SubfileControlRecordViewBean subfilecontrolrecordviewbean)
    {
        super(subfilecontrolrecordviewbean);
    }

    public boolean enablePageDown()
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).enablePageDown();
    }

    public boolean enablePageUp()
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).enablePageUp();
    }

    public boolean evaluateIndicatorExpression(int i, String s)
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).evaluateIndicatorExpression(i, s);
    }

    public String evaluateStyleClass(int i, DisplayAttributeBean displayattributebean)
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).evaluateStyleClass(i, displayattributebean);
    }

    public String getFieldValue(String s, int i)
    {
        SubfileControlRecordViewBean subfilecontrolrecordviewbean = (SubfileControlRecordViewBean)super._recordViewBean;
        String s1 = subfilecontrolrecordviewbean.getFieldValue(s, i);
        FieldViewDefinition fieldviewdefinition = subfilecontrolrecordviewbean.getSubfileFieldViewDefinition(s);
        if(fieldviewdefinition != null && fieldviewdefinition.isMasked())
            s1 = applyFieldMasking(s1, fieldviewdefinition.getStartMaskingAt(), fieldviewdefinition.getEndMaskingAt());
        return s1;
    }

    public String getFieldValueWithTransform(String s, int i, int j)
    {
        return applyFieldValueTransform(getFieldValue(s, i), j);
    }

    public int getLastRecordNumber()
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).getLastRecordNumber();
    }

    public int getNumberOfRecords()
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).getNumberOfRecords();
    }

    public int getRRN()
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).getRRN();
    }

    public int getVisibleRecordSize()
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).getVisibleRecordSize();
    }

    public boolean isActiveRecord(int i)
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).isActiveRecord(i);
    }

    public boolean isRecordPastEndOfSubfile(int i)
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).isRecordPastEndOfSubfile(i);
    }

    public boolean isRowOfSubfileRecordDisplayed(int i, int j)
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).isRowOfSubfileRecordDisplayed(i, j);
    }

    public boolean isSubfileControlVisible()
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).isSubfileControlVisible();
    }

    public boolean isSubfileFolded()
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).isSubfileFolded();
    }

    public boolean isSubfileVisible()
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).isSubfileVisible();
    }

    public int getSubfileAreaFirstRow()
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).getSubfileAreaFirstRow();
    }

    public int getSubfileAreaHeight()
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).getSubfileAreaHeight();
    }

    public boolean isScrollbarShown()
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).isScrollbarShown();
    }

    public boolean isFieldVisible(int i, String s)
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).isFieldVisible(i, s);
    }

    public boolean isMDTOn(int i, String s)
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).isMDTOn(i, s);
    }

    public String getFieldValueWithTransform(String s, int i, int j, int k, int l)
    {
        String s1 = getFieldValue(s, i);
        char c = ((SubfileControlRecordViewBean)super._recordViewBean).getSubfileFieldViewDefinition(s).getKeyboardShift();
        s1 = softSubstring(s1, k, l, c);
        return applyFieldValueTransform(s1, j);
    }

    public String getSubfileValuesAfterEditing(String s)
    {
        return ((SubfileControlRecordViewBean)super._recordViewBean).getSubfileValuesAfterEditing(s);
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2001-2002");

}
