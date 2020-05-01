// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.dev;


// Referenced classes of package com.ibm.as400ad.webfacing.dev:
//            FilterCode

public class QdspmnuFilter extends FilterCode
{

    public QdspmnuFilter()
    {
        _sizeReplaced = false;
        _maxLengthReplaced = false;
        _inputTagFound = false;
        _textAreaTagFound = false;
    }

    private String addOnKeyPress(String s)
    {
        if(s.toLowerCase().indexOf("onkeypress") < 0)
            return replaceSubString(s, "onBlur=\\\"testIfChanged();\\\"", "onBlur=\\\"testIfChanged();\\\" onKeyPress=\\\"checkCharShiftY();\\\"");
        else
            return s;
    }

    public String filterLine(String s)
    {
        if(s.toLowerCase().indexOf("<textarea") >= 0 || s.toLowerCase().indexOf("<input") >= 0)
            _inputTagFound = true;
        if(_inputTagFound)
        {
            s = replaceSize(s);
            s = replaceMaxLength(s);
            s = addOnKeyPress(s);
        }
        if(s.toLowerCase().indexOf("<textarea") >= 0)
            _textAreaTagFound = true;
        if(_textAreaTagFound)
            return textAreaToInputTag(s);
        if(s.toLowerCase().indexOf("</textarea>") >= 0)
            s = replaceSubString(s, "</textarea>", "\\\">");
        if(s.indexOf("session.getValue(") >= 0)
            s = replaceSubString(s, "session.getValue(", "session.getAttribute(");
        return s;
    }

    private String replaceMaxLength(String s)
    {
        if(!_maxLengthReplaced)
            return replaceSizeOrMaxLengthAttr(s, "maxLength");
        else
            return s;
    }

    private String replaceSize(String s)
    {
        if(!_sizeReplaced)
            return replaceSizeOrMaxLengthAttr(s, "size");
        else
            return s;
    }

    private String replaceSizeOrMaxLengthAttr(String s, String s1)
    {
        String s2 = s1 + "=";
        String s3 = s.toLowerCase();
        if(s3.indexOf(s2.toLowerCase()) >= 0)
        {
            int i = s3.substring(s3.indexOf(s2.toLowerCase()) + 1).indexOf("=") + s3.indexOf(s2.toLowerCase()) + 2;
            boolean flag;
            for(flag = false; i < s3.length() && !flag; i++)
                flag = s.charAt(i) < '0' || s.charAt(i) > '9';

            if(flag)
            {
                if(s1.equals("size"))
                    _sizeReplaced = true;
                else
                    _maxLengthReplaced = true;
                String s4 = s.substring(s3.indexOf(s2.toLowerCase()), i - 1);
                return replaceSubString(s, s4, s2 + "4");
            }
        }
        return s;
    }

    private String textAreaToInputTag(String s)
    {
        String s1 = s.toLowerCase();
        String s2 = "onFocus=";
        int i = s1.indexOf("<textarea");
        int j = s1.indexOf(s2.toLowerCase());
        if(i >= 0 && j > 0)
        {
            i += 8;
            s = replaceSubString(s, s.substring(i + 1, j), " ");
        }
        s = replaceSubString(s, "<textarea", "<input");
        s2 = "maxLength=4";
        i = s1.indexOf(s2.toLowerCase());
        j = s1.indexOf(">");
        if(i >= 0 && j > 0)
        {
            i += 10;
            s = replaceSubString(s, s.substring(i + 1, j + 1), " value=\\\"");
            _textAreaTagFound = false;
        }
        return s;
    }

    private boolean _sizeReplaced;
    private boolean _maxLengthReplaced;
    private boolean _inputTagFound;
    private boolean _textAreaTagFound;
}
