// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.util;


public class DBCSUtil
{

    public DBCSUtil()
    {
    }

    public static final boolean isDBCS(char c, int i)
    {
        byte byte0;
        if(i == 930 || i == 939 || i == 1399 || i == 5035 || i == 5026)
            byte0 = 0;
        else
        if(i == 933 || i == 1364)
            byte0 = 3;
        else
        if(i == 935 || i == 1388)
            byte0 = 1;
        else
        if(i == 937)
            byte0 = 2;
        else
            byte0 = 4;
        int j = lowerIndex[byte0].length;
        for(int k = 0; k < j; k++)
            if(c >= lowerIndex[byte0][k] && c <= highIndex[byte0][k])
                return true;

        return false;
    }

    static int lowerIndex[][] = {
        {
            128, 164, 166, 173, 4352, 7680, 8255, 8365, 44032, 63744, 
            65056, 65280, 65504, 65520
        }, {
            128, 164, 167, 173, 176, 1536, 3840, 4352, 8255, 8362, 
            63744, 65056, 65280, 65504, 65520
        }, {
            128, 164, 167, 173, 4352, 7680, 8362, 8365, 44032, 63744, 
            65056, 65280, 65504, 65520
        }, {
            128, 164, 167, 173, 176, 4352, 7680, 8255, 8362, 44032, 
            63744, 65056, 65280, 65504, 65520
        }, {
            4352, 12288, 12592, 44032, 63744, 65072, 65280, 65504
        }
    };
    static int highIndex[][] = {
        {
            161, 164, 171, 1279, 4607, 8253, 8363, 40869, 63599, 64335, 
            65135, 65375, 65510, 65535
        }, {
            161, 164, 171, 174, 1279, 1791, 4031, 8253, 8360, 63599, 
            64335, 65135, 65375, 65510, 65535
        }, {
            161, 164, 171, 1279, 4607, 8360, 8363, 40869, 63599, 64335, 
            65135, 65375, 65510, 65535
        }, {
            161, 164, 171, 174, 1279, 4607, 8253, 8360, 40869, 63599, 
            64335, 65135, 65375, 65510, 65535
        }, {
            4607, 12543, 40869, 63599, 64255, 65135, 65375, 65510
        }
    };

}
