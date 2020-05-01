// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.dhtml;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.code400.dom.constants.IFieldType;
import com.ibm.as400ad.webfacing.common.*;
import com.ibm.as400ad.webfacing.convert.*;
import com.ibm.as400ad.webfacing.convert.model.*;
import com.ibm.etools.iseries.webfacing.convert.external.*;
import com.ibm.etools.iseries.webfacing.convert.gen.tag.TagGeneratorLoader;
import com.ibm.etools.iseries.webfacing.convert.gen.tag.WSTagInput;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.dhtml:
//            DHTMLSourceCodeCollection

public class DHTMLBodyJSPVisitor
    implements IFieldOutputVisitor
{

    public DHTMLBodyJSPVisitor()
    {
        _lastColIsValid = true;
        _lastRow = 0;
        _lastCol = 0;
        _definitelyPrinted = 0;
        _firstCol = 0;
        _wdwHeight = 0;
        _wdwWidth = 0;
    }

    public DHTMLBodyJSPVisitor(RecordLayout recordlayout, DHTMLSourceCodeCollection dhtmlsourcecodecollection)
    {
        _lastColIsValid = true;
        _lastRow = 0;
        _lastCol = 0;
        _definitelyPrinted = 0;
        _firstCol = 0;
        _wdwHeight = 0;
        _wdwWidth = 0;
        _rn = recordlayout.getRecordNode();
        _rl = recordlayout;
        _scc = dhtmlsourcecodecollection;
    }

    public void addElement(Object obj)
    {
        _scc.addElement(obj);
    }

    public boolean fieldHasOutput(IFieldOutput ifieldoutput)
    {
        return true;
    }

    public String getBeanName()
    {
        return _rl.getRecordNode().getBeanName();
    }

    public IFieldOutput getFieldOutput(FieldOnRow fieldonrow)
    {
        return fieldonrow.getFieldOutput();
    }

    protected String getHTMLTableRowAttributes(int i)
    {
        if(recordHasSLNOVAR())
            return " id=\"l<%=zOrder%>r<%=" + i + getSLNOVAROffsetStr() + "%>\" class=\"trStyle\"";
        else
            return " id=\"l<%=zOrder%>r" + i + "\" class=\"trStyle\"";
    }

    public RecordLayout getRecordLayout()
    {
        return _rl;
    }

    protected int getScreenOrWindowWidth()
    {
        return _rl.getScreenOrWindowWidth();
    }

    protected String getSLNOVAROffsetStr()
    {
        if(recordHasSLNOVAR())
            return "+slnoOffset";
        else
            return "";
    }

    public boolean isControlBeforeSubfiles()
    {
        return true;
    }

    public void printBeginningLines()
    {
        String s = _rn.getBeanClassName();
        String s1 = _rn.getBeanName();
        _scc.addElement("<%@ page contentType=\"text/html; charset=UTF-8\" %>");
        _scc.addElement("<% /* %><HTML><HEAD>");
        _scc.addElement("<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        _scc.addElement("<META name=\"GENERATOR\" content=\"WebFacing - WebSphere Development Studio Client for iSeries " + Version.getVersionDTStamp() + " on " + new Date() + "\">");
        _scc.addElement("</HEAD><BODY><% */ %>");
        _scc.addElement("<%@ page import=\"com.ibm.as400ad.webfacing.runtime.view.DisplayAttributeBean,com.ibm.as400ad.webfacing.runtime.dhtmlview.IHTMLStringTransforms,com.ibm.as400ad.webfacing.runtime.httpcontroller.IHttpSessionVariable\" %>");
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(90);
        paddedstringbuffer.concat("<jsp:useBean id='", s1, "' scope='request' type=\"", _rl.getViewInterface(), "\" />");
        _scc.addElement(paddedstringbuffer);
        _scc.addElement("<% final int QUOTED_TRANSFORM = IHTMLStringTransforms.QUOTED_STRING_TRANSFORM; ");
        _scc.addElement(" final int UNQUOTED_TRANSFORM = IHTMLStringTransforms.UNQUOTED_STRING_TRANSFORM; ");
        _scc.addElement(" final String zOrder = Integer.toString(" + s1 + ".getDisplayZIndex()); ");
        if(recordHasSLNOVAR())
            _scc.addElement(" final int slnoOffset = " + s1 + ".getSLNOVAROffset(); ");
        _scc.addElement("final boolean isProtected=" + s1 + ".isProtected();");
        _scc.addElement("int lastCol; /* %><TABLE><TBODY><% */ %>");
        printVersionInfo();
    }

    protected void printBeginOfRow(int i)
    {
        _lastCol = 0;
        _lastColIsValid = true;
        _definitelyPrinted = 0;
        _scc.addElement("<TR" + getHTMLTableRowAttributes(i) + ">");
        KeywordNodeEnumeration keywordnodeenumeration = _rn.getKeywordsOfType(74);
        if(keywordnodeenumeration.hasMoreElements() && !_rn.isWindow())
        {
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            String s = keywordnode.getParmsAsString();
            if(s.equals("*NO") || !s.equals("*ALL") && !s.equals("*END") && Integer.parseInt(s) <= _rl.getLastRow() - _rl.getFirstRow())
                _lastCol = _rl.getFirstColumn() - 1;
        }
        _scc.addElement("<% lastCol = " + _lastCol + "; %>");
        _firstCol = _lastCol;
        _lastRow = i;
    }

    protected void printEmptyRow(int i)
    {
        _scc.addElement("<TR" + getHTMLTableRowAttributes(i) + "><TD>&nbsp;</TD></TR>");
    }

    public void printEndingLines()
    {
        _scc.addElement("<% /* %></TBODY></TABLE>");
        _scc.addElement("</BODY>");
        _scc.addElement("</HTML><% */ %>");
    }

    public void printVersionInfo()
    {
        String s = WebfacingConstants.getLayerPrefix() + _rn.getBeanName() + "_version";
        _scc.addElement("<INPUT TYPE=\"HIDDEN\" ID =\"" + s + "\" NAME=\"" + s + "\" VALUE=" + Version.getVersionDigits() + ">");
    }

    public boolean processBeginOfRow(RecordLayoutRow recordlayoutrow)
    {
        int i = recordlayoutrow.getRowNumber();
        if(i <= _lastRow)
            return false;
        if(!recordlayoutrow.isElementStartOnRow())
            return false;
        if(i == _rl.getFirstRow() && _rl.isCLRLWindow())
        {
            _lastRow = i;
            return false;
        }
        for(; _lastRow < i - 1; _lastRow++)
            printEmptyRow(_lastRow + 1);

        printBeginOfRow(i);
        return true;
    }

    public void processBeginOfSubfiles()
    {
    }

    public void processBeginOfTraversal()
    {
        _lastRow = getRecordLayout().getFirstRow() - 1;
        if(_lastRow < 0)
            _lastRow = 0;
        IFieldOutput ifieldoutput;
        for(Iterator iterator = getRecordLayout().getOutOfFlowHTMLFields(); iterator.hasNext(); _scc.addAll(ifieldoutput.getOutOfFlowHTML()))
            ifieldoutput = (IFieldOutput)iterator.next();

    }

    public void processEndOfRow(RecordLayoutRow recordlayoutrow)
    {
        if(_definitelyPrinted == 0)
        {
            _scc.addElement("<% if (lastCol==" + _firstCol + ") { %>");
            _scc.addElement("    <TD>&nbsp;</TD>");
            _scc.addElement("<% } %>");
        }
        _scc.addElement("</TR>");
    }

    public void processEndOfSubfiles()
    {
    }

    public void processEndOfTraversal()
    {
        for(; _lastRow < _rl.getLastRow(); _lastRow++)
            printEmptyRow(_lastRow + 1);

        _scc.addAll(processScriptableInvisibleFields());
    }

    public void processFieldOnRow(boolean flag, FieldOnRow fieldonrow)
    {
        IFieldOutput ifieldoutput = getFieldOutput(fieldonrow);
        if(!fieldonrow.isElementOnRow())
            return;
        if(flag)
            addElement("<% if (" + ifieldoutput.getIsFieldVisibleCall() + ") { %>");
        int i = getColumn(fieldonrow);
        if(_lastColIsValid)
            printSpaces(i - _lastCol - 1, false);
        else
            printSpaces(i, true);
        _lastCol = printDHTMLElement(fieldonrow, ifieldoutput);
        if(ifieldoutput.getFieldVisibility().isAlwaysVisible())
        {
            if(fieldonrow.isElementStartOnRow())
                _definitelyPrinted++;
        } else
        {
            _lastColIsValid = false;
        }
        if(flag)
            addElement("<% } %>");
    }

    public static final String wrapJSPExpr(String s)
    {
        return DHTMLSourceCodeCollection.wrapJSPExpression(s);
    }

    int getColumn(FieldOnRow fieldonrow)
    {
        return fieldonrow.getElementColumn();
    }

    protected int printDHTMLElement(FieldOnRow fieldonrow, IFieldOutput ifieldoutput)
    {
        String s = "";
        if(fieldonrow.isElementStartOnRow())
        {
            s = "<TD " + ifieldoutput.getTDAttributes() + " colspan=" + Integer.toString(fieldonrow.getElementWidth()) + " rowspan=" + Integer.toString(fieldonrow.getElementHeight()) + ">";
            boolean flag = printTags(ifieldoutput);
            if(!flag)
            {
                s = s + ifieldoutput.getFieldDHTML().toString();
            } else
            {
                s = s + ifieldoutput.getTagDHTML().toString();
                ifieldoutput.clearTagDHTML();
            }
            s = s + "</TD>";
            if(ifieldoutput.getHtmlHeader() != null)
                addHtmlHeader(ifieldoutput.getHtmlHeader());
        }
        int i = (getColumn(fieldonrow) + fieldonrow.getElementWidth()) - 1;
        s = s + "<% lastCol=" + i + "; %>";
        _scc.addElement(s);
        return i;
    }

    protected void printSpaces(int i, boolean flag)
    {
        if(flag)
        {
            _scc.addElement("<% { int cspan = " + i + "-lastCol-1; %>");
            _scc.addElement("<% if (cspan > 0) { %>");
            _scc.addElement("    <TD colspan=<%=cspan%>>&nbsp;</TD>");
            _scc.addElement("<% } %>");
            _scc.addElement("<% } %>");
        } else
        if(i > 0)
            _scc.addElement("<TD colspan=" + i + ">&nbsp;</TD>");
    }

    public DHTMLSourceCodeCollection processScriptableInvisibleFields()
    {
        DHTMLSourceCodeCollection dhtmlsourcecodecollection = new DHTMLSourceCodeCollection();
        IFieldOutput ifieldoutput;
        for(Iterator iterator = getRecordLayout().getScriptableInvisibleFields(); iterator.hasNext(); dhtmlsourcecodecollection.addAll(ifieldoutput.getDHTML()))
            ifieldoutput = (IFieldOutput)iterator.next();

        return dhtmlsourcecodecollection;
    }

    boolean recordHasSLNOVAR()
    {
        return _rl.recordHasSLNOVAR();
    }

    public void insertHeader()
    {
        _scc.addElementAtBeginning(_htmlHeader);
    }

    private boolean printTags(IFieldOutput ifieldoutput)
    {
        boolean flag = false;
        boolean flag1 = ExportSettings.getExportSettings().genTags();
        if(flag1)
        {
            ExportHandler.dbg(8, "DHTMLBodyJSPVisitor:printTags - fGenTags = true");
            TagGeneratorLoader taggeneratorloader = TagGeneratorLoader.getTagGeneratorLoader();
            IRawWebSetting irawwebsetting = ifieldoutput.getMainWebSetting();
            if(null != irawwebsetting)
                try
                {
                    IWSTagGenerator iwstaggenerator = taggeneratorloader.getWSTagGenerator(irawwebsetting.getWebSettingId());
                    if(iwstaggenerator != null)
                    {
                        WSTagInput wstaginput = new WSTagInput((IFieldTagInput)ifieldoutput, irawwebsetting);
                        flag = iwstaggenerator.generate(wstaginput, (ITagOutput)ifieldoutput);
                    }
                }
                catch(Throwable throwable)
                {
                    flag = false;
                }
            if(!flag)
                try
                {
                    IFieldTagGenerator ifieldtaggenerator = taggeneratorloader.getFieldTagGenerator(ifieldoutput.getFieldType().toString());
                    if(null == ifieldtaggenerator)
                        ifieldtaggenerator = taggeneratorloader.getFieldTagGenerator(Integer.toString(ifieldoutput.getFieldType().typeId()));
                    if(ifieldtaggenerator != null)
                        flag = ifieldtaggenerator.generate((IFieldTagInput)ifieldoutput, (ITagOutput)ifieldoutput);
                }
                catch(Throwable throwable1)
                {
                    flag = false;
                }
        }
        return flag;
    }

    private void addHtmlHeader(ArrayList arraylist)
    {
        if(arraylist != null)
        {
            if(_htmlHeader == null)
                _htmlHeader = new Hashtable();
            for(int i = 0; i < arraylist.size(); i++)
            {
                Object obj = arraylist.get(i);
                _htmlHeader.put(obj, obj);
            }

        }
    }

    static final String copyRight = "(c) Copyright IBM Corporation 1999-2003. All Rights Reserved";
    RecordLayout _rl;
    RecordNode _rn;
    DHTMLSourceCodeCollection _scc;
    Hashtable _htmlHeader;
    protected boolean _lastColIsValid;
    protected int _lastRow;
    protected int _lastCol;
    protected int _definitelyPrinted;
    protected int _firstCol;
    protected int _wdwHeight;
    protected int _wdwWidth;
    public static final String HYPERLINK_TRANSFORM_VAR = "IHTMLStringTransforms.HYPERLINK_TRANSFORM";
    public static final String QUOTED_TRANSFORM_VAR = "QUOTED_TRANSFORM";
    public static final String UNQUOTED_TRANSFORM_VAR = "UNQUOTED_TRANSFORM";
    public static final String TRIMMED_QUOTED_TRANSFORM_VAR = "IHTMLStringTransforms.TRIMMED_QUOTED_STRING_TRANSFORM";
    public static final String TRIMMED_JAVA_STRING_TRANSFORM_VAR = "IHTMLStringTransforms.TRIMMED_JAVA_STRING_TRANSFORM";
    public static final String IS_PROTECTED_VAR = "isProtected";
}
