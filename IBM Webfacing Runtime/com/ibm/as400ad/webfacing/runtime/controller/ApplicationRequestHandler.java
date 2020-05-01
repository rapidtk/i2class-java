// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.controller;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.runtime.host.ADBDOutputBuffer;
import com.ibm.as400ad.webfacing.runtime.host.WFCommunicationsException;
import com.ibm.as400ad.webfacing.runtime.model.IIndicatorArea;
import com.ibm.as400ad.webfacing.runtime.model.RecordDataBean;
import com.ibm.as400ad.webfacing.runtime.view.IRecordOperations;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.*;
import java.util.Enumeration;
import java.util.ResourceBundle;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.controller:
//            ApplicationRequest, WFSession, RecordBeanFactory, IDSPFObject

public class ApplicationRequestHandler
{

    public ApplicationRequestHandler(IRecordOperations irecordoperations)
    {
        _trace = WFSession.getTraceLogger();
        _recordOperations = irecordoperations;
    }

    public int processApplicationRequests(InputStream inputstream, boolean flag, RecordBeanFactory recordbeanfactory, boolean flag1)
        throws IOException, WFApplicationRuntimeError, WebfacingLevelCheckException, WebfacingInternalException, WFUnsupportedException, WFCommunicationsException
    {
        int i = 2;
        DataInputStream datainputstream = new DataInputStream(inputstream);
        ADBDOutputBuffer adbdoutputbuffer = new ADBDOutputBuffer(datainputstream, recordbeanfactory);
        _recordOperations.setJobCCSID(adbdoutputbuffer.getJobCCSID());
        _recordOperations.setIsInBiDiMode(adbdoutputbuffer.isInBidiMode());
        Enumeration enumeration = adbdoutputbuffer.getApplicationRequests();
        Object obj = null;
        Object obj1 = null;
        while(enumeration.hasMoreElements()) 
        {
            ApplicationRequest applicationrequest = (ApplicationRequest)enumeration.nextElement();
            switch(applicationrequest.getRequestType())
            {
            case 5: // '\005'
                RecordDataBean recorddatabean = applicationrequest.getRecordDataBean();
                boolean flag2 = recorddatabean.isDeferWrite(flag);
                i = writeRecord(applicationrequest.getDSPFObject(), recorddatabean, _recordOperations, flag2, recordbeanfactory, flag1);
                break;

            case 6: // '\006'
                RecordDataBean recorddatabean1 = applicationrequest.getRecordDataBean();
                IDSPFObject idspfobject = applicationrequest.getDSPFObject();
                writeRecord(idspfobject, recorddatabean1, _recordOperations, true, recordbeanfactory, flag1);
                i = readRecord(idspfobject, recorddatabean1.getRecordName(), _recordOperations, applicationrequest.getIndArea());
                break;

            case 1: // '\001'
            case 2: // '\002'
                i = readRecord(applicationrequest.getDSPFObject(), applicationrequest.getRecordName(), _recordOperations, applicationrequest.getIndArea());
                break;

            case 7: // '\007'
                throw new WebfacingInternalException(_resmri.getString("WF0002"));

            case 16: // '\020'
                _recordOperations.close(applicationrequest.getDSPFObject());
                i = 2;
                break;

            case 17: // '\021'
                _recordOperations.open(applicationrequest.getDSPFObject());
                i = 2;
                break;

            case 3: // '\003'
            case 4: // '\004'
            case 8: // '\b'
            case 9: // '\t'
            case 10: // '\n'
            case 11: // '\013'
            case 12: // '\f'
            case 13: // '\r'
            case 14: // '\016'
            case 15: // '\017'
            default:
                throw new WebfacingInternalException(WebfacingConstants.replaceSubstring(_resmri.getString("WF0003"), "&1", Byte.toString(applicationrequest.getRequestType())));
            }
        }
        return i;
    }

    public int writeRecord(IDSPFObject idspfobject, RecordDataBean recorddatabean, IRecordOperations irecordoperations, boolean flag, RecordBeanFactory recordbeanfactory, boolean flag1)
        throws WebfacingInternalException, WebfacingLevelCheckException, WFApplicationRuntimeError
    {
        com.ibm.as400ad.webfacing.runtime.view.RecordViewBean recordviewbean = irecordoperations.write(idspfobject, recorddatabean, flag, recordbeanfactory, flag1);
        return recordviewbean == null || flag ? 2 : 3;
    }

    public int readRecord(IDSPFObject idspfobject, String s, IRecordOperations irecordoperations, IIndicatorArea iindicatorarea)
        throws WFApplicationRuntimeError, WebfacingInternalException, WebfacingLevelCheckException
    {
        boolean flag = irecordoperations.read(idspfobject, s, iindicatorarea);
        return !flag ? 1 : 0;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 1999-2003.  All Rights Reserved.";
    public static final int REQUEST_INPUT_FROM_DEVICE = 0;
    public static final int READ_FROM_SAVE_AREA = 1;
    public static final int WAIT_FOR_READ = 2;
    public static final int IMMEDIATE_WRITE = 3;
    private static ResourceBundle _resmri;
    IRecordOperations _recordOperations;
    protected ITraceLogger _trace;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
