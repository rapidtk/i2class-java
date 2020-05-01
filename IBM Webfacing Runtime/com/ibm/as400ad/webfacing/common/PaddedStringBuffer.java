// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.common;

import com.ibm.as400ad.webfacing.util.DBCSUtil;

public class PaddedStringBuffer
{

    public PaddedStringBuffer(int i)
    {
        _impl = new StringBuffer(i);
    }

    public PaddedStringBuffer(String s)
    {
        _impl = new StringBuffer(s);
    }

    public void append(char c)
    {
        _impl.append(c);
    }

    public void append(int i)
    {
        _impl.append(i);
    }

    public void append(long l)
    {
        _impl.append(l);
    }

    public void append(Object obj)
    {
        _impl.append(obj);
    }

    public void append(String s)
    {
        _impl.append(s);
    }

    public void appendBinaryInt(int i)
    {
        _impl.append((char)(i >> 16));
        _impl.append((char)i);
    }

    public void concat(String s, String s1, String s2, String s3)
    {
        _impl.append(s);
        _impl.append(s1);
        _impl.append(s2);
        _impl.append(s3);
    }

    public void concat(String s, String s1, String s2)
    {
        _impl.append(s);
        _impl.append(s1);
        _impl.append(s2);
    }

    public void concat(String s, String s1, String s2, String s3, String s4)
    {
        _impl.append(s);
        _impl.append(s1);
        _impl.append(s2);
        _impl.append(s3);
        _impl.append(s4);
    }

    public void concat(String s, String s1, String s2, String s3, String s4, String s5)
    {
        _impl.append(s);
        _impl.append(s1);
        _impl.append(s2);
        _impl.append(s3);
        _impl.append(s4);
        _impl.append(s5);
    }

    public void concat(String s, String s1, String s2, String s3, String s4, String s5, String s6)
    {
        _impl.append(s);
        _impl.append(s1);
        _impl.append(s2);
        _impl.append(s3);
        _impl.append(s4);
        _impl.append(s5);
        _impl.append(s6);
    }

    public void concat(String s, String s1, String s2, String s3, String s4, String s5, String s6, 
            String s7)
    {
        _impl.append(s);
        _impl.append(s1);
        _impl.append(s2);
        _impl.append(s3);
        _impl.append(s4);
        _impl.append(s5);
        _impl.append(s6);
        _impl.append(s7);
    }

    public void concat(String s, String s1, String s2, String s3, String s4, String s5, String s6, 
            String s7, String s8)
    {
        _impl.append(s);
        _impl.append(s1);
        _impl.append(s2);
        _impl.append(s3);
        _impl.append(s4);
        _impl.append(s5);
        _impl.append(s6);
        _impl.append(s7);
        _impl.append(s8);
    }

    public void concat(String s, String s1, String s2, String s3, String s4, String s5, String s6, 
            String s7, String s8, String s9)
    {
        _impl.append(s);
        _impl.append(s1);
        _impl.append(s2);
        _impl.append(s3);
        _impl.append(s4);
        _impl.append(s5);
        _impl.append(s6);
        _impl.append(s7);
        _impl.append(s8);
        _impl.append(s9);
    }

    public void concat(String s, String s1, String s2, String s3, String s4, String s5, String s6, 
            String s7, String s8, String s9, String s10)
    {
        _impl.append(s);
        _impl.append(s1);
        _impl.append(s2);
        _impl.append(s3);
        _impl.append(s4);
        _impl.append(s5);
        _impl.append(s6);
        _impl.append(s7);
        _impl.append(s8);
        _impl.append(s9);
        _impl.append(s10);
    }

    public void concat(String s, String s1, String s2, String s3, String s4, String s5, String s6, 
            String s7, String s8, String s9, String s10, String s11)
    {
        _impl.append(s);
        _impl.append(s1);
        _impl.append(s2);
        _impl.append(s3);
        _impl.append(s4);
        _impl.append(s5);
        _impl.append(s6);
        _impl.append(s7);
        _impl.append(s8);
        _impl.append(s9);
        _impl.append(s10);
        _impl.append(s11);
    }

    public void concat(String s, String s1, String s2, String s3, String s4, String s5, String s6, 
            String s7, String s8, String s9, String s10, String s11, String s12)
    {
        _impl.append(s);
        _impl.append(s1);
        _impl.append(s2);
        _impl.append(s3);
        _impl.append(s4);
        _impl.append(s5);
        _impl.append(s6);
        _impl.append(s7);
        _impl.append(s8);
        _impl.append(s9);
        _impl.append(s10);
        _impl.append(s11);
        _impl.append(s12);
    }

    public void concat(String s, String s1, String s2, String s3, String s4, String s5, String s6, 
            String s7, String s8, String s9, String s10, String s11, String s12, String s13)
    {
        _impl.append(s);
        _impl.append(s1);
        _impl.append(s2);
        _impl.append(s3);
        _impl.append(s4);
        _impl.append(s5);
        _impl.append(s6);
        _impl.append(s7);
        _impl.append(s8);
        _impl.append(s9);
        _impl.append(s10);
        _impl.append(s11);
        _impl.append(s12);
        _impl.append(s13);
    }

    public void concat(String s, String s1, String s2, String s3, String s4, String s5, String s6, 
            String s7, String s8, String s9, String s10, String s11, String s12, String s13, 
            String s14)
    {
        _impl.append(s);
        _impl.append(s1);
        _impl.append(s2);
        _impl.append(s3);
        _impl.append(s4);
        _impl.append(s5);
        _impl.append(s6);
        _impl.append(s7);
        _impl.append(s8);
        _impl.append(s9);
        _impl.append(s10);
        _impl.append(s11);
        _impl.append(s12);
        _impl.append(s13);
        _impl.append(s14);
    }

    public void concat(String s, String s1, String s2, String s3, String s4, String s5, String s6, 
            String s7, String s8, String s9, String s10, String s11, String s12, String s13, 
            String s14, String s15)
    {
        _impl.append(s);
        _impl.append(s1);
        _impl.append(s2);
        _impl.append(s3);
        _impl.append(s4);
        _impl.append(s5);
        _impl.append(s6);
        _impl.append(s7);
        _impl.append(s8);
        _impl.append(s9);
        _impl.append(s10);
        _impl.append(s11);
        _impl.append(s12);
        _impl.append(s13);
        _impl.append(s14);
        _impl.append(s15);
    }

    public void concat(String s, String s1, String s2, String s3, String s4, String s5, String s6, 
            String s7, String s8, String s9, String s10, String s11, String s12, String s13, 
            String s14, String s15, String s16)
    {
        _impl.append(s);
        _impl.append(s1);
        _impl.append(s2);
        _impl.append(s3);
        _impl.append(s4);
        _impl.append(s5);
        _impl.append(s6);
        _impl.append(s7);
        _impl.append(s8);
        _impl.append(s9);
        _impl.append(s10);
        _impl.append(s11);
        _impl.append(s12);
        _impl.append(s13);
        _impl.append(s14);
        _impl.append(s15);
        _impl.append(s16);
    }

    public void concat(String s, String s1, String s2, String s3, String s4, String s5, String s6, 
            String s7, String s8, String s9, String s10, String s11, String s12, String s13, 
            String s14, String s15, String s16, String s17)
    {
        _impl.append(s);
        _impl.append(s1);
        _impl.append(s2);
        _impl.append(s3);
        _impl.append(s4);
        _impl.append(s5);
        _impl.append(s6);
        _impl.append(s7);
        _impl.append(s8);
        _impl.append(s9);
        _impl.append(s10);
        _impl.append(s11);
        _impl.append(s12);
        _impl.append(s13);
        _impl.append(s14);
        _impl.append(s15);
        _impl.append(s16);
        _impl.append(s17);
    }

    public void concat(String s, String s1, String s2, String s3, String s4, String s5, String s6, 
            String s7, String s8, String s9, String s10, String s11, String s12, String s13, 
            String s14, String s15, String s16, String s17, String s18)
    {
        _impl.append(s);
        _impl.append(s1);
        _impl.append(s2);
        _impl.append(s3);
        _impl.append(s4);
        _impl.append(s5);
        _impl.append(s6);
        _impl.append(s7);
        _impl.append(s8);
        _impl.append(s9);
        _impl.append(s10);
        _impl.append(s11);
        _impl.append(s12);
        _impl.append(s13);
        _impl.append(s14);
        _impl.append(s15);
        _impl.append(s16);
        _impl.append(s17);
        _impl.append(s18);
    }

    public void concat(String s, String s1, String s2, String s3, String s4, String s5, String s6, 
            String s7, String s8, String s9, String s10, String s11, String s12, String s13, 
            String s14, String s15, String s16, String s17, String s18, String s19)
    {
        _impl.append(s);
        _impl.append(s1);
        _impl.append(s2);
        _impl.append(s3);
        _impl.append(s4);
        _impl.append(s5);
        _impl.append(s6);
        _impl.append(s7);
        _impl.append(s8);
        _impl.append(s9);
        _impl.append(s10);
        _impl.append(s11);
        _impl.append(s12);
        _impl.append(s13);
        _impl.append(s14);
        _impl.append(s15);
        _impl.append(s16);
        _impl.append(s17);
        _impl.append(s18);
        _impl.append(s19);
    }

    public boolean isAllBlank()
    {
        return isAllChar(' ');
    }

    public boolean isAllChar(char c)
    {
        int i = _impl.length();
        if(0 == i)
            return false;
        for(int j = 0; j < i; j++)
            if(c != _impl.charAt(j))
                return false;

        return true;
    }

    public boolean isAllDBCSBlank()
    {
        return isAllChar('\u3000');
    }

    public boolean isCharDBCS(int i, int j)
    {
        if(i < 0 || i > _impl.length() - 1)
            throw new IllegalArgumentException("Parameter i to PaddedStringBuffer.isCharDBCS is out of range.");
        else
            return DBCSUtil.isDBCS(_impl.charAt(i), j);
    }

    public boolean isEmptyOrBlank()
    {
        return _impl.length() == 0 || isAllBlank();
    }

    public PaddedStringBuffer padLeft(char c, int i)
    {
        int j = i - _impl.length();
        if(j > 0)
        {
            String s = "";
            for(int k = 0; k < j; k++)
                _impl.insert(0, c);

        }
        return this;
    }

    public void padRightOrTruncateToLength(int i, char c)
    {
        if(_impl.length() < i)
            padRight(c, i);
        else
        if(_impl.length() > i)
            _impl.setLength(i);
    }

    public void padLeftOrTruncateToLength(int i, char c)
    {
        if(_impl.length() < i)
            padLeft(c, i);
        else
        if(_impl.length() > i)
            setValue(_impl.substring(_impl.length() - i));
    }

    public PaddedStringBuffer padRight(char c, int i)
    {
        int j = i - _impl.length();
        if(j > 0)
        {
            for(int k = 0; k < j; k++)
                _impl.append(c);

        }
        return this;
    }

    public void replaceSubstring(String s, String s1)
    {
        replaceSubstring(_impl, _impl.toString(), s, s1);
    }

    public static final String replaceSubstring(String s, String s1, String s2)
    {
        StringBuffer stringbuffer = new StringBuffer(80);
        replaceSubstring(stringbuffer, s, s1, s2);
        return stringbuffer.toString();
    }

    public static final void replaceSubstring(StringBuffer stringbuffer, String s, String s1, String s2)
    {
        stringbuffer.setLength(0);
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
    }

    public void setValue(String s)
    {
        _impl = new StringBuffer(s);
    }

    public void setValue(StringBuffer stringbuffer)
    {
        _impl = stringbuffer;
    }

    public String toString()
    {
        return _impl.toString();
    }

    public StringBuffer toStringBuffer()
    {
        return _impl;
    }

    public void truncateAllSpacesFromRight()
    {
        int i;
        for(i = _impl.length() - 1; i >= 0 && (_impl.charAt(i) == ' ' || _impl.charAt(i) == '\u3000'); i--);
        if(i == -1)
            setValue("");
        else
            setValue(_impl.substring(0, i + 1));
    }

    public int truncateAllSpacesFromLeft()
    {
        int i;
        for(i = 0; i < _impl.length() && (_impl.charAt(i) == ' ' || _impl.charAt(i) == '\u3000'); i++);
        if(i == _impl.length())
            setValue("");
        else
            setValue(_impl.substring(i));
        return i;
    }

    public void toUpperCaseIgnoreDBCS(int i)
    {
        StringBuffer stringbuffer = new StringBuffer("");
        for(int j = 0; j < _impl.length(); j++)
        {
            char c = _impl.charAt(j);
            if(!isCharDBCS(j, i))
                c = Character.toUpperCase(c);
            stringbuffer.append(c);
        }

        setValue(stringbuffer);
    }

    public void toUpperCase()
    {
        setValue(_impl.toString().toUpperCase());
    }

    public boolean isAllNumbers()
    {
        int i = _impl.length();
        if(0 == i)
            return false;
        for(int j = 0; j < i; j++)
            if(!Character.isDigit(_impl.charAt(j)))
                return false;

        return true;
    }

    public void padLeftAndConcat(String s, int i, char c, String s1, int j, char c1, String s2, 
            int k, char c2, String s3)
    {
        setValue(s);
        padLeftOrTruncateToLength(i, c);
        if(j > 0)
        {
            PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(s1);
            paddedstringbuffer.padLeftOrTruncateToLength(j, c1);
            append(s3);
            append(paddedstringbuffer.toString());
        }
        if(k > 0)
        {
            PaddedStringBuffer paddedstringbuffer1 = new PaddedStringBuffer(s2);
            paddedstringbuffer1.padLeftOrTruncateToLength(k, c2);
            append(s3);
            append(paddedstringbuffer1.toString());
        }
    }

    public void padLeftAndConcat(String s, int i, String s1, int j, String s2, int k, String s3, 
            char c)
    {
        padLeftAndConcat(s, i, c, s1, j, c, s2, k, c, s3);
    }

    public void deleteCharAt(int i)
    {
        _impl.deleteCharAt(i);
    }

    public void setCharAt(int i, char c)
    {
        _impl.setCharAt(i, c);
    }

    public void insert(int i, char c)
    {
        _impl.insert(i, c);
    }

    public void insert(int i, String s)
    {
        _impl.insert(i, s);
    }

    public int length()
    {
        return _impl.length();
    }

    public char charAt(int i)
    {
        return _impl.charAt(i);
    }

    public boolean containsChar(char c)
    {
        boolean flag = false;
        for(int i = 0; i < _impl.length(); i++)
            if(_impl.charAt(i) == c)
            {
                flag = true;
                i = 9999;
            }

        return flag;
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 1999=2003, all rights reserved.");
    StringBuffer _impl;

}
