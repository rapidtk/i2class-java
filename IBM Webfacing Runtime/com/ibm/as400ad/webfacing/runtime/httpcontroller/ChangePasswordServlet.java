// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.httpcontroller;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400ad.webfacing.common.WFAppProperties;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.controller.ErrorHandler;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import javax.servlet.*;
import javax.servlet.http.*;

public class ChangePasswordServlet extends HttpServlet
{

    public ChangePasswordServlet()
    {
    }

    private boolean containsRepeatedConsecutiveChars(String s)
    {
        boolean flag = false;
        for(int i = 0; i <= s.length() - 2; i++)
            if(s.charAt(i) == s.charAt(i + 1))
                return true;

        return flag;
    }

    public void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException
    {
        doPost(httpservletrequest, httpservletresponse);
    }

    public void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException, ServletException
    {
        WFAppProperties wfappproperties = (WFAppProperties)getServletContext().getAttribute(WFAppProperties.WFAPPPROPERTIES);
        String s = (String)httpservletrequest.getSession().getAttribute("url");
        if(httpservletrequest.getParameter("logon") != null && httpservletrequest.getParameter("logon").toLowerCase().equals("cancel"))
        {
            if(wfappproperties.getLogonPageName() == null)
                getServletContext().getRequestDispatcher("/services/Logon/ChangePassword.jsp").forward(httpservletrequest, httpservletresponse);
            else
                getServletContext().getRequestDispatcher(wfappproperties.getLogonPageName()).forward(httpservletrequest, httpservletresponse);
            return;
        }
        String s1 = httpservletrequest.getParameter("old");
        String s2 = httpservletrequest.getParameter("new");
        String s3 = httpservletrequest.getParameter("confirm");
        String s4 = s1.trim();
        String s5 = s2.trim();
        String s6 = s3.trim();
        Object obj = null;
        if(!s5.equals(s6))
        {
            String s7 = _resmri.getString("WF0131");
            httpservletrequest.setAttribute("message", s7);
            getServletContext().getRequestDispatcher("/services/Logon/ChangePassword.jsp").forward(httpservletrequest, httpservletresponse);
            return;
        }
        try
        {
            String s9 = wfappproperties.getHostName();
            String s10 = (String)httpservletrequest.getSession().getAttribute("user");
            AS400 as400 = new AS400(s9, s10);
            as400.changePassword(s4, s5);
            String s11 = s5;
            httpservletrequest.setAttribute("userid", s10);
            httpservletrequest.setAttribute("password", s11);
            getServletContext().getRequestDispatcher(s + "&userid=" + s10 + "&password=" + s11).forward(httpservletrequest, httpservletresponse);
        }
        catch(AS400SecurityException as400securityexception)
        {
            String s8;
            switch(as400securityexception.getReturnCode())
            {
            case 11: // '\013'
                s8 = _resmri.getString("WF0134");
                break;

            case 12: // '\f'
                if(!containsRepeatedConsecutiveChars(s5))
                    s8 = _resmri.getString("WF0138");
                else
                    s8 = _resmri.getString("WF0135");
                break;

            case 15: // '\017'
                s8 = _resmri.getString("WF0137");
                break;

            case 17: // '\021'
                s8 = _resmri.getString("WF0136");
                break;

            case 18: // '\022'
                s8 = _resmri.getString("WF0138");
                break;

            case 39: // '\''
                s8 = _resmri.getString("WF0142");
                break;

            case 20: // '\024'
                s8 = _resmri.getString("WF0132");
                break;

            case 19: // '\023'
                s8 = _resmri.getString("WF0133");
                break;

            case 21: // '\025'
                s8 = _resmri.getString("WF0141");
                break;

            case 8: // '\b'
            case 23: // '\027'
                s8 = _resmri.getString("WF0143");
                break;

            case 13: // '\r'
                s8 = _resmri.getString("WF0140");
                break;

            case 9: // '\t'
            case 10: // '\n'
            case 14: // '\016'
            case 16: // '\020'
            case 22: // '\026'
            case 24: // '\030'
            case 25: // '\031'
            case 26: // '\032'
            case 27: // '\033'
            case 28: // '\034'
            case 29: // '\035'
            case 30: // '\036'
            case 31: // '\037'
            case 32: // ' '
            case 33: // '!'
            case 34: // '"'
            case 35: // '#'
            case 36: // '$'
            case 37: // '%'
            case 38: // '&'
            default:
                s8 = _resmri.getString("WF0140");
                break;
            }
            if(s8 != null)
            {
                httpservletrequest.setAttribute("message", s8);
                getServletContext().getRequestDispatcher("/services/Logon/ChangePassword.jsp").forward(httpservletrequest, httpservletresponse);
            }
        }
        catch(UnknownHostException unknownhostexception)
        {
            ErrorHandler errorhandler = new ErrorHandler(getServletContext(), httpservletrequest, httpservletresponse, null);
            errorhandler.handleError(unknownhostexception, _resmri.getString("WF0103"), unknownhostexception.getMessage());
        }
        catch(IOException ioexception)
        {
            ErrorHandler errorhandler1 = new ErrorHandler(getServletContext(), httpservletrequest, httpservletresponse, null);
            errorhandler1.handleError(ioexception, _resmri.getString("WF0103"), ioexception.getMessage());
        }
        catch(Throwable throwable)
        {
            ErrorHandler errorhandler2 = new ErrorHandler(getServletContext(), httpservletrequest, httpservletresponse, null);
            errorhandler2.handleError(throwable, _resmri.getString("WF0102"), throwable.getMessage());
        }
    }

    public void init(ServletConfig servletconfig)
        throws ServletException
    {
        super.init(servletconfig);
    }

    private static final String copyRight = new String("(C) Copyright IBM Corporation 1999-2003 all rights reserved");
    protected static ResourceBundle _resmri;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
