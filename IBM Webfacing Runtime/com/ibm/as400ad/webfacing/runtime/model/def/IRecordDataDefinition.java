// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model.def;

import com.ibm.as400ad.webfacing.runtime.controller.*;
import com.ibm.as400ad.webfacing.runtime.core.*;
import com.ibm.as400ad.webfacing.runtime.model.RecordDataBean;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model.def:
//            FieldDataDefinition, IndicatorDataDefinition, KeywordDefinition

public interface IRecordDataDefinition
    extends ICacheable, IElementContainer
{

    public abstract void add(FieldDataDefinition fielddatadefinition);

    public abstract void add(IndicatorDataDefinition indicatordatadefinition);

    public abstract RecordDataBean createRecordDataBean(IReadOutputBuffer ireadoutputbuffer, RecordBeanFactory recordbeanfactory)
        throws WebfacingLevelCheckException, WebfacingInternalException, IOException;

    public abstract RecordDataBean createRecordDataBean()
        throws WebfacingLevelCheckException, WebfacingInternalException, IOException;

    public abstract FieldDataDefinition getFieldDefinition(String s);

    public abstract Collection getFieldDefinitions();

    public abstract String getFileMemberType();

    public abstract IndicatorDataDefinition getIndicatorDefinition();

    public abstract Collection getInputCapableFieldDefinitions();

    public abstract int getInputIOBufferLength();

    public abstract KeywordDefinition getKeywordDefinition(long l);

    public abstract Iterator getMSGIDFieldDefinitions();

    public abstract int getOutputBufferFieldCount();

    public abstract Collection getOutputBufferFieldDefinitions();

    public abstract String getOutputIOBufferDescription();

    public abstract int getOutputIOBufferLength();

    public abstract boolean hasSeparateIndicatorArea();

    public abstract boolean isKeywordSpecified(long l);

    public abstract void setFileMemberType(String s);

    public abstract void setInputIOBufferLength(int i);

    public abstract void setOutputIOBufferLength(int i);

    public abstract void setSeparateIndicatorArea(boolean flag);

    public abstract void setVersionDigits(long l);

    public abstract long getVersionDigits();

    public abstract void setRecordClassName(String s);

    public abstract String getRecordClassName();

    /**
     * @deprecated Method isFieldOfType is deprecated
     */

    public abstract boolean isFieldOfType(int i);

    /**
     * @deprecated Method isOfType is deprecated
     */

    public abstract boolean isOfType(int i);

    /**
     * @deprecated Method typeId is deprecated
     */

    public abstract int typeId();

    public abstract ICacheable getNext();

    public abstract ICacheable getPrevious();

    public abstract void setNext(ICacheable icacheable);

    public abstract void setPrevious(ICacheable icacheable);

    public abstract void logHit();

    public abstract void loseHit();

    public abstract long getHitCount();

    public abstract void resetHitCount();

    public static final String Copyright = "(C) Copyright IBM Corp. 2003.  All Rights Reserved.";
    public static final String INPUT_CAPABLE_FIELD = "INPUT_CAPABLE_FIELD";
    public static final String MSGID_FIELD = "MSGID_FIELD";
    public static final String OUTPUT_BUFFER_FIELD = "OUTPUT_BUFFER_FIELD";
}
