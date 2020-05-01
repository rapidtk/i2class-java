// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.code400.dom.constants.*;
import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.convert.*;
import com.ibm.as400ad.webfacing.convert.gen.bean.JavaSourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.ClientScriptSourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.DHTMLSourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.rules.RuletFactory;
import com.ibm.as400ad.webfacing.runtime.dhtmlview.IHTMLStringTransforms;
import com.ibm.as400ad.webfacing.runtime.view.CommandKeyLabelList;
import com.ibm.as400ad.webfacing.runtime.view.def.*;
import com.ibm.etools.iseries.webfacing.convert.external.*;
import com.ibm.etools.iseries.webfacing.convert.gen.tag.*;
import java.util.ArrayList;
import java.util.Iterator;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            FieldWebSettings, FieldArea, FieldLines, SpecialCharHandler, 
//            FieldVisibility, IFieldText, RecordLayout

public abstract class FieldOutput
    implements IFieldTagInput, ITagOutput, IFieldOutput, ENUM_KeywordIdentifiers, IFieldText, IHTMLStringTransforms
{

    public FieldOutput()
    {
        _width = -1;
        _height = -1;
        _row = -1;
        _column = -1;
        _scriptmethods = null;
        _fn = null;
        _beanname = null;
        got = false;
        _DSPSIZIndex = 0;
        _DHTMLElements = null;
        _onClick = null;
        _styleClass = null;
        _fieldArea = null;
        INDEX_NOT_USED = -1;
        _fieldVisibility = null;
        _htmlHeader = null;
        _tags = new DHTMLSourceCodeCollection();
        _commandKeyLabelList = null;
        _visibilityConditionedCommandKeyLabelList = null;
    }

    public FieldOutput(FieldNode fieldnode)
    {
        _width = -1;
        _height = -1;
        _row = -1;
        _column = -1;
        _scriptmethods = null;
        _fn = null;
        _beanname = null;
        got = false;
        _DSPSIZIndex = 0;
        _DHTMLElements = null;
        _onClick = null;
        _styleClass = null;
        _fieldArea = null;
        INDEX_NOT_USED = -1;
        _fieldVisibility = null;
        _htmlHeader = null;
        _tags = new DHTMLSourceCodeCollection();
        _commandKeyLabelList = null;
        _visibilityConditionedCommandKeyLabelList = null;
        _fn = fieldnode;
        _width = fieldnode.getDisplayLength();
        _row = fieldnode.getRow(0);
        _column = fieldnode.getColumn(0);
        _height = 1;
        _webSettings = new FieldWebSettings(_fn);
    }

    protected String getBeanName()
    {
        if(null == _beanname)
        {
            RecordNode recordnode = _fn.getParentRecord();
            if(recordnode.isSFL() || recordnode.isSFLMSG())
                recordnode = recordnode.getRelatedSFLCTL();
            _beanname = recordnode.getBeanName();
        }
        return _beanname;
    }

    public String getChangeCursor()
    {
        return "";
    }

    String getRowExprWithSlnoOffset()
    {
        String s = Integer.toString(getRow());
        KeywordNodeEnumeration keywordnodeenumeration = getFieldNode().getParentRecord().getKeywordsOfType(209);
        if(keywordnodeenumeration.hasMoreElements() && keywordnodeenumeration.nextKeyword().getParmsAsString().indexOf("*VAR") >= 0)
            s = "<%=" + getRow() + "+slnoOffset%>";
        return s;
    }

    public String getSetCursor()
    {
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(50);
        paddedstringbuffer.concat("\"setCursor(", getRowExprWithSlnoOffset(), ", ", Integer.toString(getColumn()), ");\"");
        return paddedstringbuffer.toString();
    }

    public abstract ClientScriptSourceCodeCollection getClientScript();

    public String getClientScriptLocation()
    {
        return "";
    }

    public int getColumn()
    {
        return _column;
    }

    public String getConditioning()
    {
        String s = _fn.getIndicatorString();
        if(s != null)
            s = s.trim();
        return s;
    }

    public abstract JavaSourceCodeCollection getDataBeanInitialization();

    public DHTMLSourceCodeCollection getDHTML()
    {
        DHTMLSourceCodeCollection dhtmlsourcecodecollection = new DHTMLSourceCodeCollection();
        try
        {
            if(_DHTMLElements == null)
                initializeDHTMLElements();
            if(_DHTMLElements.hasNext())
                dhtmlsourcecodecollection.addElement(_DHTMLElements.next());
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(2, throwable, "Exception occurred in FieldOutput.getDHTML().");
        }
        return dhtmlsourcecodecollection;
    }

    int getDHTMLHeight()
    {
        return getFieldArea().getElementHeight();
    }

    int getDHTMLWidth()
    {
        return getFieldArea().getElementWidth();
    }

    public int getDisplaySizeIndex()
    {
        return _DSPSIZIndex;
    }

    public void addProtectDsplAttr(PaddedStringBuffer paddedstringbuffer, PaddedStringBuffer paddedstringbuffer1, String s)
    {
    }

    private static boolean addDsplAttr(PaddedStringBuffer paddedstringbuffer, PaddedStringBuffer paddedstringbuffer1, String s, String s1, String s2)
    {
        if(s == null)
            return false;
        if(s1.equals("PField"))
        {
            paddedstringbuffer.concat("dsplAttrBean", ".set", s1, "(\"", s, "\",\"", s2, "\"); ");
            return true;
        }
        if(s.equals("") && !s1.equals("Reverse"))
        {
            paddedstringbuffer1.append("wf_");
            paddedstringbuffer1.append(s2);
            paddedstringbuffer1.append(" ");
            return true;
        } else
        {
            paddedstringbuffer.concat("dsplAttrBean", ".set", s1, "IndExpr(\"", s, "\"); ");
            return true;
        }
    }

    private static String getStyleClassColorPrefix(int i)
    {
        switch(i)
        {
        case 268: 
            return "blue";

        case 262: 
            return "green";

        case 267: 
            return "pink";

        case 264: 
            return "red";

        case 265: 
            return "turquoise";

        case 263: 
            return "white";

        case 266: 
            return "yellow";
        }
        return "";
    }

    private String processDsplAttrs(PaddedStringBuffer paddedstringbuffer, PaddedStringBuffer paddedstringbuffer1)
    {
        String s = null;
        try
        {
            for(KeywordNodeEnumeration keywordnodeenumeration = getFieldNode().getKeywordsOfType(77); keywordnodeenumeration.hasMoreElements();)
            {
                KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
                if(keywordnode != null)
                {
                    String s1 = keywordnode.getIndicatorString();
                    if(null == s1)
                        s1 = "";
                    KeywordParm keywordparm = keywordnode.getFirstParm();
                    if(null != keywordparm)
                    {
                        String s2 = getStyleClassColorPrefix(keywordparm.getVarParmToken());
                        paddedstringbuffer.concat("dsplAttrBean", ".addColourIndExpr(\"", s2, "\",\"", s1, "\"); ");
                        if(s1.equals(""))
                            s = s2;
                    }
                }
            }

            DisplayAttributes displayattributes = getFieldNode().getDisplayAttributes();
            int i = paddedstringbuffer.toStringBuffer().length();
            addDsplAttr(paddedstringbuffer, paddedstringbuffer1, displayattributes.getBLIndExpr(), "Blink", "bl");
            addDsplAttr(paddedstringbuffer, paddedstringbuffer1, displayattributes.getHIIndExpr(), "Highlight", "hi");
            addDsplAttr(paddedstringbuffer, paddedstringbuffer1, displayattributes.getCSIndExpr(), "ColSeparators", "cs");
            addDsplAttr(paddedstringbuffer, paddedstringbuffer1, displayattributes.getRIIndExpr(), "Reverse", "ri_");
            addDsplAttr(paddedstringbuffer, paddedstringbuffer1, displayattributes.getPFIELDIndExpr(), "PField", displayattributes.getPFIELDName());
            addProtectDsplAttr(paddedstringbuffer, paddedstringbuffer1, displayattributes.getPRIndExpr());
            if(!addDsplAttr(paddedstringbuffer, paddedstringbuffer1, displayattributes.getULIndExpr(), "Underline", "ul") && displayattributes.getChginpdftKeywordNode() != null)
                paddedstringbuffer.append("dsplAttrBean.setChginpdftNoUL(); ");
            if(paddedstringbuffer.toStringBuffer().length() > i)
                s = null;
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(3, throwable);
        }
        return s;
    }

    protected String getEvalIndExprCall(String s)
    {
        return getBeanName() + ".evaluateIndicatorExpression(\"" + s + "\")";
    }

    public FieldArea getFieldArea()
    {
        if(_fieldArea == null)
            _fieldArea = new FieldArea(this);
        return _fieldArea;
    }

    public abstract String getFieldId();

    public String getFieldName()
    {
        return _fn.getWebName();
    }

    public final FieldNode getFieldNode()
    {
        return _fn;
    }

    public abstract String getFieldText();

    public String getFieldTextWithTransform()
    {
        return getFieldTextWithTransform(getHTMLTransform());
    }

    public abstract String getFieldTextWithTransform(int i);

    public String getFieldTextWithTransform(int i, int j)
    {
        return getFieldTextWithTransform(getHTMLTransform(), i, j);
    }

    public abstract String getFieldTextWithTransform(int i, int j, int k);

    FieldLines getGeneratedDHTML()
    {
        FieldLines fieldlines = new FieldLines();
        String s = _webSettings.createGraphic(getFieldTextWithTransform(1));
        if(s != null)
            fieldlines.add(s);
        else
            fieldlines.addFieldText(getColumn(), getScreenWidth(), this);
        boolean flag = _webSettings.createHyperLink(fieldlines, getStyleClass());
        wrapWithSPANTag(fieldlines, flag);
        return fieldlines;
    }

    public int getHeight()
    {
        return _height;
    }

    public int getHTMLTransform()
    {
        return 2;
    }

    String getIndexParams(int i, int j)
    {
        if(i != INDEX_NOT_USED && j != INDEX_NOT_USED)
            return i + "," + j;
        else
            return null;
    }

    public String getIsFieldVisibleCall()
    {
        return getBeanName() + "." + IS_FIELD_VISIBLE_METHOD + "(\"" + getFieldName() + "\")";
    }

    String getOnClick()
    {
        if(_onClick == null)
        {
            String s = getChangeCursor().trim();
            _onClick = s.equals("") ? "" : " onClick=" + s;
        }
        return _onClick;
    }

    public DHTMLSourceCodeCollection getOutOfFlowHTML()
    {
        return new DHTMLSourceCodeCollection();
    }

    public String getPrgDefineHTML()
    {
        return getFieldText();
    }

    public String getQualifiedFieldName()
    {
        return _fn.getDhtmlName();
    }

    public int getRow()
    {
        return _row;
    }

    public String getSampleText()
    {
        return _fn.getSampleText();
    }

    int getScreenWidth()
    {
        return DSPSIZConstants.getScreenWidth(getDisplaySizeIndex());
    }

    public String getStyleClass()
    {
        if(_styleClass == null)
        {
            PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(50);
            if(_webSettings.getStyle() != null)
            {
                paddedstringbuffer.append(" class=\"");
                paddedstringbuffer.append(_webSettings.getStyle());
                paddedstringbuffer.append("\" ");
            } else
            {
                try
                {
                    PaddedStringBuffer paddedstringbuffer1 = new PaddedStringBuffer(50);
                    PaddedStringBuffer paddedstringbuffer2 = new PaddedStringBuffer(30);
                    String s = processDsplAttrs(paddedstringbuffer1, paddedstringbuffer2);
                    String s1 = paddedstringbuffer1.toString();
                    boolean flag = _fn.getKeywordsOfType(99).hasMoreElements() || _fn.getKeywordsOfType(100).hasMoreElements();
                    if((s != null || s1.length() == 0) && !flag)
                    {
                        paddedstringbuffer.append(" class=\"");
                        paddedstringbuffer.append(paddedstringbuffer2.toString());
                        paddedstringbuffer.append(getRightAlignmentStyle());
                        paddedstringbuffer.append("wf_");
                        paddedstringbuffer.append(s != null ? s : "default");
                        paddedstringbuffer.append(" ");
                        paddedstringbuffer.append("wf_");
                        paddedstringbuffer.append("field");
                        paddedstringbuffer.append("\" ");
                    } else
                    {
                        paddedstringbuffer.append(" <%{DisplayAttributeBean ");
                        paddedstringbuffer.append("dsplAttrBean");
                        paddedstringbuffer.append(" = new DisplayAttributeBean(");
                        if(flag && (_fn.getFieldUsage() == 'B' || _fn.getFieldUsage() == 'I'))
                        {
                            paddedstringbuffer.append("\"");
                            paddedstringbuffer.append(getFieldName());
                            paddedstringbuffer.append("\"");
                        }
                        paddedstringbuffer.append("); ");
                        paddedstringbuffer.append(s1);
                        paddedstringbuffer.append("%> class=\"");
                        paddedstringbuffer.append(paddedstringbuffer2.toString());
                        paddedstringbuffer.append(getRightAlignmentStyle());
                        paddedstringbuffer.append("<%=");
                        paddedstringbuffer.append(getBeanName());
                        paddedstringbuffer.append(".evaluateStyleClass(");
                        paddedstringbuffer.append("dsplAttrBean");
                        paddedstringbuffer.append(")%>\" <%}%> ");
                    }
                }
                catch(Throwable throwable)
                {
                    ExportHandler.err(3, "error in FieldOutput.getStyleClass() = " + throwable);
                    Util.logThrowableMessage("error in FieldOutput.getStyleClass()", throwable, false);
                }
            }
            _styleClass = paddedstringbuffer.toString();
        }
        return _styleClass;
    }

    public String getTagId()
    {
        return WebfacingConstants.getLayerPrefix() + getFieldId();
    }

    public String getTDAttributes()
    {
        return "NOWRAP";
    }

    String getUsrDefineHTML()
    {
        return _webSettings.getUsrDefineHTML();
    }

    public abstract JavaSourceCodeCollection getViewBeanInitialization();

    public int getWidth()
    {
        return _width;
    }

    public boolean hasKeyLabelDetected()
    {
        return false;
    }

    public boolean hasOutOfFlowHTML()
    {
        return false;
    }

    protected void initialize()
    {
        if(_fn.hasWebSettings())
            _webSettings.initialize();
        if(_webSettings.getRow() > 0)
            _row = _webSettings.getRow();
        if(_webSettings.getColumn() > 0)
            _column = _webSettings.getColumn();
        initializeWidthAndHeight();
        if(_webSettings.getColSpan() > 0)
            _width = _webSettings.getColSpan();
        if(_webSettings.getRowSpan() > 0)
            _height = _webSettings.getRowSpan();
        _charHandler = new SpecialCharHandler(this);
    }

    void initializeDHTMLElements()
    {
        FieldLines fieldlines = new FieldLines();
        try
        {
            if(getUsrDefineHTML() != null)
                fieldlines.add(getUsrDefineHTML());
            else
            if(_webSettings.isPrgDefine())
                fieldlines.add(getPrgDefineHTML());
            else
                fieldlines = getGeneratedDHTML();
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(2, throwable, "Exception occurred in FieldOutput.initializeDHTMLElements().");
        }
        setDHTMLElements(fieldlines);
    }

    protected void initializeWidthAndHeight()
    {
    }

    public abstract boolean isComputed();

    public boolean isScriptableInvisibleField()
    {
        return false;
    }

    public boolean isSingleDHTMLElement()
    {
        return !isWrapped() || _webSettings.isSingleDHTMLElement();
    }

    public boolean isWrapped()
    {
        boolean flag = (getColumn() + getWidth()) - 1 > DSPSIZConstants.getScreenWidth(getDisplaySizeIndex());
        return flag;
    }

    public DHTMLSourceCodeCollection keywordConditionedDHTML(DHTMLSourceCodeCollection dhtmlsourcecodecollection, DHTMLSourceCodeCollection dhtmlsourcecodecollection1, String s)
    {
        DHTMLSourceCodeCollection dhtmlsourcecodecollection2 = new DHTMLSourceCodeCollection();
        try
        {
            boolean flag = s != null && !s.equals("");
            if(flag)
            {
                PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(50);
                paddedstringbuffer.concat("<%if (!", getEvalIndExprCall(s), ") {%> ", dhtmlsourcecodecollection2.getNewline());
                dhtmlsourcecodecollection2.addElement(paddedstringbuffer);
            }
            if(s == null || flag)
                dhtmlsourcecodecollection2.addAll(dhtmlsourcecodecollection);
            if(flag)
                dhtmlsourcecodecollection2.addElement("<% } else { %> " + dhtmlsourcecodecollection2.getNewline());
            if(s != null)
                dhtmlsourcecodecollection2.addAll(dhtmlsourcecodecollection1);
            if(flag)
                dhtmlsourcecodecollection2.addElement(dhtmlsourcecodecollection2.getNewline() + "<% } %>" + dhtmlsourcecodecollection2.getNewline());
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(3, "error in FieldOutput.keywordConditionedDHTML(DHTMLSourceCodeCollection,DHTMLSourceCodeCollection) = " + throwable);
        }
        return dhtmlsourcecodecollection2;
    }

    public String nonDisplayConditionedText(String s)
    {
        String s1 = getFieldNode().getDisplayAttributes().getNDIndExpr();
        if(s1 == null)
            return s;
        if(s1.equals(""))
        {
            return "";
        } else
        {
            PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(65);
            paddedstringbuffer.concat("<%if (!", getEvalIndExprCall(s1), ") {%>", s, "<%}%>");
            return paddedstringbuffer.toString();
        }
    }

    void setDHTMLElements(FieldLines fieldlines)
    {
        _DHTMLElements = fieldlines;
    }

    public void setDisplaySizeIndex(int i)
    {
        _DSPSIZIndex = i;
        if(_webSettings.getRow() <= 0)
            _row = _fn.getRow(i);
        if(_webSettings.getColumn() <= 0)
            _column = _fn.getColumn(i);
    }

    public boolean useLargestRectangle()
    {
        return false;
    }

    public static final String wrapJSPExpr(String s)
    {
        return DHTMLSourceCodeCollection.wrapJSPExpression(s);
    }

    void wrapWithSPANTag(FieldLines fieldlines, boolean flag)
    {
        String s4 = "";
        Object obj = null;
        if(!flag)
            s4 = getStyleClass();
        for(int i = 0; i < fieldlines.size(); i++)
        {
            String s = fieldlines.get(i);
            String s1 = i != 0 ? "" : _webSettings.getHtmlBefore();
            String s5 = _webSettings.getDynLabelKey();
            String s2 = i != 0 ? "" : "id='" + getTagId() + "'";
            String s3 = i != fieldlines.size() - 1 ? "" : _webSettings.getHtmlAfter();
            if(RuletFactory.getRuletFactory().isTextConstantOptionButton() && null != s5 || RuletFactory.getRuletFactory().isTextConstantOptionWindowButton() && null != s5 && (getFieldNode().getParentRecord().isWindow() || getRecordLayout().isCLRLWindow()))
            {
                String s6 = "";
                if(i > 0)
                {
                    fieldlines.set(i, "");
                    continue;
                }
                String s7 = s5;
                if(s7.length() == 1)
                    s7 = "CA0" + s7;
                else
                if(s7.length() == 2)
                    s7 = "CA" + s7;
                s6 = s6 + s7 + ",";
                int j = 1;
                String s8 = getRecordName();
                if(!s.equals(""))
                    s = s1 + "<span " + s4 + " " + _webSettings.getHtmlInside() + "></span>" + s3 + "<% if(" + s8 + ".isRecordOnTopLayer()) {%>" + "<jsp:include page=\"/WFCmdKeysBuilder?count=" + j + "&fieldname=" + getFieldNode().getName() + "&title=yes&cmdkeylist=[" + s6 + "]\" flush=\"true\"/>" + "<%}%>";
                else
                    s = "<% if(" + s8 + ".isRecordOnTopLayer()) {%>" + "<jsp:include page=\"/WFCmdKeysBuilder?count=" + j + "&fieldname=" + getFieldNode().getName() + "&title=yes&cmdkeylist=[" + s6 + "]\" flush=\"true\"/>" + "<%}%>";
            } else
            {
                s = s1 + "<span " + s2 + " " + s4 + getOnClick() + " " + _webSettings.getHtmlInside() + ">" + nonDisplayConditionedText(s) + "</span>" + s3;
            }
            fieldlines.set(i, s);
        }

    }

    public FieldVisibility getFieldVisibility()
    {
        if(_fieldVisibility == null)
            _fieldVisibility = new FieldVisibility(getFieldName(), getFieldNode().getIndicatorString());
        return _fieldVisibility;
    }

    private String getRightAlignmentStyle()
    {
        StringBuffer stringbuffer = new StringBuffer();
        CheckAttributes checkattributes = getFieldNode().getCheckAttributes();
        if(getFieldNode().getFieldType().isOfType(9) || checkattributes.isRB() || checkattributes.isRZ())
        {
            stringbuffer.append("wf_");
            stringbuffer.append("rightJustify");
            stringbuffer.append(" ");
        }
        return stringbuffer.toString();
    }

    public IFieldType getFieldType()
    {
        return getFieldNode().getFieldType();
    }

    public String getRecordName()
    {
        return getFieldNode().getParentRecord().getName();
    }

    public String getFieldValue()
    {
        return getFieldText();
    }

    public int getStartColumn()
    {
        return getFieldArea().getLeftmostColumn();
    }

    public int getEndColumn()
    {
        return (getFieldArea().getLeftmostColumn() + getFieldArea().getWidth()) - 1;
    }

    public int getStartRow()
    {
        return getFieldArea().getStartingRow();
    }

    public int getEndRow()
    {
        return getFieldArea().getLastRow();
    }

    public Iterator getSubWebSettings()
    {
        return _webSettings.getSubRawWebSettings();
    }

    public IRawWebSetting getMainWebSetting()
    {
        return _webSettings.getMainWebSetting();
    }

    public void setTag(String s)
    {
        if(_tags == null)
            _tags = new DHTMLSourceCodeCollection();
        _tags.addElement(s);
    }

    public String getTagDHTML()
    {
        String s = "";
        if(_tags != null)
            s = _tags.toString();
        return s;
    }

    public void clearTagDHTML()
    {
        _tags = new DHTMLSourceCodeCollection();
    }

    public void addHtmlHeader(String s, String s1)
    {
        if(_htmlHeader == null)
            _htmlHeader = new ArrayList();
        _htmlHeader.add("<%@ taglib uri=\"" + s1 + "\" prefix=\"" + s + "\" %>");
    }

    public ArrayList getHtmlHeader()
    {
        return _htmlHeader;
    }

    public String getSubTag(IRawWebSetting irawwebsetting)
    {
        WSSubTagInput wssubtaginput = new WSSubTagInput(this, irawwebsetting);
        WSSubTagOutput wssubtagoutput = new WSSubTagOutput();
        try
        {
            IWSSubTagGenerator iwssubtaggenerator = TagGeneratorLoader.getTagGeneratorLoader().getWSSubTagGenerator(irawwebsetting.getWebSettingId());
            if(iwssubtaggenerator != null)
            {
                boolean flag = iwssubtaggenerator.generate(wssubtaginput, wssubtagoutput);
                if(flag)
                {
                    if(_htmlHeader == null)
                        _htmlHeader = new ArrayList();
                    _htmlHeader.add(wssubtagoutput.getHtmlHeader());
                    return wssubtagoutput.getTagString();
                }
            }
        }
        catch(Throwable throwable) { }
        return "";
    }

    public void logMessage(String s, int i, String s1)
    {
        if(i > 0)
            if(s.equals("ERR"))
                ExportHandler.err(i, s1);
            else
            if(s.equals("DBG"))
                ExportHandler.dbg(i, s1);
            else
            if(s.equals("EVT"))
                ExportHandler.evt(i, s1);
    }

    public IRecordType getRecordType()
    {
        IRecordType irecordtype = _fn.getParentRecord().getType();
        return irecordtype;
    }

    public String getFieldHTML()
    {
        String s = getDHTML().toString();
        got = true;
        return s;
    }

    public String getFieldDHTML()
    {
        if(got)
            backupFieldDHTML();
        String s = getFieldHTML();
        got = false;
        return s;
    }

    public void backupFieldDHTML()
    {
        if(null != _DHTMLElements)
            _DHTMLElements.backup();
    }

    public FieldWebSettings getFieldWebSettings()
    {
        return _webSettings;
    }

    public RecordLayout getRecordLayout()
    {
        return _recordlayout;
    }

    public void setRecordLayout(RecordLayout recordlayout)
    {
        _recordlayout = recordlayout;
    }

    public CommandKeyLabelList getVisibilityConditonedCommandKeyLabels()
    {
        return _visibilityConditionedCommandKeyLabelList;
    }

    public CommandKeyLabelList getCommandKeyLabels()
    {
        return _commandKeyLabelList;
    }

    public void updateStringMatchedKeyLabelList()
    {
        FieldVisibility fieldvisibility = getFieldVisibility();
        String s = getFieldNode().getIndicatorString();
        String s1 = getMainWebSetting().getWebSettingValue();
        String s2 = "&{" + getFieldName() + ".value}";
        _commandKeyLabelList = new CommandKeyLabelList();
        _visibilityConditionedCommandKeyLabelList = new CommandKeyLabelList();
        if(fieldvisibility.isAlwaysVisible())
            _commandKeyLabelList.add(new CommandKeyLabel(s1, s2, 3, getFieldName()));
        else
        if(fieldvisibility.isConditionallyVisible())
        {
            VisibilityConditionedCommandKeyLabel visibilityconditionedcommandkeylabel = new VisibilityConditionedCommandKeyLabel(s1, null, null, 3);
            visibilityconditionedcommandkeylabel.addAConditionedLabel(new VisibilityConditionedLabel(null, getFieldName(), "true"));
            _visibilityConditionedCommandKeyLabelList.add(visibilityconditionedcommandkeylabel);
        }
    }

    static final String copyRight = new String("(C) Copyright IBM Corporation 1999-2003, all rights reserved");
    protected int _width;
    protected int _height;
    private int _row;
    private int _column;
    private String _scriptmethods;
    private FieldNode _fn;
    private String _beanname;
    private boolean got;
    private RecordLayout _recordlayout;
    protected static final String DSPL_ATTR_BEAN_NAME = "dsplAttrBean";
    private int _DSPSIZIndex;
    protected FieldWebSettings _webSettings;
    SpecialCharHandler _charHandler;
    public static String IS_FIELD_VISIBLE_METHOD = "isFieldVisible";
    private FieldLines _DHTMLElements;
    private String _onClick;
    private String _styleClass;
    private FieldArea _fieldArea;
    int INDEX_NOT_USED;
    FieldVisibility _fieldVisibility;
    ArrayList _htmlHeader;
    DHTMLSourceCodeCollection _tags;
    protected CommandKeyLabelList _commandKeyLabelList;
    protected CommandKeyLabelList _visibilityConditionedCommandKeyLabelList;

}
