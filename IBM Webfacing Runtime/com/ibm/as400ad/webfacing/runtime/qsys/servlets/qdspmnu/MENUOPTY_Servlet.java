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

public class MENUOPTY_Servlet extends HttpServlet
{

    public MENUOPTY_Servlet()
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
                idisplayrecord = (IDisplayRecord)httpsession.getAttribute("MENUOPTY");
                if(idisplayrecord == null)
                    throw new InstantiationException("bean MENUOPTY  not found within scope ");
            }
            if(!flag);
            printwriter.write("\r\n");
            String s = Integer.toString(idisplayrecord.getDisplayZIndex());
            int i = Integer.parseInt((String)httpsession.getAttribute("WWidth"));
            boolean flag1 = idisplayrecord.isProtected();
            printwriter.write("\r\n<INPUT TYPE=\"HIDDEN\" ID =\"l");
            printwriter.print(s);
            printwriter.write("_MENUOPTY_version\" NAME=\"l");
            printwriter.print(s);
            printwriter.write("_MENUOPTY_version\" VALUE=501020000>\r\n<TR id=\"l");
            printwriter.print(s);
            printwriter.write("r20\" class=\"trStyle\">\r\n");
            byte byte0 = 1;
            printwriter.write("\r\n<TD NOWRAP colspan=4 rowspan=1><span id='l");
            printwriter.print(s);
            printwriter.write("_MENUOPTY$Unnamed2'  class=\"wf_default wf_field\"  onClick=\"setCursor(20, 2);\" >&nbsp;&nbsp;&nbsp;&nbsp;</span></TD>");
            byte0 = 5;
            printwriter.write("\r\n<TD colspan=1>&nbsp;</TD>\r\n<TD NOWRAP colspan=4 rowspan=1><INPUT ");
            if(flag1)
                printwriter.write(" readonly tabindex=-1 ");
            printwriter.write(" id=\"l");
            printwriter.print(s);
            printwriter.write("_MENUOPTY$OPTLINE\" previousValue=\"");
            printwriter.print(idisplayrecord.getFieldValueWithTransform("OPTLINE", 1));
            printwriter.write("\"  class=\"");
            printwriter.print(flag1 ? "wf_pr" : "");
            printwriter.write(" wf_green wf_field\" VALUE=\"");
            printwriter.print(idisplayrecord.getFieldValueWithTransform("OPTLINE", 1));
            printwriter.write("\" size=4 maxLength=4 STYLE=\"width: 4em\"></TD>");
            byte0 = 10;
            printwriter.write("\r\n<TD colspan=1>&nbsp;</TD>\r\n<TD NOWRAP colspan=67 rowspan=1><span id='l");
            printwriter.print(s);
            printwriter.write("_MENUOPTY$Unnamed3'  class=\"wf_default wf_field\"  onClick=\"setCursor(20, 12);\" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></TD>");
            byte0 = 78;
            printwriter.write("\r\n</TR>\r\n<TR id=\"l");
            printwriter.print(s);
            printwriter.write("r21\" class=\"trStyle\">\r\n");
            byte0 = 1;
            printwriter.write("\r\n<TD NOWRAP colspan=79 rowspan=1><span id='l");
            printwriter.print(s);
            printwriter.write("_MENUOPTY$Unnamed4'  class=\"wf_default wf_field\"  onClick=\"setCursor(21, 2);\" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></TD>");
            byte0 = 80;
            printwriter.write("\r\n</TR>\r\n<TR id=\"l");
            printwriter.print(s);
            printwriter.write("r22\" class=\"trStyle\">\r\n");
            byte0 = 1;
            printwriter.write("\r\n<TD NOWRAP colspan=77 rowspan=1><span id='l");
            printwriter.print(s);
            printwriter.write("_MENUOPTY$Unnamed5'  class=\"wf_blue wf_field\"  onClick=\"setCursor(22, 2);\" ></span></TD>");
            byte0 = 78;
            printwriter.write("\r\n</TR>\r\n<TR id=\"l");
            printwriter.print(s);
            printwriter.write("r23\" class=\"trStyle\">\r\n");
            byte0 = 1;
            printwriter.write("\r\n");
            if(idisplayrecord.isFieldVisible("Unnamed1"))
            {
                printwriter.write("\r\n<TD NOWRAP colspan=79 rowspan=1><span id='l");
                printwriter.print(s);
                printwriter.write("_MENUOPTY$Unnamed1'  class=\"wf_blue wf_field\"  onClick=\"setCursor(23, 2);\" ></span></TD>");
                byte0 = 80;
                printwriter.write("\r\n");
            }
            printwriter.write("\r\n");
            if(idisplayrecord.isFieldVisible("Unnamed0"))
            {
                printwriter.write("\r\n");
                int j = 2 - byte0 - 1;
                printwriter.write("\r\n");
                if(j > 0)
                {
                    printwriter.write("\r\n<TD colspan=");
                    printwriter.print(j);
                    printwriter.write(">&nbsp;</TD>\r\n");
                }
                printwriter.write("\r\n");
                printwriter.write("\r\n<TD NOWRAP colspan=79 rowspan=1><span id='l");
                printwriter.print(s);
                printwriter.write("_MENUOPTY$Unnamed0'  class=\"wf_default wf_field\"  onClick=\"setCursor(23, 2);\" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></TD>");
                byte0 = 80;
                printwriter.write("\r\n");
            }
            printwriter.write("\r\n");
            if(byte0 == 1)
                printwriter.write("\r\n<TD>&nbsp;</TD>\r\n");
            printwriter.write("\r\n</TR>\r\n<TR id=\"l");
            printwriter.print(s);
            printwriter.write("r24\" class=\"trStyle\">\r\n");
            byte0 = 1;
            printwriter.write("\r\n<TD NOWRAP colspan=78 rowspan=1><span id='l");
            printwriter.print(s);
            printwriter.write("_MENUOPTY$MSGLINE'  class=\"wf_hi wf_white wf_field\"  >");
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
