// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.fldformat;

import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.runtime.model.IFormattableFieldData;
import com.ibm.as400ad.webfacing.runtime.view.IFormattableFieldView;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.fldformat:
//            NumericFieldFormatter

public class SFieldFormatter extends NumericFieldFormatter
{

    public SFieldFormatter()
    {
    }

    public String format(int i, IFormattableFieldData iformattablefielddata, String s)
    {
        boolean flag = false;
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(s.trim());
        paddedstringbuffer.truncateAllSpacesFromLeft();
        String s1 = paddedstringbuffer.toString();
        int j = s1.length();
        if(j > 0)
            if(s1.charAt(0) == '+')
            {
                paddedstringbuffer.deleteCharAt(0);
                paddedstringbuffer.truncateAllSpacesFromLeft();
            } else
            if(s1.charAt(j - 1) == '+')
            {
                paddedstringbuffer.deleteCharAt(j - 1);
                paddedstringbuffer.truncateAllSpacesFromRight();
            } else
            if(s1.charAt(0) == '-')
            {
                flag = true;
                paddedstringbuffer.deleteCharAt(0);
            } else
            if(s1.charAt(j - 1) == '-')
            {
                flag = true;
                paddedstringbuffer.deleteCharAt(j - 1);
                paddedstringbuffer.truncateAllSpacesFromRight();
            }
        paddedstringbuffer.replaceSubstring(" ", "0");
        paddedstringbuffer.padLeftOrTruncateToLength(iformattablefielddata.getFieldLength(), '0');
        if(flag)
            paddedstringbuffer.insert(0, '-');
        return paddedstringbuffer.toString();
    }

    protected int getDecimalPrecision(IFormattableFieldView iformattablefieldview)
    {
        return 0;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2001-2002");

}
