// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.common;

import com.ibm.as400ad.webfacing.runtime.core.WFException;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

// Referenced classes of package com.ibm.as400ad.webfacing.common:
//            BaseProperties, WFAppProperties, WebfacingConstants

public class InvocationProperties extends BaseProperties
{

    public InvocationProperties()
    {
    }

    public InvocationProperties(ServletContext servletcontext, String s)
        throws WebfacingInternalException
    {
        WFAppProperties wfappproperties = WFAppProperties.getWFAppProperties(servletcontext);
        String s1 = servletcontext.getInitParameter(s + INVOCATIONCLC);
        String s2 = servletcontext.getInitParameter(s + INVOCATIONHOSTNAME);
        if(null == s2 && null != wfappproperties)
            s2 = wfappproperties.getHostName();
        if(null == s1 || null == s2)
            throw new WebfacingInternalException(WebfacingConstants.replaceSubstring(_resmri.getString("WF0130"), "&1", s));
        if(null != s1)
            setProperty(INVOCATIONCLC, s1);
        String s3 = servletcontext.getInitParameter(s2);
        if(null != s3)
            setProperty(INVOCATIONHOSTNAME, s3);
        else
        if(null != wfappproperties && null != wfappproperties.getHostName())
            setProperty(INVOCATIONHOSTNAME, wfappproperties.getHostName());
        String s4 = servletcontext.getInitParameter(s2 + INVOCATIONHOSTPORT);
        if(null != s4)
            setProperty(INVOCATIONHOSTPORT, s4);
        else
        if(null != wfappproperties && null != wfappproperties.getHostPort())
            setProperty(INVOCATIONHOSTPORT, wfappproperties.getHostPort());
        String s5 = servletcontext.getInitParameter(s + INVOCATIONUSERID);
        if(null != s5)
            setProperty(INVOCATIONUSERID, s5);
        else
        if(null != wfappproperties && null != wfappproperties.getUserID())
            setProperty(INVOCATIONUSERID, wfappproperties.getUserID());
        String s6 = servletcontext.getInitParameter(s + INVOCATIONPASSWORD);
        if(null != s6)
            setProperty(INVOCATIONPASSWORD, s6);
        else
        if(null != wfappproperties && null != wfappproperties.getPassword())
            setProperty(INVOCATIONPASSWORD, wfappproperties.getPassword());
        String s7 = servletcontext.getInitParameter(s + INVOCATIONTITLE);
        if(null != s7)
            setProperty(INVOCATIONTITLE, s7);
        String s8 = servletcontext.getInitParameter(s + INVOCATIONPROMPT);
        if(null != s8)
            setProperty(INVOCATIONPROMPT, s8);
        else
        if(null != wfappproperties && !wfappproperties.needToPrompt())
            setProperty(INVOCATIONPROMPT, WFAppProperties.FALSE);
        else
            setProperty(INVOCATIONPROMPT, WFAppProperties.TRUE);
        String s9 = servletcontext.getInitParameter(s + INVOCATIONFIXEDHEIGHT);
        if(null != s9)
            setProperty(INVOCATIONFIXEDHEIGHT, s9);
        else
        if(null != wfappproperties && null != wfappproperties.getFixedHeight())
            setProperty(INVOCATIONFIXEDHEIGHT, wfappproperties.getFixedHeight());
        String s10 = servletcontext.getInitParameter(s + INVOCATIONFORCEDFRWRT);
        if(null != s10)
            setProperty(INVOCATIONFORCEDFRWRT, s10);
        else
        if(null != wfappproperties && !wfappproperties.forceDFRWRT())
            setProperty(INVOCATIONFORCEDFRWRT, WFAppProperties.FALSE);
        else
            setProperty(INVOCATIONFORCEDFRWRT, WFAppProperties.TRUE);
        String s11 = servletcontext.getInitParameter(s + INVOCATIONINSERTMODE);
        if(null != s11)
            setProperty(INVOCATIONINSERTMODE, s11);
        else
        if(null != wfappproperties && null != wfappproperties.getInsertMode())
            setProperty(INVOCATIONINSERTMODE, wfappproperties.getInsertMode());
    }

    public InvocationProperties(String s)
        throws IOException, FileNotFoundException
    {
        super(s);
    }

    public InvocationProperties(URL url)
        throws IOException, FileNotFoundException
    {
        super(url);
    }

    public BaseProperties createNewProperties()
        throws IOException
    {
        return new InvocationProperties();
    }

    public String getCLCommand(HttpServletRequest httpservletrequest)
    {
        String s = getProperty(INVOCATIONCLC).trim();
        if(s.indexOf("(") < 0)
            return s;
        else
            return getParmValues(s, httpservletrequest).trim();
    }

    public String getFixedHeight()
    {
        return getProperty(INVOCATIONFIXEDHEIGHT);
    }

    public String getHostAddress()
    {
        return getProperty(INVOCATIONHOSTNAME);
    }

    public String getHostPort()
    {
        return getProperty(INVOCATIONHOSTPORT);
    }

    public static InvocationProperties getInvocationProperties(ServletContext servletcontext, String s)
        throws WebfacingInternalException, WFException
    {
        InvocationProperties invocationproperties = null;
        String s1 = PREFIX + s;
        invocationproperties = (InvocationProperties)servletcontext.getAttribute(s1);
        if(null == invocationproperties)
        {
            invocationproperties = new InvocationProperties(servletcontext, s);
            if(null != invocationproperties)
                servletcontext.setAttribute(s1, invocationproperties);
        }
        return invocationproperties;
    }

    public static void preload(ServletContext servletcontext)
    {
        for(Enumeration enumeration = servletcontext.getInitParameterNames(); enumeration.hasMoreElements();)
        {
            String s = (String)enumeration.nextElement();
            if(null != s && s.endsWith(INVOCATIONCLC))
            {
                String s1 = s.substring(0, s.length() - INVOCATIONCLC.length());
                try
                {
                    getInvocationProperties(servletcontext, s1);
                }
                catch(WebfacingInternalException webfacinginternalexception)
                {
                    servletcontext.log(webfacinginternalexception.getLocalizedMessage());
                }
                catch(Throwable throwable) { }
            }
        }

    }

    public String getName()
    {
        return super.getName();
    }

    private String getParmValues(String s, HttpServletRequest httpservletrequest)
    {
        String s1 = s;
        int i = 0;
        boolean flag = false;
        int j;
        for(; i < s1.length(); i = j + 1)
        {
            i = s1.indexOf("&", i);
            if(i < 0)
                break;
            j = i + 1;
            for(boolean flag1 = false; j < s1.length() && !flag1;)
            {
                char c = s1.charAt(j);
                if(!Character.isDigit(c) && c != '_' && c != '-' && c != '.' && c != ':' && (c < 'a' || c > 'z') && (c < 'A' || c > 'Z'))
                    flag1 = true;
                else
                    j++;
            }

            String s2 = s1.substring(i, j);
            int k = s1.length();
            String s3 = null;
            s3 = httpservletrequest.getParameter(s2.substring(1));
            if(s3 != null)
                s1 = s1.substring(0, i) + s3 + s1.substring(j);
            j = (j + s1.length()) - k;
        }

        return s1;
    }

    public String getPassword()
    {
        return getProperty(INVOCATIONPASSWORD);
    }

    public String getTitle()
    {
        return getProperty(INVOCATIONTITLE);
    }

    public String getUserID()
    {
        return getProperty(INVOCATIONUSERID);
    }

    public Boolean isPromptAtRuntime()
    {
        String s = getProperty(INVOCATIONPROMPT);
        if(s != null)
        {
            if(s.trim().equalsIgnoreCase(FALSE))
                return new Boolean(false);
            if(s.trim().equalsIgnoreCase(TRUE))
                return new Boolean(true);
        }
        return null;
    }

    public Boolean forceDFRWRT()
    {
        String s = getProperty(INVOCATIONFORCEDFRWRT);
        if(s != null)
        {
            if(s.trim().equalsIgnoreCase(FALSE))
                return new Boolean(false);
            if(s.trim().equalsIgnoreCase(TRUE))
                return new Boolean(true);
        }
        return null;
    }

    public void setCLCommand(String s)
    {
        setPropertyString(INVOCATIONCLC, s);
    }

    public void setFixedHeight(String s)
    {
        setPropertyString(INVOCATIONFIXEDHEIGHT, s);
    }

    public void setHostAddress(String s)
    {
        setPropertyString(INVOCATIONHOSTNAME, s);
    }

    public void setHostPort(String s)
    {
        setPropertyString(INVOCATIONHOSTPORT, s);
    }

    public void setName(String s)
    {
        super.setName(s);
    }

    public void setPassword(String s)
    {
        setPropertyString(INVOCATIONPASSWORD, s);
    }

    public void setPromptAtRuntime(boolean flag)
    {
        if(flag)
            setPropertyString(INVOCATIONPROMPT, TRUE);
        else
            setPropertyString(INVOCATIONPROMPT, FALSE);
    }

    public void setTitle(String s)
    {
        setPropertyString(INVOCATIONTITLE, s);
    }

    public void setUserID(String s)
    {
        setPropertyString(INVOCATIONUSERID, s);
    }

    public void setForceDFRWRT(boolean flag)
    {
        if(flag)
            setProperty(INVOCATIONFORCEDFRWRT, TRUE);
        else
            setProperty(INVOCATIONFORCEDFRWRT, FALSE);
    }

    public String getInsertMode()
    {
        String s = getProperty(INVOCATIONINSERTMODE);
        if(s != null)
        {
            s = s.trim();
            if(s.equalsIgnoreCase(TRUE))
                return String.valueOf(true);
            if(s.equalsIgnoreCase(FALSE))
                return String.valueOf(false);
            else
                return null;
        } else
        {
            return s;
        }
    }

    public void setInsertMode(boolean flag)
    {
        setProperty(INVOCATIONINSERTMODE, String.valueOf(flag));
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 1999=2003, all rights reserved.");
    protected static ResourceBundle _resmri;
    public static String INVOCATIONHOSTNAME = "_WFHost";
    public static String INVOCATIONHOSTPORT = "_WFPort";
    public static String INVOCATIONUSERID = "_WFUserID";
    public static String INVOCATIONPASSWORD = "_WFPassword";
    public static String INVOCATIONCLC = "_WFCommand";
    public static String INVOCATIONPROMPT = "_WFPrompt";
    public static String INVOCATIONTITLE = "_WFLabel";
    public static String INVOCATIONFIXEDHEIGHT = "_WFFixedLineHeight";
    public static String INVOCATIONFORCEDFRWRT = "_WFDeferWrite";
    public static String INVOCATIONINSERTMODE = "_WFInsertMode";
    public static String TRUE = "TRUE";
    public static String FALSE = "FALSE";
    public static String PREFIX = "INV";

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
