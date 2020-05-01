// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model.def;

import com.ibm.as400ad.code400.dom.constants.*;
import com.ibm.as400ad.webfacing.runtime.controller.*;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.runtime.model.RecordDataBean;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordFeedbackDefinition;
import com.ibm.as400ad.webfacing.runtime.view.def.IRecordViewDefinition;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model.def:
//            FieldDataDefinition, IndicatorDataDefinition, KeywordDefinition, IRecordDataDefinition

/**
 * @deprecated Class RecordDataDefinition is deprecated
 */

public class RecordDataDefinition extends ElementContainer
    implements ENUM_KeywordIdentifiers, IRecordDataDefinition, IFieldType, ICacheable
{

    public RecordDataDefinition(String s)
    {
        super(s);
        _rfd = null;
        _rvd = null;
        _versionDigits = 0L;
        _inputIOBufferLength = 0;
        _outputIOBufferLength = 0;
        _fileMemberType = "DSPF";
        _hasSeparateIndicatorArea = false;
        _recordClassName = "unknown";
        _previous = null;
        _next = null;
        hitCount = 0L;
    }

    public void add(FieldDataDefinition fielddatadefinition)
    {
        super.add(fielddatadefinition);
        if(fielddatadefinition.isInputCapable())
            add(fielddatadefinition, "INPUT_CAPABLE_FIELD");
        if(fielddatadefinition.isInOutputBuffer())
            add(fielddatadefinition, "OUTPUT_BUFFER_FIELD");
        if(fielddatadefinition.isMSGID())
            add(fielddatadefinition, "MSGID_FIELD");
    }

    public void add(IndicatorDataDefinition indicatordatadefinition)
    {
        add(indicatordatadefinition, com.ibm.as400ad.webfacing.runtime.model.def.IndicatorDataDefinition.class);
    }

    public RecordDataBean createRecordDataBean(IReadOutputBuffer ireadoutputbuffer, RecordBeanFactory recordbeanfactory)
        throws WebfacingLevelCheckException, WebfacingInternalException, IOException
    {
        return new RecordDataBean(this, ireadoutputbuffer);
    }

    public RecordDataBean createRecordDataBean()
        throws WebfacingLevelCheckException, WebfacingInternalException, IOException
    {
        return new RecordDataBean(this);
    }

    public FieldDataDefinition getFieldDefinition(String s)
    {
        return (FieldDataDefinition)get(s, com.ibm.as400ad.webfacing.runtime.model.def.FieldDataDefinition.class);
    }

    public Collection getFieldDefinitions()
    {
        return getList(com.ibm.as400ad.webfacing.runtime.model.def.FieldDataDefinition.class);
    }

    public String getFileMemberType()
    {
        return _fileMemberType;
    }

    public IndicatorDataDefinition getIndicatorDefinition()
    {
        Iterator iterator = iterator(com.ibm.as400ad.webfacing.runtime.model.def.IndicatorDataDefinition.class);
        if(iterator.hasNext())
            return (IndicatorDataDefinition)iterator.next();
        else
            return null;
    }

    public Collection getInputCapableFieldDefinitions()
    {
        return getList("INPUT_CAPABLE_FIELD");
    }

    public int getInputIOBufferLength()
    {
        return _inputIOBufferLength;
    }

    public KeywordDefinition getKeywordDefinition(long l)
    {
        return (KeywordDefinition)get((new Long(l)).toString(), com.ibm.as400ad.webfacing.runtime.model.def.KeywordDefinition.class);
    }

    public Iterator getMSGIDFieldDefinitions()
    {
        return iterator("MSGID_FIELD");
    }

    public int getOutputBufferFieldCount()
    {
        return getCollection("OUTPUT_BUFFER_FIELD").size();
    }

    public Collection getOutputBufferFieldDefinitions()
    {
        return getList("OUTPUT_BUFFER_FIELD");
    }

    public String getOutputIOBufferDescription()
    {
        StringBuffer stringbuffer = new StringBuffer("The expected output buffer would contain the following characters in the prescribed order:\n");
        stringbuffer.append("Chars&nbsp;&nbsp;Bytes&nbsp;&nbsp;Application value\n");
        Iterator iterator = getOutputBufferFieldDefinitions().iterator();
        byte byte0 = 6;
        FieldDataDefinition fielddatadefinition;
        for(; iterator.hasNext(); stringbuffer.append("&nbsp;&nbsp;&nbsp;&nbsp;Type:").append(fielddatadefinition.getDataType().toString()).append("\n"))
        {
            fielddatadefinition = (FieldDataDefinition)iterator.next();
            int i = fielddatadefinition.getFieldLength();
            String s = Integer.toString(i);
            String s1 = Integer.toString(i * 2);
            stringbuffer.append(s);
            for(int j = 0; j < byte0 - s.length(); j++)
                stringbuffer.append("&nbsp;&nbsp;");

            stringbuffer.append(s1);
            for(int k = 0; k < byte0 - s1.length(); k++)
                stringbuffer.append("&nbsp;&nbsp;");

            stringbuffer.append("Field:");
            stringbuffer.append(fielddatadefinition.getFieldName()).append("&nbsp;&nbsp;&nbsp;&nbsp;Length:").append(i);
        }

        return stringbuffer.toString();
    }

    public int getOutputIOBufferLength()
    {
        return _outputIOBufferLength;
    }

    public boolean hasSeparateIndicatorArea()
    {
        return _hasSeparateIndicatorArea;
    }

    public boolean isKeywordSpecified(long l)
    {
        return getKeywordDefinition(l) != null;
    }

    public void setFileMemberType(String s)
    {
        _fileMemberType = s;
    }

    public void setInputIOBufferLength(int i)
    {
        _inputIOBufferLength = i;
    }

    public void setOutputIOBufferLength(int i)
    {
        _outputIOBufferLength = i;
    }

    public void setSeparateIndicatorArea(boolean flag)
    {
        _hasSeparateIndicatorArea = flag;
    }

    public void setVersionDigits(long l)
    {
        _versionDigits = l;
    }

    public long getVersionDigits()
    {
        return _versionDigits;
    }

    public void setRecordClassName(String s)
    {
        _recordClassName = s;
    }

    public String getRecordClassName()
    {
        return _recordClassName;
    }

    public IRecordDataDefinition getDataDefinition()
    {
        return this;
    }

    public void setDataDefinition(IRecordDataDefinition irecorddatadefinition)
    {
    }

    public IRecordFeedbackDefinition getFeedbackDefinition()
    {
        return _rfd;
    }

    public void setFeedbackDefinition(IRecordFeedbackDefinition irecordfeedbackdefinition)
    {
        _rfd = irecordfeedbackdefinition;
    }

    public IRecordViewDefinition getViewDefinition()
    {
        return _rvd;
    }

    public void setViewDefinition(IRecordViewDefinition irecordviewdefinition)
    {
        _rvd = irecordviewdefinition;
    }

    /**
     * @deprecated Method isFieldOfType is deprecated
     */

    public boolean isFieldOfType(int i)
    {
        return false;
    }

    /**
     * @deprecated Method isOfType is deprecated
     */

    public boolean isOfType(int i)
    {
        return false;
    }

    /**
     * @deprecated Method typeId is deprecated
     */

    public int typeId()
    {
        return -1;
    }

    public ICacheable getNext()
    {
        return _next;
    }

    public ICacheable getPrevious()
    {
        return _previous;
    }

    public void setNext(ICacheable icacheable)
    {
        _next = icacheable;
    }

    public void setPrevious(ICacheable icacheable)
    {
        _previous = icacheable;
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

    public String toString()
    {
        String s = "Record Data Definition " + getName() + " " + this;
        return s;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 1999-2003.  All Rights Reserved.";
    private IRecordFeedbackDefinition _rfd;
    private IRecordViewDefinition _rvd;
    private long _versionDigits;
    private int _inputIOBufferLength;
    private int _outputIOBufferLength;
    private String _fileMemberType;
    private boolean _hasSeparateIndicatorArea;
    private String _recordClassName;
    private ICacheable _previous;
    private ICacheable _next;
    private long hitCount;
    public static final String INPUT_CAPABLE_FIELD = "INPUT_CAPABLE_FIELD";
    public static final String MSGID_FIELD = "MSGID_FIELD";
    public static final String OUTPUT_BUFFER_FIELD = "OUTPUT_BUFFER_FIELD";
}
