// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.runtime.core.ElementContainer;
import com.ibm.as400ad.webfacing.runtime.core.Key;
import com.ibm.as400ad.webfacing.runtime.model.*;
import com.ibm.as400ad.webfacing.runtime.view.def.BLANKSResponseIndicator;
import com.ibm.as400ad.webfacing.runtime.view.def.FieldViewDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordViewDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.ResponseIndicator;
import com.ibm.as400ad.webfacing.runtime.view.def.SubfileControlRecordFeedbackDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.SubfileControlRecordViewDefinition;
import java.util.Iterator;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            RecordFeedbackBean

public class SubfileRecordFeedbackBean extends RecordFeedbackBean
    implements ENUM_KeywordIdentifiers
{

    public SubfileRecordFeedbackBean(SubfileControlRecordFeedbackDefinition subfilecontrolrecordfeedbackdefinition, IRecordData irecorddata)
    {
        super(subfilecontrolrecordfeedbackdefinition, irecorddata);
    }

    protected BLANKSResponseIndicator getBLANKSRespInd(String s)
    {
        SubfileControlRecordFeedbackDefinition subfilecontrolrecordfeedbackdefinition = getSFLCTLFeedbackDefinition();
        return (BLANKSResponseIndicator)subfilecontrolrecordfeedbackdefinition.getForSubfile(new Key(s), com.ibm.as400ad.webfacing.runtime.view.def.BLANKSResponseIndicator.class);
    }

    protected Iterator getBLANKSRespInds()
    {
        return getSFLCTLFeedbackDefinition().getSubfileContainer().iterator(com.ibm.as400ad.webfacing.runtime.view.def.BLANKSResponseIndicator.class);
    }

    protected ResponseIndicator getFieldCHANGERespInd(String s)
    {
        return (ResponseIndicator)getSFLCTLFeedbackDefinition().getForSubfile(new Key(s), com.ibm.as400ad.webfacing.runtime.view.def.FieldResponseIndicator.class);
    }

    protected FieldViewDefinition getFieldViewDefinition(String s, IRecordViewDefinition irecordviewdefinition)
    {
        return ((SubfileControlRecordViewDefinition)irecordviewdefinition).getSubfileFieldViewDefinition(s);
    }

    protected Iterator getRecordCHANGERespInds()
    {
        return getSFLCTLFeedbackDefinition().getSubfileContainer().iterator(com.ibm.as400ad.webfacing.runtime.view.def.AnyFieldResponseIndicator.class);
    }

    protected SubfileControlRecordFeedbackDefinition getSFLCTLFeedbackDefinition()
    {
        return (SubfileControlRecordFeedbackDefinition)getRecordFeedbackDefinition();
    }

    public boolean isChangedIndirectly()
    {
        return ((SubfileRecordDataBean)getRecordData()).isChangedIndirectly();
    }

    public void prepareForRead()
    {
        setCheckedAnyFieldRI(false);
    }

    public void setBLANKS_RespIndOnWrite(String s, String s1)
    {
        super.setBLANKS_RespIndOnWrite(s, s1);
        BLANKSResponseIndicator blanksresponseindicator = getBLANKSRespInd(s);
        if(blanksresponseindicator != null && getRecordData().getResponseIndData().getIndicator(blanksresponseindicator.getIndex()))
            ((SubfileRecordDataBean)getRecordData()).setChangedIndirectly(true);
    }

    public void setChangedIndirectly(boolean flag)
    {
        ((SubfileRecordDataBean)getRecordData()).setChangedIndirectly(flag);
    }
}
