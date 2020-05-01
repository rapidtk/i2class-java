// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.rules;


public class Property
{

    public Property()
    {
        this("noname", "");
    }

    public Property(String s, String s1)
    {
        key = s;
        value = s1;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2000-2002 All rights reserved.");
    public static String KEY_PREFIX = "prefix";
    public static String KEY_SEPARATOR = "separator";
    public String key;
    public String value;

}
