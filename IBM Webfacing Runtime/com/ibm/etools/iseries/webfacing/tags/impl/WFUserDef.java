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

public class WFUserDef extends WFFieldTagSupport
{

    public WFUserDef()
    {
    }

    public int doAfterBody()
    {
        int i = 0;
        BodyContent bodycontent = getBodyContent();
        if(isVisible())
        {
            String s = bodycontent.getString();
            if(null != s)
            {
                JspWriter jspwriter = bodycontent.getEnclosingWriter();
                try
                {
                    jspwriter.print("<span id=\"" + getFieldId() + "\">" + s + "</span>");
                }
                catch(IOException ioexception)
                {
                    logMsg("ERR", 1, "exception in WFUserDef.doAfterBody for " + getField() + " in " + getRecord() + " :\n" + ioexception);
                }
            }
        } else
        {
            logMsg("DBG", 3, "field " + getField() + " is not visible");
        }
        return i;
    }

    public static final String Copyright = "(c) Copyright IBM Corporation 2002-2003. All Rights Reserved.";
}
