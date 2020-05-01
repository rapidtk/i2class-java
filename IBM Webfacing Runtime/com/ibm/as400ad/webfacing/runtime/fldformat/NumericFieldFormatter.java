// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.fldformat;

import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.runtime.view.IFormattableFieldView;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.fldformat:
//            FieldDataFormatter

public abstract class NumericFieldFormatter extends FieldDataFormatter
{

    public NumericFieldFormatter()
    {
    }

    public String format(int i, IFormattableFieldView iformattablefieldview, String s)
    {
        if(iformattablefieldview == null)
            return s;
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(s);
        if(!iformattablefieldview.isOutputOnly() || iformattablefieldview.hasEditCodeEditWord())
            paddedstringbuffer.setValue(applyNegativeNumberTransform(paddedstringbuffer.toStringBuffer()));
        return super.format(i, iformattablefieldview, paddedstringbuffer.toString());
    }

    private String applyNegativeNumberTransform(StringBuffer stringbuffer)
    {
        int i = stringbuffer.length() - 1;
        char c = stringbuffer.charAt(i);
        boolean flag = true;
        switch(c)
        {
        case 125: // '}'
            stringbuffer.setCharAt(i, '0');
            break;

        case 74: // 'J'
            stringbuffer.setCharAt(i, '1');
            break;

        case 75: // 'K'
            stringbuffer.setCharAt(i, '2');
            break;

        case 76: // 'L'
            stringbuffer.setCharAt(i, '3');
            break;

        case 77: // 'M'
            stringbuffer.setCharAt(i, '4');
            break;

        case 78: // 'N'
            stringbuffer.setCharAt(i, '5');
            break;

        case 79: // 'O'
            stringbuffer.setCharAt(i, '6');
            break;

        case 80: // 'P'
            stringbuffer.setCharAt(i, '7');
            break;

        case 81: // 'Q'
            stringbuffer.setCharAt(i, '8');
            break;

        case 82: // 'R'
            stringbuffer.setCharAt(i, '9');
            break;

        default:
            flag = false;
            break;
        }
        if(flag)
            if(stringbuffer.charAt(0) != ' ')
            {
                stringbuffer.insert(0, '-');
            } else
            {
                for(int j = 0; j <= stringbuffer.length(); j++)
                {
                    if(stringbuffer.charAt(j) == ' ')
                        continue;
                    stringbuffer.insert(j, '-');
                    break;
                }

            }
        return stringbuffer.toString();
    }
}
