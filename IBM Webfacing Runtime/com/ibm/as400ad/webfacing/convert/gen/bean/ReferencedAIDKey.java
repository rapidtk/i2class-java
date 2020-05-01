// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.convert.gen.bean;

import com.ibm.as400ad.code400.dom.*;
import com.ibm.as400ad.webfacing.convert.IReferencedAIDKey;

public class ReferencedAIDKey
    implements IReferencedAIDKey
{

    public ReferencedAIDKey()
    {
        _indicatorString = null;
    }

    public ReferencedAIDKey(KeywordNode keywordnode)
    {
        _indicatorString = null;
        _keywordID = keywordnode.getKeywordId();
        switch(_keywordID)
        {
        case 185: 
        case 190: 
            KeywordParm keywordparm = keywordnode.getFirstParm();
            if(keywordparm != null && keywordparm.getParmType() == 1)
                _name = keywordnode.getParmsAsString();
            break;

        default:
            _name = keywordnode.getName();
            _indicatorString = keywordnode.getIndicatorString();
            break;
        }
    }

    public String indicatorString()
    {
        return _indicatorString;
    }

    public String name()
    {
        return _name;
    }

    public int getKeywordID()
    {
        return _keywordID;
    }

    private String _name;
    private String _indicatorString;
    private int _keywordID;
}
