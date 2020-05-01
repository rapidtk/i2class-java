// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.httpcontroller;

import com.ibm.as400ad.webfacing.runtime.controller.RecordBeanFactory;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import com.ibm.etools.iseries.webfacing.runtime.platform.J2eeLevel;
import com.ibm.etools.iseries.webfacing.runtime.platform.J2eeServlet;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.httpcontroller:
//            ControllerRequestHandler, HttpSessionManager, ClientLock, HttpRequestHandler

public class ControllerServlet extends HttpServlet
{

    public ControllerServlet()
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
            HttpSession httpsession = httpservletrequest.getSession();
            ClientLock clientlock = HttpSessionManager.getSessionLock(httpservletrequest.getSession());
            int i = clientlock.acquireLockWithWait();
            if(i == ClientLock.INTERMEDTHREAD)
                return;
            if(i == ClientLock.LASTTHREAD)
            {
                HttpRequestHandler.putSessionValue(httpsession, "controllerState", new Integer(5));
                if(httpsession.getAttribute("screenbuilder") == null)
                {
                    J2eeServlet._j2eeLevel.wfSetCharacterEncoding(httpservletrequest);
                    javax.servlet.ServletContext servletcontext = getServletConfig().getServletContext();
                    ControllerRequestHandler controllerrequesthandler = new ControllerRequestHandler(httpservletrequest, httpservletresponse, servletcontext);
                    try
                    {
                        controllerrequesthandler.processEndOfApplication();
                    }
                    catch(WebfacingInternalException webfacinginternalexception)
                    {
                        WFSession.getTraceLogger().err(2, "Exception thrown while processing End Of Application:" + webfacinginternalexception);
                    }
                    clientlock.releaseLock();
                    return;
                }
            }
            J2eeServlet._j2eeLevel.wfSetCharacterEncoding(httpservletrequest);
            javax.servlet.ServletContext servletcontext1 = getServletConfig().getServletContext();
            ControllerRequestHandler controllerrequesthandler1 = new ControllerRequestHandler(httpservletrequest, httpservletresponse, servletcontext1);
            controllerrequesthandler1.handleRequest(RecordBeanFactory.getRecordBeanFactory(servletcontext1));
            clientlock.releaseLock();
        }
        catch(Exception exception)
        {
            HttpSessionManager.getSessionLock(httpservletrequest.getSession()).releaseLock();
            WFSession.getTraceLogger().err(2, " Exception caught in ControllerServlet :" + exception);
        }
    }

    public void init(ServletConfig servletconfig)
        throws ServletException
    {
        super.init(servletconfig);
    }

    public static final String copyRight = "(C) Copyright IBM Corporation 1999-2003 all rights reserved";
}
