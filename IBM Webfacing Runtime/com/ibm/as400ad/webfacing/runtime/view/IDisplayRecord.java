// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.webfacing.runtime.host.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            DisplayAttributeBean

public interface IDisplayRecord
{

    public abstract boolean evaluateIndicatorExpression(String s);

    public abstract String evaluateStyleClass(DisplayAttributeBean displayattributebean);

    public abstract String getCHKMSG(String s);

    public abstract String getDate(DateType datetype);

    public abstract String getDate(DateType datetype, CenturyType centurytype, char c, char c1);

    public abstract String getDate(DateType datetype, CenturyType centurytype, SeparatorType separatortype);

    public abstract String getDate(DateType datetype, CenturyType centurytype, String s);

    public abstract String getDateWithTransform(DateType datetype, CenturyType centurytype, String s, int i);

    public abstract String getFieldValue(String s);

    public abstract String getFieldValueWithTransform(String s, int i);

    public abstract int getSLNOVAROffset();

    public abstract String getSystemName();

    public abstract String getSystemTime();

    public abstract String getSystemTime(char c, char c1);

    public abstract String getSystemTime(String s);

    public abstract String getSystemTimeWithTransform(String s, int i);

    public abstract String getUserID();

    /**
     * @deprecated Method getZOrder is deprecated
     */

    public abstract int getZOrder();

    public abstract boolean isDSPFActive();

    public abstract boolean isDspfDbcsCapable();

    public abstract int getDisplayZIndex();

    public abstract boolean isFieldVisible(String s);

    public abstract boolean isMDTOn(String s);

    public abstract String getDateWithTransform(DateType datetype, CenturyType centurytype, String s, int i, int j, int k);

    public abstract String getFieldValueWithTransform(String s, int i, int j, int k);

    public abstract String getSystemName(int i, int j);

    public abstract String getSystemTimeWithTransform(String s, int i, int j, int k);

    public abstract String getUserID(int i, int j);

    public abstract boolean isProtected();

    public abstract boolean isRecordOnTopLayer();

    public abstract String getValuesAfterEditing(String s);

    public abstract String getActiveKeyName(String s);

    public abstract boolean disableHyperlink(String s, String s1);

    public abstract int getZOrderPrefix(String s);

    public static final String Copyright = "(C) Copyright IBM Corp. 2000,2003.  All Rights Reserved.";
}
