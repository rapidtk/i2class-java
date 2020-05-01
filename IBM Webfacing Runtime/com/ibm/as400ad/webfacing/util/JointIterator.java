// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.util;

import java.util.Iterator;

public class JointIterator
    implements Iterator
{

    public JointIterator(Iterator iterator, Iterator iterator1)
    {
        _a = iterator;
        _b = iterator1;
        _aIteratorUsedLast = true;
    }

    public boolean hasNext()
    {
        return _a.hasNext() || _b.hasNext();
    }

    public Object next()
    {
        if(_a.hasNext())
        {
            return _a.next();
        } else
        {
            _aIteratorUsedLast = false;
            return _b.next();
        }
    }

    public void remove()
    {
        if(_aIteratorUsedLast)
            _a.remove();
        else
            _b.remove();
    }

    Iterator _a;
    Iterator _b;
    boolean _aIteratorUsedLast;
}
