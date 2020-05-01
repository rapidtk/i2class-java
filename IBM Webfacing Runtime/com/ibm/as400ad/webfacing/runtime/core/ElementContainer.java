// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.core;

import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.core:
//            Element, IListable, IListOnly, CompositeList, 
//            CompositeMap, IElement, IElementContainer, IKey, 
//            ITraverser, IAction

public class ElementContainer extends Element
    implements IElementContainer
{

    public ElementContainer()
    {
        super("ElementContainer");
        map = null;
        list = null;
    }

    public ElementContainer(String s)
    {
        super(s);
        map = null;
        list = null;
    }

    public void add(IElement ielement)
    {
        add(ielement, ielement.getClass());
    }

    public void add(IElement ielement, Object obj)
    {
        if(!addToListOnlyHelper(ielement, obj))
            addHelper(ielement, ielement.getKey().getId(), obj);
    }

    public void add(Object obj)
    {
        addToListHelper(obj, obj.getClass());
    }

    public void add(Object obj, IKey ikey)
    {
        Class class1 = obj.getClass();
        if(!addToListOnlyHelper(obj, class1))
            addHelper(obj, ikey.getId(), class1);
    }

    public void add(Object obj, IKey ikey, Object obj1)
    {
        if(!addToListOnlyHelper(obj, obj1))
            addHelper(obj, ikey.getId(), obj1);
    }

    public void add(Object obj, Object obj1)
    {
        addToListHelper(obj, obj1);
    }

    private void addHelper(Object obj, String s, Object obj1)
    {
        addToMapHelper(obj, s, obj1);
        if(obj instanceof IListable)
            addToListHelper(obj, obj1);
    }

    private void addToListHelper(Object obj, Object obj1)
    {
        getCompositeList().add(obj, obj1);
    }

    private boolean addToListOnlyHelper(Object obj, Object obj1)
    {
        if(obj instanceof IListOnly)
        {
            addToListHelper(obj, obj1);
            return true;
        } else
        {
            return false;
        }
    }

    private void addToMapHelper(Object obj, String s, Object obj1)
    {
        getCompositeMap().add(obj, s, obj1);
    }

    public Object get(int i, Object obj)
    {
        return getHelper(Integer.toString(i), obj);
    }

    public Object get(IKey ikey, Object obj)
    {
        return getHelper(ikey.getId(), obj);
    }

    public Object get(String s, Object obj)
    {
        return getHelper(s, obj);
    }

    public Collection getChildrenCriteria()
    {
        Object obj = null;
        if(list != null)
            obj = list.getCriteria();
        if(map != null)
        {
            Collection collection = map.getCriteria();
            for(Iterator iterator1 = collection.iterator(); iterator1.hasNext();)
            {
                Object obj1 = iterator1.next();
                if(!((Collection) (obj)).contains(obj1))
                    ((Collection) (obj)).add(obj1);
            }

        }
        if(obj == null)
            obj = new Vector();
        return ((Collection) (obj));
    }

    public Collection getCollection(Object obj)
    {
        Vector vector = new Vector();
        Object obj1 = null;
        if(list != null)
        {
            Collection collection = list.findList(obj);
            if(collection != null)
            {
                for(Iterator iterator1 = collection.iterator(); iterator1.hasNext();)
                {
                    Object obj2 = iterator1.next();
                    if(!vector.contains(obj2))
                        vector.add(obj2);
                }

            }
        }
        if(map != null)
        {
            Collection collection1 = map.getValues(obj);
            if(collection1 != null)
            {
                for(Iterator iterator2 = collection1.iterator(); iterator2.hasNext();)
                {
                    Object obj3 = iterator2.next();
                    if(!vector.contains(obj3))
                        vector.add(obj3);
                }

            }
        }
        return vector;
    }

    protected CompositeList getCompositeList()
    {
        if(list == null)
            list = new CompositeList();
        return list;
    }

    protected CompositeMap getCompositeMap()
    {
        if(map == null)
            map = new CompositeMap();
        return map;
    }

    private Object getHelper(Object obj, Object obj1)
    {
        return getCompositeMap().get(obj, obj1);
    }

    public Collection getList(Object obj)
    {
        return getCompositeList().getCollection(obj);
    }

    public Map getMap(Object obj)
    {
        return getCompositeMap().getMap(obj);
    }

    public ITraverser getTraverser()
    {
        return new ITraverser() {

            public void forAllDo(IAction iaction)
                throws Exception
            {
                for(; hasNext(); iaction.perform(next()));
            }

            public boolean hasNext()
            {
                if(objectIterator != null && objectIterator.hasNext())
                    return true;
                while(criteriaIterator.hasNext()) 
                {
                    objectIterator = getCollection(criteriaIterator.next()).iterator();
                    if(objectIterator.hasNext())
                        return true;
                }
                objectIterator = null;
                return false;
            }

            public Object next()
            {
                return objectIterator != null ? objectIterator.next() : null;
            }

            public void reset()
            {
                criteriaIterator = criteria.iterator();
                objectIterator = null;
            }

            Collection criteria;
            Iterator criteriaIterator;
            Iterator objectIterator;

            
            {
                criteria = null;
                criteriaIterator = null;
                objectIterator = null;
                criteria = getChildrenCriteria();
                reset();
            }
        };
    }

    public Iterator iterator(Object obj)
    {
        return getCollection(obj).iterator();
    }

    public Object remove(IElement ielement, Object obj)
    {
        return remove(ielement.getKey(), obj);
    }

    public Object remove(IKey ikey, Object obj)
    {
        Object obj1 = getMap(obj).remove(ikey.getId());
        if(null != obj1 && (obj1 instanceof IListable))
            getList(obj).remove(obj1);
        return obj1;
    }

    public void remove(Object obj)
    {
        Class class1 = obj.getClass();
        if(obj instanceof IListOnly)
        {
            getList(class1).remove(obj);
        } else
        {
            if(obj instanceof IListable)
                getList(class1).remove(obj);
            if(obj instanceof IElement)
                getMap(class1).remove(((IElement)obj).getKey().getId());
        }
    }

    public void removeAllElements()
    {
        if(list != null)
            list.removeCollections();
        if(map != null)
            map.removeCollections();
    }

    public void removeCriteria(Object obj)
    {
        if(list != null)
            list.removeCriteria(obj);
        if(map != null)
            map.removeCriteria(obj);
    }

    private CompositeMap map;
    private CompositeList list;
}
