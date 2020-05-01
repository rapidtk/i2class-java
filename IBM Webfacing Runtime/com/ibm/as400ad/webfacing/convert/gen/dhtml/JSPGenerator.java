// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.dhtml;

import com.ibm.as400ad.code400.dom.AnyNode;
import com.ibm.as400ad.webfacing.convert.*;
import com.ibm.as400ad.webfacing.convert.gen.*;
import com.ibm.as400ad.webfacing.convert.model.RecordLayout;
import java.io.IOException;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.dhtml:
//            ClientScriptSourceCodeCollection, DHTMLSourceCodeCollection

public class JSPGenerator
    implements IWebResourceGenerator
{

    public JSPGenerator(RecordLayout recordlayout, IConversionFactory iconversionfactory, WebResourceFileWriter webresourcefilewriter)
    {
        _clientScriptSource = null;
        _conversionFactory = null;
        _dhtmlSource = null;
        _fileWriter = null;
        _recLayout = null;
        _recLayout = recordlayout;
        _conversionFactory = iconversionfactory;
        _fileWriter = webresourcefilewriter;
    }

    public void generate()
        throws IOException
    {
        String s = getRecordLayout().getRecordNode().getWebName();
        JointFieldOutputVisitor jointfieldoutputvisitor = getVisitors();
        generateCode(jointfieldoutputvisitor);
        writeSourceToFiles(s);
    }

    protected void generateCode(IFieldOutputVisitor ifieldoutputvisitor)
    {
        FieldOutputTraversal fieldoutputtraversal = new FieldOutputTraversal();
        ifieldoutputvisitor.printBeginningLines();
        fieldoutputtraversal.traverse(ifieldoutputvisitor);
        ifieldoutputvisitor.printEndingLines();
        ifieldoutputvisitor.insertHeader();
    }

    private WebResourceFileWriter getFileWriter()
    {
        return _fileWriter;
    }

    private RecordLayout getRecordLayout()
    {
        return _recLayout;
    }

    private JointFieldOutputVisitor getVisitors()
    {
        RecordLayout recordlayout = getRecordLayout();
        _clientScriptSource = new ClientScriptSourceCodeCollection();
        _dhtmlSource = new DHTMLSourceCodeCollection();
        IFieldOutputVisitor ifieldoutputvisitor = _conversionFactory.getDHTMLBodyJSPVisitor(recordlayout, _dhtmlSource);
        IFieldOutputVisitor ifieldoutputvisitor1 = _conversionFactory.getClientScriptJSPVisitor(recordlayout, _clientScriptSource);
        JointFieldOutputVisitor jointfieldoutputvisitor = new JointFieldOutputVisitor(ifieldoutputvisitor, ifieldoutputvisitor1);
        return jointfieldoutputvisitor;
    }

    private void writeSourceToFiles(String s)
        throws IOException
    {
        getFileWriter().writeSourceToFile(_dhtmlSource, s, ".jsp");
        getFileWriter().writeSourceToFile(_clientScriptSource, s + "JavaScript", ".jsp");
    }

    private ClientScriptSourceCodeCollection _clientScriptSource;
    private IConversionFactory _conversionFactory;
    private DHTMLSourceCodeCollection _dhtmlSource;
    private WebResourceFileWriter _fileWriter;
    private RecordLayout _recLayout;
}
