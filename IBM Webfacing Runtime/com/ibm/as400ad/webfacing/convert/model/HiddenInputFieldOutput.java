// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.dom.FieldNode;
import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.DHTMLSourceCodeCollection;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            InputFieldOutput, FieldLines, FieldOutput, NamedFieldOutput

public class HiddenInputFieldOutput extends InputFieldOutput
{

    public HiddenInputFieldOutput(FieldNode fieldnode)
    {
        super(fieldnode);
    }

    FieldLines getGeneratedDHTML()
    {
        DHTMLSourceCodeCollection dhtmlsourcecodecollection = new DHTMLSourceCodeCollection();
        FieldNode fieldnode = getFieldNode();
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(50);
        String s = getTagId();
        String s1 = getFieldTextWithTransform(1);
        paddedstringbuffer.concat("<INPUT TYPE=\"hidden\" ID=\"", s, "\" previousValue=\"", s1, "\" ", "VALUE=\"", s1, "\" size=", Integer.toString(getWidth()), " maxLength=", Integer.toString(getWidth()), "  >");
        dhtmlsourcecodecollection.addElement(paddedstringbuffer);
        FieldLines fieldlines = new FieldLines();
        fieldlines.add(dhtmlsourcecodecollection);
        return fieldlines;
    }

    protected void initializeWidthAndHeight()
    {
    }

    protected String jsEventHandlers()
    {
        return null;
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2000");

}
