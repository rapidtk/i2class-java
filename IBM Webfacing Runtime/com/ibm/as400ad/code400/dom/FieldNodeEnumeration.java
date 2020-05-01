// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import java.util.Enumeration;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            FieldNode

public class FieldNodeEnumeration
    implements Enumeration
{

    protected FieldNodeEnumeration(Vector vector)
    {
        fields = vector;
        index = 0;
    }

    public boolean hasMoreElements()
    {
        if(fields == null)
            return false;
        else
            return index < fields.size();
    }

    public Object nextElement()
    {
        if(fields != null && index < fields.size())
            return fields.elementAt(index++);
        else
            return null;
    }

    public FieldNode nextField()
    {
        return (FieldNode)nextElement();
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2001");
    private int index;
    private Vector fields;

}
