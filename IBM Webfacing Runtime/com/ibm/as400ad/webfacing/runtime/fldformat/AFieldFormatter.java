// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.fldformat;

import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.runtime.model.IFormattableFieldData;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.fldformat:
//            FieldDataFormatter

public class AFieldFormatter extends FieldDataFormatter
{

    public AFieldFormatter()
    {
    }

    public String format(int i, IFormattableFieldData iformattablefielddata, String s)
    {
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(s);
        String s1 = iformattablefielddata.getCheckAttr();
        int j = s.length() - 1;
        if(s1.indexOf("LC") < 0 && j >= 0)
            if(j > 1 && s.charAt(0) == '"' && s.charAt(j) == '"' && s1.indexOf("VE") >= 0)
            {
                paddedstringbuffer.deleteCharAt(j);
                paddedstringbuffer.deleteCharAt(0);
            } else
            if(iformattablefielddata.getKeyboardShift() != 'W')
                paddedstringbuffer.toUpperCase();
        paddedstringbuffer.padRightOrTruncateToLength(iformattablefielddata.getFieldLength(), ' ');
        return paddedstringbuffer.toString();
    }

    public String formatForMSG(int i, IFormattableFieldData iformattablefielddata, String s)
    {
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(s);
        paddedstringbuffer.padRightOrTruncateToLength(iformattablefielddata.getFieldLength(), ' ');
        return paddedstringbuffer.toString();
    }

    static final String copyRight = new String("(C) Copyright IBM Corporation 2001-2003, all rights reserved");

}
