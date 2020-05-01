// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.controller;

import com.ibm.as400ad.code400.dom.constants.FieldType;
import com.ibm.as400ad.code400.dom.constants.KeywordIdentifier;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.core.IElementContainer;
import com.ibm.as400ad.webfacing.runtime.help.*;
import com.ibm.as400ad.webfacing.runtime.model.def.*;
import com.ibm.as400ad.webfacing.runtime.view.def.*;
import java.io.PrintStream;
import java.util.ResourceBundle;
import java.util.Vector;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.controller:
//            ICacheable

public class DataDefinitionBeanHandler extends DefaultHandler
{

    public DataDefinitionBeanHandler()
    {
        locator = null;
        rdd = null;
        rvd = null;
        srvd = null;
        rfd = null;
        srfd = null;
        indata = false;
        indataind = false;
        indatafield = false;
        indicators = null;
        fdd = null;
        inview = false;
        inviewfvis = false;
        inviewflds = false;
        inviewccl = false;
        inviewhlpa = false;
        fvd = null;
        ckl = null;
        icckl = null;
        fvisstr = null;
        vcckl = null;
        inviewkwds = false;
        inviewkwdsv = null;
        vkd = null;
        inviewsfl = false;
        fsshi = null;
        infeedback = false;
        infbinds = false;
        infbsfl = false;
        infbrcl = false;
        recordName = "unknown";
        recordType = "unknown";
        recordPackage = "unknown";
        recordClassName = "unknown";
        ha = null;
        hd = null;
    }

    public IRecordDataDefinition getDataDefinition()
    {
        IRecordDataDefinition irecorddatadefinition = rdd;
        rdd.setFeedbackDefinition(rfd);
        rdd.setViewDefinition(rvd);
        if(null == rdd.getIndicatorDefinition())
            rdd.add(new IndicatorDataDefinition());
        return irecorddatadefinition;
    }

    public void setDocumentLocator(Locator locator1)
    {
        locator = locator1;
    }

    public void startElement(String s, String s1, String s2, Attributes attributes)
        throws SAXException
    {
        if(indata)
            startDataElement(s, s1, s2, attributes);
        else
        if(inview)
            startViewElement(s, s1, s2, attributes);
        else
        if(infeedback)
            startFeedbackElement(s, s1, s2, attributes);
        else
        if(s1.equals("data"))
        {
            indata = true;
            startElement(s, s1, s2, attributes);
        } else
        if(s1.equals("view"))
        {
            inview = true;
            if(recordType.equals("REC"))
                rvd = new RecordViewDefinition(recordName);
            else
            if(recordType.equals("SFLCTL"))
            {
                srvd = new SubfileControlRecordViewDefinition(recordName);
                rvd = srvd;
            }
            String s3 = attributes.getValue("wide");
            if(s3 != null && s3.length() > 0)
                rvd.setIsWide((new Boolean(s3)).booleanValue());
            rvd.setPrimaryFileDisplaySize(new Integer(attributes.getValue("primarysize")));
            String s4 = attributes.getValue("secondarysize");
            if(s4 != null && s4.length() > 0)
                rvd.setSecondaryFileDisplaySize(new Integer(s4));
            String s5 = attributes.getValue("sflrolvalfieldname");
            if(s5 != null)
                ((SubfileControlRecordViewDefinition)rvd).setSFLROLVALFieldName(s5);
            rvd.setFirstFieldLine(Integer.parseInt(attributes.getValue("firstfieldline")));
            rvd.setLastFieldLine(Integer.parseInt(attributes.getValue("lastfieldline")));
            rvd.setIsOutputOnly((new Boolean(attributes.getValue("outputonly"))).booleanValue());
            String s6 = attributes.getValue("firstcolumn");
            if(s6 != null && s6.length() > 0)
                rvd.setFirstColumn(Integer.parseInt(s6));
            String s7 = attributes.getValue("lastcolumn");
            if(s7 != null && s7.length() > 0)
                rvd.setLastColumn(Integer.parseInt(s7));
        } else
        if(s1.equals("feedback"))
        {
            infeedback = true;
            startElement(s, s1, s2, attributes);
        } else
        if(s1.equals("record"))
        {
            recordName = attributes.getValue("name");
            recordType = attributes.getValue("type");
            recordPackage = attributes.getValue("package");
            recordClassName = recordPackage + '.' + recordName;
            if(recordType.equals("SFLCTL"))
                rdd = new SubfileControlRecordDataDefinition(recordName);
            else
                rdd = new RecordDataDefinition(recordName);
            if(null != rdd)
                rdd.setRecordClassName(recordClassName);
        } else
        {
            System.out.println("!!!! We have a problem here houston");
            System.out.println(">>>? MyContentHandler.startElement(" + s + ", " + s1 + ", " + s2 + ", " + attributes + ")");
            showAtts(attributes);
            throw new SAXParseException("Unknown section :" + s1, locator);
        }
    }

    public void endElement(String s, String s1, String s2)
        throws SAXException
    {
        if(indata)
            endDataElement(s, s1, s2);
        else
        if(inview)
            endViewElement(s, s1, s2);
        else
        if(infeedback)
            endFeedbackElement(s, s1, s2);
        else
        if(!s1.equals("record"))
            System.out.println("<<< MyContentHandler.endElement(" + s + ", " + s1 + ", " + s2 + ")");
    }

    public void startDataElement(String s, String s1, String s2, Attributes attributes)
        throws SAXException
    {
        if(s1.equals("field"))
        {
            indatafield = true;
            String s3 = attributes.getValue("name");
            char c = attributes.getValue("usage").charAt(0);
            int i = Integer.parseInt(attributes.getValue("length"));
            FieldType fieldtype = FieldType.fromString(attributes.getValue("type"));
            String s13 = attributes.getValue("shift");
            if(null == s13)
                fdd = new FieldDataDefinition(s3, c, i, fieldtype);
            else
                fdd = new FieldDataDefinition(s3, c, i, fieldtype, s13.charAt(0));
        } else
        if(indatafield)
        {
            if(s1.equals("decimalprecision"))
                fdd.setDecimalPrecision(Integer.parseInt(attributes.getValue("precision")));
            else
            if(s1.equals("dateformat"))
                fdd.setDatFmt(attributes.getValue("format"));
            else
            if(s1.equals("dateseparator"))
                fdd.setDatSep(attributes.getValue("separator"));
            else
            if(s1.equals("timeformat"))
                fdd.setTimFmt(attributes.getValue("format"));
            else
            if(s1.equals("timeseparator"))
                fdd.setTimSep(attributes.getValue("separator"));
            else
            if(s1.equals("defaultvalue"))
            {
                String s4 = attributes.getValue("indicator");
                if(null == s4)
                    fdd.setDefaultValue(attributes.getValue("parameter"));
                else
                    fdd.setDefaultValue(attributes.getValue("parameter"), s4);
            } else
            if(s1.equals("ovrdta"))
            {
                String s5 = attributes.getValue("indicator");
                if(s5 == null)
                    fdd.setOVRDTA("");
                else
                    fdd.setOVRDTA(s5);
            } else
            if(s1.equals("msgid"))
                fdd.addMSGIDKeyword(loadDataFieldMSGID(attributes));
            else
            if(s1.equals("checkattributes"))
                fdd.setCheckAttr(attributes.getValue("attributes"));
            else
                throw new SAXParseException("Unknown data indicator type to create view definition", locator);
        } else
        if(indataind)
            handleDataIndicators(s, s1, s2, attributes);
        else
        if(s1.equals("indicators"))
        {
            indataind = true;
            String s6 = attributes.getValue("separateindicatorarea");
            if(s6 != null && s6.length() > 0)
                rdd.setSeparateIndicatorArea((new Boolean(s6)).booleanValue());
            indicators = new IndicatorDataDefinition();
        } else
        if(s1.equals("keyword"))
        {
            KeywordDefinition keyworddefinition = null;
            long l = KeywordIdentifier.keywordID(attributes.getValue("id")).longValue();
            String s12 = attributes.getValue("indicator");
            if(s12 == null)
                keyworddefinition = new KeywordDefinition(l);
            else
                keyworddefinition = new KeywordDefinition(l, s12);
            rdd.add(keyworddefinition);
        } else
        if(!s1.equals("fields"))
            if(s1.equals("subfile"))
            {
                if(recordType.equals("SFLCTL"))
                {
                    ((SubfileControlRecordDataDefinition)rdd).setSubfileName(attributes.getValue("subfilename"));
                    ((SubfileControlRecordDataDefinition)rdd).setPageSize(Integer.parseInt(attributes.getValue("pagesize")));
                    String s7 = attributes.getValue("subfilesizefieldname");
                    if(s7 != null)
                        ((SubfileControlRecordDataDefinition)rdd).setSubfileSizeFieldName(s7);
                    String s10 = attributes.getValue("subfilesize");
                    if(s10 != null)
                        ((SubfileControlRecordDataDefinition)rdd).setSubfileSize(Integer.parseInt(s10));
                } else
                {
                    throw new SAXParseException("SUBFILE section in non-SFLCTL record.", locator);
                }
            } else
            if(s1.equals("buffers"))
            {
                String s8 = attributes.getValue("outputlength");
                if(null != s8 && s8.length() > 0)
                    rdd.setOutputIOBufferLength(Integer.parseInt(s8));
                String s11 = attributes.getValue("inputlength");
                if(null != s11 && s11.length() > 0)
                    rdd.setInputIOBufferLength(Integer.parseInt(s11));
            } else
            if(s1.equals("data"))
            {
                rdd.setVersionDigits(Long.parseLong(attributes.getValue("versiondigits")));
                String s9 = attributes.getValue("filemembertype");
                if(null != s9)
                    rdd.setFileMemberType(s9);
            } else
            {
                System.out.println(">>> MyContentHandler.startDataElement(" + s + ", " + s1 + ", " + s2 + ", " + attributes + ")");
                showAtts(attributes);
            }
    }

    public void endDataElement(String s, String s1, String s2)
        throws SAXException
    {
        if(s1.equals("field"))
        {
            indatafield = false;
            rdd.add(fdd);
            fdd = null;
        } else
        if(!indatafield && !s1.equals("buffers") && !s1.equals("response") && !s1.equals("subfile") && !s1.equals("fields") && !s1.equals("option"))
            if(indataind && s1.equals("indicators"))
            {
                indataind = false;
                rdd.add(indicators);
                indicators = null;
            } else
            if(s1.equals("data"))
                indata = false;
            else
            if(!s1.equals("keyword"))
                System.out.println("<<< MyContentHandler.endDataElement(" + s + ", " + s1 + ", " + s2 + ")");
    }

    private static XXXMSGIDDefinition loadViewMSGID(Attributes attributes)
    {
        XXXMSGIDDefinition xxxmsgiddefinition = new XXXMSGIDDefinition();
        String s = attributes.getValue("indicator");
        if(s != null)
            xxxmsgiddefinition.setIndicatorExpression(s);
        xxxmsgiddefinition.setMsgId(attributes.getValue("msgid"));
        xxxmsgiddefinition.setMsgFile(attributes.getValue("msgfile"));
        String s1 = attributes.getValue("libraryname");
        if(s1 != null)
            xxxmsgiddefinition.setLibraryName(s1);
        String s2 = attributes.getValue("responseindicator");
        if(s2 != null)
            xxxmsgiddefinition.setResponseIndicator(s2);
        String s3 = attributes.getValue("msgdatafieldname");
        if(s3 != null)
            xxxmsgiddefinition.setMsgDataFieldName(s3);
        return xxxmsgiddefinition;
    }

    private static MSGIDDefinition loadDataFieldMSGID(Attributes attributes)
    {
        MSGIDDefinition msgiddefinition = new MSGIDDefinition();
        String s = attributes.getValue("indicator");
        if(s != null)
            msgiddefinition.setIndicatorExpression(s);
        String s1 = attributes.getValue("noneisset");
        if(s1 != null && s1.equals("true"))
            msgiddefinition.noneIsSet();
        String s2 = attributes.getValue("fieldname");
        if(s2 != null)
            msgiddefinition.setMsgIdFieldName(s2);
        String s3 = attributes.getValue("prefix");
        if(s3 != null)
            msgiddefinition.setMsgIdPrefix(s3);
        String s4 = attributes.getValue("msgid");
        if(s4 != null)
            msgiddefinition.setMsgId(s4);
        String s5 = attributes.getValue("libraryfieldname");
        if(s5 != null)
            msgiddefinition.setLibraryFieldName(s5);
        String s6 = attributes.getValue("libraryname");
        if(s6 != null)
            msgiddefinition.setLibraryName(s6);
        String s7 = attributes.getValue("filefieldname");
        if(s7 != null)
            msgiddefinition.setFileFieldName(s7);
        String s8 = attributes.getValue("filename");
        if(s8 != null)
            msgiddefinition.setFileName(s8);
        return msgiddefinition;
    }

    public void handleDataIndicators(String s, String s1, String s2, Attributes attributes)
        throws SAXException
    {
        if(s1.equals("option"))
            indicators.addReferencedOptionIndicator(Integer.parseInt(attributes.getValue("indicator")));
        else
        if(s1.equals("response"))
            indicators.addReferencedResponseIndicator(Integer.parseInt(attributes.getValue("indicator")), (new Boolean(attributes.getValue("isaidkey"))).booleanValue());
        else
            throw new SAXParseException("Unknown data indicator type to create view definition", locator);
    }

    public void startViewElement(String s, String s1, String s2, Attributes attributes)
        throws SAXException
    {
        if(inviewhlpa)
            handleViewOnlineHelp(s, s1, s2, attributes);
        else
        if(inviewccl)
            handleViewCommandKeyLabels(s, s1, s2, attributes);
        else
        if(inviewflds)
            handleViewFields(s, s1, s2, attributes);
        else
        if(inviewfvis)
        {
            String s3 = attributes.getValue("string");
            fvisstr.add(s3);
        } else
        if(inviewsfl)
        {
            if(s1.equals("fieldvisdef"))
            {
                inviewfvis = true;
                fvisstr = new Vector(20, 20);
            } else
            if(s1.equals("fieldselection"))
            {
                int i = Integer.parseInt(attributes.getValue("minimumheight"));
                fsshi = new FieldSelectionSubfileHeightInfo(i);
            } else
            if(s1.equals("indicatorandrow"))
            {
                String s4 = attributes.getValue("indicator");
                int j = Integer.parseInt(attributes.getValue("row"));
                fsshi.addIndicatorAndRow(s4, j);
            } else
            if(s1.equals("pcfieldinfo"))
            {
                String s5 = attributes.getValue("fieldname");
                String s8 = attributes.getValue("indicator");
                srvd.addForSubfile(new DSPATR_PCFieldInfo(s5, s8));
            } else
            if(s1.equals("fields"))
                inviewflds = true;
            else
            if(!s1.equals("displayattributes"))
                throw new SAXParseException("Unknown element '" + s1 + "' in view subfile", locator);
        } else
        if(inviewkwds)
        {
            if(s1.equals("keyword"))
            {
                vkd = null;
                long l = KeywordIdentifier.keywordID(attributes.getValue("id")).longValue();
                String s10 = attributes.getValue("indicator");
                if(s10 == null)
                    vkd = new KeywordDefinition(l);
                else
                    vkd = new KeywordDefinition(l, s10);
            } else
            if(s1.equals("msgid"))
                srvd.addSFLMSGIDKeyword(loadViewMSGID(attributes));
            else
            if(s1.equals("parameter"))
                vkd.addParameter(attributes.getValue("string"));
            else
                throw new SAXParseException("Unknown element '" + s1 + "' in view keyword", locator);
        } else
        if(!s1.equals("displayattributes"))
            if(s1.equals("window"))
                handleViewWindow(s, s1, s2, attributes);
            else
            if(s1.equals("windowtitle"))
                handleViewWindowTitles(s, s1, s2, attributes);
            else
            if(s1.equals("pcfieldinfo"))
            {
                String s6 = attributes.getValue("fieldname");
                String s9 = attributes.getValue("indicator");
                rvd.add(new DSPATR_PCFieldInfo(s6, s9));
            } else
            if(s1.equals("onlinehelp"))
                inviewhlpa = true;
            else
            if(s1.equals("fields"))
                inviewflds = true;
            else
            if(s1.equals("aidkey"))
                rvd.add(loadViewAIDKey(attributes));
            else
            if(s1.equals("commandkeylabels"))
                inviewccl = true;
            else
            if(s1.equals("subfile"))
            {
                inviewsfl = true;
                srvd.setSubfileAreaFirstRow(Integer.parseInt(attributes.getValue("areafirstrow")));
                srvd.setSubfileAreaHeight(Integer.parseInt(attributes.getValue("areaheight")));
                srvd.setSubfileRecordsPerRow(Integer.parseInt(attributes.getValue("recordsperrow")));
                srvd.setSubfileFirstColumn(Integer.parseInt(attributes.getValue("firstcolumn")));
                srvd.setSubfileRecordWidth(Integer.parseInt(attributes.getValue("recordwidth")));
                String s7 = attributes.getValue("sfllin");
                if(s7 != null)
                    srvd.setSFLLIN(Integer.parseInt(s7));
            } else
            if(!s1.equals("aidkeys"))
                if(s1.equals("fieldvisdef"))
                {
                    inviewfvis = true;
                    fvisstr = new Vector(20, 20);
                } else
                if(s1.equals("keywords"))
                {
                    inviewkwds = true;
                    inviewkwdsv = new Vector(20, 20);
                } else
                if(s1.equals("view"))
                {
                    if(recordType.equals("REC"))
                    {
                        rvd = new RecordViewDefinition(recordName);
                        srvd = null;
                        rdd.setViewDefinition(rvd);
                    } else
                    if(recordType.equals("SFLCTL"))
                    {
                        srvd = new SubfileControlRecordViewDefinition(recordName);
                        rvd = srvd;
                        rdd.setViewDefinition(rvd);
                    } else
                    {
                        throw new SAXParseException("Unknown record type to create view definition", locator);
                    }
                } else
                {
                    System.out.println(">>> MyContentHandler.startViewElement(" + s + ", " + s1 + ", " + s2 + ", " + attributes + ")");
                    showAtts(attributes);
                }
    }

    public void endViewElement(String s, String s1, String s2)
        throws SAXException
    {
        if(inviewhlpa)
        {
            if(s1.equals("onlinehelp"))
                inviewhlpa = false;
            else
            if(s1.equals("helparea") && null != ha)
            {
                rvd.add(ha);
                ha = null;
            }
        } else
        if(inviewflds)
        {
            if(s1.equals("fields"))
                inviewflds = false;
            else
            if(s1.equals("keyword"))
            {
                fvd.add(vkd);
                vkd = null;
            } else
            if(s1.equals("field"))
            {
                if(!inviewsfl)
                    rvd.add(fvd);
                else
                    srvd.addSubfileFieldViewDefinition(fvd);
                fvd = null;
            }
        } else
        if(inviewccl)
        {
            if(s1.equals("commandkeylabel"))
            {
                rvd.add(ckl);
                ckl = null;
            } else
            if(s1.equals("visibilityconditioned"))
            {
                rvd.add(vcckl);
                vcckl = null;
            } else
            if(s1.equals("indicatorconditioned"))
            {
                rvd.add(icckl);
                icckl = null;
            } else
            if(s1.equals("commandkeylabels"))
                inviewccl = false;
        } else
        if(inviewsfl)
        {
            if(s1.equals("fieldvisdef"))
            {
                inviewfvis = false;
                int i = fvisstr.size();
                String as[] = new String[i];
                for(int k = 0; k < i; k++)
                    as[k] = (String)fvisstr.elementAt(k);

                fvisstr = null;
                srvd.setSubfileFieldVisDef(as);
                as = null;
            } else
            if(s1.equals("fieldselection"))
            {
                srvd.setFieldSelectionSubfileHeightInfo(fsshi);
                fsshi = null;
            } else
            if(s1.equals("subfile"))
                inviewsfl = false;
        } else
        if(inviewkwds)
        {
            if(s1.equals("keyword"))
            {
                rvd.add(vkd);
                vkd = null;
            } else
            if(s1.equals("keywords"))
                inviewkwds = false;
        } else
        if(!s1.equals("aidkey") && !s1.equals("visdef") && !s1.equals("aidkeys"))
            if(s1.equals("keywords"))
                inviewkwds = false;
            else
            if(s1.equals("fieldvisdef"))
            {
                inviewfvis = false;
                int j = fvisstr.size();
                String as1[] = new String[j];
                for(int l = 0; l < j; l++)
                    as1[l] = (String)fvisstr.elementAt(l);

                fvisstr = null;
                rvd.setFieldVisDef(as1);
                as1 = null;
            } else
            if(s1.equals("view"))
                inview = false;
            else
            if(!s1.equals("displayattributes") && !s1.equals("pcfieldinfo") && !s1.equals("window") && !s1.equals("windowtitle"))
                System.out.println("<<< MyContentHandler.endViewElement(" + s + ", " + s1 + ", " + s2 + ")");
    }

    private AIDKey loadViewAIDKey(Attributes attributes)
    {
        AIDKey aidkey = null;
        String s = attributes.getValue("key");
        String s1 = attributes.getValue("label");
        String s2 = attributes.getValue("tran");
        if(null != s2)
        {
            String s3 = null;
            try
            {
                s3 = WebfacingConstants.RUNTIME_MRI_BUNDLE.getString(s1);
            }
            catch(Throwable throwable)
            {
                s3 = null;
            }
            if(null != s3)
                s1 = s3;
        }
        String s4 = attributes.getValue("beanname");
        int i = Integer.parseInt(attributes.getValue("priority"));
        String s5 = attributes.getValue("indicator");
        String s6 = attributes.getValue("shown");
        if(s5 == null)
        {
            if(s6 == null)
                aidkey = new AIDKey(s, s1, s4, i);
            else
                aidkey = new AIDKey(s, s1, s4, i, (new Boolean(s6)).booleanValue());
        } else
        if(s6 == null)
            aidkey = new AIDKey(s, s1, s4, i, s5);
        else
            aidkey = new AIDKey(s, s1, s4, i, s5, (new Boolean(s6)).booleanValue());
        return aidkey;
    }

    public void startFeedbackElement(String s, String s1, String s2, Attributes attributes)
        throws SAXException
    {
        if(s1.equals("indicators"))
            infbinds = true;
        else
        if(s1.equals("feedback"))
        {
            if(recordType.equals("REC"))
            {
                rfd = new RecordFeedbackDefinition(recordName);
                rdd.setFeedbackDefinition(rfd);
            } else
            if(recordType.equals("SFLCTL"))
            {
                srfd = new SubfileControlRecordFeedbackDefinition(recordName);
                rfd = srfd;
                rdd.setFeedbackDefinition(rfd);
            } else
            {
                throw new SAXParseException("Unknown record type to create feedback definition", locator);
            }
        } else
        if(infbsfl)
            handleFeedbackSubfile(s, s1, s2, attributes);
        else
        if(infbinds)
        {
            ResponseIndicator responseindicator = loadFeedbackResponseIndicator(s1, attributes);
            if(null != responseindicator)
                rfd.add(responseindicator);
        } else
        if(s1.equals("subfile"))
        {
            infbsfl = true;
            String s3 = attributes.getValue("rowpersubfile");
            if(s3 != null)
                srfd.setRowPerSubfile(Integer.parseInt(s3));
            String s4 = attributes.getValue("sflendscrollbar");
            if(s4 != null)
                srfd.setSFLENDScrollBar((new Boolean(s4)).booleanValue());
        } else
        if(s1.equals("rtncsrloc"))
            infbrcl = true;
        else
        if(infbrcl)
        {
            if(s1.equals("window"))
            {
                RTNCSRLOCDefinition_WINDOW rtncsrlocdefinition_window = new RTNCSRLOCDefinition_WINDOW();
                rtncsrlocdefinition_window.setAbsoluteRowField(attributes.getValue("absoluterowfield"));
                rtncsrlocdefinition_window.setAbsoluteColumnField(attributes.getValue("absolutecolumnfield"));
                String s5 = attributes.getValue("windowrowfield");
                if(s5 != null)
                    rtncsrlocdefinition_window.setWindowRowField(s5);
                String s7 = attributes.getValue("windowcolumnfield");
                if(s7 != null)
                    rtncsrlocdefinition_window.setWindowColumnField(s7);
                rfd.add(rtncsrlocdefinition_window, KeywordIdentifier.keywordID("KWD_RTNCSRLOC").toString());
            } else
            if(s1.equals("recname"))
            {
                RTNCSRLOCDefinition_RECNAME rtncsrlocdefinition_recname = new RTNCSRLOCDefinition_RECNAME();
                rtncsrlocdefinition_recname.setRecordNameField(attributes.getValue("recordnamefield"));
                rtncsrlocdefinition_recname.setFieldNameField(attributes.getValue("fieldnamefield"));
                String s6 = attributes.getValue("cursorposfield");
                if(s6 != null)
                    rtncsrlocdefinition_recname.setCursorPosField(s6);
                rfd.add(rtncsrlocdefinition_recname, KeywordIdentifier.keywordID("KWD_RTNCSRLOC").toString());
            }
        } else
        {
            System.out.println(">>> MyContentHandler.startFeedbackElement(" + s + ", " + s1 + ", " + s2 + ", " + attributes + ")");
            showAtts(attributes);
        }
    }

    public void endFeedbackElement(String s, String s1, String s2)
        throws SAXException
    {
        if(!s1.equals("anyaidkeyresponse") && !s1.equals("blanksresponse") && !s1.equals("response") && !s1.equals("hlprtnresponse") && !s1.equals("fieldresponse") && !s1.equals("anyfieldresponse") && !s1.equals("aidkeyresponse"))
            if(s1.equals("indicators"))
                infbinds = false;
            else
            if(s1.equals("feedback"))
                infeedback = false;
            else
            if(infbsfl)
            {
                if(s1.equals("subfile"))
                    infbsfl = false;
                else
                if(s1.equals("keyword"))
                {
                    srfd.add(vkd);
                    vkd = null;
                }
            } else
            if(infbrcl)
            {
                if(s1.equals("rtncsrloc"))
                    infbrcl = false;
            } else
            {
                System.out.println("<<< MyContentHandler.endFeedbackElement(" + s + ", " + s1 + ", " + s2 + ")");
            }
    }

    private static ResponseIndicator loadFeedbackResponseIndicator(String s, Attributes attributes)
    {
        Object obj = null;
        int i = Integer.parseInt(attributes.getValue("indicator"));
        if(s.equals("anyfieldresponse"))
            obj = new AnyFieldResponseIndicator(i);
        else
        if(s.equals("fieldresponse"))
            obj = new FieldResponseIndicator(attributes.getValue("name"), i);
        else
        if(s.equals("anyaidkeyresponse"))
            obj = new AnyAIDKeyResponseIndicator(i);
        else
        if(s.equals("aidkeyresponse"))
            obj = new AIDKeyResponseIndicator(attributes.getValue("name"), i);
        else
        if(s.equals("blanksresponse"))
            obj = new BLANKSResponseIndicator(attributes.getValue("name"), i);
        else
        if(s.equals("hlprtnresponse"))
        {
            String s1 = attributes.getValue("indexpr");
            if(s1 == null)
                obj = new HLPRTNResponseIndicator(i);
            else
                obj = new HLPRTNResponseIndicator(s1, i);
        } else
        if(s.equals("response"))
            obj = new ResponseIndicator(i);
        return ((ResponseIndicator) (obj));
    }

    private void showAtts(Attributes attributes)
    {
        if(null != attributes && attributes.getLength() > 0)
        {
            for(int i = 0; i < attributes.getLength(); i++)
            {
                String s = attributes.getLocalName(i);
                String s1 = attributes.getQName(i);
                String s2 = attributes.getType(i);
                String s3 = attributes.getValue(i);
                String s4 = " ++ i = " + i + "::: LocalName = " + s + ", QName = " + s1 + ", Type = " + s2 + ", value = " + s3;
                System.out.println(s4);
            }

        }
    }

    private void handleViewFields(String s, String s1, String s2, Attributes attributes)
    {
        if(s1.equals("field"))
        {
            String s3 = attributes.getValue("name");
            int i = Integer.parseInt(attributes.getValue("row"));
            int j = Integer.parseInt(attributes.getValue("column"));
            int k = Integer.parseInt(attributes.getValue("width"));
            fvd = new FieldViewDefinition(s3, i, j, k);
            String s10 = attributes.getValue("height");
            if(s10 != null)
                fvd.setHeight(Integer.parseInt(s10));
            String s12 = attributes.getValue("values");
            if(s12 != null)
                fvd.addValues(s12);
        } else
        if(s1.equals("editcode"))
        {
            String s4 = attributes.getValue("code");
            String s5 = attributes.getValue("secondcode");
            if(s5 == null)
                fvd.addEditCode(s4.charAt(0));
            else
                fvd.addEditCode(s4.charAt(0), s5.charAt(0));
        } else
        if(s1.equals("editword"))
            fvd.addEditWord(attributes.getValue("word"));
        else
        if(s1.equals("msgid"))
            fvd.addERRMSGIDKeyword(loadViewMSGID(attributes));
        else
        if(s1.equals("mask"))
        {
            fvd.setMasked(true);
            fvd.setStartMaskingAt(Integer.parseInt(attributes.getValue("start")));
            fvd.setEndMaskingAt(Integer.parseInt(attributes.getValue("end")));
        } else
        if(s1.equals("chkmsgid"))
        {
            CHKMSGIDDefinition chkmsgiddefinition = null;
            String s6 = attributes.getValue("messageid");
            String s7 = attributes.getValue("library");
            String s9 = attributes.getValue("messagefile");
            String s11 = attributes.getValue("messagedatafield");
            String s13 = attributes.getValue("fieldname");
            chkmsgiddefinition = new CHKMSGIDDefinition(s6, s7, s9, s11, s13);
            rvd.add(chkmsgiddefinition);
        } else
        if(s1.equals("keyword"))
        {
            long l = KeywordIdentifier.keywordID(attributes.getValue("id")).longValue();
            String s8 = attributes.getValue("indicator");
            if(s8 == null)
                vkd = new KeywordDefinition(l);
            else
                vkd = new KeywordDefinition(l, s8);
        } else
        if(s1.equals("parameter"))
        {
            vkd.addParameter(attributes.getValue("string"));
        } else
        {
            System.out.println(">>> MyContentHandler.startViewElement-inviewflds(" + s + ", " + s1 + ", " + s2 + ", " + attributes + ")");
            showAtts(attributes);
        }
    }

    private void handleFeedbackSubfile(String s, String s1, String s2, Attributes attributes)
    {
        if(infbinds)
        {
            ResponseIndicator responseindicator = loadFeedbackResponseIndicator(s1, attributes);
            if(null != responseindicator)
                srfd.addForSubfile(responseindicator);
        } else
        if(s1.equals("indicators"))
            infbinds = true;
        else
        if(!s1.equals("keywords"))
            if(s1.equals("keyword"))
            {
                long l = KeywordIdentifier.keywordID(attributes.getValue("id")).longValue();
                String s3 = attributes.getValue("indicator");
                if(s3 == null)
                    vkd = new KeywordDefinition(l);
                else
                    vkd = new KeywordDefinition(l, s3);
            } else
            {
                if(s1.equals("parameter"))
                    vkd.addParameter(attributes.getValue("string"));
                if(s1.equals("subfilefold"))
                    srfd.setSubfileFoldKey(attributes.getValue("key"));
                else
                if(s1.equals("subfiledrop"))
                    srfd.setSubfileDropKey(attributes.getValue("key"));
                else
                if(s1.equals("subfilemode"))
                    srfd.setSubfileModeFieldName(attributes.getValue("fieldname"));
                else
                if(s1.equals("subfilecursorrrn"))
                    srfd.setSubfileCursorRRNFieldName(attributes.getValue("fieldname"));
                else
                if(s1.equals("subfilescroll"))
                    srfd.setSubfileScrollFieldName(attributes.getValue("fieldname"));
                else
                if(s1.equals("subfilerecordnumber"))
                    srfd.setSubfileRecordNumberFieldName(attributes.getValue("fieldname"));
            }
    }

    private void handleViewCommandKeyLabels(String s, String s1, String s2, Attributes attributes)
    {
        if(s1.equals("commandkeylabel"))
        {
            String s3 = attributes.getValue("key");
            String s7 = attributes.getValue("label");
            String s11 = attributes.getValue("beanname");
            int i = Integer.parseInt(attributes.getValue("priority"));
            String s16 = attributes.getValue("fieldid");
            ckl = new CommandKeyLabel(s3, s7, s11, i, s16);
        } else
        if(s1.equals("visibilityconditioned"))
        {
            String s4 = attributes.getValue("key");
            String s8 = attributes.getValue("label");
            String s12 = attributes.getValue("beanname");
            int j = Integer.parseInt(attributes.getValue("priority"));
            vcckl = new VisibilityConditionedCommandKeyLabel(s4, s8, s12, j);
        } else
        if(s1.equals("indicatorconditioned"))
        {
            String s5 = attributes.getValue("key");
            String s9 = attributes.getValue("label");
            String s13 = attributes.getValue("beanname");
            int k = Integer.parseInt(attributes.getValue("priority"));
            icckl = new IndicatorConditionedCommandKeyLabel(s5, s9, s13, k);
        } else
        if(s1.equals("conditionedlabel"))
        {
            String s6 = attributes.getValue("label");
            String s10 = attributes.getValue("isdynamic");
            String s14 = attributes.getValue("fieldid");
            if(null != icckl)
            {
                String s15 = attributes.getValue("indicator");
                icckl.addAConditionedLabel(new IndicatorConditionedLabel(s6, s15, s10, s14));
            } else
            {
                vcckl.addAConditionedLabel(new VisibilityConditionedLabel(s6, s14, s10));
            }
        } else
        {
            System.out.println(">>> MyContentHandler.handleViewCommandKeyLabels(" + s + ", " + s1 + ", " + s2 + ", " + attributes + ")");
            showAtts(attributes);
        }
    }

    private void handleViewOnlineHelp(String s, String s1, String s2, Attributes attributes)
    {
        if(s1.equals("helparea"))
        {
            if(null != attributes.getValue("top"))
            {
                int i = Integer.parseInt(attributes.getValue("top"));
                int j = Integer.parseInt(attributes.getValue("left"));
                int k = Integer.parseInt(attributes.getValue("bottom"));
                int j1 = Integer.parseInt(attributes.getValue("right"));
                ha = new HelpArea(i, j, k, j1);
            } else
            {
                ha = new HelpArea();
            }
        } else
        if(s1.equals("field"))
        {
            String s3 = attributes.getValue("fieldname");
            String s6 = attributes.getValue("fieldchoice");
            if(s6 == null)
                ha.setField(s3);
            else
                ha.setField(s3, Integer.parseInt(s6));
        } else
        if(s1.equals("definition"))
        {
            String s4 = attributes.getValue("recordname");
            String s7 = attributes.getValue("helpspecname");
            String s9 = attributes.getValue("object");
            String s12 = attributes.getValue("library");
            String s13 = attributes.getValue("type");
            if(s13.equals("HelpRecord"))
            {
                if(s9 == null && s12 == null)
                    hd = new HelpRecord(s4, s7);
                else
                if(s12 == null)
                    hd = new HelpRecord(s4, s7, s9);
                else
                    hd = new HelpRecord(s4, s7, s9, s12);
            } else
            if(s13.equals("HelpPanelGroup"))
                if(s9 == null && s12 == null)
                    hd = new HelpPanelGroup(s4, s7);
                else
                if(s12 == null)
                    hd = new HelpPanelGroup(s4, s7, s9);
                else
                    hd = new HelpPanelGroup(s4, s7, s9, s12);
            String s14 = attributes.getValue("indicator");
            if(s14 != null)
                hd.setIndicatorExpression(s14);
            ha.setHelpDefinition(hd);
        } else
        if(s1.equals("helpboundary"))
        {
            long l = KeywordIdentifier.keywordID(attributes.getValue("id")).longValue();
            String s10 = attributes.getValue("indicator");
            if(s10 == null)
                ha.setHelpBoundary(new HelpKeyword(l));
            else
                ha.setHelpBoundary(new HelpKeyword(l, s10));
        } else
        if(s1.equals("helpexcluded"))
        {
            long l1 = KeywordIdentifier.keywordID(attributes.getValue("id")).longValue();
            String s11 = attributes.getValue("indicator");
            if(s11 == null)
                ha.setHelpExcluded(new HelpKeyword(l1));
            else
                ha.setHelpExcluded(new HelpKeyword(l1, s11));
        } else
        if(s1.equals("helpgroup"))
        {
            String s5 = attributes.getValue("name");
            String s8 = attributes.getValue("recordname");
            int i1 = Integer.parseInt(attributes.getValue("sequence"));
            HelpGroup helpgroup = new HelpGroup(s5, s8, i1);
            rvd.add(helpgroup);
        }
    }

    private void handleViewWindow(String s, String s1, String s2, Attributes attributes)
    {
        String s3 = attributes.getValue("startline");
        if(s3 != null)
            rvd.setWdwStartLine(Integer.parseInt(s3));
        String s4 = attributes.getValue("startlinefield");
        if(s4 != null)
            rvd.setWdwStartLineField(s4);
        String s5 = attributes.getValue("startpos");
        if(s5 != null)
            rvd.setWdwStartPos(Integer.parseInt(s5));
        String s6 = attributes.getValue("startposfield");
        if(s6 != null)
            rvd.setWdwStartPosField(s6);
    }

    private void handleViewWindowTitles(String s, String s1, String s2, Attributes attributes)
    {
        WindowTitleDefinition windowtitledefinition = null;
        String s3 = attributes.getValue("indicator");
        boolean flag = (new Boolean(attributes.getValue("constantastitle"))).booleanValue();
        String s4 = attributes.getValue("title");
        String s5 = attributes.getValue("color");
        String s6 = s5 != null ? s5 : "";
        String s7 = attributes.getValue("dspatr");
        String s8 = s7 != null ? s7 : "";
        String s9 = attributes.getValue("alignment");
        String s10 = s9 != null ? s9 : "";
        String s11 = attributes.getValue("position");
        String s12 = s11 != null ? s11 : "";
        if(s5 == null && s7 == null && s9 == null && s11 == null)
            windowtitledefinition = new WindowTitleDefinition(s3, flag, s4);
        else
            windowtitledefinition = new WindowTitleDefinition(s3, flag, s4, s6, s8, s10, s12);
        rvd.add(windowtitledefinition);
    }

    Locator locator;
    IRecordDataDefinition rdd;
    IRecordViewDefinition rvd;
    SubfileControlRecordViewDefinition srvd;
    IRecordFeedbackDefinition rfd;
    SubfileControlRecordFeedbackDefinition srfd;
    boolean indata;
    boolean indataind;
    boolean indatafield;
    IndicatorDataDefinition indicators;
    FieldDataDefinition fdd;
    boolean inview;
    boolean inviewfvis;
    boolean inviewflds;
    boolean inviewccl;
    boolean inviewhlpa;
    FieldViewDefinition fvd;
    CommandKeyLabel ckl;
    IndicatorConditionedCommandKeyLabel icckl;
    Vector fvisstr;
    VisibilityConditionedCommandKeyLabel vcckl;
    boolean inviewkwds;
    Vector inviewkwdsv;
    KeywordDefinition vkd;
    boolean inviewsfl;
    FieldSelectionSubfileHeightInfo fsshi;
    boolean infeedback;
    boolean infbinds;
    boolean infbsfl;
    boolean infbrcl;
    String recordName;
    String recordType;
    String recordPackage;
    String recordClassName;
    private HelpArea ha;
    private HelpDefinition hd;
}
