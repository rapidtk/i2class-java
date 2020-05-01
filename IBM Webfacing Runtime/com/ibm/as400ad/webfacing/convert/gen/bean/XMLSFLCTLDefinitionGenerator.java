// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.convert.IFieldOutput;
import com.ibm.as400ad.webfacing.convert.gen.WebResourceFileWriter;
import com.ibm.as400ad.webfacing.convert.model.*;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.bean:
//            XMLDefinitionGenerator, XMLElement, SubfileResponseIndicatorList, IXMLComponent, 
//            ResponseIndicatorList

public class XMLSFLCTLDefinitionGenerator extends XMLDefinitionGenerator
{

    public XMLSFLCTLDefinitionGenerator(RecordLayout recordlayout, WebResourceFileWriter webresourcefilewriter)
    {
        super(recordlayout, webresourcefilewriter);
    }

    protected IXMLComponent generateDataElement()
    {
        IXMLComponent ixmlcomponent = super.generateDataElement();
        ixmlcomponent.add(generateDataSubfileElement());
        return ixmlcomponent;
    }

    protected IXMLComponent generateDataSubfileElement()
    {
        XMLElement xmlelement = new XMLElement("subfile");
        RecordNode recordnode = getRecordNode();
        RecordNode recordnode1 = recordnode.getRelatedSFL();
        xmlelement.addAttribute("subfilename", recordnode1.getWebName());
        xmlelement.addAttribute("pagesize", Integer.toString(getRelatedSubfileRecordLayout().getSFLPAG()));
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
                    catch(Throwable throwable) { }
                    xmlelement.addAttribute("subfilesizefieldname", s);
                } else
                {
                    short word0 = keywordparm.getVarNumber();
                    if(word0 > 0)
                        xmlelement.addAttribute("subfilesize", Integer.toString(word0));
                }
        }
        return xmlelement;
    }

    protected IXMLComponent generateViewElement()
    {
        IXMLComponent ixmlcomponent = super.generateViewElement();
        ixmlcomponent.add(generateViewSubfileElement());
        return ixmlcomponent;
    }

    protected void generateViewFirstFieldLineAttribute(XMLElement xmlelement)
    {
        SubfileControlRecordLayout subfilecontrolrecordlayout = (SubfileControlRecordLayout)getRecordLayout();
        SubfileRecordLayout subfilerecordlayout = getRelatedSubfileRecordLayout();
        int i;
        if(subfilecontrolrecordlayout.getFirstRow() != -1 && subfilecontrolrecordlayout.getFirstRow() < subfilerecordlayout.getFirstRow())
            i = subfilecontrolrecordlayout.getFirstRow();
        else
            i = subfilerecordlayout.getFirstRow();
        xmlelement.addAttribute("firstfieldline", Integer.toString(i));
    }

    protected void generateViewLastFieldLineAttribute(XMLElement xmlelement)
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
        xmlelement.addAttribute("lastfieldline", Integer.toString(j));
    }

    protected void generateViewOutputOnlyAttribute(XMLElement xmlelement)
    {
        boolean flag = true;
        for(FieldOutputEnumeration fieldoutputenumeration = getRecordLayout().getDisplayableFields(); fieldoutputenumeration.hasMoreElements();)
        {
            IFieldOutput ifieldoutput = fieldoutputenumeration.nextFieldOutput();
            if(!ifieldoutput.getFieldNode().isUnnamedConstantField() && (ifieldoutput.getFieldNode().getFieldUsage() == 'B' || ifieldoutput.getFieldNode().getFieldUsage() == 'I'))
            {
                flag = false;
                break;
            }
        }

        if(flag)
        {
            for(FieldOutputEnumeration fieldoutputenumeration1 = getRelatedSubfileRecordLayout().getDisplayableFields(); fieldoutputenumeration1.hasMoreElements();)
            {
                IFieldOutput ifieldoutput1 = fieldoutputenumeration1.nextFieldOutput();
                if(!ifieldoutput1.getFieldNode().isUnnamedConstantField() && (ifieldoutput1.getFieldNode().getFieldUsage() == 'B' || ifieldoutput1.getFieldNode().getFieldUsage() == 'I'))
                {
                    flag = false;
                    break;
                }
            }

        }
        xmlelement.addAttribute("outputonly", (new Boolean(flag)).toString());
    }

    protected IXMLComponent generateViewKeywordsElement(XMLElement xmlelement)
    {
        IXMLComponent ixmlcomponent = super.generateViewKeywordsElement(xmlelement);
        RecordNode recordnode = getRecordNode();
        ixmlcomponent.add(generateViewKeywordElement(recordnode.getKeywordsOfType(186)));
        ixmlcomponent.add(generateViewKeywordElement(recordnode.getKeywordsOfType(187)));
        ixmlcomponent.add(generateViewKeywordElement(recordnode.getKeywordsOfType(188)));
        ixmlcomponent.add(generateViewKeywordElement(recordnode.getKeywordsOfType(180)));
        ixmlcomponent.add(generateViewKeywordElement(recordnode.getKeywordsOfType(184)));
        ixmlcomponent.add(generateViewKeywordElement(recordnode.getKeywordsOfType(191)));
        for(KeywordNodeEnumeration keywordnodeenumeration = recordnode.getKeywordsOfType(195); keywordnodeenumeration.hasMoreElements(); ixmlcomponent.add(generateViewKeywordXXXMSGElement(keywordnodeenumeration.nextKeyword(), "SFL")));
        for(KeywordNodeEnumeration keywordnodeenumeration1 = recordnode.getKeywordsOfType(196); keywordnodeenumeration1.hasMoreElements(); ixmlcomponent.add(generateViewKeywordXXXMSGIDElement(keywordnodeenumeration1.nextKeyword(), "SFL")));
        for(FieldOutputEnumeration fieldoutputenumeration = getRecordLayout().getAllFields(); fieldoutputenumeration.hasMoreElements();)
        {
            FieldNode fieldnode = fieldoutputenumeration.nextFieldOutput().getFieldNode();
            if(!fieldnode.isUnnamedConstantField())
            {
                KeywordNodeEnumeration keywordnodeenumeration2 = fieldnode.getKeywordsOfType(204);
                if(keywordnodeenumeration2 != null && keywordnodeenumeration2.hasMoreElements())
                {
                    xmlelement.addAttribute("sflrolvalfieldname", fieldnode.getWebName());
                    XMLElement xmlelement1 = new XMLElement("keyword");
                    xmlelement1.addAttribute("id", "KWD_SFLROLVAL");
                    ixmlcomponent.add(xmlelement1);
                }
            }
        }

        return ixmlcomponent;
    }

    protected IXMLComponent generateViewSubfileElement()
    {
        XMLElement xmlelement = new XMLElement("subfile");
        SubfileRecordLayout subfilerecordlayout = getRelatedSubfileRecordLayout();
        xmlelement.addAttribute("areafirstrow", Integer.toString(subfilerecordlayout.getFirstRow()));
        xmlelement.addAttribute("areaheight", Integer.toString(subfilerecordlayout.getNumDisplayLines()));
        xmlelement.addAttribute("recordsperrow", Integer.toString(subfilerecordlayout.getRecordsPerRow()));
        xmlelement.addAttribute("firstcolumn", Integer.toString(subfilerecordlayout.getFirstColumn()));
        xmlelement.addAttribute("recordwidth", Integer.toString(subfilerecordlayout.getWidth()));
        if(subfilerecordlayout.isSFLLINSpecified())
            xmlelement.addAttribute("sfllin", Integer.toString(subfilerecordlayout.getSFLLIN()));
        if(subfilerecordlayout.getFieldVisDefList().size() > 0)
            xmlelement.add(generateViewFieldVisibilityDefinitionElement(getRelatedSubfileRecordLayout().getFieldVisDefList()));
        xmlelement.add(generateViewSubfileFieldsElement());
        xmlelement.add(generateViewSubfileDisplayAttributesElement());
        if(subfilerecordlayout.hasFieldSelection())
            xmlelement.add(generateViewSubfileFieldSelectionElement());
        return xmlelement;
    }

    protected IXMLComponent generateViewSubfileFieldsElement()
    {
        XMLElement xmlelement = new XMLElement("fields");
        for(FieldOutputEnumeration fieldoutputenumeration = getRelatedSubfileRecordLayout().getDisplayableFields(); fieldoutputenumeration.hasMoreElements();)
        {
            IFieldOutput ifieldoutput = fieldoutputenumeration.nextFieldOutput();
            if(!ifieldoutput.getFieldNode().isUnnamedConstantField())
                xmlelement.add(generateViewFieldElement(ifieldoutput));
        }

        return xmlelement;
    }

    protected IXMLComponent generateViewSubfileDisplayAttributesElement()
    {
        XMLElement xmlelement = new XMLElement("displayattributes");
        for(FieldOutputEnumeration fieldoutputenumeration = getRelatedSubfileRecordLayout().getDisplayableFields(); fieldoutputenumeration.hasMoreElements();)
        {
            IFieldOutput ifieldoutput = fieldoutputenumeration.nextFieldOutput();
            String s = ifieldoutput.getFieldNode().getDisplayAttributes().getPCIndExpr();
            if(s != null)
            {
                XMLElement xmlelement1 = new XMLElement("pcfieldinfo");
                xmlelement1.addAttribute("fieldname", ifieldoutput.getFieldName());
                xmlelement1.addAttribute("indicator", s);
                xmlelement.add(xmlelement1);
            }
        }

        return xmlelement;
    }

    protected IXMLComponent generateViewSubfileFieldSelectionElement()
    {
        XMLElement xmlelement = new XMLElement("fieldselection");
        int i = getRelatedSubfileRecordLayout().getMinumumHeight();
        int j = getRelatedSubfileRecordLayout().getFirstRow();
        xmlelement.addAttribute("minimumheight", Integer.toString(i));
        for(FieldOutputEnumeration fieldoutputenumeration = getRelatedSubfileRecordLayout().getDisplayableFields(); fieldoutputenumeration.hasMoreElements();)
        {
            IFieldOutput ifieldoutput = fieldoutputenumeration.nextFieldOutput();
            FieldArea fieldarea = ifieldoutput.getFieldArea();
            int k = (fieldarea.getLastRow() - j) + 1;
            if(k > i)
            {
                String s = ifieldoutput.getFieldNode().getIndicatorString();
                if(s != null)
                {
                    XMLElement xmlelement1 = new XMLElement("indicatorandrow");
                    xmlelement1.addAttribute("indicator", s);
                    xmlelement1.addAttribute("row", Integer.toString(k));
                    xmlelement.add(xmlelement1);
                }
            }
        }

        return xmlelement;
    }

    protected IXMLComponent generateFeedbackElement()
    {
        IXMLComponent ixmlcomponent = super.generateFeedbackElement();
        ixmlcomponent.add(generateFeedbackSubfileElement());
        return ixmlcomponent;
    }

    protected IXMLComponent generateFeedbackSubfileElement()
    {
        XMLElement xmlelement = new XMLElement("subfile");
        xmlelement.addAttribute("rowpersubfile", Integer.toString(getRelatedSubfileRecordLayout().getHeight()));
        xmlelement.add(generateFeedbackSubfileIndicatorsElement());
        xmlelement.add(generateFeedbackSubfileKeywordsElement());
        RecordNode recordnode = getRecordNode();
        XMLElement xmlelement1;
        for(KeywordNodeEnumeration keywordnodeenumeration = recordnode.getKeywordsOfType(190); keywordnodeenumeration.hasMoreElements(); xmlelement.add(xmlelement1))
        {
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            xmlelement1 = new XMLElement("subfilefold");
            xmlelement1.addAttribute("key", keywordnode.getParmsAsString());
        }

        XMLElement xmlelement2;
        for(KeywordNodeEnumeration keywordnodeenumeration1 = recordnode.getKeywordsOfType(185); keywordnodeenumeration1.hasMoreElements(); xmlelement.add(xmlelement2))
        {
            KeywordNode keywordnode1 = keywordnodeenumeration1.nextKeyword();
            xmlelement2 = new XMLElement("subfiledrop");
            xmlelement2.addAttribute("key", keywordnode1.getParmsAsString());
        }

        XMLElement xmlelement3;
        for(KeywordNodeEnumeration keywordnodeenumeration2 = recordnode.getKeywordsOfType(194); keywordnodeenumeration2.hasMoreElements(); xmlelement.add(xmlelement3))
        {
            KeywordNode keywordnode2 = keywordnodeenumeration2.nextKeyword();
            String s = keywordnode2.getParmsAsString().substring(1);
            try
            {
                s = WebfacingConstants.replaceSpecialCharacters(s);
            }
            catch(Throwable throwable) { }
            xmlelement3 = new XMLElement("subfilemode");
            xmlelement3.addAttribute("fieldname", s);
        }

        XMLElement xmlelement4;
        for(KeywordNodeEnumeration keywordnodeenumeration3 = recordnode.getKeywordsOfType(182); keywordnodeenumeration3.hasMoreElements(); xmlelement.add(xmlelement4))
        {
            KeywordNode keywordnode3 = keywordnodeenumeration3.nextKeyword();
            String s1 = keywordnode3.getParmsAsString().substring(1);
            try
            {
                s1 = WebfacingConstants.replaceSpecialCharacters(s1);
            }
            catch(Throwable throwable1) { }
            xmlelement4 = new XMLElement("subfilecursorrrn");
            xmlelement4.addAttribute("fieldname", s1);
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
                    xmlelement.addAttribute("sflendscrollbar", "true");
            }
        }
        for(FieldOutputEnumeration fieldoutputenumeration = getRecordLayout().getAllFields(); fieldoutputenumeration.hasMoreElements();)
        {
            FieldNode fieldnode = fieldoutputenumeration.nextFieldOutput().getFieldNode();
            if(!fieldnode.isUnnamedConstantField())
            {
                KeywordNodeEnumeration keywordnodeenumeration5 = fieldnode.getKeywordsOfType(206);
                if(keywordnodeenumeration5 != null && keywordnodeenumeration5.hasMoreElements())
                {
                    XMLElement xmlelement5 = new XMLElement("subfilescroll");
                    xmlelement5.addAttribute("fieldname", fieldnode.getWebName());
                    xmlelement.add(xmlelement5);
                }
                keywordnodeenumeration5 = fieldnode.getKeywordsOfType(202);
                if(keywordnodeenumeration5 != null && keywordnodeenumeration5.hasMoreElements())
                {
                    XMLElement xmlelement6 = new XMLElement("subfilerecordnumber");
                    xmlelement6.addAttribute("fieldname", fieldnode.getWebName());
                    xmlelement.add(xmlelement6);
                }
            }
        }

        return xmlelement;
    }

    protected IXMLComponent generateFeedbackSubfileIndicatorsElement()
    {
        XMLElement xmlelement = new XMLElement("indicators");
        RecordNode recordnode = getRecordNode().getRelatedSFL();
        SubfileResponseIndicatorList subfileresponseindicatorlist = new SubfileResponseIndicatorList();
        subfileresponseindicatorlist.populateFrom(recordnode);
        for(Iterator iterator = subfileresponseindicatorlist.getList().iterator(); iterator.hasNext(); xmlelement.add(generateFeedbackResponseIndicatorElement((ResponseIndicatorList.ResponseIndicator)iterator.next(), recordnode)));
        for(FieldNodeEnumeration fieldnodeenumeration = recordnode.getFields(); fieldnodeenumeration.hasMoreElements();)
        {
            FieldNode fieldnode = fieldnodeenumeration.nextField();
            SubfileResponseIndicatorList subfileresponseindicatorlist1 = new SubfileResponseIndicatorList();
            subfileresponseindicatorlist1.populateFrom(fieldnode);
            for(Iterator iterator1 = subfileresponseindicatorlist1.getList().iterator(); iterator1.hasNext(); xmlelement.add(generateFeedbackResponseIndicatorElement((ResponseIndicatorList.ResponseIndicator)iterator1.next(), fieldnode)));
        }

        return xmlelement;
    }

    protected IXMLComponent generateFeedbackSubfileKeywordsElement()
    {
        XMLElement xmlelement = new XMLElement("keywords");
        RecordNode recordnode = getRecordNode();
        KeywordNodeEnumeration keywordnodeenumeration = recordnode.getKeywordsOfType(190);
        if(keywordnodeenumeration != null && keywordnodeenumeration.hasMoreElements())
        {
            KeywordNode keywordnode = keywordnodeenumeration.nextKeyword();
            xmlelement.add(generateViewKeywordElement(keywordnode.getKeywordIdAsString(), keywordnode.getIndicatorString()));
        }
        KeywordNodeEnumeration keywordnodeenumeration1 = recordnode.getKeywordsOfType(185);
        if(keywordnodeenumeration1 != null && keywordnodeenumeration1.hasMoreElements())
        {
            KeywordNode keywordnode1 = keywordnodeenumeration1.nextKeyword();
            xmlelement.add(generateViewKeywordElement(keywordnode1.getKeywordIdAsString(), keywordnode1.getIndicatorString()));
        }
        for(FieldOutputEnumeration fieldoutputenumeration = getRecordLayout().getAllFields(); fieldoutputenumeration.hasMoreElements();)
        {
            FieldNode fieldnode = fieldoutputenumeration.nextFieldOutput().getFieldNode();
            if(!fieldnode.isUnnamedConstantField())
            {
                KeywordNodeEnumeration keywordnodeenumeration2 = fieldnode.getKeywordsOfType(206);
                if(keywordnodeenumeration2 != null && keywordnodeenumeration2.hasMoreElements())
                    xmlelement.add(generateViewKeywordElement("KWD_SFLSCROLL"));
                keywordnodeenumeration2 = fieldnode.getKeywordsOfType(202);
                if(keywordnodeenumeration2 != null && keywordnodeenumeration2.hasMoreElements())
                {
                    KeywordNode keywordnode2 = keywordnodeenumeration2.nextKeyword();
                    IXMLComponent ixmlcomponent = generateViewKeywordElement("KWD_SFLRCDNBR");
                    for(KeywordParmEnumeration keywordparmenumeration = keywordnode2.getParms(); keywordparmenumeration.hasMoreElements();)
                    {
                        KeywordParm keywordparm = keywordparmenumeration.nextParm();
                        if(keywordparm.getParmType() == 2)
                            if(keywordparm.getVarParmToken() == 340)
                                ixmlcomponent.add(generateViewKeywordParameterElement("PAR_SFLRCDNBR_TOP"));
                            else
                            if(keywordparm.getVarParmToken() == 339)
                                ixmlcomponent.add(generateViewKeywordParameterElement("PAR_SFLRCDNBR_CURSOR"));
                    }

                    xmlelement.add(ixmlcomponent);
                }
            }
        }

        return xmlelement;
    }

    private SubfileRecordLayout getRelatedSubfileRecordLayout()
    {
        return ((SubfileControlRecordLayout)getRecordLayout()).getSubfileRecordLayout();
    }

    protected String getBeanType()
    {
        return "SFLCTL";
    }

    static final String BEAN_TYPE = "SFLCTL";
}
