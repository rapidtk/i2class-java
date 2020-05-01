// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.fldformat;

import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.runtime.model.IFormattableFieldData;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.fldformat:
//            FieldDataFormatter

public class OFieldFormatter extends FieldDataFormatter
{

    public OFieldFormatter()
    {
    }

    public String format(int i, IFormattableFieldData iformattablefielddata, String s)
    {
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(formatForMSG(i, iformattablefielddata, s));
        if(iformattablefielddata.getCheckAttr().indexOf("LC") < 0)
            paddedstringbuffer.toUpperCaseIgnoreDBCS(i);
        return paddedstringbuffer.toString();
    }

    public String formatForMSG(int i, IFormattableFieldData iformattablefielddata, String s)
    {
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(s);
        boolean flag = false;
        int j = paddedstringbuffer.toStringBuffer().length();
        int k = 0;
        int l = iformattablefielddata.getFieldLength();
        int i1 = -1;
        for(int j1 = 0; j1 < j; j1++)
        {
            int k1 = k;
            boolean flag1 = paddedstringbuffer.isCharDBCS(j1, i);
            if(flag1)
                k += 2;
            else
                k++;
            if(flag1 && !flag)
                k += 2;
            flag = flag1;
            if(k == l)
            {
                i1 = j1 + 1;
                break;
            }
            if(k <= l)
                continue;
            for(int l1 = 0; l1 < l - k1; l1++)
                if(j > j1 + l1)
                    paddedstringbuffer.setCharAt(j1 + l1, ' ');
                else
                    paddedstringbuffer.append(' ');

            i1 = (j1 + l) - k1;
            break;
        }

        if(i1 == -1)
            i1 = (j + l) - k;
        paddedstringbuffer.padRightOrTruncateToLength(i1, ' ');
        return paddedstringbuffer.toString();
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2001-2002");

}
