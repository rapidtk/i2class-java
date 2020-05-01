// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import com.ibm.as400ad.webfacing.runtime.controller.ApplicationRequest;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.*;

public class ADBDRecordHeader
{

    public ADBDRecordHeader()
    {
        _inviteOn = false;
        _DFRWRTNo = false;
        _FRCDTA = false;
        _AlwRolStrt = 0;
        _AlwRolEnd = 0;
        _AlwRolNumLines = 0;
        _NumSubfiles = 0;
        _LastSflRRN = 0;
        _NextSflRRN = 0;
        _BytesRead = 0;
        _OffsetToIndicatorArea = 0;
        _IOBufferLength = 0;
        _recordHeaderLength = 0;
        _trace = WFSession.getTraceLogger();
    }

    public int getAlwRolEnd()
    {
        return _AlwRolEnd;
    }

    public int getAlwRolNumLines()
    {
        return _AlwRolNumLines;
    }

    public int getAlwRolStrt()
    {
        return _AlwRolStrt;
    }

    protected int getBytesRead()
    {
        return _BytesRead;
    }

    public int getIOBufferLength()
    {
        return _IOBufferLength;
    }

    public int getNumberOfSubfileRecords()
    {
        return _NumSubfiles;
    }

    public int getLastSflRRN()
    {
        return _LastSflRRN;
    }

    public int getOffsetToIndicatorArea()
    {
        return _OffsetToIndicatorArea;
    }

    public int getOffsetToIOBuffer()
    {
        return _OffsetToIOBuffer;
    }

    public int getOffsetToNextRFHeader()
    {
        return _OffsetToNextRFHeader;
    }

    public int getOffsetToSubfileArea()
    {
        return _OffsetToSubfileArea;
    }

    public int getOffsetToSubfileIndicatorArea()
    {
        return _OffsetToSubfileIndicatorArea;
    }

    public String getRecordFormatName()
    {
        return _RecordFormatName;
    }

    protected byte getRequestType()
    {
        return _RequestType;
    }

    public int getSLNO()
    {
        return _SLNO;
    }

    protected void initializeFromOutputBuffer(DataInputStream datainputstream)
        throws IOException, EOFException
    {
        StringBuffer stringbuffer = new StringBuffer(10);
        for(int i = 1; i <= 10; i++)
            stringbuffer.append(datainputstream.readChar());

        _RecordFormatName = stringbuffer.toString().trim();
        datainputstream.readByte();
        datainputstream.readByte();
        _RequestType = datainputstream.readByte();
        byte byte0 = datainputstream.readByte();
        _inviteOn = (byte0 & 0x80) > 0;
        _FRCDTA = (byte0 & 0x40) > 0;
        _DFRWRTNo = (byte0 & 0x20) > 0;
        _SflComplete = (byte0 & 0x10) > 0;
        _SLNO = datainputstream.readInt();
        _AlwRolStrt = datainputstream.readInt();
        _AlwRolEnd = datainputstream.readInt();
        _AlwRolNumLines = datainputstream.readInt();
        _OffsetToIOBuffer = datainputstream.readInt();
        _IOBufferLength = datainputstream.readInt();
        _OffsetToIndicatorArea = datainputstream.readInt();
        _OffsetToSubfileArea = datainputstream.readInt();
        _NumSubfiles = datainputstream.readInt();
        _LastSflRRN = datainputstream.readInt();
        _OffsetToSubfileIndicatorArea = datainputstream.readInt();
        _OffsetToNextRFHeader = datainputstream.readInt();
        _recordHeaderLength = datainputstream.readInt();
    }

    public boolean isINVITE()
    {
        return _inviteOn;
    }

    public int length()
    {
        return 76;
    }

    public void setAlwRolEnd(int i)
    {
        _AlwRolEnd = i;
    }

    public void setAlwRolNumLines(int i)
    {
        _AlwRolNumLines = i;
    }

    public void setAlwRolStrt(int i)
    {
        _AlwRolStrt = i;
    }

    public void setBytesRead(int i)
    {
        _BytesRead += i;
    }

    public void setIOBufferLength(int i)
    {
        _IOBufferLength = i;
    }

    public void setNumberOfSubfileRecords(int i)
    {
        _NumSubfiles = i;
    }

    public void setNextSflRRN(int i)
    {
        _NextSflRRN = i;
    }

    public void setNextSflPageSize(int i)
    {
        _NextSflPageSize = i;
    }

    public void setOffsetToIndicatorArea(int i)
    {
        _OffsetToIndicatorArea = i;
    }

    public void setOffsetToIOBuffer(int i)
    {
        _OffsetToIOBuffer = i;
    }

    public void setOffsetToSubfileArea(int i)
    {
        _OffsetToSubfileArea = i;
    }

    public void setOffsetToSubfileIndicatorArea(int i)
    {
        _OffsetToSubfileIndicatorArea = i;
    }

    public void setRecordFormatName(String s)
    {
        _RecordFormatName = s;
    }

    protected void setRequestType(byte byte0)
    {
        _RequestType = byte0;
    }

    public void setSLNO(int i)
    {
        _SLNO = i;
    }

    protected void toStream(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeChars(_RecordFormatName);
        dataoutputstream.writeByte(0);
        dataoutputstream.writeByte(0);
        dataoutputstream.writeByte(_RequestType);
        dataoutputstream.writeByte(0);
        dataoutputstream.writeInt(_SLNO);
        dataoutputstream.writeInt(_AlwRolStrt);
        dataoutputstream.writeInt(_AlwRolEnd);
        dataoutputstream.writeInt(_AlwRolNumLines);
        dataoutputstream.writeInt(76);
        dataoutputstream.writeInt(_IOBufferLength);
        dataoutputstream.writeInt(_OffsetToIndicatorArea);
        dataoutputstream.writeInt(_OffsetToSubfileArea);
        dataoutputstream.writeInt(_NumSubfiles);
        dataoutputstream.writeInt(_NextSflRRN);
        dataoutputstream.writeInt(_OffsetToSubfileIndicatorArea);
        dataoutputstream.writeInt(_NextSflPageSize);
        dataoutputstream.writeInt(76);
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("\nRECORD HEADER DATA:");
        stringbuffer.append("\n- Record Format Name: ");
        stringbuffer.append(_RecordFormatName);
        stringbuffer.append("\n- Request type: ");
        stringbuffer.append(_RequestType);
        stringbuffer.append(ApplicationRequest.getRequestTypeString(_RequestType));
        stringbuffer.append("\n- SLNO: ");
        stringbuffer.append(_SLNO);
        stringbuffer.append("\n- IO Buf offset: ");
        stringbuffer.append(_OffsetToIOBuffer);
        stringbuffer.append("\n- IO Buf Length: ");
        stringbuffer.append(_IOBufferLength);
        stringbuffer.append("\n- INDARA offset: ");
        stringbuffer.append(_OffsetToIndicatorArea);
        stringbuffer.append("\n- Subfile IO Buf offset: ");
        stringbuffer.append(_OffsetToSubfileArea);
        stringbuffer.append("\n- Number of Subfile records: ");
        stringbuffer.append(_NumSubfiles);
        stringbuffer.append("\n- Last relative record number: ");
        stringbuffer.append(_LastSflRRN);
        stringbuffer.append("\n- Subfile INDARA offset: ");
        stringbuffer.append(_OffsetToSubfileIndicatorArea);
        stringbuffer.append("\n- Offset to Next RF Header: ");
        stringbuffer.append(_OffsetToNextRFHeader);
        stringbuffer.append("\n");
        return stringbuffer.toString();
    }

    public boolean isDFRWRTNo()
    {
        return _DFRWRTNo;
    }

    public boolean isFRCDTA()
    {
        return _FRCDTA;
    }

    public boolean isSflComplete()
    {
        return _SflComplete;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 1999, 2002.  All Rights Reserved.";
    public static final int ADBD_RECORD_HEADER_LENGTH = 76;
    private String _RecordFormatName;
    private byte _RequestType;
    private int _SLNO;
    private boolean _inviteOn;
    private boolean _DFRWRTNo;
    private boolean _FRCDTA;
    private boolean _SflComplete;
    private int _AlwRolStrt;
    private int _AlwRolEnd;
    private int _AlwRolNumLines;
    private int _NumSubfiles;
    private int _LastSflRRN;
    private int _NextSflRRN;
    private int _OffsetToSubfileArea;
    private int _OffsetToSubfileIndicatorArea;
    private int _OffsetToNextRFHeader;
    private int _NextSflPageSize;
    private int _BytesRead;
    private int _OffsetToIndicatorArea;
    private int _IOBufferLength;
    private int _OffsetToIOBuffer;
    private int _recordHeaderLength;
    protected ITraceLogger _trace;
}
