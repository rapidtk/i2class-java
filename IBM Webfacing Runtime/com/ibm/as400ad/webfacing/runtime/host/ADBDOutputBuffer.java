// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.host;

import com.ibm.as400ad.webfacing.common.*;
import com.ibm.as400ad.webfacing.runtime.controller.*;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingLevelCheckException;
import com.ibm.as400ad.webfacing.runtime.model.*;
import com.ibm.as400ad.webfacing.runtime.model.def.IndicatorDataDefinition;
import com.ibm.as400ad.webfacing.runtime.view.SubfileControlRecordViewBean;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.host:
//            ADBDHeader, ADBDRecordHeader, WFCommunicationsException

public class ADBDOutputBuffer
    implements IReadOutputBuffer
{

    public ADBDOutputBuffer(DataInputStream datainputstream, RecordBeanFactory recordbeanfactory)
        throws IOException, WebfacingLevelCheckException, WebfacingInternalException, WFCommunicationsException
    {
        _trace = WFSession.getTraceLogger();
        _inputStream = datainputstream;
        _bytesRead = 0;
        _ApplicationRequests = new Vector();
        initializeFromOutputBuffer(recordbeanfactory);
    }

    public ADBDOutputBuffer(DataInputStream datainputstream, SubfileControlRecordViewBean subfilecontrolrecordviewbean, int i)
        throws IOException, WebfacingLevelCheckException
    {
        _trace = WFSession.getTraceLogger();
        _inputStream = datainputstream;
        _bytesRead = 0;
        initializeFromOutputBuffer(subfilecontrolrecordviewbean, i);
    }

    public Enumeration getApplicationRequests()
    {
        return _ApplicationRequests.elements();
    }

    public IDSPFObject getDSPFObject()
    {
        return _ADBDHeader;
    }

    public int getFieldCount()
        throws IOException
    {
        int i = readShort();
        if(isInBidiMode())
            i /= 2;
        return i;
    }

    public DataInputStream getInputStream()
    {
        return _inputStream;
    }

    public int getIOBufferLength()
    {
        return _ADBDRecordHeader.getIOBufferLength();
    }

    public String getJobCCSID()
    {
        return _ADBDHeader.getJobCCSID();
    }

    public int getNumberOfSubfileRecords()
    {
        return _ADBDRecordHeader.getNumberOfSubfileRecords();
    }

    public int getLastSflRRN()
    {
        return _ADBDRecordHeader.getLastSflRRN();
    }

    public String getRecordFormatName()
    {
        return _ADBDRecordHeader.getRecordFormatName();
    }

    public int getSLNO()
    {
        return _ADBDRecordHeader.getSLNO();
    }

    public boolean hasSubfileIndicatorArea()
    {
        return 0 < _ADBDRecordHeader.getOffsetToSubfileIndicatorArea();
    }

    public void incrementBytesRead(int i)
    {
        _currentRecordPosition += i;
        _currentBufferPosition += i;
        if(_currentBufferPosition > _bytesRead)
            _bytesRead = _currentBufferPosition;
    }

    private void initializeFromOutputBuffer(RecordBeanFactory recordbeanfactory)
        throws IOException, WebfacingLevelCheckException, WebfacingInternalException
    {
        _ADBDHeader = new ADBDHeader();
        _ADBDHeader.initializeFromOutputBuffer(_inputStream);
        incrementBytesRead(_ADBDHeader.length());
        if(_ADBDHeader.isFileOpened())
        {
            _ApplicationRequests.addElement(new ApplicationRequest((byte)17, _ADBDHeader));
            return;
        }
        if(_ADBDHeader.getOffsetToRecordFormatHeader() > 0)
        {
            Object obj = null;
            do
            {
                positionStreamToNextRecordHeader();
                _ADBDRecordHeader = new ADBDRecordHeader();
                _ADBDRecordHeader.initializeFromOutputBuffer(_inputStream);
                if(_ADBDRecordHeader.isINVITE())
                    throw new WebfacingInternalException(_resmri.getString("WF0039"));
                incrementBytesRead(_ADBDRecordHeader.length());
                byte byte0 = _ADBDRecordHeader.getRequestType();
                ApplicationRequest applicationrequest;
                switch(byte0)
                {
                case 5: // '\005'
                case 6: // '\006'
                    com.ibm.as400ad.webfacing.runtime.model.RecordDataBean recorddatabean = recordbeanfactory.createRecordDataBean(this);
                    applicationrequest = new ApplicationRequest(byte0, _ADBDHeader, recorddatabean);
                    break;

                case 1: // '\001'
                case 2: // '\002'
                    applicationrequest = new ApplicationRequest(byte0, _ADBDHeader, _ADBDRecordHeader.getRecordFormatName());
                    if(_ADBDRecordHeader.getOffsetToIndicatorArea() > 0)
                    {
                        positionStreamToIndicatorArea();
                        ResponseIndicators responseindicators = new ResponseIndicators(new IndicatorDataDefinition());
                        readIndicatorArea(responseindicators);
                        applicationrequest.setIndArea(responseindicators);
                    }
                    break;

                case 3: // '\003'
                case 4: // '\004'
                default:
                    throw new WebfacingInternalException(_resmri.getString("WF0013"));
                }
                _ApplicationRequests.addElement(applicationrequest);
            } while(_ADBDRecordHeader.getOffsetToNextRFHeader() > 0);
        }
        if(_ADBDHeader.isFileClose())
            _ApplicationRequests.addElement(new ApplicationRequest((byte)16, _ADBDHeader));
    }

    private void initializeFromOutputBuffer(SubfileControlRecordViewBean subfilecontrolrecordviewbean, int i)
        throws IOException, WebfacingLevelCheckException
    {
        _ADBDHeader = new ADBDHeader();
        _ADBDHeader.initializeFromOutputBuffer(_inputStream);
        incrementBytesRead(_ADBDHeader.length());
        if(_ADBDHeader.getOffsetToRecordFormatHeader() > 0)
        {
            positionStreamToNextRecordHeader();
            _ADBDRecordHeader = new ADBDRecordHeader();
            _ADBDRecordHeader.initializeFromOutputBuffer(_inputStream);
            incrementBytesRead(_ADBDRecordHeader.length());
            subfilecontrolrecordviewbean.updateForPaging(this, i);
        }
    }

    public boolean isInBidiMode()
    {
        return _ADBDHeader.isInBidiMode();
    }

    private void positionStream(int i)
        throws IOException
    {
        if(_currentRecordPosition < i)
        {
            int j = i - _currentRecordPosition;
            _inputStream.skipBytes(j);
            incrementBytesRead(j);
        } else
        if(_currentRecordPosition > i)
            throw new IOException(_resmri.getString("WF0014"));
    }

    public DataInputStream positionStreamToIndicatorArea()
        throws IOException
    {
        int i = _ADBDRecordHeader.getOffsetToIndicatorArea();
        positionStream(i);
        return _inputStream;
    }

    public DataInputStream positionStreamToIOBuffer()
        throws IOException
    {
        int i = _ADBDRecordHeader.getOffsetToIOBuffer();
        positionStream(i);
        return _inputStream;
    }

    private void positionStreamToNextRecordHeader()
        throws IOException
    {
        int i;
        if(_ADBDRecordHeader == null)
            i = _ADBDHeader.getOffsetToRecordFormatHeader();
        else
            i = _ADBDRecordHeader.getOffsetToNextRFHeader();
        positionStream(i);
        _currentRecordPosition = 0;
    }

    public DataInputStream positionStreamToSubfileIndicatorArea()
        throws IOException
    {
        int i = _ADBDRecordHeader.getOffsetToSubfileIndicatorArea();
        positionStream(i);
        return _inputStream;
    }

    public DataInputStream positionStreamToSubfileIOBuffer()
        throws IOException
    {
        int i = _ADBDRecordHeader.getOffsetToSubfileArea();
        if(i > 0)
            positionStream(i);
        return _inputStream;
    }

    public int readFieldCount()
        throws IOException
    {
        int i = readShort();
        return i;
    }

    public void readIndicatorArea(IIndicatorUpdate iindicatorupdate)
        throws IOException
    {
        try
        {
            int i = 1;
            byte byte0 = 0;
            for(int j = 0; j <= 11; j++)
            {
                byte0 = _inputStream.readByte();
                for(int k = 0; k <= 7; k++)
                    iindicatorupdate.setIndicator(i++, (byte0 & 128 >> k) > 0);

            }

            byte0 = _inputStream.readByte();
            iindicatorupdate.setIndicator(i, (byte0 & 0x80) > 0);
            iindicatorupdate.setIndicator(i + 1, (byte0 & 0x40) > 0);
            iindicatorupdate.setIndicator(i + 2, (byte0 & 0x20) > 0);
            incrementBytesRead(13);
        }
        catch(IOException ioexception)
        {
            _trace.err(2, "While reading separate indicator area for option indicators, received IO Exception " + ioexception.getMessage());
            throw ioexception;
        }
    }

    public void readIndicatorsFromIOBuffer(IIndicatorRead iindicatorread)
        throws IOException, WebfacingLevelCheckException
    {
        int i = 0;
        int j = 0;
        try
        {
            int k = readShort() / 2;
            List list = iindicatorread.getReferencedOptionIndicators();
            if(list.size() != k)
            {
                PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(_resmri.getString("WF0036"));
                paddedstringbuffer.replaceSubstring("&1", _ADBDRecordHeader.getRecordFormatName());
                paddedstringbuffer.replaceSubstring("&2", _ADBDHeader.getFileName());
                paddedstringbuffer.replaceSubstring("&3", Integer.toString(list.size()));
                paddedstringbuffer.replaceSubstring("&4", Integer.toString(k));
                StringBuffer stringbuffer = new StringBuffer();
                for(int l = 0; l < list.size(); l++)
                {
                    int i1 = ((Integer)list.get(l)).intValue();
                    stringbuffer.append(String.valueOf(i1) + ": ");
                }

                paddedstringbuffer.replaceSubstring("&5", stringbuffer.toString());
                _trace.err(2, paddedstringbuffer.toString());
                throw new WebfacingLevelCheckException(paddedstringbuffer.toString());
            }
            for(Iterator iterator = list.iterator(); iterator.hasNext();)
            {
                j = ((Integer)iterator.next()).intValue();
                char c = _inputStream.readChar();
                i += 2;
                switch(c)
                {
                case 49: // '1'
                    iindicatorread.setIndicator(j, true);
                    break;

                case 48: // '0'
                default:
                    iindicatorread.setIndicator(j, false);
                    break;
                }
            }

        }
        catch(IOException ioexception)
        {
            _trace.err(2, "IO error while reading option Indicator " + j + " from the IO buffer: " + ioexception.toString());
            throw ioexception;
        }
        incrementBytesRead(i);
    }

    public int readInt()
        throws IOException
    {
        int i;
        try
        {
            i = _inputStream.readInt();
            incrementBytesRead(4);
        }
        catch(IOException ioexception)
        {
            _trace.err(2, "Unexpected error while trying to read an int from the output buffer" + ioexception.toString());
            _trace.err(3, ioexception);
            throw ioexception;
        }
        return i;
    }

    public int readShort()
        throws IOException
    {
        int i;
        try
        {
            i = _inputStream.readUnsignedShort();
            incrementBytesRead(2);
        }
        catch(IOException ioexception)
        {
            _trace.err(2, "Unexpected error while trying to read a short from the output buffer" + ioexception.toString());
            _trace.err(3, ioexception);
            throw ioexception;
        }
        return i;
    }

    public IFieldData getFieldValue(IFormattableFieldData iformattablefielddata, boolean flag)
        throws IOException, WebfacingLevelCheckException
    {
        int i = readShort();
        boolean flag1 = (i & 0x8000) != 0;
        int j = (i & 0x7fff) / 2;
        iformattablefielddata.validateDataLength(j);
        StringBuffer stringbuffer = new StringBuffer(j);
        byte abyte0[] = null;
        try
        {
            int k = Integer.parseInt(getJobCCSID());
            for(int l = 0; l < j; l++)
            {
                char c = _inputStream.readChar();
                stringbuffer.append(c);
            }

            incrementBytesRead(j * 2);
            String s = stringbuffer.toString();
            if(WFSession.getOutputCharMappingProperties() != null)
            {
                com.ibm.as400ad.webfacing.common.MappingProperties mappingproperties = WFSession.getOutputCharMappingProperties();
                for(Enumeration enumeration = mappingproperties.propertyNames(); enumeration.hasMoreElements();)
                {
                    String s1 = (String)enumeration.nextElement();
                    String s2 = mappingproperties.getProperty(s1);
                    if(s2 != null && s1.length() == s2.length())
                        s = WebfacingConstants.replaceSubstring(s, s1, s2);
                }

            }
            if(flag1)
            {
                int i1 = readShort();
                abyte0 = new byte[i1];
                for(int j1 = 0; j1 < i1; j1++)
                    abyte0[j1] = _inputStream.readByte();

                incrementBytesRead(i1);
                if(!flag)
                    return new BidiFieldData(s, abyte0, iformattablefielddata, k);
            }
            if(flag)
                return new BidiOnWSMFieldData(s, iformattablefielddata, k);
            else
                return new FieldData(s, iformattablefielddata, k);
        }
        catch(IOException ioexception)
        {
            _trace.err(2, "IO Exception occurred while reading field " + iformattablefielddata.getFieldName() + "from the IO Buffer for record " + _ADBDRecordHeader.getRecordFormatName());
            if(stringbuffer.length() < j)
                _trace.err(2, "Only able to read in the following field value :" + stringbuffer.toString() + ": \n" + "due to the error " + ioexception.toString());
            else
                _trace.err(2, "Only able to read in the following ebcdic field value :" + (abyte0 != null ? abyte0.toString() : "") + ": \n" + "due to the error " + ioexception.toString());
            _trace.err(3, ioexception);
            throw ioexception;
        }
    }

    public boolean isDFRWRTNo()
    {
        return _ADBDRecordHeader.isDFRWRTNo();
    }

    public boolean isFRCDTA()
    {
        return _ADBDRecordHeader.isFRCDTA();
    }

    public boolean isSflComplete()
    {
        return _ADBDRecordHeader.isSflComplete();
    }

    public static final String copyRight = "(C) Copyright IBM Corporation 1999-2003 all rights reserved";
    private static ResourceBundle _resmri;
    private DataInputStream _inputStream;
    private int _currentRecordPosition;
    private int _currentBufferPosition;
    private int _bytesRead;
    private ADBDHeader _ADBDHeader;
    private ADBDRecordHeader _ADBDRecordHeader;
    private Vector _ApplicationRequests;
    protected ITraceLogger _trace;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
