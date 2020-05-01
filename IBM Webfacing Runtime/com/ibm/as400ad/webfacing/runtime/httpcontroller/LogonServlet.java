// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.httpcontroller;

import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import com.ibm.etools.iseries.webfacing.runtime.platform.J2eeLevel;
import com.ibm.etools.iseries.webfacing.runtime.platform.J2eeServlet;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.httpcontroller:
//            LogonRequestHandler, HttpSessionManager, ClientLock

public class LogonServlet extends HttpServlet
{

    public LogonServlet()
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
        try
        {
            ClientLock clientlock = HttpSessionManager.getSessionLock(httpservletrequest.getSession());
            int i = clientlock.acquireLockWithWait();
            if(i == ClientLock.INTERMEDTHREAD)
                return;
            J2eeServlet._j2eeLevel.wfSetCharacterEncoding(httpservletrequest);
            LogonRequestHandler logonrequesthandler = new LogonRequestHandler(httpservletrequest, httpservletresponse, getServletConfig().getServletContext());
            logonrequesthandler.handleRequest();
            clientlock.releaseLock();
        }
        catch(Exception exception)
        {
            HttpSessionManager.getSessionLock(httpservletrequest.getSession()).releaseLock();
            WFSession.getTraceLogger().err(1, " Exception caught in LogonServlet :" + exception);
        }
    }

    public void init(ServletConfig servletconfig)
        throws ServletException
    {
        super.init(servletconfig);
    }

    private static final String copyRight = new String("(C) Copyright IBM Corporation 1999-2002 all rights reserved");

}
