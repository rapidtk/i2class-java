// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.fldformat;

import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.host.HostJobInfo;
import com.ibm.as400ad.webfacing.runtime.model.IFormattableFieldData;
import com.ibm.as400ad.webfacing.runtime.view.IFormattableFieldView;
import com.ibm.as400ad.webfacing.util.Assert;
import java.util.Calendar;
import java.util.GregorianCalendar;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.fldformat:
//            FieldDataFormatter, IDateFormat

public class LFieldFormatter extends FieldDataFormatter
    implements IDateFormat
{

    public LFieldFormatter()
    {
        _calendar = new GregorianCalendar();
        _presentationDatFmt = -1;
        _presentationDatSep = null;
        _applicationDatFmt = -1;
        _applicationDatSep = null;
    }

    private void initialize(IFormattableFieldData iformattablefielddata)
    {
        _presentationDatFmt = getDatFmt(iformattablefielddata.getDatFmt());
        _presentationDatSep = iformattablefielddata.getDatSep();
        _applicationDatSep = _presentationDatSep;
        HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
        if(_presentationDatFmt == 0)
        {
            _presentationDatFmt = getDatFmt(hostjobinfo.getDateFormat());
            _applicationDatFmt = 5;
        }
        if(_presentationDatSep.equals("JOB"))
        {
            _presentationDatSep = String.valueOf(hostjobinfo.getDateSeparator());
            _applicationDatSep = "/";
        }
    }

    public String format(int i, IFormattableFieldData iformattablefielddata, String s)
    {
        initialize(iformattablefielddata);
        switch(_presentationDatFmt)
        {
        case 1: // '\001'
        case 2: // '\002'
        case 3: // '\003'
            return getAppDateForYMDMDYDMY(s);

        case 4: // '\004'
            return getAppDateForJUL(s);

        case 5: // '\005'
        case 8: // '\b'
            return getAppDateForISOJIS(s);

        case 6: // '\006'
        case 7: // '\007'
            return getAppDateForUSAEUR(s);
        }
        return s;
    }

    public String format(int i, IFormattableFieldView iformattablefieldview, String s)
    {
        if(iformattablefieldview == null)
            return s;
        initialize(iformattablefieldview.getFormattableDataDefinition());
        if(_applicationDatFmt == 5 && _presentationDatFmt != 5)
        {
            String s1 = s.substring(0, 4);
            String s2 = s.substring(5, 7);
            String s3 = s.substring(8, 10);
            PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer("");
            String s4 = s1.substring(2);
            switch(_presentationDatFmt)
            {
            case 1: // '\001'
                paddedstringbuffer.concat(s2, _presentationDatSep, s3, _presentationDatSep, s4);
                break;

            case 2: // '\002'
                paddedstringbuffer.concat(s3, _presentationDatSep, s2, _presentationDatSep, s4);
                break;

            case 3: // '\003'
                paddedstringbuffer.concat(s4, _presentationDatSep, s2, _presentationDatSep, s3);
                break;

            case 4: // '\004'
                setCalendar(s1, s2, s3);
                paddedstringbuffer.concat(s4, _presentationDatSep, getDayOfYear());
                break;

            case 6: // '\006'
                paddedstringbuffer.concat(s2, _presentationDatSep, s3, _presentationDatSep, s1);
                break;

            case 7: // '\007'
                paddedstringbuffer.concat(s3, _presentationDatSep, s2, _presentationDatSep, s1);
                break;

            case 8: // '\b'
                paddedstringbuffer.concat(s1, _presentationDatSep, s2, _presentationDatSep, s3);
                break;
            }
            return super.format(i, iformattablefieldview, paddedstringbuffer.toString());
        } else
        {
            return super.format(i, iformattablefieldview, s);
        }
    }

    private String toFourDigitYear(String s)
    {
        if(s.length() == 4)
            return s;
        int i = Integer.parseInt(s);
        if(i >= 50)
            return "19" + s;
        else
            return "20" + s;
    }

    private String getDayOfMonth()
    {
        return String.valueOf(_calendar.get(5));
    }

    private String getDayOfYear()
    {
        return String.valueOf(_calendar.get(6));
    }

    private String getMonth()
    {
        int i = _calendar.get(2);
        return String.valueOf(i);
    }

    private void setCalendar(String s, String s1, String s2)
    {
        s = toFourDigitYear(s);
        _calendar.set(1, Integer.parseInt(s));
        _calendar.set(2, Integer.parseInt(s1));
        _calendar.set(5, Integer.parseInt(s2));
    }

    private void setCalendar(String s, String s1)
    {
        s = toFourDigitYear(s);
        _calendar.set(1, Integer.parseInt(s));
        _calendar.set(6, Integer.parseInt(s1));
    }

    private int getDatFmt(String s)
    {
        if(s.equals("MDY"))
            return 1;
        if(s.equals("DMY"))
            return 2;
        if(s.equals("YMD"))
            return 3;
        if(s.equals("JUL"))
            return 4;
        if(s.equals("USA"))
            return 6;
        if(s.equals("EUR"))
            return 7;
        if(s.equals("JIS"))
            return 8;
        if(s.equals("JOB"))
            return 0;
        return !s.equals("ISO") ? -1 : 5;
    }

    private String getAppDateForYMDMDYDMY(String s)
    {
        byte byte0 = -1;
        byte byte1 = -1;
        String s1 = "";
        String s2 = "";
        String s3 = "";
        s = s.trim();
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(s);
        if(s.length() == 0)
        {
            s1 = "00";
            s2 = "00";
            s3 = "00";
        } else
        {
            paddedstringbuffer.truncateAllSpacesFromLeft();
            Assert.assertTrue("Date data length should be equal to or greater than 5.", paddedstringbuffer.length() >= 5);
            String s4 = paddedstringbuffer.toString();
            if(paddedstringbuffer.isAllNumbers())
            {
                paddedstringbuffer.padLeftOrTruncateToLength(6, '0');
                s4 = paddedstringbuffer.toString();
                s1 = s4.substring(0, 2);
                s2 = s4.substring(2, 4);
                s3 = s4.substring(4, 6);
            } else
            {
                int i = s4.indexOf(_presentationDatSep);
                Assert.assertTrue("Date format is wrong", i < 3 && i > 0);
                int j = s4.substring(i + 1).indexOf(_presentationDatSep);
                Assert.assertTrue("Date format is wrong", j < 3 && j > 0);
                j = s4.lastIndexOf(_presentationDatSep);
                s1 = s4.substring(0, i);
                s2 = s4.substring(i + 1, j);
                s3 = s4.substring(j + 1);
            }
        }
        if(_applicationDatFmt == -1)
            paddedstringbuffer.padLeftAndConcat(s1, 2, s2, 2, s3, 2, _applicationDatSep, '0');
        else
            switch(_presentationDatFmt)
            {
            case 1: // '\001'
                paddedstringbuffer.padLeftAndConcat(toFourDigitYear(s3), 4, s1, 2, s2, 2, _applicationDatSep, '0');
                break;

            case 2: // '\002'
                paddedstringbuffer.padLeftAndConcat(toFourDigitYear(s3), 4, s2, 2, s1, 2, _applicationDatSep, '0');
                break;

            case 3: // '\003'
                paddedstringbuffer.padLeftAndConcat(toFourDigitYear(s1), 4, s2, 2, s3, 2, _applicationDatSep, '0');
                break;
            }
        return paddedstringbuffer.toString();
    }

    private String getAppDateForJUL(String s)
    {
        byte byte0 = -1;
        String s1 = "";
        String s2 = "";
        s = s.trim();
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(s);
        if(s.length() == 0)
        {
            s1 = "01";
            s2 = "001";
        } else
        {
            paddedstringbuffer.truncateAllSpacesFromLeft();
            String s3 = paddedstringbuffer.toString();
            if(paddedstringbuffer.isAllNumbers())
            {
                Assert.assertTrue("Date data length should be equal to or less than 5.", paddedstringbuffer.length() <= 5);
                paddedstringbuffer.padLeftOrTruncateToLength(5, '0');
                String s4 = paddedstringbuffer.toString();
                s1 = s4.substring(0, 2);
                s2 = s4.substring(2);
            } else
            {
                String s5 = paddedstringbuffer.toString();
                int i = s5.indexOf(_presentationDatSep);
                Assert.assertTrue("Date format is wrong", i < 3 && i > 0);
                s1 = s5.substring(0, i);
                s2 = s5.substring(i + 1);
            }
        }
        if(_applicationDatFmt == -1)
        {
            paddedstringbuffer.padLeftAndConcat(s1, 2, s2, 3, "", 0, _applicationDatSep, '0');
        } else
        {
            s1 = toFourDigitYear(s1);
            setCalendar(s1, s2);
            paddedstringbuffer.padLeftAndConcat(s1, 4, getMonth(), 2, getDayOfMonth(), 2, _applicationDatSep, '0');
        }
        return paddedstringbuffer.toString();
    }

    private String getAppDateForISOJIS(String s)
    {
        byte byte0 = -1;
        byte byte1 = -1;
        String s1 = "";
        String s3 = "";
        String s5 = "";
        s = s.trim();
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(10);
        if(s.length() == 0)
        {
            paddedstringbuffer.concat("0001", _applicationDatSep, "01", _applicationDatSep, "01");
            return paddedstringbuffer.toString();
        }
        paddedstringbuffer.setValue(s);
        paddedstringbuffer.truncateAllSpacesFromLeft();
        Assert.assertTrue("Date data length should be equal to or greater than 8.", paddedstringbuffer.length() >= 8);
        if(paddedstringbuffer.isAllNumbers())
        {
            Assert.assertTrue("Date data length should be 8 when it only contains numbers.", paddedstringbuffer.length() == 8);
            paddedstringbuffer.insert(6, _applicationDatSep);
            paddedstringbuffer.insert(4, _applicationDatSep);
            return paddedstringbuffer.toString();
        } else
        {
            String s7 = paddedstringbuffer.toString();
            int i = s7.indexOf(_presentationDatSep);
            Assert.assertTrue("Date format is wrong", i == 4);
            int j = s7.substring(i + 1).indexOf(_presentationDatSep);
            Assert.assertTrue("Date format is wrong", j < 3 && j > 0);
            j = s7.lastIndexOf(_presentationDatSep);
            String s2 = s7.substring(0, i);
            String s4 = s7.substring(i + 1, j);
            String s6 = s7.substring(j + 1);
            paddedstringbuffer.padLeftAndConcat(s2, 4, s4, 2, s6, 2, _applicationDatSep, '0');
            return paddedstringbuffer.toString();
        }
    }

    private String getAppDateForUSAEUR(String s)
    {
        byte byte0 = -1;
        byte byte1 = -1;
        String s1 = "";
        String s2 = "";
        String s3 = "";
        s = s.trim();
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(s);
        if(s.length() == 0)
        {
            s1 = "0001";
            s2 = "01";
            s3 = "01";
        } else
        {
            paddedstringbuffer.truncateAllSpacesFromLeft();
            String s4 = paddedstringbuffer.toString();
            if(paddedstringbuffer.isAllNumbers())
            {
                Assert.assertTrue("Date data length should be 7 or 8 when it only contains numbers.", paddedstringbuffer.length() == 7 || paddedstringbuffer.length() == 8);
                paddedstringbuffer.padLeftOrTruncateToLength(8, '0');
                s1 = s4.substring(4);
                s2 = s4.substring(0, 2);
                s3 = s4.substring(2, 4);
            } else
            {
                Assert.assertTrue("Date data length should be equal to or greater than 8 when it only contains separators.", paddedstringbuffer.length() >= 8);
                int i = s4.indexOf(_presentationDatSep);
                Assert.assertTrue("Date format is wrong", i > 0 && i < 3);
                int j = s4.substring(i + 1).indexOf(_presentationDatSep);
                Assert.assertTrue("Date format is wrong", j < 3 && j > 0);
                j = s4.lastIndexOf(_presentationDatSep);
                s2 = s4.substring(0, i);
                s3 = s4.substring(i + 1, j);
                s1 = s4.substring(j + 1);
                Assert.assertTrue("Date format is wrong", s1.length() == 4);
            }
        }
        if(_applicationDatFmt == -1)
            paddedstringbuffer.padLeftAndConcat(s2, 2, s3, 2, s1, 4, _applicationDatSep, '0');
        else
        if(_presentationDatFmt == 6)
            paddedstringbuffer.padLeftAndConcat(s1, 4, s2, 2, s3, 2, _applicationDatSep, '0');
        else
        if(_presentationDatFmt == 7)
            paddedstringbuffer.padLeftAndConcat(s1, 4, s3, 2, s2, 2, _applicationDatSep, '0');
        return paddedstringbuffer.toString();
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2001-2002");
    private GregorianCalendar _calendar;
    int _presentationDatFmt;
    String _presentationDatSep;
    int _applicationDatFmt;
    String _applicationDatSep;

}
