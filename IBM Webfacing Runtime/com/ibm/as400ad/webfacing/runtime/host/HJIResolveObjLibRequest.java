// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.host:
//            HJIRequest

public class HJIResolveObjLibRequest extends HJIRequest
{

    public HJIResolveObjLibRequest(String s, String s1, String s2)
    {
        this(s, s1, s2, false);
    }

    public HJIResolveObjLibRequest(String s, String s1, String s2, boolean flag)
    {
        super(19);
        _useOverride = 0;
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(s);
        paddedstringbuffer.padRight(' ', 10);
        _object = paddedstringbuffer.toString();
        paddedstringbuffer = new PaddedStringBuffer(s1);
        paddedstringbuffer.padRight(' ', 10);
        _library = paddedstringbuffer.toString();
        paddedstringbuffer = new PaddedStringBuffer(s2);
        paddedstringbuffer.padRight(' ', 10);
        _type = paddedstringbuffer.toString();
        _useOverride = flag ? 1 : 0;
    }

    public String getObjLibName()
    {
        return getReplyData();
    }

    protected IOException issueError(int i)
    {
        String s = "";
        switch(i)
        {
        case 9: // '\t'
            s = "External help file " + _object.trim() + " could not be opened.";
            break;

        default:
            return super.issueError(i);
        }
        return new IOException(s);
    }

    public int length()
    {
        return super.length() + 64;
    }

    void logError(IOException ioexception)
    {
        ITraceLogger itracelogger = WFSession.getTraceLogger();
        itracelogger.err(2, "Communications error while receiving reply to the request to resolve to the library " + _library.trim() + " for " + _object.trim() + " of type " + _type.trim() + ". " + ioexception.getMessage());
        itracelogger.err(3, ioexception);
    }

    public void replyReceived(boolean flag)
    {
        if(flag)
            setReplyData(null);
    }

    public void toStream(OutputStream outputstream)
        throws IOException
    {
        super.toStream(outputstream);
        DataOutputStream dataoutputstream = new DataOutputStream(outputstream);
        dataoutputstream.writeChars(_object);
        dataoutputstream.writeChars(_library);
        dataoutputstream.writeChars(_type);
        dataoutputstream.writeInt(_useOverride);
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    private String _object;
    private String _library;
    private String _type;
    private int _useOverride;
}
