// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.qsys.servlets.qdspmnu;

import com.ibm.as400ad.webfacing.runtime.controller.ErrorHandler;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.view.DisplayAttributeBean;
import com.ibm.as400ad.webfacing.runtime.view.IDisplayRecord;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.*;

public class HELPFMT_Servlet extends HttpServlet
{

    public HELPFMT_Servlet()
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
                idisplayrecord = (IDisplayRecord)httpsession.getAttribute("HELPFMT");
                if(idisplayrecord == null)
                    throw new InstantiationException("bean HELPFMT  not found within scope ");
            }
            if(!flag);
            printwriter.write("\r\n");
            String s = Integer.toString(idisplayrecord.getDisplayZIndex());
            int i = Integer.parseInt((String)httpsession.getAttribute("WWidth"));
            boolean flag1 = idisplayrecord.isProtected();
            printwriter.write("\r\n<INPUT TYPE=\"HIDDEN\" ID =\"l");
            printwriter.print(s);
            printwriter.write("_HELPFMT_version\" NAME=\"l");
            printwriter.print(s);
            printwriter.write("_HELPFMT_version\" VALUE=501020000>\r\n<TR id=\"l");
            printwriter.print(s);
            printwriter.write("r22\" class=\"trStyle\">\r\n");
            byte byte0 = 1;
            printwriter.write("\r\n<TD NOWRAP colspan=52 rowspan=1><span id='l");
            printwriter.print(s);
            printwriter.write("_HELPFMT$Unnamed0'  class=\"wf_blue wf_field\"  onClick=\"setCursor(22, 2);\" ></span></TD>");
            byte0 = 53;
            printwriter.write("\r\n</TR>\r\n<TR id=\"l");
            printwriter.print(s);
            printwriter.write("r23\" class=\"trStyle\">\r\n");
            byte0 = 1;
            printwriter.write("\r\n<TD NOWRAP colspan=78 rowspan=1><span id='l");
            printwriter.print(s);
            printwriter.write("_HELPFMT$OVERLAY'  class=\"wf_white wf_field\"  >");
            printwriter.print(idisplayrecord.getFieldValueWithTransform("OVERLAY", 2));
            printwriter.write("</span></TD>");
            byte0 = 79;
            printwriter.write("\r\n</TR>\r\n<TR id=\"l");
            printwriter.print(s);
            printwriter.write("r24\" class=\"trStyle\">\r\n");
            byte0 = 1;
            printwriter.write("\r\n<TD NOWRAP colspan=78 rowspan=1><span id='l");
            printwriter.print(s);
            printwriter.write("_HELPFMT$MSGLINE'  ");
            DisplayAttributeBean displayattributebean = new DisplayAttributeBean();
            displayattributebean.addColourIndExpr("white", "");
            printwriter.write(" class=\"wf_hi ");
            printwriter.print(idisplayrecord.evaluateStyleClass(displayattributebean));
            printwriter.write("\" ");
            printwriter.write("  >");
            printwriter.print(idisplayrecord.getFieldValueWithTransform("MSGLINE", 2));
            printwriter.write("</span></TD>");
            byte0 = 79;
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
