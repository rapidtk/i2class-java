// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.httpcontroller;

import com.ibm.as400ad.webfacing.common.InvocationProperties;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.controller.*;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.runtime.dhtmlview.HtmlReportGenerator;
import com.ibm.as400ad.webfacing.runtime.host.*;
import com.ibm.as400ad.webfacing.runtime.model.IRecordData;
import com.ibm.as400ad.webfacing.runtime.view.*;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.httpcontroller:
//            HttpRequestHandler, LogonRequestHandler

public class ControllerRequestHandler extends HttpRequestHandler
    implements IWFServerProtocol
{
    public class FieldNameValuePair
        implements IFieldValue
    {

        public String record()
        {
            return _fieldName.getRecordName();
        }

        public String field()
        {
            return _fieldName.getFieldName();
        }

        public String value()
        {
            return _value;
        }

        public boolean isBLANKSSatisfied()
        {
            return _value.trim().equals("");
        }

        QualifiedFieldName _fieldName;
        String _value;

        public FieldNameValuePair(QualifiedFieldName qualifiedfieldname, String s)
        {
            _fieldName = qualifiedfieldname;
            _value = s;
        }
    }

    private class FieldValueEnumeration
        implements Enumeration
    {

        private boolean notAValidFieldElement(String s)
        {
            return s.equals("AID") || s.equals("SFLRRN") || s.equals("PAGEID") || s.equals("CURSOR") || s.equals("INSERTMODE") || s.equals("W_WIDTH") || s.equals("INPUTFIELD_HEIGHT") || s.equals("ENCODING_TEST") || s.equals("MNUDDS_OPTION") || s.equals("LOGOFF");
        }

        public boolean hasMoreElements()
        {
            if(null != _peekAhead)
                return true;
            while(_requestParms.hasMoreElements()) 
            {
                String s = (String)_requestParms.nextElement();
                if(!notAValidFieldElement(s))
                {
                    _peekAhead = createFieldValue(s);
                    return true;
                }
            }
            return false;
        }

        public Object nextElement()
        {
            if(null != _peekAhead)
            {
                IFieldValue ifieldvalue = _peekAhead;
                _peekAhead = null;
                return ifieldvalue;
            }
            while(_requestParms.hasMoreElements()) 
            {
                String s = (String)_requestParms.nextElement();
                if(!notAValidFieldElement(s))
                    return createFieldValue(s);
            }
            return null;
        }

        private IFieldValue createFieldValue(String s)
        {
            String as[] = _request.getParameterValues(s);
            String s1 = as[0];
            if(_forceUTF8)
                s1 = WebfacingConstants.toUTF8String(s1);
            return new FieldNameValuePair(new QualifiedFieldName(s), s1);
        }

        private Enumeration _requestParms;
        private IFieldValue _peekAhead;

        public FieldValueEnumeration(HttpServletRequest httpservletrequest)
        {
            _peekAhead = null;
            _requestParms = httpservletrequest.getParameterNames();
        }
    }

    static class QualifiedFieldName
    {

        String getRecordName()
        {
            return _recordName;
        }

        private String deletePrefix(String s)
        {
            if(s != null && s.length() > 0)
            {
                int i = s.indexOf("_");
                if(s.charAt(0) == 'l' && i >= 2)
                {
                    String s1 = s.substring(1, i);
                    int j = 0;
                    boolean flag;
                    for(flag = true; j < s1.length() && flag; j++)
                    {
                        if(Character.isDigit(s1.charAt(j)))
                            continue;
                        flag = false;
                        break;
                    }

                    if(flag)
                        s = s.substring(i + 1);
                }
            }
            return s;
        }

        String getFieldName()
        {
            return _fieldName;
        }

        private String _recordName;
        private String _fieldName;

        QualifiedFieldName(String s)
        {
            _recordName = "";
            _fieldName = "";
            s = deletePrefix(s);
            int i = s.indexOf("$");
            if(i > 0)
            {
                _recordName = s.substring(0, i);
                _fieldName = s.substring(i + 1);
            } else
            {
                _fieldName = s;
            }
        }
    }


    public ControllerRequestHandler(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, ServletContext servletcontext)
    {
        super(httpservletrequest, httpservletresponse, servletcontext);
        _forceUTF8 = false;
        if(HttpRequestHandler.getSessionValue(super._session, "FirstScreen") != null)
            HttpRequestHandler.updateWFSession(super._session, servletcontext);
        super._trace = WFSession.getTraceLogger();
        if(HttpRequestHandler.getSessionValue(super._session, "FORCE_UTF8") != null && ((Boolean)HttpRequestHandler.getSessionValue(super._session, "FORCE_UTF8")).booleanValue())
            _forceUTF8 = true;
    }

    private boolean checkEncoding()
    {
        String s = super._request.getParameter("ENCODING_TEST");
        boolean flag = false;
        if(s != null)
        {
            if(s.length() == 1 && s.equals("\u3310"))
                flag = true;
            else
            if(_forceUTF8)
            {
                s = WebfacingConstants.toUTF8String(s);
                if(s.length() == 1 && s.equals("\u3310"))
                    flag = true;
            }
        } else
        {
            flag = true;
        }
        if(!flag)
        {
            super._trace.err(2, "Failed UTF-8 encoding check character length = " + s.length());
            byte abyte0[];
            try
            {
                abyte0 = s.getBytes("UTF-8");
            }
            catch(Throwable throwable)
            {
                abyte0 = null;
            }
            if(null != abyte0 && abyte0.length > 0)
            {
                String s1 = "";
                for(int i = 0; i < abyte0.length; i++)
                {
                    Byte byte1 = new Byte(abyte0[i]);
                    int j = byte1.intValue() & 0xff;
                    String s2 = Integer.toHexString(j);
                    if(s2.length() < 2)
                        s2 = "0" + s2;
                    s1 = s1 + s2;
                }

                super._trace.err(2, "Failed UTF-8 encoding check character (hex) = " + s1);
            }
        }
        return flag;
    }

    private void displayScreen()
        throws WebfacingInternalException
    {
        HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
        hostjobinfo.submitRequests();
        Boolean boolean1 = (Boolean)HttpRequestHandler.getSessionValue(super._session, "FirstScreen");
        if(null == boolean1)
            boolean1 = new Boolean(true);
        if(boolean1.booleanValue())
        {
            PrintWriter printwriter;
            try
            {
                super._response.setContentType("text/html; charset=UTF-8");
                printwriter = super._response.getWriter();
            }
            catch(IOException ioexception)
            {
                throw new WebfacingInternalException(WebfacingConstants.replaceSubstring(HttpRequestHandler._resmri.getString("WF0024"), "&1", "ControllerRequestHandler.displayScreen()"));
            }
            printwriter.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\">");
            printwriter.println("<html>");
            printwriter.println("<head>");
            printwriter.println("<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
            printwriter.println("<title>" + HttpRequestHandler.getSessionValue(super._session, "ApplicationTitle") + "</title>");
            printwriter.println("<script language='JavaScript'> window.status = 'Opening page...'; </script> ");
            printwriter.println("</head>");
            printwriter.println("<script language='JavaScript'> ");
            printwriter.println(" logoff = function(obj) { ");
            printwriter.println(" try{window.opener.closeWinListner();}catch(anyexp){}");
            printwriter.println(" if (window.event.clientX < 0){");
            printwriter.println("     obj.sender.document.open();");
            printwriter.println("     obj.sender.document.writeln(\"<html>\");");
            printwriter.println("     obj.sender.document.writeln(\"<head>\");");
            printwriter.println("     obj.sender.document.writeln(\"<META http-equiv=Content-Type content='text/html; charset=UTF-8'>\");");
            printwriter.println("     obj.sender.document.writeln(\"</head>\");");
            printwriter.println("     obj.sender.document.writeln(\"<BODY>\");");
            printwriter.println("     obj.sender.document.writeln(\"<FORM NAME=WINDOWCLOSE METHOD=POST ACTION='WebFacing'>\");");
            printwriter.println("     obj.sender.document.writeln(\"<INPUT NAME='LOGOFF'  SIZE=30 VALUE='LOGOFF'> \");");
            printwriter.println("     obj.sender.document.writeln(\"</FORM>\");");
            printwriter.println("     obj.sender.document.writeln(\"</BODY>\");");
            printwriter.println("     obj.sender.document.writeln(\"</html>\");");
            printwriter.println("     obj.sender.document.close();");
            printwriter.println("     obj.sender.document.WINDOWCLOSE.submit();");
            printwriter.println("   alert('" + HttpRequestHandler._resmri.getString("Close_window") + "'); ");
            printwriter.println(" } ");
            printwriter.println("}</script>");
            printwriter.println("<frameset name=\"main\" rows=\"*,100%\" border=\"0\" onunload=\"logoff(this);\">");
            printwriter.println("<frame name=\"sender\" src=\"styles/m-t.html\">");
            printwriter.println("<frame name=\"app\" src=\"WebFacing\">");
            printwriter.println("</frameset>");
            printwriter.println("</html>");
            HttpRequestHandler.putSessionValue(super._session, "FirstScreen", new Boolean(true));
        } else
        {
            try
            {
                super._servletContext.getRequestDispatcher("/styles/chrome/PageBuilder.jsp").forward(super._request, super._response);
            }
            catch(Exception exception)
            {
                if(!super._response.isCommitted())
                {
                    String s = WebfacingConstants.replaceSubstring(HttpRequestHandler._resmri.getString("WF0025"), "&1", "styles");
                    s = WebfacingConstants.replaceSubstring(s, "&2", "chrome");
                    throw new WebfacingInternalException(s);
                }
            }
        }
    }

    private int getControllerState()
    {
        Integer integer = (Integer)HttpRequestHandler.getSessionValue(super._session, "controllerState");
        if(null == integer)
        {
            integer = new Integer(3);
            setControllerState(3);
        }
        return integer.intValue();
    }

    private RecordBeanFactory getRecordBeanFactory()
    {
        return RecordBeanFactory.getRecordBeanFactory(super._servletContext);
    }

    private ScreenBuilderModel getScreenBuilderModel()
    {
        ScreenBuilderModel screenbuildermodel = (ScreenBuilderModel)HttpRequestHandler.getSessionValue(super._session, "screenbuilder");
        if(null == screenbuildermodel)
        {
            RecordBeanFactory recordbeanfactory = getRecordBeanFactory();
            screenbuildermodel = new ScreenBuilderModel();
            HttpRequestHandler.putSessionValue(super._session, "screenbuilder", screenbuildermodel);
        }
        return screenbuildermodel;
    }

    private int handleApplicationRequest(DataInputStream datainputstream, WFConnection wfconnection, RecordBeanFactory recordbeanfactory)
        throws WebfacingInternalException, WFApplicationRuntimeError, WebfacingLevelCheckException, WFUnsupportedException, IOException, WFCommunicationsException
    {
        ApplicationRequestHandler applicationrequesthandler = new ApplicationRequestHandler(getScreenBuilderModel());
        boolean flag = true;
        if(HttpRequestHandler.getSessionValue(super._session, "FORCE_DFRWRT") != null)
            flag = ((Boolean)HttpRequestHandler.getSessionValue(super._session, "FORCE_DFRWRT")).booleanValue();
        boolean flag1 = HttpRequestHandler.showVisibleLabelsOnly(super._servletContext);
        byte byte0;
        switch(applicationrequesthandler.processApplicationRequests(datainputstream, flag, recordbeanfactory, flag1))
        {
        case 0: // '\0'
            displayScreen();
            byte0 = 2;
            break;

        case 1: // '\001'
            ADBDInputBuffer adbdinputbuffer = new ADBDInputBuffer(getScreenBuilderModel(), true);
            wfconnection.putData(adbdinputbuffer);
            byte0 = 1;
            break;

        case 2: // '\002'
            byte0 = 1;
            break;

        case 3: // '\003'
            HttpRequestHandler.putSessionValue(super._session, "ImmediateWriteRequest", "ImmediateWriteRequest");
            displayScreen();
            byte0 = 4;
            break;

        default:
            throw new WebfacingInternalException(HttpRequestHandler._resmri.getString("WF0028"));
        }
        return byte0;
    }

    private int handleHostRequestErrors(Exception exception)
    {
        byte byte0 = 2;
        if(exception instanceof IOException)
        {
            super._trace.err(2, "While processing application requests received " + exception.toString());
            getErrorHandler().handleError(exception, HttpRequestHandler._resmri.getString("WF0096"), HttpRequestHandler._resmri.getString("WF0097"));
        } else
        if(exception instanceof WFCommunicationsException)
        {
            super._trace.err(2, exception, "Communications error " + exception.toString());
            getErrorHandler().handleError(exception, HttpRequestHandler._resmri.getString("WF0096"), exception.getMessage());
        } else
        if(exception instanceof WFApplicationRuntimeError)
        {
            WFApplicationRuntimeError wfapplicationruntimeerror = (WFApplicationRuntimeError)exception;
            super._trace.err(2, "While processing application requests received " + exception.toString());
            getErrorHandler().handleError(exception, WebfacingConstants.replaceSubstring(HttpRequestHandler._resmri.getString("WF0098"), "&1", wfapplicationruntimeerror.getMsgid()), HttpRequestHandler._resmri.getString("WF0099"));
        } else
        if(exception instanceof WebfacingLevelCheckException)
        {
            super._trace.err(2, "While processing application requests received " + exception.toString());
            getErrorHandler().handleError(exception, HttpRequestHandler._resmri.getString("WF0096"), HttpRequestHandler._resmri.getString("WF0100"));
        } else
        if(exception instanceof WebfacingInternalException)
        {
            super._trace.err(2, "While processing application requests received " + exception.toString());
            getErrorHandler().handleError(exception, HttpRequestHandler._resmri.getString("WF0096"), exception.getMessage());
        } else
        if(exception instanceof WFUnsupportedException)
        {
            super._trace.err(2, "While processing application requests received " + exception.toString());
            getErrorHandler().handleError(exception, exception.getMessage(), HttpRequestHandler._resmri.getString("WF0096"));
        } else
        if(exception instanceof Exception)
        {
            super._trace.err(2, "While processing application requests received " + exception.toString());
            getErrorHandler().handleError(exception, HttpRequestHandler._resmri.getString("WF0096"), HttpRequestHandler._resmri.getString("WF0102"));
        }
        return byte0;
    }

    public void handleRequest(RecordBeanFactory recordbeanfactory)
        throws IOException, ServletException
    {
        int i;
        try
        {
            if(!sendFirstScreen())
                do
                {
                    i = processStateTransition(recordbeanfactory);
                    setControllerState(i);
                } while(i != 2 && i != 4);
        }
        finally
        {
            if(HttpRequestHandler.getSessionValue(super._session, "FirstScreen") == null || !((Boolean)HttpRequestHandler.getSessionValue(super._session, "FirstScreen")).booleanValue())
                WFSession.clearSessionData();
        }
    }

    private int processBrowserRequest()
    {
        try
        {
            String s = null;
            String s1 = super._request.getParameter("AID");
            if(s1 != null)
            {
                StringTokenizer stringtokenizer = new StringTokenizer(s1, ":");
                s = stringtokenizer.nextToken();
            }
            String s2 = super._request.getParameter("LOGOFF");
            if(s2 != null && s2.equalsIgnoreCase("LOGOFF") || s != null && s.equalsIgnoreCase("LOGOFF"))
            {
                WFConnection wfconnection = (WFConnection)HttpRequestHandler.getSessionValue(super._session, "WFConnection");
                if(wfconnection == null)
                {
                    return 2;
                } else
                {
                    wfconnection.logoff();
                    return 1;
                }
            }
            if(!checkEncoding())
            {
                super._trace.err(1, "The check field was received incorrectly by the application server.\n This is likely caused by the client encoding being incorrect.\n");
                getErrorHandler().handleError(HttpRequestHandler._resmri.getString("WF0116"));
                return 2;
            }
            String s3 = super._request.getParameter("PAGEID");
            String s4 = super._session.getAttribute("pageId").toString();
            if(!s4.equals(s3))
                return 5;
            String s5 = super._request.getParameter("SpecialRequest");
            if(s5 != null)
            {
                if(s5.equals("DSPFFD"))
                    HtmlReportGenerator.writeDDSDetails(super._response, (String)HttpRequestHandler.getSessionValue(super._session, "ApplicationTitle"), getScreenBuilderModel());
                if(!s5.equals("Debug"));
                return 2;
            }
            InputDeviceRequestHandler inputdevicerequesthandler = new InputDeviceRequestHandler(getScreenBuilderModel());
            String s6 = super._request.getParameter("W_WIDTH");
            if(s6 != null)
                HttpRequestHandler.putSessionValue(super._session, "WWidth", s6);
            String s7 = super._request.getParameter("INSERTMODE");
            if(s7 != null)
                HttpRequestHandler.putSessionValue(super._session, "INSERTMODE", s7);
            String s8 = super._request.getParameter("SFLRRN");
            String s9 = super._request.getParameter("CURSOR").trim();
            String s10 = super._request.getParameter("MNUDDS_OPTION");
            if(s != null)
            {
                boolean flag = inputdevicerequesthandler.processDeviceInputRequest(s10, s, new CursorPosition(s9), new FieldValueEnumeration(super._request));
                if(flag)
                {
                    displayScreen();
                    return 2;
                } else
                {
                    ADBDInputBuffer adbdinputbuffer = new ADBDInputBuffer(getScreenBuilderModel(), false);
                    WFConnection wfconnection1 = (WFConnection)HttpRequestHandler.getSessionValue(super._session, "WFConnection");
                    wfconnection1.putData(adbdinputbuffer);
                    return 1;
                }
            }
            if(s8 != null)
            {
                StringTokenizer stringtokenizer1 = new StringTokenizer(s8, ":");
                String s11 = stringtokenizer1.nextToken();
                Integer integer = new Integer(stringtokenizer1.nextToken());
                if(!inputdevicerequesthandler.processRelativeRecordNumberRequest(s11, integer.intValue(), new CursorPosition(s9), new FieldValueEnumeration(super._request)))
                    requestSubfilePage(s11, integer.intValue());
                displayScreen();
                return 2;
            } else
            {
                return 0;
            }
        }
        catch(WebfacingLevelCheckException webfacinglevelcheckexception)
        {
            super._trace.err(2, "Level Check Exception while writing input buffer back to the WebFacing Server. \n" + webfacinglevelcheckexception.toString());
            getErrorHandler().handleError(webfacinglevelcheckexception, HttpRequestHandler._resmri.getString("WF0088"), HttpRequestHandler._resmri.getString("WF0089"));
        }
        catch(WFInvalidSessionException wfinvalidsessionexception)
        {
            super._trace.err(2, "Internal Exception while building Host Application Response. \n" + wfinvalidsessionexception.toString());
            return 3;
        }
        catch(WebfacingInternalException webfacinginternalexception)
        {
            super._trace.err(2, "Internal Exception while building Host Application Response. \n" + webfacinginternalexception.toString());
            getErrorHandler().handleError(webfacinginternalexception, HttpRequestHandler._resmri.getString("WF0088"), HttpRequestHandler._resmri.getString("WF0090"));
        }
        catch(IOException ioexception)
        {
            super._trace.err(2, "IO Exception while writing input buffer back to the WebFacing Server. \n" + ioexception.toString());
            getErrorHandler().handleError(ioexception, HttpRequestHandler._resmri.getString("WF0088"), HttpRequestHandler._resmri.getString("WF0091"));
        }
        catch(Exception exception)
        {
            super._trace.err(2, "Unexpected exception while building Host Application Response. \n" + exception.toString());
            getErrorHandler().handleError(exception, HttpRequestHandler._resmri.getString("WF0092"), HttpRequestHandler._resmri.getString("WF0093"));
        }
        return 2;
    }

    public void processEndOfSession()
        throws WebfacingInternalException
    {
        processEndOfApplication();
        HttpRequestHandler.removeWebAppSessionAttributes(super._session);
    }

    private int processHostData(WFConnection wfconnection, RecordBeanFactory recordbeanfactory)
    {
        int i = 2;
        try
        {
            DataInputStream datainputstream = new DataInputStream(wfconnection.getData());
            int j = (new Byte(datainputstream.readByte())).intValue();
            switch(j)
            {
            default:
                break;

            case 12: // '\f'
                processEndOfApplication();
                break;

            case 18: // '\022'
                processEndOfSession();
                break;

            case 9: // '\t'
                i = handleApplicationRequest(datainputstream, wfconnection, recordbeanfactory);
                break;

            case 8: // '\b'
                String s;
                try
                {
                    s = super._request.getParameter("inv");
                }
                catch(Exception exception1)
                {
                    throw new WebfacingInternalException(HttpRequestHandler._resmri.getString("WF0026"));
                }
                InvocationProperties invocationproperties = LogonRequestHandler.getInvocationProperties(super._servletContext, s);
                String s1 = invocationproperties.getCLCommand(super._request).trim();
                getErrorHandler().handleError(HttpRequestHandler._resmri.getString("WF0086"), WebfacingConstants.replaceSubstring(HttpRequestHandler._resmri.getString("WF0087"), "&1", s1));
                break;
            }
        }
        catch(Exception exception)
        {
            i = handleHostRequestErrors(exception);
        }
        return i;
    }

    private int processImmediateWriteBrowserRequest()
    {
        ScreenBuilderModel screenbuildermodel = getScreenBuilderModel();
        screenbuildermodel.reverseProtectForImmediateWrite();
        return 1;
    }

    private int processStateTransition(RecordBeanFactory recordbeanfactory)
    {
        int i = 2;
        WFConnection wfconnection = (WFConnection)HttpRequestHandler.getSessionValue(super._session, "WFConnection");
        switch(getControllerState())
        {
        case 5: // '\005'
            i = processRefresh();
            break;

        case 1: // '\001'
            i = processHostData(wfconnection, recordbeanfactory);
            break;

        case 2: // '\002'
            i = processBrowserRequest();
            break;

        case 4: // '\004'
            i = processImmediateWriteBrowserRequest();
            break;

        case 3: // '\003'
            getErrorHandler().handleError(WebfacingConstants.replaceSubstring(HttpRequestHandler._resmri.getString("WF0094"), "&1", Integer.toString(super._session.getMaxInactiveInterval() / 60)));
            i = 2;
            break;
        }
        return i;
    }

    private void setControllerState(int i)
    {
        HttpRequestHandler.putSessionValue(super._session, "controllerState", new Integer(i));
    }

    private int processRefresh()
    {
        try
        {
            displayScreen();
        }
        catch(Exception exception)
        {
            super._trace.err(2, "While refreshing the screen received " + exception.toString());
            getErrorHandler().handleError(exception, HttpRequestHandler._resmri.getString("WF0119"), exception.getMessage());
        }
        return 2;
    }

    private boolean sendFirstScreen()
    {
        Boolean boolean1 = (Boolean)HttpRequestHandler.getSessionValue(super._session, "FirstScreen");
        if(null != boolean1 && boolean1.booleanValue())
        {
            HttpRequestHandler.putSessionValue(super._session, "FirstScreen", new Boolean(false));
            try
            {
                displayScreen();
            }
            catch(WebfacingInternalException webfacinginternalexception) { }
            return true;
        } else
        {
            return false;
        }
    }

    private void requestSubfilePage(String s, int i)
    {
        try
        {
            ScreenBuilderModel screenbuildermodel = getScreenBuilderModel();
            SubfileControlRecordViewBean subfilecontrolrecordviewbean = screenbuildermodel.getSFLCTLRecordViewBeanForPaging(s);
            ADBDInputBuffer adbdinputbuffer = new ADBDInputBuffer(getScreenBuilderModel(), subfilecontrolrecordviewbean.getFeedbackBean().getRecordData().getInputBufferSaveArea(), screenbuildermodel.isSubfileChanged(s), i, subfilecontrolrecordviewbean.getMaximumVisibleRecordSize());
            WFConnection wfconnection = (WFConnection)HttpRequestHandler.getSessionValue(super._session, "WFConnection");
            wfconnection.putData(adbdinputbuffer);
            DataInputStream datainputstream = new DataInputStream(wfconnection.getData());
            int j = (new Byte(datainputstream.readByte())).intValue();
            if(j != 9)
                throw new WebfacingInternalException(WebfacingConstants.replaceSubstring(HttpRequestHandler._resmri.getString("WF0049"), "&1", String.valueOf(j)));
            ADBDOutputBuffer adbdoutputbuffer = new ADBDOutputBuffer(datainputstream, subfilecontrolrecordviewbean, i);
        }
        catch(WebfacingInternalException webfacinginternalexception)
        {
            super._trace.err(2, "Internal Exception while requesting another subfile page. \n" + webfacinginternalexception.toString());
            getErrorHandler().handleError(webfacinginternalexception, HttpRequestHandler._resmri.getString("WF0043"), webfacinginternalexception.getMessage());
        }
        catch(IOException ioexception)
        {
            super._trace.err(2, "IO Exception while requesing another subfile page. \n" + ioexception.toString());
            getErrorHandler().handleError(ioexception, HttpRequestHandler._resmri.getString("WF0043"), ioexception.getMessage());
        }
        catch(Exception exception)
        {
            super._trace.err(2, "Unexpected exception while requesting another subfile page. \n" + exception.toString());
            getErrorHandler().handleError(exception, HttpRequestHandler._resmri.getString("WF0043"), exception.getMessage());
        }
    }

    public static final String copyRight = "(C) Copyright IBM Corporation 1999-2003 all rights reserved";
    public static final int WAIT_FOR_INVOCATION = 0;
    public static final int WAIT_FOR_HOST_BUFFER = 1;
    public static final int WAIT_FOR_BROWSER_REQUEST = 2;
    public static final int INVALID_SESSION = 3;
    public static final int IMMEDIATE_WRITE_PENDING = 4;
    public static final int REFRESH = 5;
    private boolean _forceUTF8;

}
