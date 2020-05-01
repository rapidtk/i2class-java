// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import java.util.Enumeration;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            KeywordParm

public class KeywordParmEnumeration
    implements Enumeration
{

    public boolean hasMoreElements()
    {
        if(parms == null)
            return false;
        else
            return index < parms.size();
    }

    public Object nextElement()
    {
        if(parms == null)
            return null;
        if(index < parms.size())
            return parms.elementAt(index++);
        else
            return null;
    }

    public KeywordParmEnumeration(Vector vector)
    {
        parms = vector;
        index = 0;
    }

    public KeywordParm nextParm()
    {
        return (KeywordParm)nextElement();
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2001");
    private int index;
    private Vector parms;

}
