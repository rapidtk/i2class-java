// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.help;


public abstract class GenericHelpDefinition
{

    public GenericHelpDefinition(String s)
    {
        _definition = null;
        _definition = s;
    }

    public String getDefinition()
    {
        return _definition;
    }

    public void setDefinition(String s)
    {
        _definition = s;
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2001, 2002, all rights reserved");
    private String _definition;

}
