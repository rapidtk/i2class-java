// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.tags.impl;

import java.io.Writer;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

// Referenced classes of package com.ibm.etools.iseries.webfacing.tags.impl:
//            WFFieldTagSupport

public class WFMTFld extends WFFieldTagSupport
{

    public WFMTFld()
    {
    }

    public int doStartTag()
    {
        return 2;
    }

    public int doAfterBody()
    {
        try
        {
            BodyContent bodycontent = getBodyContent();
            String s = bodycontent.getString();
            javax.servlet.jsp.JspWriter jspwriter = bodycontent.getEnclosingWriter();
            jspwriter.write(s);
        }
        catch(Exception exception) { }
        return 0;
    }

    public int doEndTag()
    {
        return 6;
    }

    public static final String Copyright = "(c) Copyright IBM Corporation 2002-2003. All Rights Reserved.";
}
