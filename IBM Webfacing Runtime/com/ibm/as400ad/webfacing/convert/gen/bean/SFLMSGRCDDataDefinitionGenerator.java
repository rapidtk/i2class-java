// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.webfacing.convert.ExportHandler;
import com.ibm.as400ad.webfacing.convert.gen.WebResourceFileWriter;
import com.ibm.as400ad.webfacing.convert.model.RecordLayout;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.bean:
//            SFLDataDefinitionGenerator, DefinitionGenerator, DataDefinitionGenerator, ResponseIndicatorList

public class SFLMSGRCDDataDefinitionGenerator extends SFLDataDefinitionGenerator
{

    public SFLMSGRCDDataDefinitionGenerator(RecordLayout recordlayout, WebResourceFileWriter webresourcefilewriter)
    {
        super(recordlayout, webresourcefilewriter);
    }

    private void addFieldDataDefinition(String s, String s1, int i)
    {
        addLineToConstructor("FieldDataDefinition " + s + " = new FieldDataDefinition(\"" + s1 + "\", 'O', " + i + ", FT_ALPHA);");
        addLineToConstructor("add(" + s + ");");
    }

    protected void generateFieldDataDefinitions(ResponseIndicatorList responseindicatorlist)
    {
        try
        {
            RecordNode recordnode = getRecordNode();
            int i = 1;
            boolean flag = false;
            FieldNodeEnumeration fieldnodeenumeration = recordnode.getFields();
            while(fieldnodeenumeration.hasMoreElements()) 
            {
                com.ibm.as400ad.code400.dom.FieldNode fieldnode = fieldnodeenumeration.nextField();
                KeywordNodeEnumeration keywordnodeenumeration = fieldnode.getKeywordsOfType(201);
                if(!keywordnodeenumeration.hasMoreElements())
                    continue;
                KeywordParm keywordparm = keywordnodeenumeration.nextKeyword().getFirstParm();
                if(null == keywordparm || keywordparm.getVarNumber() != 276)
                    continue;
                flag = true;
                break;
            }
            addFieldDataDefinition("field" + i++, "MSGKEY", 4);
            if(flag)
            {
                addFieldDataDefinition("field" + i++, "MSGQ", 256);
                addFieldDataDefinition("field" + i++, "MSGMOD", 10);
                addFieldDataDefinition("field" + i++, "MSGBPGM", 10);
                super._outputFieldLengthAccumulator += 409L;
            } else
            {
                addFieldDataDefinition("field" + i++, "MSGQ", 10);
                super._outputFieldLengthAccumulator += 143L;
            }
            addFieldDataDefinition("field" + i++, "MSGATTR", 1);
            addFieldDataDefinition("field" + i++, "MSGDATA", 128);
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, throwable, "error in SFLMSGRCDDataDefinitionGenerator.generateFieldDataDefinitions() while generating " + getBeanName() + " = " + throwable);
        }
    }
}
