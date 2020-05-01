// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.diags;

import com.ibm.as400ad.webfacing.common.WFAppProperties;
import com.ibm.as400ad.webfacing.runtime.controller.ICacheable;
import com.ibm.as400ad.webfacing.runtime.controller.RecordBeanFactory;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CacheDumper extends HttpServlet
{

    public CacheDumper()
    {
        rbf = null;
        wap = null;
    }

    public void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException
    {
        doPost(httpservletrequest, httpservletresponse);
    }

    public void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException
    {
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        String as[] = httpservletrequest.getParameterValues(ACTIONNAME);
        if(null != as && null != as[0])
            if(as[0].startsWith(RESETPREFIX))
            {
                for(ICacheable icacheable = rbf.getCacheHead(); null != icacheable;)
                {
                    icacheable.resetHitCount();
                    icacheable = icacheable.getNext();
                    flag1 = true;
                }

            } else
            if(as[0].startsWith(SETPREFIX))
            {
                String as1[] = httpservletrequest.getParameterValues(LIMITVALUENAME);
                if(null != as1 && null != as1[0])
                    try
                    {
                        int j = Integer.parseInt(as1[0]);
                        if(j < 0)
                            j = 0;
                        rbf.setCacheLimit(j);
                        flag = true;
                    }
                    catch(Throwable throwable) { }
            } else
            if(as[0].startsWith(CLEARPREFIX))
            {
                int i = rbf.getCacheLimit();
                if(i > 0)
                {
                    rbf.setCacheLimit(0);
                    rbf.setCacheLimit(i);
                }
            } else
            if(as[0].startsWith(SAVEPREFIX))
            {
                String as2[] = httpservletrequest.getParameterValues(SAVEFILEVALUENAME);
                if(null != as2 && null != as2[0])
                    try
                    {
                        flag3 = saveList(httpservletrequest, as2[0]);
                    }
                    catch(Throwable throwable1) { }
            }
        PrintWriter printwriter = null;
        try
        {
            httpservletresponse.setContentType("text/html;charset=UTF-8");
            printwriter = httpservletresponse.getWriter();
        }
        catch(Throwable throwable2) { }
        printwriter.print("<html><head>");
        printwriter.print(getProjectStyleSheets(httpservletrequest));
        printwriter.print("<title>" + TITLESTRING + "</title>");
        printwriter.print("</head><body class=\"theapp wf_field wf_default\">");
        printwriter.print("<h2>" + TITLESTRING + "</h2>");
        if(flag1)
            printwriter.println("<p><strong>The cache hit counters have been reset.</strong></p>");
        if(flag)
            printwriter.println("<p><strong>The cache size has been updated.</strong></p>");
        if(flag3)
            printwriter.println("<p><strong>The list of elements in the cache have been saved.</strong></p>");
        printwriter.println("<p>The number of elements in the cache is " + getCacheCount() + " of " + rbf.getCacheLimit() + ".</p>");
        ICacheable icacheable1 = rbf.getCacheHead();
        if(null != icacheable1)
        {
            printwriter.println("<p>The number of total cache hits is   " + icacheable1.getHitCount() + ".</p>");
            printwriter.println("<p>The number of total cache misses is " + getCacheTail().getHitCount() + ".</p>");
            printwriter.print("<table class=\"wf_field wf_default\" border=\"0\">");
            printwriter.print("<tr>");
            printwriter.print("<td>");
            printwriter.print("<FORM class=\"wf_field wf_default\" method=\"post\" action=\"" + httpservletrequest.getRequestURI() + "\">");
            printwriter.print("<INPUT class=\"wf_field wf_default\" type=\"submit\" name=\"" + ACTIONNAME + "\" value=\"" + RESETNAME + "\">");
            printwriter.print("</form>");
            printwriter.print("</td>");
            printwriter.print("<td></td>");
            printwriter.print("</tr>");
            printwriter.print("<tr>");
            printwriter.print("<td>");
            printwriter.print("<FORM class=\"wf_field wf_default\" method=\"post\" action=\"" + httpservletrequest.getRequestURI() + "\">");
            printwriter.print("<table class=\"wf_field wf_default\" border=\"0\">");
            printwriter.print("<tr><td>");
            printwriter.print("<INPUT class=\"wf_field wf_default\" type=\"submit\" name=\"" + ACTIONNAME + "\" value=\"" + SETLIMITNAME + "\">");
            printwriter.print("</td><td>");
            printwriter.print("<INPUT class=\"wf_field wf_default\" type=\"text\" name=\"" + LIMITVALUENAME + "\" size=\"4\" maxlength=\"4\" value=\"" + rbf.getCacheLimit() + "\">");
            printwriter.print("</td></tr></table>");
            printwriter.print("</form>");
            printwriter.print("</td>");
            printwriter.print("</tr>");
            printwriter.print("<tr>");
            printwriter.print("<td>");
            printwriter.print("<FORM class=\"wf_field wf_default\" method=\"post\" action=\"" + httpservletrequest.getRequestURI() + "\">");
            printwriter.print("<INPUT class=\"wf_field wf_default\" type=\"submit\" name=\"" + ACTIONNAME + "\" value=\"" + REFRESHNAME + "\">");
            printwriter.print("</form>");
            printwriter.print("</td>");
            printwriter.print("<td>");
            printwriter.print("<FORM class=\"wf_field wf_default\" method=\"post\" action=\"" + httpservletrequest.getRequestURI() + "\">");
            printwriter.print("<INPUT class=\"wf_field wf_default\" type=\"submit\" name=\"" + ACTIONNAME + "\" value=\"" + CLEARNAME + "\">");
            printwriter.print("</form>");
            printwriter.print("</td>");
            printwriter.print("</tr>");
            printwriter.print("<tr>");
            printwriter.print("<td>");
            printwriter.print("<FORM class=\"wf_field wf_default\" method=\"post\" action=\"" + httpservletrequest.getRequestURI() + "\">");
            printwriter.print("<table border=\"0\">");
            printwriter.print("<tr><td>");
            printwriter.print("<INPUT class=\"wf_field wf_default\" type=\"submit\" name=\"" + ACTIONNAME + "\" value=\"" + SAVENAME + "\">");
            printwriter.print("</td><td>");
            printwriter.print("<INPUT class=\"wf_field wf_default\" type=\"text\" name=\"" + SAVEFILEVALUENAME + "\" size=\"20\" maxlength=\"100\" value=\"" + SAVEFILENAMEDEFAULT + "\">");
            printwriter.print("</td></tr></table>");
            printwriter.print("</form>");
            printwriter.print("</td>");
            printwriter.print("</tr>");
            printwriter.print("</table>");
            printwriter.println("<table class=\"wf_field wf_default\" border=\"0\"><tr><th>Cache Entry<br>Oldest on Top</th><th>D</th><th>V</th><th>F</th><th>Hits</th></tr>");
            int k = 0;
            for(icacheable1 = icacheable1.getNext(); null != icacheable1; icacheable1 = icacheable1.getNext())
            {
                String s = icacheable1.getRecordClassName();
                if(null != s)
                {
                    int l = k++ % 2;
                    String s1 = null;
                    switch(l)
                    {
                    case 0: // '\0'
                        s1 = EVENROWSTYLE;
                        break;

                    case 1: // '\001'
                    default:
                        s1 = ODDROWSTYLE;
                        break;
                    }
                    printwriter.print("<tr class=\"" + s1 + "\"><td>" + s + "</td><td>");
                    if(null != icacheable1.getDataDefinition())
                        printwriter.print("Y");
                    else
                        printwriter.print("n");
                    printwriter.print("</td><td>");
                    if(null != icacheable1.getViewDefinition())
                        printwriter.print("Y");
                    else
                        printwriter.print("n");
                    printwriter.print("</td><td>");
                    if(null != icacheable1.getFeedbackDefinition())
                        printwriter.print("Y");
                    else
                        printwriter.print("n");
                    printwriter.print("</td><td>");
                    printwriter.print(icacheable1.getHitCount());
                    printwriter.println("</td></tr>");
                }
            }

            printwriter.print("</table>");
        }
        printwriter.println("</body></html>");
    }

    public void init(ServletConfig servletconfig)
        throws ServletException
    {
        super.init(servletconfig);
        rbf = RecordBeanFactory.getRecordBeanFactory(servletconfig.getServletContext());
        wap = WFAppProperties.getWFAppProperties(servletconfig.getServletContext());
    }

    private boolean saveList(HttpServletRequest httpservletrequest, String s)
    {
        boolean flag = false;
        try
        {
            String s1 = httpservletrequest.getSession().getServletContext().getRealPath("RecordJSPs/");
            s1 = s1 + s;
            File file = new File(s1);
            file.delete();
            FileOutputStream fileoutputstream = new FileOutputStream(file);
            BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(fileoutputstream);
            ICacheable icacheable = rbf.getCacheHead();
            for(icacheable = icacheable.getNext(); null != icacheable; icacheable = icacheable.getNext())
            {
                String s2 = icacheable.getRecordClassName();
                if(null != s2)
                {
                    s2 = s2 + '\n';
                    fileoutputstream.write(s2.getBytes());
                }
            }

            fileoutputstream.close();
            flag = true;
        }
        catch(Throwable throwable)
        {
            flag = false;
        }
        return flag;
    }

    private int getCacheCount()
    {
        int i = 0;
        for(ICacheable icacheable = rbf.getCacheHead(); null != icacheable; icacheable = icacheable.getNext())
            i++;

        i -= 2;
        return i;
    }

    private ICacheable getCacheTail()
    {
        ICacheable icacheable = rbf.getCacheHead();
        if(null != icacheable)
            for(; null != icacheable.getNext(); icacheable = icacheable.getNext());
        return icacheable;
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

    public static final String copyRight = "(C) Copyright IBM Corporation 2003 all rights reserved";
    private static String LIMITNAME = "NewLimit";
    private static String RESETPREFIX;
    private static String RESETNAME;
    private static String ACTIONNAME = "Action";
    private static String SETPREFIX;
    private static String SETLIMITNAME;
    private static String LIMITVALUENAME = "NewLimit";
    private static String REFRESHNAME = "Refresh";
    private static String CLEARPREFIX;
    private static String CLEARNAME;
    private static String SAVEPREFIX;
    private static String SAVENAME;
    private static String SAVEFILEPREFIX = "File";
    private static String SAVEFILEVALUENAME = "FileName";
    protected static String SAVEFILENAMEDEFAULT = "cachedbeannames.lst";
    private static String TITLESTRING = "WebFacing Definition Bean Cache Viewer";
    private static String EVENROWSTYLE = "subfileRecord1";
    private static String ODDROWSTYLE = "subfileRecord2";
    private RecordBeanFactory rbf;
    private WFAppProperties wap;

    static 
    {
        RESETPREFIX = "Reset";
        RESETNAME = RESETPREFIX + "Counters";
        SETPREFIX = "Set";
        SETLIMITNAME = SETPREFIX + "Limit";
        CLEARPREFIX = "Clear";
        CLEARNAME = CLEARPREFIX + "Cache";
        SAVEPREFIX = "Save";
        SAVENAME = SAVEPREFIX + "List";
    }
}
