// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.qsys.servlets.qddspext;

import com.ibm.as400ad.webfacing.runtime.controller.ErrorHandler;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.view.IDisplayRecord;
import com.ibm.as400ad.webfacing.runtime.view.IDisplaySFLCTLRecord;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.*;

public class SCRN2CTLJavaScript_Servlet extends HttpServlet
{

    public SCRN2CTLJavaScript_Servlet()
    {
    }

    public final void init()
    {
    }

    public void service(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException
    {
        HttpSession httpsession = httpservletrequest.getSession();
        PrintWriter printwriter = httpservletresponse.getWriter();
        try
        {
            try
            {
                httpservletresponse.setContentType("text/html");
            }
            catch(IllegalStateException illegalstateexception) { }
            printwriter.write("\r\n");
            printwriter.write("\r\n");
            IDisplaySFLCTLRecord idisplaysflctlrecord = null;
            boolean flag = false;
            synchronized(httpservletrequest)
            {
                idisplaysflctlrecord = (IDisplaySFLCTLRecord)httpsession.getAttribute("SCRN2CTL");
                if(idisplaysflctlrecord == null)
                    throw new InstantiationException("bean SCRN2CTL  not found within scope ");
            }
            if(!flag);
            printwriter.write("\r\n");
            int i = idisplaysflctlrecord.getDisplayZIndex();
            printwriter.write("\r\n");
            for(int j = 1; j <= 15; j++)
            {
                printwriter.write("\r\n");
                if(idisplaysflctlrecord.isActiveRecord(j))
                {
                    printwriter.write("\r\nrc(\"l");
                    printwriter.print(i);
                    printwriter.write("_SCRN2CTL$MSGDATA$");
                    printwriter.print(j);
                    printwriter.write("\");\r\n");
                }
            }

            printwriter.write("\r\n");
        }
        catch(Throwable throwable)
        {
            ErrorHandler errorhandler = new ErrorHandler(getServletConfig().getServletContext(), httpservletrequest, httpservletresponse, WFSession.getTraceLogger(), true);
            errorhandler.handleError(new Exception(throwable.toString()), "Error in system servlet.");
        }
        finally
        {
            printwriter.flush();
        }
    }
}
