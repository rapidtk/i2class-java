// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model;

import com.ibm.as400ad.code400.dom.constants.FieldType;
import com.ibm.as400ad.webfacing.common.PaddedStringBuffer;
import com.ibm.as400ad.webfacing.common.WebfacingConstants;
import com.ibm.as400ad.webfacing.runtime.controller.*;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.runtime.fldformat.FieldDataFormatter;
import com.ibm.as400ad.webfacing.runtime.model.def.FieldDataDefinition;
import com.ibm.as400ad.webfacing.runtime.model.def.IRecordDataDefinition;
import com.ibm.as400ad.webfacing.runtime.model.def.KeywordDefinition;
import com.ibm.as400ad.webfacing.util.ITraceLogger;
import java.io.*;
import java.util.*;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model:
//            OptionIndicators, ResponseIndicators, IFieldData, BidiOnWSMFieldData, 
//            FieldData, IRecordData, IInputBufferSaveArea, IAccessFieldData, 
//            BaseIndicators, IIndicatorArea, IIndicatorEvaluation, IIndicatorData, 
//            IIndicatorValue

public class RecordDataBean
    implements IRecordData, IInputBufferSaveArea, IAccessFieldData, IElement, Cloneable
{

    public RecordDataBean(IRecordDataDefinition irecorddatadefinition, IReadOutputBuffer ireadoutputbuffer)
        throws IOException, WebfacingLevelCheckException
    {
        _definition = null;
        _trace = WFSession.getTraceLogger();
        _fieldValues = new HashMap();
        _hasBeenMapped = false;
        _msgidRequests = new ArrayList();
        _SLNO = 0;
        _definition = irecorddatadefinition;
        _optionIndicators = new OptionIndicators(getRecordDataDefinition().getIndicatorDefinition());
        _responseIndicators = new ResponseIndicators(getRecordDataDefinition().getIndicatorDefinition());
        initializeFromOutputBuffer(ireadoutputbuffer);
    }

    public Object clone()
    {
        RecordDataBean recorddatabean = null;
        try
        {
            recorddatabean = (RecordDataBean)super.clone();
        }
        catch(CloneNotSupportedException clonenotsupportedexception) { }
        recorddatabean._optionIndicators = (OptionIndicators)_optionIndicators.clone();
        recorddatabean._responseIndicators = (ResponseIndicators)_responseIndicators.clone();
        recorddatabean._fieldValues = new HashMap(_fieldValues);
        return recorddatabean;
    }

    public IIndicatorArea createSeparateIndicatorArea()
    {
        ResponseIndicators responseindicators = new ResponseIndicators(getRecordDataDefinition().getIndicatorDefinition());
        BaseIndicators.copyIndicators(getOptionIndEval(), responseindicators);
        return responseindicators;
    }

    public ILibraryFile getDSPFObject()
    {
        return _DSPFObject;
    }

    public FieldDataDefinition getFieldDataDefinition(String s)
    {
        return getRecordDataDefinition().getFieldDefinition(s);
    }

    public String getFieldValue(String s)
    {
        if(_fieldValues.containsKey(s))
        {
            return ((IFieldData)_fieldValues.get(s)).toString();
        } else
        {
            String s1 = "Record " + getRecordName() + " does not have a value for field " + s + ". Attempt to get its value failed.";
            _trace.err(1, s1);
            return null;
        }
    }

    public Map getFieldValues()
    {
        HashMap hashmap = new HashMap(2 * _fieldValues.size());
        java.util.Map.Entry entry;
        for(Iterator iterator = _fieldValues.entrySet().iterator(); iterator.hasNext(); hashmap.put(entry.getKey(), ((IFieldData)entry.getValue()).toString()))
            entry = (java.util.Map.Entry)iterator.next();

        return hashmap;
    }

    public IInputBufferSaveArea getInputBufferSaveArea()
    {
        return this;
    }

    public int getJobCCSID()
    {
        return _jobCCSID;
    }

    public IKey getKey()
    {
        return new Key(getRecordName());
    }

    public IIndicatorEvaluation getOptionIndEval()
    {
        return getOptionIndicators();
    }

    OptionIndicators getOptionIndicators()
    {
        return _optionIndicators;
    }

    public IRecordDataDefinition getRecordDataDefinition()
    {
        return _definition;
    }

    public String getRecordName()
    {
        String s = getRecordDataDefinition().getName();
        return s;
    }

    public IIndicatorData getResponseIndData()
    {
        return getResponseIndicators();
    }

    ResponseIndicators getResponseIndicators()
    {
        return _responseIndicators;
    }

    public int getSLNO()
    {
        return _SLNO;
    }

    public String getSourceQualifiedRecordName()
    {
        String s = getRecordDataDefinition().getRecordClassName();
        return WebfacingConstants.replaceSubstring(s, ".", "$");
    }

    public String getUntransformedRecordName()
    {
        return _untransformedRecordName;
    }

    private boolean hasBeenMapped()
    {
        return _hasBeenMapped;
    }

    public void initDSPFObject(IDSPFObject idspfobject)
    {
        _DSPFObject = idspfobject;
    }

    protected void initializeDefaultValues()
    {
        for(Iterator iterator = getRecordDataDefinition().getInputCapableFieldDefinitions().iterator(); iterator.hasNext();)
        {
            FieldDataDefinition fielddatadefinition = (FieldDataDefinition)iterator.next();
            if(!fielddatadefinition.isOutputCapable())
            {
                Object obj;
                if(hasEbcdicOnWSM(fielddatadefinition))
                    obj = new BidiOnWSMFieldData(fielddatadefinition.getDefaultValue(_jobCCSID), fielddatadefinition, _jobCCSID);
                else
                    obj = new FieldData(fielddatadefinition.getDefaultValue(_jobCCSID), fielddatadefinition, _jobCCSID);
                setFieldValue(fielddatadefinition.getFieldName(), ((IFieldData) (obj)));
            }
        }

        FieldDataDefinition fielddatadefinition1;
        PaddedStringBuffer paddedstringbuffer;
        for(Iterator iterator1 = getRecordDataDefinition().getMSGIDFieldDefinitions(); iterator1.hasNext(); setFieldValue(fielddatadefinition1.getFieldName(), paddedstringbuffer.toString()))
        {
            fielddatadefinition1 = (FieldDataDefinition)iterator1.next();
            paddedstringbuffer = new PaddedStringBuffer(fielddatadefinition1.getFieldLength());
            paddedstringbuffer.setValue("");
            paddedstringbuffer.padRight(' ', fielddatadefinition1.getFieldLength());
        }

    }

    void initializeFromIOBuffer(IReadOutputBuffer ireadoutputbuffer, Collection collection)
        throws IOException, WebfacingLevelCheckException
    {
        IRecordDataDefinition irecorddatadefinition = getRecordDataDefinition();
        if(!irecorddatadefinition.hasSeparateIndicatorArea())
        {
            ireadoutputbuffer.readIndicatorsFromIOBuffer(getOptionIndicators());
        } else
        {
            int i = ireadoutputbuffer.readShort();
            if(i != 0)
                throw new WebfacingLevelCheckException(_resmri.getString("WF0032"));
        }
        int j = ireadoutputbuffer.readFieldCount();
        if(j != collection.size())
        {
            PaddedStringBuffer paddedstringbuffer = new PaddedStringBuffer(_resmri.getString("WF0071"));
            paddedstringbuffer.replaceSubstring("&1", _definition.getName());
            paddedstringbuffer.replaceSubstring("&2", _DSPFObject.getHostName());
            paddedstringbuffer.replaceSubstring("&3", Integer.toString(irecorddatadefinition.getOutputBufferFieldCount()));
            paddedstringbuffer.replaceSubstring("&4", Integer.toString(j));
            paddedstringbuffer.append("\n" + getRecordDataDefinition().getOutputIOBufferDescription());
            _trace.err(2, paddedstringbuffer.toString());
            throw new WebfacingLevelCheckException(paddedstringbuffer.toString());
        }
        FieldDataDefinition fielddatadefinition;
        IFieldData ifielddata;
        for(Iterator iterator = collection.iterator(); iterator.hasNext(); _fieldValues.put(fielddatadefinition.getFieldName(), ifielddata))
        {
            fielddatadefinition = (FieldDataDefinition)iterator.next();
            ifielddata = ireadoutputbuffer.getFieldValue(fielddatadefinition, hasEbcdicOnWSM(fielddatadefinition));
        }

    }

    protected void initializeFromOutputBuffer(IReadOutputBuffer ireadoutputbuffer)
        throws IOException, WebfacingLevelCheckException
    {
        _DSPFObject = ireadoutputbuffer.getDSPFObject();
        _jobCCSID = Integer.parseInt(ireadoutputbuffer.getJobCCSID());
        _untransformedRecordName = ireadoutputbuffer.getRecordFormatName();
        _DFRWRTNo = ireadoutputbuffer.isDFRWRTNo();
        _FRCDTA = ireadoutputbuffer.isFRCDTA();
        if(ireadoutputbuffer.getIOBufferLength() > 0)
        {
            ireadoutputbuffer.positionStreamToIOBuffer();
            initializeFromIOBuffer(ireadoutputbuffer, _definition.getOutputBufferFieldDefinitions());
        }
        initializeDefaultValues();
        _SLNO = ireadoutputbuffer.getSLNO();
        if(getRecordDataDefinition().hasSeparateIndicatorArea())
        {
            ireadoutputbuffer.positionStreamToIndicatorArea();
            ireadoutputbuffer.readIndicatorArea(_optionIndicators);
        }
    }

    public void initOptionIndsFrom(IIndicatorValue iindicatorvalue)
    {
        BaseIndicators.copyIndicators(iindicatorvalue, getOptionIndicators());
    }

    public boolean isDspfDbcsCapable()
    {
        return _DSPFObject.isIGCDTA();
    }

    public boolean isKeywordActive(long l)
    {
        IRecordDataDefinition irecorddatadefinition = getRecordDataDefinition();
        if(!irecorddatadefinition.isKeywordSpecified(l))
            return false;
        String s = irecorddatadefinition.getKeywordDefinition(l).getIndicatorExpression();
        if(s.equals(""))
            return true;
        else
            return getOptionIndicators().evaluateIndicatorExpression(s);
    }

    public boolean isKeywordConditioned(long l)
    {
        KeywordDefinition keyworddefinition = getRecordDataDefinition().getKeywordDefinition(l);
        String s = keyworddefinition.getIndicatorExpression();
        if(s.equals(""))
            return true;
        else
            return getOptionIndicators().evaluateIndicatorExpression(s);
    }

    public boolean isKeywordSpecified(long l)
    {
        return getRecordDataDefinition().isKeywordSpecified(l);
    }

    public void prepareForRead()
    {
        if(hasBeenMapped())
            reinitializeMappedRecord();
    }

    protected void reinitializeMappedRecord()
    {
        _responseIndicators.clearReferencedResponseIndicators();
        setHasBeenMapped(false);
    }

    public void setFieldValue(String s, String s1)
    {
        setFieldValue(s, ((IFieldData) (new FieldData(s1, getFieldDataDefinition(s), _jobCCSID))));
    }

    public void setHasBeenMapped(boolean flag)
    {
        _hasBeenMapped = flag;
    }

    public String toString()
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("\n- - - - - - - - - - - - - - - - - - - - - - -");
        stringbuffer.append("\n");
        stringbuffer.append(_resmri.getString("RECORD_DATA_BEAN"));
        stringbuffer.append("\n");
        stringbuffer.append(_resmri.getString("Record_name__"));
        stringbuffer.append(getRecordName());
        Iterator iterator = getRecordDataDefinition().getFieldDefinitions().iterator();
        stringbuffer.append("\n");
        stringbuffer.append(_resmri.getString("Fields_"));
        FieldDataDefinition fielddatadefinition;
        for(; iterator.hasNext(); stringbuffer.append(_resmri.getString(",_Decimal_") + fielddatadefinition.getDecimalPrecision()))
        {
            fielddatadefinition = (FieldDataDefinition)iterator.next();
            String s = fielddatadefinition.getFieldName();
            stringbuffer.append("\n");
            stringbuffer.append(s);
            if(fielddatadefinition.isOutputCapable())
                stringbuffer.append("[O]");
            else
                stringbuffer.append("[I]");
            stringbuffer.append(_resmri.getString(",_Saved_Value_") + getFieldValue(s));
            stringbuffer.append(_resmri.getString(",_DDS_length_") + fielddatadefinition.getFieldLength());
            stringbuffer.append(_resmri.getString(",_Default_value_") + fielddatadefinition.getDefaultValue(_jobCCSID));
            stringbuffer.append(_resmri.getString(",_Numeric_") + fielddatadefinition.isNumeric());
        }

        stringbuffer.append("\n");
        stringbuffer.append(_optionIndicators.toString());
        stringbuffer.append("\n");
        stringbuffer.append(_responseIndicators.toString());
        stringbuffer.append("\n- - - - - - - - - - - - - - - - - - - - - - -");
        return stringbuffer.toString();
    }

    public void writeIOBuffer(OutputStream outputstream)
    {
        writeIOBuffer(outputstream, getRecordDataDefinition().getInputCapableFieldDefinitions());
    }

    protected void writeIOBuffer(OutputStream outputstream, Collection collection)
    {
        try
        {
            DataOutputStream dataoutputstream = new DataOutputStream(outputstream);
            if(!getRecordDataDefinition().hasSeparateIndicatorArea())
                dataoutputstream.writeChars(_responseIndicators.produceIndicatorsForIOBuffer());
            else
                dataoutputstream.writeShort(0);
            dataoutputstream.writeShort(collection.size());
            for(Iterator iterator = collection.iterator(); iterator.hasNext();)
            {
                FieldDataDefinition fielddatadefinition = (FieldDataDefinition)iterator.next();
                if(fielddatadefinition != null)
                {
                    String s = fielddatadefinition.getFieldName();
                    IFieldData ifielddata = (IFieldData)_fieldValues.get(s);
                    ifielddata.toInputBuffer(dataoutputstream);
                }
            }

        }
        catch(IOException ioexception) { }
    }

    public RecordDataBean(IRecordDataDefinition irecorddatadefinition)
        throws IOException, WebfacingLevelCheckException, WebfacingInternalException
    {
        _definition = null;
        _trace = WFSession.getTraceLogger();
        _fieldValues = new HashMap();
        _hasBeenMapped = false;
        _msgidRequests = new ArrayList();
        _SLNO = 0;
        _definition = irecorddatadefinition;
        _optionIndicators = new OptionIndicators(getRecordDataDefinition().getIndicatorDefinition());
        _responseIndicators = new ResponseIndicators(getRecordDataDefinition().getIndicatorDefinition());
        initializeDefaultValuesForHelp();
    }

    protected void initializeDefaultValuesForHelp()
    {
        for(Iterator iterator = getRecordDataDefinition().getFieldDefinitions().iterator(); iterator.hasNext();)
        {
            FieldDataDefinition fielddatadefinition = (FieldDataDefinition)iterator.next();
            if(fielddatadefinition.isOutputCapable() || fielddatadefinition.isInputCapable())
                setFieldValue(fielddatadefinition.getFieldName(), fielddatadefinition.getDefaultValue(_jobCCSID));
        }

    }

    public String getFileMemberType()
    {
        return _definition.getFileMemberType();
    }

    protected IDSPFObject getIDSPFObject()
    {
        return _DSPFObject;
    }

    protected boolean hasEbcdicOnWSM(FieldDataDefinition fielddatadefinition)
    {
        return fielddatadefinition.isInputOnly() && fielddatadefinition.hasDefaultValue() && getIDSPFObject().isInBidiMode();
    }

    public void setFieldValue(String s, IFieldData ifielddata)
    {
        _fieldValues.put(s, ifielddata);
    }

    public void setDefaultFieldValue(String s, String s1)
    {
        FieldDataDefinition fielddatadefinition = getFieldDataDefinition(s);
        Object obj;
        if(_DSPFObject.isInBidiMode())
            obj = new BidiOnWSMFieldData(s1, fielddatadefinition, _jobCCSID);
        else
            obj = new FieldData(s1, fielddatadefinition, _jobCCSID);
        setFieldValue(s, ((IFieldData) (obj)));
    }

    public void updateFieldValue(String s, String s1)
    {
        IFieldData ifielddata = (IFieldData)_fieldValues.get(s);
        if(getVersionDigits() >= 0x1ddcceb4L || getFieldDataDefinition(s).getDataType().isOfType(19))
            ifielddata.formatAndSetValue(s1);
        else
            ifielddata.transformAndSetValue(s1);
    }

    public long getVersionDigits()
    {
        return _definition.getVersionDigits();
    }

    public FieldDataFormatter getFieldDataFormatter(String s)
    {
        IFieldData ifielddata = (IFieldData)_fieldValues.get(s);
        return ifielddata.getFieldDataFormatter();
    }

    public boolean isDeferWrite(boolean flag)
    {
        if(getFileMemberType().equals("MNUDDS"))
            return true;
        return !_FRCDTA && (!_DFRWRTNo || flag);
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 1999, 2002.  All Rights Reserved.";
    private static ResourceBundle _resmri;
    private IRecordDataDefinition _definition;
    private IDSPFObject _DSPFObject;
    private ResponseIndicators _responseIndicators;
    private OptionIndicators _optionIndicators;
    protected ITraceLogger _trace;
    private Map _fieldValues;
    private boolean _hasBeenMapped;
    private List _msgidRequests;
    private int _SLNO;
    private String _untransformedRecordName;
    private int _jobCCSID;
    private boolean _DFRWRTNo;
    private boolean _FRCDTA;

    static 
    {
        _resmri = WebfacingConstants.RUNTIME_MRI_BUNDLE;
    }
}
