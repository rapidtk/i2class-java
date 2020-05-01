// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.util;

import java.io.File;
import java.util.*;

public class HashedList
{
    public static class FileSorterByTime
        implements Comparator
    {

        public int compare(Object obj, Object obj1)
        {
            long l = ((File)obj).lastModified();
            long l1 = ((File)obj1).lastModified();
            return l <= l1 ? l >= l1 ? 0 : -1 : 1;
        }

        public boolean equals(Object obj)
        {
            return obj.equals(this);
        }

        public FileSorterByTime()
        {
        }
    }


    public HashedList()
    {
        _linkedList = null;
        _hashTable = null;
        _fileSorter = null;
        _linkedList = new LinkedList();
        _hashTable = new Hashtable();
        _fileSorter = new FileSorterByTime();
    }

    public void add(Object obj)
    {
        _hashTable.put(obj, obj);
        _linkedList.add(obj);
    }

    public void get()
    {
    }

    public Object get(int i)
    {
        if(i >= 0 && i <= size())
            return _linkedList.get(i);
        else
            return null;
    }

    public FileSorterByTime getFileSorter()
    {
        return _fileSorter;
    }

    public boolean hasElement(Object obj)
    {
        if(_hashTable != null)
            return _hashTable.containsKey(obj);
        else
            return false;
    }

    public Object removeFirst()
    {
        Object obj = null;
        if(!_linkedList.isEmpty())
        {
            obj = _linkedList.removeFirst();
            _hashTable.remove(obj);
        }
        return obj;
    }

    public int size()
    {
        if(_linkedList != null)
            return _linkedList.size();
        else
            return 0;
    }

    private LinkedList _linkedList;
    private Hashtable _hashTable;
    private FileSorterByTime _fileSorter;
}
