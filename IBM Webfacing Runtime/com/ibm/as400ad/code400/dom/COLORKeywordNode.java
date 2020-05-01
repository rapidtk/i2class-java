// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;


// Referenced classes of package com.ibm.as400ad.code400.dom:
//            KeywordNode, KeywordParm, AnyNode

public final class COLORKeywordNode extends KeywordNode
{

    public COLORKeywordNode(AnyNode anynode)
    {
        super(anynode);
        colorCache = -99;
    }

    public int getColorParm()
    {
        if(colorCache != -99)
            return colorCache;
        KeywordParm keywordparm = getFirstParm();
        if(keywordparm != null)
            colorCache = mapTokenToColor(keywordparm.getVarParmToken());
        else
            return 0;
        return colorCache;
    }

    public static final int mapColorToToken(int i)
    {
        char c = '\0';
        switch(i)
        {
        case 4: // '\004'
            c = '\u0106';
            break;

        case 7: // '\007'
            c = '\u0107';
            break;

        case 2: // '\002'
            c = '\u0108';
            break;

        case 5: // '\005'
            c = '\u0109';
            break;

        case 6: // '\006'
            c = '\u010A';
            break;

        case 3: // '\003'
            c = '\u010B';
            break;

        case 1: // '\001'
            c = '\u010C';
            break;

        case 14: // '\016'
            c = '\u0252';
            break;

        case -2: 
            c = '\u0107';
            break;

        case -1: 
            c = '\u0251';
            break;

        case -3: 
            c = '\0';
            break;
        }
        return c;
    }

    public static final int mapTokenToColor(int i)
    {
        byte byte0 = 0;
        switch(i)
        {
        case 262: 
            byte0 = 4;
            break;

        case 263: 
            byte0 = 7;
            break;

        case 264: 
            byte0 = 2;
            break;

        case 265: 
            byte0 = 5;
            break;

        case 266: 
            byte0 = 6;
            break;

        case 267: 
            byte0 = 3;
            break;

        case 268: 
            byte0 = 1;
            break;

        case 594: 
            byte0 = 14;
            break;

        case 593: 
            byte0 = 7;
            break;

        default:
            byte0 = -3;
            break;
        }
        return byte0;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2001");
    private int colorCache;

}
