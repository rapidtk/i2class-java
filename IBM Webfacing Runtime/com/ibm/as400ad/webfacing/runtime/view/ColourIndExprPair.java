// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;


public class ColourIndExprPair
{

    public ColourIndExprPair(String s, String s1)
    {
        colour = null;
        indExpr = null;
        colour = s;
        indExpr = s1;
    }

    public String getColour()
    {
        return colour;
    }

    public String getIndExpr()
    {
        return indExpr;
    }

    private String colour;
    private String indExpr;
}
