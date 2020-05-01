// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.convert.ExportHandler;
import com.ibm.as400ad.webfacing.convert.IWebResourceGenerator;
import com.ibm.as400ad.webfacing.convert.gen.WebResourceFileWriter;
import com.ibm.as400ad.webfacing.convert.model.RecordLayout;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.bean:
//            DefinitionGenerator, JavaClassSourceCode, ResponseIndicatorList

public class FeedbackDefinitionGenerator extends DefinitionGenerator
    implements IWebResourceGenerator, ENUM_KeywordIdentifiers
{

    public FeedbackDefinitionGenerator(RecordLayout recordlayout, WebResourceFileWriter webresourcefilewriter)
    {
        super(recordlayout, webresourcefilewriter);
    }

    protected String getBeanBaseClassSuffix()
    {
        return "RecordFeedbackDefinition";
    }

    protected String getBeanName()
    {
        return super.getBeanName() + "Feedback";
    }

    protected void specifyClassAttributes(JavaClassSourceCode javaclasssourcecode)
    {
        super.specifyClassAttributes(javaclasssourcecode);
        javaclasssourcecode.addImport("com.ibm.as400ad.webfacing.runtime.view.def.*");
    }

    protected void specifyNullConstructorBody()
    {
        try
        {
            com.ibm.as400ad.code400.dom.FileNode filenode = getFileNode();
            RecordNode recordnode = getRecordNode();
            getConstructor().addAll(ResponseIndicatorList.generateFeedbackResponseIndicator(filenode));
            getConstructor().addAll(ResponseIndicatorList.generateFeedbackResponseIndicator(recordnode));
            com.ibm.as400ad.code400.dom.FieldNode fieldnode;
            for(FieldNodeEnumeration fieldnodeenumeration = recordnode.getFields(); fieldnodeenumeration.hasMoreElements(); getConstructor().addAll(ResponseIndicatorList.generateFeedbackResponseIndicator(fieldnode)))
                fieldnode = fieldnodeenumeration.nextField();

            KeywordNodeEnumeration keywordnodeenumeration = recordnode.getKeywordsOfType(174);
            for(int i = 1; keywordnodeenumeration.hasMoreElements(); i++)
            {
                KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
                int j = 0;
                KeywordParm keywordparm = keywordnode.getParm(j);
                boolean flag = true;
                switch(keywordparm.getVarParmToken())
                {
                case 330: 
                    flag = false;
                    keywordparm = null;
                    break;

                case 329: 
                    addLineToConstructor("RTNCSRLOCDefinition_WINDOW rtncsrloc" + i + " = new RTNCSRLOCDefinition_WINDOW();");
                    keywordparm = keywordnode.getParm(++j);
                    String s = keywordparm.getVarString().substring(1);
                    try
                    {
                        s = WebfacingConstants.replaceSpecialCharacters(s);
                    }
                    catch(Throwable throwable1) { }
                    addLineToConstructor("rtncsrloc" + i + ".setAbsoluteRowField(\"" + s + "\");");
                    keywordparm = keywordnode.getParm(++j);
                    s = keywordparm.getVarString().substring(1);
                    try
                    {
                        s = WebfacingConstants.replaceSpecialCharacters(s);
                    }
                    catch(Throwable throwable2) { }
                    addLineToConstructor("rtncsrloc" + i + ".setAbsoluteColumnField(\"" + s + "\");");
                    keywordparm = keywordnode.getParm(++j);
                    if(keywordparm != null)
                    {
                        String s1 = keywordparm.getVarString().substring(1);
                        try
                        {
                            s1 = WebfacingConstants.replaceSpecialCharacters(s1);
                        }
                        catch(Throwable throwable3) { }
                        addLineToConstructor("rtncsrloc" + i + ".setWindowRowField(\"" + s1 + "\");");
                        keywordparm = keywordnode.getParm(++j);
                    }
                    if(keywordparm != null)
                    {
                        String s2 = keywordparm.getVarString().substring(1);
                        try
                        {
                            s2 = WebfacingConstants.replaceSpecialCharacters(s2);
                        }
                        catch(Throwable throwable4) { }
                        addLineToConstructor("rtncsrloc" + i + ".setWindowColumnField(\"" + s2 + "\");");
                        keywordparm = keywordnode.getParm(++j);
                    }
                    break;

                case 328: 
                    keywordparm = keywordnode.getParm(++j);
                    // fall through

                default:
                    addLineToConstructor("RTNCSRLOCDefinition_RECNAME rtncsrloc" + i + " = new RTNCSRLOCDefinition_RECNAME();");
                    String s3 = keywordparm.getVarString().substring(1);
                    try
                    {
                        s3 = WebfacingConstants.replaceSpecialCharacters(s3);
                    }
                    catch(Throwable throwable5) { }
                    addLineToConstructor("rtncsrloc" + i + ".setRecordNameField(\"" + s3 + "\");");
                    keywordparm = keywordnode.getParm(++j);
                    s3 = keywordparm.getVarString().substring(1);
                    try
                    {
                        s3 = WebfacingConstants.replaceSpecialCharacters(s3);
                    }
                    catch(Throwable throwable6) { }
                    addLineToConstructor("rtncsrloc" + i + ".setFieldNameField(\"" + s3 + "\");");
                    keywordparm = keywordnode.getParm(++j);
                    if(keywordparm != null)
                    {
                        String s4 = keywordparm.getVarString().substring(1);
                        try
                        {
                            s4 = WebfacingConstants.replaceSpecialCharacters(s4);
                        }
                        catch(Throwable throwable7) { }
                        addLineToConstructor("rtncsrloc" + i + ".setCursorPosField(\"" + s4 + "\");");
                        keywordparm = keywordnode.getParm(++j);
                    }
                    break;
                }
                if(flag)
                    addLineToConstructor("add(rtncsrloc" + i + ", new Long(KWD_RTNCSRLOC).toString());");
            }

        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, throwable, "error in FeedbackDefinitionGenerator.specifyNullConstructorBody() while generating " + getBeanName() + " = " + throwable);
        }
    }

    private static final String _beanDefClass = "RecordFeedbackDefinition";
}
