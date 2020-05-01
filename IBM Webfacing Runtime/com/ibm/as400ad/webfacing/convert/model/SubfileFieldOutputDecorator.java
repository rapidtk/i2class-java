// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.code400.dom.constants.*;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.convert.ExportHandler;
import com.ibm.as400ad.webfacing.convert.IFieldOutput;
import com.ibm.as400ad.webfacing.convert.gen.bean.JavaSourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.ClientScriptSourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.DHTMLSourceCodeCollection;
import com.ibm.etools.iseries.webfacing.convert.external.*;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            FieldOutput, FieldArea, FieldVisibility, FieldWebSettings, 
//            RecordLayout

public class SubfileFieldOutputDecorator
    implements IFieldTagInput, ITagOutput, IFieldOutput, ENUM_FieldUsage
{

    public SubfileFieldOutputDecorator()
    {
        _sflWidth = 0;
        _sfllin = -1;
        got = false;
    }

    public SubfileFieldOutputDecorator(IFieldOutput ifieldoutput, int i, int j)
    {
        _sflWidth = 0;
        _sfllin = -1;
        got = false;
        setTarget(ifieldoutput);
        columnOffset = i;
        _sflWidth = j;
    }

    public ClientScriptSourceCodeCollection getClientScript()
    {
        ClientScriptSourceCodeCollection clientscriptsourcecodecollection = target.getClientScript();
        try
        {
            String s = clientscriptsourcecodecollection.removeElementWithPrefix("rc(\"");
            if(s != null)
            {
                int i = s.indexOf("\",");
                if(i != -1)
                    s = s.substring(0, i + 1) + ");";
                clientscriptsourcecodecollection.addElement(s);
            } else
            {
                String s1 = clientscriptsourcecodecollection.removeElementWithPrefix("cf(\"");
                int j = s1.indexOf("\",");
                int k = s1.indexOf(",{");
                if(j == -1 || k == -1 || k - j <= 1)
                    clientscriptsourcecodecollection.addElement(s1);
                else
                    clientscriptsourcecodecollection.addElement("s" + s1.substring(0, j + 1) + s1.substring(k));
            }
        }
        catch(Throwable throwable) { }
        Enumeration enumeration = clientscriptsourcecodecollection.elements();
        ClientScriptSourceCodeCollection clientscriptsourcecodecollection1 = new ClientScriptSourceCodeCollection();
        String s2;
        for(; enumeration.hasMoreElements(); clientscriptsourcecodecollection1.addElement(replaceRecordName(s2)))
        {
            s2 = enumeration.nextElement().toString();
            s2 = WebfacingConstants.replaceSubstring(s2, ".evaluateIndicatorExpression(", ".evaluateIndicatorExpression(rrn,");
            s2 = WebfacingConstants.replaceSubstring(s2, ".isMDTOn(", ".isMDTOn(rrn,");
            s2 = WebfacingConstants.replaceSubstring(s2, ".getValuesAfterEditing(", ".getSubfileValuesAfterEditing(");
        }

        return clientscriptsourcecodecollection1;
    }

    public String getClientScriptLocation()
    {
        return target.getClientScriptLocation();
    }

    public int getColumn()
    {
        return (target.getColumn() - columnOffset) + 1;
    }

    public String getConditioning()
    {
        return target.getConditioning();
    }

    public JavaSourceCodeCollection getDataBeanInitialization()
    {
        return target.getDataBeanInitialization();
    }

    public DHTMLSourceCodeCollection getDHTML()
    {
        DHTMLSourceCodeCollection dhtmlsourcecodecollection = new DHTMLSourceCodeCollection();
        DHTMLSourceCodeCollection dhtmlsourcecodecollection1 = target.getDHTML();
        String s = ".getFieldValueWithTransform(\"" + getFieldName() + "\", ";
        String s1;
        for(Enumeration enumeration = dhtmlsourcecodecollection1.elements(); enumeration.hasMoreElements(); dhtmlsourcecodecollection.addElement(s1))
        {
            s1 = enumeration.nextElement().toString();
            s1 = WebfacingConstants.replaceSubstring(s1, s, s + "rrn" + ",");
            s1 = replaceRecordName(s1);
            s1 = WebfacingConstants.replaceSubstring(s1, ".evaluateStyleClass(", ".evaluateStyleClass(rrn,");
            s1 = WebfacingConstants.replaceSubstring(s1, ".evaluateIndicatorExpression(", ".evaluateIndicatorExpression(rrn,");
            String s2 = "<%=currentRow%>";
            String s3;
            if(_sfllin == -1)
                s3 = "<%=" + _sflWidth + "*(" + "col" + "-1)+" + Integer.toString(columnOffset + getColumn()) + "-1%>";
            else
                s3 = "<%=(" + _sflWidth + "+" + _sfllin + ")*(" + "col" + "-1)+" + Integer.toString(columnOffset + getColumn()) + "-1%>";
            String s4 = "rowValue=\"" + s2 + "\" colValue=\"" + s3 + "\" ";
            if(s1.indexOf("<INPUT ") >= 0)
            {
                String s5 = s1.toLowerCase();
                if(!target.isScriptableInvisibleField())
                    s1 = WebfacingConstants.replaceSubstring(s1, "<INPUT ", "<INPUT " + s4);
            } else
            if(s1.indexOf("<TEXTAREA ") >= 0)
                s1 = WebfacingConstants.replaceSubstring(s1, "<TEXTAREA ", "<TEXTAREA " + s4);
            else
            if(s1.indexOf("setCursor") != -1)
            {
                int i = s1.indexOf("setCursor");
                int j = s1.indexOf("(", i);
                int k = s1.indexOf(";", j);
                if(i != -1)
                {
                    String s6 = s2 + ", " + s3;
                    s1 = s1.substring(0, j + 1) + s6 + s1.substring(k - 1);
                }
            } else
            if(s1.indexOf("id=") >= 0)
                s1 = WebfacingConstants.replaceSubstring(s1, "id=", s4 + "id=");
        }

        return dhtmlsourcecodecollection;
    }

    public int getDisplaySizeIndex()
    {
        return target.getDisplaySizeIndex();
    }

    public FieldArea getFieldArea()
    {
        return target.getFieldArea();
    }

    public String getFieldId()
    {
        return replaceRecordName(target.getFieldId());
    }

    public String getFieldName()
    {
        return target.getFieldName();
    }

    public FieldNode getFieldNode()
    {
        return target.getFieldNode();
    }

    public String getFieldText()
    {
        String s = target.getFieldText();
        String s1 = ".getFieldValue(\"" + getFieldName() + "\"";
        return WebfacingConstants.replaceSubstring(s, s1, s1 + "." + "rrn");
    }

    public String getFieldTextWithTransform(int i)
    {
        String s = target.getFieldTextWithTransform(i);
        String s1 = ".getFieldValueWithTransform(\"" + getFieldName() + "\", ";
        s = WebfacingConstants.replaceSubstring(s, s1, s1 + "rrn" + ",");
        return s;
    }

    public int getHeight()
    {
        return target.getHeight();
    }

    public String getIsFieldVisibleCall()
    {
        String s = target.getIsFieldVisibleCall();
        String s1 = FieldOutput.IS_FIELD_VISIBLE_METHOD + "(";
        return WebfacingConstants.replaceSubstring(s, s1, s1 + "rrn" + ",");
    }

    public DHTMLSourceCodeCollection getOutOfFlowHTML()
    {
        return target.getOutOfFlowHTML();
    }

    public String getQualifiedFieldName()
    {
        String s = target.getQualifiedFieldName();
        return s + "$" + "<%=" + "rrn" + "%>";
    }

    public int getRow()
    {
        return target.getRow();
    }

    public String getTagId()
    {
        return replaceRecordName(target.getTagId());
    }

    public String getTDAttributes()
    {
        String s;
        if(getFieldNode().getFieldType().isOfType(9) && getFieldNode().getFieldIOCapability() == 'O')
            s = " align=right";
        else
            s = "";
        return target.getTDAttributes() + s;
    }

    public JavaSourceCodeCollection getViewBeanInitialization()
    {
        return target.getViewBeanInitialization();
    }

    public int getWidth()
    {
        return target.getWidth();
    }

    public boolean hasKeyLabelDetected()
    {
        return target.hasKeyLabelDetected();
    }

    public boolean hasOutOfFlowHTML()
    {
        return target.hasOutOfFlowHTML();
    }

    public boolean isScriptableInvisibleField()
    {
        return target.isScriptableInvisibleField();
    }

    public boolean isSingleDHTMLElement()
    {
        return target.isSingleDHTMLElement();
    }

    public boolean isWrapped()
    {
        return target.isWrapped();
    }

    private String replaceRecordName(String s)
    {
        s = WebfacingConstants.replaceSubstring(s, _sflName + "$", _sflCtlName + "$");
        String s1 = _sflCtlName + "$" + target.getFieldName();
        s = WebfacingConstants.replaceSubstring(s, s1, s1 + "$" + "<%=" + "rrn" + "%>");
        return s;
    }

    public void setColumnOffset(int i)
    {
        columnOffset = i;
    }

    public void setDisplaySizeIndex(int i)
    {
        target.setDisplaySizeIndex(i);
    }

    public void setSFLLIN(int i)
    {
        _sfllin = i;
    }

    public void setTarget(IFieldOutput ifieldoutput)
    {
        target = ifieldoutput;
        RecordNode recordnode = ifieldoutput.getFieldNode().getParentRecord();
        RecordNode recordnode1 = recordnode.getRelatedSFLCTL();
        _sflCtlName = recordnode1.getWebName();
        _sflName = recordnode.getWebName();
    }

    public boolean useLargestRectangle()
    {
        return target.useLargestRectangle();
    }

    public FieldVisibility getFieldVisibility()
    {
        return target.getFieldVisibility();
    }

    public Iterator getSubWebSettings()
    {
        return target.getSubWebSettings();
    }

    public IRawWebSetting getMainWebSetting()
    {
        return target.getMainWebSetting();
    }

    public ArrayList getHtmlHeader()
    {
        return target.getHtmlHeader();
    }

    public String getSubTag(IRawWebSetting irawwebsetting)
    {
        return target.getSubTag(irawwebsetting);
    }

    public void addHtmlHeader(String s, String s1)
    {
        target.addHtmlHeader(s, s1);
    }

    public void setTag(String s)
    {
        target.setTag(s);
    }

    public String getTagDHTML()
    {
        return target.getTagDHTML();
    }

    public void clearTagDHTML()
    {
        target.clearTagDHTML();
    }

    public IFieldType getFieldType()
    {
        return target.getFieldType();
    }

    public String getRecordName()
    {
        return _sflCtlName;
    }

    public String getFieldValue()
    {
        return getFieldText();
    }

    public int getStartColumn()
    {
        return target.getStartColumn();
    }

    public int getEndColumn()
    {
        return target.getEndColumn();
    }

    public int getStartRow()
    {
        return target.getStartRow();
    }

    public int getEndRow()
    {
        return target.getEndRow();
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
        IRecordType irecordtype = target.getRecordType();
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
            target.backupFieldDHTML();
        String s = getFieldHTML();
        got = false;
        return s;
    }

    public void backupFieldDHTML()
    {
        target.backupFieldDHTML();
    }

    public FieldWebSettings getFieldWebSettings()
    {
        return target.getFieldWebSettings();
    }

    public void setRecordLayout(RecordLayout recordlayout)
    {
        target.setRecordLayout(recordlayout);
    }

    public RecordLayout getRecordLayout()
    {
        return target.getRecordLayout();
    }

    static final String copyRight = new String("(c) Copyright IBM Corporation 1999-2003, all rights reserved");
    IFieldOutput target;
    int columnOffset;
    private String _sflCtlName;
    private String _sflName;
    public static final String RRN_VARNAME = "rrn";
    private int _sflWidth;
    private int _sfllin;
    private boolean got;

}
