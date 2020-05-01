// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.designer.io.SourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.gen.WebResourceFileWriter;
import java.io.IOException;

public class XMLSourceCodeCollection extends SourceCodeCollection
{

    public XMLSourceCodeCollection(String s, String s1)
    {
        _fileName = null;
        _packageName = null;
        _fileName = s;
        _packageName = s1;
        addElement("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    }

    public void write(WebResourceFileWriter webresourcefilewriter)
        throws IOException
    {
        webresourcefilewriter.writeSourceToFile(this, _fileName, ".xml");
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2000, 2002");
    private String _fileName;
    private String _packageName;

}
