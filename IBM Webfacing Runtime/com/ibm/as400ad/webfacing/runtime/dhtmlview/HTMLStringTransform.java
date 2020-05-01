// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.dhtmlview;

import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.view.IStringTransform;
import java.util.ResourceBundle;

public class HTMLStringTransform
    implements IStringTransform
{

    public HTMLStringTransform()
    {
    }

    public String transform(String s, int i)
        throws IllegalArgumentException
    {
        String s1;
        switch(i)
        {
        case 0: // '\0'
            s1 = s;
            break;

        case 1: // '\001'
            s1 = transformQuotedString(s);
            break;

        case 2: // '\002'
            s1 = transformUnquotedString(s);
            break;

        case 3: // '\003'
            s1 = transformTrimmedQuotedString(s);
            break;

        case 4: // '\004'
            s1 = transformTrimmedJavaString(s);
            break;

        case 5: // '\005'
            s1 = transformForHyperlink(s);
            break;

        default:
            throw new IllegalArgumentException(_resmri.getString("WF0012"));
        }
        return s1;
    }

    public static String transformQuotedString(String s)
    {
        String s1 = WebfacingConstants.replaceSubstring(s, "&", "&amp;");
        s1 = WebfacingConstants.replaceSubstring(s1, "\"", "&quot;");
        return s1;
    }

    public static String transformUnquotedString(String s)
    {
        String s1 = WebfacingConstants.replaceSubstring(s, "&", "&amp;");
        s1 = WebfacingConstants.replaceSubstring(s1, " ", "&nbsp;");
        s1 = WebfacingConstants.replaceSubstring(s1, "<", "&lt;");
        s1 = WebfacingConstants.replaceSubstring(s1, ">", "&gt;");
        return s1;
    }

    public static String transformKeyLabelString(String s)
    {
        String s1 = WebfacingConstants.replaceSubstring(s, "&", "&amp;");
        s1 = WebfacingConstants.replaceSubstring(s1, "<", "&lt;");
        s1 = WebfacingConstants.replaceSubstring(s1, ">", "&gt;");
        return s1;
    }

    public static String transformUnquotedString(String s, boolean flag)
    {
        String s1 = transformUnquotedString(s);
        if(flag)
            s1 = WebfacingConstants.replaceSubstring(s1, "\"", "&quot;");
        return s1;
    }

    public static String transformTrimmedQuotedString(String s)
    {
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(transformQuotedString(s));
        paddedstringbuffer.truncateAllSpacesFromLeft();
        paddedstringbuffer.truncateAllSpacesFromRight();
        return paddedstringbuffer.toString();
    }

    public static String transformTrimmedJavaString(String s)
    {
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(WebfacingConstants.getJavaString(s));
        paddedstringbuffer.truncateAllSpacesFromLeft();
        paddedstringbuffer.truncateAllSpacesFromRight();
        return paddedstringbuffer.toString();
    }

    public static String transformForHyperlink(String s)
    {
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(s);
        paddedstringbuffer.truncateAllSpacesFromLeft();
        paddedstringbuffer.truncateAllSpacesFromRight();
        for(int i = 0; i < encodeThese.length; i++)
        {
            char c = encodeThese[i];
            if(paddedstringbuffer.containsChar(c))
            {
                String s1 = Integer.toHexString(c);
                if(s1.length() < 2)
                    s1 = "%0" + s1;
                else
                    s1 = "%" + s1;
                paddedstringbuffer.replaceSubstring((new Character(c)).toString(), s1);
            }
        }

        return paddedstringbuffer.toString();
    }

    public static final String copyRight = new String(" (C) Copyright IBM Corporation 1999-2003, all rights reserved");
    private static ResourceBundle _resmri;
    private static char encodeThese[] = {
        '%', '\0', '\001', '\002', '\003', '\004', '\005', '\006', '\007', '\b', 
        '\t', '\n', '\013', '\f', '\r', '\016', '\f', '\016', '\017', '\020', 
        '\021', '\022', '\023', '\024', '\025', '\026', '\027', '\030', '\031', '\032', 
        '\033', '\034', '\035', '\036', '\037', '"', '{', '}', '|', '\\', 
        '^', '[', ']', ',', '`', '<', '>', '#', '<', ' ', 
        ';', '/', '?', ':', '@', '&', '=', '+', '\'', '~', 
        '$'
    };
    public static final int DO_NOT_TRANSFORM = 0;
    public static final int QUOTED_STRING_TRANSFORM = 1;
    public static final int UNQUOTED_STRING_TRANSFORM = 2;
    public static final int HYPERLINK_TRANSFORM = 5;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
