// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.convert.*;
import com.ibm.as400ad.webfacing.convert.gen.WebResourceFileWriter;
import com.ibm.as400ad.webfacing.convert.model.*;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.bean:
//            FeedbackDefinitionGenerator, DefinitionGenerator, SubfileResponseIndicatorList, JavaSourceCodeCollection

public class SFLCTLFeedbackDefinitionGenerator extends FeedbackDefinitionGenerator
    implements IWebResourceGenerator, ENUM_KeywordIdentifiers
{

    public SFLCTLFeedbackDefinitionGenerator(RecordLayout recordlayout, WebResourceFileWriter webresourcefilewriter)
    {
        super(recordlayout, webresourcefilewriter);
    }

    private void genRespIndsForSubfile()
    {
        RecordNode recordnode = getRecordNode().getRelatedSFL();
        getConstructor().addAll(SubfileResponseIndicatorList.generateFeedbackResponseIndicator(recordnode));
        FieldNode fieldnode;
        for(FieldNodeEnumeration fieldnodeenumeration = recordnode.getFields(); fieldnodeenumeration.hasMoreElements(); getConstructor().addAll(SubfileResponseIndicatorList.generateFeedbackResponseIndicator(fieldnode)))
            fieldnode = fieldnodeenumeration.nextField();

    }

    protected String getBeanBaseClassPrefix()
    {
        return "SubfileControl";
    }

    protected void specifyNullConstructorBody()
    {
        super.specifyNullConstructorBody();
        genRespIndsForSubfile();
        try
        {
            RecordNode recordnode = getRecordNode();
            addLineToConstructor("setRowPerSubfile(" + getRelatedSubfileRecordLayout().getHeight() + ");");
            getConstructor().addKeywordWithNoParam(recordnode.getKeywordsOfType(190));
            getConstructor().addKeywordWithNoParam(recordnode.getKeywordsOfType(185));
            KeywordNode keywordnode;
            for(KeywordNodeEnumeration keywordnodeenumeration = recordnode.getKeywordsOfType(190); keywordnodeenumeration.hasMoreElements(); addLineToConstructor("setSubfileFoldKey(\"" + keywordnode.getParmsAsString() + "\");"))
                keywordnode = keywordnodeenumeration.nextKeyword();

            KeywordNode keywordnode1;
            for(KeywordNodeEnumeration keywordnodeenumeration1 = recordnode.getKeywordsOfType(185); keywordnodeenumeration1.hasMoreElements(); addLineToConstructor("setSubfileDropKey(\"" + keywordnode1.getParmsAsString() + "\");"))
                keywordnode1 = keywordnodeenumeration1.nextKeyword();

            String s;
            for(KeywordNodeEnumeration keywordnodeenumeration2 = recordnode.getKeywordsOfType(194); keywordnodeenumeration2.hasMoreElements(); addLineToConstructor("setSubfileModeFieldName(\"" + s + "\");"))
            {
                KeywordNode keywordnode2 = keywordnodeenumeration2.nextKeyword();
                s = keywordnode2.getParmsAsString().substring(1);
                try
                {
                    s = WebfacingConstants.replaceSpecialCharacters(s);
                }
                catch(Throwable throwable1) { }
            }

            String s1;
            for(KeywordNodeEnumeration keywordnodeenumeration3 = recordnode.getKeywordsOfType(182); keywordnodeenumeration3.hasMoreElements(); addLineToConstructor("setSubfileCursorRRNFieldName(\"" + s1 + "\");"))
            {
                KeywordNode keywordnode3 = keywordnodeenumeration3.nextKeyword();
                s1 = keywordnode3.getParmsAsString().substring(1);
                try
                {
                    s1 = WebfacingConstants.replaceSpecialCharacters(s1);
                }
                catch(Throwable throwable2) { }
            }

            KeywordNodeEnumeration keywordnodeenumeration4 = recordnode.getKeywordsOfType(188);
            if(keywordnodeenumeration4.hasMoreElements())
            {
                KeywordNode keywordnode4 = keywordnodeenumeration4.nextKeyword();
                KeywordParm keywordparm = keywordnode4.getFirstParm();
                if(keywordparm != null)
                {
                    int i = keywordparm.getVarKwdToken();
                    if(i == 334)
                        addLineToConstructor("setSFLENDScrollBar(true);");
                }
            }
            generateCodeForFieldKeywords();
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, throwable, "error in SubfileControlFeedbackBeanGenerator.generateRecordDefinitionInitialization() while generating " + getBeanName() + " = " + throwable);
        }
    }

    protected void generateCodeForFieldKeywords()
    {
        try
        {
            for(FieldOutputEnumeration fieldoutputenumeration = getRecordLayout().getAllFields(); fieldoutputenumeration.hasMoreElements();)
            {
                FieldNode fieldnode = fieldoutputenumeration.nextFieldOutput().getFieldNode();
                if(!fieldnode.isUnnamedConstantField())
                {
                    KeywordNodeEnumeration keywordnodeenumeration = fieldnode.getKeywordsOfType(206);
                    if(keywordnodeenumeration != null && keywordnodeenumeration.hasMoreElements())
                    {
                        addLineToConstructor("setSubfileScrollFieldName(\"" + fieldnode.getWebName() + "\");");
                        addLineToConstructor("add(new KeywordDefinition(KWD_SFLSCROLL));");
                    }
                    keywordnodeenumeration = fieldnode.getKeywordsOfType(202);
                    if(keywordnodeenumeration != null && keywordnodeenumeration.hasMoreElements())
                    {
                        addLineToConstructor("setSubfileRecordNumberFieldName(\"" + fieldnode.getWebName() + "\");");
                        KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
                        addLineToConstructor("KeywordDefinition rtnrcdnbr = new KeywordDefinition(KWD_SFLRCDNBR);");
                        int i = 0;
                        for(KeywordParm keywordparm = keywordnode.getParm(i); keywordparm != null; keywordparm = keywordnode.getParm(++i))
                            if(keywordparm.getParmType() == 2)
                                if(keywordparm.getVarParmToken() == 340)
                                    addLineToConstructor("rtnrcdnbr.addParameter(\"PAR_SFLRCDNBR_TOP\");");
                                else
                                if(keywordparm.getVarParmToken() == 339)
                                    addLineToConstructor("rtnrcdnbr.addParameter(\"PAR_SFLRCDNBR_CURSOR\");");

                        addLineToConstructor("add(rtnrcdnbr);");
                    }
                }
            }

        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, throwable, "error in SubfileControlFeedbackDefinitionGenerator.generateCodeForFieldKeywords() while generating " + getBeanName() + " = " + throwable);
        }
    }

    private SubfileRecordLayout getRelatedSubfileRecordLayout()
    {
        return ((SubfileControlRecordLayout)getRecordLayout()).getSubfileRecordLayout();
    }

    private static final String _beanBaseClassPrefix = "SubfileControl";
}
