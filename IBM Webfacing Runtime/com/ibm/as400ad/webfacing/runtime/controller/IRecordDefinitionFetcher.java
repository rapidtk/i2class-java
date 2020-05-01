// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.controller;

import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingLevelCheckException;
import com.ibm.as400ad.webfacing.runtime.model.def.IRecordDataDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordFeedbackDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordViewDefinition;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.controller:
//            ICacheable

public interface IRecordDefinitionFetcher
{

    public abstract IRecordDataDefinition requestDataDefinition(String s)
        throws WebfacingLevelCheckException, WebfacingInternalException;

    public abstract IRecordViewDefinition requestViewDefinition(String s)
        throws WebfacingLevelCheckException, WebfacingInternalException;

    public abstract IRecordFeedbackDefinition requestFeedbackDefinition(String s)
        throws WebfacingLevelCheckException, WebfacingInternalException;

    public abstract String checkQualifiedPath(String s, String s1);

    public abstract void clearCache();

    public abstract ICacheable getCacheHead();

    public abstract void setCacheLimit(int i);

    public abstract int getCacheLimit();

    public static final String copyRight = new String("(c) Copyright IBM Corporation 1999-2003, all rights reserved.");
    public static final int CACHE_SIZE = 600;

}
