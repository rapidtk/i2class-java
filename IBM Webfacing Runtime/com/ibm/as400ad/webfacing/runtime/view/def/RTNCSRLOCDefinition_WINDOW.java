// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.webfacing.runtime.view.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view.def:
//            RTNCSRLOCDefinition

public class RTNCSRLOCDefinition_WINDOW extends RTNCSRLOCDefinition
{

    public RTNCSRLOCDefinition_WINDOW()
    {
        _absoluteRowField = null;
        _absolueColumnField = null;
        _windowRowField = null;
        _windowColumnField = null;
    }

    public String getAbsoluteRowField()
    {
        return _absoluteRowField;
    }

    public String getWindowColumnField()
    {
        return _windowColumnField;
    }

    public String getWindowRowField()
    {
        return _windowRowField;
    }

    public void setCursor(RecordFeedbackBean recordfeedbackbean, CursorReturnData cursorreturndata)
    {
        if(getAbsoluteRowField() != null)
            recordfeedbackbean.updateFieldValue(getAbsoluteRowField(), Integer.toString(cursorreturndata.getAbsolutePosition().getRow()));
        if(getAbsoluteColumnField() != null)
            recordfeedbackbean.updateFieldValue(getAbsoluteColumnField(), Integer.toString(cursorreturndata.getAbsolutePosition().getColumn()));
        if(getWindowRowField() != null)
            recordfeedbackbean.updateFieldValue(getWindowRowField(), Integer.toString(cursorreturndata.getWinRelativePosition().getRow()));
        if(getWindowColumnField() != null)
            recordfeedbackbean.updateFieldValue(getWindowColumnField(), Integer.toString(cursorreturndata.getWinRelativePosition().getColumn()));
    }

    public void setAbsoluteRowField(String s)
    {
        _absoluteRowField = s;
    }

    public void setWindowColumnField(String s)
    {
        _windowColumnField = s;
    }

    public void setWindowRowField(String s)
    {
        _windowRowField = s;
    }

    public String getAbsoluteColumnField()
    {
        return _absolueColumnField;
    }

    public void setAbsoluteColumnField(String s)
    {
        _absolueColumnField = s;
    }

    private String _absoluteRowField;
    private String _absolueColumnField;
    private String _windowRowField;
    private String _windowColumnField;
}
