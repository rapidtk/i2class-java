// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) 

package com.ibm.as400ad.webfacing.runtime.view.def;

import com.ibm.as400ad.webfacing.runtime.core.IKey;
import com.ibm.as400ad.webfacing.runtime.core.Key;

// Referenced classes of package com.ibm.as400ad.webfacing.runtime.view.def:
//            ResponseIndicator

public class FieldResponseIndicator extends ResponseIndicator
{

    public FieldResponseIndicator(String s, int i)
    {
        super(i);
        _fieldName = s;
    }

    protected IKey createKey()
    {
        return new Key(_fieldName);
    }

    private String _fieldName;
}
