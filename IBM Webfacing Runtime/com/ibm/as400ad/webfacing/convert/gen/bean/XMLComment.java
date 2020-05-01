// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import java.util.Iterator;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.bean:
//            IXMLComponent

public class XMLComment
    implements IXMLComponent
{

    public XMLComment(String s)
    {
        _text = null;
        _indent = null;
        _text = s;
    }

    public XMLComment()
    {
        _text = null;
        _indent = null;
    }

    public void add(IXMLComponent ixmlcomponent)
    {
    }

    public void remove(IXMLComponent ixmlcomponent)
    {
    }

    public Iterator getChildIterator()
    {
        return null;
    }

    public String getIndent()
    {
        return _indent != null ? _indent : "";
    }

    public void setIndent(String s)
    {
        _indent = s;
    }

    public String getText()
    {
        return _text != null ? _text : "";
    }

    public void setText(String s)
    {
        _text = s;
    }

    public String getNewline()
    {
        return System.getProperty("line.separator");
    }

    public String toString()
    {
        return getIndent() + "<!-- " + getText() + " -->" + getNewline();
    }

    private String _text;
    private String _indent;
}
