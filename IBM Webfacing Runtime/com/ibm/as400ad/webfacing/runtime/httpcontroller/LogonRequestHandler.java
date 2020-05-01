// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.httpcontroller;

import com.ibm.as400ad.webfacing.common.*;
import com.ibm.as400ad.webfacing.runtime.controller.ErrorHandler;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.runtime.host.*;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import com.ibm.as400ad.webfacing.util.TraceLoggerProxy;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import javax.servlet.*;
import javax.servlet.http.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.httpcontroller:
//            HttpRequestHandler

public class LogonRequestHandler extends HttpRequestHandler
{

    public LogonRequestHandler(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, ServletContext servletcontext)
    {
        super(httpservletrequest, httpservletresponse, servletcontext);
        initializeSession();
        manageTrace();
        HttpRequestHandler.updateWFSession(super._session, servletcontext);
    }

    public void handleRequest()
        throws IOException, ServletException
    {
        try
        {
            if(HttpRequestHandler.getSessionValue(super._session, "WFConnection") != null && HttpRequestHandler.getSessionValue(super._session, "Invocation") != null && (super._request.getParameter("inv") != null && super._request.getParameter("inv").equals(HttpRequestHandler.getSessionValue(super._session, "Invocation")) || super._request.getParameter("inv") == null && super._request.getParameter("timestamp") != null) || HttpRequestHandler.getSessionValue(super._session, "WFConnection") != null && HttpRequestHandler.getSessionValue(super._session, "clcommand_from_program_call") != null && (super._request.getParameter("clcmd") != null && super._request.getParameter("clcmd").equals(HttpRequestHandler.getSessionValue(super._session, "clcommand_from_program_call")) || super._request.getParameter("clcmd") == null && super._request.getParameter("timestamp") != null))
            {
                HttpRequestHandler.putSessionValue(super._session, "controllerState", new Integer(5));
                HttpRequestHandler.putSessionValue(super._session, "FirstScreen", null);
                super._servletContext.getRequestDispatcher("/WebFacing").forward(super._request, super._response);
            } else
            if(initializeInvocation((WFConnection)HttpRequestHandler.getSessionValue(super._session, "WFConnection")))
            {
                HttpRequestHandler.putSessionValue(super._session, "controllerState", new Integer(1));
                super._servletContext.getRequestDispatcher("/WebFacing").forward(super._request, super._response);
            }
        }
        catch(Exception exception)
        {
            String s = WebfacingConstants.replaceSubstring(HttpRequestHandler._resmri.getString("WF0117"), "&2", "ControllerServlet");
            s = WebfacingConstants.replaceSubstring(s, "&1", "LogonServlet");
            if(!super._response.isCommitted())
                getErrorHandler().handleError(exception, s);
        }
        finally
        {
            WFSession.clearSessionData();
        }
    }

    private void promptForChallenge()
        throws WebfacingInternalException
    {
        WFAppProperties wfappproperties = (WFAppProperties)super._servletContext.getAttribute(WFAppProperties.WFAPPPROPERTIES);
        boolean flag = !wfappproperties.useUserDefinedLogon();
        String s = wfappproperties.getLogonPageName();
        if(!flag && s != null)
            try
            {
                super._servletContext.getRequestDispatcher(s).forward(super._request, super._response);
            }
            catch(Exception exception)
            {
                flag = true;
            }
        if(flag)
        {
            PrintWriter printwriter;
            try
            {
                super._response.setContentType("text/html;charset=UTF-8");
                printwriter = super._response.getWriter();
            }
            catch(IOException ioexception)
            {
                throw new WebfacingInternalException(WebfacingConstants.replaceSubstring(HttpRequestHandler._resmri.getString("WF0024"), "&1", "LogonRequestHandler.promptForChallenge()"));
            }
            printwriter.print("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\">");
            printwriter.print("<html>");
            printwriter.print("<head>");
            printwriter.print("<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
            printwriter.print("<script language=\"javascript\">");
            printwriter.print("function submitLogon(name)");
            printwriter.print("{");
            printwriter.print("if (typeof(alreadySubmitLogon)==\"undefined\" )");
            printwriter.print("{");
            printwriter.print("document.logon_form.logon.value=name;");
            printwriter.print("document.logon_form.submit();");
            printwriter.print("alreadySubmitLogon=\"yes\";");
            printwriter.print("}");
            printwriter.println("}");
            printwriter.print("document.onkeydown=function key_handler()");
            printwriter.print("{");
            printwriter.print("var key=window.event.keyCode;");
            printwriter.print("if (key==13)");
            printwriter.print("{");
            printwriter.print("submitLogon(\"logon\");");
            printwriter.print("return false;");
            printwriter.println("}");
            printwriter.println("}");
            printwriter.print(" closeWindow = function() { ");
            printwriter.print(" if (window.event.clientX < 0){");
            printwriter.print(" try{window.opener.closeWinListner();}catch(anyexp){}");
            printwriter.println("}}");
            printwriter.print("</script>");
            printwriter.print("<title>" + HttpRequestHandler._resmri.getString("Logon") + "</title>");
            printwriter.print("</head>");
            printwriter.print("<body onLoad=\"document.logon_form.userid.focus();\" onunload=\"closeWindow();\">");
            printwriter.print("<center>");
            printwriter.print("<form name=\"logon_form\" method=\"post\" action=\"WFLogon\">");
            printwriter.print("<center>");
            printwriter.print("<table border=\"1\" cellspacing=\"0\" cellpadding=\"10\" bgcolor=\"#ccccff\">");
            printwriter.print("<tr valign=\"middle\" align=\"center\">");
            printwriter.print("<td width=\"258\" height=\"139\"><font color=\"#000000\" size=\"-1\">");
            printwriter.print(HttpRequestHandler._resmri.getString("Please_type_pwd_userid") + "</font>");
            printwriter.println("<table width=\"200\" border=\"0\" cellspacing=\"3\" cellpadding=\"3\">");
            printwriter.print("<tr>");
            printwriter.print("<td align=\"right\"><font color=\"#000000\" size=\"-1\">" + HttpRequestHandler._resmri.getString("User_Name") + "</font></td>");
            printwriter.print("<td><input type=\"text\" name=\"userid\" size=\"10\" maxlength=\"10\"></td>");
            printwriter.print("</tr>");
            printwriter.print("<tr>");
            printwriter.print("<td align=\"right\"><font color=\"#000000\" size=\"-1\">" + HttpRequestHandler._resmri.getString("Password") + "</font></td>");
            printwriter.print("<td><input type=\"password\" name=\"password\" size=\"10\" maxlength=\"128\"></td>");
            printwriter.print("</tr><tr>");
            printwriter.print("<td align=\"right\" colspan=\"2\"><input type=\"button\"  value=\"" + HttpRequestHandler._resmri.getString("Logon") + "\" onclick =\"submitLogon('logon');\">&nbsp;&nbsp;<input type=\"button\"  value=\"" + HttpRequestHandler._resmri.getString("Cancel") + "\" onclick =\"submitLogon('cancel');\"></td>");
            printwriter.print("</tr>");
            printwriter.print("</table></td></tr>");
            printwriter.print("</table></center>");
            printwriter.print("<input type=\"hidden\" name=\"logon\">");
            long l = System.currentTimeMillis();
            printwriter.print("<input type=\"hidden\" name=\"timestamp\" value=\"" + l + "\">");
            printwriter.print("</form>");
            printwriter.print("</center>");
            printwriter.print("</body>");
            printwriter.println("</html>");
        }
        HttpRequestHandler.putSessionValue(super._session, "ChallengeIssued", new Boolean(true));
    }

    private boolean initializeInvocation(WFConnection wfconnection)
    {
        boolean flag = true;
        try
        {
            WFAppProperties wfappproperties = (WFAppProperties)super._servletContext.getAttribute(WFAppProperties.WFAPPPROPERTIES);
            if(!wfappproperties.ignoreBrowserTypeCheck() && !checkBrowserLevel())
                return false;
            wfconnection = null;
            String s = null;
            s = super._request.getParameter("inv");
            if(s == null && HttpRequestHandler.getSessionValue(super._session, "Invocation") != null)
                s = (String)HttpRequestHandler.getSessionValue(super._session, "Invocation");
            String s1 = null;
            s1 = super._request.getParameter("clcmd");
            if(s1 == null && HttpRequestHandler.getSessionValue(super._session, "clcommand_from_program_call") != null)
                s1 = (String)HttpRequestHandler.getSessionValue(super._session, "clcommand_from_program_call");
            HttpRequestHandler.putSessionValue(super._session, "Invocation", null);
            HttpRequestHandler.putSessionValue(super._session, "clcommand_from_program_call", null);
            InvocationProperties invocationproperties = null;
            if(s != null)
                invocationproperties = getInvocationProperties(super._servletContext, s);
            else
            if(s1 == null)
                throw new WebfacingInternalException(HttpRequestHandler._resmri.getString("WF0122"));
            if(super._request.getParameter("timestamp") == null || HttpRequestHandler.getSessionValue(super._session, "errorBean") != null)
            {
                HttpRequestHandler.putSessionValue(super._session, "errorBean", null);
                HttpRequestHandler.putSessionValue(super._session, "Invocation", s);
                HttpRequestHandler.putSessionValue(super._session, "clcommand_from_program_call", s1);
                if(invocationproperties != null && s1 == null)
                    HttpRequestHandler.putSessionValue(super._session, "clcommand_from_inv", invocationproperties.getCLCommand(super._request));
                HttpRequestHandler.putSessionValue(super._session, "FORCE_UTF8", new Boolean(wfappproperties.canHandleUTF8()));
                HttpRequestHandler.putSessionValue(super._session, "ErrorJSPDetailLevel", new Integer(wfappproperties.getErrorJSPDetail()));
                HttpRequestHandler.putSessionValue(super._session, "AdminEmail", wfappproperties.getContactAdminURL());
                if(invocationproperties != null)
                    HttpRequestHandler.putSessionValue(super._session, "ApplicationTitle", invocationproperties.getTitle());
                else
                    HttpRequestHandler.putSessionValue(super._session, "ApplicationTitle", "");
                initializeFixedHeightForEachRowProperty(wfappproperties, invocationproperties);
                HttpRequestHandler.putSessionValue(super._session, "WWidth", wfappproperties.getInitialWWidth());
                Boolean boolean1;
                if(invocationproperties != null && invocationproperties.forceDFRWRT() != null)
                    boolean1 = invocationproperties.forceDFRWRT();
                else
                    boolean1 = new Boolean(wfappproperties.forceDFRWRT());
                HttpRequestHandler.putSessionValue(super._session, "FORCE_DFRWRT", boolean1);
                String s3 = null;
                if(invocationproperties != null)
                    s3 = invocationproperties.getInsertMode();
                if(s3 == null)
                    s3 = wfappproperties.getInsertMode();
                HttpRequestHandler.putSessionValue(super._session, "INSERTMODE", s3);
                HttpRequestHandler.putSessionValue(super._session, "FIELDEXITKEYCODE", wfappproperties.getFieldExitKeyCode());
                HttpRequestHandler.putSessionValue(super._session, "InputCharMapping", MappingProperties.getMappingProperties(MappingProperties.INPUTCHARMAPPING_FILENAME));
                HttpRequestHandler.putSessionValue(super._session, "OutputCharMapping", MappingProperties.getMappingProperties(MappingProperties.OUTPUTCHARMAPPING_FILENAME));
            }
            String s2;
            if(super._request.getParameter("host") != null && !super._request.getParameter("host").equals(""))
                s2 = super._request.getParameter("host");
            else
            if(invocationproperties != null && invocationproperties.getHostAddress() != null && !invocationproperties.getHostAddress().equals(""))
                s2 = invocationproperties.getHostAddress();
            else
                s2 = wfappproperties.getHostName();
            if(s2 == null)
                throw new WebfacingInternalException(HttpRequestHandler._resmri.getString("WF0030"));
            String s4;
            if(super._request.getParameter("port") != null && !super._request.getParameter("port").equals(""))
                s4 = super._request.getParameter("port");
            else
            if(invocationproperties != null && invocationproperties.getHostPort() != null && !invocationproperties.getHostPort().equals(""))
                s4 = invocationproperties.getHostPort();
            else
                s4 = wfappproperties.getHostPort();
            if(s4 == null)
                s4 = "4004";
            else
                s4 = s4.trim();
            try
            {
                Integer.parseInt(s4);
            }
            catch(Throwable throwable1)
            {
                s4 = "4004";
            }
            String s5;
            if(invocationproperties != null && invocationproperties.getUserID() != null && !invocationproperties.getUserID().equals(""))
                s5 = invocationproperties.getUserID();
            else
                s5 = wfappproperties.getUserID();
            String s6;
            if(invocationproperties != null && invocationproperties.getPassword() != null && !invocationproperties.getPassword().equals(""))
                s6 = invocationproperties.getPassword();
            else
                s6 = wfappproperties.getPassword();
            boolean flag1;
            if(invocationproperties != null && invocationproperties.isPromptAtRuntime() != null)
                flag1 = invocationproperties.isPromptAtRuntime().booleanValue();
            else
                flag1 = wfappproperties.needToPrompt();
            boolean flag2 = false;
            if((s6 == null || s6.equals("") || s5 == null || s5.equals("") || flag1) && wfappproperties.promptRetain() && HttpRequestHandler.getSessionValue(super._session, "ChallengeIssued") != null && ((Boolean)HttpRequestHandler.getSessionValue(super._session, "ChallengeIssued")).booleanValue() && HttpRequestHandler.getSessionValue(super._session, "USERID") != null && HttpRequestHandler.getSessionValue(super._session, "PWD") != null)
            {
                s5 = (String)HttpRequestHandler.getSessionValue(super._session, "USERID");
                s6 = (String)HttpRequestHandler.getSessionValue(super._session, "PWD");
                flag2 = true;
            }
            boolean flag3 = false;
            if(super._request.getParameter("logon") != null && super._request.getParameter("logon").toLowerCase().equals("cancel"))
            {
                processEndOfApplication();
                return false;
            }
            if(super._request.getParameter("logon") != null && super._request.getParameter("logon").toLowerCase().equals("logon") || super._request.getParameter("inv") != null || super._request.getParameter("clcmd") != null)
            {
                String s7 = super._request.getParameter("userid");
                String s9 = super._request.getParameter("password");
                if(s9 != null && !s9.trim().equals("") && s7 != null && !s7.trim().equals("") && s9.length() <= 128 && s7.length() <= 10)
                {
                    Encoder encoder = new Encoder();
                    s5 = s7;
                    s6 = Encoder.encodePassword(s9);
                    flag1 = false;
                    if(super._request.getParameter("timestamp") != null)
                        flag3 = true;
                }
            }
            if(s6 == null || s6.equals("") || s5 == null || s5.equals("") || s5.length() > 10 || flag1 && !flag2)
            {
                if(HttpRequestHandler.getSessionValue(super._session, "ChallengeIssued") != null && ((Boolean)HttpRequestHandler.getSessionValue(super._session, "ChallengeIssued")).booleanValue())
                {
                    HttpRequestHandler.putSessionValue(super._session, "Invocation", s);
                    HttpRequestHandler.putSessionValue(super._session, "clcommand_from_program_call", s1);
                }
                promptForChallenge();
                return false;
            }
            String s8 = "";
            if(s1 != null)
                s8 = s1.trim();
            else
            if(invocationproperties != null)
                if(HttpRequestHandler.getSessionValue(super._session, "clcommand_from_inv") != null)
                    s8 = (String)HttpRequestHandler.getSessionValue(super._session, "clcommand_from_inv");
                else
                    s8 = invocationproperties.getCLCommand(super._request);
            try
            {
                wfconnection = new WFConnection(super._session, s2.trim(), s4, s5.trim(), s6.trim(), s8);
                HttpRequestHandler.putSessionValue(super._session, "WFConnection", wfconnection);
                HttpRequestHandler.putSessionValue(super._session, "Invocation", s);
                HttpRequestHandler.putSessionValue(super._session, "clcommand_from_program_call", s1);
                HostJobInfo hostjobinfo = new HostJobInfo(wfconnection);
                WFSession.setJobInfoRequestor(hostjobinfo);
                HttpRequestHandler.putSessionValue(super._session, "HostJobInfo", hostjobinfo);
                if(wfappproperties.promptRetain() && flag3 && HttpRequestHandler.getSessionValue(super._session, "ChallengeIssued") != null && ((Boolean)HttpRequestHandler.getSessionValue(super._session, "ChallengeIssued")).booleanValue())
                {
                    HttpRequestHandler.putSessionValue(super._session, "PWD", s6.trim());
                    HttpRequestHandler.putSessionValue(super._session, "USERID", s5.trim());
                } else
                if(!wfappproperties.promptRetain())
                {
                    HttpRequestHandler.putSessionValue(super._session, "PWD", null);
                    HttpRequestHandler.putSessionValue(super._session, "USERID", null);
                    HttpRequestHandler.putSessionValue(super._session, "ChallengeIssued", null);
                }
            }
            catch(WFInvalidSignOnException wfinvalidsignonexception)
            {
                super._trace.err(2, wfinvalidsignonexception);
                if(HttpRequestHandler.getSessionValue(super._session, "ChallengeIssued") != null && ((Boolean)HttpRequestHandler.getSessionValue(super._session, "ChallengeIssued")).booleanValue())
                {
                    HttpRequestHandler.putSessionValue(super._session, "Invocation", s);
                    HttpRequestHandler.putSessionValue(super._session, "clcommand_from_program_call", s1);
                } else
                {
                    getErrorHandler().handleError(wfinvalidsignonexception, HttpRequestHandler._resmri.getString("WF0103"), wfinvalidsignonexception.getMessage());
                }
                if(wfappproperties.promptRetain() && flag3 && HttpRequestHandler.getSessionValue(super._session, "ChallengeIssued") != null && ((Boolean)HttpRequestHandler.getSessionValue(super._session, "ChallengeIssued")).booleanValue())
                {
                    HttpRequestHandler.putSessionValue(super._session, "PWD", null);
                    HttpRequestHandler.putSessionValue(super._session, "USERID", null);
                }
                super._request.setAttribute("exception", wfinvalidsignonexception);
                super._request.setAttribute("user", s5);
                if(invocationproperties == null)
                    super._request.setAttribute("url", "/WFLogon?clcmd=" + s8 + "&host=" + s2 + "&port=" + s4);
                else
                    super._request.setAttribute("url", "/WFLogon?clcmd=" + invocationproperties.getCLCommand(super._request) + "&host=" + invocationproperties.getHostAddress() + "&port=" + invocationproperties.getHostPort());
                super._servletContext.getRequestDispatcher("/WFLogonExceptionHandler").forward(super._request, super._response);
                flag = false;
            }
        }
        catch(WebfacingInternalException webfacinginternalexception)
        {
            super._trace.err(2, "Internal Exception while initializing invocation. \n" + webfacinginternalexception);
            getErrorHandler().handleError(webfacinginternalexception, HttpRequestHandler._resmri.getString("WF0104"));
            flag = false;
        }
        catch(WFServerDownLevelException wfserverdownlevelexception)
        {
            super._trace.err(2, "WebFacing server down level version error. \n" + wfserverdownlevelexception);
            getErrorHandler().handleError(wfserverdownlevelexception, HttpRequestHandler._resmri.getString("WF0105"));
            flag = false;
        }
        catch(Throwable throwable)
        {
            super._trace.err(2, "Internal Throwable caught while initializing invocation. \n" + throwable);
            getErrorHandler().handleError(new Exception(throwable.toString()), HttpRequestHandler._resmri.getString("WF0104"));
            flag = false;
        }
        return flag;
    }

    static InvocationProperties getInvocationProperties(ServletContext servletcontext, String s)
        throws WebfacingInternalException, WFException
    {
        if(s == null || s.length() == 0)
        {
            throw new WebfacingInternalException(HttpRequestHandler._resmri.getString("WF0026"));
        } else
        {
            InvocationProperties invocationproperties = null;
            invocationproperties = InvocationProperties.getInvocationProperties(servletcontext, s);
            return invocationproperties;
        }
    }

    private void initializeSession()
    {
        if(super._request.getParameter("timestamp") == null && HttpRequestHandler.getSessionValue(super._session, "WFConnection") == null && HttpRequestHandler.getSessionValue(super._session, "errorBean") == null || super._request.getParameter("inv") != null && HttpRequestHandler.getSessionValue(super._session, "Invocation") != null && !super._request.getParameter("inv").equals(HttpRequestHandler.getSessionValue(super._session, "Invocation")) || super._request.getParameter("clcmd") != null && HttpRequestHandler.getSessionValue(super._session, "clcommand_from_program_call") != null && !super._request.getParameter("clcmd").equals(HttpRequestHandler.getSessionValue(super._session, "clcommand_from_program_call")))
        {
            HttpRequestHandler.removeWebAppSessionAttributes(super._session);
            String s = super._request.getHeader("referer");
            if(s == null)
                s = super._request.getParameter("refererPage");
            HttpRequestHandler.putSessionValue(super._session, "RefererPage", s);
        } else
        if(HttpRequestHandler.getSessionValue(super._session, "WFConnection") == null && HttpRequestHandler.getSessionValue(super._session, "errorBean") != null)
        {
            Object obj = HttpRequestHandler.getSessionValue(super._session, "RefererPage");
            Object obj1 = HttpRequestHandler.getSessionValue(super._session, "Invocation");
            Object obj2 = HttpRequestHandler.getSessionValue(super._session, "clcommand_from_program_call");
            Object obj3 = HttpRequestHandler.getSessionValue(super._session, "errorBean");
            HttpRequestHandler.removeWebAppSessionAttributes(super._session);
            HttpRequestHandler.putSessionValue(super._session, "RefererPage", obj);
            HttpRequestHandler.putSessionValue(super._session, "Invocation", obj1);
            HttpRequestHandler.putSessionValue(super._session, "clcommand_from_program_call", obj2);
            HttpRequestHandler.putSessionValue(super._session, "errorBean", obj3);
        }
    }

    private boolean checkBrowserLevel()
    {
        String s = super._request.getHeader("user-agent");
        try
        {
            if(s.indexOf("MSIE") >= 0)
            {
                String s1 = s.substring(s.indexOf("MSIE") + 5);
                int i = 0;
                String s2 = "";
                for(; i < s1.length() && s1.charAt(i) >= '0' && s1.charAt(i) < '9'; i++)
                    s2 = s2 + s1.charAt(i);

                if(Double.parseDouble(s2) < 5D)
                {
                    ErrorHandler errorhandler1 = new ErrorHandler(super._servletContext, super._request, super._response, WFSession.getTraceLogger(), false, 1);
                    errorhandler1.handleError(WebfacingConstants.replaceSubstring(HttpRequestHandler._resmri.getString("WF0095"), "&1", " <a href=\"http://www.microsoft.com/ie\">http://www.microsoft.com/ie</a>."));
                    return false;
                }
            } else
            {
                ErrorHandler errorhandler = new ErrorHandler(super._servletContext, super._request, super._response, WFSession.getTraceLogger(), false, 1);
                errorhandler.handleError(WebfacingConstants.replaceSubstring(HttpRequestHandler._resmri.getString("WF0095"), "&1", " <a href=\"http://www.microsoft.com/ie\">http://www.microsoft.com/ie</a>."));
                return false;
            }
        }
        catch(Exception exception) { }
        return true;
    }

    private void initializeFixedHeightForEachRowProperty(WFAppProperties wfappproperties, InvocationProperties invocationproperties)
    {
        String s;
        if(invocationproperties != null && invocationproperties.getFixedHeight() != null && !invocationproperties.getFixedHeight().equals(""))
            s = invocationproperties.getFixedHeight();
        else
            s = wfappproperties.getFixedHeight();
        if(s != null)
            HttpRequestHandler.putSessionValue(super._session, "FixedHeightForEachRow", s);
    }

    private void manageTrace()
    {
        super._trace = (ITraceLogger)HttpRequestHandler.getSessionValue(super._session, "traceLogger");
        if(null == super._trace)
        {
            super._trace = new TraceLoggerProxy(super._session.getId());
            HttpRequestHandler.putSessionValue(super._session, "traceLogger", super._trace);
        }
    }

    private static final String copyRight = new String("(C) Copyright IBM Corporation 1999-2003 all rights reserved");

}
