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

public class EMSGCTL_Servlet extends HttpServlet
{

    public EMSGCTL_Servlet()
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
            IDisplaySFLCTLRecord idisplaysflctlrecord = null;
            boolean flag = false;
            synchronized(httpservletrequest)
            {
                idisplaysflctlrecord = (IDisplaySFLCTLRecord)httpsession.getAttribute("EMSGCTL");
                if(idisplaysflctlrecord == null)
                    throw new InstantiationException("bean EMSGCTL  not found within scope ");
            }
            if(!flag);
            printwriter.write("\r\n");
            String s = Integer.toString(idisplaysflctlrecord.getDisplayZIndex());
            int i = Integer.parseInt((String)httpsession.getAttribute("WWidth"));
            boolean flag1 = idisplaysflctlrecord.isProtected();
            printwriter.write("\r\n<INPUT TYPE=\"HIDDEN\" ID =\"l");
            printwriter.print(s);
            printwriter.write("_EMSGCTL_version\" NAME=\"l");
            printwriter.print(s);
            printwriter.write("_EMSGCTL_version\" VALUE=501020000>\r\n");
            printwriter.write("\r\n<TR id=\"l");
            printwriter.print(s);
            printwriter.write("r24\" style=\"visibility:hidden\">\r\n<TD><IMG src=\"styles/apparea/LeftArrow.gif\" onClick=\"SingleLineSubfileScrollUp(l");
            printwriter.print(s);
            printwriter.write("_EMSGCTL$$cbField, ");
            printwriter.print(idisplaysflctlrecord.enablePageUp());
            printwriter.write(");\"></TD>\r\n<TD colspan=76>\r\n<INPUT CLASS=\"cbField\" ID=\"l");
            printwriter.print(s);
            printwriter.write("_EMSGCTL$$cbField\" onclick=\"this.comboBoxField.showList();\" READONLY TABINDEX=\"-1\" STYLE=\"width:100%\">\r\n</TD>\r\n<TD><IMG src=\"styles/apparea/UpArrow.gif\" class=\"cbButton\" id=\"l");
            printwriter.print(s);
            printwriter.write("_EMSGCTL$$cbButton\" onClick=\"this.comboBoxButton.showList();\"></TD>\r\n<TD><IMG src=\"styles/apparea/RightArrow.gif\" onClick=\"SingleLineSubfileScrollDown(l");
            printwriter.print(s);
            printwriter.write("_EMSGCTL$$cbField, ");
            printwriter.print(idisplaysflctlrecord.enablePageDown());
            printwriter.write(");\"></TD>\r\n</TR>\r\n");
            printwriter.write("\r\n");
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
