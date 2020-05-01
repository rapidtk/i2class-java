// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import com.ibm.as400ad.webfacing.runtime.controller.IReadOutputBuffer;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingLevelCheckException;
import com.ibm.as400ad.webfacing.runtime.model.def.IRecordDataDefinition;
import java.io.IOException;
import java.util.List;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model:
//            IRecordData, ISFLCTLInputBufferSaveArea

public interface ISFLCTLRecordData
    extends IRecordData
{

    public abstract void addToChangedRecords(int i);

    public abstract int getLastRecordNumber();

    public abstract int getNumberOfRecords();

    public abstract int getPageSize();

    public abstract ISFLCTLInputBufferSaveArea getSFLCTLInputBufferSaveArea();

    public abstract IRecordDataDefinition getSubfileRecordDefinition();

    public abstract List getSubfileRecords();

    public abstract int getSubfileSize();

    public abstract void updateSubfileInfo(int i, int j);

    public abstract boolean isSubfileComplete();

    public abstract void updateForPaging(IReadOutputBuffer ireadoutputbuffer, int i)
        throws IOException, WebfacingLevelCheckException;
}
