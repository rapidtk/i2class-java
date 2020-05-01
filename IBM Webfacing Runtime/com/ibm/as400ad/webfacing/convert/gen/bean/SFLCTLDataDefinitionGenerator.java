// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordParmType;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.convert.ExportHandler;
import com.ibm.as400ad.webfacing.convert.IWebResourceGenerator;
import com.ibm.as400ad.webfacing.convert.gen.WebResourceFileWriter;
import com.ibm.as400ad.webfacing.convert.model.*;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.bean:
//            DataDefinitionGenerator, DefinitionGenerator

public class SFLCTLDataDefinitionGenerator extends DataDefinitionGenerator
    implements IWebResourceGenerator, ENUM_KeywordParmType
{

    public SFLCTLDataDefinitionGenerator(RecordLayout recordlayout, WebResourceFileWriter webresourcefilewriter)
    {
        super(recordlayout, webresourcefilewriter);
    }

    protected String getBeanBaseClassPrefix()
    {
        return "SubfileControl";
    }

    private SubfileRecordLayout getRelatedSubfileRecordLayout()
    {
        return ((SubfileControlRecordLayout)getRecordLayout()).getSubfileRecordLayout();
    }

    protected void specifyNullConstructorBody()
    {
        super.specifyNullConstructorBody();
        try
        {
            RecordNode recordnode = getRecordNode();
            RecordNode recordnode1 = recordnode.getRelatedSFL();
            addLineToConstructor("setSubfileName(\"" + recordnode1.getWebName() + "\");");
            addLineToConstructor("setPageSize(" + getRelatedSubfileRecordLayout().getSFLPAG() + ");");
            KeywordNode keywordnode = recordnode.findKeywordById(207, getRecordLayout().getDisplaySizeIndex(), ((FileNode)recordnode.getParent()).getPrimaryDisplaySize());
            if(keywordnode != null)
            {
                KeywordParm keywordparm = keywordnode.getFirstParm();
                if(keywordparm != null)
                    if(keywordparm.getParmType() == 68)
                    {
                        String s = keywordparm.getVarString();
                        s = s.substring(1);
                        try
                        {
                            s = WebfacingConstants.replaceSpecialCharacters(s);
                        }
                        catch(Throwable throwable1) { }
                        addLineToConstructor("setSubfileSizeFieldName(\"" + s + "\");");
                    } else
                    {
                        short word0 = keywordparm.getVarNumber();
                        if(word0 > 0)
                            addLineToConstructor("setSubfileSize(" + word0 + ");");
                    }
            }
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, throwable, "error in SFLCTLDataDefinitionGenerator.specifyNullConstructorBody() while generating " + getBeanName() + " = " + throwable);
        }
    }

    private static final String _beanBaseClassPrefix = "SubfileControl";
}
