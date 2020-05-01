// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.fldformat;

import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.host.HostJobInfo;
import com.ibm.as400ad.webfacing.runtime.model.IFormattableFieldData;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.fldformat:
//            NumericFieldFormatter

public class YFieldFormatter extends NumericFieldFormatter
{

    public YFieldFormatter()
    {
    }

    public String format(int i, IFormattableFieldData iformattablefielddata, String s)
    {
        HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
        char c = hostjobinfo.getDecimalSeparator();
        char c1 = hostjobinfo.getThousandSeparator();
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(s.trim());
        paddedstringbuffer.truncateAllSpacesFromLeft();
        String s1 = paddedstringbuffer.toString();
        paddedstringbuffer.setValue("");
        boolean flag = false;
        int j = s1.length();
        if(j >= 2 && s1.substring(j - 2).equals("CR"))
        {
            flag = true;
            s1 = s1.substring(0, j - 2);
        }
        String s2 = "";
        String s3 = "";
        for(int k = 0; k < s1.length(); k++)
        {
            char c2 = s1.charAt(k);
            if((Character.isDigit(c2) || c2 == c || c2 == '-' || c2 == '+' || c2 == c1 || c2 == ' ') && (c2 != '-' || k == 0 || c2 != '-' || k == s1.length() - 1) && c2 != '+' && c2 != c1)
                if(c2 == '-' && (k == 0 || k == s1.length() - 1))
                    flag = true;
                else
                    paddedstringbuffer.append(c2);
        }

        paddedstringbuffer.truncateAllSpacesFromRight();
        paddedstringbuffer.replaceSubstring(" ", "0");
        s1 = paddedstringbuffer.toString();
        int l = s1.indexOf(c);
        if(l != -1)
        {
            s3 = s1.substring(l + 1, s1.length());
            s2 = s1.substring(0, l);
        } else
        {
            s2 = s1;
        }
        int i1 = iformattablefielddata.getDecimalPrecision();
        paddedstringbuffer.setValue(s2);
        paddedstringbuffer.padLeftOrTruncateToLength(iformattablefielddata.getFieldLength() - i1, '0');
        PaddedStringBuffer paddedstringbuffer1 = new PaddedStringBuffer(s3);
        paddedstringbuffer1.padRightOrTruncateToLength(i1, '0');
        paddedstringbuffer.append(paddedstringbuffer1.toString());
        if(flag)
            paddedstringbuffer.insert(0, '-');
        return paddedstringbuffer.toString();
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2001-2002");

}
