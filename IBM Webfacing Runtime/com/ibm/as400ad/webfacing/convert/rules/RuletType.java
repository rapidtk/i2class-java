// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.rules;


public class RuletType
{

    public RuletType()
    {
        type = "unknown";
    }

    public RuletType(String s)
    {
        type = s;
    }

    public boolean equals(Object obj)
    {
        return (obj instanceof RuletType) && type.equals(((RuletType)obj).type);
    }

    public String toString()
    {
        return type;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2000-2002 All rights reserved.");
    private String type;
    public static RuletType KEY_PATTERN = new RuletType("KEY_PATTERN");
    public static RuletType DSPF_MENU_PATTERN = new RuletType("DSPF_MENU_PATTERN");

}
