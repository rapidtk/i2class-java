// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.model.def;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.runtime.model.IQueryFieldData;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.model.def:
//            ConditionedKeyword

public class MSGIDDefinition extends ConditionedKeyword
    implements ENUM_KeywordIdentifiers
{

    public MSGIDDefinition()
    {
        _isNONESet = false;
        _msgIdPrefix = null;
        _msgId = null;
        _msgIdFieldName = null;
        _libraryName = null;
        _libraryFieldName = null;
        _fileName = null;
        _fileFieldName = null;
    }

    public boolean isNONESet()
    {
        return _isNONESet;
    }

    public void noneIsSet()
    {
        _isNONESet = true;
    }

    public void setFileFieldName(String s)
    {
        _fileFieldName = s;
        _fileName = null;
    }

    public void setFileName(String s)
    {
        _fileName = s;
        _fileFieldName = null;
    }

    public void setLibraryFieldName(String s)
    {
        _libraryFieldName = s;
        _libraryName = null;
    }

    public void setLibraryName(String s)
    {
        _libraryName = s;
        _libraryFieldName = null;
    }

    public void setMsgId(String s)
    {
        _msgId = s;
        _msgIdFieldName = null;
    }

    public void setMsgIdFieldName(String s)
    {
        _msgIdFieldName = s;
        _msgId = null;
    }

    public void setMsgIdPrefix(String s)
    {
        _msgIdPrefix = s;
    }

    public String getMsgFile(IQueryFieldData iqueryfielddata)
    {
        if(_fileName != null)
            return _fileName;
        else
            return iqueryfielddata.getFieldValue(_fileFieldName).trim();
    }

    public String getMsgId(IQueryFieldData iqueryfielddata)
    {
        String s = null;
        if(_msgIdPrefix != null)
            s = _msgIdPrefix;
        else
            s = "";
        if(_msgId != null)
            return s + _msgId;
        else
            return s + iqueryfielddata.getFieldValue(_msgIdFieldName).trim();
    }

    public String getMsgLibrary(IQueryFieldData iqueryfielddata)
    {
        if(_libraryName != null)
            return _libraryName;
        if(_libraryFieldName != null)
            return iqueryfielddata.getFieldValue(_libraryFieldName).trim();
        else
            return "*LIBL";
    }

    public boolean equals(Object obj)
    {
        boolean flag = true;
        try
        {
            flag = equals((MSGIDDefinition)obj);
        }
        catch(Throwable throwable)
        {
            flag = false;
        }
        return flag;
    }

    public boolean equals(MSGIDDefinition msgiddefinition)
    {
        boolean flag = true;
        flag &= _isNONESet == msgiddefinition._isNONESet;
        flag &= eq(_msgIdPrefix, msgiddefinition._msgIdPrefix);
        flag &= eq(_msgId, msgiddefinition._msgId);
        flag &= eq(_msgIdFieldName, msgiddefinition._msgIdFieldName);
        flag &= eq(_libraryName, msgiddefinition._libraryName);
        flag &= eq(_libraryFieldName, msgiddefinition._libraryFieldName);
        flag &= eq(_fileName, msgiddefinition._fileName);
        flag &= eq(super._indicatorExpression, ((ConditionedKeyword) (msgiddefinition))._indicatorExpression);
        return flag;
    }

    private boolean eq(String s, String s1)
    {
        boolean flag = true;
        if(s != s1)
            if(s != null)
                flag = s.equals(s1);
            else
                flag = false;
        return flag;
    }

    private boolean _isNONESet;
    private String _msgIdPrefix;
    private String _msgId;
    private String _msgIdFieldName;
    private String _libraryName;
    private String _libraryFieldName;
    private String _fileName;
    private String _fileFieldName;
}
