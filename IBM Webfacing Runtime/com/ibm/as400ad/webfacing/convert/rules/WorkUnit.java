// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.rules;


// Referenced classes of package com.ibm.as400ad.webfacing.convert.rules:
//            Unit

class WorkUnit
{

    public WorkUnit()
    {
    }

    WorkUnit(Unit unit1, String s, String s1)
    {
        unit = unit1;
        remains = s;
        replacedString = s1;
    }

    public String getReplacedString()
    {
        return replacedString;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2000-2002 All rights reserved.");
    Unit unit;
    String remains;
    String replacedString;

}
