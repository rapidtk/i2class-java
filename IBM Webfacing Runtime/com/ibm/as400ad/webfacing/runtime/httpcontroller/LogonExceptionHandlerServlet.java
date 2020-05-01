// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.httpcontroller;

import com.ibm.as400ad.webfacing.common.WFAppProperties;
import com.ibm.as400ad.webfacing.runtime.host.WFInvalidSignOnException;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

public class LogonExceptionHandlerServlet extends HttpServlet
{

    public LogonExceptionHandlerServlet()
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
        ServletContext servletcontext = getServletContext();
        WFInvalidSignOnException wfinvalidsignonexception = (WFInvalidSignOnException)httpservletrequest.getAttribute("exception");
        httpservletrequest.setAttribute("exception", wfinvalidsignonexception);
        httpservletrequest.getSession().setAttribute("user", httpservletrequest.getAttribute("user"));
        httpservletrequest.getSession().setAttribute("url", httpservletrequest.getAttribute("url"));
        String s = wfinvalidsignonexception.getMessage().substring(3, 6);
        WFAppProperties wfappproperties = (WFAppProperties)getServletContext().getAttribute(WFAppProperties.WFAPPPROPERTIES);
        if(s.equals("125") && wfappproperties.getChangeExpiredPassword().equals("true"))
        {
            servletcontext.getRequestDispatcher("/services/Logon/ChangePassword.jsp").forward(httpservletrequest, httpservletresponse);
        } else
        {
            String s1 = wfappproperties.getLogonPageName();
            httpservletrequest.setAttribute(WFAppProperties.WFLOGONPAGE, s1);
            servletcontext.getRequestDispatcher("/services/Logon/PasswordFailure.jsp").forward(httpservletrequest, httpservletresponse);
        }
    }

    public void init(ServletConfig servletconfig)
        throws ServletException
    {
        super.init(servletconfig);
    }

    private static final String copyRight = new String("(C) Copyright IBM Corporation 1999-2003 all rights reserved");

}
