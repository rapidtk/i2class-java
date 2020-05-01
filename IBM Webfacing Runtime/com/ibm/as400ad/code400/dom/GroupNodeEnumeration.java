// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import java.util.Enumeration;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            GroupNode

public class GroupNodeEnumeration
    implements Enumeration
{

    public GroupNodeEnumeration(Vector vector)
    {
        index = 0;
        groups = null;
        index = 0;
    }

    public boolean hasMoreElements()
    {
        if(groups == null)
            return false;
        else
            return index < groups.size();
    }

    public Object nextElement()
    {
        if(groups != null && index < groups.size())
            return groups.elementAt(index++);
        else
            return null;
    }

    public GroupNode nextGroup()
    {
        return (GroupNode)nextElement();
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2001");
    private int index;
    private Vector groups;

}
