// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.dev;

import java.awt.Component;
import java.io.*;

// Referenced classes of package com.ibm.as400ad.webfacing.dev:
//            ViewDefinitionFilter, DataDefinitionFilter, DefinitionFilter, DirectoryPromptDialog, 
//            FilterCode

public class BuildDefinitionCode
{

    public BuildDefinitionCode()
    {
    }

    private static void generateCode(String s, String s1)
    {
        try
        {
            String s2 = s.substring(s.lastIndexOf("\\") + 1);
            FileInputStream fileinputstream = new FileInputStream(s);
            LineNumberReader linenumberreader = new LineNumberReader(new InputStreamReader(fileinputstream));
            _outWriter = openOutFile(s1 + "\\" + s2);
            if(s2.indexOf("View.java") >= 0)
                _filterCode = new ViewDefinitionFilter(s2);
            else
            if(s2.indexOf("Data.java") >= 0)
                _filterCode = new DataDefinitionFilter();
            else
                _filterCode = new DefinitionFilter();
            for(String s3 = linenumberreader.readLine(); s3 != null; s3 = linenumberreader.readLine())
            {
                String s4 = _filterCode.filterLine(s3);
                if(s4 != null)
                {
                    _outWriter.write(s4);
                    _outWriter.println();
                }
            }

            _outWriter.flush();
            _outWriter.close();
        }
        catch(Exception exception) { }
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
        File file = new File(s);
        String args1[] = file.list();
        for(int i = 0; i < args1.length; i++)
            if(args1[i].indexOf(".java") >= 0)
                generateCode(s + "\\" + args1[i], s1);

    }

    public static PrintWriter openOutFile(String s)
    {
        PrintWriter printwriter = null;
        try
        {
            File file = new File(s);
            System.out.println("Opening output file " + s + ". Exists? " + file.exists());
            FileOutputStream fileoutputstream = new FileOutputStream(file);
            printwriter = new PrintWriter(fileoutputstream);
        }
        catch(Exception exception)
        {
            System.out.println("Error: output file " + s + " did not open: " + exception.getClass().getName() + ": " + exception.getMessage());
        }
        return printwriter;
    }

    private static PrintWriter _outWriter = null;
    private static FilterCode _filterCode = null;

}
