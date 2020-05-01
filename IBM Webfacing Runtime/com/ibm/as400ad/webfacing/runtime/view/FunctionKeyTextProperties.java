// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;


public class FunctionKeyTextProperties
{

    public FunctionKeyTextProperties(String s, int i)
    {
        label = s;
        priority = i;
    }

    public String getLabel()
    {
        return label;
    }

    public int getPriority()
    {
        return priority;
    }

    public String toString()
    {
        return null;
    }

    private String label;
    private int priority;
    private static final String priorities[] = {
        "UNDEFINED", "DEFAULT", "DDS_DEFINITION", "PATTERN_ON_SCREEN", "WEBSETTING"
    };
    public static final int DEFAULT = 0;
    public static final int DDS_DEFINITION = 1;
    public static final int PATTERN_ON_SCREEN = 2;
    public static final int WEBSETTING = 3;
    public static final int UNDEFINED = -1;

}
