// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.dhtml;

import com.ibm.as400ad.code400.designer.io.OutputCollection;
import com.ibm.as400ad.code400.designer.io.SourceCodeCollection;
import java.util.Enumeration;

public class ClientScriptSourceCodeCollection extends SourceCodeCollection
{
    public class Attribute
    {

        public String getAttribute()
        {
            return _attribute;
        }

        public String getFieldObject()
        {
            return _fieldObject;
        }

        public String getValue()
        {
            return _value;
        }

        public String toString()
        {
            StringBuffer stringbuffer = new StringBuffer(_fieldObject);
            stringbuffer.append(".");
            stringbuffer.append(_attribute);
            stringbuffer.append("=");
            stringbuffer.append(_value);
            stringbuffer.append(";");
            stringbuffer.append(getNewline());
            return stringbuffer.toString();
        }

        private String _attribute;
        private String _value;
        private String _fieldObject;

        public Attribute(String s, String s1, String s2)
        {
            _attribute = null;
            _value = null;
            _fieldObject = null;
            _fieldObject = s;
            _attribute = s1;
            _value = s2;
        }
    }


    public ClientScriptSourceCodeCollection()
    {
        _fieldObject = null;
    }

    private void addAttribute(Attribute attribute)
    {
        addAttribute(attribute, true);
    }

    private void addAttribute(Attribute attribute, boolean flag)
    {
        if(flag)
            removeAttribute(attribute.getAttribute());
        addElement(attribute);
    }

    public void addAttribute(String s, char c)
    {
        addAttribute(new Attribute("fR", s, "\"" + c + "\""));
    }

    public void addAttribute(String s, int i)
    {
        addAttribute(new Attribute("fR", s, "\"" + i + "\""));
    }

    public void addAttribute(String s, String s1)
    {
        if(s1 == null)
            addAttribute(new Attribute("fR", s, "null"));
        else
            addAttribute(new Attribute("fR", s, "\"" + s1 + "\""));
    }

    public void addAttribute(String s, boolean flag)
    {
        addAttribute(new Attribute("fR", s, "" + flag));
    }

    public void addAttribute(String s, boolean flag, boolean flag1)
    {
        addAttribute(new Attribute("fR", s, "" + flag), flag1);
    }

    public void removeAttribute(String s)
    {
        Enumeration enumeration = elements();
        boolean flag = false;
        if(enumeration != null)
        {
            Object obj = null;
            while(enumeration.hasMoreElements() && !flag) 
            {
                obj = enumeration.nextElement();
                if(obj instanceof Attribute)
                    flag = s.equals(((Attribute)obj).getAttribute());
            }
            if(flag)
                removeElement(obj);
        }
    }

    public String removeElementWithPrefix(String s)
    {
        Enumeration enumeration = elements();
        if(enumeration != null)
        {
            Object obj = null;
            while(enumeration.hasMoreElements()) 
            {
                Object obj1 = enumeration.nextElement();
                if(obj1 instanceof String)
                {
                    String s1 = (String)obj1;
                    if(s1.startsWith(s))
                    {
                        removeElement(obj1);
                        return s1;
                    }
                }
            }
        }
        return null;
    }

    public void setCurrentField(String s)
    {
        setFieldObject("document.SCREEN." + s);
    }

    public void setCurrentField(String s, String s1)
    {
        setFieldObject(s + s1);
    }

    private void setFieldObject(String s)
    {
        if(!s.equals(_fieldObject))
        {
            _fieldObject = s;
            addElement("fR=" + _fieldObject + ";" + getNewline());
        }
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999-2002, all rights reserved");
    private String _fieldObject;
    static final String FIELD_REFERENCE_NAME = "fR";

}
