// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.common;

import com.ibm.as400ad.webfacing.runtime.httpcontroller.HttpRequestHandler;
import com.ibm.etools.iseries.webfacing.runtime.platform.J2eeLevel;
import com.ibm.etools.iseries.webfacing.runtime.platform.J2eeServlet;
import java.io.File;
import java.util.Hashtable;
import java.util.Map;
import javax.servlet.ServletContext;

public class WFAppProperties
{

    public static WFAppProperties getWFAppProperties(ServletContext servletcontext)
    {
        WFAppProperties wfappproperties = null;
        if(null != servletcontext)
            wfappproperties = (WFAppProperties)servletcontext.getAttribute(WFAPPPROPERTIES);
        if(null == wfappproperties)
        {
            wfappproperties = new WFAppProperties(servletcontext);
            if(null != servletcontext)
                servletcontext.setAttribute(WFAPPPROPERTIES, wfappproperties);
        }
        return wfappproperties;
    }

    public WFAppProperties(ServletContext servletcontext)
    {
        _properties = null;
        _properties = new Hashtable();
        retrieveAndSaveProperty(servletcontext, WFDEFAULTHOST, null);
        String s = getProperty(WFDEFAULTHOST);
        retrieveAndSaveIntProperty(servletcontext, WFAPPBUTTONHEIGHT, "30");
        retrieveAndSaveIntProperty(servletcontext, WFAPPBUTTONWIDTH, "100");
        retrieveAndSaveProperty(servletcontext, WFAPPCONTACTADMINURL, null);
        retrieveAndSaveIntProperty(servletcontext, WFAPPERRORJSPDETAIL, "0");
        retrieveAndSaveProperty(servletcontext, WFAPPFIXEDHEIGHT, "false");
        retrieveAndSaveProperty(servletcontext, s, null);
        retrieveAndSaveProperty(servletcontext, s + WFPORT, "4004");
        retrieveAndSaveProperty(servletcontext, WFAPPINCLUDECOMMANDKEYNAME, "false");
        retrieveAndSaveProperty(servletcontext, WFAPPINSERTMODE, "false");
        retrieveAndSaveProperty(servletcontext, WFAPPPROMPT, "true");
        retrieveAndSaveProperty(servletcontext, s + WFPWD, null);
        retrieveAndSaveProperty(servletcontext, s + WFUID, null);
        retrieveAndSaveProperty(servletcontext, WFFIELDEXITKEYCODE, "-1");
        retrieveAndSaveProperty(servletcontext, WFFORCE_UTF8, "false");
        retrieveAndSaveProperty(servletcontext, WFFORCEDFRWRT, "true");
        retrieveAndSaveProperty(servletcontext, WFIGNOREBROWSERTYPECHECK, "false");
        retrieveAndSaveIntProperty(servletcontext, WFINITIALWWIDTH, "13");
        retrieveAndSaveProperty(servletcontext, WFRETAINPROMPTCREDENTIALS, "false");
        retrieveAndSaveProperty(servletcontext, WFUSERDEFINEDLOGON, "false");
        retrieveAndSaveProperty(servletcontext, WFAPPCHANGEEXPIREDPASSWORD, "true");
        retrieveAndSaveIntProperty(servletcontext, WFBEANCACHESIZE, Integer.toString(600));
        String s1 = getLogonPageName(servletcontext);
        setProperty(WFLOGONPAGE, s1);
    }

    private WFAppProperties()
    {
        _properties = null;
    }

    private String getLogonPageName(ServletContext servletcontext)
    {
        try
        {
            String s = servletcontext.getRealPath("/") + HttpRequestHandler.FILE_SEPARATOR;
            File file = null;
            file = new File(s, "logon.jsp");
            boolean flag = file.exists();
            if(flag)
                return "logon.jsp";
            if(!flag)
            {
                File file1 = new File(s, "logon.html");
                if(file1.exists())
                    return "logon.html";
            }
        }
        catch(Exception exception)
        {
            return null;
        }
        return null;
    }

    public String getDefaultHost()
    {
        String s = getProperty(WFDEFAULTHOST);
        return s;
    }

    public String getContactAdminURL()
    {
        return getProperty(WFAPPCONTACTADMINURL);
    }

    public String getErrorJSPDetail()
    {
        return getProperty(WFAPPERRORJSPDETAIL);
    }

    public String getFixedHeight()
    {
        return getProperty(WFAPPFIXEDHEIGHT);
    }

    public String getHostName()
    {
        String s = getProperty(getDefaultHost());
        return s;
    }

    public String getHostPort()
    {
        return getProperty(getDefaultHost() + WFPORT);
    }

    public String getPassword()
    {
        return getProperty(getDefaultHost() + WFPWD);
    }

    public String getUserID()
    {
        return getProperty(getDefaultHost() + WFUID);
    }

    public boolean canHandleUTF8()
    {
        boolean flag = false;
        String s = getProperty(WFFORCE_UTF8);
        if(s != null)
            flag = s.equalsIgnoreCase(TRUE);
        return flag;
    }

    public boolean promptRetain()
    {
        boolean flag = false;
        String s = getProperty(WFRETAINPROMPTCREDENTIALS);
        if(s != null)
            flag = s.equalsIgnoreCase(TRUE);
        return flag;
    }

    public boolean useUserDefinedLogon()
    {
        boolean flag = false;
        String s = getProperty(WFUSERDEFINEDLOGON);
        if(s != null)
            flag = s.equalsIgnoreCase(TRUE);
        return flag;
    }

    public boolean needToPrompt()
    {
        boolean flag = true;
        String s = getProperty(WFAPPPROMPT);
        if(s != null)
            flag = !s.equalsIgnoreCase(FALSE);
        return flag;
    }

    public boolean forceDFRWRT()
    {
        boolean flag = true;
        String s = getProperty(WFFORCEDFRWRT);
        if(s != null)
            flag = !s.equalsIgnoreCase(FALSE);
        return flag;
    }

    public boolean ignoreBrowserTypeCheck()
    {
        boolean flag = false;
        String s = getProperty(WFIGNOREBROWSERTYPECHECK);
        if(s != null)
            flag = s.equalsIgnoreCase(TRUE);
        return flag;
    }

    public String getKeepCmdKeyNameOption()
    {
        return getProperty(WFAPPINCLUDECOMMANDKEYNAME);
    }

    public String getAppAreaButtonWidth()
    {
        return getProperty(WFAPPBUTTONWIDTH);
    }

    public String getLogonPageName()
    {
        return getProperty(WFLOGONPAGE);
    }

    public String getAppAreaButtonHeight()
    {
        return getProperty(WFAPPBUTTONHEIGHT);
    }

    public String getInsertMode()
    {
        return getProperty(WFAPPINSERTMODE);
    }

    public String getInitialWWidth()
    {
        return getProperty(WFINITIALWWIDTH);
    }

    public String getFieldExitKeyCode()
    {
        String as[] = {
            "Ctrl", "Alt"
        };
        String as1[] = {
            "17", "18"
        };
        try
        {
            String s = getProperty(WFFIELDEXITKEYCODE);
            s = s.trim();
            if(s.length() == 0)
                return "-1";
            for(int i = 0; i < as.length; i++)
                if(s.equalsIgnoreCase(as[i]))
                    return as1[i];

            Integer.parseInt(s);
            return s;
        }
        catch(Exception exception)
        {
            return "-1";
        }
    }

    private String getProperty(String s)
    {
        return (String)_properties.get(s);
    }

    private void retrieveAndSaveIntProperty(ServletContext servletcontext, String s, String s1)
    {
        String s2 = null;
        if(null != servletcontext)
            s2 = servletcontext.getInitParameter(s);
        try
        {
            s2 = s2.trim();
            Integer.parseInt(s2);
        }
        catch(Exception exception)
        {
            s2 = s1;
        }
        setProperty(s, s2);
    }

    private void retrieveAndSaveProperty(ServletContext servletcontext, String s, String s1)
    {
        String s2 = null;
        if(null != servletcontext)
            s2 = servletcontext.getInitParameter(s);
        if(s2 == null)
        {
            s2 = s1;
        } else
        {
            s2 = s2.trim();
            if(s2.length() == 0)
                s2 = s1;
        }
        setProperty(s, s2);
    }

    private void setProperty(String s, String s1)
    {
        if(s1 != null && s != null)
            _properties.put(s, s1);
    }

    public int getBeanCacheSize()
    {
        int i = Integer.parseInt(getProperty(WFBEANCACHESIZE));
        if(i < 0)
            i = 600;
        return i;
    }

    public String getChangeExpiredPassword()
    {
        return getProperty(WFAPPCHANGEEXPIREDPASSWORD);
    }

    public String getJ2EELevel()
    {
        return J2eeServlet._j2eeLevel.getJ2EELevel();
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 1999-2003, all rights reserved.");
    private Map _properties;
    public static String WFAPPCONTACTADMINURL = "ContactAdminEmail";
    public static String WFAPPERRORJSPDETAIL = "WFErrorJSPDetail";
    public static String WFDEFAULTHOST = "WFDefaultHost";
    public static String WFHOST = "Host";
    public static String WFPORT = "_Port";
    public static String WFUID = "_UserID";
    public static String WFPWD = "_Password";
    public static String WFAPPFIXEDHEIGHT = "WFFixedLineHeight";
    public static String WFFORCE_UTF8 = "WFUTF8";
    public static String WFRETAINPROMPTCREDENTIALS = "WFRetainPromptCredentials";
    public static String WFAPPPROMPT = "WFPrompt";
    public static String WFAPPCHANGEEXPIREDPASSWORD = "WFChangeExpiredPassword";
    public static String WFUSERDEFINEDLOGON = "WFUserDefinedSignon";
    public static String WFFORCEDFRWRT = "WFDeferWrite";
    public static String WFAPPINSERTMODE = "WFInsertMode";
    public static String WFAPPINCLUDECOMMANDKEYNAME = "WFShowCommandKeyName";
    public static String WFAPPBUTTONWIDTH = "WFCommandKeyWidth";
    public static String WFAPPBUTTONHEIGHT = "WFCommandKeyHeight";
    public static String WFFIELDEXITKEYCODE = "WFFieldExitKeyCode";
    public static String WFINITIALWWIDTH = "WFInitialWWidth";
    public static String WFIGNOREBROWSERTYPECHECK = "WFIgnoreBrowserTypeCheck";
    public static String WFBEANCACHESIZE = "WFBeanCacheSize";
    public static String WFWEBPAGECOMPRESSION = "WFWebPageCompression";
    public static String WFLOGONPAGE = "WFLogonPage";
    public static String WFSHOWVISIBLELABELSONLY = "WFShowVisibleLabelsOnly";
    public static String TRUE = "TRUE";
    public static String FALSE = "FALSE";
    public static String DEFAULTHOSTNAME = "Host1";
    public static String DEFAULTPORTNAME = "4004";
    public static String WFAPPPROPERTIES = "WFAppProperties";

}
