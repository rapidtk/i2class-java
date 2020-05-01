// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.fldformat;

import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.runtime.model.IFormattableFieldData;
import com.ibm.as400ad.webfacing.util.Assert;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.fldformat:
//            FieldDataFormatter

public class ZFieldFormatter extends FieldDataFormatter
{

    public ZFieldFormatter()
    {
    }

    public String format(int i, IFormattableFieldData iformattablefielddata, String s)
    {
        s = s.trim();
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(s);
        if(s.length() == 0)
            return "0001-01-01-01.01.01.000000";
        paddedstringbuffer.truncateAllSpacesFromLeft();
        s = paddedstringbuffer.toString();
        if(paddedstringbuffer.isAllNumbers())
        {
            Assert.assertTrue("TimeStamp data is invalid.", paddedstringbuffer.toString().length() == 20);
            paddedstringbuffer.setValue(s.substring(0, 4));
            paddedstringbuffer.concat("-", s.substring(4, 6), "-", s.substring(6, 8), "-", s.substring(8, 10), ".", s.substring(10, 12), ".", s.substring(12, 14), ".", s.substring(14, 20));
        } else
        {
            Assert.assertTrue("TimeStamp data is invalid.", paddedstringbuffer.toString().length() == 26);
        }
        return paddedstringbuffer.toString();
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2001-2002");

}
