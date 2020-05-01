// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import java.util.Enumeration;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            HelpspecNode

public class HelpspecNodeEnumeration
    implements Enumeration
{

    public HelpspecNodeEnumeration(Vector vector)
    {
        index = 0;
        helpspec = null;
        helpspec = vector;
        index = 0;
    }

    public boolean hasMoreElements()
    {
        if(helpspec == null)
            return false;
        else
            return index < helpspec.size();
    }

    public Object nextElement()
    {
        if(helpspec != null && index < helpspec.size())
            return helpspec.elementAt(index++);
        else
            return null;
    }

    public HelpspecNode nextHelpspec()
    {
        return (HelpspecNode)nextElement();
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2001");
    private int index;
    private Vector helpspec;

}
