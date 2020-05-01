// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.dom.AnyNodeWithKeywords;
import com.ibm.as400ad.code400.dom.FileNode;
import com.ibm.as400ad.webfacing.convert.gen.WebResourceFileWriter;
import com.ibm.as400ad.webfacing.convert.model.RecordLayout;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.bean:
//            XMLDefinitionGenerator, XMLElement, IXMLComponent

public class XMLSFLDefinitionGenerator extends XMLDefinitionGenerator
{

    public XMLSFLDefinitionGenerator(RecordLayout recordlayout, WebResourceFileWriter webresourcefilewriter)
    {
        super(recordlayout, webresourcefilewriter);
    }

    protected IXMLComponent generateRecordElement()
    {
        XMLElement xmlelement = new XMLElement("record");
        xmlelement.addAttribute("name", getBeanName());
        xmlelement.addAttribute("type", getBeanType());
        xmlelement.addAttribute("package", getFileNode().getPackageName());
        xmlelement.add(generateDataElement());
        return xmlelement;
    }

    protected IXMLComponent generateDataElement()
    {
        IXMLComponent ixmlcomponent = super.generateDataElement();
        ixmlcomponent.add(generateViewKeywordElement(getRecordNode().getKeywordsOfType(199)));
        return ixmlcomponent;
    }

    protected void accumulateInputFieldLength(char c, int i)
    {
        super._inputFieldLengthAccumulator += i;
    }

    protected void accumulateOutputFieldLength(char c, int i, boolean flag)
    {
        if(!flag)
            super._outputFieldLengthAccumulator += i;
    }

    protected String getBeanType()
    {
        return "SFL";
    }

    static final String BEAN_TYPE = "SFL";
}
