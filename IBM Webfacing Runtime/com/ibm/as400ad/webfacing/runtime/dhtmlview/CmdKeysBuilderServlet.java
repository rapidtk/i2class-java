// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.dhtmlview;

import com.ibm.as400ad.webfacing.common.WFAppProperties;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.controller.ErrorHandler;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.view.*;
import com.ibm.as400ad.webfacing.runtime.view.def.AIDKey;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.dhtmlview:
//            ClientScriptBuilderHelper, CmdKeysInterface, HTMLStringTransform

public class CmdKeysBuilderServlet extends HttpServlet
{

    public CmdKeysBuilderServlet()
    {
        _trace = null;
    }

    public void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws ServletException, IOException
    {
        doPost(httpservletrequest, httpservletresponse);
    }

    public void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws ServletException, IOException
    {
        Object obj = null;
        Object obj1 = null;
        boolean flag = false;
        HttpSession httpsession = httpservletrequest.getSession();
        ServletContext servletcontext = getServletConfig().getServletContext();
        _trace = WFSession.getTraceLogger();
        ResourceBundle resourcebundle = WebfacingConstants.RUNTIME_MRI_BUNDLE;
        Object obj2 = null;
        String s = "";
        String s2 = "";
        int i = 1;
        boolean flag1 = false;
        boolean flag2 = false;
        String s4 = "bo_ibm";
        String s5 = "bu_ibm";
        String s6 = "bd_ibm";
        try
        {
            WFAppProperties wfappproperties = (WFAppProperties)servletcontext.getAttribute(WFAppProperties.WFAPPPROPERTIES);
            String s1 = wfappproperties.getAppAreaButtonWidth();
            String s3 = wfappproperties.getAppAreaButtonHeight();
            boolean flag3 = wfappproperties.getKeepCmdKeyNameOption().equals("true");
            if(httpsession.getAttribute("errorBean") != null)
                return;
            ClientScriptBuilderHelper clientscriptbuilderhelper = new ClientScriptBuilderHelper(httpservletrequest, httpservletresponse, servletcontext, _trace);
            clientscriptbuilderhelper.buildClientScript();
            clientscriptbuilderhelper = null;
            IScreenBuilder iscreenbuilder = (IScreenBuilder)httpsession.getAttribute("screenbuilder");
            PrintWriter printwriter = httpservletresponse.getWriter();
            Iterator iterator = iscreenbuilder.getActiveKeysWithoutEnter();
            for(Enumeration enumeration = httpservletrequest.getParameterNames(); enumeration.hasMoreElements();)
            {
                boolean flag4 = false;
                String s7 = (String)enumeration.nextElement();
                String s9 = s7.toLowerCase();
                String s11 = httpservletrequest.getParameter(s7);
                String s12 = s11.toLowerCase();
                if(s9.equals("title"))
                    if(s12.equals("true") || s12.equals("yes"))
                        flag1 = true;
                    else
                        flag1 = false;
                if(s9.equals("width"))
                    s1 = s11;
                if(s9.equals("height"))
                    s3 = s11;
                if(s9.equals("count"))
                    try
                    {
                        i = Integer.parseInt(s11);
                    }
                    catch(Throwable throwable3)
                    {
                        i = 1;
                    }
                if(s9.equals("cmdkeylist"))
                {
                    String as[] = httpservletrequest.getParameterValues(s9);
                    String s15 = (String)httpservletrequest.getAttribute("caller");
                    if(s15 == null)
                    {
                        flag = false;
                    } else
                    {
                        Vector vector = new Vector();
                        for(StringTokenizer stringtokenizer = new StringTokenizer(s11, ",[]"); stringtokenizer.hasMoreTokens();)
                        {
                            String s18 = stringtokenizer.nextToken();
                            String s19 = httpservletrequest.getParameter("fieldname");
                            byte byte0 = 0;
                            if(!s18.equals("*AUTO"))
                                byte0 = AIDKeyDictionary.getKeyCode(s18);
                            else
                                i = 0;
                            for(Iterator iterator1 = iscreenbuilder.getActiveKeys(); iterator1.hasNext();)
                            {
                                AIDKey aidkey = (AIDKey)iterator1.next();
                                if(aidkey.getKeyCode().byteValue() == byte0 || s18.equals("*AUTO") && null != aidkey.getFieldName() && aidkey.getFieldName().equals(s19))
                                    vector.add(new AIDKey(aidkey.getKeyName(), aidkey.getKeyLabel()));
                                if(s18.equals("*AUTO"))
                                    i++;
                            }

                        }

                        iterator = vector.iterator();
                        flag = true;
                    }
                }
            }

            CmdKeysInterface cmdkeysinterface = (CmdKeysInterface)httpservletrequest.getAttribute("WFCmdKeys");
            if(cmdkeysinterface == null)
                cmdkeysinterface = new CmdKeysInterface(iterator, null, s1, s3, i, flag3, flag1, flag);
            else
                cmdkeysinterface.reload(iterator, null, s1, s3, i, flag3, flag1, flag);
            httpservletrequest.setAttribute("WFCmdKeys", cmdkeysinterface);
            Boolean boolean1 = (Boolean)servletcontext.getAttribute("useCmdJsp");
            if(boolean1 == null)
            {
                String s8 = System.getProperty("file.separator");
                String s10 = servletcontext.getRealPath("/") + s8 + "styles" + s8;
                File file = new File(s10, "/chrome/CmdKeys.jsp");
                if(file.exists())
                    boolean1 = new Boolean(true);
                else
                    boolean1 = new Boolean(false);
                servletcontext.setAttribute("useCmdJsp", boolean1);
            }
            if(boolean1.booleanValue())
            {
                try
                {
                    servletcontext.getRequestDispatcher("/styles/chrome/CmdKeys.jsp").include(httpservletrequest, httpservletresponse);
                }
                catch(Throwable throwable1)
                {
                    _trace.err(1, throwable1, "Calling CmdKeys.jsp failed with Exception : ");
                    (new ErrorHandler(servletcontext, httpservletrequest, httpservletresponse, _trace, true)).handleError(throwable1, resourcebundle.getString("WF0083"));
                }
            } else
            {
                int j;
                try
                {
                    j = Integer.parseInt(s1) * i;
                }
                catch(Throwable throwable2)
                {
                    j = 145;
                }
                if(!flag)
                {
                    printwriter.println("<table cellspacing=\"4\" border=\"0\"><tr><td><table width=\"" + j + "\" border=\"0\">");
                    printwriter.println("<tr> \n<td id=\"kENTER\" name=\"kENTER\" width=\"" + s1 + "\" height=\"" + s3 + "\" title=\"ENTER\" onmouseover=\"bo_ibm(this);\" " + "onmouseout=\"bu_ibm(this);\" onClick=\"bd_ibm(this);validateAndSubmit('Enter');\">" + "Enter<script language='JavaScript'> regCmdKey('kENTER','ENTER','','');</script></td> \n</tr>");
                } else
                {
                    printwriter.println("<table cellspacing=\"1\" cellpadding=\"1\" border=\"0\">");
                }
                for(; iterator.hasNext(); printwriter.println("</tr>"))
                {
                    printwriter.println("<tr>");
                    int k = 0;
                    do
                    {
                        IClientAIDKey iclientaidkey = (IClientAIDKey)iterator.next();
                        if(iclientaidkey.isKeyShownOnClient())
                        {
                            String s13 = iclientaidkey.getKeyName();
                            String s14 = cmdkeysinterface.getKeyUniqueId() + s13;
                            String s16 = "";
                            if(!s1.equals("0"))
                                s16 = " width=\"" + s1 + "\"";
                            if(!s3.equals("0"))
                                s16 = s16 + " height=\"" + s3 + "\"";
                            printwriter.print("<td id=\"" + s14 + "\" name=\"" + s14 + "\" " + s16);
                            if(flag1)
                                printwriter.print(" title=\"" + iclientaidkey.transformFKeyName(s13) + "\"");
                            String s17 = iclientaidkey.getKeyLabel();
                            if(flag3 && !s13.toUpperCase().equals(s17.toUpperCase()))
                                s17 = iclientaidkey.transformFKeyName(s13) + "=" + s17;
                            printwriter.println(" onmouseover=\"" + s4 + "(this);\" onmouseout=\"" + s5 + "(this);\" onClick=\"" + s6 + "(this);validateAndSubmit('" + s13 + "');\">" + HTMLStringTransform.transformKeyLabelString(iclientaidkey.transformFKeyName(s17)) + "<script language='JavaScript'>regCmdKey('" + s14 + "','" + s13 + "');</script></td>");
                        } else
                        {
                            k--;
                        }
                    } while(++k < i && iterator.hasNext());
                    for(; k < i; k++)
                        printwriter.print("<td width=\"" + s1 + "\" height=\"" + s3 + "\"></td>");

                }

                if(!flag)
                    printwriter.println("</table></td></tr></table>");
                else
                    printwriter.println("</table>");
            }
        }
        catch(Exception exception)
        {
            _trace.err(1, exception, "Exception within CmdKeysBuilderHelper.buildCmdKeys() : ");
            (new ErrorHandler(servletcontext, httpservletrequest, httpservletresponse, _trace, true)).handleError(exception, resourcebundle.getString("WF0083"));
        }
        catch(Throwable throwable)
        {
            _trace.err(1, throwable, "Throwable within CmdKeysBuilderHelper.buildCmdKeys() : ");
            (new ErrorHandler(servletcontext, httpservletrequest, httpservletresponse, _trace, true)).handleError(throwable, resourcebundle.getString("WF0083"));
        }
    }

    static final String copyRight = new String("(c) Copyright IBM Corporation 1999-2003, all rights reserved");
    ITraceLogger _trace;

}
