// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.convert.gen.bean:
//            IXMLComponent

public class XMLElement
    implements IXMLComponent
{

    public XMLElement(String s)
    {
        _children = null;
        _attributes = null;
        _name = null;
        _indent = null;
        _name = s;
    }

    public XMLElement()
    {
        this("anonymous");
    }

    public void add(IXMLComponent ixmlcomponent)
    {
        if(ixmlcomponent == null)
            return;
        if(_children == null)
            _children = new Vector();
        _children.add(ixmlcomponent);
    }

    public void remove(IXMLComponent ixmlcomponent)
    {
        if(_children != null)
            _children.remove(ixmlcomponent);
    }

    public Iterator getChildIterator()
    {
        if(_children != null)
            return _children.iterator();
        else
            return null;
    }

    public void addAttribute(String s, String s1)
    {
        if(_attributes == null)
            _attributes = new HashMap();
        _attributes.put(s, s1);
    }

    public String toString()
    {
        if(_attributes == null && _children == null)
            return "";
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append(getIndent() + "<" + getName());
        if(_attributes != null)
        {
            Object obj;
            for(Iterator iterator = _attributes.keySet().iterator(); iterator.hasNext(); stringbuffer.append(" " + obj + "=\"" + _attributes.get(obj) + "\""))
                obj = iterator.next();

        }
        if(_children == null)
        {
            stringbuffer.append("/>" + getNewline());
        } else
        {
            stringbuffer.append(">" + getNewline());
            IXMLComponent ixmlcomponent;
            for(Iterator iterator1 = getChildIterator(); iterator1.hasNext(); stringbuffer.append(ixmlcomponent.toString()))
            {
                ixmlcomponent = (IXMLComponent)iterator1.next();
                ixmlcomponent.setIndent(getIndent() + "  ");
            }

            stringbuffer.append(getIndent() + "</" + getName() + ">" + getNewline());
        }
        return stringbuffer.toString();
    }

    public String getIndent()
    {
        return _indent != null ? _indent : "";
    }

    public void setIndent(String s)
    {
        _indent = s;
    }

    public String getName()
    {
        return _name != null ? _name : "";
    }

    public void setName(String s)
    {
        _name = s;
    }

    public String getNewline()
    {
        return System.getProperty("line.separator");
    }

    private Vector _children;
    private HashMap _attributes;
    private String _name;
    private String _indent;
    public static final String INDENT_CHARS = "  ";
}
