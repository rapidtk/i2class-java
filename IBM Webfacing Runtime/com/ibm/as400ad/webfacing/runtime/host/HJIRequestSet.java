// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.*;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.host:
//            HJIRequest, OffsetInputStream, WFCommunicationsException, IWFInputBuffer, 
//            IHostRequest

class HJIRequestSet
    implements IWFInputBuffer, Serializable
{

    public HJIRequestSet()
    {
        _requests = new Vector();
        _reqHeaderLength = -1;
        _totalReqLength = -1;
    }

    void add(HJIRequest hjirequest)
    {
        _requests.add(hjirequest);
    }

    private HJIRequest getRequest(int i)
    {
        return (HJIRequest)_requests.get(i);
    }

    public int length()
    {
        if(-1 == _totalReqLength)
        {
            _reqHeaderLength = 8 + _requests.size() * 4;
            _totalReqLength = _reqHeaderLength;
            for(int i = 0; i < _requests.size(); i++)
                _totalReqLength += getRequest(i).length();

        }
        return _totalReqLength;
    }

    private void parseReplies(InputStream inputstream)
        throws WFCommunicationsException
    {
        try
        {
            OffsetInputStream offsetinputstream = new OffsetInputStream(new DataInputStream(inputstream));
            int i = offsetinputstream.readInt();
            int j = offsetinputstream.readInt();
            int k = offsetinputstream.readInt();
            int ai[] = new int[k];
            for(int l = 0; l < k; l++)
                ai[l] = offsetinputstream.readInt();

            for(int i1 = 0; i1 < k; i1++)
            {
                offsetinputstream.skipToOffset(ai[i1]);
                getRequest(i1).readReply(offsetinputstream);
            }

        }
        catch(IOException ioexception)
        {
            throw new WFCommunicationsException(ioexception, "Communications error while receiving replies to a set of requests for information from the application job: " + ioexception.getMessage());
        }
    }

    void submit(IHostRequest ihostrequest)
    {
        try
        {
            InputStream inputstream = ihostrequest.request(this);
            parseReplies(inputstream);
        }
        catch(IOException ioexception)
        {
            handleError(new WFCommunicationsException(ioexception, "Communications error while querying job information from the host: " + ioexception.getMessage()));
        }
        catch(WebfacingInternalException webfacinginternalexception)
        {
            handleError(new WFCommunicationsException(webfacinginternalexception, "Communications error while querying job information from the host: " + webfacinginternalexception.getMessage()));
        }
        catch(WFCommunicationsException wfcommunicationsexception)
        {
            handleError(wfcommunicationsexception);
        }
    }

    public void toStream(OutputStream outputstream)
        throws IOException
    {
        DataOutputStream dataoutputstream = new DataOutputStream(outputstream);
        int i = _requests.size();
        dataoutputstream.writeInt(length());
        dataoutputstream.writeInt(i);
        int j = _reqHeaderLength;
        for(int k = 0; k < i; k++)
        {
            dataoutputstream.writeInt(j);
            getRequest(k).setOffset(j);
            j += getRequest(k).length();
        }

        for(int l = 0; l < i; l++)
            getRequest(l).toStream(dataoutputstream);

    }

    void handleError(Exception exception)
    {
        ITraceLogger itracelogger = WFSession.getTraceLogger();
        itracelogger.err(2, exception);
        for(int i = 0; i < _requests.size(); i++)
            getRequest(i).replyReceived(true);

    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    private static final int LENGTH_UNDEFINED = -1;
    private static final int REQ_HEADER_LENGTH = 8;
    private static final int REPLY_HEADER_LENGTH = 12;
    private Vector _requests;
    private int _reqHeaderLength;
    private int _totalReqLength;
}
