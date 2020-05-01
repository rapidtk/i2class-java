// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.help;

import com.ibm.as400ad.code400.dom.constants.ENUM_KeywordIdentifiers;
import com.ibm.as400ad.webfacing.runtime.model.def.ConditionedKeyword;

public class HelpKeyword extends ConditionedKeyword
    implements ENUM_KeywordIdentifiers
{

    public HelpKeyword(long l)
    {
        this(l, "");
    }

    public HelpKeyword(long l, String s)
    {
        _ddsKeyword = l;
        setIndicatorExpression(s);
    }

    public long getKeywordIdentifier()
    {
        return _ddsKeyword;
    }

    public static final String copyRight = new String("(c) Copyright IBM Corporation 2001, 2002, all rights reserved");
    long _ddsKeyword;

}
