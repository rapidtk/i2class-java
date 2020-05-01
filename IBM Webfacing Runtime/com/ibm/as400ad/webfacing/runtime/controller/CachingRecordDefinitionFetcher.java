// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.controller;

import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingLevelCheckException;
import com.ibm.as400ad.webfacing.runtime.model.def.IRecordDataDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordFeedbackDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordViewDefinition;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.controller:
//            RecordDefinitionFetcher, DummyCacheable, ICacheable, WFSession

public class CachingRecordDefinitionFetcher extends RecordDefinitionFetcher
{

    public CachingRecordDefinitionFetcher(int i)
    {
        _cache = new HashMap();
        _head = new DummyCacheable();
        _tail = new DummyCacheable();
        _cacheLimit = i;
        clearCache();
    }

    public IRecordDataDefinition requestDataDefinition(String s)
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        IRecordDataDefinition irecorddatadefinition = null;
        ICacheable icacheable = requestFromCache(s);
        if(icacheable != null)
            irecorddatadefinition = icacheable.getDataDefinition();
        return irecorddatadefinition;
    }

    public IRecordViewDefinition requestViewDefinition(String s)
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        IRecordViewDefinition irecordviewdefinition = null;
        ICacheable icacheable = requestFromCache(s);
        if(icacheable != null)
            irecordviewdefinition = icacheable.getViewDefinition();
        return irecordviewdefinition;
    }

    public IRecordFeedbackDefinition requestFeedbackDefinition(String s)
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        IRecordFeedbackDefinition irecordfeedbackdefinition = null;
        ICacheable icacheable = requestFromCache(s);
        if(icacheable != null)
            irecordfeedbackdefinition = icacheable.getFeedbackDefinition();
        return irecordfeedbackdefinition;
    }

    public String checkQualifiedPath(String s, String s1)
    {
        for(Iterator iterator = RecordDefinitionFetcher.getPathTokens(s); iterator.hasNext();)
        {
            String s2 = (String)iterator.next();
            String s3 = s2 + s1;
            s3 = s3.replace('/', '.');
            try
            {
                ICacheable icacheable = requestFromCache(s3);
                if(icacheable != null)
                {
                    icacheable.loseHit();
                    _head.loseHit();
                    return s2;
                }
            }
            catch(Throwable throwable) { }
        }

        return null;
    }

    private ICacheable requestFromCache(String s)
        throws WebfacingLevelCheckException, WebfacingInternalException
    {
        Object obj = null;
        obj = getput(null, s);
        if(null == obj)
        {
            obj = loadXMLDefinition(s);
            if(null == obj)
            {
                obj = (IRecordDataDefinition)loadJavaDefinition(s + "Data");
                if(obj != null)
                {
                    ((IRecordDataDefinition)obj).setRecordClassName(s);
                    try
                    {
                        ((ICacheable) (obj)).setViewDefinition((IRecordViewDefinition)loadJavaDefinition(s + "View"));
                        ((ICacheable) (obj)).setFeedbackDefinition((IRecordFeedbackDefinition)loadJavaDefinition(s + "Feedback"));
                    }
                    catch(Throwable throwable) { }
                }
            }
            if(obj != null)
                getput(((ICacheable) (obj)), s);
            else
                WFSession.getTraceLogger().err(1, "ERROR loading record definition " + s);
        }
        return ((ICacheable) (obj));
    }

    public void clearCache()
    {
        for(ICacheable icacheable = _head.getNext(); null != icacheable && icacheable != _tail; icacheable = _head.getNext())
            remove(icacheable);

        _head.setNext(_tail);
        _tail.setPrevious(_head);
        _cache.clear();
    }

    public ICacheable getCacheHead()
    {
        return _head;
    }

    public void setCacheLimit(int i)
    {
        boolean flag = false;
        if(i < _cacheLimit)
            flag = true;
        _cacheLimit = i;
        if(flag)
            clearCache();
    }

    public int getCacheLimit()
    {
        return _cacheLimit;
    }

    private void chain(ICacheable icacheable)
    {
        if(null != icacheable)
        {
            ICacheable icacheable1 = _tail.getPrevious();
            _tail.setPrevious(icacheable);
            icacheable1.setNext(icacheable);
            icacheable.setNext(_tail);
            icacheable.setPrevious(icacheable1);
        }
    }

    private void dechain(ICacheable icacheable)
    {
        if(null != icacheable)
        {
            ICacheable icacheable1 = icacheable.getPrevious();
            ICacheable icacheable2 = icacheable.getNext();
            if(null != icacheable1 && null != icacheable2)
            {
                icacheable1.setNext(icacheable2);
                icacheable2.setPrevious(icacheable1);
            } else
            if(null != icacheable1)
                if(null != icacheable2);
            icacheable.setNext(null);
            icacheable.setPrevious(null);
        }
    }

    private void remove(ICacheable icacheable)
    {
        if(null != icacheable)
        {
            _cache.remove(icacheable);
            dechain(icacheable);
        }
    }

    private synchronized ICacheable getput(ICacheable icacheable, String s)
    {
        ICacheable icacheable1 = icacheable;
        ICacheable icacheable2 = (ICacheable)_cache.get(s);
        if(null != icacheable2)
        {
            dechain(icacheable2);
            icacheable1 = icacheable2;
            icacheable1.logHit();
            _head.logHit();
        } else
        if(null != icacheable1)
        {
            _cache.put(s, icacheable1);
            if(_cache.size() > _cacheLimit)
            {
                ICacheable icacheable3 = _head.getNext();
                remove(icacheable3);
            }
        } else
        {
            _tail.logHit();
        }
        if(null != icacheable1)
            chain(icacheable1);
        return icacheable1;
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2003, all rights reserved.");
    private int _cacheLimit;
    private Map _cache;
    private ICacheable _head;
    private ICacheable _tail;

}
