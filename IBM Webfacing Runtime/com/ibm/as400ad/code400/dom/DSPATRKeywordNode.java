// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            KeywordNode, KeywordParm, AnyNode

public final class DSPATRKeywordNode extends KeywordNode
{

    public DSPATRKeywordNode(AnyNode anynode)
    {
        super(anynode);
        dspatrCache = -99;
    }

    public int getDspatrParm()
    {
        if(dspatrCache != -99)
            return dspatrCache;
        Vector vector = getParmsVector();
        Object obj = null;
        dspatrCache = 0;
        if(vector != null)
        {
            for(int i = 0; i < vector.size(); i++)
            {
                KeywordParm keywordparm = (KeywordParm)vector.elementAt(i);
                dspatrCache |= mapTokenToDspAtr(keywordparm.getVarParmToken());
            }

        } else
        {
            return 0;
        }
        return dspatrCache;
    }

    public static final int mapDspAtrToToken(int i)
    {
        char c = '\0';
        switch(i)
        {
        case 4: // '\004'
            c = '\u010F';
            break;

        case 16: // '\020'
            c = '\u0112';
            break;

        case 32: // ' '
            c = '\u010E';
            break;

        case 2: // '\002'
            c = '\u0111';
            break;

        case 1: // '\001'
            c = '\u0110';
            break;

        case 64: // '@'
            c = '\u0115';
            break;

        case 256: 
            c = '\u0117';
            break;

        case 1024: 
            c = '\u0114';
            break;

        case 2048: 
            c = '\u0118';
            break;

        case 128: 
            c = '\u0113';
            break;

        case 512: 
            c = '\u0116';
            break;
        }
        return c;
    }

    public static final int mapTokenToDspAtr(int i)
    {
        char c = '\0';
        switch(i)
        {
        case 273: 
            c = '\002';
            break;

        case 270: 
            c = ' ';
            break;

        case 271: 
            c = '\004';
            break;

        case 277: 
            c = '@';
            break;

        case 274: 
            c = '\020';
            break;

        case 272: 
            c = '\001';
            break;

        case 279: 
            c = '\u0100';
            break;

        case 276: 
            c = '\u0400';
            break;

        case 275: 
            c = '\200';
            break;

        case 278: 
            c = '\u0200';
            break;
        }
        return c;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2001");
    private int dspatrCache;

}
