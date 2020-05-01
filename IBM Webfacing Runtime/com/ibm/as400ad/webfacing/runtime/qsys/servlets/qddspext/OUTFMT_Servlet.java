// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.qsys.servlets.qddspext;

import com.ibm.as400ad.webfacing.runtime.controller.ErrorHandler;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.view.IDisplayRecord;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.*;

public class OUTFMT_Servlet extends HttpServlet
{

    public OUTFMT_Servlet()
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
                idisplayrecord = (IDisplayRecord)httpsession.getAttribute("OUTFMT");
                if(idisplayrecord == null)
                    throw new InstantiationException("bean OUTFMT  not found within scope ");
            }
            if(!flag);
            printwriter.write("\r\n");
            String s = Integer.toString(idisplayrecord.getDisplayZIndex());
            int i = Integer.parseInt((String)httpsession.getAttribute("WWidth"));
            boolean flag1 = idisplayrecord.isProtected();
            printwriter.write("\r\n<INPUT TYPE=\"HIDDEN\" ID =\"l");
            printwriter.print(s);
            printwriter.write("_OUTFMT_version\" NAME=\"l");
            printwriter.print(s);
            printwriter.write("_OUTFMT_version\" VALUE=501020000>\r\n<TR id=\"l");
            printwriter.print(s);
            printwriter.write("r19\" class=\"trStyle\">\r\n");
            byte byte0 = 0;
            printwriter.write("\r\n<TD colspan=1>&nbsp;</TD>\r\n<TD NOWRAP colspan=43 rowspan=1><span id='l");
            printwriter.print(s);
            printwriter.write("_OUTFMT$Unnamed0'  class=\"wf_blue wf_field\"  onClick=\"setCursor(19, 2);\" >Type&nbsp;reply,&nbsp;press&nbsp;Enter.&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></TD>");
            byte0 = 44;
            printwriter.write("\r\n</TR>\r\n<TR id=\"l");
            printwriter.print(s);
            printwriter.write("r20\" class=\"trStyle\">\r\n");
            byte0 = 0;
            printwriter.write("\r\n<TD colspan=3>&nbsp;</TD>\r\n<TD NOWRAP colspan=11 rowspan=1><span id='l");
            printwriter.print(s);
            printwriter.write("_OUTFMT$Unnamed1'  class=\"wf_default wf_field\"  onClick=\"setCursor(20, 4);\" >Reply&nbsp;.&nbsp;.&nbsp;.</span></TD>");
            byte0 = 14;
            printwriter.write("\r\n<TD colspan=3>&nbsp;</TD>\r\n<TD NOWRAP colspan=63 rowspan=1><span id='l");
            printwriter.print(s);
            printwriter.write("_OUTFMT$REPLYOUT'  class=\"wf_default wf_field\"  >");
            printwriter.print(idisplayrecord.getFieldValueWithTransform("REPLYOUT", 2, 0, 63));
            printwriter.write("</span></TD>");
            byte0 = 80;
            printwriter.write("\r\n</TR>\r\n<TR id=\"l");
            printwriter.print(s);
            printwriter.write("r21\" class=\"trStyle\">\r\n");
            byte0 = 0;
            printwriter.write("\r\n<TD NOWRAP colspan=79 rowspan=1><span   class=\"wf_default wf_field\"  >");
            printwriter.print(idisplayrecord.getFieldValueWithTransform("REPLYOUT", 2, 63, 142));
            printwriter.write("</span></TD>");
            byte0 = 79;
            printwriter.write("\r\n</TR>\r\n<TR id=\"l");
            printwriter.print(s);
            printwriter.write("r22\" class=\"trStyle\"><TD>&nbsp;</TD></TR>\r\n<TR id=\"l");
            printwriter.print(s);
            printwriter.write("r23\" class=\"trStyle\">\r\n");
            byte0 = 0;
            printwriter.write("\r\n<TD colspan=1>&nbsp;</TD>\r\n<TD NOWRAP colspan=30 rowspan=1><span id='l");
            printwriter.print(s);
            printwriter.write("_OUTFMT$Unnamed2'  class=\"wf_blue wf_field\"  onClick=\"setCursor(23, 2);\" ></span></TD>");
            byte0 = 31;
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
