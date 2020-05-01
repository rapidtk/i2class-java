// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.runtime.model.IQueryFieldData;
import com.ibm.as400ad.webfacing.runtime.view.RecordViewBean;
import java.io.Serializable;

public class CHKMSGIDDefinition
    implements ENUM_KeywordIdentifiers, Serializable
{

    public CHKMSGIDDefinition(String s, String s1, String s2, String s3, String s4)
    {
        _msgId = null;
        _libraryName = null;
        _msgFile = null;
        _msgDataFieldName = null;
        _fieldName = null;
        _rvb = null;
        _msgId = s;
        _libraryName = s1;
        _msgFile = s2;
        _msgDataFieldName = s3;
        _fieldName = s4;
    }

    public String getFieldName()
    {
        return _fieldName;
    }

    public String getLibraryName()
    {
        if(_libraryName != null)
            return _libraryName;
        else
            return "*LIBL";
    }

    public String getMsgFile()
    {
        return _msgFile;
    }

    public String getMsgId()
    {
        return _msgId;
    }

    public void setCHKMSG(String s)
    {
        _rvb.setCHKMSG(_fieldName, s);
    }

    public void setRecordViewBean(RecordViewBean recordviewbean)
    {
        _rvb = recordviewbean;
    }

    public String getMsgData(IQueryFieldData iqueryfielddata)
    {
        if(_msgDataFieldName == null)
            return null;
        else
            return iqueryfielddata.getFieldValue(_msgDataFieldName);
    }

    public String getMsgDataFieldName()
    {
        return _msgDataFieldName;
    }

    public static final String Copyright = "(C) Copyright IBM Corp. 2001, 2002.  All Rights Reserved.";
    private String _msgId;
    private String _libraryName;
    private String _msgFile;
    private String _msgDataFieldName;
    private String _fieldName;
    private RecordViewBean _rvb;
}
