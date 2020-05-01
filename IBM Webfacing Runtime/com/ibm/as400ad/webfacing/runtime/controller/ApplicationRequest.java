// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.controller;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.model.IIndicatorArea;
import com.ibm.as400ad.webfacing.runtime.model.RecordDataBean;
import com.ibm.as400ad.webfacing.runtime.model.def.IRecordDataDefinition;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.controller:
//            IDSPFObject

public class ApplicationRequest
{

    public ApplicationRequest(byte byte0, IDSPFObject idspfobject)
    {
        _RecordBean = null;
        _RecordName = null;
        _indArea = null;
        _RequestType = byte0;
        _DSPFObject = idspfobject;
    }

    public ApplicationRequest(byte byte0, IDSPFObject idspfobject, RecordDataBean recorddatabean)
    {
        this(byte0, idspfobject);
        _RecordBean = recorddatabean;
        if(recorddatabean.getRecordDataDefinition().hasSeparateIndicatorArea())
            setIndArea(recorddatabean.createSeparateIndicatorArea());
    }

    public ApplicationRequest(byte byte0, IDSPFObject idspfobject, String s)
    {
        this(byte0, idspfobject);
        _RecordName = s;
    }

    public IDSPFObject getDSPFObject()
    {
        return _DSPFObject;
    }

    public IIndicatorArea getIndArea()
    {
        return _indArea;
    }

    public RecordDataBean getRecordDataBean()
    {
        return _RecordBean;
    }

    public String getRecordName()
    {
        if(null == _RecordBean)
            return WebfacingConstants.replaceSpecialCharacters(_RecordName);
        else
            return _RecordBean.getRecordName();
    }

    public byte getRequestType()
    {
        return _RequestType;
    }

    public static final String getRequestTypeString(byte byte0)
    {
        switch(byte0)
        {
        case 1: // '\001'
            return " REQTYPE_GET";

        case 2: // '\002'
            return " REQTYPE_GETD";

        case 5: // '\005'
            return " REQTYPE_PUT";

        case 6: // '\006'
            return " REQTYPE_PUTGET";

        case 17: // '\021'
            return " REQTYPE_FILEOPEN";

        case 16: // '\020'
            return " REQTYPE_FILECLOSE";

        case 3: // '\003'
        case 4: // '\004'
        case 7: // '\007'
        case 8: // '\b'
        case 9: // '\t'
        case 10: // '\n'
        case 11: // '\013'
        case 12: // '\f'
        case 13: // '\r'
        case 14: // '\016'
        case 15: // '\017'
        default:
            return " UNKNOWN REQUEST TYPE!";
        }
    }

    public void setIndArea(IIndicatorArea iindicatorarea)
    {
        _indArea = iindicatorarea;
    }

    public static final byte ADBD_REQTYPE_GET = 1;
    public static final byte ADBD_REQTYPE_GETD = 2;
    public static final byte ADBD_REQTYPE_PUT = 5;
    public static final byte ADBD_REQTYPE_PUTGET = 6;
    public static final byte ADBD_REQTYPE_UPDATE = 7;
    public static final byte ADBD_REQTYPE_FILECLOSE = 16;
    private byte _RequestType;
    private RecordDataBean _RecordBean;
    private String _RecordName;
    private IDSPFObject _DSPFObject;
    private IIndicatorArea _indArea;
    public static final byte ADBD_REQTYPE_FILEOPEN = 17;
}
