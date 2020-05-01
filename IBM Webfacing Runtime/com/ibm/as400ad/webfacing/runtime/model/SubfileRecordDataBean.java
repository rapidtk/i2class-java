// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.controller.IDSPFObject;
import com.ibm.as400ad.webfacing.runtime.controller.IReadOutputBuffer;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingLevelCheckException;
import com.ibm.as400ad.webfacing.runtime.model.def.FieldDataDefinition;
import com.ibm.as400ad.webfacing.runtime.model.def.IRecordDataDefinition;
import java.io.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model:
//            RecordDataBean, ISubfileInputBufferSaveArea, BaseIndicators, ResponseIndicators, 
//            IIndicatorArea

public class SubfileRecordDataBean extends RecordDataBean
    implements ISubfileInputBufferSaveArea
{

    protected SubfileRecordDataBean(IRecordDataDefinition irecorddatadefinition, IReadOutputBuffer ireadoutputbuffer)
        throws IOException, WebfacingLevelCheckException
    {
        super(irecorddatadefinition, ireadoutputbuffer);
        _changedIndirectly = false;
    }

    public IIndicatorArea getResponseIndArea()
    {
        return getResponseIndicators();
    }

    public int getRRN()
    {
        return _rrn;
    }

    public String getUntransformedRecordName()
    {
        return WebfacingConstants.undoReplaceSpecialChars(getRecordName());
    }

    protected boolean hasEbcdicOnWSM(FieldDataDefinition fielddatadefinition)
    {
        return getIDSPFObject().isInBidiMode();
    }

    protected void initializeFromOutputBuffer(IReadOutputBuffer ireadoutputbuffer)
        throws IOException, WebfacingLevelCheckException
    {
        initDSPFObject(ireadoutputbuffer.getDSPFObject());
        _rrn = ireadoutputbuffer.readInt();
        initializeFromIOBuffer(ireadoutputbuffer, getRecordDataDefinition().getFieldDefinitions());
    }

    public boolean isChangedIndirectly()
    {
        return _changedIndirectly;
    }

    void readIndicatorArea(IReadOutputBuffer ireadoutputbuffer)
        throws IOException
    {
        OptionIndicators optionindicators = getOptionIndicators();
        ResponseIndicators responseindicators = getResponseIndicators();
        ireadoutputbuffer.readIndicatorArea(optionindicators);
        BaseIndicators.copyIndicators(optionindicators, responseindicators);
        responseindicators.clearReferencedResponseIndicators();
    }

    public void setChangedIndirectly(boolean flag)
    {
        _changedIndirectly = flag;
    }

    public void writeSubfileInputBuffer(OutputStream outputstream)
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        writeIOBuffer(bytearrayoutputstream, getRecordDataDefinition().getFieldDefinitions());
        StringBuffer stringbuffer = new StringBuffer("          ");
        stringbuffer.insert(0, getUntransformedRecordName());
        stringbuffer.setLength(10);
        DataOutputStream dataoutputstream = new DataOutputStream(outputstream);
        try
        {
            dataoutputstream.writeInt(getRRN());
            dataoutputstream.writeInt(bytearrayoutputstream.size());
            dataoutputstream.writeChars(stringbuffer.toString());
            if(!isChangedIndirectly())
                dataoutputstream.writeByte(0);
            else
                dataoutputstream.writeByte(128);
            dataoutputstream.write(bytearrayoutputstream.toByteArray());
        }
        catch(IOException ioexception) { }
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 1999, 2002.  All Rights Reserved.";
    private int _rrn;
    private boolean _changedIndirectly;
}
