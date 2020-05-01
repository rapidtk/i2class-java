// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.dev;

import java.awt.Component;
import java.io.*;

// Referenced classes of package com.ibm.as400ad.webfacing.dev:
//            ServletFilter, DirectoryPromptDialog, FilterCode

public class BuildJasperServletCode
{

    public BuildJasperServletCode()
    {
    }

    private static void generateCode(String s, String s1)
    {
        try
        {
            String s2 = s.substring(s.lastIndexOf("\\") + 1);
            String s3 = s2.substring(0, s2.indexOf(".java")) + "_Servlet";
            FileInputStream fileinputstream = new FileInputStream(s);
            LineNumberReader linenumberreader = new LineNumberReader(new InputStreamReader(fileinputstream));
            _outWriter = openOutFile(s1 + "\\" + s3 + ".java");
            _filterCode = new ServletFilter(s3);
            for(String s4 = linenumberreader.readLine(); s4 != null; s4 = linenumberreader.readLine())
            {
                String s5 = _filterCode.filterLine(s4);
                if(s5 != null)
                {
                    _outWriter.write(s5);
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
