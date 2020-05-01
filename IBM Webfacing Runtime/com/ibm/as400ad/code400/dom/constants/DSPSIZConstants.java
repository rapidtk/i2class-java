// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom.constants;


public class DSPSIZConstants
{

    public DSPSIZConstants()
    {
    }

    public static final int getScreenHeight(int i)
    {
        byte byte0 = -1;
        switch(i)
        {
        case 0: // '\0'
            byte0 = 24;
            break;

        case 1: // '\001'
            byte0 = 27;
            break;
        }
        return byte0;
    }

    public static final int getScreenWidth(int i)
    {
        char c = '\uFFFF';
        switch(i)
        {
        case 0: // '\0'
            c = 'P';
            break;

        case 1: // '\001'
            c = '\204';
            break;
        }
        return c;
    }

    public static final int DS3_Index = 0;
    public static final int DS4_Index = 1;
    public static final int DS3_Height = 24;
    public static final int DS4_Height = 27;
    public static final int DS3_Width = 80;
    public static final int DS4_Width = 132;
    public static final String DS3_IBM_name = "*DS3";
    public static final String DS4_IBM_name = "*DS4";
}
