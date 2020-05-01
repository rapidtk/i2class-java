// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import com.ibm.as400ad.webfacing.runtime.controller.*;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.*;

public class ADBDHeader
    implements IDSPFObject
{

    public ADBDHeader()
    {
        _OffsetToRecordFormatHeader = 0;
        _fileOpen = true;
        _SFLLowestRRN = 0;
        _jobCCSID = 0;
        _trace = WFSession.getTraceLogger();
        setOffsetToRecordFormatHeader(60);
    }

    public void close()
    {
        _fileOpen = false;
    }

    public boolean equalsDSPF(IDSPFObject idspfobject)
    {
        return getName().equals(idspfobject.getName());
    }

    public byte getAIDKey()
    {
        return _AIDKey;
    }

    public byte[] getCursorPosition()
    {
        byte abyte0[] = new byte[2];
        abyte0[0] = _CursorRow;
        abyte0[1] = _CursorCol;
        return abyte0;
    }

    public String getFileName()
    {
        return _FileName;
    }

    public String getHostName()
    {
        return _LibraryName + "/" + _FileName;
    }

    public String getJobCCSID()
    {
        return Integer.toString(_jobCCSID);
    }

    public String getLibraryName()
    {
        return _LibraryName;
    }

    public String getName()
    {
        return _LibraryName + "_" + _FileName;
    }

    public int getOffsetToRecordFormatHeader()
    {
        return _OffsetToRecordFormatHeader;
    }

    public byte[] getWindowRelativeCursorPosition()
    {
        byte abyte0[] = new byte[2];
        abyte0[0] = _WindowRelativeCursorRow;
        abyte0[1] = _WindowRelativeCursorCol;
        return abyte0;
    }

    protected void initializeFromOutputBuffer(DataInputStream datainputstream)
        throws IOException
    {
        StringBuffer stringbuffer = new StringBuffer(10);
        for(int i = 1; i <= 10; i++)
            stringbuffer.append(datainputstream.readChar());

        _FileName = stringbuffer.toString().trim();
        StringBuffer stringbuffer1 = new StringBuffer(10);
        for(int j = 1; j <= 10; j++)
            stringbuffer1.append(datainputstream.readChar());

        _LibraryName = stringbuffer1.toString().trim();
        _AIDKey = datainputstream.readByte();
        _CursorRow = datainputstream.readByte();
        _CursorCol = datainputstream.readByte();
        _WindowRelativeCursorRow = datainputstream.readByte();
        _WindowRelativeCursorCol = datainputstream.readByte();
        byte byte0 = datainputstream.readByte();
        _ADBD_RSTDSP = (byte0 & 0x80) > 0;
        _ADBD_IGCDTA = (byte0 & 0x40) > 0;
        _ADBD_SHARE = (byte0 & 0x20) > 0;
        _ADBD_FileClose = (byte0 & 8) > 0;
        _ADBD_FileOpened = (byte0 & 4) > 0;
        _ADBD_BidiMode = (byte0 & 2) > 0;
        datainputstream.readByte();
        datainputstream.readByte();
        _OffsetToRecordFormatHeader = datainputstream.readInt();
        _SFLLowestRRN = datainputstream.readInt();
        _jobCCSID = datainputstream.readInt();
    }

    public boolean isFileClose()
    {
        return _ADBD_FileClose;
    }

    public boolean isFileOpened()
    {
        return _ADBD_FileOpened;
    }

    public boolean isIGCDTA()
    {
        return _ADBD_IGCDTA;
    }

    public boolean isInBidiMode()
    {
        return _ADBD_BidiMode;
    }

    public boolean isKEEP()
    {
        return _keepSpecified;
    }

    public boolean isOpen()
    {
        return _fileOpen;
    }

    public boolean isRSTDSP()
    {
        return _ADBD_RSTDSP;
    }

    public boolean isSHARE()
    {
        return _ADBD_SHARE;
    }

    public int length()
    {
        return 60;
    }

    public void setAIDKey(byte byte0)
    {
        _AIDKey = byte0;
    }

    public void setCursorPosition(byte byte0, byte byte1)
    {
        _CursorRow = byte0;
        _CursorCol = byte1;
    }

    public void setDSPFObject(ILibraryFile ilibraryfile)
    {
        StringBuffer stringbuffer = new StringBuffer("          ");
        stringbuffer.insert(0, ilibraryfile.getLibraryName());
        stringbuffer.setLength(10);
        StringBuffer stringbuffer1 = new StringBuffer("          ");
        stringbuffer1.insert(0, ilibraryfile.getFileName());
        stringbuffer1.setLength(10);
        _LibraryName = stringbuffer.toString();
        _FileName = stringbuffer1.toString();
    }

    public void setKEEP(boolean flag)
    {
        _keepSpecified = flag;
    }

    public void setNoRIforAID(boolean flag)
    {
        _ADBD_NoRIforAID = flag;
    }

    public void setOffsetToRecordFormatHeader(int i)
    {
        _OffsetToRecordFormatHeader = i;
    }

    public void setSFLLowestRRN(int i)
    {
        _SFLLowestRRN = i;
    }

    public void setWindowRelativeCursorPosition(byte byte0, byte byte1)
    {
        _WindowRelativeCursorRow = byte0;
        _WindowRelativeCursorCol = byte1;
    }

    protected void toStream(DataOutputStream dataoutputstream)
        throws IOException
    {
        dataoutputstream.writeChars(_FileName);
        dataoutputstream.writeChars(_LibraryName);
        dataoutputstream.writeByte(_AIDKey);
        dataoutputstream.writeByte(_CursorRow);
        dataoutputstream.writeByte(_CursorCol);
        dataoutputstream.writeByte(_WindowRelativeCursorRow);
        dataoutputstream.writeByte(_WindowRelativeCursorCol);
        if(_ADBD_NoRIforAID)
            dataoutputstream.writeByte(16);
        else
            dataoutputstream.writeByte(0);
        dataoutputstream.writeByte(0);
        dataoutputstream.writeByte(0);
        dataoutputstream.writeInt(60);
        dataoutputstream.writeInt(_SFLLowestRRN);
        dataoutputstream.writeInt(0);
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("\nADBD HEADER:");
        stringbuffer.append("\n- Filename: ");
        stringbuffer.append(_FileName);
        stringbuffer.append("\n- Library name: ");
        stringbuffer.append(_LibraryName);
        stringbuffer.append("\n- AIDKey: ");
        stringbuffer.append(_AIDKey);
        stringbuffer.append("\n- Cursor row " + _CursorRow);
        stringbuffer.append("\n- Cursor col " + _CursorCol);
        stringbuffer.append("\n- Wdw relative cursor row: " + _WindowRelativeCursorRow);
        stringbuffer.append("\n- Wdw relative cursor col: " + _WindowRelativeCursorCol);
        stringbuffer.append("\n- RSTDSP: " + _ADBD_RSTDSP);
        stringbuffer.append("\n- IGCDTA: " + _ADBD_IGCDTA);
        stringbuffer.append("\n- SHARE: " + _ADBD_SHARE);
        stringbuffer.append("\n- NoRIforAID: " + _ADBD_NoRIforAID);
        stringbuffer.append("\n- FileClose: " + _ADBD_FileClose);
        stringbuffer.append("\n- FileOpen: " + _ADBD_FileOpened);
        stringbuffer.append("\n- Offset to record header: ");
        stringbuffer.append(_OffsetToRecordFormatHeader);
        stringbuffer.append("\n- Lowest RRN of top subfile: ");
        stringbuffer.append(_SFLLowestRRN);
        stringbuffer.append("\n- Job CCSID: ");
        stringbuffer.append(_jobCCSID);
        return stringbuffer.toString();
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 1999, 2002.  All Rights Reserved.";
    public static final int ADBD_HEADER_LENGTH = 60;
    private byte _AIDKey;
    private byte _CursorRow;
    private byte _CursorCol;
    private byte _WindowRelativeCursorRow;
    private byte _WindowRelativeCursorCol;
    private int _OffsetToRecordFormatHeader;
    private String _FileName;
    private String _LibraryName;
    private boolean _ADBD_IGCDTA;
    private boolean _ADBD_RSTDSP;
    private boolean _ADBD_SHARE;
    private boolean _ADBD_NoRIforAID;
    private boolean _ADBD_FileClose;
    private boolean _ADBD_BidiMode;
    private boolean _fileOpen;
    private boolean _keepSpecified;
    private boolean _ADBD_FileOpened;
    private int _SFLLowestRRN;
    private int _jobCCSID;
    protected ITraceLogger _trace;
}
