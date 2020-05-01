// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.webfacing.runtime.core.Element;
import com.ibm.as400ad.webfacing.runtime.core.IListable;

public class DSPATR_PCFieldInfo extends Element
    implements IListable
{

    public DSPATR_PCFieldInfo(String s, String s1)
    {
        super(s);
        _indExpr = null;
        _indExpr = s1;
    }

    public String getFieldName()
    {
        return getName();
    }

    public String getIndExpr()
    {
        return _indExpr;
    }

    private String _indExpr;
}
