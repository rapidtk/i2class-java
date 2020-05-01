// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.fldformat;

import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.runtime.model.IFormattableFieldData;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.fldformat:
//            FieldDataFormatter

public class XFieldFormatter extends FieldDataFormatter
{

    public XFieldFormatter()
    {
    }

    public String format(int i, IFormattableFieldData iformattablefielddata, String s)
    {
        if(iformattablefielddata.getCheckAttr().indexOf("LC") < 0)
            s = s.toUpperCase();
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(s);
        paddedstringbuffer.padRightOrTruncateToLength(iformattablefielddata.getFieldLength(), ' ');
        return paddedstringbuffer.toString();
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2001-2002");

}
