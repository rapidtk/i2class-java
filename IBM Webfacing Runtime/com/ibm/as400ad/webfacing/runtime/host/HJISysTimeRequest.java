// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.IOException;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.host:
//            HJIRequest

class HJISysTimeRequest extends HJIRequest
{

    public HJISysTimeRequest()
    {
        super(6);
    }

    public String getSystemTime()
    {
        return getReplyData();
    }

    void logError(IOException ioexception)
    {
        ITraceLogger itracelogger = WFSession.getTraceLogger();
        itracelogger.err(2, "Communications error while receiving reply to the request to retrieve the system time " + ioexception.getMessage());
        itracelogger.err(3, ioexception);
    }

    public void replyReceived(boolean flag)
    {
        if(flag)
            setReplyData("HHMMSS");
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
}
