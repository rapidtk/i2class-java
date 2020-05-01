// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.diags;

import com.ibm.as400ad.webfacing.runtime.controller.*;
import com.ibm.as400ad.webfacing.runtime.model.def.IRecordDataDefinition;
import java.io.*;
import java.util.Date;
import java.util.Vector;
import javax.servlet.*;
import javax.servlet.http.*;

// Referenced classes of package com.ibm.etools.iseries.webfacing.diags:
//            RecordDefinitionComparer

public class BeanCompare extends HttpServlet
{

    public BeanCompare()
    {
        br = null;
        rdf = new RecordDefinitionFetcher();
        rbf = null;
        beanCount = 0;
    }

    public void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException
    {
        doPost(httpservletrequest, httpservletresponse);
    }

    public void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException
    {
        ServletConfig servletconfig = getServletConfig();
        ServletContext servletcontext = servletconfig.getServletContext();
        rbf = RecordBeanFactory.getRecordBeanFactory(servletcontext);
        PrintWriter printwriter = null;
        try
        {
            httpservletresponse.setContentType("text/html;charset=UTF-8");
            printwriter = httpservletresponse.getWriter();
        }
        catch(Throwable throwable) { }
        printwriter.print("<html><head>");
        printwriter.print(getProjectStyleSheets(httpservletrequest));
        printwriter.print("<title>" + TITLESTRING + "</title>");
        printwriter.print("</head><body class=\"theapp wf_field wf_default\">");
        printwriter.print("<h2>" + TITLESTRING + "</h2>");
        printwriter.print("<table class=\"wf_field wf_default\"><tr><td>");
        printwriter.print("<FORM method=\"post\" action=\"" + httpservletrequest.getRequestURI() + "\">");
        printwriter.print("<INPUT class=\"wf_field wf_default\" type=\"submit\" name=\"" + ACTIONNAME + "\" value=\"CompareBeans\">");
        printwriter.print("</form>");
        printwriter.print("</td>");
        printwriter.print("</tr>");
        printwriter.print("</table>");
        String s = servletcontext.getRealPath("WEB-INF/classes");
        beanCount = 0;
        String as[] = httpservletrequest.getParameterValues(ACTIONNAME);
        if(null != as && null != as[0] && as[0].startsWith("Comp"))
            loadAndCompare(printwriter, s);
        printwriter.println("</body></html>");
    }

    public void init(ServletConfig servletconfig)
        throws ServletException
    {
        super.init(servletconfig);
        try
        {
            br = new BeanDefXMLReader();
        }
        catch(Throwable throwable)
        {
            br = null;
        }
        ServletContext servletcontext = getServletContext();
        rbf = RecordBeanFactory.getRecordBeanFactory(servletcontext);
        ServletConfig servletconfig1 = getServletConfig();
    }

    private void loadAndCompare(PrintWriter printwriter, String s)
    {
        Vector vector = new Vector(100, 100);
        beanCount = 0;
        vector = getAllDefnsInThisApp(s, vector);
        PrintWriter printwriter1 = null;
        try
        {
            FileOutputStream fileoutputstream = new FileOutputStream("c:\beancompare.out", true);
            printwriter1 = new PrintWriter(fileoutputstream);
        }
        catch(Throwable throwable) { }
        printwriter.print("<table class=\"wf_field wf_default\" border=\"0\" cellspacing=\"0\"><tr><th>Bean Name</th><th>DOM ---- </th><th>SAX ---- </th><th colspan=\"3\">Compare</th>");
        if(vector.size() > 0)
        {
            int i = 0;
            for(int j = 0; j < vector.size(); j++)
            {
                String s1 = (String)vector.elementAt(j);
                if(!s1.endsWith("JavaScript.jsp"))
                {
                    String s2 = loadBeansForJSP(vector.elementAt(j).toString(), s, i++, printwriter, printwriter1);
                    if(null != s2)
                        printwriter.print(s2);
                }
            }

        } else
        {
            printwriter.print("<tr><td>v is empty</td></tr>");
        }
        printwriter.println("</table>");
    }

    private Vector getAllDefnsInThisApp(String s, Vector vector)
    {
        String s1 = s;
        if(!s.endsWith(File.separator))
            s1 = s1 + File.separator;
        File file = new File(s1);
        File afile[] = file.listFiles();
        if(null != afile && afile.length > 0)
        {
            for(int i = 0; i < afile.length; i++)
                if(null != afile[i])
                {
                    String s2 = afile[i].getName();
                    if(null != s2)
                        if(afile[i].isDirectory())
                        {
                            if(!s2.startsWith("."))
                            {
                                String s3 = afile[i].getAbsolutePath() + File.separator;
                                getAllDefnsInThisApp(s3, vector);
                            }
                        } else
                        if(s2.endsWith(".xml"))
                        {
                            String s4 = afile[i].getAbsolutePath();
                            vector.add(s4);
                        }
                }

        }
        return vector;
    }

    private String loadBeansForJSP(String s, String s1, int i, PrintWriter printwriter, PrintWriter printwriter1)
    {
        int j = i % 2;
        String s2 = null;
        switch(j)
        {
        case 0: // '\0'
            s2 = EVENROWSTYLE;
            break;

        case 1: // '\001'
        default:
            s2 = ODDROWSTYLE;
            break;
        }
        String s3 = null;
        boolean flag = false;
        Object obj = null;
        Object obj1 = null;
        Object obj2 = null;
        String s4 = s.trim();
        if(s4.endsWith(".xml"))
            s4 = s4.substring(0, s4.length() - 4);
        if(s1.length() > 2 && s4.startsWith(s1))
            s4 = s4.substring(s1.length());
        if(!s4.endsWith("JavaScript"))
        {
            s4 = s4.replace('\\', '.');
            s4 = s4.replace(File.separatorChar, '.');
            if(s4.startsWith("."))
                s4 = s4.substring(1);
            printwriter.print("<tr class=\"" + s2 + "\"><td>");
            printwriter.print(s4 + "</td><td>");
            IRecordDataDefinition irecorddatadefinition;
            try
            {
                irecorddatadefinition = loadAndCompare(rbf, s4, printwriter, printwriter1);
            }
            catch(Throwable throwable)
            {
                System.out.println("error loading >" + s4 + "< was " + throwable);
            }
            printwriter.print("</tr>");
        } else
        {
            s3 = null;
        }
        return s3;
    }

    public static void main(String args[])
    {
        BeanCompare beancompare = new BeanCompare();
        beancompare.rbf = RecordBeanFactory.getRecordBeanFactory((ServletContext)null);
        Vector vector = new Vector(100, 100);
        String s = "D:\\workspaces\\runtime-workspace\\m2\\WebContent\\WEB-INF\\classes\\";
        vector = beancompare.getAllDefnsInThisApp(s, vector);
        PrintWriter printwriter = null;
        try
        {
            File file = new File("c:\\beancompare.out");
            file.delete();
            file.createNewFile();
            FileOutputStream fileoutputstream = new FileOutputStream(file.getAbsolutePath());
            printwriter = new PrintWriter(fileoutputstream);
        }
        catch(Throwable throwable)
        {
            System.err.println("hmmm, caught an error here, " + throwable);
        }
        if(vector.size() > 0)
        {
            for(int i = 0; i < vector.size(); i++)
                beancompare.loadBeansForJSP(vector.elementAt(i).toString(), s, i, new PrintWriter(System.out), printwriter);

        } else
        {
            System.out.println("v is empty");
        }
        System.out.println("done");
    }

    private static String getProjectStyleSheets(HttpServletRequest httpservletrequest)
    {
        String s = "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + httpservletrequest.getContextPath() + "/styles/apparea/apparea.css\">";
        String s1 = httpservletrequest.getSession().getServletContext().getRealPath("styles/chrome/");
        File file = new File(s1);
        String as[] = file.list();
        if(null != as)
        {
            for(int i = 0; i < as.length; i++)
                if(as[i].endsWith(".css"))
                    s = s + "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + httpservletrequest.getContextPath() + "/styles/chrome/" + as[i] + "\">";

        }
        return s;
    }

    private IRecordDataDefinition loadAndCompare(RecordBeanFactory recordbeanfactory, String s, PrintWriter printwriter, PrintWriter printwriter1)
    {
        IRecordDataDefinition irecorddatadefinition = null;
        try
        {
            printwriter1.println("Bean = " + s);
            long l = (new Date()).getTime();
            IRecordDataDefinition irecorddatadefinition1 = null;
            try
            {
                irecorddatadefinition1 = rdf.DOMLoadXMLDefinition(s);
            }
            catch(Throwable throwable1)
            {
                System.err.println("Exception caught in BeanCompare.loadAndCompare when trying to load " + s + " from RecordBeanFactory :" + throwable1);
            }
            long l1 = (new Date()).getTime();
            if(null != irecorddatadefinition1)
            {
                String s1 = Long.toString(l1 - l);
                printwriter.print(s1 + "</td><td>");
                printwriter1.println("DOM= " + s1);
            } else
            {
                printwriter.print("<span class=\"wf_field wf_default wf_ri_red\">FAILED</span></td><td>");
                printwriter1.println("DOM=FAILED ");
            }
            long l2 = (new Date()).getTime();
            IRecordDataDefinition irecorddatadefinition2 = rdf.SAXLoadXMLDefinition(s);
            long l3 = (new Date()).getTime();
            if(null != irecorddatadefinition2)
            {
                String s2 = Long.toString(l3 - l2);
                printwriter.print(s2 + "</td><td>");
                printwriter1.println("SAX= " + s2);
            } else
            {
                printwriter.print("<span class=\"wf_field wf_default wf_ri_red\">FAILED</span></td><td>");
                printwriter1.println("SAX=FAILED ");
            }
            boolean flag = false;
            try
            {
                flag = RecordDefinitionComparer.identicalRecordDataDefinitions(irecorddatadefinition1, irecorddatadefinition2, printwriter1);
            }
            catch(Throwable throwable2) { }
            if(flag)
            {
                printwriter.print("ok</td><td>");
            } else
            {
                printwriter.print("<span class=\"wf_field wf_default wf_ri_red\">FAILED</span></td><td>");
                printwriter1.println("Data Compare=FAILED");
                System.err.print("d");
            }
            try
            {
                flag = RecordDefinitionComparer.identicalRecordViewDefinitions(irecorddatadefinition1.getViewDefinition(), irecorddatadefinition2.getViewDefinition(), printwriter1);
            }
            catch(Throwable throwable3) { }
            if(flag)
            {
                printwriter.print("ok</td><td>");
            } else
            {
                printwriter.print("<span class=\"wf_field wf_default wf_ri_red\">FAILED</span></td><td>");
                printwriter1.println("Compare=FAILED");
                System.err.print("v");
            }
            try
            {
                flag = RecordDefinitionComparer.identicalRecordFeedbackDefinitions(irecorddatadefinition1.getFeedbackDefinition(), irecorddatadefinition2.getFeedbackDefinition(), printwriter1);
            }
            catch(Throwable throwable4) { }
            if(flag)
            {
                printwriter.print("ok</td></tr>");
            } else
            {
                printwriter.print("<span class=\"wf_field wf_default wf_ri_red\">FAILED</span></td></tr>");
                printwriter1.println("Compare=FAILED");
                System.err.print("f");
            }
            printwriter1.flush();
        }
        catch(Throwable throwable)
        {
            System.err.println("Exception caught in BeanCompare.loadAndCompare :" + throwable);
        }
        printwriter1.println("--------------------------------------------------------");
        return irecorddatadefinition;
    }

    private IRecordDataDefinition loadWithSAX(String s, PrintWriter printwriter)
    {
        IRecordDataDefinition irecorddatadefinition = null;
        try
        {
            irecorddatadefinition = br.getBeanDef(s);
        }
        catch(Throwable throwable) { }
        return irecorddatadefinition;
    }

    public static final String copyRight = "(C) Copyright IBM Corporation 2003 all rights reserved";
    private static String ACTIONNAME = "Action";
    private static String TITLESTRING = "Mike's WebFacing Definition Bean Compare Tool";
    private static String EVENROWSTYLE = "subfileRecord1";
    private static String ODDROWSTYLE = "subfileRecord2";
    private BeanDefXMLReader br;
    private RecordDefinitionFetcher rdf;
    private RecordBeanFactory rbf;
    private int beanCount;

}
