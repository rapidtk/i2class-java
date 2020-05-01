// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import java.io.Serializable;

public class SystemRecord
    implements Serializable
{

    public SystemRecord(String s, String s1)
    {
        _record = "";
        _fileName = "";
        _record = s;
        _fileName = s1;
    }

    public String getFileName()
    {
        return _fileName;
    }

    public String getPackage()
    {
        return ("com/ibm/as400ad/webfacing/runtime/qsys/" + _fileName).toLowerCase();
    }

    public String getRecord()
    {
        return _record;
    }

    public String getServlet()
    {
        return _record;
    }

    public String getServletJavascript()
    {
        return _record + "JavaScript";
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    private String _record;
    private String _fileName;
    private static final String BASE_PACKAGE = "com/ibm/as400ad/webfacing/runtime/qsys";
}
