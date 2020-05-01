// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.qsys.servlets.qddspext;

import com.ibm.as400ad.webfacing.runtime.controller.ErrorHandler;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.dhtmlview.IScrollbarBean;
import com.ibm.as400ad.webfacing.runtime.view.IDisplayRecord;
import com.ibm.as400ad.webfacing.runtime.view.IDisplaySFLCTLRecord;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.*;

public class SCRN2CTL_Servlet extends HttpServlet
{

    public SCRN2CTL_Servlet()
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
                idisplaysflctlrecord = (IDisplaySFLCTLRecord)httpsession.getAttribute("SCRN2CTL");
                if(idisplaysflctlrecord == null)
                    throw new InstantiationException("bean SCRN2CTL  not found within scope ");
            }
            if(!flag);
            printwriter.write("\r\n");
            String s = Integer.toString(idisplaysflctlrecord.getDisplayZIndex());
            int i = Integer.parseInt((String)httpsession.getAttribute("WWidth"));
            boolean flag1 = idisplaysflctlrecord.isProtected();
            printwriter.write("\r\n<INPUT TYPE=\"HIDDEN\" ID =\"l");
            printwriter.print(s);
            printwriter.write("_SCRN2CTL_version\" NAME=\"l");
            printwriter.print(s);
            printwriter.write("_SCRN2CTL_version\" VALUE=501020000>\r\n<TR>\r\n");
            IScrollbarBean iscrollbarbean = null;
            boolean flag2 = false;
            synchronized(httpservletrequest)
            {
                iscrollbarbean = (IScrollbarBean)httpsession.getAttribute("SCRN2CTL$Scrollbar");
                if(iscrollbarbean == null)
                    throw new InstantiationException("bean SCRN2CTL$Scrollbar  not found within scope ");
            }
            if(!flag2);
            printwriter.write("\r\n");
            iscrollbarbean.setScrollbarJavascriptID(idisplaysflctlrecord.getDisplayZIndex(), "SCRN2CTL$scrollbarTable");
            printwriter.write("\r\n<TD colspan='80' height='0' style='line-height:0%; font-size:0pt'>\r\n");
            printwriter.print(iscrollbarbean.getPositioningHTMLSource(80));
            printwriter.write("\r\n</TD>\r\n<TD id='l");
            printwriter.print(s);
            printwriter.write("_SCRN2CTL$scrollbarCell' colspan=1 rowspan=16 cellspacing='0' cellpadding='0'>\r\n<!-- begin of scrollbar --->\r\n");
            printwriter.print(iscrollbarbean.getHTMLSource());
            printwriter.write("\r\n<!-- end of scrollbar --->\r\n</TD>\r\n</TR>\r\n");
            int k = 2;
            printwriter.write("\r\n");
            printwriter.write("\r\n");
            String s2 = null;
            printwriter.write("\r\n");
            for(int l = 1; l <= 15; l++)
            {
                printwriter.write("\r\n");
                String s1 = l % 2 != 1 ? "subfileRecord2" : "subfileRecord1";
                printwriter.write("\r\n<TR id=\"l");
                printwriter.print(s);
                printwriter.write("r");
                printwriter.print(k + 1);
                printwriter.write("\" class=\"");
                printwriter.print(s2 == null ? s1 : s2);
                printwriter.write("\">\r\n");
                int j = 0;
                printwriter.write("\r\n");
                k++;
                printwriter.write("\r\n<TD colspan=1>&nbsp;</TD>\r\n");
                int i1 = l;
                printwriter.write("\r\n");
                int k1 = 1;
                printwriter.write("\r\n");
                if(s2 == null && k1 == 1 && idisplaysflctlrecord.isRecordPastEndOfSubfile(i1))
                    s2 = s1;
                printwriter.write("\r\n");
                j = 0;
                printwriter.write("\r\n");
                if(idisplaysflctlrecord.isActiveRecord(i1))
                {
                    printwriter.write("\r\n<TD NOWRAP colspan=76 rowspan=1><span rowValue=\"");
                    printwriter.print(k);
                    printwriter.write("\" colValue=\"");
                    printwriter.print((76 * (k1 - 1) + 3) - 1);
                    printwriter.write("\" id='l");
                    printwriter.print(s);
                    printwriter.write("_SCRN2CTL$MSGDATA$");
                    printwriter.print(i1);
                    printwriter.write("'  class=\"wf_default wf_field\"  >");
                    printwriter.print(idisplaysflctlrecord.getFieldValueWithTransform("MSGDATA", i1, 2));
                    printwriter.write("</span></TD>");
                    j = 76;
                    printwriter.write("\r\n");
                    if(76 - j > 0)
                    {
                        printwriter.write("\r\n<TD colspan=");
                        printwriter.print(76 - j);
                        printwriter.write(">&nbsp;</TD>\r\n");
                    }
                    printwriter.write("\r\n");
                } else
                {
                    printwriter.write("\r\n<TD colspan=76>&nbsp;</TD>\r\n");
                    j += 76;
                    printwriter.write("\r\n");
                }
                printwriter.write("\r\n");
                printwriter.write("\r\n<TD colspan=3>&nbsp;</TD>\r\n</TR>\r\n");
            }

            printwriter.write("\r\n<TR><TD id='l");
            printwriter.print(s);
            printwriter.write("_SCRN2CTL$cellBelowScrollbar'></TD></TR>\r\n");
            for(int j1 = 1; j1 <= 15; j1++)
                printwriter.write("\r\n");

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
