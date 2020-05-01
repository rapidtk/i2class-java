// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.fldformat;

import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.host.HostJobInfo;
import com.ibm.as400ad.webfacing.runtime.model.IFormattableFieldData;
import com.ibm.as400ad.webfacing.runtime.view.IFormattableFieldView;
import com.ibm.as400ad.webfacing.util.Assert;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.fldformat:
//            FieldDataFormatter

public class TFieldFormatter extends FieldDataFormatter
{

    public TFieldFormatter()
    {
    }

    private void initialize(IFormattableFieldData iformattablefielddata)
    {
        _presentationTimSep = iformattablefielddata.getTimSep();
        _applicationTimSep = _presentationTimSep;
        HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
        if(_presentationTimSep.equals("JOB"))
        {
            _presentationTimSep = String.valueOf(hostjobinfo.getTimeSeparator());
            _applicationTimSep = ":";
        }
    }

    public String format(int i, IFormattableFieldView iformattablefieldview, String s)
    {
        if(iformattablefieldview == null)
            return s;
        initialize(iformattablefieldview.getFormattableDataDefinition());
        if(!_presentationTimSep.equals(_applicationTimSep))
            s = WebfacingConstants.replaceSubstring(s, _applicationTimSep, _presentationTimSep);
        return super.format(i, iformattablefieldview, s);
    }

    public String format(int i, IFormattableFieldData iformattablefielddata, String s)
    {
        initialize(iformattablefielddata);
        byte byte0 = -1;
        byte byte1 = -1;
        String s1 = "";
        String s4 = "";
        String s7 = "";
        String s11 = iformattablefielddata.getTimFmt();
        s = s.trim();
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(s);
        if(!s11.equals("USA"))
        {
            if(s.length() == 0)
            {
                paddedstringbuffer.concat("00", _applicationTimSep, "00", _applicationTimSep + "00");
                return paddedstringbuffer.toString();
            }
            paddedstringbuffer.truncateAllSpacesFromRight();
            if(paddedstringbuffer.isAllNumbers())
            {
                int j1 = paddedstringbuffer.length();
                Assert.assertTrue("Data length can only be 2,4 or 6 for time fields when it only contains numbers.", j1 % 2 == 0 || j1 > 6);
                switch(j1)
                {
                case 2: // '\002'
                    paddedstringbuffer.concat(_applicationTimSep, "00", _applicationTimSep, "00");
                    break;

                case 4: // '\004'
                    paddedstringbuffer.insert(2, _applicationTimSep);
                    paddedstringbuffer.append(_applicationTimSep);
                    paddedstringbuffer.append("00");
                    break;

                case 6: // '\006'
                    paddedstringbuffer.insert(4, _applicationTimSep);
                    paddedstringbuffer.insert(2, _applicationTimSep);
                    break;
                }
            } else
            {
                String s9 = paddedstringbuffer.toString();
                int j = s9.indexOf(_presentationTimSep);
                Assert.assertTrue("Time format is wrong", j < 3 && j > 0);
                String s2 = s9.substring(0, j);
                int l = s9.indexOf(_presentationTimSep, j + 1);
                String s5;
                String s8;
                if(l >= 0)
                {
                    int i1 = s9.lastIndexOf(_presentationTimSep);
                    s5 = s9.substring(j + 1, i1);
                    s8 = s9.substring(i1 + 1);
                    Assert.assertTrue("Time data is invalid", s5.length() < 3 && s8.length() > 0 && s8.length() < 3);
                } else
                {
                    s5 = s9.substring(j + 1);
                    Assert.assertTrue("Time data is invalid.", s5.length() < 3 && s5.length() > 0);
                    s8 = "00";
                }
                paddedstringbuffer.padLeftAndConcat(s2, 2, s5, 2, s8, 2, _applicationTimSep, '0');
            }
        } else
        if(s.length() == 0)
        {
            paddedstringbuffer.concat("00", _applicationTimSep, "00 AM");
        } else
        {
            Assert.assertTrue("Time data length should be equal to or greater than 6.", paddedstringbuffer.length() >= 6);
            String s10 = paddedstringbuffer.toString();
            int k = s10.indexOf(_presentationTimSep);
            Assert.assertTrue("Time data is invalid", k < 3 && k > 0);
            String s3 = s10.substring(0, k);
            String s6 = s10.substring(k + 1, k + 3);
            s6.trim();
            s6 = WebfacingConstants.replaceSubstring(s6, " ", "0");
            String s12 = s10.substring(k + 3);
            s12.trim();
            s12.toUpperCase();
            paddedstringbuffer.padLeftAndConcat(s3, 2, s6, 2, "", 0, _applicationTimSep, '0');
            PaddedStringBuffer paddedstringbuffer1 = new PaddedStringBuffer(s12);
            paddedstringbuffer1.padLeftOrTruncateToLength(3, ' ');
            paddedstringbuffer.append(paddedstringbuffer1.toString());
        }
        return paddedstringbuffer.toString();
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2001-2002");
    private String _presentationTimSep;
    private String _applicationTimSep;

}
