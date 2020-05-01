// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.xml;

import java.io.PrintStream;

// Referenced classes of package com.ibm.etools.iseries.webfacing.xml:
//            IEntryProperty, XElement

public class XAttribute
    implements IEntryProperty
{

    public XAttribute(XElement xelement, String s, String s1)
    {
        _strName = null;
        _strValue = null;
        _strDateCreated = null;
        _strDateModified = null;
        _entryParent = null;
        _entryParent = xelement;
        _strName = s;
        _strValue = s1;
    }

    public String getName()
    {
        return _strName != null ? _strName : _strEmpty;
    }

    public XElement getParentElement()
    {
        return _entryParent;
    }

    public String getValue()
    {
        return _strValue != null ? _strValue : _strEmpty;
    }

    public void printAttribute(int i)
    {
        StringBuffer stringbuffer = new StringBuffer();
        for(int j = 0; j < i; j++)
            stringbuffer.append(' ');

        if(_strName != null)
            stringbuffer.append(_strName);
        else
            stringbuffer.append("null");
        if(_strValue != null)
            stringbuffer.append(", <" + _strValue + ">");
        else
            stringbuffer.append(", null");
        System.out.println(stringbuffer.toString());
    }

    public void printAttributeString(StringBuffer stringbuffer)
    {
        if(_strName != null)
            stringbuffer.append(_strName);
        else
            stringbuffer.append("undefined");
        stringbuffer.append("=\"");
        if(_strValue != null)
            stringbuffer.append(_strValue);
        stringbuffer.append("\"");
    }

    public void setName(String s)
    {
        _strName = s;
    }

    public void setValue(String s)
    {
        _strValue = s;
    }

    public static final String copyright = "(c) Copyright IBM Corporation 2002. all rights reserved";
    protected static final String _strEmpty = new String();
    protected String _strName;
    protected String _strValue;
    protected String _strDateCreated;
    protected String _strDateModified;
    protected XElement _entryParent;

}
