// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.convert.ExportHandler;
import com.ibm.as400ad.webfacing.convert.IWebResourceGenerator;
import com.ibm.as400ad.webfacing.convert.gen.WebResourceFileWriter;
import com.ibm.as400ad.webfacing.convert.model.RecordLayout;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.bean:
//            JavaClassSourceCode, JavaSourceCodeCollection

public abstract class DefinitionGenerator
    implements IWebResourceGenerator, ENUM_KeywordIdentifiers
{

    public DefinitionGenerator(RecordLayout recordlayout, WebResourceFileWriter webresourcefilewriter)
    {
        _rn = null;
        _recLayout = null;
        _fileWriter = null;
        _fileNode = null;
        _constructor = null;
        _class = null;
        _recLayout = recordlayout;
        _rn = recordlayout.getRecordNode();
        _fileWriter = webresourcefilewriter;
    }

    public void generate()
    {
        String s = getBeanName();
        _class = new JavaClassSourceCode(s, getFileNode().getPackageName());
        specifyClassAttributes(_class);
        _class.addConstructor("", generateConstructor());
        try
        {
            _class.write(getFileWriter());
        }
        catch(Throwable throwable)
        {
            ExportHandler.err(1, throwable, "Error while writing out definiton for " + getBeanName() + " = " + throwable);
        }
    }

    protected String getBeanBaseClassPrefix()
    {
        return "";
    }

    protected abstract String getBeanBaseClassSuffix();

    protected String getBeanName()
    {
        return getRecordNode().getWebName();
    }

    protected FileNode getFileNode()
    {
        if(_fileNode == null)
            _fileNode = (FileNode)_rn.getParent();
        return _fileNode;
    }

    protected WebResourceFileWriter getFileWriter()
    {
        return _fileWriter;
    }

    protected RecordLayout getRecordLayout()
    {
        return _recLayout;
    }

    protected RecordNode getRecordNode()
    {
        return _rn;
    }

    private JavaSourceCodeCollection generateConstructor()
    {
        _constructor = new JavaSourceCodeCollection();
        _constructor.setIndentLevel(1);
        _constructor.addElement("   super(\"" + getRecordNode().getWebName() + "\");" + _constructor.getNewline());
        specifyNullConstructorBody();
        return _constructor;
    }

    protected String getBaseClassName()
    {
        return getBeanBaseClassPrefix() + getBeanBaseClassSuffix();
    }

    protected void specifyClassAttributes(JavaClassSourceCode javaclasssourcecode)
    {
        javaclasssourcecode.addImport("com.ibm.as400ad.webfacing.runtime.model.def.*");
        javaclasssourcecode.addClassExtension(getBaseClassName());
    }

    protected void addLineToConstructor(String s)
    {
        _constructor.addLine(s);
    }

    protected JavaSourceCodeCollection getConstructor()
    {
        return _constructor;
    }

    protected abstract void specifyNullConstructorBody();

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2000");
    private RecordNode _rn;
    private RecordLayout _recLayout;
    private WebResourceFileWriter _fileWriter;
    private FileNode _fileNode;
    private JavaSourceCodeCollection _constructor;
    protected JavaClassSourceCode _class;

}
