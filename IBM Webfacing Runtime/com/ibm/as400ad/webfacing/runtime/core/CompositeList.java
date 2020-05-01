// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.core;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.core:
//            CollectionFactory

class CompositeList
    implements Serializable
{

    CompositeList()
    {
        collections = new Hashtable();
    }

    public void add(Object obj)
    {
        add(obj, obj.getClass());
    }

    public void add(Object obj, Object obj1)
    {
        getCollection(obj1).add(obj);
    }

    public Collection findList(Object obj)
    {
        return (Collection)collections.get(obj);
    }

    public Collection getCollection(Object obj)
    {
        Collection collection = (Collection)collections.get(obj);
        if(collection == null)
        {
            collection = CollectionFactory.createCollection(obj);
            collections.put(obj, collection);
        }
        return collection;
    }

    public Collection getCriteria()
    {
        Vector vector = new Vector();
        for(Enumeration enumeration = collections.keys(); enumeration.hasMoreElements(); vector.add(enumeration.nextElement()));
        return vector;
    }

    public Iterator iterator(Object obj)
    {
        Collection collection = (Collection)collections.get(obj);
        if(collection != null)
            return collection.iterator();
        else
            return null;
    }

    public boolean remove(Object obj)
    {
        return getCollection(obj.getClass()).remove(obj);
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
