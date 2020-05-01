// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.runtime.controller.*;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.runtime.fldformat.FieldDataFormatter;
import com.ibm.as400ad.webfacing.runtime.model.*;
import com.ibm.as400ad.webfacing.runtime.model.def.*;
import com.ibm.as400ad.webfacing.runtime.view.def.AIDKeyResponseIndicator;
import com.ibm.as400ad.webfacing.runtime.view.def.BLANKSResponseIndicator;
import com.ibm.as400ad.webfacing.runtime.view.def.FieldViewDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.HLPRTNResponseIndicator;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordFeedbackDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordViewDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.RTNCSRLOCDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.ResponseIndicator;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view:
//            AIDKeyDictionary, CursorReturnData

public class RecordFeedbackBean
    implements IElement, IAccessFieldData, Cloneable, ENUM_KeywordIdentifiers
{

    public RecordFeedbackBean(IRecordFeedbackDefinition irecordfeedbackdefinition, IRecordData irecorddata)
    {
        _dataBean = null;
        _trace = WFSession.getTraceLogger();
        _definition = null;
        _fieldValues = null;
        _MDTOn = new HashSet();
        _checkedAnyFieldRI = false;
        _BLANKSFields = new HashSet();
        _definition = irecordfeedbackdefinition;
        _dataBean = irecorddata;
        initializeOnScreenValues();
    }

    void assignNewRecordData(IRecordData irecorddata)
    {
        irecorddata.initOptionIndsFrom(getRecordData().getOptionIndEval());
        _dataBean = irecorddata;
    }

    protected void clearMDTFor(String s)
    {
        _MDTOn.remove(s);
        _BLANKSFields.remove(s);
    }

    protected void clearMDTs(IRecordViewDefinition irecordviewdefinition, boolean flag)
    {
        if(flag)
            setOffAllMDT();
        else
            setOffMDTsExceptProtected(irecordviewdefinition);
    }

    public Object clone()
    {
        RecordFeedbackBean recordfeedbackbean = null;
        try
        {
            recordfeedbackbean = (RecordFeedbackBean)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        recordfeedbackbean._dataBean = (IRecordData)getRecordData().clone();
        recordfeedbackbean._MDTOn = new HashSet(_MDTOn);
        recordfeedbackbean._fieldValues = new HashMap(_fieldValues);
        recordfeedbackbean._BLANKSFields = new HashSet(_BLANKSFields);
        return recordfeedbackbean;
    }

    public boolean evaluateIndicatorExpression(String s)
    {
        return getOptionIndEval().evaluateIndicatorExpression(s);
    }

    private Set getBLANKSFields()
    {
        return _BLANKSFields;
    }

    protected BLANKSResponseIndicator getBLANKSRespInd(String s)
    {
        return (BLANKSResponseIndicator)getRecordFeedbackDefinition().get(new Key(s), com.ibm.as400ad.webfacing.runtime.view.def.BLANKSResponseIndicator.class);
    }

    protected Iterator getBLANKSRespInds()
    {
        return getRecordFeedbackDefinition().iterator(com.ibm.as400ad.webfacing.runtime.view.def.BLANKSResponseIndicator.class);
    }

    protected ILibraryFile getDSPFObject()
    {
        return getRecordData().getDSPFObject();
    }

    protected ResponseIndicator getFieldCHANGERespInd(String s)
    {
        return (ResponseIndicator)getRecordFeedbackDefinition().get(new Key(s), com.ibm.as400ad.webfacing.runtime.view.def.FieldResponseIndicator.class);
    }

    protected FieldDataDefinition getFieldDataDefinition(String s)
    {
        return getRecordData().getFieldDataDefinition(s);
    }

    public String getFieldValue(String s)
    {
        if(_fieldValues.containsKey(s))
        {
            return (String)_fieldValues.get(s);
        } else
        {
            String s1 = "Record " + getRecordName() + " does not have a value for field " + s + ". Attempt to get its value failed.";
            _trace.err(1, s1);
            return null;
        }
    }

    protected FieldViewDefinition getFieldViewDefinition(String s, IRecordViewDefinition irecordviewdefinition)
    {
        return irecordviewdefinition.getFieldViewDefinition(s);
    }

    private HLPRTNResponseIndicator getHLPRTN_RespInd()
    {
        Iterator iterator = getRecordFeedbackDefinition().iterator(com.ibm.as400ad.webfacing.runtime.view.def.HLPRTNResponseIndicator.class);
        if(iterator.hasNext())
            return (HLPRTNResponseIndicator)iterator.next();
        else
            return null;
    }

    public IKey getKey()
    {
        return new Key(getRecordName());
    }

    protected Iterator getMDTIter()
    {
        return _MDTOn.iterator();
    }

    public IIndicatorEvaluation getOptionIndEval()
    {
        return getRecordData().getOptionIndEval();
    }

    protected Iterator getRecordCHANGERespInds()
    {
        return getRecordFeedbackDefinition().iterator(com.ibm.as400ad.webfacing.runtime.view.def.AnyFieldResponseIndicator.class);
    }

    public IRecordData getRecordData()
    {
        return _dataBean;
    }

    public IRecordDataDefinition getRecordDataDefinition()
    {
        return getRecordData().getRecordDataDefinition();
    }

    protected IRecordFeedbackDefinition getRecordFeedbackDefinition()
    {
        return _definition;
    }

    public String getRecordName()
    {
        return _definition.getName();
    }

    public IIndicatorData getResponseIndData()
    {
        return getRecordData().getResponseIndData();
    }

    public int getSLNO()
    {
        return getRecordData().getSLNO();
    }

    public String getSourceQualifiedRecordName()
    {
        return getRecordData().getSourceQualifiedRecordName();
    }

    public boolean hasChangedFields()
    {
        return _MDTOn.size() > 0;
    }

    public void initializeMDT(FieldViewDefinition fieldviewdefinition)
    {
        String s = fieldviewdefinition.getKeywordDefinition(276L).getIndicatorExpression();
        if(s.equals(""))
            setMDT(fieldviewdefinition.getFieldName());
        else
        if(evaluateIndicatorExpression(s))
            setMDT(fieldviewdefinition.getFieldName());
    }

    public void initializeMDTs(Iterator iterator)
    {
        while(iterator.hasNext()) 
        {
            FieldViewDefinition fieldviewdefinition = (FieldViewDefinition)iterator.next();
            KeywordDefinition keyworddefinition = fieldviewdefinition.getKeywordDefinition(276L);
            if(keyworddefinition != null)
                initializeMDT(fieldviewdefinition);
        }
    }

    protected void initializeOnScreenValues()
    {
        _fieldValues = getRecordData().getFieldValues();
    }

    private boolean isBLANKSSatisfied(String s)
    {
        return getBLANKSFields().contains(s);
    }

    private boolean isChanged(String s)
    {
        return _MDTOn.contains(s);
    }

    public boolean isDspfDbcsCapable()
    {
        return _dataBean.isDspfDbcsCapable();
    }

    public boolean isFieldProtectedByDSPATR(FieldViewDefinition fieldviewdefinition)
    {
        KeywordDefinition keyworddefinition = fieldviewdefinition.getKeywordDefinition(275L);
        boolean flag = false;
        if(keyworddefinition != null && evaluateIndicatorExpression(keyworddefinition.getIndicatorExpression()))
            flag = true;
        return flag;
    }

    boolean isKeywordActive(long l)
    {
        IRecordFeedbackDefinition irecordfeedbackdefinition = getRecordFeedbackDefinition();
        if(!irecordfeedbackdefinition.isKeywordSpecified(l))
            return false;
        else
            return isKeywordConditioned(l);
    }

    boolean isKeywordConditioned(long l)
    {
        KeywordDefinition keyworddefinition = getRecordFeedbackDefinition().getKeywordDefinition(l);
        String s = keyworddefinition.getIndicatorExpression();
        if(s.equals(""))
            return true;
        else
            return evaluateIndicatorExpression(s);
    }

    boolean isMDTOn(String s)
    {
        return _MDTOn.contains(s);
    }

    public void prepareForRead()
    {
        setCheckedAnyFieldRI(false);
        getRecordData().prepareForRead();
    }

    protected boolean setAIDKey(String s)
    {
        boolean flag = AIDKeyDictionary.isNonCommandAID(s);
        IIndicatorData iindicatordata = getRecordData().getResponseIndData();
        AIDKeyResponseIndicator aidkeyresponseindicator = (AIDKeyResponseIndicator)getRecordFeedbackDefinition().get(new Key(s), com.ibm.as400ad.webfacing.runtime.view.def.AIDKeyResponseIndicator.class);
        if(null != aidkeyresponseindicator)
        {
            iindicatordata.setIndicator(aidkeyresponseindicator.getIndex(), true);
            flag = false;
        }
        if(!s.equals("ENTER") && !s.equals("SLPAUTOENTER"))
        {
            Iterator iterator = getRecordFeedbackDefinition().iterator(com.ibm.as400ad.webfacing.runtime.view.def.AnyAIDKeyResponseIndicator.class);
            if(iterator.hasNext())
            {
                ResponseIndicator responseindicator = (ResponseIndicator)iterator.next();
                iindicatordata.setIndicator(responseindicator.getIndex(), true);
            }
        }
        if(s.equals("HELP"))
        {
            HLPRTNResponseIndicator hlprtnresponseindicator = getHLPRTN_RespInd();
            if(hlprtnresponseindicator != null && (hlprtnresponseindicator.getIndExpr() == null || evaluateIndicatorExpression(hlprtnresponseindicator.getIndExpr())))
                iindicatordata.setIndicator(hlprtnresponseindicator.getIndex(), true);
        }
        return flag;
    }

    public void setBLANKS_RespIndOnWrite(String s, String s1)
    {
        BLANKSResponseIndicator blanksresponseindicator = getBLANKSRespInd(s);
        if(blanksresponseindicator != null && (new PaddedStringBuffer(s1)).isEmptyOrBlank())
            getRecordData().getResponseIndData().setIndicator(blanksresponseindicator.getIndex(), true);
    }

    private void setBLANKS_RespIndsOnRead()
    {
        IIndicatorData iindicatordata = getRecordData().getResponseIndData();
        for(Iterator iterator = getBLANKSRespInds(); iterator.hasNext();)
        {
            BLANKSResponseIndicator blanksresponseindicator = (BLANKSResponseIndicator)iterator.next();
            String s = blanksresponseindicator.getFieldName();
            if(isChanged(s))
                iindicatordata.setIndicator(blanksresponseindicator.getIndex(), isBLANKSSatisfied(blanksresponseindicator.getFieldName()));
        }

    }

    private void setCHANGE_RespInds(String s)
    {
        IIndicatorData iindicatordata = getRecordData().getResponseIndData();
        ResponseIndicator responseindicator = getFieldCHANGERespInd(s);
        if(null != responseindicator)
            iindicatordata.setIndicator(responseindicator.getIndex(), true);
        if(!_checkedAnyFieldRI)
        {
            _checkedAnyFieldRI = true;
            Iterator iterator = getRecordCHANGERespInds();
            if(iterator.hasNext())
            {
                ResponseIndicator responseindicator1 = (ResponseIndicator)iterator.next();
                iindicatordata.setIndicator(responseindicator1.getIndex(), true);
            }
        }
    }

    protected void setCheckedAnyFieldRI(boolean flag)
    {
        _checkedAnyFieldRI = flag;
    }

    public void setCursor(CursorReturnData cursorreturndata)
    {
        IRecordFeedbackDefinition irecordfeedbackdefinition = getRecordFeedbackDefinition();
        if(irecordfeedbackdefinition.isRTNCSRLOCSpecified())
        {
            RTNCSRLOCDefinition rtncsrlocdefinition;
            for(Iterator iterator = irecordfeedbackdefinition.getRTNCSRLOCDefinitions(); iterator.hasNext(); rtncsrlocdefinition.setCursor(this, cursorreturndata))
                rtncsrlocdefinition = (RTNCSRLOCDefinition)iterator.next();

        }
    }

    public void setFieldValue(String s, String s1)
    {
        setOnScreenFieldValue(s, s1);
        getRecordData().setFieldValue(s, s1);
    }

    private void setMDT(String s)
    {
        _MDTOn.add(s);
    }

    public void setOffAllMDT()
    {
        _MDTOn.clear();
        getBLANKSFields().clear();
    }

    protected void setOffMDTsExceptProtected(IRecordViewDefinition irecordviewdefinition)
    {
        if(hasChangedFields())
        {
            Iterator iterator = getMDTIter();
            Vector vector = new Vector(_MDTOn.size());
            while(iterator.hasNext()) 
            {
                String s = (String)iterator.next();
                if(!isFieldProtectedByDSPATR(getFieldViewDefinition(s, irecordviewdefinition)))
                    vector.add(s);
            }
            String s1;
            for(Iterator iterator1 = vector.iterator(); iterator1.hasNext(); clearMDTFor(s1))
                s1 = (String)iterator1.next();

        }
    }

    public void setOnScreenFieldValue(IFieldValue ifieldvalue)
    {
        updateMDTandBLANKS(ifieldvalue.field(), ifieldvalue.isBLANKSSatisfied());
        setOnScreenFieldValue(ifieldvalue.field(), ifieldvalue.value());
    }

    void setOnScreenFieldValue(String s, String s1)
    {
        _fieldValues.put(s, s1);
    }

    public void updateFieldValue(String s, String s1)
    {
        getRecordData().updateFieldValue(s, s1);
    }

    public void storeOnScreenValues()
    {
        String s;
        for(Iterator iterator = _MDTOn.iterator(); iterator.hasNext(); setCHANGE_RespInds(s))
        {
            s = (String)iterator.next();
            getRecordData().updateFieldValue(s, getFieldValue(s));
        }

        setBLANKS_RespIndsOnRead();
    }

    public String toString()
    {
        return "Feedback Bean for: " + getRecordData().toString();
    }

    public void updateMDTandBLANKS(String s, boolean flag)
    {
        setMDT(s);
        if(flag)
        {
            if(!getBLANKSFields().contains(s))
                getBLANKSFields().add(s);
        } else
        if(getBLANKSFields().contains(s))
            getBLANKSFields().remove(s);
    }

    void applyDefaultValues(boolean flag)
    {
        for(Iterator iterator = getRecordDataDefinition().getOutputBufferFieldDefinitions().iterator(); iterator.hasNext();)
        {
            FieldDataDefinition fielddatadefinition = (FieldDataDefinition)iterator.next();
            String s = fielddatadefinition.getDefaultValue(getOptionIndEval(), getRecordData().getJobCCSID());
            if(s != null && (!flag || !fielddatadefinition.isOVRDTAActive(getOptionIndEval())))
            {
                String s1 = fielddatadefinition.getFieldName();
                _fieldValues.put(s1, s);
                getRecordData().setDefaultFieldValue(s1, s);
            }
        }

    }

    public long getVersionDigits()
    {
        return getRecordData().getVersionDigits();
    }

    public FieldDataFormatter getFieldDataFormatter(String s)
    {
        return getRecordData().getFieldDataFormatter(s);
    }

    public int getJobCCSID()
    {
        return getRecordData().getJobCCSID();
    }

    public void formatFieldValues(IRecordViewDefinition irecordviewdefinition)
    {
        String s;
        String s1;
        for(Iterator iterator = _fieldValues.entrySet().iterator(); iterator.hasNext(); _fieldValues.put(s, s1))
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            s = (String)entry.getKey();
            s1 = (String)entry.getValue();
            FieldDataFormatter fielddataformatter = getFieldDataFormatter(s);
            FieldViewDefinition fieldviewdefinition = getFieldViewDefinition(s, irecordviewdefinition);
            s1 = fielddataformatter.format(getJobCCSID(), fieldviewdefinition, s1);
        }

    }

    public static final String copyright = new String("(c) Copyright IBM Corporation 1999-2003, all rights reserved");
    private IRecordData _dataBean;
    protected ITraceLogger _trace;
    protected IRecordFeedbackDefinition _definition;
    private Map _fieldValues;
    private HashSet _MDTOn;
    private boolean _checkedAnyFieldRI;
    private HashSet _BLANKSFields;

}
