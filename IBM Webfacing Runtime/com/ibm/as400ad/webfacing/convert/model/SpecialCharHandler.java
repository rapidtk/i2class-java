// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.dhtmlview.HTMLStringTransform;
import com.ibm.as400ad.webfacing.runtime.dhtmlview.IHTMLStringTransforms;
import com.ibm.as400ad.webfacing.runtime.view.IStringTransform;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            IFieldText

public class SpecialCharHandler
    implements IHTMLStringTransforms
{

    public SpecialCharHandler(IFieldText ifieldtext)
    {
        _fieldOutput = ifieldtext;
    }

    public String getTransformParam(int i)
    {
        String s = null;
        switch(i)
        {
        case 1: // '\001'
            s = "QUOTED_TRANSFORM";
            break;

        case 3: // '\003'
            s = "IHTMLStringTransforms.TRIMMED_QUOTED_STRING_TRANSFORM";
            break;

        case 4: // '\004'
            s = "IHTMLStringTransforms.TRIMMED_JAVA_STRING_TRANSFORM";
            break;

        case 2: // '\002'
        default:
            s = "UNQUOTED_TRANSFORM";
            break;
        }
        return s;
    }

    public String replaceFieldValueSymbol(String s)
    {
        return replaceFieldValueSymbol(s, "&FieldValue");
    }

    public String replaceFieldValueSymbol(String s, String s1)
    {
        String s2 = s;
        if(!s.equals(""))
        {
            int i = s.indexOf("\"");
            boolean flag = false;
            String s3 = _fieldOutput.getFieldTextWithTransform(1);
            String s4 = _fieldOutput.getFieldTextWithTransform(2);
            while(i >= 0) 
            {
                int j;
                for(j = s.indexOf("\"", i + 1); j > 0 && s.charAt(j - 1) == '\\'; j = s.indexOf("\"", j + 1));
                if(j > 0 && j < s.length() && s.substring(i, j + 1).indexOf(s1) >= 0)
                    s2 = WebfacingConstants.replaceSubstring(s, s1, s3, i);
                if(j < s.length() - 1)
                    i = s.indexOf("\"", j + 1);
            }
            s2 = WebfacingConstants.replaceSubstring(s, s1, s4);
        }
        return s2;
    }

    public String replaceSpecialSymbols(String s)
    {
        s = replaceFieldValueSymbol(s, "&SystemName");
        s = replaceFieldValueSymbol(s, "&Date");
        s = replaceFieldValueSymbol(s, "&Time");
        s = replaceFieldValueSymbol(s, "&UserID");
        return s;
    }

    public String transformSpecialChars(String s, int i)
    {
        return _targetStringTransform.transform(s, i);
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2000");
    private IFieldText _fieldOutput;
    protected static final IStringTransform _targetStringTransform = new HTMLStringTransform();

}
