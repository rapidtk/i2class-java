// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.diags;

import com.ibm.as400ad.webfacing.runtime.controller.ICacheable;
import com.ibm.as400ad.webfacing.runtime.controller.RecordBeanFactory;
import java.io.*;
import java.util.Vector;
import javax.servlet.*;
import javax.servlet.http.*;

// Referenced classes of package com.ibm.etools.iseries.webfacing.diags:
//            CacheDumper

public class BeanLoader extends HttpServlet
{

    public BeanLoader()
    {
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
        rbf = RecordBeanFactory.getRecordBeanFactory(httpservletrequest.getSession().getServletContext());
        ServletConfig servletconfig = getServletConfig();
        String s = servletconfig.getInitParameter("DisableUI");
        PrintWriter printwriter = null;
        try
        {
            httpservletresponse.setContentType("text/html;charset=UTF-8");
            printwriter = httpservletresponse.getWriter();
        }
        catch(Throwable throwable) { }
        if(null == s || null != s && !s.equalsIgnoreCase("true"))
        {
            printwriter.print("<html><head>");
            printwriter.print(getProjectStyleSheets(httpservletrequest));
            printwriter.print("<title>" + TITLESTRING + "</title>");
            printwriter.print("</head><body class=\"theapp wf_field wf_default\">");
            printwriter.print("<h2>" + TITLESTRING + "</h2>");
            printwriter.print("<table class=\"wf_field wf_default\"><tr><td>");
            printwriter.print("<FORM method=\"post\" action=\"" + httpservletrequest.getRequestURI() + "\">");
            printwriter.print("<INPUT class=\"wf_field wf_default\" type=\"submit\" name=\"" + ACTIONNAME + "\" value=\"InferFromJSPNames\">");
            printwriter.print("</form>");
            printwriter.print("</td>");
            printwriter.print("</tr>");
            printwriter.print("<tr>");
            printwriter.print("<td>");
            printwriter.print("<FORM class=\"wf_field wf_default\" method=\"post\" action=\"" + httpservletrequest.getRequestURI() + "\">");
            printwriter.print("<table class=\"wf_field wf_default\" border=\"0\">");
            printwriter.print("<tr><td>");
            printwriter.print("<INPUT class=\"wf_field wf_default\" type=\"submit\" name=\"" + ACTIONNAME + "\" value=\"LoadFromFile\">");
            printwriter.print("</td><td>");
            printwriter.print("<INPUT class=\"wf_field wf_default\" type=\"text\" name=\"FileName\" size=\"20\" maxlength=\"100\" value=\"" + CacheDumper.SAVEFILENAMEDEFAULT + "\">");
            printwriter.print("</td></tr></table>");
            printwriter.print("</form>");
            printwriter.print("</td>");
            printwriter.print("</tr>");
            printwriter.print("</table>");
            String s1 = httpservletrequest.getSession().getServletContext().getRealPath("RecordJSPs/");
            beanCount = 0;
            String as[] = httpservletrequest.getParameterValues(ACTIONNAME);
            if(null != as && null != as[0])
                if(as[0].startsWith("Infer"))
                    loadFromJspNames(printwriter, s1);
                else
                if(as[0].startsWith("Load"))
                {
                    String as1[] = httpservletrequest.getParameterValues("FileName");
                    if(null != as1 && null != as1[0])
                        try
                        {
                            loadFromFile(printwriter, s1, as1[0]);
                        }
                        catch(Throwable throwable1) { }
                }
            printwriter.println("</body></html>");
        } else
        {
            printwriter.println(httpservletrequest.getRemoteAddr());
        }
    }

    public void init(ServletConfig servletconfig)
        throws ServletException
    {
        super.init(servletconfig);
        ServletContext servletcontext = getServletContext();
        rbf = RecordBeanFactory.getRecordBeanFactory(servletcontext);
        ServletConfig servletconfig1 = getServletConfig();
        String s = servletconfig1.getInitParameter("FileName");
        if(null != s)
        {
            String s1 = servletcontext.getRealPath("RecordJSPs/");
            loadFromFile(null, s1, s);
        }
    }

    private void loadFromFile(PrintWriter printwriter, String s, String s1)
    {
        String s2 = s + s1;
        try
        {
            File file = new File(s2);
            FileReader filereader = new FileReader(s2);
            LineNumberReader linenumberreader = new LineNumberReader(filereader);
            int i = 0;
            String s3 = "";
            for(String s4 = linenumberreader.readLine(); s4 != null; s4 = linenumberreader.readLine())
                s3 = s3 + loadBeansForJSP(s4, "", i++);

            if(null != printwriter)
            {
                printwriter.print("<table class=\"wf_field wf_default\" border=\"0\" cellspacing=\"0\"><tr><th>Record</th><th>Loaded Count</th><th>JVM Free Mem</th>");
                printwriter.print(s3);
                printwriter.println("</table>");
            } else
            {
                System.out.println("BeanLoader complete for " + s + s1);
            }
        }
        catch(Throwable throwable)
        {
            if(null == printwriter)
                System.out.println("BeanLoader init failed for " + s + s1 + "\n" + throwable);
        }
    }

    private void loadFromJspNames(PrintWriter printwriter, String s)
    {
        Vector vector = new Vector(100, 100);
        beanCount = 0;
        vector = getAllJSPsInThisApp(s, vector);
        printwriter.print("<table class=\"wf_field wf_default\" border=\"0\" cellspacing=\"0\"><tr><th>Record</th><th>Loaded Count</th><th>JVM Free Mem</th>");
        if(vector.size() > 0)
        {
            int i = 0;
            for(int j = 0; j < vector.size(); j++)
            {
                String s1 = (String)vector.elementAt(j);
                if(!s1.endsWith("JavaScript.jsp"))
                {
                    String s2 = loadBeansForJSP(vector.elementAt(j).toString(), s, i++);
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

    private Vector getAllJSPsInThisApp(String s, Vector vector)
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
                                getAllJSPsInThisApp(s3, vector);
                            }
                        } else
                        if(s2.endsWith(".jsp"))
                        {
                            String s4 = afile[i].getAbsolutePath();
                            vector.add(s4);
                        }
                }

        }
        return vector;
    }

    private String loadBeansForJSP(String s, String s1, int i)
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
        String s3 = "<tr class=\"" + s2 + "\"><td>";
        int k = 0;
        Object obj = null;
        Object obj1 = null;
        Object obj2 = null;
        String s4 = s.trim();
        if(s4.endsWith(".jsp"))
            s4 = s4.substring(0, s4.length() - 4);
        if(s1.length() > 2 && s4.startsWith(s1))
            s4 = s4.substring(s1.length());
        if(!s4.endsWith("JavaScript"))
        {
            s3 = s3 + s4 + "</td><td>";
            s4 = s4.replace('\\', '.');
            s4 = s4.replace(File.separatorChar, '.');
            try
            {
                com.ibm.as400ad.webfacing.runtime.model.def.IRecordDataDefinition irecorddatadefinition = rbf.loadRecordDataDefinition(s4);
                if(null != irecorddatadefinition)
                    k++;
                com.ibm.as400ad.webfacing.runtime.view.def.IRecordViewDefinition irecordviewdefinition = irecorddatadefinition.getViewDefinition();
                if(null != irecordviewdefinition)
                    k++;
                com.ibm.as400ad.webfacing.runtime.view.def.IRecordFeedbackDefinition irecordfeedbackdefinition = irecorddatadefinition.getFeedbackDefinition();
                if(null != irecordfeedbackdefinition)
                    k++;
            }
            catch(Throwable throwable)
            {
                System.out.println("error loading >" + s4 + "< was " + throwable);
            }
            s3 = s3 + k + "</td><td>" + Runtime.getRuntime().freeMemory() + "</td></tr>";
        } else
        {
            s3 = null;
        }
        return s3;
    }

    public static void main(String args[])
    {
        BeanLoader beanloader = new BeanLoader();
        Vector vector = new Vector(100, 100);
        String s = "D:\\Program Files\\IBM\\wsa-base-20021125_2118-WB202-AD-V50D-GA\\eclipse\\runtime-workspace\\tx\\Web Content\\RecordJSPs\\";
        vector = beanloader.getAllJSPsInThisApp(s, vector);
        if(vector.size() > 0)
        {
            for(int i = 0; i < vector.size(); i++)
                beanloader.loadBeansForJSP(vector.elementAt(i).toString(), s, i);

        } else
        {
            System.out.println("v is empty");
        }
    }

    protected static String getProjectStyleSheets(HttpServletRequest httpservletrequest)
    {
        String s = "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + httpservletrequest.getContextPath() + "/styles/apparea/apparea.css\">";
        String s1 = httpservletrequest.getSession().getServletContext().getRealPath("RecordJSPs/");
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

    public static final String copyRight = "(C) Copyright IBM Corporation 2003 all rights reserved";
    private static String ACTIONNAME = "Action";
    private static String TITLESTRING = "WebFacing Definition Bean Loader";
    private static String EVENROWSTYLE = "subfileRecord1";
    private static String ODDROWSTYLE = "subfileRecord2";
    private RecordBeanFactory rbf;
    private int beanCount;

}
