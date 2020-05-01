// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.dom.AnyNodeWithKeywords;
import com.ibm.as400ad.webfacing.convert.ExportHandler;
import com.ibm.as400ad.webfacing.convert.gen.WebResourceFileWriter;
import com.ibm.as400ad.webfacing.convert.model.RecordLayout;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.bean:
//            DataDefinitionGenerator, DefinitionGenerator, JavaSourceCodeCollection

public class SFLDataDefinitionGenerator extends DataDefinitionGenerator
{

    public SFLDataDefinitionGenerator(RecordLayout recordlayout, WebResourceFileWriter webresourcefilewriter)
    {
        super(recordlayout, webresourcefilewriter);
    }

    protected void accumulateInputFieldLength(char c, int i)
    {
        super._inputFieldLengthAccumulator += i;
    }

    protected void specifyNullConstructorBody()
    {
        try
        {
            super.specifyNullConstructorBody();
            com.ibm.as400ad.code400.dom.KeywordNodeEnumeration keywordnodeenumeration = getRecordNode().getKeywordsOfType(199);
            getConstructor().addKeywordWithNoParam(keywordnodeenumeration);
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, "error in SFLDataDefinitionGenerator.generateRecordDefinitionInitialization() while generating " + getBeanName() + " = " + throwable);
        }
    }

    protected void accumulateOutputFieldLength(char c, int i, boolean flag)
    {
        if(!flag)
            super._outputFieldLengthAccumulator += i;
    }
}
