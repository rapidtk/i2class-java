// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.webfacing.convert.gen.WebResourceFileWriter;
import com.ibm.as400ad.webfacing.convert.model.RecordLayout;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.bean:
//            XMLDefinitionGenerator, XMLElement, IXMLComponent

public class XMLMSGRCDDefinitionGenerator extends XMLDefinitionGenerator
{

    public XMLMSGRCDDefinitionGenerator(RecordLayout recordlayout, WebResourceFileWriter webresourcefilewriter)
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

    protected IXMLComponent generateDataFieldsElement()
    {
        XMLElement xmlelement = new XMLElement("fields");
        RecordNode recordnode = getRecordNode();
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
        xmlelement.add(generateDataFieldElement("MSGKEY", 4));
        if(flag)
        {
            xmlelement.add(generateDataFieldElement("MSGQ", 256));
            xmlelement.add(generateDataFieldElement("MSGMOD", 10));
            xmlelement.add(generateDataFieldElement("MSGBPGM", 10));
            super._outputFieldLengthAccumulator += 409L;
        } else
        {
            xmlelement.add(generateDataFieldElement("MSGQ", 10));
            super._outputFieldLengthAccumulator += 143L;
        }
        xmlelement.add(generateDataFieldElement("MSGATTR", 1));
        xmlelement.add(generateDataFieldElement("MSGDATA", 128));
        return xmlelement;
    }

    protected IXMLComponent generateDataFieldElement(String s, int i)
    {
        XMLElement xmlelement = new XMLElement("field");
        xmlelement.addAttribute("name", s);
        xmlelement.addAttribute("usage", "O");
        xmlelement.addAttribute("length", Integer.toString(i));
        xmlelement.addAttribute("type", "FT_ALPHA");
        return xmlelement;
    }

    protected String getBeanType()
    {
        return "MSGRCD";
    }

    static final String BEAN_TYPE = "MSGRCD";
}
