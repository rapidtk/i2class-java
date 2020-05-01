// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.tags.impl;

import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.etools.iseries.webfacing.tags.def.ILogger;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

public class WFBodyTagSupport extends BodyTagSupport
{

    public WFBodyTagSupport()
    {
        ilg = null;
        name = MT;
        record = MT;
        context = null;
        parent = null;
    }

    public void release()
    {
        name = MT;
        record = MT;
        context = null;
        parent = null;
        ilg = null;
        super.release();
    }

    public void logMsg(String s, int i, String s1)
    {
        if(i > 0)
        {
            getLogger();
            if(s.equals("ERR"))
                ilg.err(i, s1);
            else
            if(s.equals("DBG"))
                ilg.dbg(i, s1);
            else
            if(s.equals("EVT"))
                ilg.evt(i, s1);
        }
    }

    public void setParent(Tag tag)
    {
        parent = tag;
    }

    public void setPageContext(PageContext pagecontext)
    {
        context = pagecontext;
    }

    public ILogger getLogger()
    {
        if(null == ilg)
            ilg = WFSession.getTraceLogger();
        return ilg;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2002-2003.  All Rights Reserved.";
    protected static String MT = "";
    private ILogger ilg;
    protected String name;
    protected String record;
    protected PageContext context;
    protected Tag parent;

}
