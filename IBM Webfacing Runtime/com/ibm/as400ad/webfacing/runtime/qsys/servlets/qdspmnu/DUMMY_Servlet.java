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

public class DUMMY_Servlet extends HttpServlet
{

    public DUMMY_Servlet()
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
            printwriter.write("\r\n");
            IDisplayRecord idisplayrecord = null;
            boolean flag = false;
            synchronized(httpservletrequest)
            {
                idisplayrecord = (IDisplayRecord)httpsession.getAttribute("DUMMY");
                if(idisplayrecord == null)
                    throw new InstantiationException("bean DUMMY  not found within scope ");
            }
            if(!flag);
            printwriter.write("\r\n");
            String s = Integer.toString(idisplayrecord.getDisplayZIndex());
            int i = Integer.parseInt((String)httpsession.getAttribute("WWidth"));
            boolean flag1 = idisplayrecord.isProtected();
            printwriter.write("\r\n<INPUT TYPE=\"HIDDEN\" ID =\"l");
            printwriter.print(s);
            printwriter.write("_DUMMY_version\" NAME=\"l");
            printwriter.print(s);
            printwriter.write("_DUMMY_version\" VALUE=501020000>\r\n<TR id=\"l");
            printwriter.print(s);
            printwriter.write("r1\" class=\"trStyle\">\r\n");
            byte byte0 = 0;
            printwriter.write("\r\n<TD colspan=1>&nbsp;</TD>\r\n<TD NOWRAP colspan=1 rowspan=1><span id='l");
            printwriter.print(s);
            printwriter.write("_DUMMY$DUMMY1'  class=\"wf_default wf_field\"  >");
            printwriter.print(idisplayrecord.getFieldValueWithTransform("DUMMY1", 2));
            printwriter.write("</span></TD>");
            byte0 = 2;
            printwriter.write("\r\n</TR>\r\n");
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
