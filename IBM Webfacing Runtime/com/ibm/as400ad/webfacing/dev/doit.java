// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.dev;

import com.ibm.as400ad.code400.dom.WebfaceInvoker;
import com.ibm.as400ad.webfacing.convert.ICustomTagExtensions;
import java.io.PrintStream;

public class doit
{

    public doit()
    {
    }

    public static void main(String args[])
    {
        String s = null;
        String s1 = null;
        String s2 = null;
        String s3 = null;
        ICustomTagExtensions icustomtagextensions = null;
        if(args != null && args.length > 0)
        {
            s = WebfaceInvoker.restoreBlanks(args[0]);
            if(args.length > 1)
            {
                s1 = WebfaceInvoker.restoreBlanks(args[1]);
                if(args.length > 2)
                {
                    s2 = WebfaceInvoker.restoreBlanks(args[2]);
                    if(args.length > 3)
                        s3 = WebfaceInvoker.restoreBlanks(args[3]);
                }
            }
        }
        if(null == s || s.trim().length() < 5)
            s = "D:\\Program Files\\IBM\\wsa-base-20021125_2118-WB202-AD-V50D-GA\\workspace\\com.ibm.etools.iseries.webfacing\\buildtools\\INFO\\QDDSSRC\\QDSPMNU.xml";
        System.out.println("xmlFile = " + s);
        if(null == s1 || s1.trim().length() < 5)
            s1 = "d:\\wdsc";
        System.out.println("wfInstall = " + s1);
        if(null == s2 || s2.trim().length() < 5)
            s2 = "d:\\wdsc\\tmp";
        System.out.println("tmpDir = " + s2);
        if(null == s3 || s3.trim().length() < 5)
            s3 = "d:\\wdsc";
        System.out.println("projectRoot = " + s3);
        doIt(s, s1, s2, s3, icustomtagextensions);
    }

    private static boolean doIt(String s, String s1, String s2, String s3, ICustomTagExtensions icustomtagextensions)
    {
        return WebfaceInvoker.invokeWebFacing(s, s1, s2, s3, icustomtagextensions);
    }
}
