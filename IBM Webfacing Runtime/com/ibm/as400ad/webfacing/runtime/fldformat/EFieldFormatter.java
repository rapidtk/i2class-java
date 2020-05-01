// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.fldformat;

import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.runtime.model.IFormattableFieldData;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.fldformat:
//            FieldDataFormatter

public class EFieldFormatter extends FieldDataFormatter
{

    public EFieldFormatter()
    {
        _isFirstCharDBCS = false;
    }

    public void setIsFirstCharDBCS(boolean flag)
    {
        _isFirstCharDBCS = flag;
    }

    public String format(int i, IFormattableFieldData iformattablefielddata, String s)
    {
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(formatForMSG(i, iformattablefielddata, s));
        if(iformattablefielddata.getCheckAttr().indexOf("LC") < 0 && s.length() > 0 && paddedstringbuffer.isCharDBCS(0, i))
            paddedstringbuffer.toUpperCaseIgnoreDBCS(i);
        return paddedstringbuffer.toString();
    }

    public String formatForMSG(int i, IFormattableFieldData iformattablefielddata, String s)
    {
        char c = ' ';
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(s);
        int j = iformattablefielddata.getFieldLength();
        if(_isFirstCharDBCS)
        {
            c = '\u3000';
            j = (j - 2) / 2;
        }
        paddedstringbuffer.padRightOrTruncateToLength(j, c);
        return paddedstringbuffer.toString();
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2001-2002");
    private boolean _isFirstCharDBCS;

}
