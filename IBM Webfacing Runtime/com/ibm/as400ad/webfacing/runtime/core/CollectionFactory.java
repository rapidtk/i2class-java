// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.core;

import java.lang.reflect.Method;
import java.util.*;

public class CollectionFactory
{

    public CollectionFactory()
    {
    }

    public static Collection createCollection(Object obj)
    {
        if(obj != null)
        {
            Collection collection = (Collection)collectionPrototypes.get(obj);
            if(collection != null && (collection instanceof Cloneable))
                try
                {
                    Class class1 = collection.getClass();
                    Method method = class1.getMethod("clone", new Class[0]);
                    return (Collection)method.invoke(collection, new Object[0]);
                }
                catch(Exception exception)
                {
                    return null;
                }
        }
        return new Vector();
    }

    public static Map createMap(Object obj)
    {
        if(obj != null)
        {
            Map map = (Map)mapPrototypes.get(obj);
            if(map != null && (map instanceof Cloneable))
                try
                {
                    Class class1 = map.getClass();
                    Method method = class1.getMethod("clone", new Class[0]);
                    return (Map)method.invoke(map, new Object[0]);
                }
                catch(Exception exception)
                {
                    return null;
                }
        }
        return new Hashtable();
    }

    public static void setPrototype(Object obj, Collection collection)
    {
        collectionPrototypes.put(obj, collection);
    }

    public static void setPrototype(Object obj, Map map)
    {
        mapPrototypes.put(obj, map);
    }

    private static Hashtable collectionPrototypes = new Hashtable();
    private static Hashtable mapPrototypes = new Hashtable();

}
