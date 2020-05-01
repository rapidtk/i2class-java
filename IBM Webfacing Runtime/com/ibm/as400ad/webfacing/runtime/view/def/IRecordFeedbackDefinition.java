// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.webfacing.runtime.core.IElementContainer;
import com.ibm.as400ad.webfacing.runtime.core.WebfacingInternalException;
import com.ibm.as400ad.webfacing.runtime.model.IRecordData;
import com.ibm.as400ad.webfacing.runtime.model.def.KeywordDefinition;
import com.ibm.as400ad.webfacing.runtime.view.RecordFeedbackBean;
import java.util.Iterator;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view.def:
//            ResponseIndicator

public interface IRecordFeedbackDefinition
    extends IElementContainer
{

    public abstract void add(KeywordDefinition keyworddefinition);

    public abstract void add(ResponseIndicator responseindicator);

    public abstract Iterator getCommandKeyRespInds();

    public abstract KeywordDefinition getKeywordDefinition(long l);

    public abstract Iterator getKeywordDefinitions();

    public abstract Iterator getNonCommandAIDKeyRespInds();

    public abstract Iterator getRTNCSRLOCDefinitions();

    public abstract boolean isKeywordSpecified(long l);

    public abstract boolean isRTNCSRLOCSpecified();

    public abstract RecordFeedbackBean createFeedbackBean(IRecordData irecorddata)
        throws WebfacingInternalException;

    public static final String copyright = new String("(c) Copyright IBM Corporation 2003, all rights reserved");

}
