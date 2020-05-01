// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.help;

import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.controller.*;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingLevelCheckException;
import com.ibm.as400ad.webfacing.runtime.host.HostJobInfo;
import com.ibm.as400ad.webfacing.runtime.httpcontroller.HttpRequestHandler;
import com.ibm.as400ad.webfacing.runtime.httpcontroller.IHttpSessionVariable;
import com.ibm.as400ad.webfacing.runtime.view.*;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import com.ibm.etools.iseries.webfacing.runtime.platform.J2eeLevel;
import com.ibm.etools.iseries.webfacing.runtime.platform.J2eeServlet;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.help:
//            GenericHelpServlet, UIMHelpBean, AppHelpTable, HelpException, 
//            HelpNotFoundException, HelpDisplayBean, HelpRecord, HelpPanelGroup, 
//            IDisplayHelpInfo, UIMMap, IAppHelpTable, HelpDefinition

public class AppHelpServlet extends GenericHelpServlet
    implements IHttpSessionVariable
{

    public AppHelpServlet()
    {
        _trace = null;
        _screenBuilder = null;
        _displayOneAtATime = true;
        _uimDir = null;
        _uimMap = null;
        _out = null;
    }

    private IDisplayHelpInfo createHelpBean(HelpPanelGroup helppanelgroup)
        throws HelpException, HelpNotFoundException
    {
        String s = resolveFullPathHtml(helppanelgroup, null);
        UIMHelpBean uimhelpbean = new UIMHelpBean();
        uimhelpbean.setJspName(s);
        uimhelpbean.setBeanName(helppanelgroup.getDefinition());
        return uimhelpbean;
    }

    private IDisplayHelpInfo createHelpBean(String s)
        throws HelpException, HelpNotFoundException
    {
        UIMHelpBean uimhelpbean = new UIMHelpBean();
        uimhelpbean.setJspName(s);
        return uimhelpbean;
    }

    private IDisplayHelpInfo createHelpBean(HelpRecord helprecord, RecordBeanFactory recordbeanfactory)
        throws HelpException, HelpNotFoundException
    {
        try
        {
            resolveHelpObjLibrary(helprecord);
            com.ibm.as400ad.webfacing.runtime.model.RecordDataBean recorddatabean = recordbeanfactory.createRecordDataBean(helprecord);
            RecordViewBean recordviewbean = recordbeanfactory.createRecordViewBean(recorddatabean);
            AppHelpTable apphelptable = (AppHelpTable)getHelpLookup();
            apphelptable.addHelpGroupList(helprecord.getObject(), recordviewbean.getHelpGroups());
            setHelpLookup(apphelptable);
            return recordviewbean;
        }
        catch(WebfacingLevelCheckException webfacinglevelcheckexception)
        {
            throw new HelpException(webfacinglevelcheckexception.getMessage());
        }
        catch(HelpNotFoundException helpnotfoundexception)
        {
            throw helpnotfoundexception;
        }
        catch(Exception exception)
        {
            _trace.err(2, exception, "Exception occurred while loading the record bean for the online help.");
        }
        String s = WebfacingConstants.replaceSubstring(_resmri.getString("WF0078"), "&1", helprecord.getDefinition());
        s = WebfacingConstants.replaceSubstring(s, "&2", helprecord.getLibraryName() + "/" + helprecord.getObject());
        throw new HelpException(s);
    }

    private HelpDisplayBean createHelpDisplayBean(List list, RecordBeanFactory recordbeanfactory)
        throws HelpException, HelpNotFoundException
    {
        return createHelpDisplayBean(list, true, recordbeanfactory);
    }

    private HelpDisplayBean createHelpDisplayBean(List list, boolean flag, RecordBeanFactory recordbeanfactory)
        throws HelpException, HelpNotFoundException
    {
        HelpDisplayBean helpdisplaybean = new HelpDisplayBean(flag);
        StringBuffer stringbuffer = new StringBuffer((String)super._session.getAttribute("ApplicationTitle"));
        stringbuffer.append(" - ");
        String s = ((AppHelpTable)getHelpLookup()).getHelpTitle();
        if(s.equals(""))
            s = _resmri.getString("Help");
        stringbuffer.append(s);
        helpdisplaybean.setHelpTitle(stringbuffer.toString());
        helpdisplaybean.setHasExtendedHelp(((AppHelpTable)getHelpLookup()).hasExtendedHelp());
        if(list == null)
            return null;
        for(Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            Object obj = iterator.next();
            if(obj instanceof HelpRecord)
                helpdisplaybean.addHelpInfo(createHelpBean((HelpRecord)obj, recordbeanfactory));
            else
            if(obj instanceof HelpPanelGroup)
            {
                helpdisplaybean.setIsHelpRecord(false);
                helpdisplaybean.addHelpInfo(createHelpBean((HelpPanelGroup)obj));
            } else
            if(obj instanceof String)
            {
                helpdisplaybean.setIsHelpRecord(false);
                helpdisplaybean.addHelpInfo(createHelpBean((String)obj));
            } else
            {
                _trace.err(2, "Unexpected exception within AppHelpServlet.createHelpDisplayBean() : The help specification is neither a help record nor a help panel group.");
                throw new HelpException(_resmri.getString("WF0077"));
            }
        }

        return helpdisplaybean;
    }

    private void printNextPrevButtons()
    {
        _out.println("<tr>");
        _out.println("<td class=\"wf_font\" colspan=\"3\" rowspan=\"1\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" valign=\"top\">");
        _out.println("<table width=\"100%\">");
        _out.println("<tr>");
        _out.println("<td width=\"90%\">&nbsp;</td>");
        _out.println("<td width=\"5%\" class=\"helpprev\"><A href=\"WFHelp?ACTION=PREVIOUS\"><IMG border=\"0\" src=\"styles/transparent.gif\" alt=\"&lt;\" width='40' height='40'></A></td>");
        _out.println("<td width=\"5%\" class=\"helpnext\"><A href=\"WFHelp?ACTION=NEXT\"><IMG border =\"0\" src=\"styles/transparent.gif\" alt=\"&gt;\" width='40' height='40'></A></td>");
        _out.println("</tr>");
        _out.println("</table>");
        _out.println("</td>");
        _out.println("</tr>");
    }

    private void printTableOfContents(Iterator iterator)
    {
        _out.println("<div class=\"helpToC\">");
        _out.println("<b>" + _resmri.getString("Table_of_contents") + "</b>");
        String s1;
        String s2;
        for(; iterator.hasNext(); _out.println("<p><a href=\"#" + s2 + "\">" + s1 + "</a></p>"))
        {
            IDisplayHelpInfo idisplayhelpinfo = (IDisplayHelpInfo)iterator.next();
            String s = idisplayhelpinfo.getJspName();
            s1 = idisplayhelpinfo.getBeanName();
            int i = s.lastIndexOf(File.separator);
            s2 = s.substring(0, i + 1);
            s2 = WebfacingConstants.replaceSubstring(s2, File.separator, "/");
            s2 = s2 + s1;
        }

        _out.println("</div>");
        _out.println("<hr>");
    }

    private void printExtendedHelpLink()
    {
        _out.println("<a href=\"WFHelp?ACTION=EXTENDED\">" + _resmri.getString("Extended_help") + "</a>");
        _out.println("<hr>");
    }

    private void printBackToTopLink()
    {
        _out.println("<p><a href=\"#top\">" + _resmri.getString("Back_to_the_top") + "</a></p>");
    }

    private void displayHelp(HelpDisplayBean helpdisplaybean)
        throws HelpException, HelpNotFoundException
    {
        boolean flag = helpdisplaybean.isHelpRecord();
        List list = helpdisplaybean.getHelpInfoList();
        if(list == null || list.size() <= 0)
            return;
        try
        {
            super._response.setContentType("text/html; charset=utf-8");
            _out = super._response.getWriter();
        }
        catch(IOException ioexception)
        {
            _trace.err(2, ioexception, "IOException within AppHelpServlet.displayHelp().");
            throw new HelpException(_resmri.getString("WF0074"));
        }
        _out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\">");
        _out.println("<html>");
        _out.println("<head>");
        _out.println("<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        String s = flag ? "styles/apparea/apparea.css" : "UIMHelp/UIMHelp.css";
        _out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + s + "\">");
        _out.println("<title>" + helpdisplaybean.getHelpTitle() + "</title>");
        _out.println("</head>");
        _out.println("<body>");
        if(flag)
        {
            _out.println("<script language='JavaScript'>window.onerror = function(){return true;/* skip the error */}</script>");
            _out.println("<script language='JavaScript'>setCursor(row,col){}</script>");
            _out.println("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" frame=\"box\">");
            _out.println("<tbody>");
            _out.println("<tr>");
            _out.println("<td class=\"theapp\" colspan='3' rowspan='1'>");
            _out.println("<table class=\"wf_font\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
            _out.println("<tr height=\"0\" style=\"line-height:0%;\">");
            int i = _screenBuilder.getMaxRow();
            int j = _screenBuilder.getMaxColumn();
            for(int k = 1; k <= j; k++)
                _out.print("<TD>&nbsp;</TD>");

            _out.println("</tr>");
            int l = 0;
            boolean flag2 = false;
            for(Iterator iterator1 = list.iterator(); iterator1.hasNext();)
            {
                IDisplayHelpInfo idisplayhelpinfo1 = (IDisplayHelpInfo)iterator1.next();
                if(idisplayhelpinfo1.getBeanName() != null && idisplayhelpinfo1.getBeanValue() != null)
                {
                    int k1 = idisplayhelpinfo1.getFirstDisplayLine();
                    if(k1 > 1)
                    {
                        for(int i1 = 1; i1 < k1; i1++)
                            _out.println("<tr><td>&nbsp;</td></tr>");

                    }
                    try
                    {
                        String s1 = idisplayhelpinfo1.getBeanName();
                        String s2 = idisplayhelpinfo1.getJspName();
                        super._session.setAttribute(s1, idisplayhelpinfo1.getBeanValue());
                        super._request.setAttribute(s1, idisplayhelpinfo1.getBeanValue());
                        super._servletContext.getRequestDispatcher(s2).include(super._request, super._response);
                        super._session.removeAttribute(s1);
                    }
                    catch(Exception exception)
                    {
                        _trace.err(2, exception, "Exception within AppHelpServlet.displayHelp() while including the help record jsp.");
                        throw new HelpException(_resmri.getString("WF0075"));
                    }
                    l = idisplayhelpinfo1.getLastDisplayLine();
                }
            }

            for(int j1 = l; j1 < i; j1++)
                _out.println("<tr><td>&nbsp;</td></tr>");

            _out.println("</table>");
            if(helpdisplaybean.isDisplayOneAtATime())
                printNextPrevButtons();
            _out.println("</td>");
            _out.println("</tr>");
            _out.println("</tbody>");
            _out.println("</table>");
        } else
        {
            boolean flag1 = list.size() > 1;
            if(flag1)
                printTableOfContents(list.iterator());
            else
            if(helpdisplaybean.hasExtendedHelp())
                printExtendedHelpLink();
            for(Iterator iterator = list.iterator(); iterator.hasNext();)
            {
                IDisplayHelpInfo idisplayhelpinfo = (IDisplayHelpInfo)iterator.next();
                displayHelp(idisplayhelpinfo.getJspName());
                if(flag1)
                {
                    printBackToTopLink();
                    _out.println("<hr>");
                }
            }

        }
        _out.println("</body>");
        _out.println("</html>");
    }

    private void displayHelp(String s)
        throws HelpException, HelpNotFoundException
    {
        try
        {
            String s1 = null;
            File file = new File(_uimDir, s);
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            for(String s3 = bufferedreader.readLine(); s3.indexOf("<body>") == -1; s3 = bufferedreader.readLine());
            for(String s4 = bufferedreader.readLine(); s4 != null; s4 = bufferedreader.readLine())
            {
                if(s4.indexOf("<a href") == 0)
                {
                    if(s4.indexOf("&HtmlFile") > 0 && s1 != null)
                    {
                        s4 = WebfacingConstants.replaceSubstring(s4, "&HtmlFile", s1);
                        s1 = null;
                    }
                    _out.println(s4);
                    continue;
                }
                if(s4.indexOf("</body>") == 0)
                    break;
                if(s4.indexOf("<!-- externlink") == -1 && s4.indexOf("<!-- include") == -1)
                {
                    _out.println(s4);
                } else
                {
                    String s5 = null;
                    String s6 = null;
                    for(StringTokenizer stringtokenizer = new StringTokenizer(s4, " ,=,<,>,!,-"); stringtokenizer.hasMoreTokens();)
                    {
                        String s7 = stringtokenizer.nextToken();
                        if(s7.equals("NAME"))
                            s5 = WebfacingConstants.trimQuotes(stringtokenizer.nextToken());
                        else
                        if(s7.equals("PNLGRP"))
                            s6 = WebfacingConstants.trimQuotes(stringtokenizer.nextToken());
                    }

                    if(s6 == null)
                        s1 = WebfacingConstants.replaceSubstring(s5, File.separator, "/");
                    else
                        s1 = resolveFullPathHtml(new HelpPanelGroup(s6), s5);
                    if(s4.indexOf("<!-- include") == 0)
                        displayHelp(s1);
                }
            }

            bufferedreader.close();
        }
        catch(IOException ioexception)
        {
            _trace.err(2, ioexception, "Unexpected IOException within AppHelpServlet.displayHelp() while loading the HTML for UIM help.");
            String s2 = WebfacingConstants.replaceSubstring(_resmri.getString("WF0121"), "&1", s);
            throw new HelpException(s2);
        }
    }

    public void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws ServletException, IOException
    {
        J2eeServlet._j2eeLevel.wfSetCharacterEncoding(httpservletrequest);
        httpservletresponse.setHeader("Cache-Control", "no-cache");
        httpservletresponse.setHeader("Pragma", "no-cache");
        super._request = httpservletrequest;
        super._response = httpservletresponse;
        super._session = httpservletrequest.getSession();
        super._servletContext = getServletContext();
        HttpRequestHandler.updateWFSession(super._session, super._servletContext);
        _trace = WFSession.getTraceLogger();
        _screenBuilder = (ScreenBuilderModel)super._session.getAttribute("screenbuilder");
        _uimDir = super._servletContext.getRealPath("/") + System.getProperty("file.separator") + "UIMHelp" + System.getProperty("file.separator");
        _uimMap = new UIMMap(_uimDir);
        doHelp(RecordBeanFactory.getRecordBeanFactory(super._servletContext));
    }

    private void doHelp(RecordBeanFactory recordbeanfactory)
    {
        try
        {
            Object obj = null;
            String s = super._request.getParameter("ACTION");
            if(s == null)
            {
                Object obj1 = retrieveKeyParm();
                setHelpLookup(retrieveHelpLookup());
                obj = getHelpLookup().getOnlineHelp(obj1);
            } else
            if(s.toUpperCase().equals("PREVIOUS"))
                obj = getHelpLookup().findPrevious();
            else
            if(s.toUpperCase().equals("NEXT"))
                obj = getHelpLookup().findNext();
            else
            if(s.toUpperCase().equals("EXTENDED"))
                obj = getHelpLookup().getExtendedHelp();
            else
            if(s.toUpperCase().equals("LINK"))
            {
                String s1 = super._request.getParameter("NAME");
                String s2 = super._request.getParameter("PNLGRP");
                obj = new ArrayList(1);
                if(s2 == null)
                    ((List)obj).add(s1);
                else
                    ((List)obj).add(resolveFullPathHtml(new HelpPanelGroup(s2), s1));
            } else
            {
                _trace.err(2, "Unexpected exception within AppHelpServlet.doHelp() : The help request is not one of the following types: find primary help, find next help, find previous help.");
                throw new HelpException(_resmri.getString("WF0077"));
            }
            HelpDisplayBean helpdisplaybean = createHelpDisplayBean((List)obj, recordbeanfactory);
            displayHelp(helpdisplaybean);
        }
        catch(HelpNotFoundException helpnotfoundexception)
        {
            handleHelpException(helpnotfoundexception);
        }
        catch(HelpException helpexception)
        {
            handleHelpException(helpexception);
        }
        catch(Exception exception)
        {
            _trace.err(2, exception, "Uncaught exception within AppHelpServlet.doHelp().");
        }
        finally
        {
            WFSession.clearSessionData();
        }
    }

    public void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws ServletException, IOException
    {
        doGet(httpservletrequest, httpservletresponse);
    }

    public IAppHelpTable getHelpLookup()
        throws HelpException
    {
        AppHelpTable apphelptable = (AppHelpTable)super._session.getAttribute("appHelpTable");
        if(apphelptable != null)
        {
            return apphelptable;
        } else
        {
            _trace.err(2, "Unable to retrieve the application help table stored in the session.");
            throw new HelpException(_resmri.getString("WF0079"));
        }
    }

    private HostJobInfo getJobInfoRequestor()
    {
        return WFSession.getJobInfoRequestor();
    }

    public void handleHelpException(HelpException helpexception)
    {
        ErrorHandler errorhandler = new ErrorHandler(super._servletContext, super._request, super._response, _trace);
        errorhandler.handleError(helpexception, helpexception.getMessage());
    }

    public void handleHelpException(HelpNotFoundException helpnotfoundexception)
    {
        PrintWriter printwriter = null;
        try
        {
            super._response.setContentType("text/html; charset=utf-8");
            printwriter = super._response.getWriter();
        }
        catch(IOException ioexception)
        {
            _trace.err(2, ioexception, "IOException within AppHelpServlet.handleHelpException() while displaying error messages in the help window.");
            return;
        }
        printwriter.println("<script language='JavaScript' src='ClientScript/webface.js'></script>");
        String s = System.getProperty("file.separator");
        String s1 = super._servletContext.getRealPath("/") + s + "ClientScript" + s + "usr" + s;
        try
        {
            File afile[] = (new File(s1)).listFiles();
            if(null != afile)
            {
                for(int i = 0; i < afile.length; i++)
                    if(null != afile[i] && afile[i].isFile() && !afile[i].isHidden())
                        printwriter.println("<script language='JavaScript' src='ClientScript/usr/" + afile[i].getName() + "'></script>");

            }
        }
        catch(Throwable throwable) { }
        printwriter.println("<script language='JavaScript'>");
        printwriter.println("document.write(" + helpnotfoundexception.getJavaScriptHandler() + ");");
        printwriter.println("</script>");
    }

    public boolean isDisplayOneAtATime()
    {
        return _displayOneAtATime;
    }

    private void resolveHelpObjLibrary(HelpDefinition helpdefinition)
        throws HelpNotFoundException
    {
        if(helpdefinition.needCurrentDSPF())
            helpdefinition.setLibraryObject(_screenBuilder.getDSPFObject());
        else
        if(helpdefinition.needToResolve())
            try
            {
                String s = getJobInfoRequestor().getObjLibName(helpdefinition.getObject(), helpdefinition.getLibraryName(), helpdefinition.getType(), false);
                helpdefinition.setLibraryName(s.substring(10).trim());
            }
            catch(Exception exception)
            {
                _trace.err(2, exception, "Unable to resolve to the library for the display file or panel group that contains help.");
                throw new HelpNotFoundException(1);
            }
        else
            return;
    }

    public IAppHelpTable retrieveHelpLookup()
        throws HelpException
    {
        AppHelpTable apphelptable = _screenBuilder.getHelpTable();
        return (IAppHelpTable)apphelptable.clone();
    }

    public Object retrieveKeyParm()
    {
        String s = super._request.getParameter("CURSOR").trim();
        CursorPosition cursorposition = new CursorPosition(s);
        IScreenBuilder iscreenbuilder = (IScreenBuilder)super._session.getAttribute("screenbuilder");
        return iscreenbuilder.getLocationOnDeviceAt(cursorposition);
    }

    public void setDisplayOneAtATime(boolean flag)
    {
        _displayOneAtATime = flag;
    }

    public void setHelpLookup(IAppHelpTable iapphelptable)
    {
        super._session.setAttribute("appHelpTable", iapphelptable);
    }

    protected String resolveFullPathHtml(HelpPanelGroup helppanelgroup, String s)
        throws HelpException, HelpNotFoundException
    {
        resolveHelpObjLibrary(helppanelgroup);
        String s1;
        if(s == null)
            s1 = _uimMap.getQualifiedPath(helppanelgroup);
        else
        if(s.indexOf(".htm") == -1)
        {
            helppanelgroup.setDefinition(s);
            s1 = _uimMap.getQualifiedPath(helppanelgroup);
        } else
        {
            s1 = _uimMap.getQualifiedPath(helppanelgroup, s);
        }
        if(s1.indexOf(".htm") == -1)
        {
            _trace.err(2, "Unable to find the HTML file for the help module.");
            String s2;
            if(s == null)
            {
                s2 = WebfacingConstants.replaceSubstring(_resmri.getString("WF0076"), "&1", helppanelgroup.getDefinition());
                s2 = WebfacingConstants.replaceSubstring(s2, "&2", helppanelgroup.getLibraryName() + "/" + helppanelgroup.getObject());
            } else
            {
                s2 = WebfacingConstants.replaceSubstring(_resmri.getString("WF0121"), "&1", s);
            }
            throw new HelpException(s2);
        } else
        {
            return s1;
        }
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2001-2003, all rights reserved");
    private static ResourceBundle _resmri;
    private static final String SERVLET_NAME = "WFHelp";
    protected ITraceLogger _trace;
    private ScreenBuilderModel _screenBuilder;
    private boolean _displayOneAtATime;
    private String _uimDir;
    private UIMMap _uimMap;
    private PrintWriter _out;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
