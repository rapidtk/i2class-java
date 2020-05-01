// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.httpcontroller;

import com.ibm.as400ad.webfacing.common.*;
import com.ibm.as400ad.webfacing.runtime.controller.ErrorHandler;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.runtime.host.HostJobInfo;
import com.ibm.as400ad.webfacing.runtime.view.IScreenBuilder;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import javax.servlet.*;
import javax.servlet.http.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.httpcontroller:
//            IHttpSessionVariable

public class HttpRequestHandler
    implements IHttpSessionVariable
{

    public HttpRequestHandler(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, ServletContext servletcontext)
    {
        _request = httpservletrequest;
        _response = httpservletresponse;
        _servletContext = servletcontext;
        _session = _request.getSession();
    }

    public ErrorHandler getErrorHandler()
    {
        return new ErrorHandler(_servletContext, _request, _response, _trace);
    }

    public ErrorHandler getNestedErrorHandler()
    {
        return new ErrorHandler(_servletContext, _request, _response, _trace, true);
    }

    protected static Object getSessionValue(HttpSession httpsession, String s)
    {
        Object obj = null;
        try
        {
            obj = httpsession.getAttribute(s);
        }
        catch(Throwable throwable)
        {
            WFSession.getTraceLogger().err(3, "Get of value failed with exception: " + throwable);
            obj = null;
        }
        return obj;
    }

    protected static void putSessionValue(HttpSession httpsession, String s, Object obj)
    {
        try
        {
            if(null != obj)
                httpsession.setAttribute(s, obj);
            else
                httpsession.removeAttribute(s);
        }
        catch(Throwable throwable)
        {
            WFSession.getTraceLogger().err(3, "Put of value (" + s + ") with value (" + obj + ") failed with exception: " + throwable);
        }
    }

    protected static boolean showVisibleLabelsOnly(ServletContext servletcontext)
    {
        String s = servletcontext.getInitParameter(WFAppProperties.WFSHOWVISIBLELABELSONLY);
        return null != s && s.equalsIgnoreCase("true");
    }

    public static void updateWFSession(HttpSession httpsession, ServletContext servletcontext)
    {
        WFSession.setTraceLogger((ITraceLogger)getSessionValue(httpsession, "traceLogger"), servletcontext.getRealPath("/") + FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "trace");
        WFSession.setJobInfoRequestor((HostJobInfo)getSessionValue(httpsession, "HostJobInfo"));
        WFSession.setScreenBuilderModel((IScreenBuilder)getSessionValue(httpsession, "screenbuilder"));
        if(getSessionValue(httpsession, "InputCharMapping") != null)
            WFSession.setInputCharMappingProperties((MappingProperties)getSessionValue(httpsession, "InputCharMapping"));
        if(getSessionValue(httpsession, "OutputCharMapping") != null)
            WFSession.setOutputCharMappingProperties((MappingProperties)getSessionValue(httpsession, "OutputCharMapping"));
    }

    public void processEndOfApplication()
        throws WebfacingInternalException
    {
        try
        {
            _response.setContentType("text/html");
            PrintWriter printwriter = _response.getWriter();
            String s = (String)getSessionValue(_session, "RefererPage");
            printwriter.print("<html>");
            if(s == null)
                s = new String("javascript:self.close()");
            printwriter.println("<script language='JavaScript'> ");
            printwriter.println(" function exitApp() { ");
            if(_request.getParameter("logon") != null && _request.getParameter("logon").toLowerCase().equals("cancel"))
            {
                printwriter.println("try{parent.opener.closeWinListner();}catch(anyexp){}");
            } else
            {
                printwriter.println("var s='try{parent.opener.closeWinListner();}catch(anyexp){}'");
                printwriter.println("   try {parent.main.onunload=new Function(s);}catch(anyexp){}");
            }
            printwriter.println("   parent.location.href ='" + s + "';");
            printwriter.println("}</script>");
            printwriter.println("<body onload=\"exitApp();\"></body></html>");
        }
        catch(Exception exception)
        {
            throw new WebfacingInternalException(_resmri.getString("WF0035"));
        }
    }

    public static void removeWebAppSessionAttributes(HttpSession httpsession)
    {
        httpsession.removeAttribute("ApplicationTitle");
        httpsession.removeAttribute("HostJobInfo");
        httpsession.removeAttribute("traceLogger");
        httpsession.removeAttribute("ImmediateWriteRequest");
        httpsession.removeAttribute("FixedHeightForEachRow");
        httpsession.removeAttribute("WWidth");
        httpsession.removeAttribute("screenbuilder");
        httpsession.removeAttribute("FirstScreen");
        httpsession.removeAttribute("WFConnection");
        httpsession.removeAttribute("AdminEmail");
        httpsession.removeAttribute("controllerState");
        httpsession.removeAttribute("errorBean");
        httpsession.removeAttribute("ErrorJSPDetailLevel");
        httpsession.removeAttribute("DebugHostApplication");
        httpsession.removeAttribute("FORCE_UTF8");
        httpsession.removeAttribute("Invocation");
        httpsession.removeAttribute("appHelpTable");
        httpsession.removeAttribute("FORCE_DFRWRT");
        httpsession.removeAttribute("clcommand_from_program_call");
        httpsession.removeAttribute("clcommand_from_inv");
        httpsession.removeAttribute("pageId");
        httpsession.removeAttribute("INSERTMODE");
        httpsession.removeAttribute("InputCharMapping");
        httpsession.removeAttribute("OutputCharMapping");
        httpsession.removeAttribute("useCmdJsp");
        httpsession.removeAttribute("FIELDEXITKEYCODE");
    }

    public static void removeWFSessionAttributes(HttpSession httpsession)
    {
        removeWebAppSessionAttributes(httpsession);
        httpsession.removeAttribute("USERID");
        httpsession.removeAttribute("PWD");
        httpsession.removeAttribute("ChallengeIssued");
    }

    static final String copyRight = new String("(c) Copyright IBM Corporation 1999-2002, all rights reserved.");
    protected static ResourceBundle _resmri;
    public static String FILE_SEPARATOR = System.getProperty("file.separator");
    protected HttpServletRequest _request;
    protected HttpServletResponse _response;
    protected ServletContext _servletContext;
    protected HttpSession _session;
    protected ITraceLogger _trace;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
