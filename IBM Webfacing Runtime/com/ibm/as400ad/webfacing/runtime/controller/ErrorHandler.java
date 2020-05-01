// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.controller;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.httpcontroller.IHttpSessionVariable;
import com.ibm.as400ad.webfacing.runtime.view.ErrorBean;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.*;
import java.util.ResourceBundle;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.*;

public class ErrorHandler
    implements IHttpSessionVariable
{

    public ErrorHandler(ServletContext servletcontext, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, ITraceLogger itracelogger)
    {
        _errorJSPDetailLevel = 3;
        _isNestedPage = false;
        _servletContext = servletcontext;
        _session = httpservletrequest.getSession();
        _request = httpservletrequest;
        _response = httpservletresponse;
        _trace = itracelogger;
        Integer integer = (Integer)_session.getAttribute("ErrorJSPDetailLevel");
        if(integer != null)
            _errorJSPDetailLevel = integer.intValue();
    }

    public ErrorHandler(ServletContext servletcontext, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, ITraceLogger itracelogger, boolean flag)
    {
        this(servletcontext, httpservletrequest, httpservletresponse, itracelogger);
        setIsNestedPage(flag);
    }

    private int getErrorJSPDetailLevel()
    {
        return _errorJSPDetailLevel;
    }

    private String getErrorPageDir()
    {
        return _servletContext.getRealPath("/") + System.getProperty("file.separator") + WebfacingConstants.STYLES_DIR;
    }

    public static String getRedirectJavascript(HttpSession httpsession)
    {
        if(httpsession.getAttribute("errorBean") != null)
            return "<script language='JavaScript'>location.replace('styles/error.jsp')</script>";
        else
            return "";
    }

    public void handleError(Exception exception, String s, String s1)
    {
        ErrorBean errorbean = new ErrorBean(exception);
        errorbean.extractExceptionInfo();
        errorbean.setDetailLevel(_errorJSPDetailLevel);
        if(s != null)
            errorbean.setEndUserMessage(s);
        if(s1 != null)
            errorbean.setAdminMessage(s1);
        issueError(errorbean);
    }

    public void handleError(String s)
    {
        ErrorBean errorbean = new ErrorBean(s);
        issueError(errorbean);
    }

    public void handleError(String s, String s1)
    {
        ErrorBean errorbean = new ErrorBean(s, s1);
        issueError(errorbean);
    }

    private boolean isNestedPage()
    {
        return _isNestedPage;
    }

    private void issueError(ErrorBean errorbean)
    {
        errorbean.setDetailLevel(_errorJSPDetailLevel);
        _session.setAttribute("errorBean", errorbean);
        if(!isNestedPage())
        {
            File file = new File(getErrorPageDir() + "error.jsp");
            if(file.exists())
                try
                {
                    PrintWriter printwriter = _response.getWriter();
                    _response.setContentType("text/html");
                    printwriter.println("<script language='JavaScript'>location.replace('styles/error.jsp')</script>");
                }
                catch(Exception exception)
                {
                    _trace.err(1, exception, "Exception while forwarding to error servlet in ErrorHandler.issueError() : ");
                }
            else
                try
                {
                    PrintWriter printwriter1 = _response.getWriter();
                    _response.setContentType("text/html");
                    printwriter1.println("<HTML>");
                    printwriter1.println("<HEAD>");
                    printwriter1.println("<script language='JavaScript'> ");
                    printwriter1.println(" closeWindow = function() { ");
                    printwriter1.println(" if (window.event.clientX < 0){");
                    printwriter1.println(" try{window.opener.closeWinListner();}catch(anyexp){}");
                    printwriter1.println("}}</script>");
                    printwriter1.println("<TITLE>" + _resmri.getString("backup_error_page") + "</TITLE>");
                    printwriter1.println("</HEAD>");
                    printwriter1.println("<BODY onunload=\"closeWindow();\">");
                    printwriter1.println("<H2>" + _resmri.getString("WF0080") + "</H2>");
                    if(null != errorbean.getException())
                    {
                        printwriter1.println("<P>" + _resmri.getString("WF0081") + "<PRE>");
                        errorbean.getException().printStackTrace(printwriter1);
                    } else
                    {
                        printwriter1.println("<P>" + _resmri.getString("WF0082") + "<PRE>");
                        printwriter1.println(errorbean.getEndUserMessage());
                    }
                    printwriter1.println("</PRE></P>");
                    printwriter1.println("</BODY>");
                    printwriter1.println("</HTML>");
                }
                catch(IOException ioexception)
                {
                    _trace.err(1, ioexception, "Exception while retrieving output writer in ErrorHandler.issueError() : ");
                }
        }
    }

    public void setIsNestedPage(boolean flag)
    {
        _isNestedPage = flag;
    }

    public ErrorHandler(ServletContext servletcontext, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, ITraceLogger itracelogger, boolean flag, int i)
    {
        this(servletcontext, httpservletrequest, httpservletresponse, itracelogger);
        setIsNestedPage(flag);
        _errorJSPDetailLevel = i;
    }

    public void handleError(Throwable throwable)
    {
        handleError(throwable, null);
    }

    public void handleError(Throwable throwable, String s)
    {
        handleError(throwable, s, null);
    }

    public void handleError(Throwable throwable, String s, String s1)
    {
        ErrorBean errorbean = new ErrorBean(throwable);
        errorbean.extractExceptionInfo();
        errorbean.setDetailLevel(_errorJSPDetailLevel);
        if(s != null)
            errorbean.setEndUserMessage(s);
        if(s1 != null)
            errorbean.setAdminMessage(s1);
        issueError(errorbean);
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    private ServletContext _servletContext;
    private HttpSession _session;
    private HttpServletRequest _request;
    private HttpServletResponse _response;
    private ITraceLogger _trace;
    private static ResourceBundle _resmri;
    private int _errorJSPDetailLevel;
    private boolean _isNestedPage;
    private static final String ERROR_JSP = "error.jsp";

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
