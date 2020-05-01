// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import java.io.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model:
//            FieldData, IFormattableFieldData

public class BidiFieldData extends FieldData
{

    public void toInputBuffer(DataOutputStream dataoutputstream)
    {
        try
        {
            if(super._modified)
            {
                super.toInputBuffer(dataoutputstream);
            } else
            {
                int i = _ebcdicValue.length | 0x8000;
                dataoutputstream.writeShort(i);
                dataoutputstream.write(_ebcdicValue);
            }
        }
        catch(IOException ioexception) { }
    }

    public BidiFieldData(String s, byte abyte0[], IFormattableFieldData iformattablefielddata, int i)
    {
        super(s, iformattablefielddata, i);
        _ebcdicValue = abyte0;
    }

    private byte _ebcdicValue[];
}
