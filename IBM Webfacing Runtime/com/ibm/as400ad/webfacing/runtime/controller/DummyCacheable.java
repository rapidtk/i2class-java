// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.controller;

import com.ibm.as400ad.webfacing.runtime.model.def.IRecordDataDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordFeedbackDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordViewDefinition;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.controller:
//            ICacheable

public class DummyCacheable
    implements ICacheable
{

    public DummyCacheable()
    {
        _previous = null;
        _next = null;
        hitCount = 0L;
    }

    public String getRecordClassName()
    {
        return null;
    }

    public IRecordDataDefinition getDataDefinition()
    {
        return null;
    }

    public void setDataDefinition(IRecordDataDefinition irecorddatadefinition)
    {
    }

    public IRecordFeedbackDefinition getFeedbackDefinition()
    {
        return null;
    }

    public void setFeedbackDefinition(IRecordFeedbackDefinition irecordfeedbackdefinition)
    {
    }

    public IRecordViewDefinition getViewDefinition()
    {
        return null;
    }

    public void setViewDefinition(IRecordViewDefinition irecordviewdefinition)
    {
    }

    public ICacheable getPrevious()
    {
        return _previous;
    }

    public void setPrevious(ICacheable icacheable)
    {
        _previous = icacheable;
    }

    public ICacheable getNext()
    {
        return _next;
    }

    public void setNext(ICacheable icacheable)
    {
        _next = icacheable;
    }

    public void logHit()
    {
        hitCount++;
    }

    public void loseHit()
    {
        hitCount--;
    }

    public long getHitCount()
    {
        return hitCount;
    }

    public void resetHitCount()
    {
        hitCount = 0L;
    }

    private ICacheable _previous;
    private ICacheable _next;
    private long hitCount;
}
