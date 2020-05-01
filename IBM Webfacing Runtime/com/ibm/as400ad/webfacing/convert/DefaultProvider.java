// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert;


public final class DefaultProvider
{

    public DefaultProvider()
    {
    }

    public static final Object makeInstance(String s, String s1)
    {
        String s2 = s;
        Class class1 = null;
        Object obj1 = null;
        if(null == s || s.length() < 2)
            s2 = s1;
        try
        {
            class1 = Class.forName(s2);
        }
        catch(Throwable throwable)
        {
            String s3 = s1;
            try
            {
                class1 = Class.forName(s3);
            }
            catch(Throwable throwable2)
            {
                class1 = null;
            }
        }
        if(null != class1)
            try
            {
                obj1 = class1.newInstance();
            }
            catch(Throwable throwable1)
            {
                try
                {
                    String s4 = s1;
                    Class class2 = Class.forName(s4);
                    obj1 = class2.newInstance();
                }
                catch(Throwable throwable3)
                {
                    Object obj = null;
                    obj1 = null;
                }
            }
        return obj1;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2000");
    public static final String DEFAULTWebFaceConverter = "com.ibm.as400ad.webfacing.convert.WebFaceConverter";
    public static final String DEFAULTConversionFactory = "com.ibm.as400ad.webfacing.convert.ConversionFactory";

}
