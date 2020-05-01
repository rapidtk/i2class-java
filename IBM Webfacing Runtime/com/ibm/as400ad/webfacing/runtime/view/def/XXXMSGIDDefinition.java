// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.runtime.model.IQueryFieldData;
import com.ibm.as400ad.webfacing.runtime.model.def.ConditionedKeyword;

public class XXXMSGIDDefinition extends ConditionedKeyword
    implements ENUM_KeywordIdentifiers
{

    public XXXMSGIDDefinition()
    {
        _msgId = null;
        _libraryName = null;
        _msgFile = null;
        _msgDataFieldName = null;
        _responseIndicator = null;
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

    public String getResponseIndicator()
    {
        return _responseIndicator;
    }

    public void setLibraryName(String s)
    {
        _libraryName = s;
    }

    public void setMsgDataFieldName(String s)
    {
        _msgDataFieldName = s;
    }

    public void setMsgFile(String s)
    {
        _msgFile = s;
    }

    public void setMsgId(String s)
    {
        _msgId = s;
    }

    public void setResponseIndicator(String s)
    {
        _responseIndicator = s;
    }

    public String getMsgData(IQueryFieldData iqueryfielddata)
    {
        if(_msgDataFieldName == null)
            return null;
        else
            return iqueryfielddata.getFieldValue(_msgDataFieldName);
    }

    public String getMsgdataFieldName()
    {
        return _msgDataFieldName;
    }

    private String _msgId;
    private String _libraryName;
    private String _msgFile;
    private String _msgDataFieldName;
    private String _responseIndicator;
}
