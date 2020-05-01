// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.help;

import java.io.Serializable;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.help:
//            HelpRecord, HelpDefinition

public class HelpGroup
    implements Serializable
{

    public HelpGroup(String s, HelpDefinition helpdefinition, int i)
    {
        _name = null;
        _sequence = -1;
        _definition = null;
        _name = s;
        _definition = helpdefinition;
        _sequence = i;
    }

    public HelpGroup(String s, String s1, int i)
    {
        this(s, ((HelpDefinition) (new HelpRecord(s1))), i);
    }

    public HelpDefinition getDefinition()
    {
        return _definition;
    }

    public String getName()
    {
        return _name;
    }

    public int getSequence()
    {
        return _sequence;
    }

    public void setDefinition(HelpDefinition helpdefinition)
    {
        _definition = helpdefinition;
    }

    public void setName(String s)
    {
        _name = s;
    }

    public String toString()
    {
        return "HLPSEQ(" + _name + " " + _sequence + ")";
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2001, 2002, all rights reserved");
    private String _name;
    private int _sequence;
    private HelpDefinition _definition;

}
