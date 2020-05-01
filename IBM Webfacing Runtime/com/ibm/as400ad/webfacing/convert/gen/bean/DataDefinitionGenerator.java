// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.webfacing.common.*;
import com.ibm.as400ad.webfacing.convert.ExportHandler;
import com.ibm.as400ad.webfacing.convert.IFieldOutput;
import com.ibm.as400ad.webfacing.convert.gen.WebResourceFileWriter;
import com.ibm.as400ad.webfacing.convert.model.FieldOutputEnumeration;
import com.ibm.as400ad.webfacing.convert.model.RecordLayout;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.bean:
//            DefinitionGenerator, ResponseIndicatorList

public class DataDefinitionGenerator extends DefinitionGenerator
{

    public DataDefinitionGenerator(RecordLayout recordlayout, WebResourceFileWriter webresourcefilewriter)
    {
        super(recordlayout, webresourcefilewriter);
        _inputFieldLengthAccumulator = 0L;
        _outputFieldLengthAccumulator = 0L;
    }

    protected void accumulateInputFieldLength(char c, int i)
    {
        if(c == 'I' || c == 'B')
            _inputFieldLengthAccumulator += i;
    }

    protected void accumulateOutputFieldLength(char c, int i, boolean flag)
    {
        if((c == 'O' || c == 'B') && !flag)
            _outputFieldLengthAccumulator += i;
    }

    private void generateBufferLengthInitialization(int i, int j)
    {
        KeywordNodeEnumeration keywordnodeenumeration = getFileNode().getKeywordsOfType(126);
        boolean flag = keywordnodeenumeration != null && keywordnodeenumeration.hasMoreElements();
        if(flag)
            addLineToConstructor("setSeparateIndicatorArea(" + flag + ");");
        long l = (_outputFieldLengthAccumulator + (flag ? 0L : i)) * 2L;
        if(l > 0L)
            addLineToConstructor("setOutputIOBufferLength(" + l + ");");
        l = (_inputFieldLengthAccumulator + (flag ? 0L : j)) * 2L;
        if(l > 0L)
            addLineToConstructor("setInputIOBufferLength(" + l + ");");
    }

    protected void generateCodeForFieldKeywords(FieldNode fieldnode, String s)
    {
        KeywordNodeEnumeration keywordnodeenumeration = null;
        keywordnodeenumeration = fieldnode.getKeywordsOfType(84);
        boolean flag = setDefaultValue(s, keywordnodeenumeration);
        keywordnodeenumeration = fieldnode.getKeywordsOfType(85);
        flag = setDefaultValue(s, keywordnodeenumeration) || flag;
        if(flag)
            setOVRDTA(s, getRecordNode().getKeywordsOfType(154), fieldnode.getKeywordsOfType(154));
        keywordnodeenumeration = fieldnode.getKeywordsOfType(148);
        for(int i = 1; keywordnodeenumeration.hasMoreElements(); i++)
        {
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            KeywordParmEnumeration keywordparmenumeration = keywordnode.getParms();
            PaddedStringBuffer paddedstringbuffer = null;
            String s1 = s + "$MSGID" + Integer.toString(i);
            addLineToConstructor("MSGIDDefinition " + s1 + " = new MSGIDDefinition();");
            String s2 = keywordnode.getIndicatorString();
            if(s2 != null)
            {
                paddedstringbuffer = new PaddedStringBuffer(35);
                paddedstringbuffer.concat(s1, ".setIndicatorExpression(\"", s2, "\");");
                addLineToConstructor(paddedstringbuffer.toString());
            }
            KeywordParm keywordparm = keywordparmenumeration.nextParm();
            String s3 = keywordparm.getVarString();
            if("*NONE".equals(s3))
                addLineToConstructor(s1 + ".noneIsSet();");
            else
            if(s3 == null)
            {
                addLineToConstructor(s1 + ".noneIsSet();");
            } else
            {
                if(s3.indexOf("&") == 0)
                {
                    paddedstringbuffer = new PaddedStringBuffer(35);
                    paddedstringbuffer.concat(s1, ".setMsgIdFieldName(\"", WebfacingConstants.replaceSpecialCharacters(s3.substring(1)), "\");");
                    addLineToConstructor(paddedstringbuffer.toString());
                } else
                if(s3.length() == 3)
                {
                    paddedstringbuffer = new PaddedStringBuffer(35);
                    paddedstringbuffer.concat(s1, ".setMsgIdPrefix(\"", s3, "\");");
                    addLineToConstructor(paddedstringbuffer.toString());
                    KeywordParm keywordparm1 = keywordparmenumeration.nextParm();
                    s3 = keywordparm1.getVarString();
                    if(s3.indexOf("&") == 0)
                    {
                        paddedstringbuffer = new PaddedStringBuffer(35);
                        paddedstringbuffer.concat(s1, ".setMsgIdFieldName(\"", WebfacingConstants.replaceSpecialCharacters(s3.substring(1)), "\");");
                        addLineToConstructor(paddedstringbuffer.toString());
                    } else
                    {
                        paddedstringbuffer = new PaddedStringBuffer(35);
                        paddedstringbuffer.concat(s1, ".setMsgId(\"", s3, "\");");
                        addLineToConstructor(paddedstringbuffer.toString());
                    }
                } else
                {
                    paddedstringbuffer = new PaddedStringBuffer(35);
                    paddedstringbuffer.concat(s1, ".setMsgId(\"", s3, "\");");
                    addLineToConstructor(paddedstringbuffer.toString());
                }
                KeywordParm keywordparm2 = keywordparmenumeration.nextParm();
                s3 = keywordparm2.getVarString();
                int j = s3.indexOf("/");
                String s4 = null;
                if(j >= 0)
                {
                    String s5 = s3.substring(0, j);
                    s4 = s3.substring(j + 1);
                    if(s5.indexOf("&") == 0)
                    {
                        paddedstringbuffer = new PaddedStringBuffer(35);
                        paddedstringbuffer.concat(s1, ".setLibraryFieldName(\"", WebfacingConstants.replaceSpecialCharacters(s5.substring(1)), "\");");
                        addLineToConstructor(paddedstringbuffer.toString());
                    } else
                    {
                        paddedstringbuffer = new PaddedStringBuffer(35);
                        paddedstringbuffer.concat(s1, ".setLibraryName(\"", s5, "\");");
                        addLineToConstructor(paddedstringbuffer.toString());
                    }
                } else
                {
                    s4 = s3;
                }
                if(s4.indexOf("&") == 0)
                {
                    paddedstringbuffer = new PaddedStringBuffer(35);
                    paddedstringbuffer.concat(s1, ".setFileFieldName(\"", WebfacingConstants.replaceSpecialCharacters(s4.substring(1)), "\");");
                    addLineToConstructor(paddedstringbuffer.toString());
                } else
                {
                    paddedstringbuffer = new PaddedStringBuffer(35);
                    paddedstringbuffer.concat(s1, ".setFileName(\"", s4, "\");");
                    addLineToConstructor(paddedstringbuffer.toString());
                }
            }
            paddedstringbuffer = new PaddedStringBuffer(35);
            paddedstringbuffer.concat(s, ".addMSGIDKeyword(", s1, ");");
            addLineToConstructor(paddedstringbuffer.toString());
            addLineToConstructor("");
        }

    }

    protected void generateFieldDataDefinitions(ResponseIndicatorList responseindicatorlist)
    {
        try
        {
            for(FieldOutputEnumeration fieldoutputenumeration = getRecordLayout().getAllFields(); fieldoutputenumeration.hasMoreElements();)
            {
                IFieldOutput ifieldoutput = fieldoutputenumeration.nextFieldOutput();
                FieldNode fieldnode = ifieldoutput.getFieldNode();
                if(!fieldnode.isUnnamedConstantField())
                {
                    responseindicatorlist.populateFrom(fieldnode);
                    String s = ifieldoutput.getQualifiedFieldName();
                    getConstructor().addAll(ifieldoutput.getDataBeanInitialization());
                    generateCodeForFieldKeywords(fieldnode, s);
                    addLineToConstructor("add(" + s + ");");
                    char c = fieldnode.getFieldIOCapability();
                    accumulateInputFieldLength(c, fieldnode.getLength());
                    accumulateOutputFieldLength(c, fieldnode.getLength(), null != fieldnode.findKeywordById(148));
                }
            }

        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, "error in DataBeanGenerator.generateFieldDataDefinitions() while generating " + getBeanName() + " = " + throwable);
            ExportHandler.err(3, throwable);
        }
    }

    protected String getBeanBaseClassSuffix()
    {
        return "RecordDataDefinition";
    }

    protected String getBeanName()
    {
        return super.getBeanName() + "Data";
    }

    private Vector getReferencedOptionIndicators()
    {
        Vector vector = new Vector();
        try
        {
            RecordNode recordnode = getRecordNode();
            StringBuffer stringbuffer = new StringBuffer();
            getReferencedOptionIndicatorsFromKeywords(getFileNode(), stringbuffer);
            getReferencedOptionIndicatorsFromKeywords(recordnode, stringbuffer);
            Iterator iterator = recordnode.getHelpspecs();
            if(iterator != null)
            {
                HelpspecNode helpspecnode;
                for(; iterator.hasNext(); getReferencedOptionIndicatorsFromKeywords(helpspecnode, stringbuffer))
                    helpspecnode = (HelpspecNode)iterator.next();

            }
            FieldNode fieldnode;
            for(FieldNodeEnumeration fieldnodeenumeration = recordnode.getFields(); fieldnodeenumeration.hasMoreElements(); getReferencedOptionIndicatorsFromKeywords(fieldnode, stringbuffer))
            {
                fieldnode = fieldnodeenumeration.nextField();
                String s1 = fieldnode.getIndicatorString();
                if(s1 != null)
                    stringbuffer.append(' ').append(s1);
            }

            String s = stringbuffer.toString();
            if(s.length() > 0)
            {
                for(StringTokenizer stringtokenizer = new StringTokenizer(s, " \t\n\r\fNO"); stringtokenizer.hasMoreTokens();)
                {
                    String s2 = stringtokenizer.nextToken();
                    if(s2.charAt(0) == '0')
                        s2 = s2.substring(1);
                    if(!vector.contains(s2))
                        vector.addElement(s2);
                }

            }
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, "error in DataBeanGenerator.getReferencedOptionIndicators() while generating " + getBeanName() + " = " + throwable);
            ExportHandler.err(3, throwable);
        }
        return vector;
    }

    private void getReferencedOptionIndicatorsFromKeywords(AnyNodeWithKeywords anynodewithkeywords, StringBuffer stringbuffer)
    {
        try
        {
            Vector vector = anynodewithkeywords.getKeywordsVector();
            if(null != vector)
            {
                for(int i = 0; i < vector.size(); i++)
                {
                    String s = ((KeywordNode)vector.elementAt(i)).getIndicatorString();
                    if(s != null)
                        stringbuffer.append(' ').append(s);
                }

            }
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, "error in DataBeanGenerator.getReferencedOptionIndicatorsFromKeywords() while generating " + getBeanName() + " = " + throwable);
            ExportHandler.err(3, throwable);
        }
    }

    protected void specifyNullConstructorBody()
    {
        try
        {
            String s = getFileNode().getMemberType();
            if(s.equals("MNUDDS"))
                addLineToConstructor("setFileMemberType(\"" + s + "\");");
            addLineToConstructor("setVersionDigits(" + Version.getVersionDigits() + ");");
            ResponseIndicatorList responseindicatorlist = new ResponseIndicatorList();
            responseindicatorlist.populateFrom(getFileNode());
            responseindicatorlist.populateFrom(getRecordNode());
            generateFieldDataDefinitions(responseindicatorlist);
            Vector vector = getReferencedOptionIndicators();
            responseindicatorlist.generateIndicatorDataDefinition(vector, getConstructor());
            generateBufferLengthInitialization(vector.size(), responseindicatorlist.size());
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, throwable, "error in DataDefinitionGenerator.specifyNullConstructorBody() while generating " + getBeanName() + " = " + throwable);
        }
    }

    private boolean setDefaultValue(String s, KeywordNodeEnumeration keywordnodeenumeration)
    {
        try
        {
            if(keywordnodeenumeration != null && keywordnodeenumeration.hasMoreElements())
            {
                KeywordNode keywordnode = (KeywordNode)keywordnodeenumeration.nextElement();
                KeywordParm keywordparm = keywordnode.getFirstParm();
                if(keywordparm != null)
                {
                    StringBuffer stringbuffer = (new StringBuffer()).append(s + ".setDefaultValue(\"");
                    String s1 = keywordparm.getVarStringUnquoted();
                    if(s1 != null)
                        stringbuffer.append(keywordparm.getJavaString() + "\"");
                    if(keywordnode.getIndicatorString() != null)
                        stringbuffer.append(", \"" + keywordnode.getIndicatorString() + "\"");
                    stringbuffer.append(");");
                    addLineToConstructor(stringbuffer.toString());
                    return true;
                }
            }
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, throwable, "error in DataBeanGenerator.setDefaultValue() while generating " + getBeanName() + " = " + throwable);
        }
        return false;
    }

    private void setOVRDTA(String s, KeywordNodeEnumeration keywordnodeenumeration, KeywordNodeEnumeration keywordnodeenumeration1)
    {
        try
        {
            KeywordNodeEnumeration keywordnodeenumeration2 = keywordnodeenumeration1;
            if(keywordnodeenumeration2 == null || !keywordnodeenumeration2.hasMoreElements())
                keywordnodeenumeration2 = keywordnodeenumeration;
            if(keywordnodeenumeration2 != null && keywordnodeenumeration2.hasMoreElements())
            {
                KeywordNode keywordnode = keywordnodeenumeration2.nextKeyword();
                StringBuffer stringbuffer = (new StringBuffer()).append(s + ".setOVRDTA(\"");
                if(keywordnode.getIndicatorString() != null)
                    stringbuffer.append(keywordnode.getIndicatorString());
                stringbuffer.append("\");");
                addLineToConstructor(stringbuffer.toString());
            }
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, throwable, "error in DataBeanGenerator.setOVRDTA while generating " + getBeanName() + " = " + throwable);
        }
    }

    protected long _inputFieldLengthAccumulator;
    protected long _outputFieldLengthAccumulator;
    private static final String _beanBaseClassSuffix = "RecordDataDefinition";
}
