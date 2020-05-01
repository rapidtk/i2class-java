// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.controller;

import com.ibm.as400ad.code400.dom.constants.FieldType;
import com.ibm.as400ad.code400.dom.constants.KeywordIdentifier;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.core.ElementContainer;
import com.ibm.as400ad.webfacing.runtime.help.*;
import com.ibm.as400ad.webfacing.runtime.model.def.*;
import com.ibm.as400ad.webfacing.runtime.view.def.*;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import com.ibm.etools.iseries.webfacing.xml.*;
import java.util.ResourceBundle;
import java.util.Vector;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.controller:
//            WFSession

public class XMLDefinitionLoader
{

    public XMLDefinitionLoader()
    {
    }

    public static IRecordDataDefinition loadDataDefinition(XDocument xdocument)
    {
        Object obj = null;
        XElement xelement = xdocument.getChildElement("record");
        String s = xelement.getAttribute("name").getValue();
        String s1 = xelement.getAttribute("type").getValue();
        String s2 = xelement.getAttribute("package").getValue();
        String s3 = s2 + '.' + s;
        if(s1.equals("SFLCTL"))
            obj = new SubfileControlRecordDataDefinition(s);
        else
            obj = new RecordDataDefinition(s);
        ((IRecordDataDefinition) (obj)).setRecordClassName(s3);
        XElement xelement1 = xelement.getChildElement("data");
        if(xelement1 == null)
            return ((IRecordDataDefinition) (obj));
        XAttribute xattribute = xelement1.getAttribute("filemembertype");
        if(xattribute != null)
            ((IRecordDataDefinition) (obj)).setFileMemberType(xattribute.getValue());
        ((IRecordDataDefinition) (obj)).setVersionDigits(Long.parseLong(xelement1.getAttribute("versiondigits").getValue()));
        XElement xelement2 = xelement1.getChildElement("fields");
        if(xelement2 != null)
            loadDataFields(((IRecordDataDefinition) (obj)), xelement2);
        XElement xelement3 = xelement1.getChildElement("indicators");
        if(xelement3 != null)
            loadDataIndicators(((IRecordDataDefinition) (obj)), xelement3);
        else
            ((IRecordDataDefinition) (obj)).add(new IndicatorDataDefinition());
        XElement xelement4 = xelement1.getChildElement("buffers");
        if(xelement4 != null)
            loadDataBuffers(((IRecordDataDefinition) (obj)), xelement4);
        if(s1.equals("SFLCTL"))
        {
            XElement xelement5 = xelement1.getChildElement("subfile");
            if(xelement5 != null)
                loadDataSubfile((SubfileControlRecordDataDefinition)obj, xelement5);
        }
        if(s1.equals("SFL"))
        {
            XElement xelement6 = xelement1.getChildElement("keyword");
            if(xelement6 != null)
                ((ElementContainer)obj).add(loadViewKeyword(xelement6));
        }
        return ((IRecordDataDefinition) (obj));
    }

    private static void loadDataFields(IRecordDataDefinition irecorddatadefinition, XElement xelement)
    {
        Vector vector = xelement.getChildElementsVector("field");
        if(vector != null)
        {
            int i = vector.size();
            for(int j = 0; j < i; j++)
                irecorddatadefinition.add(loadDataField((XElement)vector.elementAt(j)));

        }
    }

    private static FieldDataDefinition loadDataField(XElement xelement)
    {
        FieldDataDefinition fielddatadefinition = null;
        String s = xelement.getAttribute("name").getValue();
        char c = xelement.getAttribute("usage").getValue().charAt(0);
        int i = Integer.parseInt(xelement.getAttribute("length").getValue());
        FieldType fieldtype = FieldType.fromString(xelement.getAttribute("type").getValue());
        XAttribute xattribute = xelement.getAttribute("shift");
        if(xattribute == null)
            fielddatadefinition = new FieldDataDefinition(s, c, i, fieldtype);
        else
            fielddatadefinition = new FieldDataDefinition(s, c, i, fieldtype, xattribute.getValue().charAt(0));
        XElement xelement1 = xelement.getChildElement("decimalprecision");
        if(xelement1 != null)
            fielddatadefinition.setDecimalPrecision(Integer.parseInt(xelement1.getAttribute("precision").getValue()));
        XElement xelement2 = xelement.getChildElement("dateformat");
        if(xelement2 != null)
            fielddatadefinition.setDatFmt(xelement2.getAttribute("format").getValue());
        XElement xelement3 = xelement.getChildElement("dateseparator");
        if(xelement3 != null)
            fielddatadefinition.setDatSep(xelement3.getAttribute("separator").getValue());
        XElement xelement4 = xelement.getChildElement("timeformat");
        if(xelement4 != null)
            fielddatadefinition.setTimFmt(xelement4.getAttribute("format").getValue());
        XElement xelement5 = xelement.getChildElement("timeseparator");
        if(xelement5 != null)
            fielddatadefinition.setTimSep(xelement5.getAttribute("separator").getValue());
        XElement xelement6 = xelement.getChildElement("defaultvalue");
        if(xelement6 != null)
        {
            XAttribute xattribute1 = xelement6.getAttribute("indicator");
            if(xattribute1 == null)
                fielddatadefinition.setDefaultValue(xelement6.getAttribute("parameter").getValue());
            else
                fielddatadefinition.setDefaultValue(xelement6.getAttribute("parameter").getValue(), xattribute1.getValue());
        }
        XElement xelement7 = xelement.getChildElement("ovrdta");
        if(xelement7 != null)
        {
            XAttribute xattribute2 = xelement7.getAttribute("indicator");
            if(xattribute2 == null)
                fielddatadefinition.setOVRDTA("");
            else
                fielddatadefinition.setOVRDTA(xattribute2.getValue());
        }
        Vector vector = xelement.getChildElementsVector("msgid");
        if(vector != null)
        {
            int j = vector.size();
            for(int k = 0; k < j; k++)
                fielddatadefinition.addMSGIDKeyword(loadDataFieldMSGID((XElement)vector.elementAt(k)));

        }
        XElement xelement8 = xelement.getChildElement("checkattributes");
        if(xelement8 != null)
            fielddatadefinition.setCheckAttr(xelement8.getAttribute("attributes").getValue());
        return fielddatadefinition;
    }

    private static MSGIDDefinition loadDataFieldMSGID(XElement xelement)
    {
        MSGIDDefinition msgiddefinition = new MSGIDDefinition();
        XAttribute xattribute = xelement.getAttribute("indicator");
        if(xattribute != null)
            msgiddefinition.setIndicatorExpression(xattribute.getValue());
        XAttribute xattribute1 = xelement.getAttribute("noneisset");
        if(xattribute1 != null && xattribute1.getValue().equals("true"))
            msgiddefinition.noneIsSet();
        XAttribute xattribute2 = xelement.getAttribute("fieldname");
        if(xattribute2 != null)
            msgiddefinition.setMsgIdFieldName(xattribute2.getValue());
        XAttribute xattribute3 = xelement.getAttribute("prefix");
        if(xattribute3 != null)
            msgiddefinition.setMsgIdPrefix(xattribute3.getValue());
        XAttribute xattribute4 = xelement.getAttribute("msgid");
        if(xattribute4 != null)
            msgiddefinition.setMsgId(xattribute4.getValue());
        XAttribute xattribute5 = xelement.getAttribute("libraryfieldname");
        if(xattribute5 != null)
            msgiddefinition.setLibraryFieldName(xattribute5.getValue());
        XAttribute xattribute6 = xelement.getAttribute("libraryname");
        if(xattribute6 != null)
            msgiddefinition.setLibraryName(xattribute6.getValue());
        XAttribute xattribute7 = xelement.getAttribute("filefieldname");
        if(xattribute7 != null)
            msgiddefinition.setFileFieldName(xattribute7.getValue());
        XAttribute xattribute8 = xelement.getAttribute("filename");
        if(xattribute8 != null)
            msgiddefinition.setFileName(xattribute8.getValue());
        return msgiddefinition;
    }

    private static void loadDataIndicators(IRecordDataDefinition irecorddatadefinition, XElement xelement)
    {
        XAttribute xattribute = xelement.getAttribute("separateindicatorarea");
        if(xattribute != null)
            irecorddatadefinition.setSeparateIndicatorArea((new Boolean(xattribute.getValue())).booleanValue());
        IndicatorDataDefinition indicatordatadefinition = new IndicatorDataDefinition();
        Vector vector = xelement.getChildElementsVector("option");
        if(vector != null)
        {
            int i = vector.size();
            for(int j = 0; j < i; j++)
                indicatordatadefinition.addReferencedOptionIndicator(Integer.parseInt(((XElement)vector.elementAt(j)).getAttributeValue("indicator")));

        }
        Vector vector1 = xelement.getChildElementsVector("response");
        if(vector1 != null)
        {
            int k = vector1.size();
            for(int l = 0; l < k; l++)
            {
                XElement xelement1 = (XElement)vector1.elementAt(l);
                indicatordatadefinition.addReferencedResponseIndicator(Integer.parseInt(xelement1.getAttributeValue("indicator")), (new Boolean(xelement1.getAttributeValue("isaidkey"))).booleanValue());
            }

        }
        irecorddatadefinition.add(indicatordatadefinition);
    }

    private static void loadDataBuffers(IRecordDataDefinition irecorddatadefinition, XElement xelement)
    {
        XAttribute xattribute = xelement.getAttribute("outputlength");
        if(xattribute != null)
            irecorddatadefinition.setOutputIOBufferLength(Integer.parseInt(xattribute.getValue()));
        XAttribute xattribute1 = xelement.getAttribute("inputlength");
        if(xattribute1 != null)
            irecorddatadefinition.setInputIOBufferLength(Integer.parseInt(xattribute1.getValue()));
    }

    private static void loadDataSubfile(SubfileControlRecordDataDefinition subfilecontrolrecorddatadefinition, XElement xelement)
    {
        subfilecontrolrecorddatadefinition.setSubfileName(xelement.getAttribute("subfilename").getValue());
        subfilecontrolrecorddatadefinition.setPageSize(Integer.parseInt(xelement.getAttribute("pagesize").getValue()));
        XAttribute xattribute = xelement.getAttribute("subfilesizefieldname");
        if(xattribute != null)
            subfilecontrolrecorddatadefinition.setSubfileSizeFieldName(xattribute.getValue());
        XAttribute xattribute1 = xelement.getAttribute("subfilesize");
        if(xattribute1 != null)
            subfilecontrolrecorddatadefinition.setSubfileSize(Integer.parseInt(xattribute1.getValue()));
    }

    public static IRecordViewDefinition loadViewDefinition(XDocument xdocument)
    {
        Object obj = null;
        XElement xelement = xdocument.getChildElement("record");
        String s = xelement.getAttribute("name").getValue();
        String s1 = xelement.getAttribute("type").getValue();
        String s2 = xelement.getAttribute("package").getValue();
        if(s1.equals("REC"))
            obj = new RecordViewDefinition(s);
        else
        if(s1.equals("SFLCTL"))
            obj = new SubfileControlRecordViewDefinition(s);
        else
            return null;
        XElement xelement1 = xelement.getChildElement("view");
        if(xelement1 == null)
            return ((IRecordViewDefinition) (obj));
        XAttribute xattribute = xelement1.getAttribute("wide");
        if(xattribute != null)
            ((IRecordViewDefinition) (obj)).setIsWide((new Boolean(xattribute.getValue())).booleanValue());
        ((IRecordViewDefinition) (obj)).setPrimaryFileDisplaySize(new Integer(xelement1.getAttribute("primarysize").getValue()));
        XAttribute xattribute1 = xelement1.getAttribute("secondarysize");
        if(xattribute1 != null)
            ((IRecordViewDefinition) (obj)).setSecondaryFileDisplaySize(new Integer(xattribute1.getValue()));
        ((IRecordViewDefinition) (obj)).setFirstFieldLine(Integer.parseInt(xelement1.getAttribute("firstfieldline").getValue()));
        ((IRecordViewDefinition) (obj)).setLastFieldLine(Integer.parseInt(xelement1.getAttribute("lastfieldline").getValue()));
        ((IRecordViewDefinition) (obj)).setIsOutputOnly((new Boolean(xelement1.getAttribute("outputonly").getValue())).booleanValue());
        XAttribute xattribute2 = xelement1.getAttribute("firstcolumn");
        if(xattribute2 != null)
            ((IRecordViewDefinition) (obj)).setFirstColumn(Integer.parseInt(xattribute2.getValue()));
        XAttribute xattribute3 = xelement1.getAttribute("lastcolumn");
        if(xattribute3 != null)
            ((IRecordViewDefinition) (obj)).setLastColumn(Integer.parseInt(xattribute3.getValue()));
        XElement xelement2 = xelement1.getChildElement("fieldvisdef");
        if(xelement2 != null)
            ((IRecordViewDefinition) (obj)).setFieldVisDef(loadViewFieldVisibilityDefinition(xelement2));
        XElement xelement3 = xelement1.getChildElement("keywords");
        if(xelement3 != null)
            loadViewKeywords(((IRecordViewDefinition) (obj)), xelement3);
        XElement xelement4 = xelement1.getChildElement("fields");
        if(xelement4 != null)
            loadViewFields(((IRecordViewDefinition) (obj)), xelement4);
        XElement xelement5 = xelement1.getChildElement("aidkeys");
        if(xelement5 != null)
            loadViewAIDKeys(((IRecordViewDefinition) (obj)), xelement5);
        XElement xelement6 = xelement1.getChildElement("commandkeylabels");
        if(xelement6 != null)
            loadViewCommandKeyLabels(((IRecordViewDefinition) (obj)), xelement6);
        XElement xelement7 = xelement1.getChildElement("window");
        if(xelement7 != null)
            loadViewWindow(((IRecordViewDefinition) (obj)), xelement7);
        XElement xelement8 = xelement1.getChildElement("displayattributes");
        if(xelement8 != null)
            loadViewDisplayAttributes(((IRecordViewDefinition) (obj)), xelement8);
        XElement xelement9 = xelement1.getChildElement("onlinehelp");
        if(xelement9 != null)
            loadViewOnlineHelp(((IRecordViewDefinition) (obj)), xelement9);
        if(s1.equals("SFLCTL"))
        {
            XElement xelement10 = xelement1.getChildElement("subfile");
            if(xelement10 != null)
                loadViewSubfile((SubfileControlRecordViewDefinition)obj, xelement10);
            XElement xelement11 = xelement1.getChildElement("keywords");
            if(xelement11 != null)
                loadViewSubfileKeywords((SubfileControlRecordViewDefinition)obj, xelement11);
            XAttribute xattribute4 = xelement1.getAttribute("sflrolvalfieldname");
            if(xattribute4 != null)
                ((SubfileControlRecordViewDefinition)obj).setSFLROLVALFieldName(xattribute4.getValue());
        }
        return ((IRecordViewDefinition) (obj));
    }

    private static String[] loadViewFieldVisibilityDefinition(XElement xelement)
    {
        String as[] = null;
        Vector vector = xelement.getChildElementsVector("visdef");
        if(vector != null)
        {
            int i = vector.size();
            as = new String[i];
            for(int j = 0; j < i; j++)
                as[j] = ((XElement)vector.elementAt(j)).getAttributeValue("string");

        }
        return as;
    }

    private static void loadViewKeywords(IRecordViewDefinition irecordviewdefinition, XElement xelement)
    {
        Vector vector = xelement.getChildElementsVector("keyword");
        if(vector != null)
        {
            int i = vector.size();
            for(int j = 0; j < i; j++)
                irecordviewdefinition.add(loadViewKeyword((XElement)vector.elementAt(j)));

        }
    }

    private static KeywordDefinition loadViewKeyword(XElement xelement)
    {
        KeywordDefinition keyworddefinition = null;
        long l = KeywordIdentifier.keywordID(xelement.getAttributeValue("id")).longValue();
        XAttribute xattribute = xelement.getAttribute("indicator");
        if(xattribute == null)
            keyworddefinition = new KeywordDefinition(l);
        else
            keyworddefinition = new KeywordDefinition(l, xattribute.getValue());
        Vector vector = xelement.getChildElementsVector("parameter");
        if(vector != null)
        {
            int i = vector.size();
            for(int j = 0; j < i; j++)
                keyworddefinition.addParameter(((XElement)vector.elementAt(j)).getAttributeValue("string"));

        }
        return keyworddefinition;
    }

    private static void loadViewFields(IRecordViewDefinition irecordviewdefinition, XElement xelement)
    {
        Vector vector = xelement.getChildElementsVector("field");
        if(vector != null)
        {
            int i = vector.size();
            for(int j = 0; j < i; j++)
                irecordviewdefinition.add(loadViewField(irecordviewdefinition, (XElement)vector.elementAt(j)));

        }
    }

    private static FieldViewDefinition loadViewField(IRecordViewDefinition irecordviewdefinition, XElement xelement)
    {
        FieldViewDefinition fieldviewdefinition = null;
        String s = xelement.getAttributeValue("name");
        int i = Integer.parseInt(xelement.getAttributeValue("row"));
        int j = Integer.parseInt(xelement.getAttributeValue("column"));
        int k = Integer.parseInt(xelement.getAttributeValue("width"));
        fieldviewdefinition = new FieldViewDefinition(s, i, j, k);
        XAttribute xattribute = xelement.getAttribute("height");
        if(xattribute != null)
            fieldviewdefinition.setHeight(Integer.parseInt(xelement.getAttribute("height").getValue()));
        XAttribute xattribute1 = xelement.getAttribute("values");
        if(xattribute1 != null)
            fieldviewdefinition.addValues(xattribute1.getValue());
        XElement xelement1 = xelement.getChildElement("editcode");
        if(xelement1 != null)
        {
            String s1 = xelement1.getAttributeValue("code");
            XAttribute xattribute2 = xelement1.getAttribute("secondcode");
            if(xattribute2 == null)
                fieldviewdefinition.addEditCode(s1.charAt(0));
            else
                fieldviewdefinition.addEditCode(s1.charAt(0), xattribute2.getValue().charAt(0));
        }
        XElement xelement2 = xelement.getChildElement("editword");
        if(xelement2 != null)
            fieldviewdefinition.addEditWord(xelement2.getAttribute("word").getValue());
        Vector vector = xelement.getChildElementsVector("keyword");
        if(vector != null)
        {
            int l = vector.size();
            for(int i1 = 0; i1 < l; i1++)
                fieldviewdefinition.add(loadViewKeyword((XElement)vector.elementAt(i1)));

        }
        Vector vector1 = xelement.getChildElementsVector("msgid");
        if(vector1 != null)
        {
            int j1 = vector1.size();
            for(int k1 = 0; k1 < j1; k1++)
                fieldviewdefinition.addERRMSGIDKeyword(loadViewMSGID((XElement)vector1.elementAt(k1)));

        }
        XElement xelement3 = xelement.getChildElement("mask");
        if(xelement3 != null)
        {
            fieldviewdefinition.setMasked(true);
            fieldviewdefinition.setStartMaskingAt(Integer.parseInt(xelement3.getAttributeValue("start")));
            fieldviewdefinition.setEndMaskingAt(Integer.parseInt(xelement3.getAttributeValue("end")));
        }
        XElement xelement4 = xelement.getChildElement("chkmsgid");
        if(xelement4 != null)
            irecordviewdefinition.add(loadViewCHKMSGID(xelement4));
        return fieldviewdefinition;
    }

    private static XXXMSGIDDefinition loadViewMSGID(XElement xelement)
    {
        XXXMSGIDDefinition xxxmsgiddefinition = new XXXMSGIDDefinition();
        XAttribute xattribute = xelement.getAttribute("indicator");
        if(xattribute != null)
            xxxmsgiddefinition.setIndicatorExpression(xattribute.getValue());
        xxxmsgiddefinition.setMsgId(xelement.getAttribute("msgid").getValue());
        xxxmsgiddefinition.setMsgFile(xelement.getAttribute("msgfile").getValue());
        XAttribute xattribute1 = xelement.getAttribute("libraryname");
        if(xattribute1 != null)
            xxxmsgiddefinition.setLibraryName(xattribute1.getValue());
        XAttribute xattribute2 = xelement.getAttribute("responseindicator");
        if(xattribute2 != null)
            xxxmsgiddefinition.setResponseIndicator(xattribute2.getValue());
        XAttribute xattribute3 = xelement.getAttribute("msgdatafieldname");
        if(xattribute3 != null)
            xxxmsgiddefinition.setMsgDataFieldName(xattribute3.getValue());
        return xxxmsgiddefinition;
    }

    private static CHKMSGIDDefinition loadViewCHKMSGID(XElement xelement)
    {
        CHKMSGIDDefinition chkmsgiddefinition = null;
        String s = xelement.getAttribute("messageid").getValue();
        String s1 = xelement.getAttribute("library").getValue();
        String s2 = xelement.getAttribute("messagefile").getValue();
        String s3 = null;
        XAttribute xattribute = xelement.getAttribute("messagedatafield");
        if(xattribute != null)
            s3 = xattribute.getValue();
        String s4 = xelement.getAttribute("fieldname").getValue();
        chkmsgiddefinition = new CHKMSGIDDefinition(s, s1, s2, s3, s4);
        return chkmsgiddefinition;
    }

    private static void loadViewAIDKeys(IRecordViewDefinition irecordviewdefinition, XElement xelement)
    {
        Vector vector = xelement.getChildElementsVector("aidkey");
        if(vector != null)
        {
            int i = vector.size();
            for(int j = 0; j < i; j++)
                irecordviewdefinition.add(loadViewAIDKey((XElement)vector.elementAt(j)));

        }
    }

    private static AIDKey loadViewAIDKey(XElement xelement)
    {
        AIDKey aidkey = null;
        String s = xelement.getAttribute("key").getValue();
        String s1 = xelement.getAttribute("label").getValue();
        XAttribute xattribute = xelement.getAttribute("tran");
        if(null != xattribute)
        {
            String s2 = null;
            try
            {
                s2 = WebfacingConstants.RUNTIME_MRI_BUNDLE.getString(s1);
            }
            catch(Throwable throwable)
            {
                s2 = null;
            }
            if(null != s2)
                s1 = s2;
        }
        String s3 = xelement.getAttribute("beanname").getValue();
        int i = Integer.parseInt(xelement.getAttribute("priority").getValue());
        XAttribute xattribute1 = xelement.getAttribute("indicator");
        XAttribute xattribute2 = xelement.getAttribute("shown");
        if(xattribute1 == null)
        {
            if(xattribute2 == null)
                aidkey = new AIDKey(s, s1, s3, i);
            else
                aidkey = new AIDKey(s, s1, s3, i, (new Boolean(xattribute2.getValue())).booleanValue());
        } else
        if(xattribute2 == null)
            aidkey = new AIDKey(s, s1, s3, i, xattribute1.getValue());
        else
            aidkey = new AIDKey(s, s1, s3, i, xattribute1.getValue(), (new Boolean(xattribute2.getValue())).booleanValue());
        return aidkey;
    }

    private static void loadViewCommandKeyLabels(IRecordViewDefinition irecordviewdefinition, XElement xelement)
    {
        Vector vector = xelement.getChildElementsVector("commandkeylabel");
        if(vector != null)
        {
            int i = vector.size();
            for(int j = 0; j < i; j++)
                irecordviewdefinition.add(loadViewCommandKeyLabel((XElement)vector.elementAt(j)));

        }
        Vector vector1 = xelement.getChildElementsVector("indicatorconditioned");
        if(vector1 != null)
        {
            int k = vector1.size();
            for(int l = 0; l < k; l++)
                irecordviewdefinition.add(loadViewCommandKeyLabelIndicatorConditioned((XElement)vector1.elementAt(l)));

        }
        Vector vector2 = xelement.getChildElementsVector("visibilityconditioned");
        if(vector2 != null)
        {
            int i1 = vector2.size();
            for(int j1 = 0; j1 < i1; j1++)
                irecordviewdefinition.add(loadViewCommandKeyLabelVisibilityConditioned((XElement)vector2.elementAt(j1)));

        }
    }

    private static CommandKeyLabel loadViewCommandKeyLabel(XElement xelement)
    {
        CommandKeyLabel commandkeylabel = null;
        String s = xelement.getAttribute("key").getValue();
        String s1 = null;
        XAttribute xattribute = xelement.getAttribute("label");
        if(xattribute != null)
            s1 = xattribute.getValue();
        String s2 = xelement.getAttribute("beanname").getValue();
        int i = Integer.parseInt(xelement.getAttribute("priority").getValue());
        String s3 = xelement.getAttributeValue("fieldid");
        commandkeylabel = new CommandKeyLabel(s, s1, s2, i, s3);
        return commandkeylabel;
    }

    private static IndicatorConditionedCommandKeyLabel loadViewCommandKeyLabelIndicatorConditioned(XElement xelement)
    {
        IndicatorConditionedCommandKeyLabel indicatorconditionedcommandkeylabel = null;
        String s = xelement.getAttribute("key").getValue();
        String s1 = null;
        XAttribute xattribute = xelement.getAttribute("label");
        if(xattribute != null)
            s1 = xattribute.getValue();
        String s2 = xelement.getAttribute("beanname").getValue();
        int i = Integer.parseInt(xelement.getAttribute("priority").getValue());
        indicatorconditionedcommandkeylabel = new IndicatorConditionedCommandKeyLabel(s, s1, s2, i);
        Vector vector = xelement.getChildElementsVector("conditionedlabel");
        if(vector != null)
        {
            int j = vector.size();
            for(int k = 0; k < j; k++)
            {
                XElement xelement1 = (XElement)vector.elementAt(k);
                String s3 = xelement1.getAttributeValue("label");
                String s4 = xelement1.getAttributeValue("indicator");
                String s5 = xelement1.getAttributeValue("isdynamic");
                String s6 = xelement1.getAttributeValue("fieldid");
                indicatorconditionedcommandkeylabel.addAConditionedLabel(new IndicatorConditionedLabel(s3, s4, s5, s6));
            }

        }
        return indicatorconditionedcommandkeylabel;
    }

    private static VisibilityConditionedCommandKeyLabel loadViewCommandKeyLabelVisibilityConditioned(XElement xelement)
    {
        VisibilityConditionedCommandKeyLabel visibilityconditionedcommandkeylabel = null;
        String s = xelement.getAttribute("key").getValue();
        String s1 = null;
        XAttribute xattribute = xelement.getAttribute("label");
        if(xattribute != null)
            s1 = xattribute.getValue();
        String s2 = xelement.getAttribute("beanname").getValue();
        int i = Integer.parseInt(xelement.getAttribute("priority").getValue());
        visibilityconditionedcommandkeylabel = new VisibilityConditionedCommandKeyLabel(s, s1, s2, i);
        Vector vector = xelement.getChildElementsVector("conditionedlabel");
        if(vector != null)
        {
            int j = vector.size();
            for(int k = 0; k < j; k++)
            {
                XElement xelement1 = (XElement)vector.elementAt(k);
                String s3 = xelement1.getAttributeValue("label");
                String s4 = xelement1.getAttributeValue("fieldid");
                String s5 = xelement1.getAttributeValue("isdynamic");
                visibilityconditionedcommandkeylabel.addAConditionedLabel(new VisibilityConditionedLabel(s3, s4, s5));
            }

        }
        return visibilityconditionedcommandkeylabel;
    }

    private static void loadViewWindow(IRecordViewDefinition irecordviewdefinition, XElement xelement)
    {
        XAttribute xattribute = xelement.getAttribute("startline");
        if(xattribute != null)
            irecordviewdefinition.setWdwStartLine(Integer.parseInt(xattribute.getValue()));
        XAttribute xattribute1 = xelement.getAttribute("startlinefield");
        if(xattribute1 != null)
            irecordviewdefinition.setWdwStartLineField(xattribute1.getValue());
        XAttribute xattribute2 = xelement.getAttribute("startpos");
        if(xattribute2 != null)
            irecordviewdefinition.setWdwStartPos(Integer.parseInt(xattribute2.getValue()));
        XAttribute xattribute3 = xelement.getAttribute("startposfield");
        if(xattribute3 != null)
            irecordviewdefinition.setWdwStartPosField(xattribute3.getValue());
        Vector vector = xelement.getChildElementsVector("windowtitle");
        if(vector != null)
        {
            int i = vector.size();
            for(int j = 0; j < i; j++)
                irecordviewdefinition.add(loadViewWindowTitle((XElement)vector.elementAt(j)));

        }
    }

    private static WindowTitleDefinition loadViewWindowTitle(XElement xelement)
    {
        WindowTitleDefinition windowtitledefinition = null;
        String s = xelement.getAttribute("indicator").getValue();
        boolean flag = (new Boolean(xelement.getAttribute("constantastitle").getValue())).booleanValue();
        String s1 = xelement.getAttribute("title").getValue();
        XAttribute xattribute = xelement.getAttribute("color");
        String s2 = xattribute != null ? xattribute.getValue() : "";
        XAttribute xattribute1 = xelement.getAttribute("dspatr");
        String s3 = xattribute1 != null ? xattribute1.getValue() : "";
        XAttribute xattribute2 = xelement.getAttribute("alignment");
        String s4 = xattribute2 != null ? xattribute2.getValue() : "";
        XAttribute xattribute3 = xelement.getAttribute("position");
        String s5 = xattribute3 != null ? xattribute3.getValue() : "";
        if(xattribute == null && xattribute1 == null && xattribute2 == null && xattribute3 == null)
            windowtitledefinition = new WindowTitleDefinition(s, flag, s1);
        else
            windowtitledefinition = new WindowTitleDefinition(s, flag, s1, s2, s3, s4, s5);
        return windowtitledefinition;
    }

    private static void loadViewDisplayAttributes(IRecordViewDefinition irecordviewdefinition, XElement xelement)
    {
        Vector vector = xelement.getChildElementsVector("pcfieldinfo");
        if(vector != null)
        {
            int i = vector.size();
            for(int j = 0; j < i; j++)
            {
                XElement xelement1 = (XElement)vector.elementAt(j);
                String s = xelement1.getAttributeValue("fieldname");
                String s1 = xelement1.getAttributeValue("indicator");
                irecordviewdefinition.add(new DSPATR_PCFieldInfo(s, s1));
            }

        }
    }

    private static void loadViewSubfile(SubfileControlRecordViewDefinition subfilecontrolrecordviewdefinition, XElement xelement)
    {
        subfilecontrolrecordviewdefinition.setSubfileAreaFirstRow(Integer.parseInt(xelement.getAttributeValue("areafirstrow")));
        subfilecontrolrecordviewdefinition.setSubfileAreaHeight(Integer.parseInt(xelement.getAttributeValue("areaheight")));
        subfilecontrolrecordviewdefinition.setSubfileRecordsPerRow(Integer.parseInt(xelement.getAttributeValue("recordsperrow")));
        subfilecontrolrecordviewdefinition.setSubfileFirstColumn(Integer.parseInt(xelement.getAttributeValue("firstcolumn")));
        subfilecontrolrecordviewdefinition.setSubfileRecordWidth(Integer.parseInt(xelement.getAttributeValue("recordwidth")));
        XAttribute xattribute = xelement.getAttribute("sfllin");
        if(xattribute != null)
            subfilecontrolrecordviewdefinition.setSFLLIN(Integer.parseInt(xattribute.getValue()));
        XElement xelement1 = xelement.getChildElement("fieldvisdef");
        if(xelement1 != null)
            subfilecontrolrecordviewdefinition.setSubfileFieldVisDef(loadViewFieldVisibilityDefinition(xelement1));
        XElement xelement2 = xelement.getChildElement("fields");
        if(xelement2 != null)
        {
            Vector vector = xelement2.getChildElementsVector("field");
            int i = vector.size();
            for(int j = 0; j < i; j++)
                subfilecontrolrecordviewdefinition.addSubfileFieldViewDefinition(loadViewField(subfilecontrolrecordviewdefinition, (XElement)vector.elementAt(j)));

        }
        XElement xelement3 = xelement.getChildElement("displayattributes");
        if(xelement3 != null)
        {
            Vector vector1 = xelement3.getChildElementsVector("pcfieldinfo");
            int k = vector1.size();
            for(int i1 = 0; i1 < k; i1++)
            {
                XElement xelement5 = (XElement)vector1.elementAt(i1);
                String s = xelement5.getAttributeValue("fieldname");
                String s1 = xelement5.getAttributeValue("indicator");
                subfilecontrolrecordviewdefinition.addForSubfile(new DSPATR_PCFieldInfo(s, s1));
            }

        }
        XElement xelement4 = xelement.getChildElement("fieldselection");
        if(xelement4 != null)
        {
            int l = Integer.parseInt(xelement4.getAttributeValue("minimumheight"));
            FieldSelectionSubfileHeightInfo fieldselectionsubfileheightinfo = new FieldSelectionSubfileHeightInfo(l);
            Vector vector2 = xelement4.getChildElementsVector("indicatorandrow");
            if(vector2 != null)
            {
                int j1 = vector2.size();
                for(int k1 = 0; k1 < j1; k1++)
                {
                    XElement xelement6 = (XElement)vector2.elementAt(k1);
                    String s2 = xelement6.getAttributeValue("indicator");
                    int l1 = Integer.parseInt(xelement6.getAttributeValue("row"));
                    fieldselectionsubfileheightinfo.addIndicatorAndRow(s2, l1);
                }

            }
            subfilecontrolrecordviewdefinition.setFieldSelectionSubfileHeightInfo(fieldselectionsubfileheightinfo);
        }
    }

    private static void loadViewSubfileKeywords(SubfileControlRecordViewDefinition subfilecontrolrecordviewdefinition, XElement xelement)
    {
        Vector vector = xelement.getChildElementsVector("msgid");
        if(vector != null)
        {
            int i = vector.size();
            for(int j = 0; j < i; j++)
                subfilecontrolrecordviewdefinition.addSFLMSGIDKeyword(loadViewMSGID((XElement)vector.elementAt(j)));

        }
    }

    private static void loadViewOnlineHelp(IRecordViewDefinition irecordviewdefinition, XElement xelement)
    {
        Vector vector = xelement.getChildElementsVector("helparea");
        if(vector != null)
        {
            int i = vector.size();
            for(int j = 0; j < i; j++)
            {
                XElement xelement1 = (XElement)vector.elementAt(j);
                HelpArea helparea = null;
                if(xelement1.getAttribute("top") != null)
                {
                    int i1 = Integer.parseInt(xelement1.getAttributeValue("top"));
                    int j1 = Integer.parseInt(xelement1.getAttributeValue("left"));
                    int k1 = Integer.parseInt(xelement1.getAttributeValue("bottom"));
                    int i2 = Integer.parseInt(xelement1.getAttributeValue("right"));
                    helparea = new HelpArea(i1, j1, k1, i2);
                } else
                {
                    helparea = new HelpArea();
                }
                XElement xelement3 = xelement1.getChildElement("field");
                if(xelement3 != null)
                {
                    String s1 = xelement3.getAttributeValue("fieldname");
                    XAttribute xattribute = xelement3.getAttribute("fieldchoice");
                    if(xattribute == null)
                        helparea.setField(s1);
                    else
                        helparea.setField(s1, Integer.parseInt(xattribute.getValue()));
                }
                XElement xelement4 = xelement1.getChildElement("definition");
                if(xelement4 != null)
                {
                    Object obj = null;
                    String s3 = xelement4.getAttribute("recordname").getValue();
                    String s4 = xelement4.getAttribute("helpspecname").getValue();
                    String s5 = null;
                    if(xelement4.getAttribute("object") != null)
                        s5 = xelement4.getAttribute("object").getValue();
                    String s6 = null;
                    if(xelement4.getAttribute("library") != null)
                        s6 = xelement4.getAttribute("library").getValue();
                    String s7 = xelement4.getAttribute("type").getValue();
                    if(s7.equals("HelpRecord"))
                    {
                        if(s5 == null && s6 == null)
                            obj = new HelpRecord(s3, s4);
                        else
                        if(s6 == null)
                            obj = new HelpRecord(s3, s4, s5);
                        else
                            obj = new HelpRecord(s3, s4, s5, s6);
                    } else
                    if(s7.equals("HelpPanelGroup"))
                        if(s5 == null && s6 == null)
                            obj = new HelpPanelGroup(s3, s4);
                        else
                        if(s6 == null)
                            obj = new HelpPanelGroup(s3, s4, s5);
                        else
                            obj = new HelpPanelGroup(s3, s4, s5, s6);
                    XAttribute xattribute3 = xelement4.getAttribute("indicator");
                    if(xattribute3 != null)
                        ((ConditionedKeyword) (obj)).setIndicatorExpression(xattribute3.getValue());
                    helparea.setHelpDefinition(((com.ibm.as400ad.webfacing.runtime.help.HelpDefinition) (obj)));
                }
                XElement xelement5 = xelement1.getChildElement("helpboundary");
                if(xelement5 != null)
                {
                    long l2 = KeywordIdentifier.keywordID(xelement5.getAttribute("id").getValue()).longValue();
                    XAttribute xattribute1 = xelement5.getAttribute("indicator");
                    if(xattribute1 == null)
                        helparea.setHelpBoundary(new HelpKeyword(l2));
                    else
                        helparea.setHelpBoundary(new HelpKeyword(l2, xattribute1.getValue()));
                }
                XElement xelement6 = xelement1.getChildElement("helpexcluded");
                if(xelement6 != null)
                {
                    long l3 = KeywordIdentifier.keywordID(xelement6.getAttribute("id").getValue()).longValue();
                    XAttribute xattribute2 = xelement6.getAttribute("indicator");
                    if(xattribute2 == null)
                        helparea.setHelpExcluded(new HelpKeyword(l3));
                    else
                        helparea.setHelpExcluded(new HelpKeyword(l3, xattribute2.getValue()));
                }
                irecordviewdefinition.add(helparea);
            }

        }
        Vector vector1 = xelement.getChildElementsVector("helpgroup");
        if(vector1 != null)
        {
            int k = vector1.size();
            for(int l = 0; l < k; l++)
            {
                XElement xelement2 = (XElement)vector1.elementAt(l);
                String s = xelement2.getAttributeValue("name");
                String s2 = xelement2.getAttributeValue("recordname");
                int l1 = Integer.parseInt(xelement2.getAttributeValue("sequence"));
                irecordviewdefinition.add(new HelpGroup(s, s2, l1));
            }

        }
    }

    public static RecordFeedbackDefinition loadFeedbackDefinition(XDocument xdocument)
    {
        Object obj = null;
        XElement xelement = xdocument.getChildElement("record");
        String s = xelement.getAttribute("name").getValue();
        String s1 = xelement.getAttribute("type").getValue();
        String s2 = xelement.getAttribute("package").getValue();
        if(s1.equals("REC"))
            obj = new RecordFeedbackDefinition(s);
        else
        if(s1.equals("SFLCTL"))
            obj = new SubfileControlRecordFeedbackDefinition(s);
        else
            return null;
        XElement xelement1 = xelement.getChildElement("feedback");
        if(xelement1 == null)
            return ((RecordFeedbackDefinition) (obj));
        XElement xelement2 = xelement1.getChildElement("indicators");
        if(xelement2 != null)
            loadFeedbackIndicators(((RecordFeedbackDefinition) (obj)), xelement2);
        XElement xelement3 = xelement1.getChildElement("rtncsrloc");
        if(xelement3 != null)
            loadFeedbackRTNCSRLOC(((RecordFeedbackDefinition) (obj)), xelement3);
        if(s1.equals("SFLCTL"))
        {
            XElement xelement4 = xelement1.getChildElement("subfile");
            if(xelement4 != null)
                loadFeedbackSubfile((SubfileControlRecordFeedbackDefinition)obj, xelement4);
        }
        return ((RecordFeedbackDefinition) (obj));
    }

    private static void loadFeedbackIndicators(RecordFeedbackDefinition recordfeedbackdefinition, XElement xelement)
    {
        Vector vector = xelement.getChildElementsVector();
        if(vector != null)
        {
            int i = vector.size();
            for(int j = 0; j < i; j++)
                recordfeedbackdefinition.add(loadFeedbackResponseIndicator((XElement)vector.elementAt(j)));

        }
    }

    private static ResponseIndicator loadFeedbackResponseIndicator(XElement xelement)
    {
        Object obj = null;
        String s = xelement.getName();
        int i = Integer.parseInt(xelement.getAttribute("indicator").getValue());
        if(s.equals("anyfieldresponse"))
            obj = new AnyFieldResponseIndicator(i);
        else
        if(s.equals("fieldresponse"))
            obj = new FieldResponseIndicator(xelement.getAttribute("name").getValue(), i);
        else
        if(s.equals("anyaidkeyresponse"))
            obj = new AnyAIDKeyResponseIndicator(i);
        else
        if(s.equals("aidkeyresponse"))
            obj = new AIDKeyResponseIndicator(xelement.getAttribute("name").getValue(), i);
        else
        if(s.equals("blanksresponse"))
            obj = new BLANKSResponseIndicator(xelement.getAttribute("name").getValue(), i);
        else
        if(s.equals("hlprtnresponse"))
        {
            XAttribute xattribute = xelement.getAttribute("indexpr");
            if(xattribute == null)
                obj = new HLPRTNResponseIndicator(i);
            else
                obj = new HLPRTNResponseIndicator(xattribute.getValue(), i);
        } else
        if(s.equals("response"))
            obj = new ResponseIndicator(i);
        return ((ResponseIndicator) (obj));
    }

    private static void loadFeedbackRTNCSRLOC(RecordFeedbackDefinition recordfeedbackdefinition, XElement xelement)
    {
        Vector vector = xelement.getChildElementsVector("window");
        if(vector != null)
        {
            int i = vector.size();
            for(int j = 0; j < i; j++)
            {
                RTNCSRLOCDefinition_WINDOW rtncsrlocdefinition_window = new RTNCSRLOCDefinition_WINDOW();
                XElement xelement1 = (XElement)vector.elementAt(j);
                rtncsrlocdefinition_window.setAbsoluteRowField(xelement1.getAttributeValue("absoluterowfield"));
                rtncsrlocdefinition_window.setAbsoluteColumnField(xelement1.getAttributeValue("absolutecolumnfield"));
                XAttribute xattribute = xelement1.getAttribute("windowrowfield");
                if(xattribute != null)
                    rtncsrlocdefinition_window.setWindowRowField(xattribute.getValue());
                XAttribute xattribute1 = xelement1.getAttribute("windowcolumnfield");
                if(xattribute1 != null)
                    rtncsrlocdefinition_window.setWindowColumnField(xattribute1.getValue());
                recordfeedbackdefinition.add(rtncsrlocdefinition_window, KeywordIdentifier.keywordID("KWD_RTNCSRLOC").toString());
            }

        }
        Vector vector1 = xelement.getChildElementsVector("recname");
        if(vector1 != null)
        {
            int k = vector1.size();
            for(int l = 0; l < k; l++)
            {
                RTNCSRLOCDefinition_RECNAME rtncsrlocdefinition_recname = new RTNCSRLOCDefinition_RECNAME();
                XElement xelement2 = (XElement)vector1.elementAt(l);
                rtncsrlocdefinition_recname.setRecordNameField(xelement2.getAttributeValue("recordnamefield"));
                rtncsrlocdefinition_recname.setFieldNameField(xelement2.getAttributeValue("fieldnamefield"));
                XAttribute xattribute2 = xelement2.getAttribute("cursorposfield");
                if(xattribute2 != null)
                    rtncsrlocdefinition_recname.setCursorPosField(xattribute2.getValue());
                recordfeedbackdefinition.add(rtncsrlocdefinition_recname, KeywordIdentifier.keywordID("KWD_RTNCSRLOC").toString());
            }

        }
    }

    private static void loadFeedbackSubfile(SubfileControlRecordFeedbackDefinition subfilecontrolrecordfeedbackdefinition, XElement xelement)
    {
        XAttribute xattribute = xelement.getAttribute("rowpersubfile");
        if(xattribute != null)
            subfilecontrolrecordfeedbackdefinition.setRowPerSubfile(Integer.parseInt(xattribute.getValue()));
        XAttribute xattribute1 = xelement.getAttribute("sflendscrollbar");
        if(xattribute1 != null)
            subfilecontrolrecordfeedbackdefinition.setSFLENDScrollBar((new Boolean(xattribute1.getValue())).booleanValue());
        XElement xelement1 = xelement.getChildElement("indicators");
        if(xelement1 != null)
        {
            Vector vector = xelement1.getChildElementsVector();
            if(vector != null)
            {
                int i = vector.size();
                for(int j = 0; j < i; j++)
                    subfilecontrolrecordfeedbackdefinition.addForSubfile(loadFeedbackResponseIndicator((XElement)vector.elementAt(j)));

            }
        }
        XElement xelement2 = xelement.getChildElement("keywords");
        if(xelement2 != null)
        {
            Vector vector1 = xelement2.getChildElementsVector("keyword");
            if(vector1 != null)
            {
                int k = vector1.size();
                for(int i1 = 0; i1 < k; i1++)
                    subfilecontrolrecordfeedbackdefinition.add(loadViewKeyword((XElement)vector1.elementAt(i1)));

            }
        }
        Vector vector2 = xelement.getChildElementsVector("subfilefold");
        if(vector2 != null)
        {
            int l = vector2.size();
            for(int j1 = 0; j1 < l; j1++)
                subfilecontrolrecordfeedbackdefinition.setSubfileFoldKey(((XElement)vector2.elementAt(j1)).getAttributeValue("key"));

        }
        Vector vector3 = xelement.getChildElementsVector("subfiledrop");
        if(vector3 != null)
        {
            int k1 = vector3.size();
            for(int l1 = 0; l1 < k1; l1++)
                subfilecontrolrecordfeedbackdefinition.setSubfileDropKey(((XElement)vector3.elementAt(l1)).getAttributeValue("key"));

        }
        Vector vector4 = xelement.getChildElementsVector("subfilemode");
        if(vector4 != null)
        {
            int i2 = vector4.size();
            for(int j2 = 0; j2 < i2; j2++)
                subfilecontrolrecordfeedbackdefinition.setSubfileModeFieldName(((XElement)vector4.elementAt(j2)).getAttributeValue("fieldname"));

        }
        Vector vector5 = xelement.getChildElementsVector("subfilecursorrrn");
        if(vector5 != null)
        {
            int k2 = vector5.size();
            for(int l2 = 0; l2 < k2; l2++)
                subfilecontrolrecordfeedbackdefinition.setSubfileCursorRRNFieldName(((XElement)vector5.elementAt(l2)).getAttributeValue("fieldname"));

        }
        Vector vector6 = xelement.getChildElementsVector("subfilescroll");
        if(vector6 != null)
        {
            int i3 = vector6.size();
            for(int j3 = 0; j3 < i3; j3++)
                subfilecontrolrecordfeedbackdefinition.setSubfileScrollFieldName(((XElement)vector6.elementAt(j3)).getAttributeValue("fieldname"));

        }
        Vector vector7 = xelement.getChildElementsVector("subfilerecordnumber");
        if(vector7 != null)
        {
            int k3 = vector7.size();
            for(int l3 = 0; l3 < k3; l3++)
                subfilecontrolrecordfeedbackdefinition.setSubfileRecordNumberFieldName(((XElement)vector7.elementAt(l3)).getAttributeValue("fieldname"));

        }
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2003, all rights reserved.");
    private static ITraceLogger traceLogger = WFSession.getTraceLogger();

}
