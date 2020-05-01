// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.help;

import java.io.Serializable;
import java.util.*;

public class HelpTable
    implements Cloneable, Serializable
{

    public HelpTable()
    {
        _table = null;
        _table = new HashMap();
    }

    public void add(Object obj, Object obj1)
    {
        add(obj, obj1, -1);
    }

    public void add(Object obj, Object obj1, int i)
    {
        Object obj3 = get(obj);
        Object obj2;
        if(obj3 != null)
            obj2 = (List)obj3;
        else
            obj2 = new ArrayList();
        if(i < 0)
        {
            ((List) (obj2)).add(obj1);
        } else
        {
            for(; ((List) (obj2)).size() <= i; ((List) (obj2)).add(((List) (obj2)).size(), DUMMY));
            ((List) (obj2)).set(i, obj1);
        }
        _table.put(obj, obj2);
    }

    public void addWithoutDuplicates(Object obj, Object obj1)
    {
        addWithoutDuplicates(obj, obj1, -1);
    }

    public void addWithoutDuplicates(Object obj, Object obj1, int i)
    {
        if(_table.containsKey(obj) && ((List)get(obj)).contains(obj1))
        {
            return;
        } else
        {
            add(obj, obj1, i);
            return;
        }
    }

    public void clear()
    {
        _table.clear();
    }

    public Object clone()
    {
        HelpTable helptable = null;
        try
        {
            helptable = (HelpTable)super.clone();
            helptable._table = (HashMap)_table.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        return helptable;
    }

    public Object get(Object obj)
    {
        return _table.get(obj);
    }

    public Object get(Object obj, int i)
    {
        Object obj1 = get(obj);
        if(obj1 != null)
            try
            {
                Object obj2 = ((List)obj1).get(i);
                if(obj2 == DUMMY)
                    return null;
                else
                    return obj2;
            }
            catch(IndexOutOfBoundsException indexoutofboundsexception)
            {
                return null;
            }
        else
            return null;
    }

    public Map getHelpTable()
    {
        return _table;
    }

    public List getList(Object obj)
    {
        List list = (List)_table.get(obj);
        if(list == null)
            return null;
        for(; list.contains(DUMMY); list.remove(DUMMY));
        return list;
    }

    public ListIterator getListAsIterator(Object obj)
    {
        List list = getList(obj);
        if(list == null)
            return null;
        else
            return list.listIterator();
    }

    public boolean isEmpty()
    {
        return _table.isEmpty();
    }

    public void remove(Object obj)
    {
        _table.remove(obj);
    }

    public void remove(Object obj, int i)
    {
        Object obj1 = get(obj);
        if(obj1 != null)
            ((List)obj1).set(i, DUMMY);
    }

    public void remove(Object obj, Object obj1)
    {
        Object obj2 = get(obj);
        if(obj2 != null)
            ((List)obj2).remove(obj1);
    }

    public String toString()
    {
        return _table.toString();
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2001, 2002, all rights reserved");
    private HashMap _table;
    private static final Object DUMMY = new String();

}
