// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.help;

import java.io.Serializable;
import java.util.*;

public class HelpList
    implements Cloneable, Serializable
{

    public HelpList()
    {
        _list = null;
        _list = new ArrayList();
    }

    public void addFirstToSublist(List list, int i)
    {
        try
        {
            LinkedList linkedlist = (LinkedList)_list.get(i);
            if(linkedlist == null)
                return;
            for(int j = 0; j < list.size(); j++)
                linkedlist.add(j, list.get(j));

        }
        catch(IndexOutOfBoundsException indexoutofboundsexception)
        {
            return;
        }
    }

    public void addSublistToList(List list)
    {
        _list.add(list);
    }

    public void addSublistToList(List list, int i)
    {
        _list.add(i, list);
    }

    public void addToList(List list)
    {
        for(int i = 0; i < list.size(); i++)
        {
            List list1 = (List)list.get(i);
            addSublistToList(list1, i);
        }

    }

    public void addToSublist(List list, int i)
    {
        try
        {
            LinkedList linkedlist = (LinkedList)_list.get(i);
            if(linkedlist == null)
                return;
            for(int j = 0; j < list.size(); j++)
                linkedlist.add(list.get(j));

        }
        catch(IndexOutOfBoundsException indexoutofboundsexception)
        {
            return;
        }
    }

    public void clear()
    {
        _list.clear();
    }

    public Object clone()
    {
        HelpList helplist = null;
        try
        {
            helplist = (HelpList)super.clone();
            helplist._list = (ArrayList)_list.clone();
            for(int i = 0; i < _list.size(); i++)
                helplist.setSublist(i, cloneSublist(i));

        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        return helplist;
    }

    public List cloneSublist(int i)
    {
        LinkedList linkedlist = (LinkedList)_list.get(i);
        return (List)linkedlist.clone();
    }

    public List getList()
    {
        return _list;
    }

    public Iterator getListAsIterator()
    {
        return getList().iterator();
    }

    public List getSublist(int i)
    {
        Object obj = _list.get(i);
        if(obj instanceof List)
            return (List)obj;
        else
            return null;
    }

    public Iterator getSublistAsIterator(int i)
    {
        List list = getSublist(i);
        if(list != null)
            return ((List)list).iterator();
        else
            return null;
    }

    public boolean isEmpty()
    {
        return _list.isEmpty();
    }

    public void setSublist(int i, List list)
    {
        try
        {
            _list.set(i, list);
        }
        catch(IndexOutOfBoundsException indexoutofboundsexception)
        {
            return;
        }
    }

    public String toString()
    {
        String s = new String();
        Iterator iterator = getListAsIterator();
        Object obj = null;
        while(iterator.hasNext()) 
        {
            s = s + "[ ";
            List list = (List)iterator.next();
            for(Iterator iterator1 = list.iterator(); iterator1.hasNext();)
            {
                s = s + "[";
                s = s + iterator1.next().toString();
                s = s + "] ";
            }

            s = s + " ]";
        }
        return s;
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2001, 2002, all rights reserved");
    private ArrayList _list;

}
