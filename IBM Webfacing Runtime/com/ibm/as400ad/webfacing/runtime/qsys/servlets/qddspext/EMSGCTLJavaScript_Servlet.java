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

public class EMSGCTLJavaScript_Servlet extends HttpServlet
{

    public EMSGCTLJavaScript_Servlet()
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
                idisplaysflctlrecord = (IDisplaySFLCTLRecord)httpsession.getAttribute("EMSGCTL");
                if(idisplaysflctlrecord == null)
                    throw new InstantiationException("bean EMSGCTL  not found within scope ");
            }
            if(!flag);
            printwriter.write("\r\n");
            int i = idisplaysflctlrecord.getDisplayZIndex();
            printwriter.write("\r\n");
            printwriter.write("\r\n ");
            byte byte0 = 4;
            int j = idisplaysflctlrecord.getLastRecordNumber();
            if(j > 0)
            {
                printwriter.write("\r\n var allItems = new Array();\r\n var itemIndex = -1;\r\n ");
                int k = idisplaysflctlrecord.getRRN();
                int l = 0;
                int i1 = 0;
                for(int j1 = 1; j1 <= j; j1++)
                    if(idisplaysflctlrecord.isActiveRecord((j1 - k) + 1))
                    {
                        if(k == j1)
                        {
                            printwriter.write("\r\n itemIndex = ");
                            printwriter.print(l);
                            printwriter.write(";\r\n ");
                        }
                        String s = idisplaysflctlrecord.getFieldValueWithTransform("MSGDATA", (j1 - k) + 1, byte0);
                        printwriter.write("\r\n allItems[");
                        printwriter.print(l);
                        printwriter.write("]=\"");
                        printwriter.print(s);
                        printwriter.write("\";\r\n ");
                        l++;
                        if(i1 < s.length())
                            i1 = s.length();
                    }

                if(i1 != 0)
                {
                    printwriter.write("\r\ndocument.SCREEN.l");
                    printwriter.print(i);
                    printwriter.write("_EMSGCTL$$cbField.size = ");
                    printwriter.print(i1);
                    printwriter.write(";\r\n");
                }
                printwriter.write("\r\n document.SCREEN.l");
                printwriter.print(i);
                printwriter.write("_EMSGCTL$$cbField.comboBoxField = new ComboBoxField(document.SCREEN.l");
                printwriter.print(i);
                printwriter.write("_EMSGCTL$$cbField, document.SCREEN.l");
                printwriter.print(i);
                printwriter.write("_EMSGCTL$$cbButton);\r\n document.SCREEN.l");
                printwriter.print(i);
                printwriter.write("_EMSGCTL$$cbButton.comboBoxButton = new ComboBoxButton(document.SCREEN.l");
                printwriter.print(i);
                printwriter.write("_EMSGCTL$$cbField);\r\n l");
                printwriter.print(i);
                printwriter.write("_EMSGCTL$$cbList.comboBoxList = new ComboBoxList(l");
                printwriter.print(i);
                printwriter.write("r24, document.SCREEN.l");
                printwriter.print(i);
                printwriter.write("_EMSGCTL$$cbField, l");
                printwriter.print(i);
                printwriter.write("_EMSGCTL$$cbList, allItems, itemIndex);\r\n ");
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
