// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.dev;

import java.awt.Component;
import java.io.*;
import org.apache.xerces.parsers.AbstractDOMParser;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.serialize.*;

// Referenced classes of package com.ibm.as400ad.webfacing.dev:
//            XMLDataDefinitionFilter, XMLViewDefinitionFilter, DirectoryPromptDialog, XMLDefinitionFilter, 
//            FilterCode

public class XMLBuildDefinitionCode
{

    public XMLBuildDefinitionCode()
    {
    }

    private static void generateCode(String s, String s1)
    {
        try
        {
            try
            {
                DOMParser domparser = new DOMParser();
                domparser.parse(s);
                org.w3c.dom.Document document = domparser.getDocument();
                XMLDataDefinitionFilter xmldatadefinitionfilter = new XMLDataDefinitionFilter(document);
                document = xmldatadefinitionfilter.fixPackage();
                document = xmldatadefinitionfilter.filter();
                XMLViewDefinitionFilter xmlviewdefinitionfilter = new XMLViewDefinitionFilter(document);
                document = xmlviewdefinitionfilter.filterKeys();
                FileOutputStream fileoutputstream = new FileOutputStream(s1, false);
                OutputFormat outputformat = new OutputFormat(document, "UTF-8", true);
                outputformat.setOmitComments(true);
                XMLSerializer xmlserializer = new XMLSerializer(fileoutputstream, outputformat);
                xmlserializer.asDOMSerializer();
                /*TODO: Encode?
                xmlserializer.setEncoding("UTF-8");
                */
                xmlserializer.serialize(document);
                System.out.println("file " + s1 + " serialized OK");
                fileoutputstream.flush();
                fileoutputstream.close();
            }
            catch(Throwable throwable)
            {
                System.out.println("error processing : " + s + "\n" + throwable);
            }
        }
        catch(Exception exception) { }
    }

    public static void processFiles(String s, String s1)
    {
        File file = new File(s);
        String as[] = file.list();
        for(int i = 0; i < as.length; i++)
        {
            System.out.println(as[i]);
            if(as[i].indexOf(".xml") >= 0)
            {
                String s2 = s + "\\" + as[i];
                String s3 = s1 + "\\" + as[i];
                System.out.println("calling generateCode(" + s2 + ", " + s1 + ");");
                generateCode(s2, s3);
            }
        }

    }

    public static void main(String args[])
    {
        String s = null;
        String s1 = null;
        if(args.length == 2)
        {
            s = args[0];
            s1 = args[1];
        }
        if(s == null || s1 == null)
        {
            DirectoryPromptDialog directorypromptdialog = new DirectoryPromptDialog(null, "Select Source and Destination directories");
            directorypromptdialog.setVisible(true);
            if(!directorypromptdialog.wasCancelled())
            {
                s = directorypromptdialog.getDirectory();
                s1 = directorypromptdialog.getDestDirectory();
            } else
            {
                System.exit(0);
            }
        }
        processFiles(s, s1);
    }

    private static PrintWriter _outWriter = null;
    private static FilterCode _filterCode = null;

}
