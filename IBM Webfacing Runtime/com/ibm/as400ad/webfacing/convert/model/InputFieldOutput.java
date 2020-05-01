// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.designer.io.SourceCodeCollection;
import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.code400.dom.constants.*;
import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.convert.Util;
import com.ibm.as400ad.webfacing.convert.gen.bean.JavaSourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.ClientScriptSourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.DHTMLSourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.rules.EditCodeMappingHandler;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            NamedFieldOutput, FieldLines, FieldOutput, FieldWebSettings

public class InputFieldOutput extends NamedFieldOutput
{

    public InputFieldOutput(FieldNode fieldnode)
    {
        super(fieldnode);
        _checkAttributes = null;
        _nonConditionedCheckAttributes = null;
        _nonDisplayOverride = false;
    }

    private void generateCHKMSGIDDefinition(JavaSourceCodeCollection javasourcecodecollection, KeywordNodeEnumeration keywordnodeenumeration)
    {
        KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
        KeywordParmEnumeration keywordparmenumeration = keywordnode.getParms();
        KeywordParm keywordparm = null;
        String s = null;
        String s1 = null;
        String s2 = null;
        String s3 = null;
        keywordparm = keywordparmenumeration.nextParm();
        s = keywordparm.getVarString();
        keywordparm = keywordparmenumeration.nextParm();
        String s4 = keywordparm.getVarString();
        int i = s4.indexOf("/");
        if(i >= 0)
        {
            s1 = s4.substring(0, i);
            s2 = s4.substring(i + 1);
        } else
        {
            s2 = s4;
        }
        if(keywordparmenumeration.hasMoreElements())
        {
            KeywordParm keywordparm1 = keywordparmenumeration.nextParm();
            s3 = WebfacingConstants.replaceSpecialCharacters(keywordparm1.getVarString().substring(1));
        }
        String s5 = "\"" + s + "\", ";
        if(s1 != null)
            s5 = s5 + "\"" + s1 + "\", ";
        else
            s5 = s5 + "\"*LIBL\", ";
        s5 = s5 + "\"" + s2 + "\", ";
        if(s3 != null)
            s5 = s5 + "\"" + s3 + "\", ";
        else
            s5 = s5 + "null, ";
        s5 = s5 + "\"" + getFieldName() + "\"";
        javasourcecodecollection.addLine("add(new CHKMSGIDDefinition(" + s5 + "));");
    }

    private String getCheckAttributes()
    {
        if(_checkAttributes == null)
        {
            StringBuffer stringbuffer = new StringBuffer(getNonConditionedCheckAttributes());
            CheckAttributes checkattributes = getFieldNode().getCheckAttributes();
            String s = checkattributes.getMEIndExpr();
            if(s != null)
                if(s.equals(""))
                    stringbuffer.append("ME;");
                else
                    stringbuffer.append("<%=" + getEvalIndExprCall(s) + " ? \"ME;\" : \"\"%>");
            _checkAttributes = stringbuffer.toString();
        }
        return _checkAttributes;
    }

    public String getNonConditionedCheckAttributes()
    {
        if(_nonConditionedCheckAttributes == null)
        {
            StringBuffer stringbuffer = new StringBuffer(20);
            CheckAttributes checkattributes = getFieldNode().getCheckAttributes();
            if(checkattributes.isAB())
                stringbuffer.append("AB;");
            if(checkattributes.isMF())
                stringbuffer.append("MF;");
            if(checkattributes.isM10() || checkattributes.isM10F())
                if(!getFieldNode().getFieldType().isOfType(9))
                {
                    getFieldNode().logEvent(14);
                } else
                {
                    if(checkattributes.isM10F())
                        getFieldNode().logEvent(12);
                    stringbuffer.append("M10;");
                }
            if(checkattributes.isM11() || checkattributes.isM11F())
                if(!getFieldNode().getFieldType().isOfType(9))
                {
                    getFieldNode().logEvent(14);
                } else
                {
                    if(checkattributes.isM11F())
                        getFieldNode().logEvent(13);
                    stringbuffer.append("M11;");
                }
            if(checkattributes.isVN())
                stringbuffer.append("VN;");
            if(checkattributes.isVNE())
                stringbuffer.append("VE;");
            if(checkattributes.isLC())
                stringbuffer.append("LC;");
            if(checkattributes.isRB() || getFieldNode().getFieldShift() == 'S')
                stringbuffer.append("RB;");
            if(checkattributes.isRZ())
                stringbuffer.append("RZ;");
            if(checkattributes.isFE())
                stringbuffer.append("FE;");
            _nonConditionedCheckAttributes = stringbuffer.toString();
        }
        return _nonConditionedCheckAttributes;
    }

    public String getEdtcde()
    {
        String s = "";
        KeywordNode keywordnode = getFieldNode().findKeywordById(93);
        if(keywordnode != null)
        {
            KeywordParm keywordparm = keywordnode.getFirstParm();
            s = s + EditCodeMappingHandler.getSystemEditCode(keywordparm.getVarChar());
            keywordparm = keywordnode.getParm(1);
            if(keywordparm != null)
                s = s + keywordparm.getVarChar();
        }
        return s;
    }

    public String getEdtwrd()
    {
        String s = "";
        KeywordNode keywordnode = getFieldNode().findKeywordById(95);
        if(keywordnode != null)
        {
            KeywordParm keywordparm = keywordnode.getFirstParm();
            s = keywordparm.getJavaString();
        }
        return s;
    }

    private void setAttributeIfNotDefault(StringBuffer stringbuffer, String s, String s1, String s2)
    {
        if(!s1.equals(s2))
            stringbuffer.append(s).append(":\"").append(s1).append("\",");
    }

    private void setAttributeIfNotDefault(StringBuffer stringbuffer, String s, int i, int j)
    {
        if(i != j)
            stringbuffer.append(s).append(":").append(i).append(",");
    }

    public ClientScriptSourceCodeCollection getClientScript()
    {
        ClientScriptSourceCodeCollection clientscriptsourcecodecollection = new ClientScriptSourceCodeCollection();
        if(super._webSettings.getUsrDefineHTML() != null || super._webSettings.isPrgDefine())
            return clientscriptsourcecodecollection;
        try
        {
            FieldNode fieldnode = getFieldNode();
            String s = fieldnode.getDisplayAttributes().getNDIndExpr();
            String s1 = fieldnode.getDisplayAttributes().getPRIndExpr();
            if(s != null && s.equals("") && s1 != null && s1.equals(""))
                return clientscriptsourcecodecollection;
            String s2 = getTagId();
            StringBuffer stringbuffer = new StringBuffer(200);
            stringbuffer.append("cf(\"").append(s2).append("\",");
            stringbuffer.append(super.getRowCol()).append(",{");
            stringbuffer.append("<% if(").append(getBeanName()).append(".isMDTOn(\"");
            stringbuffer.append(getFieldName()).append("\")) {%>mdt:true,<% }%>");
            char c = fieldnode.getFieldShift();
            KeywordNodeEnumeration keywordnodeenumeration = fieldnode.getKeywordsOfType(124);
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            if(keywordnode != null && (c == 'A' || c == 'N' || c == 'X' || c == 'W' || c == 'I'))
            {
                String s3 = clientscriptsourcecodecollection.getNewline();
                char c1 = 'O';
                stringbuffer.append("<% if(" + getBeanName() + ".isDspfDbcsCapable()){ %>");
                setAttributeIfNotDefault(stringbuffer, "shift", c1 + "", "A");
                setAttributeIfNotDefault(stringbuffer, "ptype", FieldType.getFieldTypeFromId(19).toString(), "FT_ALPHA");
                stringbuffer.append("<% } else{ %>");
                setAttributeIfNotDefault(stringbuffer, "shift", fieldnode.getFieldShift() + "", "A");
                setAttributeIfNotDefault(stringbuffer, "ptype", fieldnode.getFieldType().toString(), "FT_ALPHA");
                stringbuffer.append("<% }%>");
            } else
            {
                setAttributeIfNotDefault(stringbuffer, "shift", fieldnode.getFieldShift() + "", "A");
                setAttributeIfNotDefault(stringbuffer, "ptype", fieldnode.getFieldType().toString(), "FT_ALPHA");
            }
            setAttributeIfNotDefault(stringbuffer, "decpos", fieldnode.getDecimals(), 0);
            setAttributeIfNotDefault(stringbuffer, "check", getCheckAttributes(), "");
            setAttributeIfNotDefault(stringbuffer, "range", getRange(), "");
            if(!getValues().equals("") && (!getEdtcde().equals("") || !getEdtwrd().equals("")))
                setAttributeIfNotDefault(stringbuffer, "values", "<%=" + getBeanName() + ".getValuesAfterEditing(\"" + getFieldName() + "\")%>", "");
            else
                setAttributeIfNotDefault(stringbuffer, "values", getValues(), "");
            Vector vector = super._webSettings.getLabelsForValues();
            if(vector != null && vector.size() > 0)
                stringbuffer.append("valuesLabelArr:" + super._webSettings.getLabelsForValuesInArrayFormat() + ",");
            setAttributeIfNotDefault(stringbuffer, "edtcde", getEdtcde(), "");
            setAttributeIfNotDefault(stringbuffer, "edtwrd", getEdtwrd(), "");
            if(hasValNum())
                stringbuffer.append("valnum:\"TRUE\",");
            setAttributeIfNotDefault(stringbuffer, "comp", getComp(), "");
            KeywordNodeEnumeration keywordnodeenumeration1 = getFieldNode().getKeywordsOfType(70);
            if(keywordnodeenumeration1.hasMoreElements())
            {
                stringbuffer.append("chkmsg:\"<%=").append(getBeanName()).append(".getCHKMSG(\"");
                stringbuffer.append(getFieldName()).append("\")%>\",");
            }
            String s4 = getDatFmt();
            if(s4 != null)
                setAttributeIfNotDefault(stringbuffer, "datfmt", s4, "ISO");
            String s5 = getDatSep();
            if(s5 != null)
                setAttributeIfNotDefault(stringbuffer, "datsep", s5, "JOB");
            s4 = getTimFmt();
            if(s4 != null)
                setAttributeIfNotDefault(stringbuffer, "timfmt", s4, "ISO");
            s5 = getTimSep();
            if(s5 != null)
                setAttributeIfNotDefault(stringbuffer, "timsep", s5, "JOB");
            if(isWrapped())
            {
                int i = DSPSIZConstants.getScreenWidth(getDisplaySizeIndex());
                if(i > 0)
                    stringbuffer.append("maxNumOfCols:").append(i).append(",");
            }
            stringbuffer.append("datalength:").append(fieldnode.getLength()).append("}");
            String s6 = jsEventHandlers();
            if(s6 != null)
                stringbuffer.append(",").append(s6);
            stringbuffer.append(");");
            clientscriptsourcecodecollection.addElement(stringbuffer.toString());
            clientscriptsourcecodecollection = NDAndPRConditionedClientScript(clientscriptsourcecodecollection, s, s1);
        }
        catch(Throwable throwable)
        {
            Util.logThrowableMessage("error in OutputFieldOutput.getScript()", throwable, false);
        }
        return clientscriptsourcecodecollection;
    }

    public String getClientScriptLocation()
    {
        return "document.SCREEN.";
    }

    public String getComp()
    {
        FieldNode fieldnode = getFieldNode();
        KeywordNodeEnumeration keywordnodeenumeration = fieldnode.getKeywordsOfType(78);
        if(keywordnodeenumeration == null)
            keywordnodeenumeration = fieldnode.getKeywordsOfType(75);
        String s = null;
        if(keywordnodeenumeration != null)
        {
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            if(keywordnode != null)
            {
                s = keywordnode.getParmsAsString();
                if(!fieldnode.getFieldType().isOfType(9) && !s.substring(0, 2).equals("EQ") && !s.substring(0, 2).equals("NE"))
                {
                    fieldnode.logEvent(15);
                    return "";
                }
                int i = s.indexOf(" ");
                int j = s.indexOf("'");
                String s1;
                if(j == -1)
                    s1 = s.substring(i + 1, s.length());
                else
                    s1 = s.substring(j, s.length());
                s = s.substring(0, i) + ";";
                if(!fieldnode.getFieldType().isOfType(9))
                    s1 = s1.substring(1, s1.length() - 1);
                s = s + s1;
            }
        }
        return s != null ? s : "";
    }

    public DHTMLSourceCodeCollection getDHTML()
    {
        if(notVisibleForNDandPR())
            return new DHTMLSourceCodeCollection();
        else
            return super.getDHTML();
    }

    FieldLines getGeneratedDHTML()
    {
        DHTMLSourceCodeCollection dhtmlsourcecodecollection = new DHTMLSourceCodeCollection();
        FieldNode fieldnode = getFieldNode();
        String s = fieldnode.getDisplayAttributes().getNDIndExpr();
        String s1 = fieldnode.getDisplayAttributes().getPRIndExpr();
        String s2 = getTagId();
        int i = getDHTMLHeight();
        int j = getDHTMLWidth();
        String s3 = getFieldTextWithTransform(1);
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(50);
        paddedstringbuffer.concat(protectConditionedText(), " id=\"", s2, "\" previousValue=\"", s3, "\" ");
        if(fieldnode.getFieldShift() == 'I')
            paddedstringbuffer.append(" READONLY ");
        paddedstringbuffer.append(getStyleClass());
        paddedstringbuffer.append(getBidiDirection());
        Object obj = null;
        if(i > 1)
        {
            PaddedStringBuffer paddedstringbuffer1 = new PaddedStringBuffer(50);
            paddedstringbuffer1.concat(super._webSettings.getHtmlBefore(), "<textarea rows=", Integer.toString(i), " cols=", Integer.toString(j), " WRAP=SOFT style=\"overflow-y : auto;\" ", paddedstringbuffer.toString(), " size=", Integer.toString(i * j), " maxLength=", Integer.toString(getMaxLength()), " ", super._webSettings.getHtmlInside(), ">", s3, "</textarea>", super._webSettings.getHtmlAfter());
            dhtmlsourcecodecollection.addElement(paddedstringbuffer1);
            if(s != null)
            {
                DHTMLSourceCodeCollection dhtmlsourcecodecollection1 = new DHTMLSourceCodeCollection();
                PaddedStringBuffer paddedstringbuffer2 = new PaddedStringBuffer(50);
                paddedstringbuffer2.concat(super._webSettings.getHtmlBefore(), "<INPUT type=\"password\"", paddedstringbuffer.toString(), "VALUE=\"", s3, "\" SIZE=", Integer.toString(j), " MAXLENGTH=", Integer.toString(getMaxLength()), " ", super._webSettings.getHtmlInside(), " >", super._webSettings.getHtmlAfter());
                dhtmlsourcecodecollection1.addElement(paddedstringbuffer2);
                if(_nonDisplayOverride)
                    dhtmlsourcecodecollection = keywordConditionedDHTML(dhtmlsourcecodecollection, dhtmlsourcecodecollection1, "");
                else
                    dhtmlsourcecodecollection = keywordConditionedDHTML(dhtmlsourcecodecollection, dhtmlsourcecodecollection1, s);
            }
        } else
        {
            int k = j;
            String s4 = " ";
            if(k < 3)
                s4 = " STYLE=\"width: " + 1.2D * (double)k + "em\"";
            else
            if(k < 6)
                s4 = " STYLE=\"width: auto\"";
            PaddedStringBuffer paddedstringbuffer3 = new PaddedStringBuffer(50);
            paddedstringbuffer3.concat(super._webSettings.getHtmlBefore(), "<INPUT ", nonDisplayConditionedText(), paddedstringbuffer.toString(), "VALUE=\"", s3, "\" SIZE=", Integer.toString(k), " MAXLENGTH=", Integer.toString(getMaxLength()), s4, super._webSettings.getHtmlInside(), " >", super._webSettings.getHtmlAfter());
            dhtmlsourcecodecollection.addElement(paddedstringbuffer3);
        }
        dhtmlsourcecodecollection = NDAndPRConditionedDHTML(dhtmlsourcecodecollection, s, s1);
        FieldLines fieldlines = new FieldLines();
        fieldlines.add(dhtmlsourcecodecollection);
        return fieldlines;
    }

    public int getHTMLTransform()
    {
        return 1;
    }

    int getMaxLength()
    {
        return getFieldNode().getMaxDisplayChars();
    }

    public String getMouseEvents()
    {
        return "";
    }

    String getOnClick()
    {
        String s = getFieldNode().getDisplayAttributes().getSPIndExpr();
        if(s != null)
            return "onClick=\"handleDSPATR_SP();\"";
        else
            return "";
    }

    protected String getOnKeyPressString()
    {
        if(getFieldNode().getFieldShift() == 'Y')
            return "onKeyPress=\"checkCharShiftY();\" ";
        else
            return "";
    }

    public String getRange()
    {
        FieldNode fieldnode = getFieldNode();
        KeywordNodeEnumeration keywordnodeenumeration = fieldnode.getKeywordsOfType(165);
        String s = null;
        if(keywordnodeenumeration != null)
        {
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            if(keywordnode != null)
            {
                s = keywordnode.getParmsAsString();
                int i = s.indexOf(" ");
                s = s.substring(0, i) + ";" + s.substring(i + 1);
                if(!fieldnode.getFieldType().isOfType(9))
                {
                    keywordnode.logEvent(9);
                    return "";
                }
            }
        }
        return s != null ? s : "";
    }

    public String getValues()
    {
        FieldNode fieldnode = getFieldNode();
        KeywordNodeEnumeration keywordnodeenumeration = fieldnode.getKeywordsOfType(222);
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer("");
        if(keywordnodeenumeration != null)
        {
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            if(keywordnode != null)
            {
                int i = 0;
                for(KeywordParm keywordparm = keywordnode.getParm(i); null != keywordparm; keywordparm = keywordnode.getParm(++i))
                {
                    String s;
                    if(keywordparm.getParmType() == 15)
                        s = keywordparm.getVarString();
                    else
                        s = keywordparm.getJavaString();
                    paddedstringbuffer.append(s);
                    if(keywordparm != null)
                        paddedstringbuffer.append(";");
                }

            }
        }
        return paddedstringbuffer.toString();
    }

    public JavaSourceCodeCollection getViewBeanInitialization()
    {
        JavaSourceCodeCollection javasourcecodecollection = new JavaSourceCodeCollection();
        javasourcecodecollection.setIndentLevel(1);
        javasourcecodecollection.addAll(super.getViewBeanInitialization());
        if(getHeight() > 1)
            javasourcecodecollection.addLine(getQualifiedFieldName() + ".setHeight(" + getHeight() + ");");
        KeywordNodeEnumeration keywordnodeenumeration = getFieldNode().getKeywordsOfType(70);
        if(keywordnodeenumeration.hasMoreElements())
            generateCHKMSGIDDefinition(javasourcecodecollection, keywordnodeenumeration);
        if(!getValues().equals("") && (!getEdtcde().equals("") || !getEdtwrd().equals("")))
            javasourcecodecollection.addLine(getQualifiedFieldName() + ".addValues(\"" + getValues() + "\");");
        keywordnodeenumeration = getFieldNode().getKeywordsOfType(204);
        if(keywordnodeenumeration.hasMoreElements())
        {
            String s = getFieldNode().getDisplayAttributes().getNDIndExpr();
            if(s != null)
                if(s.equals(""))
                    javasourcecodecollection.addLine(getQualifiedFieldName() + ".add(new KeywordDefinition(PAR_DSPATR_ND));");
                else
                    javasourcecodecollection.addLine(getQualifiedFieldName() + ".add(new KeywordDefinition(PAR_DSPATR_ND,\"" + s + "\"));");
        }
        String s1 = getFieldNode().getDisplayAttributes().getPRIndExpr();
        if(s1 != null)
            if(s1.equals(""))
                javasourcecodecollection.addLine(getQualifiedFieldName() + ".add(new KeywordDefinition(PAR_DSPATR_PR));");
            else
                javasourcecodecollection.addLine(getQualifiedFieldName() + ".add(new KeywordDefinition(PAR_DSPATR_PR,\"" + s1 + "\"));");
        s1 = getFieldNode().getDisplayAttributes().getMDTIndExpr();
        if(s1 != null)
            if(s1.equals(""))
                javasourcecodecollection.addLine(getQualifiedFieldName() + ".add(new KeywordDefinition(PAR_DSPATR_MDT));");
            else
                javasourcecodecollection.addLine(getQualifiedFieldName() + ".add(new KeywordDefinition(PAR_DSPATR_MDT,\"" + s1 + "\"));");
        return javasourcecodecollection;
    }

    public boolean hasValNum()
    {
        FieldNode fieldnode = getFieldNode();
        KeywordNodeEnumeration keywordnodeenumeration = fieldnode.getKeywordsOfType(221);
        if(keywordnodeenumeration != null)
        {
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            if(keywordnode != null)
                return true;
        }
        com.ibm.as400ad.code400.dom.RecordNode recordnode = fieldnode.getParentRecord();
        keywordnodeenumeration = recordnode.getKeywordsOfType(221);
        if(keywordnodeenumeration != null)
        {
            KeywordNode keywordnode1 = keywordnodeenumeration.nextKeyword();
            if(keywordnode1 != null)
                return true;
        }
        FileNode filenode = (FileNode)recordnode.getParent();
        keywordnodeenumeration = filenode.getKeywordsOfType(221);
        if(keywordnodeenumeration != null)
        {
            KeywordNode keywordnode2 = keywordnodeenumeration.nextKeyword();
            if(keywordnode2 != null)
                return true;
        }
        return false;
    }

    protected void initializeWidthAndHeight()
    {
        FieldNode fieldnode = getFieldNode();
        if(fieldnode.isContinuedField())
        {
            KeywordNodeEnumeration keywordnodeenumeration = fieldnode.getKeywordsOfType(76);
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            if(keywordnode != null)
            {
                KeywordParm keywordparm = keywordnode.getFirstParm();
                if(keywordparm != null)
                {
                    int i = keywordparm.getVarNumber();
                    if(i > 0)
                    {
                        super._height = super._width / i + (super._width % i != 0 ? 1 : 0);
                        super._width = i;
                    }
                }
            }
        }
    }

    public boolean isSingleDHTMLElement()
    {
        return true;
    }

    protected ClientScriptSourceCodeCollection NDAndPRConditionedClientScript(ClientScriptSourceCodeCollection clientscriptsourcecodecollection, String s, String s1)
    {
        ClientScriptSourceCodeCollection clientscriptsourcecodecollection1 = new ClientScriptSourceCodeCollection();
        clientscriptsourcecodecollection1.addAll(NDAndPRConditionedSource(clientscriptsourcecodecollection, s, s1));
        return clientscriptsourcecodecollection1;
    }

    protected DHTMLSourceCodeCollection NDAndPRConditionedDHTML(DHTMLSourceCodeCollection dhtmlsourcecodecollection, String s, String s1)
    {
        DHTMLSourceCodeCollection dhtmlsourcecodecollection1 = new DHTMLSourceCodeCollection();
        dhtmlsourcecodecollection1.addAll(NDAndPRConditionedSource(dhtmlsourcecodecollection, s, s1));
        return dhtmlsourcecodecollection1;
    }

    protected SourceCodeCollection NDAndPRConditionedSource(SourceCodeCollection sourcecodecollection, String s, String s1)
    {
        if(s == null || s1 == null)
            return sourcecodecollection;
        String s2 = s.equals("") ? "" : getEvalIndExprCall(s);
        String s3 = s1.equals("") ? "" : getEvalIndExprCall(s1);
        String s4;
        if(s2.equals(""))
        {
            if(s3.equals(""))
                return new SourceCodeCollection();
            s4 = s3;
        } else
        if(s3.equals(""))
            s4 = s2;
        else
            s4 = s2 + " && " + s3;
        SourceCodeCollection sourcecodecollection1 = new SourceCodeCollection();
        sourcecodecollection1.addElement("<% if (!(" + s4 + ")) { %>" + sourcecodecollection1.getNewline());
        sourcecodecollection1.addAll(sourcecodecollection);
        sourcecodecollection1.addElement(sourcecodecollection1.getNewline() + "<% } %>" + sourcecodecollection1.getNewline());
        return sourcecodecollection1;
    }

    protected String nonDisplayConditionedText()
    {
        String s = getFieldNode().getDisplayAttributes().getNDIndExpr();
        if(!_nonDisplayOverride && s == null)
            return "";
        if(_nonDisplayOverride || s.equals(""))
        {
            return " type=\"password\" ";
        } else
        {
            PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(50);
            paddedstringbuffer.concat(" <%if (", getEvalIndExprCall(s), ") {%>", " type=\"password\" ", " <%}%> ");
            return paddedstringbuffer.toString();
        }
    }

    boolean notVisibleForNDandPR()
    {
        FieldNode fieldnode = getFieldNode();
        String s = fieldnode.getDisplayAttributes().getNDIndExpr();
        String s1 = fieldnode.getDisplayAttributes().getPRIndExpr();
        return s != null && s.equals("") && s1 != null && s1.equals("");
    }

    protected String protectConditionedText()
    {
        String s = getFieldNode().getDisplayAttributes().getPRIndExpr();
        if(s == null)
            return "<% if (isProtected) { %> readonly tabindex=-1 <% } %>";
        if(s.equals(""))
        {
            return " readonly tabindex=-1 ";
        } else
        {
            PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(50);
            paddedstringbuffer.concat(" <%if (isProtected || ", getEvalIndExprCall(s), ") {%>", " readonly tabindex=-1 ", " <%}%> ");
            return paddedstringbuffer.toString();
        }
    }

    protected void setNonDisplayOverride(boolean flag)
    {
        _nonDisplayOverride = flag;
    }

    public boolean useLargestRectangle()
    {
        return isWrapped();
    }

    private String getBidiDirection()
    {
        CheckAttributes checkattributes = getFieldNode().getCheckAttributes();
        if(checkattributes.isRL() && getFieldNode().getFieldShift() == 'A')
            return " dir=\"rtl\" ";
        else
            return "";
    }

    public JavaSourceCodeCollection getDataBeanInitialization()
    {
        JavaSourceCodeCollection javasourcecodecollection = super.getDataBeanInitialization();
        try
        {
            FieldNode fieldnode = getFieldNode();
            String s = getNonConditionedCheckAttributes();
            if(s != null && s.length() > 0)
                javasourcecodecollection.addLine(getQualifiedFieldName() + ".setCheckAttr(\"" + s + "\");");
            if(fieldnode.getFieldType().isOfType(16))
            {
                String s1 = getTimFmt();
                if(s1 != null && !s1.equals("ISO"))
                    javasourcecodecollection.addLine(getQualifiedFieldName() + ".setTimFmt(\"" + s1 + "\");");
            }
        }
        catch(Throwable throwable)
        {
            Util.logThrowableMessage("error in InputFieldOutput.getDataBeanInitialization()", throwable, false);
        }
        return javasourcecodecollection;
    }

    public void addProtectDsplAttr(PaddedStringBuffer paddedstringbuffer, PaddedStringBuffer paddedstringbuffer1, String s)
    {
        if(s == null)
            paddedstringbuffer1.concat("<%=", "isProtected", "?\"", "wf_", "pr", "\":\"\"%> ");
        else
        if(s.equals(""))
        {
            paddedstringbuffer1.append("wf_");
            paddedstringbuffer1.append("pr");
            paddedstringbuffer1.append(" ");
        } else
        {
            paddedstringbuffer.concat("dsplAttrBean", ".setProtectIndExpr(", "isProtected", "?\"\":\"", s, "\"); ");
        }
    }

    protected String jsEventHandlers()
    {
        try
        {
            StringBuffer stringbuffer = new StringBuffer(100);
            stringbuffer.append("{");
            if(getHeight() <= 1)
            {
                String s = getFieldNode().getDisplayAttributes().getSPIndExpr();
                if(s != null)
                    stringbuffer.append("onClick:\"\"");
            }
            CheckAttributes checkattributes = getFieldNode().getCheckAttributes();
            String s1 = checkattributes.getERIndExpr();
            if(s1 != null)
            {
                if(stringbuffer.length() != 1)
                    stringbuffer.append(",");
                if(s1.equals(""))
                    stringbuffer.append("onKeyUp:\"\"");
                else
                    stringbuffer.append("<% if(" + getEvalIndExprCall(s1) + "){ %>onKeyUp:\"\"<% } %>");
            }
            if(stringbuffer.length() == 1)
            {
                return null;
            } else
            {
                stringbuffer.append("}");
                return stringbuffer.toString();
            }
        }
        catch(Throwable throwable)
        {
            return null;
        }
    }

    static final String copyRight = new String("(C) Copyright IBM Corporation 1999-2003, all rights reserved");
    private String _checkAttributes;
    private String _nonConditionedCheckAttributes;
    private boolean _nonDisplayOverride;
    private static final boolean JS_MDT_DEFAULT = false;
    private static final String JS_SHIFT_DEFAULT = "A";
    private static final String JS_PTYPE_DEFAULT = "FT_ALPHA";
    private static final int JS_DATALENGTH_DEFAULT = 0;
    private static final int JS_DECPOS_DEFAULT = 0;
    private static final String JS_CHECK_DEFAULT = "";
    private static final String JS_RANGE_DEFAULT = "";
    public static final String JS_VALUES_DEFAULT = "";
    public static final String JS_EDTCDE_DEFAULT = "";
    public static final String JS_EDTWRD_DEFAULT = "";
    private static final String JS_VALNUM_DEFAULT = "FALSE";
    private static final String JS_COMP_DEFAULT = "";
    private static final String JS_VALIDATE_DEFAULT = "TRUE";
    private static final String JS_CHKMSG_DEFAULT = "";
    private static final String JS_TIMFMT_DEFAULT = "ISO";
    private static final String JS_TIMSEP_DEFAULT = "JOB";
    private static final String JS_DATFMT_DEFAULT = "ISO";
    private static final String JS_DATSEP_DEFAULT = "JOB";
    private static final int JS_ERRORID_DEFAULT = 0;
    private static final String JS_ATTRS_DEFAULT = "";
    private static final boolean JS_CURRENT_DEFAULT = false;
    private static final int JS_ROW_DEFAULT = 1;
    private static final int JS_COLUMN_DEFAULT = 1;
    private static final String JS_BUFFER_DEFAULT = "";
    private static final String JS_FORMAT_DEFAULT = "FMTXXX";

}
