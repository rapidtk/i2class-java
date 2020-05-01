// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.fldformat;

import com.ibm.as400ad.code400.dom.constants.FieldType;
import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.host.HostJobInfo;
import com.ibm.as400ad.webfacing.runtime.model.IFormattableFieldData;
import com.ibm.as400ad.webfacing.runtime.view.IFormattableFieldView;
import com.ibm.ivj.et400.util.DataAttributes;
import com.ibm.ivj.et400.util.EditcodeEditwordFormatter;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.fldformat:
//            GFieldFormatter, JFieldFormatter, EFieldFormatter, OFieldFormatter, 
//            DFieldFormatter, FFieldFormatter, LFieldFormatter, MFieldFormatter, 
//            YFieldFormatter, AFieldFormatter, SFieldFormatter, TFieldFormatter, 
//            XFieldFormatter, ZFieldFormatter

public abstract class FieldDataFormatter
{

    public FieldDataFormatter()
    {
    }

    public abstract String format(int i, IFormattableFieldData iformattablefielddata, String s);

    public String format(int i, IFormattableFieldView iformattablefieldview, String s)
    {
        if(iformattablefieldview == null)
            return s;
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(s);
        if(iformattablefieldview.hasEditCodeEditWord())
            paddedstringbuffer.setValue(applyEditing(iformattablefieldview, paddedstringbuffer.toStringBuffer()));
        if(iformattablefieldview.isInputOnly() && iformattablefieldview.isNumeric() && !iformattablefieldview.hasDefaultValue())
            paddedstringbuffer.setValue("");
        if(!iformattablefieldview.isOutputOnly())
        {
            if(paddedstringbuffer.isAllBlank() || paddedstringbuffer.isAllDBCSBlank())
                paddedstringbuffer.setValue("");
            if(!paddedstringbuffer.toString().equals(""))
                paddedstringbuffer.truncateAllSpacesFromRight();
        }
        return paddedstringbuffer.toString();
    }

    public static FieldDataFormatter getFieldFormatterInstance(char c, FieldType fieldtype)
    {
        switch(c)
        {
        case 71: // 'G'
            return new GFieldFormatter();

        case 74: // 'J'
            return new JFieldFormatter();

        case 69: // 'E'
            return new EFieldFormatter();

        case 79: // 'O'
            return new OFieldFormatter();

        case 68: // 'D'
            return new DFieldFormatter();

        case 70: // 'F'
            return new FFieldFormatter();

        case 76: // 'L'
            return new LFieldFormatter();

        case 77: // 'M'
            return new MFieldFormatter();

        case 78: // 'N'
            if(fieldtype.equals(9))
                return new YFieldFormatter();
            if(fieldtype.equals(8))
                return new AFieldFormatter();
            // fall through

        case 83: // 'S'
            return new SFieldFormatter();

        case 84: // 'T'
            return new TFieldFormatter();

        case 88: // 'X'
            return new XFieldFormatter();

        case 89: // 'Y'
            return new YFieldFormatter();

        case 90: // 'Z'
            return new ZFieldFormatter();

        case 72: // 'H'
        case 73: // 'I'
        case 75: // 'K'
        case 80: // 'P'
        case 81: // 'Q'
        case 82: // 'R'
        case 85: // 'U'
        case 86: // 'V'
        case 87: // 'W'
        default:
            return new AFieldFormatter();
        }
    }

    public String formatForMSG(int i, IFormattableFieldData iformattablefielddata, String s)
    {
        return format(i, iformattablefielddata, s);
    }

    public static String formatString(String s, char c, char c1, String s1, int i, int j)
    {
        EditcodeEditwordFormatter editcodeeditwordformatter = getEditFormatter(c, c1, s1, i, j);
        return editcodeeditwordformatter.formatString(s);
    }

    private static EditcodeEditwordFormatter getEditFormatter(char c, char c1, String s, int i, int j)
    {
        HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
        char c2 = hostjobinfo.getCurrencySymbol();
        byte byte0;
        if('0' == c1)
            byte0 = 0;
        else
        if('*' == c1)
        {
            byte0 = 1;
        } else
        {
            byte0 = 2;
            c2 = c1;
        }
        return new EditcodeEditwordFormatter(new DataAttributes(1, j, i, hostjobinfo.getDecimalSeparator()), c, byte0, s, c2, hostjobinfo.getThousandSeparator(), hostjobinfo.getDateSeparator(), hostjobinfo.isQdecfmtJValue());
    }

    public StringBuffer applyEditing(IFormattableFieldView iformattablefieldview, StringBuffer stringbuffer)
    {
        EditcodeEditwordFormatter editcodeeditwordformatter = iformattablefieldview.getEditFormatter();
        if(editcodeeditwordformatter == null)
        {
            int i = getDecimalPrecision(iformattablefieldview);
            int j = iformattablefieldview.getFieldLength();
            editcodeeditwordformatter = getEditFormatter(iformattablefieldview.getEditCode(), iformattablefieldview.getEditCodeParm(), iformattablefieldview.getEditWord(), i, j);
            iformattablefieldview.setEditFormatter(editcodeeditwordformatter);
        }
        return new StringBuffer(editcodeeditwordformatter.formatString(iformattablefieldview.getDecimalFormattedString(stringbuffer)));
    }

    protected int getDecimalPrecision(IFormattableFieldView iformattablefieldview)
    {
        return iformattablefieldview.getDecimalPrecision();
    }

    static final String copyRight = new String("(C) Copyright IBM Corporation 2001-2003, all rights reserved");

}
