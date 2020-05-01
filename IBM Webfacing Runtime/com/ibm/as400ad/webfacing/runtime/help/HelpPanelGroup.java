// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.help;


// Referenced classes of package com.ibm.as400ad.webfacing.runtime.help:
//            HelpDefinition

public class HelpPanelGroup extends HelpDefinition
{

    public HelpPanelGroup(String s)
    {
        this(null, null, s.substring(s.indexOf('/') + 1), s.substring(0, s.indexOf('/')));
    }

    public HelpPanelGroup(String s, String s1)
    {
        this(null, null, s, s1);
    }

    public HelpPanelGroup(String s, String s1, String s2)
    {
        this(s, s1, s2, null);
    }

    public HelpPanelGroup(String s, String s1, String s2, String s3)
    {
        super(s, s1, s2, s3);
        setType("*PNLGRP");
    }

    public String toString()
    {
        return "HLPPNLGRP(" + getDefinition() + ")";
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2001, 2002, all rights reserved");

}
