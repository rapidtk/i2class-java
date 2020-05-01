// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import com.ibm.as400ad.webfacing.runtime.fldformat.FieldDataFormatter;
import com.ibm.as400ad.webfacing.runtime.model.IAccessFieldData;
import com.ibm.as400ad.webfacing.runtime.model.IFormattableFieldData;
import com.ibm.as400ad.webfacing.runtime.model.def.MSGIDDefinition;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.host:
//            HJIMessageRequest

public class MSGIDRequest extends HJIMessageRequest
{

    public void replyReceived(boolean flag)
    {
        String s;
        if(!flag)
            s = getMessageText();
        else
            s = getErrorText();
        _recordFieldData.setFieldValue(_field.getFieldName(), s);
        s = _recordFieldData.getFieldDataFormatter(_field.getFieldName()).formatForMSG(_CCSID, _field, s);
        _recordFieldData.setFieldValue(_field.getFieldName(), s);
    }

    public MSGIDRequest(MSGIDDefinition msgiddefinition, IFormattableFieldData iformattablefielddata, IAccessFieldData iaccessfielddata, int i)
    {
        super(msgiddefinition.getMsgId(iaccessfielddata), msgiddefinition.getMsgFile(iaccessfielddata), msgiddefinition.getMsgLibrary(iaccessfielddata));
        _recordFieldData = null;
        _field = null;
        _field = iformattablefielddata;
        _recordFieldData = iaccessfielddata;
        _CCSID = i;
    }

    private IAccessFieldData _recordFieldData;
    private int _CCSID;
    private IFormattableFieldData _field;
}
