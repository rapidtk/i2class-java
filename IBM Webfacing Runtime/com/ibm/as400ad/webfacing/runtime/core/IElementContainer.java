// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.core;

import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.core:
//            IDefinitionElement, IElement, IKey, ITraverser

public interface IElementContainer
    extends IDefinitionElement
{

    public abstract void add(IElement ielement);

    public abstract void add(IElement ielement, Object obj);

    public abstract void add(Object obj);

    public abstract void add(Object obj, IKey ikey);

    public abstract void add(Object obj, IKey ikey, Object obj1);

    public abstract void add(Object obj, Object obj1);

    public abstract Object get(int i, Object obj);

    public abstract Object get(IKey ikey, Object obj);

    public abstract Object get(String s, Object obj);

    public abstract Collection getChildrenCriteria();

    public abstract Collection getCollection(Object obj);

    public abstract Collection getList(Object obj);

    public abstract Map getMap(Object obj);

    public abstract ITraverser getTraverser();

    public abstract Iterator iterator(Object obj);

    public abstract Object remove(IElement ielement, Object obj);

    public abstract Object remove(IKey ikey, Object obj);

    public abstract void remove(Object obj);

    public abstract void removeAllElements();

    public abstract void removeCriteria(Object obj);
}
