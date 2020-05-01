// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.code400.dom.FieldNode;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.DHTMLSourceCodeCollection;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            NamedFieldOutput

public class NonDisplayFieldOutput extends NamedFieldOutput
{

    public DHTMLSourceCodeCollection getDHTML()
    {
        DHTMLSourceCodeCollection dhtmlsourcecodecollection = new DHTMLSourceCodeCollection();
        return dhtmlsourcecodecollection;
    }

    public NonDisplayFieldOutput(FieldNode fieldnode)
    {
        super(fieldnode);
    }

    public int getColumn()
    {
        return 0;
    }

    public int getRow()
    {
        return 0;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2000");

}
