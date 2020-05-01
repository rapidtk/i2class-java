// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.controller;

import com.ibm.as400ad.webfacing.runtime.model.def.IRecordDataDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordFeedbackDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordViewDefinition;

public interface ICacheable
{

    public abstract String getRecordClassName();

    public abstract IRecordDataDefinition getDataDefinition();

    public abstract void setDataDefinition(IRecordDataDefinition irecorddatadefinition);

    public abstract IRecordFeedbackDefinition getFeedbackDefinition();

    public abstract void setFeedbackDefinition(IRecordFeedbackDefinition irecordfeedbackdefinition);

    public abstract IRecordViewDefinition getViewDefinition();

    public abstract void setViewDefinition(IRecordViewDefinition irecordviewdefinition);

    public abstract ICacheable getPrevious();

    public abstract void setPrevious(ICacheable icacheable);

    public abstract ICacheable getNext();

    public abstract void setNext(ICacheable icacheable);

    public abstract void logHit();

    public abstract void loseHit();

    public abstract long getHitCount();

    public abstract void resetHitCount();
}
