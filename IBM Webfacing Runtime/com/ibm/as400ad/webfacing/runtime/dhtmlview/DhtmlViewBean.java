// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.dhtmlview;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.host.*;
import com.ibm.as400ad.webfacing.runtime.model.IRecordData;
import com.ibm.as400ad.webfacing.runtime.view.*;
import com.ibm.as400ad.webfacing.runtime.view.def.FieldViewDefinition;
import com.ibm.as400ad.webfacing.util.DBCSUtil;
import com.ibm.as400ad.webfacing.util.ITraceLogger;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.dhtmlview:
//            HTMLStringTransform

public class DhtmlViewBean
    implements IDisplayRecord
{

    public DhtmlViewBean(RecordViewBean recordviewbean)
    {
        _targetFieldValueTransform = new HTMLStringTransform();
        _recordViewBean = recordviewbean;
    }

    public boolean evaluateIndicatorExpression(String s)
    {
        return _recordViewBean.evaluateIndicatorExpression(s);
    }

    public String evaluateStyleClass(DisplayAttributeBean displayattributebean)
    {
        return _recordViewBean.evaluateStyleClass(displayattributebean);
    }

    public String getCHKMSG(String s)
    {
        return applyFieldValueTransform(_recordViewBean.getCHKMSG(s), 4);
    }

    public String getDate(DateType datetype)
    {
        return _recordViewBean.getDate(datetype);
    }

    public String getDate(DateType datetype, CenturyType centurytype, char c, char c1)
    {
        return _recordViewBean.getDate(datetype, centurytype, c, c1);
    }

    public String getDate(DateType datetype, CenturyType centurytype, SeparatorType separatortype)
    {
        return _recordViewBean.getDate(datetype, centurytype, separatortype);
    }

    public String getDate(DateType datetype, CenturyType centurytype, String s)
    {
        return _recordViewBean.getDate(datetype, centurytype, s);
    }

    public String getDateWithTransform(DateType datetype, CenturyType centurytype, String s, int i)
    {
        return applyFieldValueTransform(getDate(datetype, centurytype, s), i);
    }

    public String getFieldValue(String s)
    {
        String s1 = _recordViewBean.getFieldValue(s);
        FieldViewDefinition fieldviewdefinition = _recordViewBean.getFieldViewDefinition(s);
        if(fieldviewdefinition != null && fieldviewdefinition.isMasked())
            s1 = applyFieldMasking(s1, fieldviewdefinition.getStartMaskingAt(), fieldviewdefinition.getEndMaskingAt());
        return s1;
    }

    public String getFieldValueWithTransform(String s, int i)
    {
        return applyFieldValueTransform(getFieldValue(s), i);
    }

    public int getSLNOVAROffset()
    {
        return _recordViewBean.getSLNOVAROffset();
    }

    public String getSystemName()
    {
        return _recordViewBean.getSystemName();
    }

    public String getSystemTime()
    {
        return _recordViewBean.getSystemTime();
    }

    public String getSystemTime(char c, char c1)
    {
        return _recordViewBean.getSystemTime(c, c1);
    }

    public String getSystemTime(String s)
    {
        return _recordViewBean.getSystemTime(s);
    }

    public String getSystemTimeWithTransform(String s, int i)
    {
        return applyFieldValueTransform(getSystemTime(s), i);
    }

    public String getUserID()
    {
        return _recordViewBean.getUserID();
    }

    public int getZOrder()
    {
        return _recordViewBean.getZOrder();
    }

    public boolean isDSPFActive()
    {
        return _recordViewBean.isDSPFActive();
    }

    public boolean isDspfDbcsCapable()
    {
        return _recordViewBean.isDspfDbcsCapable();
    }

    public int getDisplayZIndex()
    {
        return _recordViewBean.getDisplayZIndex();
    }

    public boolean isFieldVisible(String s)
    {
        return _recordViewBean.isFieldVisible(s);
    }

    public boolean isMDTOn(String s)
    {
        return _recordViewBean.isMDTOn(s);
    }

    public String getDateWithTransform(DateType datetype, CenturyType centurytype, String s, int i, int j, int k)
    {
        String s1 = getDate(datetype, centurytype, s);
        s1 = softSubstring(s1, j, k);
        return applyFieldValueTransform(s1, i);
    }

    public String getFieldValueWithTransform(String s, int i, int j, int k)
    {
        String s1 = getFieldValue(s);
        char c = _recordViewBean.getFieldViewDefinition(s).getKeyboardShift();
        s1 = softSubstring(s1, j, k, c);
        return applyFieldValueTransform(s1, i);
    }

    public String getSystemName(int i, int j)
    {
        String s = getSystemName();
        s = softSubstring(s, i, j);
        return s;
    }

    public String getSystemTimeWithTransform(String s, int i, int j, int k)
    {
        String s1 = getSystemTime(s);
        s1 = softSubstring(s1, j, k);
        return applyFieldValueTransform(s1, i);
    }

    public String getUserID(int i, int j)
    {
        String s = getUserID();
        s = softSubstring(s, i, j);
        return s;
    }

    public boolean isProtected()
    {
        return _recordViewBean.isProtected();
    }

    protected String applyFieldMasking(String s, int i, int j)
    {
        int k = 0;
        if(s != null)
            k = s.length();
        if(k == 0)
        {
            i = 0;
            j = 0;
        } else
        {
            if(k <= i)
                i = k - 1;
            if(k < j)
                j = k;
        }
        StringBuffer stringbuffer = new StringBuffer(s);
        return stringbuffer.replace(i, j, "").toString();
    }

    protected String applyFieldValueTransform(String s, int i)
    {
        String s1;
        if(_targetFieldValueTransform != null)
            s1 = _targetFieldValueTransform.transform(s, i);
        else
            s1 = s;
        return s1;
    }

    protected String softSubstring(String s, int i, int j)
    {
        try
        {
            s = WebfacingConstants.softSubstring(s, i, j);
        }
        catch(StringIndexOutOfBoundsException stringindexoutofboundsexception)
        {
            WFSession.getTraceLogger().err(2, stringindexoutofboundsexception, "Invalid begin and/or end index in RecordViewBean.softSubstring(String,int,int)");
        }
        return s;
    }

    protected String softSubstring(String s, int i, int j, char c)
    {
        try
        {
            switch(c)
            {
            case 79: // 'O'
                int k = _recordViewBean.getFeedbackBean().getRecordData().getJobCCSID();
                s = WebfacingConstants.softSubstring(s, i, j, k);
                break;

            case 69: // 'E'
            case 74: // 'J'
                int l = _recordViewBean.getFeedbackBean().getRecordData().getJobCCSID();
                if(c == 'J' || DBCSUtil.isDBCS(s.charAt(1), l))
                {
                    if(i == 0)
                    {
                        if(j % 2 == 0)
                        {
                            j /= 2;
                            s = WebfacingConstants.softSubstring(s, i, j) + " ";
                        } else
                        {
                            j = j / 2 + 1;
                            s = WebfacingConstants.softSubstring(s, i, j);
                        }
                    } else
                    {
                        i = i / 2 + i % 2;
                        j = j / 2 + j % 2;
                        s = WebfacingConstants.softSubstring(s, i, j);
                    }
                } else
                {
                    s = WebfacingConstants.softSubstring(s, i, j);
                }
                break;

            case 71: // 'G'
                j /= 2;
                i /= 2;
                s = WebfacingConstants.softSubstring(s, i, j);
                break;

            default:
                s = WebfacingConstants.softSubstring(s, i, j);
                break;
            }
        }
        catch(StringIndexOutOfBoundsException stringindexoutofboundsexception)
        {
            WFSession.getTraceLogger().err(2, stringindexoutofboundsexception, "Invalid begin and/or end index in RecordViewBean.softSubstring(String,int,int)");
        }
        return s;
    }

    public String getValuesAfterEditing(String s)
    {
        return _recordViewBean.getValuesAfterEditing(s);
    }

    public String getActiveKeyName(String s)
    {
        return _recordViewBean.getActiveKeyName(s);
    }

    public int getZOrderPrefix(String s)
    {
        return _recordViewBean.getZOrderPrefix(s);
    }

    public boolean disableHyperlink(String s, String s1)
    {
        return _recordViewBean.disableHyperlink(s, s1);
    }

    public boolean isRecordOnTopLayer()
    {
        return WFSession.getScreenBuilderModel().isRecordOnTopLayer(_recordViewBean);
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2001-2003");
    protected IStringTransform _targetFieldValueTransform;
    protected RecordViewBean _recordViewBean;

}
