// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.convert.Util;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.ClientScriptSourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.DHTMLSourceCodeCollection;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            InputFieldOutput, FieldLines, FieldOutput, FieldWebSettings, 
//            NamedFieldOutput

public class EDTMSKFieldOutput extends InputFieldOutput
{
    public class FieldSegment
    {

        public int getBeginningIndex()
        {
            return _begIndex;
        }

        public int getEndingIndex()
        {
            return _endIndex;
        }

        public boolean isInputCapable()
        {
            return _inputCapable;
        }

        private int _begIndex;
        private int _endIndex;
        private boolean _inputCapable;

        public FieldSegment(int i, int j, boolean flag)
        {
            _begIndex = i;
            _endIndex = j;
            _inputCapable = flag;
        }
    }


    public EDTMSKFieldOutput(FieldNode fieldnode)
    {
        super(fieldnode);
        _fieldSegments = null;
    }

    public ClientScriptSourceCodeCollection getClientScript()
    {
        ClientScriptSourceCodeCollection clientscriptsourcecodecollection = new ClientScriptSourceCodeCollection();
        if(super._webSettings.getUsrDefineHTML() != null || super._webSettings.isPrgDefine())
            return clientscriptsourcecodecollection;
        try
        {
            clientscriptsourcecodecollection.addAll(super.getClientScript());
            List list = getFieldSegments();
            String s = clientscriptsourcecodecollection.getNewline();
            String s1 = getTagId() + "EDTMSKObj";
            clientscriptsourcecodecollection.addElement(s1 + " = new EDTMSKObject(" + getClientScriptLocation() + getTagId() + ");" + s);
            clientscriptsourcecodecollection.addElement(getClientScriptLocation() + getTagId() + ".focus = EDTMSKHiddenField_focus;" + s);
            clientscriptsourcecodecollection.addElement(getClientScriptLocation() + getTagId() + ".edtmskObj = " + s1 + ";" + s);
            for(int i = 0; i < list.size(); i++)
            {
                FieldSegment fieldsegment = (FieldSegment)list.get(i);
                clientscriptsourcecodecollection.addElement(s1 + ".addFieldSegment(new FieldSegment(" + s1 + ", " + fieldsegment.getBeginningIndex() + ", " + fieldsegment.getEndingIndex() + ", " + getClientScriptLocation() + getTagId() + "$$$$$" + i + "));" + s);
            }

            clientscriptsourcecodecollection.addElement(s1 + ".updateHiddenField();" + s);
            clientscriptsourcecodecollection.addElement(getClientScriptLocation() + getTagId() + ".previousValue = " + getClientScriptLocation() + getTagId() + ".value;" + s);
        }
        catch(Throwable throwable)
        {
            Util.logThrowableMessage("error in OutputFieldOutput.getScript()", throwable, false);
        }
        return clientscriptsourcecodecollection;
    }

    private List getFieldSegments()
    {
        if(_fieldSegments != null)
            return _fieldSegments;
        _fieldSegments = new ArrayList();
        KeywordNodeEnumeration keywordnodeenumeration = getFieldNode().getKeywordsOfType(94);
        String s = keywordnodeenumeration.nextKeyword().getParms().nextParm().getVarString();
        s = s.substring(1, s.length() - 1);
        if(s.length() > 0)
        {
            int i = 0;
            for(boolean flag = s.charAt(0) == ' '; i < s.length(); flag = !flag)
            {
                int j = i;
                if(flag)
                    i = s.indexOf('&', i + 1);
                else
                    i = s.indexOf(' ', i + 1);
                if(i < 0)
                    i = s.length();
                _fieldSegments.add(new FieldSegment(j, i - 1, flag));
            }

        }
        return _fieldSegments;
    }

    protected String getOnKeyPressString()
    {
        if(getFieldNode().getFieldShift() == 'Y')
            return "onKeyPress=\"this.checkEDTMSKKeyPress();\" ";
        else
            return "";
    }

    private String getOnKeyUpString()
    {
        CheckAttributes checkattributes = getFieldNode().getCheckAttributes();
        String s = checkattributes.getERIndExpr();
        if(s != null)
        {
            if(s.equals(""))
                return "onKeyUp=\"handleCHECK_ERForEDTMSK();\" ";
            else
                return "<%=" + getEvalIndExprCall(s) + "? \"onKeyUp=\\\"handleCHECK_ERForEDTMSK();\\\" \": \"\"%>";
        } else
        {
            return "";
        }
    }

    FieldLines getGeneratedDHTML()
    {
        DHTMLSourceCodeCollection dhtmlsourcecodecollection = new DHTMLSourceCodeCollection();
        FieldNode fieldnode = getFieldNode();
        String s = getTagId();
        String s1 = getFieldTextWithTransform(1);
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(50);
        paddedstringbuffer.concat(protectConditionedText(), " onFocus=", getSetCursor(), " ");
        String s2 = " onBlur=\"this.testIfChanged();\" ";
        if(fieldnode.getFieldShift() == 'I')
            paddedstringbuffer.append(" READONLY ");
        paddedstringbuffer.append(s2);
        paddedstringbuffer.append(getOnKeyPressString());
        String s3 = getOnKeyUpString();
        paddedstringbuffer.append(getStyleClass());
        paddedstringbuffer.append(getMouseEvents());
        PaddedStringBuffer paddedstringbuffer1 = null;
        paddedstringbuffer1 = new PaddedStringBuffer(50);
        paddedstringbuffer1.concat("<INPUT TYPE=\"hidden\"", " ID=\"", s, "\" VALUE=\"", s1, "\" previousValue=\"", s1, "\" size=", Integer.toString(getWidth()), " maxLength=", Integer.toString(getWidth()), " >");
        dhtmlsourcecodecollection.addElement(paddedstringbuffer1);
        dhtmlsourcecodecollection.addElement(dhtmlsourcecodecollection.getNewline());
        KeywordNodeEnumeration keywordnodeenumeration = getFieldNode().getKeywordsOfType(94);
        String s4 = keywordnodeenumeration.nextKeyword().getParms().nextParm().getVarString();
        s4 = s4.substring(1, s4.length() - 1);
        List list = getFieldSegments();
        int i = list.size();
        char c = fieldnode.getFieldShift();
        boolean flag = i > 1;
        if(flag)
            paddedstringbuffer1.append("<TABLE cellpadding=\"0\" cellspacing=\"0\" style=\"border-width:0px; margin:'0px 0px 0px 0px';\"><TR>");
        for(int j = 0; j < i; j++)
        {
            paddedstringbuffer1 = new PaddedStringBuffer(50);
            FieldSegment fieldsegment = (FieldSegment)list.get(j);
            int k = fieldsegment.getEndingIndex() + 1;
            int l = fieldsegment.getBeginningIndex();
            String s5 = getFieldTextWithTransform(1, l, k);
            String s6 = "";
            if(flag)
            {
                String s7;
                if(fieldsegment.isInputCapable())
                    s7 = "width: " + Double.toString((double)(k - l) * 0.75D) + "em;";
                else
                    s7 = "width:100%;";
                if(j == 0)
                    s6 = " style=\"border-right-width:0px;" + s7 + "\" ";
                else
                if(j == i - 1)
                    s6 = " style=\"border-left-width:0px;" + s7 + "\" ";
                else
                    s6 = " style=\"border-right-width:0px;border-left-width:0px;" + s7 + "\" ";
                paddedstringbuffer1.append("<TD>");
            }
            if(fieldsegment.isInputCapable())
                paddedstringbuffer1.concat(super._webSettings.getHtmlBefore(), "<INPUT ", nonDisplayConditionedText(), " ID=\"", s + "$$$$$" + j, "\" VALUE=\"", s5, "\" previousValue=\"", s5, "\"", paddedstringbuffer.toString(), j != list.size() - 1 ? "" : s3, " SIZE=", Integer.toString(k - l), " MAXLENGTH=", Integer.toString(k - l), s6, super._webSettings.getHtmlInside(), " >", super._webSettings.getHtmlAfter());
            else
                paddedstringbuffer1.concat(super._webSettings.getHtmlBefore(), "<INPUT ", nonDisplayConditionedText(), " ID=\"", s + "$$$$$" + j, "\" ", getStyleClass(), " onFocus=", getSetCursor(), " VALUE=\"", s5, "\" SIZE=", Integer.toString(k - l), " MAXLENGTH=", Integer.toString(k - l), s6 + " readOnly tabIndex=\"-1\" ", super._webSettings.getHtmlInside(), " >", super._webSettings.getHtmlAfter());
            if(flag)
                paddedstringbuffer1.append("</TD>");
            dhtmlsourcecodecollection.addElement(paddedstringbuffer1);
            dhtmlsourcecodecollection.addElement(dhtmlsourcecodecollection.getNewline());
        }

        if(flag)
            paddedstringbuffer1.append("</TR></TABLE>");
        dhtmlsourcecodecollection = NDAndPRConditionedDHTML(dhtmlsourcecodecollection, fieldnode.getDisplayAttributes().getNDIndExpr(), fieldnode.getDisplayAttributes().getPRIndExpr());
        FieldLines fieldlines = new FieldLines();
        fieldlines.add(dhtmlsourcecodecollection);
        return fieldlines;
    }

    protected String jsEventHandlers()
    {
        return null;
    }

    private static final String FIELD_SEPARATOR = "$$$$$";
    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2001");
    private List _fieldSegments;

}
