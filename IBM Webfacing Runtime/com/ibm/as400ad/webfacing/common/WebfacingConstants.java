// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.common;

import com.ibm.as400ad.webfacing.util.DBCSUtil;
import java.io.PrintStream;
import java.util.*;

public class WebfacingConstants
{

    public WebfacingConstants()
    {
    }

    public static final List condensedStrToOrderedIntList(String s)
    {
        ArrayList arraylist = new ArrayList();
        for(StringTokenizer stringtokenizer = new StringTokenizer(s, ","); stringtokenizer.hasMoreTokens();)
        {
            String s1 = stringtokenizer.nextToken();
            int i = s1.indexOf("-");
            if(i == -1)
            {
                arraylist.add(new Integer(s1));
            } else
            {
                int j = Integer.parseInt(s1.substring(0, i));
                int k = Integer.parseInt(s1.substring(i + 1));
                for(int l = j; l <= k; l++)
                    arraylist.add(new Integer(l));

            }
        }

        return arraylist;
    }

    public static String createBlanks()
    {
        if(_blanks == null)
        {
            StringBuffer stringbuffer = new StringBuffer();
            for(int i = 0; i < 254; i++)
                stringbuffer.append(" ");

            _blanks = stringbuffer.toString();
        }
        return _blanks;
    }

    public static final String getCharacterReplacedPackageName(String s)
        throws IllegalArgumentException
    {
        StringBuffer stringbuffer;
        try
        {
            String s1 = s;
            int i = indexOfUnquotedChar(s1, '/');
            String s2 = s1.substring(0, i);
            s1 = s1.substring(i + 1);
            i = indexOfUnquotedChar(s1, '/');
            String s3 = s1.substring(0, i);
            s1 = s1.substring(i + 1);
            String s4 = s1;
            stringbuffer = new StringBuffer(40);
            stringbuffer.append(replaceSpecialCharacters(s2));
            stringbuffer.append('.');
            stringbuffer.append(replaceSpecialCharacters(s3));
            stringbuffer.append('.');
            stringbuffer.append(replaceSpecialCharacters(s4));
        }
        catch(IndexOutOfBoundsException indexoutofboundsexception)
        {
            throw new IllegalArgumentException(replaceSubstring(RUNTIME_MRI_BUNDLE.getString("WF0069"), "&1", " WebfacingConstants.getCharacterReplacedPackageName()"));
        }
        return stringbuffer.toString();
    }

    public static final int hexStringToInteger(String s, boolean flag)
    {
        int i = 0;
        byte byte0 = 16;
        for(int k = 0; k < s.length(); k++)
        {
            char c = s.charAt(k);
            i *= byte0;
            int j;
            if(flag)
                j = c - 97;
            else
            if(c >= '0' && c <= '9')
                j = c - 48;
            else
                j = (10 + c) - 65;
            i += j;
        }

        return i;
    }

    public static final int indexOfUnquotedChar(String s, char c)
    {
        int i = s.indexOf(c);
        if(i == -1)
            return -1;
        int j = s.indexOf('"');
        if(j == -1)
            return s.indexOf(c);
        if(i < j)
            return i;
        j = s.indexOf('"', j + 1);
        int k = indexOfUnquotedChar(s.substring(j + 1), c);
        if(k == -1)
            return -1;
        else
            return j + 1 + k;
    }

    public static final String integerToAlphabeticAnyBaseString(int i, int j)
    {
        if(j < 2 || j > 26)
            return null;
        if(i == 0)
            return "a";
        StringBuffer stringbuffer = new StringBuffer();
        String s;
        if(i < 0)
        {
            s = "-";
            i = -1 * i;
        } else
        {
            s = "";
        }
        for(; i != 0; i /= j)
        {
            int k = i % j;
            stringbuffer.append((char)(k + 97));
        }

        return s + stringbuffer.reverse().toString();
    }

    public static final String integerToAnyBaseString(int i, int j)
    {
        if(j < 2 || j > 36)
            return null;
        if(i == 0)
            return "0";
        StringBuffer stringbuffer = new StringBuffer();
        String s;
        if(i < 0)
        {
            s = "-";
            i = -1 * i;
        } else
        {
            s = "";
        }
        for(; i != 0; i /= j)
        {
            int k = i % j;
            stringbuffer.append(k >= 10 ? (char)((k - 10) + 65) : (char)(k + 48));
        }

        return s + stringbuffer.reverse().toString();
    }

    public static final String integerToHexString(int i)
    {
        return integerToAnyBaseString(i, 16);
    }

    public static final String integerToHexString(int i, boolean flag)
    {
        if(flag)
            return integerToAlphabeticAnyBaseString(i, 16);
        else
            return integerToAnyBaseString(i, 16);
    }

    public static final String orderedIntListToCondensedStr(List list)
    {
        String s = "";
        for(int i = 0; i < list.size(); i++)
            if(i == 0)
                s = s + list.get(i);
            else
            if(((Integer)list.get(i)).intValue() > ((Integer)list.get(i - 1)).intValue() + 1)
                s = s + "," + list.get(i);
            else
            if(i == list.size() - 1 || ((Integer)list.get(i + 1)).intValue() > ((Integer)list.get(i)).intValue() + 1)
                s = s + "-" + list.get(i);

        return s;
    }

    public static final String replaceCharsForPropertiesFile(String s)
    {
        String s1 = s;
        s1 = replaceSubstring(s1, "\\", "\\\\");
        s1 = replaceSubstring(s1, "#", "\\#");
        s1 = replaceSubstring(s1, "=", "\\=");
        s1 = replaceSubstring(s1, ":", "\\:");
        return s1;
    }

    public static final String replaceSpecialCharacters(String s)
    {
        return replaceSpecialCharacters(s, null);
    }

    public static final String replaceSpecialCharacters(String s, String s1)
    {
        StringBuffer stringbuffer = new StringBuffer(80);
        for(int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if(c >= 'A' && c <= 'Z' || c >= '0' && c <= '9')
                stringbuffer.append(c);
            else
            if(s1 != null && s1.indexOf(c) >= 0)
                stringbuffer.append(c);
            else
            if(c == '_')
                stringbuffer.append("__");
            else
            if(c == '@')
                stringbuffer.append("_x");
            else
            if(c == '#')
                stringbuffer.append("_y");
            else
            if(c == '$')
                stringbuffer.append("_z");
            else
                stringbuffer.append(unicodeEscapeSequence(c, true));
        }

        return stringbuffer.toString();
    }

    public static final String replaceSubstring(String s, String s1, String s2)
    {
        StringBuffer stringbuffer = new StringBuffer(80);
        int i = s1.length();
        int j = 0;
        if(i > 0)
        {
            for(int k = s.indexOf(s1); k >= 0; k = s.indexOf(s1, j))
            {
                stringbuffer.append(s.substring(j, k));
                stringbuffer.append(s2);
                j = k + i;
            }

        }
        stringbuffer.append(s.substring(j));
        return stringbuffer.toString();
    }

    public static final String replaceSubstring(String s, String s1, String s2, int i)
    {
        StringBuffer stringbuffer = new StringBuffer(80);
        int j = s1.length();
        int k = 0;
        if(j > 0)
        {
            int l = s.indexOf(s1, i);
            if(l >= 0)
            {
                stringbuffer.append(s.substring(k, l));
                stringbuffer.append(s2);
                k = l + j;
                stringbuffer.append(s.substring(k));
            }
        }
        return stringbuffer.toString();
    }

    public static final String undoReplaceSpecialChars(String s)
    {
        StringBuffer stringbuffer = new StringBuffer(80);
        for(int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if(c == '_')
            {
                i++;
                char c1 = s.charAt(i);
                switch(c1)
                {
                case 95: // '_'
                    stringbuffer.append('_');
                    break;

                case 120: // 'x'
                    stringbuffer.append('@');
                    break;

                case 121: // 'y'
                    stringbuffer.append('#');
                    break;

                case 122: // 'z'
                    stringbuffer.append('$');
                    break;

                default:
                    stringbuffer.append(undoUnicodeEscapeSequence(s.substring(i - 1, i + 4), true));
                    i += 3;
                    break;
                }
            } else
            {
                stringbuffer.append(c);
            }
        }

        return stringbuffer.toString();
    }

    public static final char undoUnicodeEscapeSequence(String s, boolean flag)
    {
        return (char)hexStringToInteger(s.substring(1), flag);
    }

    public static final String unicodeEscapeSequence(char c)
    {
        return unicodeEscapeSequence(c, false);
    }

    public static final String unicodeEscapeSequence(char c, boolean flag)
    {
        String s = integerToHexString(c, flag);
        String s1;
        if(flag)
            s1 = "aaaa";
        else
            s1 = "0000";
        s = s1.substring(s.length()) + s;
        return '_' + s;
    }

    public static final String getLayerPrefix()
    {
        return "l<%=zOrder%>_";
    }

    public static String replaceInapplicableCharacters(String s)
    {
        String s1 = s;
        try
        {
            s1 = replaceSubstring(s1, "\\r\\n", System.getProperty("line.separator"));
            s1 = replaceSubstring(s1, "\\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        catch(Throwable throwable)
        {
            s1 = s;
        }
        return s1;
    }

    public static final String getJavaString(String s)
    {
        if(s != null)
            return replaceSubstring(s, "\"", "\\\"");
        else
            return null;
    }

    public static final String getXMLString(String s)
    {
        String s1 = null;
        if(s != null)
        {
            s1 = replaceSubstring(s, "&", "&amp;");
            s1 = replaceSubstring(s1, "\"", "&quot;");
            s1 = replaceSubstring(s1, "<", "&lt;");
            s1 = replaceSubstring(s1, ">", "&gt;");
        }
        return s1;
    }

    public static final String toUTF8String(String s)
    {
        try
        {
            s = new String(s.getBytes("ISO-8859-1"), "UTF-8");
        }
        catch(Exception exception)
        {
            System.out.println("Can not handle UTF-8 encoding");
        }
        return s;
    }

    public static final String trimLeadingSpaces(String s)
    {
        if(s == null || s.length() == 0)
            return s;
        if(s.trim().equals(""))
            return "";
        int i = -1;
        for(int j = 0; j < s.length(); j++)
        {
            if(s.charAt(j) != ' ')
                break;
            i = j;
        }

        return s.substring(i + 1);
    }

    public static String softSubstring(String s, int i, int j)
        throws StringIndexOutOfBoundsException
    {
        if(i < 0 || j < 0 || j < i)
            throw new StringIndexOutOfBoundsException("Invalid begin and/or end index parameter to WebfacingConstants.softSubstring(String,int,int).");
        try
        {
            return s.substring(i, j);
        }
        catch(StringIndexOutOfBoundsException stringindexoutofboundsexception) { }
        try
        {
            return s.substring(i);
        }
        catch(StringIndexOutOfBoundsException stringindexoutofboundsexception1)
        {
            return "";
        }
    }

    public static String softSubstring(String s, int i, int j, int k)
        throws StringIndexOutOfBoundsException
    {
        if(i < 0 || j < 0 || j < i)
            throw new StringIndexOutOfBoundsException("Invalid begin and/or end index parameter to WebfacingConstants.softSubstring(String,int,int).");
        try
        {
            int l = 0;
            int i1 = 0;
            int j1;
            for(j1 = 0; j1 < i;)
            {
                char c = s.charAt(l++);
                j1++;
                if(DBCSUtil.isDBCS(c, k))
                    j1++;
            }

            if(j1 > i)
            {
                j1 = i;
                l--;
            }
            i1 = l;
            while(j1 < j) 
            {
                char c1 = s.charAt(i1++);
                j1++;
                if(DBCSUtil.isDBCS(c1, k))
                    j1++;
            }
            String s1;
            if(j1 > j)
            {
                i1--;
                s1 = s.substring(l, i1) + " ";
            } else
            {
                s1 = s.substring(l, i1);
            }
            return s1;
        }
        catch(StringIndexOutOfBoundsException stringindexoutofboundsexception) { }
        try
        {
            return s.substring(i);
        }
        catch(StringIndexOutOfBoundsException stringindexoutofboundsexception1)
        {
            return "";
        }
    }

    public static final String trimQuotes(String s)
    {
        if(s == null || s.length() == 0)
            return s;
        char c = s.charAt(0);
        if(c != '\'' && c != '"')
            return s;
        else
            return s.substring(1, s.length() - 1);
    }

    public static final Iterator reverseIterator(Iterator iterator)
    {
        Vector vector = new Vector();
        for(; iterator.hasNext(); vector.insertElementAt(iterator.next(), 0));
        return vector.iterator();
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 1999-2003, all rights reserved.");
    public static final ResourceBundle RUNTIME_MRI_BUNDLE = ResourceBundle.getBundle("com/ibm/as400ad/webfacing/runtime/rtmessage");
    public static final String RUNTIME_MRI_BUNDLE_PACKAGE = "com.ibm.as400ad.webfacing.runtime";
    public static final String RUNTIME_MRI_BUNDLE_NAME = "rtmessage";
    private static String _blanks = null;
    public static final String FIELDNAME_SEPARATOR = "$";
    public static final String LAYER_PREFIX = "l";
    public static final String LAYER_SEPARATOR = "_";
    public static final String SOURCE_QUALIFIED_RECORD_SEPARATOR = "$";
    public static final String RECORD_JSPS_WEB_DIR = "RecordJSPs";
    public static final String STYLES_DIR_NAME = "styles";
    public static final String STYLES_DIR = "styles" + System.getProperty("file.separator");
    public static final String GENERATED_IMAGES_WEB_DIR = "/images/generated";
    public static final String WEBFACING_WEBSITE_URL = "http://www.ibm.com/software/ad/wdt400/webfacing/";
    public static final String UIM_WEB_DIR = "UIMHelp";
    public static final String RANGE_SEPARATOR = ",";
    public static final String RANGE_INDICATOR = "-";
    public static final String FIELD_NEVER_VISIBLE = "NEVER";

}
