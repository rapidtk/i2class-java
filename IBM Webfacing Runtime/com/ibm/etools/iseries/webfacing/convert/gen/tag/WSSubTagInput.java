// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.etools.iseries.webfacing.convert.gen.tag;

import com.ibm.as400ad.code400.dom.constants.IFieldType;
import com.ibm.as400ad.code400.dom.constants.IRecordType;
import com.ibm.as400ad.webfacing.convert.model.FieldWebSettings;
import com.ibm.etools.iseries.webfacing.convert.external.*;

public class WSSubTagInput
    implements IWSSubTagInput
{

    public WSSubTagInput(IFieldTagInput ifieldtaginput, IRawWebSetting irawwebsetting)
    {
        _fTagInput = null;
        _websetting = null;
        _fTagInput = ifieldtaginput;
        _websetting = irawwebsetting;
    }

    public String getFieldName()
    {
        return _fTagInput.getFieldName();
    }

    public IFieldType getFieldType()
    {
        return _fTagInput.getFieldType();
    }

    public String getRecordName()
    {
        return _fTagInput.getRecordName();
    }

    public String getFieldValue()
    {
        return _fTagInput.getFieldValue();
    }

    public int getStartColumn()
    {
        return _fTagInput.getStartColumn();
    }

    public int getEndColumn()
    {
        return _fTagInput.getEndColumn();
    }

    public int getStartRow()
    {
        return _fTagInput.getStartRow();
    }

    public int getEndRow()
    {
        return _fTagInput.getEndRow();
    }

    public IRawWebSetting getWebSetting()
    {
        return _websetting;
    }

    public String replaceFieldValueSymbol(String s, boolean flag, String s1)
    {
        String s2 = FieldWebSettings.replaceFieldValueSymbol(s, flag, s1, null);
        return s2;
    }

    public IRecordType getRecordType()
    {
        return _fTagInput.getRecordType();
    }

    public String getFieldHTML()
    {
        String s = _fTagInput.getFieldHTML();
        return s;
    }

    static final String copyRight = new String("(c) Copyright IBM Corporation 2002-2003 All rights reserved.");
    protected IFieldTagInput _fTagInput;
    protected IRawWebSetting _websetting;

}
