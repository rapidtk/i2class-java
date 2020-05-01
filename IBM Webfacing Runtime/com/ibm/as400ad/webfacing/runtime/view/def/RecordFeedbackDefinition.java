// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.runtime.model.IRecordData;
import com.ibm.as400ad.webfacing.runtime.model.def.KeywordDefinition;
import com.ibm.as400ad.webfacing.runtime.view.AIDKeyDictionary;
import com.ibm.as400ad.webfacing.runtime.view.RecordFeedbackBean;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view.def:
//            AIDKeyResponseIndicator, IRecordFeedbackDefinition, ResponseIndicator

/**
 * @deprecated Class RecordFeedbackDefinition is deprecated
 */

public class RecordFeedbackDefinition extends ElementContainer
    implements IRecordFeedbackDefinition, ENUM_KeywordIdentifiers
{

    public RecordFeedbackDefinition(String s)
    {
        super(s);
    }

    public void add(KeywordDefinition keyworddefinition)
    {
        super.add(keyworddefinition);
    }

    public void add(ResponseIndicator responseindicator)
    {
        super.add(responseindicator);
    }

    public RecordFeedbackBean createFeedbackBean(IRecordData irecorddata)
        throws WebfacingInternalException
    {
        return new RecordFeedbackBean(this, irecorddata);
    }

    public Iterator getCommandKeyRespInds()
    {
        Iterator iterator = iterator(com.ibm.as400ad.webfacing.runtime.view.def.AIDKeyResponseIndicator.class);
        Vector vector = new Vector();
        while(iterator.hasNext()) 
        {
            AIDKeyResponseIndicator aidkeyresponseindicator = (AIDKeyResponseIndicator)iterator.next();
            if(AIDKeyDictionary.isCommandKey(aidkeyresponseindicator.getKey().getId()))
                vector.add(aidkeyresponseindicator);
        }
        return vector.iterator();
    }

    public KeywordDefinition getKeywordDefinition(long l)
    {
        return (KeywordDefinition)get((new Long(l)).toString(), com.ibm.as400ad.webfacing.runtime.model.def.KeywordDefinition.class);
    }

    public Iterator getKeywordDefinitions()
    {
        return iterator(com.ibm.as400ad.webfacing.runtime.model.def.KeywordDefinition.class);
    }

    public Iterator getNonCommandAIDKeyRespInds()
    {
        Iterator iterator = iterator(com.ibm.as400ad.webfacing.runtime.view.def.AIDKeyResponseIndicator.class);
        Vector vector = new Vector();
        while(iterator.hasNext()) 
        {
            AIDKeyResponseIndicator aidkeyresponseindicator = (AIDKeyResponseIndicator)iterator.next();
            if(AIDKeyDictionary.isNonCommandAID(aidkeyresponseindicator.getKey().getId()))
                vector.add(aidkeyresponseindicator);
        }
        return vector.iterator();
    }

    public Iterator getRTNCSRLOCDefinitions()
    {
        return iterator((new Long(174L)).toString());
    }

    public boolean isKeywordSpecified(long l)
    {
        return getKeywordDefinition(l) != null;
    }

    public boolean isRTNCSRLOCSpecified()
    {
        return getRTNCSRLOCDefinitions().hasNext();
    }

    public String toString()
    {
        String s = "Record Feedback Definition " + getName() + " " + this;
        return s;
    }
}
