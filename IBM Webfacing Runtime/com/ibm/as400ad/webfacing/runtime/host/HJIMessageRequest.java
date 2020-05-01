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

public class HJIMessageRequest extends HJIRequest
{

    public HJIMessageRequest(String s, String s1)
    {
        this(s, s1, "     *LIBL", null);
    }

    public HJIMessageRequest(String s, String s1, String s2)
    {
        this(s, s1, s2, null);
    }

    public HJIMessageRequest(String s, String s1, String s2, String s3)
    {
        super(17);
        _msgFile = null;
        if(s == null || s.length() == 0)
        {
            throw new IllegalArgumentException("Message identifiers can not be empty,  Attempt to retrieve message " + s + " in " + s2 + "/" + s1 + " failed.");
        } else
        {
            _msgId = padToWidth(s, 7);
            _msgFile = padToWidth(s1, 10);
            _msgLibrary = padToWidth(s2, 10);
            _msgData = s3;
            return;
        }
    }

    public String getErrorText()
    {
        return _errorText != null ? _errorText : _msgId.substring(3) + "?" + _msgFile.trim() + "?";
    }

    String getMessageId()
    {
        return _msgId;
    }

    public String getMessageText()
    {
        return getReplyData();
    }

    protected IOException issueError(int i)
    {
        String s = "";
        StringBuffer stringbuffer = new StringBuffer(_msgId.substring(3));
        switch(i)
        {
        case 5: // '\005'
            stringbuffer.append(" ");
            stringbuffer.append(_msgFile.trim() + "?");
            s = "Could not find message file " + _msgFile.trim() + " in the library " + _msgLibrary.trim() + ".";
            break;

        case 6: // '\006'
            stringbuffer.append("?");
            stringbuffer.append(_msgFile.trim());
            s = "Could not find message id " + _msgId + " in the message file " + _msgLibrary.trim() + "/" + _msgFile.trim() + ".";
            break;

        case 8: // '\b'
            stringbuffer.append("?");
            stringbuffer.append(_msgFile.trim() + "?");
            s = "Not authorized to the message file " + _msgFile.trim() + " in the library " + _msgLibrary.trim() + ".";
            break;

        case 7: // '\007'
            stringbuffer.append("?");
            stringbuffer.append(_msgFile.trim() + "?");
            s = "Host error occurred while resolving message id " + _msgId + " in the message file " + _msgLibrary.trim() + "/" + _msgFile.trim() + ".";
            break;

        default:
            stringbuffer.append("?");
            stringbuffer.append(_msgFile.trim() + "?");
            _errorText = stringbuffer.toString();
            return super.issueError(i);
        }
        _errorText = stringbuffer.toString();
        return new IOException(s);
    }

    public int length()
    {
        return super.length() + 62 + (_msgData != null ? 2 * _msgData.length() : 0);
    }

    void logError(IOException ioexception)
    {
        ITraceLogger itracelogger = WFSession.getTraceLogger();
        itracelogger.err(2, "Communications error while receiving reply to the request to retrieve message " + _msgId + " in " + _msgLibrary.trim() + "/" + _msgFile.trim() + ". " + ioexception.getMessage());
        itracelogger.err(3, ioexception);
    }

    private String padToWidth(String s, int i)
    {
        if(s.length() == i)
            return s;
        if(s.length() > i)
        {
            if(null == _msgFile)
                throw new IllegalArgumentException("Message file names must be length 10,  Attempt to retrieve message " + _msgId + " in " + s + " failed.");
            else
                throw new IllegalArgumentException("Library names must be length 10,  Attempt to retrieve message " + _msgId + " in " + s + "/" + _msgFile + " failed.");
        } else
        {
            PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(s);
            paddedstringbuffer.padRight(' ', i);
            return paddedstringbuffer.toString();
        }
    }

    public void toStream(OutputStream outputstream)
        throws IOException
    {
        super.toStream(outputstream);
        DataOutputStream dataoutputstream = new DataOutputStream(outputstream);
        dataoutputstream.writeChars(_msgFile);
        dataoutputstream.writeChars(_msgLibrary);
        dataoutputstream.writeChars(_msgId);
        if(null == _msgData)
        {
            dataoutputstream.writeInt(0);
            dataoutputstream.writeInt(0);
        } else
        {
            dataoutputstream.writeInt(2 * _msgData.length());
            dataoutputstream.writeInt(getOffset() + super.length() + 62);
            dataoutputstream.writeChars(_msgData);
        }
    }

    public String toString()
    {
        return getClass().getName() + ": Retrieved message '" + getMessageText() + "' from " + _msgId.trim() + " in " + _msgLibrary.trim() + "/" + _msgFile.trim() + " with substitution data '" + _msgData + "'";
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    private String _msgId;
    private String _msgFile;
    private String _msgLibrary;
    private String _msgData;
    private String _errorText;
}
