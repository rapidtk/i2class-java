// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.code400.dom.FieldNode;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            SelectFieldOutput, FieldOutput

public class SubfileSelectionFieldOutput extends SelectFieldOutput
{

    public SubfileSelectionFieldOutput(FieldNode fieldnode)
    {
        super(fieldnode);
        _nl = System.getProperty("line.separator");
    }

    public String getCurrentRowForMode(int i)
    {
        String s = "";
        if(i == 1)
            s = "<%=currentRow-" + getBeanName() + ".getSubfileAreaFirstRow()%>";
        else
        if(i == 0)
            s = "i_row";
        return s;
    }

    public String getTotalRowForMode(int i)
    {
        String s = "";
        if(i == 1 || i == 0)
            s = "<%=" + getBeanName() + ".getSubfileAreaHeight()%>";
        return s;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2000");
    private String _nl;

}
