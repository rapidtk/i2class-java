// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert;

import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.webfacing.convert.gen.MultiWebResourceGenerator;
import com.ibm.as400ad.webfacing.convert.gen.SubfileControlMultiWebResourceGenerator;
import com.ibm.as400ad.webfacing.convert.gen.WebResourceFileWriter;
import com.ibm.as400ad.webfacing.convert.gen.bean.DataDefinitionGenerator;
import com.ibm.as400ad.webfacing.convert.gen.bean.FeedbackDefinitionGenerator;
import com.ibm.as400ad.webfacing.convert.gen.bean.SFLCTLDataDefinitionGenerator;
import com.ibm.as400ad.webfacing.convert.gen.bean.SFLCTLFeedbackDefinitionGenerator;
import com.ibm.as400ad.webfacing.convert.gen.bean.SFLCTLViewDefinitionGenerator;
import com.ibm.as400ad.webfacing.convert.gen.bean.SFLDataDefinitionGenerator;
import com.ibm.as400ad.webfacing.convert.gen.bean.SFLMSGRCDDataDefinitionGenerator;
import com.ibm.as400ad.webfacing.convert.gen.bean.ViewDefinitionGenerator;
import com.ibm.as400ad.webfacing.convert.gen.bean.XMLDefinitionGenerator;
import com.ibm.as400ad.webfacing.convert.gen.bean.XMLMSGRCDDefinitionGenerator;
import com.ibm.as400ad.webfacing.convert.gen.bean.XMLSFLCTLDefinitionGenerator;
import com.ibm.as400ad.webfacing.convert.gen.bean.XMLSFLDefinitionGenerator;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.AbstractSubfileControlClientScriptJSPVisitor;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.ClientScriptJSPVisitor;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.ClientScriptSourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.DHTMLBodyJSPVisitor;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.DHTMLSourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.FieldSelectionSubfileDHTMLBodyJSPVisitor;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.JSPGenerator;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.SingleLineSubfileDHTMLBodyJSPVisitor;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.SubfileControlClientScriptJSPVisitor;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.SubfileControlDHTMLBodyJSPVisitor;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.SubfileControlJSPGenerator;
import com.ibm.as400ad.webfacing.convert.model.ComputedConstantFieldOutput;
import com.ibm.as400ad.webfacing.convert.model.ConstantFieldOutput;
import com.ibm.as400ad.webfacing.convert.model.EDTMSKFieldOutput;
import com.ibm.as400ad.webfacing.convert.model.HiddenInputFieldOutput;
import com.ibm.as400ad.webfacing.convert.model.HiddenScriptableInputFieldOutput;
import com.ibm.as400ad.webfacing.convert.model.InputFieldOutput;
import com.ibm.as400ad.webfacing.convert.model.MsgDataFieldOutput;
import com.ibm.as400ad.webfacing.convert.model.NonDisplayFieldOutput;
import com.ibm.as400ad.webfacing.convert.model.OutputFieldOutput;
import com.ibm.as400ad.webfacing.convert.model.RecordLayout;
import com.ibm.as400ad.webfacing.convert.model.SelectFieldOutput;
import com.ibm.as400ad.webfacing.convert.model.SubfileControlRecordLayout;
import com.ibm.as400ad.webfacing.convert.model.SubfileRecordLayout;

// Referenced classes of package com.ibm.as400ad.webfacing.convert:
//            WebFaceConverter, IConversionFactory, IRecordLayout, ConversionLogger, 
//            IFieldOutput, IWebFaceConverter, IFieldOutputVisitor, IWebResourceGenerator, 
//            IMultiWebResourceGenerator

public class ConversionFactory
    implements IConversionFactory
{

    public ConversionFactory()
    {
        _javaBeanFileWriter = null;
        _jspFileWriter = null;
    }

    public IFieldOutput getFieldOutput(FieldNode fieldnode)
    {
        Object obj = null;
        if(fieldnode.hasWebSettings())
        {
            for(WebSettingsNodeEnumeration websettingsnodeenumeration = fieldnode.getWebSettings(); websettingsnodeenumeration.hasMoreElements();)
            {
                WebSettingsNode websettingsnode = websettingsnodeenumeration.nextWebSettings();
                if(websettingsnode.getType() == 4)
                {
                    if(fieldnode.isUnnamedConstantField())
                        return null;
                    if(fieldnode.getFieldUsage() == 'O')
                        return new NonDisplayFieldOutput(fieldnode);
                    else
                        return new HiddenInputFieldOutput(fieldnode);
                }
                if(websettingsnode.getType() == 19)
                    return new HiddenScriptableInputFieldOutput(fieldnode);
            }

        }
        KeywordNodeEnumeration keywordnodeenumeration = fieldnode.getKeywordsOfType(201);
        if(keywordnodeenumeration.hasMoreElements() && fieldnode.getParentRecord().isSFLMSG())
            return null;
        keywordnodeenumeration = fieldnode.getKeywordsOfType(197);
        if(keywordnodeenumeration.hasMoreElements())
        {
            obj = new MsgDataFieldOutput(fieldnode);
            return ((IFieldOutput) (obj));
        }
        switch(fieldnode.getFieldUsage())
        {
        default:
            break;

        case 66: // 'B'
        case 73: // 'I'
            KeywordNodeEnumeration keywordnodeenumeration1 = fieldnode.getKeywordsOfType(222);
            if(keywordnodeenumeration1.hasMoreElements())
            {
                obj = new SelectFieldOutput(fieldnode);
                keywordnodeenumeration1 = null;
            }
            keywordnodeenumeration1 = fieldnode.getKeywordsOfType(94);
            if(null == obj && keywordnodeenumeration1.hasMoreElements())
                obj = new EDTMSKFieldOutput(fieldnode);
            if(null == obj)
                obj = new InputFieldOutput(fieldnode);
            break;

        case 32: // ' '
            if(fieldnode.isUnnamedConstantField())
            {
                KeywordNodeEnumeration keywordnodeenumeration2 = fieldnode.getKeywordsOfType(81);
                KeywordNodeEnumeration keywordnodeenumeration3 = fieldnode.getKeywordsOfType(213);
                KeywordNodeEnumeration keywordnodeenumeration4 = fieldnode.getKeywordsOfType(217);
                KeywordNodeEnumeration keywordnodeenumeration5 = fieldnode.getKeywordsOfType(211);
                if(keywordnodeenumeration2.nextKeyword() != null || keywordnodeenumeration3.nextKeyword() != null || keywordnodeenumeration4.nextKeyword() != null || keywordnodeenumeration5.nextKeyword() != null)
                    obj = new ComputedConstantFieldOutput(fieldnode);
                else
                    obj = new ConstantFieldOutput(fieldnode);
            } else
            {
                obj = new OutputFieldOutput(fieldnode);
            }
            break;

        case 79: // 'O'
            obj = new OutputFieldOutput(fieldnode);
            break;

        case 72: // 'H'
        case 77: // 'M'
        case 80: // 'P'
            obj = new NonDisplayFieldOutput(fieldnode);
            break;
        }
        return ((IFieldOutput) (obj));
    }

    public IWebFaceConverter getWebFaceConverter()
    {
        return new WebFaceConverter();
    }

    public IFieldOutputVisitor getClientScriptJSPVisitor(IRecordLayout irecordlayout, ClientScriptSourceCodeCollection clientscriptsourcecodecollection)
    {
        if(irecordlayout.getRecordNode().isSFLCTL())
        {
            if(irecordlayout.getRecordNode().isSFLMSGCTL() && ((SubfileControlRecordLayout)irecordlayout).getSFLPAG() == 1)
                return new AbstractSubfileControlClientScriptJSPVisitor((SubfileControlRecordLayout)irecordlayout, clientscriptsourcecodecollection);
            else
                return new SubfileControlClientScriptJSPVisitor((SubfileControlRecordLayout)irecordlayout, clientscriptsourcecodecollection);
        } else
        {
            return new ClientScriptJSPVisitor((RecordLayout)irecordlayout, clientscriptsourcecodecollection);
        }
    }

    public IWebResourceGenerator getDataBeanGenerator(IRecordLayout irecordlayout)
    {
        RecordNode recordnode = irecordlayout.getRecordNode();
        if(recordnode.isSFLCTL())
            return new SFLCTLDataDefinitionGenerator((RecordLayout)irecordlayout, getJavaBeanFileWriter());
        if(recordnode.isSFL())
            return new SFLDataDefinitionGenerator((RecordLayout)irecordlayout, getJavaBeanFileWriter());
        if(recordnode.isSFLMSG())
            return new SFLMSGRCDDataDefinitionGenerator((RecordLayout)irecordlayout, getJavaBeanFileWriter());
        else
            return new DataDefinitionGenerator((RecordLayout)irecordlayout, getJavaBeanFileWriter());
    }

    public IFieldOutputVisitor getDHTMLBodyJSPVisitor(IRecordLayout irecordlayout, DHTMLSourceCodeCollection dhtmlsourcecodecollection)
    {
        if(irecordlayout.getRecordNode().isSFLCTL())
        {
            if(irecordlayout.getRecordNode().isSFLMSGCTL() && ((SubfileControlRecordLayout)irecordlayout).getSFLPAG() == 1)
                return new SingleLineSubfileDHTMLBodyJSPVisitor((SubfileControlRecordLayout)irecordlayout, dhtmlsourcecodecollection);
            if(((SubfileControlRecordLayout)irecordlayout).subfileHasFieldSelection())
                return new FieldSelectionSubfileDHTMLBodyJSPVisitor((SubfileControlRecordLayout)irecordlayout, dhtmlsourcecodecollection);
            else
                return new SubfileControlDHTMLBodyJSPVisitor((SubfileControlRecordLayout)irecordlayout, dhtmlsourcecodecollection);
        } else
        {
            return new DHTMLBodyJSPVisitor((RecordLayout)irecordlayout, dhtmlsourcecodecollection);
        }
    }

    public ExportSettings getExportSettings()
    {
        return ExportSettings.getExportSettings();
    }

    public IWebResourceGenerator getFeedbackBeanGenerator(IRecordLayout irecordlayout)
    {
        RecordNode recordnode = irecordlayout.getRecordNode();
        if(recordnode.isSFLCTL())
            return new SFLCTLFeedbackDefinitionGenerator((RecordLayout)irecordlayout, getJavaBeanFileWriter());
        else
            return new FeedbackDefinitionGenerator((RecordLayout)irecordlayout, getJavaBeanFileWriter());
    }

    private WebResourceFileWriter getJavaBeanFileWriter()
    {
        if(_javaBeanFileWriter == null)
            _javaBeanFileWriter = new WebResourceFileWriter(getExportSettings().getJavaDirectoryWithPackage());
        return _javaBeanFileWriter;
    }

    private WebResourceFileWriter getJspFileWriter()
    {
        if(_jspFileWriter == null)
            _jspFileWriter = new WebResourceFileWriter(getExportSettings().getJspDirectoryWithPackage());
        return _jspFileWriter;
    }

    public IWebResourceGenerator getJSPGenerator(IRecordLayout irecordlayout)
    {
        RecordNode recordnode = irecordlayout.getRecordNode();
        if(recordnode.isSFLCTL())
            return new SubfileControlJSPGenerator((RecordLayout)irecordlayout, this, getJspFileWriter());
        else
            return new JSPGenerator((RecordLayout)irecordlayout, this, getJspFileWriter());
    }

    public IMultiWebResourceGenerator getMultiWebResourceGenerator(IRecordLayout irecordlayout)
    {
        RecordNode recordnode = irecordlayout.getRecordNode();
        if(recordnode.isSFLCTL())
            return new SubfileControlMultiWebResourceGenerator(this, irecordlayout);
        else
            return new MultiWebResourceGenerator(this, irecordlayout);
    }

    public IRecordLayout getRecordLayout(RecordNode recordnode)
    {
        Object obj;
        if(recordnode.isSFLCTL())
        {
            obj = new SubfileControlRecordLayout(recordnode, this);
            if(ConversionLogger.isAbortRecordConversion())
            {
                recordnode.logEvent(8);
                ConversionLogger.setAbortRecordConversion(false);
            }
            RecordNode recordnode1 = recordnode.getRelatedSFL();
            SubfileRecordLayout subfilerecordlayout = (SubfileRecordLayout)getRecordLayout(recordnode1);
            ((SubfileControlRecordLayout)obj).setSubfileRecordLayout(subfilerecordlayout);
        } else
        if(recordnode.isSFL() || recordnode.isSFLMSG())
            obj = new SubfileRecordLayout(recordnode, this);
        else
            obj = new RecordLayout(recordnode, this);
        if(ConversionLogger.isAbortRecordConversion())
            recordnode.logEvent(8);
        return ((IRecordLayout) (obj));
    }

    public IWebResourceGenerator getViewBeanGenerator(IRecordLayout irecordlayout)
    {
        RecordNode recordnode = irecordlayout.getRecordNode();
        if(recordnode.isSFLCTL())
            return new SFLCTLViewDefinitionGenerator((RecordLayout)irecordlayout, getJavaBeanFileWriter());
        else
            return new ViewDefinitionGenerator((RecordLayout)irecordlayout, getJavaBeanFileWriter());
    }

    public IWebResourceGenerator getXMLBeanGenerator(IRecordLayout irecordlayout)
    {
        RecordNode recordnode = irecordlayout.getRecordNode();
        if(recordnode.isSFLCTL())
            return new XMLSFLCTLDefinitionGenerator((RecordLayout)irecordlayout, getJavaBeanFileWriter());
        if(recordnode.isSFL())
            return new XMLSFLDefinitionGenerator((RecordLayout)irecordlayout, getJavaBeanFileWriter());
        if(recordnode.isSFLMSG())
            return new XMLMSGRCDDefinitionGenerator((RecordLayout)irecordlayout, getJavaBeanFileWriter());
        else
            return new XMLDefinitionGenerator((RecordLayout)irecordlayout, getJavaBeanFileWriter());
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2000");
    private WebResourceFileWriter _javaBeanFileWriter;
    private WebResourceFileWriter _jspFileWriter;

}
