// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen;

import com.ibm.as400ad.code400.designer.io.OutputFile;
import com.ibm.as400ad.code400.designer.io.SourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.Util;
import java.io.IOException;

public class WebResourceFileWriter
{

    public WebResourceFileWriter(String s)
    {
        _filePath = null;
        _filePath = s;
    }

    public void writeSourceToFile(SourceCodeCollection sourcecodecollection, String s)
        throws IOException
    {
        writeSourceToFile(sourcecodecollection, s, "");
    }

    public void writeSourceToFile(SourceCodeCollection sourcecodecollection, String s, String s1)
        throws IOException
    {
        String s2 = _filePath + s + s1;
        Util.showDebugMessage("output file = " + s2);
        OutputFile outputfile;
        if(s1.toLowerCase().equals(".jsp") || s1.toLowerCase().equals(".htm") || s1.toLowerCase().equals(".html") || s1.equals(".xml"))
            outputfile = new OutputFile(s2, "UTF8");
        else
            outputfile = new OutputFile(s2);
        outputfile.printOutputCollection(sourcecodecollection);
        outputfile.close();
    }

    private String _filePath;
}
