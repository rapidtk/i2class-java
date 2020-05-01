// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.fldformat;

import com.ibm.as400ad.code400.dom.constants.FieldType;
import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.runtime.model.IFormattableFieldData;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.fldformat:
//            FieldDataFormatter

public class DFieldFormatter extends FieldDataFormatter
{

    public DFieldFormatter()
    {
    }

    public String format(int i, IFormattableFieldData iformattablefielddata, String s)
    {
        FieldType fieldtype = iformattablefielddata.getDataType();
        int j = iformattablefielddata.getFieldLength();
        PaddedStringBuffer paddedstringbuffer = null;
        if(fieldtype.equals(8))
        {
            paddedstringbuffer = new PaddedStringBuffer(s);
            paddedstringbuffer.padRightOrTruncateToLength(j, ' ');
        } else
        if(fieldtype.equals(9))
        {
            paddedstringbuffer = new PaddedStringBuffer(s.trim());
            paddedstringbuffer.replaceSubstring(" ", "0");
            paddedstringbuffer.padLeftOrTruncateToLength(j, '0');
        }
        if(paddedstringbuffer == null)
            return s;
        else
            return paddedstringbuffer.toString();
    }

    static final String copyRight = new String("(C) Copyright IBM Corporation 2001-2003, all rights reserved");

}
