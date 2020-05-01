// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.webfacing.convert.*;
import com.ibm.as400ad.webfacing.convert.gen.WebResourceFileWriter;
import com.ibm.as400ad.webfacing.convert.model.*;
import java.util.ArrayList;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.bean:
//            ViewDefinitionGenerator, DefinitionGenerator, JavaSourceCodeCollection, JavaClassSourceCode

public class SFLCTLViewDefinitionGenerator extends ViewDefinitionGenerator
    implements IWebResourceGenerator
{

    public SFLCTLViewDefinitionGenerator(RecordLayout recordlayout, WebResourceFileWriter webresourcefilewriter)
    {
        super(recordlayout, webresourcefilewriter);
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
                    KeywordNodeEnumeration keywordnodeenumeration = fieldnode.getKeywordsOfType(204);
                    if(keywordnodeenumeration != null && keywordnodeenumeration.hasMoreElements())
                    {
                        addLineToConstructor("setSFLROLVALFieldName(\"" + fieldnode.getWebName() + "\");");
                        addLineToConstructor("add(new KeywordDefinition(KWD_SFLROLVAL));");
                    }
                }
            }

        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, throwable, "error in SubfileControlViewDefinitionGenerator.generateCodeForFieldKeywords() while generating " + getBeanName() + " = " + throwable);
        }
    }

    protected void generateDspatrPCInfo()
    {
        super.generateDspatrPCInfo();
        for(FieldOutputEnumeration fieldoutputenumeration = getRelatedSubfileRecordLayout().getDisplayableFields(); fieldoutputenumeration.hasMoreElements();)
        {
            IFieldOutput ifieldoutput = fieldoutputenumeration.nextFieldOutput();
            String s = ifieldoutput.getFieldNode().getDisplayAttributes().getPCIndExpr();
            if(s != null)
            {
                String s1 = ifieldoutput.getConditioning();
                if(s1 == null)
                    s1 = "";
                addLineToConstructor("addForSubfile(new DSPATR_PCFieldInfo(\"" + ifieldoutput.getFieldName() + "\", \"" + s + "\"));");
            }
        }

    }

    private void generateFieldSelectionInitialization()
    {
        int i = getRelatedSubfileRecordLayout().getMinumumHeight();
        int j = getRelatedSubfileRecordLayout().getFirstRow();
        addLineToConstructor("FieldSelectionSubfileHeightInfo subfileHeightInfo = new FieldSelectionSubfileHeightInfo(" + i + ");");
        for(FieldOutputEnumeration fieldoutputenumeration = getRelatedSubfileRecordLayout().getDisplayableFields(); fieldoutputenumeration.hasMoreElements();)
        {
            IFieldOutput ifieldoutput = fieldoutputenumeration.nextFieldOutput();
            FieldArea fieldarea = ifieldoutput.getFieldArea();
            int k = (fieldarea.getLastRow() - j) + 1;
            if(k > i)
            {
                String s = ifieldoutput.getFieldNode().getIndicatorString();
                if(s != null)
                    addLineToConstructor("subfileHeightInfo.addIndicatorAndRow(\"" + s + "\", " + k + ");");
            }
        }

        addLineToConstructor("setFieldSelectionSubfileHeightInfo(subfileHeightInfo);");
    }

    protected void generateFieldViewDefinitions()
    {
        try
        {
            super.generateFieldViewDefinitions();
            boolean flag = true;
            for(FieldOutputEnumeration fieldoutputenumeration = getRelatedSubfileRecordLayout().getDisplayableFields(); fieldoutputenumeration.hasMoreElements();)
            {
                IFieldOutput ifieldoutput = fieldoutputenumeration.nextFieldOutput();
                if(!ifieldoutput.getFieldNode().isUnnamedConstantField())
                {
                    if(flag && (ifieldoutput.getFieldNode().getFieldUsage() == 'B' || ifieldoutput.getFieldNode().getFieldUsage() == 'I'))
                        flag = false;
                    getConstructor().addAll(ifieldoutput.getViewBeanInitialization());
                    addLineToConstructor("addSubfileFieldViewDefinition(" + ifieldoutput.getQualifiedFieldName() + ");");
                }
            }

            setOutputOnly(_controlRecordHasNoInputCapableFields, flag);
            if(getRelatedSubfileRecordLayout().getFieldVisDefList().size() > 0)
                addLineToConstructor("setSubfileFieldVisDef(" + SUBFILE_FIELD_VIS_DEF_NAME + ");");
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, "error in SubfileControlViewBeanGenerator.generateSubfileFieldViewDefinitions() while generating " + getBeanName() + " = " + throwable);
        }
    }

    void generateFieldVisDef(JavaClassSourceCode javaclasssourcecode)
    {
        super.generateFieldVisDef(javaclasssourcecode);
        generateFieldVisDef(getRelatedSubfileRecordLayout().getFieldVisDefList(), SUBFILE_FIELD_VIS_DEF_NAME, javaclasssourcecode);
    }

    protected void generateSetFirstFieldLine()
    {
        SubfileControlRecordLayout subfilecontrolrecordlayout = (SubfileControlRecordLayout)getRecordLayout();
        SubfileRecordLayout subfilerecordlayout = getRelatedSubfileRecordLayout();
        int i;
        if(subfilecontrolrecordlayout.getFirstRow() != -1 && subfilecontrolrecordlayout.getFirstRow() < subfilerecordlayout.getFirstRow())
            i = subfilecontrolrecordlayout.getFirstRow();
        else
            i = subfilerecordlayout.getFirstRow();
        addLineToConstructor("setFirstFieldLine(" + i + ");");
    }

    protected void generateSetLastFieldLine()
    {
        try
        {
            SubfileControlRecordLayout subfilecontrolrecordlayout = (SubfileControlRecordLayout)getRecordLayout();
            SubfileRecordLayout subfilerecordlayout = getRelatedSubfileRecordLayout();
            SubfileInfo subfileinfo = subfilerecordlayout.subfileInfo;
            int i;
            if(subfilerecordlayout.hasFieldSelection())
                i = (subfilerecordlayout.getFirstRow() + subfileinfo.SFLPAG) - 1;
            else
            if(subfileinfo.recordsPerRow == 1)
                i = subfilerecordlayout.getLastRow() + (subfileinfo.SFLPAG - 1) * subfilerecordlayout.getHeight();
            else
                i = subfilerecordlayout.getLastRow() + (subfileinfo.totalRepeats - 1) * subfilerecordlayout.getHeight();
            int j;
            if(subfilecontrolrecordlayout.getLastRow() > i)
                j = subfilecontrolrecordlayout.getLastRow();
            else
                j = i;
            addLineToConstructor("setLastFieldLine(" + j + ");");
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, "error in SubfileControlViewBeanGenerator.generateGetLastFieldLineMethod() while generating " + getBeanName() + " = " + throwable);
        }
    }

    private void generateSubfileAreaInit()
    {
        SubfileRecordLayout subfilerecordlayout = getRelatedSubfileRecordLayout();
        addLineToConstructor("setSubfileAreaFirstRow(" + subfilerecordlayout.getFirstRow() + ");");
        addLineToConstructor("setSubfileAreaHeight(" + subfilerecordlayout.getNumDisplayLines() + ");");
        addLineToConstructor("setSubfileRecordsPerRow(" + subfilerecordlayout.getRecordsPerRow() + ");");
        addLineToConstructor("setSubfileFirstColumn(" + subfilerecordlayout.getFirstColumn() + ");");
        addLineToConstructor("setSubfileRecordWidth(" + subfilerecordlayout.getWidth() + ");");
        if(subfilerecordlayout.isSFLLINSpecified())
            addLineToConstructor("setSFLLIN(" + subfilerecordlayout.getSFLLIN() + ");");
    }

    protected void generateViewKeywords()
    {
        super.generateViewKeywords();
        try
        {
            com.ibm.as400ad.code400.dom.RecordNode recordnode = getRecordNode();
            String s = recordnode.getWebName();
            getConstructor().addKeywordWithNoParam(recordnode.getKeywordsOfType(186));
            getConstructor().addKeywordWithNoParam(recordnode.getKeywordsOfType(187));
            getConstructor().addKeywordDefinition(recordnode.getKeywordsOfType(188));
            getConstructor().addKeywordWithNoParam(recordnode.getKeywordsOfType(180));
            getConstructor().addKeywordWithNoParam(recordnode.getKeywordsOfType(184));
            getConstructor().addKeywordWithNoParam(recordnode.getKeywordsOfType(191));
            getConstructor().addCodeForXXXMSGKeyword(recordnode.getKeywordsOfType(195), "SFL", s, null);
            getConstructor().addCodeForXXXMSGIDKeyword(recordnode.getKeywordsOfType(196), "SFL", s, null);
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, "error in SubfileControlViewBeanGenerator.generateViewKeywords() while generating " + getBeanName() + " = " + throwable);
        }
    }

    protected String getBeanBaseClassPrefix()
    {
        return "SubfileControl";
    }

    private SubfileRecordLayout getRelatedSubfileRecordLayout()
    {
        return ((SubfileControlRecordLayout)getRecordLayout()).getSubfileRecordLayout();
    }

    protected void setOutputOnly(boolean flag)
    {
        _controlRecordHasNoInputCapableFields = flag;
    }

    private void setOutputOnly(boolean flag, boolean flag1)
    {
        addLineToConstructor("setIsOutputOnly(" + (flag && flag1) + ");");
    }

    protected void specifyNullConstructorBody()
    {
        super.specifyNullConstructorBody();
        generateCodeForFieldKeywords();
        generateSubfileAreaInit();
        if(getRelatedSubfileRecordLayout().hasFieldSelection())
            generateFieldSelectionInitialization();
    }

    private static final String _beanBaseClassPrefix = "SubfileControl";
    private boolean _controlRecordHasNoInputCapableFields;
    private static String SUBFILE_FIELD_VIS_DEF_NAME = "SUBFILE_FIELD_VIS_DEF";

}
