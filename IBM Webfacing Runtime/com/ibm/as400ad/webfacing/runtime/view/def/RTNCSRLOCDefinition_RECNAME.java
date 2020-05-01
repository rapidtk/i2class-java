// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.view.CursorReturnData;
import com.ibm.as400ad.webfacing.runtime.view.RecordFeedbackBean;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view.def:
//            RTNCSRLOCDefinition

public class RTNCSRLOCDefinition_RECNAME extends RTNCSRLOCDefinition
{

    public RTNCSRLOCDefinition_RECNAME()
    {
        _recordNameField = null;
        _fieldNameField = null;
        _cursorPosField = null;
    }

    public String getCursorPosField()
    {
        return _cursorPosField;
    }

    public String getFieldNameField()
    {
        return _fieldNameField;
    }

    public String getRecordNameField()
    {
        return _recordNameField;
    }

    public void setCursor(RecordFeedbackBean recordfeedbackbean, CursorReturnData cursorreturndata)
    {
        if(getRecordNameField() != null)
            recordfeedbackbean.updateFieldValue(getRecordNameField(), WebfacingConstants.undoReplaceSpecialChars(cursorreturndata.getRecordName()));
        if(getFieldNameField() != null)
            recordfeedbackbean.updateFieldValue(getFieldNameField(), WebfacingConstants.undoReplaceSpecialChars(cursorreturndata.getFieldName()));
        if(getCursorPosField() != null)
        {
            String s;
            if(cursorreturndata.getFieldName().toString().equals(CursorReturnData.BLANK))
                s = "0000";
            else
                s = "0001";
            recordfeedbackbean.updateFieldValue(getCursorPosField(), s);
        }
    }

    public void setCursorPosField(String s)
    {
        _cursorPosField = s;
    }

    public void setFieldNameField(String s)
    {
        _fieldNameField = s;
    }

    public void setRecordNameField(String s)
    {
        _recordNameField = s;
    }

    private String _recordNameField;
    private String _fieldNameField;
    private String _cursorPosField;
}
