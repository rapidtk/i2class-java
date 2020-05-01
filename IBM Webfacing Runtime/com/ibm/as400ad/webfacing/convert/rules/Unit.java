// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.rules;


public class Unit
{

    Unit(int i, String s)
    {
        sequenceNumber = i;
        text = s;
    }

    public int sequenceNumber;
    public String text;
    static final String copyRight = new String(" (C) Copyright IBM Corporation 2000-2002 All rights reserved.");

}
