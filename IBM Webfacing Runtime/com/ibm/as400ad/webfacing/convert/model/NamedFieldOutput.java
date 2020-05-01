// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.model;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.code400.dom.constants.FieldType;
import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.convert.Util;
import com.ibm.as400ad.webfacing.convert.gen.bean.JavaSourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.ClientScriptSourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.rules.EditCodeMappingHandler;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.model:
//            FieldOutput, FieldWebSettings, SpecialCharHandler

public abstract class NamedFieldOutput extends FieldOutput
{

    public String getFieldText()
    {
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(50);
        paddedstringbuffer.concat("<%= ", getBeanName(), ".getFieldValue(\"", getFieldName(), "\") %>");
        return paddedstringbuffer.toString();
    }

    public NamedFieldOutput()
    {
        _timSep = null;
        _datSep = null;
        _timFmt = null;
        _datFmt = null;
    }

    public NamedFieldOutput(FieldNode fieldnode)
    {
        super(fieldnode);
        _timSep = null;
        _datSep = null;
        _timFmt = null;
        _datFmt = null;
        initialize();
        initializeFmtAndSep(fieldnode);
    }

    public JavaSourceCodeCollection getDataBeanInitialization()
    {
        JavaSourceCodeCollection javasourcecodecollection = new JavaSourceCodeCollection();
        javasourcecodecollection.setIndentLevel(1);
        try
        {
            FieldNode fieldnode = getFieldNode();
            String s = getFieldName();
            FieldType fieldtype = fieldnode.getFieldType();
            PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(85);
            paddedstringbuffer.concat("FieldDataDefinition ", getQualifiedFieldName(), " = new FieldDataDefinition(\"", s, "\", '", (new Character(fieldnode.getFieldIOCapability())).toString(), "', ", Integer.toString(fieldnode.getLength()), ", " + fieldtype.toString() + ", '", (new Character(fieldnode.getFieldShift())).toString(), "');");
            javasourcecodecollection.addLine(paddedstringbuffer.toString());
            if(fieldtype.isOfType(9) || fieldtype.isOfType(11) || fieldtype.isOfType(10))
                javasourcecodecollection.addLine(getQualifiedFieldName() + ".setDecimalPrecision(" + fieldnode.getDecimals() + ");");
            if(fieldnode.getFieldType().isOfType(15))
            {
                String s1 = getDatFmt();
                String s2 = getDatSep();
                if(s1 != null && !s1.equals("ISO"))
                    javasourcecodecollection.addLine(getQualifiedFieldName() + ".setDatFmt(\"" + s1 + "\");");
                if(s2 != null && !s2.equals("JOB"))
                    javasourcecodecollection.addLine(getQualifiedFieldName() + ".setDatSep(\"" + s2 + "\");");
            }
            if(fieldnode.getFieldType().isOfType(16))
            {
                String s3 = getTimSep();
                if(s3 != null && !s3.equals("JOB"))
                    javasourcecodecollection.addLine(getQualifiedFieldName() + ".setTimSep(\"" + s3 + "\");");
            }
        }
        catch(Throwable throwable)
        {
            Util.logThrowableMessage("error in NamedFieldOutput.getDataBeanInitialization()", throwable, false);
        }
        return javasourcecodecollection;
    }

    public ClientScriptSourceCodeCollection getClientScript()
    {
        ClientScriptSourceCodeCollection clientscriptsourcecodecollection = new ClientScriptSourceCodeCollection();
        if(super._webSettings.getUsrDefineHTML() == null && !super._webSettings.isPrgDefine())
        {
            StringBuffer stringbuffer = new StringBuffer(60);
            stringbuffer.append("rc(\"").append(getTagId()).append("\",").append(getRowCol()).append(");");
            clientscriptsourcecodecollection.addElement(stringbuffer.toString());
        }
        return clientscriptsourcecodecollection;
    }

    public String getRowCol()
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append(getRowExprWithSlnoOffset()).append(",").append(getColumn());
        return stringbuffer.toString();
    }

    protected String getCallToFieldGetterWithTransform(String s)
    {
        return getCallToFieldGetterWithTransform(s, super.INDEX_NOT_USED, super.INDEX_NOT_USED);
    }

    public String getFieldTextWithTransform(int i)
    {
        return getFieldTextWithTransform(i, super.INDEX_NOT_USED, super.INDEX_NOT_USED);
    }

    public JavaSourceCodeCollection getViewBeanInitialization()
    {
        JavaSourceCodeCollection javasourcecodecollection = new JavaSourceCodeCollection();
        javasourcecodecollection.setIndentLevel(1);
        FieldNode fieldnode = getFieldNode();
        try
        {
            String s = getFieldName();
            PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(85);
            javasourcecodecollection.addLine("FieldViewDefinition " + getQualifiedFieldName() + " = new FieldViewDefinition(\"" + s + "\"," + getRow() + "," + getColumn() + "," + getWidth() + ");");
            KeywordNodeEnumeration keywordnodeenumeration = fieldnode.getKeywordsOfType(93);
            if(keywordnodeenumeration.hasMoreElements())
            {
                KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
                KeywordParm keywordparm1 = keywordnode.getFirstParm();
                StringBuffer stringbuffer = (new StringBuffer()).append(EditCodeMappingHandler.getSystemEditCode(keywordparm1.getVarChar()));
                keywordparm1 = keywordnode.getParm(1);
                if(keywordparm1 != null)
                    stringbuffer.append("', '" + keywordparm1.getVarChar());
                PaddedStringBuffer paddedstringbuffer1 = new PaddedStringBuffer(35);
                paddedstringbuffer1.concat(getQualifiedFieldName(), ".addEditCode('", stringbuffer.toString(), "');");
                javasourcecodecollection.addLine(paddedstringbuffer1.toString());
            }
            keywordnodeenumeration = fieldnode.getKeywordsOfType(95);
            if(keywordnodeenumeration.hasMoreElements())
            {
                KeywordParm keywordparm = keywordnodeenumeration.nextKeyword().getFirstParm();
                String s1 = keywordparm.getJavaString();
                PaddedStringBuffer paddedstringbuffer2 = new PaddedStringBuffer(35);
                paddedstringbuffer2.concat(getQualifiedFieldName(), ".addEditWord(\"", s1, "\");");
                javasourcecodecollection.addLine(paddedstringbuffer2.toString());
            }
            javasourcecodecollection.addCodeForXXXMSGKeyword(fieldnode.getKeywordsOfType(99), "ERR", s, getQualifiedFieldName());
            javasourcecodecollection.addCodeForXXXMSGIDKeyword(fieldnode.getKeywordsOfType(100), "ERR", getQualifiedFieldName(), getQualifiedFieldName());
            if(super._webSettings.isMask())
            {
                int i = super._webSettings.getMaskLow();
                int j = super._webSettings.getMaskHigh();
                javasourcecodecollection.addLine(getQualifiedFieldName() + ".setMasked(true);");
                javasourcecodecollection.addLine(getQualifiedFieldName() + ".setStartMaskingAt(" + i + ");");
                javasourcecodecollection.addLine(getQualifiedFieldName() + ".setEndMaskingAt(" + j + ");");
            }
        }
        catch(Throwable throwable)
        {
            Util.logThrowableMessage("error in FieldOutput.getBeanInitialization()", throwable, false);
        }
        return javasourcecodecollection;
    }

    public boolean isComputed()
    {
        return true;
    }

    public String getFieldId()
    {
        return getQualifiedFieldName();
    }

    protected String getCallToFieldGetterWithTransform(String s, int i, int j)
    {
        PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(100);
        paddedstringbuffer.concat(getBeanName(), ".getFieldValueWithTransform(\"", getFieldName(), "\", ", s);
        String s1 = getIndexParams(i, j);
        if(s1 != null)
            paddedstringbuffer.append("," + s1);
        paddedstringbuffer.append(")");
        return paddedstringbuffer.toString();
    }

    public String getFieldTextWithTransform(int i, int j, int k)
    {
        String s = null;
        String s1 = super._webSettings.getText();
        if(s1 != null)
        {
            s = super._webSettings.masking(s1);
            if(j != super.INDEX_NOT_USED && k != super.INDEX_NOT_USED)
                s = s.substring(j, k);
            s = super._charHandler.transformSpecialChars(s, i);
        } else
        {
            String s2 = super._charHandler.getTransformParam(i);
            s = FieldOutput.wrapJSPExpr(getCallToFieldGetterWithTransform(s2, j, k));
        }
        return s;
    }

    String getUsrDefineHTML()
    {
        String s = super._webSettings.getUsrDefineHTML();
        if(s != null)
            s = super._charHandler.replaceFieldValueSymbol(s);
        return s;
    }

    private void initializeFmtAndSep(FieldNode fieldnode)
    {
        if(fieldnode.getFieldType().isOfType(15))
        {
            KeywordNodeEnumeration keywordnodeenumeration = fieldnode.getKeywordsOfType(83);
            if(keywordnodeenumeration != null && keywordnodeenumeration.hasMoreElements())
            {
                int i = keywordnodeenumeration.nextKeyword().getFirstParm().getVarParmToken();
                switch(i)
                {
                case 425: 
                    _datSep = " ";
                    break;

                case 426: 
                    _datSep = ",";
                    break;

                case 427: 
                    _datSep = "JOB";
                    break;

                case 428: 
                    _datSep = "-";
                    break;

                case 429: 
                    _datSep = ".";
                    break;

                case 430: 
                    _datSep = "/";
                    break;
                }
            }
            keywordnodeenumeration = fieldnode.getKeywordsOfType(82);
            if(keywordnodeenumeration != null && keywordnodeenumeration.hasMoreElements())
            {
                int j = keywordnodeenumeration.nextKeyword().getFirstParm().getVarParmToken();
                switch(j)
                {
                case 416: 
                    _datFmt = "DMY";
                    break;

                case 417: 
                    _datFmt = "EUR";
                    _datSep = ".";
                    break;

                case 418: 
                    _datFmt = "ISO";
                    _datSep = "-";
                    break;

                case 419: 
                    _datFmt = "JIS";
                    _datSep = "-";
                    break;

                case 420: 
                    _datFmt = "JOB";
                    break;

                case 421: 
                    _datFmt = "JUL";
                    break;

                case 422: 
                    _datFmt = "MDY";
                    break;

                case 423: 
                    _datFmt = "USA";
                    _datSep = "/";
                    break;

                case 424: 
                    _datFmt = "YMD";
                    break;
                }
            } else
            {
                _datSep = "-";
            }
        }
        if(fieldnode.getFieldType().isOfType(16))
        {
            KeywordNodeEnumeration keywordnodeenumeration1 = fieldnode.getKeywordsOfType(215);
            if(keywordnodeenumeration1 != null && keywordnodeenumeration1.hasMoreElements())
            {
                int k = keywordnodeenumeration1.nextKeyword().getFirstParm().getVarParmToken();
                switch(k)
                {
                case 449: 
                    _timSep = " ";
                    break;

                case 450: 
                    _timSep = ":";
                    break;

                case 451: 
                    _timSep = ",";
                    break;

                case 452: 
                    _timSep = "JOB";
                    break;

                case 453: 
                    _timSep = ".";
                    break;
                }
            }
            keywordnodeenumeration1 = fieldnode.getKeywordsOfType(214);
            if(keywordnodeenumeration1 != null && keywordnodeenumeration1.hasMoreElements())
            {
                int l = keywordnodeenumeration1.nextKeyword().getFirstParm().getVarParmToken();
                switch(l)
                {
                case 444: 
                    _timFmt = "EUR";
                    _timSep = ".";
                    break;

                case 445: 
                    _timFmt = "HMS";
                    break;

                case 446: 
                    _timFmt = "ISO";
                    _timSep = ".";
                    break;

                case 447: 
                    _timFmt = "JIS";
                    _timSep = ":";
                    break;

                case 448: 
                    _timFmt = "USA";
                    _timSep = ":";
                    break;
                }
            } else
            {
                _timSep = ".";
            }
        }
    }

    public String getDatFmt()
    {
        return _datFmt;
    }

    public String getDatSep()
    {
        return _datSep;
    }

    public String getTimFmt()
    {
        return _timFmt;
    }

    public String getTimSep()
    {
        return _timSep;
    }

    public FieldWebSettings getWebsettings()
    {
        return super._webSettings;
    }

    static final String copyRight = new String("(c) Copyright IBM Corporation 1999-2003, all rights reserved");
    private String _timSep;
    private String _datSep;
    private String _timFmt;
    private String _datFmt;

}
