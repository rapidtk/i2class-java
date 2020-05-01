// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert;

import com.ibm.as400ad.code400.dom.FieldNode;
import com.ibm.as400ad.code400.dom.RecordNode;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.ClientScriptSourceCodeCollection;
import com.ibm.as400ad.webfacing.convert.gen.dhtml.DHTMLSourceCodeCollection;

// Referenced classes of package com.ibm.as400ad.webfacing.convert:
//            IFieldOutput, IWebFaceConverter, IRecordLayout, IFieldOutputVisitor, 
//            IWebResourceGenerator, IMultiWebResourceGenerator

public interface IConversionFactory
{

    public abstract IFieldOutput getFieldOutput(FieldNode fieldnode);

    public abstract IWebFaceConverter getWebFaceConverter();

    public abstract IFieldOutputVisitor getClientScriptJSPVisitor(IRecordLayout irecordlayout, ClientScriptSourceCodeCollection clientscriptsourcecodecollection);

    public abstract IWebResourceGenerator getDataBeanGenerator(IRecordLayout irecordlayout);

    public abstract IFieldOutputVisitor getDHTMLBodyJSPVisitor(IRecordLayout irecordlayout, DHTMLSourceCodeCollection dhtmlsourcecodecollection);

    public abstract IWebResourceGenerator getFeedbackBeanGenerator(IRecordLayout irecordlayout);

    public abstract IWebResourceGenerator getJSPGenerator(IRecordLayout irecordlayout);

    public abstract IMultiWebResourceGenerator getMultiWebResourceGenerator(IRecordLayout irecordlayout);

    public abstract IRecordLayout getRecordLayout(RecordNode recordnode);

    public abstract IWebResourceGenerator getViewBeanGenerator(IRecordLayout irecordlayout);

    public abstract IWebResourceGenerator getXMLBeanGenerator(IRecordLayout irecordlayout);
}
