// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import java.util.Set;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.bean:
//            JavaSourceCodeCollection

public class JavaMethodSourceCode extends JavaSourceCodeCollection
{

    public JavaMethodSourceCode(String s, String s1, JavaSourceCodeCollection javasourcecodecollection)
    {
        this(s, s1, "public", javasourcecodecollection);
    }

    public JavaMethodSourceCode(String s, String s1, String s2, JavaSourceCodeCollection javasourcecodecollection)
    {
        _isConstructor = false;
        _isConstructor = true;
        _name = s;
        _parameters = s1;
        _visibility = s2;
        _body = javasourcecodecollection;
    }

    public JavaMethodSourceCode(String s, String s1, String s2, String s3, JavaSourceCodeCollection javasourcecodecollection)
    {
        _isConstructor = false;
        _isConstructor = false;
        _name = s;
        _parameters = s1;
        _visibility = s2;
        _returnType = s3;
        _body = javasourcecodecollection;
    }

    public void addToBody(JavaSourceCodeCollection javasourcecodecollection)
    {
        _body.addAll(javasourcecodecollection);
    }

    public String getName()
    {
        return _name;
    }

    public String getParameters()
    {
        return _parameters;
    }

    private String modifiersToString()
    {
        Set set = getModifiers();
        if(set.contains("abstract"))
            return "abstract ";
        StringBuffer stringbuffer = new StringBuffer(30);
        if(set.contains("final"))
            stringbuffer.append("final ");
        if(set.contains("native"))
            stringbuffer.append("native ");
        if(set.contains("static"))
            stringbuffer.append("static ");
        if(set.contains("synchronized"))
            stringbuffer.append("synchronized ");
        return stringbuffer.toString();
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer(getNewline());
        stringbuffer.append(_visibility);
        stringbuffer.append(" ");
        stringbuffer.append(modifiersToString());
        if(!_isConstructor)
            stringbuffer.append(_returnType + " ");
        stringbuffer.append(_name);
        stringbuffer.append("(");
        stringbuffer.append(_parameters);
        stringbuffer.append(")");
        stringbuffer.append(getNewline());
        stringbuffer.append("{");
        stringbuffer.append(getNewline());
        stringbuffer.append(_body.toString());
        stringbuffer.append(getNewline());
        stringbuffer.append("}");
        stringbuffer.append(getNewline());
        return stringbuffer.toString();
    }

    private String _name;
    private String _parameters;
    private JavaSourceCodeCollection _body;
    private boolean _isConstructor;
    private String _visibility;
    private String _returnType;
}
