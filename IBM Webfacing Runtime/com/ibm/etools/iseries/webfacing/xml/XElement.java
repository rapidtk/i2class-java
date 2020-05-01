// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.xml;

import java.io.PrintStream;
import java.util.Vector;

// Referenced classes of package com.ibm.etools.iseries.webfacing.xml:
//            XAttribute, IEntry

public class XElement
{

    public XAttribute getAttribute(String s)
    {
        XAttribute xattribute = null;
        if(null != _vectorProperties)
        {
            for(int i = 0; null == xattribute && i < _vectorProperties.size(); i++)
                if(((XAttribute)_vectorProperties.elementAt(i)).getName().equals(s))
                    xattribute = (XAttribute)_vectorProperties.elementAt(i);

        }
        return xattribute;
    }

    public XElement(XElement xelement, String s)
    {
        _strDateCreated = null;
        _strName = null;
        _vectorChildEntries = null;
        _vectorProperties = null;
        _entryParent = null;
        _entryParent = xelement;
        _strName = s;
    }

    public void addAttribute(XAttribute xattribute)
    {
        if(_vectorProperties == null)
            _vectorProperties = new Vector();
        _vectorProperties.add(xattribute);
    }

    public void addChildElement(XElement xelement)
    {
        if(_vectorChildEntries == null)
            _vectorChildEntries = new Vector();
        _vectorChildEntries.add(xelement);
    }

    public String getAttributeValue(String s)
    {
        String s1 = null;
        if(null != _vectorProperties)
        {
            for(int i = 0; null == s1 && i < _vectorProperties.size(); i++)
                if(((XAttribute)_vectorProperties.elementAt(i)).getName().equals(s))
                    s1 = ((XAttribute)_vectorProperties.elementAt(i)).getValue();

        }
        return s1;
    }

    public XAttribute[] getAttributes()
    {
        if(_vectorProperties == null)
            return new XAttribute[0];
        XAttribute axattribute[] = new XAttribute[_vectorProperties.size()];
        for(int i = 0; i < _vectorProperties.size(); i++)
            axattribute[i] = (XAttribute)_vectorProperties.elementAt(i);

        return axattribute;
    }

    public XElement getChildElement(String s)
    {
        if(_vectorChildEntries != null)
        {
            for(int i = 0; i < _vectorChildEntries.size(); i++)
                if(((XElement)_vectorChildEntries.elementAt(i)).getName().equals(s))
                    return (XElement)_vectorChildEntries.elementAt(i);

        }
        return null;
    }

    public XElement[] getChildElements()
    {
        if(_vectorChildEntries == null)
            return new XElement[0];
        XElement axelement[] = new XElement[_vectorChildEntries.size()];
        for(int i = 0; i < _vectorChildEntries.size(); i++)
            axelement[i] = (XElement)_vectorChildEntries.elementAt(i);

        return axelement;
    }

    public Vector getChildElementsVector()
    {
        Vector vector = _vectorChildEntries;
        return vector;
    }

    public boolean hasChildElements()
    {
        return null != _vectorChildEntries;
    }

    public Vector getChildElementsVector(String s)
    {
        Vector vector = null;
        if(null != _vectorChildEntries)
        {
            vector = new Vector(20, 100);
            for(int i = 0; i < _vectorChildEntries.size(); i++)
            {
                XElement xelement = (XElement)_vectorChildEntries.elementAt(i);
                if(xelement.getName().equals(s))
                    vector.add(xelement);
            }

        }
        return vector;
    }

    public XElement[] getChildElements(String s)
    {
        XElement axelement[] = null;
        if(_vectorChildEntries == null)
        {
            axelement = new XElement[0];
        } else
        {
            Vector vector = getChildElementsVector(s);
            axelement = new XElement[vector.size()];
            axelement = (XElement[])vector.toArray(axelement);
        }
        return axelement;
    }

    public String getName()
    {
        return _strName != null ? _strName : _strEmpty;
    }

    public int getNumberOfAttributes()
    {
        return _vectorProperties != null ? _vectorProperties.size() : 0;
    }

    public int getNumberOfChildElements()
    {
        return _vectorChildEntries != null ? _vectorChildEntries.size() : 0;
    }

    public XElement getParentElement()
    {
        return _entryParent;
    }

    public void printElementTree(int i)
    {
        StringBuffer stringbuffer = new StringBuffer();
        for(int j = 0; j < i; j++)
            stringbuffer.append(' ');

        if(_strName != null)
            stringbuffer.append("<" + _strName + ">");
        else
            stringbuffer.append("<>");
        System.out.println(stringbuffer.toString());
        if(_vectorProperties != null)
        {
            for(int k = 0; k < _vectorProperties.size(); k++)
                ((XAttribute)_vectorProperties.elementAt(k)).printAttribute(i + 4);

        }
        if(_vectorChildEntries != null)
        {
            for(int l = 0; l < _vectorChildEntries.size(); l++)
                ((XElement)_vectorChildEntries.elementAt(l)).printElementTree(i + 4);

        }
    }

    public void printPersistentElementString(StringBuffer stringbuffer, int i)
    {
        printPersistentElementStringIndentation(stringbuffer, i);
        stringbuffer.append('<');
        printPersistentElementStringName(stringbuffer);
        Object obj = null;
        if(_vectorProperties != null && _vectorProperties.size() > 0)
        {
            for(int j = 0; j < _vectorProperties.size(); j++)
            {
                XAttribute xattribute = (XAttribute)_vectorProperties.elementAt(j);
                stringbuffer.append(' ');
                xattribute.printAttributeString(stringbuffer);
            }

        }
        if(_vectorChildEntries != null && _vectorChildEntries.size() > 0)
        {
            stringbuffer.append(">\r\n");
            if(_vectorChildEntries != null)
            {
                for(int k = 0; k < _vectorChildEntries.size(); k++)
                    ((XElement)_vectorChildEntries.elementAt(k)).printPersistentElementString(stringbuffer, i + 4);

            }
            printPersistentElementStringIndentation(stringbuffer, i);
            stringbuffer.append("</");
            printPersistentElementStringName(stringbuffer);
            stringbuffer.append(">\r\n");
        } else
        {
            stringbuffer.append(" />\r\n");
        }
    }

    protected void printPersistentElementStringIndentation(StringBuffer stringbuffer, int i)
    {
        for(int j = 0; j < i; j++)
            stringbuffer.append(' ');

    }

    public void printPersistentElementStringName(StringBuffer stringbuffer)
    {
        stringbuffer.append(_strName == null ? "undefined" : _strName);
    }

    public void removeAllAttributes()
    {
        if(_vectorProperties != null)
            _vectorProperties.removeAllElements();
    }

    public void removeAllChildElements()
    {
        if(_vectorChildEntries != null)
            _vectorChildEntries.removeAllElements();
    }

    public boolean removeAttribute(String s)
    {
        if(_vectorProperties != null)
            return _vectorProperties.removeElement(s);
        else
            return false;
    }

    public boolean removeChildElements(IEntry ientry)
    {
        if(_vectorChildEntries != null)
            return _vectorChildEntries.removeElement(ientry);
        else
            return false;
    }

    public static final String copyright = "(c) Copyright IBM Corporation 2002. all rights reserved";
    protected static final String _strEmpty = new String();
    protected String _strDateCreated;
    protected String _strName;
    protected Vector _vectorChildEntries;
    protected Vector _vectorProperties;
    protected XElement _entryParent;

}
