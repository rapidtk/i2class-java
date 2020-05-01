// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.dhtmlview;

import com.ibm.as400ad.webfacing.runtime.controller.ErrorHandler;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.runtime.httpcontroller.HttpRequestHandler;
import com.ibm.as400ad.webfacing.runtime.view.*;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.dhtmlview:
//            ClientScriptBuilderHelper, DefaultScrollbarBean, HTMLStringTransform, IScrollbarBean

class ScreenBuilderHelper extends HttpRequestHandler
{

    public ScreenBuilderHelper(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, ServletContext servletcontext)
        throws IOException
    {
        super(httpservletrequest, httpservletresponse, servletcontext);
        super._trace = WFSession.getTraceLogger();
        try
        {
            _out = httpservletresponse.getWriter();
        }
        catch(IOException ioexception)
        {
            getNestedErrorHandler().handleError(ioexception, HttpRequestHandler._resmri.getString("WF0120"));
        }
    }

    protected void buildScreen()
        throws IOException, ServletException
    {
        try
        {
            super._response.setContentType("text/html");
            if(super._session.getAttribute("errorBean") != null)
                return;
            _out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"styles/apparea/apparea.css\">");
            ClientScriptBuilderHelper clientscriptbuilderhelper = new ClientScriptBuilderHelper(super._request, super._response, super._servletContext, super._trace);
            clientscriptbuilderhelper.buildClientScript();
            _out.println("<form  id=\"SCREEN\" name=\"SCREEN\" onSubmit=\"return false;\">");
            IScreenBuilder iscreenbuilder = (IScreenBuilder)super._session.getAttribute("screenbuilder");
            int i = iscreenbuilder.getMaxRow();
            int j = iscreenbuilder.getMaxColumn();
            ArrayList arraylist = iscreenbuilder.getRecordLayersOnDevice();
            Iterator iterator = arraylist.iterator();
            int k = 0;
            boolean flag = false;
            boolean flag1 = false;
            boolean flag3 = false;
            Object obj = null;
            if(iterator.hasNext())
            {
                IDeviceLayer idevicelayer = (IDeviceLayer)arraylist.get(arraylist.size() - 1);
                if(idevicelayer.isWindowed())
                    flag3 = true;
                while(iterator.hasNext()) 
                {
                    IDeviceLayer idevicelayer1 = (IDeviceLayer)iterator.next();
                    if(k == 0 && idevicelayer1.isVerticallyPositioned())
                    {
                        if(!flag3)
                            flag1 = true;
                        generatePositioningTable(i, j, flag1);
                        flag = true;
                    }
                    if(idevicelayer1.isWindowed() || idevicelayer1.isCLRLWindow())
                    {
                        if(idevicelayer1.isWindowed() && !iterator.hasNext())
                            flag1 = true;
                        generateWindow(idevicelayer1, k++, j, flag1);
                    } else
                    {
                        if(!flag3)
                            flag1 = true;
                        generateTable(idevicelayer1, k++, i, j, flag, flag1);
                    }
                }
            } else
            {
                boolean flag2 = true;
                generatePositioningTable(i, j, flag2);
            }
            _out.print("<INPUT type=\"hidden\" maxlength=\"30\" name=\"CURSOR\" id=\"CURSOR\">");
            _out.print("<INPUT type=\"hidden\" maxlength=\"30\" name=\"PAGEID\" id=\"PAGEID\">");
        }
        catch(WebfacingInternalException webfacinginternalexception)
        {
            super._trace.err(1, webfacinginternalexception, "WebfacingInternalException within ScreenBuilderHelper.buildScreen() : ");
            (new ErrorHandler(super._servletContext, super._request, super._response, super._trace, true)).handleError(webfacinginternalexception, HttpRequestHandler._resmri.getString("WF0084"));
        }
        catch(Exception exception)
        {
            if(super._response.isCommitted())
            {
                super._trace.err(2, "Exception within ScreenBuilderHelper.buildScreen() : response already committed");
            } else
            {
                super._trace.err(1, exception, "Exception within ScreenBuilderHelper.buildScreen() : ");
                (new ErrorHandler(super._servletContext, super._request, super._response, super._trace, true)).handleError(exception, HttpRequestHandler._resmri.getString("WF0085"));
            }
        }
        catch(Throwable throwable)
        {
            super._trace.err(1, throwable, "Throwable within ScreenBuilderHelper.buildScreen() : ");
            (new ErrorHandler(super._servletContext, super._request, super._response, super._trace, true)).handleError(throwable, HttpRequestHandler._resmri.getString("WF0085"));
        }
        finally
        {
            _out.print("</form>");
            _out.print(ErrorHandler.getRedirectJavascript(super._session));
        }
    }

    private void generatePositioningTable(int i, int j, boolean flag)
        throws IOException
    {
        _out.print("<TABLE  class=\"wf_font\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
        generateTRForPositioning(0, 1, j);
        for(int k = 1; k <= i; k++)
        {
            _out.print("<TR id=\"l0r" + k + "\" class=\"trStyle\">");
            _out.print("<TD>&nbsp;</TD>");
            _out.print("</TR>");
        }

        if(flag)
            includeMessageLine(j - 1);
        _out.println("</TABLE>");
    }

    private void generateTable(IDeviceLayer idevicelayer, int i, int j, int k, boolean flag, boolean flag1)
        throws IOException, WebfacingInternalException, ServletException
    {
        if(i == 0 && !flag)
        {
            _out.print("<TABLE class=\"wf_font\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
        } else
        {
            String s = idevicelayer.name();
            if(s == null)
                s = "ClearedLinesLayer";
            s = s + String.valueOf(i + 1);
            if(idevicelayer.isVerticallyPositioned())
                _out.println("<TABLE class=\"wf_font\" id=\"" + s + "\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"position: absolute; z-index:" + (i + 1) + "; visibility:hidden; background-image:expression(document.body.currentStyle.backgroundImage); background-color:expression(document.body.currentStyle.backgroundColor); background-repeat:expression(document.body.currentStyle.backgroundRepeat); background-attachment: expression(document.body.currentStyle.backgroundAttachment)\">");
            else
                _out.println("<TABLE class=\"wf_font\" id=\"" + s + "\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"position: absolute; z-index:" + (i + 1) + "; visibility:hidden\">");
        }
        int l = idevicelayer.getLastColumn();
        if(k < l)
            l = k;
        generateTRForPositioning(i + 1, idevicelayer.getFirstColumn(), l);
        int i1 = idevicelayer.getFirstRow() - 1;
        Iterator iterator = idevicelayer.getRectanglesIterator();
        Object obj = null;
        while(iterator.hasNext()) 
        {
            IVisibleRectangle ivisiblerectangle = (IVisibleRectangle)iterator.next();
            int j1 = ivisiblerectangle.getFirstFieldLine();
            if(j1 != -1)
            {
                if(j1 > i1 + 1)
                    _out.println(insertBlankLines(j1 - (i1 + 1), i + 1, i1));
                if(ivisiblerectangle instanceof IBuildRecordViewBean)
                    includeRecordJSP((IBuildRecordViewBean)ivisiblerectangle, i + 1);
                else
                    _out.println(insertBlankLines((ivisiblerectangle.getLastFieldLine() - j1) + 1, i + 1, j1 - 1));
                i1 = ivisiblerectangle.getLastFieldLine();
            }
        }
        if(!idevicelayer.isVerticallyPositioned() && j > i1)
            _out.println(insertBlankLines(j - i1, i + 1, i1));
        if(i == 0 && !flag && flag1)
            includeMessageLine(k - 1);
        _out.println("</TABLE>");
    }

    private void generateTRForPositioning(int i, int j, int k)
    {
        _out.println("<TR height=\"0\" style=\"line-height:0%;\">");
        for(; j <= k; j++)
            if(i >= 0)
                _out.print("<TD id=\"l" + i + "c" + j + "\">&nbsp;</TD>");
            else
                _out.print("<TD>&nbsp;</TD>");

        _out.println("</TR>");
    }

    private void generateWindow(IDeviceLayer idevicelayer, int i, int j, boolean flag)
        throws IOException, WebfacingInternalException, ServletException
    {
        String s = idevicelayer.name() + String.valueOf(i + 1);
        boolean flag1 = idevicelayer.isCLRLWindow();
        String s1 = idevicelayer.getWindowTitle();
        if(!s1.equals(""))
            s1 = HTMLStringTransform.transformUnquotedString(s1);
        String s2 = idevicelayer.getWindowTitleAlignment();
        String s3 = "";
        if(!flag1 && !s2.equals(""))
            if(s2.equals("*LEFT"))
                s3 = "style=\"text-align:left\"";
            else
            if(s2.equals("*RIGHT"))
                s3 = "style=\"text-align:right\"";
        _out.println("<DIV class=\"wdwDef\" id=\"" + s + "\" style=\"z-index:" + (i + 1) + "; visibility:hidden\">");
        _out.println("<TABLE class=\"wf_font\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
        _out.println("<TR><TD class=\"wdwTitleBar\" colspan=\"100%\" " + s3 + " onMouseOver=\"mouseOver(" + s + ");\" onMouseOut=\"mouseOut();\">&nbsp;&nbsp;" + s1 + "&nbsp;&nbsp;</TD></TR>");
        _out.println("<TR><TD class=\"wdwLeft\">&nbsp;</TD><TD>&nbsp;</TD>");
        _out.println("<TD><TABLE class=\"wf_font\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
        int k = 0;
        boolean flag2 = false;
        Iterator iterator = idevicelayer.getRectanglesIterator();
        Object obj = null;
        IBuildRecordViewBean ibuildrecordviewbean = null;
        while(iterator.hasNext()) 
        {
            IVisibleRectangle ivisiblerectangle = (IVisibleRectangle)iterator.next();
            if(ivisiblerectangle instanceof IBuildRecordViewBean)
            {
                ibuildrecordviewbean = (IBuildRecordViewBean)ivisiblerectangle;
                if(!flag2)
                {
                    if(flag1)
                        generateTRForPositioning(-1, 1, idevicelayer.getLastColumn() - idevicelayer.getFirstColumn());
                    else
                        generateTRForPositioning(-1, 1, ibuildrecordviewbean.getWdwWidth());
                    flag2 = true;
                }
                int l = ibuildrecordviewbean.getFirstFieldLine();
                if(l != -1)
                {
                    if(!flag1 && l > k + 1)
                        _out.println(insertBlankLines(l - (k + 1), i + 1, k));
                    includeRecordJSP(ibuildrecordviewbean, i + 1);
                    k = ibuildrecordviewbean.getLastFieldLine();
                }
            }
        }
        if(!flag1)
        {
            if(flag)
            {
                _out.println(insertBlankLines(ibuildrecordviewbean.getWdwHeight() - k - 1, i + 1, k));
                includeMessageLine(j - 1);
            } else
            {
                _out.println(insertBlankLines(ibuildrecordviewbean.getWdwHeight() - k, i + 1, k));
            }
            _out.print("</TABLE></TD><TD>&nbsp;</TD><TD class=\"wdwRight\">&nbsp;</TD></TR>");
            _out.print("<TR><TD class=\"wdwBotLeft\">&nbsp;</TD><TD class=\"wdwBot\" colspan=\"3\">&nbsp;</TD><TD class=\"wdwBotRight\">&nbsp;</TD></TR>");
        } else
        {
            _out.print("</TABLE></TD><TD>&nbsp;</TD><TD class=\"wdwRight\">&nbsp;</TD></TR>");
        }
        _out.println("</TABLE></DIV>");
    }

    private IScrollbarBean getScrollbarBean(IBuildSFLCTLViewBean ibuildsflctlviewbean)
    {
        DefaultScrollbarBean defaultscrollbarbean = new DefaultScrollbarBean();
        defaultscrollbarbean.setControlRecordViewBean(ibuildsflctlviewbean);
        defaultscrollbarbean.setContext(super._session);
        return defaultscrollbarbean;
    }

    private void includeMessageLine(int i)
    {
        _out.println("<TR id=\"MSGLINE$$$COMBOBOX\" style=\"visibility:hidden\">");
        _out.println("<TD colspan=\"" + i + "\"><INPUT class=\"cbField\" id=\"MSGLINE$$$cbField\" onClick=\"this.comboBoxField.showList();\" READONLY TABINDEX=\"-1\" style=\"width:100%\"></TD>");
        _out.println("<TD class=\"cbButton\"><IMG src=\"styles/transparent.gif\" width='16' height='16' id=\"MSGLINE$$$cbButton\" onClick=\"this.comboBoxButton.showList();\"></TD></TR>");
    }

    private void includeRecordJSP(IBuildRecordViewBean ibuildrecordviewbean, int i)
        throws IOException, WebfacingInternalException, ServletException
    {
        String s = ibuildrecordviewbean.getRecordName();
        if(ibuildrecordviewbean instanceof IBuildSFLCTLViewBean)
        {
            super._session.setAttribute(s, ((IBuildSFLCTLViewBean)ibuildrecordviewbean).getDisplaySFLCTLRecord());
            super._request.setAttribute(s, ((IBuildSFLCTLViewBean)ibuildrecordviewbean).getDisplaySFLCTLRecord());
        } else
        {
            super._session.setAttribute(s, ibuildrecordviewbean.getDisplayRecord());
            super._request.setAttribute(s, ibuildrecordviewbean.getDisplayRecord());
        }
        if(ibuildrecordviewbean instanceof IBuildSFLCTLViewBean)
        {
            IScrollbarBean iscrollbarbean = getScrollbarBean((IBuildSFLCTLViewBean)ibuildrecordviewbean);
            super._session.setAttribute(s + "$" + "Scrollbar", iscrollbarbean);
            super._request.setAttribute(s + "$" + "Scrollbar", iscrollbarbean);
        }
        String s1 = ibuildrecordviewbean.getJspName();
        try
        {
            super._request.setAttribute("caller", "SBH");
            super._servletContext.getRequestDispatcher(s1).include(super._request, super._response);
            super._request.removeAttribute("caller");
        }
        catch(Exception exception)
        {
            super._trace.err(1, exception, "Exception within ScreenBuilderHelper.includeRecordJSP() : " + s1);
        }
        catch(Throwable throwable)
        {
            super._trace.err(1, throwable, "Throwable within ScreenBuilderHelper.includeRecordJSP() : " + s1);
        }
        super._session.removeAttribute(s);
        super._session.removeAttribute(s + "$" + "Scrollbar");
    }

    private String insertBlankLines(int i, int j, int k)
    {
        StringBuffer stringbuffer = new StringBuffer();
        for(int l = 1; l <= i; l++)
            stringbuffer.append("<TR id=\"l" + Integer.toString(j) + "r" + Integer.toString(k + l) + "\" class=\"trStyle\"><TD>&nbsp;</TD></TR>\n");

        return stringbuffer.toString();
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 1999-2003, all rights reserved");
    private PrintWriter _out;

}
