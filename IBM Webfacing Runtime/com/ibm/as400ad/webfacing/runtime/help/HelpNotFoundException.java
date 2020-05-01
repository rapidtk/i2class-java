// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.help;


public class HelpNotFoundException extends Exception
{

    public HelpNotFoundException()
    {
        _type = 0;
    }

    public HelpNotFoundException(int i)
    {
        _type = 0;
        _type = i;
    }

    public String getJavaScriptHandler()
    {
        if(_type == 1)
            return "getExternalHelpNotFoundMsg()";
        if(_type == 2)
            return "getOutsideHelpAreaMsg()";
        else
            return "getExternalHelpNotFoundMsg()";
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2001, 2002, all rights reserved");
    private int _type;
    public static final int ExternalHelpNotFound = 1;
    private static final String ExternalHelpNotFoundFn = "getExternalHelpNotFoundMsg()";
    public static final int OutsideHelpArea = 2;
    private static final String OutsideHelpAreaFn = "getOutsideHelpAreaMsg()";

}
