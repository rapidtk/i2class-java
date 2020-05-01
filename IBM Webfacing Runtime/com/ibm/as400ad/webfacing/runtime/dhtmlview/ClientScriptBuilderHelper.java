// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.dhtmlview;

import com.ibm.as400ad.webfacing.common.*;
import com.ibm.as400ad.webfacing.runtime.controller.ErrorHandler;
import com.ibm.as400ad.webfacing.runtime.controller.WFSession;
import com.ibm.as400ad.webfacing.runtime.host.HostJobInfo;
import com.ibm.as400ad.webfacing.runtime.httpcontroller.HttpRequestHandler;
import com.ibm.as400ad.webfacing.runtime.httpcontroller.IHttpSessionVariable;
import com.ibm.as400ad.webfacing.runtime.view.*;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ClientScriptBuilderHelper extends HttpRequestHandler
    implements IHttpSessionVariable
{

    public ClientScriptBuilderHelper(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse, ServletContext servletcontext, ITraceLogger itracelogger)
    {
        super(httpservletrequest, httpservletresponse, servletcontext);
        _layerToPositionTo = 1;
        _winitGenerated = false;
        super._trace = itracelogger;
    }

    public void buildClientScript()
    {
        try
        {
            if(super._session.getAttribute("errorBean") != null)
                return;
            if(null == super._request.getAttribute("ClientScriptLoaded"))
            {
                super._request.setAttribute("ClientScriptLoaded", "ClientScriptLoaded");
                _out = super._response.getWriter();
                _screenbuilder = (IScreenBuilder)super._request.getSession().getAttribute("screenbuilder");
                generateImports();
                generateStyleForTR();
                generateVersions();
                generateOnLoadFunction();
            }
        }
        catch(Exception exception)
        {
            super._trace.err(1, exception, "Exception within ClientScriptBuilderHelper.buildClientScript() : ");
            (new ErrorHandler(super._servletContext, super._request, super._response, super._trace, true)).handleError(exception, _resmri.getString("WF0083"));
        }
        catch(Throwable throwable)
        {
            super._trace.err(1, throwable, "Throwable within ClientScriptBuilderHelper.buildClientScript() : ");
            (new ErrorHandler(super._servletContext, super._request, super._response, super._trace, true)).handleError(throwable, _resmri.getString("WF0083"));
        }
    }

    private void generateFieldAttributes(IBuildRecordViewBean ibuildrecordviewbean)
    {
        try
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
            String s1 = ibuildrecordviewbean.getClientScriptJSPName();
            try
            {
                super._servletContext.getRequestDispatcher(s1).include(super._request, super._response);
            }
            catch(Exception exception)
            {
                if(super._response.isCommitted())
                    super._trace.err(2, "Exception within ClientScriptBuilderHelper : response already committed");
                else
                    super._trace.err(1, exception, "Exception within ClientScriptBuilderHelper.generateFieldAttributes() : " + s1);
            }
            super._session.removeAttribute(s);
        }
        catch(Throwable throwable)
        {
            System.out.println("Exception in generateFieldAttributes : " + throwable);
        }
    }

    private void generateSetCursorJavascript(LocationOnDevice locationondevice)
    {
        if(locationondevice.getField() != null)
        {
            int i = 0;
            if(locationondevice.getCursorPosition() != null)
                i = locationondevice.getCursorPosition().getColumnOffset();
            _out.print("setCursorForEDTMSK_DBCS(\"" + locationondevice.getTagID() + "\"," + i + ");");
        } else
        {
            CursorPosition cursorposition = locationondevice.getCursorPosition();
            if(cursorposition != null)
            {
                _out.print("setCursor(" + cursorposition.getSetCursorParameters());
                IBuildRecordViewBean ibuildrecordviewbean = _screenbuilder.getRecordAt(cursorposition);
                if(ibuildrecordviewbean != null)
                    _out.print(", \"" + getRecordTagId(ibuildrecordviewbean) + "$\"");
                _out.print(");");
            }
        }
    }

    private void generateFocusJScript(LocationOnDevice locationondevice)
    {
        if(locationondevice.getField() != null)
        {
            int i = 0;
            if(locationondevice.getCursorPosition() != null)
                i = locationondevice.getCursorPosition().getColumnOffset();
            _out.print("setFocusForTagID(\"" + locationondevice.getTagID() + "\"," + i + ");");
        }
    }

    private void generateHelpInfo()
    {
        if(_screenbuilder.isHelpEnabled())
            _out.print("HELPenabled=true;");
        if(!_screenbuilder.isHLPRTNActive() && _screenbuilder.hasHelpSpecifications())
            _out.print("callHelp=true;");
        if(_screenbuilder.isHtmlHelp())
            _out.print("isHTMLHelp=true;");
    }

    public void generateUserImports()
    {
        String s = System.getProperty("file.separator");
        String s1 = super._servletContext.getRealPath("/") + s + "ClientScript" + s + "usr" + s;
        try
        {
            File afile[] = (new File(s1)).listFiles();
            if(null != afile)
            {
                for(int i = 0; i < afile.length; i++)
                    if(null != afile[i] && afile[i].isFile() && !afile[i].isHidden())
                    {
                        _out.println("<script language='JavaScript' src='ClientScript/usr/" + afile[i].getName() + "'></script>");
                     /*
						   _out.println("<script language='JavaScript'>");
						   super._servletContext.getRequestDispatcher("ClientScript/usr/" + afile[i].getName()).include(super._request, super._response);
						   _out.println("</script>");
						   */
                    }

            }
        }
        catch(Throwable throwable) { }
    }

    private void generateImports()
    {
        _out.println("<script language='JavaScript' src='ClientScript/webface.js'></script>");
      /*
      try
      {
		_out.println("<script language='JavaScript'>");
		super._servletContext.getRequestDispatcher("ClientScript/webface.js").include(super._request, super._response);
		_out.println("</script>");
	  }
	  catch (Exception e)
	  {
	  	e.printStackTrace();
	  }
	  */
        generateUserImports();
        _out.print("<script language='JavaScript'>");
        _out.println("document.body.onload=addStatementToFunction(document.body.onload,'wfBodyOnLoad();',true);");
        _out.print("var isOverriden='false', uriOvr, targetFrameOvr;");
        _out.print("function setOvrInfo(uri,target){isOverriden='true';uriOvr=uri;targetFrameOvr=target;}");
        _out.print("var wfCmdKeyDB=new Object();");
        _out.print("function wfCmdKeyObj(id,key,ovrUri,ovrFrame){this.listOfId=[id];this.keyStr=key;this.uri=ovrUri;this.frame=ovrFrame}");
        _out.print("wfCmdKeyObj.prototype={listOfId:null,listOfElem:null,keyStr:null};");
        _out.print("wfCmdKeyObj.UNKNOWN_CMD_KEY='';");
        Object obj = null;
        IScreenBuilder iscreenbuilder = (IScreenBuilder)super._session.getAttribute("screenbuilder");
        Iterator iterator = iscreenbuilder.getActiveKeys();
        _out.print("wfCmdKeyObj.validCmdKey=\"|");
        IClientAIDKey iclientaidkey;
        for(; iterator.hasNext(); _out.print(iclientaidkey.getKeyName() + "|"))
            iclientaidkey = (IClientAIDKey)iterator.next();

        _out.print("\";");
        String as[] = iscreenbuilder.getRecordsWithPageUPDNKey();
        String s = as[0];
        String s1 = as[1];
        if(s1.length() != 0)
            _out.print("wfCmdKeyObj.recWtPGDN=new Array(" + s1 + ");");
        if(s.length() != 0)
            _out.print("wfCmdKeyObj.recWtPGUP=new Array(" + s + ");");
        _out.println("");
        _out.print("function regCmdKey(id,key,uri,frame)");
        _out.print("{try");
        _out.print("{var wfkey=calcIndex(key);");
        _out.print("var t=wfCmdKeyDB[wfkey];");
        _out.print("if(isUndefined(t))");
        _out.print("{t=new wfCmdKeyObj(id,key,uri,frame);");
        _out.print("wfCmdKeyDB[wfkey]=t;");
        _out.print("}");
        _out.print("else{t.listOfId[t.listOfId.length]=id;}");
        _out.print("}");
        _out.print("catch(any_exp){}");
        _out.println("}");
        _out.print("function calcIndex(kn)");
        _out.print("{try");
        _out.print("{var arr=kn.match(/^.*([0-9]{2})$/);");
        _out.print("if(arr!=null){return 'F'+trimLeadingChars(arr[1],'0');}");
        _out.print("else if(wfCmdKeyObj.validCmdKey.indexOf('|'+kn+'|')!=-1)");
        _out.print("{return kn;");
        _out.print("}");
        _out.print("}catch(any_exp){}");
        _out.print("return wfCmdKeyObj.UNKNOWN_CMD_KEY;");
        _out.print("}");
        _out.println("</script>");
    }

    private void generateLayerPositioningJScript(IDeviceLayer idevicelayer, String s, boolean flag, int i)
    {
        if(flag && idevicelayer.isVerticallyPositioned())
            _layerToPositionTo = 0;
        if(!flag || idevicelayer.isVerticallyPositioned())
        {
            int j = _screenbuilder.getMaxRow();
            int k = _screenbuilder.getMaxColumn();
            if(!_winitGenerated && (idevicelayer.isWindowed() || idevicelayer.isCLRLWindow()))
            {
                _out.print("winInit(); ");
                _winitGenerated = true;
            }
            _out.println("positionLayer(" + s + ", " + _layerToPositionTo + ", " + idevicelayer.getFirstRow() + ", " + idevicelayer.getFirstColumn() + ", " + j + ", " + k + ");");
        }
        if(!flag && !idevicelayer.isVerticallyPositioned())
            _layerToPositionTo = i;
    }

    private void generateMessageLineInfo()
    {
        Iterator aiterator[] = _screenbuilder.getMessagesAndIDs();
        Iterator iterator = aiterator[0];
        Iterator iterator1 = aiterator[1];
        boolean flag = false;
        int i = 0;
        _out.print("msglnInit([");
        if(iterator.hasNext())
            do
            {
                IMessageDefinition imessagedefinition = (IMessageDefinition)iterator.next();
                String s = imessagedefinition.getMessageText().trim();
                s = WebfacingConstants.getJavaString(s);
                _out.print("\"" + s + "\"");
                int j = s.length();
                if(i < j)
                    i = j;
                if(!iterator.hasNext())
                    break;
                _out.print(",");
            } while(true);
        _out.print("]," + i + ",[");
        if(iterator1 != null && iterator1.hasNext())
        {
            Object obj = iterator1.next();
            _out.print(obj != null ? "\"" + obj + "\"" : "null");
            Object obj1;
            for(; iterator1.hasNext(); _out.print(obj1 != null ? "\"" + obj1 + "\"" : "null"))
            {
                _out.print(",");
                obj1 = iterator1.next();
            }

        }
        _out.println("]);");
    }

    private void generateOnLoadFunction()
    {
        try
        {
            HostJobInfo hostjobinfo = WFSession.getJobInfoRequestor();
            char c = hostjobinfo.getDecimalSeparator();
            char c1 = hostjobinfo.getThousandSeparator();
            String s = hostjobinfo.getDateFormat();
            char c2 = hostjobinfo.getDateSeparator();
            char c3 = hostjobinfo.getTimeSeparator();
            _out.print("<script language='JavaScript'>");
            _out.print("function setAttr(){");
            _out.print("decsep='" + c + "';");
            _out.print("thousandSep='" + c1 + "';");
            _out.print("jobDatFmt=\"" + s + "\";");
            _out.print("jobDatSep=\"" + c2 + "\";");
            _out.print("jobTimSep=\"" + c3 + "\";");
            _out.print("hostJobCCSID=\"" + _screenbuilder.getJobCCSID() + "\";");
            _out.println("setUpMT();");
            Iterator iterator = _screenbuilder.getRecordLayersOnDevice().iterator();
            boolean flag = true;
            int i = 0;
            Object obj = null;
            ArrayList arraylist = new ArrayList();
            StringBuffer stringbuffer = new StringBuffer(200);
            ArrayList arraylist1 = new ArrayList();
            while(iterator.hasNext()) 
            {
                IDeviceLayer idevicelayer = (IDeviceLayer)iterator.next();
                String s1 = idevicelayer.name();
                if(s1 == null)
                    s1 = "ClearedLinesLayer";
                s1 = s1 + String.valueOf(i + 1);
                Iterator iterator1 = idevicelayer.getRecords();
                boolean flag1 = idevicelayer.isFocusCapable();
                boolean flag2 = true;
                int k = 28;
                while(iterator1.hasNext()) 
                {
                    IBuildRecordViewBean ibuildrecordviewbean = (IBuildRecordViewBean)iterator1.next();
                    if(ibuildrecordviewbean.getFirstFieldLine() != -1)
                    {
                        ibuildrecordviewbean.setDisplayZIndex(i + 1);
                        if((ibuildrecordviewbean instanceof SubfileControlRecordViewBean) && ((SubfileControlRecordViewBean)ibuildrecordviewbean).isScrollbarShown())
                        {
                            int l = ibuildrecordviewbean.getFirstFieldLine();
                            if(l > k)
                            {
                                stringbuffer.append("posScb(\"l").append(ibuildrecordviewbean.getDisplayZIndex()).append("_");
                                stringbuffer.append(ibuildrecordviewbean.getRecordName()).append("\",").append(((SubfileControlRecordViewBean)ibuildrecordviewbean).enablePageUp());
                                stringbuffer.append(",").append(((SubfileControlRecordViewBean)ibuildrecordviewbean).enablePageDown()).append("); ");
                            } else
                            {
                                StringBuffer stringbuffer1 = new StringBuffer(200);
                                stringbuffer1.append("posScb(\"l").append(ibuildrecordviewbean.getDisplayZIndex()).append("_");
                                stringbuffer1.append(ibuildrecordviewbean.getRecordName()).append("\",").append(((SubfileControlRecordViewBean)ibuildrecordviewbean).enablePageUp());
                                stringbuffer1.append(",").append(((SubfileControlRecordViewBean)ibuildrecordviewbean).enablePageDown()).append("); ");
                                stringbuffer.insert(0, stringbuffer1);
                                k = l;
                            }
                        }
                        generateFieldAttributes(ibuildrecordviewbean);
                        boolean flag3 = false;
                    }
                }
                i++;
                arraylist1.add(((Object) (new Object[] {
                    idevicelayer, s1, new Boolean(flag), new Integer(i)
                })));
                if(!idevicelayer.isWindowed() && idevicelayer.isVerticallyPositioned())
                    arraylist.add(s1);
                flag = false;
            }
            LocationOnDevice locationondevice = _screenbuilder.calculateLocationForCursor();
            generateSetCursorJavascript(locationondevice);
            Integer integer = null;
            if(super._session.getAttribute("pageId") == null)
            {
                integer = new Integer(1);
            } else
            {
                integer = (Integer)super._session.getAttribute("pageId");
                integer = new Integer(integer.intValue() + 1);
            }
            super._session.setAttribute("pageId", integer);
            _out.print("document.SCREEN.PAGEID.value=" + integer.intValue() + ";");
            _out.print("WFFieldExitKeyCode=" + super._session.getAttribute("FIELDEXITKEYCODE") + ";");
            com.ibm.as400ad.webfacing.runtime.view.RecordViewBean arecordviewbean[] = _screenbuilder.getFirstRollEnabledRecords();
            if(arecordviewbean[0] != null)
                _out.print("firstPageUpEnabledRec='" + getRecordTagId(arecordviewbean[0]) + "';");
            if(arecordviewbean[1] != null)
                _out.print("firstPageDnEnabledRec='" + getRecordTagId(arecordviewbean[1]) + "';");
            WFAppProperties wfappproperties = WFAppProperties.getWFAppProperties(super._servletContext);
            _out.print("projectRuntimeEnvironment='';projectJ2EELevel='" + wfappproperties.getJ2EELevel() + "';");
            _out.print("javascriptLoaded=true; ");
            generateHelpInfo();
            _out.print("try{init_usr();}catch(any_exc){}");
            _out.println("}");
            _out.print("function setAttrImmediate(){");
            for(int j = 0; j < arraylist1.size(); j++)
            {
                Object aobj[] = (Object[])arraylist1.get(j);
                generateLayerPositioningJScript((IDeviceLayer)aobj[0], (String)aobj[1], ((Boolean)aobj[2]).booleanValue(), ((Integer)aobj[3]).intValue());
            }

            String s2 = stringbuffer.toString().trim();
            if(!s2.equals(""))
                _out.println(s2);
            _out.print("WFInsertMode=" + super._session.getAttribute("INSERTMODE") + "; restrictTabsToDocument();");
            generateFocusJScript(locationondevice);
            if(super._session.getAttribute("ImmediateWriteRequest") != null)
            {
                _out.print("submitForImmediateWrite();");
                super._session.removeAttribute("ImmediateWriteRequest");
            }
            generateMessageLineInfo();
        }
        finally
        {
            _out.println("} </script>");
        }
    }

    private void generateStyleForTR()
    {
        String s = (String)super._session.getAttribute("FixedHeightForEachRow");
        if(s != null && s.equalsIgnoreCase("true"))
            styleForTR();
    }

    private void generateVersions()
    {
        Iterator iterator = VersionTable.VERSIONS.keySet().iterator();
        if(iterator.hasNext())
        {
            _out.print("<script language='JavaScript'>");
            Object obj;
            for(; iterator.hasNext(); _out.print("var " + (String)obj + " = " + VersionTable.VERSIONS.get(obj) + ";"))
                obj = iterator.next();

            _out.println("</script>");
        }
    }

    private String getERRMSGTagId(IFieldMessageDefinition ifieldmessagedefinition)
    {
        return "l" + ifieldmessagedefinition.getRecordLayer() + "_" + ifieldmessagedefinition.getRecordName() + "$" + ifieldmessagedefinition.getFieldName();
    }

    private String getRecordTagId(IBuildRecordViewBean ibuildrecordviewbean)
    {
        return "l" + ibuildrecordviewbean.getDisplayZIndex() + "_" + ibuildrecordviewbean.getRecordName();
    }

    private void styleForTR()
    {
        String s = super._request.getParameter("INPUTFIELD_HEIGHT");
        if(s == null)
            s = "22";
        _out.println("<style>");
        _out.print(".trStyle {");
        _out.print("height:" + s + "px;");
        _out.println("}");
        _out.print(".subfileRecord1 {");
        _out.print("height:" + s + "px;");
        _out.println("}");
        _out.print(".subfileRecord2 {");
        _out.print("height:" + s + "px;");
        _out.println("}");
        _out.println("</style>");
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 1999-2002, all rights reserved");
    private static ResourceBundle _resmri;
    private static final String CLIENTSCRIPT_LOADED_FLAG = "ClientScriptLoaded";
    public static final String CLIENTSCRIPT_FILENAME = "JavaScript";
    private PrintWriter _out;
    private IScreenBuilder _screenbuilder;
    private int _layerToPositionTo;
    private boolean _winitGenerated;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
