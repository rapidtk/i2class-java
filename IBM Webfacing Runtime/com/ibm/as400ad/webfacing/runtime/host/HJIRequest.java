// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.host:
//            IWFInputBuffer, IHJIRequestType, IHJIErrors, WFCommunicationsException, 
//            OffsetInputStream

class HJIRequest
    implements IWFInputBuffer, IHJIRequestType, IHJIErrors, Serializable
{

    public int length()
    {
        return 8;
    }

    public void toStream(OutputStream outputstream)
        throws IOException
    {
        DataOutputStream dataoutputstream = new DataOutputStream(outputstream);
        dataoutputstream.writeInt(_requestType);
        dataoutputstream.writeInt(getOffset() + 8);
    }

    public HJIRequest(int i)
    {
        _offset = 0;
        _replyData = null;
        _requestType = i;
    }

    private String parseReply(OffsetInputStream offsetinputstream)
        throws IOException
    {
        int i = offsetinputstream.readInt();
        if(i != 0)
            throw issueError(i);
        int j = offsetinputstream.readInt();
        int k = offsetinputstream.readInt();
        StringBuffer stringbuffer = new StringBuffer(k);
        for(; k > 0; k -= 2)
        {
            char c = offsetinputstream.readChar();
            stringbuffer.append(c);
        }

        return stringbuffer.toString();
    }

    void readReply(OffsetInputStream offsetinputstream)
        throws WFCommunicationsException
    {
        try
        {
            _replyData = parseReply(offsetinputstream);
            replyReceived(false);
        }
        catch(IOException ioexception)
        {
            logError(ioexception);
            replyReceived(true);
        }
    }

    void setOffset(int i)
    {
        _offset = i;
    }

    int getOffset()
    {
        return _offset;
    }

    public String getReplyData()
    {
        return _replyData != null ? _replyData : "";
    }

    void logError(IOException ioexception)
    {
        ITraceLogger itracelogger = WFSession.getTraceLogger();
        itracelogger.err(2, "Communications error while receiving reply to the request for information about the interactive job the WebFaced application is running in: " + ioexception.getMessage());
        itracelogger.err(3, ioexception);
    }

    public void replyReceived(boolean flag)
    {
    }

    protected IOException issueError(int i)
    {
        String s = "";
        switch(i)
        {
        case 1: // '\001'
            s = "Fatal error occurred while retrieving information from the host application's job.";
            // fall through

        case 3: // '\003'
            s = "The given request type " + _requestType + " is not recognized by the API to retrieve information from the host application's job.";
            // fall through

        default:
            return new IOException(s);
        }
    }

    void setReplyData(String s)
    {
        _replyData = s;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    private int _requestType;
    private int _offset;
    private String _replyData;
}
