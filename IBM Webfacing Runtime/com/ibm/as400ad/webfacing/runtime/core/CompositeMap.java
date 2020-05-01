// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.core;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.core:
//            Element, IKey, CollectionFactory

class CompositeMap
    implements Serializable
{

    public CompositeMap()
    {
        collections = new Hashtable();
    }

    public void add(Element element)
    {
        add(element, element.getClass());
    }

    public void add(Element element, Object obj)
    {
        add(element, element.getKey().getId(), obj);
    }

    public void add(Object obj, Object obj1)
    {
        add(obj, obj1, obj.getClass());
    }

    public void add(Object obj, Object obj1, Object obj2)
    {
        getMap(obj2).put(obj1, obj);
    }

    public Map findMap(Object obj)
    {
        return (Map)collections.get(obj);
    }

    public Object get(int i, Object obj)
    {
        return get(Integer.toString(i), obj);
    }

    public Object get(Object obj, Object obj1)
    {
        return getMap(obj1).get(obj);
    }

    public Collection getCriteria()
    {
        Vector vector = new Vector();
        for(Enumeration enumeration = collections.keys(); enumeration.hasMoreElements(); vector.add(enumeration.nextElement()));
        return vector;
    }

    public Map getMap(Object obj)
    {
        Map map = (Map)collections.get(obj);
        if(map == null)
        {
            map = CollectionFactory.createMap(obj);
            collections.put(obj, map);
        }
        return map;
    }

    public Collection getValues(Object obj)
    {
        return getMap(obj).values();
    }

    public void removeCollections()
    {
        collections.clear();
    }

    public void removeCriteria(Object obj)
    {
        collections.remove(obj);
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2000, 2002.  All Rights Reserved.";
    private Hashtable collections;
}
