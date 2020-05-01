// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.runtime.filters;

import com.ibm.as400ad.webfacing.runtime.httpcontroller.ClientLock;
import com.ibm.as400ad.webfacing.runtime.httpcontroller.HttpSessionManager;
import com.ibm.as400ad.webfacing.util.TraceLogger;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

// Referenced classes of package com.ibm.etools.iseries.webfacing.runtime.filters:
//            CharArrayHTTPResponseWrapper

public class RequestSynchronizationFilter
    implements Filter
{

    public RequestSynchronizationFilter()
    {
    }

    public void doFilter(ServletRequest servletrequest, ServletResponse servletresponse, FilterChain filterchain)
        throws ServletException, IOException
    {
        HttpServletRequest httpservletrequest = (HttpServletRequest)servletrequest;
        HttpServletResponse httpservletresponse = (HttpServletResponse)servletresponse;
        HttpSession httpsession = httpservletrequest.getSession();
        try
        {
            ClientLock clientlock = HttpSessionManager.getSessionLock(httpsession);
            int i = clientlock.acquireLockWithWait();
            if(i != ClientLock.INTERMEDTHREAD)
            {
                if(i == ClientLock.FIRSTTHREAD)
                {
                    CharArrayHTTPResponseWrapper chararrayhttpresponsewrapper = new CharArrayHTTPResponseWrapper(httpservletresponse);
                    filterchain.doFilter(httpservletrequest, chararrayhttpresponsewrapper);
                    char ac1[] = chararrayhttpresponsewrapper.toCharArray();
                    httpsession.setAttribute("RESPONSEOBJECT", ac1);
                }
                if(clientlock.isThisTheLastThread())
                {
                    char ac[] = (char[])httpsession.getAttribute("RESPONSEOBJECT");
                    PrintWriter printwriter = httpservletresponse.getWriter();
                    printwriter.write(ac);
                }
                clientlock.releaseLock();
            }
        }
        catch(Exception exception)
        {
            ClientLock clientlock1 = HttpSessionManager.getSessionLock(httpsession);
            clientlock1.releaseLock();
            if(TraceLogger.getErrorLevel() > 2)
            {
                System.out.println("An uncaught exception has propagated to RequestSynchronizationFilter and was caught: " + exception.getMessage());
                exception.printStackTrace();
            }
        }
    }

    public void init(FilterConfig filterconfig)
        throws ServletException
    {
        config = filterconfig;
    }

    protected FilterConfig getFilterConfig()
    {
        return config;
    }

    public void destroy()
    {
    }

    private FilterConfig config;
    public static final String STOREDRESPONSE = "RESPONSEOBJECT";
}
