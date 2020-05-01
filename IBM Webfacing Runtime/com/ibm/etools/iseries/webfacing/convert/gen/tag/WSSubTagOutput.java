// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.convert.gen.tag;

import com.ibm.as400ad.webfacing.convert.ExportHandler;
import com.ibm.etools.iseries.webfacing.convert.external.IWSSubTagOutput;

public class WSSubTagOutput
    implements IWSSubTagOutput
{

    public WSSubTagOutput()
    {
        _tags = null;
        _htmlHeader = null;
    }

    public void setSubTag(String s)
    {
        _tags = s;
    }

    public void addHtmlHeader(String s, String s1)
    {
        _htmlHeader = "<%@ taglib uri=\"/" + s1 + "\" prefix=\"" + s + "\" %>";
    }

    public String getHtmlHeader()
    {
        return _htmlHeader;
    }

    public String getTagString()
    {
        return _tags;
    }

    public void logMessage(String s, int i, String s1)
    {
        if(i > 0)
            if(s.equals("ERR"))
                ExportHandler.err(i, s1);
            else
            if(s.equals("DBG"))
                ExportHandler.dbg(i, s1);
            else
            if(s.equals("EVT"))
                ExportHandler.evt(i, s1);
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 2002-2003 All rights reserved.");
    private String _tags;
    private String _htmlHeader;

}
