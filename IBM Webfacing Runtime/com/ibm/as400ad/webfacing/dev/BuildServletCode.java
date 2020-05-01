// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.dev;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import java.awt.Component;
import java.io.*;

// Referenced classes of package com.ibm.as400ad.webfacing.dev:
//            DirectoryPromptDialog

public class BuildServletCode
{

    public BuildServletCode()
    {
    }

    private static void generateCode(String s, String s1)
    {
        try
        {
            _inFile = s;
            retrieveFileInformation();
            _outWriter = openOutFile(s1 + "\\" + _servletName + ".java");
            generateImportStatementsAndClassHeader();
            _outWriter.println("{");
            _outWriter.println("    static String[] _jspx_html_data = null;");
            _outWriter.println();
            _outWriter.println();
            generateConstructor();
            _outWriter.println();
            _outWriter.println();
            generateInitMethod();
            _outWriter.println();
            generateServiceMethod();
            _outWriter.println();
            _outWriter.println("}");
            _outWriter.flush();
            _outWriter.close();
        }
        catch(Exception exception) { }
    }

    private static void generateConstructor()
    {
        _outWriter.println("public " + _servletName + "() {");
        _outWriter.println("}");
    }

    private static void generateImportStatementsAndClassHeader()
    {
        _outWriter.println("package com.ibm.as400ad.webfacing.runtime.qsys.servlets." + _packageFile + ";");
        _outWriter.println();
        _outWriter.println("import javax.servlet.*;");
        _outWriter.println("import javax.servlet.http.*;");
        _outWriter.println("import java.io.PrintWriter;");
        _outWriter.println("import java.io.IOException;");
        _outWriter.println("import java.util.Vector;");
        _outWriter.println("import java.beans.*;");
        _outWriter.println("import com.ibm.as400ad.webfacing.runtime.view.DisplayAttributeBean;");
        _outWriter.println("import com.ibm.as400ad.webfacing.runtime.dhtmlview.HTMLStringTransform;");
        _outWriter.println("import com.ibm.as400ad.webfacing.runtime.controller.ErrorHandler;");
        _outWriter.println("import com.ibm.as400ad.webfacing.runtime.controller.WFSession;");
        _outWriter.println("import com.ibm.as400ad.webfacing.util.TraceLogger;");
        _outWriter.println();
        _outWriter.println();
        _outWriter.println("public class " + _servletName + " extends HttpServlet");
    }

    private static void generateInitMethod()
    {
        ObjectInputStream objectinputstream = null;
        StringBuffer astringbuffer[] = null;
        try
        {
            FileInputStream fileinputstream = new FileInputStream(_datFile);
            objectinputstream = new ObjectInputStream(fileinputstream);
            astringbuffer = (StringBuffer[])objectinputstream.readObject();
        }
        catch(Exception exception) { }
        finally
        {
            if(objectinputstream != null)
                try
                {
                    objectinputstream.close();
                }
                catch(IOException ioexception) { }
        }
        _outWriter.println("public final void init()");
        _outWriter.println("{");
        int i = 0;
        _outWriter.println("_jspx_html_data = new String[" + astringbuffer.length + "];");
        for(; i < astringbuffer.length; i++)
        {
            String s = astringbuffer[i].toString();
            s = WebfacingConstants.replaceSubstring(s, "\"", "\\\"");
            s = WebfacingConstants.replaceSubstring(s, System.getProperty("line.separator"), "\"+System.getProperty(\"line.separator\")+\"");
            _outWriter.println("_jspx_html_data[" + i + "]=new String(\"" + s + "\");");
        }

        _outWriter.println("}");
    }

    private static void generateServiceMethod()
    {
        _outWriter.println("public void service(HttpServletRequest request, HttpServletResponse  response)");
        _outWriter.println("    throws IOException, ServletException {");
        _outWriter.println();
        _outWriter.println("HttpSession session = request.getSession();");
        _outWriter.println("PrintWriter out = response.getWriter();");
        _outWriter.println(" try {");
        _outWriter.println("        try");
        _outWriter.println("        {");
        _outWriter.println("             response.setContentType(\"text/html\");");
        _outWriter.println("        }");
        _outWriter.println("        catch (IllegalStateException ws_jsp_ise)");
        _outWriter.println("        {");
        _outWriter.println("        }");
        Object obj = null;
        try
        {
            FileInputStream fileinputstream = new FileInputStream(_inFile);
            LineNumberReader linenumberreader = new LineNumberReader(new InputStreamReader(fileinputstream));
            int i = 0;
            linenumberreader.setLineNumber(1);
            String s = linenumberreader.readLine();
            boolean flag = false;
            boolean flag1 = false;
            for(boolean flag2 = false; s != null && !flag2; s = linenumberreader.readLine())
            {
                if(flag)
                {
                    if(s.indexOf("out = pageContext.getOut();") >= 0)
                        flag1 = true;
                    else
                    if(s.indexOf("pageContext.getAttribute(") >= 0)
                    {
                        flag1 = false;
                        int j = s.indexOf("\"");
                        int k = s.lastIndexOf("\"");
                        String s1 = s.substring(j + 1, k);
                        _outWriter.println("\t\t\tsession.getValue(\"" + s1 + "\");");
                        _outWriter.println("\t\t\tif (" + s1 + "  == null) ");
                        _outWriter.println("\t\t\t\tthrow new java.lang.InstantiationException (\"bean " + s1 + "  not found within scope \"); ");
                        _outWriter.println("\t    \t}");
                    } else
                    if(s.indexOf("if(_special") >= 0)
                    {
                        flag1 = true;
                        _outWriter.println(s);
                    } else
                    if(s.indexOf("if ((!_jspx_cleared_due_to_forward) && (out.getBufferSize() != 0))") >= 0)
                    {
                        _outWriter.println("            ErrorHandler errHandler = new ErrorHandler(getServletConfig().getServletContext(), request, response, WFSession.getTraceLogger(), true);");
                        _outWriter.println("            errHandler.handleError(new Exception(t.toString()), \"Error in system servlet.\");");
                        _outWriter.println("\t} finally {");
                        _outWriter.println("\t\tout.flush();");
                        _outWriter.println("\t}");
                        flag2 = true;
                    } else
                    if(flag1)
                        _outWriter.println(s);
                } else
                if(s.indexOf("public void _jspService(HttpServletRequest request, HttpServletResponse  response)") >= 0)
                    flag = true;
                i++;
                linenumberreader.setLineNumber(i);
            }

            linenumberreader.close();
            _outWriter.println("}");
        }
        catch(Exception exception)
        {
            System.exit(1);
        }
    }

    public static void main(String args[])
    {
        String s = "D:\\jspservlets";
        String s1 = "D:\\newservlets";
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
        File file = new File(s);
        String args1[] = file.list();
        for(int i = 0; i < args1.length; i++)
            if(args1[i].indexOf("xjsp.java") >= 0)
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

    private static void retrieveFileInformation()
    {
        Object obj = null;
        _baseName = _inFile.substring(_inFile.lastIndexOf("\\_") + 2, _inFile.lastIndexOf("_xjsp"));
        _servletName = _baseName + "_Servlet";
        try
        {
            FileInputStream fileinputstream = new FileInputStream(_inFile);
            LineNumberReader linenumberreader = new LineNumberReader(new InputStreamReader(fileinputstream));
            int i = 0;
            linenumberreader.setLineNumber(1);
            String s = linenumberreader.readLine();
            boolean flag = false;
            boolean flag1 = false;
            boolean flag2 = false;
            for(; s != null && !flag; s = linenumberreader.readLine())
            {
                if(!flag1 && s.indexOf("package ") >= 0)
                {
                    _packageFile = s.substring(s.lastIndexOf(".") + 1, s.lastIndexOf(";")).toLowerCase();
                    flag1 = true;
                }
                if(flag2)
                {
                    if(s.indexOf("FileInputStream fin = new FileInputStream(") >= 0)
                    {
                        int j = s.indexOf("\"");
                        int k = s.lastIndexOf("\"");
                        _datFile = s.substring(j + 1, k);
                        _datFile = WebfacingConstants.replaceSubstring(_datFile, "\\\\", "\\");
                        flag = true;
                    }
                } else
                if(s.indexOf("public final void _jspx_init() ") >= 0)
                    flag2 = true;
                i++;
            }

            linenumberreader.close();
        }
        catch(Exception exception) { }
    }

    private static String _baseName = null;
    private static String _packageFile = null;
    private static String _servletName = null;
    private static String _datFile = null;
    private static String _inFile = null;
    private static PrintWriter _outWriter = null;

}
