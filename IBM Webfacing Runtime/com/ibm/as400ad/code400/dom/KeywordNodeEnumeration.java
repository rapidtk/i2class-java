// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import java.util.Enumeration;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            KeywordNode

public class KeywordNodeEnumeration
    implements Enumeration
{

    public boolean hasMoreElements()
    {
        if(keywords == null)
            return false;
        else
            return index < keywords.size();
    }

    public Object nextElement()
    {
        if(keywords != null && index < keywords.size())
            return keywords.elementAt(index++);
        else
            return null;
    }

    public KeywordNode nextKeyword()
    {
        return (KeywordNode)nextElement();
    }

    public KeywordNodeEnumeration(Vector vector)
    {
        index = 0;
        keywords = null;
        keywords = vector;
        index = 0;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2001");
    private int index;
    private Vector keywords;

}
