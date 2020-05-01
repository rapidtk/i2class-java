// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.tags.impl;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

// Referenced classes of package com.ibm.etools.iseries.webfacing.tags.impl:
//            WFFieldTagSupport, WFBodyTagSupport

public class WFProgDef extends WFFieldTagSupport
{

    public WFProgDef()
    {
    }

    public int doStartTag()
    {
        return 2;
    }

    public int doAfterBody()
    {
        return 0;
    }

    public int doEndTag()
    {
        byte byte0 = 6;
        try
        {
            if(isVisible())
            {
                logMsg("DBG", 6, "field " + getRecord() + "." + getField() + " IS visible");
                BodyContent bodycontent = getBodyContent();
                String s = getFieldValue();
                if(null != s)
                {
                    JspWriter jspwriter = bodycontent.getEnclosingWriter();
                    try
                    {
                        jspwriter.print("<span id=\"" + getFieldId() + "\">" + s + "</span>");
                    }
                    catch(IOException ioexception)
                    {
                        logMsg("ERR", 1, "exception in WFProgDef.doEndTag for " + getField() + " in " + getRecord() + " :\n" + ioexception);
                    }
                }
            } else
            {
                logMsg("DBG", 6, "field " + getField() + " is NOT visible");
            }
        }
        catch(Exception exception)
        {
            logMsg("ERR", 1, "Exception in WFProgDef.doEndTag :\n" + exception);
        }
        return byte0;
    }

    public static final String Copyright = "(c) Copyright IBM Corporation 2002-2003. All Rights Reserved.";
}
