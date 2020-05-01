// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import java.io.DataOutputStream;
import java.io.IOException;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model:
//            FieldData, IFormattableFieldData

public class BidiOnWSMFieldData extends FieldData
{

    public void toInputBuffer(DataOutputStream dataoutputstream)
    {
        try
        {
            if(super._modified)
                super.toInputBuffer(dataoutputstream);
            else
                dataoutputstream.writeShort(16384);
        }
        catch(IOException ioexception) { }
    }

    public BidiOnWSMFieldData(String s, IFormattableFieldData iformattablefielddata, int i)
    {
        super(s, iformattablefielddata, i);
    }
}
