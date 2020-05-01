// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.code400.designer.io;

import java.util.*;

// Referenced classes of package com.ibm.as400ad.code400.designer.io:
//            IOutputCollection

public class OutputCollection
    implements IOutputCollection
{

    public OutputCollection()
    {
        _v = null;
        _v = new Vector();
    }

    public void addAll(OutputCollection outputcollection)
    {
        for(Enumeration enumeration = outputcollection._v.elements(); enumeration.hasMoreElements();)
        {
            Object obj = enumeration.nextElement();
            if(null != obj)
                _v.addElement(obj);
        }

    }

    public void addElement(Object obj)
    {
        if(null != _v)
            _v.addElement(obj);
    }

    public Enumeration elements()
    {
        Enumeration enumeration = null;
        if(_v != null)
            enumeration = _v.elements();
        return enumeration;
    }

    public String getNewline()
    {
        return System.getProperty("line.separator");
    }

    public boolean newline()
    {
        return print(getNewline());
    }

    public boolean print(String s)
    {
        return false;
    }

    public boolean println(String s)
    {
        boolean flag = false;
        flag |= print(s);
        flag |= newline();
        return flag;
    }

    public int size()
    {
        int i = 0;
        if(null != _v)
            i = _v.size();
        return i;
    }

    public String toString()
    {
        String s = "";
        if(null != _v && _v.size() > 0)
        {
            Enumeration enumeration = _v.elements();
            Object obj = enumeration.nextElement();
            if(null != obj)
                s = obj.toString();
            while(enumeration.hasMoreElements()) 
            {
                Object obj1 = enumeration.nextElement();
                if(null != obj1)
                    s = s + " " + obj1.toString();
            }
        }
        return s;
    }

    public void removeElement(Object obj)
    {
        if(null != _v)
            _v.removeElement(obj);
    }

    public void addElementAtBeginning(Hashtable hashtable)
    {
        if(hashtable != null)
        {
            Enumeration enumeration = hashtable.elements();
            String s;
            for(s = ""; enumeration.hasMoreElements(); s = s + (String)enumeration.nextElement());
            if(_v.size() > 0)
                _v.add(0, s);
            else
                _v.add(s);
        }
    }

    static final String copyRight = new String(" (C) Copyright IBM Corporation 1999, 2000");
    private Vector _v;

}
