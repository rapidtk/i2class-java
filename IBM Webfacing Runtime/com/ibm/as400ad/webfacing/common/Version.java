// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.common;


public final class Version
{

    public Version()
    {
    }

    public static final String getDTStamp()
    {
        return _dtstamp;
    }

    public static final int getModificationNo()
    {
        return 2;
    }

    public static final int getModificationNo(long l)
    {
        return (int)((l % 0xf4240L) / 10000L);
    }

    public static final int getReleaseNo()
    {
        return 1;
    }

    public static final int getReleaseNo(long l)
    {
        return (int)((l % 0x5f5e100L) / 0xf4240L);
    }

    public static final int getSPNo()
    {
        return 1;
    }

    public static final int getSPNo(long l)
    {
        return (int)((l % 10000L) / 100L);
    }

    public static final char getSubSPChar(long l)
    {
        return (char)(getSubSPNo(l) + 96);
    }

    public static final char getSubSPNo()
    {
        return _subServicePack;
    }

    public static final int getSubSPNo(long l)
    {
        return (int)(l % 100L);
    }

    public static final long getVersionDigits()
    {
        return getVersionDigits(5, 1, 2, 1, _subServicePack);
    }

    public static final long getVersionDigits(int i, int j, int k, int l)
    {
        return getVersionDigits(i, j, k, l, '`');
    }

    public static final long getVersionDigits(int i, int j, int k, int l, char c)
    {
        return (long)(i * 0x5f5e100 + j * 0xf4240 + k * 10000 + l * 100 + (c - 96));
    }

    public static final String getVersionDTStamp()
    {
        String s = "V5.1.0";
        String s1 = getDTStamp();
        if(!s1.equals("20030910 1557"))
            s = s + " " + s1;
        return s;
    }

    public static final int getVersionNo()
    {
        return 5;
    }

    public static final int getVersionNo(long l)
    {
        return (int)(l / 0x5f5e100L);
    }

    public String toString()
    {
        String s = getVersionDTStamp();
        return s;
    }

    private static final String copyRight = new String("(C) Copyright IBM Corporation 1999-2003 all rights reserved");
    private static final int _version = 5;
    private static final int _release = 1;
    private static final int _modification = 2;
    private static final int _servicePack = 1;
    private static char _subServicePack = '`';
    private static final char _defaultSubSP = 96;
    private static String _dtstamp = "20030910 1557";

}
