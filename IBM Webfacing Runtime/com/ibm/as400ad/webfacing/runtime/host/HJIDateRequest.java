// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.host:
//            HJIRequest, SeparatorType, CenturyType, DateType

public class HJIDateRequest extends HJIRequest
{

    public String getDate()
    {
        return getReplyData();
    }

    public int length()
    {
        return super.length() + 8;
    }

    public void toStream(OutputStream outputstream)
        throws IOException
    {
        super.toStream(outputstream);
        DataOutputStream dataoutputstream = new DataOutputStream(outputstream);
        dataoutputstream.writeInt(_separator.getSeparator());
        dataoutputstream.writeInt(_digit.getDigits());
    }

    public void replyReceived(boolean flag)
    {
        if(flag)
            if(_digit == CenturyType.FOUR_DIGITS && _separator == SeparatorType.HAS_SEPARATOR)
                setReplyData("YYYY/MM/DD");
            else
            if(_digit == CenturyType.FOUR_DIGITS && _separator == SeparatorType.NO_SEPARATOR)
                setReplyData("YYYYMMDD");
            else
            if(_digit == CenturyType.TWO_DIGITS && _separator == SeparatorType.HAS_SEPARATOR)
                setReplyData("YY/MM/DD");
            else
                setReplyData("YYMMDD");
    }

    public HJIDateRequest(DateType datetype)
    {
        super(datetype != DateType.SYS_DATE ? 12 : 5);
        _separator = SeparatorType.NO_SEPARATOR;
        _digit = CenturyType.TWO_DIGITS;
    }

    public HJIDateRequest(DateType datetype, CenturyType centurytype)
    {
        this(datetype);
        _digit = centurytype;
    }

    public HJIDateRequest(DateType datetype, CenturyType centurytype, SeparatorType separatortype)
    {
        this(datetype);
        _digit = centurytype;
        _separator = separatortype;
    }

    void logError(IOException ioexception)
    {
        ITraceLogger itracelogger = WFSession.getTraceLogger();
        itracelogger.err(2, "Communications error while receiving reply to the request to retrieve the date " + ioexception.getMessage());
        itracelogger.err(3, ioexception);
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    private SeparatorType _separator;
    private CenturyType _digit;
}
