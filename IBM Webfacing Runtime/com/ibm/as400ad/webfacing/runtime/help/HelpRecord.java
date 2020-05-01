// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.help;


// Referenced classes of package com.ibm.as400ad.webfacing.runtime.help:
//            HelpDefinition

public class HelpRecord extends HelpDefinition
{

    public HelpRecord(String s)
    {
        this(null, s, null, null);
    }

    public HelpRecord(String s, String s1)
    {
        this(s, s1, null, null);
    }

    public HelpRecord(String s, String s1, String s2)
    {
        this(s, s1, s2, null);
    }

    public HelpRecord(String s, String s1, String s2, String s3)
    {
        super(s, s1, s2, s3);
        setType("*FILE");
    }

    public String toString()
    {
        return "HLPRCD(" + getDefinition() + ")";
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2001, 2002, all rights reserved");

}
