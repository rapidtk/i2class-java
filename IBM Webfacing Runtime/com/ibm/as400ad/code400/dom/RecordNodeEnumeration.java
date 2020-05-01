// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.dom;

import java.util.Enumeration;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.code400.dom:
//            RecordNode

public class RecordNodeEnumeration
    implements Enumeration
{

    public RecordNodeEnumeration(Vector vector)
    {
        records = null;
        records = vector;
        index = 0;
    }

    public boolean hasMoreElements()
    {
        if(records == null)
            return false;
        else
            return index < records.size();
    }

    public Object nextElement()
    {
        if(records != null && index < records.size())
            return records.elementAt(index++);
        else
            return null;
    }

    public RecordNode nextRecord()
    {
        return (RecordNode)nextElement();
    }

    public int getNumberOfRecords()
    {
        if(records == null)
            return 0;
        else
            return records.size();
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2001");
    private int index;
    private Vector records;

}
