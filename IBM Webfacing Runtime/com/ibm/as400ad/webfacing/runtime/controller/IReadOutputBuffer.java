// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.controller;

import com.ibm.as400ad.webfacing.runtime.core.WebfacingLevelCheckException;
import com.ibm.as400ad.webfacing.runtime.model.*;
import java.io.DataInputStream;
import java.io.IOException;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.controller:
//            IDSPFObject

public interface IReadOutputBuffer
{

    public abstract IDSPFObject getDSPFObject();

    public abstract IFieldData getFieldValue(IFormattableFieldData iformattablefielddata, boolean flag)
        throws IOException, WebfacingLevelCheckException;

    public abstract DataInputStream getInputStream();

    public abstract int getIOBufferLength();

    public abstract String getJobCCSID();

    public abstract int getNumberOfSubfileRecords();

    public abstract int getLastSflRRN();

    public abstract String getRecordFormatName();

    public abstract int getSLNO();

    public abstract boolean hasSubfileIndicatorArea();

    public abstract void incrementBytesRead(int i);

    public abstract DataInputStream positionStreamToIndicatorArea()
        throws IOException;

    public abstract DataInputStream positionStreamToIOBuffer()
        throws IOException;

    public abstract DataInputStream positionStreamToSubfileIndicatorArea()
        throws IOException;

    public abstract DataInputStream positionStreamToSubfileIOBuffer()
        throws IOException;

    public abstract int readFieldCount()
        throws IOException;

    public abstract void readIndicatorArea(IIndicatorUpdate iindicatorupdate)
        throws IOException;

    public abstract void readIndicatorsFromIOBuffer(IIndicatorRead iindicatorread)
        throws IOException, WebfacingLevelCheckException;

    public abstract int readInt()
        throws IOException;

    public abstract int readShort()
        throws IOException;

    public abstract boolean isDFRWRTNo();

    public abstract boolean isFRCDTA();

    public abstract boolean isSflComplete();
}
