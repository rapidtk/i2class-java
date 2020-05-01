// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.runtime.platform;

import com.ibm.as400ad.webfacing.common.InvocationProperties;
import com.ibm.as400ad.webfacing.common.WFAppProperties;
import com.ibm.as400ad.webfacing.runtime.controller.RecordBeanFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.*;

// Referenced classes of package com.ibm.etools.iseries.webfacing.runtime.platform:
//            J2eeLevel

public class J2eeServlet extends HttpServlet
{

    public J2eeServlet()
    {
    }

    public void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException
    {
        doPost(httpservletrequest, httpservletresponse);
    }

    public void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException
    {
        httpservletresponse.setContentType("text/html");
        _j2eeLevel.wfSetCharacterEncoding(httpservletrequest);
        PrintWriter printwriter = httpservletresponse.getWriter();
        String s = "<!DOCTYPE HTML PUBLIC \"=//W3C//DTD HTML 4.0 Transitional//EN\">\n";
        printwriter.println(s + "<HTML>\n" + "<HEAD><TITLE>J2EE</TITLE></HEAD>\n" + "<BODY>\n" + "<H2>Major: " + getServletContext().getMajorVersion() + "</H2>\n" + "<H2>Minor: " + getServletContext().getMinorVersion() + "</H2>\n" + "</BODY></HTML>");
    }

    public void init(ServletConfig servletconfig)
        throws ServletException
    {
        super.init(servletconfig);
        ServletContext servletcontext = getServletContext();
        RecordBeanFactory.getRecordBeanFactory(servletcontext);
        WFAppProperties.getWFAppProperties(servletcontext);
        InvocationProperties.preload(servletcontext);
        Class class1 = null;
        try
        {
            switch(servletVersion(servletcontext))
            {
            case 23: // '\027'
                class1 = Class.forName("com.ibm.etools.iseries.webfacing.runtime.platform.J2ee13");
                break;

            default:
                class1 = Class.forName("com.ibm.etools.iseries.webfacing.runtime.platform.J2ee12");
                break;
            }
        }
        catch(ClassNotFoundException classnotfoundexception) { }
        try
        {
            _j2eeLevel = (J2eeLevel)class1.newInstance();
        }
        catch(InstantiationException instantiationexception) { }
        catch(IllegalAccessException illegalaccessexception) { }
    }

    private int servletVersion(ServletContext servletcontext)
    {
        return 10 * servletcontext.getMajorVersion() + servletcontext.getMinorVersion();
    }

    public static J2eeLevel _j2eeLevel = null;

}
