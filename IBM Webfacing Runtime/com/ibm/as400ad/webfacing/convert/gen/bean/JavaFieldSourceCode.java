// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import java.util.Set;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.bean:
//            JavaSourceCodeCollection

public class JavaFieldSourceCode extends JavaSourceCodeCollection
{

    public JavaFieldSourceCode(String s, String s1)
    {
        this(s, s1, "");
    }

    public JavaFieldSourceCode(String s, String s1, String s2)
    {
        this(s, s1, s2, null);
    }

    public JavaFieldSourceCode(String s, String s1, String s2, JavaSourceCodeCollection javasourcecodecollection)
    {
        this(s, s1, s2, javasourcecodecollection, null);
    }

    public JavaFieldSourceCode(String s, String s1, String s2, JavaSourceCodeCollection javasourcecodecollection, JavaSourceCodeCollection javasourcecodecollection1)
    {
        _comment = null;
        _name = s;
        _dataType = s1;
        _visibility = s2;
        _initializer = javasourcecodecollection;
        _comment = javasourcecodecollection1;
    }

    private String modifiersToString()
    {
        Set set = getModifiers();
        StringBuffer stringbuffer = new StringBuffer(30);
        if(set.contains("final"))
            stringbuffer.append("final ");
        if(set.contains("static"))
            stringbuffer.append("static ");
        if(set.contains("synchronized"))
            stringbuffer.append("synchronized ");
        if(set.contains("volatile"))
            stringbuffer.append("volatile ");
        return stringbuffer.toString();
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer(getNewline());
        if(_comment != null)
        {
            stringbuffer.append(_comment.toString());
            stringbuffer.append(getNewline());
        }
        stringbuffer.append(_visibility);
        stringbuffer.append(" ");
        stringbuffer.append(modifiersToString());
        stringbuffer.append(_dataType);
        stringbuffer.append(" ");
        stringbuffer.append(_name);
        stringbuffer.append(" ");
        if(_initializer != null)
        {
            stringbuffer.append("=");
            stringbuffer.append(_initializer.toString());
        }
        stringbuffer.append(";");
        return stringbuffer.toString();
    }

    private String _name;
    private JavaSourceCodeCollection _initializer;
    private String _visibility;
    private String _dataType;
    private JavaSourceCodeCollection _comment;
}
