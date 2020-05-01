// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.qsys.servlets.qdspmnu;

import com.ibm.as400ad.webfacing.runtime.controller.ErrorHandler;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.view.IDisplayRecord;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.*;

public class MENUOPTNJavaScript_Servlet extends HttpServlet
{

    public MENUOPTNJavaScript_Servlet()
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
            IDisplayRecord idisplayrecord = null;
            boolean flag = false;
            synchronized(httpservletrequest)
            {
                idisplayrecord = (IDisplayRecord)httpsession.getAttribute("MENUOPTN");
                if(idisplayrecord == null)
                    throw new InstantiationException("bean MENUOPTN  not found within scope ");
            }
            if(!flag);
            printwriter.write("\r\n");
            int i = idisplayrecord.getDisplayZIndex();
            printwriter.write("\r\ncf(\"l");
            printwriter.print(i);
            printwriter.write("_MENUOPTN$OPTLINE\",22,7,{");
            if(idisplayrecord.isMDTOn("OPTLINE"))
                printwriter.write("mdt:true,");
            printwriter.write("datalength:4,shift:\"Y\"});\r\nrc(\"l");
            printwriter.print(i);
            printwriter.write("_MENUOPTN$MSGLINE\",24,2);\r\n");
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
