// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import com.ibm.as400ad.webfacing.runtime.controller.ILibraryFile;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingLevelCheckException;
import com.ibm.as400ad.webfacing.runtime.model.*;
import com.ibm.as400ad.webfacing.runtime.model.def.IRecordDataDefinition;
import com.ibm.as400ad.webfacing.runtime.view.*;
import com.ibm.as400ad.webfacing.runtime.view.def.AIDKey;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.*;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.host:
//            ADBDHeader, ADBDRecordHeader, IWFInputBuffer

public class ADBDInputBuffer
    implements IWFInputBuffer
{

    public ADBDInputBuffer(IRecordSaveArea irecordsavearea, boolean flag)
        throws IOException, WebfacingInternalException, WebfacingLevelCheckException
    {
        _InputBufferLength = 0;
        _trace = WFSession.getTraceLogger();
        _activeRecord = irecordsavearea.mapRecordBeingRead();
        _hasSeparateIndicatorArea = _activeRecord.getRecordDataDefinition().hasSeparateIndicatorArea();
        if(flag)
            _changedSubfileControlRecords = null;
        else
            _changedSubfileControlRecords = irecordsavearea.getChangedSubfileControlRecords();
        irecordsavearea.postProcessRead();
        ILibraryFile ilibraryfile = _activeRecord.getDSPFObject();
        buildADBDHeader(ilibraryfile, irecordsavearea.getAIDKey(), irecordsavearea.getCursorBufferData(), irecordsavearea.isNoResponseIndOnAID(), irecordsavearea.getSFLLowestRRN());
        _IOBuffer = new ByteArrayOutputStream();
        _activeRecord.writeIOBuffer(_IOBuffer);
        _InputBufferLength = _header.length();
        _InputBufferLength += initializeRecordHeader(irecordsavearea);
    }

    public ADBDInputBuffer(IRecordSaveArea irecordsavearea, IInputBufferSaveArea iinputbuffersavearea, boolean flag, int i, int j)
        throws IOException, WebfacingInternalException, WebfacingLevelCheckException
    {
        _InputBufferLength = 0;
        _trace = WFSession.getTraceLogger();
        _activeRecord = iinputbuffersavearea;
        _hasSeparateIndicatorArea = _activeRecord.getRecordDataDefinition().hasSeparateIndicatorArea();
        if(flag)
        {
            _changedSubfileControlRecords = new Vector(1);
            _changedSubfileControlRecords.add(iinputbuffersavearea);
        }
        ILibraryFile ilibraryfile = _activeRecord.getDSPFObject();
        buildADBDHeader(ilibraryfile, irecordsavearea.getAIDKey());
        _InputBufferLength = _header.length();
        _InputBufferLength += initializeRecordHeader(irecordsavearea, i, j);
    }

    private void buildADBDHeader(ILibraryFile ilibraryfile, String s, ICursorBufferData icursorbufferdata, boolean flag, int i)
    {
        buildADBDHeader(ilibraryfile, s);
        _header.setCursorPosition((byte)icursorbufferdata.getAbsolutePosition().getRow(), (byte)icursorbufferdata.getAbsolutePosition().getColumn());
        _header.setWindowRelativeCursorPosition((byte)icursorbufferdata.getWinRelativePositionForBuffer().getRow(), (byte)icursorbufferdata.getWinRelativePositionForBuffer().getColumn());
        _header.setNoRIforAID(flag);
        _header.setSFLLowestRRN(i);
    }

    private void buildADBDHeader(ILibraryFile ilibraryfile, String s)
    {
        _header = new ADBDHeader();
        _header.setDSPFObject(ilibraryfile);
        AIDKey aidkey = new AIDKey(s);
        _header.setAIDKey(aidkey.getKeyCode().byteValue());
        _header.setOffsetToRecordFormatHeader(_header.length());
    }

    private int initializeRecordHeader(IRecordSaveArea irecordsavearea)
        throws IOException
    {
        _recordHeader = new ADBDRecordHeader();
        int i = _recordHeader.length();
        StringBuffer stringbuffer = new StringBuffer("          ");
        stringbuffer.insert(0, _activeRecord.getUntransformedRecordName());
        stringbuffer.setLength(10);
        _recordHeader.setRecordFormatName(stringbuffer.toString());
        int j = _IOBuffer.size();
        _recordHeader.setIOBufferLength(j);
        i += j;
        if(j > 0)
            _recordHeader.setOffsetToIOBuffer(_recordHeader.length());
        else
            _recordHeader.setOffsetToIOBuffer(0);
        if(_hasSeparateIndicatorArea)
        {
            _recordHeader.setOffsetToIndicatorArea(_recordHeader.length() + j);
            i += 13;
            _indicatorArea = irecordsavearea.getIndicatorArea();
        } else
        {
            _recordHeader.setOffsetToIndicatorArea(0);
        }
        i += updateRecordHeaderWithSubfileInfo(i, 0);
        return i;
    }

    private int initializeRecordHeader(IRecordSaveArea irecordsavearea, int i, int j)
        throws IOException
    {
        _recordHeader = new ADBDRecordHeader();
        int k = _recordHeader.length();
        StringBuffer stringbuffer = new StringBuffer("          ");
        stringbuffer.insert(0, _activeRecord.getUntransformedRecordName());
        stringbuffer.setLength(10);
        _recordHeader.setRecordFormatName(stringbuffer.toString());
        _recordHeader.setIOBufferLength(0);
        _recordHeader.setOffsetToIOBuffer(0);
        _recordHeader.setOffsetToIndicatorArea(0);
        _recordHeader.setNextSflPageSize(j);
        k += updateRecordHeaderWithSubfileInfo(k, i);
        return k;
    }

    public int length()
    {
        return _InputBufferLength;
    }

    private void printIOBuffer()
    {
    }

    public void toStream(OutputStream outputstream)
        throws IOException
    {
        DataOutputStream dataoutputstream = new DataOutputStream(outputstream);
        _header.toStream(dataoutputstream);
        _recordHeader.toStream(dataoutputstream);
        if(_IOBuffer != null)
            dataoutputstream.write(_IOBuffer.toByteArray());
        if(_hasSeparateIndicatorArea && _indicatorArea != null)
            _indicatorArea.writeIndicatorArea(dataoutputstream);
        if(_hasChangedSubfiles)
        {
            _subfileIOBuffers.writeTo(dataoutputstream);
            if(_hasSeparateIndicatorArea)
            {
                for(int i = 0; i < _changedSubfileControlRecords.size(); i++)
                {
                    SubfileControlRecordDataBean subfilecontrolrecorddatabean = (SubfileControlRecordDataBean)_changedSubfileControlRecords.get(i);
                    Vector vector = subfilecontrolrecorddatabean.getChangedRecords();
                    for(int j = 0; j < vector.size(); j++)
                    {
                        ISubfileInputBufferSaveArea isubfileinputbuffersavearea = (ISubfileInputBufferSaveArea)vector.get(j);
                        isubfileinputbuffersavearea.getResponseIndArea().writeIndicatorArea(dataoutputstream);
                    }

                }

            }
        }
        dataoutputstream.close();
    }

    private int updateRecordHeaderWithSubfileInfo(int i, int j)
    {
        int k = 0;
        int l = 0;
        int i1 = 0;
        _hasChangedSubfiles = _changedSubfileControlRecords != null && _changedSubfileControlRecords.size() > 0;
        if(_hasChangedSubfiles)
        {
            _subfileIOBuffers = new ByteArrayOutputStream();
            for(int j1 = 0; j1 < _changedSubfileControlRecords.size(); j1++)
            {
                ISFLCTLInputBufferSaveArea isflctlinputbuffersavearea = (ISFLCTLInputBufferSaveArea)_changedSubfileControlRecords.get(j1);
                Vector vector = isflctlinputbuffersavearea.getChangedRecords();
                for(int k1 = 0; k1 < vector.size();)
                {
                    ((ISubfileInputBufferSaveArea)vector.get(k1)).writeSubfileInputBuffer(_subfileIOBuffers);
                    k1++;
                    l++;
                }

                if(_hasSeparateIndicatorArea)
                    i1 += 13 * vector.size();
            }

            k = _subfileIOBuffers.size();
            _recordHeader.setOffsetToSubfileArea(i);
            _recordHeader.setNumberOfSubfileRecords(l);
            if(_hasSeparateIndicatorArea)
                _recordHeader.setOffsetToSubfileIndicatorArea(i + k);
            else
                _recordHeader.setOffsetToSubfileIndicatorArea(0);
        } else
        {
            _recordHeader.setOffsetToSubfileArea(0);
            _recordHeader.setOffsetToSubfileIndicatorArea(0);
            _recordHeader.setNumberOfSubfileRecords(0);
        }
        _recordHeader.setNextSflRRN(j);
        return k + i1;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 1999, 2002.  All Rights Reserved.";
    private ByteArrayOutputStream _IOBuffer;
    private ADBDRecordHeader _recordHeader;
    private ADBDHeader _header;
    private IInputBufferSaveArea _activeRecord;
    private IIndicatorArea _indicatorArea;
    private Vector _changedSubfileControlRecords;
    private int _InputBufferLength;
    private boolean _hasSeparateIndicatorArea;
    private boolean _hasChangedSubfiles;
    private ByteArrayOutputStream _subfileIOBuffers;
    protected ITraceLogger _trace;
}
