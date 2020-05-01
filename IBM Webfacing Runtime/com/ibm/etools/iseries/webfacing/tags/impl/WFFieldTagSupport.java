// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.tags.impl;

import com.ibm.as400ad.webfacing.runtime.view.IDisplayRecord;
import com.ibm.as400ad.webfacing.runtime.view.IDisplaySFLCTLRecord;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

// Referenced classes of package com.ibm.etools.iseries.webfacing.tags.impl:
//            WFBodyTagSupport

public class WFFieldTagSupport extends WFBodyTagSupport
{

    public WFFieldTagSupport()
    {
        rcdRef = null;
        record = WFBodyTagSupport.MT;
        _class = WFBodyTagSupport.MT;
        style = WFBodyTagSupport.MT;
        htmlbefore = WFBodyTagSupport.MT;
        htmlafter = WFBodyTagSupport.MT;
        onclick = WFBodyTagSupport.MT;
        onblur = WFBodyTagSupport.MT;
        _sflrow = WFBodyTagSupport.MT;
        _ctrlrcd = WFBodyTagSupport.MT;
    }

    public void release()
    {
        _class = WFBodyTagSupport.MT;
        style = WFBodyTagSupport.MT;
        htmlbefore = WFBodyTagSupport.MT;
        htmlafter = WFBodyTagSupport.MT;
        onclick = WFBodyTagSupport.MT;
        onblur = WFBodyTagSupport.MT;
        rcdRef = null;
        record = WFBodyTagSupport.MT;
        _sflrow = WFBodyTagSupport.MT;
        _ctrlrcd = WFBodyTagSupport.MT;
        super.release();
    }

    public String getFieldValue()
    {
        String s;
        if(WFBodyTagSupport.MT != getSflrow())
            s = ((IDisplaySFLCTLRecord)getRecordRef()).getFieldValue(getField(), Integer.parseInt(getSflrow()));
        else
            s = getRecordRef().getFieldValue(getField());
        logMsg("DBG", 6, "Value for field '" + getField() + "' = >" + s + "<");
        return s;
    }

    private IDisplayRecord getRecordRef()
    {
        if(null == rcdRef)
            rcdRef = (IDisplayRecord)super.context.getSession().getAttribute(record);
        return rcdRef;
    }

    public String getField()
    {
        return super.name;
    }

    public void setField(String s)
    {
        super.name = s;
    }

    public String getFieldId()
    {
        String s = getCtrlrcd();
        if(WFBodyTagSupport.MT != s);
        String s1 = "l" + getRecordRef().getDisplayZIndex() + "_" + s + "$" + getField();
        if(WFBodyTagSupport.MT != getSflrow())
            s1 = s1 + "$" + getSflrow();
        return s1;
    }

    public boolean isVisible()
    {
        return getRecordRef().isFieldVisible(super.name);
    }

    public boolean isProtected()
    {
        return getRecordRef().isProtected();
    }

    public String getRecord()
    {
        return record;
    }

    public void setRecord(String s)
    {
        record = s;
    }

    public String getclass()
    {
        return _class;
    }

    public String getHtmlafter()
    {
        return htmlafter;
    }

    public String getHtmlbefore()
    {
        return htmlbefore;
    }

    public String getOnblur()
    {
        return onblur;
    }

    public String getOnclick()
    {
        return onclick;
    }

    public String getStyle()
    {
        return style;
    }

    public void setClass(String s)
    {
        _class = s;
    }

    public void setHtmlafter(String s)
    {
        htmlafter = s;
    }

    public void setHtmlbefore(String s)
    {
        htmlbefore = s;
    }

    public void setOnblur(String s)
    {
        onblur = s;
    }

    public void setOnclick(String s)
    {
        onclick = s;
    }

    public void setStyle(String s)
    {
        style = s;
    }

    public void setSflrow(String s)
    {
        _sflrow = s;
    }

    public String getSflrow()
    {
        return _sflrow;
    }

    public void setCtrlrcd(String s)
    {
        _ctrlrcd = s;
    }

    public String getCtrlrcd()
    {
        return _ctrlrcd;
    }

    public static final String Copyright = "(C) Copyright IBM Corporation 2002-2003.  All Rights Reserved.";
    private IDisplayRecord rcdRef;
    private String record;
    private String _class;
    private String style;
    private String htmlbefore;
    private String htmlafter;
    private String onclick;
    private String onblur;
    private String _sflrow;
    private String _ctrlrcd;
}
